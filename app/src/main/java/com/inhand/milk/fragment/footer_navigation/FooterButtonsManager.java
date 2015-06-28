package com.inhand.milk.fragment.footer_navigation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.inhand.milk.R;
import com.inhand.milk.fragment.TitleFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class FooterButtonsManager implements OnClickListener{
	private ImageButton mCurrent;
	private HashMap<ImageButton, Fragment> map = new HashMap<ImageButton, Fragment>();
	private List<ImageButton> buttons = new ArrayList<ImageButton>();
	private FragmentManager mFragmentManager;
	
	public FooterButtonsManager(FragmentManager fragmentManager){
		mFragmentManager = fragmentManager;
	}
	
	public void addButtons( ImageButton button , Fragment fragment){
		if (button == null || fragment == null)
			throw new NullPointerException();
		map.put(button, fragment);
		buttons.add(button);
		button.setOnClickListener(this);
		FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
		fragmentTransaction.add(R.id.Activity_fragments_container,fragment); 
		fragmentTransaction.hide(fragment);
		fragmentTransaction.commit();
	}
	
	public void setStartFragment(ImageButton button){
		mCurrent = button;
		FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
		fragmentTransaction.show( map.get(mCurrent));
		fragmentTransaction.commit();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		for (ImageButton button : buttons) {
			if (v == button){
				if (v != mCurrent){
					FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction(); 	
					Fragment fragment = map.get(button);
					fragmentTransaction.hide(map.get(mCurrent));
					fragmentTransaction.show( fragment);
					fragmentTransaction.commit();
					( (TitleFragment)fragment ).refresh();
					mCurrent = button;
				}
				break;
			}
		}
	}
}
