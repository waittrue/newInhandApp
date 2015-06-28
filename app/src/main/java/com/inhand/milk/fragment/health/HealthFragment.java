package com.inhand.milk.fragment.health;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.inhand.milk.R;
import com.inhand.milk.activity.HealthNutritionActivity;
import com.inhand.milk.fragment.TitleFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HealthFragment extends TitleFragment{
	private ListView listView;
	private List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView  = inflater.inflate(R.layout.health,null);
		
		setTitleview(getString(R.string.health_title_text), 0,null,null);
		setListView();
		setListViewClick();
		return mView;
	}

	private void setListView() {
		listView = (ListView)mView.findViewById(R.id.health_listview);
		int[] images = new int[]{R.drawable.health_icon_baby, R.drawable.health_icon_nutrition,
					R.drawable.health_icon_statistics,R.drawable.health_icon_statistics};
		String[] stringNames = getResources().getStringArray(R.array.health_item_name);
		String[] docs = getResources().getStringArray(R.array.health_item_doc);
		
		
		for (int i = 0; i< stringNames.length ;i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("image", images[i]);
			map.put("name", stringNames[i]);
			map.put("details", docs[i]);
			data.add(map);
		}
		
		SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), data, R.layout.health_item, 
					new String[]{"image","name","details"},
					new int[]{R.id.health_item_icon_container, R.id.health_item_name,R.id.health_item_details});
		listView.setAdapter(simpleAdapter);
	}
	
	private void setListViewClick(){
		OnItemClickListener onItemClickListener = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (position < 0 || position > data.size())
					return;
				
				Intent intent = new Intent();
				switch (position) {
				case 0:
					break;
				case 1:
					intent.setClass(HealthFragment.this.getActivity(), HealthNutritionActivity.class);
					startActivity(intent);
				default:
					break;
				}
			}
			
		};
		listView.setOnItemClickListener(onItemClickListener);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i("Health", "destroy");
	}
}

