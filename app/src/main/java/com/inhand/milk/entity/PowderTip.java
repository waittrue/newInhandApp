package com.inhand.milk.entity;

import com.avos.avoscloud.AVClassName;

/**
 * PoweDerTip
 * Desc:
 * Team: InHand
 * User: Wooxxx
 * Date: 2015-07-27
 * Time: 21:04
 */
@AVClassName(PowderTip.POWDER_TIP_CLASS)
public class PowderTip extends Base {
    public static final String POWDER_TIP_CLASS = "Milk_PowderTip";

    public static final String FEED_PLAN_KEY = "feedPlan"; // 所属喂养计划
    public static final String SPOON_DOSAGE_KEY = "spoonDosage"; // 喂养量/勺
    public static final String GRAM_DOSAGE_KEY = "gramDosage"; // 喂养量/克
    public static final String FOR_AGE_KEY = "forAge"; // 年龄段
    public static final String COUNT_KEY = "count"; // 饮用次数
    public static final String REC_TEMPERATURE_KEY = "recTemperature"; // 冲泡温度
    public static final String REC_DENSITY_KEY = "recDensity"; // 奶水密度

    public static final String REC_VOL_MIN_KEY = "recVolMin"; // 最小建议量
    public static final String REC_VOL_MAX_KEY = "recVolMax"; // 最大建议量
    public static final String REC_VOL_NORM_KEY = "recVolNorm"; // 建议标准量

    public static final String POWDER_KEY = "powder";

    public static final String CACHE_KEY = "cache";

    /**
     * 获得奶粉默认喂养计划
     *
     * @return 奶粉默认喂养计划
     */
    public FeedPlan getFeedPlan() {
        try {
            return this.getAVObject(FEED_PLAN_KEY, FeedPlan.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setFeedPlan(FeedPlan plan) {
        this.put(FEED_PLAN_KEY, plan);
    }

    /**
     * 获得奶粉用量/勺
     *
     * @return 奶粉用量/勺
     */
    public int getSpoonDosage() {
        return this.getInt(SPOON_DOSAGE_KEY);
    }

    public void setSpoonDosage(int spoonDosage) {
        this.put(SPOON_DOSAGE_KEY, spoonDosage);
    }

    /**
     * 获得奶粉用量/克
     *
     * @return 奶粉用量/克
     */
    public float getGramDosage() {
        return this.getNumber(GRAM_DOSAGE_KEY).floatValue();
    }

    public void setGramDosage(float gramDosage) {
        this.put(GRAM_DOSAGE_KEY, gramDosage);
    }

    /**
     * 获得奶粉针对年龄段
     *
     * @return 奶粉针对年龄段
     */
    public String getForAge() {
        return this.getString(FOR_AGE_KEY);
    }

    public void setForAge(String forAge) {
        this.put(FOR_AGE_KEY, forAge);
    }

    /**
     * 获得奶粉推荐饮用次数
     *
     * @return 奶粉引用次数
     */
    public int getCount() {
        return this.getInt(COUNT_KEY);
    }

    public void setCount(int count) {
        this.put(COUNT_KEY, count);
    }

    /**
     * 获得奶粉推荐冲泡温度
     *
     * @return 奶粉冲泡温度
     */
    public float getRecTemperature() {
        return this.getInt(REC_TEMPERATURE_KEY);
    }

    public void setRecTemperature(float recTemperature) {
        this.put(REC_TEMPERATURE_KEY, recTemperature);
    }

    /**
     * 获得推荐奶粉密度
     *
     * @return 奶粉密度
     */
    public float getRecDensity() {
        return this.getNumber(REC_DENSITY_KEY).floatValue();
    }

    public void setRecDensity(float recDensity) {
        this.put(REC_DENSITY_KEY, recDensity);
    }

    /**
     * 获得建议最小量
     *
     * @return 建议最小量
     */
    public int getRecVolMin() {
        return this.getInt(REC_VOL_MIN_KEY);
    }

    public void setRecVolMin(int recVolMin) {
        this.put(REC_VOL_MIN_KEY, recVolMin);
    }

    /**
     * 获得建议最大量
     *
     * @return 建议最大量
     */
    public int getRecVolMax() {
        return this.getInt(REC_VOL_MAX_KEY);
    }

    public void setRecVolMax(int recVolMax) {
        this.put(REC_VOL_MAX_KEY, recVolMax);
    }

    /**
     * 获得建议标准量
     *
     * @return 建议标准量
     */
    public int getRecVolNorm() {
        return this.getInt(REC_VOL_NORM_KEY);
    }

    public void setRecVolNorm(int recVolNorm) {
        this.put(REC_VOL_NORM_KEY, recVolNorm);
    }

}
