package com.inhand.milk.fragment.temperature_amount;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.inhand.milk.R;
import com.inhand.milk.dao.BaseDao;
import com.inhand.milk.dao.OneDayDao;
import com.inhand.milk.entity.OneDay;
import com.inhand.milk.entity.Record;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

public class AmountDay extends OnePaper {
	public AmountDay(Activity activity, Context context,int width) {
		super(activity, context, width,false);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void setExcle(Excle excle) {
		// TODO Auto-generated method stub
		excle.initDayText();
		excle.setLeftTiltle(mActivty.getResources().
				getString(R.string.milk_excle_day_left_title));
		excle.setRightTiltle(mActivty.getResources().
				getString(R.string.milk_excle_day_right_title));
	}
	/*
	 * (non-Javadoc)
	 * @see com.inhand.milk.fragment.temperature_amount.OnePaper#refreshData(java.util.List)
	 * 必须把返回来的值赋值到data 里面去；
	 * 返回的是     今天      每次喝奶 的时间点和奶量<float  float> 时间为3:30 为3.30   奶量单位为ml
	 *
	 *参照下面
	 */

	@Override
	public void refreshData(final List<List<float[]>> data) {
		// TODO Auto-generated method stub
        String today="";
        Date dt=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        today=sdf.format(dt);
        final OneDayDao oneDayDao=new OneDayDao(mActivty);
        AVQuery<OneDay> query = new AVQuery<OneDay>("Milk_OneDay");
        query.whereEqualTo("date", today);
        query.findInBackground(new FindCallback<OneDay>() {
            public void done(List<OneDay> oneDays, AVException e) {
                if (e == null) {
                    //Log.d("成功", "查询到" + oneDays.size() + " 条符合条件的数据");
                } else {
                    //Log.d("失败", "查询错误: " + e.getMessage());
                }
            }
        });
/*        oneDayDao.findOneDayFromCloud(today, new FindCallback<OneDay>() {
            @Override
            public void done(List<OneDay> oneDays, AVException e) {
                if (e == null) {
                    Log.d("成功", "查询到" + oneDays.size() + " 条符合条件的数据");
                } else {
                    Log.d("失败", "查询错误: " + e.getMessage());
                }
                OneDay oneDay=(OneDay)oneDays.get(0);
                Log.d("OneDayJson",oneDay.getJSONArray(OneDay.RECORDS_KEY).toString());
                Log.d("List_Size",oneDays.size()+"");
                Record record=oneDay.getRecords().get(0);
                //Log.d("record",record);
                //Log.d("Milk_Volume",String.valueOf(oneDay.getRecords().get(0).getVolume()));
            }
        });*/

        oneDayDao.findOneDayFromDB(today,new BaseDao.DBFindCallback<OneDay>() {
            @Override
            public void done(List<OneDay> oneDays) {
                if(oneDays!=null&&oneDays.size()>0){
                    OneDay oneDay=(OneDay)oneDays.get(0);
                    List<float[]> points = new ArrayList<float[]>();
                    for(Record record:oneDay.getRecords()){
                        float []point=new float[2];
                        point[0]=Float.valueOf(record.getBeginTime().replace(":","."));
                        //Log.d("point0",String.valueOf(point[0]));
                        point[1]=record.getVolume();
                        points.add(point);
                    }
                    //Log.d("point",String.valueOf(points.get(0)[0]));
                    data.clear();
                    data.add(points);
                }
            }
        });
//        Log.d("today",today);
//
//		data.clear();
//		List<float[]> points = new ArrayList<float[]>();
//        if(oneDay!=null){
//            Log.d("Milk_Volume",String.valueOf(oneDay.getRecords().get(0).getVolume()));
//            for(int i=0;i<oneDay.getRecords().size();i++){
//                Record record=oneDay.getRecords().get(i);
//                float []point=new float[2];
//                point[0]=Float.valueOf(record.getBeginTime().replace(":","."));
//                point[1]=record.getVolume();
//                points.add(point);
//            }
//        }
//		data.add(points);
	}

	
}
