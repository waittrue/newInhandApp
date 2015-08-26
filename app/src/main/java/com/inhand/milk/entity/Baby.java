package com.inhand.milk.entity;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.SaveCallback;
import com.inhand.milk.App;
import com.inhand.milk.utils.ACache;
import com.inhand.milk.utils.LocalGetAvFileCallBack;
import com.inhand.milk.utils.LocalSaveTask;

import java.util.List;


/**
 * Baby
 * Desc:Baby实体
 * Team: InHand
 * User:Wooxxx
 * Date: 2015-03-17
 * Time: 08:37
 */
@AVClassName(Baby.BABY_CLASS)
public class Baby extends Base implements CacheSaving<Baby> {
    public static final String BABY_CLASS = "Baby";
    public static final String NICKNAME_KEY = "nickname"; // 宝宝昵称
    public static final String BIRTHDAY_KEY = "birthday";// 生日
    public static final String POWDER_KEY = "powderSerie"; // 奶粉
    public static final String FEEDCATE_KEY = "feedCate";//喂养分类
    public static final String USER_KEY = "user"; // 所属用户
    public static final String SEX_KEY = "sex"; // 性别
    public static final String STATISTICS_KEY = "statistics"; // 统计信息
    public static final String AVATAR_KEY = "avatar"; // 宝宝头像
    public static final String ACACEAVATAR_KEY = "baby_imageve";
    public static final String CACHE_KEY = "baby";

    public static int FEMALE = 2; // 女性
    public static int MALE = 1; // 男性
    private byte[] imageBytes;

    public Baby() {
        super();
        // 首次创建宝宝时，为其自动创建统计信息及喂养计划
        this.setStatistics(new Statistics());
        this.setUser(App.getCurrentUser());
    }

    /**
     * 获得宝宝昵称
     *
     * @return 宝宝昵称
     */
    public String getNickname() {
        return this.getString(NICKNAME_KEY);
    }

    /**
     * 设置宝宝昵称
     *
     * @param nickname 昵称
     */
    public void setNickname(String nickname) {
        this.put(NICKNAME_KEY, nickname);
    }

    /**
     * 获得宝宝生日
     *
     * @return 宝宝生日 格式：2014-02-03
     */
    public String getBirthday() {
        return this.getString(BIRTHDAY_KEY);
    }

    /**
     * 设置宝宝生日
     *
     * @param birthday 宝宝生日 格式：2014-02-03
     */
    public void setBirthday(String birthday) {
        this.put(BIRTHDAY_KEY, birthday);
    }


    /**
     * 获得宝宝性别
     *
     * @return 宝宝性别：1===MALE,2===FEMALE
     */
    public int getSex() {
        return this.getInt(SEX_KEY);
    }

    /**
     * 设置宝宝性别
     *
     * @param sex 宝宝性别：1===MALE,2==FEMALE
     */
    public void setSex(int sex) {
        this.put(SEX_KEY, sex);
    }

    /**
     * 获得宝宝当前所用奶粉
     *
     * @return 宝宝当前所用奶粉
     */
    private PowderSerie getPowderSeriesObject() {
        try {
            return this.getAVObject(POWDER_KEY, PowderSerie.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 同步的获取宝宝的奶粉段数
     * @return 宝宝的段数
     */
    public PowderSerie getPowderSerie() throws AVException {
        PowderSerie powderSerie = getPowderSeriesObject();
        if (powderSerie == null)
            return null;
        try {
            powderSerie.fetch();
            return powderSerie;
        } catch (AVException e) {
            throw e;
        }
    }

    /**
     * 设置宝宝当前所用奶粉
     *
     * @param powder 宝宝当前所用奶粉
     */
    public void setPowderSeries(PowderSerie powder) {
        this.put(POWDER_KEY, powder);
    }

    /**
     * 获得一个空的绑定的feedcate的对象
     * @return 空的绑定的feedcate对象
     */
    private FeedCate getFeedCateObject() {
        try {
            FeedCate feedCate = getAVObject(FEEDCATE_KEY, FeedCate.class);
            return feedCate;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public void setFeedCate(FeedCate feedCate) {
        put(FEEDCATE_KEY, feedCate);
    }

    /**
     * 同步的从云端获得feedcate对象
     *
     * @return 喂养计划分类表
     * @throws AVException
     */
    public FeedCate getFeedCate() throws AVException {
        try {
            FeedCate feedCate = getFeedCateObject();
            if (feedCate == null)
                return null;
            feedCate.fetch();
            return feedCate;
        } catch (AVException e) {
            throw e;
        }
    }

    /**
     * 获得当前宝宝所在账户
     *
     * @return 宝宝所在账户
     */
    public User getUser() {
        return this.getAVUser(USER_KEY, User.class);
    }

    public void setUser(User user) {
        try {
            this.put(USER_KEY, AVObject.createWithoutData(User.class, user.getObjectId()));
        } catch (AVException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得宝宝饮奶信息
     *
     * @return 宝宝饮奶信息
     */
    public Statistics getStatistics() {
        try {
            return this.getAVObject(STATISTICS_KEY, Statistics.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 设置宝宝饮奶信息
     *
     * @param statistics 宝宝饮奶信息
     */
    public void setStatistics(Statistics statistics) {
        this.put(STATISTICS_KEY, statistics);
    }

    /**
     * 获得宝宝头像
     *
     * @return 宝宝头像
     */
    public AVFile getAvatar() {
        return this.getAVFile(AVATAR_KEY);
    }

    /**
     * 设置宝宝头像
     *
     * @param avatar 宝宝头像
     */
    public void setAvatar(AVFile avatar) {
        this.put(AVATAR_KEY, avatar);
    }

    /**
     * 同步的 吧文件二进制存储到本地和云端
     * @param bytes
     */
    public void saveAvatorBytes(byte[] bytes) throws AVException {
        if (bytes == null)
            return;
        AVFile avFile = new AVFile("宝宝头像", bytes);
        try {
            avFile.save();
            setAvatar(avFile);
            imageBytes = bytes;
            saveImageInAcache();
            save();
        } catch (AVException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 异步的获得宝宝头像的二进制
     *
     * @param callBack 找到的回调接口
     */
    public void getAvatorBytes(final LocalGetAvFileCallBack callBack) {
        AVFile avFile = getAvatar();
        if (avFile == null)
            return;
        avFile.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(final byte[] bytes, AVException e) {
                if (e == null && bytes != null)
                    imageBytes = bytes;
                callBack.done(bytes, e);
            }
        });
    }

    /**
     * 同步的得到二进制值
     *
     * @return
     * @throws AVException 当网络发生异常的时候
     */
    public byte[] getAvatorBytes() throws AVException {
        AVFile avFile = getAvatar();
        if (avFile == null) {
            return null;
        }
        try {
            imageBytes = avFile.getData();
            return imageBytes;
        } catch (AVException e) {
            imageBytes = null;
            throw e;
        }
    }



    /**
     * 异步地存储Baby对象，若已存在，则为更新
     *
     * @param saveCallback ß回调接口
     */
    public void saveInCloud(final SaveCallback saveCallback) {
        this.saveInBackground(saveCallback);
    }

    /**
     * 同步地存储Baby对象，若已存在，则为更新
     */
    public void saveInCloud() throws AVException {
        this.save();
    }

    @Override
    public void saveInCache(final Context ctx, final LocalSaveTask.LocalSaveCallback callback) {
        LocalSaveTask task =
                new LocalSaveTask(callback) {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        saveInCache(ctx);
                        return super.doInBackground(voids);
                    }
                };
        task.execute();
    }

    @Override
    public void saveInCache(final Context ctx) {
        ACache aCache = ACache.get(ctx);
        aCache.put(Baby.CACHE_KEY, this.toJSONObject());
        // 同时更新CurrentBaby
        App.currentBaby = this;
    }

    /**
     * 把头像的二进制缓存起来
     */
    public void saveImageInAcache() {
        ACache aCache = ACache.get(App.getAppContext());
        if (imageBytes != null)
            aCache.put(ACACEAVATAR_KEY + this.getObjectId(), JSON.toJSONString(imageBytes));
    }

    /**
     * 从本地缓存中获取头像二进制
     *
     * @return 头像的二进制
     */
    public byte[] getImageFromAcache() {
        ACache aCache = ACache.get(App.getAppContext());
        String json = aCache.getAsString(Baby.ACACEAVATAR_KEY + this.getObjectId());
        if (json == null)
            return null;
        byte[] image = JSON.parseObject(json, byte[].class);
        return image;
    }

    /**
     * 同步云端，从云端完全复制下来，已云端为准.
     */
    public void sync() {
        AVQuery<Baby> query = AVQuery.getQuery(Baby.class);
        query.whereEqualTo("objectId", this.getObjectId());
        try {
            List<Baby> babies = query.find();
            if (babies == null || babies.isEmpty())
                return;
            Baby baby = babies.get(0);
            App.currentBaby = baby;
            baby.saveInCache(App.getAppContext());
        } catch (AVException e) {
            e.printStackTrace();
        }
    }

    public void saveBabyItemAcache(List<BabyFeedItem> babyFeedItems) {
        if (babyFeedItems == null)
            return;
        String json = JSON.toJSONString(babyFeedItems);
        ACache aCache = ACache.get(App.getAppContext());
        aCache.put(BabyFeedItem.ACACHE_KEY, json);
        Log.i("baby", "savebabyitemache json success");
    }

    public List<BabyFeedItem> getBabyItemFromAcache() {
        String json = ACache.get(App.getAppContext()).getAsString(BabyFeedItem.ACACHE_KEY);
        if (json == null)
            return null;
        List<BabyFeedItem> babyFeedItems = JSON.parseArray(json, BabyFeedItem.class);
        if (babyFeedItems == null || babyFeedItems.isEmpty())
            return null;
        return babyFeedItems;
    }
}
