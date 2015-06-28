package com.inhand.milk.helper;

import android.content.Context;
import android.os.AsyncTask;

import com.avos.avoscloud.AVException;
import com.inhand.milk.dao.DeviceDao;
import com.inhand.milk.dao.OneDayDao;
import com.inhand.milk.dao.StatisticsDao;

/**
 * SyncHelper
 * Desc:同步助手
 * Team: InHand
 * User:Wooxxx
 * Date: 2015-03-17
 * Time: 08:56
 */
public class SyncHelper {

    /**
     * 从蓝牙抓取数据
     */
    public static void syncBluetooth() {

    }

    /**
     * 与云端同步数据
     * @param ctx         上下文环境
     * @param callback 同步回调接口
     */
    public static void syncCloud(final Context ctx, final SyncCallback callback) {
        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                //同步OneDay
                OneDayDao oneDayDao = new OneDayDao(ctx);
                //同步设备
                DeviceDao deviceDao= new DeviceDao(ctx);
                //同步statistic
                StatisticsDao statisticsDao=new StatisticsDao(ctx);
                try {
                    oneDayDao.syncCloud();
                    deviceDao.syncCloud();
                    statisticsDao.syncCloud();
                    return null;
                } catch (AVException e) {
                    e.printStackTrace();
                    return e;
                }
            }

            @Override
            protected void onPostExecute(Object o) {
                callback.done((AVException) o);
            }
        };
        task.execute();

    }

    /**
     * 同步回调接口
     */
    public interface SyncCallback {
        public void done(AVException e);
    }
}
