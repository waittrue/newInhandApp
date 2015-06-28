package com.inhand.milk.helper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/4/19.
 */
public class DateHelper {
    public static List<Date> dateToWeek(Date date) {
        int b=DateHelper.dayForWeek(date);
        Date fdate;
        List<Date> list = new ArrayList<Date>();
        Long fTime = date.getTime() - b * 24 * 3600000;
        for (int a = 1; a <= 7; a++) {
            fdate = new Date();
            fdate.setTime(fTime + (a * 24 * 3600000));
            list.add(a-1, fdate);
        }
        return list;
    }
    public static List<Date> dateToMonth(Date date) {
        Date fstday=DateHelper.getFirstDay(date);
        int daynum=DateHelper.getDayOfMonth(date);
        Date fdate;
        List<Date> list = new ArrayList<Date>();
        Long fTime = fstday.getTime();
        for (int a = 0; a <daynum; a++) {
            fdate = new Date();
            fdate.setTime(fTime);
            list.add(a, fdate);
            fTime=fTime+24*3600000;
        }
        return list;
    }
    public static List<Date>getWeekDays(Date date){
        int week=7;
        Date fdate=date;
        List<Date> list = new ArrayList<Date>();
        Long fTime=fdate.getTime();
        for(int a=0;a<week;a++)
        {
            fdate=new Date();
            fdate.setTime(fTime);
            list.add(a,fdate);
            fTime=fTime-24*3600000;
        }
        Collections.reverse(list);
        return list;
    }
    public static List<Date>getMonthDays(Date date){
        int month=30;
        Date fdate=date;
        List<Date> list = new ArrayList<Date>();
        Long fTime=fdate.getTime();
        for(int a=0;a<month;a++)
        {
            fdate=new Date();
            fdate.setTime(fTime);
            list.add(a,fdate);
            fTime=fTime-24*3600000;
        }
        Collections.reverse(list);
        return list;
    }
    public static Date getLastDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, -1);
        Date lastday = cal.getTime();
        return lastday;
    }

    public static Date getFirstDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date fstday = cal.getTime();
        return fstday;
    }

    public static int dayForWeek(Date date)  {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayForWeek = c.get(Calendar.DAY_OF_WEEK)-1;
        if(c.get(Calendar.DAY_OF_WEEK) == 1){
            dayForWeek = 7;
        }else{
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return dayForWeek;
    }
    public static int getDayOfMonth(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int day=c.getActualMaximum(Calendar.DATE);
        return day;
    }

    public static List<Date> getPreDays(Date date,int preDay) {
        Date fdate=date;
        List<Date> list = new ArrayList<Date>();
        Long fTime=fdate.getTime();
        for(int a=0;a<preDay;a++)
        {
            fdate=new Date();
            fdate.setTime(fTime);
            list.add(a,fdate);
            fTime=fTime-24*3600000;
        }
        Collections.reverse(list);
        return list;
    }
}
