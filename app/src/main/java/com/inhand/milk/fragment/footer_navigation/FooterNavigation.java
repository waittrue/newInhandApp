package com.inhand.milk.fragment.footer_navigation;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.inhand.milk.R;
import com.inhand.milk.fragment.health.HealthFragment;
import com.inhand.milk.fragment.home.HomeFragment;
import com.inhand.milk.fragment.milk_amount.MilkAmountFragment;
import com.inhand.milk.fragment.person_center.PersonCenterFragment;
import com.inhand.milk.fragment.weight.WeightFragment;

public class FooterNavigation extends Fragment {

    private View view;
    private HomeFragment home;
    private HealthFragment health;
    private MilkAmountFragment milkAmountFragment;
    private WeightFragment weight;
    private PersonCenterFragment personCenterFragment;
    private FooterButtonsManager buttonsManager;
    private FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = inflater.inflate(R.layout.buttons, null);
        Log.i("buttons", "oncreateview");
        initButtons();
        return view;
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        Log.i("buttons", "onStart");
        //attachHome();
    }


    private void initButtons() {
        fragmentManager = getFragmentManager();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                buttonsManager = new FooterButtonsManager(fragmentManager);
                milkAmountFragment = new MilkAmountFragment();
                weight = new WeightFragment();
                health = new HealthFragment();
                personCenterFragment = new PersonCenterFragment();

                RelativeLayout button;
                button = (RelativeLayout) view.findViewById(R.id.buttons_temperature_icon);
                buttonsManager.addButtons(button, weight);

                button = (RelativeLayout) view.findViewById(R.id.buttons_milk_icon);
                buttonsManager.addButtons(button, milkAmountFragment);

                button = (RelativeLayout) view.findViewById(R.id.buttons_health);
                buttonsManager.addButtons(button, health);

                button = (RelativeLayout) view.findViewById(R.id.buttons_person_center);
                buttonsManager.addButtons(button, personCenterFragment);
            }
        });
        thread.start();
        RelativeLayout button;
        home = new HomeFragment();
        button = (RelativeLayout) view.findViewById(R.id.buttons_home);
        buttonsManager.addButtons(button, home);
        buttonsManager.setStartFragment(button);
    }

}

	

