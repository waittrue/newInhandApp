package com.inhand.milk.fragment.milk_amount_curve;

import android.app.Activity;

import com.inhand.milk.R;

import java.util.ArrayList;
import java.util.List;

public class curveMonth extends com.inhand.milk.fragment.milk_amount_curve.OnePaper {

	public curveMonth(Activity activity, int width) {
		super(activity,width);
		// TODO Auto-generated constructor stub
	}

    /**
     * 返回当天前30天，温度分为两个<list<float[]>>
     * 当天最高温度，当天最低温度
     * 每个温度为     < float>
     */
    /*
	@Override
	public void refreshData(List<List<float[]>> data) {
		// TODO Auto-generated method stub
        OneDayDao oneDayDao=new OneDayDao(mActivty);
        Date dt=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        List<Date>days= DateHelper.getMonthDays(dt);
        int len=0;
        data.clear();
        final List<float[]> max_temp_points = new ArrayList<float[]>();
        final List<float[]> min_temp_points = new ArrayList<float[]>();
        for (Date date:days){
            String monthday=sdf.format(date);
            Log.d("monthday", monthday);
            oneDayDao.findOneDayFromDB(monthday, new BaseDao.DBFindCallback<OneDay>() {
                @Override
                public void done(List<OneDay> oneDays) {
                    if (oneDays != null && oneDays.size() > 0) {
                        OneDay oneDay=oneDays.get(0);
                        float[] point = new float[1];
                        //Log.d("point0",String.valueOf(point[0]));
                        point[0]=getMaxTemp(oneDay);
                        Log.d("max_temp",String.valueOf(point[0]));
                        max_temp_points.add(point);
                        point = new float[1];
                        point[0]=getMinTemp(oneDay);
                        Log.d("min_temp",String.valueOf(point[0]));
                        min_temp_points.add(point);
                        min_temp_points.add(point);
                    }
                }
            });
        }
        data.add(max_temp_points);
        data.add(min_temp_points);
	}
*/
    @Override
    protected void setTemperatureExcle(com.inhand.milk.fragment.milk_amount_curve.Excle excle) {
        excle.setLeftTiltle(mActivty.getResources().
                getString(R.string.temperature_excle_month_left_title));
        excle.setRightTiltle(mActivty.getResources().
                getString(R.string.temperature_excle_month_right_title));
        excle.initMonthText();
    }

    @Override
    protected void setAmountExcle(com.inhand.milk.fragment.milk_amount_curve.Excle excle) {
        excle.initMonthText();
        excle.setLeftTiltle(mActivty.getResources().
                getString(R.string.milk_excle_month_left_title));
        excle.setRightTiltle(mActivty.getResources().
                getString(R.string.milk_excle_month_right_title));
    }

    @Override
    protected void refreshTemperatureData(List<List<float[]>> data) {
        data.clear();
        temperatureMin = Integer.MAX_VALUE;
        temperatureMax = Integer.MIN_VALUE;
        List<float[]> maxTemperature = new ArrayList<>();
        List<float[]> minTemperature = new ArrayList<>();
        for(int i=0;i<30;i++){
            float[] maxTemp = new float[2];
            float[] minTemp = new float[2];
            maxTemp[0] = (float)i/30;
            maxTemp[1] = (float)Math.random()*20+20;
            maxTemperature.add(maxTemp);
            minTemp[0] = (float)i/30;
            minTemp[1] = (float)Math.random()*10+10;
            minTemperature.add(minTemp);

            if(maxTemp[1] > temperatureMax)
                temperatureMax = (int)maxTemp[1];
            if(minTemp[1] < temperatureMin)
                temperatureMin = (int)minTemp[1];
        }
        data.add(maxTemperature);
        data.add(minTemperature);
        temperatureMin = 10;
        temperatureMax = 50;
    }

    @Override
    protected void refreshAmountData(List<List<float[]>> data) {
        data.clear();
        amountMin = Integer.MAX_VALUE;
        amountMax = Integer.MIN_VALUE;
        List<float[]> list = new ArrayList<>();
        for(int i=0;i<30;i++){
            float[] temp = new float[2];
            temp[0] = (float)i/30;
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
