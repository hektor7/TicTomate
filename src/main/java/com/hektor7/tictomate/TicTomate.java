package com.hektor7.tictomate;

import com.hektor7.tictomate.enums.TimerMode;
import com.hektor7.tictomate.ui.MainScreen;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.stage.Stage;

import java.util.concurrent.TimeUnit;

//TODO: Include a progress indicator
//TODO: Use CSS to customize the UI
//TODO: Include number of "pomodoros"
//TODO: Include time indicator to big rest
public class TicTomate extends Application {

    long workingSeconds = 0;
    long restingSeconds = 0;
    TimerMode mode;

    MainScreen mainScreen;
    private AnimationTimer animationTimer;

    @Override
    public void start(Stage stage) {

        this.mode = TimerMode.STAND_BY;

        this.mainScreen = new MainScreen();
        this.mainScreen.buildUi(stage);

        this.setUiBehaviour();

        stage.show();
    }

    /**
     * It establishes the behaviour related with the main timer.
     */
    private void setUiBehaviour() {
        this.mainScreen.getStopButton().setOnAction(event -> {
            this.animationTimer.stop();
            this.mainScreen.enableComponentsToStop();
        });
        this.mainScreen.getStartButton().setOnAction(event -> {
            this.startTimer(
                    Double.valueOf(this.mainScreen.getSliderTaskTime().getValue()).intValue(),
                    Double.valueOf(this.mainScreen.getSliderRestTime().getValue()).intValue());
            this.mainScreen.disableComponentsToStart();
        });
    }

    /**
     * It initiates the countdown with the given working and resting minutes.
     *
     * @param workingMinutes working minutes
     * @param restingMinutes resting minutes
     */
    private void startTimer(int workingMinutes, int restingMinutes) {
        this.mode = TimerMode.WORKING;
        this.workingSeconds = TimeUnit.MINUTES.toSeconds(workingMinutes);
        this.restingSeconds = TimeUnit.MINUTES.toSeconds(restingMinutes);

        final LongProperty lastUpdate = new SimpleLongProperty();

        final long minUpdateInterval = TimeUnit.SECONDS.toNanos(1);

        this.animationTimer = new AnimationTimer() {

            @Override
            public void handle(long now) {
                if (now - lastUpdate.get() > minUpdateInterval) {
                    handleTimer();
                    lastUpdate.set(now);
                }

            }
        };
        this.animationTimer.start();
    }

    /**
     * It handles the timer operations for each iteration of the timer.
     * //FIXME: This method could be needless
     */
    private void handleTimer() { //FIXME: These operations should be moved to another place...
        if (this.mode.equals(TimerMode.FINISHED)){
            this.animationTimer.stop();
            this.mainScreen.enableComponentsToStop();
            this.mainScreen.playBell();
            //this.mainScreen.showFinishedDialog(); //TODO: Check for do it in another way

        }else{
            this.updateCounter();
        }
    }

    /**
     * It updates counters.
     */
    private void updateCounter(){

        if (this.mode.equals(TimerMode.WORKING)){
            if (this.workingSeconds > 0){
                this.workingSeconds--;
                this.updateCurrentTimerLabel();
            }else{
                this.mode = TimerMode.RESTING;
            }
        }else if (this.mode.equals(TimerMode.RESTING)){
            if (this.restingSeconds > 0){
                this.restingSeconds--;
                this.updateCurrentTimerLabel();
            }
        }

        if (this.mode.equals(TimerMode.RESTING) && this.restingSeconds == 0){
            this.mode = TimerMode.FINISHED;
        }
    }

    /**
     * It updates timer label in order to show the current countdown value.
     */
    private void updateCurrentTimerLabel(){
        long seconds = this.getSeconds() % 60;
        long totalMinutes = this.getSeconds() / 60;
        long minutes = totalMinutes % 60;
        StringBuilder sb = new StringBuilder();
        sb.append(minutes).append(":").append(seconds);
        this.mainScreen.getTimerLabel().setText(sb.toString());
    }

    /**
     * It gets seconds for Working or Resting time bearing in mind the current mode.
     * @return current seconds
     */
    private long getSeconds() {
        long seconds = 0;
        if (this.mode.equals(TimerMode.WORKING)){
            seconds = this.workingSeconds;
        }else if (this.mode.equals(TimerMode.RESTING)){
            seconds = this.restingSeconds;
        }
        return seconds;
    }
}
