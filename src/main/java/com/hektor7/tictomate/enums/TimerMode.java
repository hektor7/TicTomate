package com.hektor7.tictomate.enums;

/**
 * Created by hector on 2/09/15.
 */
public enum TimerMode {
    STAND_BY("Stand by", false, true),
    WORKING("Working", true, false),
    RESTING("Resting", true, false),
    BIG_RESTING("Big Resting", true, false),
    PAUSED("Paused", false, true),
    FINISHED("Finished", false, true);

    String name = "";
    boolean entailsPlaySound = false;
    boolean enabledControls = true;

    public boolean isEntailsPlaySound() {
        return entailsPlaySound;
    }

    public boolean isEnabledControls(){
        return enabledControls;
    }

    public String getName() {
        return name;
    }

    TimerMode(String name, boolean entailsPlaySound, boolean enabledControls){
        this.name = name;
        this.entailsPlaySound = entailsPlaySound;
        this.enabledControls = enabledControls;
    }


}
