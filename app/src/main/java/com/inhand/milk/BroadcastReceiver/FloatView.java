package com.inhand.milk.BroadcastReceiver;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inhand.milk.App;
import com.inhand.milk.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2015/7/28.
 */
public class FloatView {
    private boolean ismilk;
    private View view;
    private int width, heihgt, leftMargin, upHeight, downHeight;
    private Context context;
    private View.OnClickListener listener;

    public FloatView(Context context, View view, boolean ismilk, View.OnClickListener listener) {
        this.view = view;
        this.ismilk = ismilk;
        this.context = context;
        this.listener = listener;
        initViews();
    }

    public View getView() {
        return view;
    }

    private void initViews() {
        width = App.getWindowWidth(context) * 6 / 8;
        heihgt = (int) (width / 2f);
        leftMargin = width / 15;
        upHeight = heihgt * 5 / 7;
        downHeight = heihgt * 2 / 7;
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.alarm_total_container);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) linearLayout.getLayoutParams();
        lp.width = width;
        lp.height = heihgt;
        initTimeTextView();
        initIcon();
        initDownIcon();
        initDocTextView();
        initButton();
    }

    private void initTimeTextView() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String time = dateFormat.format(date);
        TextView textView = (TextView) view.findViewById(R.id.alarm_time_textview);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, upHeight / 2);
        textView.setText(time);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) textView.getLayoutParams();
        lp.leftMargin = leftMargin;
    }

    private void initIcon() {
        ImageView imageView = (ImageView) view.findViewById(R.id.alarm_milk_food_icon);
        if (ismilk)
            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.alarm_milk_icon));
        else
            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.alarm_food_icon));

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
        lp.height = upHeight * 3 / 4;
        lp.width = lp.height;
        lp.rightMargin = leftMargin;
    }

    private void initDownIcon() {
        ImageView imageView = (ImageView) view.findViewById(R.id.alarm_icon_imageview);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
        lp.height = downHeight * 3 / 4;
        lp.width = lp.height;
        lp.leftMargin = leftMargin;
    }

    private void initDocTextView() {
        TextView textView = (TextView) view.findViewById(R.id.alarm_doc_textview);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (downHeight / 2.5f));
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) textView.getLayoutParams();
        lp.leftMargin = leftMargin / 4;
        if (ismilk)
            textView.setText(context.getResources().getString(R.string.alarm_eating_milk));
        else
            textView.setText(context.getResources().getString(R.string.alarm_eating_food));
    }

    private void initButton() {
        TextView imageView = (TextView) view.findViewById(R.id.alarm_image_button);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
        lp.height = (int) (downHeight * 0.7f);
        lp.width = (int) (lp.height * 2f);
        lp.rightMargin = leftMargin;
        imageView.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (lp.height * 0.6f));
        imageView.setOnClickListener(listener);
    }
}
