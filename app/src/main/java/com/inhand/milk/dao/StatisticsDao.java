package com.inhand.milk.dao;

import com.avos.avoscloud.AVQuery;
import com.inhand.milk.entity.Statistics;


/**
 * StatisticsDao
 * Desc: 数据访问层--统计信息
 * Date: 2015/6/3
 * Time: 7:24
 * Created by: Wooxxx
 */
public class StatisticsDao {

    private AVQuery<Statistics> query = AVQuery.getQuery(Statistics.class);

}
