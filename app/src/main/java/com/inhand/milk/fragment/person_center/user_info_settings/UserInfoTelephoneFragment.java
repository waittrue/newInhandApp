package com.inhand.milk.fragment.person_center.user_info_settings;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.inhand.milk.R;
import com.inhand.milk.activity.UserInfoSettingsActivity;

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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.user_info_settings_cellphone, container, false);
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
                //这里没有校验码的核对
                ((UserInfoSettingsActivity) getActivity()).setTelephone(editText.getText().toString());
                FragmentManager manager = getActivity().getFragmentManager();
                manager.popBackStack();
                manager.beginTransaction().commit();
                hiddenSoftInput();
            }
        });
        checkTextView = (TextView) view.findViewById(R.id.user_info_settings_telephone_check_button);
        checkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTiming();
            }
        });
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
                    } else
                        handler.postDelayed(runnable, 1000);
                }
            };
        }
        handler.removeCallbacks(runnable);
        leftTime = 60;
        handler.post(runnable);

    }

    protected void hiddenSoftInput() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

}
