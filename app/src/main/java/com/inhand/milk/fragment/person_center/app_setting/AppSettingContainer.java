package com.inhand.milk.fragment.person_center.app_setting;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inhand.milk.R;
import com.inhand.milk.fragment.TitleFragment;

/**
 * Created by Administrator on 2015/8/24.
 * 作者：大力
 * 时间：2015/8/24
 * 描述：这个主要涉及标题
 */
public class AppSettingContainer extends TitleFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_app_setting, container, false);
        setTitleview(getResources().getString(R.string.setting_title), 2);
        FragmentManager manager = getFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.add(R.id.app_setting_container, new AppSettingFragment());
        fragmentTransaction.commit();
        return mView;
    }
}
