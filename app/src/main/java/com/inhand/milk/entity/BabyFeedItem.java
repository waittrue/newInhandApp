package com.inhand.milk.entity;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVException;

import java.util.List;

/**
 * Created by Administrator on 2015/8/26.
 * 作者：大力
 * 时间：2015/8/26
 * 描述：宝宝私有化的喂养条目，
 */
@AVClassName(BabyFeedItem.CLASS_NAME)
public class BabyFeedItem extends Base {
    public static final String CLASS_NAME = "Milk_BabyFeedDetail";
    public static final String TIME_KEY = "time";
    public static final String BABY_KEY = "baby";
    public static final String TYPE_KEY = "type";
    public static final String SUPPLEMENTS_KEY = "supplements";
    public static final String ACACHE_KEY = "babyfeedItem";

    public static final int TYPE_MILK = 0; // 饮奶方式
    public static final int TYPE_SUPP = 1; // 辅食方式

    public void setTime(String time) {
        this.put(TIME_KEY, time);
    }

    public String getTime() {
        return getString(TIME_KEY);
    }

    public void setBaby(Baby baby) {
        this.put(BABY_KEY, baby);
    }

    public Baby getBaby() {
        return getAVObject(BABY_KEY);
    }

    public void setType(int type) {
        put(TYPE_KEY, type);
    }

    public int getType() {
        return getInt(TYPE_KEY);
    }


    public void addSupplement(Supplement supplement) {
        addUnique(SUPPLEMENTS_KEY, supplement);
    }

    public void addSupplement(List<Supplement> supplements) {
        if (supplements == null)
            return;
        addAllUnique(SUPPLEMENTS_KEY, supplements);
    }


    private List<Supplement> getSupplementsObjects() {
        return getList(SUPPLEMENTS_KEY);
    }


    private void setSupplementsObjects(List<Supplement> supplements) {
        put(SUPPLEMENTS_KEY, supplements);
    }

    /**
     * 同步的获取所有的辅食对象
     *
     * @return
     * @throws AVException
     */

    public List<Supplement> fechSupplements() throws AVException {
        List<Supplement> supplements = getSupplementsObjects();
        if (supplements == null)
            return null;
        try {
            for (Supplement s : supplements) {
                s.fetch();
            }
            return supplements;
        } catch (AVException e) {
            throw e;
        }
    }

}
