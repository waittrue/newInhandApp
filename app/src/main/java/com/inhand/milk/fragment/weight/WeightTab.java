package com.inhand.milk.fragment.weight;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inhand.milk.utils.ObservableHorizonScrollView;

/**
 * Created by Administrator on 2015/6/6.
 * 实现weight那个图里面  最上面那个Tab
 */
public class WeightTab extends ObservableHorizonScrollView {
    private int num = 0;
    private int textViewWidth;
    private int textColor = Color.WHITE;
    private int lastX;
    private float textSize;
    private ViewGroup.LayoutParams lp;
    private LinearLayout linearLayout;
    private Handler handler ;
    private Runnable runnable;
    private static final float Alpha_Center = 1f,Alpha_Minor = 0.5f, Alpha_Most =0.1f;
    private StopLisetner stopLisetner;

    public interface StopLisetner {
        void stopLisetner(int position);
    }
    public WeightTab(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initVaribles();
    }

    public WeightTab(Context context, AttributeSet attrs) {
        super(context, attrs);
        initVaribles();
    }

    public WeightTab(Context context) {
        super(context);
        initVaribles();
    }

    public void setStopLisetner(StopLisetner stopLisetner) {
        this.stopLisetner = stopLisetner;
    }
    private void initVaribles(){
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);

        textViewWidth = wm.getDefaultDisplay().getWidth() /4;
        textSize = textViewWidth / 5;
        lp = new ViewGroup.LayoutParams(textViewWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        linearLayout = new LinearLayout(this.getContext());
        this.addView(linearLayout,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        this.setOverScrollMode(OVER_SCROLL_NEVER);
        setFingStopListener();

    }
    private void setFingStopListener(){
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                int x = WeightTab.this.getScrollX();
                if (lastX == x) {
                    handerStop(x);
                    handler.removeCallbacks(runnable);
                }
                else {
                    lastX = x;
                    handler.postDelayed(runnable,20);
                }

            }
        };

        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    handler.post(runnable);
                }
                return false;
            }
        });
    }

    public  void setTextColor(int color){
        this.textColor = color;
    }
    public void setTabNum(int num){
        if (num < 0)
            num = 0;
        this.num = num ;
    }



    public  void initTabs(){
        TextView  textView ;
        linearLayout.removeAllViews();
        initEnd();
        for (int i=0;i<num;i++) {
            textView = new TextView(this.getContext());
            initTextView(textView,i);
            linearLayout.addView(textView);
        }
        initEnd();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // WeightTab.this.fullScroll(FOCUS_RIGHT);
                if (WeightTab.this.getWidth() == 0) {
                    handler.postDelayed(this, 20);
                } else {
                    int tempX = postionToLocal(num - 1);
                    scrollTo(tempX, getScrollY());
                    handler.removeCallbacks(this);
                }
            }
        }, 10);
    }

    private void handerStop(int x){
        int position = localToPostion(x);
        int tempX = postionToLocal(position);
        smoothScrollTo(tempX, getScrollY());
        if (stopLisetner != null)
            stopLisetner.stopLisetner(position);
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);

        int position = localToPostion(x);
        View child;
        position = position+2;
        child  = linearLayout.getChildAt(position);
        child.setAlpha(Alpha_Center);

        child  = linearLayout.getChildAt(position-1);
        child.setAlpha(Alpha_Minor);

        child = linearLayout.getChildAt(position + 1);
        child.setAlpha(Alpha_Minor);

        child = linearLayout.getChildAt(position - 2);
        child.setAlpha(Alpha_Most);

        child = linearLayout.getChildAt(position + 2);
        child.setAlpha(Alpha_Most);

    }

    //根据x，y算出应该把第几个textview放在中间;
    private int localToPostion(int x){
        return (int)((float)x/textViewWidth + 0.5);
    }
    //根据第几个，返回位置x
    private int postionToLocal(int postion){
        return postion*textViewWidth;
    }

    private void initTextView(TextView v,int position){
        v.setText(String.valueOf(position)+"个月");
        v.setTextColor(textColor);
        v.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
        v.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        v.setLayoutParams(lp);
        v.setAlpha(Alpha_Most);
    }
    private void initEnd(){
        TextView v = new TextView(this.getContext());
        v.setText("");
        v.setLayoutParams(lp);
        v.setAlpha(Alpha_Most);
        linearLayout.addView(v);

        v = new TextView(this.getContext());
        v.setText("");
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(textViewWidth/2, ViewGroup.LayoutParams.MATCH_PARENT);
        v.setLayoutParams(lp);
        v.setAlpha(Alpha_Most);
        linearLayout.addView(v);
    }
}

