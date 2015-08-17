package com.inhand.milk.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inhand.milk.App;
import com.inhand.milk.BroadcastReceiver.AlarmReceiver;
import com.inhand.milk.BroadcastReceiver.AlarmSeting;
import com.inhand.milk.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2015/7/27.
 */
public class AlarmActivity extends Activity {
    public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;
    private ScreenBroadcastReceiver mScreenReceiver;
    private boolean isMilk;
    private int width, height, leftMargin, upHeight, downHeight;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Window win = getWindow();
        win.setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED);
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        setContentView(R.layout.alarm_notification);

        isMilk = getIntent().getBooleanExtra(AlarmSeting.MilkKey, false);
        initViews();
        startScreenBroadcastReceiver();
        start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_HOME) {
            return true;
        } else
            return super.onKeyDown(keyCode, event);
    }


    private void initViews() {
        width = App.getWindowWidth(this) * 6 / 8;
        height = (int) (width / 2f);
        leftMargin = width / 15;
        upHeight = height * 5 / 7;
        downHeight = height * 2 / 7;
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.alarm_total_container);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) linearLayout.getLayoutParams();
        lp.width = width;
        lp.height = height;
        initTimeTextView();
        initIcon();
        initDownIcon();
        initDocTextView();
        initButton();
    }

    private void initTimeTextView() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String time = dateFormat.format(date);
        TextView textView = (TextView) findViewById(R.id.alarm_time_textview);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, upHeight / 2);
        textView.setText(time);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) textView.getLayoutParams();
        lp.leftMargin = leftMargin;
    }

    private void initIcon() {
        ImageView imageView = (ImageView) findViewById(R.id.alarm_milk_food_icon);
        if (isMilk)
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.alarm_milk_icon));
        else
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.alarm_food_icon));

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
        lp.height = upHeight * 3 / 4;
        lp.width = lp.height;
        lp.rightMargin = leftMargin;
    }

    private void initDownIcon() {
        ImageView imageView = (ImageView) findViewById(R.id.alarm_icon_imageview);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
        lp.height = downHeight * 3 / 4;
        lp.width = lp.height;
        lp.leftMargin = leftMargin;
    }

    private void initDocTextView() {
        TextView textView = (TextView) findViewById(R.id.alarm_doc_textview);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (downHeight / 2.5f));
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) textView.getLayoutParams();
        lp.leftMargin = leftMargin / 4;
        if (isMilk)
            textView.setText(getResources().getString(R.string.alarm_eating_milk));
        else
            textView.setText(getResources().getString(R.string.alarm_eating_food));
    }

    private void initButton() {
        TextView imageView = (TextView) findViewById(R.id.alarm_image_button);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
        lp.height = (int) (downHeight * 0.7f);
        lp.width = (int) (lp.height * 2f);
        lp.rightMargin = leftMargin;
        imageView.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (lp.height * 0.6f));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void start() {
        AlarmReceiver.playSound(getApplicationContext());
        AlarmReceiver.vibrator(getApplicationContext());
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("alarmR", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("alarmR", "onStop");
        AlarmReceiver.stopSound();
        AlarmReceiver.stopVibrator();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("alarmR", "onRestart");
        AlarmReceiver.playSound(this.getApplicationContext());
        AlarmReceiver.vibrator(this.getApplicationContext());
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i("alarmR", "onnewintent");
        initViews();
        start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AlarmReceiver.stopSound();
        AlarmReceiver.stopVibrator();
        stopScrenBroadcastReceiver();
    }

    private void startScreenBroadcastReceiver() {
        mScreenReceiver = new ScreenBroadcastReceiver();
        if (mScreenReceiver == null)
            return;
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        this.registerReceiver(mScreenReceiver, filter);
    }

    private void stopScrenBroadcastReceiver() {
        if (mScreenReceiver == null)
            return;
        this.unregisterReceiver(mScreenReceiver);
    }

    private class ScreenBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {

            } else if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
                Log.i("alarmr", "screen_off");
                finish();
            } else if (Intent.ACTION_USER_PRESENT.equals(intent.getAction())) {

            }
        }
    }
}
