package com.inhand.milk.entity;

import com.avos.avoscloud.AVClassName;

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
    public static final String OVERTIME_NUM_KEY = "overtimeNum"; // 超时次数
    public static final String HIGH_TEMPERATURE_NUM_KEY = "highTemperatureNum"; // 高温次数
    public static final String LOW_TEMPERATURE_NUM_KEY = "lowTemperatureNum"; // 低温次数

    public static final String CACHE_KEY = "statistics";

    public Statistics() {

    }

    /**
     * 获得饮奶超时次数
     *
     * @return 饮奶超时次数
     */
    public int getOvertimeNum() {
        return this.getInt(OVERTIME_NUM_KEY);
    }

    /**
     * 设置超时次数
     *
     * @param overtimeNum 超时次数
     */
    public void setOvertimeNum(int overtimeNum) {
        this.put(OVERTIME_NUM_KEY, overtimeNum);
    }

    /**
     * 获得饮奶温度过高次数
     *
     * @return 饮奶温度过高次数
     */
    public int getHighTemperatureNum() {
        return this.getInt(HIGH_TEMPERATURE_NUM_KEY);
    }

    /**
     * 设置饮奶温度过高次数
     *
     * @param highTemperatureNum 饮奶温度过高次数
     */
    public void setHighTemperatureNum(int highTemperatureNum) {
        this.put(HIGH_TEMPERATURE_NUM_KEY, highTemperatureNum);
    }

    /**
     * 获得饮奶温度过低次数
     *
     * @return 饮奶温度过低次数
     */
    public int getLowTemperatureNum() {
        return this.getInt(LOW_TEMPERATURE_NUM_KEY);
    }

    /**
     * 设施饮奶温度过低次数
     *
     * @param lowTemperatureNum 饮奶温度过低次数
     */
    public void setLowTemperatureNum(int lowTemperatureNum) {
        this.put(LOW_TEMPERATURE_NUM_KEY, lowTemperatureNum);
    }

}
