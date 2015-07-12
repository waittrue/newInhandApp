package com.inhand.milk.fragment.footer_navigation;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;

import com.inhand.milk.R;
import com.inhand.milk.fragment.TitleFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FooterButtonsManager {
    private View mCurrent;
    private HashMap<View, Fragment> map = new HashMap<>();
    private List<View> buttons = new ArrayList<>();
    private FragmentManager mFragmentManager;

    public FooterButtonsManager(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
    }

    public void addButtons(View button, Fragment fragment) {
        if (button == null || fragment == null)
            throw new NullPointerException();
        map.put(button, fragment);
        buttons.add(button);
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.Activity_fragments_container, fragment);
        fragmentTransaction.hide(fragment);
        fragmentTransaction.commit();
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == mCurrent)
                    return;
                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                Fragment fragment = map.get(v);
                fragmentTransaction.hide(map.get(mCurrent));
                fragmentTransaction.show(fragment);
                fragmentTransaction.commit();
                ((TitleFragment) fragment).refresh();
                mCurrent = v;
            }
        });

    }

    public void setStartFragment(View button) {
        mCurrent = button;
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.show(map.get(mCurrent));
        fragmentTransaction.commit();
    }

}
