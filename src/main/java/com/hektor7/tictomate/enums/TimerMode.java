package com.hektor7.tictomate.enums;

/**
 * Created by hector on 2/09/15.
 */
public enum TimerMode {
    STAND_BY("Stand by", false),
    WORKING("Working", true),
    RESTING("Resting", true),
    FINISHED("Finished", false);

    public String getName() {
        return name;
    }

    public boolean isExitFromThisModePlaysBell() {
        return existFromThisModePlaysBell;
    }

    String name = "";
    boolean existFromThisModePlaysBell = false;

    TimerMode(String name, boolean exitFromThisModePlaysBell){
        this.name = name;
        this.existFromThisModePlaysBell = exitFromThisModePlaysBell;
    }


}
