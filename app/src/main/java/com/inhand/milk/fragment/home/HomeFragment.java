package com.inhand.milk.fragment.home;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inhand.milk.App;
import com.inhand.milk.R;
import com.inhand.milk.STANDAR.Standar;
import com.inhand.milk.dao.OneDayDao;
import com.inhand.milk.entity.OneDay;
import com.inhand.milk.entity.Record;
import com.inhand.milk.fragment.TitleFragment;

import java.util.List;

public class HomeFragment extends TitleFragment{
	
	private Circle circle;
	private float  width;
    private String lastTString,adviseTString;
    private String lastAmountString,adviseAmountString;
    private int score;
    private TextView adviseAmount,adviseT,lastAmount,lastT;
    private Record record;
	@Override  
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
        // ---Inflate the layout for this fragment---  
        mView = inflater.inflate(R.layout.home, container, false);
        setTitleview(getString(R.string.home_title_text), 0);
        setHome(mView);
        return mView;
    }  
    private void initData(){
        List<OneDay> oneDays = new OneDayDao(App.getAppContext()).findAllFromDB(1);
        if(oneDays == null || oneDays.size() == 0){
            lastTString = adviseTString = lastAmountString = adviseAmountString
                    = getResources().getString(R.string.public_no_data);
            score = -1;
            return ;
        }
        List<Record> records = oneDays.get(0).getRecords();
        if(records == null || records.size() == 0){
            lastTString = adviseTString = lastAmountString = adviseAmountString
                    = getResources().getString(R.string.public_no_data);
            score = -1;
            return;
        }
        record  = records.get(0);
        lastTString = Standar.TeamperatureFormat.format( (record.getBeginTemperature() + record.getEndTemperature())/2 )
                +"°C";
        lastAmountString = String.valueOf( record.getVolume())+"ml";
        adviseAmountString = String.valueOf(record.getAdviceVolumn())+"ml";
        adviseTString = "35°C";
        score = record.getScore();
        lastAmount.setText(lastAmountString);
        lastT.setText(lastTString);
        adviseT.setText(adviseTString);
        adviseAmount.setText(adviseAmountString);
    }
    private void initViews(){
        lastAmount = (TextView)mView.findViewById(R.id.home_left_amount);
        lastT = (TextView)mView.findViewById(R.id.home_left_temperature);
        adviseAmount = (TextView)mView.findViewById(R.id.home_right_amount);
        adviseT = (TextView)mView.findViewById(R.id.home_right_temperature);
        lastAmount.setTypeface(App.Typeface_arial);
        lastT.setTypeface(App.Typeface_arial);
        adviseAmount.setTypeface(App.Typeface_arial);
        adviseT.setTypeface(App.Typeface_arial);
    }
	private void setHome(View view){
        initViews();
        initData();
		DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        width= dm.widthPixels;
	    float r =width/4.15f;
		circle= new Circle(this.getActivity().getApplicationContext(),r);
        circle.setScore(score);
		LinearLayout linearCircle = (LinearLayout)view.findViewById(R.id.temperature_milk_circle_container);
		linearCircle.addView(circle);
		circle.start();
		setOthers(view);
		
	}
	
	private void setOthers(View view){
		
		TextView tuisong = (TextView) view.findViewById(R.id.tuisong);
		LinearLayout.LayoutParams paramsss = (LinearLayout.LayoutParams) tuisong.getLayoutParams();
		paramsss.width = (int)(width*0.85);
		tuisong.setLayoutParams(paramsss);
	}
	
	public void refresh(){
		initData();
        circle.start();
	}
}
