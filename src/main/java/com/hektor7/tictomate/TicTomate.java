package com.hektor7.tictomate;

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

    MainScreen mainScreen;
    private AnimationTimer animationTimer;

    @Override
    public void start(Stage stage) {

        this.mainScreen = new MainScreen();
        this.mainScreen.buildUi(stage);

        this.setUiBehaviour();

        stage.show();
    }

    private void setUiBehaviour() {
        this.mainScreen.getStopButton().setOnAction(event -> {
            this.animationTimer.stop();
            this.mainScreen.enableComponentsToStop();
        });
        this.mainScreen.getStartButton().setOnAction(event -> {
            this.startTimer(Double.valueOf(this.mainScreen.getSliderTaskTime().getValue()).intValue(),
                    Double.valueOf(this.mainScreen.getSliderRestTime().getValue()).intValue());
            this.mainScreen.disableComponentsToStart();
        });
    }

    private void startTimer(int workingMinutes, int restingMinutes) {
        this.workingSeconds = TimeUnit.MINUTES.toSeconds(workingMinutes);
        this.restingSeconds = TimeUnit.MINUTES.toSeconds(restingMinutes);

        final LongProperty lastUpdate = new SimpleLongProperty();

        final long minUpdateInterval = TimeUnit.SECONDS.toNanos(1); // nanoseconds. Set to higher number to slow output.

        this.animationTimer = new AnimationTimer() {

            @Override
            public void handle(long now) {
                if (now - lastUpdate.get() > minUpdateInterval) {
                    updateCounter();
                    lastUpdate.set(now);
                }

            }
        };
        this.animationTimer.start();
    }

    private void updateCounter(){
        if (workingSeconds == 0){
            this.animationTimer.stop();
            this.mainScreen.enableComponentsToStop();
            this.mainScreen.playBell();
        }else {
            this.workingSeconds--;
            this.updateCurrentTimerLabel();
        }
    }

    private void updateCurrentTimerLabel(){
        long seconds = this.workingSeconds % 60;
        long totalMinutes = this.workingSeconds / 60;
        long minutes = totalMinutes % 60;
        StringBuilder sb = new StringBuilder();
        sb.append(minutes).append(":").append(seconds);
        this.mainScreen.getTimerLabel().setText(sb.toString());
    }
}
