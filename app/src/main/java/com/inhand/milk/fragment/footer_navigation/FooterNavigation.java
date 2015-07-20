package com.inhand.milk.fragment.footer_navigation;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.inhand.milk.R;
import com.inhand.milk.fragment.health.HealthFragment;
import com.inhand.milk.fragment.home.HomeFragment;
import com.inhand.milk.fragment.milk_amount.MilkAmountFragment;
import com.inhand.milk.fragment.person_center.PersonCenterFragment;
import com.inhand.milk.fragment.weight.WeightFragment;

import java.util.HashMap;
import java.util.Map;

public class FooterNavigation extends Fragment {

    private View view;
    private HomeFragment home;
    private HealthFragment health;
    private MilkAmountFragment milkAmountFragment;
    private WeightFragment weight;
    private PersonCenterFragment personCenterFragment;
    private FooterButtonsManager buttonsManager;
    private FragmentManager fragmentManager;
    private static final int pressed = 1, unpressed = 2;
    private Map<ImageButton, Map<Integer, Integer>> src = new HashMap<>();

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
        buttonsManager = new FooterButtonsManager(fragmentManager);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                milkAmountFragment = new MilkAmountFragment();
                weight = new WeightFragment();
                health = new HealthFragment();
                personCenterFragment = new PersonCenterFragment();

                RelativeLayout button;
                ImageButton imageButton;
                button = (RelativeLayout) view.findViewById(R.id.buttons_temperature_icon);
                imageButton = (ImageButton) view.findViewById(R.id.buttons_temperature_icon_button);
                button.setTag(imageButton);
                initMap(button, R.drawable.footer_develop_ico, R.drawable.footer_develop_cur_ico);
                button.setOnTouchListener(onTouchListener);
                buttonsManager.addButtons(button, weight);

                button = (RelativeLayout) view.findViewById(R.id.buttons_milk_icon);
                imageButton = (ImageButton) view.findViewById(R.id.buttons_milk_icon_button);
                button.setTag(imageButton);
                initMap(button, R.drawable.footer_record_ico, R.drawable.footer_record_cur_ico);
                button.setOnTouchListener(onTouchListener);
                buttonsManager.addButtons(button, milkAmountFragment);

                button = (RelativeLayout) view.findViewById(R.id.buttons_health);
                imageButton = (ImageButton) view.findViewById(R.id.buttons_health_button);
                button.setTag(imageButton);
                initMap(button, R.drawable.footer_eating_ico, R.drawable.footer_eating_cur_ico);
                button.setOnTouchListener(onTouchListener);
                buttonsManager.addButtons(button, health);

                button = (RelativeLayout) view.findViewById(R.id.buttons_person_center);
                imageButton = (ImageButton) view.findViewById(R.id.buttons_person_center_button);
                button.setTag(imageButton);
                initMap(button, R.drawable.footer_mine_ico, R.drawable.footer_mine_cur_ico);
                button.setOnTouchListener(onTouchListener);
                buttonsManager.addButtons(button, personCenterFragment);
            }
        });
        thread.start();
        RelativeLayout button;
        home = new HomeFragment();
        button = (RelativeLayout) view.findViewById(R.id.buttons_home);
        ImageButton imageButton = (ImageButton) view.findViewById(R.id.buttons_home_button);
        button.setTag(imageButton);
        initMap(button, R.drawable.footer_grade_ico, R.drawable.footer_grade_cur_ico);
        button.setOnTouchListener(onTouchListener);
        buttonsManager.addButtons(button, home);
        buttonsManager.setStartFragment(button);
    }

    private void initMap(RelativeLayout button, int unpd, int pd) {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(unpressed, unpd);
        map.put(pressed, pd);
        src.put((ImageButton) button.getTag(), map);
    }

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                ImageButton imageButton = (ImageButton) v.getTag();
                for (ImageButton temp : src.keySet()) {
                    Map<Integer, Integer> map = src.get(temp);
                    Log.i("footernavigation", temp.toString());
                    if (temp.equals(imageButton)) {
                        Log.i("footernavigation == ", temp.toString());
                        temp.setImageDrawable(getResources().getDrawable(map.get(pressed)));
                    } else
                        temp.setImageDrawable(getResources().getDrawable(map.get(unpressed)));
                }
            }
            return false;
        }
    };
}

	

