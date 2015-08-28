package com.inhand.milk.entity;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVException;
import com.inhand.milk.App;
import com.inhand.milk.utils.ACache;

/**
 * Created by Administrator on 2015/8/21.
 * 作者：大力
 * 时间：2015/8/21
 * 描述：奶粉的详细信息
 */
@AVClassName(PowderDetail.CLASS_NAME)
public class PowderDetail extends Base {
    public static final String CLASS_NAME = "Milk_PowderDetail";
    public static final String ISDEL_KEY = "isDel";
    public static final String ADVISEMAX_KEY = "recVoIMax";
    public static final String FORAGE_KEY = "forAge";
    public static final String COUNT_KEY = "count";
    public static final String POWDERSERIER_KEY = "powderSerie";
    public static final String RECDENSITY_KEY = "recDensity";
    public static final String SPOONDOSAGE_KEY = "spoonDosage";
    public static final String ADVISENORMAL_KEY = "recVoINorm";
    public static final String ADVISEMIN_KEY = "recVoiMin";
    public static final String GRAMDOSAGE_KEY = "gramDosage";
    public static final String ACHACE_KEY = "powderDetailAchace";
    public static final String NEXTDETAIL_KEY = "nextDetail";
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public void setIsDel(boolean a) {
        put(ISDEL_KEY, a);
    }

    public boolean getIsDel() {
        return getBoolean(ISDEL_KEY);
    }

    public void setAdviseMax(int num) {
        put(ADVISEMAX_KEY, num);
    }

    public int getAdviseMax() {
        return getInt(ADVISEMAX_KEY);
    }

    public void setForAge(String age) {
        put(FORAGE_KEY, age);
    }

    public String getForAge() {
        return getString(FORAGE_KEY);
    }

    public void setAdviseNorm(int num) {
        put(ADVISENORMAL_KEY, num);
    }

    public String getAdviseNorm() {
        return getString(ADVISENORMAL_KEY);
    }

    public void setCount(int num) {
        put(COUNT_KEY, num);
    }

    public int getCount() {
        return getInt(COUNT_KEY);
    }

    public void setPowderSerie(PowderSerie powderSerie) {
        put(POWDERSERIER_KEY, powderSerie);
    }

    public PowderSerie getPowderSerie() {
        return getAVObject(POWDERSERIER_KEY);
    }

    public void setDensity(double a) {
        put(RECDENSITY_KEY, a);
    }

    public float getDensity() {
        return (float) getDouble(RECDENSITY_KEY);
    }

    public void setSpoonDosage(int num) {
        put(SPOONDOSAGE_KEY, num);
    }

    public int getSpoonDosage() {
        return getInt(SPOONDOSAGE_KEY);
    }

    public void setAdviseMin(int a) {
        put(ADVISEMIN_KEY, a);
    }

    public int getAdviseMin() {
        return getInt(ADVISEMIN_KEY);
    }

    public void setGramDosage(double a) {
        put(GRAMDOSAGE_KEY, a);
    }

    public double getGramDosage() {
        return getDouble(GRAMDOSAGE_KEY);
    }

    public void saveInACache() {
        ACache aCache = ACache.get(App.getAppContext());
        String json = this.toString();
        aCache.put(ACHACE_KEY, json);
    }

    public void setNextDetail(PowderDetail powderDetail) {
        this.put(NEXTDETAIL_KEY, powderDetail);
    }

    private PowderDetail getNextPowderDetailObject() {
        try {
            return getAVObject(NEXTDETAIL_KEY, PowderDetail.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 同步的返回下一个powderDetail
     *
     * @return 奶粉详细
     */
    public PowderDetail fetchNextPowderDetail() throws AVException {
        PowderDetail powderDetail = getNextPowderDetailObject();
        if (powderDetail == null)
            return null;
        try {
            powderDetail.fetch();
            return powderDetail;
        } catch (AVException e) {
            throw e;
        }
    }

    /**
     * 是否需要换宝宝的详细
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

    /**
     * 是否合适这个宝宝
     *
     * @param monthAge
     * @return
     */
    public boolean isForAge(int monthAge) {
        String forage = getForAge();
        String[] ages = forage.split("-");
        int min = Integer.parseInt(ages[0]);
        int max = Integer.parseInt(ages[1]);
        if (monthAge <= max && monthAge >= min)
            return true;
        return false;
    }

    /**
     * 同步的，返回下一个时间段的powderDetail，并储存缓存
     *
     * @return
     */
    public PowderDetail changeToNext() throws AVException {
        try {
            PowderDetail powderDetail = fetchNextPowderDetail();
            if (powderDetail == null)
                return null;
            powderDetail.saveInACache();
            return powderDetail;
        } catch (AVException e) {
            throw e;
        }
    }
}
