package com.inhand.milk.fragment.health.last_drink;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.inhand.milk.R;
import com.inhand.milk.STANDAR.Standar;
import com.inhand.milk.entity.Record;
import com.inhand.milk.fragment.TitleFragment;
import com.inhand.milk.utils.Circle;
import com.inhand.milk.utils.RingWithText;
import com.inhand.milk.utils.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/6/3.
 */
public class LastDrink extends TitleFragment {

    private static final String TAG = "LAST DRINK";
    private static final int AnimationDuration = 500, AnimationSpace = 50;
    private static final int DrinkScore = 0, DrinkDuration = 1, DrinkBeginTp = 2, DrinkEndTp = 3, DrinkAmount = 4, ItemUpColor = 5, ItemDownColor = 6,
            ItemLeftColor = 7, ItemRightColor = 8, ItemNum = 9, ItemUnit = 10, ItemUpText = 11, ItempDownText = 12;
    private ListView listView;
    private List<Map<Integer, Object>> mData;
    private LayoutInflater mInflater;
    private int warningColor, normalColor;
    private List<RingWithText> ringWithTexts = new ArrayList<>();
    private Record record;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.health_last_drink, container, false);
        setTitleview(getResources().getString(R.string.health_last_drink_title), 2);
        warningColor = getResources().getColor(R.color.health_drink_last_warning_color);
        normalColor = getResources().getColor(R.color.health_drink_last_normal_color);
        initViews(mView);
        return mView;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    public void start() {
        int len = ringWithTexts.size();
        for (int i = 0; i < len; i++) {
            RingWithText ringWithText = ringWithTexts.get(i);
            Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.right_in);
            animation.setDuration(AnimationDuration + i * AnimationSpace);
            animation.setFillAfter(true);
            ringWithText.setAlpha(255);
            ringWithText.startAnimation(animation);
        }
    }

    private void initViews(View v) {
        listView = (ListView) v.findViewById(R.id.health_last_drink_listview);
        initData();
        mInflater = LayoutInflater.from(getActivity().getApplicationContext());
        BaseAdapter baseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return mData.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.health_last_drink_listview_item, null);
                    RingWithText ringWithText = (RingWithText) convertView.findViewById(R.id.health_drink_last_item_ringWithText);
                    ringWithText.setTextsSpace(new float[]{getResources().getDimension(R.dimen.health_last_drink_item_ring_texts_space)});
                    ringWithText.setAlpha(0);
                    ringWithTexts.add(ringWithText);
                }
                if (position < 0 || position > mData.size() - 1)
                    return null;
                Map<Integer, Object> data = mData.get(position);
                Circle circle = ViewHolder.get(convertView, R.id.health_drink_last_circle);
                circle.setColor((int) data.get(ItemLeftColor));

                TextView textView = ViewHolder.get(convertView, R.id.health_drink_last_up_doc_text);
                textView.setText((String) data.get(ItemUpText));
                textView.setTextColor((int) data.get(ItemUpColor));

                textView = ViewHolder.get(convertView, R.id.health_drink_last_down_text_doc);
                textView.setText((String) data.get(ItempDownText));

                RingWithText ringWithText = ViewHolder.get(convertView, R.id.health_drink_last_item_ringWithText);
                ringWithText.setRingBgColor((int) data.get(ItemRightColor));
                ringWithText.setTextColor(getResources().getColor(R.color.color_white));
                // ringWithText.setRingWidth(ringWithText.getR());

                String[] strs = new String[2];
                strs[0] = (String) data.get(ItemNum);
                strs[1] = (String) data.get(ItemUnit);
                ringWithText.setTexts(strs);
                return convertView;
            }
        };
        listView.setAdapter(baseAdapter);
    }

    //这个地方按照 1、持续时间 2.饮奶奶量，3开始温度，饮奶温度
    private void initData() {

        mData = new ArrayList<>();
        mData.add(DrinkScore, getScore(record));
        mData.add(DrinkDuration, getDurationFromRecord(record));
        mData.add(DrinkBeginTp, getBeginTp(record));
        mData.add(DrinkEndTp, getEndTp(record));
        mData.add(DrinkAmount, getAmount(record));

    }

    private Map<Integer, Object> getScore(Record record) {
        Map<Integer, Object> data = new HashMap<>();
        int score = record.getScore();
        data.put(ItemNum, String.valueOf(score));
        data.put(ItemUnit, "分");
        data.put(ItemUpText, getResources().getString(R.string.health_last_drink_score));

        if (score < Standar.drinkMinScore) {
            data.put(ItempDownText, getResources().getString(R.string.health_last_drink_score_less_doc));
            data.put(ItemRightColor, warningColor);
            data.put(ItemLeftColor, warningColor);
            data.put(ItemUpColor, warningColor);
        } else {
            data.put(ItempDownText, getResources().getString(R.string.health_last_drink_score_suit_doc));
            data.put(ItemRightColor, normalColor);
            data.put(ItemLeftColor, normalColor);
            data.put(ItemUpColor, normalColor);
        }
        return data;
    }

    private Map<Integer, Object> getDurationFromRecord(Record record) {
        Map<Integer, Object> data = new HashMap<>();
        int duration = record.getDuration();
        data.put(ItemNum, String.valueOf(duration));
        data.put(ItemUnit, "分钟");
        data.put(ItemUpText, getResources().getString(R.string.health_last_drink_duration));

        if (duration > Standar.drinkMaxDuration) {
            data.put(ItempDownText, getResources().getString(R.string.health_last_drink_duration_more_doc));
            data.put(ItemRightColor, warningColor);
            data.put(ItemLeftColor, warningColor);
            data.put(ItemUpColor, warningColor);
        } else if (duration < Standar.drinkMinDuration) {
            data.put(ItempDownText, getResources().getString(R.string.health_last_drink_duration_less_doc));
            data.put(ItemRightColor, warningColor);
            data.put(ItemLeftColor, warningColor);
            data.put(ItemUpColor, warningColor);
        } else {
            data.put(ItempDownText, getResources().getString(R.string.health_last_drink_duration_suit_doc));
            data.put(ItemRightColor, normalColor);
            data.put(ItemLeftColor, normalColor);
            data.put(ItemUpColor, normalColor);
        }
        return data;
    }

    private Map<Integer, Object> getAmount(Record record) {
        Map<Integer, Object> data = new HashMap<>();
        int volume = record.getVolume();
        data.put(ItemNum, String.valueOf(volume));
        data.put(ItemUnit, "毫升");

        data.put(ItemUpText, getResources().getString(R.string.health_last_drink_amount));
        if (volume > Standar.drinkMaxAmount) {
            data.put(ItempDownText, getResources().getString(R.string.health_last_drink_amount_more_doc));
            data.put(ItemRightColor, warningColor);
            data.put(ItemLeftColor, warningColor);
            data.put(ItemUpColor, warningColor);
        } else if (volume < Standar.drinkMinAmount) {
            data.put(ItempDownText, getResources().getString(R.string.health_last_drink_amount_less_doc));
            data.put(ItemRightColor, warningColor);
            data.put(ItemLeftColor, warningColor);
            data.put(ItemUpColor, warningColor);
        } else {
            data.put(ItempDownText, getResources().getString(R.string.health_last_drink_amount_suit_doc));
            data.put(ItemRightColor, normalColor);
            data.put(ItemLeftColor, normalColor);
            data.put(ItemUpColor, normalColor);
        }
        return data;
    }

    private Map<Integer, Object> getBeginTp(Record record) {
        Map<Integer, Object> data = new HashMap<>();
        float beginTp = record.getBeginTemperature();
        data.put(ItemNum, Standar.TeamperatureFormat.format(beginTp));
        data.put(ItemUnit, "°C");
        data.put(ItemUpText, getResources().getString(R.string.health_last_drink_begin_temperature));
        if (beginTp > Standar.drinkBeginMaxTp) {
            data.put(ItempDownText, getResources().getString(R.string.health_last_drink_begin_temperature_more_doc));
            data.put(ItemRightColor, warningColor);
            data.put(ItemLeftColor, warningColor);
            data.put(ItemUpColor, warningColor);
        } else if (beginTp < Standar.drinkBeginMinTp) {
            data.put(ItempDownText, getResources().getString(R.string.health_last_drink_begin_temperature_less_doc));
            data.put(ItemRightColor, warningColor);
            data.put(ItemLeftColor, warningColor);
            data.put(ItemUpColor, warningColor);
        } else {
            data.put(ItempDownText, getResources().getString(R.string.health_last_drink_begin_temperature_suit_doc));
            data.put(ItemRightColor, normalColor);
            data.put(ItemLeftColor, normalColor);
            data.put(ItemUpColor, normalColor);
        }
        return data;
    }

    private Map<Integer, Object> getEndTp(Record record) {
        Map<Integer, Object> data = new HashMap<>();
        float endTp = record.getEndTemperature();
        data.put(ItemNum, Standar.TeamperatureFormat.format(endTp));
        data.put(ItemUnit, "°C");
        data.put(ItemUpText, getResources().getString(R.string.health_last_drink_end_temperature));
        if (endTp > Standar.drinkEndMaxTp) {
            data.put(ItempDownText, getResources().getString(R.string.health_last_drink_end_temperature_more_doc));
            data.put(ItemRightColor, warningColor);
            data.put(ItemLeftColor, warningColor);
            data.put(ItemUpColor, warningColor);
        } else if (endTp < Standar.drinkEndMinTp) {
            data.put(ItempDownText, getResources().getString(R.string.health_last_drink_end_temperature_less_doc));
            data.put(ItemRightColor, warningColor);
            data.put(ItemLeftColor, warningColor);
            data.put(ItemUpColor, warningColor);
        } else {
            data.put(ItempDownText, getResources().getString(R.string.health_last_drink_end_temperature_suit_doc));
            data.put(ItemRightColor, normalColor);
            data.put(ItemLeftColor, normalColor);
            data.put(ItemUpColor, normalColor);
        }
        return data;
    }


}
