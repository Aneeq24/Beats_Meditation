package com.las.atmosherebinauraltherapy.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by RanaTalal on 9/22/2017.
 */

public class SharedPreferenceHelper {
    public static SharedPreferenceHelper sharedPreferenceHelper;
    SharedPreferences sharedPreferences;


    public SharedPreferenceHelper() {

        sharedPreferences = AppConstant.CONTEXT.getSharedPreferences(AppConstant.FILE_NAME, Context.MODE_PRIVATE);
    }

    public static SharedPreferenceHelper getInstance() {
        if (sharedPreferenceHelper == null) {
            sharedPreferenceHelper = new SharedPreferenceHelper();
        }
        return sharedPreferenceHelper;
    }

    public void setIntegerValue(String key, int value) {
        sharedPreferences.edit().putInt(key, value).apply();
    }

    public int getIntegerValue(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public void setBooleanValue(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public boolean getBooleanValue(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public void setStringValue(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    public String getStringValue(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }



}
