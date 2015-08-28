package com.inhand.milk.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/8/28.
 * 作者：大力
 * 时间：2015/8/28
 * 描述： 观察者的管理类
 */
public abstract class Observable<T> {
    protected List<T> mObservable = new ArrayList<>();

    abstract public void notifyChanged();

    public void registerObserver(T a) {
        mObservable.add(a);
    }

    public void unregisterObserver(T a) {
        mObservable.remove(a);
    }
}
