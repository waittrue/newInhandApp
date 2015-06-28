package com.inhand.milk.entity;

/**
 * ConfigableMenuItem
 * Desc:可配置选项菜单基类
 * Team: InHand
 * User:Wooxxx
 * Date: 2015-03-17
 * Time: 08:34
 */
public class ConfigableMenuItem {
    //菜单显示名
    private String name;
    //菜单图标
    private String icon;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
