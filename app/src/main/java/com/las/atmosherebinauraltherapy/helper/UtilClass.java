package com.las.atmosherebinauraltherapy.helper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;


public class UtilClass {
    public static UtilClass utilClass;

    public static UtilClass getInstaance() {
        if (utilClass == null) {
            utilClass = new UtilClass();
        }
        return utilClass;
    }

    public void shareApp(String shareText, Context context) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, shareText);
        intent.setType("text/plain");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, "share"));
    }


    public void likeUs(Context context) {
        String url = "";
        try {
            //Check whether Google Play store is installed or not:
            context.getPackageManager().getPackageInfo("com.android.vending", 0);
            url = "market://details?id=" + context.getPackageName();
        } catch (final Exception e) {
            url = "https://play.google.com/store/apps/details?id=" + context.getPackageName();
        }

        //Open the app page in Google Play store:
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        context.startActivity(intent);
    }

    public void moreApps(Context context) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://developer?id=Local+App+Store")));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=Local+App+Store")));
        }
    }
}
