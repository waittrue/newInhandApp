package com.inhand.milk.fragment;


import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.inhand.milk.App;
import com.inhand.milk.R;

public class MainTitle extends BaseTitle {

    private String mTitle;

    public MainTitle(String title) {
        mTitle = title;

    }

    public View getView(Activity activity) {
        // TODO Auto-generated method stu
        return setView(activity, R.layout.title_main, App.getAppContext().getResources().getDrawable(R.drawable.header_connect_false_icon),
                mTitle, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (v instanceof ImageView) {
                            ImageView imageView = (ImageView) v;
                            imageView.getDrawable().equals(App.getAppContext().getResources().getDrawable(R.drawable.header_connect_false_icon));
                            Toast.makeText(App.getAppContext(), "请靠近奶瓶，连接奶瓶", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        /*
                activity.getResources().getDrawable( R.drawable.menu_entry),mTitle,
				((MainActivity)activity).getMyOnclickListener());*/
    }

}
