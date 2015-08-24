package com.inhand.milk.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/7/27.
 */
public class AlarmSeting {
    public static String MilkKey = "ismilk";
    private AlarmManager alarmManager;
    private Context activity;

    public AlarmSeting(Context context) {
        this.activity = context;
        alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);

    }

    public void start(List<int[]> time, boolean[] isMilk) {
        cancleAlarm();
        if (time == null || isMilk == null)
            return;
        int count = time.size();
        if (count > isMilk.length)
            count = isMilk.length;
        if (count <= 0)
            return;

        Date date = new Date();
        int mHour, mMinute;
        int[] mTime;
        boolean milk, over;
        mHour = date.getHours();
        mMinute = date.getMinutes();
        for (int i = 0; i < count; i++) {
            mTime = time.get(i);
            milk = isMilk[i];
            if (mTime[0] < mHour)
                over = true;
            else if (mTime[0] > mHour)
                over = false;
            else {
                if (mTime[1] < mMinute)
                    over = true;
                else if (mTime[1] > mMinute)
                    over = false;
                else
                    over = false;
            }
            setAlarm(mTime, i, milk, over);
        }

    }

    public void cancleAlarm() {
        for (int i = 0; i < 24 * 12; i++) {
            Intent intent = new Intent(activity, AlarmReceiver.class);
            PendingIntent sender = PendingIntent.getBroadcast(activity, i, intent, 0);
            alarmManager.cancel(sender);
        }
    }
    private void setAlarm(int[] time, int flag, boolean isMilk, boolean over) {
        Intent intent = new Intent(activity, AlarmReceiver.class);
        intent.putExtra(MilkKey, isMilk);
        PendingIntent sender = PendingIntent.getBroadcast(activity, flag, intent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, time[0]);
        calendar.set(Calendar.MINUTE, time[1]);
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Log.i("alarm", dateFormat.format(calendar.getTime()));
        if (over)
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + 1 * 24 * 60 * 60 * 1000, 1 * 24 * 60 * 60 * 1000, sender);
        else
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1 * 24 * 60 * 60 * 1000, sender);
    }
}
