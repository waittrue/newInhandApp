package com.inhand.milk.fragment.user_info_settings;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.inhand.milk.R;
import com.inhand.milk.activity.UserInfoSettingsActivity;
import com.inhand.milk.fragment.TitleFragment;
import com.inhand.milk.utils.PinnerListViewAdapter;
import com.inhand.milk.utils.QuickListView;
import com.inhand.milk.utils.QuickListViewAdapter;
import com.inhand.milk.utils.ViewHolder;

import java.util.Map;

/**
 * Created by Administrator on 2015/7/6.
 */
public class UserInfoCityFragment extends TitleFragment {
    private UserInfoCityXmlPares pares;
    private QuickListView listView;
    private QuickListViewAdapter adapter;
    private static final String TAG = "UserInfoCityFragment";
    private static final String TITLE="title",CONTENT = "content";
    private static final String[] defualtString={"热","A","B","C","D","E","F","G","H","I","J","K","L","M","N",
            "O","P","Q","R","S","T","U","V","W","X","Y","Z"};
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.user_info_settngs_city, container, false);
        setTitleview("选择城市",2);
        initData();
        return mView;
    }
    private void initData(){
        pares = UserInfoCityXmlPares.getInstance();
        listView = (QuickListView)mView.findViewById(R.id.user_info_settings_city_listview);
        listView.setRightStrings(defualtString);
        listView.setHead(R.layout.user_info_settings_city_listview_item);

        adapter = new QuickListViewAdapter(getActivity()) {
            @Override
            public String getTitle(int position) {
                int len;
                len = pares.getCount();
                if(position<0 || position> len-1)
                    return null;
                String str = pares.getTitle(position);
                if(str != null && str.equals("热门城市"))
                    str = "热";
                return str;
            }
        };
        int len = pares.getCount();
        for(int i =0;i<len;i++){
            Map<String,Object> head = null;
            Map<String,Object> content;
            String str;
            str = pares.getTitle(i);
            if(str !=null ) {
                head = new ArrayMap<>();
                head.put(TITLE,str);
            }
            str = pares.getContent(i);
            content = new ArrayMap<>();
            content.put(CONTENT,str);
            adapter.addMap(head,content,i);
        }
        adapter.setConfigureView(new PinnerListViewAdapter.ConfigureView() {
            @Override
            public View configureHead(Map<String, Object> map, View convertView, int position) {
                if(convertView == null){
                      convertView = LayoutInflater.from(getActivity()).inflate(R.layout.user_info_settings_city_listview_item, null);
                }
                ViewHolder.get(convertView,R.id.user_info_settings_city_listview_content_container).setVisibility(View.GONE);
                TextView textView = ViewHolder.get(convertView,R.id.user_info_settings_city_listview_item_title_textview);
                textView.setText((String)map.get(TITLE));
                return convertView;
            }

            @Override
            public View configureContent(Map<String, Object> map, View convertView, int position) {
                if(convertView == null){
                    convertView = LayoutInflater.from(getActivity()).inflate(R.layout.user_info_settings_city_listview_item, null);
                    ViewHolder.get(convertView,R.id.user_info_settings_city_listview_title_container).setVisibility(View.GONE);
                }
                TextView textView = ViewHolder.get(convertView,R.id.user_info_settings_city_listview_item_content_textview);
                textView.setText((String)map.get(CONTENT));
                if(adapter.needHead(position+1) == true){
                    ViewHolder.get(convertView,R.id.user_info_settings_city_listview_content_divider).setVisibility(View.GONE);
                }
                else {
                    ViewHolder.get(convertView,R.id.user_info_settings_city_listview_content_divider).setVisibility(View.VISIBLE);
                }
                return convertView;
            }
        });
        listView.setAdapter(adapter);
        initListViewClick();
    }
    private void initListViewClick(){
        ListView innerListview = listView.getListView();
        innerListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((UserInfoSettingsActivity)getActivity()).setCity(pares.getContent(position));
                FragmentManager manager = getActivity().getFragmentManager();
                manager.popBackStack();
                manager.beginTransaction().commit();
            }
        });
    }
}
