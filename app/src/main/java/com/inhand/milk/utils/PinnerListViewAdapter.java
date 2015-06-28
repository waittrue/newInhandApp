package com.inhand.milk.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/6/4.
 * 管理数据，数据封装有这个类做，
 * 判断是否需要head
 * 提供判断是否head需要移动；
 */
public class PinnerListViewAdapter extends BaseAdapter {
    private List<List<Map>> mData;
    private Context context;
    private static final int HEAD = 0, CONTENT = 1;
    private ConfigureView configureView;
    private static final String TAG = "PinnerListViewAdapter";
    private static LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);

    public interface ConfigureView {
        View configureHead(Map<String, Object> map, View convertView, int position);

        View configureContent(Map<String, Object> map, View convertView, int position);
    }

    public PinnerListViewAdapter(Context context) {
        this.mData = new ArrayList<>();
        this.context = context;
    }

    public void setConfigureView(ConfigureView configureView) {
        this.configureView = configureView;
    }

    public void addMap(Map<String, Object> head, Map<String, Object> content, int postion) {
        if (head == null && content == null)
            return;
        if (postion < 0)
            return;
        List<Map> list = new ArrayList<>();
        Map<String, Object> mHead = new HashMap<>();
        Map<String, Object> mContent = new HashMap<>();
        mHead.putAll(head);
        mContent.putAll(content);
        list.add(mHead);
        list.add(mContent);
        mData.add(postion, list);
        // Log.i(TAG,String.valueOf(mData.size()));
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        LinearLayout linearLayout;
        if (convertView == null) {
            convertView = new LinearLayout(context);
            ((LinearLayout) convertView).setOrientation(LinearLayout.VERTICAL);
            ((LinearLayout) convertView).addView(new LinearLayout(context), HEAD);
            ((LinearLayout) convertView).addView(new LinearLayout(context), CONTENT);
        }
        linearLayout = (LinearLayout) ((LinearLayout) convertView).getChildAt(HEAD);
        if (needHead(position)) {
            view = configureView.configureHead(mData.get(position).get(HEAD),
                    linearLayout.getChildAt(0), position);
            linearLayout.removeAllViews();
            linearLayout.addView(view, lp);
            linearLayout.setVisibility(View.VISIBLE);
        } else {
            linearLayout.removeAllViews();
            linearLayout.setVisibility(View.INVISIBLE);
        }

        linearLayout = (LinearLayout) ((LinearLayout) convertView).getChildAt(CONTENT);
        if (mData.get(position).get(CONTENT) != null) {
            view = configureView.configureContent(mData.get(position).get(CONTENT),
                    linearLayout.getChildAt(0), position);
            linearLayout.removeAllViews();
            linearLayout.addView(view, lp);
            linearLayout.setVisibility(View.VISIBLE);
        } else
            linearLayout.setVisibility(View.INVISIBLE);
        return convertView;
    }

    public View getListViewHead(int position, View view) {
        Map<String, Object> map = null;
        for (int i = position; i >= 0; i--) {
            map = mData.get(position).get(HEAD);
            if (map == null)
                continue;
            else
                break;
        }
        if (map == null)
            return null;
        return configureView.configureHead(map, view, position);
    }

    public boolean hasHead(int position) {
        return needHead(position);
    }

    private boolean needHead(int postion) {
        if (postion >= mData.size())
            return true;
        Map<String, Object> currentHead = mData.get(postion).get(HEAD);
        if (currentHead == null) {
            return false;
        }
        if (postion == 0) {
            return true;
        }

        Map<String, Object> PreHead = mData.get(postion - 1).get(HEAD);
        if (PreHead == null) {
            return true;
        }
        Object tmp1, tmp2;
        for (String key : currentHead.keySet()) {
            if (PreHead.containsKey(key)) {
                tmp1 = currentHead.get(key);
                tmp2 = PreHead.get(key);

                if (null != tmp1 && null != tmp2) {

                    if (tmp1.equals(tmp2))
                        continue;
                    else {
                        return true;
                    }
                } else if (null == tmp1 && null == tmp2) {
                    continue;
                } else {
                    return true;
                }
            } else {
                return true;
            }

        }
        return false;
    }
}
