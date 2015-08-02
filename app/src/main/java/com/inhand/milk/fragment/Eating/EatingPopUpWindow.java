package com.inhand.milk.fragment.Eating;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.content.Context;
import android.media.Image;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.inhand.milk.App;
import com.inhand.milk.R;

/**
 * Created by Administrator on 2015/7/29.
 */
public class EatingPopUpWindow {
    private View mview;
    private LinearLayout content,outside;
    private PopupWindow popupWindow;
    private Context context;
    private ImageView leftImageView,rightImageView;
    private View.OnClickListener rightListener,leftListener;
    public  EatingPopUpWindow(Context context){
        this.context = context;
        initView(context);
    }
    private void initView(Context context){
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        mview = layoutInflater.inflate(R.layout.eating_popupwindow, null);
        popupWindow = new PopupWindow(mview, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT, true);
        popupWindow.setFocusable(true);
        content = (LinearLayout)mview.findViewById(R.id.eating_pop_content);
        outside = (LinearLayout)mview.findViewById(R.id.eating_pop_outside);
        leftImageView = (ImageView)mview.findViewById(R.id.eating_pop_recommend);
        rightImageView = (ImageView)mview.findViewById(R.id.eating_pop_custom);

        outside.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }
    public void show() {
        popupWindow.showAtLocation(mview, Gravity.BOTTOM, 0, 0);
        setLeftImageViewAnimation();
        setRightImageViewAnimation();
    }
    public void setRightButtonListener(View.OnClickListener listener){
        rightListener = listener;
        rightImageView.setOnClickListener(listener);
    }
    public void setLeftButtonListener(View.OnClickListener listener){
        leftListener = listener;
        leftImageView.setOnClickListener(listener);
    }
    private void setLeftImageViewAnimation(){
        float width = App.getWindowWidth(context);
        float ImageWidth = ((float)context.getResources().getDimensionPixelOffset(R.dimen.eating_pop_btn_width_height))/284*233 ;
        Animation first = new TranslateAnimation(-width/2,(width/2 - ImageWidth)/2,0,0);
        first.setDuration(200);
        final Animation second = new TranslateAnimation((width/2 - ImageWidth)/2,0,0,0);
        second.setDuration(200);
        first.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                leftImageView.startAnimation(second);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        leftImageView.startAnimation(first);

    }
    private void setRightImageViewAnimation(){
        float width = App.getWindowWidth(context);
        float ImageWidth = ((float)context.getResources().getDimensionPixelOffset(R.dimen.eating_pop_btn_width_height))/284*233 ;//这个比例是图片额比例
        Animation first = new TranslateAnimation(width/2,-(width/2 - ImageWidth)/2,0,0);
        first.setDuration(200);
        final Animation second = new TranslateAnimation(-(width/2 - ImageWidth)/2,0,0,0);
        second.setDuration(200);
        first.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                rightImageView.startAnimation(second);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        rightImageView.startAnimation(first);

    }
    private void dismiss(){
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.popupwindow_out);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mview.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        content.startAnimation(animation);
    }

}
