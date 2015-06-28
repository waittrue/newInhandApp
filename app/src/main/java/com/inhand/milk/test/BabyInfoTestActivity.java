package com.inhand.milk.test;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SaveCallback;
import com.inhand.milk.App;
import com.inhand.milk.R;
import com.inhand.milk.activity.BaseActivity;
import com.inhand.milk.entity.Baby;
import com.inhand.milk.entity.Base;
import com.inhand.milk.entity.Weight;

import java.util.Calendar;

/**
 * BabyInfoTestActivity
 * Desc:
 * Team: InHand
 * User:Wooxxx
 * Date: 2015-03-19
 * Time: 08:10
 */
public class BabyInfoTestActivity extends BaseActivity {
    private EditText nicknameTxt;
    private EditText birthdayTxt;
    private EditText heightTxt;
    private EditText weightTxt;
    private EditText headSizeTxt;
    private Button submit;
    private Button show;
    private TextView showTxt;
    private Button logOutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_baby);
        nicknameTxt = (EditText) findViewById(R.id.nickname);
        birthdayTxt = (EditText) findViewById(R.id.birthday);
        heightTxt = (EditText) findViewById(R.id.height);
        weightTxt = (EditText) findViewById(R.id.weight);
        headSizeTxt = (EditText) findViewById(R.id.head_size);
        submit = (Button) findViewById(R.id.submit);
        show = (Button) findViewById(R.id.show);
        showTxt = (TextView) findViewById(R.id.show_txt);
        logOutBtn = (Button) findViewById(R.id.log_out_btn);
        setListeners();
    }

    private void setListeners() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname = nicknameTxt.getText().toString();
                String birthday = birthdayTxt.getText().toString();
                int height = Integer.parseInt(heightTxt.getText().toString());
                final int weight = Integer.parseInt(weightTxt.getText().toString());
                int headSize = Integer.parseInt(headSizeTxt.getText().toString());
                final Baby baby = new Baby();
                baby.setSex(1);
                baby.setNickname(nickname);
                baby.setHeight(height);
                baby.setHeadSize(headSize);
                baby.setBirthday(birthday);
                final Weight weight1=new Weight();
                weight1.setMoonAge(5);
                weight1.setWeight(weight);
                weight1.save(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e != null) {
                            Log.d("weighterror", e.toString());
                            //Log.d("weightObjectId",weight1.getObjectId());
                        }
                        Log.d("weightObjectId",weight1.getObjectId());
                        baby.addWeight(weight1);
                        //写入云端后缓存至本地
                        baby.save(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e == null) {
                                    Toast.makeText(BabyInfoTestActivity.this, "同步成功", Toast.LENGTH_SHORT)
                                            .show();
                                    baby.saveInCache(BabyInfoTestActivity.this, new Base.CacheSavingCallback() {
                                        @Override
                                        public void done() {
                                            Toast.makeText(BabyInfoTestActivity.this, "缓存成功", Toast.LENGTH_SHORT)
                                                    .show();
                                        }
                                    });
                                }
                                else{
                                    Log.d("babyerror",e.toString());
                                }
                            }
                        });
                    }
                });
                //Log.d("weightObjectId11",weight1.getObjectId());



            }
        });

        birthdayTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                new DatePickerDialog(BabyInfoTestActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                birthdayTxt.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
                            }
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Baby currentBaby = App.getCurrentBaby();
                showTxt.setText(currentBaby.getNickname());
            }
        });

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.getCurrentUser().logOut(BabyInfoTestActivity.this);
            }
        });
    }

}
