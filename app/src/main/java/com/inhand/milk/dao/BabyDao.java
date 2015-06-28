package com.inhand.milk.dao;

import android.content.Context;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.inhand.milk.entity.Baby;
import com.inhand.milk.entity.OneDay;

import java.util.List;

/**
 * BabyDao
 * Desc:
 * Team: InHand
 * User:Wooxxx
 * Date: 2015-03-17
 * Time: 08:44
 */
public class BabyDao extends BaseDao {
    public static AVQuery<Baby> query = AVQuery.getQuery(Baby.class);

    public BabyDao(Context ctx) {
        super(ctx);
    }


    void updateOrSaveInCloud(OneDay oneDay) throws AVException {

    }

    /**
     * 异步地查询某用户的所有宝宝
     *
     * @param user     用户
     * @param callback 回调接口
     */
    public static void findBabiesByUser(AVUser user, FindCallback<Baby> callback) {
        query.whereEqualTo(Baby.USER_KEY, user);
        query.findInBackground(callback);
    }

    /**
     * 同步地查询某用户的所有宝宝
     *
     * @param user 用户
     */
    public static List<Baby> findBabiesByUser(AVUser user) {
        query.whereEqualTo(Baby.USER_KEY, user);
        try {
            return query.find();
        } catch (AVException e) {
            e.printStackTrace();
        }
        return null;
    }
}
