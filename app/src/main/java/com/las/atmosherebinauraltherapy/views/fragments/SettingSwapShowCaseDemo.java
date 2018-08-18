package com.las.atmosherebinauraltherapy.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.las.atmosherebinauraltherapy.R;
import com.las.atmosherebinauraltherapy.helper.AppConstant;
import com.las.atmosherebinauraltherapy.helper.SharedPreferenceHelper;


public class SettingSwapShowCaseDemo extends Fragment {

    Button button;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_swap_show_case_demo, container, false);
        ConstraintLayout relativeLayout = (ConstraintLayout) view.findViewById(R.id.swap_demo_parent);
        relativeLayout.setOnClickListener(null);

        return view;
    }

    public void buttonClick(){
        SharedPreferenceHelper.getInstance().setBooleanValue(AppConstant.LIST_DEMO, true);
        getActivity().getSupportFragmentManager().beginTransaction().remove(SettingSwapShowCaseDemo.this).commit();
    }

}
