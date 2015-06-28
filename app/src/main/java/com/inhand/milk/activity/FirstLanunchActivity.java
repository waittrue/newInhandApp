package com.inhand.milk.activity;

import com.inhand.milk.R;
import com.inhand.milk.entity.Baby;
import com.inhand.milk.fragment.fisrt_lanunch.ChooseParentsFragment;
import com.inhand.milk.utils.firstlanunch.FirstLanunchBottom;
import com.inhand.milk.utils.firstlanunch.SmallDotsTab;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FirstLanunchActivity extends BaseActivity {
	
	private FirstLanunchBottom bottomView;
	private SmallDotsTab smallDotsTab;
	private final int num = 6;
	private String extraInfo;
    private Baby baby;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first_lanunch);
		baby=new Baby();
		DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float width= dm.widthPixels;
        
		FragmentManager manager = getFragmentManager();
		FragmentTransaction mTransaction = manager.beginTransaction();
		Fragment mFragment = new ChooseParentsFragment();
		mTransaction.add(R.id.Activity_fragments_container,mFragment);
		mTransaction.commit();	
		
		bottomView = new FirstLanunchBottom(getApplicationContext());
		LinearLayout linearLayout =  (LinearLayout) findViewById(R.id.first_lanunch_bottom_container);
		linearLayout.addView(bottomView,
				new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		bottomView.setVisibility(View.INVISIBLE);
		
		linearLayout  = (LinearLayout)findViewById(R.id.first_launch_small_bots_container);
		smallDotsTab = new SmallDotsTab(getApplicationContext(), num, width/66, width/3/14);
		linearLayout.addView(smallDotsTab);
		
	}
    @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		bottomView.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(bottomView.getHeight() == 0);
				bottomView.setVisibility(View.INVISIBLE);
				Animation translatAnimation = new TranslateAnimation(0,0,bottomView.getMeasuredHeight(),0);
				translatAnimation.setDuration(1000);
				translatAnimation.setFillAfter(true);
				bottomView.startAnimation(translatAnimation);
				
			}
		}, 300);
		
		
	}

	public FirstLanunchBottom getFirstLanunchBottom(){
		return bottomView;
	}
	public Baby getBaby(){return this.baby;}
	public void moveNextDots(){
		smallDotsTab.setNextDots();
	}
	public void movePreDots(){
		smallDotsTab.setPreDots();
	}
	
	public void setTitle(String str ){
		TextView view = (TextView)findViewById(R.id.first_launch_title_text);
		view.setText(str);
	}

}
