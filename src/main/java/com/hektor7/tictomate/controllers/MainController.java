package com.hektor7.tictomate.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

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
    private Spinner<?> pickPomodoros;

    @FXML
    private Spinner<?> pickerWorkingTime;

    @FXML
    private Spinner<?> pickerRestingTime;

    @FXML
    private Spinner<?> pickerBigRestTime;

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
        assert mainPane != null : "fx:id=\"mainPane\" was not injected: check your FXML file 'main.fxml'.";
        assert mainVBox != null : "fx:id=\"mainVBox\" was not injected: check your FXML file 'main.fxml'.";
        assert pickPomodoros != null : "fx:id=\"pickPomodoros\" was not injected: check your FXML file 'main.fxml'.";
        assert pickerWorkingTime != null : "fx:id=\"pickerWorkingTime\" was not injected: check your FXML file 'main.fxml'.";
        assert pickerRestingTime != null : "fx:id=\"pickerRestingTime\" was not injected: check your FXML file 'main.fxml'.";
        assert pickerBigRestTime != null : "fx:id=\"pickerBigRestTime\" was not injected: check your FXML file 'main.fxml'.";
        assert btnStart != null : "fx:id=\"btnStart\" was not injected: check your FXML file 'main.fxml'.";
        assert btnStop != null : "fx:id=\"btnStop\" was not injected: check your FXML file 'main.fxml'.";

    }
}

