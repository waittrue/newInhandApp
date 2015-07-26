package com.inhand.milk.entity;

import android.content.Context;

import com.avos.avoscloud.AVClassName;
import com.inhand.milk.utils.ACache;

/**
 * Powder
 * Desc: 奶粉模型
 * Team: InHand
 * User: Wooxxx
 * Date: 2015-07-26
 * Time: 08:51
 */
@AVClassName(Powder.POWEDER_CLASS)
public class Powder extends Base{

    public static final String POWEDER_CLASS = "Milk_Powder";
    public static final String ZH_NAME_KEY = "zh_name"; // 中文名
    public static final String EN_NAME_KEY = "en_name"; // 英文名
    public static final String SERIAL_NAME_KEY = "serial_name"; // 系列名
    public static final String PHASE_KEY = "phase"; // 段数
    public static final String FEED_PLAN_KEY = "feed_plan"; // 所属喂养计划
    public static final String SPOON_DOSAGE_KEY = "spoon_dosage"; // 喂养量/勺
    public static final String GRAM_DOSAGE_KEY = "gram_dosage"; // 喂养量/克
    public static final String FOR_AGE_KEY = "for_age"; // 年龄段
    public static final String COUNT_KEY = "count"; // 饮用次数
    public static final String REC_TEMPERATURE_KEY = "rec_temperature"; // 冲泡温度
    public static final String REC_DENSITY_KEY = "rec_density"; // 奶水密度

    public static final String CACHE_KEY = "powder"; // 奶粉缓存键
    /**
     * 获得奶粉中文名
     * @return 奶粉中文名
     */
    public String getZhName(){
        return this.getString(ZH_NAME_KEY);
    }

    /**
     * 获得奶粉英文名
     * @return 奶粉英文名
     */
    public String getEnName(){
        return this.getString(EN_NAME_KEY);
    }

    /**
     * 获得奶粉系列名
     * @return 奶粉系列名
     */
    public String getSerialName(){
        return this.getString(SERIAL_NAME_KEY);
    }

    /**
     * 获得奶粉段数
     * @return 奶粉段数
     */
    public int getPhase(){
        return this.getInt(PHASE_KEY);
    }

    /**
     * 获得奶粉默认喂养计划
     * @return 奶粉默认喂养计划
     */
    public FeedPlan getFeedPlan(){
        try {
            return this.getAVObject(FEED_PLAN_KEY,FeedPlan.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获得奶粉用量/勺
     * @return 奶粉用量/勺
     */
    public int getSpoonDosage(){
        return this.getInt(SPOON_DOSAGE_KEY);
    }

    /**
     * 获得奶粉用量/克
     * @return 奶粉用量/克
     */
    public float getGramDosage(){
        return this.getNumber(GRAM_DOSAGE_KEY).floatValue();
    }

    /**
     * 获得奶粉针对年龄段
     * @return 奶粉针对年龄段
     */
    public String getForAge(){
        return this.getString(FOR_AGE_KEY);
    }

    /**
     * 获得奶粉推荐饮用次数
     * @return 奶粉引用次数
     */
    public int getCount(){
        return this.getInt(COUNT_KEY);
    }

    /**
     * 获得奶粉推荐冲泡温度
     * @return 奶粉冲泡温度
     */
    public int getRecTemperature(){
        return this.getInt(REC_TEMPERATURE_KEY);
    }

    /**
     * 获得推荐奶粉密度
     * @return 奶粉密度
     */
    public float getRecDensity(){
        return this.getNumber(REC_DENSITY_KEY).floatValue();
    }

    /**
     * 将该奶粉缓存到缓存
     * @param ctx 上下文环境
     * @param callback 回调接口
     */
    public void saveInCache(final Context ctx, final CacheSavingCallback callback){
        final Powder powder = this;
        CacheSavingTask cacheSavingTask =
                new CacheSavingTask(ctx, callback) {
                    @Override
                    protected Object doInBackground(Object[] params) {
                        ACache aCache = ACache.get(ctx);
                        aCache.put(Powder.CACHE_KEY,powder.toJSONObject());
                        return super.doInBackground(params);
                    }
                };
        cacheSavingTask.execute();
    }

}
