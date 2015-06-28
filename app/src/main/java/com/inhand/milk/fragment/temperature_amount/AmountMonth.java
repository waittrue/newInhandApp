package com.inhand.milk.fragment.temperature_amount;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import com.inhand.milk.R;
import com.inhand.milk.dao.BaseDao;
import com.inhand.milk.dao.OneDayDao;
import com.inhand.milk.entity.OneDay;
import com.inhand.milk.entity.Record;
import com.inhand.milk.helper.DateHelper;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

public class AmountMonth extends OnePaper {
	
	public AmountMonth(Activity activity, Context context,int width) {
		super(activity, context, width,false);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void setExcle(Excle excle) {
		// TODO Auto-generated method stub
		excle.initMonthText();
		excle.setLeftTiltle(mActivty.getResources().
				getString(R.string.milk_excle_month_left_title));
		excle.setRightTiltle(mActivty.getResources().
				getString(R.string.milk_excle_month_right_title));
	}
    /**
     * 返回以当天未起点，30天的每天饮奶的奶量：
     * 必须赋值给data 每天奶量为<float>
     */

    /**
     * 返回以当天未起点，30天的每天饮奶的奶量：
     * 必须赋值给data 每天奶量为<float>
     */

    @Override
    public void refreshData(final List<List<float[]>> data) {
        // TODO Auto-generated method stub
        OneDayDao oneDayDao=new OneDayDao(mActivty);
        Date dt=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        List<Date>days= DateHelper.getMonthDays(dt);
        int len=0;
        data.clear();
        final List<float[]> points = new ArrayList<float[]>();
        for (Date date:days){
            String monthday=sdf.format(date);
            Log.d("monthday",monthday);
            oneDayDao.findOneDayFromDB(monthday, new BaseDao.DBFindCallback<OneDay>() {
                @Override
                public void done(List<OneDay> oneDays) {
                    if (oneDays != null && oneDays.size() > 0) {
                        float point[]=new float[1];
                        OneDay oneday=oneDays.get(0);
                        point[0]=100;
                        points.add(point);
                    }
                }
            });
        }
        data.add(points);
    }

}
