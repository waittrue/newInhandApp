package com.inhand.milk.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;

/**
 * Created by Administrator on 2015/7/27.
 */
public class AlarmFloatActivity extends AlarmActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.i("alarmR", "onPause");
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("alarmR", "onStop");
        finish();
    }
    @Override
    public void finish() {
        super.finish();
    }

}
