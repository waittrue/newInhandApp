package com.inhand.milk.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inhand.milk.R;

/**
 * VolumeFragment
 * Desc:奶量统计Fragment
 * Team: InHand
 * User:Wooxxx
 * Date: 2015-03-13
 * Time: 11:20
 */
public class VolumeFragment extends MainFragment {

    public VolumeFragment() {
        this.titleId = R.string.fragment_title_volume;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_volume,
                container,
                false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initViews() {
        super.initViews();
    }

    @Override
    public void setListeners() {
        super.setListeners();
    }


}
