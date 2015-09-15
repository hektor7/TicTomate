package com.hektor7.tictomate.ui;

import com.hektor7.tictomate.TicTomate;
import com.hektor7.tictomate.enums.TimerMode;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.media.AudioClip;
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
    private GridPane mainGrid;

    Label timerLabel = new Label();
    private Slider sliderTaskTime;
    private Slider sliderRestTime;
    private Slider sliderBigRestTime;
    private Slider sliderPomodoros;
    private Button startButton;
    private Button stopButton;
    private Label sliderRestTimeLabel;
    private Label sliderRestTimeValue;
    private Label sliderPomodorosValue;
    private Label sliderPomodorosLabel;
    private Label sliderBigRestTimeLabel;
    private Label sliderBigRestTimeValue;

    private Label sliderTaskTimeLabel;
    private Label sliderTaskTimeValue;
    private TextField taskName;
    private Label taskLabel;
    private Label headerLabel;
    private ProgressIndicator progressIndicator;

    private Label modeLabel;
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

    public Label getModeLabel() {
        return modeLabel;
    }

    public ProgressIndicator getProgressIndicator() {
        return progressIndicator;
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

        this.createWorkingTimeBlock();
        this.createRestingTimeBlock();
        this.createBigRestingTimeBlock();
        this.createPomodorosBlock();


        this.createModeLabel();

        this.setupTimerLabel();

        this.createProgressIndicator();
        this.createButtons();

        this.scene = new Scene(root, root.getMaxWidth(), root.getMaxHeight());

        stage.getIcons().add(new Image(TicTomate.class.getResourceAsStream("/icon.png")));
        stage.setScene(this.scene);

    }

    private void setupStage(Stage stage) {
        stage.setTitle("TicTomate v0.1");
        stage.setResizable(false);
    }

    private void createMainGrid(BorderPane root) {
        this.mainGrid = new GridPane();
        this.mainGrid.setAlignment(Pos.CENTER);
        this.mainGrid.setHgap(1);
        this.mainGrid.setVgap(10);
        root.setCenter(this.mainGrid);
    }

    private void createHeader() {
        GridPane headerGrid = new GridPane();
        headerGrid.setAlignment(Pos.CENTER);
        headerGrid.setHgap(1);
        headerGrid.setVgap(1);
        this.headerLabel = new Label("TicTomate");
        this.headerLabel.setAlignment(Pos.CENTER);
        headerGrid.add(this.headerLabel, 0, 0);
        this.mainGrid.add(headerGrid, 0, 0);
    }

    private void createTaskTextField() {
        GridPane taskTextGrid = new GridPane();
        taskTextGrid.setAlignment(Pos.CENTER);
        taskTextGrid.setHgap(1);
        taskTextGrid.setVgap(2);
        this.mainGrid.add(taskTextGrid, 0, 1);

        this.taskName = new TextField();
        taskTextGrid.add(this.taskName, 1, 0);
        this.taskLabel = new Label("Task name: ");
        this.taskLabel.setLabelFor(this.taskName);
        taskTextGrid.add(this.taskLabel, 0, 0);
    }

    private void createWorkingTimeBlock() {

        GridPane grid = this.createGridForSlider();
        this.mainGrid.add(grid, 0, 2);

        this.sliderTaskTime = this.createSliderBySliderMaxAndCurrentVal(90, 25);

        grid.add(this.createLabelForSlider(this.sliderTaskTime, this.sliderTaskTimeLabel, "Task time: "), 0, 0);

        grid.add(this.createValueLabelForSlider(this.sliderTaskTime, this.sliderTaskTimeValue), 2, 0);

        grid.add(this.sliderTaskTime, 1, 0);
    }

    private void createRestingTimeBlock() {
        GridPane grid = this.createGridForSlider();
        this.mainGrid.add(grid, 0, 3);

        this.sliderRestTime = this.createSliderBySliderMaxAndCurrentVal(40, 5);

        grid.add(this.createLabelForSlider(this.sliderRestTime, this.sliderRestTimeLabel, "Rest time: "), 0, 0);

        grid.add(this.createValueLabelForSlider(this.sliderRestTime, this.sliderRestTimeValue), 2, 0);

        grid.add(this.sliderRestTime, 1, 0);
    }

    private void createBigRestingTimeBlock() {
        GridPane grid= this.createGridForSlider();
        this.mainGrid.add(grid, 0, 4);

        this.sliderBigRestTime = this.createSliderBySliderMaxAndCurrentVal(25, 15);

        grid.add(this.createLabelForSlider(this.sliderBigRestTime, this.sliderBigRestTimeLabel, "Big rest time: "), 0, 0);

        grid.add(this.createValueLabelForSlider(this.sliderBigRestTime, this.sliderBigRestTimeValue), 2, 0);

        grid.add(this.sliderBigRestTime, 1, 0);
    }

    private void createPomodorosBlock() {

        GridPane grid = this.createGridForSlider();
        this.mainGrid.add(grid, 0, 5);

        this.sliderPomodoros = this.createSliderBySliderMaxAndCurrentVal(20, 1);

        grid.add(this.createLabelForSlider(this.sliderPomodoros, this.sliderPomodorosLabel, "Big rest time: "), 0, 0);

        grid.add(this.createValueLabelForSlider(this.sliderPomodoros, this.sliderPomodorosValue), 2, 0);

        grid.add(this.sliderPomodoros, 1, 0);
    }

    private Label createValueLabelForSlider(Slider slider, Label label) {
        label = new Label();
        label.setText(String.format("%.0f", slider.getValue()));
        label.setLabelFor(slider);

        final Label finalLabel = label;
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            finalLabel.setText(String.format("%.0f", newValue));
        });

        return finalLabel;

    }

    private Label createLabelForSlider(Slider slider, Label label, String text) {
        label = new Label("Task time: ");
        label.setLabelFor(slider);
        return label;
    }

    private Slider createSliderBySliderMaxAndCurrentVal(int max, int current) {
        Slider slider = new Slider();
        slider.setMin(1);
        slider.setMax(max);
        slider.setValue(current);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(5);
        slider.setMinorTickCount(5);
        slider.setBlockIncrement(1);
        slider.setValueChanging(true);
        return slider;
    }

    private GridPane createGridForSlider() {
        GridPane taskTimeGrid;
        taskTimeGrid = new GridPane();
        taskTimeGrid.setVgap(2);
        taskTimeGrid.setHgap(1);
        taskTimeGrid.setAlignment(Pos.CENTER);
        return taskTimeGrid;
    }

    private void createModeLabel() {
        GridPane modeLabelGrid = new GridPane();
        modeLabelGrid.setAlignment(Pos.CENTER);
        modeLabelGrid.setHgap(1);
        modeLabelGrid.setVgap(1);
        this.modeLabel = new Label(TimerMode.STAND_BY.getName());
        this.modeLabel.setAlignment(Pos.CENTER);
        modeLabelGrid.add(this.modeLabel, 0, 0);
        this.mainGrid.add(modeLabelGrid, 0, 6);
    }

    private void setupTimerLabel() {

        GridPane timerLabelGrid = new GridPane();
        timerLabelGrid.setAlignment(Pos.CENTER);
        timerLabelGrid.setHgap(1);
        timerLabelGrid.setVgap(1);
        this.timerLabel = new Label("0:00");
        this.timerLabel.setAlignment(Pos.CENTER);
        timerLabelGrid.add(this.timerLabel, 0, 0);
        this.mainGrid.add(timerLabelGrid, 0, 7);
    }

    private void createProgressIndicator() {
        this.progressIndicator = new ProgressIndicator(0.0);
        this.mainGrid.add(this.progressIndicator, 0, 8);
    }

    private void createButtons() {
        GridPane buttonsGrid = this.createButtonsGrid();

        this.startButton = new Button();
        this.startButton.setText("Start");
        this.startButton.setAlignment(Pos.CENTER_LEFT);
        this.startButton.setDefaultButton(true);
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
        this.mainGrid.add(buttonsGrid, 0, 9);
        return buttonsGrid;
    }

    /**
     * It enables components when user presses Stop.
     */
    public void enableComponentsToStop() {
        this.getTaskName().setDisable(false);
        this.getSliderTaskTime().setDisable(false);
        this.getSliderRestTime().setDisable(false);
        this.getSliderBigRestTime().setDisable(false);
        this.getSliderPomodoros().setDisable(false);
        this.getStartButton().setDisable(false);
        this.getStopButton().setDefaultButton(false);
        this.getStartButton().setDefaultButton(true);

    }

    /**
     * It disables components when user presses Start.
     */
    public void disableComponentsToStart() {
        this.getTaskName().setDisable(true);
        this.getSliderTaskTime().setDisable(true);
        this.getSliderRestTime().setDisable(true);
        this.getSliderBigRestTime().setDisable(true);
        this.getSliderPomodoros().setDisable(true);
        this.getStartButton().setDisable(true);
        this.getStopButton().setDefaultButton(true);
        this.getStartButton().setDefaultButton(false);
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

    public Slider getSliderBigRestTime() {
        return sliderBigRestTime;
    }

    public Slider getSliderPomodoros() {
        return sliderPomodoros;
    }
}
