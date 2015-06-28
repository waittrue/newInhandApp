package com.inhand.milk.fragment.weight;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.inhand.milk.R;
import com.inhand.milk.utils.ProgressBar;

/**
 * Created by Administrator on 2015/6/16.
 */
public class Adder extends ViewGroup {
    private float height, width;
    private static final String TAG = "ADDER";
    private static final float scaleX = 0.8f;

    public Adder(Context context, float width, float height) {
        super(context);
        this.width = width;
        this.height = height;
    }

    public Adder(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Adder(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public Adder(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init() {

        ProgressBar progressBar = new ProgressBar(getContext());
        progressBar.setBgColor(Color.RED);
        this.addView(progressBar, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        TextView textview = new TextView(getContext());
        textview.setText(getResources().getString(R.string.weight_fragment_adder_text));
        textview.setTextColor(getResources().getColor(R.color.weight_fragment_adder_text_color));
        textview.setTextSize(getResources().getDimension(R.dimen.weight_fragment_adder_text_size));
        this.addView(textview, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        ImageView imageView = new ImageView(getContext());
        imageView.setImageResource(R.drawable.weight_fragment_adder_icon);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        this.addView(imageView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int wideSize = getMeasuredWidth();
        int heightSize = getMeasuredHeight();
        Log.i(TAG + " onmeasure", String.valueOf(wideSize));
        Log.i(TAG + " onmeasure", String.valueOf(heightSize));

        View child;
        for (int i = 0; i < 2; i++) {
            child = getChildAt(i);
            measureChild(child, MeasureSpec.makeMeasureSpec(wideSize, MeasureSpec.AT_MOST),
                    MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.AT_MOST));
        }
        child = getChildAt(2);
        measureChild(child, MeasureSpec.makeMeasureSpec((int) (heightSize * scaleX), MeasureSpec.AT_MOST),
                MeasureSpec.makeMeasureSpec((int) (heightSize * scaleX), MeasureSpec.AT_MOST));


    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View child;
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        child = getChildAt(0);
        child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());

        child = getChildAt(1);
        child.layout(height / 2, (height - child.getMeasuredHeight()) / 2,
                height / 2 + child.getMeasuredWidth(),
                (height - child.getMeasuredHeight()) / 2 + child.getMeasuredHeight());

        Log.i(TAG, String.valueOf(child.getMeasuredWidth()));
        Log.i(TAG, String.valueOf(child.getMeasuredHeight()));

        float space = height - height * scaleX;
        space = space / 2;
        child = getChildAt(2);
        child.layout((int) (width - child.getMeasuredWidth() - space),
                (int) (space),
                (int) (width - space),
                (int) (space + child.getMeasuredHeight()));

    }

}
