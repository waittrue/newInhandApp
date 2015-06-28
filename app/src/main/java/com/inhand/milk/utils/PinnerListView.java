package com.inhand.milk.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by Administrator on 2015/6/4.
 */
public class PinnerListView extends ListView{
    private static final String TAG = "PinnerListView";
    private PinnerListViewAdapter myAdapter;
    private View head;
    public PinnerListView(Context context) {
        super(context);
    }

    public PinnerListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PinnerListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PinnerListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public void setHead(int resoure){
        head = LayoutInflater.from(this.getContext()).inflate(resoure,this,false);
    }
    @Override
    public void setAdapter(ListAdapter adapter) {
        myAdapter =	 (PinnerListViewAdapter)adapter;
        adapteHead();
        super.setAdapter(adapter);

    }
    private void adapteHead(){
        if(myAdapter != null) {
            head = myAdapter.getListViewHead(getFirstVisiblePosition(), head);
            if (head == null)
                return;
        }
    }
    private void layoutHead(){
        int width = head.getMeasuredWidth();
        int height = head.getMeasuredHeight();
        int offset;
        View child;
        if(myAdapter.hasHead(getFirstVisiblePosition()) ){
            //Log.i(TAG,"hasHead");
            head.layout(0,0,width,height);
        }
        else{
            child = getChildAt(0);
            if ( child.getBottom() > height)
                head.layout(0,0,width,height);
            else {
               // Log.i(TAG,"bottom < height");
                int i =1,count;
                count = getChildCount();
                while(i+1 <count){
                    child = getChildAt(i);
                    if ( child.getBottom() > height){
                        if (myAdapter.hasHead(i+getFirstVisiblePosition())){
                            offset = getChildAt(i-1).getBottom();
                          //  Log.i(TAG,String.valueOf(offset)+" offset" + String.valueOf(height));
                            head.layout(0, offset - height, width, offset);
                        }
                        else
                            head.layout(0,0,width,height);
                        break;
                    }
                    i++;
                }
            }
        }
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        adapteHead();
        if(head != null)
            measureChild(head,widthMeasureSpec,heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(head != null)
            layoutHead();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        adapteHead();
        if(head != null)
            layoutHead();
        if(head != null)
            drawChild(canvas, head, getDrawingTime());
    }
}
