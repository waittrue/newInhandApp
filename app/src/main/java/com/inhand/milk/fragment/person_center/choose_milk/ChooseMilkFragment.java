package com.inhand.milk.fragment.person_center.choose_milk;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inhand.milk.App;
import com.inhand.milk.R;
import com.inhand.milk.dao.PowderDao;
import com.inhand.milk.fragment.TitleFragment;
import com.inhand.milk.ui.DefaultLoadingView;
import com.inhand.milk.ui.LoadingView;

/**
 * Created by Administrator on 2015/8/10.
 */
public class ChooseMilkFragment extends TitleFragment {
    private DefaultLoadingView loadingView;
    private DefaultLoadingView.LoadingCallback loadingCallback;
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
            }

            @Override
            public void onPreExecute() {

            }

            @Override
            public void onPostExecute() {

            }
        };
        return mView;
    }
}
