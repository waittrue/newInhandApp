package com.inhand.milk.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.inhand.milk.R;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends Activity{
    private final  float timeJump = 2f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                jumpActivity();
            }
        };
        timer.schedule(task, (long) (1000 * timeJump));
    }
    private void jumpActivity(){
        Intent intent = new Intent();
        if ( isfirst() ){
            intent.setClass(WelcomeActivity.this,HealthPromptActivity.class );
            save();
        }
        else {
            intent.setClass(WelcomeActivity.this, MainActivity.class);
        }
        startActivity(intent);
    }
    /**
     *
     * @return 第一次登陆返回真，否则假
     */
    private boolean isfirst(){
        return true;
    }
    /**
     * 存入值，表示已经是登陆过了
     */
    private void save(){

    }
}
