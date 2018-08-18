package com.las.atmosherebinauraltherapy.views.fragments;

import android.databinding.DataBindingUtil;
import android.media.AudioManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.las.atmosherebinauraltherapy.R;
import com.las.atmosherebinauraltherapy.databinding.VolumeSettingFragmentBinding;
import com.las.atmosherebinauraltherapy.helper.AppConstant;
import com.las.atmosherebinauraltherapy.helper.SharedPreferenceHelper;
import com.las.atmosherebinauraltherapy.model.SetBgVolumeModel;
import com.las.atmosherebinauraltherapy.model.SetWaveVolumeModel;

import org.greenrobot.eventbus.EventBus;

public class VolumeSettingFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {
    VolumeSettingFragmentBinding binding;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.volume_setting_fragment, container, false);
        binding.backgroundVolume.setOnSeekBarChangeListener(this);
        binding.waveVolume.setOnSeekBarChangeListener(this);
        binding.waveVolume.setProgress(SharedPreferenceHelper.getInstance().getIntegerValue(AppConstant.WAVE_VOLUME, 20));
        binding.backgroundVolume.setProgress(SharedPreferenceHelper.getInstance().getIntegerValue(AppConstant.BG_Volume, 10));
        return binding.getRoot();
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.background_volume:
                binding.backgroundVolume.setProgress(progress);
                EventBus.getDefault().post(new SetBgVolumeModel(progress));
                SharedPreferenceHelper.getInstance().setIntegerValue(AppConstant.BG_Volume, progress);
                break;
            case R.id.wave_volume:
                binding.waveVolume.setProgress(progress);
                EventBus.getDefault().post(new SetWaveVolumeModel(progress));
                SharedPreferenceHelper.getInstance().setIntegerValue(AppConstant.WAVE_VOLUME, progress);
                break;

        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

}
