package com.inhand.milk.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.inhand.milk.R;

/**
 * Created by Administrator on 2015/7/29.
 */
public class ButtonB extends ButtonA {
    public ButtonB(Context context) {
        super(context);
    }

    public ButtonB(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ButtonB(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ButtonB(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void init(AttributeSet attrs) {
        super.init(attrs);
        TypedArray t = getContext().obtainStyledAttributes(attrs, R.styleable.RoundImageView);
        color = t.getColor(R.styleable.RoundImageView_backgroundcolor, getResources().getColor(R.color.public_button_b_bg_color));
    }
}
