package com.example.administrator.virtuilmilk;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import bluetooth.Bluetooth;
import bluetooth.OneDayMessageTest;


public class MainActivity extends ActionBarActivity {
    byte[] sendMessage = new byte[100];
    private EditText advise, time, startT, endT, amount;
    private Button send, makeDataSend;
    private Bluetooth bluetooth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        advise = (EditText) findViewById(R.id.advise);
        time = (EditText) findViewById(R.id.time);
        startT = (EditText) findViewById(R.id.start_temperature);
        endT = (EditText) findViewById(R.id.end_temperature);
        amount = (EditText) findViewById(R.id.amount);
        send = (Button) findViewById(R.id.send);
        makeDataSend = (Button) findViewById(R.id.makeSend);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OneDayMessageTest test = new OneDayMessageTest(getApplicationContext());
                float data;
                try {
                    data = Float.parseFloat(advise.getText().toString());
                    test.setadvise(data);
                    data = Float.parseFloat(time.getText().toString());
                    test.setTimeDuration(data);
                    data = Float.parseFloat(startT.getText().toString());
                    test.setStartT(data);
                    data = Float.parseFloat(endT.getText().toString());
                    test.setEndT(data);
                    data = Float.parseFloat(amount.getText().toString());
                    test.setamount(data);
                    test.setjiange(0);

                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "请输入数字", Toast.LENGTH_SHORT).show();
                    return;
                }
                int count = 0;
                count = test.message2Bytes(sendMessage);
                bluetooth.sendStream(sendMessage, count);
            }
        });
        makeDataSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OneDayMessageTest test = new OneDayMessageTest(getApplicationContext());
                int count = 0;
                count = test.message2Bytes(sendMessage);
                bluetooth.sendStream(sendMessage, count);
            }
        });
        bluetooth = Bluetooth.getInstance();
        bluetooth.setActivity(this);
        bluetooth.discoverable();
        bluetooth.asServer();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
