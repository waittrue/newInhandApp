package com.inhand.milk.activity;

import android.app.Fragment;
import android.os.Bundle;

import com.inhand.milk.fragment.milk_amount_curve.milkAmountCurve;

/**
 * Created by Administrator on 2015/6/30.
 */
public class MilkAmountCurveActivity extends SubActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment initFragment() {
        // TODO Auto-generated method stub
        Fragment mFragment = new milkAmountCurve();// new Nutrition();
        return mFragment;
    }


}
