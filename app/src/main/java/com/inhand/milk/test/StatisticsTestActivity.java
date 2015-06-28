package com.inhand.milk.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.inhand.milk.R;
import com.inhand.milk.entity.Base;
import com.inhand.milk.entity.Statistics;
import com.inhand.milk.helper.SyncHelper;

/**
 * StatisticsTestActivity
 * Desc: 统计信息测试
 * Date: 2015/6/3
 * Time: 8:10
 * Created by: Wooxxx
 */
public class StatisticsTestActivity extends Activity {
    private EditText highTemperatureNumEditor;
    private EditText lowTemperatureNumEditor;
    private EditText overtimeNumEditor;
    private Button saveBtn;
    private Button syncBtn;
    private Statistics statistics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_test_statistics);
        highTemperatureNumEditor=(EditText)findViewById(R.id.high_temperature_num_test);
        lowTemperatureNumEditor=(EditText)findViewById(R.id.low_temperature_num_test);
        overtimeNumEditor=(EditText)findViewById(R.id.over_time_num_test);
        saveBtn=(Button)findViewById(R.id.save_statistic_test_bth);
        syncBtn=(Button)findViewById(R.id.sync_statistic_test_btn);
        setListeners();

    }

    private void setListeners() {
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statistics = new Statistics();
                int highTemperatureNum = Integer.valueOf(highTemperatureNumEditor.getText().toString());
                int lowTemperatureNum = Integer.valueOf(lowTemperatureNumEditor.getText().toString());
                int overTimeNum= Integer.valueOf(overtimeNumEditor.getText().toString());
                statistics.setHighTemperatureNum(highTemperatureNum);
                statistics.setLowTemperatureNum(lowTemperatureNum);
                statistics.setOvertimeNum(overTimeNum);
                statistics.saveInDB(StatisticsTestActivity.this, new Base.DBSavingCallback() {
                    @Override
                    public void done() {
                        Toast.makeText(StatisticsTestActivity.this, "存储成功", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        //同步按钮
        syncBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SyncHelper.syncCloud(StatisticsTestActivity.this, new SyncHelper.SyncCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e != null)
                            e.getMessage();
                        else
                            Toast.makeText(StatisticsTestActivity.this, "同步成功", Toast.LENGTH_LONG)
                                    .show();
                    }
                });
            }
        });
    }
}
