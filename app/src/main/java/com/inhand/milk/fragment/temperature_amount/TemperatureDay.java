package com.inhand.milk.fragment.temperature_amount;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.inhand.milk.R;
import com.inhand.milk.dao.BaseDao;
import com.inhand.milk.dao.OneDayDao;

import com.inhand.milk.entity.OneDay;
import com.inhand.milk.entity.Record;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

public class TemperatureDay extends OnePaper {

	public TemperatureDay(Activity activity,int width, Context context) {
		super(activity, context,width, true);
		// TODO Auto-generated constructor stub
	}


	@Override
	protected void setExcle(Excle excle) {
		// TODO Auto-generated method stub
		excle.setLeftTiltle(mActivty.getResources().getString(R.string.temperature_excle_day_left_title));
		excle.setRightTiltle(mActivty.getResources().getString(R.string.temperature_excle_day_right_title));
		excle.initDayText();
	}

    /**
     * 返回当天温度，温度分为两个<list<float[]>>
     * 一个代表最高温度，一个最低温度
     * 每个温度为     时间 和 温度<float float> 时间3：30 = 3.5
     */
	@Override
	public void  refreshData(final List<List<float[]>> data) {
		// TODO Auto-generated method stub
        String today="";
        Date dt=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        today=sdf.format(dt);
        final OneDayDao oneDayDao=new OneDayDao(mActivty);
        oneDayDao.findOneDayFromDB(today, new BaseDao.DBFindCallback<OneDay>() {
            @Override
            public void done(List<OneDay> oneDays) {
                if (oneDays != null && oneDays.size() > 0) {
                    OneDay oneDay = (OneDay) oneDays.get(0);
                    List<float[]> max_temp_points = new ArrayList<float[]>();
                    List<float[]> min_temp_points = new ArrayList<float[]>();
                    for (Record record : oneDay.getRecords()) {
                        float[] point = new float[2];
                        point[0] = Float.valueOf(record.getBeginTime().replace(":", "."));
                        //Log.d("point0",String.valueOf(point[0]));
                        point[1]=getMaxTemp(record);
                        Log.d("max_temp",String.valueOf(point[1]));
                        max_temp_points.add(point);
                        point = new float[2];
                        point[0] = Float.valueOf(record.getBeginTime().replace(":", "."));
                        point[1]=getMinTemp(record);
                       Log.d("min_temp",String.valueOf(point[1]));
                        min_temp_points.add(point);
                    }
                    //Log.d("point",String.valueOf(points.get(0)[0]));
                    data.clear();
                    data.add(max_temp_points);
                    data.add(min_temp_points);
                }
            }
        });
	}



}
