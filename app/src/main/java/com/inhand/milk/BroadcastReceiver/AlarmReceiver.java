package com.inhand.milk.BroadcastReceiver;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.inhand.milk.R;
import com.inhand.milk.activity.AlarmActivity;
import com.inhand.milk.activity.AlarmFloatActivity;

/**
 * Created by Administrator on 2015/7/27.
 */
public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver";
    private static MediaPlayer mMediaPlayer = null;
    private static Vibrator vibrator = null;
    private WindowManager mWindowManager;
    private View floatView;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("AlarmReceiver", "ffffffffffffffffffffffffffff");
        boolean ismilk  = intent.getBooleanExtra(AlarmSeting.MilkKey,true);
        Intent alarmIntent;
        KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        if (km.inKeyguardRestrictedInputMode()) {
            Log.i("AlarmReceiver", "iskeyguar");
             alarmIntent = new Intent(context, AlarmActivity.class);
        }
        else{
            alarmIntent = new Intent(context, AlarmFloatActivity.class);
        }
        alarmIntent.putExtra(AlarmSeting.MilkKey,ismilk);
        alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(alarmIntent);
    }
    private void initFloatWindow(Context context,boolean ismilk){
        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
        mWindowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        wmParams.format = PixelFormat.RGBA_8888;
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.CENTER;
        wmParams.x = 0;
        wmParams.y = 0;

        //设置悬浮窗口长宽数据
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        LayoutInflater inflater = LayoutInflater.from(context);
        //获取浮动窗口视图所在布局
        floatView = inflater.inflate(R.layout.alarm_notification, null);
        FloatView ff = new FloatView(context, floatView, ismilk, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFloatWindow();
            }
        });
        //添加mFloatLayout
        mWindowManager.addView(ff.getView(), wmParams);
    }
    private void removeFloatWindow(){
        if(mWindowManager == null)
            return;
        mWindowManager.removeView(floatView);
    }
    public static void playSound(Context context){
        stopSound();
        Uri notification = RingtoneManager.getActualDefaultRingtoneUri(context,
                RingtoneManager.TYPE_RINGTONE);
        mMediaPlayer = MediaPlayer.create(context, notification);
        mMediaPlayer.setLooping(true);//设置循环
        mMediaPlayer.start();
    }
    public static void stopSound(){
        if(mMediaPlayer == null) {
            Log.i("alarme","mmediaplaye === null");
            return;
        }
        mMediaPlayer.stop();
        mMediaPlayer.release();
        mMediaPlayer = null;
        Log.i("alarme","mmediaplaye != null");
    }
    public static void vibrator(Context context){
        stopVibrator();
        vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {1000, 2000, 1000, 3000}; // 停止 开启 停止 开启
        vibrator.vibrate(pattern, 2);           //重复两次上面的pattern 如果只想震动一次，index设为-1
    }
    public static void stopVibrator(){
       if(vibrator == null)
           return;
         vibrator.cancel();
         vibrator = null;
   }
}
