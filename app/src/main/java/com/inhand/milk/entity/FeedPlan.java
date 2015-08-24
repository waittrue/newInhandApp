package com.inhand.milk.entity;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVClassName;
import com.inhand.milk.dao.FeedItemDao;
import com.inhand.milk.utils.ACache;
import com.inhand.milk.utils.LocalFindTask;
import com.inhand.milk.utils.LocalSaveTask;

import java.util.List;

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

    /**
     * 异步地从本地获取该喂养计划的所有条目
     *
     * @param ctx      上下文环境
     * @param callback 回调接口
     */
    public void fetchItems(final Context ctx, final LocalFindTask.LocalFindCallback<FeedItem> callback) {
        ACache aCache = ACache.get(ctx);
        String json = aCache.getAsString(FeedItem.CACHE_KEY);
        LocalFindTask task = new LocalFindTask() {
            List<FeedItem> items;

            @Override
            protected Void doInBackground(Void... voids) {
                items = fetchItems(ctx);
                return super.doInBackground(voids);
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                callback.done(items);
                super.onPostExecute(aVoid);
            }
        };
    }

    /**
     * 同步地从本地获取该喂养计划的所有条目
     *
     * @param ctx 上下文环境
     * @return 奶粉使用说明列表
     */
    public List<FeedItem> fetchItems(final Context ctx) {
        ACache aCache = ACache.get(ctx);
        String json = aCache.getAsString(FeedItem.CACHE_KEY);
        return json == null ? null : JSON.parseArray(json, FeedItem.class);
    }

    /**
     * 异步地缓存喂养计划条目列表到本地
     *
     * @param ctx      上下文环境
     * @param items    待缓存喂养计划条目列表
     * @param callback 回调接口
     */
    public void cacheItems(
            final Context ctx
            , final List<FeedItem> items
            , final LocalSaveTask.LocalSaveCallback<FeedItem> callback) {
        FeedItemDao ptd = new FeedItemDao();
        ptd.cache(ctx, items, callback);
    }

    /**
     * 同步地缓存喂养计划条目列表到本地
     *
     * @param ctx   上下文环境
     * @param items 待缓存喂养计划条目说明列表
     */
    public void cacheItems(Context ctx, List<FeedItem> items) {
        FeedItemDao ptd = new FeedItemDao();
        ptd.cache(ctx, items);
    }

}
