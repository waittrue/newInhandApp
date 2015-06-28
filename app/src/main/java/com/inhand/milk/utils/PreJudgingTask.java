package com.inhand.milk.utils;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.inhand.milk.App;
import com.inhand.milk.activity.FirstLanunchActivity;
import com.inhand.milk.activity.MainActivity;
import com.inhand.milk.entity.User;

/**
 * Created by Administrator on 2015/4/16.
 */
public class PreJudgingTask extends AsyncTask{

    private FragmentActivity activity;
    public PreJudgingTask(FragmentActivity activity){
        this.activity=activity;
    }
    @Override
    protected Object doInBackground(Object[] params) {
            int errorCode = App.getCurrentUser().hasBaby(activity);
            Log.d("errorCode",errorCode+"");
            switch (errorCode) {
                case User.HAS_BABY:
                    Log.d("User has baby", "has");
                    activity.startActivity(new Intent(activity,MainActivity.class));
                    activity.finish();
                    break;
                case User.NO_BABY:
                    Log.d("User has baby", "none");
                    activity.startActivity(new Intent(activity,FirstLanunchActivity.class));
                    activity.finish();
                    break;
                case User.NETWORK_ERROR:
                    Log.d("User has baby", "network error");
                    break;
            }
            return null;
        }
}

