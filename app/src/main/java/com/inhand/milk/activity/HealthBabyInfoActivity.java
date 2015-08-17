package com.inhand.milk.activity;

import android.app.Fragment;
import android.os.Bundle;

import com.inhand.milk.fragment.health.babyinfo.BabyInfoFragment;

public class HealthBabyInfoActivity extends SubActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment initFragment() {
        // TODO Auto-generated method stub
        Fragment fragment = new BabyInfoFragment();
        return fragment;
    }

}
