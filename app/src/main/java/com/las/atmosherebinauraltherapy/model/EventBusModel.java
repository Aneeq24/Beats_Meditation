package com.las.atmosherebinauraltherapy.model;

/**
 * Created by RanaTalal on 4/26/2018.
 */

public class EventBusModel {
    boolean isPlaying;

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public EventBusModel(boolean isPlaying) {

        this.isPlaying = isPlaying;
    }
}
