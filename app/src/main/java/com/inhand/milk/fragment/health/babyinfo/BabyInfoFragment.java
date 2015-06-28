package com.inhand.milk.fragment.health.babyinfo;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.inhand.milk.R;
import com.inhand.milk.fragment.TitleFragment;

import java.util.Calendar;

public class BabyInfoFragment extends TitleFragment{

	private TextView boy,girl,date;
	private EditText name;
	private ImageView button;
	private int sex = 0;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView =  inflater.inflate(R.layout.health_baby_info, container, false);
		initViews(mView);
		setTitleview(getResources().getString(R.string.baby_info_activity_baby_title_text), 2,null,null);
		return mView;
	}
	
	
	private void initViews(View view){
		boy = (TextView)view.findViewById(R.id.baby_info_activity_boy_textview);
		girl = (TextView)view.findViewById(R.id.baby_info_activity_girl_textview);
		sex = getBabySex();
		switch (sex) {
		case 0:
			boy.setBackgroundDrawable(getResources().getDrawable(R.drawable.baby_info_activity_textview_noselect_corner));
			girl.setBackgroundDrawable(getResources().getDrawable(R.drawable.baby_info_activity_textview_noselect_corner));
			boy.setTextColor(getResources().getColor(R.color.baby_info_activity_noselect_sex_color));
			girl.setTextColor(getResources().getColor(R.color.baby_info_activity_noselect_sex_color));
			break;
		case 1:
			boy.setBackgroundDrawable(getResources().getDrawable(R.drawable.baby_info_activity_textview_select_corner));
			girl.setBackgroundDrawable(getResources().getDrawable(R.drawable.baby_info_activity_textview_noselect_corner));
			boy.setTextColor(getResources().getColor(R.color.baby_info_activity_select_sex_color));
			girl.setTextColor(getResources().getColor(R.color.baby_info_activity_noselect_sex_color));
			break;
		case 2:
			boy.setBackgroundDrawable(getResources().getDrawable(R.drawable.baby_info_activity_textview_noselect_corner));
			girl.setBackgroundDrawable(getResources().getDrawable(R.drawable.baby_info_activity_textview_select_corner));
			boy.setTextColor(getResources().getColor(R.color.baby_info_activity_noselect_sex_color));
			girl.setTextColor(getResources().getColor(R.color.baby_info_activity_select_sex_color));
			break;
		}
		
		girl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boy.setBackgroundDrawable(getResources().getDrawable(R.drawable.baby_info_activity_textview_noselect_corner));
				girl.setBackgroundDrawable(getResources().getDrawable(R.drawable.baby_info_activity_textview_select_corner));
				boy.setTextColor(getResources().getColor(R.color.baby_info_activity_noselect_sex_color));
				girl.setTextColor(getResources().getColor(R.color.baby_info_activity_select_sex_color));
				sex =2;
			}
		});
		boy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boy.setBackgroundDrawable(getResources().getDrawable(R.drawable.baby_info_activity_textview_select_corner));
				girl.setBackgroundDrawable(getResources().getDrawable(R.drawable.baby_info_activity_textview_noselect_corner));
				boy.setTextColor(getResources().getColor(R.color.baby_info_activity_select_sex_color));
				girl.setTextColor(getResources().getColor(R.color.baby_info_activity_noselect_sex_color));
				sex = 1;
			}
		});
		date = (TextView)view.findViewById(R.id.baby_info_activity_choose_date_textview);
		String str = getBabyDate();
		if (str != null){
			date.setText(str);
			Log.i("baby_info", str);
		}
		date.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDatePickerDialog();
			}
		});
		
	
		name = (EditText)view.findViewById(R.id.baby_info_activity_choose_name_textview);
		str = getBabyName();
		if (str != null){
			name.setHint(str);
		}
		name.clearFocus();
		name.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				
				if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER){
					//setNextclick();
					//lanunchBottom.setNextClickable(true);
					InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
							 getActivity().getApplicationContext().INPUT_METHOD_SERVICE); 
			        imm.hideSoftInputFromWindow(name.getWindowToken(),0);
			        name.clearFocus();
			        return true; 
				}	
				return false;
			}
		});
		
		button = (ImageView)view.findViewById(R.id.baby_info_activity_save_button);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				save();
			}
		});

		
	}
	
	
	private void showDatePickerDialog(){
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		
		OnDateSetListener dateSetListener = new OnDateSetListener() {
			
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				// TODO Auto-generated method stub
				date.setText(String.valueOf(year)+"��"+
						String.valueOf(monthOfYear+1)+"��"+String.valueOf(dayOfMonth)+"��");
			}
		};
		DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
					dateSetListener, year, month, day);
		datePickerDialog.show();
	}


    /**
     *
     * @return 0:没有填写   1：boy 2：girl
     *
     */
    private int getBabySex(){
        return 1;
    }
    /**
     *
     * @return null :没有填写
     * 		str:格式 xxxx年xx月xx日
     *
     */
    private String getBabyDate(){
        String str = "2014年3月4日";
        return str;
    }
    /**
     *
     * @return null :没有填写
     */

    private String getBabyName(){
        String str = "悠悠";
        return str;
    }

    private void save(){
        String myname ;
        myname = name.getText().toString();
        if (myname.equals("")){
            myname = name.getHint().toString();
        }
        Toast.makeText(getActivity().getApplicationContext(), "sex :"+String.valueOf(sex)+
                date.getText().toString()+myname,1000).show();
    }
}
