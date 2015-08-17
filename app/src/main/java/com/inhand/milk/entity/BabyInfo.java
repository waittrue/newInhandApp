package com.inhand.milk.entity;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.inhand.milk.utils.ACache;
import com.inhand.milk.utils.LocalSaveTask;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

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
     *
     * @return 宝宝日龄
     */
    public String getAge() {
        return this.getString(AGE_KEY);
    }

    /**
     * 设置宝宝日龄
     *
     * @param date 宝宝日龄
     */
    public void setAge(String date) {
        this.put(AGE_KEY, date);
    }

    /**
     * 获得宝宝身高
     *
     * @return 身高
     */
    public float getHeight() {
        return this.getNumber(HEIGHT_KEY) == null ? 0 : this.getNumber(HEIGHT_KEY).floatValue();
    }

    /**
     * 设置宝宝身高
     *
     * @param height 身高
     */
    public void setHeight(float height) {
        this.put(HEIGHT_KEY, height);
    }

    /**
     * 获得宝宝体重
     *
     * @return 体重
     */
    public float getWeight() {
        return this.getNumber(WEIGHT_KEY) == null ? 0 : this.getNumber(WEIGHT_KEY).floatValue();
    }

    /**
     * 设置宝宝体重
     *
     * @param weight 体重
     */
    public void setWeight(float weight) {
        this.put(WEIGHT_KEY, weight);
    }

    /**
     * 获得宝宝头围
     *
     * @return 头围
     */
    public float getHeadSize() {
        return this.getNumber(HEAD_SIZE_KEY) == null ? 0 : this.getNumber(HEAD_SIZE_KEY).floatValue();
    }

    /**
     * 设置宝宝头围
     *
     * @param headSize 宝宝头围
     */
    public void setHeadSize(float headSize) {
        this.put(HEAD_SIZE_KEY, headSize);
    }

    /**
     * 获得宝宝
     *
     * @return 宝宝
     */
    public Baby getBaby() {
        return this.getAVObject(BABY_KEY);
    }

    /**
     * 设置信息所属宝宝
     *
     * @param baby 宝宝
     */
    public void setBaby(Baby baby) {
        this.put(BABY_KEY, baby);
    }

    /**
     * 同步地将BabyInfo存至云端
     */
    public void saveInCloud() {
        // 同一age的进行覆盖
        AVQuery<BabyInfo> query = AVQuery.getQuery(BabyInfo.class);
        query.whereEqualTo(BabyInfo.AGE_KEY, this.getAge());
        query.whereEqualTo(BabyInfo.BABY_KEY, this.getBaby());
        try {
            List<BabyInfo> infos = query.find();
            if (infos.size() > 0) {
                // 如果存在，则覆盖
                BabyInfo info = infos.get(0);
                info.refresh(this);
                info.save();
            } else {
                this.save();
            }
        } catch (AVException e) {
            e.printStackTrace();
        }
    }

    /**
     * 异步地将BabyInfo存至云端
     *
     * @param callback 回调接口
     */
    public void saveInCloud(final SaveCallback callback) {
        final BabyInfo newInfo = this;
        // 同一age的进行覆盖
        AVQuery<BabyInfo> query = AVQuery.getQuery(BabyInfo.class);
        query.whereEqualTo(BabyInfo.AGE_KEY, this.getAge());
        query.whereEqualTo(BabyInfo.BABY_KEY, this.getBaby());
        query.findInBackground(new FindCallback<BabyInfo>() {
            @Override
            public void done(List<BabyInfo> list, AVException e) {
                if (e == null) {//这里是大力 增加了错误检查
                    if (list.size() > 0) {
                        // 如果存在，则覆盖
                        BabyInfo info = list.get(0);
                        info.refresh(newInfo);
                        info.saveInBackground(callback);
                    } else {
                        newInfo.saveInBackground(callback);
                    }
                }
                else //这里也是大力增加的错误检查
                    if(callback !=null)
                        callback.done(e);
            }
        });
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

    public void saveInCache(Context ctx) {
        ACache aCache = ACache.get(ctx);
        // 根据年-月（"BabyInfo-2014-02"）进行缓存
        String cacheKey = CACHE_KEY_PREFIX + this.getAge().substring(0, 7);
        JSONArray infos = aCache.getAsJSONArray(cacheKey);
        // 本地是否存在本月信息列表缓存，不存在则新建
        if (infos == null) {
            infos = new JSONArray();
        }
        if (infos.length() > 0) {
            int lastIdx = infos.length() - 1;
            try {
                String latestStr = infos.getString(lastIdx);
                BabyInfo babyInfo = JSON.parseObject(latestStr, BabyInfo.class);
                // 比较是否重复,若重复，则覆盖
                if (babyInfo.getAge()
                        .equals(this.getAge())) {
                    babyInfo.refresh(this);
                    infos.put(lastIdx, babyInfo.toJSONObject());
                } else {
                    // 否则，追加
                    infos.put(this.toJSONObject());
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            // 如果array为空，追加
            infos.put(this.toJSONObject());
        }
        // 刷新缓存
        aCache.put(cacheKey, infos);
    }

    private void refresh(BabyInfo newInfo) {

        float headSize = newInfo.getHeadSize();
        float height = newInfo.getHeight();
        float weight = newInfo.getWeight();
        if (headSize != 0) {
            this.setHeadSize(headSize);
        }
        if (height != 0) {
            this.setHeight(height);
        }
        if (weight != 0) {
            this.setWeight(weight);
        }
    }
}
