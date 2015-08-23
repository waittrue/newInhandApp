package com.inhand.milk.fragment.person_center;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.inhand.milk.App;
import com.inhand.milk.R;
import com.inhand.milk.activity.BluetoothPairedAcivity;
import com.inhand.milk.activity.MilkChooseActivity;
import com.inhand.milk.activity.PersonCenterBabyInfoActivity;
import com.inhand.milk.activity.UserInfoSettingsActivity;
import com.inhand.milk.fragment.TitleFragment;
import com.inhand.milk.ui.CircleImageView;

/**
 * Created by Administrator on 2015/7/7.
 */
public class PersonCenterFragment extends TitleFragment {
    private RelativeLayout userInfo, babyInfo, babyRecord, babyMilk, myDevice, syn, bluetooth;

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
                Toast.makeText(getActivity(), "shezhi", Toast.LENGTH_SHORT).show();
            }
        };
        setTitleview(getResources().getString(R.string.person_center_fragment_title), 0,
                getResources().getString(R.string.person_center_fragment_title_right_text), listener);
        initUserinfo();
        initBabyInfo();
        initSyn();
        initBluetoothPaired();
        initBabyMilk();
        return mView;
    }


    private void initUserinfo() {
        if (userInfo == null)
            userInfo = (RelativeLayout) mView.findViewById(R.id.person_center_info_container);
        userInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), UserInfoSettingsActivity.class);
                startActivity(intent);
            }
        });
        byte[] bytes = App.getCurrentUser().getImageFromAcache();
        if (bytes == null)
            return;
        CircleImageView circleImageView = (CircleImageView) mView.findViewById(R.id.person_center_user_icon);
        circleImageView.setImageDrawable(new BitmapDrawable(BitmapFactory.decodeByteArray(bytes, 0, bytes.length)));
        TextView textView = (TextView) mView.findViewById(R.id.person_center_user_name_text);
        textView.setText(App.getCurrentUser().getNickname());
    }

    private void initBabyInfo() {
        if (babyInfo == null)
            babyInfo = (RelativeLayout) mView.findViewById(R.id.person_center_baby_info_container);
        babyInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), PersonCenterBabyInfoActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initBabyMilk() {
        if (babyMilk == null)
            babyMilk = (RelativeLayout) mView.findViewById(R.id.person_center_baby_milk_container);
        babyMilk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), MilkChooseActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initSyn() {
        if (syn == null)
            syn = (RelativeLayout) mView.findViewById(R.id.person_center_syn_container);
        syn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initBluetoothPaired() {
        if (bluetooth == null)
            bluetooth = (RelativeLayout) mView.findViewById(R.id.person_center_bluetooth_paired_container);
        bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), BluetoothPairedAcivity.class);
                startActivity(intent);
            }
        });
    }
}
