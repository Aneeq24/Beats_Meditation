package com.las.atmosherebinauraltherapy.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.las.atmosherebinauraltherapy.helper.AppConstant;
import com.las.atmosherebinauraltherapy.views.fragments.CategoryFragment;
import com.las.atmosherebinauraltherapy.views.fragments.SubCategoryFragment;

import java.util.ArrayList;

import binauralbeats.Binaural;

/**
 * Created by RanaTalal on 4/21/2018.
 */

public class CategoryViewPagerAdapter extends FragmentStatePagerAdapter {
    ArrayList<SubCategoryFragment> arrayList;
    String categoryName;
    float frequency, beat;
    Binaural binaural;

    public CategoryViewPagerAdapter(FragmentManager fm, ArrayList<SubCategoryFragment> arrayList, String categoryName) {
        super(fm);
        this.arrayList = arrayList;
        this.categoryName = categoryName;
    }


    @Override
    public Fragment getItem(int position) {

        Bundle bundle = new Bundle();

        bundle.putString("name", categoryName);
        bundle.putInt("position", position);

        arrayList.get(position).setArguments(bundle);
        return arrayList.get(position);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "1";
            case 1:
                return "2";
            case 2:
                return "3";

        }

       /* switch (categoryName) {
            case "Brain":
                return addTabs(AppConstant.brainCategory, position);

            case "Body":
                return addTabs(AppConstant.bodyCategory, position);

            case "Sleep":
                return addTabs(AppConstant.sleepCategory, position);

            case "Spirit":
                return addTabs(AppConstant.spiritCategory, position);

        }*/
        return super.getPageTitle(position);
    }

    private CharSequence addTabs(String[] categoryNameArray, int position) {
        return categoryNameArray[position];
    }


}
