package com.hektor7.tictomate;

import com.hektor7.tictomate.enums.TimerMode;
import com.hektor7.tictomate.ui.MainScreen;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.stage.Stage;

import java.util.concurrent.TimeUnit;

public class TicTomate extends Application {

    long workingSeconds = 0;
    long restingSeconds = 0;
    TimerMode mode = TimerMode.STAND_BY;

    MainScreen mainScreen;
    private AnimationTimer animationTimer;

    @Override
    public void start(Stage stage) {

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
        this.changeModeTo(TimerMode.WORKING);
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
            }else{
                this.changeModeTo(TimerMode.RESTING);
            }
        }else if (this.mode.equals(TimerMode.RESTING)){
            if (this.restingSeconds > 0){
                this.restingSeconds--;
            }
        }
        if (this.mode.equals(TimerMode.RESTING) && this.restingSeconds == 0){
            this.changeModeTo(TimerMode.FINISHED);
        }
        this.updateCurrentTimerIndicators();
    }

    private void changeModeTo(TimerMode mode) {
        if (mode.isExitFromThisModePlaysBell()){
            this.mainScreen.playBell();
        }

        this.mode = mode;
        this.mainScreen.getModeLabel().setText(this.mode.getName());
    }

    /**
     * It updates timer label in order to show the current countdown value.
     */
    private void updateCurrentTimerIndicators(){
        long seconds = this.getCurrentSeconds() % 60;
        long totalMinutes = this.getCurrentSeconds() / 60;
        long minutes = totalMinutes % 60;
        StringBuilder sb = new StringBuilder();
        sb.append(minutes).append(":").append(seconds);
        this.mainScreen.getTimerLabel().setText(sb.toString());
        this.mainScreen.getProgressIndicator().setProgress(this.getProgress());
    }

    private double getProgress() {
        double current = this.getCurrentSeconds();
        double total = this.getTotalSeconds();
        return (double) current/ total;
    }

    private long getTotalSeconds() {
        long seconds = 0;
        if (this.mode.equals(TimerMode.WORKING)){
            seconds = TimeUnit.MINUTES.toSeconds((long) this.mainScreen.getSliderTaskTime().getValue());
        }else if (this.mode.equals(TimerMode.RESTING)){
            seconds = TimeUnit.MINUTES.toSeconds((long) this.mainScreen.getSliderRestTime().getValue());
        }
        return seconds;
    }

    /**
     * It gets seconds for Working or Resting time bearing in mind the current mode.
     * @return current seconds
     */
    private long getCurrentSeconds() {
        long seconds = 0;
        if (this.mode.equals(TimerMode.WORKING)){
            seconds = this.workingSeconds;
        }else if (this.mode.equals(TimerMode.RESTING)){
            seconds = this.restingSeconds;
        }
        return seconds;
    }
}
