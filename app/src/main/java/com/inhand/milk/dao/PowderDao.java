package com.inhand.milk.dao;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.inhand.milk.entity.Powder;

import java.util.List;

/**
 * PowderDao
 * Desc: 数据访问层--奶粉
 * Team: InHand
 * User: Wooxxx
 * Date: 2015-07-26
 * Time: 10:34
 */
public class PowderDao {

    private AVQuery<Powder> query = AVQuery.getQuery(Powder.class);


    /**
     * 异步地分页查询奶粉
     *
     * @param page     当前页数（从第0页起算）
     * @param per      每页条目数
     * @param callback 回调接口
     */
    public void findFromCloud(final int page,
                              final int per,
                              final FindCallback<Powder> callback) {
        query.orderByAscending(Powder.PINYIN_NAME_KEY);
        query.whereNotEqualTo("isDel", true);
        query.setSkip(page * per);
        query.findInBackground(callback);

    }

    /**
     * 作者：大力
     * 异步地查询所有奶粉
     *
     * @param callback
     */
    public void findFromCloud(final FindCallback<Powder> callback) {
        query.orderByAscending(Powder.PINYIN_NAME_KEY);
        query.whereNotEqualTo("isDel", true);
        query.findInBackground(callback);
    }

    /**
     * 同步地分页查询奶粉
     *
     * @param page 当前页数（从第0页起算）
     * @param per  每页条目数
     * @return 奶粉列表
     */
    public List<Powder> findFromCloud(final int page, final int per) {
        query.orderByAscending(Powder.PINYIN_NAME_KEY);
        query.whereNotEqualTo("isDel", true);
        query.setSkip(page * per);
        try {
            return query.find();
        } catch (AVException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 作者：大力
     * 同步地分页查询所有奶粉
     *
     * @return 奶粉列表
     */
    public List<Powder> findFromCloud() {
        query.orderByAscending(Powder.PINYIN_NAME_KEY);
        query.whereNotEqualTo("isDel", true);
        try {
            return query.find();
        } catch (AVException e) {
            e.printStackTrace();
            return null;
        }
    }
}
