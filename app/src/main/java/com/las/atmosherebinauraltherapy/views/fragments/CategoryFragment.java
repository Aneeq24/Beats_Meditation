package com.las.atmosherebinauraltherapy.views.fragments;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.las.atmosherebinauraltherapy.R;
import com.las.atmosherebinauraltherapy.adapters.CategoryViewPagerAdapter;
import com.las.atmosherebinauraltherapy.databinding.CategoryFragmentBinding;
import com.las.atmosherebinauraltherapy.helper.AppConstant;

import java.util.ArrayList;

import binauralbeats.BeatsEngine;

public class CategoryFragment extends Fragment implements ViewPager.OnPageChangeListener {

    CategoryFragmentBinding binding;
    CategoryViewPagerAdapter adapter;
    SubCategoryFragment subCategoryFragment;
    String categoryName;
    ArrayList<SubCategoryFragment> arrayList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.category_fragment, container, false);
        categoryName = getArguments().getString("name");
//        creatTabs();
        setAdapter();
        binding.subCategoryViewPager.addOnPageChangeListener(this);
        return binding.getRoot();
    }

    private void setAdapter() {
        switch (categoryName) {
            case "Brain":
                createFragmentList(AppConstant.brainCategory);
                break;
            case "Body":
                createFragmentList(AppConstant.bodyCategory);
                break;
            case "Sleep":
                createFragmentList(AppConstant.sleepCategory);
                break;
            case "Spirit":
                createFragmentList(AppConstant.spiritCategory);
                break;
        }
        adapter = new CategoryViewPagerAdapter(getChildFragmentManager(), arrayList, categoryName);
        binding.subCategoryViewPager.setAdapter(adapter);
        binding.subCategoryTabLayout.setupWithViewPager(binding.subCategoryViewPager);
    }

    private void createFragmentList(String[] categoryNameArray) {
        arrayList = new ArrayList<>();
        for (int i = 0; i < categoryNameArray.length; i++) {
            subCategoryFragment = new SubCategoryFragment();
            arrayList.add(subCategoryFragment);
        }


    }

    private void creatTabs() {
        switch (categoryName) {
            case "Brain":
                addTabs(AppConstant.brainCategory);
                break;
            case "Body":
                addTabs(AppConstant.bodyCategory);
                break;
            case "Sleep":
                addTabs(AppConstant.sleepCategory);
                break;
            case "Spirit":
                addTabs(AppConstant.spiritCategory);
                break;
        }

    }

    private void addTabs(String[] categoryNameArray) {
        for (int i = 0; i < categoryNameArray.length; i++) {
            binding.subCategoryTabLayout.addTab(binding.subCategoryTabLayout.newTab().setText(categoryNameArray[i]).setIcon(R.drawable.ic_launcher_background));
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        Log.d("talal", "onPageSelected: " + position);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
