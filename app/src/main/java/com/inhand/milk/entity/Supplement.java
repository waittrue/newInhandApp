package com.inhand.milk.entity;

import com.avos.avoscloud.AVClassName;

/**
 * Supplement
 * Desc: 辅食模型
 * Team: InHand
 * User: Wooxxx
 * Date: 2015-07-26
 * Time: 09:26
 */
@AVClassName(Supplement.SUPPLEMENT_CLASS)
public class Supplement extends Base {

    public static final String SUPPLEMENT_CLASS = "Milk_Supplement";
    public static final String NAME_KEY = "name"; // 辅食名称
    public static final String INGREDIENTS_KEY = "ingredients"; // 辅食食材
    public static final String COOKING_METHOD_KEY = "cooking_method"; // 辅食做法
    public static final String DESC_KEY = "desc"; // 辅食描述
    public static final String FOR_AGE_KEY = "for_age"; // 针对年龄段

    /**
     * 获得辅食名称
     * @return  辅食名称
     */
    public String getName(){
        return this.getString(NAME_KEY);
    }

    /**
     * 获得辅食食材
     * @return 辅食食材
     */
    public String getIngredients(){
        return this.getString(INGREDIENTS_KEY);
    }


    /**
     * 获得辅食做法
     * @return 辅食做法
     */
    public String getCookingMethod(){
        return this.getString(COOKING_METHOD_KEY);
    }


    /**
     * 获得辅食描述
     * @return 辅食描述
     */
    public String getDesc(){
        return this.getString(DESC_KEY);
    }

    /**
     * 获得辅食针对年龄段
     * @return 辅食针对年龄段
     */
    public String getForAge(){
        return this.getString(FOR_AGE_KEY);
    }


}
