package com.inhand.milk.entity;

import android.content.Context;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.SaveCallback;
import com.inhand.milk.App;
import com.inhand.milk.utils.ACache;

/**
 * FeedPlan
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
    public static final String CACHE_KEY= "feed_plan"; // 本地缓存KEY

    /**
     * 获得营养师推荐名称
     * @return 营养师推荐名称
     */
    public String getRCMD(){
        return this.getString(RCMD_KEY);
    }

    /**
     * 获得该推荐使用次数
     * @return 该推荐使用次数
     */
    public int getCount(){
        return this.getInt(COUNT_KEY);
    }

    /**
     * 增加推荐使用次数1次
     */
    public void addCount(){
        this.put(COUNT_KEY,this.getCount()+1);
    }

    /**
     * 将该喂养计划缓存到缓存
     * @param ctx 上下文环境
     * @param callback 回调接口
     */
    public void saveInCache(final Context ctx, final CacheSavingCallback callback){
        final FeedPlan feedPlan = this;
        CacheSavingTask cacheSavingTask =
                new CacheSavingTask(ctx, callback) {
                    @Override
                    protected Object doInBackground(Object[] params) {
                        ACache aCache = ACache.get(ctx);
                        aCache.put(FeedPlan.CACHE_KEY,feedPlan.toJSONObject());
                        return super.doInBackground(params);
                    }
                };
        cacheSavingTask.execute();
    }


}
