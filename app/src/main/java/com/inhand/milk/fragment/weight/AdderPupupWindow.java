package com.inhand.milk.fragment.weight;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.inhand.milk.App;
import com.inhand.milk.R;
import com.inhand.milk.utils.ObservableHorizonScrollView;
import com.inhand.milk.utils.firstlanunch.Ruler;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2015/6/19.
 */
public class AdderPupupWindow {

    private static final String TAG = "FloatADDERwindow";
    private WindowManager windowManager;
    private View view;
    private Activity activity;
    private LinearLayout outerLayer, rulerContainer;
    private TextView numTextView;
    private int space;
    private PopupWindow popupWindow;
    private static final int AnimationDuration = 300;
    private static final int spacenum = 2;
    private static final DecimalFormat decimalFormat = new DecimalFormat("###.##");
    private ObservableHorizonScrollView scrollView;

    public AdderPupupWindow(Activity activity) {
        windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);


        view = LayoutInflater.from(activity).inflate(R.layout.fragment_weight_ruler, null);
        initPopupWindow(view);
        this.activity = activity;
        initOuter(view);
    }

    private void initPopupWindow(View view) {
        popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT, true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        ColorDrawable dw = new ColorDrawable(0x77000000);
        popupWindow.setBackgroundDrawable(dw);

        popupWindow.setAnimationStyle(R.style.AnimationPopupwindow);
    }

    public void show() {
        WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
        mParams.gravity = Gravity.LEFT | Gravity.TOP;
        mParams.x = 0;
        mParams.y = 0;
        mParams.type = 2;
        mParams.flags = WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        mParams.height = LinearLayout.LayoutParams.MATCH_PARENT;
        mParams.width = LinearLayout.LayoutParams.MATCH_PARENT;

        // windowManager.addView(view, mParams);
        Log.i(TAG, "show");
        popupWindow.showAsDropDown(view);
        int height = App.getWindowHeight(activity);
        Animation animation = new TranslateAnimation(0, 0, height / 2, 0);
        animation.setDuration(AnimationDuration);
        rulerContainer.startAnimation(animation);


    }

    private void hidden() {
        //windowManager.removeView(view);
        popupWindow.dismiss();
    }

    private void initOuter(View view) {
        outerLayer = (LinearLayout) view.findViewById(R.id.weigth_fragment_outest_layer);
        rulerContainer = (LinearLayout) view.findViewById(R.id.weight_fragment_ruler_container);

        TextView adderTitle = (TextView) view.findViewById(R.id.weight_fragment_adder_title_text);
        adderTitle.setText(getCurrentDate());

        ImageView cancleIcon = (ImageView) view.findViewById(R.id.weight_fragment_adder_cancle_icon);
        ImageView okIcon = (ImageView) view.findViewById(R.id.weight_fragment_adder_ok_icon);
        cancleIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outAnimation();
            }
        });
        okIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outAnimation();
                //还有数据存储没有做
            }
        });
        numTextView = (TextView) view.findViewById(R.id.weight_fragment_adder_num_text);
        initRuler(view);

    }

    private void initRuler(View view) {
        space = App.getWindowWidth(activity) / 30;
        int height = App.getWindowHeight(activity) - App.getStatusHeight(activity);
        //这接下来的和布局有关,最后我们要求出ruler的高度.
        height = height / 2;
        height = height - activity.getResources().getDimensionPixelOffset(R.dimen.weight_fragment_adder_title_height) -
                activity.getResources().getDimensionPixelOffset(R.dimen.weight_fragment_adder_title_line_height);
        height = height / 3 - activity.getResources().getDimensionPixelOffset(R.dimen.weight_fragment_adder_ruler_container_margin_top);

        LinearLayout ruler = (LinearLayout) view.findViewById(R.id.weight_fragment_adder_really_ruler_container);
        Ruler myRuler = new Ruler(activity,
                activity.getResources().getDimensionPixelSize(R.dimen.weight_fragment_adder_ruler_container_width) -
                        2 * activity.getResources().getDimensionPixelOffset(R.dimen.weight_fragment_adder_ruler_margin_left_right),
                0, height,
                0, 400, space, spacenum, true);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ruler.addView(myRuler, lp);
        scrollView = (ObservableHorizonScrollView) view.findViewById(R.id.weight_fragment_adder_observableHorizonScorollView);
        scrollView.setScrollViewListener(new ObservableHorizonScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(ObservableHorizonScrollView scrollView, int x, int y, int oldx, int oldy) {
                float xx = scrollView.getScrollX();
                xx = xx / space * spacenum / 10f;
                String str = decimalFormat.format(xx);
                numTextView.setText(str);
            }
        });
    }

    private void outAnimation() {
        int height = App.getWindowHeight(activity);
        Animation animation = new TranslateAnimation(0, 0, 0, height / 2);
        animation.setDuration(AnimationDuration);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                hidden();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        rulerContainer.startAnimation(animation);

    }

    private String getCurrentDate() {
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd");
        return sdf.format(today);
    }

}

