package com.las.atmosherebinauraltherapy.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.las.atmosherebinauraltherapy.helper.AppConstant;
import com.las.atmosherebinauraltherapy.helper.SharedPreferenceHelper;
import com.las.atmosherebinauraltherapy.model.SetBgVolumeModel;
import com.las.atmosherebinauraltherapy.model.SetWaveVolumeModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import binauralbeats.Binaural;

/**
 * Created by RanaTalal on 4/23/2018.
 */

public class AudioService extends Service {
    boolean isPlaying;
    Binaural binaural;
    int maxVolume = 50;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            float frequency = intent.getFloatExtra("frequency", 0.0f);
            float beat = intent.getFloatExtra("beat", 0.0f);
            boolean status = intent.getBooleanExtra("status", false);
            audioPlay(frequency, beat, status);
            if (!EventBus.getDefault().isRegistered(this))
                EventBus.getDefault().register(this);
        }

        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        if (isPlaying)
            binaural.stop();
        super.onDestroy();

    }

    public void audioPlay(float frequency, float beat, boolean isBinaural) {
        final float volume = (float) (1 - (Math.log(maxVolume - SharedPreferenceHelper.getInstance().getIntegerValue(AppConstant.WAVE_VOLUME, 20)) / Math.log(maxVolume)));
        if (!isPlaying) {
            isPlaying = true;
            binaural = new Binaural(frequency, beat, volume);
            if (!binaural.getIsPlaying())
                binaural.start();

        } else {
            isPlaying = false;
            if (isBinaural) {
                isPlaying = true;
                binaural.release();
                binaural.stop();
                binaural = new Binaural(frequency, beat, volume);
                if (!binaural.getIsPlaying())
                    binaural.start();
            } else {
                binaural.stop();
                binaural.release();


            }
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SetWaveVolumeModel volumeModel) {
        if (isPlaying && binaural != null) {
            final float volume = (float) (1 - (Math.log(maxVolume - volumeModel.getVolume()) / Math.log(maxVolume)));
            binaural.setVolume(volume);
        }


    }


}
