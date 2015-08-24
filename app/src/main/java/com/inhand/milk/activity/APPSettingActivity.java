package com.inhand.milk.activity;

import android.app.Fragment;

import com.inhand.milk.fragment.person_center.app_setting.AppSettingContainer;

/**
 * Created by Administrator on 2015/8/24.
 * 作者：大力
 * 时间：2015/8/24
 * 描述：app 设置
 */
public class APPSettingActivity extends SubActivity {

    @Override
    protected Fragment initFragment() {
        return new AppSettingContainer();
    }
}
