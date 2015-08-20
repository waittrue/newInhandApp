package com.inhand.milk.ui;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inhand.milk.R;

/**
 * Created by Administrator on 2015/7/3.
 */
public class PopupWindowSelected {
    private static final float alpha = 0.5f;
    private View popView;
    private PopupWindow headPopupWiondow;
    private Activity activity;
    private RelativeLayout first, seconde, cancle;
    private TextView firstTextView, SecondTextView;
    private LinearLayout popupWindowContent, popupWhiteSpace, popupWindowWhole;

    public PopupWindowSelected(Activity context) {
        activity = context;
        popView = LayoutInflater.from(context).inflate(R.layout.popupwindow_select, null);
        firstTextView = (TextView) popView.findViewById(R.id.popupwindow_selected_first_textview);
        SecondTextView = (TextView) popView.findViewById(R.id.popupwindow_selected_seconde_textview);
        first = (RelativeLayout) popView.findViewById(R.id.popupwindow_selected_first_item);
        seconde = (RelativeLayout) popView.findViewById(R.id.popupwindow_selected_seconde_item);
        cancle = (RelativeLayout) popView.findViewById(R.id.popupwindow_selected_cancle);
        popupWindowWhole = (LinearLayout) popView.findViewById(R.id.popupwindow_selected_whole_window);
        popupWhiteSpace = (LinearLayout) popView.findViewById(R.id.popupwindow_selected_white_space);
        popupWhiteSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        popupWindowContent = (LinearLayout) popView.findViewById(R.id.popupwindow_selected_content);

        headPopupWiondow = new PopupWindow(popView, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT, true);
        headPopupWiondow.setFocusable(true);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    public void setFirstListener(View.OnClickListener listener) {
        first.setOnClickListener(listener);
    }

    public void setFirstItemText(String str) {
        firstTextView.setText(str);
    }

    public void setSecondeListener(View.OnClickListener listener) {
        seconde.setOnClickListener(listener);
    }

    public void setSecondeItemText(String str) {
        SecondTextView.setText(str);
    }

    public void show() {
        headPopupWiondow.showAtLocation(popView, Gravity.BOTTOM, 0, 0);
        popupWindowWhole.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.popupwindow_in);
        popupWindowContent.startAnimation(animation);
    }

    public void dismiss() {

        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.popupwindow_out);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                popupWindowWhole.setVisibility(View.GONE);
                headPopupWiondow.dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        popupWindowContent.startAnimation(animation);

    }

}
