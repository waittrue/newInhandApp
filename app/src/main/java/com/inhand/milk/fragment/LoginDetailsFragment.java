package com.inhand.milk.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.inhand.milk.App;
import com.inhand.milk.R;
import com.inhand.milk.activity.FirstLanunchActivity;
import com.inhand.milk.activity.MainActivity;
import com.inhand.milk.entity.User;
import com.inhand.milk.ui.DefaultLoadingView;

/**
 * LoginDetailsFragment
 * Desc:
 * Team: Rangers
 * Date: 2015/4/12
 * Time: 21:21
 * Created by: Wooxxx
 */
public class LoginDetailsFragment extends BaseFragment {
    private final int FADE_DURATION = 500;
    private final int ANIM_DURATION = 300;
    Animation weiXinAnim, weiBoAnim, footerAnim, fadeInAnim;
    private ImageView appWeiXin;
    private ImageView appQQ;
    private ImageView appWeiBo;
    private LinearLayout footerOpts;
    private LinearLayout formContainer;
    private ImageView appTitle;
    private View divider;
    private EditText usernameEditor;
    private EditText passwordEditor;
    private TextView forgetPwdTxt;
    private ImageView toReg;
    private ImageView submitBtn;
    private Resources rs;

    //大力加的
    private  boolean success ;
    private int errorCode;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_login_details, container, false);
        initAnimations();
        super.onCreateView(inflater, container, savedInstanceState);
        appWeiXin.startAnimation(weiXinAnim);
        appWeiBo.startAnimation(weiBoAnim);
        footerOpts.startAnimation(footerAnim);
        formContainer.startAnimation(fadeInAnim);
        appQQ.startAnimation(fadeInAnim);
        appTitle.startAnimation(fadeInAnim);
        divider.startAnimation(fadeInAnim);
        return rootView;
    }

    @Override
    public void initViews() {
        appWeiXin = (ImageView) rootView.findViewById(R.id.app_weixin);
        appQQ = (ImageView) rootView.findViewById(R.id.app_qq);
        appWeiBo = (ImageView) rootView.findViewById(R.id.app_weibo);
        footerOpts = (LinearLayout) rootView.findViewById(R.id.login_details_options_container);
        formContainer = (LinearLayout) rootView.findViewById(R.id.login_form_container);
        appTitle = (ImageView) rootView.findViewById(R.id.login_app_title);
        divider = (View) rootView.findViewById(R.id.divider);

        usernameEditor = (EditText) rootView.findViewById(R.id.username);
        passwordEditor = (EditText) rootView.findViewById(R.id.password);
        forgetPwdTxt = (TextView) rootView.findViewById(R.id.forget_pwd);
        toReg = (ImageView) rootView.findViewById(R.id.to_register);
        submitBtn = (ImageView) rootView.findViewById(R.id.submit_btn);

        rs = getResources();
    }

    @Override
    public void setListeners() {
        // 登陆按钮按下事件监听
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               login();
            }
        });

        // 第三方登陆按钮监听
        appWeiXin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        appQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        appWeiBo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // 跳转到注册
        toReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.addToBackStack(null);
                ft.replace(R.id.main_container, new RegisterFragment());
                ft.setTransition(FragmentTransaction.TRANSIT_NONE);
                ft.commit();
            }
        });

        // 忘记密码？
        forgetPwdTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /**
     * 初始化过度动画
     */
    private void initAnimations() {
        weiXinAnim = AnimationUtils.loadAnimation(
                getActivity(), R.anim.left_in
        );
        weiXinAnim.setDuration(ANIM_DURATION);
        weiBoAnim = AnimationUtils.loadAnimation(
                getActivity(), R.anim.right_in
        );
        weiBoAnim.setDuration(ANIM_DURATION);
        footerAnim = AnimationUtils.loadAnimation(
                getActivity(), R.anim.bottom_in
        );
        footerAnim.setDuration(ANIM_DURATION);
        fadeInAnim = AnimationUtils.loadAnimation(
                getActivity(), R.anim.fade_in
        );
        fadeInAnim.setDuration(FADE_DURATION);
        fadeInAnim.setFillAfter(true);
    }

    /**
     * 登录方法
     */
    private void login() {
        final String username = usernameEditor.getText()
                .toString();
        final String password = passwordEditor.getText()
                .toString();
        // 基本验证
        if (username.length() == 0) {
            usernameEditor.setError(
                    rs.getString(R.string.username_null));
            usernameEditor.requestFocus();
            return;
        }
        if (password.length() == 0) {
            passwordEditor.setError(
                    rs.getString(R.string.password_null));
            passwordEditor.requestFocus();
            return;
        }
        //进行登录
        final DefaultLoadingView defaultLoadingView = new DefaultLoadingView(getActivity(),"同步中");
        DefaultLoadingView.LoadingCallback callback = new DefaultLoadingView.LoadingCallback() {
            @Override
            public void doInBackground() {
                success = true;
                try {
                    //  AVUser.logIn(username,password);
                    AVUser.loginByMobilePhoneNumber(username, password);
                } catch (AVException e) {
                    e.printStackTrace();
                    success = false;
                    return;
                }
                errorCode = App.getCurrentUser().hasBaby(getActivity());
                }
            @Override
            public void onPreExecute() {

            }

            @Override
            public void onPostExecute() {
                defaultLoadingView.disppear();
                if(success){
                    Log.d("errorCode", errorCode + "");
                    switch (errorCode) {
                        case User.HAS_BABY:
                            Log.d("User has baby", "has");
                            getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
                            getActivity().finish();
                            break;
                        case User.NO_BABY:
                            Log.d("User has baby", "none");
                            getActivity().startActivity(new Intent( getActivity(), FirstLanunchActivity.class));
                            getActivity().finish();
                            break;
                        case User.NETWORK_ERROR:
                            Log.d("User has baby", "network error");
                            break;
                    }
                }
                else {
                    usernameEditor.setError(rs.getString(R.string.login_error));
                    usernameEditor.requestFocus();
                }
            }
        };
        defaultLoadingView.loading(callback);
                /*
        AVUser.logInInBackground(username, password,
                new LogInCallback<User>() {
                    @Override
                    public void done(User avUser, AVException e) {
                        if (avUser == null) {
                            //登录失败进行提示
                            usernameEditor.setError(rs.getString(R.string.login_error));
                            usernameEditor.requestFocus();
                        } else {
                            Log.d("usersex1", App.getCurrentUser().getSex() + "");
                            PreJudgingTask task = new PreJudgingTask(getActivity());
                            task.execute();
                        }
                    }
                }, User.class);
                */

    }
}
