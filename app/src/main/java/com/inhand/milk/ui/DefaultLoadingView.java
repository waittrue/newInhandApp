package com.inhand.milk.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.inhand.milk.R;

/**
 * Created by Administrator on 2015/7/14.
 */
public class DefaultLoadingView extends Dialog {
    private String doc;
    private Drawable drawable;
    private ImageView imageView;
    private TextView textView;
    private Animation animation ;
    private boolean finish;
    private static final String TAG = "DEFAULTLOADomgView";
    private static final int TimeDelay = 800;
    public DefaultLoadingView(Context context,String doc,Drawable drawable) {
        super(context);
        this.doc = doc;
        this.drawable = drawable;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }
    public DefaultLoadingView(Context context,String doc){
        super(context);
        drawable = context.getResources().getDrawable(R.drawable.ic_launcher);
        this.doc = doc;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_view_layout);
        animation = AnimationUtils.loadAnimation(getContext(),R.anim.loading_view_animation);
        textView = (TextView)findViewById(R.id.ui_loading_textview);
        imageView = (ImageView)findViewById(R.id.ui_loading_imageview);
        textView.setText(doc);
        imageView.setImageDrawable(drawable);
    }
    public void setdoc(String doc){
        this.doc = doc;
        textView.setText(doc);
    }
    public void loading(LoadingCallback loadingCallback){
        LoadingTask task = new LoadingTask(loadingCallback);
        task.execute();
    }
    public void disppear(){
        this.dismiss();
    }
    public void disppear(Drawable drawable,String doc,int time){
        imageView.clearAnimation();
        if(drawable != null){
            imageView.setImageDrawable(drawable);
        }
        if(doc != null)
            textView.setText(doc);
        android.os.Handler handler = new android.os.Handler();
        Runnable runnable  = new Runnable() {
            @Override
            public void run() {
                DefaultLoadingView.this.dismiss();
            }
        };
        handler.postDelayed(runnable,time*1000);
    }
    private class LoadingTask extends AsyncTask{
        private LoadingCallback loadingCallback ;
        private DefaultLoadingView defaultLoadingView;
        public LoadingTask(LoadingCallback loadingCallback) {
            super();
            this.loadingCallback = loadingCallback;
            defaultLoadingView = DefaultLoadingView.this;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Log.i(TAG,"onpreExecute");
            finish = false;
            android.os.Handler handler = new android.os.Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (finish == true)
                        return;
                    DefaultLoadingView.this.setCanceledOnTouchOutside(false);
                    defaultLoadingView.show();
                    imageView.startAnimation(animation);
                }
            };
            handler.postDelayed(runnable, TimeDelay);
            loadingCallback.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o) {
            //Log.i(TAG,"onpostExecute");
            finish = true;
            DefaultLoadingView.this.setCanceledOnTouchOutside(true);
            super.onPostExecute(o);
            loadingCallback.onPostExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            //Log.i(TAG,"doinbackground");
            loadingCallback.doInBackground();
            return null;
        }
    }
    /**
     * LoadingCallBack
     * Desc:���ض����ص��ӿ�
     */
    public static interface LoadingCallback {
        public void doInBackground();

        public void onPreExecute();

        public void onPostExecute();
    }
}
