package com.hektor7.tictomate.ui;

import com.hektor7.tictomate.TicTomate;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.media.AudioClip;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Class to manage the main screen of the UI.
 * Created by hector on 2/09/15.
 */
public class MainScreen {

    //--> UI Components
    private Scene scene;
    private GridPane grid;

    Label timerLabel = new Label();
    private Slider sliderTaskTime;
    private Slider sliderRestTime;
    private Button startButton;
    private Button stopButton;
    private Label sliderRestTimeLabel;
    private Label sliderRestTimeValue;
    private Label sliderTaskTimeLabel;
    private Label sliderTaskTimeValue;
    private TextField taskName;
    private Label taskLabel;
    private Label headerLabel;
    //UI Components <--

    public Button getStartButton() {
        return startButton;
    }

    public Button getStopButton() {
        return stopButton;
    }

    public TextField getTaskName() {
        return taskName;
    }

    public Slider getSliderRestTime() {
        return sliderRestTime;
    }

    public Slider getSliderTaskTime() {
        return sliderTaskTime;
    }

    public Label getTimerLabel() {
        return this.timerLabel;
    }

    /**
     * It initializes the User Interface.
     *
     * @param stage
     */
    public void buildUi(Stage stage) {

        BorderPane root = new BorderPane();

        this.setupStage(stage);

        this.createMainGrid(root);

        this.createHeader();
        this.createTaskTextField();

        this.createWorkingTimeSlider();
        this.createRestingTimeSlider();

        this.setupTimerLabel();
        this.createButtons();

        this.scene = new Scene(root, root.getWidth(), root.getHeight());

        stage.getIcons().add(new Image(TicTomate.class.getResourceAsStream("/icon.png")));
        stage.setScene(this.scene);

    }

    private void setupStage(Stage stage) {
        stage.setTitle("TicTomate v0.1");
        stage.setResizable(false);
    }

    private void createMainGrid(BorderPane root) {
        this.grid = new GridPane();
        this.grid.setAlignment(Pos.CENTER);
        this.grid.setHgap(1);
        this.grid.setVgap(6);
        root.setCenter(this.grid);
    }

    private void createButtons() {
        GridPane buttonsGrid = this.createButtonsGrid();

        this.startButton = new Button();
        this.startButton.setText("Start");
        this.startButton.setAlignment(Pos.CENTER_LEFT);
        buttonsGrid.add(this.startButton, 0, 0);

        this.stopButton = new Button();
        this.stopButton.setText("Stop");
        this.stopButton.setAlignment(Pos.CENTER_RIGHT);
        buttonsGrid.add(this.stopButton, 1, 0);
    }

    private GridPane createButtonsGrid() {
        GridPane buttonsGrid = new GridPane();
        buttonsGrid.setHgap(2);
        buttonsGrid.setVgap(1);
        buttonsGrid.setAlignment(Pos.CENTER);
        this.grid.add(buttonsGrid, 0, 5);
        return buttonsGrid;
    }

    private void setupTimerLabel() {
        this.timerLabel.setText("0:00");
        this.timerLabel.setAlignment(Pos.CENTER);
        this.grid.add(this.timerLabel, 0, 4);
    }

    private void createRestingTimeSlider() {
        GridPane restTimeGrid = new GridPane();
        restTimeGrid.setVgap(3);
        restTimeGrid.setHgap(1);
        restTimeGrid.setAlignment(Pos.CENTER);
        this.grid.add(restTimeGrid, 0, 3);

        this.sliderRestTime = new Slider();
        this.sliderRestTime.setMin(1);
        this.sliderRestTime.setMax(25);
        this.sliderRestTime.setValue(5);
        this.sliderRestTime.setShowTickMarks(true);
        this.sliderRestTime.setMajorTickUnit(5);
        this.sliderRestTime.setMinorTickCount(5);
        this.sliderRestTime.setBlockIncrement(1);

        this.sliderRestTimeLabel = new Label("Rest time: ");
        this.sliderRestTimeLabel.setLabelFor(this.sliderRestTime);
        restTimeGrid.add(this.sliderRestTimeLabel, 0, 0);

        this.sliderRestTimeValue = new Label();
        this.sliderRestTimeValue.setText(String.format("%.0f", this.sliderRestTime.getValue()));
        this.sliderRestTimeValue.setLabelFor(this.sliderRestTime);
        restTimeGrid.add(this.sliderRestTimeValue, 2, 0);

        this.sliderRestTime.valueProperty().addListener((observable, oldValue, newValue) -> {
            sliderRestTimeValue.setText(String.format("%.0f", newValue));
        });

        restTimeGrid.add(this.sliderRestTime, 1, 0);
    }

    private void createWorkingTimeSlider() {
        GridPane taskTimeGrid = new GridPane();
        taskTimeGrid.setVgap(2);
        taskTimeGrid.setHgap(1);
        taskTimeGrid.setAlignment(Pos.CENTER);
        this.grid.add(taskTimeGrid, 0, 2);

        this.sliderTaskTime = new Slider();
        this.sliderTaskTime.setMin(1);
        this.sliderTaskTime.setMax(90);
        this.sliderTaskTime.setValue(25);
        this.sliderTaskTime.setShowTickMarks(true);
        this.sliderTaskTime.setMajorTickUnit(5);
        this.sliderTaskTime.setMinorTickCount(5);
        this.sliderTaskTime.setBlockIncrement(1);
        this.sliderTaskTime.setValueChanging(true);

        this.sliderTaskTimeLabel = new Label("Task time: ");
        this.sliderTaskTimeLabel.setLabelFor(this.sliderTaskTime);
        taskTimeGrid.add(this.sliderTaskTimeLabel, 0, 0);

        this.sliderTaskTimeValue = new Label();
        this.sliderTaskTimeValue.setText(String.format("%.0f", this.sliderTaskTime.getValue()));
        this.sliderTaskTimeValue.setLabelFor(this.sliderTaskTime);
        taskTimeGrid.add(this.sliderTaskTimeValue, 2, 0);

        this.sliderTaskTime.valueProperty().addListener((observable, oldValue, newValue) -> {
            sliderTaskTimeValue.setText(String.format("%.0f", newValue));
        });
        taskTimeGrid.add(this.sliderTaskTime, 1, 0);
    }

    private void createTaskTextField() {
        GridPane taskTextGrid = new GridPane();
        taskTextGrid.setAlignment(Pos.CENTER);
        taskTextGrid.setHgap(1);
        taskTextGrid.setVgap(2);
        this.grid.add(taskTextGrid, 0, 1);

        this.taskName = new TextField();
        taskTextGrid.add(this.taskName, 1, 0);
        this.taskLabel = new Label("Task name: ");
        this.taskLabel.setLabelFor(this.taskName);
        taskTextGrid.add(this.taskLabel, 0, 0);
    }

    private void createHeader() {
        GridPane headerGrid = new GridPane();
        headerGrid.setAlignment(Pos.CENTER);
        headerGrid.setHgap(1);
        headerGrid.setVgap(1);
        this.headerLabel = new Label("TicTomate");
        this.headerLabel.setAlignment(Pos.CENTER);
        headerGrid.add(this.headerLabel, 0, 0);
        this.grid.add(headerGrid, 0, 0);
    }

    /**
     * It enables components when user presses Stop.
     */
    public void enableComponentsToStop() {
        this.getTaskName().setDisable(false);
        this.getSliderTaskTime().setDisable(false);
        this.getSliderRestTime().setDisable(false);
        this.getStartButton().setDisable(false);
    }

    /**
     * It disables components when user presses Start.
     */
    public void disableComponentsToStart() {
        this.getTaskName().setDisable(true);
        this.getSliderTaskTime().setDisable(true);
        this.getSliderRestTime().setDisable(true);
        this.getStartButton().setDisable(true);
    }

    /**
     * It plays a bell.
     */
    public void playBell() {
        URI uri = null;
        try {
            uri = this.getClass().getResource("/bell.wav").toURI();
        } catch (URISyntaxException e) {
            this.showError("Ooops, I can't play the bell's sound!"); //FIXME: Bad practice?
        }
        AudioClip audioClip = new AudioClip(uri.toString());
        audioClip.play();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Unexpected error");
        alert.setHeaderText("Error");
        alert.setContentText(message);

        alert.showAndWait();

    }

    public void showFinishedDialog() { //TODO: Check TicTomate.java usage.
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Time is up!");
        alert.setHeaderText("Information");
        StringBuilder sb = new StringBuilder();
        sb.append("This task should be finished: ")
                .append(this.getTaskName().getText());
        alert.setContentText(sb.toString());

        alert.showAndWait();
    }
}
