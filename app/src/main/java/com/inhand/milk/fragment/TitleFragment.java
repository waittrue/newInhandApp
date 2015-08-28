package com.inhand.milk.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.inhand.milk.R;
import com.inhand.milk.utils.Observer;

public class TitleFragment extends Fragment {
    private static LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    protected View mView;
    private boolean needRefersh = false;
    protected mOberver mOberver = new mOberver();
    private MyHander myHander = new MyHander(Looper.getMainLooper());

    protected boolean needRefresh() {
        return needRefersh;
    }

    protected void enableRefresh() {
        needRefersh = true;
    }

    protected void clearNeedRefresh() {
        needRefersh = false;
    }
    public void refresh() {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden == false) {
            refresh();
        }
    }

    protected void setTitleview(String text, int type) {
        setTitleview(text, type, "", null);
    }

    protected void setTitleview(String text, int type, String rightIcon, View.OnClickListener righListenner) {

        switch (type) {
            case 0:
                MainTitle title = new MainTitle(text);
                title.setrightTextView(righListenner, rightIcon);
                LinearLayout layout = (LinearLayout) mView.findViewById(R.id.title);
                layout.addView(title.getView(this.getActivity()));
                break;
            case 2:
                BackTitle title3 = new BackTitle(text);
                title3.setrightTextView(righListenner, rightIcon);
                LinearLayout layout3 = (LinearLayout) mView.findViewById(R.id.title);
                layout3.addView(title3.getView(this.getActivity()));
            default:
                break;
        }
    }

    protected void setTitleview(String text, int type, Drawable rightIcon, View.OnClickListener righListenner) {

        switch (type) {
            case 0:
                MainTitle title = new MainTitle(text);
                title.setrightIcon(righListenner, rightIcon);
                LinearLayout layout = (LinearLayout) mView.findViewById(R.id.title);
                layout.addView(title.getView(this.getActivity()));
                break;
            case 2:
                BackTitle title3 = new BackTitle(text);
                title3.setrightIcon(righListenner, rightIcon);
                LinearLayout layout3 = (LinearLayout) mView.findViewById(R.id.title);
                layout3.addView(title3.getView(this.getActivity()));
                break;
            default:
                break;
        }


    }

    protected Fragment getNextFragment() {
        return null;
    }


    protected void gotoNextFragment() {
        FragmentManager manager = (TitleFragment.this).getActivity().getFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.hide(TitleFragment.this);
        fragmentTransaction.add(R.id.Activity_fragments_container, getNextFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    protected void gotoSpecialFragment(Fragment fragment) {
        FragmentManager manager = (TitleFragment.this).getActivity().getFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.hide(TitleFragment.this);
        fragmentTransaction.add(R.id.Activity_fragments_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    protected void finishFragment() {
        FragmentManager manager = (TitleFragment.this).getActivity().getFragmentManager();
        manager.popBackStack();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.commit();
    }

    //观察者
    private class mOberver extends Observer {

        @Override
        public void onChanged() {
            Log.i("mberver", "true true");
            needRefersh = true;
            if (TitleFragment.this.isHidden() == false) {
                Message message = myHander.obtainMessage();
                message.what = REFRESH_STATE;
                myHander.sendMessage(message);
            }

        }
    }

    private static final int REFRESH_STATE = 1;

    //制造一个自己的hander 吧图形处理放回主线程
    private class MyHander extends Handler {
        public MyHander(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == REFRESH_STATE) {
                refresh();
            }
        }
    }
}
