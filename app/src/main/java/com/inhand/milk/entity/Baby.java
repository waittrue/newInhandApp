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
    public static final String HEIGHT_KEY = "height";
    public static final String WEIGHT_KEY = "weights";
    public static final String HEAD_SIZE_KEY = "head_size";
    public static final String USER_KEY = "user";
    public static final String SEX_KEY = "sex";
    public static int FEMALE = 2; // 女性
    public static int MALE = 1; // 男性

    public String getNickname() {
        return this.getString(NICKNAME_KEY);
    }

    public void setNickname(String nickname) {
        this.put(NICKNAME_KEY, nickname);
    }

    public String getBirthday() {
        return this.getString(BIRTHDAY_KEY);
    }

    public void setBirthday(String birthday) {
        this.put(BIRTHDAY_KEY, birthday);
    }

    public float getHeight() {
        return this.getInt(HEIGHT_KEY);
    }

    public void setHeight(float height) {
        this.put(HEIGHT_KEY, height);
    }

    /**
     * 异步地获取宝宝体重列表
     * @param callback 回调接口
     */
    public void getWeight(
            final FindCallback<Weight> callback) {
        AVRelation<Weight> relation =
                this.getRelation(WEIGHT_KEY);
        relation.getQuery().findInBackground(callback);
    }

    /**
     * 同步地获取宝宝体重列表
     * @return 体重列表
     */
    public List<Weight> getWeight() {
        AVRelation<Weight> relation =
                this.getRelation(WEIGHT_KEY);
        try {
            return relation.getQuery().find();
        } catch (AVException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 添加一条体重记录
     * @param weight 待添加体重记录
     */
    public void addWeight(Weight weight) {
        AVRelation<Weight> relation =
                this.getRelation(WEIGHT_KEY);
        relation.add(weight);
    }

    public float getHeadSize() {
        return this.getInt(HEAD_SIZE_KEY);
    }

    public void setHeadSize(float headSize) {
        this.put(HEAD_SIZE_KEY, headSize);
    }

    public int getSex() {
        return this.getInt(SEX_KEY);
    }

    public void setSex(int sex) {
        this.put(SEX_KEY, sex);
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
