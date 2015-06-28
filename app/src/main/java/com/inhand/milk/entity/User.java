package com.inhand.milk.entity;


import android.content.Context;
import android.util.Log;

import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.inhand.milk.App;
import com.inhand.milk.dao.BabyDao;
import com.inhand.milk.utils.ACache;

import java.util.List;


/**
 * User
 * Desc:
 * Team: InHand
 * User:Wooxxx
 * Date: 2015-03-19
 * Time: 14:35
 */
public class User extends AVUser {
    public static final String CLASS_NAME = "_USER";
    //昵称列
    public static final String NICKNAME_KEY = "nickname";
    //性别列
    public  static  final String SEX_KEY="sex";

    public static int FEMALE = 2; // 女性
    public static int MALE = 1; // 男性
    // 判断用户是否有baby的错误码
    public static final int NO_BABY = 0;
    public static final int HAS_BABY = 1;
    public static final int NETWORK_ERROR = 2;

    public String getNickname() {
        return this.getString(NICKNAME_KEY);
    }

    public void setNickname(String nickname) {
        this.put(NICKNAME_KEY, nickname);
    }

    public int getSex(){return this.getInt(SEX_KEY);  }

    public void setSex(int sex) {
        this.put(SEX_KEY, sex);
    }
    /**
     * 取得当前用户的所有宝宝
     * @param findCallback 回调接口
     */
    public void getBabies(FindCallback<Baby> findCallback) {
        BabyDao.findBabiesByUser(this, findCallback);
    }

    /**
     * 查看该用户是否填写了宝宝信息
     */
    public int hasBaby(Context ctx) {
        // 首先判断本地缓存中是否有Baby
        if (App.getCurrentBaby() == null) {
            List<Baby> babies = BabyDao.findBabiesByUser(this);
            if (babies != null) {
                if (babies.size() == 0)
                    return NO_BABY;
                Baby baby = babies.get(0);
                baby.saveInCache(ctx, new Base.CacheSavingCallback() {
                    @Override
                    public void done() {
                        Log.d("Cache Saving", "OK");
                    }
                });
                return HAS_BABY;
            } else {
                return NETWORK_ERROR;
            }
        }
        return HAS_BABY;
    }

    /**
     * 登出方法,会清除保存的宝宝信息
     */
    public void logOut(Context ctx) {
        ACache aCache = ACache.get(ctx);
        aCache.remove(App.BABY_CACHE_KEY);
        AVUser.logOut();
    }



}
