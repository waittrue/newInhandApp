package com.inhand.milk.utils;

import android.content.Context;
import android.graphics.Color;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.inhand.milk.R;

/**
 * Created by Administrator on 2015/7/6.
 * 主要实现iphone那种右边有个快速查找的功能
 */
public class QuickListView extends ViewGroup {
    private PinnerListView listView;
    private QuickListViewAdapter adapter;
    private static final String TAG = "quickListView";
    private LinearLayout linearLayout;
    private static final String[] defualtString={"A","B","C","D","E","F","G","H","I","J","K","L","M","N",
                    "O","P","Q","R","S","T","U","V","W","X","Y","Z","L"};
    private String[] rightStrings = defualtString;
    private int quickNavigationWidth,quickNavigationHeight,width,height;
    public QuickListView(Context context) {
        super(context);
        init();
    }

    public QuickListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QuickListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public QuickListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
    private void init(){
        quickNavigationWidth = getResources().getDimensionPixelOffset(R.dimen.quicklistview_right_navigation_default_width);
        listView = new PinnerListView(this.getContext());
        listView.setDividerHeight(0);
        listView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        listView.setVerticalScrollBarEnabled(false);

        ViewGroup.LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        this.addView(listView,lp);

        linearLayout = new LinearLayout(getContext());
        this.addView(linearLayout);
    }
    private void initLinearlayout(int width,int height){
        if(linearLayout == null)
            linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        int oneHeight,textsize;
        if(rightStrings== null)
            return;
        oneHeight = height / rightStrings.length;
        textsize = Math.min(width/2,oneHeight);
        int len = rightStrings.length;
        linearLayout.removeAllViews();
        this.removeView(linearLayout);
        for(int index=0;index< len;index++){
            TextView textView = new TextView(this.getContext());
            textView.setText(rightStrings[index]);
            textView.setTextColor(Color.BLUE);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textsize);
            textView.setGravity(Gravity.CENTER);
            TextPaint tp = textView.getPaint();
            tp.setFakeBoldText(true);
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listViewScroll(  ((TextView) v).getText().toString() );
                }
            });

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width,oneHeight);
            linearLayout.addView(textView, lp);
        }
    }
    public void setAdapter(QuickListViewAdapter adapter){
        this.adapter = adapter;
        listView.setAdapter(adapter);
    }
    public void setRightStrings(String[] str){
        rightStrings =str;
    }
    public void setHead(int resouce){
        listView.setHead(resouce);
    }
    public ListView getListView(){
        return  listView;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();

        Log.i(TAG,"onmearsur"+" "+String.valueOf(width)+" "+String.valueOf(height));
        quickNavigationHeight = height*7/10;
        initLinearlayout(quickNavigationWidth,quickNavigationHeight);
        ViewGroup.LayoutParams lp = new LayoutParams(quickNavigationWidth,quickNavigationHeight);
        this.addView(linearLayout,lp);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View child ;
        child  = this.getChildAt(0);
        child.layout(l,t,r,b);
        Log.i("l t r b", String.valueOf(l) + " " + String.valueOf(t) + " " + String.valueOf(r) + " " + String.valueOf(b) + " ");
        child = this.getChildAt(1);
        int left,right,top,bottom;
        left = width - quickNavigationWidth;
        right = r;
        top = t + (height -quickNavigationHeight)/2;
        bottom = top+ quickNavigationHeight;
        child.layout(left,top,right,bottom);
    }

    private void listViewScroll(String title){
        int len = adapter.getCount();
        int i;
        String str;
        for(i=0;i<len;i++){
            str = adapter.getTitle(i);
            if(str == null)
                continue;
            if(str.equals(title))
                break;
        }
        if(i == len)
            return;
        listView.setSelection(i);
    }
}
