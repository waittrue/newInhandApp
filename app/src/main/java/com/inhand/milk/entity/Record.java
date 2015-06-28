package com.inhand.milk.entity;


import com.alibaba.fastjson.JSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * Record
 * Desc: 某次的记录
 * Team: InHand
 * User: Wooxxx
 * Date: 2015-03-04
 * Time: 20:22
 */
public class Record implements Serializable{
    //奶量
    public static final String VOLUME_KEY = "volume";
    //温度(最低温度，最高温度)
    public static final String BEGIN_TEMPERATURE_KEY = "beginTemperature";
    public static final String END_TEMPERATURE_KEY = "endTemperature";
    //日期
    public static final String BEGIN_TIME_KEY = "beginTime";
    //饮奶时间
    public static final String DURATION_KEY = "duration";
    //建议奶量
    public static final String ADVICE_VOLUMN_KEY = "adviceVolumn";
    //喝奶分数
    public static final String SCORE_KEY = "score";
    //奶量
    private int volume;
    //温度(最低温度，最高温度)
    private float beginTemperature;
    private float endTemperature;
    //喝奶时间
    private String beginTime;
    //喝奶时长
    private int duration;
    //喝奶评分
    private int score;
    //建议奶量
    private int adviceVolumn;

    public Record() {

    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public float getEndTemperature() {
        return endTemperature;
    }

    public void setEndTemperature(float endTemperature) {
        this.endTemperature = endTemperature;
    }

    public float getBeginTemperature() {
        return beginTemperature;
    }

    public void setBeginTemperature(float beginTemperature) {
        this.beginTemperature = beginTemperature;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getAdviceVolumn() {
        return adviceVolumn;
    }

    public void setAdviceVolumn(int adviceVolumn) {
        this.adviceVolumn = adviceVolumn;
    }

    /**
     * 根据饮奶的开始时间判断两次饮奶记录是否为
     * 同一条
     *
     * @param record 比较对象
     * @return 是否相等
     */
    public boolean equals(Record record) {
        if (record.getBeginTime()
                .equals(this.getBeginTime()))
            return true;
        return false;
    }

    /**
     * 将Record对象转换为JSON
     */
    public JSONObject toJsonObj(){
        JSONObject obj = new JSONObject();
        try {
            obj.put(Record.VOLUME_KEY, this.getVolume());
            obj.put(Record.BEGIN_TEMPERATURE_KEY, this.getBeginTemperature());
            obj.put(Record.END_TEMPERATURE_KEY, this.getEndTemperature());
            obj.put(Record.BEGIN_TIME_KEY, this.getBeginTime());
            obj.put(Record.SCORE_KEY,this.getScore());
            obj.put(Record.DURATION_KEY,this.getDuration());
            obj.put(Record.ADVICE_VOLUMN_KEY,this.getAdviceVolumn());
            return obj;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
