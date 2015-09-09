package com.inhand.milk.fragment.milk_amount;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.inhand.milk.App;
import com.inhand.milk.R;
import com.inhand.milk.STANDAR.Standar;
import com.inhand.milk.activity.HealthDrinkLastActivity;
import com.inhand.milk.activity.MilkAmountCurveActivity;
import com.inhand.milk.entity.OneDay;
import com.inhand.milk.entity.Record;
import com.inhand.milk.fragment.TitleFragment;
import com.inhand.milk.fragment.bluetooth.Bluetooth;
import com.inhand.milk.fragment.bluetooth.UniversalBluetoothLE;
import com.inhand.milk.helper.RecordHelper;
import com.inhand.milk.ui.MultiLayerCircle;
import com.inhand.milk.ui.PinnerListView;
import com.inhand.milk.ui.PinnerListViewAdapter;
import com.inhand.milk.ui.ProgressBar;
import com.inhand.milk.ui.RingWithText;
import com.inhand.milk.utils.ViewHolder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/6/4.
 */
public class MilkAmountFragment extends TitleFragment {
    final static float TEMPREATUREHIGH = 40, TEMPREATURELOW = 37;
    private final static int timeRing = 200, dataLoadAmount = 7;
    private static final String HEAD_DATA = "data", HEAD_TOTALNUM = "totalNum";
    private static final String CONTETN_TIME = "onceTime", CONTENT_TP = "oncetemperate", CONTENT_AMOUNT = "onceAmount",
            CONTENT_COLOR = "color", CONTENT_SCORE = "score";
    private TextView adviseNum, drinkNum;
    private float drinkAmount, adviseAmount;
    private RingWithText ringWithText;
    private int[] multiLayerCircleColors, multiLayerCircleWeights;
    private List<ProgressBar> progressBarList;
    private PinnerListView headlistView;
    private PinnerListViewAdapter adpter;
    private Record lastRecord;
    private int warningHighColor, warningLowColor, normalColor, progressBgColor;
    private List<OneDay> oneDays;
    private RecordHelper recordHelper;

    public MilkAmountFragment() {
        recordHelper = RecordHelper.getInstance();
        recordHelper.registerObserver(mOberver);
        initOnedays();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_milk_amount, container, false);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MilkAmountCurveActivity.class);
                getActivity().startActivity(intent);
            }
        };
        setTitleView(getResources().getDrawable(R.drawable.header_curve_ico), listener);
        initVarables();
        initViews();
        return mView;
    }

    private void setTitleView(Drawable drawable, View.OnClickListener listener) {
        ImageView imageView = (ImageView) mView.findViewById(R.id.title_right_icon);
        imageView.setImageDrawable(drawable);
        imageView.setOnClickListener(listener);

        final ImageView leftIcon = (ImageView) mView.findViewById(R.id.title_left_icon);
        leftIcon.setImageDrawable(App.getAppContext().getResources().getDrawable(R.drawable.header_connect_false_icon));
        leftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v instanceof ImageView) {
                    ImageView imageView = (ImageView) v;
                    imageView.getDrawable().equals(App.getAppContext().getResources().getDrawable(R.drawable.header_connect_false_icon));
                    Toast.makeText(App.getAppContext(), "请靠近奶瓶，连接奶瓶", Toast.LENGTH_LONG).show();
                }
            }
        });
        UniversalBluetoothLE bluetoothLE = UniversalBluetoothLE.getInistance();
        if(bluetoothLE != null) {
            bluetoothLE.addBluetoothStateChanggedListener(new UniversalBluetoothLE.ConnectedChanggedListener() {
                @Override
                public void connectedChangged(boolean connect) {
                    if (connect) {
                        leftIcon.setImageDrawable(App.getAppContext().getResources().getDrawable(R.drawable.header_connect_true_icon));
                    } else {
                        leftIcon.setImageDrawable(App.getAppContext().getResources().getDrawable(R.drawable.header_connect_false_icon));
                    }
                }
            });
        }

    }

    private void initVarables() {
        warningHighColor = getResources().getColor(R.color.public_high_color);
        warningLowColor = getResources().getColor(R.color.public_low_color);
        normalColor = getResources().getColor(R.color.public_nor_color);
        progressBgColor = getResources().getColor(R.color.milk_amount_listview_item_progress_bar_background_color);
        multiLayerCircleColors = getResources().getIntArray(R.array.milk_amount_listview_list_item_circle_colors);
        multiLayerCircleWeights = getResources().getIntArray(R.array.milk_amount_listview_item_title_circle_weights);
        progressBarList = new ArrayList<>();

        drinkNum = (TextView) mView.findViewById(R.id.milk_amount_drink_num);
        adviseNum = (TextView) mView.findViewById(R.id.milk_amount_advise_num);

        headlistView = (PinnerListView) mView.findViewById(R.id.milk_amount_listview);
        headlistView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        headlistView.setDividerHeight(0);
        headlistView.setHead(R.layout.fragment_milk_amount_listview_head);
        headlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int len = oneDays.size();
                Record result = null;
                List<Record> records;
                position = position + 1;
                for (int i = 0; i < len; i++) {
                    records = oneDays.get(i).getRecords();
                    if (position > records.size()) {
                        position = position - records.size();
                        continue;
                    }
                    int size = records.size();
                    result = records.get(size - position);
                    break;
                }
                Intent intent = new Intent(getActivity(), HealthDrinkLastActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Standar.LASTDRINK_KEY, result);
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
            }
        });

        LinearLayout linearLayout = (LinearLayout) mView.findViewById(R.id.milk_amount_advise_ring_container);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(getResources().getDimensionPixelOffset(R.dimen.milk_amount_advise_ring_size_D),
                getResources().getDimensionPixelOffset(R.dimen.milk_amount_advise_ring_size_D));
        float r = getResources().getDimension(R.dimen.milk_amount_advise_ring_size_D) / 2;
        ringWithText = new RingWithText(getActivity().getApplicationContext(), r);
        ringWithText.setRingWidth(r / 30);
        linearLayout.addView(ringWithText, lp);
        ringWithText.setRingBgColor(getResources().getColor(R.color.public_darkin_littlelight_color));
        ringWithText.setRingColor(getResources().getColor(R.color.milk_amount_ring_color));
        ringWithText.setTextColor(getResources().getColor(R.color.milk_amount_ring_text_color));
        ringWithText.setTimeRing(timeRing);
    }

    private void initViews() {
        initListViews();
        drinkNum.setText(getResources().getString(R.string.milk_amount_drink_num_doc) + getOneDayDrinkAmount());
        adviseNum.setText(getResources().getString(R.string.milk_amount_advise_num_doc) + getOneDayAdviseAmount());
        ringWithText.setMaxSweepAngle(drinkAmount / adviseAmount * 360);

        final String[] strings = new String[2];
        strings[0] = getResources().getString(R.string.milk_amount_advise_ring_up_doc);
        strings[1] = String.valueOf(Standar.AMOUNT_FORMAT.format(drinkAmount / adviseAmount * 100)) + "%";
        float[] sizes = {getResources().getDimension(R.dimen.milk_amount_advise_ring_text_up_size),
                getResources().getDimension(R.dimen.milk_amount_advise_ring_text_down_size)};
        ringWithText.setTexts(strings, sizes);
        RingWithText.updateListener listener = new RingWithText.updateListener() {
            @Override
            public void updateSweepAngle(float sweepAngle) {
                strings[1] = String.valueOf(Standar.AMOUNT_FORMAT.format(sweepAngle / 360 * 100)) + "%";
            }
        };
        ringWithText.setListener(listener);
    }

    private void initAdpter() {
        if (adpter == null) {
            adpter = new PinnerListViewAdapter(this.getActivity());
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

                    linearLayout = ViewHolder.get(convertView, R.id.milk_amount_listview_item_divider);
                    if (adpter.hasHead(position + 1)) {
                        linearLayout.setVisibility(View.INVISIBLE);
                    } else
                        linearLayout.setVisibility(View.VISIBLE);
                    return convertView;
                }
            });
        }
    }

    private void initListViews() {
        initAdpter();
        getDataFromDB();
    }

    private void getDataFromDB() {
        adpter.clearData();
        initOnedays();
        if(oneDays.isEmpty())
            return;
        int len = oneDays.size();
        int addCount = 0;
        for (int i = 0; i < len; i++) {
            OneDay oneDay = oneDays.get(i);
            List<Record> temp = oneDay.getRecords();
            int recordSize = temp.size();
            for (int j = 0; j < recordSize; j++) {
                if (i == 0 && j == 0)
                    lastRecord = temp.get(recordSize - 1 - j);
                adpter.addMap(getHeadData(oneDay), getContentData(temp.get(recordSize - 1 - j)), addCount++);
            }
        }
        headlistView.setAdapter(adpter);
    }

    private void initOnedays() {
        oneDays = recordHelper.getOnedays(dataLoadAmount);
        if(oneDays != null)
            return;
        oneDays = recordHelper.getOnedaysBylimit(dataLoadAmount);
        if (oneDays == null)
            oneDays = new ArrayList<>();
    }

    private Map<String, Object> getHeadData(OneDay oneDay) {
        Map<String, Object> map = new HashMap<>();
        map.put(HEAD_DATA, getCalenderBefore(oneDay));
        map.put(HEAD_TOTALNUM, getResources().getString(R.string.milk_amount_drink_total_num_doc) +
                String.valueOf(getTotalDrinkNum(oneDay)) + "次");
        return map;
    }

    private Map<String, Object> getContentData(Record record) {
        Map<String, Object> map = new HashMap<>();
        int color;
        float score, amount;
        float[] mTemperature;
        amount = getOnceAmount(record);
        mTemperature = getOnceTemperature(record);
        map.put(CONTETN_TIME, record.getBeginTime());
        map.put(CONTENT_TP, Standar.TEMPERATURE_FORMAT.format(mTemperature[1]) + "°C");
        map.put(CONTENT_AMOUNT, Standar.AMOUNT_FORMAT.format(amount) + "ml");

        score = record.getScore();
        color = selectColor(mTemperature[0], mTemperature[1]);
        map.put(CONTENT_COLOR, color);
        map.put(CONTENT_SCORE, score);
        return map;
    }

    @Override
    public void refresh() {
        if (needRefresh()) {
            Log.i("milkAmountFragmnet", "true");
            getDataFromDB();
            drinkNum.setText(getResources().getString(R.string.milk_amount_drink_num_doc) + getOneDayDrinkAmount());
            adviseNum.setText(getResources().getString(R.string.milk_amount_advise_num_doc) + getOneDayAdviseAmount());
            ringWithText.setMaxSweepAngle(drinkAmount / adviseAmount * 360);
            startAnimator();
            clearNeedRefresh();
        }
    }

    private void startAnimator() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startProgressBar();
                ringWithText.startRing();
            }
        }, 100);

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

    private String getCalenderBefore(OneDay oneDay) {
        String temp = oneDay.getDate();
        Calendar calendar = Calendar.getInstance();
        if (temp.equals(Standar.DATE_FORMAT.format(calendar.getTime())))
            return "今天";
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        if (temp.equals(Standar.DATE_FORMAT.format(calendar.getTime())))
            return "昨天";
        return temp;

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
        OneDay oneDay = recordHelper.getOneday(new Date());
        if (oneDay == null)
            return "无数据";
        drinkAmount = recordHelper.getOneday(new Date()).getVolume();
        // Log.i("milkamount",String.valueOf(drinkAmount));
        return Standar.AMOUNT_FORMAT.format(drinkAmount) + "ml";
    }

    private String getOneDayAdviseAmount() {
        OneDay oneDay = recordHelper.getOneday(new Date());
        if (oneDay == null) {
            return "无数据";
        }
        List<Record> records = oneDay.getRecords();
        int len = records.size();
        adviseAmount = 0;
        for (int i = 0; i < len; i++) {
            adviseAmount += records.get(i).getAdviceVolume();
            // Log.i("milkamount",String.valueOf(adviseAmount));
        }
        //Log.i("milkamount",String.valueOf(adviseAmount));
        return Standar.AMOUNT_FORMAT.format(adviseAmount) + "ml";
    }

    private int getTotalDrinkNum(OneDay oneDay) {
        return oneDay.getRecords().size();
    }

    private float[] getOnceTemperature(Record record) {
        float[] tempreture = new float[2];
        float temp;
        tempreture[0] = record.getBeginTemperature();
        tempreture[1] = record.getEndTemperature();
        if (tempreture[0] < tempreture[1]) {
            temp = tempreture[0];
            tempreture[0] = tempreture[1];
            tempreture[1] = temp;
        }

        return tempreture;
    }

    private float getOnceAmount(Record record) {
        return record.getVolume();
    }
}
