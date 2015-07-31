package com.inhand.milk.entity;

import com.avos.avoscloud.AVClassName;

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
    public static final String FEED_PLAN_KEY = "feedPlan"; // 所属喂养计划
    public static final String TIME_KEY = "time"; // 喂养时间
    public static final String SUPPLEMENT_KEY = "supplement"; // 辅食
    public static final String TYPE_KEY = "type"; // 喂食方式

    public static final int TYPE_MILK = 0; // 饮奶方式
    public static final int TYPE_SUPP = 1; // 辅食方式

    public static final String CACHE_KEY = "feed_items";


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
        // 设置装填为辅食
        this.put(TYPE_KEY, TYPE_SUPP);
        this.put(SUPPLEMENT_KEY,supplement);
    }

    /**
     * 返回喂食类型
     * @return 喂食类型
     */
    public int getType() {
        return this.getInt(TYPE_KEY);
    }

    /**
     * 设置喂食类型
     * @param type 喂食类型
     */
    public void setType(int type) {
        this.put(TYPE_KEY, type);
    }


}
