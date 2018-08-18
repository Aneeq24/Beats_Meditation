package com.las.atmosherebinauraltherapy.helper;

/**
 * Created by RanaTalal on 4/23/2018.
 */

public class AudioStatus {
    public static AudioStatus status;

    public static AudioStatus getInstance() {
        if (status==null){
            status=new AudioStatus();
        }
        return status;
    }

    boolean isPlaying;

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }


}
