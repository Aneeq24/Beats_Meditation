package com.las.atmosherebinauraltherapy.views.fragments;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Chronometer;

import com.las.atmosherebinauraltherapy.R;
import com.las.atmosherebinauraltherapy.databinding.SubCategoryFragmentBinding;
import com.las.atmosherebinauraltherapy.helper.AnimationHelper;
import com.las.atmosherebinauraltherapy.helper.AppConstant;
import com.las.atmosherebinauraltherapy.helper.SharedPreferenceHelper;
import com.las.atmosherebinauraltherapy.model.CloseAppBussEvent;
import com.las.atmosherebinauraltherapy.model.EventBusModel;
import com.las.atmosherebinauraltherapy.model.MusicActivityModel;
import com.las.atmosherebinauraltherapy.model.SetAlphaValueModel;
import com.las.atmosherebinauraltherapy.model.TimeSaveEventModel;
import com.las.atmosherebinauraltherapy.model.UpdateTimeEventModel;
import com.las.atmosherebinauraltherapy.services.AudioService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import binauralbeats.Binaural;

public class SubCategoryFragment extends Fragment implements Chronometer.OnChronometerTickListener {
    SubCategoryFragmentBinding binding;
    String categoryName;
    String subCategoryName, description;
    int position;
    float frequency, beat;
    Binaural binaural;
    boolean isPlaying = false;
    MusicActivityModel model;
    public CountDownTimer countDownTimer;
    long totalDuration;
    boolean isRuningCDT, completeCounter;
    int hour, minute;

    java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("mm:ss", Locale.getDefault());


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.sub_category_fragment, container, false);
        binding.setActivity(this);
        categoryName = getArguments().getString("name");
        position = getArguments().getInt("position");
        binding.secondView.setVisibility(View.GONE);
        model = new MusicActivityModel();
        getSubCategoryAndDescription(position);
        binding.setModel(model);
        binding.startBtn.setText("START");
        binding.startBtn.setTextColor(getResources().getColor(R.color.whiteColor));
        hour = SharedPreferenceHelper.getInstance().getIntegerValue(AppConstant.HOUR_DATA, 00);
        minute = SharedPreferenceHelper.getInstance().getIntegerValue(AppConstant.MINUTE_DATA, 00);
        setTimeInTextView();
        binding.chronometerView.setOnChronometerTickListener(this);
//        binding.timeDurationView.setOnChronometerTickListener(this);

        getFrequencyAndBeat();
        return binding.getRoot();
    }

    private void setTimeInTextView() {
        Log.d("talal", "onMessageEvent: done");
        if (hour < 10) {
            Log.d("talal", "onMessageEvent: done");
            if (minute < 10) {
                binding.timeDurationView.setText("0" + hour + ":" + "0" + minute);
            } else {
                binding.timeDurationView.setText("0" + hour + ":" + minute);
            }

        } else {
            Log.d("talal", "onMessageEvent: done");
            if (minute < 10) {
                binding.timeDurationView.setText(hour + ":" + "0" + minute);
            } else {
                binding.timeDurationView.setText(hour + ":" + minute);
            }

        }
    }

    private void getFrequencyAndBeat() {
        switch (categoryName) {
            case "Brain":
                frequency = getFrequency(AppConstant.brainFrequency, position);
                beat = getBeat(AppConstant.brainBeat, position);
                break;

            case "Body":
                frequency = getFrequency(AppConstant.bodyFrequency, position);
                beat = getBeat(AppConstant.bodyBeat, position);
                break;
            case "Sleep":
                frequency = getFrequency(AppConstant.sleepFrequency, position);
                beat = getBeat(AppConstant.sleepBeat, position);
                break;
            case "Spirit":
                frequency = getFrequency(AppConstant.spiritFrequency, position);
                beat = getBeat(AppConstant.spiritBeat, position);
                break;
        }
        Log.d("talal", "getFrequencyAndBeat: " + frequency);
        if (!isPlaying)
            binaural = new Binaural(frequency, beat, 30f);
    }

    private float getBeat(float[] beat, int position) {
        return beat[position];
    }

    private float getFrequency(float[] frequency, int position) {
        return frequency[position];
    }


    private void initCountDownTimer() {
        totalDuration = TimeUnit.MINUTES.toMillis(getTotalMinute());
        binding.startBtn.setMax((int) totalDuration);
        binding.chronometerView.setBase(SystemClock.elapsedRealtime());

        binding.timeDurationView.setText("00:00");


        countDownTimer = new CountDownTimer(totalDuration, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.startBtn.setProgress((int) (totalDuration - millisUntilFinished));
                getTime(millisUntilFinished);
                if ((totalDuration / 1.75) > millisUntilFinished) {
                    binding.startBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
                }

                isRuningCDT = true;
                if (millisUntilFinished < 100) {
                    completeCounter = true;
                }

            }


            @Override
            public void onFinish() {
                if (SharedPreferenceHelper.getInstance().getBooleanValue(AppConstant.APP_CLOSE, false)) {
                    if (totalDuration != 0 && completeCounter) {
                        getActivity().finish();
                        EventBus.getDefault().post(new CloseAppBussEvent());

                    }
                }
                binding.secondView.setText("00");
                binding.secondView.setVisibility(View.GONE);
                binding.chronometerView.setBase(SystemClock.elapsedRealtime());
                binding.chronometerView.stop();
                binding.timeDurationView.setText("00" + ":" + "00");
                binding.startBtn.setTextColor(getResources().getColor(R.color.whiteColor));
                binding.startBtn.setText("START");
                isPlaying = false;
                isRuningCDT = false;
                binding.startBtn.setProgress((int) 0);

                destoryService();
            }
        };
    }

    private void closeApp() {
        if (SharedPreferenceHelper.getInstance().getBooleanValue(AppConstant.APP_CLOSE, false)) {
            if (totalDuration != 0) {
                countDownTimer.onFinish();
                if (getActivity() != null)
                    getActivity().finish();
                EventBus.getDefault().post(new CloseAppBussEvent());
            }
        }
    }

    private void getTime(long millisUntilFinished) {

        long currHour = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
        long currMinute = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - (currHour * 60);

        long currSecond = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));


        if (currHour == 0) {

            if (currMinute < 10) {
                if (currSecond < 10) {
                    binding.timeDurationView.setText("0" + currMinute + ":" + "0" + currSecond);
                } else {
                    binding.timeDurationView.setText("0" + currMinute + ":" + currSecond);
                }
            } else {
                if (currSecond < 10) {
                    binding.timeDurationView.setText(currMinute + ":" + "0" + currSecond);
                } else {
                    binding.timeDurationView.setText(currMinute + ":" + currSecond);
                }
            }
        } else {
            binding.secondView.setVisibility(View.VISIBLE);
            if (currSecond < 10) {
                binding.secondView.setText("0" + currSecond);
            } else {
                binding.secondView.setText(String.valueOf(currSecond));
            }
            if (hour < 10) {
                if (currMinute < 10) {
                    binding.timeDurationView.setText("0" + currHour + ":" + "0" + currMinute);


                } else {
                    binding.timeDurationView.setText("0" + currHour + ":" + currMinute);
                }
            } else {
                if (currMinute < 10) {
                    binding.timeDurationView.setText(currHour + ":" + "0" + currMinute);
                } else {
                    binding.timeDurationView.setText(currHour + ":" + currMinute);
                }
            }
        }
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_btn:

                if (!isPlaying) {
                    isPlaying = true;
                    EventBus.getDefault().post(new EventBusModel(true));
                    if (isRuningCDT) {
                        countDownTimer.cancel();
                        countDownTimer.onFinish();
                    }
                    initCountDownTimer();
                    if (countDownTimer != null && getTotalMinute() != 0) {
                        binding.chronometerView.start();
                        countDownTimer.start();

                    } else {
                        binding.chronometerView.start();

                    }
                    binding.startBtn.setText("STOP");
                    startAudioService(isPlaying);
                } else {
                    isPlaying = false;
                    EventBus.getDefault().post(new EventBusModel(false));
                    binding.startBtn.setText("START");
                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                        countDownTimer.onFinish();
                    }

                }
                break;
        }
    }

    private void startAudioService(boolean status) {
        Intent intent = new Intent(getContext(), AudioService.class);
        intent.putExtra("frequency", frequency);
        intent.putExtra("beat", beat);
        intent.putExtra("status", status);
        getActivity().startService(intent);
    }

    private void getSubCategoryAndDescription(int position) {
        switch (categoryName) {
            case "Brain":
                subCategoryName = AppConstant.brainCategory[position];
                description = AppConstant.brainCategoryDescription[position];
                model.setDescription(description);
                model.setSubTitle(subCategoryName);
                break;

            case "Body":
                subCategoryName = AppConstant.bodyCategory[position];
                description = AppConstant.bodyCategoryDescription[position];
                model.setSubTitle(subCategoryName);
                model.setDescription(description);
                break;

            case "Sleep":
                subCategoryName = AppConstant.sleepCategory[position];
                description = AppConstant.sleepCategoryDescription[position];
                model.setSubTitle(subCategoryName);
                model.setDescription(description);
                break;

            case "Spirit":
                subCategoryName = AppConstant.spiritCategory[position];
                description = AppConstant.spiritCategoryDescription[position];
                model.setSubTitle(subCategoryName);
                model.setDescription(description);
                break;
        }
    }

    private long getTotalMinute() {
        Log.d("talal", "getTotalMinute: " + minute + (hour * 60));
        hour = SharedPreferenceHelper.getInstance().getIntegerValue(AppConstant.HOUR_DATA, 00);
        minute = SharedPreferenceHelper.getInstance().getIntegerValue(AppConstant.MINUTE_DATA, 00);
        return minute + (hour * 60);

    }

    private void destoryService() {
        Intent intent = new Intent(getActivity(), AudioService.class);
        intent.putExtra("frequency", frequency);
        intent.putExtra("beat", beat);
        intent.putExtra("status", false);
        getActivity().stopService(intent);
    }

    @Override
    public void onDestroy() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer.onFinish();
        }

        super.onDestroy();

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UpdateTimeEventModel eventBusModel) {
        Log.d("talal", "onMessageEvent: done");
        hour = SharedPreferenceHelper.getInstance().getIntegerValue(AppConstant.HOUR_DATA, 00);
        minute = SharedPreferenceHelper.getInstance().getIntegerValue(AppConstant.MINUTE_DATA, 00);
        setTimeInTextView();

    }


    private void defaultScreenBrightness(float progress) {
        final WindowManager.LayoutParams layout = getActivity().getWindow().getAttributes();
        layout.screenBrightness = progress;
        getActivity().getWindow().setAttributes(layout);
    }


    @Override
    public void onChronometerTick(Chronometer chronometer) {
        if (getTotalMinute() == 0) {
            binding.timeDurationView.setText(chronometer.getText());
        }

    }
}
