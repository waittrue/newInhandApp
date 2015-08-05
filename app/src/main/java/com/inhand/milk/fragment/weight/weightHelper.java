package com.inhand.milk.fragment.weight;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SaveCallback;
import com.inhand.milk.App;
import com.inhand.milk.STANDAR.Standar;
import com.inhand.milk.dao.BabyInfoDao;
import com.inhand.milk.entity.Baby;
import com.inhand.milk.entity.BabyInfo;
import com.inhand.milk.utils.Calculator;
import com.inhand.milk.utils.LocalSaveTask;

import java.text.ParseException;
import java.util.ArrayList;
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
 */
public class weightHelper {
    private static final String TAG = "weightAache";
    private  Map<Integer, List<Map<Integer,Float>>> monthToweights = new HashMap<>();
    private static weightHelper instance;
    private static List<BabyInfo> babyInfos;
    private static Baby currentBaby = App.getCurrentBaby();
    private weightHelper() {
        initData();
    }
    private static synchronized void initInstance(){
        if(instance == null)
            instance = new weightHelper();
    }

    /**
     * 单例模式接口
     * @return 获得该类的单例对象
     */
    public static weightHelper getInstance(){
        if(instance == null){
            initInstance();
        }
        return instance;
    }

    /**
     * 初始化本类
     */
    private void  initData(){
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
     * 得到所有weight结果
     * @return 所有的weights的map值
     */
    public Map<Integer, List<Map<Integer, Float>>> getMonthToweights() {
        return monthToweights;
    }

    /**
     * 得到某个月的weight值
     * @param monthAge 月龄
     * @return 返回一个list 里面的map的key代表的是天龄，float代表的是weight值
     */
    public List<Map<Integer, Float>> getWeights(int monthAge){
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
     * 在原来的基础上增加一个weight
     * @param babyInfo 增加的weight信息
     */
    private void addOneWeight(BabyInfo babyInfo){
        String birth = babyInfo.getAge();
        Date date = null;
        try {
            date = Standar.dateFormat.parse(birth);
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }
        int month = Calculator.getBabyMonthAge(date);
        int days = Calculator.getBabyDayAge(date);
        Map<Integer,Float> map = new HashMap<>();
        map.put(days,babyInfo.getWeight());

        List<Map<Integer,Float>> list = monthToweights.get(month);
        if(list == null) {
            list = new ArrayList<>();
            monthToweights.put(month,list);
        }
        list.add(map);
    }

    /**
     * 从本地同步获取所有的babyinfo缓存
     * @return 返回一个babyinfo的list；
     */
    private List<BabyInfo> getAllBabyInfoCache(){
        BabyInfoDao babyInfoDao = new BabyInfoDao();
        Date birth = currentBaby.getCreatedAt();
        int birthYear = birth.getYear();
        int birthMonth = birth.getMonth();
        Date date = new Date();
        int year =date.getYear();
        int month =date.getMonth();
        List<BabyInfo> result = new ArrayList<>();
        for(int i=birthYear;i<=year;i++){
            for(int j=birthMonth;j<=month;j++){
                List<BabyInfo> babyInfos = babyInfoDao.findByDateFromCache(App.getAppContext(),String.valueOf(i)+"-"+String.valueOf(j));
                if(babyInfos == null)
                    continue;

                result.addAll(babyInfos);
            }
        }
        return result;
    }

    /**
     * 存入一个babyinfo，先存网络，如果成功在存本地，本地也成功，则更新本类的babyinfos，更新一下monthtoweights
     * @param babyInfo 要存入的babyinfo
     */
    public void  saveOneWeight(final  BabyInfo babyInfo){
        babyInfo.setBaby(App.getCurrentBaby());
        babyInfo.saveInCloud(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if(e !=null){
                    return ;
                }
                babyInfo.saveInCache(App.getAppContext(), new LocalSaveTask.LocalSaveCallback() {
                    @Override
                    public void done() {
                        babyInfos.add(babyInfo);
                        addOneWeight(babyInfo);
                    }
                });
            }
        });
    }
}
