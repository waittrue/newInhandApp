package com.inhand.milk.fragment.milk_amount_curve;

import android.app.Activity;

import com.inhand.milk.App;
import com.inhand.milk.R;
import com.inhand.milk.STANDAR.Standar;
import com.inhand.milk.dao.OneDayDao;
import com.inhand.milk.entity.OneDay;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class curveWeek extends com.inhand.milk.fragment.milk_amount_curve.OnePaper {
    private static final int TotalDays = 7;
    private int year, month, day;
    private Calendar startCalender;

    public curveWeek(Activity activity, int width) {
		super(activity,width);
		// TODO Auto-generated constructor stub
        startCalender = Calendar.getInstance();
        startCalender.add(Calendar.DAY_OF_MONTH, -TotalDays + 1);
        year = startCalender.get(Calendar.YEAR);
        month = startCalender.get(Calendar.MONTH) + 1;
        day = startCalender.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    protected void setTemperatureExcle(com.inhand.milk.fragment.milk_amount_curve.Excle excle) {
        excle.setLeftTiltle(mActivty.getResources().
                getString(R.string.temperature_excle_week_left_title));
        excle.setRightTiltle(mActivty.getResources().
                getString(R.string.temperature_excle_week_right_title));
        excle.initWeekText();
    }

    @Override
    protected void setAmountExcle(com.inhand.milk.fragment.milk_amount_curve.Excle excle) {
        excle.initWeekText();
        excle.setLeftTiltle(mActivty.getResources().
                getString(R.string.milk_excle_week_left_title));
        excle.setRightTiltle(mActivty.getResources().
                getString(R.string.milk_excle_week_right_title));

    }

    @Override
    protected void refreshExcleData(List<List<float[]>> temperatureData, List<List<float[]>> amountData) {
        temperatureData.clear();
        amountData.clear();
        amountMin = Integer.MAX_VALUE;
        amountMax = Integer.MIN_VALUE;
        OneDayDao oneDayDao = new OneDayDao();
        List<OneDay> oneDays = oneDayDao.findFromDB(App.getAppContext(),TotalDays);
        if (oneDays == null) {
            return;
        }
        List<float[]> maxTemperature = new ArrayList<>();
        List<float[]> minTemperature = new ArrayList<>();
        List<float[]> volume = new ArrayList<>();
        String time;
        for (OneDay oneDay : oneDays) {
            time = oneDay.getDate();
            if (isOutOfdex(time))
                break;
            float[] maxTemp = new float[2];
            float[] minTemp = new float[2];
            float[] v = new float[2];
            v[0] = maxTemp[0] = minTemp[0] = day2float(time);
            v[1] = oneDay.getVolume();
            if (amountMax < v[1])
                amountMax = (int) v[1];
            if (amountMin > v[1])
                amountMin = (int) v[1];
            maxTemp[1] = getMaxTemp(oneDay);
            minTemp[1] = getMinTemp(oneDay);
            maxTemperature.add(maxTemp);
            minTemperature.add(minTemp);
            volume.add(v);
        }
        temperatureData.add(maxTemperature);
        temperatureData.add(minTemperature);
        amountData.add(volume);
        amountMax = (amountMax - amountMin) / 4 + amountMax;
        amountMin = amountMin - (amountMax - amountMin) / 4;
        if (amountMin < 0)
            amountMin = 0;
    }

    private float day2float(String time) {
        Calendar calendar = Calendar.getInstance();
        Date date1 = null;
        try {
            date1 = Standar.DATE_FORMAT.parse(time);
        } catch (ParseException e) {
            return 0;
        }
        calendar.setTime(date1);
        long diff;
        diff = (calendar.getTimeInMillis() - startCalender.getTimeInMillis()) / (1000 * 3600 * 24);
        diff = diff + 1;
        return (float) (diff) / (TotalDays - 1);
    }

    private boolean isOutOfdex(String time) {
        String[] str = time.split("-");
        int tempYear = Integer.parseInt(str[0]);
        int tempMonth = Integer.parseInt(str[1]);
        int tempDay = Integer.parseInt(str[2]);
        if (tempYear < year)
            return true;
        if (tempYear > year)
            return false;
        if (tempMonth < month)
            return true;
        if (tempMonth > month)
            return false;
        if (tempDay < day)
            return true;
        if (tempDay > day)
            return false;
        return false;
    }
}
