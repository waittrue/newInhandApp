package com.inhand.milk.dao;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.inhand.milk.App;
import com.inhand.milk.entity.FeedCate;
import com.inhand.milk.entity.FeedItem;
import com.inhand.milk.entity.FeedPlan;
import com.inhand.milk.utils.ACache;

import java.util.List;

/**
 * Created by Administrator on 2015/8/26.
 * 作者：大力
 * 时间：2015/8/26
 * 描述：
 */
public class FeedCateDao {
    public static final String FIND_ORDER = "createdAt";
    private AVQuery<FeedCate> query = AVQuery.getQuery(FeedCate.class);

    /**
     * 从云端异步地根据喂养计划获得该计划下所有喂养计划
     *
     * @param feedPlan 喂养计划
     * @param callback 回调接口
     */
    public void findByFeedCateFromCloud(final FeedPlan feedPlan
            , final FindCallback<FeedCate> callback) {
        query.orderByAscending(FIND_ORDER);
        query.whereEqualTo(FeedItem.FEED_PLAN_KEY, feedPlan);
        query.findInBackground(callback);
    }


    /**
     * 同步地根据喂养计划获得该计划下所有喂养分类
     *
     * @param feedPlan 喂养计划
     * @return 喂养计划条目
     */
    public List<FeedCate> findByFeedPlanFromCloud(final FeedPlan feedPlan) {
        query.orderByAscending(FIND_ORDER);
        query.whereEqualTo(FeedItem.FEED_PLAN_KEY, feedPlan);
        try {
            return query.find();
        } catch (AVException e) {
            e.printStackTrace();
            return null;
        }
    }

    public FeedCate findFromeAcache() {
        ACache aCache = ACache.get(App.getAppContext());
        String json = aCache.getAsString(FeedCate.CACHE_KEY);
        if (json == null)
            return null;
        return JSON.parseObject(json, FeedCate.class);
    }
}
