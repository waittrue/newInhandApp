package com.inhand.milk.fragment.person_center.choose_milk;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.inhand.milk.R;
import com.inhand.milk.activity.MilkChooseActivity;
import com.inhand.milk.dao.PowderBrandDao;
import com.inhand.milk.entity.PowderBrand;
import com.inhand.milk.fragment.TitleFragment;
import com.inhand.milk.ui.DefaultLoadingView;
import com.inhand.milk.ui.PinnerListViewAdapter;
import com.inhand.milk.ui.QuickListView;
import com.inhand.milk.ui.QuickListViewAdapter;
import com.inhand.milk.utils.LocalGetAvFileCallBack;
import com.inhand.milk.utils.ViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/10.
 */
public class ChooseMilkFragment extends TitleFragment {
    private static final String TAG = "choosemilkFragment";
    private static final String TITLE_KEY = "title_key", CONTENT_IMAGE_KEY = "content_image_key",
            NAME_KEY = "name_key";
    private DefaultLoadingView loadingView;
    private DefaultLoadingView.LoadingCallback loadingCallback;
    private QuickListView quickListView;
    private QuickListViewAdapter adapter;
    private List<PowderBrand> powderBrands;
    private boolean success;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_choosemilk, container, false);
        setTitleview(getResources().getString(R.string.choose_milk_title), 2);
        loadingView = new DefaultLoadingView(getActivity(), "同步中");
        loadingCallback = new DefaultLoadingView.LoadingCallback() {
            @Override
            public void doInBackground() {
                PowderBrandDao powderBrandDao = new PowderBrandDao();
                try {
                    powderBrands = powderBrandDao.findFromCacheOrCloud();
                    if (powderBrands == null) {
                        Log.i(TAG, "奶粉同步失败");
                        success = false;
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i(TAG, "奶粉同步失败");
                    success = false;
                    return;
                }

                Log.i(TAG, "奶粉同步成功");
                success = true;
            }

            @Override
            public void onPreExecute() {
                success = false;
            }

            @Override
            public void onPostExecute() {
                if (success) {
                    initListview();
                    loadingView.dismiss();
                } else {
                    loadingView.disppear(null, "下载失败", 1000);
                }
            }
        };
        loadingView.loading(loadingCallback);
        return mView;
    }
    private void initListview() {
        if (powderBrands == null)
            return;
        quickListView = (QuickListView) mView.findViewById(R.id.choose_milk_fragment_quicklistview);
        adapter = new QuickListViewAdapter(getActivity()) {
            @Override
            public String getTitle(int position) {
                if (powderBrands == null)
                    return null;
                int count = powderBrands.size();
                if (position < 0 || position >= count)
                    return null;
                String name = powderBrands.get(position).getPinYinName();
                String title = name.substring(0, 1).toUpperCase();
                Log.i(TAG, title);
                return title;
            }
        };
        quickListView.setHead(R.layout.fragment_choosemilk_listview_item);
        int count = powderBrands.size();
        PowderBrand powder;
        for (int i = 0; i < count; i++) {
            powder = powderBrands.get(i);
            Map<String, Object> title = new HashMap<>();
            Map<String, Object> content = new HashMap<>();
            title.put(TITLE_KEY, powder.getPinYinName().substring(0, 1).toUpperCase());

            content.put(CONTENT_IMAGE_KEY, powder);
            content.put(NAME_KEY, powder.getZhName() + "/" + powder.getEnName());
            adapter.addMap(title, content, i);
        }
        adapter.setConfigureView(new PinnerListViewAdapter.ConfigureView() {
            @Override
            public View configureHead(Map<String, Object> map, View convertView, int position) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_choosemilk_listview_item, null);
                }
                ViewHolder.get(convertView, R.id.choose_milk_fragment_content).setVisibility(View.GONE);
                ViewHolder.get(convertView, R.id.choose_milk_fragment_title).setVisibility(View.VISIBLE);
                TextView textView = ViewHolder.get(convertView, R.id.choose_milk_fragment_listview_title_textview);
                textView.setText((String) map.get(TITLE_KEY));
                return convertView;
            }

            @Override
            public View configureContent(Map<String, Object> map, View convertView, int position) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_choosemilk_listview_item, null);

                }
                ViewHolder.get(convertView, R.id.choose_milk_fragment_content).setVisibility(View.VISIBLE);
                ViewHolder.get(convertView, R.id.choose_milk_fragment_title).setVisibility(View.GONE);
                if (adapter.needHead(position + 1) == true) {
                    ViewHolder.get(convertView, R.id.choose_milk_fragment_listview_divider).setVisibility(View.GONE);
                } else {
                    ViewHolder.get(convertView, R.id.choose_milk_fragment_listview_divider).setVisibility(View.VISIBLE);
                }
                TextView textView = ViewHolder.get(convertView, R.id.choose_milk_fragment_listview_textview);
                textView.setText((String) map.get(NAME_KEY));

                final ImageView imageView = ViewHolder.get(convertView, R.id.choose_milk_fragment_listview_milk_icon);
                PowderBrand powderBrand = (PowderBrand) map.get(CONTENT_IMAGE_KEY);
                powderBrand.getLogBitmap(new LocalGetAvFileCallBack() {
                    @Override
                    public void done(byte[] data, AVException e) {
                        if (data == null || e != null) {
                            return;
                        }
                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        imageView.setImageBitmap(bitmap);
                        adapter.notifyDataSetChanged();
                    }
                });
                return convertView;
            }
        });
        quickListView.setAdapter(adapter);

        quickListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((MilkChooseActivity) getActivity()).setPowderBrand(powderBrands.get(position));
                gotoSpecialFragment(new ChooseMilkPhaseFragment());
            }
        });
    }
}
