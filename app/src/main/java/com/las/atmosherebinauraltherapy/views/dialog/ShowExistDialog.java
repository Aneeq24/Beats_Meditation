package com.las.atmosherebinauraltherapy.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.las.atmosherebinauraltherapy.R;
import com.las.atmosherebinauraltherapy.databinding.ExitDailogBinding;
import com.las.atmosherebinauraltherapy.helper.AdsManager;
import com.las.atmosherebinauraltherapy.helper.UtilClass;
import com.las.atmosherebinauraltherapy.interfaces.CloseAppListener;


/**
 * Created by saqibmirza on 22/12/2016.
 */

public class ShowExistDialog {

    Dialog dialog;
    Context context;



    CloseAppListener closeAppListener;


    public ShowExistDialog(Context context) {
        this.context = context;
        closeAppListener = (CloseAppListener) context;
    }

    public void createDialog() {
        LayoutInflater myInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ExitDailogBinding binding = DataBindingUtil.inflate(myInflator, R.layout.exit_dailog, null, false);


        if (Build.VERSION.SDK_INT >= 21) {
            dialog = new Dialog(context, android.R.style.Theme_Material_Light_Dialog_NoActionBar);
        } else {
            dialog = new Dialog(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
        }

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(binding.getRoot());
        binding.setActivity(this);


        AdsManager.getInstance(context).createAndShowBanner(binding.adView);

    }

    public void showDialog() {

        dialog.show();
    }

    public void hideDialog() {
        dialog.hide();
    }

    public void setCancelable(boolean isCancelable) {
        dialog.setCancelable(isCancelable);
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_rate_us:
                dialog.dismiss();
                UtilClass.getInstaance().likeUs(context);
                break;
            case R.id.dialog_no:
                dialog.dismiss();
                break;
            case R.id.dialog_yes:

                if (closeAppListener != null) {
                    dialog.dismiss();
                    dialog.cancel();
                    closeAppListener.closeApp();
                } else
                    dialog.dismiss();
                break;
        }
    }
}
