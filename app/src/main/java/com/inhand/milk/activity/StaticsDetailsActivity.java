package com.inhand.milk.activity;

import android.app.Fragment;
import android.os.Bundle;

import com.inhand.milk.fragment.temperature_amount.details.DetailsFragment;

public class StaticsDetailsActivity extends SubActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment initFragment() {
        // TODO Auto-generated method stub
        Fragment mFragment = new DetailsFragment();
        if (getIntent().getExtras().getBoolean("isTemperature"))
            ((DetailsFragment) mFragment).setTemperature(true);
        else
            ((DetailsFragment) mFragment).setTemperature(false);
        return mFragment;
    }


}
