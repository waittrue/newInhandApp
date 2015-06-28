package com.inhand.milk.fragment.health.prompt;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.inhand.milk.R;
import com.inhand.milk.fragment.TitleFragment;
import com.inhand.milk.utils.HealthNutritionDisk;

import java.util.List;

public class Nutrition extends TitleFragment{

    private float[] mWeight;
    private List<String[]> mText;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView =  inflater.inflate(R.layout.health_nutrition, container, false);
		addRing();
		setTitleview(getResources().getString(R.string.health_nutrition_title), 2,null,null);
		//setBackEvent();
		return mView;
	}
    public Nutrition(float[] weight) {
        mWeight = weight;
        mText = null;
    }
    public Nutrition(float[] weight,List<String[]> text) {
        mWeight = weight;
        mText = text;
    }

    private void addRing(){
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        float width= dm.widthPixels * 0.35f;
		LinearLayout linearLayout = (LinearLayout)mView.findViewById(R.id.health_nutrition_sector);
		HealthNutritionDisk ring = new HealthNutritionDisk(getActivity().getApplicationContext(),width,mWeight,mText);

		linearLayout.addView(ring);
		
	}
	
}
