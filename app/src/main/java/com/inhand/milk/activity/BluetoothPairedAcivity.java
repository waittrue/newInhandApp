package com.inhand.milk.activity;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.inhand.milk.R;
import com.inhand.milk.fragment.bluetooth.Bluetooth;
import com.inhand.milk.utils.BluetoothPairedViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BluetoothPairedAcivity extends BaseActivity {

	private BluetoothPairedViewGroup bluetoothPairedViewGroup;
    private ListView listview;
    private List<Map<String,Object>> listItems;
    private Bluetooth bluetooth;
    private TextView  bluetoothText;
    private static final int TIME = 100;
    private static final int SEARCHTIME = 12000;
    private static final int TIMESCALE = 1000;
    private static final float SCALE = 0.8f;
    private List<BluetoothDevice> devices ;
    private View currentChild;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bluetooth_paired);
        initViews();
	}

    private void initViews(){
        devices = new ArrayList<BluetoothDevice>();
        bluetoothPairedViewGroup =  (BluetoothPairedViewGroup)findViewById(R.id.bloothPairedViewGroup);
        bluetoothPairedViewGroup.setOnClickListener(listener);

        bluetooth = Bluetooth.getInstance();
        bluetooth.setActivity(this);
        setBluetoothListener();
        bluetoothText = (TextView)findViewById(R.id.bluetoot_paired_unable_text);

        listview = (ListView)findViewById(R.id.bluetooth_paired_listview);
        listItems = new ArrayList<Map<String,Object>>();
        SimpleAdapter adapter = new SimpleAdapter(this,listItems,R.layout.bluetooth_paired_listview_items,
                new String[]{"left","right"},
                new int[]{R.id.bluetooth_paired_listview_name_text,R.id.bluetooth_paired_listview_status_text});
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bluetooth.setPaired(devices.get(position));
                currentChild = listview.getChildAt(position);
                bluetooth.asClient();
            }
        });
        refreshViews();
        initBottomViews();
    }

    private void initBottomViews(){
        float width = getWindowWidth();
        LinearLayout linearLayout =(LinearLayout) findViewById(R.id.bluetooth_paired_bottom_container);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int)(width/2.5), (int) (width/2.5/4));//这里的参数和first_launch底部是一样的
        ImageView leftView = new ImageView(getApplicationContext());
        leftView.setImageDrawable(getResources().getDrawable(R.drawable.bluetooth_help_btn));
        leftView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        lp.gravity = Gravity.CENTER_VERTICAL;
        lp.setMargins((int) (width/2 - width/2.5/2- width/4),0,0,0);
        linearLayout.addView(leftView, lp);

        LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams((int)(width/2.5), (int) (width/2.5/4));//这里的参数和first_launch底部是一样的
        ImageView rightView = new ImageView(getApplicationContext());
        rightView.setImageDrawable(getResources().getDrawable(R.drawable.bluetooth_close_btn));
        rightView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        rlp.gravity = Gravity.CENTER_VERTICAL;
        rlp.setMargins((int) (-width/2.5+width/2),0,0,0);
        linearLayout.addView(rightView,rlp);
        rightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterNextAcitivity();
            }
        });
    }
    private void setBluetoothListener(){
        bluetooth.setDiscoverListener(new Bluetooth.bluetoothDiscoverListener() {
            @Override
            public void discoverFound(BluetoothDevice device) {
                if (devices.size() == 0 && bluetoothPairedViewGroup.getScaleX() == 1) {
                    animationScaleTranslation(bluetoothPairedViewGroup);
                    listview.setVisibility(View.VISIBLE);
                    animatorAlpha(listview);
                }
                HashMap<String,Object> map = new HashMap<String, Object>();
                map.put("left",device.getName());
                map.put("right","未连接");
                listItems.add(map);
                devices.add(device);
            }

            @Override
            public void discoverFiished() {
                bluetoothPairedViewGroup.animatorSmoothEnd();
                if(devices.size() == 0)
                    Toast.makeText(getApplicationContext(),"请将奶瓶靠近，点击图标重新搜索",Toast.LENGTH_LONG).show();
            }

            @Override
            public void discoverStarted() {
                devices.clear();
                listItems.clear();
                bluetoothPairedViewGroup.start(SEARCHTIME);


            }

            @Override
            public void pairedSuccess() {
                AlertDialog.Builder builder = new AlertDialog.Builder(BluetoothPairedAcivity.this);
                builder.setTitle("配对成功");
                builder.setMessage("点击确定完成配对，点击取消重新配对");
                builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        enterNextAcitivity();
                    }
                });
                builder.setNegativeButton("取消",null);
                AlertDialog alertDialog  = builder.create();
                alertDialog.show();
                ( (ImageView)currentChild.findViewById(R.id.bluetooth_paired_listview_icon)).setBackgroundResource(R.drawable.bluetooth_link_yes_ico);
            }
        });
    }
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!bluetooth.isEnabled()){
              bluetooth.openBlue();
            }
            else
                bluetooth.startSearch();
        }
    };
    private void refreshViews(){
        bluetoothPairedViewGroup.setBlueOn(bluetooth.isEnabled());
        if(bluetooth.isEnabled())
            bluetoothText.setVisibility(View.GONE);
        else
            bluetoothText.setVisibility(View.VISIBLE);
    }

    private void animationLeftRight(View view){
        float offset = view.getWidth()/10;
        ObjectAnimator leftAnimator = new ObjectAnimator().ofFloat(view,"translationX",0,-offset);
        leftAnimator.setDuration(TIME);
        ObjectAnimator rightAnimator = new ObjectAnimator().ofFloat(view,"translationX",-offset,offset);
        rightAnimator.setDuration(TIME*2);
        ObjectAnimator rightLeftAnimator = new ObjectAnimator().ofFloat(view,"translationX",offset,0);
        leftAnimator.setDuration(TIME);
        AnimatorSet set = new AnimatorSet();
        set.play(leftAnimator).before(rightAnimator);
        set.play(rightAnimator).before(rightLeftAnimator);
        set.start();
    }
    private void animationScaleTranslation(View view){
        ObjectAnimator scaleXAnimator = new ObjectAnimator().ofFloat(view,"scaleX",1f,SCALE);
        scaleXAnimator.setDuration(TIMESCALE);

        ObjectAnimator scaleYAnimator = new ObjectAnimator().ofFloat(view,"scaleY",1f,SCALE);
        scaleYAnimator.setDuration(TIMESCALE);

        float offset = view.getHeight()*SCALE*0.5f;
        ObjectAnimator translationAnimator = new ObjectAnimator().ofFloat(view,"translationY", 0, -offset);
        translationAnimator.setDuration(TIMESCALE);

        AnimatorSet set = new AnimatorSet();
        set.play(scaleXAnimator);
        set.play(scaleYAnimator);
        set.play(translationAnimator);
        set.start();
    }
    private void animatorAlpha(View view){
        ObjectAnimator alphaAnimator = new ObjectAnimator().ofFloat(view,"alpha",0f,1f);
        alphaAnimator.setDuration(TIMESCALE);

        ObjectAnimator scaleXAnimator = new ObjectAnimator().ofFloat(view,"scaleX",0f,1f);
        scaleXAnimator.setDuration(TIMESCALE);

        ObjectAnimator scaleYAnimator = new ObjectAnimator().ofFloat(view,"scaleY",0f,1f);
        scaleYAnimator.setDuration(TIMESCALE);
        AnimatorSet set = new AnimatorSet();
        set.play(scaleXAnimator);
        set.play(scaleYAnimator);
        set.play(alphaAnimator);
        set.start();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == Bluetooth.REQUEST_ENABLE_BT){
                    if (resultCode == 0) {
                        animationLeftRight(bluetoothText);
                    }
                    else if (resultCode == -1){
                         refreshViews();
                    }
            }

    }
    private void enterNextAcitivity(){
        Intent intent = new Intent();
        intent.setClass(this,MainActivity.class);
        startActivity(intent);
    }
}
