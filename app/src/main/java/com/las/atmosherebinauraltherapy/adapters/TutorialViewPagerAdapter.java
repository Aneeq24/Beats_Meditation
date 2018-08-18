package com.las.atmosherebinauraltherapy.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.las.atmosherebinauraltherapy.views.fragments.TutorialFragment;

import java.util.ArrayList;

/**
 * Created by RanaTalal on 05/04/2018.
 */

public class TutorialViewPagerAdapter extends FragmentStatePagerAdapter {
    ArrayList<TutorialFragment> arrayList;

    public TutorialViewPagerAdapter(FragmentManager fm, ArrayList<TutorialFragment> arrayList) {
        super(fm);
        this.arrayList = arrayList;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle= new Bundle();
        bundle.putInt("position",position);
        arrayList.get(position).setArguments(bundle);
        return arrayList.get(position);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }
}
