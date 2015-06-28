package com.inhand.milk.entity;

/**
 * FooterItem
 * Desc:底部栏菜单项
 * Team: InHand
 * User:Wooxxx
 * Date: 2015-03-13
 * Time: 09:36
 */
public class FooterItem extends ConfigableMenuItem {
    //待跳转fragment名称
    private String fragment;

    public String getFragment() {
        return fragment;
    }

    public void setFragment(String fragment) {
        this.fragment = fragment;
    }
}
