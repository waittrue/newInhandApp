package com.inhand.milk.fragment.temperature_amount;

import java.util.ArrayList;
import java.util.List;

import com.inhand.milk.R;
import com.inhand.milk.fragment.TitleFragment;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class BaseStatistics extends TitleFragment {

	private int width;
	private LinearLayout.LayoutParams lineParams;
	private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private List<View> listView = new ArrayList<View>();
    private List<OnePaper> listOnePapers = new ArrayList<OnePaper>();
	private float lineLength ;
	private ImageView imageView; 
	private TextView day,week,month;
	private float lineScale;//���������������������ҳ�滬������ı�ֵ��
	private float lineCenterX;
	private int  selectColor;
	private int  noSelectColor;
	private int State = 0;//0��������״̬�ϣ�1���������ϣ�2����������
	@Override  
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
		 mView = inflater.inflate(R.layout.temperature_amount, container, false);
		 initVariable();
	     return mView;
    }  
	private void initVariable(){
		DisplayMetrics dm = new DisplayMetrics();
	    getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		width= dm.widthPixels;
	    lineLength = width/7;
    	lineScale = (1 - 0.11f*2)/3;
    	lineCenterX = width*0.11f +width*0.26f/2;
	    
    	selectColor = getResources().getColor(R.color.temperature_milk_checked_color);
    	noSelectColor = getResources().getColor(R.color.temperature_milk_unchecked_color);
    	
    	day = (TextView)mView.findViewById(R.id.temperature_milk_day_show);
    	week = (TextView)mView.findViewById(R.id.temperature_milk_week_show);
		month = (TextView)mView.findViewById(R.id.temperature_milk_month_show);
		
		addPaperView(width,listView, listOnePapers);
		initPaperView(mView);
		addClick(mView); 
		drawLine(mView);
		setTitle();
		viewPager.setCurrentItem(State);
	}
	protected abstract void setTitle();
	protected abstract void addPaperView(int width,List<View> listViews,List<OnePaper> onePapers);

   //����ͼ����ɫ�ĸı�
	private void changeTextColor(int arg){
		switch (arg) {
		case 0:
			day.setTextColor(selectColor);
			week.setTextColor(noSelectColor);
			month.setTextColor(noSelectColor);
			break;
		case 1:
			day.setTextColor(noSelectColor);
			week.setTextColor(selectColor);
			month.setTextColor(noSelectColor);
			break;
		case 2:
			day.setTextColor(noSelectColor);
			week.setTextColor(noSelectColor);
			month.setTextColor(selectColor);
			break;	
		}
	}
	
	//������еĵ���¼�
	private void addClick(View view){
		day.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				changeTextColor(0);
				viewPager.setCurrentItem(0, true);
			}
		});
		
		week.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				changeTextColor(1);
				viewPager.setCurrentItem(1, true);
			}
		});
		
		month.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				changeTextColor(2);
				viewPager.setCurrentItem(2, true);
			}
		});
	}
	
	
	//���������ƶ����ߣ�
	private void drawLine(View view){
		imageView = new ImageView(this.getActivity().getApplicationContext());
		imageView.setBackgroundColor(selectColor);
		LinearLayout linearLayout = (LinearLayout)view.findViewById(R.id.temperature_milk_time_line_layout);
		lineParams = new LinearLayout.LayoutParams((int)lineLength,
							LinearLayout.LayoutParams.MATCH_PARENT);
		lineParams.setMargins( (int)(lineCenterX - lineLength/2 + width*lineScale*State), 0, (int)( width - lineCenterX -lineLength/2 -  width*lineScale*State), 0);
		linearLayout.addView(imageView,lineParams);
		changeTextColor(State);
	}
	
	private void initPaperView(View view) {
		// TODO Auto-generated method stub
		viewPager  = (ViewPager)view.findViewById(R.id.horizontalScrollView1);    
		pagerAdapter = new PagerAdapter() {	
			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				// TODO Auto-generated method stub
				container.removeView(listView.get(position));
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				// TODO Auto-generated method stub
				container.addView(listView.get(position),0);
				return  listView.get(position);
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				// TODO Auto-generated method stub
				return arg0 == arg1;
			}
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return listView.size();
			}
		};
		viewPager.setAdapter(pagerAdapter);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				changeTextColor(arg0);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub				
				if(arg2 != 0 || arg1 !=0){//��ʼ��ʱ��  arg2=0  arg1=0  Ҫ�����״̬�ų���  
					float startx = arg0*width*lineScale;
					lineParams.setMargins( (int)( startx + lineCenterX -lineLength/2 +arg2*lineScale ), 0,
										(int)(	width - startx - lineCenterX - lineLength/2 -arg2*lineScale), 0);
					imageView.setLayoutParams(lineParams);
				}		
				State = arg0;
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub	
			}
		});
		
	}
	
	
	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		listOnePapers.get(State).start();
		for (OnePaper onePaper : listOnePapers) {
			onePaper.updateDate();
		}
	}
	
	
	

}
