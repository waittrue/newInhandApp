package com.inhand.milk.fragment.weight;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SaveCallback;
import com.inhand.milk.App;
import com.inhand.milk.R;
import com.inhand.milk.entity.Baby;
import com.inhand.milk.entity.Weight;
import com.inhand.milk.ui.ObservableHorizonScrollView;
import com.inhand.milk.ui.firstlanunch.Ruler;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2015/6/28.
 */
public class AdderWindow extends Activity {
    private static final String TAG = "AdderWindow";
    private static final int AnimationDuration = 300;
    private static final int spacenum = 2;
    private static final DecimalFormat decimalFormat = new DecimalFormat("###.##");
    private LinearLayout outerLayer, rulerContainer;
    private TextView numTextView;
    private int space;
    private ObservableHorizonScrollView scrollView;
    private Weight weight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_weight_ruler);
        initOuter();

        WindowManager.LayoutParams p = getWindow().getAttributes();  //获取对话框当前的参数值
        p.height = (App.getWindowHeight(this));
        p.width = (App.getWindowWidth(this));
        p.alpha = 1.0f;                //设置本身透明度
        p.dimAmount = 0.0f;                //设置黑暗度
        getWindow().setAttributes(p);     //设置生效
        getWindow().setGravity(Gravity.RIGHT);                 //设置靠右对齐

    }

    @Override
    protected void onResume() {
        super.onResume();
        int height = App.getWindowHeight(this);

        Animation animation = new TranslateAnimation(0, 0, height / 2, 0);
        animation.setDuration(AnimationDuration);
        rulerContainer.startAnimation(animation);

    }

    private void hidden() {
        //windowManager.removeView(view);
        finish();
    }

    private void initOuter() {
        outerLayer = (LinearLayout) findViewById(R.id.weigth_fragment_outest_layer);
        outerLayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outAnimation();
            }
        });
        rulerContainer = (LinearLayout) findViewById(R.id.weight_fragment_ruler_container);

        TextView adderTitle = (TextView) findViewById(R.id.weight_fragment_adder_title_text);
        adderTitle.setText(getCurrentDate());

        ImageView cancleIcon = (ImageView) findViewById(R.id.weight_fragment_adder_cancle_icon);
        ImageView okIcon = (ImageView) findViewById(R.id.weight_fragment_adder_ok_icon);
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
                weight = new Weight();
                weight.setMoonAge(getMonthDay());
                float num;
                try {
                    num = Float.parseFloat(numTextView.getText().toString());
                } catch (Exception e) {
                    return;
                }
                weight.setWeight(num);
                weight.save(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e != null)
                            return;
                        Baby baby = App.getCurrentBaby();
                        baby.addWeight(weight);
                        baby.save(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e != null) {
                                    Log.i("AdderWindow", "save failed");
                                    return;
                                }
                                Log.i("AdderWindow", "save success");
                                new WeightAcache().syncNoFresh();
                            }
                        });
                    }
                });

                /*
                baby.saveInCache(App.getAppContext(), new Base.CacheSavingCallback() {
                    @Override
                    public void done() {
                        Log.i(TAG,"baby save success");
                    }
                });
                */
            }
        });
        numTextView = (TextView) findViewById(R.id.weight_fragment_adder_num_text);
        initRuler();

    }

    private int getMonthDay() {
        Baby baby = App.getCurrentBaby();
        int months = getMonths(baby.getBirthday());
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return WeightMonth.createbabyMonth(months, day);
    }

    private int getMonths(String birth) {

        int months = 0;
        if (birth == null)
            return months;
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy年mm月dd日");
        Date date;
        try {
            date = dateFormat1.parse(birth);
        } catch (ParseException e) {
            return 0;
        }
        Date today = new Date();
        Calendar birthCalendar = Calendar.getInstance();
        birthCalendar.setTime(date);
        Calendar todayCalendr = Calendar.getInstance();
        todayCalendr.setTime(date);

        int year1 = birthCalendar.get(Calendar.YEAR);
        int year2 = todayCalendr.get(Calendar.YEAR);
        int month1 = birthCalendar.get(Calendar.MONTH);
        int month2 = todayCalendr.get(Calendar.MONTH);
        int month = 0;
        if (month2 >= month1)
            month = month2 - month1;
        else if (month2 < month1) {
            month = 12 + month2 - month1;
            year2--;
        }
        if (year2 < year1)
            return 0;
        month = 12 * (year2 - year1) + month;
        return month;

    }
    private void initRuler() {
        space = App.getWindowWidth(this) / 30;
        int height = App.getWindowHeight(this) - App.getStatusHeight(this);
        //这接下来的和布局有关,最后我们要求出ruler的高度.
        height = height / 2;
        height = height - getResources().getDimensionPixelOffset(R.dimen.weight_fragment_adder_title_height) -
                getResources().getDimensionPixelOffset(R.dimen.weight_fragment_adder_title_line_height);
        height = height / 3 - getResources().getDimensionPixelOffset(R.dimen.weight_fragment_adder_ruler_container_margin_top);

        LinearLayout ruler = (LinearLayout) findViewById(R.id.weight_fragment_adder_really_ruler_container);
        Ruler myRuler = new Ruler(this,
                this.getResources().getDimensionPixelSize(R.dimen.weight_fragment_adder_ruler_container_width) -
                        2 * this.getResources().getDimensionPixelOffset(R.dimen.weight_fragment_adder_ruler_margin_left_right),
                0, height,
                0, 400, space, spacenum, true);
        myRuler.setRulerLineColor(getResources().getColor(R.color.weight_fragment_ruler_color));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ruler.addView(myRuler, lp);
        scrollView = (ObservableHorizonScrollView) findViewById(R.id.weight_fragment_adder_observableHorizonScorollView);
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

    private String getCurrentDate() {
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        return sdf.format(today);
    }

    private void outAnimation() {
        int height = App.getWindowHeight(this);
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
}
