package com.inhand.milk.activity;

import android.app.Fragment;

import com.inhand.milk.entity.Baby;
import com.inhand.milk.fragment.person_center.PersonCenterBabyFragment;

/**
 * Created by Administrator on 2015/7/7.
 */
public class PersonCenterBabyInfoActivity extends UserInfoSettingsActivity {
    private String birth;
    @Override
    protected void initData() {
        super.initData();
    }
    public String getBirth(){
        return birth;
    }
    public void setBirth(String str){
        birth = str;
    }
    @Override
    protected Fragment initFragment() {
        // TODO Auto-generated method stub
        return  new PersonCenterBabyFragment();// new Nutrition();
    }
}
