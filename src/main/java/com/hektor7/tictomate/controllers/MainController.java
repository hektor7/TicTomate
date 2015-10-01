package com.hektor7.tictomate.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Spinner;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Created by hector on 27/09/15.
 */


public class MainController {

    private static final int MIN_POMODOROS = 1;
    private static final int MAX_POMODOROS = 20;
    private static final int DEFAULT_POMODOROS = 1;
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

    @FXML
    void doStart(ActionEvent event) {

    }

    @FXML
    void doStop(ActionEvent event) {

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

        this.initializeSpinners();

    }

    private void initializeSpinners() {
        this.enableSpinners();
        this.enableButtons();
    }

    private void enableButtons() {
        this.btnStart.setDisable(false);
        this.btnStop.setDisable(false);
    }

    private void disableButtons() {
        this.btnStart.setDisable(true);
        this.btnStop.setDisable(true);
    }

    private void enableSpinners() {
        this.spinnerPomodoros.setEditable(true);
        this.spinnerBigRestTime.setEditable(true);
        this.spinnerRestingTime.setEditable(true);
        this.spinnerWorkingTime.setEditable(true);
    }

    private void disableSpinners() {
        this.spinnerPomodoros.setEditable(false);
        this.spinnerBigRestTime.setEditable(false);
        this.spinnerRestingTime.setEditable(false);
        this.spinnerWorkingTime.setEditable(false);
    }
}

