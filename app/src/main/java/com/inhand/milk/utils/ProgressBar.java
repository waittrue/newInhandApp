package com.inhand.milk.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Administrator on 2015/5/17.
 */
public class ProgressBar extends View {
    private int bgColor,color;
    private float width,height;
    private int timeAnimator;
    private float maxNum,currentNum = 0;
    private android.os.Handler handler;
    private boolean attchTo = false;

    public ProgressBar(Context context) {
        super(context);
        width = height = 0;
        attchTo = true;
    }
    public ProgressBar(Context context,float width,float height) {
        super(context);
        this.width = width;
        this.height = height;
        attchTo = false;
    }
    public ProgressBar(Context context,float width,float height,int bgColor,int color) {
        super(context);
        this.width = width;
        this.height = height;
        this.bgColor = bgColor;
        this.color = color;
        attchTo = false;
    }

    public ProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        attchTo = true;
    }

    public ProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        attchTo = true;
    }

    public ProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        attchTo = true;
    }
    public void setBgColor(int color){
        this.bgColor = color;
    }
    public void setColor(int color){
        this.color = color;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setTimeAnimator(int timeAnimator) {
        this.timeAnimator = timeAnimator;
    }

    public void setMaxNum(float maxNum) {
        this.maxNum = maxNum>100?100:maxNum;
        currentNum = (float)this.maxNum;
    }

    private void drawLine(Canvas canvas, int color, float len) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(1);
        paint.setColor(color);
        if(len < height)
            return;
        RectF rectF;


        rectF = new RectF(0, 0, height, height);
        canvas.drawArc(rectF,90,180,true,paint);
        canvas.drawRect(height/2-1,0,len-height/2+1,height,paint);
        rectF = new RectF(len-height,0,len,height);
        canvas.drawArc(rectF,270,180,true,paint);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLine(canvas, bgColor, width);
        drawLine(canvas, color, currentNum / 100 * width);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (attchTo) {
            Log.i("progress", "super.onmeasure");
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            width = getMeasuredWidth();
            height = getMeasuredHeight();
        } else
            setMeasuredDimension((int) (width), (int) (height));
    }

    public void startAnimator(){
        if (handler == null)
            handler  =new android.os.Handler();
        currentNum = 0;
     //   Log.i("maxNum",String.valueOf(maxNum));
        Runnable runnable =new Runnable() {
            @Override
            public void run() {
                invalidate();
                currentNum += maxNum/(timeAnimator/2);
                if (currentNum >= maxNum){
                    currentNum = maxNum;
                    handler.removeCallbacks(this);
                }
                else {
                    handler.postDelayed(this, 2);
                }
                //Log.i("currenNum",String.valueOf(currentNum));
            }
        };
        handler.post(runnable);
    }
}
