package com.inhand.milk.fragment.user_info_settings;

import android.app.FragmentManager;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inhand.milk.R;
import com.inhand.milk.activity.UserInfoSettingsActivity;
import com.inhand.milk.ui.FixInfoBaseFragment;

/**
 * Created by Administrator on 2015/7/3.
 */
public class UserinfoEmailFragmentInfo extends FixInfoBaseFragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setHintString(getResources().getString(R.string.user_info_fix_Email_hint_text));
        setTitleview(getResources().getString(R.string.user_info_fix_Email_title_text), 2, "完成",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((UserInfoSettingsActivity) getActivity()).setName(getString());
                        FragmentManager manager = getActivity().getFragmentManager();
                        manager.popBackStack();
                        manager.beginTransaction().commit();
                        hiddenSoftInput();
                    }
                });
        setLeftText(getResources().getString(R.string.user_info_fix_Email_left_text));
        String email = ((UserInfoSettingsActivity) getActivity()).getEmail();
        if (email == null)
            email = "";
        setString(email);
        editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        return mView;
    }
}
