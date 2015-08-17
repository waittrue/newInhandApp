package com.inhand.milk.fragment;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.inhand.milk.R;


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

        ImageView leftIcon = (ImageView) view.findViewById(R.id.title_left_icon);
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

        return view;
    }

}
