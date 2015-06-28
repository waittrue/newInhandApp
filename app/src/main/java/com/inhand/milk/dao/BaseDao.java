package com.inhand.milk.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.inhand.milk.App;
import com.inhand.milk.entity.Base;
import com.inhand.milk.entity.Device;
import com.inhand.milk.entity.OneDay;
import com.inhand.milk.helper.DBHelper;

import java.util.List;

/**
 * BaseDao
 * Desc:基础Dao类
 * Team: InHand
 * User:Wooxxx
 * Date: 2015-03-04
 * Time: 21:24
 */
public class BaseDao<T extends Base> {
    protected DBHelper dbHelper;
    protected SQLiteDatabase db;
    protected AVQuery<T> query;

    public BaseDao(Context ctx) {
        dbHelper = DBHelper.getInstance(ctx);
        db = dbHelper.openDatabase();
    }

    /**
     * 同步地根据Id从云端获取数据
     *
     * @param objectId 对象Id
     * @return 对应数据
     */
    public T findOneFromCloudByObjectId(String objectId) {
        query = AVQuery.getQuery(getRealClass());
        try {
            return query.get(objectId);
        } catch (AVException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 异步地根据Id从云端获取数据
     *
     * @param objectId 对象Id
     * @param callback 回调接口
     */
    public void findOneFromCloudByObjectId(String objectId,
                                           GetCallback<T> callback) {
        query = AVQuery.getQuery(getRealClass());
        query.getInBackground(objectId, callback);
    }


    /**
     * 同步地从云端获取所有数据
     *
     * @param where   查询目标
     * @param value   查询值
     * @param orderBy 排序
     * @param asc     是否正序
     * @param limit   最大获取数量，如果为0，代表传入所有
     */
    public List<T> findAllFromCloud(
            int limit,
            String where,
            String value,
            String orderBy,
            Boolean asc) {
        query = AVQuery.getQuery(getRealClass());
        query.whereEqualTo(where, value);
        if (orderBy != null) {
            if (asc)
                query.orderByAscending(orderBy);
            else
                query.orderByDescending(orderBy);
        }
        if (limit != 0)
            query.limit(limit);
        try {
            return query.find();
        } catch (AVException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 异步地从云端获取所有数据
     *
     * @param limit    最大数量，如果为0，代表传入所有
     * @param where    查询目标
     * @param value    查询值
     * @param orderBy  排序
     * @param asc      是否正序
     * @param callback 查询完后的回调函数
     */
    public void findAllFromCloud(
            int limit,
            String where,
            String value,
            String orderBy,
            Boolean asc,
            FindCallback<T> callback) {
        query = AVQuery.getQuery(getRealClass());
        query.whereEqualTo(where, value);
        if (orderBy != null) {
            if (asc)
                query.orderByAscending(orderBy);
            else
                query.orderByDescending(orderBy);
        }
        if (limit != 0)
            query.limit(limit);
        query.findInBackground(callback);
    }


    /**
     * 同步地在云端更新或者储存一个AVObject
     *
     * @param obj 待储存更新对象
     */
    public void updateOrSaveInCloud(T obj) throws AVException {

    }


    /**
     * 异步地在云端更新或者储存一个AVObject
     *
     * @param obj      待储存更新对象
     * @param callback 回调函数
     */
    public void updateOrSaveInCloud(T obj, FindCallback<T> callback) {

    }

    /**
     * 同步的从本地抓取所有
     *
     * @param limit 最大数量，如果为0，代表传入所有
     * @return 本地所有数据
     */
    public List<T> findAllFromDB(int limit) {
        return null;
    }

    /**
     * 异步地从本地抓取所有数据
     *
     * @param limit    最大数量，如果为0，代表传入所有
     * @param callback 回调接口
     */
    public void fidnAllFromDB(int limit, FindCallback<T> callback) {

    }

    /**
     * 同步地更新AVObject至本地数据库
     *
     * @param obj 目标更新对象
     */
    public void updateOrSaveInDB(T obj) {

    }

    /**
     * 数据库查询异步任务
     */
    public class DBFindTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
        }
    }

    /**
     * 数据库查找回调接口
     *
     * @param <T>
     */
    public interface DBFindCallback<T> {
        public void done(List<T> results);
    }

    protected Class<T> getRealClass() {
        return null;
    }
}
