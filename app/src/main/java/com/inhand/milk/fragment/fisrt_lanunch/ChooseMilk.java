package com.inhand.milk.fragment.fisrt_lanunch;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SaveCallback;
import com.inhand.milk.App;
import com.inhand.milk.R;
import com.inhand.milk.activity.MainActivity;
import com.inhand.milk.utils.LocalSaveTask;

public class ChooseMilk extends FirstLaunchFragment {

    private TextView text,doc;
    private ImageView brandLeft,brandRight;
    private static final int TIME = 500;
    private RelativeLayout iconsContainer;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.first_launch_choose_milk, null);
        setTitle(getResources().getString(R.string.first_launch_choose_milk_info));
        initView(view);
        setPre();
        setNext();
        Log.i("babySaveinCache", "oncreateview");
        return view;
    }

    private void initView(View view){
        text = (TextView)view.findViewById(R.id.first_launch_choose_milk_text);
        doc = (TextView)view.findViewById(R.id.first_launch_choose_mik_text_doc);
        iconsContainer = (RelativeLayout)view.findViewById(R.id.first_launch_choose_milk_icons_container);

        float milkWidth = getResources().getDimension(R.dimen.first_lanunch_milk_icon_width);
        float milkHeight = getResources().getDimension(R.dimen.first_lanunch_milk_icon_height);
        float height = getResources().getDimension(R.dimen.first_launch_milk_icons_container_height);
        float margin  = getResources().getDimension(R.dimen.first_lanunch_milk_icon_margin);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams((int)milkWidth,
                (int)milkHeight);

        lp.setMargins((int)( width/2 - milkWidth/2 + margin), (int)( (height - milkHeight)/2 ) ,
                (int)( width/2  -milkWidth/2 - margin), (int)( height/2 - milkHeight/2));
        brandLeft = new ImageView(this.getActivity().getApplicationContext());
        brandLeft.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        brandLeft.setBackgroundDrawable(getResources().getDrawable(R.drawable.first_launch_milkbrand_left_icon));
        iconsContainer.addView(brandLeft, lp);


        RelativeLayout.LayoutParams rp =  new RelativeLayout.LayoutParams((int)milkWidth,
                (int)milkHeight);
        rp.setMargins((int)( width/2 - milkWidth/2 - margin), (int)( (height - milkHeight)/2 ) ,
                (int)( width/2 - milkWidth/2 + margin), (int)( height/2 - milkHeight/2));
        brandRight = new ImageView(this.getActivity().getApplicationContext());
        brandRight.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        brandRight.setBackgroundDrawable(getResources().getDrawable(R.drawable.first_launch_milkbrand_right_ico));
        iconsContainer.addView(brandRight, rp);


    }
    @Override
    protected Fragment nextFragment() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void inAnimation() {
        // TODO Auto-generated method stub

        TranslateAnimation translateAnimation = new TranslateAnimation(-width/2, 0, 0, 0);
        translateAnimation.setDuration(TIME);
        translateAnimation.setFillAfter(true);
        brandLeft.startAnimation(translateAnimation);

        translateAnimation = new TranslateAnimation(width/2, 0, 0, 0);
        translateAnimation.setDuration(TIME);
        translateAnimation.setFillAfter(true);
        brandRight.startAnimation(translateAnimation);


        alphAnimation(text, 1f, TIME);
        alphAnimation(doc, 1f, TIME);
    }

    @Override
    protected void outAnimation() {
        // TODO Auto-generated method stub
        TranslateAnimation translateAnimation = new TranslateAnimation(0, -width/2, 0, 0);
        translateAnimation.setDuration(TIME);
        translateAnimation.setFillAfter(true);
        brandLeft.startAnimation(translateAnimation);

        translateAnimation = new TranslateAnimation(0,width/2, 0, 0);
        translateAnimation.setDuration(TIME);
        translateAnimation.setFillAfter(true);
        brandRight.startAnimation(translateAnimation);

        alphAnimation(text, 0f, TIME);
        alphAnimation(doc, 0f, TIME);
        translateAnimation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
                save();
                getActivity().finish();
            }
        });
    }

    private void enterNextActivity(){
        Intent intent  = new Intent();
        intent.setClass(getActivity(), MainActivity.class);
        getActivity().startActivity(intent);
        //getActivity().finish();
    }


    private void setPre(){
        lanunchBottom.setPreListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                lanunchBottom.finishDisapper();
                enterPreFragment();

            }
        });
    }
    private void setNext(){
        lanunchBottom.setFinishListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                outAnimation();
            }
        });
    }
    private void save(){
       // enterNextActivity();

        Log.i("babySaveinCache", "save");
        babyInfo.setBaby(baby);
        babyInfo.saveInCloud(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e != null)
                    return;
                babyInfo.saveInCache(App.getAppContext(), new LocalSaveTask.LocalSaveCallback() {
                    @Override
                    public void done() {
                        Log.i("babyInfo_save","success");
                    }
                });
            }
        });


        baby.saveInCloud(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e != null) {
                    Toast.makeText(App.getAppContext(), "存入失败，可能没有网络，请检查", Toast.LENGTH_LONG).show();
                    return;
                }
                baby.saveInCache(App.getAppContext(), new LocalSaveTask.LocalSaveCallback() {
                    @Override
                    public void done() {
                        Log.i("baby_save","success");
                        enterNextActivity();
                    }
                });

            }
        });

    }
}
