package com.inhand.milk.dao;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.inhand.milk.entity.Baby;
import com.inhand.milk.entity.BabyInfo;
import com.inhand.milk.utils.ACache;
import com.inhand.milk.utils.LocalFindTask;

import org.json.JSONArray;

import java.util.List;

/**
 * BabyInfoDao
 * Desc: 数据访问层--宝宝信息
 * Team: InHand
 * User: Wooxxx
 * Date: 2015-07-27
 * Time: 10:05
 */
public class BabyInfoDao {
    private static final String SROT_BY = "createdAt";
    private AVQuery<BabyInfo> query = AVQuery.getQuery(BabyInfo.class);


    /**
     * 根据月份异步地从缓存获取出宝宝信息
     *
     * @param ctx      上下文环境
     * @param date     查询日期
     * @param callback 回调接口
     */
    public void findByDateFromCache(final Context ctx,
                                    final String date,
                                    final LocalFindTask.LocalFindCallback<BabyInfo> callback) {

        LocalFindTask task = new LocalFindTask() {
            List<BabyInfo> babyInfos;

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                callback.done(babyInfos);

            }

            @Override
            protected Void doInBackground(Void... voids) {
                babyInfos = findByDateFromCache(ctx, date);
                return super.doInBackground(voids);
            }

        };
        task.execute();
    }

    /**
     * 根据月份同步地从缓存取出宝宝信息
     *
     * @param ctx  上下文环境
     * @param date 月份
     * @return 宝宝信息
     */
    public List<BabyInfo> findByDateFromCache(final Context ctx,
                                              final String date) {
        ACache aCache = ACache.get(ctx);
        // 根据月份取出宝宝信息
        JSONArray array = aCache.getAsJSONArray(BabyInfo.CACHE_KEY_PREFIX + date);
        return array == null ? null :
                JSON.parseArray(array.toString(), BabyInfo.class);
    }

    /**
     * 从云端取出指定宝宝的身高体重等信息
     *
     * @param baby     指定宝宝
     * @param callback 回调接口
     */
    public void findByBabyFromCloud(final Baby baby
            , final FindCallback<BabyInfo> callback) {
        query.whereEqualTo(BabyInfo.BABY_KEY, baby);
        query.orderByDescending(SROT_BY);
        query.findInBackground(callback);
    }

    /**
     * 同步地从云端获得指定宝宝的身高体重等信息
     *
     * @param baby 指定宝宝
     */
    public List<BabyInfo> findByBabyFromCloud(Baby baby) {
        query.whereEqualTo(BabyInfo.BABY_KEY, baby);
        query.orderByDescending(SROT_BY);
        try {
            return query.find();
        } catch (AVException e) {
            e.printStackTrace();
            return null;
        }
    }
}
