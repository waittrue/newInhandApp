package com.inhand.milk.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.inhand.milk.entity.PowderBrand;
import com.inhand.milk.entity.PowderDetail;
import com.inhand.milk.entity.PowderSerie;
import com.inhand.milk.fragment.person_center.choose_milk.ChooseMilkFragment;
import com.inhand.milk.fragment.person_center.choose_milk.ChooseMilkShowFragment;
import com.inhand.milk.helper.FeedPlanHelper;
import com.inhand.milk.helper.MilkHelper;

import java.text.ParseException;

/**
 * Created by Administrator on 2015/8/10.
 */
public class MilkChooseActivity extends SubActivity {
    private PowderBrand powderBrand;
    private PowderSerie powderSerie;
    private PowderDetail powderDetail;
    private static final String TAG = "MilkChooseACtivity";
    private MilkHelper milkHelper = new MilkHelper();
    public MilkChooseActivity() {
        powderBrand = milkHelper.getMilkPowderBrand();
        powderSerie = milkHelper.getMilkPowderSerie();
        powderDetail = milkHelper.getMilkPowderDetail();
    }

    public PowderDetail getPowderDetail() {
        return powderDetail;
    }

    public void setPowderDetail(PowderDetail powderDetail) {
        this.powderDetail = powderDetail;
    }

    public PowderBrand getPowderBrand() {
        return powderBrand;
    }

    public void setPowderBrand(PowderBrand powderBrand) {
        this.powderBrand = powderBrand;
    }

    public PowderSerie getPowderSerie() {
        return powderSerie;
    }

    public void setPowderSerie(PowderSerie powderSerie) {
        this.powderSerie = powderSerie;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    public void save() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (milkHelper.saveMilk(powderBrand, powderSerie) == false) {
                    Log.i("milkchooseActivit", "milk save failed");
                    return;
                }
                Log.i("milkchooseActivit", "milk save success");
                try {
                    FeedPlanHelper feedPlanHelper = new FeedPlanHelper();
                    if (powderSerie.getObjectId().equals(milkHelper.getMilkPowderSerie().getObjectId()) == false) {
                        feedPlanHelper.saveBabyItems(powderSerie);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (AVException e) {
                    e.printStackTrace();
                }

            }
        });
        thread.start();
    }

    @Override
    protected Fragment initFragment() {
        // TODO Auto-generated method stub

        Fragment mFragment;
        if (hasAche())
            mFragment = new ChooseMilkShowFragment();// new Nutrition();
        else
            mFragment = new ChooseMilkFragment();
        return mFragment;
    }

    private boolean hasAche() {
        if (powderBrand != null && powderSerie != null && powderDetail != null)
            return true;
        return false;
    }
}
