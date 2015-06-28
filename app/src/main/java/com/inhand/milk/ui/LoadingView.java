package com.inhand.milk.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.inhand.milk.R;

/**
 * LoadingView
 * Desc:自定义展示加载动画的页面
 * Team: InHand
 * User:Wooxxx
 * Date: 2015-03-03
 * Time: 12:47
 */
public class LoadingView extends LinearLayout {

    //用于显示加载动画
    private ImageView imageView;

    public LoadingView(Context context) {
        super(context);
        initView(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context ctx) {
        imageView = new ImageView(ctx);
        imageView.setLayoutParams(
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                )
        );
        this.addView(imageView);
    }

    /**
     * 执行异步任务，播放加载动画
     *
     * @param anim     加载帧动画
     * @param callback 动画加载回调接口
     */
    public void loading(AnimationDrawable anim, LoadingCallback callback) {
        anim = anim == null ? (AnimationDrawable) getResources().getDrawable(
                R.drawable.default_loading) : anim;
        // 设置图像的背景为加载动画
        imageView.setImageDrawable(anim);
        //执行加载任务
        LoadingTask task = new LoadingTask(anim, callback);
        task.execute();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.setGravity(Gravity.CENTER);

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

    }


    /**
     * LoadingTask
     * Desc:加载动画播放时执行的任务类
     */
    public class LoadingTask extends AsyncTask {
        private AnimationDrawable anim;
        private LoadingCallback callback;

        public LoadingTask(AnimationDrawable anim,
                           LoadingCallback callback) {
            this.anim = anim;
            this.callback = callback;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            callback.doInBackground();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LoadingView.this.setVisibility(VISIBLE);
            anim.start();
            callback.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            anim.stop();
            LoadingView.this.setVisibility(GONE);
            callback.onPostExecute();
        }
    }

    /**
     * LoadingCallBack
     * Desc:加载动画回调接口
     */
    public static interface LoadingCallback {
        public void doInBackground();

        public void onPreExecute();

        public void onPostExecute();
    }
}
