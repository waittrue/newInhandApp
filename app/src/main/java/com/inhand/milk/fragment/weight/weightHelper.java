package com.inhand.milk.fragment.weight;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SaveCallback;
import com.inhand.milk.App;
import com.inhand.milk.STANDAR.Standar;
import com.inhand.milk.dao.BabyInfoDao;
import com.inhand.milk.entity.Baby;
import com.inhand.milk.entity.BabyInfo;
import com.inhand.milk.utils.ACache;
import com.inhand.milk.utils.Calculator;
import com.inhand.milk.utils.LocalSaveTask;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/20.
 * 本类，主要是协助做weight方面的取和存储，这里用单例模式，主要为了维护babyinfos，和monthtoweights
 * 这两个变量，如果每次存储weight都用这个类的接口，那我们可以只需要初始化一次，以后所有的数据都从
 * 这个地方取，全部放在内存中，方便使用。
 *
 * 因为我们的App前期关于weight的数据小，所以我想把所有数据放到内存里面，后面可以修改这个类。以免占用
 * 过多的内存
 *
 */

public class WeightHelper {
    private static final String TAG = "weightAache";
    private  Map<Integer,Map<Integer,Float>> monthToweights = new HashMap<>();
    private static WeightHelper instance;
    private static List<BabyInfo> babyInfos;
    private static Baby currentBaby ;
    private BabyInfo currentBabyInfo;
    private WeightHelper() {
        initData();
    }
    private static synchronized void initInstance(){
        if(instance == null)
            instance = new WeightHelper();
    }

    /**
     * 单例模式接口
     * @return 获得该类的单例对象
     */
    public static WeightHelper getInstance(){
        if(instance == null){
            initInstance();
        }
        return instance;
    }
    /**
     * 初始化本类
     */
    private void  initData(){
        currentBaby = App.getCurrentBaby();
        if(currentBaby == null){
            ACache aCache = ACache.get(App.getAppContext());
            String json = aCache.getAsString(Baby.CACHE_KEY);
            Log.i("baby currentbaby json",json);
            currentBaby = JSON.parseObject(json, Baby.class);
        }
        if(babyInfos == null)
            babyInfos = getAllBabyInfoCache();
        months2Weights();
    }

    /**
     * 得到所有babyinfos的信息
     * @return 返回babyinfo的list
     */
    public List<BabyInfo> getBabyInfos(){
        return babyInfos;
    }

    /**
     * 得到当前最近,最新的一个体重
     * @return
     */
    public float getCurrentWeight(){
        if(currentBabyInfo == null){
            NullPointerException e = new NullPointerException("weight no data in ache");
            throw e;
        }
        return currentBabyInfo.getWeight();
    }

    /**
     * 返回最后一条weight的时间
     * @return 返回时间
     */
    public Date getLastWeightDate(){
        if(currentBabyInfo == null){
            NullPointerException e = new NullPointerException("本地没有缓存weight");
            throw e;
        }
        Date date = null;
        try {
            date = Standar.DATE_FORMAT.parse(currentBabyInfo.getAge());
        }catch (ParseException e){
            e.printStackTrace();
            return null;
        }
        return  date;
    }
    /**
     * 得到所有weight结果
     * @return 所有的weights的map值
     */
    public Map<Integer, Map<Integer, Float>> getMonthToweights() {
        return monthToweights;
    }

    /**
     * 得到某个月的weight值
     * @param monthAge 月龄
     * @return 返回一个list 里面的map的key代表的是天龄，float代表的是weight值
     */
    public Map<Integer, Float>getWeights(int monthAge){
        return monthToweights.get(monthAge);
    }

    /**
     * 更新一下monthtoweights
     */
    private void months2Weights(){
        if(babyInfos == null)
            return;
        monthToweights.clear();
        for(BabyInfo babyInfo:babyInfos){
           addOneWeight(babyInfo);
        }
    }

    /**
     * 在原来的基础上增加一个weight,主要实现本地monthtoweights的跟新。
     * @param babyInfo 增加的weight信息
     */
    private void addOneWeight(BabyInfo babyInfo){
        String birth = babyInfo.getAge();
        Date date = null;
        try {
            date = Standar.DATE_FORMAT.parse(birth);
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }
        int month = Calculator.getBabyMonthAge(date);
        int days = Calculator.getBabyDayAge(date);
        Map<Integer,Float> map = new HashMap<>();
        map.put(days,babyInfo.getWeight());

        Map<Integer,Float> list = monthToweights.get(month);
        if(list == null) {
            list = new HashMap<>();
            monthToweights.put(month,list);
        }
        list.put(days, babyInfo.getWeight());
    }

    /**
     * 从本地同步获取所有的babyinfo缓存,并更新currentbabyinfo
     * @return 返回一个babyinfo的list；
     */
    private List<BabyInfo> getAllBabyInfoCache(){
        BabyInfoDao babyInfoDao = new BabyInfoDao();
        if(currentBaby == null)
            Log.i("baby current","null");
        Date birth = currentBaby.getCreatedAt();
        if(birth == null)
            Log.i("baby birth","null");

        /*
        String birthday = currentBaby.getBirthday();
        Date birth = null;
        try {
            birth = Standar.DATE_FORMAT.parse(birthday);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        */
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(birth);
        int birthYear = calendar.get(Calendar.YEAR);
        int birthMonth = calendar.get(Calendar.MONTH)+1;
        Date date = new Date();
        calendar.setTime(date);
        int year =calendar.get(Calendar.YEAR);
        int month =calendar.get(Calendar.MONTH)+1;
        List<BabyInfo> result = new ArrayList<>();
        for(int i=birthYear;i<=year;i++){
            for(int j=birthMonth;j<=month;j++){
                String time;
                if(j>=10)
                    time =String.valueOf(i)+"-"+String.valueOf(j);
                else
                    time = String.valueOf(i)+"-0"+String.valueOf(j);
                Log.d("baby gettime",time);
                List<BabyInfo> tempBabyInfos = babyInfoDao.findByDateFromCache(App.getAppContext(),time);
                if(tempBabyInfos == null)
                    continue;
                result.addAll(tempBabyInfos);
                int count = tempBabyInfos.size();
                if(count <=0 )
                    continue;
                currentBabyInfo = tempBabyInfos.get(count-1);
                Log.d("baby currentbabyinfo","not null");
            }
        }

        return result;
    }

    /**
     * 存入一个babyinfo，先存网络，如果成功在存本地，本地也成功，则更新本类的babyinfos，更新一下monthtoweights
     * 并更新currentbabyinfo
     * @param babyInfo 要存入的babyinfo
     */
    public void  saveOneWeight(final  BabyInfo babyInfo){
        babyInfo.setBaby(App.getCurrentBaby());
        babyInfo.saveInCloud(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if(e !=null){
                    e.printStackTrace();
                    Log.i(TAG, "babyinfo incloud failed");
                    return ;
                }
                Log.i(TAG,"babyinfo incloud success");
                babyInfo.saveInCache(App.getAppContext(), new LocalSaveTask.LocalSaveCallback() {
                    @Override
                    public void done() {
                        babyInfos.add(babyInfo);
                        if(babyInfos.isEmpty())
                            babyInfos.add(babyInfo);
                        else {
                            BabyInfo tempBabyinfo = babyInfos.get(babyInfos.size() - 1);
                            if( tempBabyinfo.getAge().equals( babyInfo.getAge() ) ){
                                babyInfos.remove(babyInfos.size()-1);
                                babyInfos.add(babyInfo);
                            }
                            else {
                                babyInfos.add(babyInfo);
                            }
                        }
                        addOneWeight(babyInfo);
                        String currentDate = currentBabyInfo.getAge();
                        String today = babyInfo.getAge();
                        Date current =null,now = null;
                        try{
                            current = Standar.DATE_FORMAT.parse(currentDate);
                        }catch (ParseException e){
                            e.printStackTrace();
                            return;
                        }
                        try {
                            now  = Standar.DATE_FORMAT.parse(today);
                        }catch (ParseException e){
                            e.printStackTrace();
                            return;
                        }
                        if(current.compareTo(now) <= 0 ){
                            currentBabyInfo = babyInfo;
                        }
                    }
                });
            }
        });
    }

    /**
     * 同步的与云端同步，并更新weighthelper里面数据
     */
    public void sync(){
        BabyInfoDao babyInfoDao = new BabyInfoDao();
        List<BabyInfo> babyInfosCloud = babyInfoDao.findByBabyFromCloud(currentBaby);
        if(babyInfosCloud == null || babyInfosCloud.isEmpty())
            return;
        if(equals(currentBabyInfo,babyInfosCloud.get(0))){
            return;
        }
        for(BabyInfo babyInfo:babyInfosCloud){
            babyInfo.saveInCache(App.getAppContext());
        }
        initData();
    }

    /**
     * 比较两个babyinfo 是否相同
     * @param a
     * @param b
     * @return 相同返回真
     */
    private boolean equals(BabyInfo a,BabyInfo b){
        if(a.getCreatedAt().compareTo(b.getCreatedAt()) != 0)
            return false;
        return true;
    }
}
