package com.inhand.milk.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.inhand.milk.DataHelper.EatingPlanHelper;

/**
 * Created by Administrator on 2015/7/27.
 */
public class StartingUpReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmSeting alarmSeting = new AlarmSeting(context);
        EatingPlanHelper eatingPlanHelper = new EatingPlanHelper();
        alarmSeting.start(eatingPlanHelper.getPlanTime(), eatingPlanHelper.getIsMilk());
        Log.i("AlarmReceiver", "ffffffffffffffffffffffffffff");
    }
}
