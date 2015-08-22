package com.inhand.milk.activity;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import com.inhand.milk.App;
import com.inhand.milk.entity.Baby;
import com.inhand.milk.fragment.person_center.PersonCenterBabyFragment;

/**
 * Created by Administrator on 2015/7/7.
 */
public class PersonCenterBabyInfoActivity extends UserInfoSettingsActivity {
    private String birth;
    private Fragment mFragment;
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
    protected void setImageToHeadView(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            ((PersonCenterBabyFragment) mFragment).setHeadImageview(photo);
            saveBitmap(photo);
        }
    }

    @Override
    protected Fragment initFragment() {
        // TODO Auto-generated method stub
        mFragment = new PersonCenterBabyFragment();
        return mFragment;
    }

}
