package com.inhand.milk.entity;

import android.content.Context;

import com.avos.avoscloud.AVClassName;
import com.inhand.milk.utils.ACache;
import com.inhand.milk.utils.LocalSaveTask;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * BabyInfo
 * Desc: 宝宝信息模型
 * Team: InHand
 * User: Wooxxx
 * Date: 2015-07-26
 * Time: 10:36
 */
@AVClassName(BabyInfo.BABY_INFO_CLASS)
public class BabyInfo extends Base implements CacheSaving<BabyInfo> {
    public static final String BABY_INFO_CLASS = "Milk_BabyInfo";

    public static final String AGE_KEY = "age"; // 宝宝日龄
    public static final String HEIGHT_KEY = "height"; // 宝宝身高
    public static final String WEIGHT_KEY = "weight"; // 宝宝体重
    public static final String HEAD_SIZE_KEY = "headSize"; // 宝宝头围
    public static final String BABY_KEY = "baby"; // 信息所属宝宝
    public static final String CACHE_KEY_PREFIX = "babyinfo-";
    public static final String AGE_DATE_FORMAT = "yyyy-MM-dd"; // 宝宝日龄格式


    /**
     * 获得宝宝日龄
     * @return 宝宝日龄
     */
    public String getAge() {
        return this.getString(AGE_KEY);
    }

    /**
     * 设置宝宝日龄
     * @param date 宝宝日龄
     */
    public void setAge(String date) {
        this.put(AGE_KEY, date);
    }

    /**
     * 获得宝宝身高
     * @return 身高
     */
    public float getHeight() {
        return this.getNumber(HEIGHT_KEY).floatValue();
    }

    /**
     * 设置宝宝身高
     * @param height 身高
     */
    public void setHeight(float height) {
        this.put(HEIGHT_KEY,height);
    }

    /**
     * 获得宝宝体重
     * @return 体重
     */
    public float getWeight() {
        return this.getNumber(WEIGHT_KEY).floatValue();
    }

    /**
     * 设置宝宝体重
     * @param weight 体重
     */
    public void setWeight(float weight) {
        this.put(WEIGHT_KEY,weight);
    }

    /**
     * 获得宝宝头围
     * @return 头围
     */
    public float getHeadSize() {
        return this.getNumber(HEAD_SIZE_KEY).floatValue();
    }

    /**
     * 设置宝宝头围
     * @param headSize 宝宝头围
     */
    public void setHeadSize(float headSize) {
        this.put(HEAD_SIZE_KEY,headSize);
    }

    /**
     * 获得宝宝
     * @return 宝宝
     */
    public Baby getBaby(){
        return this.getAVObject(BABY_KEY);
    }

    /**
     * 设置信息所属宝宝
     * @param baby 宝宝
     */
    public void setBaby(Baby baby){
        this.put(BABY_KEY,baby);
    }

    @Override
    public void saveInCache(final Context ctx, final LocalSaveTask.LocalSaveCallback callback) {
        LocalSaveTask task =
                new LocalSaveTask(callback) {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        saveInCache(ctx);
                        return super.doInBackground(voids);
                    }
                };
        task.execute();
    }

    @Override
    public void saveInCache(Context ctx) {
        ACache aCache = ACache.get(ctx);
        // 根据年-月（"BabyInfo-2014-02"）进行缓存
        String cacheKey = CACHE_KEY_PREFIX + this.getAge().substring(0, 7);
        JSONArray infos = aCache.getAsJSONArray(cacheKey);
        // 本地是否存在信息列表缓存，不存在则新建
        if (infos == null) {
            infos = new JSONArray();
            aCache.put(cacheKey, infos);
        }
        JSONObject obj = this.toJSONObject();
        infos.put(obj);
        aCache.put(cacheKey, infos);
    }
}
