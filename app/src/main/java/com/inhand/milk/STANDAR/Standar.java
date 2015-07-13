package com.inhand.milk.STANDAR;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2015/6/3.
 * 提供整个app中的标准值。
 */
public class Standar {
    public static final String LastRecord = "lastRecord";
    public static final String MilkAmountRecord = "milkamountrecord";
    public static final String LastDrinkIntentKey = "lastDrinkintentkey";
    public final static int drinkMaxDuration = 20;
    public final static int drinkMinDuration = 10;
    public final static int drinkMaxAmount = 200;
    public final static int drinkMinAmount = 100;
    public final static float drinkBeginMaxTp = (float) 40.5;
    public final static float drinkBeginMinTp = (float) 38.4;
    public final static float drinkEndMaxTp = (float) 34.5;
    public final static float drinkEndMinTp = (float) 30.4;
    public final static int drinkMinScore = 60;
    final static float AMOUNTSCORE = 55;
    final static float TEMPERATURESCORE = 35;
    final static float TIMESCORE = 10, TEMPREATUREHIGH = 40, TEMPREATURELOW = 37, STANDARTIME = 30;
    public static DecimalFormat TeamperatureFormat = new DecimalFormat("##.#");
    public static DecimalFormat AmountFormat = new DecimalFormat("###");
    public static SimpleDateFormat OneDayDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static float getRecord(float advise, float amount, float temperatureHigh, float temperatureLow, float time) {
        float ratio, sum = 0;
        if (advise > amount)
            ratio = amount / advise;
        else
            ratio = advise / amount;
        sum += AMOUNTSCORE * ratio;
        //Log.i("amount " ,String.valueOf(sum));
        ratio = 0;
        if (temperatureHigh > TEMPREATUREHIGH)
            ratio += (temperatureHigh - TEMPREATUREHIGH) / TEMPREATUREHIGH;
        if (temperatureLow < TEMPREATURELOW)
            ratio += (TEMPREATURELOW - temperatureLow) / TEMPREATURELOW;
        ratio = ratio > 1 ? 1 : ratio;
        sum += TEMPERATURESCORE * (1 - ratio);
        //Log.i("amount tempreature " ,String.valueOf(sum));
        if (STANDARTIME > time)
            ratio = time / STANDARTIME;
        else
            ratio = STANDARTIME / time;
        sum += TIMESCORE * ratio;
        //Log.i("amount tempreature time" ,String.valueOf(sum));
        return sum;
    }
}
