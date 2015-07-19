package com.inhand.milk.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.inhand.milk.R;
import com.inhand.milk.fragment.TitleFragment;

/**
 * Created by Administrator on 2015/7/3.
 */
public class FixInfoBaseFragment extends TitleFragment {
    protected EditText editText;
    private TextView title, leftText, rightTextview;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.user_info_settings_fix, container, false);
        initView(mView);
        return mView;
    }

    private void initView(View view) {
        leftText = (TextView) view.findViewById(R.id.user_info_fix_left_textview);
        editText = (EditText) view.findViewById(R.id.user_info_fix_edit);
    }

    public void hiddenSoftInput() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hiddenSoftInput();
    }

    public String getString() {
        return editText.getText().toString();
    }

    protected void setString(String str) {
        editText.setText(str);
    }

    protected void setLeftText(String str) {
        leftText.setText(str);
    }

    protected void setHintString(String str) {
        editText.setHint(str);
    }

    protected void setTitle(String str) {
        title.setText(str);
    }
}
