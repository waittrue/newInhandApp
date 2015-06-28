package com.inhand.milk.fragment.health.last_drink;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.inhand.milk.R;
import com.inhand.milk.STANDAR.Standar;
import com.inhand.milk.dao.OneDayDao;
import com.inhand.milk.entity.OneDay;
import com.inhand.milk.entity.Record;
import com.inhand.milk.fragment.TitleFragment;
import com.inhand.milk.utils.Circle;
import com.inhand.milk.utils.RingWithText;
import com.inhand.milk.utils.ViewHolder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/6/3.
 */
public class LastDrink extends TitleFragment {

    private ListView listView;
    private List<Map<Integer,Object>> mData;
    private LayoutInflater mInflater;
    private SimpleDateFormat simpleDateFormat;
    private int warningColor,normalColor;
    private static final String TAG ="LAST DRINK";
    private static final int DrinkDuration = 0,DrinkBeginTp=1,DrinkEndTp=2,DrinkAmount=3,ItemUpColor =5,ItemDownColor=6,
                ItemLeftColor =7,ItemRightColor=8,ItemNum=9,ItemUnit=10,ItemUpText=11,ItempDownText=12;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       mView = inflater.inflate(R.layout.health_last_drink,container,false);
       simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:MM");
       setTitleview(getResources().getString(R.string.health_last_drink_title),2);
       warningColor = getResources().getColor(R.color.health_drink_last_warning_color);
       normalColor = getResources().getColor(R.color.health_drink_last_normal_color);
       initViews(mView);
       return mView;
    }
    private void initViews(View v){
        listView = (ListView)v.findViewById(R.id.health_last_drink_listview);
        initData();
        mInflater  =  LayoutInflater.from(getActivity().getApplicationContext());
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
                if (convertView == null){
                    convertView = mInflater.inflate(R.layout.health_last_drink_listview_item,null);
                }
                Map<Integer,Object> data  = mData.get(position);
                Circle circle = ViewHolder.get(convertView, R.id.health_drink_last_circle);
                circle.setColor((int)data.get(ItemLeftColor));

                TextView textView = ViewHolder.get(convertView,R.id.health_drink_last_up_doc_text);
                textView.setText((String)data.get(ItemUpText));
                textView.setTextColor((int)data.get(ItemUpColor));

                textView = ViewHolder.get(convertView,R.id.health_drink_last_down_text_doc);
                textView.setText((String)data.get(ItempDownText));

                RingWithText ringWithText = ViewHolder.get(convertView,R.id.health_drink_last_item_ringWithText);
                ringWithText.setRingColor((int)data.get(ItemRightColor));
                String[] strs = new String[2];
                strs[0] = (String) data.get(ItemNum);
                strs[1] = (String) data.get(ItemUnit);
                ringWithText.setTexts(strs);
                return null;
            }
        };
        listView.setAdapter(baseAdapter);
    }

    //这个地方按照 1、持续时间 2.饮奶奶量，3开始温度，饮奶温度
    private void initData(){
        mData = new ArrayList<>();
        OneDayDao oneDayDao = new OneDayDao(this.getActivity().getApplication());
        OneDay oneDay= oneDayDao.findAllFromDB(1).get(0);
        Record record = oneDay.getRecords().get(0);
        if (record == null){
            Log.i(TAG,"record null");
            return ;
        }
        mData.add(DrinkDuration,getDurationFromRecord(record));
        mData.add(DrinkAmount,getAmount(record));
        mData.add(DrinkBeginTp,getBeginTp(record));
        mData.add(DrinkEndTp,getBeginTp(record));
    }
    private Map<Integer,Object> getDurationFromRecord(Record record)  {
        Map<Integer,Object> data  = new HashMap<>();
        long diff;
        int duration;
        Date endtime,startTime;
        try{ endtime = simpleDateFormat.parse(record.getBeginTime());
             startTime = simpleDateFormat.parse(record.getBeginTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        diff = endtime.getTime() - startTime.getTime();
        duration = (int)diff/(1000*60);
        data.put(ItemNum,duration);
        data.put(ItemUnit, "分钟");

        data.put(ItemLeftColor,0x213244);
        data.put(ItemUpText,getResources().getString(R.string.health_last_drink_duration));

        if (duration > Standar.drinkMaxDuration) {
            data.put(ItempDownText,getResources().getString(R.string.health_last_drink_duration_more_doc) );
            data.put(ItemRightColor,warningColor);
            data.put(ItemUpColor,warningColor);
        }
        else if (duration < Standar.drinkMinDuration){
            data.put(ItempDownText,getResources().getString(R.string.health_last_drink_duration_less_doc) );
            data.put(ItemRightColor,warningColor);
            data.put(ItemUpColor,warningColor);
        }
        else {
            data.put(ItempDownText,getResources().getString(R.string.health_last_drink_duration_suit_doc) );
            data.put(ItemRightColor,normalColor);
            data.put(ItemUpColor,normalColor);
        }
        return data;
    }
    private Map<Integer,Object> getAmount(Record record){
        Map<Integer,Object> data = new HashMap<>();
        int volume = record.getVolume();
        data.put(ItemNum,volume);
        data.put(ItemUnit,"毫升");
        data.put(ItemLeftColor,0xee3424);
        data.put(ItemUpText,getResources().getString(R.string.health_last_drink_amount));
        if ( volume> Standar.drinkMaxAmount) {
            data.put(ItempDownText,getResources().getString(R.string.health_last_drink_amount_more_doc) );
            data.put(ItemRightColor,warningColor);
            data.put(ItemUpColor,warningColor);
        }
        else if (volume < Standar.drinkMinAmount){
            data.put(ItempDownText,getResources().getString(R.string.health_last_drink_amount_less_doc) );
            data.put(ItemRightColor,warningColor);
            data.put(ItemUpColor,warningColor);
        }
        else {
            data.put(ItempDownText,getResources().getString(R.string.health_last_drink_amount_suit_doc) );
            data.put(ItemRightColor,normalColor);
            data.put(ItemUpColor,normalColor);
        }
        return data;
    }
    private Map<Integer,Object> getBeginTp(Record record){
        Map<Integer,Object> data = new HashMap<>();
        float beginTp = (float)record.getBeginTemperature();
        data.put(ItemNum,beginTp);
        data.put(ItemUnit,"°C");
        data.put(ItemLeftColor,0xee3424);
        data.put(ItemUpText,getResources().getString(R.string.health_last_drink_begin_temperature));
        if ( beginTp> Standar.drinkBeginMaxTp) {
            data.put(ItempDownText,getResources().getString(R.string.health_last_drink_begin_temperature_more_doc) );
            data.put(ItemRightColor,warningColor);
            data.put(ItemUpColor,warningColor);
        }
        else if (beginTp < Standar.drinkBeginMinTp){
            data.put(ItempDownText,getResources().getString(R.string.health_last_drink_begin_temperature_less_doc) );
            data.put(ItemRightColor,warningColor);
            data.put(ItemUpColor,warningColor);
        }
        else {
            data.put(ItempDownText,getResources().getString(R.string.health_last_drink_begin_temperature_suit_doc) );
            data.put(ItemRightColor,normalColor);
            data.put(ItemUpColor,normalColor);
        }
        return data;
    }
    private Map<Integer,Object> getEndTp(Record record){
        Map<Integer,Object> data = new HashMap<>();
        float endTp = (float)record.getEndTemperature();
        data.put(ItemNum, endTp);
        data.put(ItemUnit,"°C");
        data.put(ItemLeftColor,0xee3424);
        data.put(ItemUpText,getResources().getString(R.string.health_last_drink_end_temperature));
        if ( endTp> Standar.drinkEndMaxTp) {
            data.put(ItempDownText,getResources().getString(R.string.health_last_drink_end_temperature_more_doc) );
            data.put(ItemRightColor,warningColor);
            data.put(ItemUpColor,warningColor);
        }
        else if (endTp < Standar.drinkEndMinTp){
            data.put(ItempDownText,getResources().getString(R.string.health_last_drink_end_temperature_less_doc) );
            data.put(ItemRightColor,warningColor);
            data.put(ItemUpColor,warningColor);
        }
        else {
            data.put(ItempDownText,getResources().getString(R.string.health_last_drink_end_temperature_suit_doc) );
            data.put(ItemRightColor,normalColor);
            data.put(ItemUpColor,normalColor);
        }
        return data;
    }

}
