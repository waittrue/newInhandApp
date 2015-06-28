package com.inhand.milk.activity;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.app.AlertDialog.Builder;
import com.inhand.milk.R;
import com.inhand.milk.fragment.bluetooth.Bluetooth;
import com.inhand.milk.fragment.footer_navigation.FooterNavigation;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *
 * @author Administrator
 *主要的activity，管理进入app后的几个fragment
 */


public class MainActivity extends BaseActivity {

    private FooterNavigation buttons ;
    private Bluetooth bluetooth ;
    private SlidingMenu menu;
    private OnClickListener  onClickListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        buttons = new FooterNavigation();
        fragmentTransaction.add(R.id.Activity_buttons_fragments_container, buttons,"buttons");
        fragmentTransaction.commit();


        // bluetooth = new Bluetooth(this);
        // bluetooth.openBlue();
        //bluetooth.startSearch();
        setSlidingMenu();
        onClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                menu.toggle();
            }
        };


    }

    private void setSlidingMenu(){
        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        menu.setBackgroundResource(R.drawable.menu_background);
        menu.setBehindOffsetRes(R.dimen.menu_behind_offset);
        menu.setFadeDegree(0.8f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);

        View view = getLayoutInflater().inflate(R.layout.menu, null);
        menu.setMenu(view );


        ListView listView = (ListView)view.findViewById(R.id.detals_listView);
        List< Map<String,Object> > listitems = new ArrayList<Map<String,Object>>();
        Map<String, Object> map1 = new HashMap<String, Object>();
        Map<String, Object> map2 = new HashMap<String, Object>();
        Map<String, Object> map3 = new HashMap<String, Object>();
        String string[]  = getResources().getStringArray(R.array.menu_item_texts);
        map1.put("image", R.drawable.menu_family_icon);
        map1.put("text",string[0]);
        map2.put("image", R.drawable.menu_milk_settings_icon);
        map2.put("text",string[1]);
        map3.put("image", R.drawable.menu_advise_icon);
        map3.put("text",string[2]);

        listitems.add(map1);
        listitems.add(map2);
        listitems.add(map3);

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listitems, R.layout.menu_item,
                new String[] {"image","text"},
                new int[] { R.id.menu_image,  R.id.menu_text}  );
        listView.setAdapter(simpleAdapter);

    }

    public android.view.View.OnClickListener getMyOnclickListener(){
        return onClickListener;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public Bluetooth getBluetooth(){
        return bluetooth;
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Log.i("activity", "onDestroy");
        if (bluetooth != null)
            bluetooth.ShutConnect();
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        AlertDialog alert = new Builder(MainActivity.this).create();
        alert.setMessage("确认退出吗？");
        alert.setTitle("提示");
        alert.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                finish();
            }
        });

        alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

            }
        });
        alert.show();
    }
}
