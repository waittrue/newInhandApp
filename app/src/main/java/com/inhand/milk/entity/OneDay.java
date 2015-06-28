package com.inhand.milk.entity;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.inhand.milk.App;
import com.inhand.milk.dao.OneDayDao;
import com.inhand.milk.helper.JSONHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * OneDay
 * Desc: 某日的统计
 * Team: InHand
 * User: Wooxxx
 * Date: 2015-03-05
 * Time: 09:17
 */
@AVClassName(OneDay.ONEDAY_CLASS)
public class OneDay extends Base {
    //远程及云端表名都为此
    public static final String ONEDAY_CLASS = "Milk_OneDay";
    public static final String VOLUME_KEY = "volume";
    public static final String RECORDS_KEY = "records";
    public static final String DATE_KEY = "date";
    public static final String SCORE_KEY = "score";
    public static final String VERSION_KEY = "version";
    public static final String BABY_KEY = "baby";

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public OneDay() {

    }

    public int getVolume() {
        return this.getInt(VOLUME_KEY);
    }

    public void setVolume(int volume) {
        this.put(VOLUME_KEY, volume);
    }

    public List<Record> getRecords() {
        JSONArray array = this.getJSONArray(RECORDS_KEY);
        String recordsJson= JSONHelper.getValidCloudJSON(array.toString());
        return JSON.parseArray(recordsJson, Record.class);
    }

    public void setRecords(List<Record> records) {
        JSONArray array = new JSONArray();
        for (Record record : records) {
            JSONObject obj = record.toJsonObj();
            array.put(obj);
        }
        this.put(RECORDS_KEY, array);
    }

    public String getDate() {
        return this.getString(DATE_KEY);
    }

    public void setDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        String dateStr = sdf.format(date);
        this.put(DATE_KEY, dateStr);
    }

    public int getScore() {
        return this.getInt(SCORE_KEY);
    }

    public void setScore(int score) {
        this.put(SCORE_KEY, score);
    }

    public void setBaby(Baby baby) {
        try {
            this.put(BABY_KEY, AVObject.createWithoutData(Baby.class, baby.getObjectId()));
        } catch (AVException e) {
            e.printStackTrace();
        }
    }

    public String getVersion() {
        return this.getString(VERSION_KEY);
    }

    public void setVersion(String version) {
        this.put(VERSION_KEY, version);
    }

    /**
     * 将数据存储至云端
     * 存储OneDay对象，若该“日”已存在，则为更新
     * @param ctx   上下文环境
     * @param saveCallback 回调接口
     */
    public void saveInCloud(Context ctx, final SaveCallback saveCallback) {
        final OneDay day = this;
        //更新版本表示
        String version = day.getVersion();
        if (version == null) {
            //如果先前没有手动设置过version
            SimpleDateFormat sdf = new SimpleDateFormat(VERSION_FORMAT);
            version = sdf.format(new Date());
        }
        day.setVersion(version);
        OneDayDao oneDayDao = new OneDayDao(ctx);
        oneDayDao.findOneDayFromCloud(day.getDate(), new FindCallback<OneDay>() {
            @Override
            public void done(List<OneDay> oneDays, AVException e) {
                //如果不存在，插入
                if (e != null) {
                    day.setBaby(App.getCurrentBaby());
                    day.saveInBackground(saveCallback);
                } else {
                    //否则更新
                    OneDay oneDay = oneDays.get(0);
                    oneDay.setRecords(day.getRecords());
                    oneDay.setVolume(day.getVolume());
                    oneDay.setScore(day.getScore());
                    oneDay.setVersion(day.getVersion());
                    oneDay.saveInBackground(saveCallback);
                }
            }
        });
    }

    /**
     * 将数据存至数据库
     *
     * @param ctx      上下文环境
     * @param callback 回调接口
     */
    public void saveInDB(final Context ctx,
                         final DBSavingCallback callback) {
        this.setBaby(App.getCurrentBaby());
        //更新版本表示
        String version = this.getVersion();
        if (version == null) {
            //如果先前没有手动设置过version
            SimpleDateFormat sdf = new SimpleDateFormat(VERSION_FORMAT);
            version = sdf.format(new Date());
        }
        this.setVersion(version);
        DBSavingTask task = new DBSavingTask(ctx, callback) {
            @Override
            protected Object doInBackground(Object[] params) {
                //先查询数据库中是否有此记录
                OneDayDao oneDayDao = new OneDayDao(ctx);
                oneDayDao.updateOrSaveInDB(OneDay.this);
                return null;
            }
        };
        task.execute();
    }

    @Override
    public String getTable() {
        return ONEDAY_CLASS;
    }
}
