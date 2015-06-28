package com.inhand.milk.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.avos.avoscloud.AVUser;
import com.inhand.milk.App;
import com.inhand.milk.R;
import com.inhand.milk.activity.FirstLanunchActivity;
import com.inhand.milk.activity.MainActivity;
import com.inhand.milk.activity.WelcomeActivity;
import com.inhand.milk.entity.User;
import com.inhand.milk.utils.PreJudgingTask;

/**
 * LoginOptFragment
 * Desc:登录选项Fragment
 * Team: Rangers
 * Date: 2015/4/12
 * Time: 18:56
 * Created by: Wooxxx
 */
public class LoginOptFragment extends BaseFragment {
    private ImageView logo;
    private LinearLayout optContainer;
    // 两个登录选项
    private LinearLayout latestOpt;
    private LinearLayout otherOpt;
    // 加载动画
    private Animation logoInAnim;
    private Animation logoOutAnim;
    private final int ANIM_DURATION = 400;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_login_options, container, false);
        initAnimations();
        //先进行登录判断
        Handler x = new Handler();
        x.postDelayed(new NeedLoginHandler(), 1500);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void initViews() {
        logo = (ImageView) rootView.findViewById(R.id.logo);
        latestOpt = (LinearLayout) rootView.findViewById(R.id.by_weixin);
        otherOpt = (LinearLayout) rootView.findViewById(R.id.other);
        optContainer = (LinearLayout) rootView.findViewById(R.id.login_options_container);
    }

    @Override
    public void setListeners() {
        // 点击“用其他方式登录，显示详细登陆方式”
        otherOpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logo.startAnimation(logoOutAnim);
            }
        });
    }


    /**
     * 初始化组件动画
     */
    private void initAnimations() {
        // logo进入位移动画
        logoInAnim = new TranslateAnimation(0, 0, 0, -50);
        logoInAnim.setDuration(ANIM_DURATION);
        logoInAnim.setFillAfter(true);
        logoInAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                optContainer.setVisibility(View.VISIBLE);
                optContainer.startLayoutAnimation();
                optContainer.getLayoutAnimation().
                        getAnimation().setDuration(ANIM_DURATION);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //logo移出位移动画
        logoOutAnim = new TranslateAnimation(0, 0, 0, -1000);
        logoOutAnim.setDuration(ANIM_DURATION);
        logoOutAnim.setFillAfter(true);
        logoOutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                LayoutAnimationController layoutOut = AnimationUtils.loadLayoutAnimation(
                        getActivity(), R.anim.login_options_layout_out);
                layoutOut.getAnimation().setFillAfter(true);
                optContainer.setLayoutAnimation(layoutOut);
                optContainer.setLayoutAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        optContainer.setVisibility(View.GONE);
                        //动画结束时切换到登陆详情页面
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.addToBackStack(null);
                        ft.replace(R.id.main_container, new LoginDetailsFragment());
                        ft.setTransition(FragmentTransaction.TRANSIT_NONE);
                        ft.commit();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                optContainer.scheduleLayoutAnimation();
                optContainer.getLayoutAnimation().
                        getAnimation().setDuration(ANIM_DURATION / 2);
                optContainer.startLayoutAnimation();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 一个延迟任务用以判断是否需要登录
     */
    public class NeedLoginHandler implements Runnable {
        public void run() {
            //如果用户未登录，显示登录选项,否则
            // 根据用户是否含有宝宝判断是否需要进入宝宝信息填写
            if (App.logged()) {
                Log.d("usersex", String.valueOf(AVUser.getCurrentUser(User.class).getSex()));
                PreJudgingTask task=new PreJudgingTask(getActivity());
                task.execute();
            }
            else
                logo.startAnimation(logoInAnim);
        }
    }
}
