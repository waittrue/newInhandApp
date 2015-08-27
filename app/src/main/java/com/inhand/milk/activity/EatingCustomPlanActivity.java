package com.inhand.milk.activity;

import android.app.Fragment;
import android.os.Bundle;

import com.inhand.milk.entity.BabyFeedItem;
import com.inhand.milk.fragment.Eating.EatingCustomFragment;

/**
 * Created by Administrator on 2015/7/29.
 */
public class EatingCustomPlanActivity extends SubActivity {
    private BabyFeedItem babyFeedItem, addBabyFeedItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    public BabyFeedItem getBabyFeedItem() {
        return babyFeedItem;
    }

    public void setBabyFeedItem(BabyFeedItem babyFeedItem) {
        this.babyFeedItem = babyFeedItem;
    }

    public BabyFeedItem getAddBabyFeedItem() {
        return addBabyFeedItem;
    }

    public void setAddBabyFeedItem(BabyFeedItem addBabyFeedItem) {
        this.addBabyFeedItem = addBabyFeedItem;
    }

    @Override
    protected Fragment initFragment() {
        // TODO Auto-generated method stub
        Fragment mFragment = new EatingCustomFragment();
        return mFragment;
    }
}
