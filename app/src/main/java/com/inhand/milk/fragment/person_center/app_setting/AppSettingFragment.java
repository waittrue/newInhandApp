package com.inhand.milk.fragment.person_center.app_setting;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.inhand.milk.R;
import com.inhand.milk.alarm.AlarmSeting;
import com.inhand.milk.entity.BabyFeedItem;
import com.inhand.milk.helper.FeedPlanHelper;

import java.text.ParseException;
import java.util.List;

/**
 * Created by Administrator on 2015/8/24.
 * 作者：大力
 * 时间：2015/8/24
 * 描述：整个App一些基本的设置，如闹钟提醒
 */
public class AppSettingFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.preference);
        CheckBoxPreference checkBoxPreference = (CheckBoxPreference) findPreference(
                getResources().getString(R.string.setting_alarm_open_key));
        checkBoxPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                AlarmSeting alarmSeting = new AlarmSeting(getActivity());
                if ((boolean) newValue == true) {
                    List<int[]> planTime = null;
                    boolean[] isMilk = null;
                    FeedPlanHelper feedPlanHelper = null;
                    try {
                        feedPlanHelper = new FeedPlanHelper();
                    } catch (ParseException e) {
                        return true;
                    }
                    List<BabyFeedItem> babyFeedItems = feedPlanHelper.getBabyFeedItemsFromAcache();
                    if (babyFeedItems == null)
                        return true;
                    babyFeedItems = feedPlanHelper.sortBabyfeedItems(babyFeedItems);
                    planTime = feedPlanHelper.getTime(babyFeedItems);
                    isMilk = feedPlanHelper.getType(babyFeedItems);
                    alarmSeting.start(planTime, isMilk);
                } else {
                    alarmSeting.cancleAlarm();
                }
                return true;
            }
        });
    }
}
