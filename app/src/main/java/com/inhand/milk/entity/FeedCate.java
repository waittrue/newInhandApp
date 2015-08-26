package com.inhand.milk.entity;

import android.content.Context;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVException;
import com.inhand.milk.App;
import com.inhand.milk.dao.FeedItemDao;
import com.inhand.milk.utils.ACache;
import com.inhand.milk.utils.LocalSaveTask;

import java.util.List;

/**
 * Created by Administrator on 2015/8/25.
 * 作者：大力
 * 时间：2015/8/25
 * 描述：
 */
@AVClassName(FeedCate.CLASS_NAME)
public class FeedCate extends Base {
    public static final String CLASS_NAME = "Milk_FeedPlanCate";
    public static final String FEED_PLAN_KEY = "feedPlan"; // 所属喂养计划
    public static final String FORAGE_KEY = "forAge";
    public static final String CACHE_KEY = "feed_cate";
    public static final String NEXTAGE_KEY = "nextAge";

    public void setFeedPlan(FeedPlan feedPlan) {
        add(FEED_PLAN_KEY, feedPlan);
    }

    public FeedPlan getFeedPlan() {
        return getAVObject(FEED_PLAN_KEY);
    }

    public String getForAge() {
        return getString(FORAGE_KEY);
    }

    public void setForAge(String age) {
        put(FORAGE_KEY, age);
    }

    public void saveInCache() {
        ACache aCache = ACache.get(App.getAppContext());
        aCache.put(CACHE_KEY, this.toString());
    }

    public void setNextFeedCate(FeedCate feedCate) {
        put(NEXTAGE_KEY, feedCate);
    }

    /**
     * 这里返回的feedcate是没有内容的。
     *
     * @return
     */
    private FeedCate getNextFeedCateObject() {
        try {
            FeedCate result = getAVObject(NEXTAGE_KEY, FeedCate.class);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 同步的抓取下一个feedcate
     *
     * @return
     * @throws AVException
     */
    public FeedCate getNextFeedCate() throws AVException {
        FeedCate feedCate = getNextFeedCateObject();
        if (feedCate == null)
            return null;
        try {
            feedCate.fetch();
            return feedCate;
        } catch (AVException e) {
            throw e;
        }
    }

    /**
     * 异步地缓存喂养计划条目列表到本地
     *
     * @param ctx      上下文环境
     * @param items    待缓存喂养计划条目列表
     * @param callback 回调接口
     */
    public void cacheItems(
            final Context ctx
            , final List<FeedItem> items
            , final LocalSaveTask.LocalSaveCallback<FeedItem> callback) {
        FeedItemDao ptd = new FeedItemDao();
        ptd.cache(ctx, items, callback);
    }

    /**
     * 同步地缓存喂养计划条目列表到本地
     *
     * @param ctx   上下文环境
     * @param items 待缓存喂养计划条目说明列表
     */
    public void cacheItems(Context ctx, List<FeedItem> items) {
        FeedItemDao ptd = new FeedItemDao();
        ptd.cache(ctx, items);
    }

    /**
     * 是否需要换喂养计划
     *
     * @param monthAge 宝宝的月龄
     * @return 是否需要跟新
     */
    public boolean needChange(int monthAge) {
        String forage = getForAge();
        String[] ages = forage.split("-");
        int min = Integer.parseInt(ages[0]);
        int max = Integer.parseInt(ages[1]);
        if (monthAge > max)
            return true;
        return false;
    }

    public boolean isForage(int monthAge) {
        String forage = getForAge();
        String[] ages = forage.split("-");
        int min = Integer.parseInt(ages[0]);
        int max = Integer.parseInt(ages[1]);
        if (monthAge <= max && monthAge >= min)
            return true;
        return false;
    }

    /**
     * 同步的，返回下一个时间段的feedcate，并储存缓存
     *
     * @return
     */
    public FeedCate changeToNext() throws AVException {
        try {
            FeedCate feedCate = getNextFeedCate();
            if (feedCate == null)
                return null;
            feedCate.saveInCache();
            return feedCate;
        } catch (AVException e) {
            throw e;
        }
    }
}
