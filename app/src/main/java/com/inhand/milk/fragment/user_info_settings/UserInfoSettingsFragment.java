package com.inhand.milk.fragment.user_info_settings;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inhand.milk.R;
import com.inhand.milk.activity.UserInfoSettingsActivity;
import com.inhand.milk.fragment.TitleFragment;
import com.inhand.milk.utils.PopupWindowSelected;

/**
 * Created by Administrator on 2015/7/2.
 */
public class UserInfoSettingsFragment extends TitleFragment{
    private RelativeLayout head,name,sex,city,telephone,email;
    private ImageView headImageview;
    private TextView sexTextview,nameTextView,telephoneTextView,emailTextView,cityTextView;
    private String man,woman;
    private int clickColor,unclickColor = Color.WHITE;
    private PopupWindowSelected headPopupWiondow,sexPopupWindow;
    private View.OnTouchListener backgroundListener;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            mView = inflater.inflate(R.layout.user_info_settings, container, false);
            setTitleview(getResources().getString(R.string.user_info_title), 2);
            initView(mView);
            initTextViews();
            return mView;
    }
    private void initView(View view){
        clickColor = getResources().getColor(R.color.common_settings_item_click_bg_color);
        headImageview =(ImageView)view.findViewById(R.id.user_info_headimage_imageview);

        sexTextview = (TextView)view.findViewById(R.id.user_info_sex_textview);
        nameTextView = (TextView)view.findViewById(R.id.user_info_name_textview);
        telephoneTextView = (TextView)view.findViewById(R.id.user_info_telephone_textview);
        emailTextView = (TextView)view.findViewById(R.id.user_info_email_textview);
        cityTextView = (TextView)view.findViewById(R.id.user_info_city_textview);
        backgroundListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundColor(clickColor);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundColor(unclickColor);
                }
                else if (event.getAction() == MotionEvent.ACTION_MOVE){
                    v.setBackgroundColor(unclickColor);
                }
                return false;
            }
        };
        man = getResources().getString(R.string.user_info_popupwindow_sex_man);
        woman = getResources().getString(R.string.user_info_popupwindow_sex_woman);

        head = (RelativeLayout)view.findViewById(R.id.user_info_head_image);
        name = (RelativeLayout)view.findViewById(R.id.person_center_name_container);
        sex =(RelativeLayout)view.findViewById(R.id.person_center_sex_container);
        city = (RelativeLayout)view.findViewById(R.id.user_info_city);
        telephone = (RelativeLayout)view.findViewById(R.id.user_info_telephone);
        email = (RelativeLayout)view.findViewById(R.id.user_info_email);
        setHead(head);
        setSex(sex);
        setname(name);
        setEmail(email);
        setCity(city);
        setTelephone(telephone);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden == false){
              initTextViews();
        }
    }
    private void initTextViews(){
        String noData = getResources().getString(R.string.user_info_fix_no_data);
        String temp;
        temp = ((UserInfoSettingsActivity)getActivity()).getName();
        if(temp == null)
            temp = noData;
        nameTextView.setText(temp);

        temp = ((UserInfoSettingsActivity)getActivity()).getEmail();
        if(temp == null)
            temp = noData;
        emailTextView.setText(temp);

        temp = ((UserInfoSettingsActivity)getActivity()).getTelephone();
        if(temp == null)
            temp = noData;
        telephoneTextView.setText(temp);

        temp = ((UserInfoSettingsActivity)getActivity()).getCity();
        if(temp == null)
            temp = noData;
        cityTextView.setText(temp);

        temp = ((UserInfoSettingsActivity)getActivity()).getSex();
        if(temp == null)
            temp = noData;
        sexTextview.setText(temp);
    }

    public void setHeadImageview(Bitmap bitmap){
        headImageview.setImageBitmap(bitmap);
    }
    private void setTelephone(final RelativeLayout telephone){
        telephone.setOnTouchListener(backgroundListener);
        telephone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSpecialFragment(new UserInfoTelephoneFragment());
            }
        });
    }
    private void setCity(final RelativeLayout city){
        city.setOnTouchListener(backgroundListener);
        city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSpecialFragment(new UserInfoCityFragment());
            }
        });
    }
    private void setEmail(final RelativeLayout email){
        email.setOnTouchListener(backgroundListener);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSpecialFragment(new UserinfoEmailFragmentInfo());
            }
        });
    }
    private void setname(final RelativeLayout name){
        name.setOnTouchListener(backgroundListener);
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSpecialFragment(new UserinfoNameFragment());
            }
        });
    }
    private void setSex(final RelativeLayout sex){
        sexPopupWindow = new PopupWindowSelected(getActivity());
        sexPopupWindow.setFirstItemText(man);
        sexPopupWindow.setSecondeItemText(woman);
        sexPopupWindow.setFirstListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexTextview.setText(man);
                ((UserInfoSettingsActivity)getActivity()).setSex(man);
                sexPopupWindow.dismiss();
            }
        });
        sexPopupWindow.setSecondeListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexTextview.setText(woman);
                ((UserInfoSettingsActivity)getActivity()).setSex(woman);
                sexPopupWindow.dismiss();
            }
        });
        sex.setOnTouchListener(backgroundListener);
        sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexPopupWindow.show();
            }
        });
    }
    private void setHead(RelativeLayout head){
        headPopupWiondow = new PopupWindowSelected(getActivity());
        headPopupWiondow.setFirstListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((UserInfoSettingsActivity)getActivity()).choseHeadImageFromGallery();
                 headPopupWiondow.dismiss();
            }
        });
        headPopupWiondow.setSecondeListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((UserInfoSettingsActivity) getActivity()).choseHeadImageFromCameraCapture();
                headPopupWiondow.dismiss();
            }
        });
        headPopupWiondow.setFirstItemText(getActivity().getResources().getString(R.string.user_info_popupwindow_choose_photo));
        headPopupWiondow.setSecondeItemText(getActivity().getResources().getString(R.string.user_info_popupwindow_create_photo));

        head.setOnTouchListener(backgroundListener);
        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                headPopupWiondow.show();
            }
        });
    }


}
