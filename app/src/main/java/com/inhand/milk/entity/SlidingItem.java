package com.inhand.milk.entity;

/**
 * SlidingItem
 * Desc:侧滑菜单项
 * Team: InHand
 * User:Wooxxx
 * Date: 2015-03-14
 * Time: 15:26
 */
public class SlidingItem extends ConfigableMenuItem {
    //侧滑待跳转地址名
    private String redirect;

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }
}
