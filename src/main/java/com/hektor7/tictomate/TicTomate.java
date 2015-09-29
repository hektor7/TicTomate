package com.hektor7.tictomate;

import com.hektor7.tictomate.ui.FXMLPage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class TicTomate extends Application {


    @Override
    public void start(Stage stage) throws IOException {

        URL url = FXMLPage.MAIN.getPageUrl();

        Parent root = FXMLLoader.load(FXMLPage.MAIN.getPageUrl());

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("TicTomate v0.1");
        stage.setResizable(false);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
