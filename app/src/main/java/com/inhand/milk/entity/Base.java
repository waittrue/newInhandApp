package com.inhand.milk.entity;

import android.content.Context;
import android.os.AsyncTask;

import com.avos.avoscloud.AVObject;

/**
 * Base
 * Desc:实体基类
 * Team: InHand
 * User:Wooxxx
 * Date: 2015-03-03
 * Time: 16:06
 */
public class Base extends AVObject {
    public static final String VERSION_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 缓存异步存储任务
     */
    class CacheSavingTask extends AsyncTask {
        private final Context ctx;
        private final CacheSavingCallback cacheSavingCallback;



        CacheSavingTask(final Context ctx, final CacheSavingCallback cacheSavingCallback) {
            this.ctx = ctx;
            this.cacheSavingCallback = cacheSavingCallback;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o) {
            cacheSavingCallback.done();
        }
    }

    /**
     * 缓存回调接口
     */
    public interface CacheSavingCallback {
        public void done();
    }

    /**
     * 数据库存储任务
     */
    class DBSavingTask extends AsyncTask {
        private final DBSavingCallback callback;
        private final Context ctx;

        DBSavingTask(final Context ctx, final DBSavingCallback callback) {
            this.callback = callback;
            this.ctx = ctx;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o) {
            callback.done();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            return null;
        }
    }

    public interface DBSavingCallback {
        public void done();
    }


    public String getTable() {
        return null;
    }

}
