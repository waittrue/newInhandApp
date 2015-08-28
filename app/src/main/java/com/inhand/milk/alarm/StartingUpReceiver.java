package com.inhand.milk.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.inhand.milk.App;
import com.inhand.milk.entity.BabyFeedItem;
import com.inhand.milk.helper.FeedPlanHelper;

import java.util.List;

/**
 * Created by Administrator on 2015/7/27.
 */
public class StartingUpReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmSeting alarmSeting = new AlarmSeting(context);
        FeedPlanHelper feedPlanHelper = FeedPlanHelper.getInstance();
            List<BabyFeedItem> babyFeedItems = feedPlanHelper.getBabyFeedItemsFromAcache();
            if (babyFeedItems == null)
                return;
            babyFeedItems = feedPlanHelper.sortBabyfeedItems(babyFeedItems);
            if (App.getAlarmOpen() == true) {
                alarmSeting.start(feedPlanHelper.getTime(babyFeedItems),
                        feedPlanHelper.getType(babyFeedItems));
            } else {
                alarmSeting.cancleAlarm();
            }

    }
}
