package com.inhand.milk.dao;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.inhand.milk.entity.Device;
import com.inhand.milk.entity.User;
import com.inhand.milk.utils.ACache;
import com.inhand.milk.utils.LocalGetTask;

import org.json.JSONObject;

import java.util.List;

/**
 * DeviceDao
 * Desc: 数据访问层--设备
 * Date: 2015/6/3
 * Time: 7:23
 * Created by: Wooxxx
 */
public class DeviceDao {

    private AVQuery<Device> query = AVQuery.getQuery(Device.class);


    /**
     * 同步地从云端获取用户的设备
     *
     * @param user 指定用户
     * @return 当前用户的所有设备
     */
    public List<Device> findByUserFromCloud(User user) {
        query.whereEqualTo(Device.USER_KEY, user);
        try {
            return query.find();
        } catch (AVException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 异步地从云端获取当前用户的设备信息
     *
     * @param user     指定用户
     * @param callback 回调接口
     */
    public void findByUserFromCloud(
            User user,
            final FindCallback<Device> callback) {
        query.whereEqualTo(Device.USER_KEY, user);
        query.findInBackground(callback);
    }


    /**
     * 异步地从本地缓存获得正在使用的设备
     *
     * @param ctx      上下文环境
     * @param callback 回调接口
     */
    public void getFromCache(
            final Context ctx,
            final LocalGetTask.LocalGetCallback<Device> callback) {
        LocalGetTask task = new LocalGetTask() {
            Device device;

            @Override
            protected Void doInBackground(Void... voids) {
                device = getFromCache(ctx);
                return super.doInBackground(voids);
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                callback.done(device);
                super.onPostExecute(aVoid);
            }
        };
        task.execute();
    }


    /**
     * 同步地从缓存获得设备
     *
     * @param ctx 上下文环境
     * @return 当前所用设备
     */
    public Device getFromCache(final Context ctx) {
        ACache aCache = ACache.get(ctx);
        JSONObject obj = aCache.getAsJSONObject(Device.CACHE_KEY);
        return obj == null ? null
                : JSON.parseObject(obj.toString(), Device.class);
    }


}
