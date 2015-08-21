package com.inhand.milk.entity;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.GetDataCallback;
import com.inhand.milk.App;
import com.inhand.milk.utils.ACache;
import com.inhand.milk.utils.LocalGetAvFileCallBack;
import com.inhand.milk.utils.LocalSaveTask;

import java.util.List;

/**
 * Created by Administrator on 2015/8/20.
 * 作者：大力
 * 时间：2015/8/20
 * 描述：
 */
@AVClassName(PowderBrand.CLASS_NAME)
public class PowderBrand extends Base implements DBSaving<PowderBrand> {
    public static final String CLASS_NAME = "Milk_PowderBrand";
    public static final String ISDEL_KEY = "isDel";
    public static final String ZHNAME_KEY = "zhName";
    public static final String SERIES_KEY = "seriesArray";
    public static final String LOGO_KEY = "logo";
    public static final String ENNAME_KEY = "enName";
    public static final String ORIGIN_KEY = "origin";
    public static final String PINYINNAME_KEY = "pinyinName";
    public static final String ACHA_KEY = "powderbrandAche";
    public static final String ACHA_IMAGE_KEY = "powderbrandImage";
    public static final String DATE_FORMAT = "yyyy-MM-dd";


    private byte[] imageBytes = null;

    public PowderBrand() {
        super();
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    public void setIsDel(boolean a) {
        this.put(ISDEL_KEY, a);
    }

    public boolean getIsDel() {
        return getBoolean(ISDEL_KEY);
    }

    public void setZhName(String name) {
        put(ZHNAME_KEY, name);
    }

    public String getZhName() {
        return getString(ZHNAME_KEY);
    }

    public void addSerie(PowderSerie serie) {
        this.addUnique(SERIES_KEY, serie);
    }

    @SuppressWarnings("unchecked")
    public List<PowderSerie> getSeries() {
        return (List) getList(SERIES_KEY, PowderSerie.class);
    }

    public void setLog(AVFile file) {
        this.put(LOGO_KEY, file);
    }

    public AVFile getLog() {
        return getAVFile(LOGO_KEY);
    }

    public void setEnName(String name) {
        put(ENNAME_KEY, name);
    }

    public String getEnName() {
        return getString(ENNAME_KEY);
    }

    public void setOrigin(String name) {
        put(ORIGIN_KEY, name);
    }

    public String getOrigin() {
        return getString(ORIGIN_KEY);
    }

    public void setPinYinName(String name) {
        put(PINYINNAME_KEY, name);
    }

    public String getPinYinName() {
        return getString(PINYINNAME_KEY);
    }

    public void getLogBitmap(final LocalGetAvFileCallBack callBack) {
        AVFile avFile = getLog();
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

    @Override
    public void saveInDB(Context ctx, LocalSaveTask.LocalSaveCallback<PowderBrand> callback) {

    }

    @Override
    public void saveInDB(Context ctx) {

    }

    public void saveInCache() {
        String json = this.toString();
        ACache aCache = ACache.get(App.getAppContext());
        aCache.put(ACHA_KEY, json);
        if (imageBytes != null) {
            aCache.put(ACHA_IMAGE_KEY, JSON.toJSONString(imageBytes));
        }
    }
}
