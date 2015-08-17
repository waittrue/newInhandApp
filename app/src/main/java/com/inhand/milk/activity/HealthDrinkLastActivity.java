package com.inhand.milk.activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.inhand.milk.App;
import com.inhand.milk.STANDAR.Standar;
import com.inhand.milk.dao.OneDayDao;
import com.inhand.milk.entity.OneDay;
import com.inhand.milk.entity.Record;
import com.inhand.milk.fragment.health.last_drink.LastDrink;

import java.util.List;

/**
 * Created by Administrator on 2015/6/3.
 */
public class HealthDrinkLastActivity extends SubActivity {
    private LastDrink fragment;

    public HealthDrinkLastActivity() {
        super();
        this.fragment = new LastDrink();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Intent intent = this.getIntent();
        Record record;
        record = (Record) intent.getSerializableExtra(Standar.LASTDRINK_KEY);
        if (record != null) {
            fragment.setRecord(record);
            Log.i("healthdringk", String.valueOf(record.getScore()));
            return;
        }
        if (record == null) {
            List<OneDay> oneDays = new OneDayDao().findFromDB(App.getAppContext(), 1);
            if (oneDays == null || oneDays.size() == 0) {
                fragment.setRecord(null);
            } else {
                List<Record> records = oneDays.get(0).getRecords();
                if (records == null || records.size() == 0) {
                    fragment.setRecord(null);
                } else {
                    record = records.get(records.size() - 1);
                    fragment.setRecord(record);
                }
            }
        }
        fragment.setRecord(record);

    }

    @Override
    protected Fragment initFragment() {
        // TODO Auto-generated method stub
        return fragment;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fragment.start();
            }
        }, 200);
    }
}
