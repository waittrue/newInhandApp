package com.inhand.milk.helper;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.inhand.milk.App;
import com.inhand.milk.STANDAR.Standar;
import com.inhand.milk.dao.PowderBrandDao;
import com.inhand.milk.dao.PowderDetailDao;
import com.inhand.milk.dao.PowderSerieDao;
import com.inhand.milk.entity.Baby;
import com.inhand.milk.entity.PowderBrand;
import com.inhand.milk.entity.PowderDetail;
import com.inhand.milk.entity.PowderSerie;
import com.inhand.milk.utils.Calculator;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/8/26.
 * 作者：大力
 * 时间：2015/8/26
 * 描述：这里主要跟换存milk，换milk各种需要的逻辑
 */
public class MilkHelper {
    private static final String TAG = "MILKHELPER";

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
     * 同步的存储，奶粉的所有信息，并更爱baby的对象
     *
     * @param powderBrand  奶粉品牌
     * @param powderSerie  奶粉系列
     * @param powderDetail 奶粉详情
     * @return 存储是否成功
     */
    public boolean saveMilk(PowderBrand powderBrand, PowderSerie powderSerie,
                            PowderDetail powderDetail) {
        /**
         * 描述：当宝宝选择一个奶粉的时候，我们要缓存所有奶粉的相关信息，并吧baby绑定到这个奶粉的系列
         */
        if (powderBrand == null || powderDetail == null || powderSerie == null) {
            return false;
        }
        Baby baby = App.getCurrentBaby();
        try {
            Log.i(TAG, "ready to save baby");
            baby.setPowderSeries(powderSerie);
            baby.save();
            baby.saveInCache(App.getAppContext());
            powderBrand.saveInCache();
            powderDetail.saveInACache();
            powderSerie.saveInAcache();
        } catch (AVException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 同步的储存，奶粉的所有信息
     *
     * @param powderBrand 奶粉品牌
     * @param powderSerie 奶粉段
     * @return 存储是否成功
     */
    public boolean saveMilk(PowderBrand powderBrand, PowderSerie powderSerie) {
        if (powderBrand == null || powderSerie == null)
            return false;
        try {
            List<PowderDetail> powderDetails = new PowderDetailDao().findFromCloud(powderSerie);
            if (powderDetails == null) {
                Log.i(TAG, "powderDetails == null");
                return false;
            }
            //解析宝宝的生日,计算月龄
            int monthAge;
            try {
                monthAge = getmonthAge();
            } catch (ParseException e) {
                e.printStackTrace();
                Log.i(TAG, "monthage Pareserror");
                return false;
            }
            //选出合适生日的detail
            for (PowderDetail p : powderDetails) {
                if (p.isForAge(monthAge)) {
                    return saveMilk(powderBrand, powderSerie, p);
                }
            }
            Log.i(TAG, "not find forage Detail");
            return false;
        } catch (AVException e) {
            e.printStackTrace();
            Log.i(TAG, "detail catch error");
            return false;
        }
    }

    public boolean saveMilk(PowderBrand powderBrand, PowderSerie powderSerie, int monthAges) {
        if (powderBrand == null || powderSerie == null)
            return false;
        try {
            List<PowderDetail> powderDetails = new PowderDetailDao().findFromCloud(powderSerie);
            if (powderDetails == null) {
                Log.i(TAG, "powderDetails == null");
                return false;
            }
            //选出合适生日的detail
            for (PowderDetail p : powderDetails) {
                if (p.isForAge(monthAges)) {
                    return saveMilk(powderBrand, powderSerie, p);
                }
            }
            Log.i(TAG, "not find forage Detail");
            return false;
        } catch (AVException e) {
            e.printStackTrace();
            Log.i(TAG, "detail catch error");
            return false;
        }
    }

    /**
     * 同步的存储，奶粉所有相关的信息
     *
     * @param powderBrand 奶粉品牌
     * @return 是否存储成功
     */
    public boolean saveMilk(PowderBrand powderBrand) {
        if (powderBrand == null)
            return false;
        try {
            List<PowderSerie> powderSeries = new PowderSerieDao().getFromCloudByPowderBrand(powderBrand);
            if (powderSeries == null)
                return false;

            int monthAge;
            try {
                monthAge = getmonthAge();
            } catch (ParseException e) {
                e.printStackTrace();
                return false;
            }

            for (PowderSerie p : powderSeries) {
                if (p.isForAge(monthAge)) {
                    saveMilk(powderBrand, p);
                }
            }
            return false;
        } catch (AVException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean saveMilk(PowderSerie powderSerie, int monthAge) {
        if (powderSerie == null)
            return false;
        try {
            PowderBrand powderBrand = powderSerie.fetchPowderBrand();
            return saveMilk(powderBrand, powderSerie, monthAge);
        } catch (AVException e) {
            return false;
        }
    }

    public boolean saveMilk(PowderSerie powderSerie) {
        if (powderSerie == null)
            return false;
        try {
            PowderBrand powderBrand = powderSerie.fetchPowderBrand();
            return saveMilk(powderBrand, powderSerie);
        } catch (AVException e) {
            return false;
        }
    }

    public PowderBrand getMilkPowderBrand() {
        return new PowderBrandDao().findFromAche();
    }

    public PowderSerie getMilkPowderSerie() {
        return new PowderSerieDao().findFromAcache();
    }

    public PowderDetail getMilkPowderDetail() {
        return new PowderDetailDao().findFromAcache();
    }

    public boolean needChangeSeries() {
        int monthAge;
        try {
            monthAge = getmonthAge();
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        PowderSerie p = getMilkPowderSerie();
        return p.needChange(monthAge);
    }

    /**
     * 同步，这里同步
     */
    public void sync() {

    }

}

