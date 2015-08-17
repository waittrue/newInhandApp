package com.inhand.milk.dao;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.inhand.milk.App;
import com.inhand.milk.entity.OneDay;
import com.inhand.milk.entity.Record;
import com.inhand.milk.helper.DBHelper;
import com.inhand.milk.helper.JSONHelper;
import com.inhand.milk.utils.Calculator;
import com.inhand.milk.utils.LocalFindTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * OneDayDao
 * Desc: 数据访问层--日饮奶记录
 * Team: InHand
 * User: Wooxxx
 * Date: 2015-03-05
 * Time: 15:40
 */
public class OneDayDao {
    public static final int FIND_LIMIT_LATEST = 1; // 获得最近一天的
    public static final int FIND_LIMIT_ALL = 0;
    private static final String SORT_BY = "createdAt";
    private static final String RECORDS_COMP_FORMAT = "HH:mm";
    protected DBHelper dbHelper;
    protected SQLiteDatabase db;
    private AVQuery<OneDay> query = AVQuery.getQuery(OneDay.class);

    /**
     * 异步地根据从云端获取所有记录
     *
     * @param limit    最大条数
     *                 FIND_LIMIT_LATEST:返回最近一条
     *                 FIND_LIMIT_ALL:返回所有
     * @param callback 回调函数
     */
    public void findFromCloud(final int limit, final FindCallback<OneDay> callback) {
        // 按照更新时间降序排序
        query.whereEqualTo(OneDay.BABY_KEY, App.getCurrentBaby());
        query.orderByAscending(SORT_BY);
        // 最大返回1000条
        if (limit > 0)
            query.limit(limit);
        query.findInBackground(callback);
    }

    /**
     * 异步地根据日期从云端获取所有记录
     *
     * @param limit 最大条数
     *              FIND_LIMIT_LATEST:返回最近一条
     *              FIND_LIMIT_ALL:返回所有
     */
    public List<OneDay> findFromCloud(final int limit) {
        List<OneDay> oneDays = new ArrayList<>();
        query.whereEqualTo(OneDay.BABY_KEY, App.getCurrentBaby());
        query.orderByDescending(SORT_BY);
        if (limit > 0)
            query.limit(0);
        try {
            oneDays = query.find();
        } catch (AVException e) {
            e.printStackTrace();
        }
        return oneDays;
    }

    /**
     * 异步地根据日期从云端获取某日记录
     *
     * @param date     查询日期
     * @param callback 回调函数
     */
    public void findOneFromCloud(final String date, final FindCallback<OneDay> callback) {
        query.whereEqualTo(OneDay.BABY_KEY, App.getCurrentBaby());
        query.whereEqualTo(OneDay.DATE_KEY, date);
        query.findInBackground(callback);
    }

    /**
     * 同步地根据日期从云端获取某日记录
     *
     * @param date 查询日期
     * @return 查询到的对象
     */
    public OneDay findOneFromCloud(final String date) {
        query.whereEqualTo(OneDay.BABY_KEY, App.getCurrentBaby());
        query.whereEqualTo(OneDay.DATE_KEY, date);
        try {
            List<OneDay> days = query.find();
            if (days.size() > 0)
                return days.get(0);
            else
                return null;
        } catch (AVException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 在云端更新或存储记录
     *
     * @param oneDay 待更新记录
     */
    public void updateOrSaveInCloud(OneDay oneDay) throws AVException {
        OneDay old = findOneFromCloud(oneDay.getDate());
        if (old == null) {
            //不存在则新建
            oneDay.save();
        } else {
            merge(oneDay, old);
            //云端更新
            old.save();
        }
    }

    /**
     * 同步地从数据库获得当前宝宝所有喝奶记录
     *
     * @param ctx   上下文环境
     * @param limit 限制条数
     *              FIND_LIMIT_LATEST:返回最近一条
     *              FIND_LIMIT_ALL:返回所有
     * @return oneday记录
     */
    public List<OneDay> findFromDB(final Context ctx, final int limit) {
        dbHelper = DBHelper.getInstance(ctx);
        db = dbHelper.openDatabase();
        List<OneDay> oneDays = new ArrayList<>();
        String count = limit > 0 ? "0," + String.valueOf(limit) : null;
        final String whereClause = DBHelper.COLUMN_COMP + " like ?";
        final String[] whereArgs = new String[]{"%" + App.getCurrentBaby().getObjectId() + "%"};
        String orderBy = "_id desc";
        Cursor cursor = this.db.query(
                OneDay.ONEDAY_CLASS,
                new String[]{DBHelper.COLUMN_JSON},
                whereClause,
                whereArgs,
                null,
                null,
                orderBy,
                count
        );
        while (cursor.moveToNext()) {
            String json = cursor.getString(cursor.getColumnIndex(
                    DBHelper.COLUMN_JSON
            ));
            OneDay oneDay = JSON.parseObject(json, OneDay.class);
            oneDays.add(oneDay);
        }
        cursor.close();
        //db.close();
        dbHelper.closeDatabase();
        return oneDays;
    }


    /**
     * 异步地从数据库获得当前宝宝所有喝奶记录
     *
     * @param ctx      上下文环境
     * @param limit    限制条数
     *                 FIND_LIMIT_LATEST:返回最近一条
     *                 FIND_LIMIT_ALL:返回所有
     * @param callback 查询回调接口
     */
    public void fidnFromDB(final Context ctx, final int limit, final LocalFindTask.LocalFindCallback<OneDay> callback) {
        dbHelper = DBHelper.getInstance(ctx);
        final List<OneDay> oneDays = new ArrayList<>();
        final String count = limit > 0 ? "0," + String.valueOf(limit) : null;
        final String whereClause = DBHelper.COLUMN_COMP + " like ?";
        final String[] whereArgs = new String[]{"%" + App.getCurrentBaby().getObjectId() + "%"};
        final String orderBy = "_id desc";
        LocalFindTask task = new LocalFindTask() {
            @Override
            protected Void doInBackground(Void... voids) {
                db = dbHelper.openDatabase();
                Cursor cursor = OneDayDao.this.db.query(
                        OneDay.ONEDAY_CLASS,
                        new String[]{DBHelper.COLUMN_JSON},
                        whereClause,
                        whereArgs,
                        null,
                        null,
                        orderBy,
                        count
                );
                while (cursor.moveToNext()) {
                    String json = cursor.getString(cursor.getColumnIndex(
                            DBHelper.COLUMN_JSON
                    ));
                    OneDay oneDay = JSON.parseObject(json, OneDay.class);
                    oneDays.add(oneDay);
                }
                cursor.close();
                //db.close();
                dbHelper.closeDatabase();
                return super.doInBackground(voids);
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                callback.done(oneDays);
            }

        };
        task.execute();
    }

    /**
     * 根据日期异步地从数据库中获取某日喝奶数据
     *
     * @param date     日期
     * @param callback 回调接口
     */
    public void findOneFromDB(final Context ctx, final String date, final LocalFindTask.LocalFindCallback<OneDay> callback) {
        dbHelper = DBHelper.getInstance(ctx);
        final String compStr = date + ":" + App.getCurrentBaby().getObjectId();
        final String whereClause = DBHelper.COLUMN_COMP + "=?";
        final String[] whereArgs = new String[]{compStr};
        final List<OneDay> days = new ArrayList<>();
        LocalFindTask task = new LocalFindTask() {
            @Override
            protected Void doInBackground(Void... voids) {
                db = dbHelper.openDatabase();
                Cursor cursor = OneDayDao.this.db.query(
                        OneDay.ONEDAY_CLASS,
                        null,
                        whereClause,
                        whereArgs,
                        null, null, null);
                if (cursor.moveToNext()) {
                    cursor.move(0);
                    String json = cursor.getString(cursor.getColumnIndex(
                            DBHelper.COLUMN_JSON
                    ));
                    OneDay oneDay = JSON.parseObject(json, OneDay.class);
                    days.add(oneDay);
                }
                cursor.close();
                //db.close();
                dbHelper.closeDatabase();
                return super.doInBackground(voids);
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                callback.done(days);
            }
        };
        task.execute();
    }

    /**
     * 根据日期同步地从本地拿取某日喝奶记录
     *
     * @param ctx  上下文环境
     * @param date 查询的日期
     * @return 找到的OneDay对象
     */
    public OneDay findOneFromDB(final Context ctx, final String date) {
        dbHelper = DBHelper.getInstance(ctx);
        db = dbHelper.openDatabase();
        final String compStr = date + ":"
                + App.getCurrentBaby().getObjectId();
        String whereClause = DBHelper.COLUMN_COMP + "=?";
        String[] whereArgs = {compStr};
        Cursor cursor = OneDayDao.this.db.query(
                OneDay.ONEDAY_CLASS,
                null,
                whereClause,
                whereArgs,
                null, null, null);
        if (cursor.moveToNext()) {
            cursor.move(0);
            String json = cursor.getString(cursor.getColumnIndex(
                    DBHelper.COLUMN_JSON
            ));
            return JSON.parseObject(json, OneDay.class);
        }
        cursor.close();
        //db.close();
        dbHelper.closeDatabase();
        return null;
    }


    /**
     * 同步地从本地获得两天之间的所有记录
     *
     * @param ctx   上下文环境
     * @param start 开始日期
     * @param end   结束日期
     * @return 该期间内的天数
     */
    public List<OneDay> findBetween(final Context ctx, final String start, final String end) {
        dbHelper = DBHelper.getInstance(ctx);
        db = dbHelper.openDatabase();
        List<OneDay> oneDays = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat(OneDay.DATE_FORMAT);
        Cursor cursor = OneDayDao.this.db.query(
                OneDay.ONEDAY_CLASS,
                null,
                null,
                null,
                null, null, null);
        // 逐条遍历
        while (cursor.moveToNext()) {
            //根据列名获取列索引
            int compColIndex = cursor.getColumnIndex(DBHelper.COLUMN_COMP);
            String comStr = cursor.getString(compColIndex);
            String[] arr = comStr.split(":");
            String date = arr[0];
            String baby = arr[1];
            // 如果是当前宝宝的记录，且处于两个日期之间
            if (baby.equals(App.getCurrentBaby().getObjectId())) {
                if (isOver(date, end))
                    break;
                else if (isBetween(date, start, end)) {
                    String json = cursor.getString(cursor.getColumnIndex(
                            DBHelper.COLUMN_JSON
                    ));
                    OneDay oneDay = JSON.parseObject(json, OneDay.class);
                    oneDays.add(oneDay);
                }

            }
        }
        //db.close();
        dbHelper.closeDatabase();
        cursor.close();
        return oneDays;
    }

    /**
     * 异步地从本地获得两天之间的记录
     *
     * @param ctx      上下文环境
     * @param start    开始日期
     * @param end      结束日期
     * @param callback 回调函数
     */
    public void findBetween(final Context ctx, final String start, final String end, final LocalFindTask.LocalFindCallback<OneDay> callback) {
        dbHelper = DBHelper.getInstance(ctx);
        final List<OneDay> oneDays = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat(OneDay.DATE_FORMAT);
        LocalFindTask task = new LocalFindTask() {
            @Override
            protected Void doInBackground(Void... voids) {
                db = dbHelper.openDatabase();
                Cursor cursor = OneDayDao.this.db.query(
                        OneDay.ONEDAY_CLASS,
                        null,
                        null,
                        null,
                        null, null, null);
                // 逐条遍历
                while (cursor.moveToNext()) {
                    //根据列名获取列索引
                    int compColIndex = cursor.getColumnIndex(DBHelper.COLUMN_COMP);
                    String comStr = cursor.getString(compColIndex);
                    String[] arr = comStr.split(":");
                    String date = arr[0];
                    String baby = arr[1];
                    // 如果是当前宝宝的记录，且处于两个日期之间
                    if (baby.equals(App.getCurrentBaby().getObjectId())) {
                        if (isOver(date, end))
                            break;
                        else if (isBetween(date, start, end)) {
                            String json = cursor.getString(cursor.getColumnIndex(
                                    DBHelper.COLUMN_JSON
                            ));
                            OneDay oneDay = JSON.parseObject(json, OneDay.class);
                            oneDays.add(oneDay);
                        }

                    }
                }
                //db.close();
                dbHelper.closeDatabase();
                cursor.close();
                return super.doInBackground(voids);
            }

            @Override
            protected void onPostExecute(Void avoid) {
                super.onPostExecute(avoid);
                callback.done(oneDays);
            }
        };
    }

    /**
     * 异步地从本地分页查询OneDay记录
     *
     * @param ctx      上下文环境
     * @param page     页数（从第0页开始）
     * @param per      每页容量
     * @param callback 回调接口
     */
    public void findFromDB(
            final Context ctx,
            final int page,
            final int per,
            final LocalFindTask.LocalFindCallback<OneDay> callback) {
        dbHelper = DBHelper.getInstance(ctx);
        final List<OneDay> oneDays = new ArrayList<>();
        final String whereClause = DBHelper.COLUMN_COMP + " like ?";
        final String[] whereArgs = new String[]{"%" + App.getCurrentBaby().getObjectId() + "%"};
        final String orderBy = "_id desc";
        LocalFindTask task = new LocalFindTask() {

            @Override
            protected Void doInBackground(Void... voids) {
                db = dbHelper.openDatabase();
                Cursor cursor = OneDayDao.this.db.query(
                        OneDay.ONEDAY_CLASS,
                        new String[]{DBHelper.COLUMN_JSON},
                        whereClause,
                        whereArgs,
                        null,
                        null,
                        orderBy,
                        per * page + "," + per
                );
                while (cursor.moveToNext()) {
                    String json = cursor.getString(cursor.getColumnIndex(
                            DBHelper.COLUMN_JSON
                    ));
                    OneDay oneDay = JSON.parseObject(json, OneDay.class);
                    oneDays.add(oneDay);
                }
                cursor.close();
                //db.close();
                dbHelper.closeDatabase();
                return super.doInBackground(voids);
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                callback.done(oneDays);
            }

        };
        task.execute();
    }

    /**
     * 同步地地从本地分页查询OneDay记录
     *
     * @param ctx  上下文环境
     * @param page 页数（从第0页开始）
     * @param per  每页容量
     */
    public List<OneDay> findFromDB(final Context ctx, final int page, final int per) {
        dbHelper = DBHelper.getInstance(ctx);
        db = dbHelper.openDatabase();
        List<OneDay> oneDays = new ArrayList<>();
        final String whereClause = DBHelper.COLUMN_COMP + " like ?";
        final String[] whereArgs = new String[]{"%" + App.getCurrentBaby().getObjectId() + "%"};
        String orderBy = "_id desc";
        Cursor cursor = this.db.query(
                OneDay.ONEDAY_CLASS,
                new String[]{DBHelper.COLUMN_JSON},
                whereClause,
                whereArgs,
                null,
                null,
                orderBy,
                per * page + "," + per
        );
        while (cursor.moveToNext()) {
            String json = cursor.getString(cursor.getColumnIndex(
                    DBHelper.COLUMN_JSON
            ));
            OneDay oneDay = JSON.parseObject(json, OneDay.class);
            oneDays.add(oneDay);
        }
        cursor.close();
        //db.close();
        dbHelper.closeDatabase();
        return oneDays;
    }

    /**
     * 更新或者储存当天记录至本地，无则更新，有则创建
     *
     * @param ctx    上下文环境
     * @param oneDay 传入的当天记录
     */
    public void updateOrSaveInDB(final Context ctx, OneDay oneDay) {
        dbHelper = DBHelper.getInstance(ctx);
        db = dbHelper.openDatabase();
        String date = oneDay.getDate();
        final String compStr = date + ":"
                + App.getCurrentBaby().getObjectId();
        String whereClause = DBHelper.COLUMN_COMP + "=?";
        String[] whereArgs = {compStr};
        String oneDayJSON = JSONHelper.getValidJSON(oneDay.toJSONObject().toString());
        OneDay old = findOneFromDB(ctx, date);
        // 如果已存在且版本不一致,则更新
        if (old != null) {
            merge(oneDay, old);
            String oldJSON = JSONHelper.getValidJSON(old.toJSONObject().toString());
            //保存跟新当前版本标识
            ContentValues cv = new ContentValues();
            cv.put(DBHelper.COLUMN_JSON, oldJSON);
            cv.put(DBHelper.COLUMN_VERSION, old.getVersion());
            db.update(OneDay.ONEDAY_CLASS, cv, whereClause, whereArgs);
        } else {
            //否则直接插入
            dbHelper.insertToJson(
                    OneDay.ONEDAY_CLASS,
                    oneDayJSON,
                    oneDay.getVersion(),
                    compStr
            );
        }
        //db.close();
        dbHelper.closeDatabase();
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
     *
     * @param ctx 上下文环境
     */
    public void syncCloud(final Context ctx) throws AVException {
        query = AVQuery.getQuery(OneDay.class);
        //从云端抓取所有
        List<OneDay> daysInCloud = findFromCloud(FIND_LIMIT_ALL);

        //从本地抓取所有
        List<OneDay> daysInDB = findFromDB(ctx, FIND_LIMIT_ALL);

        //否则，比较更新
        for (int i = 0; i < daysInCloud.size(); i++) {
            OneDay cloud = daysInCloud.get(i);
            String cloudDate = cloud.getDate();
            String cloudVersion = cloud.getVersion();
            //遍历搜寻本地的
            for (int j = 0; j < daysInDB.size(); j++) {
                OneDay db = daysInDB.get(i);
                String dbDate = db.getDate();
                String dbVersion = db.getVersion();
                //同一天的不同版本需要更新（云端，本地同时需要更新）
                if (dbDate.equals(cloudDate) &&
                        !dbVersion.equals(cloudVersion)) {
                    //保证有效存储
                    db.setObjectId(cloud.getObjectId());
                    //云端并入本地
                    updateOrSaveInDB(ctx, cloud);
                    //本地并入云端
                    updateOrSaveInCloud(db);
                }
                //删去两条记录,防止重复遍历
                daysInDB.remove(db);
                daysInCloud.remove(cloud);
            }
        }
        //如果云端有剩余，更新到本地
        for (OneDay dayInCloud : daysInCloud) {
            updateOrSaveInDB(ctx, dayInCloud);
        }
        //如果本地有剩余，更新到云端
        for (OneDay dayInDB : daysInDB) {
            updateOrSaveInCloud(dayInDB);
        }
    }

    /**
     * 合并两个同日记录
     *
     * @param src 合并对象1
     * @param dst 合并对象2，其中2也作为输出合并的对象
     */
    private void merge(OneDay src, OneDay dst) {
        //进行版本比较后合并
        SimpleDateFormat sdf = new SimpleDateFormat(OneDay.VERSION_FORMAT);
        //以较新版本作为新版本
        String version;
        Date dstDate = null, srcDate = null;
        //如果版本一样，不需要
        if (dst.getVersion().equals(src.getVersion()))
            return;
        try {
            dstDate = sdf.parse(dst.getVersion());
            srcDate = sdf.parse(src.getVersion());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert dstDate != null;
        // 更新版本
        version = dstDate.after(srcDate) ?
                dst.getVersion() : src.getVersion();
        List<Record> records = new ArrayList<>(); // 目标records链表
        List<Record> dstRecords = dst.getRecords();
        //Log.d("合并 dst size", String.valueOf(dstRecords.size()));
        List<Record> srcRecords = src.getRecords();
        //Log.d("合并 src size", String.valueOf(srcRecords.size()));
        // 确定循环次数
        int count = dstRecords.size() > srcRecords.size() ?
                srcRecords.size() : dstRecords.size();
        int i;
        // 两个有序链表合并为一个有序链表
        int j = 0;
        while (dstRecords.size() != 0
                && srcRecords.size() != 0) {
            //Log.d("merge test dst size is", String.valueOf(dstRecords.size()));
            //Log.d("merge test src size is", String.valueOf(srcRecords.size()));
            Record dstRecord = dstRecords.get(0);
            Record srcRecord = srcRecords.get(0);
            if (dstRecord.equals(srcRecord)) {
                records.add(j, dstRecord);
                dstRecords.remove(0);
                srcRecords.remove(0);
            } else {
                SimpleDateFormat compSdf = new SimpleDateFormat(RECORDS_COMP_FORMAT);
                try {
                    Date srcBeginTime = compSdf.parse(srcRecord.getBeginTime());
                    Date dstBeginTime = compSdf.parse(dstRecord.getBeginTime());
                    //追加日期较早的记录
                    if (srcBeginTime.after(dstBeginTime)) {
                        records.add(j, dstRecord);
                        dstRecords.remove(0);
                    } else {
                        records.add(j, srcRecord);
                        srcRecords.remove(0);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            j++;
        }

        //剩余追加
        if (dstRecords.size() > 0) {
            records.addAll(dstRecords.subList(0, dstRecords.size()));
        } else if (srcRecords.size() > 0) {
            records.addAll(srcRecords.subList(0, srcRecords.size()));
        }
        //合并后刷新版本
        dst.setVersion(version);
        dst.setRecords(records);
        //更新分数，奶量
        dst.setScore(src.getScore() + dst.getScore());
        dst.setVolume(Calculator.calcVolume(records));
    }


    /**
     * 判断日期是否处于两日期间
     *
     * @param src   源日期
     * @param start 起始日期
     * @param end   截止日期
     * @return 源日期是否两日期间
     */
    private boolean isBetween(final String src, final String start, final String end) {
        SimpleDateFormat sdf = new SimpleDateFormat(OneDay.DATE_FORMAT);
        Date sdt = null;
        Date edt = null;
        Date dt = null;
        try {
            sdt = sdf.parse(start);
            edt = sdf.parse(end);
            dt = sdf.parse(src);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert dt != null;
        return dt.compareTo(sdt) >= 0
                && dt.compareTo(edt) <= 0;
    }

    /**
     * 判断日期是否过界
     *
     * @param src 源日期
     * @param end 界限日期
     * @return 是否过界
     */
    private boolean isOver(final String src, final String end) {
        SimpleDateFormat sdf = new SimpleDateFormat(OneDay.DATE_FORMAT);
        Date edt = null;
        Date dt = null;
        try {
            edt = sdf.parse(end);
            dt = sdf.parse(src);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert dt != null;
        return dt.compareTo(edt) > 0;
    }

}
