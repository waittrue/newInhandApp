package com.inhand.milk.helper;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.inhand.milk.App;
import com.inhand.milk.STANDAR.Standar;
import com.inhand.milk.dao.BabyFeedItemDao;
import com.inhand.milk.dao.FeedCateDao;
import com.inhand.milk.dao.FeedItemDao;
import com.inhand.milk.dao.FeedPlanDao;
import com.inhand.milk.entity.Baby;
import com.inhand.milk.entity.BabyFeedItem;
import com.inhand.milk.entity.FeedCate;
import com.inhand.milk.entity.FeedItem;
import com.inhand.milk.entity.FeedPlan;
import com.inhand.milk.entity.PowderSerie;
import com.inhand.milk.utils.Calculator;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/8/26.
 * 作者：大力
 * 时间：2015/8/26
 * 描述：完成feedplan的一些逻辑业务需求
 */
public class FeedPlanHelper {
    private FeedPlanDao feedPlanDao = new FeedPlanDao();
    private FeedCateDao feedCateDao = new FeedCateDao();
    private FeedItemDao feedItemDao = new FeedItemDao();
    private FeedCate feedCate;
    private FeedPlan feedPlan;
    private FeedItem feedItem;
    private int monthAges;

    public FeedPlanHelper() throws ParseException {
        try {
            monthAges = getmonthAge();
        } catch (ParseException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 获取宝宝的月龄
     *
     * @return 返回月龄
     * @throws ParseException 解析错误
     */
    private int getmonthAge() throws ParseException {
        String birth = App.getCurrentBaby().getBirthday();
        Date date = null;
        try {
            date = Standar.DATE_FORMAT.parse(birth);
        } catch (ParseException ee) {
            throw ee;
        }
        return Calculator.getBabyMonthAge(date);
    }

    /**
     * 是否需要改变宝宝的喂养计划，我们通过宝宝喂养的分类来实现
     *
     * @return
     */
    public boolean needChange() {
        if (feedCate == null) {
            feedCate = feedCateDao.findFromeAcache();
            if (feedCate == null)
                return false;
        }
        return feedCate.needChange(monthAges);
    }

    public boolean hasAcache() {
        List<BabyFeedItem> babyFeedItems = new BabyFeedItemDao().getBabyItemFromAcache();
        if (babyFeedItems == null || babyFeedItems.isEmpty())
            return false;
        return true;
    }

    /**
     * 更换宝宝的喂养分类，并跟新宝宝的绑定
     *
     * @return 是否跟新成功
     */
    public boolean changeToNextFeedCate() {
        if (feedCate == null) {
            feedCate = feedCateDao.findFromeAcache();
            if (feedCate == null)
                return false;
        }
        try {
            //更改缓存
            feedCate = feedCate.changeToNext();
            Baby baby = App.getCurrentBaby();
            baby.setFeedCateObject(feedCate);
            baby.save();
            return true;
        } catch (AVException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据奶粉系列，跟宝宝绑定那个系列对应的默认的喂养计划
     *
     * @param powderSerie
     * @throws AVException
     */
    public void saveBabyItems(PowderSerie powderSerie) throws AVException {
        try {
            List<FeedItem> feedItems = getFeedItemsByPowder(powderSerie);
            List<BabyFeedItem> babyFeedItems = createBabyItems(feedItems);
            FeedCate feedCate = feedItems.get(0).getFeedCate();
            saveFeedCateWithAcache(feedCate);
            saveBabyFeedItem(babyFeedItems);
            saveBabyFeedItemAcache(babyFeedItems);
        } catch (AVException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 根据奶粉的系列获取默认的喂养计划
     *
     * @param powderSerie 奶粉系列
     * @return
     * @throws AVException
     */
    public List<FeedItem> getFeedItemsByPowder(PowderSerie powderSerie) throws AVException {
        if (powderSerie == null)
            return null;
        try {
            FeedPlan feedPlan = powderSerie.getFeedPlan();
            List<FeedCate> feedCates = new FeedCateDao().findByFeedPlanFromCloud(feedPlan);
            if (feedCates == null)
                return null;
            for (FeedCate feedCate : feedCates) {
                if (feedCate.isForage(monthAges)) {
                    //根据feedcate找到feeditems
                    List<FeedItem> feedItems = new FeedItemDao().findByFeedPlanFromCloud(feedCate);
                    if (feedItems == null || feedItems.isEmpty())
                        return null;
                    return feedItems;
                }
            }
            return null;
        } catch (AVException e) {
            e.printStackTrace();
            throw e;
        }
    }


    /**
     * 同步的 缓存宝宝的喂养分类，这个做缓存，并跟新宝宝
     *
     * @param feedCate 需要绑定的喂养分类
     */
    public void saveFeedCateWithAcache(FeedCate feedCate) throws AVException {
        if (feedCate == null)
            return;
        Baby baby = App.getCurrentBaby();
        try {
            baby.setFeedCateObject(feedCate);
            baby.save();
            feedCate.saveInCache();
        } catch (AVException e) {
            throw e;
        }
    }

    private void createBabyItem(FeedItem feedItem, BabyFeedItem babyFeedItem) {
        if (feedItem == null || babyFeedItem == null)
            return;
        babyFeedItem.setBaby(App.getCurrentBaby());
        babyFeedItem.setTime(feedItem.getTime());
        babyFeedItem.setType(feedItem.getType());
        babyFeedItem.addSupplement(feedItem.getSupplementObject());
    }

    /**
     * 根据默认的喂养计划，创造宝宝自己私有的宝宝喂养计划.
     *
     * @param feedItems 默认的喂养计划
     * @return 宝宝私有化的喂养计划
     */
    public List<BabyFeedItem> createBabyItems(List<FeedItem> feedItems) {
        if (feedItems == null)
            return null;
        List<BabyFeedItem> result = new ArrayList<>();
        for (FeedItem feedItem : feedItems) {
            BabyFeedItem babyFeedItem = new BabyFeedItem();
            createBabyItem(feedItem, babyFeedItem);
            result.add(babyFeedItem);
        }
        if (result.isEmpty())
            return null;
        return result;
    }

    /**
     * 同步 存储宝宝的喂养详细,存储到云端
     *
     * @return 存储是否成功
     */
    public boolean saveBabyFeedItem(List<BabyFeedItem> babyFeedItems) {
        if (babyFeedItems == null)
            return false;
        //删除云端
        List<BabyFeedItem> babyFeedItems1 = new BabyFeedItemDao().
                findBabyFeedItemsFromCloud(App.getCurrentBaby());
        Log.i("FeedPlanHElper", String.valueOf(babyFeedItems1 == null) + " " + String.valueOf(babyFeedItems1.isEmpty()));
        if (babyFeedItems1 != null && babyFeedItems1.isEmpty() == false) {
            try {
                delectAll(babyFeedItems1);
            } catch (AVException e) {
                return false;
            }
        }
        try {
            for (BabyFeedItem babyFeedItem : babyFeedItems) {
                babyFeedItem.save();
            }
            return true;
        } catch (AVException e) {
            return false;
        }
    }

    /**
     * 同步的吧宝宝的喂养计划缓存到本地
     *
     * @param babyFeedItems 喂养计划表
     * @return 是否成功
     */
    public boolean saveBabyFeedItemAcache(List<BabyFeedItem> babyFeedItems) {
        if (babyFeedItems == null)
            return false;
        new BabyFeedItemDao().saveBabyItemAcache(babyFeedItems);
        return true;
    }

    /**
     * 同步的存储所有宝宝的相关条目,先存储到云端，成功存本地
     *
     * @param babyFeedItems
     */
    public void changeSave(List<BabyFeedItem> babyFeedItems) throws AVException {
        try {
            AVObject.saveAll(babyFeedItems);
            saveBabyFeedItemAcache(babyFeedItems);
        } catch (AVException e) {
            throw e;
        }
    }

    /**
     * 删除云端的数据，这里并没有更改本地
     *
     * @param babyFeedItems
     * @throws AVException
     */
    public void delectAll(List<BabyFeedItem> babyFeedItems) throws AVException {
        try {
            AVObject.deleteAll(babyFeedItems);
        } catch (AVException e) {
            throw e;
        }
    }
    /**
     * 按照时间递增的排序
     *
     * @param babyFeedItems 需要排序的babyfeeditem
     * @return 排好序的列表
     */
    public List<BabyFeedItem> sortBabyfeedItems(List<BabyFeedItem> babyFeedItems) {
        if (babyFeedItems == null || babyFeedItems.isEmpty())
            return null;
        Log.i("feedPlanHelper", String.valueOf(babyFeedItems.size()));
        List<BabyFeedItem> result = new ArrayList<>();
        result.add(babyFeedItems.get(0));
        babyFeedItems.remove(0);
        for (BabyFeedItem b : babyFeedItems) {
            int i;
            for (i = result.size() - 1; i >= 0; i--) {
                int num = compare(result.get(i), b);
                if (num <= 0) {
                    result.add(i + 1, b);
                    break;
                }
            }
            if (i < 0)
                result.add(0, b);
        }
        Log.i("feedPlanHelper", String.valueOf(result.size()));
        return result;
    }

    private int compare(BabyFeedItem a, BabyFeedItem b) {
        return a.getTime().compareTo(b.getTime());
    }

    /**
     * 根据列表按照相应的顺序返回相应的时间链表
     *
     * @param babyFeedItems
     * @return 相应时间的链表
     */
    public List<int[]> getTime(List<BabyFeedItem> babyFeedItems) {
        if (babyFeedItems == null || babyFeedItems.isEmpty())
            return null;
        List<int[]> reuslt = new ArrayList<>();
        for (BabyFeedItem babyFeedItem : babyFeedItems) {
            int[] a = new int[2];
            String time = babyFeedItem.getTime();
            String[] temp = time.split(":");
            a[0] = Integer.parseInt(temp[0]);
            a[1] = Integer.parseInt(temp[1]);
            reuslt.add(a);
        }
        return reuslt;
    }

    /**
     * 转化成是否是牛奶的集合
     *
     * @param babyFeedItems
     * @return 是否是牛奶的集合
     */
    public boolean[] getType(List<BabyFeedItem> babyFeedItems) {
        if (babyFeedItems == null || babyFeedItems.isEmpty())
            return null;
        boolean[] reuslt = new boolean[babyFeedItems.size()];
        int i = 0;
        for (BabyFeedItem babyFeedItem : babyFeedItems) {
            boolean a = babyFeedItem.getType() == BabyFeedItem.TYPE_MILK;
            reuslt[i++] = a;
        }
        return reuslt;
    }

    /**
     * 从缓存取所有宝宝的相关条目
     */
    public List<BabyFeedItem> getBabyFeedItemsFromAcache() {
        List<BabyFeedItem> babyFeedItems = new BabyFeedItemDao().getBabyItemFromAcache();
        if (babyFeedItems == null || babyFeedItems.isEmpty())
            return null;
        return babyFeedItems;
    }


}
