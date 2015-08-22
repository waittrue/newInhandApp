package com.inhand.milk.fragment.person_center.user_info_settings;

import android.app.FragmentManager;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVException;
import com.inhand.milk.App;
import com.inhand.milk.R;
import com.inhand.milk.activity.UserInfoSettingsActivity;
import com.inhand.milk.entity.User;
import com.inhand.milk.ui.DefaultLoadingView;
import com.inhand.milk.ui.FixInfoBaseFragment;

/**
 * Created by Administrator on 2015/7/3.
 */
public class UserinfoEmailFragmentInfo extends FixInfoBaseFragment {
    private boolean success;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setHintString(getResources().getString(R.string.user_info_fix_Email_hint_text));
        setTitleview(getResources().getString(R.string.user_info_fix_Email_title_text), 2, "完成",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final DefaultLoadingView defaultLoadingView = new DefaultLoadingView(getActivity(), "同步中");
                        DefaultLoadingView.LoadingCallback callback = new DefaultLoadingView.LoadingCallback() {
                            @Override
                            public void doInBackground() {
                                try {
                                    User user = App.getCurrentUser();
                                    user.setEmail(getString());
                                    user.save();
                                } catch (AVException e) {
                                    e.printStackTrace();
                                    success = false;
                                }
                            }

                            @Override
                            public void onPreExecute() {
                                success = true;
                            }

                            @Override
                            public void onPostExecute() {
                                if (success) {
                                    ((UserInfoSettingsActivity) getActivity()).setEmail(getString());
                                    defaultLoadingView.dismiss();
                                } else {
                                    defaultLoadingView.disppear(null, "邮箱格式不对或者网络不给力", 2);
                                }
                                FragmentManager manager = getActivity().getFragmentManager();
                                manager.popBackStack();
                                manager.beginTransaction().commit();
                                hiddenSoftInput();
                            }
                        };
                        defaultLoadingView.loading(callback);
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
