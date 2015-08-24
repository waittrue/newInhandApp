package com.inhand.milk.utils;

import com.inhand.milk.App;
import com.inhand.milk.STANDAR.Standar;
import com.inhand.milk.entity.Record;

import java.security.InvalidParameterException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Calculator
 * Desc:封装常用计算
 * Team: InHand
 * User:Wooxxx
 * Date: 2015-03-23
 * Time: 19:04
 */
public class Calculator {

    /**
     * 计算饮奶记录的总奶量
     *
     * @param records 传入饮奶记录
     * @return 总饮奶量
     */
    public static int calcVolume(List<Record> records) {
        int sum = 0;
        for (Record record : records) {
            sum = sum + record.getVolume();
        }
        return sum;
    }

    /**
     * 计算饮奶记录的评分
     *
     * @param records 传入的饮奶记录
     * @return 评分
     */
    public static int calcScore(List<Record> records) {
        return 0;
    }


    /**
     * 计算宝宝的月龄
     *
     * @param date 开始算的时间
     * @return 宝宝的月龄, 当日期低于宝宝的生日时候，抛出异常
     */
    public static int getBabyMonthAge(Date date) throws InvalidParameterException {
        Calendar birthCalendar = Calendar.getInstance();
        String birth = App.getCurrentBaby().getBirthday();
        //  Log.d("baby birth", birth);
        Date birthDay = null;
        try {
            birthDay = Standar.DATE_FORMAT.parse(birth);
        } catch (ParseException e) {
            e.printStackTrace();
            InvalidParameterException ee = new InvalidParameterException("宝宝生日解析出问题");
            throw ee;
        }
        return getMonths(birthDay, date);
    }

    private static int getMonths(Date from, Date to) {
        Calendar birthCalendar = Calendar.getInstance();
        birthCalendar.setTime(from);
        Calendar todayCalendr = Calendar.getInstance();
        todayCalendr.setTime(to);
        int year1 = birthCalendar.get(Calendar.YEAR);
        int year2 = todayCalendr.get(Calendar.YEAR);
        int month1 = birthCalendar.get(Calendar.MONTH);
        int month2 = todayCalendr.get(Calendar.MONTH);
        int day1 = birthCalendar.get(Calendar.DAY_OF_MONTH);
        int day2 = todayCalendr.get(Calendar.DAY_OF_MONTH);
        int month = 0;
        if (day2 < day1) {
            month2 = month2 - 1;
            if (month2 < 0) {
                month2 = 11;
                year2 = year2--;
            }
        } else {
            month = 1;
        }

        if (month2 >= month1)
            month = month2 - month1;
        else if (month2 < month1) {
            month = 12 + month2 - month1;
            year2--;
        }
        if (year2 < year1) {
            InvalidParameterException e = new InvalidParameterException("计算宝宝月龄");
            throw e;
        }
        month = 12 * (year2 - year1) + month;
        return month;
    }

    public static int getBabyCreatedMonths() {
        return getBabyMonthAge(App.getCurrentBaby().getCreatedAt());
    }
    /**
     * 计算宝宝的日龄
     * 注意这个方法有个bug，当日期在生日之前时候，也有一些情况可以返回正常值，所以可以先用getbabydayAge来做检查。
     *
     * @param date 宝宝开始的时间
     * @return 返回宝宝的日龄。
     */
    public static int getBabyDayAge(Date date) {
        Calendar birthCalendar = Calendar.getInstance();
        String birth = App.getCurrentBaby().getBirthday();
        Date birthDay = null;
        try {
            birthDay = Standar.DATE_FORMAT.parse(birth);
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
        birthCalendar.setTime(birthDay);
        Calendar todayCalendr = Calendar.getInstance();
        todayCalendr.setTime(date);


        int day1 = birthCalendar.get(Calendar.DAY_OF_MONTH);
        int day2 = todayCalendr.get(Calendar.DAY_OF_MONTH);
        int days = 0;
        days = day2 - day1;
        if (days < 0) {
            todayCalendr.add(Calendar.MONTH, -1);
            days = todayCalendr.getActualMaximum(Calendar.DATE) + days + 1;
        } else
            days = days + 1;
        return days;
    }
}
