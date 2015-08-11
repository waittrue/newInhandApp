package com.inhand.milk.activity;

import android.os.Bundle;

import com.inhand.milk.App;
import com.inhand.milk.R;
import com.inhand.milk.fragment.LoginOptFragment;

/**
 * LaunchActivity
 * Desc:启动Activity
 * Team: InHand
 * User:Wooxxx
 * Date: 2015-03-19
 * Time: 09:25
 */
public class LaunchActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
     //   this.startActivity(new Intent(this,StatisticsTestActivity.class));
       // App.logOut();
        getSupportFragmentManager().beginTransaction()
              .replace(R.id.main_container, new LoginOptFragment()).commit();

    }
}
