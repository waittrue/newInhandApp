package com.inhand.milk.utils;

import android.content.Context;

/**
 * Created by Administrator on 2015/7/6.
 */
public abstract class QuickListViewAdapter extends PinnerListViewAdapter {
    public QuickListViewAdapter(Context context) {
        super(context);
    }
    public abstract String getTitle(int position);
    public int getCount(){
        return mData.size();
    }
}
