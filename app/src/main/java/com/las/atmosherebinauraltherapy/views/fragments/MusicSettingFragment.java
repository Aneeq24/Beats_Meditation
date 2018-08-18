package com.las.atmosherebinauraltherapy.views.fragments;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.las.atmosherebinauraltherapy.R;
import com.las.atmosherebinauraltherapy.adapters.BgMusicAdapter;
import com.las.atmosherebinauraltherapy.databinding.MusicSettingFragmentBinding;
import com.las.atmosherebinauraltherapy.helper.AppConstant;
import com.las.atmosherebinauraltherapy.helper.SharedPreferenceHelper;
import com.las.atmosherebinauraltherapy.model.BgMusicModel;

import java.util.ArrayList;

public class MusicSettingFragment extends Fragment {
    MusicSettingFragmentBinding binding;
    ArrayList<BgMusicModel> bgMusicModelArrayList;
    BgMusicAdapter bgMusicAdapter;
    BgMusicModel bgMusicModel;
    String[] bgMusicname = {"None", "Ambient", "Magic", "Rain", "Stones"};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.music_setting_fragment, container, false);

//        initHourAndMinute();
        SetBgMusicList();

        return binding.getRoot();
    }

    private void SetBgMusicList() {
        bgMusicModelArrayList = new ArrayList<>();
        for (int i = 0; i < bgMusicname.length; i++) {
            bgMusicModel = new BgMusicModel(bgMusicname[i]);
            bgMusicModelArrayList.add(bgMusicModel);
        }
        bgMusicAdapter = new BgMusicAdapter(bgMusicModelArrayList, getContext());
        binding.backgroundMusicList.setAdapter(bgMusicAdapter);

    }

    public void onClick(View view){}
}
