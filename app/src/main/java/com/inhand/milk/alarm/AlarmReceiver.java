package com.inhand.milk.alarm;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.util.Log;

import com.inhand.milk.App;
import com.inhand.milk.activity.AlarmActivity;
import com.inhand.milk.activity.AlarmFloatActivity;

/**
 * Created by Administrator on 2015/7/27.
 */
public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver";
    private static MediaPlayer mMediaPlayer = null;
    private static Vibrator vibrator = null;
    public static void playSound(Context context) {
        if (App.getAlarmOpen() == false || App.getAlarmSound() == false)
            return;
        stopSound();
        Uri notification = RingtoneManager.getActualDefaultRingtoneUri(context,
                RingtoneManager.TYPE_RINGTONE);
        mMediaPlayer = MediaPlayer.create(context, notification);
        mMediaPlayer.setLooping(true);//设置循环
        mMediaPlayer.start();
    }

    public static void stopSound() {
        if (mMediaPlayer == null) {
            Log.i("alarme", "mmediaplaye === null");
            return;
        }
        mMediaPlayer.stop();
        mMediaPlayer.release();
        mMediaPlayer = null;
        Log.i("alarme", "mmediaplaye != null");
    }

    public static void vibrator(Context context) {

        if (App.getAlarmOpen() == false || App.getAlarmShock() == false)
            return;
        stopVibrator();
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {1000, 2000, 1000, 3000}; // 停止 开启 停止 开启
        vibrator.vibrate(pattern, 2);           //重复两次上面的pattern 如果只想震动一次，index设为-1
    }

    public static void stopVibrator() {
        if (vibrator == null)
            return;
        vibrator.cancel();
        vibrator = null;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (App.getAlarmOpen() == false)
            return;
        boolean ismilk = intent.getBooleanExtra(AlarmSeting.MilkKey, true);
        Intent alarmIntent;
        KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        if (km.inKeyguardRestrictedInputMode()) {
            alarmIntent = new Intent(context, AlarmActivity.class);
        } else {
            alarmIntent = new Intent(context, AlarmFloatActivity.class);
        }
        alarmIntent.putExtra(AlarmSeting.MilkKey, ismilk);
        alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(alarmIntent);
    }

}
