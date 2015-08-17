package com.inhand.milk.utils;

/**
 * Created by Administrator on 2015/8/10.
 */
public class MilkHelper {
    private static MilkHelper instance = null;

    private MilkHelper() {

    }

    public static MilkHelper getInstance() {
        if (instance == null) {
            synchronized (MilkHelper.class) {
                if (instance == null)
                    instance = new MilkHelper();
            }
        }
        return instance;
    }
}
