package com.inhand.milk.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;

import com.inhand.milk.fragment.health.last_drink.LastDrink;

/**
 * Created by Administrator on 2015/6/3.
 */
public class HealthDrinkLastActivity extends SubActivity {
    private LastDrink fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment initFragment() {
        // TODO Auto-generated method stub
        fragment = new LastDrink();
        return  fragment;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fragment.start();
            }
        }, 200);
    }
}
