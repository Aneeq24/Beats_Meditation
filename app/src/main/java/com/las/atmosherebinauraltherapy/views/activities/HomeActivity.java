package com.las.atmosherebinauraltherapy.views.activities;

import android.content.Intent;
import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.ads.consent.ConsentForm;
import com.google.ads.consent.ConsentFormListener;
import com.google.ads.consent.ConsentInfoUpdateListener;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.ads.consent.DebugGeography;
import com.las.atmosherebinauraltherapy.BuildConfig;
import com.las.atmosherebinauraltherapy.R;
import com.las.atmosherebinauraltherapy.databinding.ActivityHomeBinding;
import com.las.atmosherebinauraltherapy.helper.AdsManager;
import com.las.atmosherebinauraltherapy.helper.SharedPreferenceHelper;
import com.las.atmosherebinauraltherapy.helper.UtilClass;
import com.las.atmosherebinauraltherapy.interfaces.CloseAppListener;
import com.las.atmosherebinauraltherapy.model.CloseAppBussEvent;
import com.las.atmosherebinauraltherapy.model.EventBusModel;
import com.las.atmosherebinauraltherapy.services.BgMusicService;
import com.las.atmosherebinauraltherapy.views.dialog.ShowExistDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.MalformedURLException;
import java.net.URL;


@BindingMethods({
        @BindingMethod(
                type = NavigationView.class,
                attribute = "app:onNavigationItemSelected",
                method = "setNavigationItemSelectedListener"
        ),
})
public class HomeActivity extends AppCompatActivity implements CloseAppListener {

    ActivityHomeBinding binding;
    ShowExistDialog showExistDialog;
    ActionBarDrawerToggle drawerToggle;
    private static final String TAG = "HomeActivity";
    ConsentForm form;
    ConsentInformation consentInformation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        binding.setActivity(this);
        showExistDialog = new ShowExistDialog(this);
        showExistDialog.createDialog();

        consentInformation = ConsentInformation.getInstance(this);
        if (!SharedPreferenceHelper.getInstance().getBooleanValue(getString(R.string.is_consent_first_time_requested), false)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                requestGoogleConsentForm();
        }

    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.rate_us:
                UtilClass.getInstaance().likeUs(this);
                break;
            case R.id.share_app:
                String text = "https://play.google.com/store/apps/details?id=" + getPackageName();
                UtilClass.getInstaance().shareApp(text, this);
                break;
            case R.id.more_apps:
                UtilClass.getInstaance().moreApps(this);
                break;
            case R.id.about_me_icon:
                Intent intent = new Intent(this, AboutMe.class);
                startActivity(intent);
                break;
            case R.id.privacy_policy_icon:
                consentInformation.setConsentStatus(ConsentStatus.UNKNOWN);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    requestGoogleConsentForm();
                break;
        }
        binding.drawerLayout.closeDrawer(binding.navigationView);
        return false;

    }

    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.first_layout:
                openMusicActivity("Sleep");
                AdsManager.getInstance(this).showInterstitial();
                break;
            case R.id.second_layout:
                openMusicActivity("Body");
                AdsManager.getInstance(this).showInterstitial();
                break;
            case R.id.third_layout:
                openMusicActivity("Brain");
                AdsManager.getInstance(this).showInterstitial();
                break;
            case R.id.four_layout:
                openMusicActivity("Spirit");
                AdsManager.getInstance(this).showInterstitial();
                break;
            case R.id.drawer_icon_layout:
                binding.drawerLayout.openDrawer(binding.navigationView);
                break;
        }

    }

    private void openMusicActivity(String categoryName) {
        Intent intent = new Intent(this, MusicPlayerActivity.class);
        intent.putExtra("categoryName", categoryName);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(binding.navigationView)) {
            binding.drawerLayout.closeDrawer(binding.navigationView);
        } else {
            showExistDialog.showDialog();
        }

    }

    @Override
    protected void onDestroy() {
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
    public void onMessageEvent(CloseAppBussEvent eventBusModel) {
        finish();
        Log.d("ozi", "onMessageEvent: ");

    }

    @Override
    public void closeApp() {
        finish();
    }


    private void requestGoogleConsentForm() {
        if (BuildConfig.DEBUG) {
            consentInformation.setDebugGeography(DebugGeography.DEBUG_GEOGRAPHY_EEA);
            consentInformation.addTestDevice("DDB154F9DB3D129FD13065AECD1F801F");
        }
        consentInformation.requestConsentInfoUpdate(new String[]{getString(R.string.publisher_id)}, new ConsentInfoUpdateListener() {
            @Override
            public void onConsentInfoUpdated(ConsentStatus consentStatus) {
                Log.d(TAG, consentStatus.name());
                // User's consent status successfully updated
                // check where user is located either not in EEA or in EEA
                // case 1 (not in EEA): if user is not located in EEA, no consent is required, you can request to Google Mobile Ads SDK
                // case 2 (in EEA): consent is required
                if (consentInformation.isRequestLocationInEeaOrUnknown()) {
                    // user is located in EEA, consent is required
                    if (consentStatus == ConsentStatus.UNKNOWN) {
                        showGoogleConsentForm();
                    }
                } else {
                    if (SharedPreferenceHelper.getInstance().getBooleanValue(getString(R.string.is_consent_first_time_requested), false)) {
                        showPrivacyPolicy();
                    }

                }
                SharedPreferenceHelper.getInstance().setBooleanValue(getString(R.string.is_consent_first_time_requested), true);
            }

            @Override
            public void onFailedToUpdateConsentInfo(String errorDescription) {
                // User's consent status failed to update
                Log.d(TAG, "onFailedToUpdateConsentInfo: " + errorDescription);
            }
        });
    }

    private void showGoogleConsentForm() {
        // consent not provided, collect consent using Google-rendered consent form
        URL privacyUrl = null;
        try {
            privacyUrl = new URL(getString(R.string.privacy_policy_url));
        } catch (MalformedURLException e) {
            Log.e(TAG, "onConsentInfoUpdated: " + e.getLocalizedMessage());
        }
        form = new ConsentForm.Builder(HomeActivity.this, privacyUrl)
                .withPersonalizedAdsOption()
                .withNonPersonalizedAdsOption()
                .withListener(new ConsentFormListener() {
                    @Override
                    public void onConsentFormLoaded() {
                        // Consent form loaded successfully.
                        Log.d(TAG, "onConsentFormLoaded");
                        form.show();
                    }

                    @Override
                    public void onConsentFormOpened() {
                        // Consent form was displayed.
                        Log.d(TAG, "onConsentFormOpened");
                    }

                    @Override
                    public void onConsentFormClosed(ConsentStatus consentStatus, Boolean userPrefersAdFree) {
                        // Consent form was closed.
                        Log.d(TAG, "onConsentFormClosed");
                        if (consentStatus == ConsentStatus.PERSONALIZED) {
                            SharedPreferenceHelper.getInstance().setBooleanValue(getString(R.string.npa), false);
                        } else if (consentStatus == ConsentStatus.NON_PERSONALIZED) {
                            SharedPreferenceHelper.getInstance().setBooleanValue(getString(R.string.npa), true);
                        }
                    }

                    @Override
                    public void onConsentFormError(String errorDescription) {
                        // Consent form error.
                        Log.d(TAG, "onConsentFormError: " + errorDescription);
                    }
                })
                .build();
        form.load();
    }

    private void showPrivacyPolicy() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://s3-us-west-2.amazonaws.com/localappsstore/PrivacyPolicy.htm"));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        startActivity(intent);
    }


}
