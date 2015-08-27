package com.inhand.milk.dao;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.inhand.milk.App;
import com.inhand.milk.entity.Baby;
import com.inhand.milk.entity.BabyFeedItem;
import com.inhand.milk.utils.ACache;

import java.util.List;

/**
 * Created by Administrator on 2015/8/26.
 * 作者：大力
 * 时间：2015/8/26
 * 描述：宝宝私有化喂养的详细列表
 */
public class BabyFeedItemDao {
    private AVQuery<BabyFeedItem> query = AVQuery.getQuery(BabyFeedItem.class);
    private static final String ORDER_BY = "time";

    public List<BabyFeedItem> findBabyFeedItemsFromCloud(Baby baby) {
        query.whereEqualTo(BabyFeedItem.BABY_KEY, baby);
        query.orderByAscending(ORDER_BY);
        try {
            List<BabyFeedItem> babyFeedItems = query.find();
            return query.find();
        } catch (AVException e) {
            return null;
        }
    }

    public void saveBabyItemAcache(List<BabyFeedItem> babyFeedItems) {
        if (babyFeedItems == null)
            return;
        String json = JSON.toJSONString(babyFeedItems);
        ACache aCache = ACache.get(App.getAppContext());
        aCache.put(BabyFeedItem.ACACHE_KEY, json);
        Log.i("baby", json);
    }

    public List<BabyFeedItem> getBabyItemFromAcache() {
        String json = ACache.get(App.getAppContext()).getAsString(BabyFeedItem.ACACHE_KEY);
        if (json == null)
            return null;
        List<BabyFeedItem> babyFeedItems = JSON.parseArray(json, BabyFeedItem.class);
        if (babyFeedItems == null || babyFeedItems.isEmpty())
            return null;
        return babyFeedItems;
    }
}
