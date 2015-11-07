package com.hektor7.tictomate.desktop;

import com.hektor7.tictomate.Platform;
import com.hektor7.tictomate.enums.TimerMode;
import javafx.scene.control.Alert;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;

/**
 * Created by hector on 7/11/15.
 */
public class DesktopPlatform implements Platform {

    private final String LABEL_FOR_TITLE_TIME_UP = "Time is up!";
    private final String LABEL_FOR_MSG_TIME_UP = "{0} time has finished!";

    @Override
    public void warnForFinish(TimerMode finishedMode) {
        this.showFinishDialogFor(finishedMode);
        this.playBell();
    }

    private void showFinishDialogFor(TimerMode finishedMode) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(LABEL_FOR_TITLE_TIME_UP);
        alert.setHeaderText(null);
        alert.setContentText(MessageFormat.
                format(LABEL_FOR_MSG_TIME_UP,
                        finishedMode.getName()));
        alert.show();
    }

    private void playBell() {
        URI uri = null;
        try {
            uri = this.getClass().getResource("/bell.mp3").toURI();
            MediaPlayer audio = new MediaPlayer(
                    new Media(uri.toString()));
            audio.play();
        } catch (URISyntaxException e) {
            //FIXME: Show error
        }
    }
}
