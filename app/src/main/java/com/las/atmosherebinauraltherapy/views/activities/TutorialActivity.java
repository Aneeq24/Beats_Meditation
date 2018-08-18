package com.las.atmosherebinauraltherapy.views.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.las.atmosherebinauraltherapy.R;
import com.las.atmosherebinauraltherapy.databinding.ActivityTutorialBinding;
import com.las.atmosherebinauraltherapy.views.fragments.TutorialFragment;
import com.las.atmosherebinauraltherapy.adapters.TutorialViewPagerAdapter;

import java.util.ArrayList;

public class TutorialActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener/*, TabLayout.OnTabSelectedListener */ {
    ActivityTutorialBinding binding;
    ArrayList<TutorialFragment> arrayList;
    TutorialFragment tutorialFragment;
    TutorialViewPagerAdapter adapter;
    int currentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tutorial);
        binding.skipBtn.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        binding.setActivity(this);
        binding.viewPager.addOnPageChangeListener(this);
        setActivityTheme(R.drawable.brain_tutorial_bg, R.color.whiteColor, R.drawable.continue_btn_selector_white);
//        binding.tabLayout.addOnTabSelectedListener(this);
        setAdapter();
    }

    private void setAdapter() {
        arrayList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            tutorialFragment = new TutorialFragment();
            arrayList.add(tutorialFragment);
        }
        adapter = new TutorialViewPagerAdapter(getSupportFragmentManager(), arrayList);
        binding.viewPager.setAdapter(adapter);
        //  setupTabIcons();
        binding.tabLayout.setupWithViewPager(binding.viewPager);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ok_btn:
                if (currentPosition == 3) {
                    openApp();
                } else {
                    binding.viewPager.setCurrentItem(currentPosition + 1);
                }
                break;
            case R.id.skip_btn:
                openApp();
                break;
        }
    }

    private void openApp() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        currentPosition = position;
        switch (position) {
            case 0:
                setActivityTheme(R.drawable.brain_tutorial_bg, R.color.whiteColor, R.drawable.continue_btn_selector_white);
                break;
            case 1:
                setActivityTheme(R.drawable.body_tutorial_bg, R.color.whiteColor, R.drawable.continue_btn_selector_white);
                break;
            case 2:
                setActivityTheme(R.drawable.sleep_tutorial_bg, R.color.whiteColor, R.drawable.continue_btn_selector_white);
                break;
            case 3:
                setActivityTheme(R.drawable.spirirt_tutorial_bg, R.color.blackColor, R.drawable.continus_btn_selector_black);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void setActivityTheme(int tutorial_bg, int skipBtnColor, int okBtnColor) {

        binding.mainContainer.setBackgroundResource(tutorial_bg);
        binding.skipBtn.setTextColor(getResources().getColor(skipBtnColor));
                binding.okBtn.setImageResource(okBtnColor);

    }

}
