package com.inhand.milk.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.inhand.milk.App;
import com.inhand.milk.entity.Base;
import com.inhand.milk.entity.Device;
import com.inhand.milk.entity.OneDay;
import com.inhand.milk.entity.User;
import com.inhand.milk.helper.DBHelper;
import com.inhand.milk.helper.JSONHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * DeviceDao
 * Desc: 设备访问层
 * Date: 2015/6/3
 * Time: 7:23
 * Created by: Wooxxx
 */
public class DeviceDao
        extends BaseDao<Device>{

    public DeviceDao(Context ctx) {
        super(ctx);
    }

    /**
     * 同步地从云端获取当前用户的设备
     * @return 当前用户的设别
     */
    public Device findFromCloudByCurrentUser() {
        //Log.d("saveCloud1","ok");
        query = AVQuery.getQuery(Device.class);
        query.whereEqualTo(Device.USER_KEY, App.getCurrentUser());
        try {
            List<Device> devices = query.find();
            //Log.d("devices",devices.get(0).getMac());
            return devices.isEmpty()?null:devices.get(0);
        } catch (AVException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 异步地从云端获取当前用户的设备信息
     * @param callback 回调接口
     */
    public void findFromCloudByCurrentUser(
            final FindCallback<Device> callback){
        query = AVQuery.getQuery(Device.class);
        query.whereEqualTo(Device.USER_KEY, App.getCurrentUser());
        query.findInBackground(callback);
    }

    /**
     * 异步地从本地获取当前用户的设备信息
     * @param callback 查询回调接口
     */
    public void findFromDBByCurrentUser(
            final DBFindCallback<Device> callback
    ){
        final String compStr =  App.getCurrentUser().getObjectId();
        final String whereClause = DBHelper.COLUMN_COMP + "=?";
        final String[] whereArgs = new String[]{compStr};
        final List<Device> devices = new ArrayList<>();
        DBFindTask task = new DBFindTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                Cursor cursor = DeviceDao.this.db.query(
                        OneDay.ONEDAY_CLASS,
                        null,
                        whereClause,
                        whereArgs,
                        null, null, null);
                while (cursor.moveToNext()) {
                    String json = cursor.getString(cursor.getColumnIndex(
                            DBHelper.COLUMN_JSON
                    ));
                    json=json.replace("_","");
                    Device device = JSON.parseObject(json, Device.class);
                    devices.add(device);
                }
                cursor.close();
                return super.doInBackground(params);
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                callback.done(devices);
            }
        };
        task.execute();
    }

    /**
     * 同步地从本地获取当前用户的设备信息
     *
     */
    public Device findFromDBByCurrentUser(){
        List<Device> devices = new ArrayList<>();
        final String compStr = App.getCurrentUser().getObjectId();
        String whereClause = DBHelper.COLUMN_COMP + "=?";
        String[] whereArgs = {compStr};
        Cursor cursor = DeviceDao.this.db.query(
                Device.DEVICE_CLASS,
                null,
                null,
                null,
                null, null, null);
        while (cursor.moveToNext()) {
            String json = cursor.getString(cursor.getColumnIndex(
                    DBHelper.COLUMN_JSON
            ));
            json=json.replace("_","");
            Device device = JSON.parseObject(json, Device.class);
            if(device.getUser().equals(App.getCurrentUser()))
                devices.add(device);
        }
        cursor.close();
        //Log.d("devices",devices.get(0).getSoftwareVersion());
        return devices.isEmpty()?null:devices.get(0);
    }

    /**
     * 同步地从本地获取当前用户的设备信息
     *
     */
    public Device findFromDBByMAC(String MAC){
        final String compStr = MAC;
        String whereClause = DBHelper.COLUMN_COMP + "=?";
        String[] whereArgs = {compStr};
        Cursor cursor = DeviceDao.this.db.query(
                Device.DEVICE_CLASS,
                null,
                whereClause,
                whereArgs,
                null, null, null);
        if (cursor.moveToNext()) {
            cursor.move(0);
            String json = cursor.getString(cursor.getColumnIndex(
                    DBHelper.COLUMN_JSON
            ));
            return JSON.parseObject(json, Device.class);
        }
        cursor.close();
        return null;
    }

    /**
     * 同步地从本地根据MAC地址获得设备
     * @param MAC MAC地址
     * @return 设备对象
     */
    public void findFromDBByMAC(
            String MAC,
            final DBFindCallback<Device> callback){
        final String compStr = MAC;
        final String whereClause = DBHelper.COLUMN_COMP + "=?";
        final String[] whereArgs = new String[]{compStr};
        final List<Device> devices = new ArrayList<>();
        DBFindTask task = new DBFindTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                Cursor cursor = DeviceDao.this.db.query(
                        Device.DEVICE_CLASS,
                        null,
                        whereClause,
                        whereArgs,
                        null, null, null);
                if (cursor.moveToNext()) {
                    cursor.move(0);
                    String json = cursor.getString(cursor.getColumnIndex(
                            DBHelper.COLUMN_JSON
                    ));
                    Device device = JSON.parseObject(json, Device.class);
                    devices.add(device);
                }
                cursor.close();
                return super.doInBackground(params);
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                callback.done(devices);
            }
        };
        task.execute();


    }


    /**
     * 同步地从本地根据MAC地址获得设备
     * @param MAC MAC地址
     * @return 设备对象
     */
    public Device findFromCloudByMAC(String MAC){
        query = AVQuery.getQuery(Device.class);
        query.whereEqualTo(Device.MAC_KEY, MAC);
        try {
            List<Device> devicess = query.find();
            if (devicess.size() > 0)
                return devicess.get(0);
            else
                return null;
        } catch (AVException e) {
            e.printStackTrace();
            return null;
        }

    }


    /**
     * 异步地根据MAC地址从本地获取设备
     * @param MAC mac地址
     * @param callback 回调接口
     */
    public void findFromCloudByMAC(
            String MAC,
            FindCallback callback){
        query = AVQuery.getQuery(Device.class);
        query.whereEqualTo(OneDay.BABY_KEY, App.getCurrentBaby());
        query.whereEqualTo(OneDay.DATE_KEY, MAC);
        query.findInBackground(callback);

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
        query = AVQuery.getQuery(Device.class);
        //从云端抓取所有
        Device daysInCloud = findFromCloudByCurrentUser();

        //从本地抓取所有
        Device daysInDB = findFromDBByCurrentUser();
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
     * 在云端更新或存储记录
     *
     * @param device
     */
    public void updateOrSaveInCloud(Device device) throws AVException {

        Device old = findFromCloudByMAC(device.getMac());
        if (old == null) {
            //不存在则新建
            device.save();
        } else {
            refresh(device, old);
            //云端更新
            old.save();
        }
    }

    @Override
    public void updateOrSaveInDB(Device device) {
        // 比较字段为设备地址
        String MAC = device.getMac();
        String whereClause = DBHelper.COLUMN_COMP + "=?";
        String[] whereArgs = {MAC};
        String saveJson = JSONHelper.getValidJSON(device.toJSONObject().toString());
        Device old = findFromDBByMAC(MAC);
        // 如果已存在且版本不一致,则更新
        if (old != null) {
            refresh(device,old);
            String oldJSON = JSONHelper.getValidJSON(old.toJSONObject().toString());
            //保存更新当前版本标识
            ContentValues cv = new ContentValues();
            cv.put(DBHelper.COLUMN_JSON, oldJSON);
            cv.put(DBHelper.COLUMN_VERSION, old.getVersion());
            db.update(Device.DEVICE_CLASS, cv, whereClause, whereArgs);
        } else {
            //否则直接插入
            dbHelper.insertToJson(
                    Device.DEVICE_CLASS,
                    saveJson,
                    device.getVersion(),
                    MAC
            );
        }
    }


    //刷新设备信息
    private void refresh(Device newOne,Device oldOne){
        SimpleDateFormat sdf = new SimpleDateFormat(Device.VERSION_FORMAT);
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
            oldOne.setSoftwareVersion(newOne.getSoftwareVersion());
            oldOne.setHardwareVersion(newOne.getHardwareVersion());
            oldOne.setAccelerateErrorNum(newOne.getAccelerateErrorNum());
            oldOne.setAdjustNum(newOne.getAdjustNum());
            oldOne.setPressureErrorNum(newOne.getPressureErrorNum());
            oldOne.setAdjustDeviation(newOne.getAdjustDeviation());
            oldOne.setTemperatureErrorNum(newOne.getTemperatureErrorNum());
            oldOne.setVersion(newOne.getVersion());
        }
    }
}
