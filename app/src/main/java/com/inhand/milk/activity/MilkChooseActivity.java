package com.inhand.milk.activity;

import android.app.Fragment;
import android.os.Bundle;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.FindCallback;
import com.inhand.milk.dao.PowderBrandDao;
import com.inhand.milk.dao.PowderDetailDao;
import com.inhand.milk.dao.PowderSerieDao;
import com.inhand.milk.entity.PowderBrand;
import com.inhand.milk.entity.PowderDetail;
import com.inhand.milk.entity.PowderSerie;
import com.inhand.milk.fragment.person_center.choose_milk.ChooseMilkFragment;
import com.inhand.milk.fragment.person_center.choose_milk.ChooseMilkShowFragment;

import java.util.List;

/**
 * Created by Administrator on 2015/8/10.
 */
public class MilkChooseActivity extends SubActivity {
    private PowderBrand powderBrand;
    private PowderSerie powderSerie;
    private PowderDetail powderDetail;

    public MilkChooseActivity() {
        powderBrand = new PowderBrandDao().findFromAche();
        powderSerie = new PowderSerieDao().findFromAcache();
        powderDetail = new PowderDetailDao().findFromAcache();
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
        if (powderBrand != null)
            powderBrand.saveInCache();
        if (powderSerie != null) {
            powderSerie.saveInAcache();
            PowderDetailDao powderDetailDao = new PowderDetailDao();
            powderDetailDao.findFromCloudByPowderSerie(powderSerie, new FindCallback<PowderDetail>() {
                @Override
                public void done(List<PowderDetail> list, AVException e) {
                    if (e == null && list != null && list.isEmpty() == false) {
                        for (PowderDetail p : list) {
                            p.saveInACache();
                            powderDetail = p;
                        }
                    }
                }
            });
        }
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
