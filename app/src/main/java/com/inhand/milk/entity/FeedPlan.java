package com.inhand.milk.entity;

import com.avos.avoscloud.AVClassName;

/**
 * FeedItem
 * Desc: 喂养计划模型
 * Team: InHand
 * User: Wooxxx
 * Date: 2015-07-26
 * Time: 09:06
 */

@AVClassName(FeedPlan.FEED_PLAN_CLASS)
public class FeedPlan extends Base {
    public static final String FEED_PLAN_CLASS = "Milk_FeedPlan";
    public static final String RCMD_KEY = "rcmd"; // 营养师推荐名称
    public static final String COUNT_KEY = "count"; // 该推荐被使用次数
    public static final String CACHE_KEY = "feedPlan"; // 本地缓存KEY

    /**
     * 获得营养师推荐名称
     *
     * @return 营养师推荐名称
     */
    public String getRCMD() {
        return this.getString(RCMD_KEY);
    }


    public void setRCMD(String rcmd) {
        this.put(RCMD_KEY, rcmd);
    }

    /**
     * 获得该推荐使用次数
     *
     * @return 该推荐使用次数
     */
    public int getCount() {
        return this.getInt(COUNT_KEY);
    }

    public void setCount(int count) {
        this.put(COUNT_KEY, count);
    }

    /**
     * 增加推荐使用次数1次
     */
    public void addCount() {
        this.increment(COUNT_KEY);
    }
}
