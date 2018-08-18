package com.las.atmosherebinauraltherapy.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.las.atmosherebinauraltherapy.R;
import com.las.atmosherebinauraltherapy.databinding.TimeViewHolderBinding;
import com.las.atmosherebinauraltherapy.helper.AppConstant;
import com.las.atmosherebinauraltherapy.helper.SharedPreferenceHelper;
import com.las.atmosherebinauraltherapy.interfaces.TimeUpdateListener;
import com.las.atmosherebinauraltherapy.model.TimeModel;

import java.util.ArrayList;

/**
 * Created by RanaTalal on 4/25/2018.
 */

public class TimeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<TimeModel> arrayList;
    Context context;
    String categoryType;
    TimeUpdateListener listener;

    public TimeAdapter(ArrayList<TimeModel> arrayList, Context context, String categoryType) {
        this.arrayList = arrayList;
        this.context = context;
        this.categoryType = categoryType;
        listener = (TimeUpdateListener) context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TimeViewHolderBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.time_view_holder, parent, false);
        return new TimeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TimeViewHolder viewHolder = (TimeViewHolder) holder;
        viewHolder.binding.setModel(arrayList.get(position));
        viewHolder.binding.executePendingBindings();
        if (categoryType.equals("hour")) {
            listener.updateHour(arrayList.get(position).getTime());
        } else {
        }
        SharedPreferenceHelper.getInstance().setStringValue(AppConstant.MINUTES, arrayList.get(position).getTime());
        Log.d("talal", "onBindViewHolder: " + arrayList.get(position).getTime());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class TimeViewHolder extends RecyclerView.ViewHolder {
        TimeViewHolderBinding binding;

        public TimeViewHolder(TimeViewHolderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
