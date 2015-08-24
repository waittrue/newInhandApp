package com.inhand.milk.fragment.milk_amount_curve;

import android.app.Activity;

import com.inhand.milk.R;
import com.inhand.milk.STANDAR.Standar;
import com.inhand.milk.entity.OneDay;
import com.inhand.milk.entity.Record;
import com.inhand.milk.helper.RecordHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class curveDay extends com.inhand.milk.fragment.milk_amount_curve.OnePaper {
    private static final int VolunMinDiff = 80;
    private String today;

    public curveDay(Activity activity, int width) {
        super(activity, width);
        Date dt = new Date();
        today = Standar.DATE_FORMAT.format(dt);
    }


    @Override
    protected void setTemperatureExcle(Excle excle) {
        excle.setLeftTiltle(mActivty.getResources().getString(R.string.temperature_excle_day_left_title));
        excle.setRightTiltle(mActivty.getResources().getString(R.string.temperature_excle_day_right_title));
        excle.initDayText();
    }

    @Override
    protected void setAmountExcle(Excle excle) {
        excle.initDayText();
        excle.setLeftTiltle(mActivty.getResources().
                getString(R.string.milk_excle_day_left_title));
        excle.setRightTiltle(mActivty.getResources().
                getString(R.string.milk_excle_day_right_title));
    }

    @Override
    protected void refreshExcleData(List<List<float[]>> temperatureData, List<List<float[]>> amountData) {
        updateTemperatureData(temperatureData, amountData);
    }

    private void updateTemperatureData(final List<List<float[]>> temperatureData, final List<List<float[]>> volumeData) {
        amountMin = Integer.MAX_VALUE;
        amountMax = Integer.MIN_VALUE;
        temperatureData.clear();
        volumeData.clear();
        OneDay oneday = RecordHelper.getInstance().getOneday(new Date());
        if (oneday == null)
            return;
        List<float[]> maxTemperature = new ArrayList<>();
        List<float[]> minTemperature = new ArrayList<>();
        List<float[]> volume = new ArrayList<>();
        for (Record record : oneday.getRecords()) {
            float[] maxTemp = new float[2];
            float[] minTemp = new float[2];
            float[] v = new float[2];
            float time = time2float(record.getBeginTime());
            v[0] = minTemp[0] = maxTemp[0] = time;
            minTemp[1] = record.getEndTemperature();
            maxTemp[1] = record.getBeginTemperature();
            v[1] = record.getVolume();
            if (amountMax < v[1])
                amountMax = (int) v[1];
            if (amountMin > v[1])
                amountMin = (int) v[1];
            maxTemperature.add(maxTemp);
            minTemperature.add(minTemp);
            volume.add(v);
        }
        temperatureData.add(maxTemperature);
        temperatureData.add(minTemperature);
        volumeData.add(volume);


        int diff = amountMax - amountMin;
        amountMax = diff / 4 + amountMax;
        amountMin = amountMin - diff / 4;
        if (amountMin < 0)
            amountMin = 0;

    }

    private float time2float(String time) {
        String[] str = time.split(":");
        int hour = Integer.parseInt(str[0]);
        int min = Integer.parseInt(str[1]);
        return (float) min / 60 / 24 + (float) hour / 24;
    }
}
