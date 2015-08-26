package com.inhand.milk.dao;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.inhand.milk.App;
import com.inhand.milk.entity.PowderBrand;
import com.inhand.milk.utils.ACache;

import java.util.List;

/**
 * Created by Administrator on 2015/8/20.
 * 作者：大力
 * 时间：2015/8/20
 * 描述：奶粉品牌获取数据层
 */
public class PowderBrandDao {
    private static final String SORT_BY = "pinyinName";
    private AVQuery<PowderBrand> query = AVQuery.getQuery(PowderBrand.class);


    public void findFromCloud(final int limit, final FindCallback<PowderBrand> callback) {
        // 按照更新时间降序排序
        query.orderByAscending(SORT_BY);
        query.whereEqualTo(PowderBrand.ISDEL_KEY, false);
        // 最大返回1000条
        if (limit > 0)
            query.limit(limit);
        query.findInBackground(callback);
    }

    public List<PowderBrand> findFromCloud(final int limit) throws AVException {
        // 按照更新时间降序排序
        query.orderByAscending(SORT_BY);
        query.whereEqualTo(PowderBrand.ISDEL_KEY, false);
        // 最大返回1000条
        if (limit > 0)
            query.limit(limit);
        try {
            List<PowderBrand> powderBrands = query.find();
            if (powderBrands == null || powderBrands.isEmpty())
                return null;
            return powderBrands;
        } catch (AVException e) {
            throw e;
        }
    }

    public List<PowderBrand> findFromCacheOrCloud() throws AVException {
        return findFromCacheOrCloud(0);
    }

    public List<PowderBrand> findFromCacheOrCloud(final int limit) throws AVException {
        // 按照更新时间降序排序
        query.orderByAscending(SORT_BY);
        query.include(PowderBrand.LOGO_KEY);
        query.whereEqualTo(PowderBrand.ISDEL_KEY, false);
        query.setCachePolicy(AVQuery.CachePolicy.CACHE_ELSE_NETWORK);
        // 最大返回1000条
        if (limit > 0)
            query.limit(limit);
        try {
            List<PowderBrand> powderBrands = query.find();
            if (powderBrands == null || powderBrands.isEmpty())
                return null;
            return powderBrands;
        } catch (AVException e) {
            throw e;
        }
    }
    public PowderBrand findFromAche() {
        ACache aCache = ACache.get(App.getAppContext());
        String json = aCache.getAsString(PowderBrand.ACHA_KEY);
        if (json == null)
            return null;
        String image = aCache.getAsString(PowderBrand.ACHA_IMAGE_KEY);
        PowderBrand powderBrand = JSON.parseObject(json, PowderBrand.class);
        if (image != null) {
            byte[] bytes = JSON.parseObject(image, byte[].class);
            powderBrand.setImageBytes(bytes);
        }
        return powderBrand;
    }
}
