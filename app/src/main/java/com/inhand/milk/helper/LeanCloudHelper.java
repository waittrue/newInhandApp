package com.inhand.milk.helper;

import android.content.Context;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.inhand.milk.entity.Baby;
import com.inhand.milk.entity.BabyInfo;
import com.inhand.milk.entity.Device;
import com.inhand.milk.entity.FeedItem;
import com.inhand.milk.entity.FeedPlan;
import com.inhand.milk.entity.OneDay;
import com.inhand.milk.entity.Powder;
import com.inhand.milk.entity.PowderBrand;
import com.inhand.milk.entity.PowderDetail;
import com.inhand.milk.entity.PowderSerie;
import com.inhand.milk.entity.PowderTip;
import com.inhand.milk.entity.Statistics;
import com.inhand.milk.entity.Supplement;

/**
 * LeanCloudHelper
 * Desc:LeanCloud帮助类
 * Team: InHand
 * User:Wooxxx
 * Date: 2015-03-03
 * Time: 12:49
 */
public class LeanCloudHelper {
    /**
     * 初始化LeanCloud配置
     *
     * @param ctx 上下文环境
     */
    public static void initLeanCloud(Context ctx) {
        // 注册子类
        registerSubclass();
        AVOSCloud.initialize(ctx, "5dwer2xd20x59apeyfuq2bv5scn14078k0bzwqk6lnho6417", "65z49j48dcr57bplz9ecqlp3b8n8eq30sbkhpdawilg0z5q7");
        AVOSCloud.setDebugLogEnabled(true);
    }

    private static void registerSubclass() {
        AVObject.registerSubclass(OneDay.class);
        AVObject.registerSubclass(Baby.class);
        AVObject.registerSubclass(BabyInfo.class);
        AVObject.registerSubclass(Device.class);
        AVObject.registerSubclass(Powder.class);
        AVObject.registerSubclass(PowderTip.class);
        AVObject.registerSubclass(FeedPlan.class);
        AVObject.registerSubclass(FeedItem.class);
        AVObject.registerSubclass(Supplement.class);
        AVObject.registerSubclass(Statistics.class);
        AVObject.registerSubclass(PowderBrand.class);
        AVObject.registerSubclass(PowderSerie.class);
        AVObject.registerSubclass(PowderDetail.class);
    }

    /**
     * 与leancloud数据同步
     */
    public static void sync() {

    }

}
