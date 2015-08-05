package com.inhand.milk.entity;

import android.content.Context;

import com.inhand.milk.utils.LocalSaveTask;

/**
 * CacheSaving
 * Desc: 缓存接口
 * Team: InHand
 * User: Wooxxx
 * Date: 2015-07-30
 * Time: 16:47
 */
public interface CacheSaving<T extends Base> {

    /**
     * 异步存入缓存方法
     *
     * @param ctx      上下文环境
     * @param callback 回调接口
     */
    public void saveInCache(final Context ctx, final LocalSaveTask.LocalSaveCallback<T> callback);

    /**
     * 同步存入缓存方法
     *
     * @param ctx 上下文环境
     */
    public void saveInCache(final Context ctx);

}
