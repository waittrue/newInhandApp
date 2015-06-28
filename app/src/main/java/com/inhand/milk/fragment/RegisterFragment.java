package com.inhand.milk.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.avos.avoscloud.SaveCallback;
import com.inhand.milk.R;
import com.inhand.milk.activity.LaunchActivity;
import com.inhand.milk.entity.User;
import com.inhand.milk.utils.PreJudgingTask;
import com.inhand.milk.utils.Validator;

/**
 * RegisterFragment
 * Desc:注册Fragment
 * Team: InHand
 * User:Wooxxx
 * Date: 2015-03-03
 * Time: 16:56
 */
public class RegisterFragment extends BackHandleFragment {

    private LinearLayout formContainer;
    private LinearLayout optionsContainer;
    private EditText usernameEditor;
    private EditText passwordEditor;
    private EditText rePasswordEditor;
    private EditText authCodeEditor;

    private Button getAuthCodeBtn;
    private ImageView submitBtn;

    private ImageView toLogin;
    private Resources rs;
    private String username;
    private String password;

    private User user;
    private TimeCount timer;

    // 动画
    Animation leftInAnim, rightInAnim;
    Animation bottomOutAnim;
    Animation fadeInAnim, fadeOutAnim;

    private final int FADE_DURATION = 500;
    private final int ANIM_DURATION = 400;

    // 定义验证错误代码
    private final int NO_ERROR = 0; //无错误
    private final int PHONE_ERROR = 1; // 手机号码格式错误
    private final int PASSWORD_ERROR = 2; // 密码格式错误
    private final int PASSWORD_MISMATCH = 3; // 两次输入密码不一致

    /**
     * 注册的返回逻辑是返回到登陆选项页面
     *
     * @return
     */
    @Override
    public boolean onBackPressed() {
        if (timer !=null){
            timer.cancel();
            timer = null;
        }
        user = null;
        optionsContainer.startAnimation(bottomOutAnim);
        return true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_register, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        initAnimations();
        toLogin.startAnimation(leftInAnim);
        submitBtn.startAnimation(rightInAnim);
        formContainer.startAnimation(fadeInAnim);
        return rootView;
    }

    @Override
    public void initViews() {
        formContainer = (LinearLayout) rootView.findViewById(R.id.register_form_container);
        optionsContainer = (LinearLayout) rootView.findViewById(R.id.register_options_container);
        usernameEditor = (EditText) rootView.findViewById(R.id.username);
        passwordEditor = (EditText) rootView.findViewById(R.id.password);
        rePasswordEditor = (EditText) rootView.findViewById(R.id.re_password);
        authCodeEditor = (EditText) rootView.findViewById(R.id.auth_code);
        getAuthCodeBtn = (Button) rootView.findViewById(R.id.get_auth_code_btn);
        submitBtn = (ImageView) rootView.findViewById(R.id.submit_btn);
        toLogin = (ImageView) rootView.findViewById(R.id.to_login);
        rs = getResources();
    }

    @Override
    public void setListeners() {
        // 获得验证码前验证表单
        getAuthCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int errorCode = validateForm();
                switch (errorCode) {
                    case NO_ERROR:
                        //开始验证码倒计，并请求验证码
                        timer = new TimeCount(60000, 1000);
                        AVOSCloud.requestSMSCodeInBackgroud(
                                username,
                                rs.getString(R.string.app_name),
                                rs.getString(R.string.register),
                                1,
                                new RequestMobileCodeCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        if (e != null) {
                                            e.printStackTrace();
                                            Toast.makeText(getActivity(), "Bad Request", Toast.LENGTH_SHORT).show();
                                        } else {
                                            getAuthCodeBtn.setClickable(false);
                                            timer.start();
                                        }
                                    }
                                });
                        break;
                    case PHONE_ERROR:
                        usernameEditor.setError(rs.getString(R.string.register_phone_error));
                        usernameEditor.requestFocus();
                        break;
                    case PASSWORD_ERROR:
                        passwordEditor.setError(rs.getString(R.string.register_pwd_error));
                        passwordEditor.requestFocus();
                        break;
                    case PASSWORD_MISMATCH:
                        passwordEditor.setError(rs.getString(R.string.register_pwd_mismatch));
                        passwordEditor.requestFocus();
                        break;
                }
            }
        });

        // 提交注册
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 提交注册时判断验证码是否正确
                String authCode = authCodeEditor.getText()
                        .toString();
                if (authCode.length() == 6) {
                    AVOSCloud.verifySMSCodeInBackground(authCode, username, new AVMobilePhoneVerifyCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                user = new User();
                                user.setUsername(username);
                                user.setPassword(password);
                                user.setMobilePhoneNumber(username);
                                user.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        if (e == null) {
                                            // 停止验证码计时
                                            timer.cancel();
                                            // 跳转到首页入口
                                            PreJudgingTask task=new PreJudgingTask(getActivity());
                                            task.execute();
                                        } else {
                                            e.printStackTrace();
                                            user = null;
                                            usernameEditor.setError(rs.getString(R.string.register_phone_exist));
                                            usernameEditor.requestFocus();
                                        }
                                    }
                                });

                            } else {
                                authCodeEditor.setError(
                                        rs.getString(R.string.register_ac_error)
                                );
                                authCodeEditor.requestFocus();
                            }
                        }
                    });
                }
            }
        });

        // 返回登陆页
        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterFragment.this.onBackPressed();
            }
        });
    }

    /**
     * 验证注册表单
     *
     * @return 返回错误代码
     */
    private int validateForm() {
        // 验证手机格式
        username = usernameEditor.getText()
                .toString();
        if (!Validator.validatePhone(username)) {
            return PHONE_ERROR;
        }

        // 验证密码
        password = passwordEditor.getText()
                .toString();
        String rePassword = rePasswordEditor.getText()
                .toString();
        if (!password.equals(rePassword))
            return PASSWORD_MISMATCH;
        else {
            if (!Validator.validatePassword(password))
                return PASSWORD_ERROR;
        }

        return NO_ERROR;
    }

    /**
     * 初始化组件动画
     */
    private void initAnimations() {
        leftInAnim = AnimationUtils.loadAnimation(
                getActivity(), R.anim.left_in
        );
        leftInAnim.setDuration(ANIM_DURATION);
        rightInAnim = AnimationUtils.loadAnimation(
                getActivity(), R.anim.right_in
        );
        rightInAnim.setDuration(ANIM_DURATION);
        bottomOutAnim = AnimationUtils.loadAnimation(
                getActivity(), R.anim.bottom_out
        );
        bottomOutAnim.setDuration(ANIM_DURATION);
        bottomOutAnim.setFillAfter(true);
        fadeInAnim = AnimationUtils.loadAnimation(
                getActivity(), R.anim.fade_in
        );
        fadeInAnim.setDuration(
                FADE_DURATION
        );
        fadeInAnim.setFillAfter(true);
        fadeOutAnim = AnimationUtils.loadAnimation(
                getActivity(), R.anim.fade_out
        );
        fadeOutAnim.setDuration(FADE_DURATION);
        fadeOutAnim.setFillAfter(true);

        bottomOutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                formContainer.startAnimation(fadeOutAnim);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        fadeOutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                getFragmentManager().popBackStack();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 验证码倒计时类
     */
    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            getAuthCodeBtn.setTextColor(
                    rs.getColor(R.color.common_bandage_font_light)
            );
            getAuthCodeBtn.setText(millisUntilFinished / 1000 + rs
                    .getString(R.string.vc_wait));
        }

        @Override
        public void onFinish() {
            getAuthCodeBtn.setTextColor(
                    rs.getColor(R.color.common_bandage_font)
            );
            getAuthCodeBtn.setText(rs.getString(R.string.vc_reget));
            getAuthCodeBtn.setClickable(true);
        }
    }
}
