package com.inhand.milk.entity;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

/**
 * PowderInfo
 * Desc:奶粉信息类
 * Team: InHand
 * User:Wooxxx
 * Date: 2015-03-17
 * Time: 08:22
 */
@AVClassName(PowderInfo.POWDERINFO_CLASS)
public class PowderInfo extends AVObject {
    static final String POWDERINFO_CLASS = "Milk_PowderInfo";
    //奶粉品牌
    private static final String BRAND_KEY = "brand";
    //奶粉名
    private static final String RECORDS_KEY = "name";
    //奶粉描述
    private static final String DESC_KEY = "desc";
}
