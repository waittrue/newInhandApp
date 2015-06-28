package com.inhand.milk.utils;

import com.inhand.milk.entity.Record;

import java.util.List;

/**
 * Calculator
 * Desc:封装常用计算
 * Team: InHand
 * User:Wooxxx
 * Date: 2015-03-23
 * Time: 19:04
 */
public class Calculator {

    /**
     * 计算饮奶记录的总奶量
     *
     * @param records 传入饮奶记录
     * @return 总饮奶量
     */
    public static int calcVolume(List<Record> records) {
        int sum = 0;
        for (Record record : records) {
            sum = sum + record.getVolume();
        }
        return sum;
    }

    /**
     * 计算饮奶记录的评分
     *
     * @param records 传入的饮奶记录
     * @return 评分
     */
    public static int calcScore(List<Record> records) {
        return 0;
    }
}
