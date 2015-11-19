package com.hektor7.tictomate.controllers;

import com.hektor7.tictomate.PlatformFactory;
import com.hektor7.tictomate.enums.TimerMode;
import com.hektor7.tictomate.models.State;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by hector on 27/09/15.
 */

//TODO: Create a SoundService or AlertService and create an implementation for desktop and android
public class MainController {

    private final Integer MIN_POMODOROS = 1;
    private final Integer MAX_POMODOROS = 20;
    private final Integer DEFAULT_POMODOROS = 1;
    private final Integer MAX_WORKING_TIME = 120;
    private final Integer MIN_WORKING_TIME = 1;
    private final Integer DEFAULT_WORKING_TIME = 25;
    private final Integer MAX_RESTING_TIME = 60;
    private final Integer MIN_RESTING_TIME = 1;
    private final Integer DEFAULT_RESTING_TIME = 5;
    private final Integer MAX_BIG_RESTING_TIME = 120;
    private final Integer MIN_BIG_RESTING_TIME = 1;
    private final Integer DEFAULT_BIG_RESTING_TIME = 15;
    private final String LABEL_FOR_POMODOROS = "No. Pomodoros: {0}";
    private final String LABEL_FOR_WORKING = "Working time: {0}";
    private final String LABEL_FOR_RESTING = "Resting time: {0}";
    private final String LABEL_FOR_BIG_REST = "Big rest time: {0}";
    private final String LABEL_FOR_COUNTDOWN = "{0}:{1} left. Pomodoro No. {2} of {3}";

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Pane mainPane;

    @FXML
    private VBox mainVBox;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private Label labelState;

    @FXML
    private Label labelCountDown;

    @FXML
    private Spinner<Integer> spinnerPomodoros;

    @FXML
    private Slider sliderPomodoros;

    @FXML
    private Spinner<Integer> spinnerWorkingTime;

    @FXML
    private Slider sliderWorkingTime;

    @FXML
    private Spinner<Integer> spinnerRestingTime;

    @FXML
    private Slider sliderRestingTime;

    @FXML
    private Spinner<Integer> spinnerBigRestTime;

    @FXML
    private Slider sliderBigRestingTime;

    @FXML
    private Button btnStart;

    @FXML
    private Button btnStop;

    @FXML
    private Button btnReset;

    @FXML
    private Label labelPomodoros;

    @FXML
    private Label labelWorkingTime;

    @FXML
    private Label labelRestingTime;

    @FXML
    private Label labelBigRestingTime;

    Timer timer;

    long timerSeconds;

    @FXML
    void doStart(ActionEvent event) {

        List<State> states = this.createListOfStates();

        this.startTimerFor(states);
    }

    /**
     * It starts the timer for a given sequence of states.
     *
     * @param states sequence of states
     */
    private void startTimerFor(List<State> states) {
        List<State> stateList = new LinkedList<>(states);
        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    if (stateList.isEmpty()) {
                        timer.cancel();
                    } else {

                        if (timerSeconds <= 0) {
                            timerSeconds = stateList.get(0).getTotalSeconds();
                        } else {
                            timerSeconds--;
                            establishCurrentMode(stateList.get(0).getMode());
                            establishTimerProperties(stateList.get(0).getMode());
                            refreshCountdownLabel(stateList.get(0));
                            if (timerSeconds <= 0) {
                                warnIfNecessaryFor(stateList.get(0).getMode());
                                stateList.remove(0);
                            }
                        }
                    }

                });
            }
        }, 1000, 1000); //Every 1 second
    }

    private void refreshCountdownLabel(State state) {
        final long minutes = TimeUnit.SECONDS.toMinutes(this.timerSeconds);
        final long seconds = (this.timerSeconds) % 60;

        this.labelCountDown.setText(MessageFormat.format(LABEL_FOR_COUNTDOWN,
                String.format("%02d", minutes), String.format("%02d", seconds),
                state.getNumberOfPomodoro(), this.getNumberOfPomodoros()));
    }

    /**
     * It returns a number of pomodoros bearing in mind
     * if we're in desktop mode or mobile mode.
     *
     * @return number of pomodoros.
     */
    private Integer getNumberOfPomodoros() {
        return PlatformFactory.isDesktop() ? this.spinnerPomodoros.getValue()
                : Double.valueOf(this.sliderPomodoros.getValue()).intValue();
    }

    /**
     * It creates a list of states by which we're going to
     * iterate in order to create a sequence of states:
     * WORKING - REST - WORKING - REST and so on.
     *
     * @return list of states
     */
    private List<State> createListOfStates() {
        List<State> states = new LinkedList<>();

        /*IntStream.rangeClosed(1, this.getNumberOfPomodoros().intValue()).sequential().forEach(i -> {
            Stream.of(TimerMode.WORKING, TimerMode.RESTING).sequential().forEach(mode -> {
                states.add(new State(this.obtainRealState(mode, i), this.getTotalSecondsFor(mode), states.size() + 1, i));
            });
        });*///Streams isn't available for Android because it uses SDK7.

        for (int i=1;i<=this.getNumberOfPomodoros();i++){
            for (TimerMode mode:Arrays.asList(TimerMode.WORKING, TimerMode.RESTING)){
                states.add(new State(this.obtainRealState(mode, i),
                        this.getTotalSecondsFor(mode), states.size() + 1, i));
            }
        }

        states.add(new State(TimerMode.FINISHED, 1L, states.size() + 1, 0));

        return states;

    }

    /**
     * It returns the real state when current state is RESTING
     * it checks and each four iterations state it swaps RESTING with BIG RESTING.
     * @param state current so-called state
     * @param i number of iteration (pomodoros)
     * @return real state
     */
    private TimerMode obtainRealState(TimerMode state, int i) {
        return i % 4 == 0 && state.equals(TimerMode.RESTING) ?
                TimerMode.BIG_RESTING : state;
    }

    @FXML
    void doStop(ActionEvent event) {
        this.timer.cancel();
        this.establishCurrentMode(TimerMode.PAUSED);
    }

    @FXML
    void initialize() {
        assert mainPane != null : "fx:id=\"mainPane\" was not injected: check your FXML file 'Main.fxml'.";
        assert mainVBox != null : "fx:id=\"mainVBox\" was not injected: check your FXML file 'Main.fxml'.";
        assert progressIndicator != null : "fx:id=\"progressIndicator\" was not injected: check your FXML file 'Main.fxml'.";
        assert labelState != null : "fx:id=\"labelState\" was not injected: check your FXML file 'Main.fxml'.";
        assert labelCountDown != null : "fx:id=\"labelCountDown\" was not injected: check your FXML file 'Main.fxml'.";
        assert spinnerPomodoros != null : "fx:id=\"spinnerPomodoros\" was not injected: check your FXML file 'Main.fxml'.";
        assert spinnerWorkingTime != null : "fx:id=\"spinnerWorkingTime\" was not injected: check your FXML file 'Main.fxml'.";
        assert spinnerRestingTime != null : "fx:id=\"spinnerRestingTime\" was not injected: check your FXML file 'Main.fxml'.";
        assert spinnerBigRestTime != null : "fx:id=\"spinnerBigRestTime\" was not injected: check your FXML file 'Main.fxml'.";
        assert btnStart != null : "fx:id=\"btnStart\" was not injected: check your FXML file 'Main.fxml'.";
        assert btnStop != null : "fx:id=\"btnStop\" was not injected: check your FXML file 'Main.fxml'.";

        this.initializeControls();

    }

    private void initializeControls() {
        if (PlatformFactory.isDesktop()){
            this.initializeControlsForDesktop();
        }else{
            this.initializeControlsForMobile();
        }

        this.configureButtonsFor(TimerMode.STAND_BY);
    }

    private void initializeControlsForMobile() {
        this.hideDesktopControls();
        this.configureMobileControls();
    }

    private void configureMobileControls() {
        this.mainPane.setPrefHeight(this.getScreenHeight());
        this.mainPane.setPrefWidth(this.getScreenWidth());
        this.mainVBox.setPrefHeight(this.getScreenHeight());
        this.mainVBox.setPrefWidth(this.getScreenWidth());
        this.progressIndicator.setMaxWidth(this.getScreenWidth());

        this.configureSlider(this.sliderPomodoros, MIN_POMODOROS,
                MAX_POMODOROS, DEFAULT_POMODOROS, LABEL_FOR_POMODOROS,
                labelPomodoros);
        this.configureSlider(this.sliderWorkingTime, MIN_WORKING_TIME,
                MAX_WORKING_TIME, DEFAULT_WORKING_TIME, LABEL_FOR_WORKING,
                labelWorkingTime);
        this.configureSlider(this.sliderRestingTime, MIN_RESTING_TIME,
                MAX_RESTING_TIME, DEFAULT_RESTING_TIME, LABEL_FOR_RESTING,
                labelRestingTime);
        this.configureSlider(this.sliderBigRestingTime, MIN_BIG_RESTING_TIME,
                MAX_BIG_RESTING_TIME, DEFAULT_BIG_RESTING_TIME, LABEL_FOR_BIG_REST,
                labelBigRestingTime);
    }

    //FIXME: Too many arguments
    private void configureSlider(Slider slider, Integer min, Integer max, Integer defaultValue, String labelText, Label label) {
        slider.setMin(min);
        slider.setMax(max);
        slider.setValue(defaultValue);
        slider.setBlockIncrement(1);
        slider.setMinorTickCount(1);
        slider.setMajorTickUnit(1);
        slider.setSnapToTicks(true);

        label.setText(MessageFormat.format(labelText, Double.valueOf(slider.getValue()).intValue()));

        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            label.setText(MessageFormat.format(labelText, newValue.intValue()));
        });
    }

    private double getScreenWidth() {
        return Screen.getPrimary().getVisualBounds().getWidth();
    }

    private double getScreenHeight() {
        return Screen.getPrimary().getVisualBounds().getHeight();
    }

    private void hideDesktopControls() {
        this.spinnerPomodoros.setVisible(false);
        this.spinnerWorkingTime.setVisible(false);
        this.spinnerRestingTime.setVisible(false);
        this.spinnerBigRestTime.setVisible(false);
    }

    private void initializeControlsForDesktop() {

        this.sliderPomodoros.setVisible(false);
        this.sliderWorkingTime.setVisible(false);
        this.sliderRestingTime.setVisible(false);
        this.sliderBigRestingTime.setVisible(false);

        this.setEnableSpinners(true);

        this.configureSpinner(this.spinnerPomodoros, MIN_POMODOROS,
                MAX_POMODOROS, DEFAULT_POMODOROS, 1);
        this.configureSpinner(this.spinnerWorkingTime, MIN_WORKING_TIME,
                MAX_WORKING_TIME, DEFAULT_WORKING_TIME, 1);
        this.configureSpinner(this.spinnerRestingTime, MIN_RESTING_TIME,
                MAX_RESTING_TIME, DEFAULT_RESTING_TIME, 1);
        this.configureSpinner(this.spinnerBigRestTime, MIN_BIG_RESTING_TIME,
                MAX_BIG_RESTING_TIME, DEFAULT_BIG_RESTING_TIME, 1);
    }

    private void configureSpinner(Spinner<Integer> spinner, int minValue, int maxValue, int initialValue, int step) {
        this.establishUpDownAction(spinner);
        this.configureFormatterForSpinner(spinner, minValue, maxValue, initialValue, step);
    }

    private void establishUpDownAction(Spinner<Integer> spinner) {
        spinner.getEditor().setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:
                    spinner.increment(1);
                    break;
                case DOWN:
                    spinner.decrement(1);
                    break;
            }
        });
    }

    private void configureFormatterForSpinner(Spinner<Integer> spinner, int minValue, int maxValue, int initialValue, int step) {
        // normal setup of spinner
        SpinnerValueFactory factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(minValue, maxValue, initialValue, step);
        spinner.setValueFactory(factory);
        spinner.setEditable(true);
        // hook in a formatter with the same properties as the factory
        TextFormatter formatter = new TextFormatter(factory.getConverter(), factory.getValue());
        spinner.getEditor().setTextFormatter(formatter);
        // bidirectional bind values
        factory.valueProperty().bindBidirectional(formatter.valueProperty());
    }

    private void setEnableSpinners(boolean isEnable) {
        this.spinnerPomodoros.setEditable(isEnable);
        this.spinnerBigRestTime.setEditable(isEnable);
        this.spinnerRestingTime.setEditable(isEnable);
        this.spinnerWorkingTime.setEditable(isEnable);
    }

    private void establishCurrentMode(TimerMode currentMode) {
        this.labelState.setText(currentMode.getName());
        this.configureButtonsFor(currentMode);
        this.setEnableControls(currentMode.isEnabledControls());
    }

    private void setEnableControls(boolean isEnabled) {
        if (PlatformFactory.isDesktop())
            this.setEnableSpinners(isEnabled);
        else this.setEnableSliders(isEnabled);
    }

    private void setEnableSliders(boolean isEnabled) {
        this.sliderPomodoros.setDisable(!isEnabled);
        this.sliderBigRestingTime.setDisable(!isEnabled);
        this.sliderRestingTime.setDisable(!isEnabled);
        this.sliderWorkingTime.setDisable(!isEnabled);
    }

    private void configureButtonsFor(TimerMode currentMode) {
        if (Arrays.asList(TimerMode.FINISHED, TimerMode.STAND_BY, TimerMode.PAUSED).contains(currentMode)) {
            this.btnStart.setDisable(false);
            this.btnStop.setDefaultButton(false);
            this.btnStart.setDefaultButton(true);
            this.btnStop.setDisable(true);
            this.btnReset.setDisable(false);
        } else {
            this.btnStart.setDisable(true);
            this.btnStart.setDefaultButton(false);
            this.btnStop.setDefaultButton(true);
            this.btnStop.setDisable(false);
            this.btnReset.setDisable(true);
        }
    }

    private void establishTimerProperties(TimerMode mode) {
        this.progressIndicator.setProgress(this.getProgress(mode));
    }

    private double getProgress(TimerMode mode) {
        double current = this.timerSeconds;
        double total = this.getTotalSecondsFor(mode);
        return (double) current / total;
    }

    private Long getTotalSecondsFor(TimerMode mode) {
        switch (mode) {
            case WORKING:
                return TimeUnit.MINUTES.toSeconds(this.getWorkingTime().longValue());
            case RESTING:
                return TimeUnit.MINUTES.toSeconds(this.getRestingTime().longValue());
            default: //BIG REST
                return TimeUnit.MINUTES.toSeconds(this.getBigRestTime().longValue());
        }
    }

    private Integer getBigRestTime() {
        return PlatformFactory.isDesktop() ?
                this.spinnerBigRestTime.getValue() :
                Double.valueOf(this.sliderBigRestingTime.getValue()).intValue();
    }

    private Integer getRestingTime() {
        return PlatformFactory.isDesktop() ?
                this.spinnerRestingTime.getValue() :
                Double.valueOf(this.sliderRestingTime.getValue()).intValue();
    }

    private Integer getWorkingTime() {
        return PlatformFactory.isDesktop() ?
                this.spinnerWorkingTime.getValue() :
                Double.valueOf(this.sliderWorkingTime.getValue()).intValue();
    }

    private void warnIfNecessaryFor(TimerMode currentMode) {
        if (currentMode.isEntailsWarn()) {
            PlatformFactory.getPlatform().warnForFinish(currentMode);
        }

    }

    /**
     * Method invoked by reset button.
     * @param actionEvent action
     */
    @FXML
    void doReset(ActionEvent actionEvent) {
        this.timerSeconds = 0;
        this.establishCurrentMode(TimerMode.STAND_BY);
        this.labelCountDown.setText("");
    }
}

