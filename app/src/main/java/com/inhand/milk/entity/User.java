package com.inhand.milk.entity;


import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetDataCallback;
import com.inhand.milk.App;
import com.inhand.milk.STANDAR.Standar;
import com.inhand.milk.dao.BabyDao;
import com.inhand.milk.dao.BabyFeedItemDao;
import com.inhand.milk.dao.BabyInfoDao;
import com.inhand.milk.dao.OneDayDao;
import com.inhand.milk.dao.PowderDetailDao;
import com.inhand.milk.utils.ACache;
import com.inhand.milk.utils.Calculator;
import com.inhand.milk.utils.LocalGetAvFileCallBack;

import java.text.ParseException;
import java.util.Date;
import java.util.List;


/**
 * User
 * Desc:
 * Team: InHand
 * User:Wooxxx
 * Date: 2015-03-19
 * Time: 14:35
 */
public class User extends AVUser {
    public static final String CLASS_NAME = "_USER";

    public static final String NICKNAME_KEY = "nickname"; // 昵称
    public static final String SEX_KEY = "sex"; // 性别
    public static final String CITY_KEY = "city"; // 城市
    public static final String AVATAR_KEY = "avatar"; // 头像
    public static final String EMAIL_KEY = "email"; // 邮箱
    public static final String ACACEAVATAR_KEY = "user_imageve";//缓存用户头像的二进制
    public static final String UPDATEATIME_KEY = "updated_at_time";//缓存用户updatedat字段的值，leacloud这里有bug，应该是多加了一个时区的值
    //为了防止跟新后出现bug，自己就做这个缓存

    // 判断用户是否有baby的错误码
    public static final int NO_BABY = 0;
    public static final int HAS_BABY = 1;
    public static final int NETWORK_ERROR = 2;
    public static int FEMALE = 2; // 女性
    public static int MALE = 1; // 男性

    /**
     * 获得用户昵称
     *
     * @return 用户昵称
     */
    public String getNickname() {
        return this.getString(NICKNAME_KEY);
    }

    /**
     * 设置用户昵称
     *
     * @param nickname 用户昵称
     */
    public void setNickname(String nickname) {
        this.put(NICKNAME_KEY, nickname);
    }

    /**
     * 获得用户性别
     *
     * @return 用户性别
     */
    public int getSex() {
        return this.getInt(SEX_KEY);
    }

    /**
     * 设置用户性别
     *
     * @param sex 用户性别
     */
    public void setSex(int sex) {
        this.put(SEX_KEY, sex);
    }

    /**
     * 获得用户所在城市
     */
    public String getCity() {
        return this.getString(CITY_KEY);
    }

    /**
     * 设置当前城市
     *
     * @param city 城市
     */
    public void setCity(String city) {
        this.put(CITY_KEY, city);
    }

    /**
     * 获得用户头像
     *
     * @return
     */
    public AVFile getAvatar() {
        return this.getAVFile(AVATAR_KEY);
    }

    /**
     * 设置用户头像
     */
    public void setAvatar(AVFile avatar) {
        this.put(AVATAR_KEY, avatar);
    }

    /**
     * 获得用户email
     *
     * @return 用户email
     */
    public String getEmail() {
        return this.getString(EMAIL_KEY);
    }

    /**
     * 设置用户email
     *
     * @param email 用户email
     */
    public void setEmail(String email) {
        put(EMAIL_KEY, email);
    }

    /**
     * 异步地取得当前用户的所有宝宝
     *
     * @param findCallback 回调接口
     */
    public void fetchBabies(final FindCallback<Baby> findCallback) {
        BabyDao babyDao = new BabyDao();
        babyDao.findByUserFromCloud(this, findCallback);
    }

    /**
     * 同步地取得当前用户的所有宝宝
     */
    public List<Baby> fetchBabies() {
        BabyDao babyDao = new BabyDao();
        return babyDao.findByUserFromCloud(this);
    }

    /**
     * 查看该用户是否填写了宝宝信息,此方法请用异步过程或新开线程执行
     */
    public int hasBaby(final Context ctx) {
        // 首先判断本地缓存中是否有Baby
        if (App.getCurrentBaby() == null) {
            // 若没有，再判断云端是否有宝宝，若有，选择第一个宝宝将其缓存
            List<Baby> babies = fetchBabies();
            if (babies != null) {
                if (babies.size() == 0)
                    return NO_BABY;
                Baby baby = babies.get(0);
                // 初始化宝宝其他信息
                initBaby(ctx, baby);

                return HAS_BABY;
            } else {
                return NETWORK_ERROR;
            }
        }
        return HAS_BABY;
    }

    /**
     * 初始化宝宝信息
     *
     * @param ctx  上下文环境
     * @param baby 宝宝
     */
    private void initBaby(final Context ctx, Baby baby) {
        //缓存奶粉信息
        try {
            PowderSerie powderSerie = baby.getPowderSerie();
            String birth = baby.getBirthday();
            Date date = null;
            try {
                date = Standar.DATE_FORMAT.parse(birth);
            } catch (ParseException e) {
            }
            int monthAge = Calculator.getMonths(date, new Date());
            PowderBrand powderBrand = powderSerie.fetchPowderBrand();
            List<PowderDetail> powderDetails = new PowderDetailDao().findFromCloud(powderSerie);
            for (PowderDetail p : powderDetails) {
                if (p.isForAge(monthAge)) {
                    powderBrand.saveInCache();
                    p.saveInACache();
                    powderSerie.saveInAcache();
                }
            }
        } catch (AVException e) {
            return;
        }
        // 缓存喂养计划信息
        List<BabyFeedItem> babyFeedItems = new BabyFeedItemDao().findBabyFeedItemsFromCloud(baby);
        baby.saveBabyItemAcache(babyFeedItems);
        Log.i("user", String.valueOf(babyFeedItems.size()));
        //缓存baby
        baby.saveInCache(ctx);


        //缓存babyinfo 这部分是大力加的；
      //  Log.d("initBaby", "saveinache_start");
        BabyInfoDao babyInfoDao = new BabyInfoDao();
        List<BabyInfo> babyInfos = babyInfoDao.findByBabyFromCloud(baby);
        if (babyInfos == null)
            return;
        for (BabyInfo babyInfo : babyInfos) {
            try {
                babyInfo.saveInCache(App.getAppContext());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //同步oneday,这里应该用同步的方法，不能用异步。
        OneDayDao oneDayDao = new OneDayDao();
        try {
            oneDayDao.syncCloud(App.getAppContext());
        }catch (AVException e){
            return;
        }
    }

    /**
     * 同步的 吧文件二进制存储到本地和云端
     *
     * @param bytes
     */
    public void saveAvatorBytes(byte[] bytes) throws AVException {
        if (bytes == null)
            return;
        AVFile avFile = new AVFile("用户头像", bytes);
        try {
            avFile.save();
            setAvatar(avFile);
            saveImageInAcache(bytes);
            save();
        } catch (AVException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 异步的获得用户头像的二进制
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
                callBack.done(bytes, e);
            }
        });
    }

    /**
     * 同步的得到二进制值
     *
     * @return 图像的二进制数据
     * @throws AVException 当网络发生异常的时候
     */
    public byte[] getAvatorBytes() throws AVException {
        AVFile avFile = getAvatar();
        if (avFile == null) {
            return null;
        }
        try {
            return avFile.getData();
        } catch (AVException e) {
            throw e;
        }
    }

    /**
     * 从本地缓存中获取头像二进制
     *
     * @return 头像的二进制
     */
    public byte[] getImageFromAcache() {
        ACache aCache = ACache.get(App.getAppContext());
        String json = aCache.getAsString(User.ACACEAVATAR_KEY + this.getObjectId());
        if (json == null)
            return null;
        byte[] image = JSON.parseObject(json, byte[].class);
        Log.i("User", String.valueOf(image.length));
        return image;
    }

    /**
     * 把头像的二进制缓存起来
     */
    public void saveImageInAcache(byte[] bytes) {
        ACache aCache = ACache.get(App.getAppContext());
        if (bytes != null)
            aCache.put(ACACEAVATAR_KEY + this.getObjectId(), JSON.toJSONString(bytes));
    }


    public void sync() {
        AVQuery<User> query = AVQuery.getQuery(User.class);
        query.whereEqualTo("objectId", this.getObjectId());
        try {
            List<User> users = query.find();
            if (users == null || users.isEmpty())
                return;
            if (users.get(0).getUpdatedAt().equals(getUpdateAtInAcache())) {
                Log.i("USer", "时间相同return:");
                return;
            }
            copyUser(users.get(0), this);
            this.save();
        } catch (AVException e) {

        }

    }

    private void copyUser(User src, User dst) {
        dst.setEmail(src.getEmail());
        dst.setSex(src.getSex());
        dst.setCity(src.getCity());
        dst.setNickname(src.getNickname());
        dst.setMobilePhoneNumber(src.getMobilePhoneNumber());
        dst.setAvatar(src.getAvatar());
        try {
            byte[] bytes = src.getAvatorBytes();
            saveImageInAcache(bytes);
        } catch (AVException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save() throws AVException {
        super.save();
        saveUpdateAtInAcache();
    }

    private void saveUpdateAtInAcache() {
        ACache aCache = ACache.get(App.getAppContext());
        aCache.put(UPDATEATIME_KEY + getObjectId(), JSON.toJSONString(getUpdatedAt().getTime()));
    }

    private Date getUpdateAtInAcache() {
        ACache aCache = ACache.get(App.getAppContext());
        String time = aCache.getAsString(UPDATEATIME_KEY + getObjectId());
        if (time == null)
            return null;
        long t = Long.parseLong(time);
        Date date = new Date(t);
        return date;
    }
}
