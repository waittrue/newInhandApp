package com.inhand.milk.fragment.bluetooth;

/**
 * Created by Administrator on 2015/9/9.
 * 作者：大力
 * 时间：2015/9/9
 * 描述：这个报文，是发送给奶瓶，告诉他初始化
 *
 */
public class InitMessage extends BaseSendMessages{
    private static final int MAXLEN = 10;
    private byte[] bytes;
    private int flags = 0;

    public InitMessage(){
        super();
        bytes = new byte[MAXLEN];
        flags = 0;
        setTitle();
    }
    private void setTitle(){
        bytes[0] = (byte) 0x06;
        setByte(bytes, 1, 0);
    }

    /**
     * 是否恢复出厂设置
     * @param a
     */
    public void factorySetting(boolean a){
        if(a){
            bytes[0] =(byte)0xff;
        }
        else
            bytes[0] =(byte)0x00;
        setByte(bytes,1,1);
    }

    /**
     * 是否 做零校准
     * @param a
     */
    public void adjusting(boolean a){
        if(a)
            bytes[0] = (byte)0xff;
        else
            bytes[1] = (byte)0x00;
        setByte(bytes,1,2);
    }
    public int message2Bytes(byte[] buffer) {
        setEnd(3);
        return getBytes(buffer);
    }
}
