package com.hektor7.tictomate;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class TicTomate extends Application {

    Timer timer = new Timer();
    TimerTask timerTask;
    long workingSeconds = 0;
    long restingSeconds = 0;
    Label timerLabel = new Label();


    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();

        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();

        Scene scene = new Scene(root, visualBounds.getWidth(), visualBounds.getHeight());

        //Grid to organize items
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(1);
        grid.setVgap(6);
        root.setCenter(grid);

        //STARTS TASK'S NAME
        GridPane taskTextGrid = new GridPane();
        taskTextGrid.setAlignment(Pos.CENTER);
        taskTextGrid.setHgap(1);
        taskTextGrid.setVgap(2);
        grid.add(taskTextGrid, 0, 1);

        Label taskLabel = new Label("Task name: ");
        taskTextGrid.add(taskLabel, 0, 0);
        TextField taskName = new TextField();
        taskTextGrid.add(taskName, 1, 0);
        //ENDS TASK'S NAME

        //STARTS TASK'S TIME
        GridPane taskTimeGrid = new GridPane();
        taskTimeGrid.setVgap(2);
        taskTimeGrid.setHgap(1);
        taskTimeGrid.setAlignment(Pos.CENTER);
        grid.add(taskTimeGrid, 0, 2);

        Label sliderTaskTimeLabel = new Label("Task time: ");
        taskTimeGrid.add(sliderTaskTimeLabel, 0, 0);


        Slider sliderTaskTime = new Slider();
        sliderTaskTime.setMin(0);
        sliderTaskTime.setMax(90);
        sliderTaskTime.setValue(25);
        sliderTaskTime.setShowTickMarks(true);
        sliderTaskTime.setMajorTickUnit(5);
        sliderTaskTime.setMinorTickCount(5);
        sliderTaskTime.setBlockIncrement(1);
        sliderTaskTime.setValueChanging(true);

        Label sliderTaskTimeValue = new Label();
        sliderTaskTimeValue.setText(String.format("%.0f", sliderTaskTime.getValue()));
        taskTimeGrid.add(sliderTaskTimeValue, 2, 0);

        sliderTaskTime.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                sliderTaskTimeValue.setText(String.format("%.0f", newValue));
            }
        });
        taskTimeGrid.add(sliderTaskTime, 1, 0);
        //ENDS TASK'S TIME

        //STARTS REST'S TIME
        GridPane restTimeGrid = new GridPane();
        restTimeGrid.setVgap(3);
        restTimeGrid.setHgap(1);
        restTimeGrid.setAlignment(Pos.CENTER);
        grid.add(restTimeGrid, 0, 3);

        Label sliderRestTimeLabel = new Label("Rest time: ");
        restTimeGrid.add(sliderRestTimeLabel, 0, 0);

        Slider sliderRestTime = new Slider();
        sliderRestTime.setMin(0);
        sliderRestTime.setMax(25);
        sliderRestTime.setValue(5);
        sliderRestTime.setShowTickMarks(true);
        sliderRestTime.setMajorTickUnit(5);
        sliderRestTime.setMinorTickCount(5);
        sliderRestTime.setBlockIncrement(1);

        Label sliderRestTimeValue = new Label();
        sliderRestTimeValue.setText(String.format("%.0f",sliderRestTime.getValue()));
        restTimeGrid.add(sliderRestTimeValue, 2, 0);

        sliderRestTime.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                sliderRestTimeValue.setText(String.format("%.0f", newValue));
            }
        });

        restTimeGrid.add(sliderRestTime, 1, 0);
        //ENDS REST'S TIME

        //STARTS TIMER
        timerLabel.setText("0:00");
        timerLabel.setAlignment(Pos.CENTER);
        grid.add(timerLabel, 0, 4);
        //ENDS TIMER

        //STARTS BUTTONS
        GridPane buttonsGrid = new GridPane();
        buttonsGrid.setHgap(2);
        buttonsGrid.setVgap(1);
        buttonsGrid.setAlignment(Pos.CENTER);
        grid.add(buttonsGrid, 0, 5);

        Button startButton = new Button();
        startButton.setText("Start");
        startButton.setAlignment(Pos.CENTER_LEFT);
        startButton.setOnAction(event -> this.startTimer(Double.valueOf(sliderTaskTime.getValue()).intValue(),
                Double.valueOf(sliderRestTime.getValue()).intValue()));
        buttonsGrid.add(startButton, 0, 0);


        Button stopButton = new Button();
        stopButton.setText("Stop");
        stopButton.setAlignment(Pos.CENTER_RIGHT);
        buttonsGrid.add(stopButton,1,0);
        //ENDS BUTTONS

        stage.getIcons().add(new Image(TicTomate.class.getResourceAsStream("/icon.png")));
        stage.setScene(scene);
        stage.show();
    }

    private void startTimer(int workingMinutes, int restingMinutes) {
        this.workingSeconds = TimeUnit.MINUTES.toSeconds(workingMinutes);
        this.restingSeconds = TimeUnit.MINUTES.toSeconds(restingMinutes);
        //TODO: Disable UI controls

        final LongProperty lastUpdate = new SimpleLongProperty();

        final long minUpdateInterval = TimeUnit.SECONDS.toNanos(1); // nanoseconds. Set to higher number to slow output.

        AnimationTimer animationTimer = new AnimationTimer() {

            @Override
            public void handle(long now) {
                if (now - lastUpdate.get() > minUpdateInterval) {
                    updateCounter();
                    lastUpdate.set(now);
                }

            }
        };
        animationTimer.start();
    }

    private void updateCounter(){
        if (workingSeconds == 0){
            timer.cancel();
            //TODO: Enable UI controls
        }else {
            this.workingSeconds--;
            this.updateCurrentTimerLabel();
        }
    }

    private void updateCurrentTimerLabel(){
        long seconds = this.workingSeconds % 60;
        long totalMinutes = this.workingSeconds / 60;
        long minutes = totalMinutes % 60;
        StringBuffer sb = new StringBuffer();
        sb.append(minutes).append(":").append(seconds);
        this.timerLabel.setText(sb.toString());
    }

}
