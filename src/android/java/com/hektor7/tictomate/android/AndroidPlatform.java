package com.hektor7.tictomate.android;

import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import com.hektor7.tictomate.Platform;
import com.hektor7.tictomate.enums.TimerMode;
import javafxports.android.FXActivity;

/**
 * Created by hector on 7/11/15.
 */
public class AndroidPlatform implements Platform {
    

    @Override
    public void warnForFinish(TimerMode finishedMode) {
        this.playBell();
    }

    private void playBell() {

        FXActivity f =  FXActivity.getInstance();
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        MediaPlayer mp = MediaPlayer.create(f, notification);
        //TODO: It would be interesting to use android.resource:// in order to play bell.mp3

        mp.start();
    }
}
