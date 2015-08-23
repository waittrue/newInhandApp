package com.inhand.milk.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.inhand.milk.R;

public class BluetoothPairedViewGroup extends ViewGroup {

    private Ring ring;
    private int width;
    private int height;
    private Circle circle;
    private ImageView icon, bluetoothBg, bluetoothStateIcon;
    private TextView textView;
    private boolean blueStatus = false;

    public BluetoothPairedViewGroup(Context context) {
        super(context);
        init();
        // TODO Auto-generated constructor stub

    }

    public BluetoothPairedViewGroup(Context context, AttributeSet attrs) {
        // TODO Auto-generated constructor stub
        super(context, attrs);
        init();
        //this.setBackgroundColor(Color.BLACK);
    }

    private void init() {

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // TODO Auto-generated method stub

        View child;
        /*
        int i;
		for (i=0;i<1;i++){
			child = getChildAt(i);
			child.layout(width/2 - child.getMeasuredWidth()/2,
					width/2 -child.getMeasuredHeight()/2, 
					width/2 +child.getMeasuredWidth()/2, 
						width/2 + child.getMeasuredHeight()/2 );	
		}*/
        child = circle;
        child.layout(width / 2 - child.getMeasuredWidth() / 2,
                width / 2 - child.getMeasuredHeight() / 2,
                width / 2 + child.getMeasuredWidth() / 2,
                width / 2 + child.getMeasuredHeight() / 2);

        ring.layout(width / 2 - ring.getMeasuredWidth() / 2,
                width / 2 - ring.getMeasuredHeight() / 2,
                width / 2 + ring.getMeasuredWidth() / 2,
                width / 2 + ring.getMeasuredHeight() / 2);

        float margin = getResources().getDimension(R.dimen.utils_BluetoothPaired_margin);
        int totalHeight = icon.getMeasuredHeight() +
                textView.getMeasuredHeight() + (int) margin;

        icon.layout(width / 2 - icon.getMeasuredWidth() / 2,
                width / 2 - totalHeight / 2,
                width / 2 + icon.getMeasuredWidth() / 2,
                width / 2 + totalHeight / 2 - textView.getMeasuredHeight() - (int) margin);


        textView.layout(width / 2 - textView.getMeasuredWidth() / 2,
                width / 2 - totalHeight / 2 + icon.getMeasuredHeight() + (int) margin,
                width / 2 + textView.getMeasuredWidth() / 2,
                width / 2 + totalHeight / 2);

        bluetoothBg.layout(width / 2 - bluetoothBg.getMeasuredWidth() / 2,
                width - (int) margin, width / 2 + bluetoothBg.getMeasuredWidth() / 2,
                width + bluetoothBg.getMeasuredHeight() - (int) margin);

        bluetoothStateIcon.layout(width / 2 - bluetoothStateIcon.getMeasuredWidth() / 2,
                width + (bluetoothBg.getMeasuredHeight() - (int) margin - bluetoothBg.getMeasuredHeight() / 3) / 2 - bluetoothStateIcon.getMeasuredHeight() / 2,
                width / 2 + bluetoothStateIcon.getMeasuredWidth() / 2,
                width + (bluetoothBg.getMeasuredHeight() - (int) margin - bluetoothBg.getMeasuredHeight() / 3) / 2 + bluetoothStateIcon.getMeasuredHeight() / 2);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        Log.i("onmeasure", "bluetooth------------");
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        width = sizeWidth;
        height = sizeHeight;
        if (circle == null) {
            circle = new Circle(getContext());
            circle.setR(sizeWidth / 2);
            addView(circle);
        }
        measureChild(circle, widthMeasureSpec, heightMeasureSpec);

        if (ring == null) {
            ring = new Ring(getContext(), sizeWidth / 2 * 0.9f,
                    getResources().getColor(R.color.home_circle_outermost));
            ring.setSweepAngle(360);
            addView(ring);
        }
        measureChild(ring, 0, 0);

        if (icon == null) {
            icon = new ImageView(getContext());
            icon.setBackgroundDrawable(getResources().getDrawable(R.drawable.bluetooth_switch_ico));
            icon.setScaleType(ScaleType.CENTER_INSIDE);
            LayoutParams lp = new LayoutParams(sizeWidth / 4, sizeWidth / 4);
            addView(icon, lp);
        }
        measureChild(icon, widthMeasureSpec, heightMeasureSpec);

        if (textView == null) {
            textView = new TextView(getContext());
            textView.setText("开始");
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, sizeWidth / 10);
            addView(textView);
        }
        measureChild(textView, widthMeasureSpec, heightMeasureSpec);

        if (bluetoothBg == null) {
            bluetoothBg = new ImageView(getContext());
            bluetoothBg.setBackgroundDrawable(getResources().getDrawable(R.drawable.bluetooth_state_bg));
            bluetoothBg.setScaleType(ScaleType.CENTER_INSIDE);
            LayoutParams bluetoothlp = new LayoutParams(sizeWidth / 4, sizeWidth / 4);
            addView(bluetoothBg, bluetoothlp);
        }
        measureChild(bluetoothBg, widthMeasureSpec, heightMeasureSpec);

        if (bluetoothStateIcon == null) {
            bluetoothStateIcon = new ImageView(getContext());
            bluetoothStateIcon.setScaleType(ScaleType.CENTER_INSIDE);
            LayoutParams bluetoothIconlp = new LayoutParams((int) (sizeWidth / 4 / 3.5f), (int) (sizeWidth / 4 / 3.5f * 2));
            addView(bluetoothStateIcon, bluetoothIconlp);
        }
        if (blueStatus == false)
            bluetoothStateIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.bluetooth_state_off_ico));
        else
            bluetoothStateIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.bluetooth_state_on_ico));
        measureChild(bluetoothStateIcon, widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(sizeWidth, sizeHeight);

    }

    public void start() {
        ring.start();
    }

    public void setInnerTextView(String str) {
        textView.setText(str);
    }

    public void start(int time) {
        ring.setAnimalTime(time);
        ring.start();
    }

    public void animatorSmoothEnd() {
        ring.accelerateAnimator();
    }

    public void setBlueOn(boolean aa) {
        blueStatus = aa;
    }

    public void changIcon() {
        if (bluetoothStateIcon == null)
            return;
        if (blueStatus == false)
            bluetoothStateIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.bluetooth_state_off_ico));
        else
            bluetoothStateIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.bluetooth_state_on_ico));
    }
}
