package com.inhand.milk.fragment.milk_amount_curve;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.inhand.milk.App;
import com.inhand.milk.R;
import com.inhand.milk.entity.OneDay;
import com.inhand.milk.entity.Record;

import java.util.ArrayList;
import java.util.List;

public abstract class OnePaper {
    protected Activity mActivty;
    protected int temperatureMax = -1, temperatureMin = Integer.MAX_VALUE;
    protected int amountMax = -1, amountMin = Integer.MAX_VALUE;
    private LayoutInflater inflater;
    private View layout;
    private Context mContext;
    private com.inhand.milk.fragment.milk_amount_curve.Excle temperatureExcle, amountExcle;
    private int mWidth;
    private List<List<float[]>> temperatureData = new ArrayList<>();
    private List<List<float[]>> amountData = new ArrayList<>();


    public OnePaper(Activity activity, int width) {
        mActivty = activity;
        inflater = activity.getLayoutInflater();
        mContext = activity;
        layout = inflater.inflate(R.layout.milk_amount_curve_excle, null);
        temperatureMax = 50;
        temperatureMin = 0;
        mWidth = width;
    }

    private void drawExcle() {
        Context context = App.getAppContext();
        LinearLayout.LayoutParams temperatureParams = new LinearLayout.LayoutParams(mWidth,
                LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams amountParams = new LinearLayout.LayoutParams(mWidth,
                LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout temperatureContainer = (LinearLayout) layout.findViewById(R.id.temperature_excle_container);
        LinearLayout amountContainer = (LinearLayout) layout.findViewById(R.id.amount_excle_container);

        refreshExcleData(temperatureData, amountData);

        temperatureExcle = new com.inhand.milk.fragment.milk_amount_curve.Excle(mContext);
        temperatureContainer.addView(temperatureExcle, temperatureParams);
        temperatureExcle.setRange(temperatureMax, temperatureMin);
        temperatureExcle.addLine(temperatureData);
        temperatureExcle.setTextColor(context.getResources().
                getColor(R.color.milk_amount_curve_temperature_excle_title_text_color));
        temperatureExcle.setFirstBgColor(context.getResources().
                getColor(R.color.milk_amount_curve_temperature_excle_first_bg_color));
        temperatureExcle.setSecondBgColor(context.getResources().getColor(R.color.milk_amount_curve_temperature_excle_seconde_bg_color));
        temperatureExcle.setLineColor(context.getResources().getColor(R.color.milk_amount_curve_temperature_excle_line_color));
        setTemperatureExcle(temperatureExcle);

        amountExcle = new com.inhand.milk.fragment.milk_amount_curve.Excle(mContext);
        amountContainer.addView(amountExcle, amountParams);

        amountExcle.setRange(amountMax, amountMin);
        amountExcle.addLine(amountData);
        amountExcle.setTextColor(context.getResources().
                getColor(R.color.milk_amount_curve_amount_excle_title_text_color));
        amountExcle.setFirstBgColor(context.getResources().
                getColor(R.color.milk_amount_curve_amount_excle_first_bg_color));
        amountExcle.setSecondBgColor(context.getResources().getColor(R.color.milk_amount_curve_amount_excle_seconde_bg_color));
        amountExcle.setLineColor(context.getResources().getColor(R.color.milk_amount_curve_amount_excle_line_color));
        setAmountExcle(amountExcle);
    }

    protected abstract void setTemperatureExcle(com.inhand.milk.fragment.milk_amount_curve.Excle excle);

    protected abstract void setAmountExcle(com.inhand.milk.fragment.milk_amount_curve.Excle excle);

    protected abstract void refreshExcleData(List<List<float[]>> temperatureData, List<List<float[]>> amountData);


    protected View getPaper() {
        drawExcle();
        return layout;
    }

    public float getMinTemp(Record record) {
        float min_temp = record.getBeginTemperature();
        if (min_temp > record.getEndTemperature()) min_temp = record.getEndTemperature();
        return min_temp;
    }

    public float getMaxTemp(Record record) {
        float max_temp = record.getBeginTemperature();
        if (max_temp < record.getEndTemperature()) max_temp = record.getEndTemperature();
        return max_temp;
    }

    public float getMaxTemp(OneDay oneDay) {
        float max_temp = 0;
        for (Record record : oneDay.getRecords()) {
            if (max_temp < record.getBeginTemperature())
                max_temp = record.getBeginTemperature();
            if (max_temp < record.getEndTemperature())
                max_temp = record.getEndTemperature();
        }
        return max_temp;
    }

    public float getMinTemp(OneDay oneDay) {
        float min_temp = 100;
        for (Record record : oneDay.getRecords()) {
            if (min_temp > record.getBeginTemperature())
                min_temp = record.getBeginTemperature();
            if (min_temp > record.getEndTemperature())
                min_temp = record.getEndTemperature();
        }
        return min_temp;
    }
}
