package com.inhand.milk.fragment.bluetooth;

/**
 * Created by Administrator on 2015/5/30.
 */
public class RequsetVerbose extends BaseSendMessages {
    private byte[] bytes;
    private static final int MAXLEN = 10;
    private int flags =0;
    RequsetVerbose(){
        super();
        bytes = new byte[MAXLEN];
        flags = 0;
        setTitle();

    }
    private void setTitle(){
        bytes[0] = (byte)0x04;
        setByte(bytes,1,0);
    }
    public int message2Bytes(byte[] buffer){
        checkMessage();
        return getBytes(buffer);
    }
    private void checkMessage(){
        floatToData(bytes,0,DataInvaild);
        setByte(bytes,4,1);
        setCRC(3);
        setEnd(5);
    }

}
