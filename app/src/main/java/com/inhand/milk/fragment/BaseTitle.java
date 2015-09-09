package com.inhand.milk.fragment;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.inhand.milk.App;
import com.inhand.milk.R;
import com.inhand.milk.fragment.bluetooth.Bluetooth;
import com.inhand.milk.fragment.bluetooth.UniversalBluetoothLE;


public class BaseTitle {
    private static final String TAG = "BASETITLE";
    private OnClickListener listenerRight;
    private Drawable iconright;
    private String rightText;

    public void setrightIcon(OnClickListener listener, Drawable icon) {
        listenerRight = listener;
        iconright = icon;
    }

    public void setrightTextView(OnClickListener listener, String rightText) {
        this.rightText = rightText;
        listenerRight = listener;
    }

    protected View setView(Activity activity, int layoutId, Drawable iconLeft, String title,
                           OnClickListener listener) {


        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(layoutId, null);

        final ImageView leftIcon = (ImageView) view.findViewById(R.id.title_left_icon);
        leftIcon.setOnClickListener(listener);
        leftIcon.setImageDrawable(iconLeft);

        ((TextView) view.findViewById(R.id.title_text)).setText(title);
        if (listenerRight != null && iconright != null) {
            //Log.i(TAG,String.valueOf(listenerRight == null));
            ImageView rightIcon = (ImageView) view.findViewById(R.id.title_right_icon);
            rightIcon.setOnClickListener(listenerRight);
            rightIcon.setImageDrawable(iconright);
        } else if (listenerRight != null && rightText != null) {
            //Log.i(TAG,String.valueOf(listenerRight == null));
            TextView rightIcon = (TextView) view.findViewById(R.id.title_right_TextView);
            rightIcon.setOnClickListener(listenerRight);
            rightIcon.setText(rightText);
        }


        //这个地方植入到蓝牙状态的变化图标变化的代码，这个标题理论上只能由那个5个主页面生成,不能有其他页面生成
        UniversalBluetoothLE bluetoothLE = UniversalBluetoothLE.getInistance();
        if(bluetoothLE != null) {
            bluetoothLE.addBluetoothStateChanggedListener(new UniversalBluetoothLE.ConnectedChanggedListener() {
                @Override
                public void connectedChangged(boolean connect) {
                    if (connect) {
                        leftIcon.setImageDrawable(App.getAppContext().getResources().getDrawable(R.drawable.header_connect_true_icon));
                    } else {
                        leftIcon.setImageDrawable(App.getAppContext().getResources().getDrawable(R.drawable.header_connect_false_icon));
                    }
                }
            });
        }
        return view;
    }

}
