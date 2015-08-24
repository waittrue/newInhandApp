package com.inhand.milk;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVUser;
import com.inhand.milk.STANDAR.Standar;
import com.inhand.milk.entity.Baby;
import com.inhand.milk.entity.User;
import com.inhand.milk.helper.LeanCloudHelper;
import com.inhand.milk.utils.ACache;

/**
 * App
 * Desc: 应用环境初始化
 * Team: InHand
 * User: Wooxxx
 * Date: 2015-03-05
 * Time: 10:37
 */
public class App extends Application {
    public static final String BABY_CACHE_KEY = "current_baby";
    public static Baby currentBaby = null;
    public static Typeface Typeface_arial;
    private static Context context = null;

    public static User getCurrentUser() {
        return AVUser.cast(AVUser.getCurrentUser(), User.class);
    }

    /**
     * 获得当前宝宝
     *
     * @return 当前宝宝
     */
    public static Baby getCurrentBaby() {
        return currentBaby;
    }

    /**
     * 判断用户是否登陆
     *
     * @return 登陆与否
     */
    public static boolean logged() {
        if (AVUser.getCurrentUser() == null)
            return false;
        return true;
    }

    public static Context getAppContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化LeanCloud
        LeanCloudHelper.initLeanCloud(this);
        initCurrentBaby();
        context = getApplicationContext();
        Typeface_arial = Typeface.createFromAsset(context.getAssets(), "ttf/arial.ttf");

    }


    /*获取状态栏高度*/
    public static int getStatusHeight(Activity activity) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = activity.getResources().getDimensionPixelSize(i5);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }
    //分工不分--这部分我添加的

    /*
    *获取屏幕宽度
     */
    public static int getWindowWidth(Activity activity) {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;  // 屏幕宽度（像素）

    }

    public static int getWindowWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        return wm.getDefaultDisplay().getWidth();
    }

    /*
  *获取屏幕高度
   */
    public static int getWindowHeight(Activity activity) {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.heightPixels;  // 屏幕宽度（像素）

    }

    /**
     * 获得屏幕的高度
     *
     * @param context 上下文
     * @return int高度
     */
    public static int getWindowHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        return wm.getDefaultDisplay().getHeight();
    }

    /**
     * 登出方法,会清除保存的宝宝信息
     */
    public static void logOut() {
        ACache aCache = ACache.get(getAppContext());
        // 清除所有缓存
        aCache.clear();
        AVUser.logOut();
    }

    public void initCurrentBaby() {
        if (currentBaby == null) {
            ACache aCache = ACache.get(this);
            String json = aCache.getAsString(Baby.CACHE_KEY);
            if (json == null)
                return;
            Log.d("baby currentBaby", json);
            currentBaby = JSON.parseObject(json, Baby.class);
            if (currentBaby == null) {
                Log.d("baby currentBaby", "null");
            }
            Log.d("baby currentBaby", Standar.DATE_FORMAT.format(currentBaby.getCreatedAt()));
        }
    }

    /**
     * 是否闹钟开启
     *
     * @return
     */
    public static boolean getAlarmOpen() {
        SharedPreferences shp = PreferenceManager.getDefaultSharedPreferences(context);
        return shp.getBoolean(App.getAppContext().getResources().getString(R.string.setting_alarm_open_key), true);
    }

    /**
     * 是否闹钟声音开启
     *
     * @return
     */
    public static boolean getAlarmSound() {
        SharedPreferences shp = PreferenceManager.getDefaultSharedPreferences(context);
        return shp.getBoolean(App.getAppContext().getResources().getString(R.string.setting_alarm_sound_key), true);
    }

    /**
     * 是否闹钟震动开启
     *
     * @return
     */
    public static boolean getAlarmShock() {
        SharedPreferences shp = PreferenceManager.getDefaultSharedPreferences(context);
        return shp.getBoolean(App.getAppContext().getResources().getString(R.string.setting_alarm_shock_key), true);
    }
}
