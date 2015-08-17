package com.inhand.milk.activity;

import android.app.Fragment;
import android.os.Bundle;

import com.inhand.milk.fragment.person_center.choose_milk.ChooseMilkPhaseFragment;

/**
 * Created by Administrator on 2015/8/10.
 */
public class MilkChooseActivity extends SubActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment initFragment() {
        // TODO Auto-generated method stub
        Fragment mFragment = new ChooseMilkPhaseFragment();// new Nutrition();
        return mFragment;
    }

}
