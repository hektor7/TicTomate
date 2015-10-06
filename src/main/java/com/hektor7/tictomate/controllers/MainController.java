package com.hektor7.tictomate.controllers;

import com.hektor7.tictomate.enums.TimerMode;
import com.hektor7.tictomate.models.State;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by hector on 27/09/15.
 */


public class MainController {

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
    private Spinner<Integer> spinnerPomodoros;

    @FXML
    private Spinner<Integer> spinnerWorkingTime;

    @FXML
    private Spinner<Integer> spinnerRestingTime;

    @FXML
    private Spinner<Integer> spinnerBigRestTime;

    @FXML
    private Button btnStart;

    @FXML
    private Button btnStop;

    Timer timer;

    long timerSeconds;


    @FXML
    void doStart(ActionEvent event) {

        List<State> states = this.createListOfStates();

        this.startTimerFor(states);
    }

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

    private List<State> createListOfStates() {
        List<State> states = new LinkedList<>();

        IntStream.rangeClosed(1, this.spinnerPomodoros.getValue()).sequential().forEach(i -> {
            Stream.of(TimerMode.WORKING, TimerMode.RESTING).sequential().forEach(mode -> {
                states.add(new State(this.obtainRealState(mode, i), this.getTotalSecondsFor(mode), states.size() + 1));
            });
        });

        states.add(new State(TimerMode.FINISHED, 1L, states.size() + 1));

        return states;

    }

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
        assert spinnerPomodoros != null : "fx:id=\"spinnerPomodoros\" was not injected: check your FXML file 'Main.fxml'.";
        assert spinnerWorkingTime != null : "fx:id=\"spinnerWorkingTime\" was not injected: check your FXML file 'Main.fxml'.";
        assert spinnerRestingTime != null : "fx:id=\"spinnerRestingTime\" was not injected: check your FXML file 'Main.fxml'.";
        assert spinnerBigRestTime != null : "fx:id=\"spinnerBigRestTime\" was not injected: check your FXML file 'Main.fxml'.";
        assert btnStart != null : "fx:id=\"btnStart\" was not injected: check your FXML file 'Main.fxml'.";
        assert btnStop != null : "fx:id=\"btnStop\" was not injected: check your FXML file 'Main.fxml'.";

        this.initializeControls();

    }

    private void initializeControls() {
        this.initializeSpinners();
        this.configureButtonsFor(TimerMode.STAND_BY);
    }

    private void initializeSpinners() {
        this.setEnableSpinners(true);


        this.configureSpinner(this.spinnerPomodoros, 1, 20, 1, 1);
        this.configureSpinner(this.spinnerWorkingTime, 1, 120, 25, 1);
        this.configureSpinner(this.spinnerRestingTime, 1, 60, 5, 1);
        this.configureSpinner(this.spinnerBigRestTime, 1, 120, 15, 1);

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

    private void establishCurrentMode(TimerMode currentState) {
        this.labelState.setText(currentState.getName());
        this.configureButtonsFor(currentState);
        this.setEnableSpinners(currentState.isEnabledControls());
    }

    private void configureButtonsFor(TimerMode currentState) {
        if (Arrays.asList(TimerMode.FINISHED, TimerMode.STAND_BY, TimerMode.PAUSED).contains(currentState)) {
            this.btnStart.setDisable(false);
            this.btnStop.setDefaultButton(false);
            this.btnStart.setDefaultButton(true);
            this.btnStop.setDisable(true);
        } else {
            this.btnStart.setDisable(true);
            this.btnStart.setDefaultButton(false);
            this.btnStop.setDefaultButton(true);
            this.btnStop.setDisable(false);
        }
    }

    private void establishTimerProperties(TimerMode state) {
        this.progressIndicator.setProgress(this.getProgress(state));
    }

    private double getProgress(TimerMode state) {
        double current = this.timerSeconds;
        double total = this.getTotalSecondsFor(state);
        return (double) current / total;
    }

    private Long getTotalSecondsFor(TimerMode state) {
        switch (state) {
            case WORKING:
                return TimeUnit.MINUTES.toSeconds(this.spinnerWorkingTime.getValue());
            case RESTING:
                return TimeUnit.MINUTES.toSeconds(this.spinnerRestingTime.getValue());
            default: //BIG REST
                return TimeUnit.MINUTES.toSeconds(this.spinnerBigRestTime.getValue());
        }
    }

    private void warnIfNecessaryFor(TimerMode currentState) {
        if (currentState.isEntailsWarn()) {
            this.playBell();
            //TODO: Bring window to front
        }
    }

    private void playBell() {
        URI uri = null;
        try {
            uri = this.getClass().getResource("/bell.wav").toURI();
        } catch (URISyntaxException e) {
            //FIXME: Show error
        }
        AudioClip audioClip = new AudioClip(uri.toString());
        audioClip.play();
    }
}

