package com.las.atmosherebinauraltherapy.services;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import com.las.atmosherebinauraltherapy.helper.AppConstant;
import com.las.atmosherebinauraltherapy.helper.SharedPreferenceHelper;
import com.las.atmosherebinauraltherapy.model.SetBgVolumeModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

/**
 * Created by RanaTalal on 4/26/2018.
 */

public class BgMusicService extends Service {
    MediaPlayer bgMediaPlayer;
    int maxVolume = 30;

    @Override
    public void onCreate() {
        super.onCreate();


    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        if (bgMediaPlayer == null) {
            bgMediaPlayer = new MediaPlayer();
        } else {
            bgMediaPlayer.reset();
        }
        startBgMusic();
        return START_STICKY;
    }


    private void startBgMusic() {
        if (!SharedPreferenceHelper.getInstance().getStringValue(AppConstant.BG_MUSIC, "None").equals("None")) {
            setFilePathInMediaPlayer(SharedPreferenceHelper.getInstance().getStringValue(AppConstant.BG_MUSIC, "None"));
            try {
                bgMediaPlayer.start();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }


        } else {
            stopBgMusic();
        }
    }

    private void stopBgMusic() {

        try {
            if (bgMediaPlayer.isPlaying()) {
                bgMediaPlayer.stop();
                bgMediaPlayer.release();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        stopBgMusic();
        super.onDestroy();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SetBgVolumeModel volumeModel) {
        if (bgMediaPlayer.isPlaying()) {
            final float volume = (float) (1 - (Math.log(maxVolume - volumeModel.getVolume()) / Math.log(maxVolume)));
            bgMediaPlayer.setVolume(volume, volume);
        }


    }

    public void setFilePathInMediaPlayer(String filename) {

        try {
//           bgMediaPlayer=new MediaPlayer();
//            bgMediaPlayer.reset();
            AssetFileDescriptor afd;
            afd = getApplicationContext().getAssets().openFd("tones/" + filename.toLowerCase() + ".mp3");
            bgMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            final float volume = (float) (1 - (Math.log(maxVolume - SharedPreferenceHelper.getInstance().getIntegerValue(AppConstant.BG_Volume, 10)) / Math.log(maxVolume)));
            bgMediaPlayer.setVolume(volume, volume);
            bgMediaPlayer.prepare();
            bgMediaPlayer.setLooping(true);


        } catch (IllegalStateException e) {
            Log.d("saad", "setFilePathInMediaPlayer: " + e.getStackTrace());
            Log.d("saad", "setFilePathInMediaPlayer: " + e);
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
