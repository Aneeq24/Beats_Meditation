package com.las.atmosherebinauraltherapy.adapters;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.las.atmosherebinauraltherapy.R;
import com.las.atmosherebinauraltherapy.databinding.BgMusicViewholderBinding;
import com.las.atmosherebinauraltherapy.helper.AppConstant;
import com.las.atmosherebinauraltherapy.helper.SharedPreferenceHelper;
import com.las.atmosherebinauraltherapy.model.BgMusicModel;
import com.las.atmosherebinauraltherapy.services.BgMusicService;

import java.util.ArrayList;

/**
 * Created by RanaTalal on 4/25/2018.
 */

public class BgMusicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<BgMusicModel> arrayList;
    Context context;
    int selectedPosition;

    public BgMusicAdapter(ArrayList<BgMusicModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
        selectedPosition = SharedPreferenceHelper.getInstance().getIntegerValue(AppConstant.BG_MUSIC_POSITION, 0);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BgMusicViewholderBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.bg_music_viewholder, parent, false);
        binding.setActivity(this);
        return new BgMusicViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BgMusicViewHolder viewHolder = (BgMusicViewHolder) holder;
        viewHolder.binding.setModel(arrayList.get(position));
        viewHolder.binding.setPosition(position);
        viewHolder.binding.executePendingBindings();
        viewHolder.binding.musicBtnSelector.setChecked(selectedPosition == position);

    }

    public void onClick(int position) {
        selectedPosition = position;
        SharedPreferenceHelper.getInstance().setStringValue(AppConstant.BG_MUSIC, arrayList.get(position).getName());
        SharedPreferenceHelper.getInstance().setIntegerValue(AppConstant.BG_MUSIC_POSITION, position);
        notifyDataSetChanged();
        context.startService(new Intent(context, BgMusicService.class));

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class BgMusicViewHolder extends RecyclerView.ViewHolder {
        BgMusicViewholderBinding binding;

        public BgMusicViewHolder(BgMusicViewholderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
