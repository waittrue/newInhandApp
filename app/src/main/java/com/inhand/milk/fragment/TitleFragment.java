package com.inhand.milk.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.inhand.milk.R;

public class TitleFragment extends Fragment{
	protected View mView;
	private static LinearLayout.LayoutParams lp =new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
	public void refresh(){
		
	}
    protected void setTitleview(String text,int type){
        setTitleview(text,type,null,null);
    }
	protected void  setTitleview(String text , int type,Drawable rightIcon,View.OnClickListener righListenner){
		
		switch (type) {
		case 0:
			 MainTitle title =  new MainTitle(text);
             title.setrightIcon(righListenner,rightIcon);
			 LinearLayout layout = (LinearLayout)mView.findViewById(R.id.title);
			 layout.addView(title.getView(this.getActivity()));
			 break;
		case 1:
			 TemperatureMilkTitle title2 =  new TemperatureMilkTitle(text);
             title2.setrightIcon(righListenner,rightIcon);
			 LinearLayout layout2 = (LinearLayout)mView.findViewById(R.id.title);
			 layout2.addView(title2.getView(this.getActivity()),lp);
			 break;
		case 2:
			 BackTitle title3 = new BackTitle(text);
             title3.setrightIcon(righListenner,rightIcon);
			 LinearLayout layout3 = (LinearLayout)mView.findViewById(R.id.title);
			 layout3.addView(title3.getView(this.getActivity()));
			 break;

		default:
			break;
		}
		
		
	}
	protected Fragment  getNextFragment() {
		return null;
	}

	
	protected void gotoNextFragment(){
		FragmentManager manager = (TitleFragment.this).getActivity().getFragmentManager();
		FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.hide(TitleFragment.this);
		fragmentTransaction.add(R.id.Activity_fragments_container, getNextFragment());
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
	}
}
