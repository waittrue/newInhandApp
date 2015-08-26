package com.inhand.milk.dao;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.inhand.milk.App;
import com.inhand.milk.entity.PowderDetail;
import com.inhand.milk.entity.PowderSerie;
import com.inhand.milk.utils.ACache;

import java.util.List;

/**
 * Created by Administrator on 2015/8/21.
 * 作者：大力
 * 时间：2015/8/21
 * 描述：
 */
public class PowderDetailDao {
    private AVQuery<PowderDetail> query = AVQuery.getQuery(PowderDetail.class);

    public List<PowderDetail> findFromCloudByPowderSerie(PowderSerie powderSerie) throws AVException {
        if (powderSerie == null)
            return null;
        query.whereEqualTo(PowderDetail.POWDERSERIER_KEY, powderSerie);
        query.whereEqualTo(PowderDetail.ISDEL_KEY, false);
        //这里吧二级目标feedplan也加载了.
        try {
            List<PowderDetail> powderDetails = query.find();
            if (powderDetails == null || powderDetails.isEmpty())
                return null;
            return powderDetails;
        } catch (AVException e) {
            throw e;
        }
    }

    public void findFromCloudByPowderSerie(PowderSerie powderSerie,
                                           final FindCallback<PowderDetail> callback) {
        if (powderSerie == null) {
            throw new NullPointerException("powderserie 为空");
        }
        query.whereEqualTo(PowderDetail.POWDERSERIER_KEY, powderSerie);
        query.whereEqualTo(PowderDetail.ISDEL_KEY, false);
        //这里吧二级目标feedplan也加载了.
        query.findInBackground(callback);

    }

    public List<PowderDetail> findFromCloud(PowderSerie powderSerie) throws AVException {
        if (powderSerie == null) {
            Log.i("powderDetailDao", "canshu powderserie == null");
            return null;
        }
        try {
            query.whereEqualTo(PowderDetail.POWDERSERIER_KEY, powderSerie);
            Log.i("powderDetailDao", powderSerie.getObjectId());
            query.whereEqualTo(PowderDetail.ISDEL_KEY, false);
            List<PowderDetail> powderDetails = query.find();
            if (powderDetails == null || powderDetails.isEmpty()) {
                Log.i("powderDetailDao", "powderserie == null" + String.valueOf(powderDetails == null));
                return null;
            }
            return powderDetails;
        } catch (AVException e) {
            throw e;
        }
    }
    public PowderDetail findFromAcache() {
        ACache aCache = ACache.get(App.getAppContext());
        String json = aCache.getAsString(PowderDetail.ACHACE_KEY);
        if (json == null)
            return null;
        return JSON.parseObject(json, PowderDetail.class);
    }
}
