package com.inhand.milk.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * BaseFragment
 * Desc:Fragment基类，用于做一些初始化操作
 * Team: InHand
 * User:Wooxxx
 * Date: 2015-03-03
 * Time: 16:54
 */
public class BaseFragment extends Fragment {
    protected View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initViews();
        setListeners();
        return rootView;
    }

    /**
     * 初始化各个组件
     */
    public void initViews() {

    }


    /**
     * 设置部分组件的监听
     */
    public void setListeners() {

    }
}
