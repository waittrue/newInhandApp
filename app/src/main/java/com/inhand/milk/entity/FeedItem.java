package com.inhand.milk.entity;

import android.content.Context;

import com.avos.avoscloud.AVClassName;
import com.inhand.milk.utils.ACache;

/**
 * FeedItem
 * Desc: 喂养计划条目模型
 * Team: InHand
 * User: Wooxxx
 * Date: 2015-07-26
 * Time: 09:23
 */
@AVClassName(FeedItem.FEED_ITEM_CLASS_KEY)
public class FeedItem extends Base {
    public static final String FEED_ITEM_CLASS_KEY = "Milk_FeedItem";
    public static final String FEED_PLAN_KEY = "feed_plan"; // 所属喂养计划
    public static final String TIME_KEY = "time"; // 喂养时间
    public static final String SUPPLEMENT_KEY = "supplement"; // 辅食
    public static final String IS_MILKING_KEY = "is_milking"; // 是否为饮奶

    /**
     * 获得该喂养条目所属的喂养计划
     * @return 该喂养条目所属的喂养计划
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
     * 设置该喂养条目所属的喂养计划
     * @param feedPlan 设置该喂养条目所属的喂养计划
     */
    public void setFeedPlan(FeedPlan feedPlan){
        this.put(FEED_PLAN_KEY,feedPlan);
    }

    /**
     * 获得喂奶时间
     * @return 喂奶时间
     */
    public String getTime(){
        return this.getString(TIME_KEY);
    }

    /**
     * 设置喂奶时间
     * @param time 喂奶时间
     */
    public void setTime(String time){
        this.put(TIME_KEY,time);
    }

    /**
     * 获得辅食
     * @return 辅食
     */
    public Supplement getSupplement(){
        try {
            return this.getAVObject(SUPPLEMENT_KEY,Supplement.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 设置辅食
     * @param supplement 辅食
     */
    public void setSupplement(Supplement supplement){
        this.put(SUPPLEMENT_KEY,supplement);
    }

    /**
     * 获得该条目是否为喝奶
     * @return 是否为喝奶
     */
    public boolean isMilking(){
        return this.getBoolean(IS_MILKING_KEY);
    }

    /**
     * 设置该条目是否为喝奶
     * @param isMilking 是否为喝奶
     */
    public void setIsMilking(boolean isMilking){
        this.put(IS_MILKING_KEY,isMilking);
    }


}
