package com.inhand.milk.dao;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.inhand.milk.App;
import com.inhand.milk.entity.PowderBrand;
import com.inhand.milk.entity.PowderSerie;
import com.inhand.milk.utils.ACache;

import java.util.List;

/**
 * Created by Administrator on 2015/8/21.
 * 作者：大力
 * 时间：2015/8/21
 * 描述：
 */
public class PowderSerieDao {
    private static final String SORT_BY = "phase";
    private AVQuery<PowderSerie> query = AVQuery.getQuery(PowderSerie.class);

    public List<PowderSerie> getFromCloud(int limit) throws AVException {
        List<PowderSerie> result = null;
        if (limit > 0)
            query.setLimit(limit);
        query.orderByAscending(SORT_BY);
        query.whereEqualTo(PowderSerie.ISDEL_KEY, false);
        try {
            result = query.find();
            if (result == null || result.isEmpty())
                return null;
            return result;
        } catch (AVException e) {
            throw e;
        }
    }

    public List<PowderSerie> getFromCloud() throws AVException {
        return getFromCloud(0);
    }

    public List<PowderSerie> getFromCloudByPowderBrand(PowderBrand powderBrand
            , int limit) throws AVException {
        if (powderBrand == null)
            return null;
        List<PowderSerie> result = null;
        if (limit > 0)
            query.setLimit(limit);
        query.orderByAscending(SORT_BY);
        query.whereEqualTo(PowderSerie.ISDEL_KEY, false);
        query.whereEqualTo(PowderSerie.POWDERBRAND_KEY, powderBrand);
        try {
            result = query.find();
            if (result == null || result.isEmpty())
                return null;
            return result;
        } catch (AVException e) {
            throw e;
        }
    }

    public List<PowderSerie> getFromCloudByPowderBrand(PowderBrand powderBrand) throws AVException {
        return getFromCloudByPowderBrand(powderBrand, 0);
    }

    public PowderSerie findFromAcache() {
        ACache aCache = ACache.get(App.getAppContext());
        String json = aCache.getAsString(PowderSerie.ACHACE_KEY);
        if (json == null)
            return null;
        return JSON.parseObject(json, PowderSerie.class);
    }
}
