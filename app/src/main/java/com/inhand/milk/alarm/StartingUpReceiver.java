package com.inhand.milk.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.inhand.milk.App;
import com.inhand.milk.helper.EatingPlanHelper;

/**
 * Created by Administrator on 2015/7/27.
 */
public class StartingUpReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmSeting alarmSeting = new AlarmSeting(context);
        EatingPlanHelper eatingPlanHelper = new EatingPlanHelper();
        if (App.getAlarmOpen() == true) {
            alarmSeting.start(eatingPlanHelper.getPlanTime(), eatingPlanHelper.getIsMilk());
        } else {
            alarmSeting.cancleAlarm();
        }
    }
}
