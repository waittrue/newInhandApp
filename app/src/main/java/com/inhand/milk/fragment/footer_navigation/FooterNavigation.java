package com.inhand.milk.fragment.footer_navigation;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.inhand.milk.R;
import com.inhand.milk.fragment.health.HealthFragment;
import com.inhand.milk.fragment.home.HomeFragment;
import com.inhand.milk.fragment.milk_amount.MilkAmountFragment;
import com.inhand.milk.fragment.temperature_amount.AmountStatistics;
import com.inhand.milk.fragment.temperature_amount.TemperatureStatistics;
import com.inhand.milk.fragment.weight.WeightFragment;

public class FooterNavigation extends Fragment {

	private View view;
	private TemperatureStatistics tempreture;
	private AmountStatistics amount;
	private HomeFragment home;
	private HealthFragment health;
    private MilkAmountFragment milkAmountFragment;
    private WeightFragment weight;
	private FooterButtonsManager buttonsManager ;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view  = inflater.inflate(R.layout.buttons, null);
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


	private void initButtons(){
		FragmentManager fragmentManager = getFragmentManager();
		buttonsManager = new FooterButtonsManager( fragmentManager);
		//tempreture = new TemperatureStatistics();
        milkAmountFragment = new MilkAmountFragment();
		//amount = new AmountStatistics();
        weight = new WeightFragment();
		health = new HealthFragment();
		home = new HomeFragment();
		//bluetooth = new bluetooth_fragment(((MainActivity)FooterNavigation.this.getActivity() ).getBluetooth() );
		ImageButton button ;
		button = (ImageButton)view.findViewById(R.id.buttons_home);
		buttonsManager.addButtons(button,home);
		buttonsManager.setStartFragment(button);
		
		button = (ImageButton)view.findViewById(R.id.buttons_temperature_icon);
		buttonsManager.addButtons(button,milkAmountFragment);
		
		button = (ImageButton)view.findViewById(R.id.buttons_milk_icon);
		buttonsManager.addButtons(button,weight);
		
		button = (ImageButton)view.findViewById(R.id.buttons_health);
		buttonsManager.addButtons(button,health);	
		
		//button = (ImageButton)view.findViewById(R.id.buttons_person_center);
		//buttonsManager.addButtons(button,bluetooth);		
	}

}
	
	

