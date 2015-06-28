package com.inhand.milk.fragment;


import android.app.Activity;
import android.view.View;

import com.inhand.milk.R;
import com.inhand.milk.activity.MainActivity;

public class MainTitle extends BaseTitle{

	private String mTitle;
	public MainTitle( String title) {
		mTitle = title;
		
	}
	public View getView(Activity activity) {
		// TODO Auto-generated method stu
		return setView(activity, R.layout.title_main,
				activity.getResources().getDrawable( R.drawable.menu_entry),mTitle,
				((MainActivity)activity).getMyOnclickListener());
	}
	
}
