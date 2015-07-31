package com.inhand.milk.dao;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.inhand.milk.entity.Baby;
import com.inhand.milk.entity.User;

import java.util.List;

/**
 * BabyDao
 * Desc: 数据访问层--宝宝
 * Team: InHand
 * User:Wooxxx
 * Date: 2015-03-17
 * Time: 08:44
 */
public class BabyDao {
    private AVQuery<Baby> query = AVQuery.getQuery(Baby.class);


    /**
     * 异步地从云端查询某用户的所有宝宝
     *
     * @param user     用户
     * @param callback 回调接口
     */
    public void findByUserFromCloud(final User user, final FindCallback<Baby> callback) {
        initFindByUserFromCloud(user);
        query.findInBackground(callback);
    }

    /**
     * 同步地从云端查询某用户的所有宝宝
     *
     * @param user 用户
     */
    public List<Baby> findByUserFromCloud(final User user) {
        initFindByUserFromCloud(user);
        try {
            return query.find();
        } catch (AVException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void initFindByUserFromCloud(final User user) {
        query.whereEqualTo(Baby.USER_KEY, user);
        query.include(Baby.STATISTICS_KEY);
        query.include(Baby.POWDER_KEY);
        query.include(Baby.FEED_PLAN_KEY);
    }

}
