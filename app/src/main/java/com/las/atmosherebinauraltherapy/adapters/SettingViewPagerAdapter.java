package com.las.atmosherebinauraltherapy.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.las.atmosherebinauraltherapy.views.fragments.MusicSettingFragment;
import com.las.atmosherebinauraltherapy.views.fragments.TimerSettingFragment;
import com.las.atmosherebinauraltherapy.views.fragments.VolumeSettingFragment;

/**
 * Created by RanaTalal on 5/2/2018.
 */

public class SettingViewPagerAdapter extends FragmentStatePagerAdapter {
    public SettingViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new TimerSettingFragment();
            case 1:
                return new VolumeSettingFragment();
            case 2:
                return new MusicSettingFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Timer";
            case 1:
                return "Volume";
            case 2:
                return "Music";
        }
        return super.getPageTitle(position);
    }
}
