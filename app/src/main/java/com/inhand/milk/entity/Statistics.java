package com.inhand.milk.entity;

import android.content.Context;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.inhand.milk.App;
import com.inhand.milk.dao.StatisticsDao;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Statistics
 * Desc: 统计实体
 * Date: 2015/6/2
 * Time: 23:42
 * Created by: Wooxxx
 */
@AVClassName(Statistics.STATISTICS_CLASS)
public class Statistics extends Base {
    public static final String STATISTICS_CLASS = "Milk_Statistics";
    public static final String OVERTIME_NUM_KEY = "overtime_num";
    public static final String HIGH_TEMPERATURE_NUM_KEY = "high_temperature_num";
    public static final String LOW_TEMPERATURE_NUM_KEY = "low_temperature_num";
    public static final String BABY_KEY = "baby";
    public static final String VERSION_KEY = "version";

    public Statistics(){

    }

    /**
     * 获得饮奶超时次数
     * @return 饮奶超时次数
     */
    public int getOvertimeNum(){
        return this.getInt(OVERTIME_NUM_KEY);
    }

    public void setOvertimeNum(int overtimeNum){
        this.put(OVERTIME_NUM_KEY,overtimeNum);
    }

    /**
     * 获得饮奶温度过高次数
     * @return 饮奶温度过高次数
     */
    public int getHighTemperatureNum(){
        return this.getInt(HIGH_TEMPERATURE_NUM_KEY);
    }

    public void setHighTemperatureNum(int highTemperatureNum){
        this.put(HIGH_TEMPERATURE_NUM_KEY,highTemperatureNum);
    }

    /**
     * 获得饮奶温度过低次数
     * @return 饮奶温度过低次数
     */
    public int getLowTemperatureNum(){
        return this.getInt(LOW_TEMPERATURE_NUM_KEY);
    }

    public void setLowTemperatureNum(int lowTemperatureNum){
        this.put(LOW_TEMPERATURE_NUM_KEY,lowTemperatureNum);
    }

    public String getVersion(){
        return this.getString(VERSION_KEY);
    }

    public void setVersion(String version){
        this.put(VERSION_KEY,version);
    }

    public Baby getBaby(){
        try {
            return this.getAVObject(BABY_KEY,Baby.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 设置统计信息对应的宝宝
     * @param baby 宝宝
     */
    public void setBaby(Baby baby) {
        try {
            this.put(BABY_KEY, AVObject.createWithoutData(Baby.class, baby.getObjectId()));
        } catch (AVException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存统计信息至云端
     * @param callback 存储回调接口
     * @param ctx 上下文环境
     */
    public void saveInCloud(Context ctx,final SaveCallback callback){
        if( this.getBaby()==null){
            this.setBaby(App.getCurrentBaby());
        }
        this.saveInBackground(callback);
    }

    /**
     * 保存统计信息至本地
     * @param ctx 上下文环境
     * @param callback 存储回调接口
     */
    public void saveInDB(final Context ctx,final DBSavingCallback callback){
        if( this.getBaby()==null){
            this.setBaby(App.getCurrentBaby());
        }
        //更新版本表示
        SimpleDateFormat sdf = new SimpleDateFormat(VERSION_FORMAT);
        String version = sdf.format(new Date());
        this.setVersion(version);
        DBSavingTask task = new DBSavingTask(ctx, callback) {
            @Override
            protected Object doInBackground(Object[] params) {
                //更新本地数据库
                StatisticsDao statisticsDao = new StatisticsDao(ctx);
                statisticsDao.updateOrSaveInDB(Statistics.this);
                return null;
            }
        };
        task.execute();
    }

}
