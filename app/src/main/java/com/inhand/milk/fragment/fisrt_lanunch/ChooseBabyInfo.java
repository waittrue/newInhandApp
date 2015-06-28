package com.inhand.milk.fragment.fisrt_lanunch;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.inhand.milk.R;

import java.util.Calendar;

public class ChooseBabyInfo extends FirstLaunchFragment{

    private ImageView babySex,boyIcon,girlIcon,babyBirthday,babyName,girlselect,boyselect;
    private EditText nameEdit;
    private TextView birthdayTextView;
    private static final int animotionTime1 = 500;
    private int selectSex = 0;//1 boy,2 girl
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.first_launch_choose_baby_info, null);
        setTitle(getResources().getString(R.string.first_launch_choose_baby_info));
        initViews(view);
        setPre();
        setNext();
        return view;

    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        if (!hidden){
            setTitle(getResources().getString(R.string.first_launch_choose_baby_info));
            girlIcon.clearAnimation();
            boyIcon.clearAnimation();
            inAnimation();
            setPre();
            setNext();
        }
    }


    private void initViews(View view){
        babySex = (ImageView)view.findViewById(R.id.first_launch_babyinfo_sex_icon);
        boyIcon = (ImageView)view.findViewById(R.id.first_launch_baby_info_boy_icon);
        girlIcon = (ImageView)view.findViewById(R.id.first_launch_baby_info_girl_icon);
        babyBirthday = (ImageView)view.findViewById(R.id.first_launch_baby_info_birthday_icon);
        babyName = (ImageView)view.findViewById(R.id.first_launch_baby_info_name_icon);
        birthdayTextView = (TextView)view.findViewById(R.id.first_launch_baby_info_birthday_textView);
        nameEdit = (EditText)view.findViewById(R.id.first_launch_baby_info_name_edittext);
        girlselect = (ImageView)view.findViewById(R.id.first_launch_select_girl_icon);
        boyselect = (ImageView)view.findViewById(R.id.first_launch_select_boy_icon);
        nameEdit.clearFocus();
        birthdayTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDatePickerDialog();
                //birthdayTextView.requestFocus();
            }
        });



        nameEdit.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // TODO Auto-generated method stub

                if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER){
                    //setNextclick();
                    //lanunchBottom.setNextClickable(true);
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
                            getActivity().getApplicationContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(nameEdit.getWindowToken(),0);
                    nameEdit.clearFocus();
                    return true;
                }
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    nameEdit.clearFocus();
                    return true;
                }
                return false;
            }
        });

        girlselect.setAlpha(0f);
        boyselect.setAlpha(0f);
        girlIcon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                alphAnimation(girlselect, 1,100);
                boyselect.setAlpha(0f);
                selectSex = 2;
            }
        });
        boyIcon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                alphAnimation(boyselect, 1,100);
                girlselect.setAlpha(0f);
                selectSex = 1;
            }
        });

    }

    @Override
    protected Fragment nextFragment() {
        // TODO Auto-generated method stub
        return new ChooseWeight();
    }
    private void showDatePickerDialog(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener dateSetListener = new OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                birthdayTextView.setText(String.valueOf(year)+"年"+
                        String.valueOf(monthOfYear+1)+"月"+String.valueOf(dayOfMonth)+"日");
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                dateSetListener, year, month, day);
        datePickerDialog.show();
    }



    private void setPre(){
        lanunchBottom.setPreListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                lanunchBottom.NextLeftAnimation();
                enterPreFragment();
            }
        });
    }
    private void setNext(){
        lanunchBottom.setNextListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String str =  birthdayTextView.getText().toString();
                String name = nameEdit.getText().toString();
                String warning = "";
                if (selectSex == 0)
                    warning += "请选择宝宝性别";
                if (str.equals(getResources().getString(R.string.first_launch_choose_babysex_date)) ){
                    if(!warning.equals(""))
                        warning+="\n";
                    warning += "请选择宝宝的生日";
                }
                if (name.equals("") ){
                    if(!warning.equals(""))
                        warning+="\n";
                    warning += "请输入姓名";
                }
                if(!warning.equals("")){
                    Toast.makeText(getActivity().getApplicationContext(),
                            warning, 1000).show();
                    return ;
                }
                outAnimation();
            }
        });
    }

    @Override
    protected void inAnimation() {
        // TODO Auto-generated method stub
        alphAnimation(babyBirthday,1f,animotionTime1);
        alphAnimation(babyName,1f,animotionTime1);
        alphAnimation(babySex,1f,animotionTime1);
        scaleAnimation(birthdayTextView,1f,animotionTime1);
        scaleAnimation(nameEdit,1f,animotionTime1);


        Animation animation = new TranslateAnimation(-width/2, 0, 0, 0);
        animation.setDuration(animotionTime1);
        animation.setFillAfter(true);
        girlIcon.startAnimation(animation);

        animation = new TranslateAnimation(width/2, 0, 0, 0);
        animation.setDuration(animotionTime1);
        animation.setFillAfter(true);
        boyIcon.startAnimation(animation);
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
                if (selectSex == 1)
                    alphAnimation(boyselect, 1f, 100);
                else if (selectSex == 2)
                    alphAnimation(girlselect, 1f,100);
            }
        });

    }

    @Override
    protected void outAnimation() {
        // TODO Auto-generated method stub
        alphAnimation(babyBirthday,0f,animotionTime1);
        alphAnimation(babyName,0f,animotionTime1);
        alphAnimation(babySex,0f,animotionTime1);
        scaleAnimation(birthdayTextView,0f,animotionTime1);
        scaleAnimation(nameEdit,0f,animotionTime1);
        boyselect.setAlpha(0f);
        girlselect.setAlpha(0f);
        Animation animation = new TranslateAnimation(0, -width/2, 0, 0);
        animation.setDuration(animotionTime1);
        animation.setFillAfter(true);
        girlIcon.startAnimation(animation);

        animation = new TranslateAnimation(0, width/2, 0, 0);
        animation.setDuration(animotionTime1);
        animation.setFillAfter(true);
        boyIcon.startAnimation(animation);
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
                saveDate();
                if(selectSex == 1){
                    setExtraInfo("boy");
                }
                else if (selectSex == 2){
                    setExtraInfo("girl");
                }

            }
        });
    }
    /**
     * 存入 baby 的相关信息
     * str 表示 孩子的生日 格式如 ：2014年2月2号
     * name 表示孩子的姓名 格式如：张三
     * selectsex 表示孩子的性别 格式如 ：1为男，2为女
     *
     */
	private void saveDate(){
		String birthday =  birthdayTextView.getText().toString();
		String nickname = nameEdit.getText().toString();
        int sex=selectSex;
        baby.setBirthday(birthday);
        baby.setNickname(nickname);
        baby.setSex(sex);
//		Toast.makeText(getActivity().getApplicationContext(),
//				str +":"+name+":"+String.valueOf(selectSex), 1000).show();
	}
}
