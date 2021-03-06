package com.inhand.milk.fragment.health.prompt;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inhand.milk.R;
import com.inhand.milk.fragment.TitleFragment;
import com.inhand.milk.ui.HealthNutritionDisk;

import java.util.List;

public class Nutrition extends TitleFragment {

    private float[] mWeight;
    private List<String[]> mText;

    public Nutrition(float[] weight) {
        mWeight = weight;
        mText = null;
    }

    public Nutrition(float[] weight, List<String[]> text) {
        mWeight = weight;
        mText = text;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        mView = inflater.inflate(R.layout.health_nutrition, container, false);
        addRing();
        setTitleview(getResources().getString(R.string.health_nutrition_title), 2);
        return mView;
    }

    private void addRing() {
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        float width = dm.widthPixels * 0.35f;
        LinearLayout linearLayout = (LinearLayout) mView.findViewById(R.id.health_nutrition_sector);
        HealthNutritionDisk ring = new HealthNutritionDisk(getActivity().getApplicationContext(), width, mWeight, mText);
        linearLayout.addView(ring);

        TextView bottomTextView = (TextView) mView.findViewById(R.id.health_nutrition_total_num_text);
        //等待数据加入
        //bottomTextView.setText();
    }

}
