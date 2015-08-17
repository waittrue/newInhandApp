package com.inhand.milk.STANDAR;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2015/6/3.
 * 提供整个app中的标准值。
 */
public class Standar {
    public static final String LASTDRINK_KEY = "lastDrinkintentkey";
    public final static int DRINK_MAX_DURATION = 20;
    public final static int DRINK_MIN_DURATION = 10;
    public final static int DRINK_MAX_AMOUNT = 200;
    public final static int DRINK_MIN_AMOUNT = 100;
    public final static float DRINK_BEGIN_MAX_TP = (float) 40.5;
    public final static float DRINK_BEGIN_MIN_TP = (float) 38.4;
    public final static float DRINK_END_MAX_TP = (float) 34.5;
    public final static float DRINK_END_MIN_TP = (float) 30.4;
    public final static int DRINK_MIN_SCORE = 60;
    public static final String SOFT_VERSION = "1.0";
    public static final String HARD_VERSION = "1.0";
    final static float AMOUNT_SCORE = 55;
    final static float TEMPERATURE_SCORE = 35;
    final static float TIME_SCORE = 10, TEMPREATURE_HIGH = 40, TEMPREATURE_LOW = 37, STANDAR_TIME = 30;
    public static DecimalFormat TEMPERATURE_FORMAT = new DecimalFormat("##.#");
    public static DecimalFormat AMOUNT_FORMAT = new DecimalFormat("###");
    public static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");

    public static float getRecord(float advise, float amount, float temperatureHigh, float temperatureLow, float time) {
        float ratio, sum = 0;
        if (advise > amount)
            ratio = amount / advise;
        else
            ratio = advise / amount;
        sum += AMOUNT_SCORE * ratio;
        //Log.i("amount " ,String.valueOf(sum));
        ratio = 0;
        if (temperatureHigh > TEMPREATURE_HIGH)
            ratio += (temperatureHigh - TEMPREATURE_HIGH) / TEMPREATURE_HIGH;
        if (temperatureLow < TEMPREATURE_LOW)
            ratio += (TEMPREATURE_LOW - temperatureLow) / TEMPREATURE_LOW;
        ratio = ratio > 1 ? 1 : ratio;
        sum += TEMPERATURE_SCORE * (1 - ratio);
        //Log.i("amount tempreature " ,String.valueOf(sum));
        if (STANDAR_TIME > time)
            ratio = time / STANDAR_TIME;
        else
            ratio = STANDAR_TIME / time;
        sum += TIME_SCORE * ratio;
        //Log.i("amount tempreature time" ,String.valueOf(sum));
        return sum;
    }

}
