package com.inhand.milk.fragment.person_center.choose_milk;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

import com.inhand.milk.App;
import com.inhand.milk.R;
import com.inhand.milk.fragment.TitleFragment;
import com.inhand.milk.ui.PullRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/11.
 */
public class ChooseMilkPhaseFragment extends TitleFragment {
    private static final String KEY = "name_key";
    private PullRefreshListView pullToRefreshListView;
    private boolean interrupt;
    private Handler handler;
    private List<Map<String, Object>> data = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_choose_milk_phase, container, false);
        setTitleview(getResources().getString(R.string.choose_milk_phase_title), 2);
        initlistview();
        return mView;
    }

    private void initlistview() {
        for (int i = 0; i < 30; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put(KEY, "a" + String.valueOf(i));
            data.add(map);
        }
        pullToRefreshListView = (PullRefreshListView) mView.findViewById(R.id.choose_milk_phase_listview);
        final SimpleAdapter simpleAdapter = new SimpleAdapter(App.getAppContext(), data, R.layout.fragment_choose_milk_phase_listview_item,
                new String[]{KEY}, new int[]{R.id.choose_milk_phase_listview_item_name});
        pullToRefreshListView.setAdapter(simpleAdapter);
        handler = new Handler();
        pullToRefreshListView.setOnRefreshListener(new PullRefreshListView.OnRefreshListener() {
            @Override
            public void doInBackground() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        interrupt = true;
                    }
                }, 10000);
                interrupt = false;

                while (true) {
                    if (interrupt)
                        break;
                }
            }

            @Override
            public void Refresh() {
                for (int i = 0; i < 10; i++) {
                    Map<String, Object> map = new HashMap<>();
                    map.put(KEY, "b" + String.valueOf(i));
                    data.add(map);
                }
                simpleAdapter.notifyDataSetChanged();
            }
        });
    }
}
