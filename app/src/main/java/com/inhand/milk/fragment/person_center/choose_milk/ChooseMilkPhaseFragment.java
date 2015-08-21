package com.inhand.milk.fragment.person_center.choose_milk;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.inhand.milk.App;
import com.inhand.milk.R;
import com.inhand.milk.activity.MilkChooseActivity;
import com.inhand.milk.dao.PowderSerieDao;
import com.inhand.milk.entity.PowderBrand;
import com.inhand.milk.entity.PowderSerie;
import com.inhand.milk.fragment.TitleFragment;
import com.inhand.milk.ui.DefaultLoadingView;
import com.inhand.milk.ui.PullRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/11.
 */
public class ChooseMilkPhaseFragment extends TitleFragment {
    public static final String TAG = "chooseMilkPhase";
    private static final String KEY = "name_key";
    private PullRefreshListView pullToRefreshListView;
    private boolean interrupt;
    private Handler handler;
    private List<Map<String, Object>> data = new ArrayList<>();
    private PowderBrand powderBrand;
    List<PowderSerie> powderSeries = null;
    private boolean success;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        powderBrand = ((MilkChooseActivity) getActivity()).getPowderBrand();
        mView = inflater.inflate(R.layout.fragment_choose_milk_phase, container, false);
        setTitleview(getResources().getString(R.string.choose_milk_phase_title), 2);
        initDataAndListview();
        initCurrentMilk();
        return mView;
    }

    private void initDataAndListview() {
        final DefaultLoadingView loadingView = new DefaultLoadingView(getActivity(), "加载中");
        DefaultLoadingView.LoadingCallback callback = new DefaultLoadingView.LoadingCallback() {
            @Override
            public void doInBackground() {
                PowderSerieDao powderSerieDao = new PowderSerieDao();

                try {
                    powderSeries = powderSerieDao.getFromCloudByPowderBrand(powderBrand);
                } catch (AVException e) {
                    success = false;
                    return;
                }
                if (powderSeries == null)
                    return;
                data.clear();
                for (PowderSerie powderSerie : powderSeries) {
                    Map<String, Object> map = new HashMap<>();
                    map.put(KEY, getDoc(powderSerie));
                    data.add(map);
                }
            }

            @Override
            public void onPreExecute() {
                success = true;
            }

            @Override
            public void onPostExecute() {
                if (success) {
                    loadingView.dismiss();
                    initlistview();
                } else {
                    loadingView.disppear(null, "加载失败", 1);
                }
            }
        };
        loadingView.loading(callback);
    }
    private void initlistview() {
        pullToRefreshListView = (PullRefreshListView) mView.findViewById(R.id.choose_milk_phase_listview);
        final SimpleAdapter simpleAdapter = new SimpleAdapter(App.getAppContext(), data, R.layout.fragment_choose_milk_phase_listview_item,
                new String[]{KEY}, new int[]{R.id.choose_milk_phase_listview_item_name});
        pullToRefreshListView.setAdapter(simpleAdapter);
        pullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog alert = new AlertDialog.Builder(getActivity()).create();
                alert.setMessage("选择:" + getDoc(powderSeries.get(position - 1)));
                alert.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        ((MilkChooseActivity) getActivity()).setPowderSerie(powderSeries.get(position));
                        ((MilkChooseActivity) getActivity()).save();
                        getActivity().finish();
                    }
                });

                alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                    }
                });
                alert.show();
            }
        });
    }

    private String getDoc(PowderSerie powderSerie) {
        return powderSerie.getName() + "   (" + powderSerie.getNameDoc() + ")";
    }

    private void initCurrentMilk() {
        TextView textView = (TextView) mView.findViewById(R.id.choose_milk_phase_fragment_current_milk);
        PowderBrand powderBrand = ((MilkChooseActivity) getActivity()).getPowderBrand();
        String text = "当前：";
        text += powderBrand.getZhName() + "/" + powderBrand.getEnName();
        textView.setText(text);
    }

}
