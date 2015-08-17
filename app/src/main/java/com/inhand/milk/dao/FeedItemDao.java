package com.inhand.milk.dao;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.inhand.milk.entity.FeedItem;
import com.inhand.milk.entity.FeedPlan;
import com.inhand.milk.utils.ACache;
import com.inhand.milk.utils.LocalFindTask;
import com.inhand.milk.utils.LocalSaveTask;

import org.json.JSONArray;

import java.util.List;

/**
 * FeedItemDao
 * Desc: 数据防蚊虫--喂养计划条目
 * Team: InHand
 * User: Wooxxx
 * Date: 2015-07-26
 * Time: 10:29
 */
public class FeedItemDao {

    public static final String FIND_ORDER = "createdAt";
    private AVQuery<FeedItem> query = AVQuery.getQuery(FeedItem.class);

    /**
     * 从云端异步地根据喂养计划获得该计划下所有条目
     *
     * @param feedPlan 喂养计划
     * @param callback 回调接口
     */
    public void findByFeedPlanFromCloud(final FeedPlan feedPlan
            , final FindCallback<FeedItem> callback) {
        query.orderByAscending(FIND_ORDER);
        query.whereEqualTo(FeedItem.FEED_PLAN_KEY, feedPlan);
        query.findInBackground(callback);
    }


    /**
     * 同步地根据喂养计划获得该计划下所有条目
     *
     * @param feedPlan 喂养计划
     * @return 喂养计划条目
     */
    public List<FeedItem> findByFeedPlanFromCloud(final FeedPlan feedPlan) {
        query.orderByAscending(FIND_ORDER);
        query.whereEqualTo(FeedItem.FEED_PLAN_KEY, feedPlan);
        try {
            return query.find();
        } catch (AVException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 异步地缓存喂养计划条目到本地
     *
     * @param ctx       上下文环境
     * @param feedItems 待缓存喂养计划条目
     * @param callback  回调接口
     */
    public void cache(
            final Context ctx
            , final List<FeedItem> feedItems
            , final LocalSaveTask.LocalSaveCallback<FeedItem> callback) {
        LocalSaveTask task = new LocalSaveTask(callback) {
            @Override
            protected Void doInBackground(Void... voids) {
                cache(ctx, feedItems);
                return super.doInBackground(voids);
            }

        };
        task.execute();
    }

    /**
     * 同步地缓存喂养计划条目到本地
     *
     * @param ctx       上下文环境
     * @param feedItems 待缓存喂养计划条目
     */
    public void cache(Context ctx, List<FeedItem> feedItems) {
        ACache aCache = ACache.get(ctx);
        aCache.remove(FeedItem.CACHE_KEY);
        JSONArray array = new JSONArray();
        for (FeedItem item : feedItems) {
            array.put(item.toJSONObject().toString());
        }
        aCache.put(FeedItem.CACHE_KEY, array);
    }

    /**
     * 异步地从本地缓存取出喂养计划条目
     *
     * @param ctx      上下文环境
     * @param callback 回调接口
     */
    public void findFromCache(final Context ctx
            , final LocalFindTask.LocalFindCallback<FeedItem> callback) {
        LocalFindTask task = new LocalFindTask() {
            List<FeedItem> feedItems;

            @Override
            protected Void doInBackground(Void... voids) {
                feedItems = findFromCache(ctx);
                return super.doInBackground(voids);
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                callback.done(feedItems);
                super.onPostExecute(aVoid);
            }

        };
    }

    /**
     * 同步地从本地缓存取出喂养计划条目
     *
     * @param ctx 上下文环境
     * @return 喂养计划条目
     */
    public List<FeedItem> findFromCache(Context ctx) {
        ACache aCache = ACache.get(ctx);
        String json = aCache.getAsString(FeedItem.CACHE_KEY);
        return json == null ? null : JSON.parseArray(json, FeedItem.class);
    }


}
