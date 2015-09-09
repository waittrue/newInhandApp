package com.inhand.milk.fragment.bluetooth;

/**
 * Created by Administrator on 2015/9/9.
 * ���ߣ�����
 * ʱ�䣺2015/9/9
 * ������������ģ��Ƿ��͸���ƿ����������ʼ��
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
     * �Ƿ�ָ���������
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
     * �Ƿ� ����У׼
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
