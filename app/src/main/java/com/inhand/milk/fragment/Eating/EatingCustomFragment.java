package com.inhand.milk.fragment.Eating;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.inhand.milk.App;
import com.inhand.milk.R;
import com.inhand.milk.activity.EatingCustomPlanActivity;
import com.inhand.milk.entity.BabyFeedItem;
import com.inhand.milk.fragment.TitleFragment;
import com.inhand.milk.helper.FeedPlanHelper;
import com.inhand.milk.ui.ButtonA;
import com.inhand.milk.utils.ViewHolder;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/29.
 */
public class EatingCustomFragment extends TitleFragment {
    private static final String TAG = "eatingcustomfragment";
    private static final String TIME = "time", TYPE = "type";
    private static final int time = 300;
    private List<int[]> planTime = new ArrayList<>();
    private boolean[] isMilk;
    private ButtonA buttonA, buttonB;
    private ListView listView;
    private DelectAdapter delectAdapter;
    private float itemHeight;
    private List<Map<String, Object>> data;
    private boolean closeAnimationFirstEnd = false;
    private List<Integer> seletedData;
    private float animationTranslate;
    private List<BabyFeedItem> babyFeedItems;
    private List<BabyFeedItem> delectBabyFeedItems = new ArrayList<>();
    public static final String PLAN_ITEM_KEY = "plan_time_key";
    public static final String PLAN_TYPE_KEY = "plan_type_key";
    private AdapterView.OnItemClickListener listViewListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ((EatingCustomPlanActivity) getActivity()).setBabyFeedItem(babyFeedItems.get(position));
            gotoSpecialFragment(new EatingCustomFixFragment());
        }
    };
    private View.OnClickListener finishListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            closeAnimationFirstEnd = false;
            seletedData = delectAdapter.getSelected();
            int firstPosition = listView.getFirstVisiblePosition();
            int EndPosition = listView.getLastVisiblePosition();
            for (int position : seletedData) {
                if (position >= firstPosition && position <= EndPosition)
                    closeAniamtion(listView.getChildAt(position - firstPosition));
            }
            for (int position = seletedData.size() - 1; position >= 0; position--) {
                int location = seletedData.get(position);
                delectBabyFeedItems.add(babyFeedItems.get(location));
                babyFeedItems.remove(location);
            }
        }
    };
    private View.OnClickListener addListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ((EatingCustomPlanActivity) getActivity()).setBabyFeedItem(null);
            gotoSpecialFragment(new EatingCustomFixFragment());
        }
    };
    private View.OnClickListener cancleListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            buttonA.setText(getResources().getString(R.string.public_delect));
            buttonB.setText(getResources().getString(R.string.public_add));
            int count = listView.getChildCount();
            for (int i = 0; i < count; i++) {
                outAnimaton(listView.getChildAt(i));
            }
            delectAdapter.setDelect(false);
            buttonA.setOnClickListener(deleteListener);
            buttonB.setOnClickListener(addListener);
        }
    };
    private View.OnClickListener deleteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (buttonA == null)
                return;
            buttonA.setText(getResources().getString(R.string.public_cancle));
            buttonB.setText(getResources().getString(R.string.public_finish));
            int count = listView.getChildCount();
            for (int i = 0; i < count; i++) {
                inAnimation(listView.getChildAt(i));
            }
            delectAdapter.setDelect(true);
            buttonA.setOnClickListener(cancleListener);
            buttonB.setOnClickListener(finishListener);
        }
    };

    /**
     * 从babyfeeditem  中删除那些我们选中select
     *
     * @param select 需要删除的位置
     */
    private void delectSelect(List<Integer> select) {
        if (select == null || select.isEmpty())
            return;
        List<Integer> sort = new ArrayList<>();
        sort.add(select.get(0));
        select.remove(0);
        int loaction = 0;
        for (int num : select) {
            for (loaction = sort.size() - 1; loaction >= 0; loaction--) {
                int temp = sort.get(loaction);
                if (num > temp) {
                    continue;
                } else if (num <= temp) {
                    sort.add(loaction + 1, num);
                    break;
                }
            }
            if (loaction < 0) {
                sort.add(0, num);
            }
        }
        for (int num : sort) {
            babyFeedItems.remove(num);
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // return super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.eating_custom_plan, container, false);
        buttonA = (ButtonA) mView.findViewById(R.id.eating_plan_custom_button_a);
        buttonB = (ButtonA) mView.findViewById(R.id.eating_plan_custom_button_b);
        animationTranslate = getResources().getDimensionPixelOffset(R.dimen.eating_custom_plan_item_animation_long);
        itemHeight = getResources().getDimensionPixelOffset(R.dimen.eating_custom_plan_item_height);
        View.OnClickListener rightListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                AsyncTask task = new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object[] params) {

                        try {
                            FeedPlanHelper feedPlanHelper = new FeedPlanHelper();
                            feedPlanHelper.changeSave(babyFeedItems);
                            feedPlanHelper.delectAll(delectBabyFeedItems);
                        } catch (AVException e) {
                            return false;
                        } catch (ParseException e) {
                            return false;
                        }
                        return true;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        if ((boolean) o == false) {
                            Toast.makeText(App.getAppContext(), "喂养计划修改失败，网络不给力", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                };
                task.execute();
            }
        };
        initViews();
        setTitleview(getResources().getString(R.string.eating_plan_custom_title), 2, getResources().getString(R.string.public_finish), rightListener);
        buttonA.setOnClickListener(deleteListener);
        buttonB.setOnClickListener(addListener);
        return mView;
    }

    private void initViews() {
        initPlantime();
        listView = (ListView) mView.findViewById(R.id.eating_custom_plan_listview);
        data = new ArrayList<>();
        for (int i = 0; i < planTime.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            int[] time = planTime.get(i);
            boolean ismilk = isMilk[i];
            String t = formatTime(time);
            map.put(TIME, t);
            map.put(TYPE, ismilk);
            data.add(map);
        }
        delectAdapter = new DelectAdapter(getActivity());
        delectAdapter.setData(data);
        listView.setAdapter(delectAdapter);
        listView.setOnItemClickListener(listViewListener);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden == false) {
            updateViews();
        }
    }

    private void updateViews() {
        Log.i("eatingCustomFragment", "updateViews");
        initPlantime();
        data.clear();
        for (int i = 0; i < planTime.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            int[] time = planTime.get(i);
            boolean ismilk = isMilk[i];
            String t = formatTime(time);
            map.put(TIME, t);
            map.put(TYPE, ismilk);
            data.add(map);
        }
        delectAdapter.setData(data);
        delectAdapter.notifyDataSetChanged();
    }
    private void initPlantime() {
        try {
            FeedPlanHelper feedPlanHelper = new FeedPlanHelper();
            if (babyFeedItems == null) {
                babyFeedItems = feedPlanHelper.getBabyFeedItemsFromAcache();
            }
            if (babyFeedItems == null)
                return;
            BabyFeedItem addbabyFeedItem = ((EatingCustomPlanActivity) getActivity()).getAddBabyFeedItem();
            if (addbabyFeedItem != null) {
                babyFeedItems.add(addbabyFeedItem);
                ((EatingCustomPlanActivity) getActivity()).setAddBabyFeedItem(null);
            }
            babyFeedItems = feedPlanHelper.sortBabyfeedItems(babyFeedItems);
            planTime = feedPlanHelper.getTime(babyFeedItems);
            isMilk = feedPlanHelper.getType(babyFeedItems);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    private String formatTime(int[] time) {
        String hour, m, result = "";
        hour = String.valueOf(time[0]);
        m = String.valueOf(time[1]);
        if (time[0] < 10) {
            result = "0";
        }
        result += hour;
        result += ":";
        if (time[1] < 10)
            result += "0";
        result += m;
        return result;
    }


    public void inAnimation(View view) {
        final CheckBox checkBox;
        final ImageView icon;
        final TextView typeTextView;
        checkBox = ViewHolder.get(view, R.id.eating_custome_plan_check_box);
        icon = ViewHolder.get(view, R.id.eating_custome_plan_item_icon);
        typeTextView = ViewHolder.get(view, R.id.eating_custom_plan_item_type);

        Animation animation = new TranslateAnimation(-animationTranslate, 0, 0, 0);
        animation.setDuration(time);
        ObjectAnimator inalpha = ObjectAnimator.ofFloat(checkBox, "alpha", 0, 1);
        inalpha.setDuration(time);
        checkBox.setVisibility(View.VISIBLE);
        inalpha.start();
        checkBox.startAnimation(animation);

        ObjectAnimator rightAnimation1 = getRightAnimator();
        ObjectAnimator rightAnimation2 = getRightAnimator();
        rightAnimation1.setTarget(icon);
        rightAnimation2.setTarget(typeTextView);
        rightAnimation1.start();
        rightAnimation2.start();
    }

    private ObjectAnimator getRightAnimator() {
        ObjectAnimator objectAnimator = new ObjectAnimator();
        objectAnimator.setDuration(time);
        objectAnimator.setPropertyName("translationX");
        objectAnimator.setFloatValues(0, animationTranslate);
        return objectAnimator;
    }

    public void outAnimaton(View view) {
        final CheckBox checkBox;
        final ImageView icon;
        final TextView typeTextView;
        checkBox = ViewHolder.get(view, R.id.eating_custome_plan_check_box);
        icon = ViewHolder.get(view, R.id.eating_custome_plan_item_icon);
        typeTextView = ViewHolder.get(view, R.id.eating_custom_plan_item_type);

        ObjectAnimator left1 = getLeftAnimator();
        ObjectAnimator left2 = getLeftAnimator();
        left1.setTarget(icon);
        left2.setTarget(typeTextView);
        left1.start();
        left2.start();

        ObjectAnimator outalpha = ObjectAnimator.ofFloat(checkBox, "alpha", 1, 0);
        outalpha.setDuration(time);
        outalpha.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                checkBox.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        outalpha.start();

        Animation animation = new TranslateAnimation(0, -animationTranslate, 0, 0);
        animation.setDuration(time);
        checkBox.startAnimation(animation);
    }

    private ObjectAnimator getLeftAnimator() {
        ObjectAnimator objectAnimator = new ObjectAnimator();
        objectAnimator.setDuration(time);
        objectAnimator.setPropertyName("translationX");
        objectAnimator.setFloatValues(animationTranslate, 0);
        return objectAnimator;
    }

    private void closeAniamtion(final View view) {

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1, 0);
        valueAnimator.setDuration(time);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float x = (float) animation.getAnimatedValue();
                ViewGroup.LayoutParams lp = view.getLayoutParams();
                lp.height = (int) (itemHeight * x);
                view.requestLayout();
                //  Log.i(TAG, String.valueOf(lp.height));
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (closeAnimationFirstEnd == true)
                    return;
                closeAnimationFirstEnd = true;
                int count = seletedData.size();
                for (int i = count - 1; i >= 0; i--) {
                    data.remove((int) seletedData.get(i));
                    Log.i(TAG, "remove :" + String.valueOf(seletedData.get(i)));
                }
                delectAdapter.notifyDataSetChanged();
                delectAdapter.clearSelected();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.start();
    }

    private class DelectAdapter extends BaseAdapter {
        private Context context;
        private boolean delect = false;
        private List<Integer> selected = new ArrayList<>();
        private List<Map<String, Object>> data;
        private float translate;
        private float itemHeight;
        private View.OnClickListener checkBoxListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                int num = (Integer) cb.getTag();
                if (cb.isChecked()) {
                    selected.add(num);
                } else {
                    selected.remove((Integer) num);
                }
                Log.i(TAG + "ch", String.valueOf(num));
            }
        };

        public DelectAdapter(Context context) {
            this.context = context;
            translate = context.getResources().getDimensionPixelOffset(R.dimen.eating_custom_plan_item_animation_long);
            itemHeight = getResources().getDimensionPixelOffset(R.dimen.eating_custom_plan_item_height);
        }

        public void setDelect(boolean delect) {
            if (delect == true) {
                int firstPosition = listView.getFirstVisiblePosition();
                int endPosition = listView.getLastVisiblePosition();
                CheckBox checkBox;
                for (int i = firstPosition; i <= endPosition; i++) {
                    checkBox = ViewHolder.get(listView.getChildAt(i - firstPosition), R.id.eating_custome_plan_check_box);
                    checkBox.setTag(i);
                    checkBox.setOnClickListener(checkBoxListener);
                    checkBox.setChecked(false);
                }
            }

            this.delect = delect;
        }

        public void setData(List<Map<String, Object>> data) {
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.eating_custome_plan_listview_item, null);
            } else {
                ViewGroup.LayoutParams lp = convertView.getLayoutParams();
                lp.height = (int) itemHeight;
            }
            Map<String, Object> map = data.get(position);
            CheckBox checkBox;
            TextView timeTextView, typeTextView;
            ImageView icon;
            checkBox = ViewHolder.get(convertView, R.id.eating_custome_plan_check_box);
            checkBox.setChecked(false);
            icon = ViewHolder.get(convertView, R.id.eating_custome_plan_item_icon);
            typeTextView = ViewHolder.get(convertView, R.id.eating_custom_plan_item_type);
            timeTextView = ViewHolder.get(convertView, R.id.eating_custome_plan_item_time);
            if (delect) {
                checkBox.setAlpha(1);
                checkBox.setVisibility(View.VISIBLE);
                checkBox.setTag(position);
                Log.i(TAG + "::", String.valueOf(position));
                checkBox.setOnClickListener(checkBoxListener);
                icon.setTranslationX(translate);
                typeTextView.setTranslationX(translate);
                if (selected.contains(position)) {
                    checkBox.setChecked(true);
                }
            } else {
                checkBox.setAlpha(0);
                checkBox.setVisibility(View.GONE);
                checkBox.setOnClickListener(null);
                icon.setTranslationX(0);
                typeTextView.setTranslationX(0);
            }


            if ((boolean) map.get(TYPE)) {
                icon.setImageDrawable(context.getResources().getDrawable(R.drawable.eating_feed_milk_ico));
                typeTextView.setText(getResources().getString(R.string.eating_plan_custom_item_type_milk));
            } else {
                icon.setImageDrawable(context.getResources().getDrawable(R.drawable.eating_feed_food_ico));
                typeTextView.setText(getResources().getString(R.string.eating_plan_custom_item_type_food));
            }

            timeTextView.setText((String) map.get(TIME));
            return convertView;
        }

        public List<Integer> getSelected() {
            Collections.sort(selected);
            for (int a : selected) {
                Log.i("height", String.valueOf(a));
            }
            return selected;
        }

        public void clearSelected() {
            selected.clear();
        }
    }

}
