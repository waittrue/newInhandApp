package com.inhand.milk.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.inhand.milk.App;
import com.inhand.milk.entity.Baby;
import com.inhand.milk.entity.Base;
import com.inhand.milk.entity.OneDay;
import com.inhand.milk.entity.Statistics;
import com.inhand.milk.helper.DBHelper;
import com.inhand.milk.helper.JSONHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * StatisticsDao
 * Desc: 统计信息数据访问层
 * Date: 2015/6/3
 * Time: 7:24
 * Created by: Wooxxx
 */
public class StatisticsDao extends BaseDao{
    private static final String SORT_BY = "createdAt";
    private AVQuery<Statistics> query;
    private static final String RECORDS_COMP_FORMAT = "HH:mm";
    public StatisticsDao(Context ctx) {
        super(ctx);
    }
    /**
     * 与云端同步数据：
     * 1. 先从云端抓取到所有数据
     * 2. 比较日期：
     * 2.1
     * 对于都存在同一日期，而version不相同者，合并更新
     * 2.2
     * 本地存在某一OneDay记录，而云端不存在，更新至云
     * 2.3
     * 本地存在某一OneDay记录，而云端不存在，更新至本地
     * 3. 打包上传
     */
    public void syncCloud() throws AVException {

        query = AVQuery.getQuery(Statistics.class);
        //从云端抓取所有
        Statistics daysInCloud = findFromCloudByCurrentBaby();

        //从本地抓取所有
        Statistics daysInDB = findFromDBByCurrentBaby();
        if(daysInCloud==null&&daysInDB==null){
            Log.d("savecloud1","ok");
            return;
        }
        else if(daysInCloud==null){
            Log.d("savecloud2","ok");
            updateOrSaveInCloud(daysInDB);
        }
        else if(daysInDB==null){
            Log.d("savecloud3","ok");
            updateOrSaveInDB(daysInCloud);
        }
        else{
            Log.d("savecloud4","ok");
            if(!daysInCloud.getVersion().equals(daysInDB.getVersion())){
                updateOrSaveInDB(daysInCloud);
                updateOrSaveInCloud(daysInDB);
            }
        }

    }

    /**
     * 同步地从云端获取当前宝宝的统计信息
     */
    public Statistics findFromCloudByCurrentBaby() {
        query = AVQuery.getQuery(Statistics.class);
        query.whereEqualTo(Statistics.BABY_KEY, App.getCurrentBaby());
        try {
            List<Statistics> statisticses = query.find();
            if (statisticses.size() > 0)
                return statisticses.get(0);
            else
                return null;
        } catch (AVException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 异步地从云端获取当前宝宝的统计信息
     * @param callback
     */
    public void findFromCloudByCurrentBaby(final FindCallback callback){
        query = AVQuery.getQuery(Statistics.class);
        query.whereEqualTo(Statistics.BABY_KEY, App.getCurrentUser());
        query.findInBackground(callback);
    }

    /**
     * 同步地从本地获取当前宝宝的统计信息
     * @return 当前宝宝的统计信息
     */
    public Statistics findFromDBByCurrentBaby(){
        List<Statistics> statisticses = new ArrayList<>();
        final String compStr =  App.getCurrentBaby().getObjectId();
        final String whereClause = DBHelper.COLUMN_COMP + " = ?";
        final String[] whereArgs = new String[]{compStr};
        Cursor cursor = this.db.query(
                Statistics.STATISTICS_CLASS,
                new String[]{DBHelper.COLUMN_JSON},
                whereClause,
                whereArgs,
                null,
                null,
                null,
                null
        );
        while (cursor.moveToNext()) {
            String json = cursor.getString(cursor.getColumnIndex(
                    DBHelper.COLUMN_JSON
            ));
            json=json.replace("_","");
            Statistics statistics = JSON.parseObject(json, Statistics.class);
            statisticses.add(statistics);
        }
        cursor.close();
        return statisticses.isEmpty()?null:statisticses.get(0);
    }

    /**
     * 异步地从本地获取当前宝宝的统计信息
     * @param callback 回调接口
     */
    public void findFromDBByCurrentBaby(
            final DBFindCallback<Statistics> callback){
        final String compStr =  App.getCurrentBaby().getObjectId();
        final String whereClause = DBHelper.COLUMN_COMP + "=?";
        final String[] whereArgs = new String[]{compStr};
        final List<Statistics> statisticses = new ArrayList<>();
        DBFindTask task = new DBFindTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                Cursor cursor = StatisticsDao.this.db.query(
                        Statistics.STATISTICS_CLASS,
                        null,
                        whereClause,
                        whereArgs,
                        null, null, null);
                while (cursor.moveToNext()) {
                    cursor.move(0);
                    String json = cursor.getString(cursor.getColumnIndex(
                            DBHelper.COLUMN_JSON
                    ));
                    json=json.replace("_","");
                    Statistics statistics = JSON.parseObject(json, Statistics.class);
                    statisticses.add(statistics);
                }
                cursor.close();
                return super.doInBackground(params);
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                callback.done(statisticses);
            }
        };
        task.execute();
    }


    public void findAllFromCloud(int limit, FindCallback findCallback) {
        // super.findAllFromCloud(limit, findCallback);

    }


    public <T extends Base> List<T> findAllFromCloud(int limit) {
        return null;
    }


    public void updateOrSaveInCloud(Statistics statistics) throws AVException {
        Statistics old = findFromCloudByCurrentBaby();
        if (old == null) {
            //不存在则新建
            statistics.save();
        } else {
            refresh(statistics, old);
            //云端更新
            old.save();
        }
    }

    @Override
    public void updateOrSaveInCloud(Base oneDay, FindCallback callback) {
        super.updateOrSaveInCloud(oneDay, callback);
    }


    public <T extends Base> List<T> findAllFromD(int limit) {
        return null;
    }


    public void updateOrSaveInDB(Statistics statistics) {
        String COMP = statistics.getBaby().getObjectId();
        String whereClause = DBHelper.COLUMN_COMP + "=?";
        String[] whereArgs = {COMP};
        String saveJson = JSONHelper.getValidJSON(statistics.toJSONObject().toString());
        Statistics old = findFromDBByCurrentBaby();
        if (old != null) {
            refresh(statistics,old);
            String oldJSON = JSONHelper.getValidJSON(old.toJSONObject().toString());
            //保存更新当前版本标识
            ContentValues cv = new ContentValues();
            cv.put(DBHelper.COLUMN_JSON, oldJSON);
            cv.put(DBHelper.COLUMN_VERSION, old.getVersion());
            db.update(Statistics.STATISTICS_CLASS, cv, whereClause, whereArgs);
        } else {
            //否则直接插入
            dbHelper.insertToJson(
                    Statistics.STATISTICS_CLASS,
                    saveJson,
                    statistics.getVersion(),
                    COMP
            );
        }
    }
    private void refresh(Statistics newOne,Statistics oldOne){
        SimpleDateFormat sdf = new SimpleDateFormat(Statistics.VERSION_FORMAT);
        Date newVersion=null;
        Date oldVersion=null;
        if (oldOne.getVersion().equals(newOne.getVersion()))
            return;
        try {
            newVersion = sdf.parse(newOne.getVersion());
            oldVersion = sdf.parse(oldOne.getVersion());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert oldOne != null;
        // 更新版本
        if(newVersion.after(oldVersion)){
            int high_temperature=newOne.getHighTemperatureNum()>oldOne.getHighTemperatureNum()?
                    newOne.getHighTemperatureNum():oldOne.getHighTemperatureNum();
            int low_temperature=newOne.getLowTemperatureNum()>oldOne.getLowTemperatureNum()?
                    newOne.getLowTemperatureNum():oldOne.getLowTemperatureNum();
            int overtime_num=newOne.getOvertimeNum()>oldOne.getOvertimeNum()?newOne.getOvertimeNum():oldOne.getOvertimeNum();
            oldOne.setHighTemperatureNum(high_temperature);
            oldOne.setLowTemperatureNum(low_temperature);
            oldOne.setOvertimeNum(overtime_num);
            oldOne.setVersion(newOne.getVersion());
        }
    }
}
