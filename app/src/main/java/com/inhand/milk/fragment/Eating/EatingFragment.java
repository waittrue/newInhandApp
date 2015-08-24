package com.inhand.milk.fragment.Eating;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inhand.milk.App;
import com.inhand.milk.R;
import com.inhand.milk.activity.EatingCustomPlanActivity;
import com.inhand.milk.alarm.AlarmSeting;
import com.inhand.milk.fragment.TitleFragment;
import com.inhand.milk.helper.EatingPlanHelper;
import com.inhand.milk.ui.ObservableHorizonScrollView;
import com.inhand.milk.ui.RoundImageView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2015/7/24.
 */
public class EatingFragment extends TitleFragment {
    private static final String TAG = "EatingFragment";
    private static final int TimeEating = 30;
    private static final int COUNTING_TIME = 1, EATING = 2, ENDING = 3;
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
    public static String PlanTimeKey = "plantimekey", IsMilkKey = "ismilkkey";
    private float width, onePlanShowWidth;
    private int id;
    private android.os.Handler handler;
    private Runnable runnable;
    private int[] countTime;
    private List<int[]> planTime = new ArrayList<>();
    private boolean[] isMilk = new boolean[]{false, false, false, false, true, true, true, true};
    private int eatingTimeout = -1, endingTimeOut;
    private EatingPopUpWindow eatingPopUpWindow;

    public static int generateViewId() {
        for (; ; ) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.eating_fragment, container, false);
        setTitleview(getString(R.string.eating_title), 0);
        width = App.getWindowWidth(getActivity());
        onePlanShowWidth = width * 0.245f;
        handler = new Handler();
        id = generateViewId();
        initPlanTime();
        initNotifacation();
        initPlanShow();
        initButtons();
        return mView;
    }

    private void initButtons() {
        RoundImageView custom = (RoundImageView) mView.findViewById(R.id.eating_plan_custom);
        eatingPopUpWindow = new EatingPopUpWindow(getActivity());
        eatingPopUpWindow.setRightButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EatingCustomPlanActivity.class);
                intent.putExtra(IsMilkKey, isMilk);
                intent.putExtra(PlanTimeKey, (Serializable) planTime);
                getActivity().startActivity(intent);
                eatingPopUpWindow.dismissWithoutAnimation();
                // Log.i("eatingfragmnet","onclick");
            }
        });
        custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eatingPopUpWindow.show();
            }
        });
    }

    private void smoothScrollto(int i) {
        if (i < 0 || i > planTime.size() - 1)
            return;
        // Log.i(TAG,String.valueOf(i));
        ObservableHorizonScrollView observableHorizonScrollView = (ObservableHorizonScrollView) mView.findViewById(R.id.eating_plan_show_scroll);
        observableHorizonScrollView.smoothScrollTo((int) (i * onePlanShowWidth), 0);
        // Log.i(TAG, String.valueOf(observableHorizonScrollView == null) + String.valueOf(observableHorizonScrollView.getWidth()));
    }

    private void smooth2Current() {
        Date date = new Date();
        int hour, minute, count;
        int[] time;
        hour = date.getHours();
        minute = date.getMinutes();
        count = planTime.size();
        int i;
        for (i = 0; i < count; i++) {
            time = planTime.get(i);
            if (time[0] < hour)
                continue;
            else if (time[0] == hour) {
                if (time[1] < minute)
                    continue;
            }
            i = i - 2;
            if (i < 0)
                i = 0;
            smoothScrollto(i);
            return;
        }
        i = i - 2;
        smoothScrollto(i);
    }

    private void initPlanTime() {
        EatingPlanHelper eatingPlanHelper = new EatingPlanHelper();
        planTime = eatingPlanHelper.getPlanTime();
        isMilk = eatingPlanHelper.getIsMilk();
    }

    private void initNotifacation() {
        AlarmSeting alarmSeting = new AlarmSeting(App.getAppContext());
        if (App.getAlarmOpen())
            alarmSeting.start(planTime, isMilk);
        else
            alarmSeting.cancleAlarm();
    }

    @Override
    public void onResume() {
        super.onResume();
        startCount();
        // Log.i(TAG,"onresume");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        //Log.i(TAG, String.valueOf(hidden));
        if (hidden == true) {
            stopCount();
        } else {
            startCount();
            android.os.Handler handler = new android.os.Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    smooth2Current();
                }
            }, 100);
        }
    }

    private void time2End() {
        handler.removeCallbacks(runnable);
        startCount();
    }

    private void stopCount() {
        if (handler == null)
            return;
        if (runnable == null)
            return;
        handler.removeCallbacks(runnable);
    }

    private void startCount() {
        stopCount();
        LinearLayout eating, timeCount;
        TextView eatingtitle = (TextView) mView.findViewById(R.id.eating_time_title);
        eating = (LinearLayout) mView.findViewById(R.id.eating_plan_eating_container);
        timeCount = (LinearLayout) mView.findViewById(R.id.eating_plan_time_count__container);
        switch (isEating()) {
            case COUNTING_TIME:
                eating.setVisibility(View.GONE);
                timeCount.setVisibility(View.VISIBLE);
                countTime = getCountTime();
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        setTime(countTime);
                        countReduce();
                        handler.postDelayed(runnable, 1000);
                    }
                };
                handler.post(runnable);
                eatingtitle.setText(getResources().getString(R.string.eating_time_title));
                break;
            case EATING:
                eating.setVisibility(View.VISIBLE);
                timeCount.setVisibility(View.GONE);
                TextView textView = (TextView) mView.findViewById(R.id.eating_plan_eating_textview);
                int[] count = getEatingCount();
                String str = getResources().getString(R.string.eating_plan_eating_start) + String.valueOf(count[0]) +
                        getResources().getString(R.string.eating_plan_eating_middle) + String.valueOf(count[1]) +
                        getResources().getString(R.string.eating_plan_eating_end);
                textView.setText(str);
                eatingtitle.setText(getResources().getString(R.string.eating_time_eating_title));
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        startCount();
                    }
                };
                handler.postDelayed(runnable, eatingTimeout);
                break;
            case ENDING:
                eating.setVisibility(View.VISIBLE);
                timeCount.setVisibility(View.GONE);
                TextView textView1 = (TextView) mView.findViewById(R.id.eating_plan_eating_textview);
                int[] count1 = getEatingCount();
                String str1 = getResources().getString(R.string.eating_plan_eating_start) + String.valueOf(count1[0]) +
                        getResources().getString(R.string.eating_plan_eating_middle) + String.valueOf(count1[1]) +
                        getResources().getString(R.string.eating_plan_eating_end);
                textView1.setText(str1);
                eatingtitle.setText(getResources().getString(R.string.eating_time_end_title));
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        startCount();
                    }
                };
                handler.postDelayed(runnable, endingTimeOut);
                break;
        }


    }

    private int[] getEatingCount() {
        Date date = new Date();
        int hour, minute, count;
        int[] time;
        hour = date.getHours();
        minute = date.getMinutes();
        count = planTime.size();
        if (count == 0)
            return null;
        int[] result = new int[2];
        int i;
        for (i = 0; i < count; i++) {
            time = planTime.get(i);
            if (time[0] < hour)
                continue;
            else if (time[0] == hour) {
                if (time[1] < minute)
                    continue;
            }
            break;
        }
        result[0] = i;
        result[1] = count - result[0];
        return result;
    }

    private int isEating() {
        Date date = new Date();
        int hour, minute, count;
        int[] time = null;
        boolean end = true;
        hour = date.getHours();
        minute = date.getMinutes();
        count = planTime.size();
        if (count == 0)
            return COUNTING_TIME;
        for (int i = 0; i < count; i++) {
            time = planTime.get(i);
            if (time[0] < hour)
                continue;
            else if (time[0] == hour) {
                if (time[1] <= minute)
                    continue;
            }
            end = false;
            i = i - 1;
            if (i < 0)
                i = 0;
            time = planTime.get(i);
            break;
        }
        if (end == true) {
            endingTimeOut = (23 - hour) * 60 + (60 - minute);
            endingTimeOut = endingTimeOut * 1000;
            return ENDING;
        }
        int diff;
        diff = (hour - time[0]) * 60 + (minute - time[1]);
        if (diff >= 0 && diff < TimeEating) {
            eatingTimeout = (TimeEating - diff) * 60 * 1000;
            return EATING;
        }
        return COUNTING_TIME;
    }

    private void countReduce() {
        if (countTime[2] == 0) {
            countTime[2] = 59;
            if (countTime[1] == 0) {
                countTime[1] = 59;
                if (countTime[0] == 0) {
                    time2End();
                } else {
                    countTime[0]--;
                }
            } else
                countTime[1]--;
        } else {
            countTime[2]--;
        }
    }

    private void setTime(int[] t) {
        TextView hour = (TextView) mView.findViewById(R.id.eating_time_hour);
        TextView minute = (TextView) mView.findViewById(R.id.eating_time_mm);
        TextView seconde = (TextView) mView.findViewById(R.id.eating_time_second);
        hour.setText(formatTime(t[0]));
        hour.setTypeface(App.Typeface_arial);
        minute.setText(formatTime(t[1]));
        minute.setTypeface(App.Typeface_arial);
        seconde.setText(formatTime(t[2]));
        seconde.setTypeface(App.Typeface_arial);
    }

    private String formatTime(int t) {
        String result;
        if (t < 10)
            result = "0" + String.valueOf(t);
        else
            result = String.valueOf(t);
        return result;
    }

    private int[] getCountTime() {
        Date date = new Date();
        int hour, minute, second, count;
        int[] time;
        hour = date.getHours();
        minute = date.getMinutes();
        second = date.getSeconds();
        count = planTime.size();
        int[] a = new int[3];
        for (int i = 0; i < count; i++) {
            time = planTime.get(i);
            if (time[0] < hour)
                continue;
            else if (time[0] == hour) {
                if (time[1] <= minute)
                    continue;
            }
            time = planTime.get(i);
            a[0] = time[0] - hour;
            a[1] = time[1] - minute;
            a[2] = 0 - second;
            if (a[2] < 0) {
                a[1]--;
                a[2] += 60;

            }
            if (a[1] < 0) {
                a[0]--;
                a[1] += 60;
            }
            break;
        }

        return a;
    }

    private void initPlanShow() {
        LinearLayout linearLayout = (LinearLayout) mView.findViewById(R.id.eating_plan_show_container);
        int count = planTime.size();
        for (int i = 0; i < count; i++) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            linearLayout.addView(getOnePlanView(isMilk(), isOver(i), gettime(i), i == count - 1), lp);
        }
    }

    private boolean isMilk() {
        return true;
    }

    private boolean isOver(int i) {
        Date date = new Date();
        int hour, minute;
        int[] time;
        hour = date.getHours();
        minute = date.getMinutes();
        time = planTime.get(i);
        if (hour < time[0])
            return false;
        else if (hour == time[0]) {
            if (minute < time[1])
                return false;
            return true;
        }
        return true;
    }

    private String gettime(int i) {
        String result;
        int[] time = planTime.get(i);
        result = formatTime(time[0]) + ":" + formatTime(time[1]);
        return result;
    }

    private RelativeLayout getOnePlanView(boolean isMilk, boolean isOver, String doc, boolean last) {
        int viewWidth = (int) (width * 0.135f);
        int marginLeft = (int) (width * 0.07f) / 2;
        RelativeLayout relativeLayout = new RelativeLayout(getActivity());

        LinearLayout picture = getOnePicture(isMilk, isOver, doc);
        RelativeLayout.LayoutParams pictureLayout = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pictureLayout.addRule(RelativeLayout.CENTER_VERTICAL);
        relativeLayout.addView(picture, pictureLayout);
        picture.setId(id);

        if (last == true)
            return relativeLayout;
        TextView textView = new TextView(getActivity());
        textView.setText("····");
        textView.setTextSize(getResources().getDimensionPixelOffset(R.dimen.eating_time_text_dot_size));
        if (isOver)
            textView.setTextColor(getResources().getColor(R.color.public_main_a_color));
        else
            textView.setTextColor(getResources().getColor(R.color.eating_plan_unover_color));
        textView.setGravity(Gravity.CENTER);
        RelativeLayout.LayoutParams textviewLp = new RelativeLayout.LayoutParams(viewWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        textviewLp.addRule(RelativeLayout.RIGHT_OF, id);
        textviewLp.leftMargin = 0;
        textviewLp.addRule(RelativeLayout.CENTER_VERTICAL);
        relativeLayout.addView(textView, textviewLp);
        return relativeLayout;
    }

    private LinearLayout getOnePicture(boolean isMilk, boolean isOver, String doc) {
        LinearLayout linearLayout = new LinearLayout(getActivity());
        int viewWidth = (int) (width * 0.11f);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        ImageView imageView = new ImageView(getActivity());
        if (isMilk && isOver == true) {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.eating_feed_milk_ico));
        } else if (isMilk == false && isOver == true) {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.eating_feed_food_ico));
        } else if (isMilk == true && isOver == false) {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.eating_feed_no_milk_ico));
        } else {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.eating_feed_no_food_ico));
        }
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(viewWidth, viewWidth);
        linearLayout.addView(imageView, lp);
        TextView textView = new TextView(getActivity());
        textView.setText(doc);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, viewWidth / 3);
        textView.setTypeface(App.Typeface_arial);
        if (isOver)
            textView.setTextColor(getResources().getColor(R.color.public_main_a_color));
        else
            textView.setTextColor(getResources().getColor(R.color.eating_plan_unover_color));
        TextPaint tp = textView.getPaint();
        tp.setFakeBoldText(true);
        LinearLayout.LayoutParams textViewLp = new LinearLayout.LayoutParams(viewWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        textViewLp.setMargins(0, getResources().getDimensionPixelOffset(R.dimen.eating_time_text_margin_top), 0, 0);
        linearLayout.addView(textView, textViewLp);
        return linearLayout;
    }
}
