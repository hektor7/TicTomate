package com.hektor7.tictomate.models;

import com.hektor7.tictomate.enums.TimerMode;

/**
 * Created by hector on 4/10/15.
 */
public class State implements Comparable<State> {

    TimerMode mode;
    Long totalSeconds;
    Integer order;
    Integer numberOfPomodoro;

    public State(TimerMode mode, Long totalSeconds, Integer order, Integer numberOfPomodoro) {
        this.mode = mode;
        this.totalSeconds = totalSeconds;
        this.order = order;
        this.numberOfPomodoro = numberOfPomodoro;
    }

    public TimerMode getMode() {
        return mode;
    }

    public void setMode(TimerMode mode) {
        this.mode = mode;
    }

    public Long getTotalSeconds() {
        return totalSeconds;
    }

    public void setTotalSeconds(Long totalSeconds) {
        this.totalSeconds = totalSeconds;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getNumberOfPomodoro() {
        return numberOfPomodoro;
    }

    public void setNumberOfPomodoro(Integer numberOfPomodoro) {
        this.numberOfPomodoro = numberOfPomodoro;
    }

    @Override
    public int compareTo(State other) {
        return this.getOrder().compareTo(other.getOrder());
    }
}
