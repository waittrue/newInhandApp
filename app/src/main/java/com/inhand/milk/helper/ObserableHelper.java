package com.inhand.milk.helper;

import android.util.Log;

import com.inhand.milk.utils.Observable;
import com.inhand.milk.utils.Observer;

/**
 * Created by Administrator on 2015/8/28.
 * 作者：大力
 * 时间：2015/8/28
 * 描述：在这个类里面加入基本的观察者分发类，用于通知相应的页面进行刷新更改。
 */
public class ObserableHelper {
    private final Mobservable mobservable = new Mobservable();

    public void registerObserver(Observer observer) {
        mobservable.registerObserver(observer);
    }

    public void unregisterPbserver(Observer observer) {
        mobservable.unregisterObserver(observer);
    }

    public void notifyDataChanged() {
        mobservable.notifyChanged();
    }

    //这里继承相应的类，实现相应的分发方法
    private class Mobservable extends Observable<Observer> {
        @Override
        public void notifyChanged() {
            synchronized (mObservable) {
                Log.i("obserableHelper", String.valueOf(mObservable.size()));
                for (int i = mObservable.size() - 1; i >= 0; i--) {
                    mObservable.get(i).onChanged();
                }
            }
        }
    }
}
