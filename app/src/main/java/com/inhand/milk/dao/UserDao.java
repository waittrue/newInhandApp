package com.inhand.milk.dao;

import android.content.Context;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.inhand.milk.entity.User;

import java.util.List;

/**
 * UserDao
 * Desc:用户访问对象
 * Team: Rangers
 * Date: 2015/4/15
 * Time: 10:00
 * Created by: Wooxxx
 */
public class UserDao extends BaseDao {
    private AVQuery<User> query;


    public UserDao(Context ctx) {
        super(ctx);
        query = new AVQuery<>(User.CLASS_NAME);
    }

    /**
     * 异步地根据用户名查找用户
     *
     * @param username 待查询用户名
     * @param callback 回调接口
     */
    public void findUserByUsername(String username,
                                          final FindCallback<User> callback) {
        query.whereEqualTo("username", username);
        query.findInBackground(callback);
    }

    /**
     * 同步地根据用户名查找用户
     *
     * @param username 待查询用户名
     * @return callback 回调接口
     */
    public List<User> findUserByUsername(String username) {
        query.whereEqualTo("username", username);
        try {
            return query.find();
        } catch (AVException e) {
            e.printStackTrace();
        }
        return null;
    }
}
