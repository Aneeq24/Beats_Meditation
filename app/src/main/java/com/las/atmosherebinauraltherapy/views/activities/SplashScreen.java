package com.las.atmosherebinauraltherapy.views.activities;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.las.atmosherebinauraltherapy.R;
import com.las.atmosherebinauraltherapy.helper.AppConstant;
import com.las.atmosherebinauraltherapy.helper.SharedPreferenceHelper;

public class SplashScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        startSplash();
    }

    private void startSplash() {
        new CountDownTimer(2000, 10) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (!SharedPreferenceHelper.getInstance().getBooleanValue(AppConstant.TUTORIAL_SCREEN, false)) {
                    SharedPreferenceHelper.getInstance().setBooleanValue(AppConstant.TUTORIAL_SCREEN, true);
                    Intent intent = new Intent(SplashScreen.this, TutorialActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashScreen.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }.start();
    }
}
