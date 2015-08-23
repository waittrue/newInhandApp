package com.inhand.milk.fragment.person_center.user_info_settings;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.inhand.milk.App;
import com.inhand.milk.R;
import com.inhand.milk.activity.UserInfoSettingsActivity;
import com.inhand.milk.entity.User;

/**
 * Created by Administrator on 2015/7/6.
 */
public class UserInfoTelephoneFragment extends Fragment {
    protected View mView;
    protected EditText editText, checkEditText;
    private TextView rightTextview, checkTextView;
    private Handler handler;
    private Runnable runnable;
    private int leftTime;
    private User user;
    private boolean hasCode = false;//是否有验证码。

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.user_info_settings_cellphone, container, false);
        user = App.getCurrentUser();
        initView(mView);
        return mView;
    }

    private void initView(View view) {
        ImageView leftIcon = (ImageView) view.findViewById(R.id.title_left_icon);

        leftIcon.setImageDrawable(getResources().getDrawable(R.drawable.back_icon));
        leftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getActivity().getFragmentManager();
                manager.popBackStack();
                manager.beginTransaction().commit();
                hiddenSoftInput();
            }
        });
        editText = (EditText) view.findViewById(R.id.user_info_telephone_edit);
        editText.setInputType(InputType.TYPE_CLASS_PHONE);
        String str = ((UserInfoSettingsActivity) getActivity()).getTelephone();
        if (str == null)
            str = "";
        editText.setText(str);

        checkEditText = (EditText) view.findViewById(R.id.user_info_settings_second_editText);
        checkEditText.setInputType(InputType.TYPE_CLASS_PHONE);

        rightTextview = (TextView) view.findViewById(R.id.title_right_icon);
        rightTextview.setText(getResources().getString(R.string.user_info_fix_right_text));
        (view.findViewById(R.id.user_info_edit_container)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.performClick();
            }
        });
        (view.findViewById(R.id.user_info_second_container)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEditText.performClick();
            }
        });

        rightTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasCode == false)
                    return;
                String check = checkEditText.getText().toString();
                if (check == null || check.isEmpty()) {
                    Toast.makeText(getActivity(), "验证码不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                AVUser.verifyMobilePhoneInBackground(check, new AVMobilePhoneVerifyCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e != null) {
                            Toast.makeText(getActivity(), "验证码错误", Toast.LENGTH_LONG).show();
                        } else {
                            user.setMobilePhoneNumber(editText.getText().toString());
                            user.saveInBackground();
                            ((UserInfoSettingsActivity) getActivity()).setTelephone(editText.getText().toString());
                            FragmentManager manager = getActivity().getFragmentManager();
                            manager.popBackStack();
                            manager.beginTransaction().commit();
                            hiddenSoftInput();
                            Log.i("userinfotelepheone", "手机验证成功");
                        }
                    }
                });

            }
        });
        checkTextView = (TextView) view.findViewById(R.id.user_info_settings_telephone_check_button);
        checkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String telephone = editText.getText().toString();
                if (telephone == null || telephone.isEmpty()) {
                    Toast.makeText(getActivity(), "不能填写空白", Toast.LENGTH_LONG).show();
                    return;
                }
                if (user.isMobilePhoneVerified()) {
                    Toast.makeText(getActivity(), "号码验证过", Toast.LENGTH_LONG).show();
                    return;
                }
                user.requestMobilePhoneVerifyInBackground(telephone, new RequestMobileCodeCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e != null) {
                            if (e.getCode() == AVException.INVALID_PHONE_NUMBER) {
                                Toast.makeText(getActivity(), " 手机格式无效", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getActivity(), " 网络错误，验证码请求失败", Toast.LENGTH_LONG).show();
                            }
                            return;
                        } else {
                            startTiming();
                        }
                    }
                });

            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (handler == null)
            return;
        if (runnable == null)
            return;
        handler.removeCallbacks(runnable);
    }

    private void startTiming() {
        if (handler == null)
            handler = new Handler();
        if (runnable == null) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    checkTextView.setText(String.valueOf(leftTime) + "后重新发送");
                    leftTime--;
                    if (leftTime == -1) {
                        handler.removeCallbacks(runnable);
                        checkTextView.setText(getResources().getString(R.string.user_info_settings_telephone_right_text));
                        hasCode = false;
                    } else
                        handler.postDelayed(runnable, 1000);
                }
            };
        }
        handler.removeCallbacks(runnable);
        leftTime = 60;
        handler.post(runnable);
        hasCode = true;

    }

    protected void hiddenSoftInput() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

}
