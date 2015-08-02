package com.inhand.milk.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.avos.avoscloud.AVUser;
import com.inhand.milk.R;

/**
 * Created by Administrator on 2015/7/29.
 */
public class ButtonA extends View {
    public static final String  TAG = "RoundImageView";
    private String text;
    protected int color,textColor;
    private int textSize,radius;
    public ButtonA(Context context) {
        super(context);
    }

    public ButtonA(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ButtonA(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public ButtonA(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }
    protected void init(AttributeSet attrs){
        TypedArray t = getContext().obtainStyledAttributes(attrs, R.styleable.RoundImageView);
        text = t.getString(R.styleable.RoundImageView_text);
        color = t.getColor(R.styleable.RoundImageView_backgroundcolor, getResources().getColor(R.color.public_button_a_bg_color));
        textColor = t.getColor(R.styleable.RoundImageView_textcolor,getResources().getColor(R.color.color_white));
        textSize = t.getDimensionPixelOffset(R.styleable.RoundImageView_textsize, 5);
        radius = t.getDimensionPixelOffset(R.styleable.RoundImageView_radius,20);
    }
    public void setText(String string){
        text = string;
        invalidate();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        float height,width;
        height = getMeasuredHeight();
        width = getMeasuredWidth();
        Log.i(TAG, String.valueOf(height) + " " + String.valueOf(width));
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        RectF rect = new RectF(0,0,width,height);
        canvas.drawRoundRect(rect, radius, radius,paint);

        paint.setColor(textColor);
        paint.setTextSize(textSize);
        float textWidth = paint.measureText(text);
        //让文字上下居中
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float fontHeight = fontMetrics.bottom - fontMetrics.top;
        float textBaseY = height - (height - fontHeight)/2 - fontMetrics.bottom;
        canvas.drawText(text,width /2 - textWidth/2,  textBaseY  ,paint);
    }
}
