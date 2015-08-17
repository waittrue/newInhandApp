package com.inhand.milk.fragment.Eating;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import com.inhand.milk.App;
import com.inhand.milk.R;

import java.util.Date;

/**
 * Created by Administrator on 2015/7/26.
 */
public class EatingNotification {
    private NotificationManager notificationManager;

    EatingNotification() {
        notificationManager = (NotificationManager) App.getAppContext().getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void start(int hour, int minute) {
        Date date = new Date();
        int mHour, mMinute;
        mHour = date.getHours();
        mMinute = date.getMinutes();
        int diff;
        diff = (hour - mHour) * 60 + (minute - mMinute);
        diff = diff * 60;
        Log.i("aaaaa", String.valueOf(diff));
        if (diff < 0)
            return;

        Notification notification = new Notification();
        notification.when = System.currentTimeMillis() + diff * 1000;
        notification.icon = R.drawable.ic_launcher;
        notification.defaults = Notification.DEFAULT_ALL;
        notification.tickerText = "zhege shi shenme ";
        notification.setLatestEventInfo(App.getAppContext(), "setlatest", "aaaaaaaaaaaaa", null);
        notificationManager.notify(1, notification);
    }

    public void cancleAll() {
        notificationManager.cancelAll();
    }
}
