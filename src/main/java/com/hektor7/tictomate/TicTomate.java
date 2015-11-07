package com.hektor7.tictomate;

import com.hektor7.tictomate.ui.FXMLPage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class TicTomate extends Application {


    @Override
    public void start(Stage stage) throws IOException {

        this.arrangeView(stage);

    }

    private void arrangeView(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(FXMLPage.MAIN.getPageUrl());


        Scene scene = PlatformFactory.isDesktop() ? new Scene(root) : createSceneForMobile(root);

        stage.setScene(scene);
        stage.setTitle("TicTomate v0.1");
        stage.setResizable(false);
        stage.show();
    }

    private Scene createSceneForMobile(Parent root) {
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        return new Scene(root, bounds.getWidth(), bounds.getHeight());
    }

    public static void main(String[] args) {
        launch(args);
    }

}
