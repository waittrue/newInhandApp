package com.inhand.milk.fragment.weight;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.inhand.milk.App;
import com.inhand.milk.dao.BabyDao;
import com.inhand.milk.entity.Baby;
import com.inhand.milk.utils.ACache;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/20.
 */
public class WeightAcache {
    private static final String TAG = "weightAache";
    private static final String WeightKey = "weightkey", LastAddTime = "LastAddTime";
    private Weight currentWeight;
    private Date lastWeightTime;
    private Map<Integer, Map<Integer, Float>> monthToweights = new HashMap<>();

    public WeightAcache() {
        initData();
    }

    public void fresh() {
        ACache cache = ACache.get(App.getAppContext());
        List<Weight> list;
        list = initFromCloud();
        if (list == null)
            return;
        weights2Array(list);
        lastWeightTime = currentWeight.getCreatedAt();

        String save = JSON.toJSONString(list);
        cache.put(WeightKey, save);
        String t = JSON.toJSONString(lastWeightTime);
        Log.i(TAG, lastWeightTime.toString());
        cache.put(LastAddTime, t);
    }

    public void syncNoFresh() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                fresh();
            }
        });
        thread.start();

    }

    public Weight getCurrentWeight() {
        return currentWeight;
    }

    public Date getLastWeightTime() {
        return lastWeightTime;
    }

    public Map<Integer, Map<Integer, Float>> getMonthToweights() {
        return monthToweights;
    }

    private void initData() {
        ACache cache = ACache.get(App.getAppContext());
        String json = cache.getAsString(WeightKey);
        String time = cache.getAsString(LastAddTime);
        List<Weight> list;
        if (json == null || time == null) {
            fresh();
        } else {
            list = JSON.parseArray(json, Weight.class);
            if (list == null)
                return;
            lastWeightTime = JSON.parseObject(time, Date.class);
            weights2Array(list);
        }

    }

    private void weights2Array(List<Weight> list) {
        if (list == null)
            return;
        if (monthToweights == null)
            monthToweights = new HashMap<>();
        else
            monthToweights.clear();
        int month;
        int lastmonth = -1;
        Map<Integer, Float> weightValues = null;
        for (Weight weight : list) {
            month = WeightMonth.getbabyMonth(weight);
            if (lastmonth != month) {
                if (weightValues != null)
                    monthToweights.put(lastmonth, weightValues);
                weightValues = new HashMap<>();
                lastmonth = month;
                weightValues.put(WeightMonth.getbabyDay(weight), weight.getWeight());
            } else if (lastmonth == month) {
                weightValues.put(WeightMonth.getbabyDay(weight), weight.getWeight());
            }
            currentWeight = weight;
        }
        monthToweights.put(lastmonth, weightValues);
    }

    private List<Weight> initFromCloud() {
        List<Baby> babies = new BabyDao(App.getAppContext()).findBabiesByUser(App.getCurrentUser());
        if (babies == null) {
            Log.i(TAG, "babies == null");
        }
        Baby baby = babies.get(0);
        List<Weight> list = baby.getWeight();
        if (list == null) {
            Log.i(TAG, "error");
        }
        Log.i(TAG, String.valueOf(list.size()));
        return list;
    }
}
