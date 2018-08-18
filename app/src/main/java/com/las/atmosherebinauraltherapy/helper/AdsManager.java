package com.las.atmosherebinauraltherapy.helper;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.las.atmosherebinauraltherapy.R;

/**
 * Created by RanaTalal on 23-Dec-17.
 */

public class AdsManager {
    private static final String TAG = "AdsManager";
    public static AdsManager adsManager;
    private InterstitialAd interstitial;
    private Context context;

    private AdsManager(Context context) {
        this.context =context ;
        interstitial = new InterstitialAd(context);
        interstitial.setAdUnitId(context.getString(R.string.interstial_ad_id));
        loadInterstitial();
    }

    public static AdsManager getInstance(Context context) {
        if (adsManager == null) {
            adsManager = new AdsManager(context);
        }
        return adsManager;
    }


    private AdRequest appendUserConsent() {
        AdRequest adRequest = null;
        if (SharedPreferenceHelper.getInstance().getBooleanValue(context.getString(R.string.npa),false)) {
            Bundle bundle = new Bundle();
            bundle.putString(context.getString(R.string.npa), "1");
            Log.d(TAG, "consent status: npa");
            adRequest = new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, bundle).build();
        } else {
            Log.d(TAG, "consent status: pa");
            adRequest = new AdRequest.Builder().build();
        }
        return adRequest;
    }




    private void loadInterstitial() {
        interstitial.loadAd(appendUserConsent());
        interstitial.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                // if interstitial ad failed to load, then reload it after 5 seconds
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // after the ad has been shown, load interstitial again and cache it
                        loadInterstitial();
                    }
                }, 10000);
                Log.e("interstitialAd", "onInterstitialAdFailedToLoad " + String.valueOf(i));
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.e("interstitialAd", "onInterstitialAdLoaded");
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                // after the ad has been closed, load interstitial again and cache it
                loadInterstitial();
            }
        });
    }

    public void showInterstitial() {
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }




    public void createAndShowBanner(final AdView adView) {
        AdRequest adRequest =appendUserConsent();
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                adView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                adView.setVisibility(View.GONE);
                super.onAdFailedToLoad(i);
            }
        });
    }


}
