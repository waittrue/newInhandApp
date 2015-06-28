package com.inhand.milk.fragment.milk_amount;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inhand.milk.R;
import com.inhand.milk.fragment.TitleFragment;
import com.inhand.milk.utils.MultiLayerCircle;
import com.inhand.milk.utils.PinnerListView;
import com.inhand.milk.utils.PinnerListViewAdapter;
import com.inhand.milk.utils.ProgressBar;
import com.inhand.milk.utils.RingWithText;
import com.inhand.milk.utils.ViewHolder;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/6/4.
 */
public class MilkAmountFragment extends TitleFragment {
    private TextView adviseNum, drinkNum;
    private float drinkAmount, adviseAmount;
    private RingWithText ringWithText;
    private final static int timeRing = 200, dataLoadAmount = 4;
    private int[] multiLayerCircleColors, multiLayerCircleWeights;
    private List<ProgressBar> progressBarList;
    private PinnerListView headlistView;
    private int warningHighColor, warningLowColor, normalColor, progressBgColor;
    private DecimalFormat decimalFormat;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_milk_amount, container, false);
        setTitleview(getResources().getString(R.string.milk_amount_title), 1, null, null);
        initVarables();
        initViews();
        return mView;
    }

    private void initVarables() {
        decimalFormat = new DecimalFormat("###.#");
        warningHighColor = getResources().getColor(R.color.milk_amount_listview_list_warining_high_tempreature_color);
        warningLowColor = getResources().getColor(R.color.milk_amount_listview_list_warining_low_tempreature_color);
        normalColor = getResources().getColor(R.color.milk_amount_listview_list_normal_tempreature_color);
        progressBgColor = getResources().getColor(R.color.milk_amount_listview_item_progress_bar_background_color);
        multiLayerCircleColors = getResources().getIntArray(R.array.milk_amount_listview_list_item_circle_colors);
        multiLayerCircleWeights = getResources().getIntArray(R.array.milk_amount_listview_item_title_circle_weights);
        progressBarList = new ArrayList<>();
    }

    private void initViews() {

        drinkNum = (TextView) mView.findViewById(R.id.milk_amount_drink_num);
        drinkNum.setText(getResources().getString(R.string.milk_amount_drink_num_doc) + getOneDayDrinkAmount());
        adviseNum = (TextView) mView.findViewById(R.id.milk_amount_advise_num);
        adviseNum.setText(getResources().getString(R.string.milk_amount_advise_num_doc) + getOneDayAdviseAmount());
        initRingWithText();
        initListViews();

    }

    private void initRingWithText() {
        LinearLayout linearLayout = (LinearLayout) mView.findViewById(R.id.milk_amount_advise_ring_container);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(getResources().getDimensionPixelOffset(R.dimen.milk_amount_advise_ring_size_D),
                getResources().getDimensionPixelOffset(R.dimen.milk_amount_advise_ring_size_D));
        ringWithText = new RingWithText(getActivity().getApplicationContext(),
                getResources().getDimension(R.dimen.milk_amount_advise_ring_size_D) / 2);
        linearLayout.addView(ringWithText, lp);
        ringWithText.setRingBgColor(getResources().getColor(R.color.milk_amount_ring_bg_color));
        ringWithText.setRingColor(getResources().getColor(R.color.milk_amount_ring_color));
        ringWithText.setTextColor(getResources().getColor(R.color.milk_amount_ring_text_color));
        ringWithText.setMaxSweepAngle(drinkAmount / adviseAmount * 360);
        final String[] strings = new String[2];
        strings[0] = getResources().getString(R.string.milk_amount_advise_ring_up_doc);
        DecimalFormat format = new DecimalFormat("###");
        strings[1] = String.valueOf(format.format(drinkAmount / adviseAmount * 100)) + "%";
        float[] sizes = {getResources().getDimension(R.dimen.milk_amount_advise_ring_text_up_size),
                getResources().getDimension(R.dimen.milk_amount_advise_ring_text_down_size)};
        ringWithText.setTexts(strings, sizes);
        RingWithText.updateListener listener = new RingWithText.updateListener() {
            @Override
            public void updateSweepAngle(float sweepAngle) {
                DecimalFormat format = new DecimalFormat("###");
                strings[1] = String.valueOf(format.format(sweepAngle / 360 * 100)) + "%";
            }
        };
        ringWithText.setListener(listener);
        ringWithText.setTimeRing(timeRing);
    }

    private void initListViews() {
        headlistView = (PinnerListView) mView.findViewById(R.id.milk_amount_listview);
        headlistView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        headlistView.setDividerHeight(0);
        headlistView.setHead(R.layout.fragment_milk_amount_listview_head);

        final PinnerListViewAdapter adpter = new PinnerListViewAdapter(this.getActivity());
        adpter.setConfigureView(new PinnerListViewAdapter.ConfigureView() {
            @Override
            public View configureHead(Map<String, Object> map, View convertView, int position) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_milk_amount_listview_head, null);
                }
                TextView textView = ViewHolder.get(convertView, R.id.milk_amount_time_text);
                textView.setText((String) map.get(HEAD_DATA));
                textView = ViewHolder.get(convertView, R.id.milk_amount_total_num_text);
                textView.setText((String) map.get(HEAD_TOTALNUM));
                LinearLayout linearLayout = ViewHolder.get(convertView, R.id.milk_amount_listview_item_title_circle_container);
                View child = linearLayout.getChildAt(0);
                if (child == null) {
                    child = new MultiLayerCircle(getActivity(),
                            getResources().getDimension(R.dimen.milk_amount_circle_D) / 2, multiLayerCircleColors, multiLayerCircleWeights);
                    linearLayout.addView(child);
                }
                return convertView;
            }

            @Override
            public View configureContent(Map<String, Object> map, View convertView, int position) {
                ProgressBar progressBar;
                if (convertView == null) {
                    convertView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_milk_amount_listview_content, null);
                    LinearLayout linearLayout = ViewHolder.get(convertView, R.id.milk_amount_listview_item_circle_container);
                    float r = getResources().getDimension(R.dimen.milk_amount_listview_item_circle_D) / 2;
                    RingWithText ring = new RingWithText(getActivity().getApplicationContext(), r);
                    ring.setTexts(new float[]{getResources().getDimension(R.dimen.milk_amount_listview_item_circle_up_text_size),
                            getResources().getDimension(R.dimen.milk_amount_listview_item_circle_down_text_size)});
                    ring.setTextColor(Color.WHITE);
                    ring.setRingWidth(r);
                    linearLayout.addView(ring);

                    linearLayout = ViewHolder.get(convertView, R.id.milk_amount_listview_item_progress_bar);
                    progressBar = new ProgressBar(getActivity().getApplicationContext(),
                            getResources().getDimension(R.dimen.milk_amount_listview_item_progress_bar_width),
                            getResources().getDimension(R.dimen.milk_amount_listview_item_progress_bar_height));
                    progressBar.setBgColor(progressBgColor);
                    progressBar.setTimeAnimator(timeRing);
                    linearLayout.addView(progressBar);
                    progressBarList.add(progressBar);
                }
                TextView textView = ViewHolder.get(convertView, R.id.milk_amount_listview_item_time_text);
                textView.setText((String) map.get(CONTETN_TIME));
                textView = ViewHolder.get(convertView, R.id.milk_amount_listview_item_amount_text);
                textView.setText((String) map.get(CONTENT_AMOUNT));
                textView = ViewHolder.get(convertView, R.id.milk_amount_listview_item_temperature_amount_text);
                textView.setText((String) map.get(CONTENT_TP));
                LinearLayout linearLayout = ViewHolder.get(convertView, R.id.milk_amount_listview_item_circle_container);
                ((RingWithText) linearLayout.getChildAt(0)).setTexts(new String[]{String.valueOf((int) (float) map.get(CONTENT_SCORE)), "分"});
                ((RingWithText) linearLayout.getChildAt(0)).setRingBgColor((int) map.get(CONTENT_COLOR));

                linearLayout = ViewHolder.get(convertView, R.id.milk_amount_listview_item_progress_bar);
                progressBar = (ProgressBar) linearLayout.getChildAt(0);
                progressBar.setColor((int) map.get(CONTENT_COLOR));
                progressBar.setMaxNum((float) map.get(CONTENT_SCORE));


                linearLayout = ViewHolder.get(convertView, R.id.milk_amount_listview_item_divide_temperature_amount);
                linearLayout.setBackgroundColor((int) map.get(CONTENT_COLOR));

                linearLayout = ViewHolder.get(convertView, R.id.milk_amount_listview_item_divider);
                if (adpter.hasHead(position + 1)) {
                    linearLayout.setVisibility(View.INVISIBLE);
                } else
                    linearLayout.setVisibility(View.VISIBLE);
                return convertView;
            }
        });
        initMyData(adpter);
        headlistView.setAdapter(adpter);

    }

    private void initMyData(PinnerListViewAdapter adapter) {
        for (int i = 0; i < dataLoadAmount; i++) {
            for (int j = 0; j < 8; j++)
                adapter.addMap(getHeadData(i), getContentData(), i * 8 + j);
        }
    }

    private static final String HEAD_DATA = "data", HEAD_TOTALNUM = "totalNum";

    private Map<String, Object> getHeadData(int days) {
        Map<String, Object> map = new HashMap<>();
        map.put(HEAD_DATA, getCalenderBefore(days));
        map.put(HEAD_TOTALNUM, getResources().getString(R.string.milk_amount_drink_total_num_doc) +
                String.valueOf(getTotalDrinkNum()) + "次");
        return map;
    }

    private static final String CONTETN_TIME = "onceTime", CONTENT_TP = "oncetemperate", CONTENT_AMOUNT = "onceAmount",
            CONTENT_COLOR = "color", CONTENT_SCORE = "score";

    private Map<String, Object> getContentData() {
        Map<String, Object> map = new HashMap<>();
        int color;
        float score, amount;
        float[] mTemperature;
        amount = getOnceAmount();
        mTemperature = getOnceTemperature();
        map.put(CONTETN_TIME, getOnceTime());
        DecimalFormat format = new DecimalFormat("###");
        map.put(CONTENT_TP, format.format(mTemperature[1]) + "°C");
        map.put(CONTENT_AMOUNT, format.format(amount) + "ml");

        score = getRecord(getAdviseAmount(), amount, mTemperature[0], mTemperature[1], getDrinkTime());
        color = selectColor(mTemperature[0], mTemperature[1]);
        map.put(CONTENT_COLOR, color);
        map.put(CONTENT_SCORE, score);
        return map;
    }


    @Override
    public void refresh() {
        startAnimator();

    }

    private void startAnimator() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startProgressBar();
                ringWithText.startRing();
            }
        }, 200);

    }

    private void startProgressBar() {
        int count = progressBarList.size();

        if (count == 0)
            return;
        ProgressBar progressBar;
        for (int i = 0; i < count; i++) {
            progressBar = progressBarList.get(i);
            progressBar.startAnimator();
        }
    }

    private String getCalenderBefore(int days) {
        if (days == 0)
            return "今天";
        if (days == 1)
            return "昨天";
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -days);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return String.valueOf(year) + "年" + String.valueOf(month) + "月" + String.valueOf(day) + "日";

    }

    private int selectColor(float temperatureHigh, float temperatureLow) {
        float ratio1 = 0, ratio2 = 0;
        if (temperatureHigh > TEMPREATUREHIGH)
            ratio1 = (temperatureHigh - TEMPREATUREHIGH) / TEMPREATUREHIGH;
        if (temperatureLow < TEMPREATURELOW)
            ratio2 = (TEMPREATURELOW - temperatureLow) / TEMPREATURELOW;
        if (ratio1 > ratio2 && ratio1 > 0)
            return warningHighColor;
        if (ratio1 < ratio2 && ratio2 > 0)
            return warningLowColor;
        if (ratio1 == ratio2 && ratio1 == 0)
            return normalColor;
        return warningLowColor;
    }

    private String getOneDayDrinkAmount() {

        drinkAmount = getDrinkAmount();
        return decimalFormat.format(drinkAmount) + "ml";
    }

    private String getOneDayAdviseAmount() {
        adviseAmount = getAdviseAmount();
        return decimalFormat.format(adviseAmount) + "ml";
    }

    private String getOnceTime() {
        return "08:36";
    }

    private float getAdviseAmount() {
        return (float) Math.random() * 50 + 200;
    }

    private float getDrinkAmount() {
        return (float) Math.random() * 50 + 200;
    }

    private int getTotalDrinkNum() {
        return 6;
    }

    private int getDrinkTime() {
        return 10;
    }

    private float[] getOnceTemperature() {
        float[] tempreture = new float[2];
        float temp;
        tempreture[0] = (float) (Math.random() * 30 + 10);
        tempreture[1] = (float) (Math.random() * 30 + 10);
        if (tempreture[0] < tempreture[1]) {
            temp = tempreture[0];
            tempreture[0] = tempreture[1];
            tempreture[1] = temp;
        }

        return tempreture;
    }

    private float getOnceAmount() {
        return 300;
    }

    final static float AMOUNTSCORE = 55;
    final static float TEMPERATURESCORE = 35;
    final static float TIMESCORE = 10, TEMPREATUREHIGH = 40, TEMPREATURELOW = 37, STANDARTIME = 30;

    private float getRecord(float advise, float amount, float temperatureHigh, float temperatureLow, float time) {
        float ratio, sum = 0;
        if (advise > amount)
            ratio = amount / advise;
        else
            ratio = advise / amount;
        sum += AMOUNTSCORE * ratio;
        //Log.i("amount " ,String.valueOf(sum));
        ratio = 0;
        if (temperatureHigh > TEMPREATUREHIGH)
            ratio += (temperatureHigh - TEMPREATUREHIGH) / TEMPREATUREHIGH;
        if (temperatureLow < TEMPREATURELOW)
            ratio += (TEMPREATURELOW - temperatureLow) / TEMPREATURELOW;
        ratio = ratio > 1 ? 1 : ratio;
        sum += TEMPERATURESCORE * (1 - ratio);
        //Log.i("amount tempreature " ,String.valueOf(sum));
        if (STANDARTIME > time)
            ratio = time / STANDARTIME;
        else
            ratio = STANDARTIME / time;
        sum += TIMESCORE * ratio;
        //Log.i("amount tempreature time" ,String.valueOf(sum));
        return sum;
    }

}
