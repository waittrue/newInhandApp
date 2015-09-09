package com.inhand.milk.activity;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
import com.inhand.milk.fragment.bluetooth.UniversalBluetoothLE;
import com.inhand.milk.ui.BluetoothPairedViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BluetoothPairedAcivity extends BaseActivity {

    private static final int TIME = 100;
    private static final int SEARCHTIME = 12000;
    private static final int TIMESCALE = 1000;
    private static final float SCALE = 0.8f;
    private static final int PAIRED_SUCCESS = 4;
    private static final int FOUND_DEVICE = 5;
    private BluetoothPairedViewGroup bluetoothPairedViewGroup;
    private ListView listview;
    private List<Map<String, Object>> listItems;
    //private Bluetooth bluetooth = Bluetooth.getInstance();
    //这里我们硬件只支持4.0ble，所以我这里的bluetooth这从这个类出来，上面屏蔽的那个代码是经典bluetooth的
    private UniversalBluetoothLE bluetooth = UniversalBluetoothLE.getInistance();
    private TextView bluetoothText;
    private List<BluetoothDevice> devices = new ArrayList<>();
    private View currentChild;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            if (msg.what == PAIRED_SUCCESS) {
                pairedSuccess();
            }
            else if(msg.what  == FOUND_DEVICE){
                if (devices.size() == 0 && bluetoothPairedViewGroup.getScaleX() == 1) {
                    animationScaleTranslation(bluetoothPairedViewGroup);
                    listview.setVisibility(View.VISIBLE);
                    animatorAlpha(listview);
                }
                BluetoothDevice device = (BluetoothDevice)msg.obj;
                HashMap<String, Object> map = new HashMap<>();
                map.put("left", device.getName());
                map.put("right", "未连接");
                Log.i("bluetoothPairedActivity", device.getName());
                listItems.add(map);
                devices.add(device);
                Log.i(TAG, String.valueOf(devices.size()));
                listview.setAdapter(listview.getAdapter());
            }
        }
    };
    private static String TAG = "BluetoothPairedActivity";
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i(TAG,"onclick");
            if (!bluetooth.isEnabled()) {
                bluetooth.openBlue();
            } else
                bluetooth.startSearch();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_paired);
        initViews();
    }

    private void initViews() {
        bluetoothPairedViewGroup = (BluetoothPairedViewGroup) findViewById(R.id.bloothPairedViewGroup);
        bluetoothPairedViewGroup.setOnClickListener(listener);
        //bluetooth.setActivity(this);
        setBluetoothListener();
        bluetoothText = (TextView) findViewById(R.id.bluetoot_paired_unable_text);

        listview = (ListView) findViewById(R.id.bluetooth_paired_listview);
        listItems = new ArrayList<>();
        SimpleAdapter adapter = new SimpleAdapter(this, listItems, R.layout.bluetooth_paired_listview_items,
                new String[]{"left", "right"},
                new int[]{R.id.bluetooth_paired_listview_name_text, R.id.bluetooth_paired_listview_status_text});
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bluetooth.setPaired(devices.get(position));
                currentChild = listview.getChildAt(position);
                //bluetooth.asClient();
                bluetooth.getConnectGatt(true);
            }
        });
        refreshViews();
        initBottomViews();
    }

    private void initBottomViews() {
        float width = getWindowWidth();
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.bluetooth_paired_bottom_container);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int) (width / 2.5), (int) (width / 2.5 / 4));//这里的参数和first_launch底部是一样的
        ImageView leftView = new ImageView(getApplicationContext());
        leftView.setImageDrawable(getResources().getDrawable(R.drawable.bluetooth_help_btn));
        leftView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        lp.gravity = Gravity.CENTER_VERTICAL;
        lp.setMargins((int) (width / 2 - width / 2.5 / 2 - width / 4), 0, 0, 0);
        linearLayout.addView(leftView, lp);

        LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams((int) (width / 2.5), (int) (width / 2.5 / 4));//这里的参数和first_launch底部是一样的
        ImageView rightView = new ImageView(getApplicationContext());
        rightView.setImageDrawable(getResources().getDrawable(R.drawable.bluetooth_close_btn));
        rightView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        rlp.gravity = Gravity.CENTER_VERTICAL;
        rlp.setMargins((int) (-width / 2.5 + width / 2), 0, 0, 0);
        linearLayout.addView(rightView, rlp);
        rightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterNextAcitivity();
            }
        });
    }

    private void setBluetoothListener() {
        bluetooth.setDiscoverListener(new UniversalBluetoothLE.BluetoothDiscoverListener() {
            @Override
            public void discoverFound(BluetoothDevice device) {
                Log.i("universalBluetoothLe", "discoverFound");
                /*
                if (devices.size() == 0 && bluetoothPairedViewGroup.getScaleX() == 1) {
                    animationScaleTranslation(bluetoothPairedViewGroup);
                    listview.setVisibility(View.VISIBLE);
                    animatorAlpha(listview);
                }
                Message message = new Message();
                message.what = FOUND_DEVICE;
                handler.sendMessage(message);

                HashMap<String, Object> map = new HashMap<>();
                map.put("left", device.getName());
                map.put("right", "未连接");
                Log.i("bluetoothPairedActivity", device.getName());
                listItems.add(map);
                devices.add(device);
                Log.i(TAG, String.valueOf(devices.size()));
                listview.setAdapter(listview.getAdapter());
                */
                Message message = new Message();
                message.what = FOUND_DEVICE;
                message.obj = device;
                handler.sendMessage(message);
            }

            @Override
            public void discoverFinished() {
                bluetoothPairedViewGroup.animatorSmoothEnd();
                if (devices.size() == 0)
                    Toast.makeText(getApplicationContext(), "请将奶瓶靠近，点击图标重新搜索", Toast.LENGTH_LONG).show();
                bluetoothPairedViewGroup.setInnerTextView("开始");
            }

            @Override
            public void discoverStarted() {
                devices.clear();
                listItems.clear();
                listview.setAdapter(listview.getAdapter());
                bluetoothPairedViewGroup.start(SEARCHTIME);
                bluetoothPairedViewGroup.setInnerTextView("搜索中");
            }

            @Override
            public void pairedSuccess() {
                Message message = new Message();
                message.what = PAIRED_SUCCESS;
                handler.sendMessage(message);
            }
        });
    }

    private void pairedSuccess() {
        Log.i("bluetoothPaired", "pairedsuccess");
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("配对成功");
        builder.setMessage("点击确定完成配对，点击取消重新配对");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enterNextAcitivity();
            }
        });
        builder.setNegativeButton("取消", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        (currentChild.findViewById(R.id.bluetooth_paired_listview_icon)).setBackgroundResource(R.drawable.bluetooth_link_yes_ico);
    }

    private void refreshViews() {
        bluetoothPairedViewGroup.setBlueOn(bluetooth.isEnabled());
        bluetoothPairedViewGroup.changIcon();
        if (bluetooth.isEnabled())
            bluetoothText.setVisibility(View.GONE);
        else
            bluetoothText.setVisibility(View.VISIBLE);
    }

    private void animationLeftRight(View view) {
        float offset = view.getWidth() / 10;
        ObjectAnimator leftAnimator = new ObjectAnimator().ofFloat(view, "translationX", 0, -offset);
        leftAnimator.setDuration(TIME);
        ObjectAnimator rightAnimator = new ObjectAnimator().ofFloat(view, "translationX", -offset, offset);
        rightAnimator.setDuration(TIME * 2);
        ObjectAnimator rightLeftAnimator = new ObjectAnimator().ofFloat(view, "translationX", offset, 0);
        leftAnimator.setDuration(TIME);
        AnimatorSet set = new AnimatorSet();
        set.play(leftAnimator).before(rightAnimator);
        set.play(rightAnimator).before(rightLeftAnimator);
        set.start();
    }

    private void animationScaleTranslation(View view) {
        ObjectAnimator scaleXAnimator = new ObjectAnimator().ofFloat(view, "scaleX", 1f, SCALE);
        scaleXAnimator.setDuration(TIMESCALE);

        ObjectAnimator scaleYAnimator = new ObjectAnimator().ofFloat(view, "scaleY", 1f, SCALE);
        scaleYAnimator.setDuration(TIMESCALE);

        float offset = view.getHeight() * SCALE * 0.5f;
        ObjectAnimator translationAnimator = new ObjectAnimator().ofFloat(view, "translationY", 0, -offset);
        translationAnimator.setDuration(TIMESCALE);

        AnimatorSet set = new AnimatorSet();
        set.play(scaleXAnimator);
        set.play(scaleYAnimator);
        set.play(translationAnimator);
        set.start();
    }

    private void animatorAlpha(View view) {
        ObjectAnimator alphaAnimator = new ObjectAnimator().ofFloat(view, "alpha", 0f, 1f);
        alphaAnimator.setDuration(TIMESCALE);

        ObjectAnimator scaleXAnimator = new ObjectAnimator().ofFloat(view, "scaleX", 0f, 1f);
        scaleXAnimator.setDuration(TIMESCALE);

        ObjectAnimator scaleYAnimator = new ObjectAnimator().ofFloat(view, "scaleY", 0f, 1f);
        scaleYAnimator.setDuration(TIMESCALE);
        AnimatorSet set = new AnimatorSet();
        set.play(scaleXAnimator);
        set.play(scaleYAnimator);
        set.play(alphaAnimator);
        set.start();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Bluetooth.REQUEST_ENABLE_BT) {
            if (resultCode == 0) {
                animationLeftRight(bluetoothText);
            } else if (resultCode == -1) {
                refreshViews();
            }
        }

    }

    private void enterNextAcitivity() {
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        this.finish();
    }
}
