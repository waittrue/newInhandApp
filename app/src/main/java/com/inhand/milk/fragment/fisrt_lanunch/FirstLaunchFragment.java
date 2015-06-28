package com.inhand.milk.fragment.fisrt_lanunch;

import com.inhand.milk.R;
import com.inhand.milk.activity.FirstLanunchActivity;
import com.inhand.milk.entity.Baby;
import com.inhand.milk.utils.firstlanunch.FirstLanunchBottom;

import android.R.integer;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public abstract class FirstLaunchFragment extends Fragment {
	protected FirstLanunchBottom lanunchBottom;
    protected Baby baby;
	protected float width;
	private static String extraInfo;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        width= dm.widthPixels;
        
		lanunchBottom = ((FirstLanunchActivity)this.getActivity()).getFirstLanunchBottom();
        baby=((FirstLanunchActivity)this.getActivity()).getBaby();
		return  super.onCreateView(inflater, container, savedInstanceState);
	}
	
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		inAnimation();
	}
	

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
			
	}


	protected  void enterNextFragmet(){
		FragmentManager fragmentManager = getActivity().getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		Fragment fragment = nextFragment();
		if (fragment !=null){
			fragmentTransaction.hide(this);
			fragmentTransaction.add(R.id.Activity_fragments_container,fragment,null);
			//fragmentTransaction.show(fragment);
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();
			((FirstLanunchActivity)getActivity()).moveNextDots();
		}
		//lanunchBottom.setClickable(false);
		lanunchBottom.clearAllClick();
		
	}
	
	
	protected void enterPreFragment(){
		FragmentManager fragmentManager = getActivity().getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentManager.popBackStack();
		//fragmentTransaction.commit();
		((FirstLanunchActivity)getActivity()).movePreDots();
		//Toast.makeText(getActivity().getApplicationContext(), "enterPreFragment", 1000).show();
		//lanunchBottom.setClickable(false);
		lanunchBottom.clearAllClick();
	}
	
	protected ObjectAnimator getAnimator(View v,int time){
		ObjectAnimator animator = new ObjectAnimator();
		animator.setTarget(v);
		animator.setDuration(time);
		return animator;
	}
	protected void alphAnimation(View v,float f,int time){
		ObjectAnimator animator = getAnimator(v,time);
		animator.setPropertyName("alpha");
		v.setAlpha(1-f);
		animator.setFloatValues(f);
		animator.start();
		
	}
	protected void scaleAnimation(View v,float f,int time){
		ObjectAnimator animator = getAnimator(v,time);
		v.setScaleX(1-f);
		animator.setFloatValues(f);
		animator.setPropertyName("scaleX");
		animator.start();
	}
	protected void setTitle(String str){
		((FirstLanunchActivity)getActivity()).setTitle(str);
	}
	public void setExtraInfo(String str){
		extraInfo = str;
	}
	public String getExtraInfo(){
		return extraInfo;
	}
	protected abstract Fragment nextFragment();
	protected abstract void inAnimation();
	protected abstract void outAnimation();
}
