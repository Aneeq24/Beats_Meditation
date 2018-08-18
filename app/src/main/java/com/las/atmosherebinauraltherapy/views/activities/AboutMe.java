package com.las.atmosherebinauraltherapy.views.activities;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;

import com.las.atmosherebinauraltherapy.R;
import com.las.atmosherebinauraltherapy.databinding.ActivityAboutMeBinding;
import com.las.atmosherebinauraltherapy.helper.AnimationHelper;
import com.las.atmosherebinauraltherapy.helper.AppConstant;
import com.las.atmosherebinauraltherapy.helper.SharedPreferenceHelper;

public class AboutMe extends AppCompatActivity {
    ActivityAboutMeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about_me);

        binding.aboutMeText.loadData(AppConstant.aboutMeText, "text/html", "UTF-8");
        binding.setActivity(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_icon_layout:
                onBackPressed();
                break;
        }
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }*/
}
