package com.inhand.milk.helper;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.inhand.milk.App;
import com.inhand.milk.STANDAR.Standar;
import com.inhand.milk.dao.OneDayDao;
import com.inhand.milk.entity.OneDay;
import com.inhand.milk.entity.Record;
import com.inhand.milk.utils.LocalSaveTask;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2015/8/9.
 */
public class RecordHelper extends ObserableHelper {
    private static final String TAG = "RecordHelper";
    private static RecordHelper instance = null;
    private OneDayDao oneDayDao = new OneDayDao();
    private Map<String, OneDay> data = new HashMap<>();
    private Record lastRecord;

    private RecordHelper() {
    }

    private synchronized static void init() {
        if (instance == null)
            instance = new RecordHelper();
    }

    public static RecordHelper getInstance() {
        if (instance == null)
            init();
        return instance;
    }

    /**
     * 同步的 ，根据日期返回当天的oneday，如果数据data里面没有，则从本地取并更新。
     *
     * @param date 返回oneday的日期
     * @return oneday。
     */
    public OneDay getOneday(Date date) {
        String stringDate = Standar.DATE_FORMAT.format(date);
        OneDay oneDay = data.get(stringDate);
        if (oneDay != null)
            return oneDay;
        List<OneDay> oneDays = oneDayDao.findBetween(App.getAppContext(), stringDate, stringDate);
        if (oneDays == null || oneDays.isEmpty())
            return null;
        oneDay = oneDays.get(0);
        if (oneDay == null)
            return null;
        data.put(stringDate, oneDay);
        return oneDay;
    }

    /**
     * 获得从今天算起，几天内的数据
     *
     * @param days 表示一共要取的几天的数据
     * @return 这几天的oneday，如果为空则返回null。
     */
    public List<OneDay> getOnedays(int days) {
        Calendar calendar = Calendar.getInstance();
        List<OneDay> oneDays = new ArrayList<>();
        for (int i = 0; i < days; i++) {
            Date date = calendar.getTime();
            calendar.roll(Calendar.DAY_OF_MONTH, -1);
            OneDay oneDay = getOneday(date);
            if (oneDay == null)
                continue;
            oneDays.add(oneDay);
        }
        if (oneDays.isEmpty())
            return null;
        return oneDays;
    }

    /**
     * 返回最近的limit个oneday
     * @param limit 限制返回的个数
     * @return list oneday或者null
     */
    public List<OneDay> getOnedaysBylimit(int limit){
        OneDayDao oneDayDao = new OneDayDao();
        List<OneDay> oneDays = oneDayDao.findFromDB(App.getAppContext(),limit);
        if(oneDays == null || oneDays.isEmpty())
            return null;
        for(OneDay oneDay:oneDays){
            Date date = null;
            try {
                date = Standar.DATE_FORMAT.parse(oneDay.getDate());
            }catch (ParseException e){
                e.printStackTrace();
                continue;
            }
            getOneday(date);
        }
        return oneDays;
    }
    /**
     * 返回今天的oneday
     *
     * @return oneday；
     */
    public OneDay getToadayOneday() {
        return getOneday(new Date());
    }

    /**
     * 根据日期返回当天的records，我做成分页形式，没有的时候从数据库拿，有的时候从data里面拿。
     * date的数据格式必须为YYYY-MM-dd
     *
     * @param date 需求数据的日期
     * @return 返回当天的records
     */
    public List<Record> getRecords(String date) {
        OneDay oneDay = data.get(date);
        if (oneDay != null)
            return oneDay.getRecords();
        List<OneDay> oneDays = oneDayDao.findBetween(App.getAppContext(), date, date);
        if (oneDays == null || oneDays.isEmpty())
            return null;
        oneDay = oneDays.get(0);
        if (oneDay == null)
            return null;
        data.put(date, oneDay);
        return oneDay.getRecords();
    }

    /**
     * 更新本地的data这个数据
     *
     * @param date
     */
    private void updateOneday(String date) {
        List<OneDay> oneDays = oneDayDao.findBetween(App.getAppContext(), date, date);
        if (oneDays == null || oneDays.isEmpty())
            return;
        OneDay oneDay = oneDays.get(0);
        if (oneDay == null)
            return;
        data.put(date, oneDay);
    }

    /**
     * 输入一个record 存入本地和云端，这个地方必须开线程存，存完后我们更新数据结构data，
     * 这里存的实体应该是oneday，这里我们输入oneday，默认存的日期就是今天,对应的宝宝默认是
     * 本地缓存的宝宝。
     *
     * @param record 需要存入云端和本地的record。
     */
    public void saveRecord(Record record) {
        saveRecord(record, new Date());
    }

    /**
     * 把record 存入日期为date的oneday，存入云端和本地。本地完成后，更新数据结构data。
     *
     * @param record 需要存入的record
     * @param date   存入相应的日期
     */
    public void saveRecord(Record record, final Date date) {
        if (record == null) {
            Log.e(TAG, "record should not be null");
            return;
        }
        OneDay oneDay = new OneDay();
        oneDay.setDate(date);
        oneDay.setBaby(App.getCurrentBaby());
        List<Record> records = new ArrayList<>();
        records.add(record);
        oneDay.setRecords(records);
        saveOneday(oneDay);
    }

    /**
     * 把一天的oneday同步到云端和本地，并更新本地的数据data这个结构
     *
     * @param oneDay 需要存储的oneyday
     */
    public void saveOneday(final OneDay oneDay) {
        //这里的代码有点诡异，理论上这里需要的同步到云端和本地，但是这里只存到本地，同步到云端等到
        //调用同步接口的时候
        /*
        oneDay.saveInCloud(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null)
                    Log.i(TAG, "oneday save inCloud success" + oneDay.getDate());
                else {
                    Log.i(TAG, "oneday save incloud failed" + oneDay.getDate());
                }
            }
        });
        */
        oneDay.saveInDB(App.getAppContext(), new LocalSaveTask.LocalSaveCallback() {
            @Override
            public void done() {
                Log.i(TAG, "oneday save DB success" + oneDay.getDate());
                updateOneday(oneDay.getDate());
            }
        });
    }

    /**
     * 输入外部存储的record，判断他和本地最新的record是否相同
     *
     * @param record
     * @return
     */
    public boolean needChanged(Record record) {
        return !record.equals(lastRecord);
    }

    /**
     * 获得最近一次饮奶的record
     *
     * @return 最近一次饮奶的数据.
     */
    public Record getLastRecord() {
        List<OneDay> oneDays = oneDayDao.findFromDB(App.getAppContext(), 1);
        if (oneDays == null || oneDays.size() == 0)
            return null;
        OneDay oneDay = oneDays.get(0);
        if (oneDay == null)
            return null;
        List<Record> records = oneDay.getRecords();
        if (records == null || records.isEmpty())
            return null;
        lastRecord = records.get(records.size() - 1);
        return lastRecord;
    }

    public void syncRecord() {
        SyncHelper.syncCloud(App.getAppContext(), new SyncHelper.SyncCallback() {
            @Override
            public void done(AVException e) {
                if (e != null) {
                    Log.i(TAG, "sync failed");
                    e.printStackTrace();
                    return;
                } else {
                    //跟新本地的data
                    Log.i(TAG,"Record sync success");
                    Set<String> set = data.keySet();
                    for(String i:set){
                        updateOneday(i);
                    }
                }
            }
        });
    }
}
