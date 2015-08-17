package com.inhand.milk.dao;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.inhand.milk.entity.FeedItem;
import com.inhand.milk.entity.Powder;
import com.inhand.milk.entity.PowderTip;
import com.inhand.milk.utils.ACache;
import com.inhand.milk.utils.LocalFindTask;
import com.inhand.milk.utils.LocalSaveTask;

import org.json.JSONArray;

import java.util.List;

/**
 * PowderTipDao
 * Desc: 数据访问层---奶粉使用说明
 * Team: InHand
 * User: Wooxxx
 * Date: 2015-07-27
 * Time: 21:36
 */
public class PowderTipDao {

    public static final String FIND_SORT = "sort";
    private AVQuery<PowderTip> query = AVQuery.getQuery(PowderTip.class);

    /**
     * 异步地获得某奶粉对应的所有使用方法
     *
     * @param powder   奶粉
     * @param callback 回调接口
     */
    public void findByPowderFromCloud(final Powder powder, final FindCallback<PowderTip> callback) {
        query.orderByAscending(FIND_SORT);
        query.whereEqualTo(PowderTip.POWDER_KEY, powder);
        query.findInBackground(callback);
    }

    /**
     * 同步地获得某奶粉对应的所有使用方法
     *
     * @param powder 奶粉
     * @return 奶粉列表
     */
    public List<PowderTip> findByPowderFromCloud(final Powder powder) {
        query.orderByAscending(FIND_SORT);
        query.whereEqualTo(PowderTip.POWDER_KEY, powder);
        try {
            return query.find();
        } catch (AVException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 异步地缓存奶粉使用说明列表到本地
     *
     * @param ctx      上下文环境
     * @param tips     奶粉说明列表
     * @param callback 回调接口
     */
    public void cache(
            final Context ctx
            , final List<PowderTip> tips
            , final LocalSaveTask.LocalSaveCallback<PowderTip> callback) {
        LocalSaveTask task = new LocalSaveTask(callback) {
            @Override
            protected Void doInBackground(Void... voids) {
                cache(ctx, tips);
                return super.doInBackground(voids);
            }

        };
        task.execute();
    }

    /**
     * 同步地缓存奶粉使用说明列表到本地
     *
     * @param ctx  上下文环境
     * @param tips 待缓存奶粉使用说明列表
     */
    public void cache(final Context ctx, final List<PowderTip> tips) {
        ACache aCache = ACache.get(ctx);
        aCache.remove(FeedItem.CACHE_KEY);
        JSONArray array = new JSONArray();
        for (PowderTip tip : tips) {
            array.put(tip.toJSONObject().toString());
        }
        aCache.put(PowderTip.CACHE_KEY, array);
    }

    /**
     * 异步地从缓存获取奶粉使用说明
     *
     * @param ctx      上下文环境
     * @param callback 回调接口
     */
    public void findFromCache(final Context ctx, final LocalFindTask.LocalFindCallback<PowderTip> callback) {
        LocalFindTask task = new LocalFindTask() {
            List<PowderTip> tips;

            @Override
            protected Void doInBackground(Void... voids) {
                tips = findFromCache(ctx);
                return super.doInBackground(voids);
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                callback.done(tips);
                super.onPostExecute(aVoid);
            }
        };
        task.execute();
    }

    /**
     * 同步地从缓存获取奶粉使用说明
     *
     * @param ctx 上下文环境
     * @return 奶粉使用说明列表
     */
    public List<PowderTip> findFromCache(final Context ctx) {
        ACache aCache = ACache.get(ctx);
        String json = aCache.getAsString(PowderTip.CACHE_KEY);
        return json == null ? null : JSON.parseArray(json, PowderTip.class);
    }

}
