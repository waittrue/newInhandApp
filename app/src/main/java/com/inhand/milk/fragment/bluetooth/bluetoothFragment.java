package com.inhand.milk.fragment.bluetooth;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.inhand.milk.R;

/**
 * Created by Administrator on 2015/5/30.
 */
public class bluetoothFragment extends Fragment {


    Bluetooth bluetooth;
    EditText editText;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.bluetooth_test,null);
        bluetooth = Bluetooth.getInstance();
        //bluetooth.asServer();
        bluetooth.asClient();
        bluetooth.setActivity(this.getActivity());
        setlistener(view);
        return  view;
    }
    private void setlistener(View view){
         editText = (EditText)view.findViewById(R.id.bluetooth_test_edit);
        Button button =(Button)view.findViewById(R.id.bluetooth_test_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String str;
               byte[] send = new byte[100];
               int count=0;
               count = new OneDayMessageTest(getActivity().getApplication()).message2Bytes(send);
               bluetooth.sendStream(send, count);
               //editText.setText(null);
            }
        });
    }
}
