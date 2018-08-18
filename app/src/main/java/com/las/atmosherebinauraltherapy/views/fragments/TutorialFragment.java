package com.las.atmosherebinauraltherapy.views.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.las.atmosherebinauraltherapy.R;
import com.las.atmosherebinauraltherapy.databinding.TutorialFragmentBinding;


public class TutorialFragment extends Fragment {

    TutorialFragmentBinding binding;
    int position;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.tutorial_fragment, container, false);
        position = getArguments().getInt("position");


        switch (position) {
            case 0:
                setData("BRAIN", R.color.whiteColor, R.drawable.brain_tut_icon, R.drawable.meditation_logo);
                break;
            case 1:
                setData("BODY", R.color.whiteColor, R.drawable.body_tut_icon, R.drawable.meditation_logo);
                break;
            case 2:
                setData("SLEEP", R.color.whiteColor, R.drawable.sleep_tut_icon, R.drawable.meditation_logo);
                break;
            case 3:
                setData("SPIRIT", R.color.blackColor, R.drawable.spirit_tut_icon, R.drawable.meditation_logo_black);
                break;


        }
        return binding.getRoot();
    }

    private void setData(String name, int nameColor, int icon, int logo) {
        binding.title.setText(name);
        binding.title.setTextColor(getResources().getColor(nameColor));
        binding.icon.setImageResource(icon);
        binding.tutLogo.setImageResource(logo);
    }
}
