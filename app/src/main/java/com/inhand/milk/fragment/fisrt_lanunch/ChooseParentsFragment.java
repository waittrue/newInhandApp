package com.inhand.milk.fragment.fisrt_lanunch;

import android.app.Fragment;
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
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SaveCallback;
import com.inhand.milk.App;
import com.inhand.milk.R;
import com.inhand.milk.entity.User;

public class ChooseParentsFragment extends FirstLaunchFragment {
	private ImageView mother,father,motherName,fatherName,motherSelect,fatherSelect;
	private static final int animotionTime1 = 1000;
	private static final int animotionTime2 = 300;
	private int State = 0;//1����ְ�  2��������
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.first_launch_choose_parents, null);

        mother = (ImageView) (view).findViewById(R.id.choose_parents_mother_imageview);
        father = (ImageView) (view).findViewById(R.id.choose_parents_father_imageView);
        motherName = (ImageView)(view.findViewById(R.id.choose_pratens_mother_name_imageview));
        fatherName = (ImageView)(view.findViewById(R.id.choose_parents_father_name_imageview));
        motherSelect = (ImageView)(view.findViewById(R.id.first_launch_parents_mother_selects));
        fatherSelect = (ImageView)(view.findViewById(R.id.first_launch_parents_father_selects));
        fatherSelect.setAlpha(0f);
        motherSelect.setAlpha(0f);
        fatherName.setAlpha(0f);
        motherName.setAlpha(0f);
        setOnclick();
        setTitle(getResources().getString(R.string.first_launch_choose_parents));
		return view;
	}

    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        if(!hidden){
            setTitle(getResources().getString(R.string.first_launch_choose_parents));
            fatherName.setAlpha(0f);
            motherName.setAlpha(0f);
            fatherName.clearAnimation();
            motherName.clearAnimation();
            inAnimation();
            setOnclick();
        }
    }

    private OnClickListener chooseMather  = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub

            chooseMother();
            State = 2;

        }
    };

    private OnClickListener chooseFather  = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub

            chooseFather();
            State = 1;

        }
    };

    private OnClickListener nextClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (State == 0 ){
                Toast.makeText(getActivity().getApplicationContext(),
                        "请选择爸爸或者妈妈", 1000).show();
                return ;
            }
            lanunchBottom.NextRightAnimation();
            motherSelect.setAlpha(0f);
            fatherSelect.setAlpha(0f);
            outAnimation();
            save();
        }
    };

    private void setOnclick(){
        mother.setOnClickListener(chooseMather);
        father.setOnClickListener(chooseFather);
        lanunchBottom.setNextListener(nextClickListener);
    }


	@Override
	protected Fragment nextFragment() {
		// TODO Auto-generated method stub
		return new ChooseBabyInfo();
	}
	
	private void chooseMother(){
		if ( State != 2 ){
			alphAnimation(motherSelect, 1f, 100);
			fatherSelect.clearAnimation();
			fatherSelect.setAlpha(0f);
		}
	}
	private void chooseFather(){
		if ( State != 1){
			alphAnimation(fatherSelect, 1f, 100);
			motherSelect.clearAnimation();
			motherSelect.setAlpha(0f);
			Log.i("parents", String.valueOf( fatherName.getAlpha()) );
		}
	}

    @Override
    protected void inAnimation() {
        // TODO Auto-generated method stub
        Log.i("parents", "start inAnimation");


        Animation animation = new TranslateAnimation(width/2, 0, 0, 0);
        animation.setDuration(animotionTime1);
        animation.setFillAfter(true);
        father.startAnimation(animation);

        animation = new TranslateAnimation(-width/2, 0, 0, 0);
        animation.setDuration(animotionTime1);
        animation.setFillAfter(true);
        mother.startAnimation(animation);

        animation.setAnimationListener(new AnimationListener() {

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
                alphAnimation(fatherName, 1f, animotionTime1);
                alphAnimation(motherName, 1f, animotionTime1);
                if (State == 1)
                    fatherSelect.setAlpha(1f);
                else if (State == 2)
                    motherSelect.setAlpha(1f);
            }

            private void Delayed(int animotiontime1) {
                // TODO Auto-generated method stub

            }
        });
    }


    @Override
    protected void outAnimation() {
        // TODO Auto-generated method stub


        Animation animation = new TranslateAnimation(0, width/2, 0, 0);
        animation.setDuration(animotionTime1);
        animation.setFillAfter(true);
        father.startAnimation(animation);
        fatherName.startAnimation(animation);

        animation = new TranslateAnimation(0,-width/2, 0, 0);
        animation.setDuration(1000);
        animation.setFillAfter(true);
        mother.startAnimation(animation);
        motherName.startAnimation(animation);
        animation.setAnimationListener(new AnimationListener() {

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
                enterNextFragmet();
            }
        });


    }


    /*
     * state��ʾ 1��ʾ�ְ�2��ʾ����
     */
    private void save(){
		Toast.makeText(this.getActivity().getApplicationContext(),
				String.valueOf(State), 1000).show();
    /*    User currentUser=App.getCurrentUser();
        currentUser.setSex(State);
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e != null) {
                    Toast.makeText(getActivity(), "存入用户失败", Toast.LENGTH_SHORT).show();
                }
                else{
                    outAnimation();
                    //AVUser.changeCurrentUser(currentUser,true);

                }
            }
        });
        */
    }
}