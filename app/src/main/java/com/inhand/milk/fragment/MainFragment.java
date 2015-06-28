package com.inhand.milk.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inhand.milk.activity.MainActivity;

/**
 * MainFragment
 * Desc: 主窗口的Fragment基类
 * Team: InHand
 * User:Wooxxx
 * Date: 2015-03-17
 * Time: 15:13
 */
public class MainFragment extends BaseFragment {
    protected int titleId;

    public int getTitleId() {
        return titleId;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    //当宿主activity创建之后，设置标题
    @Override
    public void onResume() {
        String title = getResources().getString(getTitleId());
        //((MainActivity) getActivity()).getHeader().setTitle(title);
        super.onResume();
    }
}
