package com.inhand.milk.activity;

import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;

import com.inhand.milk.fragment.BackHandleFragment;

/**
 * BaseActivity
 * Desc:基Activity
 * Team: InHand
 * User:Wooxxx
 * Date: 2015-03-03
 * Time: 12:45
 */
public class BaseActivity extends FragmentActivity
        implements BackHandleFragment.BackHandledInterface {

    // 监听返回键的Fragment对象
    protected BackHandleFragment backHandledFragment;

    @Override
    public void setSelectedFragment(BackHandleFragment selectedFragment) {
        this.backHandledFragment = selectedFragment;
    }

    @Override
    public void onBackPressed() {
        /*
        *如果不存在BackHandledFragment对象或者该对象重写的onBackPressed方法
        则执行默认的返回键逻辑
         */
        if (backHandledFragment == null || !backHandledFragment.onBackPressed()) {
            if (getFragmentManager().getBackStackEntryCount() == 0)
                super.onBackPressed();
            else
                getSupportFragmentManager().popBackStack();
        }
    }
    public int getWindowWidth(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

}
