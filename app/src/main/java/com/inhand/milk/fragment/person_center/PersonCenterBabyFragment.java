package com.inhand.milk.fragment.person_center;

import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.inhand.milk.App;
import com.inhand.milk.R;
import com.inhand.milk.activity.PersonCenterBabyInfoActivity;
import com.inhand.milk.activity.UserInfoSettingsActivity;
import com.inhand.milk.dao.BabyDao;
import com.inhand.milk.entity.Baby;
import com.inhand.milk.entity.Base;
import com.inhand.milk.fragment.TitleFragment;
import com.inhand.milk.fragment.user_info_settings.UserinfoNameFragment;
import com.inhand.milk.ui.DefaultLoadingView;
import com.inhand.milk.ui.PopupWindowSelected;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2015/7/7.
 */
public class PersonCenterBabyFragment extends TitleFragment {
    private RelativeLayout head, name, sex, birth;
    private int clickColor, unclickColor = Color.WHITE;
    private View.OnTouchListener onTouchListener;
    private PopupWindowSelected headPopupWiondow, sexPopupWindow;
    private String man, woman,temp;
    private int year, monthOfyear, dayOfmonth;
    private TextView babySexTextView, babynameTextView, babyBirthTextView;
    private DatePickerDialog datePickerDialog;
    private UserinfoNameFragment userinfoNameFragment;
    private DefaultLoadingView loadingView;
    private DefaultLoadingView.LoadingCallback sexCallBack, birthCallBack, initCallBack;
    private boolean success;
    private Baby baby;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.person_center_baby_info, container, false);
        baby = App.getCurrentBaby();
        loadingView = new DefaultLoadingView(getActivity(), "同步中");
        setTitleview(getResources().getString(R.string.person_center_baby_info), 2);
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
        initView(mView);
        initTextViews();
        initCallBack = new DefaultLoadingView.LoadingCallback() {
            @Override
            public void doInBackground() {
                List<Baby> babies = BabyDao.findBabiesByUser(App.getCurrentUser());
                if (babies == null) {
                    return;
                }
                String birth, sex, name;
                sex = null;
                birth = baby.getBirthday();
                // Log.i("personcenterbabyinfo initcallback",birth);
                int sexIndex = baby.getSex();
                if (sexIndex == Baby.FEMALE)
                    sex = "女性";
                else if (sexIndex == Baby.MALE)
                    sex = "男性";
                name = baby.getNickname();
                ((PersonCenterBabyInfoActivity) getActivity()).setName(name);
                ((PersonCenterBabyInfoActivity) getActivity()).setBirth(birth);
                ((PersonCenterBabyInfoActivity) getActivity()).setSex(sex);
                success = true;
            }

            @Override
            public void onPreExecute() {
                success = false;
            }

            @Override
            public void onPostExecute() {
                if (success == true) {
                    loadingView.dismiss();
                    initTextViews();
                } else {
                    loadingView.disppear(null, "加载失败", 2);
                }
            }
        };
        loadingView.loading(initCallBack);
        return mView;
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden == false) {
            initTextViews();
        }
    }

    private void initView(View view) {
        head = (RelativeLayout) view.findViewById(R.id.person_center_baby_head_container);
        name = (RelativeLayout) view.findViewById(R.id.person_center_name_container);
        sex = (RelativeLayout) view.findViewById(R.id.person_center_sex_container);
        birth = (RelativeLayout) view.findViewById(R.id.person_center_birth_container);
        man = getResources().getString(R.string.user_info_popupwindow_sex_man);
        woman = getResources().getString(R.string.user_info_popupwindow_sex_woman);
        babyBirthTextView = (TextView) view.findViewById(R.id.person_center_baby_birth_textview);
        babynameTextView = (TextView) view.findViewById(R.id.person_center_baby_name_textview);
        babySexTextView = (TextView) view.findViewById(R.id.person_center_baby_info_sex_textview);

        setHead(head);
        setSex(sex);
        setBirth(birth);
        setname(name);
    }

    private void initTextViews() {
        String text;
        String noData = getResources().getString(R.string.user_info_fix_no_data);
        text = ((PersonCenterBabyInfoActivity) getActivity()).getName();
        if (text == null)
            text = noData;
        babynameTextView.setText(text);

        text = ((PersonCenterBabyInfoActivity) getActivity()).getSex();
        if (text == null)
            text = noData;
        babySexTextView.setText(text);

        text = ((PersonCenterBabyInfoActivity) getActivity()).getBirth();
        if (text == null)
            text = noData;
        babyBirthTextView.setText(text);
    }

    private void setHead(final RelativeLayout head) {
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

        head.setOnTouchListener(onTouchListener);
        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                headPopupWiondow.show();
            }
        });
    }

    private void setSex(final RelativeLayout sex) {
        sexPopupWindow = new PopupWindowSelected(getActivity());
        sexPopupWindow.setFirstItemText(man);
        sexPopupWindow.setSecondeItemText(woman);

        sexCallBack = new DefaultLoadingView.LoadingCallback() {
            @Override
            public void doInBackground() {
                String sex = babySexTextView.getText().toString();

                if (sex.equals(getResources().getString(R.string.user_info_popupwindow_sex_man))) {
                    baby.setSex(Baby.MALE);
                } else if (sex.equals(getResources().getString(R.string.user_info_popupwindow_sex_woman))) {
                    baby.setSex(Baby.FEMALE);
                }
                try {
                    baby.saveSync();
                    baby.saveInCache(getActivity(), new Base.CacheSavingCallback() {
                        @Override
                        public void done() {

                        }
                    });
                    success = true;
                } catch (AVException e) {
                    e.printStackTrace();
                    success = false;
                }

            }

            @Override
            public void onPreExecute() {
                success = false;
            }

            @Override
            public void onPostExecute() {
                if (success == true) {
                    loadingView.dismiss();
                    Log.i("personcenter resulet", "sex true");
                } else {
                    Log.i("personcenter resulet", "sex false");
                    loadingView.disppear(null, "请求失败", 2);
                }
            }
        };

        sexPopupWindow.setFirstListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                babySexTextView.setText(man);
                ((PersonCenterBabyInfoActivity) getActivity()).setSex(man);
                sexPopupWindow.dismiss();
                loadingView.loading(sexCallBack);
            }
        });
        sexPopupWindow.setSecondeListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                babySexTextView.setText(woman);
                ((PersonCenterBabyInfoActivity) getActivity()).setSex(woman);
                sexPopupWindow.dismiss();
                loadingView.loading(sexCallBack);
            }
        });
        sex.setOnTouchListener(onTouchListener);
        sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexPopupWindow.show();
            }
        });
    }

    private void setBirth(final RelativeLayout birth) {
        birth.setOnTouchListener(onTouchListener);
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        monthOfyear = calendar.get(Calendar.MONTH) + 1;
        dayOfmonth = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(this.getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String str = String.valueOf(year) + "年" + String.valueOf(monthOfYear + 1) + "月" + String.valueOf(dayOfMonth) + "日";
                babyBirthTextView.setText(str);
                loadingView.loading(birthCallBack);
            }
        }, year, monthOfyear, dayOfmonth);
        loadingView = new DefaultLoadingView(getActivity(), "同步中");
        birthCallBack = new DefaultLoadingView.LoadingCallback() {
            @Override
            public void doInBackground() {
                String birth = babyBirthTextView.getText().toString();
                baby.setBirthday(birth);
                try {
                    baby.saveSync();
                    baby.saveInCache(getActivity(), new Base.CacheSavingCallback() {
                        @Override
                        public void done() {

                        }
                    });
                    success = true;
                } catch (AVException e) {
                    e.printStackTrace();
                    success = false;
                }

            }

            @Override
            public void onPreExecute() {
                success = false;
            }

            @Override
            public void onPostExecute() {
                if (success == false) {
                    loadingView.disppear(null, "加载失败", 2);
                } else {
                    loadingView.dismiss();
                }
            }
        };
        birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
    }

    private void setname(final RelativeLayout name) {

        final DefaultLoadingView.LoadingCallback callback = new DefaultLoadingView.LoadingCallback() {
            @Override
            public void doInBackground() {

                baby.setNickname(temp);
                try {
                    baby.saveSync();
                    baby.saveInCache(getActivity(), new Base.CacheSavingCallback() {
                        @Override
                        public void done() {

                        }
                    });
                    success = true;
                } catch (AVException e) {
                    e.printStackTrace();
                    success = false;
                }
            }

            @Override
            public void onPreExecute() {
                success = false;
                userinfoNameFragment.hiddenSoftInput();
            }

            @Override
            public void onPostExecute() {

                if (success == true) {
                    ((PersonCenterBabyInfoActivity) getActivity()).setName(temp);
                    FragmentManager fragmentManager = getActivity().getFragmentManager();
                    fragmentManager.popBackStack();
                    fragmentManager.beginTransaction().commit();
                    loadingView.dismiss();
                } else {
                    loadingView.disppear(null, "请求失败", 2);
                }

            }

        };
        userinfoNameFragment = new UserinfoNameFragment(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temp = userinfoNameFragment.getString();
                if(temp.equals("")){

                    userinfoNameFragment.hiddenSoftInput();
                    Toast.makeText(getActivity(),"不能填写空白",Toast.LENGTH_SHORT).show();
                    return ;
                }
                loadingView = new DefaultLoadingView(getActivity(), "加载中");
                loadingView.loading(callback);

            }
        });
        name.setOnTouchListener(onTouchListener);
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSpecialFragment(userinfoNameFragment);
            }
        });
    }

}
