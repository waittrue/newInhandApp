package com.inhand.milk.fragment.person_center;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.inhand.milk.R;
import com.inhand.milk.activity.BluetoothPairedAcivity;
import com.inhand.milk.activity.PersonCenterBabyInfoActivity;
import com.inhand.milk.activity.UserInfoSettingsActivity;
import com.inhand.milk.fragment.TitleFragment;

/**
 * Created by Administrator on 2015/7/7.
 */
public class PersonCenterFragment extends TitleFragment {
    private RelativeLayout userInfo, babyInfo, babyRecord, babyMilk, myDevice, syn;
    private View.OnTouchListener onTouchListener;
    private int clickColor, unclickColor = Color.WHITE;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_person_center, container, false);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Intent intent = new Intent(getActivity(), MilkAmountCurveActivity.class);
                getActivity().startActivity(intent);
                */
                Toast.makeText(getActivity(), "shezhi", 1000).show();
            }
        };
        setTitleview(getResources().getString(R.string.person_center_fragment_title), 0,
                getResources().getString(R.string.person_center_fragment_title_right_text), listener);
        clickColor = getResources().getColor(R.color.common_settings_item_click_bg_color);
        onTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundColor(clickColor);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundColor(unclickColor);
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    v.setBackgroundColor(unclickColor);
                }
                return false;
            }
        };
        initUserinfo();
        initBabyInfo();
        initSyn();
        return mView;
    }


    private void initUserinfo() {
        if (userInfo == null)
            userInfo = (RelativeLayout) mView.findViewById(R.id.person_center_info_container);
        userInfo.setOnTouchListener(onTouchListener);
        userInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), UserInfoSettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initBabyInfo() {
        if (babyInfo == null)
            babyInfo = (RelativeLayout) mView.findViewById(R.id.person_center_baby_info_container);
        babyInfo.setOnTouchListener(onTouchListener);
        babyInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), PersonCenterBabyInfoActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initSyn() {
        if (syn == null)
            syn = (RelativeLayout) mView.findViewById(R.id.person_center_syn_container);
        syn.setOnTouchListener(onTouchListener);
        syn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), BluetoothPairedAcivity.class);
                startActivity(intent);
            }
        });
    }
}
