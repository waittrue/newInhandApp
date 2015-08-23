package com.inhand.milk.fragment.person_center.user_info_settings;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.inhand.milk.App;
import com.inhand.milk.R;
import com.inhand.milk.activity.LaunchActivity;
import com.inhand.milk.activity.UserInfoSettingsActivity;
import com.inhand.milk.entity.User;
import com.inhand.milk.fragment.TitleFragment;
import com.inhand.milk.ui.DefaultLoadingView;
import com.inhand.milk.ui.PopupWindowSelected;

import java.io.ByteArrayOutputStream;

/**
 * Created by Administrator on 2015/7/2.
 */
public class UserInfoSettingsFragment extends TitleFragment {
    private RelativeLayout head, name, sex, city, telephone, email,logout;
    private ImageView headImageview;
    private TextView sexTextview, nameTextView, telephoneTextView, emailTextView, cityTextView;
    private String man, woman;
    private String tempString;
    private UserinfoNameFragment nameSettingsFragment;
    private int clickColor, unclickColor = Color.WHITE;
    private PopupWindowSelected headPopupWiondow, sexPopupWindow;
    private DefaultLoadingView loadingView;
    private boolean success;
    private User user;
    private byte[] imageBytes;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        loadingView = new DefaultLoadingView(getActivity(), "同步中");
        user = App.getCurrentUser();
        imageBytes = user.getImageFromAcache();
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
        if (imageBytes != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            headImageview.setImageBitmap(bitmap);
        }

        String noData = getResources().getString(R.string.user_info_fix_no_data);
        tempString = ((UserInfoSettingsActivity) getActivity()).getName();
        if (tempString == null)
            tempString = noData;
        nameTextView.setText(tempString);

        tempString = ((UserInfoSettingsActivity) getActivity()).getEmail();
        if (tempString == null)
            tempString = noData;
        emailTextView.setText(tempString);

        tempString = ((UserInfoSettingsActivity) getActivity()).getTelephone();
        if (tempString == null)
            tempString = noData;
        telephoneTextView.setText(tempString);

        tempString = ((UserInfoSettingsActivity) getActivity()).getCity();
        if (tempString == null)
            tempString = noData;
        cityTextView.setText(tempString);

        tempString = ((UserInfoSettingsActivity) getActivity()).getSex();
        if (tempString == null)
            tempString = noData;
        sexTextview.setText(tempString);
    }

    public void setHeadImageview(final Bitmap bitmap) {
        if (bitmap == null)
            return;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        final byte[] bytes = baos.toByteArray();
        if (bytes == null)
            return;
        final DefaultLoadingView.LoadingCallback callback = new DefaultLoadingView.LoadingCallback() {
            @Override
            public void doInBackground() {
                try {
                    user.saveAvatorBytes(bytes);
                } catch (AVException e) {
                    success = false;
                }
            }

            @Override
            public void onPreExecute() {
                success = true;
            }

            @Override
            public void onPostExecute() {
                if (success) {
                    headImageview.setImageBitmap(bitmap);
                    loadingView.dismiss();
                } else {
                    loadingView.disppear(null, "加载失败", 1);
                }
            }
        };
        loadingView.loading(callback);
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
        final DefaultLoadingView.LoadingCallback nameCallback = new DefaultLoadingView.LoadingCallback() {
            @Override
            public void doInBackground() {
                try {
                    user.setNickname(tempString);
                    user.save();
                    Log.i("userinfosettting", "save success:" + user.getNickname());
                    ((UserInfoSettingsActivity) getActivity()).setName(tempString);
                } catch (AVException e) {
                    success = false;
                    Log.i("userinfosettting", "save failed");
                }
            }

            @Override
            public void onPreExecute() {
                success = true;
            }

            @Override
            public void onPostExecute() {
                if (success == true) {
                    ((UserInfoSettingsActivity) getActivity()).setName(tempString);
                    FragmentManager fragmentManager = getActivity().getFragmentManager();
                    fragmentManager.popBackStack();
                    fragmentManager.beginTransaction().commit();
                    loadingView.dismiss();
                } else {
                    loadingView.disppear(null, "请求失败", 2);
                }
            }
        };
        nameSettingsFragment = new UserinfoNameFragment(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempString = nameSettingsFragment.getString();
                if (tempString == null || tempString.isEmpty()) {
                    nameSettingsFragment.hiddenSoftInput();
                    Toast.makeText(getActivity(), "不能填写空白", Toast.LENGTH_SHORT).show();
                    return;
                }
                loadingView.loading(nameCallback);
            }
        });
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSpecialFragment(nameSettingsFragment);
            }
        });
    }

    private void setSex(final RelativeLayout sex) {
        final DefaultLoadingView.LoadingCallback manCallback = new DefaultLoadingView.LoadingCallback() {
            @Override
            public void doInBackground() {
                try {
                    user.setSex(User.MALE);
                    user.save();
                } catch (AVException e) {
                    e.printStackTrace();
                    success = false;
                }
            }

            @Override
            public void onPreExecute() {
                success = true;
            }

            @Override
            public void onPostExecute() {
                if (success) {
                    sexTextview.setText(man);
                    loadingView.dismiss();
                } else {
                    loadingView.disppear(null, "请求失败", 2);
                }
            }
        };
        final DefaultLoadingView.LoadingCallback womanCallback = new DefaultLoadingView.LoadingCallback() {
            @Override
            public void doInBackground() {
                try {
                    user.setSex(User.FEMALE);
                    user.save();
                } catch (AVException e) {
                    e.printStackTrace();
                    success = false;
                }
            }

            @Override
            public void onPreExecute() {
                success = true;
            }

            @Override
            public void onPostExecute() {
                if (success) {
                    sexTextview.setText(woman);
                    loadingView.dismiss();
                } else {
                    loadingView.disppear(null, "请求失败", 2);
                }
            }
        };
        sexPopupWindow = new PopupWindowSelected(getActivity());
        sexPopupWindow.setFirstItemText(man);
        sexPopupWindow.setSecondeItemText(woman);
        sexPopupWindow.setFirstListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexPopupWindow.dismiss();
                loadingView.loading(manCallback);
            }
        });
        sexPopupWindow.setSecondeListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexPopupWindow.dismiss();
                loadingView.loading(womanCallback);
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
