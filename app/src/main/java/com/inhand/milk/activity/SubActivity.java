package com.inhand.milk.activity;

import com.inhand.milk.R;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

public abstract class SubActivity extends BaseActivity{

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_minor);
		FragmentManager manager = getFragmentManager();
		FragmentTransaction mTransaction = manager.beginTransaction();
		Fragment mFragment = initFragment();
		mTransaction.add(R.id.Activity_fragments_container,mFragment);
		mTransaction.commit();	
	}
	protected abstract Fragment initFragment();
}
