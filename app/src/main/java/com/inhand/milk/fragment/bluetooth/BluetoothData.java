package com.inhand.milk.fragment.bluetooth;

import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2015/5/24.
 * 蓝牙接受到数据，交给这个类出来，解析出相应的报文，并储存。
 */
public class BluetoothData {
    private static byte[] buf;
    private static int  location;
    private static  final int MAXLEN = 1024;
    private static SimpleDateFormat simpleDateFormat;
    private static enum DATASTATUS  {ERROR,DATAERROR,DATAVAILD,DATAINVAILD};
    private static BluetoothData bluetoothData;
    private BluetoothData(){
        this.buf = new byte[MAXLEN];
        this.location =  0;
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:MM");
    }
    private static synchronized  void init(){
        if(bluetoothData == null)
            bluetoothData = new BluetoothData();
    }
     public static BluetoothData getInstance(){
        if (bluetoothData == null)
            init();
         return bluetoothData;
    }

    //这个没有进行多线程保护的。。。但是我觉得应该不会，也没有必要的。。除非你开两个蓝牙的接收线程。
    public boolean saveData(byte[] buf, int len){
        if (len +location > MAXLEN)
            return  false;
        for(int i=0;i<len;i++){
            this.buf[i+location] = buf[i];
        }
        location = len +location;
        handleMessage();
        return true;
    }
    public void handleMessage(){
        int count ;
        if (location <=0)
            return;
        switch (buf[0]){
            case 1:
                count = new RecieveRecordMessage().isRecordMessage(buf,location);
                if (  count == -1)
                    return;
                else {
                    deleteHeadByte(count);
                    break;
                }

            default:
                if (deleteHeadByte(1) == true){
                      handleMessage();
                }
        }
    }
    private boolean deleteHeadByte(int len){
        if(len >location )
            return false;
        int i;
        for (i = len;i<location;i++){
            buf[i - len] = buf[i];
        }
        location = location -len;
        return true;
    }
}
