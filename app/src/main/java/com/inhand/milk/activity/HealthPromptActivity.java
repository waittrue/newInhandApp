package com.inhand.milk.activity;

import android.app.Fragment;
import android.os.Bundle;

import com.inhand.milk.fragment.milk_amount.MilkAmountFragment;

/**
 * Created by Administrator on 2015/5/7.
 */
public class HealthPromptActivity extends SubActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment initFragment() {
        // TODO Auto-generated method stub
       // Fragment mFragment = new HealthPromptFragment();
        Fragment mFragment = new MilkAmountFragment();
        //Fragment mFragment = new bluetoothFragment();
        return mFragment;
    }
}

