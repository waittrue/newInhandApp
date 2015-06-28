package com.inhand.milk.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inhand.milk.R;

/**
 * TemperatureFragment
 * Desc:温度统计Fragment
 * Team: InHand
 * User:Wooxxx
 * Date: 2015-03-17
 * Time: 08:27
 */
public class TemperatureFragment extends MainFragment {
    public TemperatureFragment() {
        this.titleId = R.string.fragment_title_temperature;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(
                R.layout.fragment_temperature,
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
