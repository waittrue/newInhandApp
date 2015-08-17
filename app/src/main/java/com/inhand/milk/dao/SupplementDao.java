package com.inhand.milk.dao;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.inhand.milk.entity.Supplement;

import java.util.List;

/**
 * SupplementDao
 * Desc: 数据访问层--辅食
 * Team: InHand
 * User: Wooxxx
 * Date: 2015-07-27
 * Time: 21:03
 */
public class SupplementDao {

    public static final String FIND_SORT = "sort";
    private AVQuery<Supplement> query = AVQuery.getQuery(Supplement.class);

    /**
     * 异步从云端分页查询辅食
     *
     * @param page     第几页（从0开始）
     * @param per      每页显示数量
     * @param callback 回调接口
     */
    public void findSuppsFromCloud(final int page
            , final int per, final FindCallback<Supplement> callback) {
        query.orderByAscending(FIND_SORT);
        query.whereNotEqualTo("isDel", true);
        query.setSkip(page * per);
        query.findInBackground(callback);
    }


    /**
     * 同步地从云端获得辅食信息
     *
     * @param page 页码
     * @param per  每页显示数
     * @return 辅食列表
     */
    public List<Supplement> findSuppsFromCloud(final int page, final int per) {
        query.orderByAscending(FIND_SORT);
        query.whereNotEqualTo("isDel", true);
        query.setSkip(page * per);
        try {
            return query.find();
        } catch (AVException e) {
            e.printStackTrace();
            return null;
        }
    }
}
