package com.inhand.milk.activity;

import android.app.Fragment;
import android.os.Bundle;

import com.inhand.milk.fragment.health.last_drink.LastDrink;

/**
 * Created by Administrator on 2015/6/3.
 */
public class HealthDrinkLastActivity extends SubActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment initFragment() {
        // TODO Auto-generated method stub
        Fragment fragment = new LastDrink();
        return  fragment;
    }
}
