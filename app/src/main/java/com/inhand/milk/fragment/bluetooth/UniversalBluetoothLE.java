package com.inhand.milk.fragment.bluetooth;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SaveCallback;
import com.inhand.milk.App;
import com.inhand.milk.STANDAR.Standar;
import com.inhand.milk.activity.BluetoothPairedAcivity;
import com.inhand.milk.dao.DeviceDao;
import com.inhand.milk.entity.Device;

import java.text.Format;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Administrator on 2015/9/6.
 * 作者：大力
 * 时间：2015/9/6
 * 描述：这个是作为蓝牙4.0低功耗蓝牙的接口，这个类的代码的接口模式，都是照着bluetooth（经典蓝牙的接口）
 * 写的，有很多地方应该是不太符合逻辑的，我主要死为了让写好的接口的代码尽量不改变
 *
 * 注意：我们这里只用一个service中的一个characteristic。
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class UniversalBluetoothLE {

    private static final String TAG = "universalBluetoothLE";
    //UniversalBluetoothLE
    public static UniversalBluetoothLE universalBluetoothLE;

    //BluetoothAdapter
    private BluetoothAdapter mBluetoothAdapter;
    //BluetoothManager
    private BluetoothManager bluetoothManager;
    private  BluetoothData bluetoothData = BluetoothData.getInstance();
    //打开蓝牙的请求码
    public static final int REQUEST_ENABLE_BLUETOOTH = 10010;

    //是否正在扫描蓝牙设备
    private boolean mScanning;
    //设置扫描时长
    private static final long SCAN_PERIOD = 10000;

    //蓝牙扫描的返回
    private BluetoothAdapter.LeScanCallback leScanCallback;
    private BluetoothDiscoverListener bluetoothDiscoverListener;

    //注册在这个里面的所有观察者，到时候通知所有观察者连接状态的改变
    private List<ConnectedChanggedListener> listeners = new ArrayList<>();

    private static final int CONNECTED_STATE_CHANGED = 99; //连接状态

    //默认绑定的device
    private BluetoothDevice paired = null;
    //蓝牙连接的gatt
    private BluetoothGatt mBluetoothGatt = null;

    //用来发放所有的连接状态改变message
    private myHander myHander = new myHander();
    //这个暂时不知道是什么东西，，仿照Android官网里面写下来的。。 但是貌似真的是一些职能设备的心率测试结果
    public final static UUID UUID_HEART_RATE_MEASUREMENT = null;

    private Activity activity = null;
    //目前绑定的服务,目前绑定的character,该服务的uuid，改character的uuid
    private BluetoothGattService mCurrentService = null;
    private BluetoothGattCharacteristic mCurrentCharacteristic = null;
    private static final UUID SERVICE_UUID = UUID.fromString("00001000-0000-1000-8000-00805f9b34fb");
    private static final UUID CHARACTERISTIC_UUID = UUID.fromString("00001002-0000-1000-8000-00805f9b34fb");

    //这个是奶瓶的蓝牙模块每次发送数据都会加的头，我们处理的时候要把这个头去掉
    private static byte[] HEAD = new byte[]{(byte) 0x51,(byte)0x1b};
    private BluetoothGattCallback callback = new BluetoothGattCallback() {
        //在这个回调里面我们不要做处理时间很长的事情
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            Log.i(TAG, "onConnectionStateChange:" + String.valueOf(newState));
            if(newState == BluetoothProfile.STATE_CONNECTED){
                Message message = new Message();
                message.what = CONNECTED_STATE_CHANGED;
                message.arg1 = 1;
                myHander.sendMessage(message);
                if(bluetoothDiscoverListener != null){
                    bluetoothDiscoverListener.pairedSuccess();
                }
                //找出当前连接中的所有服务
                mBluetoothGatt.discoverServices();
            }
            else if(newState == BluetoothProfile.STATE_DISCONNECTED){
                Message message = new Message();
                message.what = CONNECTED_STATE_CHANGED;
                message.arg1 = 0;
                myHander.sendMessage(message);
            }
            super.onConnectionStateChange(gatt, status, newState);
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            //完成绑定我们需要的服务和相关的characteristic
            if( bindService())
                bindCharacteristic();
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            byte[] data = characteristic.getValue();
            //处理data
            StringBuilder builder = new StringBuilder(data.length);
            for(byte byteChar : data)
                builder.append(String.format("%02X ", byteChar));
            Log.i(TAG,"data:"+builder.toString());

            data = handleHead(data);
            if(data == null)
                return;
            if (bluetoothData.saveData(data, data.length) == false) {
                bluetoothData.handleMessage();
                bluetoothData.saveData(data, data.length);
            }

        }


    };

    /**
     * 处理接受到的数据的头部，吧头部去掉
     */
    private byte[]  handleHead(byte[] data){
        if(data == null || data.length <=3)
            return null;
        //减去两个字节的头部， 一个字节代表长度
        byte[] result = new byte[data.length-3];
        for(int i=0 ;i<result.length;i++){
            result[i] = data[i+3];
        }
        return result;
    }
    private UniversalBluetoothLE(Context context) {
        //得到BluetoothManager
        this.bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        //得到BluetoothAdapter
        this.mBluetoothAdapter = bluetoothManager.getAdapter();
        paired = selectFromBonded();
        //蓝牙搜索的回调
        leScanCallback = new BluetoothAdapter.LeScanCallback() {
            @Override
            public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                if(bluetoothDiscoverListener != null) {
                    bluetoothDiscoverListener.discoverFound(device);
                }
            }
        };
    }

    /**
     * 绑定与之关联的Activity
     * @param act
     */
    public void setActivity(Activity act) {
        activity = act;
    }
    /**
     * 这个接口是模仿的经典蓝牙，这是为了少修改代码，我们把这个接口理解成，自动连接。
     */
    public void asClient() {
        if (paired == null) {
            Log.i("paired", "null");
            if (activity != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("配对信息");
                builder.setMessage("你的蓝牙没有绑定配对的奶瓶，请去配对蓝牙");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setClass(activity, BluetoothPairedAcivity.class);
                        activity.startActivity(intent);
                    }
                });
                builder.setNegativeButton("取消", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
            return;
        }
        ShutConnect();
        getConnectGatt(true);
    }

    /**
     * 获得到UniversalBluetoothLE对象
     * @return
     */
    public static UniversalBluetoothLE getInistance() {
        int osVersion;
        try {
            osVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
        }
        catch (NumberFormatException e)
        {
            osVersion = 0;
        }
        if(osVersion < 18)
            return null;
        if (universalBluetoothLE == null) {
            universalBluetoothLE = new UniversalBluetoothLE(App.getAppContext());
        }
        return universalBluetoothLE;
    }

    /**
     * 检查蓝牙是否打开并且启动打开蓝牙的方法
     */
    public void openBlue() {
        //判断蓝牙是否开启
        if (mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled() &&activity != null) {
            //打开蓝牙
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivity(enableIntent);
        }
    }

    /**
     * 开始（true）或结束（false）蓝牙扫描
     * @param enable
     */
    private void scanLeDevice(final boolean enable) {
        if (enable && mScanning == false) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    //在每个stoplescan的地方我们都要调用接口
                    mBluetoothAdapter.stopLeScan(leScanCallback);
                    if(bluetoothDiscoverListener != null){
                        bluetoothDiscoverListener.discoverFinished();
                    }
                }
            }, SCAN_PERIOD);

            mScanning = true;
            //在每个startlescan的地方我们都要调用接口
            mBluetoothAdapter.startLeScan(leScanCallback);
            if(bluetoothDiscoverListener != null){
                bluetoothDiscoverListener.discoverStarted();
            }
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(leScanCallback);
            if(bluetoothDiscoverListener != null){
                bluetoothDiscoverListener.discoverFinished();
            }
        }
    }

    /**停止搜索，
     * 设置我们默认连接的设备
     * @param blueDevice
     */
    public void setPaired(BluetoothDevice blueDevice) {
        scanLeDevice(false);
        paired = blueDevice;
        final Device device = new Device();
        device.setSoftwareVersion(Standar.SOFT_VERSION);
        device.setHardwareVersion(Standar.HARD_VERSION);
        device.setMac(blueDevice.getAddress());
        device.setUser(App.getCurrentUser());
        device.saveInCache(App.getAppContext());
        //虽然云端准确来说没有用处，但是我们暂时还是存到云端
        device.saveInCloud(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e != null) {
                    Log.i("device_save_incloud", paired.getName() + "fail");
                    e.printStackTrace();
                    return;
                }
                Log.i("device_save_db", paired.getName() + "success");
            }
        });
    }

    /**
     * 是否现在有配对的设备
     * @return
     */
    public boolean hasPaired() {
        if (paired == null)
            return false;
        return true;
    }

    /**
     * 获取默认绑定的设备
     * @return
     */
    private BluetoothDevice selectFromBonded() {
        String defalutMac = getDefaultMac();
        if (defalutMac == null)
            return null;
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(defalutMac);
        return device;
        /*
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mScanning = false;
                mBluetoothAdapter.stopLeScan(leScanCallback);
            }
        }, SCAN_PERIOD);
        mScanning = true;
        mBluetoothAdapter.startLeScan(leScanCallback);

        Log.i("bluetooth", String.valueOf(pairedDevices.size()));
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                if (device.getAddress().equals(defalutMac)) {
                    return device;
                }
            }
        }
        */
        //return null;
    }
    /**
     * 返回绑定的蓝牙的mac地址
     *
     * @return 蓝牙mac地址
     */
    private String getDefaultMac() {
        final DeviceDao deviceDao = new DeviceDao();
        Device device = deviceDao.getFromCache(App.getAppContext());
        if (device == null)
            return null;
        Log.i("bluetooth_get_dev", device.getMac());
        return device.getMac();
    }

    /**
     * 开始搜索蓝牙设备
     *如果正在搜索，则取消
     */
    public void startSearch() {
        if(mScanning == true) {
            if(activity != null)
                Toast.makeText(activity,"正在搜索中",Toast.LENGTH_SHORT).show();
            return;
        }
        scanLeDevice(true);
    }

    /**
     * 发送数据
     * @param bytes
     * @param len
     * @return  是否能够发送数据，但是一般能，我们认为底层能够跟我们发送出去
     */
    public boolean sendStream(byte[] bytes, int len) {
        if(mCurrentCharacteristic == null||mBluetoothGatt == null
                ||bytes == null || len<=0 )
            return false;
        byte[] sendData = new byte[len+1];
        //这个是发送给奶瓶的所有报文都必须加的头部
        sendData[0] = (byte)0x51;
        for(int i=0;i<len;i++)
            sendData[i+1] = bytes[i];

        StringBuilder builder = new StringBuilder(sendData.length);
        for(byte byteChar : sendData)
            builder.append(String.format("%02X ", byteChar));
        Log.i(TAG,"sendStrea-data:"+builder.toString());
        mCurrentCharacteristic.setValue(sendData);
        mBluetoothGatt.writeCharacteristic(mCurrentCharacteristic);
        return true;
    }
    /**
     * 停止搜索设备
     */
    public void stopScanLeDevice() {
        if (leScanCallback == null)
            return;
        scanLeDevice(false);
    }
    public boolean isEnabled(){
        return  mBluetoothAdapter.isEnabled();
    }

    public void ShutConnect() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }
    /**
     * 搜索蓝牙的回调
     */
    public interface LeScanListenter {
        void leScanCallBack(List<BluetoothDevice> bluetoothDeviceList);
    }

    public BluetoothGatt getConnectGatt(boolean autoConnect){
       if(paired == null){
           mBluetoothGatt = null;
           return null;
       }
        return getConnectGatt(paired,autoConnect);
    }
    /**
     * 得到BluetoothGatt
     * @param device 设备
     * @param autoConnect 是否自动链接
     */
    public BluetoothGatt getConnectGatt(BluetoothDevice device,boolean autoConnect){
        mBluetoothGatt = device.connectGatt(App.getAppContext(), autoConnect, callback);
        return mBluetoothGatt;
    }


    public void setDiscoverListener(BluetoothDiscoverListener bluetoothDiscoverListener){
        this.bluetoothDiscoverListener = bluetoothDiscoverListener;
    }

    /**
     * 字面意思理解成连接一个service，我们就是相当于记录一个service
     * 虽然蓝牙的概念上没有这个说法
     * @return  是否绑定成功
     */
    private boolean bindService(){
        if(mBluetoothGatt == null)
            return false;
        mCurrentService = mBluetoothGatt.getService(SERVICE_UUID);
        if(mCurrentService == null)
            return false;
        return true;
    }

    /**
     * 字面意思理解成 绑定一个character，不过蓝牙4.0没有这个概念，我们只
     * 是修改mcurrentCharacteristic,并且我们设置改character的通知，当有相关的信息的时候来触发
     * onCharacteristicChanged
     * @return 是否绑定成功
     */
    private boolean bindCharacteristic(){
        if(mCurrentService == null)
            return false;
        mCurrentCharacteristic = mCurrentService.getCharacteristic(CHARACTERISTIC_UUID);
        if(mCurrentCharacteristic == null)
            return false;
        mBluetoothGatt.setCharacteristicNotification(mCurrentCharacteristic,true);
        return true;
    }
    /**
     * 蓝牙的发现回调，提供找到，结束，开始，成功配对四个接口
     */
    public interface BluetoothDiscoverListener {
        void discoverFound(BluetoothDevice device);
        void discoverFinished();
        void discoverStarted();
        void pairedSuccess();
    }



    public void addBluetoothStateChanggedListener(ConnectedChanggedListener listener) {
        listeners.add(listener);
    }

    private void dispatchConnectChangge(boolean connect) {
        for (ConnectedChanggedListener a : listeners) {
            a.connectedChangged(connect);
        }
    }

    /**
     * 蓝牙连接状态改变的接口
     */
    public interface ConnectedChanggedListener {
        public void connectedChangged(boolean connect);
    }

    //让这个在主线程中执行状态改变的函数
    private class myHander extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == CONNECTED_STATE_CHANGED) {
                if (msg.arg1 == 0)
                    dispatchConnectChangge(false);
                else
                    dispatchConnectChangge(true);
            }
            super.handleMessage(msg);
        }
    }
}
