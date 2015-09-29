package com.hektor7.tictomate.enums;

/**
 * Created by hector on 2/09/15.
 */
public enum TimerMode {
    STAND_BY("Stand by", false),
    WORKING("Working", true),
    RESTING("Resting", true),
    FINISHED("Finished", true);

    String name = "";
    boolean noisyChange = false;

    public boolean entailsPlaySound() {
        return noisyChange;
    }

    public String getName() {
        return name;
    }

    TimerMode(String name, boolean noisyChange){
        this.name = name;
        this.noisyChange = noisyChange;
    }


}
