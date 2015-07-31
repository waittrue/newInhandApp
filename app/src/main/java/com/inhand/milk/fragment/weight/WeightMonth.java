package com.inhand.milk.fragment.weight;

/**
 * Created by Administrator on 2015/7/19.
 * 由于数据接口只能存储month，这里我加这个类，利用存储的的是int
 * 可以通过增加移位来使存储month和day
 */
public class WeightMonth {
    private static final int MonthOffset = 100;

    public static int createbabyMonth(int month, int day) {
        return month * MonthOffset + day;
    }

    public static int getbabyMonth(Weight weight) {
        int num = weight.getMoonAge();
        return num / MonthOffset;
    }

    public static int getbabyDay(Weight weight) {
        int num = weight.getMoonAge();
        return num % MonthOffset;
    }
}
