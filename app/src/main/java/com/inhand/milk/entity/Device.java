package com.inhand.milk.entity;

import android.content.Context;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.inhand.milk.App;
import com.inhand.milk.utils.ACache;
import com.inhand.milk.utils.LocalSaveTask;

/**
 * Device
 * Desc: 设备实体
 * Date: 2015/6/2
 * Time: 23:43
 * Created by: Wooxxx
 */
@AVClassName(Device.DEVICE_CLASS)
public class Device extends Base implements CacheSaving<Device> {
    public static final String DEVICE_CLASS = "Milk_Device";

    public static final String SOFTWARE_VERSION_KEY = "softwareVersion"; //软件版本
    public static final String HARDWARE_VERSION_KEY = "hardwareVersion"; // 硬件版本
    public static final String ADJUST_NUM_KEY = "adjustNum"; // 校准次数
    public static final String ADJUST_DEVIATION_KEY = "adjustDeviation"; // 校准误差
    public static final String MAC_KEY = "mac"; // mac地址
    public static final String PRESSURE_ERROR_NUM_KEY = "pressureErrorNum"; // 压力传感器错误次数
    public static final String TEMPERATURE_ERROR_NUM_KEY = "temperatureErrorNum"; // 温度传感器错误次数
    public static final String ACCELERATE_ERROR_NUM_KEY = "accelerateErrorNum"; // 加速度传感器错误次数
    public static final String USER_KEY = "user"; // 所属用户
    public static final String UUID_KEY = "uuid"; // UUID
    public static final String NAME_KEY = "name"; // 自定义设备名称

    public static final String CACHE_KEY = "device";

    public Device() {

    }

    /**
     * 获得设备Mac地址
     *
     * @return MAC地址
     */
    public String getMac() {
        return this.getString(MAC_KEY);
    }

    /**
     * 设置设备Mac地址
     *
     * @param mac mac地址
     */
    public void setMac(String mac) {
        this.put(MAC_KEY, mac);
    }

    /**
     * 获得设备软件版本
     *
     * @return 软件版本
     */
    public String getSoftwareVersion() {
        return this.getString(SOFTWARE_VERSION_KEY);
    }

    /**
     * 设置设备软件版本
     *
     * @param softwareVersion 软甲版本
     */
    public void setSoftwareVersion(String softwareVersion) {
        this.put(SOFTWARE_VERSION_KEY, softwareVersion);
    }

    /**
     * 获得设备硬件版本
     *
     * @return 设备硬件版本
     */
    public String getHardwareVersion() {
        return this.getString(HARDWARE_VERSION_KEY);
    }

    /**
     * 设置设备硬件版本
     *
     * @param hardwareVersion 设备硬件版本
     */
    public void setHardwareVersion(String hardwareVersion) {
        this.put(HARDWARE_VERSION_KEY, hardwareVersion);
    }

    /**
     * 获得校准次数
     *
     * @return 校准次数
     */
    public int getAdjustNum() {
        return this.getInt(ADJUST_NUM_KEY);
    }

    /**
     * 设置校准次数
     *
     * @param adjustNum 校准次数
     */
    public void setAdjustNum(int adjustNum) {
        this.put(ADJUST_NUM_KEY, adjustNum);
    }

    /**
     * 获得校准差
     *
     * @return 校准差
     */
    public double getAdjustDeviation() {
        return this.getDouble(ADJUST_DEVIATION_KEY);
    }

    /**
     * 设置校准误差
     *
     * @param adjustNumDeviation 校准误差
     */
    public void setAdjustDeviation(double adjustNumDeviation) {
        this.put(ADJUST_DEVIATION_KEY, adjustNumDeviation);
    }

    /**
     * 获得压力传感错误次数
     *
     * @return 压力传感错误次数
     */
    public int getPressureErrorNum() {
        return this.getInt(PRESSURE_ERROR_NUM_KEY);
    }

    /**
     * 设置压力传感器错误次数
     *
     * @param pressureErrorNum 压力传感器错误次数
     */
    public void setPressureErrorNum(int pressureErrorNum) {
        this.put(PRESSURE_ERROR_NUM_KEY, pressureErrorNum);
    }

    /**
     * 获得温度错误次数
     *
     * @return 温度错误次数
     */
    public int getTemperatureErrorNum() {
        return this.getInt(TEMPERATURE_ERROR_NUM_KEY);
    }

    /**
     * 设置温度传感器错误次数
     *
     * @param temperatureErrorNum 温度传感器错误次数
     */
    public void setTemperatureErrorNum(int temperatureErrorNum) {
        this.put(TEMPERATURE_ERROR_NUM_KEY, temperatureErrorNum);
    }

    /**
     * 获得加速度模块错误次数
     *
     * @return 加速度模块错误次数
     */
    public int getAccelerateErrorNum() {
        return this.getInt(ACCELERATE_ERROR_NUM_KEY);
    }

    /**
     * 设置加速度模块错误次数
     *
     * @param accelerateErrorNum 加速度模块错误次数
     */
    public void setAccelerateErrorNum(int accelerateErrorNum) {
        this.put(ACCELERATE_ERROR_NUM_KEY, accelerateErrorNum);
    }


    /**
     * 获得设备所属用户
     *
     * @return
     */
    public User getUser() {
        return this.getAVUser(USER_KEY, User.class);
    }

    /**
     * 设置设备对应用户
     *
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
     * 获得设备UUID
     *
     * @return 设备UUID
     */
    public String getUUID() {
        return this.getString("uuid");
    }

    /**
     * 设置设备UUID
     *
     * @param uuid 设备
     */
    public void setUUID(String uuid) {
        this.put(UUID_KEY, uuid);
    }

    /**
     * 获得设备名称
     *
     * @return 设备名称
     */
    public String getName() {
        return this.getString(NAME_KEY);
    }

    /**
     * 设置设备名称
     *
     * @param name 设备名称
     */
    public void setName(String name) {
        this.put(NAME_KEY, name);
    }

    /**
     * 保存设备信息至云端，覆盖式存储
     *
     * @param callback 存储回调接口
     */
    public void saveInCloud(final SaveCallback callback) {
        if (this.getUser() == null)
            this.setUser(App.getCurrentUser());
        this.saveInBackground(callback);
    }

    @Override
    public void saveInCache(final Context ctx, LocalSaveTask.LocalSaveCallback<Device> callback) {

        LocalSaveTask task = new LocalSaveTask(callback) {
            @Override
            protected Void doInBackground(Void... voids) {
                saveInCache(ctx);
                return super.doInBackground(voids);
            }
        };

        task.execute();

    }

    @Override
    public void saveInCache(Context ctx) {
        if (this.getUser() == null)
            this.setUser(App.getCurrentUser());
        ACache aCache = ACache.get(ctx);
        aCache.put(Device.CACHE_KEY, this.toJSONObject());
    }
}
