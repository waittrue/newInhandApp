package com.inhand.milk.fragment.bluetooth;

/**
 * Created by Administrator on 2015/5/30.
 */
public class MilkMessage extends BaseSendMessages {
    private byte[] bytes;
    private static final int MAXLEN = 10,AdviseMaxFlags = 0,AdviseMinFlags =1,MilkDensityFlags =2,UerserTimeFlags =3,
                            AllFlags = 0x0f;
    private int flags =0;
    MilkMessage(){
        super();
        bytes = new byte[MAXLEN];
        flags = 0;
        setTitle();

    }
    private void setTitle(){
        bytes[0] = (byte)0x03;
        setByte(bytes,1,0);
    }

    public void setAdviseMax(float advise){
        floatToData(bytes,advise,DataVaild);
        setByte(bytes,4,1);
        flags |= 1>>AdviseMaxFlags;
    }
    private  void setAdviseMax(){
        floatToData(bytes,0,DataInvaild);
        setByte(bytes,4,1);
    }
    public void setAdviseMin(float advise){
        floatToData(bytes,advise,DataVaild);
        setByte(bytes,4,5);
        flags |= 1>>AdviseMinFlags;
    }
    private  void setAdviseMin(){
        floatToData(bytes,0,DataInvaild);
        setByte(bytes,4,5);
    }
    public void setMilkDensity(float density){
        floatToData(bytes,density,DataVaild);
        setByte(bytes,4,9);
        flags |= 1>>MilkDensityFlags;
    }
    private  void setMilkDensity(){
        floatToData(bytes,0,DataInvaild);
        setByte(bytes,4,9);
    }
    public  void setTime(float time){
        floatToData(bytes,time,DataVaild);
        setByte(bytes,4,13);
        flags |= 1>>UerserTimeFlags;
    }
    private  void setTime(){
        floatToData(bytes,0,DataInvaild);
        setByte(bytes,4,13);
    }
    public int message2Bytes(byte[] buffer){
        checkMessage();
        return getBytes(buffer);
    }
    private void checkMessage(){

        if (flags == AllFlags)
            return ;
        if  (  ( flags&( 1>>AdviseMaxFlags)) ==0)
             setAdviseMax();
        if ( (flags&(1>>AdviseMinFlags)) == 0)
            setAdviseMin();
        if ( (flags& (1>>MilkDensityFlags)) == 0)
            setMilkDensity();
        if ((flags&(1>>UerserTimeFlags)) == 0)
            setTime();
        setCRC(17);
        setEnd(19);
    }
}
