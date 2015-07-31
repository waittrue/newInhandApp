package com.inhand.milk.entity;

import android.content.Context;

import com.inhand.milk.utils.LocalSaveTask;

/**
 * DBSaving
 * Desc:
 * Team: InHand
 * User: Wooxxx
 * Date: 2015-07-30
 * Time: 17:09
 */
public interface DBSaving<T extends Base> {

    /**
     * 异步地将数据存至数据库
     *
     * @param ctx      上下文环境
     * @param callback 回调接口
     */
    public void saveInDB(final Context ctx,
                         final LocalSaveTask.LocalSaveCallback<T> callback);

    /**
     * 同步地将数据存至数据库
     *
     * @param ctx 上下文环境
     */
    public void saveInDB(final Context ctx);
}
