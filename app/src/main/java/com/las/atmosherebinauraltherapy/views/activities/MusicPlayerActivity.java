package com.las.atmosherebinauraltherapy.views.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.las.atmosherebinauraltherapy.R;
import com.las.atmosherebinauraltherapy.adapters.CategoryViewPagerAdapter;
import com.las.atmosherebinauraltherapy.adapters.SettingViewPagerAdapter;
import com.las.atmosherebinauraltherapy.databinding.ActivityMusicPlayerBinding;
import com.las.atmosherebinauraltherapy.helper.AnimationHelper;
import com.las.atmosherebinauraltherapy.helper.AppConstant;
import com.las.atmosherebinauraltherapy.helper.SharedPreferenceHelper;
import com.las.atmosherebinauraltherapy.model.EventBusModel;
import com.las.atmosherebinauraltherapy.model.MusicActivityModel;
import com.las.atmosherebinauraltherapy.model.SetAlphaValueModel;
import com.las.atmosherebinauraltherapy.model.TimeSaveEventModel;
import com.las.atmosherebinauraltherapy.services.AudioService;
import com.las.atmosherebinauraltherapy.services.BgMusicService;
import com.las.atmosherebinauraltherapy.views.fragments.SettingSwapShowCaseDemo;
import com.las.atmosherebinauraltherapy.views.fragments.SubCategoryFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;


public class MusicPlayerActivity extends AppCompatActivity implements/* NumberPicker.OnValueChangeListener,SeekBar.OnSeekBarChangeListener,*/  ViewPager.OnPageChangeListener, View.OnTouchListener {
    ActivityMusicPlayerBinding binding;
    MusicActivityModel model;

    int height;
    int currentPosition;

    float x1, x2;
    float y1, y2;

    CategoryViewPagerAdapter adapter;
    SubCategoryFragment subCategoryFragment;
    String categoryName;
    ArrayList<SubCategoryFragment> arrayList;
    boolean settingIsOpen;
    SettingViewPagerAdapter settingViewPagerAdapter;

    CountDownTimer lowToHighBrightnessCountDownTimer;

    CountDownTimer highToLowBrightnessCountDownTimer;

    SettingSwapShowCaseDemo settingSwapShowCaseDemo;
    boolean countDownTimerStop, settingBtnClick, lowToHighBrightness, highToLowBrightness;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_music_player);
        binding.setActivity(this);
        categoryName = getIntent().getStringExtra("categoryName");
        setBackgroundImage(0);
        model = new MusicActivityModel();
        checkResolution();
        startService(new Intent(this, BgMusicService.class));

        setAdapter();
        setSettingAdapter();

        if (!SharedPreferenceHelper.getInstance().getBooleanValue(AppConstant.LIST_DEMO, false)) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            settingSwapShowCaseDemo = new SettingSwapShowCaseDemo();
            transaction.add(R.id.sound_layout, settingSwapShowCaseDemo).commit();
        }


        binding.categoryViewpager.setOffscreenPageLimit(2);

        binding.categoryViewpager.addOnPageChangeListener(this);

        binding.categoryViewpager.setOnTouchListener(this);

        if (!SharedPreferenceHelper.getInstance().getBooleanValue(AppConstant.LIST_DEMO, false)) {
            AnimationHelper.getInstance().rotationAnimation(this, binding.settingIcon);
        }

        initLowToHighBrightnessCountDownTimer();
        initHightToLowBrightnessCountDownTimer();
    }

    private void initLowToHighBrightnessCountDownTimer() {
        lowToHighBrightnessCountDownTimer = new CountDownTimer(60000, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                double progress = ((millisUntilFinished * 1.0) / (60000 * 1.0));
                if (lowToHighBrightness)
                    binding.transparentImage.setAlpha((float) progress);
            }

            @Override
            public void onFinish() {
                if (!countDownTimerStop) {
                    lowToHighBrightness = false;
                    highToLowBrightness = true;
                    if (highToLowBrightnessCountDownTimer != null)
                        highToLowBrightnessCountDownTimer.start();
                } /*else {
                    binding.transparentImage.setAlpha((float) 0.0);
                }*/
            }
        };
    }

    private void initHightToLowBrightnessCountDownTimer() {
        highToLowBrightnessCountDownTimer = new CountDownTimer(60000, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                double progress = ((millisUntilFinished * 1.0) / (60000 * 1.0));
                if (highToLowBrightness)
                    binding.transparentImage.setAlpha((float) (1 - progress));

            }

            @Override
            public void onFinish() {
                if (!countDownTimerStop) {
                    lowToHighBrightness = true;
                    highToLowBrightness = false;
                    if (lowToHighBrightnessCountDownTimer != null)
                        lowToHighBrightnessCountDownTimer.start();
                } /*else {
                    binding.transparentImage.setAlpha((float) 0.0);
                }*/
            }
        };
    }

    private void setSettingAdapter() {
        settingViewPagerAdapter = new SettingViewPagerAdapter(getSupportFragmentManager());
        binding.settingViewPager.setAdapter(settingViewPagerAdapter);
        binding.settingTablayout.setupWithViewPager(binding.settingViewPager);
        applyFont();
    }

    private void applyFont() {
        for (int i = 0; i < binding.settingTablayout.getTabCount(); i++) {
            //noinspection ConstantConditions
            TextView tv = (TextView) LayoutInflater.from(this).inflate(R.layout.setting_custom_tab_layout, null);
//            tv.setTypeface(Typeface);
            binding.settingTablayout.getTabAt(i).setCustomView(tv);

        }
    }

    private void checkResolution() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        height = size.y;
    }


    private void setAdapter() {
        switch (categoryName) {
            case "Brain":
                createFragmentList(AppConstant.brainCategory);
                break;
            case "Body":
                createFragmentList(AppConstant.bodyCategory);
                break;
            case "Sleep":
                createFragmentList(AppConstant.sleepCategory);
                break;
            case "Spirit":
                createFragmentList(AppConstant.spiritCategory);
                break;
        }
        adapter = new CategoryViewPagerAdapter(getSupportFragmentManager(), arrayList, categoryName);
        binding.categoryViewpager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.categoryViewpager);
    }


    private void createFragmentList(String[] categoryNameArray) {
        arrayList = new ArrayList<>();
        for (int i = 0; i < categoryNameArray.length; i++) {
            subCategoryFragment = new SubCategoryFragment();
            arrayList.add(subCategoryFragment);
        }


    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.expand_btn:
                binding.settingIcon.setClickable(false);
                AnimationHelper.getInstance().layoutBottomToTop(height, binding.soundLayout, binding.settingLayout, binding.settingIcon);
                binding.expandBtn.setVisibility(View.GONE);
//                binding.settingLayout.setVisibility(View.GONE);
                settingIsOpen = false;
                break;
            /*case R.id.time_layout:
                binding.backgroundMusicList.setVisibility(View.GONE);
                if (!isTimerClick) {
                    isTimerClick = true;
                    binding.timerLessExpendBtn.setImageResource(R.drawable.ic_less_timer);
                    AnimationHelper.getInstance().buttonPullDown(height, binding.soundLayout);
                    binding.timeDetailLayout.setVisibility(View.VISIBLE);
                } else {
                    isTimerClick = false;
                    binding.timerLessExpendBtn.setImageResource(R.drawable.ic_expand_time);
                    AnimationHelper.getInstance().buttonPullUp(height, binding.soundLayout);
                    binding.timeDetailLayout.setVisibility(View.GONE);
                }
                break;*/
           /* case R.id.music_layout:
//                binding.timeDetailLayout.setVisibility(View.GONE);
                if (!isMusicClick) {
                    isMusicClick = true;
                    binding.musicLessExpendBtn.setImageResource(R.drawable.ic_less_timer);
                    AnimationHelper.getInstance().buttonPullDown(height, binding.soundLayout);
                    binding.backgroundMusicList.setVisibility(View.VISIBLE);
                } else {
                    isMusicClick = false;
                    binding.musicLessExpendBtn.setImageResource(R.drawable.ic_expand_time);
                    AnimationHelper.getInstance().buttonPullUp(height, binding.soundLayout);
                    binding.backgroundMusicList.setVisibility(View.GONE);
                }
                break;*/
            case R.id.back_press_icon:
                onBackPressed();
                break;
            case R.id.setting_icon:
                if (!settingBtnClick) {
                    if (!SharedPreferenceHelper.getInstance().getBooleanValue(AppConstant.LIST_DEMO, false)) {
                        binding.settingIcon.clearAnimation();
                    }
                    settingBtnClick = true;
                    settingIsOpen = true;
                    binding.settingLayout.setVisibility(View.VISIBLE);
                    binding.expandBtn.setVisibility(View.VISIBLE);
                    binding.settingIcon.clearAnimation();
                    AnimationHelper.getInstance().layoutTopToBottom(height, binding.soundLayout);
                    if (!SharedPreferenceHelper.getInstance().getBooleanValue(AppConstant.LIST_DEMO, false)) {
                        if (settingSwapShowCaseDemo != null) {
                            settingSwapShowCaseDemo.buttonClick();
                        }
                    }
                } else {
                    settingBtnClick = false;
                    binding.settingIcon.setClickable(false);
                    AnimationHelper.getInstance().layoutBottomToTop(height, binding.soundLayout, binding.settingLayout, binding.settingIcon);
                    binding.expandBtn.setVisibility(View.GONE);
//                binding.settingLayout.setVisibility(View.GONE);
                    settingIsOpen = false;
                }

                break;

        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setBackgroundImage(position);
        currentPosition = position;

    }

    private void setBackgroundImage(int position) {
        switch (categoryName) {
            case "Brain":
                binding.soundLayout.setBackgroundResource(AppConstant.brainBgImage[position]);
                break;

            case "Body":
                binding.soundLayout.setBackgroundResource(AppConstant.bodyBgImage[position]);
                break;
            case "Sleep":
                binding.soundLayout.setBackgroundResource(AppConstant.sleepBgImage[position]);
                break;
            case "Spirit":
                binding.soundLayout.setBackgroundResource(AppConstant.spiritBgImage[position]);
                break;
        }
    }


    @Override
    public void onPageScrollStateChanged(int state) {

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
    public void onMessageEvent(EventBusModel eventBusModel) {
        if (eventBusModel.isPlaying()) {
//            binding.transparentImage.setAlpha((float) 0.0);
            countDownTimerStop = false;
            highToLowBrightness = true;
            lowToHighBrightness = false;
            if (highToLowBrightnessCountDownTimer != null)
                highToLowBrightnessCountDownTimer.start();
        } else {

            countDownTimerStop = true;
            highToLowBrightness = false;
            lowToHighBrightness = false;
            highToLowBrightnessCountDownTimer.onFinish();
            lowToHighBrightnessCountDownTimer.onFinish();
            binding.transparentImage.setAlpha((float) 0.0);

        }
        for (int i = 0; i < 3; i++) {
            if (i != currentPosition) {
                if (arrayList.get(i).countDownTimer != null) {
                    arrayList.get(i).countDownTimer.cancel();
                    arrayList.get(i).countDownTimer.onFinish();
                }
                Log.d("talal", "onMessageEvent: " + currentPosition);
            }

        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(TimeSaveEventModel eventBusModel) {
        binding.settingIcon.setClickable(false);
        AnimationHelper.getInstance().layoutBottomToTop(height, binding.soundLayout, binding.settingLayout, binding.settingIcon);
        binding.expandBtn.setVisibility(View.GONE);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SetAlphaValueModel eventBusModel) {
        binding.transparentImage.setAlpha(eventBusModel.getValue());

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            // when user first touches the screen we get x and y coordinate
            case MotionEvent.ACTION_DOWN: {
                x1 = event.getX();
                y1 = event.getY();
                break;
            }
            case MotionEvent.ACTION_UP: {
                x2 = event.getX();
                y2 = event.getY();
                if (y1 > y2) {
                    if (settingIsOpen) {
                        binding.settingIcon.setClickable(false);
                        AnimationHelper.getInstance().layoutBottomToTop(height, binding.soundLayout, binding.settingLayout, binding.settingIcon);
                        binding.expandBtn.setVisibility(View.GONE);
//                        binding.settingLayout.setVisibility(View.GONE);
                        settingIsOpen = false;
                    }
                }

                break;
            }
        }

        return false;
    }


    private void destoryService() {
        Intent intent = new Intent(this, AudioService.class);
        intent.putExtra("frequency", 0);
        intent.putExtra("beat", 0);
        intent.putExtra("status", false);
        stopService(intent);
    }

    @Override
    public void onBackPressed() {
        destoryService();
        stopService(new Intent(this, BgMusicService.class));
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, BgMusicService.class));
        countDownTimerStop = true;
        highToLowBrightnessCountDownTimer.onFinish();
        lowToHighBrightnessCountDownTimer.onFinish();
        super.onDestroy();


    }
}

