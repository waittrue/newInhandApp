package com.inhand.milk.fragment.person_center.user_info_settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inhand.milk.App;
import com.inhand.milk.R;
import com.inhand.milk.activity.LaunchActivity;
import com.inhand.milk.activity.UserInfoSettingsActivity;
import com.inhand.milk.fragment.TitleFragment;
import com.inhand.milk.ui.PopupWindowSelected;

/**
 * Created by Administrator on 2015/7/2.
 */
public class UserInfoSettingsFragment extends TitleFragment {
    private RelativeLayout head, name, sex, city, telephone, email,logout;
    private ImageView headImageview;
    private TextView sexTextview, nameTextView, telephoneTextView, emailTextView, cityTextView;
    private String man, woman;
    private int clickColor, unclickColor = Color.WHITE;
    private PopupWindowSelected headPopupWiondow, sexPopupWindow;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.user_info_settings, container, false);
        setTitleview(getResources().getString(R.string.user_info_title), 2);
        initView(mView);
        initTextViews();
        return mView;
    }

    private void initView(View view) {
        clickColor = getResources().getColor(R.color.common_settings_item_click_bg_color);
        headImageview = (ImageView) view.findViewById(R.id.user_info_headimage_imageview);

        sexTextview = (TextView) view.findViewById(R.id.user_info_sex_textview);
        nameTextView = (TextView) view.findViewById(R.id.user_info_name_textview);
        telephoneTextView = (TextView) view.findViewById(R.id.user_info_telephone_textview);
        emailTextView = (TextView) view.findViewById(R.id.user_info_email_textview);
        cityTextView = (TextView) view.findViewById(R.id.user_info_city_textview);
        man = getResources().getString(R.string.user_info_popupwindow_sex_man);
        woman = getResources().getString(R.string.user_info_popupwindow_sex_woman);

        head = (RelativeLayout) view.findViewById(R.id.user_info_head_image);
        name = (RelativeLayout) view.findViewById(R.id.person_center_name_container);
        sex = (RelativeLayout) view.findViewById(R.id.person_center_sex_container);
        city = (RelativeLayout) view.findViewById(R.id.user_info_city);
        telephone = (RelativeLayout) view.findViewById(R.id.user_info_telephone);
        email = (RelativeLayout) view.findViewById(R.id.user_info_email);
        logout = (RelativeLayout)view.findViewById(R.id.user_info_log_out);
        setHead(head);
        setSex(sex);
        setname(name);
        setEmail(email);
        setCity(city);
        setTelephone(telephone);
        setLogout(logout);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden == false) {
            initTextViews();
        }
    }

    private void initTextViews() {
        String noData = getResources().getString(R.string.user_info_fix_no_data);
        String temp;
        temp = ((UserInfoSettingsActivity) getActivity()).getName();
        if (temp == null)
            temp = noData;
        nameTextView.setText(temp);

        temp = ((UserInfoSettingsActivity) getActivity()).getEmail();
        if (temp == null)
            temp = noData;
        emailTextView.setText(temp);

        temp = ((UserInfoSettingsActivity) getActivity()).getTelephone();
        if (temp == null)
            temp = noData;
        telephoneTextView.setText(temp);

        temp = ((UserInfoSettingsActivity) getActivity()).getCity();
        if (temp == null)
            temp = noData;
        cityTextView.setText(temp);

        temp = ((UserInfoSettingsActivity) getActivity()).getSex();
        if (temp == null)
            temp = noData;
        sexTextview.setText(temp);
    }

    public void setHeadImageview(Bitmap bitmap) {
        headImageview.setImageBitmap(bitmap);
    }

    private void setTelephone(final RelativeLayout telephone) {
        telephone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSpecialFragment(new UserInfoTelephoneFragment());
            }
        });
    }

    private void setCity(final RelativeLayout city) {
        city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSpecialFragment(new UserInfoCityFragment());
            }
        });
    }

    private void setEmail(final RelativeLayout email) {
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSpecialFragment(new UserinfoEmailFragmentInfo());
            }
        });
    }

    private void setname(final RelativeLayout name) {

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSpecialFragment(new UserinfoNameFragment());
            }
        });
    }

    private void setSex(final RelativeLayout sex) {
        sexPopupWindow = new PopupWindowSelected(getActivity());
        sexPopupWindow.setFirstItemText(man);
        sexPopupWindow.setSecondeItemText(woman);
        sexPopupWindow.setFirstListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexTextview.setText(man);
                ((UserInfoSettingsActivity) getActivity()).setSex(man);
                sexPopupWindow.dismiss();
            }
        });
        sexPopupWindow.setSecondeListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexTextview.setText(woman);
                ((UserInfoSettingsActivity) getActivity()).setSex(woman);
                sexPopupWindow.dismiss();
            }
        });
        sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexPopupWindow.show();
            }
        });
    }

    private void setHead(RelativeLayout head) {
        headPopupWiondow = new PopupWindowSelected(getActivity());
        headPopupWiondow.setFirstListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((UserInfoSettingsActivity) getActivity()).choseHeadImageFromGallery();
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

        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                headPopupWiondow.show();
            }
        });
    }
    private void setLogout(RelativeLayout logout){
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alert = new AlertDialog.Builder(getActivity()).create();
                alert.setMessage("确定退出登陆");
                alert.setTitle("提示");
                alert.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        App.logOut();
                        Intent intent = new Intent(getActivity(), LaunchActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });

                alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                    }
                });
                alert.show();
            }
        });
    }

}
