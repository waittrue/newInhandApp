package com.inhand.milk.fragment.bluetooth;

import android.widget.Toast;

import com.inhand.milk.App;

import java.security.InvalidParameterException;

/**
 * Created by Administrator on 2015/5/30.
 * 主要完成message到byte[]的转换，并提供返回给用户byte[]的方法；
 */
public class BaseSendMessages {
    private static int MAXLEN = 1024;
    private byte[] buf;
    private int bufLen;
    public static final int DataInvaild = 1,DataVaild = 2,DataError =3;
    BaseSendMessages(){
        buf = new byte[MAXLEN];
        bufLen = 0;
    }
    protected  boolean setByte(byte[] buffer,int len,int start){
        if (buffer.length < len)
            return false;
        if (start + len > MAXLEN)
            return false;
        int i;
        for(i=0;i<len;i++){
            buf[start++] = buffer[i];
        }
        bufLen  = start>bufLen?start:bufLen;
        return  true;
    }
    protected int getBytes(byte[] buffer){
        int len = buffer.length;

        if(bufLen > len)
            return -1;

        int i;
        for(i=0;i<bufLen;i++){
            buffer[i] = buf[i];
        }

        return bufLen;
    }
    protected void setCRC(int used){
        /*这个检查应该是没有必要的；
        if (used +2 > MAXLEN )
            return;
         */
        int index ;
        byte crcl,crch;
        crcl = (byte)0xff;
        crch = (byte)0xff;

        for(int i=0;i<used;i++)
        {
            index = (crcl ^ buf[i]) &0xff;
            crcl = (byte) (crch ^ CRC.crc_lo[index]);
            crch = CRC.crc_hi[index] ;
        }

        buf[used] = crch;
        buf[used+1] = crcl;
        bufLen  = used +2>bufLen?used +2:bufLen;
    }
   protected  void setEnd(int used){
       buf[used] = (byte)0xff;
       bufLen  = used +1>bufLen?used +1:bufLen;
   }
    protected boolean floatToData(byte[] buffer,float value,int status){
        return floatToData(buffer,0,value,status);
    }
    protected boolean floatToData(byte[] buffer,int used,float value,int status){
        if(buffer.length -used <4)
            return false;
        switch (status){
            case DataError:
                setDataError(buffer,used);
                break;
            case DataInvaild:
                setDataInvaild(buffer,used);
                break;
            case DataVaild:
                setDataVaild(buffer,used,value);
                break;
            default:
                throw new InvalidParameterException("消息输入的状态不是要求的三种状态");
        }
        return true;
   }
    private void setDataError(byte[] buffer,int used){
        buffer[used] = (byte) 0x01;
        buffer[used+1] = (byte)0xff;
        buffer[used+2] = (byte)0xff;
        buffer[used+3] = (byte)0xff;
    }
    private void setDataInvaild(byte[] buffer,int used){
        buffer[used] =  (byte)0x03;
        buffer[used+1] = (byte)0xff;
        buffer[used+2] = (byte)0xff;
        buffer[used+3] = (byte)0xff;
    }
    private void setDataVaild(byte[] buffer,int used,float value){
        int high,low,decimal;
        high = (int)value;
        if(high >= 256*256)
            high = high%(256*256);
        low = high%256;
        high = high/256;
        decimal = (int)((value - (int)value)*256);
        Toast.makeText(App.getAppContext(),String.valueOf(decimal),1000).show();
        buffer[used] = 0x00;
        buffer[used+1]= (byte)high;
        buffer[used+2]= (byte)low;
        buffer[used+3]= (byte)decimal;

    }
 }
