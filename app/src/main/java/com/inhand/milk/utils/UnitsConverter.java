package com.inhand.milk.utils;

import android.content.Context;

/**
 * UnitsConverter
 * Desc:单位转换工具
 * Team: InHand
 * User:Wooxxx
 * Date: 2015-03-17
 * Time: 12:55
 */
public class UnitsConverter {
    public static int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int Px2Dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }
}
