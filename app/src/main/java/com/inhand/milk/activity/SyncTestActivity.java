package com.inhand.milk.activity;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.FindCallback;
import com.inhand.milk.App;
import com.inhand.milk.R;
import com.inhand.milk.entity.Base;
import com.inhand.milk.entity.OneDay;
import com.inhand.milk.entity.Record;
import com.inhand.milk.helper.SyncHelper;
import com.inhand.milk.utils.ViewHolder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * SyncTestActivity
 * Desc: 测试同步的Activity
 * Team: InHand
 * User: Wooxxx
 * Date: 2015-03-05
 * Time: 09:44
 */
public class SyncTestActivity extends BaseActivity {
    private Button saveBtn;
    private Button syncBtn;
    private EditText beginTimeEditor;
    private EditText endTimeEditor;
    private EditText beginTemperEditor;
    private EditText endTemperEditor;
    private EditText volumeEditor;
    private EditText scoreEditor;
    private ListView listView;
    private OneDay today;
    private List<OneDay> oneDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_test);
        saveBtn = (Button) findViewById(R.id.save_btn);
        syncBtn = (Button) findViewById(R.id.sync_btn);
        beginTimeEditor = (EditText) findViewById(R.id.begin_time_edit);
        endTimeEditor = (EditText) findViewById(R.id.end_time_edit);
        endTemperEditor = (EditText) findViewById(R.id.end_temperature_edit);
        beginTemperEditor = (EditText) findViewById(R.id.begintemperature_edit);
        volumeEditor = (EditText) findViewById(R.id.volume_edit);
        scoreEditor = (EditText) findViewById(R.id.score_edit);
        scoreEditor = (EditText) findViewById(R.id.score_edit);
        listView = (ListView) findViewById(R.id.list);


        setListeners();
        //initList();
    }

    private void initList() {
        App.getCurrentBaby().getOnedays(SyncTestActivity.this, 0, new FindCallback<OneDay>() {
            @Override
            public void done(List<OneDay> list, AVException e) {
                if (e == null) {
                    oneDays = list;
                    List<Record> records = oneDays.get(0).getRecords();
                    System.out.println("Size:" + records.size());
                    RecordAdapter adapter = new RecordAdapter(
                            SyncTestActivity.this,
                            R.layout.item_sync_test,
                            records);
                    listView.setAdapter(adapter);
                }
            }
        });
    }

    private void setListeners() {
        // 保存按钮
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                today = new OneDay();
                today.setDate(new Date());
/*                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                cal.add(Calendar.DAY_OF_YEAR, -1);
                Date preDate =cal.getTime();
                today.setDate(preDate);*/
                //单击存储，存储一条饮奶记录
                String beginTime = null, endTime = null;
                beginTime = beginTimeEditor.getText().toString();
                endTime = endTimeEditor.getText().toString();
                float beginTemperature = Float.valueOf(beginTemperEditor.getText()
                        .toString());
                float endTemperature = Float.valueOf(endTemperEditor.getText()
                        .toString());
                int volume = Integer.valueOf(volumeEditor.getText()
                        .toString());
                int score = Integer.valueOf(scoreEditor.getText().toString());
                int sumVolume = 0;
                List<Record> records = new ArrayList<Record>();
                for (int i = 0; i < 1; i++) {
                    Record record = new Record();
                    record.setBeginTime(beginTime);
                    record.setBeginTemperature(beginTemperature + i * 2);
                    record.setEndTemperature(endTemperature + i * 2);
                    record.setVolume(volume);
                    sumVolume += record.getVolume();
                    records.add(record);
                }
                today.setVolume(sumVolume);
                today.setRecords(records);
                Record record=today.getRecords().get(0);
                Log.d("recordJson",today.getJSONArray(OneDay.RECORDS_KEY).toString());
                Log.d("record_time",record.getBeginTime());
                today.setScore(score);
                Log.d("Score is :", String.valueOf(score));
                //将today存入数据库
                today.saveInDB(SyncTestActivity.this, new Base.DBSavingCallback() {
                    @Override
                    public void done() {
                        Toast.makeText(SyncTestActivity.this, "OK", Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
        });

        //同步按钮
        syncBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SyncHelper.syncCloud(SyncTestActivity.this, new SyncHelper.SyncCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e != null)
                            e.getMessage();
                        else
                            Toast.makeText(SyncTestActivity.this, "同步成功", Toast.LENGTH_LONG)
                                    .show();
                    }
                });
//                today.save(SyncTestActivity.this,new SaveCallback() {
//                    @Override
//                    public void done(AVException e) {
//                        if( e==null) {
//                            Toast.makeText(SyncTestActivity.this,"OK",Toast.LENGTH_LONG)
//                                    .show();
//                        }else{
//                            e.printStackTrace();
//                        }
//                    }
//                });
            }
        });

        //开始喝奶时间选择
        beginTimeEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                new TimePickerDialog(
                        SyncTestActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                beginTimeEditor.setText(hourOfDay + ":" + minute);
                            }
                        },
                        calendar.get(Calendar.HOUR),
                        calendar.get(Calendar.MINUTE),
                        true
                ).show();
            }
        });
        //结束喝奶时间选择
        endTimeEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                new TimePickerDialog(
                        SyncTestActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                endTimeEditor.setText(hourOfDay + ":" + minute);
                            }
                        },
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true
                ).show();
            }
        });
    }

    /**
     * 显示饮水记录的Adapter
     */
    public class RecordAdapter extends BaseAdapter {
        private int resId;
        private List<Record> records;
        private Context context;

        public RecordAdapter(Context context, int resource, List<Record> records) {
            this.resId = resource;
            this.records = records;
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Record record = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(context)
                        .inflate(resId, parent, false);
            }
            TextView beginTimeTxt = ViewHolder.get(convertView, R.id.begin_time_text);
            TextView endTimeTxt = ViewHolder.get(convertView, R.id.end_time_text);
            TextView minTmpTxt = ViewHolder.get(convertView, R.id.end_temperature_text);
            TextView maxTmpTxt = ViewHolder.get(convertView, R.id.begin_temperature_text);
            TextView volumeTxt = ViewHolder.get(convertView, R.id.volume_text);
            String beginTime = record.getBeginTime();
            beginTimeTxt.setText(beginTime);
            minTmpTxt.setText(String.valueOf(record.getEndTemperature()));
            maxTmpTxt.setText(String.valueOf(record.getBeginTemperature()));
            volumeTxt.setText(String.valueOf(record.getVolume()));
            return convertView;
        }

        @Override
        public Record getItem(int position) {
            return records.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getCount() {
            return records.size();
        }

    }

}
