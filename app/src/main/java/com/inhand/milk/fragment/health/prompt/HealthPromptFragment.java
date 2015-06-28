package com.inhand.milk.fragment.health.prompt;

import android.app.Fragment;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.inhand.milk.R;
import com.inhand.milk.fragment.TitleFragment;
import com.inhand.milk.utils.CircleDrawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/5/7.
 */
public class HealthPromptFragment extends TitleFragment {

    private ListView listView;
    private List<List<Object>> configuration = new ArrayList<>();
    private LayoutInflater mInflater;
    private float[] mWeight;
    private List<String[]> text;
    private View.OnClickListener rightListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       // return super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.health_prompt,container,false);
        rightListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoNextFragment();
            }
        };
        initViews(mView);
        setTitleview(getResources().getString(R.string.health_prompt_title),2,getResources().getDrawable(R.drawable.back_icon),rightListener);
        return mView;
    }
    private void initViews(View v){
        initConfigures();
        mInflater  =  LayoutInflater.from(getActivity().getApplicationContext());//这个inflate可不可以等于上面那个
        listView = (ListView)v.findViewById(R.id.health_prompt_listview);
        listView.setAdapter(getAdapter());

    }
    private void initConfigures(){
        int i,temp=0;
        String[] strings = getResources().getStringArray(R.array.health_prompt_listview_item_doc);
        String[] docStrings = getResources().getStringArray(R.array.health_prompt_listview_item_long_doc);
        int[] textColors = getResources().getIntArray(R.array.health_prompt_listview_item_text_doc_colors);
        int[] numColors = getResources().getIntArray(R.array.health_prompt_listview_item_num_doc_colors);
        int[] longColors = getResources().getIntArray(R.array.health_prompt_listview_item_num_long_doc_colors);
        mWeight = new float[strings.length];
        text = new ArrayList<>();
        for( i=0;i<strings.length;i++){
            List<Object> list = new ArrayList<>();
            String[] doc = new String[3];
            list.add(strings[i]);
            doc[0] = strings[i];

            list.add(textColors[i]);
            list.add(numColors[i]);
            list.add(String.valueOf(getPromptCount(i))+"次");
            doc[1] = String.valueOf(getPromptCount(i))+"次";
            text.add(doc);

            mWeight[i] = getPromptCount(i);
            temp += mWeight[i];
            list.add(docStrings[i]);
            list.add(longColors[i]);
            configuration.add(list);
        }
        for(i=0;i<strings.length;i++){
            String[] doc = text.get(i);
            doc[2] = "共"+String.valueOf(temp)+"次";
        }
    }
    private BaseAdapter getAdapter(){
        return new BaseAdapter() {
            @Override
            public int getCount() {
                return configuration.size();
            }

            @Override
            public Object getItem(int position) {
                return configuration.get(position % getCount());
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null)
                    convertView = mInflater.inflate(R.layout.health_prompt_listview_item,null);
                itemSettings(convertView,  (List<Object> )getItem(position) );
                return  convertView;
            }
        };
    }
    private void itemSettings(View view,List<Object> item){
        ImageView  imageView = (ImageView)view.findViewById(R.id.health_prompt_listview_item_circle_icon);
        CircleDrawable circle  = new CircleDrawable(getResources().getDimension(R.dimen.health_prompt_listview_item_circle_r)/2,
                (int)item.get(1));
        imageView.setImageDrawable(circle);

        TextView textView = (TextView)view.findViewById(R.id.health_prompt_listview_item_text_doc);
        textView.setText((String)item.get(0));
        textView.setTextColor((int) item.get(1));

        textView = (TextView)view.findViewById(R.id.health_prompt_listview_item_text_total);
        textView.setTextColor((int)item.get(1));

        textView = (TextView)view.findViewById(R.id.health_prompt_listview_item_text_num);
        textView.setText((String)item.get(3));
        GradientDrawable myGrad = (GradientDrawable)textView.getBackground();
        myGrad.setColor((int)item.get(1));

        textView = (TextView)view.findViewById(R.id.health_prompt_listview_item_text_long_doc);
        textView.setText((String)item.get(4));
        myGrad = (GradientDrawable)textView.getBackground();
        myGrad.setColor((int)item.get(5));
    }

    private int getPromptCount(int i){
        switch (i){
            case 0:
                return getHeightChongtiao();
            case 1:
                return getHeightTemperature();
            case 2:
                return getLowTemperature();
        }
        return 0;
    }

    @Override
    protected Fragment getNextFragment() {
        return new Nutrition(mWeight,text);
    }

    private int getHeightChongtiao(){
        return 16;
    }
    private int getHeightTemperature(){
        return 4;
    }
    private int getLowTemperature(){
        return 2;
    }

}
