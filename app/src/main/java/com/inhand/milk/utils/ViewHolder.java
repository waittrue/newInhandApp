package com.inhand.milk.utils;

import android.util.SparseArray;
import android.view.View;

/**
 * ViewHolder
 * Desc:组件持有器
 * Team: InHand
 * User:Wooxxx
 * Date: 2015-03-05
 * Time: 22:43
 */
public class ViewHolder {
    /**
     * 从容器view中获取id为id组件
     *
     * @param view 组件容器
     * @param id   组件ID
     * @param <T>
     * @return 持久者返回该组件
     */
    @SuppressWarnings("unchecked")
    public static <T extends View> T get(View view, int id) {
        SparseArray<View> viewHolder =
                (SparseArray<View>) view.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }
}
