package com.inhand.milk.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inhand.milk.R;

/**
 * StatisticsFragment
 * Desc:健康数据Fragment
 * Team: InHand
 * User:Wooxxx
 * Date: 2015-03-17
 * Time: 15:22
 */
public class StatisticsFragment extends MainFragment {
    public StatisticsFragment() {
        this.titleId = R.string.fragment_title_statistics;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(
                R.layout.fragment_statistics,
                container,
                false
        );
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
