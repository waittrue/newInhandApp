package com.inhand.milk.fragment.home;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inhand.milk.R;
import com.inhand.milk.fragment.TitleFragment;

public class HomeFragment extends TitleFragment{
	
	private Circle circle;
	private float  width;
	
	@Override  
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
        // ---Inflate the layout for this fragment---  
        mView = inflater.inflate(R.layout.home, container, false);
        setTitleview(getString(R.string.home_title_text), 0,null,null);
        setHome(mView);
        return mView;
    }  

	
	private void setHome(View view){
		DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        width= dm.widthPixels;
     
	    float r =width/4;
		circle= new Circle(this.getActivity().getApplicationContext(),r);
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
		circle.start();
	}
	
	
	
}
