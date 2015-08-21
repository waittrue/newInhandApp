package com.inhand.milk.fragment.person_center.choose_milk;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.inhand.milk.R;
import com.inhand.milk.activity.MilkChooseActivity;
import com.inhand.milk.entity.PowderBrand;
import com.inhand.milk.entity.PowderDetail;
import com.inhand.milk.entity.PowderSerie;
import com.inhand.milk.fragment.TitleFragment;
import com.inhand.milk.ui.ButtonB;

/**
 * Created by Administrator on 2015/8/21.
 * 作者：大力
 * 时间：2015/8/21
 * 描述：
 */
public class ChooseMilkShowFragment extends TitleFragment {
    private PowderBrand powderBrand;
    private PowderDetail powderDetail;
    private PowderSerie powderSerie;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_choose_milk_show, container, false);
        setTitleview(getResources().getString(R.string.choose_milk_show_title), 2);
        powderBrand = ((MilkChooseActivity) getActivity()).getPowderBrand();
        powderDetail = ((MilkChooseActivity) getActivity()).getPowderDetail();
        powderSerie = ((MilkChooseActivity) getActivity()).getPowderSerie();
        initBrand();
        initButton();
        initDetails();
        return mView;
    }

    private void initBrand() {
        ImageView imageView = (ImageView) mView.findViewById(R.id.choose_milk_show_fragment_brand_icon_imageview);
        TextView detailsName = (TextView) mView.findViewById(R.id.choose_milk_show_fragment_brand_serie_details_name_textview);
        TextView series = (TextView) mView.findViewById(R.id.choose_milk_show_fragment_brand_serie_name_textview);
        TextView name = (TextView) mView.findViewById(R.id.choose_milk_show_fragment_brand_name_textview);
        name.setText(powderBrand.getZhName() + "/" + powderBrand.getEnName());
        series.setText(powderSerie.getName());
        detailsName.setText(String.valueOf(powderSerie.getPhase()) + "段" + " （适合" + powderSerie.getForAge() + "个月）");

        byte[] bytes = powderBrand.getImageBytes();
        if (bytes == null)
            return;
        imageView.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
    }

    private void initButton() {
        ButtonB buttonB = (ButtonB) mView.findViewById(R.id.choosemilk_show_fragment_button);
        buttonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSpecialFragment(new ChooseMilkFragment());
            }
        });
    }

    private void initDetails() {
        TextView spoonCount = (TextView) mView.findViewById(R.id.choose_milk_show_fragment_spoon_count);
        TextView temperature = (TextView) mView.findViewById(R.id.choose_milk_show_fragment_temperature);
        TextView drinkcount = (TextView) mView.findViewById(R.id.choose_milk_show_fragment_drink_num);
        spoonCount.setText(String.valueOf(powderDetail.getSpoonDosage()) + "勺");
        temperature.setText("45°C");
        drinkcount.setText("约" + String.valueOf(powderDetail.getCount()) + "次");
    }
}
