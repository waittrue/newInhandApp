package com.inhand.milk.dao;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.inhand.milk.entity.FeedPlan;

import java.util.List;


/**
 * FeedPlanDao
 * Desc: 数据访问层--喂养计划
 * Team: InHand
 * User: Wooxxx
 * Date: 2015-07-26
 * Time: 10:28
 */
public class FeedPlanDao {

    private AVQuery<FeedPlan> query = AVQuery.getQuery(FeedPlan.class);

    /**
     * 异步地根据ID从云端获得喂养计划
     *
     * @param id       待查询Id
     * @param callback 回调接口
     */
    public void findByIdFromCloud(final String id, final GetCallback<FeedPlan> callback) {
        query.getInBackground(id, callback);
    }

    /**
     * 同步地根据ID从云端获得喂养计划
     *
     * @param id 待查询Id
     * @return 获得喂养计划
     */
    public FeedPlan findByIdFromCloud(final String id) {
        try {
            return query.get(id);
        } catch (AVException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 同步地从云端获取营养师推荐
     *
     * @return 营养师推荐
     */
    public List<FeedPlan> findRCMDsFromCloud() {
        query.whereNotEqualTo(FeedPlan.RCMD_KEY, null);
        try {
            return query.find();
        } catch (AVException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 异步地从云端获得所有营养师推荐
     *
     * @param callback 回调接口
     */
    public void findRCMDsFromCloud(FindCallback<FeedPlan> callback) {
        query.whereNotEqualTo(FeedPlan.RCMD_KEY, null);
        query.findInBackground(callback);
    }


}
