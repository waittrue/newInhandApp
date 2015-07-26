package com.inhand.milk.entity;

import android.content.Context;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVRelation;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.inhand.milk.App;
import com.inhand.milk.dao.OneDayDao;
import com.inhand.milk.utils.ACache;

import java.util.List;


/**
 * Baby
 * Desc:Baby实体
 * Team: InHand
 * User:Wooxxx
 * Date: 2015-03-17
 * Time: 08:37
 */
@AVClassName(Baby.BABY_CLASS)
public class Baby extends Base {
    public static final String BABY_CLASS = "Baby";
    public static final String NICKNAME_KEY = "nickname";
    public static final String BIRTHDAY_KEY = "birthday";
    public static final String POWDER_KEY = "powder";
    public static final String USER_KEY = "user";
    public static final String SEX_KEY = "sex";
    public static int FEMALE = 2; // 女性
    public static int MALE = 1; // 男性

    /**
     * 获得宝宝昵称
     * @return 宝宝昵称
     */
    public String getNickname() {
        return this.getString(NICKNAME_KEY);
    }

    /**
     * 设置宝宝昵称
     * @param nickname 昵称
     */
    public void setNickname(String nickname) {
        this.put(NICKNAME_KEY, nickname);
    }

    /**
     * 获得宝宝生日
     * @return 宝宝生日 格式：2014-02-03
     */
    public String getBirthday() {
        return this.getString(BIRTHDAY_KEY);
    }

    /**
     * 设置宝宝生日
     * @param birthday 宝宝生日 格式：2014-02-03
     */
    public void setBirthday(String birthday) {
        this.put(BIRTHDAY_KEY, birthday);
    }


    /**
     * 获得宝宝性别
     * @return 宝宝性别：1===MALE,2===FEMALE
     */
    public int getSex() {
        return this.getInt(SEX_KEY);
    }

    /**
     * 设置宝宝性别
     * @param sex 宝宝性别：1===MALE,2==FEMALE
     */
    public void setSex(int sex) {
        this.put(SEX_KEY, sex);
    }

    /**
     * 获得宝宝当前所用奶粉
     * @return 宝宝当前所用奶粉
     */
    public Powder getPowder(){
        try {
            return this.getAVObject(POWDER_KEY,Powder.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 设置宝宝当前所用奶粉
     * @param powder 宝宝当前所用奶粉
     */
    public void setPowder(Powder powder){
        this.put(POWDER_KEY,powder);
    }

    /**
     * 获得当前宝宝所在账户
     * @return 宝宝所在账户
     */
    public User getUser(){
        return this.getAVUser(USER_KEY,User.class);
    }

    public void setUser(User user) {
        try {
            this.put(USER_KEY, AVObject.createWithoutData(User.class, user.getObjectId()));
        } catch (AVException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得当前宝宝的喝奶记录
     *
     * @param ctx          上下文环境
     * @param limit        需求数目
     * @param findCallback 回调接口
     */
    public void getOnedays(Context ctx, int limit, FindCallback<OneDay> findCallback) {
        OneDayDao oneDayDao = new OneDayDao(ctx);
        //oneDayDao.findAllOrLimit(limit, findCallback);
    }

    /**
     * 存储Baby对象，若已存在，则为更新
     * @param saveCallback    回调接口
     */
    public void save(final SaveCallback saveCallback) {
        if( this.getUser() == null ){
            this.setUser(App.getCurrentUser());
        }
        this.saveInBackground(saveCallback);
    }

    /**
     * 同步地存储Baby对象，若已存在，则为更新
     */
    public void saveSync() throws AVException {
        if (this.getUser() == null) {
            this.setUser(App.getCurrentUser());
        }
        this.save();
    }

    /**
     * 写入缓存,考虑baby对象在离线情况下始终可用，
     *
     * @param ctx 上下文环境
     * @param callback    回调接口
     */
    public void saveInCache(final Context ctx, final CacheSavingCallback callback) {
        final Baby baby = this;
        CacheSavingTask cacheSavingTask =
                new CacheSavingTask(ctx, callback) {
                    @Override
                    protected Object doInBackground(Object[] params) {
                        ACache aCache = ACache.get(ctx);
                        aCache.put(App.BABY_CACHE_KEY, baby.toJSONObject());
                        // 同时更新CurrentBaby
                        App.currentBaby = baby;
                        return super.doInBackground(params);
                    }
                };
        cacheSavingTask.execute();
    }


}
