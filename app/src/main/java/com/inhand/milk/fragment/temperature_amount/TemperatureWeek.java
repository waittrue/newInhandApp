package com.inhand.milk.fragment.temperature_amount;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.inhand.milk.R;
import com.inhand.milk.dao.BaseDao;
import com.inhand.milk.dao.OneDayDao;
import com.inhand.milk.entity.OneDay;
import com.inhand.milk.helper.DateHelper;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

public class TemperatureWeek extends OnePaper {

	public TemperatureWeek(Activity activity,int width, Context context) {
		super(activity, context,width, true);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void setExcle(Excle excle) {
		// TODO Auto-generated method stub
		excle.initWeekText();
		excle.setLeftTiltle(mActivty.getResources().
				getString(R.string.temperature_excle_week_left_title));
		excle.setRightTiltle( mActivty.getResources().
				getString(R.string.temperature_excle_week_right_title));
	}

    /**
     * 返回当天前7天，温度分为两个<list<float[]>>
     * 当天最高温度，当天最低温度
     * 每个温度为     < float>
     */

    @Override
    public void refreshData(List<List<float[]>> data) {
        // TODO Auto-generated method stub
        OneDayDao oneDayDao = new OneDayDao(mActivty);
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<Date> days = DateHelper.getWeekDays(dt);
        int len = 0;
        data.clear();
        final List<float[]> max_temp_points = new ArrayList<float[]>();
        final List<float[]> min_temp_points = new ArrayList<float[]>();
        for (Date date : days) {
            String weekday = sdf.format(date);
            Log.d("weekday", weekday);
            oneDayDao.findOneDayFromDB(weekday, new BaseDao.DBFindCallback<OneDay>() {
                @Override
                public void done(List<OneDay> oneDays) {
                    if (oneDays != null && oneDays.size() > 0) {
                        OneDay oneDay = oneDays.get(0);
                        float[] point = new float[1];
                        //Log.d("point0",String.valueOf(point[0]));
                        point[0] = getMaxTemp(oneDay);
                        Log.d("max_temp", String.valueOf(point[0]));
                        max_temp_points.add(point);
                        point = new float[1];
                        point[0] = getMinTemp(oneDay);
                        Log.d("min_temp", String.valueOf(point[0]));
                        min_temp_points.add(point);
                    }
                }
            });
        }
        data.add(max_temp_points);
        data.add(min_temp_points);
    }

}
