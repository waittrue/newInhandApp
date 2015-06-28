package com.inhand.milk.fragment;

import android.app.Activity;
import android.view.View;

import com.inhand.milk.R;
import com.inhand.milk.activity.MainActivity;

public class TemperatureMilkTitle extends BaseTitle{

	private View view;
	private String mTitle;
	
	public  TemperatureMilkTitle(String title) {
		mTitle = title;
		// TODO Auto-generated constructor stub
	}
	
	public View getView(Activity activity) {
		// TODO Auto-generated method stub
		return setView(activity, R.layout.title_temperatrue_milk,
					activity.getResources().getDrawable( R.drawable.menu_entry),
					mTitle,  ((MainActivity)activity).getMyOnclickListener());
	}
}
