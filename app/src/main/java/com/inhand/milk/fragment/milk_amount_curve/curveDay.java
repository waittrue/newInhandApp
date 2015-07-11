package com.inhand.milk.fragment.milk_amount_curve;

import android.app.Activity;

import com.inhand.milk.R;

import java.util.ArrayList;
import java.util.List;

public class curveDay extends com.inhand.milk.fragment.milk_amount_curve.OnePaper {

	public curveDay(Activity activity, int width) {
		super(activity,width);
		// TODO Auto-generated constructor stub
	}

    /**
     * 返回当天温度，温度分为两个<list<float[]>>
     * 一个代表最高温度，一个最低温度
     * 每个温度为     时间 和 温度<float float> 时间3：30 = 3.5
     */
    /*
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
*/

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
    protected void refreshTemperatureData(List<List<float[]>> data) {
        data.clear();
        temperatureMin = Integer.MAX_VALUE;
        temperatureMax = Integer.MIN_VALUE;
        List<float[]> maxTemperature = new ArrayList<>();
        List<float[]> minTemperature = new ArrayList<>();
        for(int i=0;i<8;i++){
            float[] maxTemp = new float[2];
            float[] minTemp = new float[2];
            maxTemp[0] = (float)i*3/24;
            maxTemp[1] = (float)Math.random()*20+20;
            maxTemperature.add(maxTemp);
            minTemp[0] = (float)i*3/24;
            minTemp[1] = (float)Math.random()*10+10;
            minTemperature.add(minTemp);

            if(maxTemp[1] > temperatureMax)
                temperatureMax = (int)maxTemp[1];
            if(minTemp[1] < temperatureMin)
                temperatureMin = (int)minTemp[1];
        }
        temperatureMin = 10;
        temperatureMax = 50;
        data.add(maxTemperature);
        data.add(minTemperature);

    }

    @Override
    protected void refreshAmountData(List<List<float[]>> data) {
        data.clear();
        amountMin = Integer.MAX_VALUE;
        amountMax = Integer.MIN_VALUE;
        List<float[]> list = new ArrayList<>();
        for(int i=0;i<8;i++){
            float[] temp = new float[2];
            temp[0] =(float)i*3/24;
            temp[1] = (float)Math.random()*50+60;
            list.add(temp);

            if(temp[1] > amountMax)
                amountMax = (int)temp[1];
            if(temp[1] < amountMin)
                amountMin = (int)temp[1];
        }
        data.add(list);
        amountMax = 140;
        amountMin = 0;
    }
}
