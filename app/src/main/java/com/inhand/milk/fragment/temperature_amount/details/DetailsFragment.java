package com.inhand.milk.fragment.temperature_amount.details;


import android.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.inhand.milk.R;
import com.inhand.milk.dao.BaseDao;
import com.inhand.milk.dao.OneDayDao;
import com.inhand.milk.entity.OneDay;
import com.inhand.milk.entity.Record;
import com.inhand.milk.fragment.TitleFragment;
import com.inhand.milk.fragment.temperature_amount.details_once.DetailsOnceFragment;
import com.inhand.milk.helper.DateHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DetailsFragment  extends TitleFragment{

	private  PinnedHeaderListView  listView;
	private List<ItemEntity> listItem = new ArrayList<ItemEntity>();
    private List<Record>listRecord=new ArrayList<Record>();
	private boolean isTemperature = true;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView  = inflater.inflate(R.layout.temperature_amount_details,container, false);
		if (!isTemperature)
			setTitleview(getString(R.string.details_milk_title_string), 2,null,null);
		else 
			setTitleview(getString(R.string.details_temperature_title_string), 2,null,null);
		listView = (PinnedHeaderListView)mView.findViewById(R.id.detals_listView);
		listView.setHeadView(getHeadView());
		getData();
		PinnedListViewAdapter adapter = new PinnedListViewAdapter(this.getActivity().getApplicationContext(),
								listItem);
		listView.setAdapter( adapter);
		setItemOnclick();
		return mView;
	}
	public void setTemperature(boolean aa) {
		this.isTemperature = aa;
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	private View getHeadView(){
		View headview ;
		LayoutInflater inflater = getActivity().getLayoutInflater();
		headview = inflater.inflate(R.layout.temperature_amount_details_header, listView,false);	
		return headview;
	}

    /**
     * 从当前日期开始取出前10天的数据
     * 依次赋值title，picture，anount,time;
     *
     * title 为当次发生的时间，如3月4日
     *
     * isTemperature ==false   amount 代表奶量；奶量过低 picture为details_temperature_warning
     * 									奶量正常picture为details_temperature_normal
     *
     * isTemperature ==ture  amount 代表温度  如30~40° 当最低温度过低或者最高温度过高的时候picture 为details_amount_warning
     * 								否则为details_amount_normal
     *
     * time 为该次的发生时间
     * 最后加上
     * ItemEntity  itemEntity = new ItemEntity(title, picture, amount, time);
     * listItem.add(itemEntity);
     */
    private  void  getData(){
        final float highTemp=38;
        final float lowTemp=20;
        final float lowMikeVolume=100;
        OneDayDao oneDayDao=new OneDayDao(getActivity());
        Date dt=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfShow=new SimpleDateFormat("MM月dd日");
        List<Date>days= DateHelper.getPreDays(dt,10);
        for (Date date:days){
            String preday=sdf.format(date);
            //Log.d("preday",preday);
            final String title=sdfShow.format(date);
            oneDayDao.findOneDayFromDB(preday, new BaseDao.DBFindCallback<OneDay>() {
                @Override
                public void done(List<OneDay> oneDays) {
                    String amount,time;
                    Drawable picture=null;
                    if (oneDays != null && oneDays.size() > 0) {
                        float point[]=new float[1];
                        OneDay oneday=oneDays.get(0);
                        for(Record record : oneday.getRecords()){
                            listRecord.add(record);
                            if(isTemperature==true){
                                float maxTemp=getMaxTemp(record);
                                float minTemp=getMinTemp(record);
                                amount=maxTemp+" ~ "+minTemp+"°";
                                if(maxTemp<highTemp&&minTemp>lowTemp)
                                    picture = getResources().getDrawable(R.drawable.details_temperature_normal);
                                else
                                    picture=getResources().getDrawable(R.drawable.details_temperature_warning);
                            }
                            else{
                                float milkVolume=record.getVolume();
                                if(milkVolume<lowMikeVolume)
                                    picture =  getResources().getDrawable(R.drawable.details_amount_warning);
                                else
                                    picture =  getResources().getDrawable(R.drawable.details_amount_normal);
                                amount=milkVolume+"ml";
                            }
                            time=record.getBeginTime();
                            //Log.d("recordtime",time);
                            ItemEntity itemEntity=new ItemEntity(title,picture,amount,time);
                            listItem.add(itemEntity);
                        }
                    }
                }
            });
        }
    }
	
	private void setItemOnclick(){
		PinnedHeaderListView listView = (PinnedHeaderListView)mView.findViewById(R.id.detals_listView);
		OnItemClickListener listener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
				fragmentTransaction.hide(DetailsFragment.this);
                DetailsOnceFragment dof=new DetailsOnceFragment();
                Log.d("itemid",String.valueOf(id));
                Record record=listRecord.get((int)id);
                Bundle bundle=new Bundle();
                bundle.putSerializable("record",record);
                dof.setArguments(bundle);
				fragmentTransaction.add(R.id.Activity_fragments_container, dof,
						"detailsOnce");
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();
			}
		};
		listView.setOnItemClickListener(listener);
	}
	public class ItemEntity {
		private  String mTitle;
		private  Drawable  mPicture;
		private  String mAmount;
		private  String mTime;
		public ItemEntity(String title,Drawable picture,String amount,String time) {
			// TODO Auto-generated constructor stub
			mTime = time;
			mPicture = picture;
			mTitle =title;
			mAmount = amount;
		}
		
		public String getTitle() {
			return mTitle;
		}
		public Drawable getPicture() {
			return mPicture;
			
		}
		public String getAmount() {
			return mAmount;
			
		}
		public String getTime() {
			return mTime;
		}
	}
    public float getMinTemp(Record record) {
        float min_temp=record.getBeginTemperature();
        if (min_temp>record.getEndTemperature())min_temp=record.getEndTemperature();
        return min_temp;
    }
    public float getMaxTemp(Record record) {
        float max_temp=record.getBeginTemperature();
        if (max_temp<record.getEndTemperature())max_temp=record.getEndTemperature();
        return max_temp;
    }
}
	
