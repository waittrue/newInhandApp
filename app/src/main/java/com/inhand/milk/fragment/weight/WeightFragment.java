package com.inhand.milk.fragment.weight;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inhand.milk.App;
import com.inhand.milk.R;
import com.inhand.milk.entity.Baby;
import com.inhand.milk.entity.Weight;
import com.inhand.milk.fragment.TitleFragment;
import com.inhand.milk.ui.RingWithText;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/6/6.
 */
public class WeightFragment extends TitleFragment {
    private static final DecimalFormat decimalFormat = new DecimalFormat("###.##");
    private WeightExcle weightExcle;
    private int lastPositon = -1;
    private RingWithText ringWithText;
    private Adder adder;
    private int sweepStartColor, sweepEndColor;
    private Baby baby;
    private Map<Integer, Map<Integer, Float>> monthToweights = new HashMap<>();
    private static final String TAG = "weightFragment";
    private static WeightStanderPares weightStanderPares = WeightStanderPares.getInstance();
    private float currentStanderMin = 0, currentStanderMax;
    private Weight currentWeight;
    private Date lastWeightTime;
    private int birthDay;
    private WeightAcache weightAcache;

    public WeightFragment() {
        initData();
    }

    private void initData() {
        baby = App.getCurrentBaby();
        weightAcache = new WeightAcache();
        currentWeight = weightAcache.getCurrentWeight();
        lastWeightTime = weightAcache.getLastWeightTime();
        monthToweights = weightAcache.getMonthToweights();
        initCurrentStander();
    }

    @Override
    public void refresh() {
        Log.i(TAG, "refresh");
        initData();
        initWeightTab(mView);
        initWeightExcle(mView);
        initRelativeContent(mView);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "oncreateView");
        mView = inflater.inflate(R.layout.fragment_weight, container, false);
        initViews(mView);
        return mView;
    }

    private void initViews(View view) {
        initWeightTab(view);
        initLine(view);
        initWeightExcle(view);
        initRelativeContent(view);
        initAdder(view);
    }

    private void initAdder(View view) {
        adder = (Adder) view.findViewById(R.id.weight_fragment_adder);
        adder.setBgColor(getResources().getColor(R.color.weight_fragment_adder_color));
        adder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "click");
                inAnimation();
            }
        });

    }

    private void inAnimation() {
        //floatAdderWindow.show();
        this.startActivity(new Intent(getActivity(), AdderWindow.class));
    }


    private void initRelativeContent(View view) {
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.weight_fragment_ring_container);
        initRingWithText();
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        // relativeLayout.addView(initRingWithText(), lp);
        initRingWithText().setLayoutParams(lp);
        initRelativeLeftTexts(relativeLayout);
        initRelativeRightTexts(relativeLayout);
        initBottomTextView(view);

    }

    private void initBottomTextView(View view) {
        TextView textView = (TextView) view.findViewById(R.id.weight_fragment_bottom_text);
        textView.setText(getLastTime());
    }

    private String getLastTime() {
        String str = getResources().getString(R.string.weight_fragment_bottome_text);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String time = simpleDateFormat.format(lastWeightTime);
        return str + time;
    }

    private void initRelativeLeftTexts(RelativeLayout relativeLayout) {
        TextView leftUp = new TextView(getActivity());
        TextView leftDown = new TextView(getActivity());
        String upString = decimalFormat.format(getCurrentWeight());
        String downString = getResources().getString(R.string.weight_left_down_text);

        float upLeftMargin, downLeftMargin, upTopMargin, downTopMargin;
        int color = sweepStartColor;
        float upTextSize = getResources().getDimension(R.dimen.weight_fragment_left_up_text_size);
        float downTextSize = getResources().getDimension(R.dimen.weight_fragment_left_down_text_size);
        float space = getResources().getDimension(R.dimen.weight_fragment_text_space_size);

        leftUp.setTextColor(color);
        leftUp.setText(upString);
        leftUp.setTextSize(TypedValue.COMPLEX_UNIT_PX, upTextSize);
        leftDown.setTextColor(color);
        leftDown.setText(downString);
        leftDown.setTextSize(TypedValue.COMPLEX_UNIT_PX, downTextSize);

        upLeftMargin = App.getWindowWidth(getActivity()) / 2 - ringWithText.getR() - leftUp.getPaint().measureText(upString);
        upLeftMargin = upLeftMargin / 2;
        downLeftMargin = App.getWindowWidth(getActivity()) / 2 - ringWithText.getR() - leftDown.getPaint().measureText(downString);
        downLeftMargin = downLeftMargin / 2;

        upTopMargin = ringWithText.getR() * 2 - upTextSize - downTextSize - space;
        upTopMargin = upTopMargin / 2;
        downTopMargin = upTopMargin + upTextSize + space;

        RelativeLayout.LayoutParams upLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams downLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        upLp.leftMargin = (int) upLeftMargin;
        upLp.topMargin = (int) upTopMargin;
        downLp.leftMargin = (int) downLeftMargin;
        downLp.topMargin = (int) downTopMargin;
        upLp.addRule(RelativeLayout.ALIGN_TOP, R.id.weight_fragment_ring_withtext);
        downLp.addRule(RelativeLayout.ALIGN_TOP, R.id.weight_fragment_ring_withtext);
        relativeLayout.addView(leftUp, upLp);
        relativeLayout.addView(leftDown, downLp);
    }

    private void initRelativeRightTexts(RelativeLayout relativeLayout) {
        TextView rightUp = new TextView(getActivity());
        TextView rightDown = new TextView(getActivity());
        String upString = getCurrentStander();
        String downString = getResources().getString(R.string.weight_right_down_text);

        float upRightMargin, downRightMargin, upTopMargin, downTopMargin;
        int color = sweepEndColor;
        float upTextSize = getResources().getDimension(R.dimen.weight_fragment_right_up_text_size);
        float downTextSize = getResources().getDimension(R.dimen.weight_fragment_right_down_text_size);
        float space = getResources().getDimension(R.dimen.weight_fragment_text_space_size);
        int width = App.getWindowWidth(getActivity());

        rightUp.setTextColor(color);
        rightUp.setText(upString);
        rightUp.setTextSize(TypedValue.COMPLEX_UNIT_PX, upTextSize);
        rightDown.setTextColor(color);
        rightDown.setText(downString);
        rightDown.setTextSize(TypedValue.COMPLEX_UNIT_PX, downTextSize);

        upRightMargin = App.getWindowWidth(getActivity()) / 2 - ringWithText.getR() - rightUp.getPaint().measureText(upString);
        upRightMargin = upRightMargin / 2;
        downRightMargin = App.getWindowWidth(getActivity()) / 2 - ringWithText.getR() - rightDown.getPaint().measureText(downString);
        downRightMargin = downRightMargin / 2;

        upTopMargin = ringWithText.getR() * 2 - upTextSize - downTextSize - space;
        upTopMargin = upTopMargin / 2;
        downTopMargin = upTopMargin + upTextSize + space;

        RelativeLayout.LayoutParams upLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams downLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        upLp.leftMargin = (int) (width - upRightMargin - rightUp.getPaint().measureText(upString));
        upLp.topMargin = (int) upTopMargin;
        downLp.leftMargin = (int) (width - downRightMargin - rightDown.getPaint().measureText(downString));
        downLp.topMargin = (int) downTopMargin;
        upLp.addRule(RelativeLayout.ALIGN_TOP, R.id.weight_fragment_ring_withtext);
        downLp.addRule(RelativeLayout.ALIGN_TOP, R.id.weight_fragment_ring_withtext);

        relativeLayout.addView(rightUp, upLp);
        relativeLayout.addView(rightDown, downLp);
    }

    private float getCurrentWeight() {
        return currentWeight.getWeight();
    }

    private void initCurrentStander() {
        Date date = lastWeightTime;
        Log.i(TAG, "currentdate:" + String.valueOf(date));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day1 = calendar.get(Calendar.DAY_OF_MONTH);
        String birth = baby.getBirthday();
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy年MM月dd日");
        Date birthdate;
        try {
            birthdate = dateFormat1.parse(birth);
        } catch (Exception e) {
            return;
        }
        calendar.setTime(birthdate);
        int day2 = calendar.get(Calendar.DAY_OF_MONTH);
        int months = getMonths(date);
        int diffDay;
        diffDay = day1 - day2;
        if (day2 > day1) {
            diffDay = 30 + diffDay;
        }
        float nextStandermin, nextMaxStandermax;
        if (baby.getSex() == Baby.FEMALE) {
            currentStanderMin = weightStanderPares.getGirlMin(months);
            currentStanderMax = weightStanderPares.getGirlMax(months);
            nextStandermin = weightStanderPares.getGirlMin(months + 1);
            nextMaxStandermax = weightStanderPares.getGirlMax(months + 1);
            currentStanderMax = (nextMaxStandermax - currentStanderMax) / 30 * diffDay + currentStanderMax;
            currentStanderMin = (nextStandermin - currentStanderMin) / 30 * diffDay + currentStanderMin;
        } else if (baby.getSex() == Baby.MALE) {
            currentStanderMin = weightStanderPares.getBoyMin(months);
            currentStanderMax = weightStanderPares.getBoyMax(months);
            Log.i(TAG, "month:" + String.valueOf(months));
            Log.i(TAG, "month max:" + String.valueOf(currentStanderMax));
            Log.i(TAG, "month min:" + String.valueOf(currentStanderMin));
            nextStandermin = weightStanderPares.getBoyMin(months + 1);
            nextMaxStandermax = weightStanderPares.getBoyMax(months + 1);
            currentStanderMax = (nextMaxStandermax - currentStanderMax) / 30 * diffDay + currentStanderMax;
            currentStanderMin = (nextStandermin - currentStanderMin) / 30 * diffDay + currentStanderMin;
        }

    }
    private String getCurrentStander() {
        DecimalFormat decimalFormat = new DecimalFormat("##.#");
        return decimalFormat.format(currentStanderMin) + "~" + decimalFormat.format(currentStanderMax);
    }

    private RingWithText initRingWithText() {
        float r;
        sweepStartColor = getResources().getColor(R.color.weight_fragment_ring_sweepangle_start_color);
        sweepEndColor = getResources().getColor(R.color.weight_fragment_ring_sweepangle_end_color);
        int height = App.getWindowHeight(getActivity());  // 屏幕高度（像素）
        r = height - getResources().getDimension(R.dimen.weight_fragment_up_total_height) -
                getResources().getDimension(R.dimen.weight_fragment_excle_down_divider_height) -
                getResources().getDimension(R.dimen.footer_navigation_fragment_height) -
                App.getStatusHeight(getActivity());
        //r = r / 2 / 5 * 3 / 2;//这里要跟布局图里面的那个权重对应好;
        r = r / 2 - getResources().getDimension(R.dimen.weight_fragment_bottom_text_height) - getResources().getDimension(R.dimen.weight_fragment_adder_height);
        r = r / 2;
        r = r * 7 / 10;
        //ringWithText = new RingWithText(this.getActivity(), r);
        ringWithText = (RingWithText) mView.findViewById(R.id.weight_fragment_ring_withtext);
        ringWithText.setR(r);
        ringWithText.setSweepColor(sweepStartColor, sweepEndColor);
        ringWithText.setTexts(getRingWithTextStrings(), getRingWithTextSizes(r));
        ringWithText.setRingWidth(r / 6);
        ringWithText.setTexts(new int[]{getResources().getColor(R.color.weight_fragment_ring_text_color),
                getResources().getColor(R.color.public_main_a_color),});
        ringWithText.setRingBgColor(getResources().getColor(R.color.weight_fragment_ring_bg_color));
        return ringWithText;
    }

    private float[] getRingWithTextSizes(float r) {
        float[] result = new float[2];
        result[0] = r / 5;
        result[1] = r / 2.5f;
        return result;
    }

    private String[] getRingWithTextStrings() {
        String[] result = new String[2];
        result[0] = getResources().getString(R.string.weight_ring_text_up);
        float weight = currentWeight.getWeight();
        if (weight < currentStanderMin) {
            result[1] = getResources().getStringArray(R.array.weight_ring_text_status)[0];
        } else if (weight > currentStanderMax) {
            result[1] = getResources().getStringArray(R.array.weight_ring_text_status)[2];
        } else {
            result[1] = getResources().getStringArray(R.array.weight_ring_text_status)[1];
        }
        return result;
    }

    private void initWeightExcle(View view) {
        weightExcle = (WeightExcle) view.findViewById(R.id.weight_fragment_excle);
        monthToWeightExcle(weightExcle, lastPositon);
    }

    private void addPoints(WeightExcle weightExcle, int posiont) {
        weightExcle.clearPoints();
        Log.i(TAG, String.valueOf(posiont));
        Map<Integer, Float> weights = monthToweights.get(posiont);
        for (Integer key : weights.keySet()) {
            float weight = weights.get(key);
            Log.i(TAG + String.valueOf(key), String.valueOf(weight));
            key = key - birthDay;
            if (key < 0)
                key = getMonthDays(posiont) + key + 1;
            else
                key = key + 1;
            weightExcle.addPoint(key, weight);
        }
    }

    private void initWeightTab(View view) {
        WeightTab weightTab = (WeightTab) view.findViewById(R.id.weight_tabs);
        int months = getMonths();
        weightTab.setTabNum(months + 1);
        lastPositon = months;
        weightTab.initTabs();
        weightTab.setStopLisetner(new WeightTab.StopLisetner() {
            @Override
            public void stopLisetner(int position) {
                if (lastPositon == position)
                    return;
                Log.i(TAG, "position" + String.valueOf(position));
                lastPositon = position;
                monthToWeightExcle(weightExcle, position);
                weightExcle.invalidate();
            }
        });
    }

    private int getMonths() {
        Date today = new Date();
        return getMonths(today);
    }

    private int getMonths(Date today) {
        String birth = baby.getBirthday();
        Log.i(TAG, "birth :" + birth);
        int months = 0;
        if (birth == null)
            return months;
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy年MM月dd日");
        Log.i(TAG, "today:" + dateFormat1.format(today));
        Date date;
        try {
            date = dateFormat1.parse(birth);
        } catch (ParseException e) {
            return 0;
        }
        int mun = calcuteDiffMonth(date, today);
        Log.i(TAG, "num:" + String.valueOf(mun));
        return mun;
    }

    private int calcuteDiffMonth(Date start, Date end) {
        Calendar birthCalendar = Calendar.getInstance();
        birthCalendar.setTime(start);
        Calendar todayCalendr = Calendar.getInstance();
        todayCalendr.setTime(end);

        int year1 = birthCalendar.get(Calendar.YEAR);
        int year2 = todayCalendr.get(Calendar.YEAR);
        int month1 = birthCalendar.get(Calendar.MONTH);
        int month2 = todayCalendr.get(Calendar.MONTH);
        int day1 = birthCalendar.get(Calendar.DAY_OF_MONTH);
        birthDay = day1;
        int day2 = todayCalendr.get(Calendar.DAY_OF_MONTH);
        int month = 0;
        if (day2 < day1) {
            month2 = month2 - 1;
            if (month2 < 0) {
                month2 = 11;
                year2 = year2--;
            }
        } else {
            month = 1;
        }

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
    private void monthToWeightExcle(WeightExcle weightExcle, int position) {
        weightExcle.clearPoints();
        weightExcle.clearStander();
        addPoints(weightExcle, position);
        if (baby.getSex() == Baby.FEMALE) {
            weightExcle.setStanderLeft(weightStanderPares.getGirlMin(position), weightStanderPares.getGirlMax(position));
            weightExcle.setStanderRight(weightStanderPares.getGirlMin(position + 1), weightStanderPares.getGirlMax(position + 1));
        } else if (baby.getSex() == Baby.MALE) {
            weightExcle.setStanderLeft(weightStanderPares.getBoyMin(position), weightStanderPares.getBoyMax(position));
            weightExcle.setStanderRight(weightStanderPares.getBoyMin(position + 1), weightStanderPares.getBoyMax(position + 1));
        }
        weightExcle.setMonthDays(getMonthDays(position));
    }

    private int getMonthDays(int position) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -position);
        int result = calendar.getActualMaximum(Calendar.DATE);
        Log.i(TAG, String.valueOf(result) + " nums of month");
        return result;
    }
    private void initLine(View view) {
        ImageView imageView = new ImageView(this.getActivity());
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.weight_fragment_line_container);
        WindowManager wm = (WindowManager) getActivity()
                .getSystemService(Context.WINDOW_SERVICE);

        int lineWidth = wm.getDefaultDisplay().getWidth() / 4;
        imageView.setLayoutParams(new LinearLayout.LayoutParams(lineWidth, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setBackgroundColor(getResources().getColor(R.color.public_darkin_highlight_a_color));
        //imageView.setImageDrawable(new ColorDrawable(getResources().getColor(R.color.color_white)));
        linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        linearLayout.addView(imageView);
    }

}
