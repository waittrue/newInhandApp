package com.inhand.milk.fragment.Eating;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inhand.milk.App;
import com.inhand.milk.R;
import com.inhand.milk.fragment.TitleFragment;
import com.inhand.milk.ui.ButtonA;
import com.inhand.milk.ui.ButtonB;

/**
 * Created by Administrator on 2015/8/1.
 */
public class EatingCustomFixFragment extends TitleFragment {
    private TextView chooseFood,chooseType;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // return super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.eating_custom_fix, container, false);
        setTitleview(getResources().getString(R.string.eating_plan_custom_title), 2);

        initChooseType();
        return mView;
    }
    private void initChooseType(){
        chooseFood = (TextView)mView.findViewById(R.id.eating_custom_fix_choose_food_text);
        chooseType = (TextView)mView.findViewById(R.id.eating_custom_fix_choose_type_textview);
        int height = App.getWindowHeight(getActivity()) - App.getStatusHeight(getActivity());
        height = height -getResources().getDimensionPixelOffset(R.dimen.main_title_height) -
                getResources().getDimensionPixelOffset(R.dimen.eating_custom_fix_time_type_title_container_height) *2 -
                getResources().getDimensionPixelOffset(R.dimen.eating_custom_fix_bottom_height) -
                getResources().getDimensionPixelOffset(R.dimen.eating_custom_fix_bottom_button_container_height);
        height = height /3*1;
        int width = App.getWindowWidth(getActivity()) ;
        ImageView line = (ImageView)mView.findViewById(R.id.eating_custom_fix_choose_type_divider);
        line.getLayoutParams().height = (int)(height*0.8f);

        ImageView milkIcon  = (ImageView)mView.findViewById(R.id.eating_custom_fix_milk_icon);
        RelativeLayout.LayoutParams milkIconLp =(RelativeLayout.LayoutParams) milkIcon.getLayoutParams();
        ImageView foodIcon  = (ImageView)mView.findViewById(R.id.eating_custom_fix_food_icon);
        RelativeLayout.LayoutParams foodIconLp =(RelativeLayout.LayoutParams) foodIcon.getLayoutParams();
        milkIconLp.height = (int)(height/1.5f);
        milkIconLp.width = milkIconLp.height;
        foodIconLp.height = milkIconLp.height;
        foodIconLp.width = milkIconLp.width;

        TextView milkTextView = (TextView)mView.findViewById(R.id.eating_custom_fix_milk_textview);
        RelativeLayout.LayoutParams milkTextViewlp =(RelativeLayout.LayoutParams) milkTextView.getLayoutParams();
        TextView foodTextView = (TextView)mView.findViewById(R.id.eating_custom_fix_food_textview);
        RelativeLayout.LayoutParams foodTextViewlp =(RelativeLayout.LayoutParams) foodTextView.getLayoutParams();
        milkTextViewlp.width = (width/2/5);
        milkTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int)(height/5.5f));
        foodTextViewlp.width = milkTextViewlp.width ;
        foodTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (height / 5.5f));

        final CheckBox milkCheckBox = (CheckBox)mView.findViewById(R.id.eating_custom_fix_milk_checkbox);
        RelativeLayout.LayoutParams milkCheckBoxLp =(RelativeLayout.LayoutParams) milkCheckBox.getLayoutParams();
        final CheckBox foodCheckBox = (CheckBox)mView.findViewById(R.id.eating_custom_fix_food_checkbox);
        RelativeLayout.LayoutParams foodCheckBoxLp =(RelativeLayout.LayoutParams) foodCheckBox.getLayoutParams();
        milkCheckBoxLp.width = width/2/5;
        milkCheckBoxLp.height = width/2/5;
        foodCheckBoxLp.width = milkCheckBoxLp.width;
        foodCheckBoxLp.height = milkCheckBoxLp.height;


        milkCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(milkCheckBox.isChecked() == true){
                    foodCheckBox.setChecked(false);
                    chooseFood.setVisibility(View.GONE);
                    milkCheckBox.setClickable(false);
                    foodCheckBox.setClickable(true);
                    chooseType.setText(getResources().getString(R.string.eating_custom_fix_choose_result_milk_doc));
                }
            }
        });
        milkCheckBox.setClickable(false);
        chooseFood.setVisibility(View.GONE);

        foodCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (foodCheckBox.isChecked() == true) {
                    milkCheckBox.setChecked(false);
                    chooseFood.setVisibility(View.VISIBLE);
                    milkCheckBox.setClickable(true);
                    foodCheckBox.setClickable(false);
                    chooseType.setText(getResources().getString(R.string.eating_custom_fix_choose_result_food_doc));
                }
            }
        });
        initBottomButton();
    }
    private  void initBottomButton(){
        ButtonA buttonA = (ButtonA)mView.findViewById(R.id.eating_custom_fix_cancle_button);
        ButtonB buttonB = (ButtonB)mView.findViewById(R.id.eating_custom_fix_save_button);
        buttonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        buttonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                //save
            }
        });
    }
}
