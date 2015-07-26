package com.inhand.milk.dao;

import android.content.Context;

import com.inhand.milk.entity.FeedItem;

/**
 * FeedItemDao
 * Desc: 喂养计划条目数据访问
 * Team: InHand
 * User: Wooxxx
 * Date: 2015-07-26
 * Time: 10:29
 */
public class FeedItemDao extends BaseDao<FeedItem> {
    public FeedItemDao(Context ctx) {
        super(ctx);
    }
}
