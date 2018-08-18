package com.las.atmosherebinauraltherapy.views.fragments;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.las.atmosherebinauraltherapy.R;
import com.las.atmosherebinauraltherapy.databinding.TimerSettingFragmentBinding;
import com.las.atmosherebinauraltherapy.helper.AnimationHelper;
import com.las.atmosherebinauraltherapy.helper.AppConstant;
import com.las.atmosherebinauraltherapy.helper.SharedPreferenceHelper;
import com.las.atmosherebinauraltherapy.model.SetWaveVolumeModel;
import com.las.atmosherebinauraltherapy.model.TimeSaveEventModel;
import com.las.atmosherebinauraltherapy.model.UpdateTimeEventModel;
import com.shawnlin.numberpicker.NumberPicker;

import org.greenrobot.eventbus.EventBus;

public class TimerSettingFragment extends Fragment implements NumberPicker.OnValueChangeListener {
    TimerSettingFragmentBinding binding;
    int hour = 00, minute = 00;
    int height;
    boolean numberChaged;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.timer_setting_fragment, container, false);
        hour = SharedPreferenceHelper.getInstance().getIntegerValue(AppConstant.HOUR_DATA, 00);
        minute = SharedPreferenceHelper.getInstance().getIntegerValue(AppConstant.MINUTE_DATA, 00);
        if (minute != 0) {
            setTimeInTextView();
//            binding.timeShow.setText(hour + ":" + minute);
        }
        binding.setActivity(this);
        initHourAndMinute();
        return binding.getRoot();
    }


    private void initHourAndMinute() {
        binding.hourList.setMaxValue(12);
        binding.hourList.setMinValue(0);
        binding.minuteList.setMaxValue(59);
        binding.minuteList.setMinValue(0);
        binding.minuteList.setValue(minute);
        binding.hourList.setValue(hour);
        binding.hourList.setOnValueChangedListener(this);
        binding.minuteList.setOnValueChangedListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.countinus_btn_save: {
                if (!numberChaged) {
                    if (minute == 0) {
                        minute = 59;
                        Log.d("saad", "onClick: " + minute);
                    }
                }
                SharedPreferenceHelper.getInstance().setIntegerValue(AppConstant.HOUR_DATA, hour);
                SharedPreferenceHelper.getInstance().setIntegerValue(AppConstant.MINUTE_DATA, minute);
                if (binding.closeAppCheck.isChecked()) {
                    SharedPreferenceHelper.getInstance().setBooleanValue(AppConstant.APP_CLOSE, true);
                } else {
                    SharedPreferenceHelper.getInstance().setBooleanValue(AppConstant.APP_CLOSE, false);
                }
                EventBus.getDefault().post(new TimeSaveEventModel());
                EventBus.getDefault().post(new UpdateTimeEventModel());
                break;
            }
        }
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        switch (picker.getId()) {
            case R.id.hour_list:
                numberChaged = true;
                hour = newVal;
                setTimeInTextView();

                break;
            case R.id.minute_list:
                numberChaged = true;
                minute = newVal;
                setTimeInTextView();

                break;


        }
    }

    private void setTimeInTextView() {
        if (hour < 10) {
            if (minute < 10) {
                binding.timeShow.setText("0" + hour + ":" + "0" + minute);
            } else {
                binding.timeShow.setText("0" + hour + ":" + minute);
            }

        } else {
            if (minute < 10) {
                binding.timeShow.setText(hour + ":" + "0" + minute);
            } else {
                binding.timeShow.setText(hour + ":" + minute);
            }

        }
    }

}
