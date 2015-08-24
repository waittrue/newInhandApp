package com.inhand.milk.fragment.bluetooth;

import android.util.Log;

import com.inhand.milk.App;
import com.inhand.milk.STANDAR.Standar;
import com.inhand.milk.entity.OneDay;
import com.inhand.milk.entity.Record;
import com.inhand.milk.helper.RecordHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2015/6/3.
 * 这个类主要是输入bytes进来，具备判断这个报文，和存储这个报文的功能；
 */
public class RecieveRecordMessage extends BaseRecieveMessage {
    private static final String TAG = "recordMessage";

    public RecieveRecordMessage() {

    }

    public static void printRecord(Record record) {
        String str = new String();
        str += "\n" + "开始时间:" + record.getBeginTime() + "\n";
        str += "开始温度:" + String.valueOf(record.getBeginTemperature()) + "\n";
        str += "结束温度：" + String.valueOf(record.getEndTemperature()) + "\n";
        str += "饮奶量" + String.valueOf(record.getVolume()) + "\n";
        str += "建议量" + String.valueOf(record.getAdviceVolume()) + "\n";
        str += "持续时间" + String.valueOf(record.getDuration()) + "\n";
        str += "分数" + String.valueOf(record.getScore()) + "\n";
        Log.i(TAG, str);
    }

    public int isRecordMessage(byte[] bytes, int len) {
        if (isMessage(bytes, len) == true)
            return 28;
        else
            return -1;
    }

    //暂时处理成，数据只有全部有效的时候存储，不存储错误信息,false 代表不是这个报文，true 代表是这类报文，并处理了。
    private boolean isMessage(byte[] buf, int len) {
        float advice, amount, startT, endT, continuTime, interval, score;
        Record record;
        List<Record> records = new ArrayList<>();
        OneDay oneDay = new OneDay();
        if (len < 28)
            return false;
        if (buf[0] != 1)
            return false;
        if (messageCheck(buf) == false) {
            return true;
        }
        record = new Record();
        startT = getDataValue(buf, 1);
        record.setBeginTemperature(startT);

        endT = getDataValue(buf, 5);
        record.setEndTemperature(endT);

        amount = getDataValue(buf, 9);
        record.setVolume((int) amount);

        continuTime = getDataValue(buf, 13);
        record.setDuration((int) continuTime);

        interval = getDataValue(buf, 17);
        setRecordTime(record, continuTime, interval, oneDay);

        advice = getDataValue(buf, 21);
        record.setAdviceVolume((int) advice);

        score = Standar.getRecord(advice, amount, startT, endT, continuTime);
        record.setScore((int) score);

        records.add(record);
        //打印出record的所有相关信息
        printRecord(record);

        oneDay.setRecords(records);
        oneDay.setBaby(App.getCurrentBaby());
        RecordHelper.getInstance().saveOneday(oneDay);
        return true;
    }

    private void setRecordTime(Record record, float continuTime, float interva, OneDay oneDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -(int) interva);
        calendar.add(Calendar.MINUTE, -(int) continuTime);
        record.setBeginTime(Standar.TIME_FORMAT.format(calendar.getTime()));
        oneDay.setDate(calendar.getTime());
    }

    private boolean messageCheck(byte[] buf) {
        if (checkData(buf, 1) != DATASTATUS.DATAVAILD)
            return false;
        if (checkData(buf, 5) != DATASTATUS.DATAVAILD)
            return false;
        if (checkData(buf, 9) != DATASTATUS.DATAVAILD)
            return false;
        if (checkData(buf, 13) != DATASTATUS.DATAVAILD)
            return false;
        if (checkData(buf, 17) != DATASTATUS.DATAVAILD)
            return false;
        if (checkData(buf, 21) != DATASTATUS.DATAVAILD)
            return false;
        if (checkCRC(buf, 25) == false)
            return false;
        if (buf[27] != (byte) 0xff)
            return false;
        return true;
    }
}
