package com.inhand.milk.entity;

import android.content.Context;

import com.avos.avoscloud.AVClassName;
import com.inhand.milk.App;
import com.inhand.milk.utils.ACache;
import com.inhand.milk.utils.LocalSaveTask;

/**
 * Created by Administrator on 2015/8/20.
 * 作者：大力
 * 时间：2015/8/20
 * 描述：
 */
@AVClassName(PowderSerie.CLASS_NAME)
public class PowderSerie extends Base implements DBSaving {
    public static final String CLASS_NAME = "Milk_PowderSerie";
    public static final String ISDEL_KEY = "isDel";
    public static final String NAME_KEY = "name";
    public static final String PHASE_KEY = "phase";
    public static final String FORAGE_KEY = "forAge";
    public static final String CHANGE_KEY = "changeAge";
    public static final String POWDERBRAND_KEY = "powder";
    public static final String NAMEDOC_KEY = "nameDoc";
    public static final String ACHACE_KEY = "powderSerieAchace";
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public void setIsDel(boolean a) {
        this.put(ISDEL_KEY, a);
    }

    public boolean getIsDel() {
        return getBoolean(ISDEL_KEY);
    }

    public void setName(String name) {
        put(NAME_KEY, name);
    }

    public String getName() {
        return getString(NAME_KEY);
    }

    public void setNameDoc(String name) {
        put(NAMEDOC_KEY, name);
    }

    public String getNameDoc() {
        return getString(NAMEDOC_KEY);
    }

    public void setPhase(int i) {
        this.put(PHASE_KEY, i);
    }

    public int getPhase() {
        return getInt(PHASE_KEY);
    }

    public void setForAge(String a) {
        put(FORAGE_KEY, a);
    }

    public String getForAge() {
        return getString(FORAGE_KEY);
    }

    public int getChangeAge() {
        return getInt(CHANGE_KEY);
    }

    public void setChangeAge(int age) {
        put(CHANGE_KEY, age);
    }

    public void setPowderBrand(PowderBrand powderBrand) {
        put(POWDERBRAND_KEY, powderBrand);
    }

    public PowderBrand getPowderBrand() {
        return getAVObject(POWDERBRAND_KEY);
    }

    public PowderSerie() {
        super();
    }

    public void saveInAcache() {
        ACache aCache = ACache.get(App.getAppContext());
        String json = this.toString();
        aCache.put(ACHACE_KEY, json);
    }

    @Override
    public void saveInDB(Context ctx, LocalSaveTask.LocalSaveCallback callback) {

    }

    @Override
    public void saveInDB(Context ctx) {

    }
}
