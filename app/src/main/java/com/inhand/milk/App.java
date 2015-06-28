package com.inhand.milk;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVUser;
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
    private static Context context = null;

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化LeanCloud
        LeanCloudHelper.initLeanCloud(this);
        initCurrentBaby();
        context = getApplicationContext();
    }


    public static User getCurrentUser() {
        return AVUser.cast(AVUser.getCurrentUser(), User.class);
    }

    public void initCurrentBaby() {
        if (currentBaby == null) {
            ACache aCache = ACache.get(this);
            String json = aCache.getAsString(BABY_CACHE_KEY);
            currentBaby = JSON.parseObject(json, Baby.class);
        }
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

    /*
    *获取屏幕宽度
     */
    public static int getWindowWidth(Activity activity) {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;  // 屏幕宽度（像素）

    }

    /*
  *获取屏幕高度
   */
    public static int getWindowHeight(Activity activity) {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.heightPixels;  // 屏幕宽度（像素）

    }
}
