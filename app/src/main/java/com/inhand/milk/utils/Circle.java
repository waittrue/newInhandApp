package com.inhand.milk.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.inhand.milk.R;

public class Circle extends View {

    int mColor = Color.WHITE, r = 0;
    private static final String TAG = "Circle";

    public Circle(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public Circle(Context context, AttributeSet attri) {
        super(context, attri);
        TypedArray a;
        a = context.obtainStyledAttributes(attri, R.styleable.MyCircle);
        r = a.getDimensionPixelOffset(R.styleable.MyCircle_r, 0);
        mColor = a.getColor(R.styleable.MyCircle_CircleColor, 0x000000);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (r != 0) {
            setMeasuredDimension(2 * r, 2 * r);
            return;
        } else {
            r = Math.min(getMeasuredHeight(), getMeasuredWidth());
            r = r / 2;
            Log.i(TAG, String.valueOf(r));
        }
    }

    public void setColor(int color) {
        mColor = color;
    }

    public void setR(int rr) {
        r = rr;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        float rr;
        if (r != 0)
            rr = r;
        else
            rr = getMeasuredHeight() / 2;

        Paint paint = new Paint();
        paint.setAlpha(255);
        paint.setAntiAlias(true);
        paint.setColor(mColor);
        canvas.drawCircle(rr, rr, rr, paint);
    }


}
