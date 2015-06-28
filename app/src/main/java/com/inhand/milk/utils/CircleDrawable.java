package com.inhand.milk.utils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2015/5/15.
 */
public class CircleDrawable extends Drawable {
    private float rr;
    private int color;
    public  CircleDrawable() {
        rr = 0;
        color = Color.WHITE;
    }
    public  CircleDrawable(float r, int color) {
        rr =r;
        this.color = color;
    }
    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAlpha(255);
        paint.setAntiAlias(true);
        paint.setColor(color);
        canvas.drawCircle(rr , rr , rr, paint);
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter cf) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }
}
