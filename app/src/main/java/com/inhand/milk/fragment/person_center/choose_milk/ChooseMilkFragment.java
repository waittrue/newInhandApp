package com.inhand.milk.fragment.person_center.choose_milk;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inhand.milk.App;
import com.inhand.milk.R;
import com.inhand.milk.dao.PowderDao;
import com.inhand.milk.entity.Powder;
import com.inhand.milk.fragment.TitleFragment;
import com.inhand.milk.ui.DefaultLoadingView;
import com.inhand.milk.ui.LoadingView;
import com.inhand.milk.ui.PinnerListViewAdapter;
import com.inhand.milk.ui.QuickListView;
import com.inhand.milk.ui.QuickListViewAdapter;
import com.inhand.milk.utils.ViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/10.
 */
public class ChooseMilkFragment extends TitleFragment {
    private static final  String TAG = "choosemilkFragment";
    private static final String TITLE_KEY = "title_key",CONTENT_IMAGE_KEY = "content_image_key",
                        NAME_KEY = "name_key";
    private DefaultLoadingView loadingView;
    private DefaultLoadingView.LoadingCallback loadingCallback;
    private QuickListView quickListView;
    private QuickListViewAdapter adapter;
    private List<Powder> powders;
    private boolean success;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_choosemilk, container, false);
        setTitleview(getResources().getString(R.string.choose_milk_title),2);
        loadingView = new DefaultLoadingView(getActivity(), "同步中");
        loadingCallback = new DefaultLoadingView.LoadingCallback() {
            @Override
            public void doInBackground() {
                PowderDao powderDao = new PowderDao();
                try {
                    powders = powderDao.findFromCloud();
                    if(powders == null) {
                        success = false;
                        return;
                    }
                }catch (Exception e){
                    Log.i(TAG,"奶粉同步失败");
                    success = false;
                    return;
                }
                success =true;
            }

            @Override
            public void onPreExecute() {
                success =false;
            }

            @Override
            public void onPostExecute() {
                if(success)
                    initListview();
            }
        };
        loadingView.loading(loadingCallback);
        return mView;
    }
    private void initListview(){
        if(powders == null)
            return ;
        quickListView = (QuickListView)mView.findViewById(R.id.choose_milk_fragment_quicklistview);
        adapter = new QuickListViewAdapter(getActivity()) {
            @Override
            public String getTitle(int position) {
                if(powders == null)
                    return null;
                int count = powders.size();
                if(position<0|| position >=count)
                    return null;
                String name = powders.get(position).getPinyinName();
                String title = name.substring(0,1).toUpperCase();
                Log.i(TAG,title);
                return title;
            }
        };
        int count = powders.size();
        Powder powder;
        for (int i=0;i<count;i++){
            powder = powders.get(i);
            Map<String,Object> title= new HashMap<>();
            Map<String,Object> content = new HashMap<>();
            title.put(TITLE_KEY,powder.getPinyinName().substring(0,1).toUpperCase());

            //content.put(CONTENT_IMAGE_KEY,);
            content.put(NAME_KEY,powder.getZhName());
            adapter.addMap(title,content,i);
        }
        adapter.setConfigureView(new PinnerListViewAdapter.ConfigureView() {
            @Override
            public View configureHead(Map<String, Object> map, View convertView, int position) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_choosemilk_listview_item, null);
                }
                ViewHolder.get(convertView,R.id.choose_milk_fragment_title).setVisibility(View.GONE);
                TextView textView = ViewHolder.get(convertView,R.id.choose_milk_fragment_listview_title_textview);
                textView.setText((String)map.get(TITLE_KEY));
                return convertView;
            }

            @Override
            public View configureContent(Map<String, Object> map, View convertView, int position) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_choosemilk_listview_item, null);
                }
                ViewHolder.get(convertView,R.id.choose_milk_fragment_content).setVisibility(View.GONE);
                TextView textView = ViewHolder.get(convertView,R.id.choose_milk_fragment_listview_textview);
                textView.setText((String)map.get(NAME_KEY));
                return convertView;
            }
        });
        quickListView.setAdapter(adapter);
    }
}
