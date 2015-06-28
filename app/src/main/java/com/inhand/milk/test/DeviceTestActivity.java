package com.inhand.milk.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.inhand.milk.R;
import com.inhand.milk.entity.Base;
import com.inhand.milk.entity.Device;
import com.inhand.milk.helper.SyncHelper;

/**
 * DeviceTestActivity
 * Desc: 设备信息测试
 * Date: 2015/6/3
 * Time: 8:10
 * Created by: Wooxxx
 */
public class DeviceTestActivity extends Activity {
    private EditText softwareVersionEditor;
    private EditText hardwareVersionEditor;
    private EditText adjustNumEditor;
    private EditText adjustDeviationEditor;
    private EditText macEditor;
    private EditText pressureErrorNumEditor;
    private EditText tempratureErrorNumEditor;
    private EditText accelerateErrorNumEditor;
    private Button submitBtn;
    private Button syncBtn;
    private Device device;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_test_device);
        softwareVersionEditor=(EditText)findViewById(R.id.software_version_test);
        hardwareVersionEditor=(EditText)findViewById(R.id.hardware_version_test);
        adjustNumEditor=(EditText)findViewById(R.id.adjust_num_test);
        adjustDeviationEditor=(EditText)findViewById(R.id.adjust_deviation_test);
        macEditor=(EditText)findViewById(R.id.mac_test);
        pressureErrorNumEditor=(EditText)findViewById(R.id.pressure_error_num_test);
        tempratureErrorNumEditor=(EditText)findViewById(R.id.temperature_error_num_test);
        accelerateErrorNumEditor=(EditText)findViewById(R.id.accelerate_error_num_test);
        submitBtn=(Button)findViewById(R.id.save_device_test_bth);
        syncBtn=(Button)findViewById(R.id.sync_device_test_btn);
        setListeners();

    }

    private void setListeners() {
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                device = new Device();
                String softwareVersion = softwareVersionEditor.getText().toString();
                String hardwareVersion = hardwareVersionEditor.getText().toString();
                int adjustNum = Integer.valueOf(adjustNumEditor.getText().toString());
                double adjustDeviation = Double.valueOf(adjustDeviationEditor.getText().toString());
                String mac = macEditor.getText().toString();
                int pressureErrorNum = Integer.valueOf(pressureErrorNumEditor.getText().toString());
                int temperatureErrorNum = Integer.valueOf(tempratureErrorNumEditor.getText().toString());
                int accelerateErrorNum = Integer.valueOf(accelerateErrorNumEditor.getText().toString());
                device.setMac(mac);
                device.setSoftwareVersion(softwareVersion);
                device.setAccelerateErrorNum(accelerateErrorNum);
                device.setAdjustNum(adjustNum);
                device.setAdjustDeviation(adjustDeviation);
                device.setHardwareVersion(hardwareVersion);
                device.setPressureErrorNum(pressureErrorNum);
                device.setTemperatureErrorNum(temperatureErrorNum);
                device.saveInDB(DeviceTestActivity.this, new Base.DBSavingCallback() {
                    @Override
                    public void done() {
                        Toast.makeText(DeviceTestActivity.this, "存储成功", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        //同步按钮
        syncBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SyncHelper.syncCloud(DeviceTestActivity.this, new SyncHelper.SyncCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e != null)
                            e.getMessage();
                        else
                            Toast.makeText(DeviceTestActivity.this, "同步成功", Toast.LENGTH_LONG)
                                    .show();
                    }
                });
            }
        });
    }
}
