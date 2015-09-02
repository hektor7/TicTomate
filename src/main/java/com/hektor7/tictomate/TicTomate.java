package com.hektor7.tictomate;

import com.hektor7.tictomate.ui.MainScreen;
import javafx.application.Application;
import javafx.stage.Stage;

public class TicTomate extends Application {


    MainScreen mainScreen;

    @Override
    public void start(Stage stage) {

        this.mainScreen = new MainScreen();
        this.mainScreen.buildUi(stage);

        stage.show();
    }
}
