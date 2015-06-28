package com.inhand.milk.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.inhand.milk.entity.OneDay;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * DataBaseHelper
 * Desc:SQLite数据库帮助类
 * Team: InHand
 * User:Wooxxx
 * Date: 2015-03-03
 * Time: 15:55
 */
public class DBHelper extends SQLiteOpenHelper {
    //原子计数器,用以安全地关闭数据库链接
    private AtomicInteger openCounter = new AtomicInteger();
    //数据库名
    private static final String DB_NAME = "milk.db";
    //版本维护
    private static final int version = 1;
    //每张表只包含VERSION及JSON列
    public static final String COLUMN_JSON = "json";
    public static final String COLUMN_VERSION = "version";
    //比较位置，方便比较
    public static final String COLUMN_COMP = "comp";
    //数据表列表
    private static final String[] TB_NAMES = new String[]{
            OneDay.ONEDAY_CLASS
    };
    //数据库对象
    private SQLiteDatabase db;
    private static DBHelper instance = null;

    //双重检查加锁实例化单例
    public static DBHelper getInstance(Context ctx) {
        if (instance == null) {//第一次检查
            synchronized (DBHelper.class) {
                if (instance == null) {
                    instance = new DBHelper(ctx);
                }
            }
        }
        return instance;
    }

    private DBHelper(Context context) {
        //根据当前宝宝动态指定对应数据库
        super(context, DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*
        创建数据表
         */
        for (String table : TB_NAMES) {
            String sql = "CREATE TABLE IF NOT EXISTS " + table
                    + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_JSON + " TEXT," + COLUMN_VERSION + " TEXT," + COLUMN_COMP + " TEXT)";
            db.execSQL(sql);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public synchronized SQLiteDatabase openDatabase() {
        if (openCounter.incrementAndGet() == 1) {
            // Opening new database
            db = instance.getReadableDatabase();
        }
        return db;
    }

    public synchronized void closeDatabase() {
        if (openCounter.decrementAndGet() == 0) {
            // Closing database
            db.close();
        }
    }

    /**
     * 插入方法
     *
     * @param table 表名
     * @param obj   待插入对象
     */
    public void insertToJson(String table, String obj, String version, String comp) {
        SQLiteDatabase db = this.openDatabase();
        db.execSQL("INSERT INTO " + table + " VALUES (NULL, ?, ?, ?)",
                new Object[]{obj, version, comp});
        this.closeDatabase();
    }


    /**
     * 获得所有行
     *
     * @param table 表名
     * @return 所有项目列表
     */
    public ArrayList<String> findAll(String table) {
        ArrayList<String> list = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(table, null, null, null, null, null, null);
        if (cursor != null) {
            list = new ArrayList<>();
            while (cursor.moveToNext()) {
                String json = cursor.getString(
                        cursor.getColumnIndex(COLUMN_JSON)
                );
                list.add(json);
            }
        }
        return list;
    }
}
