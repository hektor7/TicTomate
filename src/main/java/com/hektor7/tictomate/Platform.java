package com.hektor7.tictomate;

import com.hektor7.tictomate.enums.TimerMode;

/**
 * Created by hector on 7/11/15.
 */
public interface Platform {
    /**
     * Launches a warning in order to alert of current mode is finished.
     *
     * @param finishedMode finished mode
     */
    void warnForFinish(TimerMode finishedMode);
}
