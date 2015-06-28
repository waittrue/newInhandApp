package com.inhand.milk.fragment.bluetooth;

/**
 * Created by Administrator on 2015/6/3.
 */
public class BaseRecieveMessage {

    protected static enum DATASTATUS  {ERROR,DATAERROR,DATAVAILD,DATAINVAILD};

    protected float getDataValue(byte[] buf,int start){
        byte high,low,decimal;
        float value;
        high = buf[start+1];
        low = buf[start+2];
        decimal =  buf[start+3];
        value = (high&0xff)*256 + (low&0xff) + (float)(decimal&0xff)/256;
        return value;
    }
    protected DATASTATUS checkData(byte[] buf,int start){
        if ( ( buf[start]&(byte)0x02 ) != (byte)0 ) {
            if (( buf[start] & (byte)0x01) == (byte)0)
                return DATASTATUS.ERROR;
            if (  buf[start+1] != (byte)0xff)
                return DATASTATUS.ERROR;
            if ( buf[start+2] != (byte)0xff)
                return DATASTATUS.ERROR;
            if ( buf[start+3] != (byte)0xff)
                return DATASTATUS.ERROR;
            return DATASTATUS.DATAINVAILD;
        }
        if( ( buf[start]&(byte)0x01 ) != (byte)0)
            return DATASTATUS.DATAERROR;
        return DATASTATUS.DATAVAILD;
    }
    protected boolean checkCRC(byte[] buf,int start){
        int index;
        byte crcl,crch;
        crcl = (byte)0xff;
        crch = (byte)0xff;
        for(int i=0;i<start;i++)
        {
            index = (crcl ^ buf[i])&0xff ;
            crcl = (byte) (crch ^ CRC.crc_lo[index]);
            crch = CRC.crc_hi[index] ;
        }
        if ( crch == buf[start]  && crcl == buf[start +1]){
            return true;
        }
        return false;
    }
}
