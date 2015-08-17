package com.inhand.milk.entity;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVClassName;
import com.inhand.milk.dao.PowderTipDao;
import com.inhand.milk.utils.ACache;
import com.inhand.milk.utils.LocalFindTask;
import com.inhand.milk.utils.LocalSaveTask;

import java.util.List;

/**
 * Powder
 * Desc: 奶粉模型
 * Team: InHand
 * User: Wooxxx
 * Date: 2015-07-26
 * Time: 08:51
 */
@AVClassName(Powder.POWEDER_CLASS)
public class Powder extends Base {

    public static final String POWEDER_CLASS = "Milk_Powder";
    public static final String ZH_NAME_KEY = "zhName"; // 中文名
    public static final String EN_NAME_KEY = "enName"; // 英文名
    public static final String PINYIN_NAME_KEY = "pinyinName"; // 拼音名
    public static final String SERIAL_NAME_KEY = "serialName"; // 系列名
    public static final String PHASE_KEY = "phase"; // 段数


    public static final String CACHE_KEY = "powder"; // 奶粉缓存键

    /**
     * 获得奶粉中文名
     *
     * @return 奶粉中文名
     */
    public String getZhName() {
        return this.getString(ZH_NAME_KEY);
    }

    public void setZhName(String zhName) {
        this.put(ZH_NAME_KEY, zhName);
    }

    /**
     * 获得奶粉汉语拼音
     *
     * @return
     */
    public String getPinyinName() {
        return this.getString(PINYIN_NAME_KEY);
    }

    public void setPinyinName(String pinyinName) {
        this.put(PINYIN_NAME_KEY, pinyinName);
    }

    /**
     * 获得奶粉英文名
     *
     * @return 奶粉英文名
     */
    public String getEnName() {
        return this.getString(EN_NAME_KEY);
    }

    public void setEnName(String enName) {
        this.put(EN_NAME_KEY, enName);
    }

    /**
     * 获得奶粉系列名
     *
     * @return 奶粉系列名
     */
    public String getSerialName() {
        return this.getString(SERIAL_NAME_KEY);
    }

    public void setSerialName(String serialName) {
        this.put(SERIAL_NAME_KEY, serialName);
    }

    /**
     * 获得奶粉段数
     *
     * @return 奶粉段数
     */
    public int getPhase() {
        return this.getInt(PHASE_KEY);
    }

    public void setPhase(int phase) {
        this.put(PHASE_KEY, phase);
    }

    /**
     * 异步地从本地获取该奶粉的所有使用说明
     *
     * @param ctx      上下文环境
     * @param callback 回调接口
     */
    public void fetchTips(final Context ctx, final LocalFindTask.LocalFindCallback<PowderTip> callback) {
        ACache aCache = ACache.get(ctx);
        String json = aCache.getAsString(PowderTip.CACHE_KEY);
        LocalFindTask task = new LocalFindTask() {
            List<PowderTip> tips;

            @Override
            protected Void doInBackground(Void... voids) {
                tips = fetchTips(ctx);
                return super.doInBackground(voids);
            }


            @Override
            protected void onPostExecute(Void aVoid) {
                callback.done(tips);
                super.onPostExecute(aVoid);
            }
        };
    }

    /**
     * 同步地从本地获取该奶粉所有的使用说明
     *
     * @param ctx 上下文环境
     * @return 奶粉使用说明列表
     */
    public List<PowderTip> fetchTips(final Context ctx) {
        ACache aCache = ACache.get(ctx);
        String json = aCache.getAsString(PowderTip.CACHE_KEY);
        return json == null ? null : JSON.parseArray(json, PowderTip.class);
    }

    /**
     * 异步地缓存奶粉使用说明列表到本地
     *
     * @param ctx      上下文环境
     * @param tips     奶粉说明列表
     * @param callback 回调接口
     */
    public void cacheTips(
            final Context ctx
            , final List<PowderTip> tips
            , final LocalSaveTask.LocalSaveCallback<PowderTip> callback) {
        PowderTipDao ptd = new PowderTipDao();
        ptd.cache(ctx, tips, callback);
    }

    /**
     * 同步地缓存奶粉使用说明列表到本地
     *
     * @param ctx  上下文环境
     * @param tips 待缓存奶粉使用说明列表
     */
    public void cacheTips(Context ctx, List<PowderTip> tips) {
        PowderTipDao ptd = new PowderTipDao();
        ptd.cache(ctx, tips);
    }


}
