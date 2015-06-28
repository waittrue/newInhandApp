package com.inhand.milk.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2015/5/17.
 */
public class MultiLayerCircle extends View {
    private static final String TAG = "MultiLayerCircle";
    private float mR = 0;
    private int[] mColors;
    private int[] mWeights;

    public MultiLayerCircle(Context context) {
        super(context);
    }

    /*
     *这里因为这个view的大小是2*r的int，而我们画的过程中是一2*r来画的，所以这里会出现画的地方大于了实际地方，
     * 不然容易损失1px，这个眼睛是看的出来的。
     *在这里我认为即使上层给我一个的float r，我应该想的不大于这个r的最大圆来画,。
     */
    public MultiLayerCircle(Context context, float r, int[] colors, int[] weights) {
        super(context);
        this.mR = ((int) (r * 2)) / 2;
        mColors = colors;
        mWeights = weights;
    }

    public MultiLayerCircle(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MultiLayerCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MultiLayerCircle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setR(float r) {
        this.mR = ((int) (r * 2)) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        int count = mColors.length < mWeights.length ? mColors.length : mWeights.length;
        int weight = 0;
        float r = 0, tempR;
        // float centerXY = getMeasuredHeight() / 2;
        for (int i = 0; i < count; i++) {
            weight += mWeights[i];
        }
        for (int i = 0; i < count; i++) {
            paint.setColor(mColors[i]);
            tempR = mR * mWeights[i] / weight;
            paint.setStrokeWidth(tempR);
            canvas.drawCircle(mR, mR, r + tempR / 2, paint);
            r += tempR;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension((int) (2 * mR), (int) (2 * mR));
    }
}
