package com.inhand.milk.utils;

import android.os.AsyncTask;

import com.inhand.milk.entity.Base;

/**
 * LocalGetTask
 * Desc:
 * Team: InHand
 * User: Wooxxx
 * Date: 2015-07-28
 * Time: 22:03
 */
public class LocalGetTask extends AsyncTask<Void, Void, Void> {

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
    public interface LocalGetCallback<T extends Base> {
        void done(T result);
    }
}
