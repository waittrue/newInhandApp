package com.inhand.milk.fragment.temperature_amount.details_once;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inhand.milk.R;
import com.inhand.milk.entity.Record;
import com.inhand.milk.fragment.TitleFragment;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailsOnceFragment extends TitleFragment{
    Record record;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView =  inflater.inflate(R.layout.temperature_amount_details_once, null);
        record=(Record)getArguments().getSerializable("record");
		//setBack();
		setTitleview(getString(R.string.details_once_title_string), 2,null,null);
		setInfo();
		return mView;
	}
	
	
	private void setInfo(){
		TextView textView = (TextView)mView.findViewById(R.id.details_once_real_amount_num);
		textView.setText(getRealAmount());
		
		textView = (TextView)mView.findViewById(R.id.details_once_advise_amount_num);
		textView.setText(getAdviseAmount());
		
		textView = (TextView)mView.findViewById(R.id.details_once_temperature_start_num);
		textView.setText( getStartTemperature());
		
		textView = (TextView)mView.findViewById(R.id.details_once_temperature_end_num);
		textView.setText( getEndTemperature());
		
		textView = (TextView)mView.findViewById(R.id.details_once_health_num);
		textView.setText( getHealthNum());
		
		textView = (TextView)mView.findViewById(R.id.details_once_end_time_num);
		textView.setText( getEndTime());
	}

    /**
     * 返回点击的那一次的所有相关信息
     * 重写下面所有函数
     * 应该看一下就明白怎么返回
     *
     *这边没有输入具体时间,开始我忘了写，我们暂时定输入参数为Date类型,date
     *看效果你就先默认今天
     *
     */
    private String getRealAmount(){
        Date date;
        String realAmount=record.getVolume()+"ml";
        return realAmount;
    }
    private String getAdviseAmount(){
        Date date;
        return String.valueOf(  (int)(Math.random()*20+140))+"ml";
    }
    private String getStartTemperature(){
        Date date;
        String sTemp=record.getBeginTemperature()+"°";
        return sTemp;
    }
    private String getEndTemperature(){
        Date date;
        String eTemp=record.getEndTemperature()+"°";
        return eTemp;
    }
    private String getEndTime(){
        Date date=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String string = sdf.format(date)+" "+record.getBeginTime();
        return string;
    }
    private String getHealthNum(){
        return String.valueOf( (int)(Math.random()*10) );
    }
}
