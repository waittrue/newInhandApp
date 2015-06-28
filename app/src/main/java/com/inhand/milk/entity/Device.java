package com.inhand.milk.entity;

import android.content.Context;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.inhand.milk.App;
import com.inhand.milk.dao.DeviceDao;
import com.inhand.milk.dao.OneDayDao;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Device
 * Desc: 设备实体
 * Date: 2015/6/2
 * Time: 23:43
 * Created by: Wooxxx
 */
@AVClassName(Device.DEVICE_CLASS)
public class Device extends Base {
    public static final String DEVICE_CLASS = "Milk_Device";

    public static final String SOFTWARE_VERSION_KEY = "software_version";
    public static final String HARDWARE_VERSION_KEY="hardware_version";
    public static final String ADJUST_NUM_KEY= "adjust_num";
    public static final String ADJUST_DEVIATION_KEY="adjust_deviation";
    public static final String MAC_KEY = "mac";
    public static final String PRESSURE_ERROR_NUM_KEY = "pressure_error_num";
    public static final String TEMPERATURE_ERROR_NUM_KEY = "temperature_error_num";
    public static final String ACCELERATE_ERROR_NUM_KEY = "accelerate_error_num";
    public static final String USER_KEY = "user";
    public static final String VERSION_KEY = "version";

    public Device(){

    }

    /**
     * 获得设备MAC地址
     * @return MAC地址
     */
    public String getMac(){
        return this.getString(MAC_KEY);
    }

    public void setMac(String mac){
        this.put(MAC_KEY,mac);
    }

    /**
     * 获得设备软件版本
     * @return 软件版本
     */
    public String getSoftwareVersion(){
        return this.getString(SOFTWARE_VERSION_KEY);
    }

    public void setSoftwareVersion(String softwareVersion){
        this.put(SOFTWARE_VERSION_KEY,softwareVersion);
    }

    /**
     * 获得设备硬件版本
     * @return 设备硬件版本
     */
    public String getHardwareVersion(){
        return this.getString(HARDWARE_VERSION_KEY);
    }

    public void setHardwareVersion(String hardwareVersion){
        this.put(HARDWARE_VERSION_KEY,hardwareVersion);
    }

    /**
     * 获得校准次数
     * @return 校准次数
     */
    public int getAdjustNum(){
        return this.getInt(ADJUST_NUM_KEY);
    }

    public void setAdjustNum(int adjustNum){
        this.put(ADJUST_NUM_KEY,adjustNum);
    }

    /**
     * 获得校准差
     * @return 校准差
     */
    public double getAdjustDeviation(){
        return this.getDouble(ADJUST_DEVIATION_KEY);
    }

    public void setAdjustDeviation(double adjustNumDeviation){
        this.put(ADJUST_DEVIATION_KEY,adjustNumDeviation);
    }

    /**
     * 获得压力传感错误次数
     * @return 压力传感错误次数
     */
    public int getPressureErrorNum(){
        return this.getInt(PRESSURE_ERROR_NUM_KEY);
    }

    public void setPressureErrorNum(int pressureErrorNum){
        this.put(PRESSURE_ERROR_NUM_KEY,pressureErrorNum);
    }

    /**
     * 获得温度错误次数
     * @return 温度错误次数
     */
    public int getTemperatureErrorNum(){
        return this.getInt(TEMPERATURE_ERROR_NUM_KEY);
    }

    public void setTemperatureErrorNum(int temperatureErrorNum){
        this.put(TEMPERATURE_ERROR_NUM_KEY,temperatureErrorNum);
    }

    /**
     * 获得加速度模块错误次数
     * @return 加速度模块错误次数
     */
    public int getAccelerateErrorNum(){
        return this.getInt(ACCELERATE_ERROR_NUM_KEY);
    }

    public void setAccelerateErrorNum(int accelerateErrorNum){
        this.put(ACCELERATE_ERROR_NUM_KEY,accelerateErrorNum);
    }

    /**
     * 获得当前数据记录版本
     * @return 版本号
     */
    public String getVersion(){
        return this.getString(VERSION_KEY);
    }

    public void setVersion(String version){
        this.put(VERSION_KEY,version);
    }

    public User getUser(){
        return this.getAVUser(USER_KEY,User.class);
    }

    /**
     * 设置设备对应用户
     * @param user 用户
     */
    public void setUser(User user) {
        try {
            this.put(USER_KEY, AVObject.createWithoutData(User.class, user.getObjectId()));
        } catch (AVException e) {
            e.printStackTrace();
        }
    }
    /**
     * 保存设备信息至云端
     * @param callback 存储回调接口
     * @param ctx 上下文环境
     */
    public void saveInCloud(Context ctx,final SaveCallback callback){
        final Device  device = this;
        if( device.getUser() == null )
            device.setUser(App.getCurrentUser());
        //???°汾???
        SimpleDateFormat sdf = new SimpleDateFormat(VERSION_FORMAT);
        String version = sdf.format(new Date());
        device.setVersion(version);
        device.saveInBackground(callback);
    }

    /**
     * 保存设备信息至本地
     * @param ctx 上下文环境
     * @param callback 存储回调接口
     */
    public void saveInDB(final Context ctx,final DBSavingCallback callback){
        if( this.getUser() == null )
            this.setUser(App.getCurrentUser());
        //更新版本表示
        SimpleDateFormat sdf = new SimpleDateFormat(VERSION_FORMAT);
        String version = sdf.format(new Date());
        this.setVersion(version);
        DBSavingTask task = new DBSavingTask(ctx, callback) {
            @Override
            protected Object doInBackground(Object[] params) {
                //更新本地数据库
                DeviceDao deviceDao = new DeviceDao(ctx);
                deviceDao.updateOrSaveInDB(Device.this);
                return null;
            }
        };
        task.execute();
    }
}
