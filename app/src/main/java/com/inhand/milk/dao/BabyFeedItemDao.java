package com.inhand.milk.dao;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.inhand.milk.entity.Baby;
import com.inhand.milk.entity.BabyFeedItem;

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
}
