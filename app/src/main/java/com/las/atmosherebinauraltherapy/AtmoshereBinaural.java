package com.las.atmosherebinauraltherapy;

import android.app.Application;

import com.las.atmosherebinauraltherapy.helper.AppConstant;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 * Created by RanaTalal on 4/23/2018.
 */

public class AtmoshereBinaural extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        AppConstant.CONTEXT= getApplicationContext();
    }
}
