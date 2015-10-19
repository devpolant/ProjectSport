package com.polant.projectsport.eventbus;

/**
 * Created by Антон on 19.10.2015.
 */
public final class StepDetectEvent {

    private int value;

    public StepDetectEvent(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
