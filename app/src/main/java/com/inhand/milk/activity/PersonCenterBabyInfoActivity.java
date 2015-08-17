package com.inhand.milk.activity;

import android.app.Fragment;
import android.util.Log;

import com.inhand.milk.App;
import com.inhand.milk.entity.Baby;
import com.inhand.milk.fragment.person_center.PersonCenterBabyFragment;

/**
 * Created by Administrator on 2015/7/7.
 */
public class PersonCenterBabyInfoActivity extends UserInfoSettingsActivity {
    private String birth;

    @Override
    protected void initData() {
        Baby baby = App.getCurrentBaby();
        if (baby == null)
            return;
        String birth, sex, name;
        sex = null;
        birth = baby.getBirthday();
        Log.i("personcenterbabyinfo", birth);
        int sexIndex = baby.getSex();
        if (sexIndex == Baby.FEMALE)
            sex = "女性";
        else if (sexIndex == Baby.MALE)
            sex = "男性";
        name = baby.getNickname();
        setName(name);
        setBirth(birth);
        setSex(sex);
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String str) {
        birth = str;
    }

    @Override
    protected Fragment initFragment() {
        // TODO Auto-generated method stub
        return new PersonCenterBabyFragment();// new Nutrition();
    }

}
