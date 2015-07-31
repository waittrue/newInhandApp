package com.inhand.milk.utils;

import android.os.AsyncTask;

import com.inhand.milk.entity.Base;

import java.util.List;

/**
 * LocalFindTask
 * Desc: 本地异步查询任务
 * Team: InHand
 * User: Wooxxx
 * Date: 2015-07-28
 * Time: 19:37
 */
public class LocalFindTask extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... voids) {
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    /**
     * 本地查询回调接口
     *
     * @param <T>
     */
    public interface LocalFindCallback<T extends Base> {
        void done(List<T> results);
    }
}
