package com.hektor7.tictomate;

import com.sun.xml.internal.ws.util.StringUtils;
import javafx.application.Application;
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
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class TicTomate extends Application {

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();

        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();

        Scene scene = new Scene(root, visualBounds.getWidth(), visualBounds.getHeight());

        //Grid to organize items
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(1);
        grid.setVgap(5);
        root.setCenter(grid);

        GridPane textGrid = new GridPane();
        textGrid.setAlignment(Pos.CENTER);
        textGrid.setHgap(1);
        textGrid.setVgap(2);
        grid.add(textGrid, 0, 1);

        Label taskLabel = new Label("Task name: ");
        textGrid.add(taskLabel, 0, 0);
        TextField taskName = new TextField();
        textGrid.add(taskName, 1, 0);

        GridPane taskTimeGrid = new GridPane();
        taskTimeGrid.setVgap(2);
        taskTimeGrid.setHgap(1);
        taskTimeGrid.setAlignment(Pos.CENTER);
        grid.add(taskTimeGrid, 0, 2);

        Label sliderTaskTimeLabel = new Label("Task time: ");
        taskTimeGrid.add(sliderTaskTimeLabel, 0, 0);

        Label sliderTaskTimeValue = new Label();
        taskTimeGrid.add(sliderTaskTimeValue, 2, 0);

        Slider sliderTaskTime = new Slider();
        sliderTaskTime.setMin(0);
        sliderTaskTime.setMax(90);
        sliderTaskTime.setValue(25);
        sliderTaskTime.setShowTickMarks(true);
        sliderTaskTime.setMajorTickUnit(5);
        sliderTaskTime.setMinorTickCount(5);
        sliderTaskTime.setBlockIncrement(1);
        sliderTaskTime.setValueChanging(true);

        sliderTaskTime.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                sliderTaskTimeValue.setText(String.format("%.2f", newValue));
            }
        });
        taskTimeGrid.add(sliderTaskTime, 1, 0);

        GridPane restTimeGrid = new GridPane();
        restTimeGrid.setVgap(3);
        restTimeGrid.setHgap(1);
        restTimeGrid.setAlignment(Pos.CENTER);
        grid.add(restTimeGrid, 0, 3);

        Label sliderRestTimeLabel = new Label("Rest time: ");
        restTimeGrid.add(sliderRestTimeLabel, 0, 0);

        Label sliderRestTimeValue = new Label();
        restTimeGrid.add(sliderRestTimeValue, 2, 0);

        Slider sliderRestTime = new Slider();
        sliderRestTime.setMin(0);
        sliderRestTime.setMax(25);
        sliderRestTime.setValue(5);
        sliderRestTime.setShowTickMarks(true);
        sliderRestTime.setMajorTickUnit(5);
        sliderRestTime.setMinorTickCount(5);
        sliderRestTime.setBlockIncrement(1);

        sliderRestTime.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                sliderRestTimeValue.setText(String.format("%.2f", newValue));
            }
        });

        restTimeGrid.add(sliderRestTime, 1, 0);


        GridPane buttonsGrid = new GridPane();
        buttonsGrid.setHgap(2);
        buttonsGrid.setVgap(1);
        buttonsGrid.setAlignment(Pos.CENTER);
        grid.add(buttonsGrid,0,4);

        Button startButton = new Button();
        startButton.setText("Start");
        startButton.setAlignment(Pos.CENTER_LEFT);
        buttonsGrid.add(startButton, 0, 0);

        Button stopButton = new Button();
        stopButton.setText("Stop");
        stopButton.setAlignment(Pos.CENTER_RIGHT);
        buttonsGrid.add(stopButton,1,0);

        stage.getIcons().add(new Image(TicTomate.class.getResourceAsStream("/icon.png")));
        stage.setScene(scene);
        stage.show();
    }

}
