package com.inhand.milk.fragment.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

import com.inhand.milk.App;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class Bluetooth {
    private static Bluetooth instance = null;
    private BluetoothAdapter  bluetoothAdapter;
    public static final int  REQUEST_ENABLE_BT = 1;
    private static final int MESSAGE_READ = 88;
    private static BluetoothDevice paired = null;
    private Activity activity =null;
    private BluetoothData bluetoothData;
    private static UUID uuid = new UUID( 511024l,19910808l );
    private IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_FOUND);
    private IntentFilter filter2 = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
    private IntentFilter filter3 = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
    private ConnectedThread connectedThread;
    private AcceptThread acceptThread;
    private ConnectThread connectThread;
    private bluetoothDiscoverListener mListener;
    private static final String ACTION_DISCOVERY_FINISHED = BluetoothAdapter.ACTION_DISCOVERY_FINISHED;
    /*
    private final Handler mHandler = new Handler(){
        public void handleMessage(Message msg){
            if(msg.what == MESSAGE_READ){
                Toast.makeText(activity.getApplicationContext(), new String( (byte[])(msg.obj)).subSequence(0, msg.arg1) ,
                        Toast.LENGTH_LONG).show();
                Log.i("消息",    new String(  (byte[])(msg.obj) ))  ;
                //( (byte[])msg.obj ).
            }
        }
    };
    */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //Toast.makeText(activity.getApplicationContext(),"aa", Toast.LENGTH_SHORT).show();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (mListener !=null)
                    mListener.discoverFound(device);
                Toast.makeText(activity.getApplicationContext(),device.getName(), Toast.LENGTH_LONG).show();
            }
            else if ( ACTION_DISCOVERY_FINISHED.equals(action) ){
                Toast.makeText(activity.getApplicationContext(), "finish_discover", Toast.LENGTH_SHORT).show();
                activity.unregisterReceiver(mReceiver);
                if (mListener !=null)
                    mListener.discoverFiished();
            }
            else if (bluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
                Toast.makeText(activity, "discover start", Toast.LENGTH_SHORT).show();
                if (mListener !=null)
                    mListener.discoverStarted();
            }
        }
    };

    public void setDiscoverListener(bluetoothDiscoverListener listener){
        mListener =  listener;
    }
    public interface bluetoothDiscoverListener{
        void discoverFound(BluetoothDevice device);
        void discoverFiished();
        void discoverStarted();
        void pairedSuccess();
    }


    private Bluetooth() {
        // TODO Auto-generated constructor stub
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothData = BluetoothData.getInstance();
        if(bluetoothAdapter == null){
            if (activity != null)
                 Toast.makeText(activity.getApplicationContext(), "你没有蓝牙设备,无法传输数据", Toast.LENGTH_LONG).show();
        }
        paired = selectFromBonded();
    }
    private static synchronized void synInit(){
        if (instance == null)
            instance = new Bluetooth();
    }
    public static Bluetooth getInstance(){
        if (instance == null)
            synInit();
        return instance;
    }

    public void openBlue(){
        if(!bluetoothAdapter.isEnabled()){
            if (activity !=null) {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                activity.startActivityForResult(enableBluetooth, REQUEST_ENABLE_BT);
            }
        }
    }
    public boolean isEnabled(){
        return bluetoothAdapter.isEnabled();
    }
    public void  discoverable(){
        if (activity != null) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            activity.startActivityForResult(discoverableIntent, REQUEST_ENABLE_BT);
        }
    }

    public boolean startSearch(){
        boolean result = false;
        if (activity != null) {
            activity.registerReceiver(mReceiver, filter1); // Don't forget to unregister during onDestroyactivity.registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
            activity.registerReceiver(mReceiver, filter2); // Don't forget to unregister during onDestroy
            activity.registerReceiver(mReceiver, filter3);
            Toast.makeText(activity, "准备发现模块", Toast.LENGTH_SHORT).show();
        };
         result = bluetoothAdapter.startDiscovery();
         if (result == false && activity != null) {
                Toast.makeText(activity, "蓝牙没有开启", Toast.LENGTH_SHORT).show();
            }

        return result;
    }

    public void setPaired(BluetoothDevice device){
        bluetoothAdapter.cancelDiscovery();
        paired = device;
        Log.i("device_name", paired.getName());
    }
    public void setActivity(Activity act){
        activity = act;

        if(paired == null){
            Toast.makeText(App.getAppContext(),"请去配对蓝牙",1000).show();
        }
    }
    public boolean hasPaired(){
        if (paired == null )
            return false;
        return true;
    }
    private BluetoothDevice selectFromBonded(){
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView

                if  (device.getAddress().equals( getDefaultMac() ) ) {
                    return device;
                }
                if (device.getName().equals( "HUAWEI MT7-TL10")){
                    Log.i(" find device",device.getName());
                    return device;
                }

            }
        }
        Log.i("no find device","null");
        return null;
    }
    /*返回默认存储的蓝牙地址，当没有的时候返回null
    * **/
    private String getDefaultMac(){
        return "fadfsaf";
    }


    public void asClient(){
        if(paired == null){
            Log.i("paired", "null");
            if (activity != null)
                Toast.makeText(activity.getApplicationContext(), "没有配对对象",
                     Toast.LENGTH_LONG).show();
            return ;
        }

        if(connectThread == null){
            ShutConnect();
        }
        connectThread = new ConnectThread(paired);
        connectThread.start();
    }


    private boolean  connect(BluetoothSocket socket ){
        if(socket ==null)
            return false;
       if (connectedThread != null) {
           connectedThread.cancel();
           connectedThread.interrupt();
       }

        connectedThread = new ConnectedThread(socket);
        connectedThread.start();
        Log.i(  "连入", String.valueOf(  socket.isConnected() )  );
        return socket.isConnected();
    }

    public void asServer(){
        // = true;
        if(acceptThread == null ){
            acceptThread = new AcceptThread();
            acceptThread.start();
        }
        else {
            ShutConnect();
            acceptThread = new AcceptThread();
            acceptThread.start();
        }
    }

    public void ShutConnect(){

        if (connectThread != null) {
                connectThread.cancel();
                connectThread.interrupt();
                 connectThread = null;
        }

        if (connectedThread != null){
            connectedThread.cancel();
            connectedThread.interrupt();
            connectedThread = null;
        }

    }



    public void sendStream(byte[] bytes,int len){
        if( connectedThread !=null){
            connectedThread.write(bytes,len);
        }
    }

    private class ConnectThread extends Thread {
        private final BluetoothDevice mmDevice;
        BluetoothSocket socket = null;
        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;

            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = device.createRfcommSocketToServiceRecord(uuid);
            } catch (IOException e) { }
            socket = tmp;
        }

        public void run() {
            // Cancel discovery because it will slow down the connection
            bluetoothAdapter.cancelDiscovery();
            while(true) {
                try {
                    socket.connect();
                    break;
                } catch (IOException connectException) {
                    Log.i("bluetooth", "连入" + paired.getName() + "失败");
                    /*
                    try {
                        socket.close();
                    } catch (IOException closeException) {
                    }
                    */
                }
            }
            Log.i("bluetooth", "连入"+paired.getName()+":成功创建了socket");
            connect(socket);
            if (mListener != null)
                mListener.pairedSuccess();
        }

        /** Will cancel an in-progress connection, and close the socket */
        public void cancel() {
            try {
                socket.close();
            } catch (IOException e) { }
        }
    }



    private class ConnectedThread extends Thread {

        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private byte[] buffer;
        private int bytes;
        public ConnectedThread(BluetoothSocket mmsocket) {
            mmSocket = mmsocket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            buffer = new byte[1024];
            try {
                tmpIn = mmsocket.getInputStream();
                tmpOut = mmsocket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {


            // Keep listening to the InputStream until an exception occurs
            while (true) {
	        	/*
	        	if(mmSocket.isConnected() == false){
	        		Log.i("bluetooth","关闭了连接");
	        		ShutConnect();
	        		break;
	        	}
	        	*/
                Log.i(  "连入", String.valueOf(  mmSocket.isConnected() )  );
                try {
                    Log.i("bluetooth", "连入成功—等待数据");
                    bytes = mmInStream.read(buffer);
                    // Send the obtained bytes to the UI activity
                  /*  mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                            .sendToTarget();
                            */
                    if (bluetoothData.saveData(buffer,bytes) == false) {
                        bluetoothData.handleMessage();
                        bluetoothData.saveData(buffer,bytes);
                    }
                } catch (IOException e) {
                    break;
                }
            }
            Log.i("连接失败","抛出异常");

           connectThread = new ConnectThread(paired);
           connectThread.start();
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(byte[] bytes,int len) {
            try {
                mmOutStream.write(bytes,0,len);
                Log.i("发送",bytes.toString());
            } catch (IOException e) { }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }


    private class AcceptThread extends Thread {
            private final BluetoothServerSocket mmServerSocket;

            public AcceptThread() {
                // Use a temporary object that is later assigned to mmServerSocket,
                // because mmServerSocket is final
                BluetoothServerSocket tmp = null;
                try {
                    // MY_UUID is the app's UUID string, also used by the client code
                    tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord("milk", uuid);
                } catch (IOException e) { }
                mmServerSocket = tmp;
            }

            public void run() {

                BluetoothSocket socket = null;
                while (true) {
                    try {
                        Log.i("bluetooth","listening ......");
                        socket = mmServerSocket.accept();
                    } catch (IOException e) {
                        break;
                    }
                    // If a connection was accepted
                    if (socket != null) {
                        Log.i("bluetooth","accpted");
                        try {
                            mmServerSocket.close();
                            connect(socket);
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }

        // Will cancel the listening socket, and cause the thread to finish
        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) { }
        }
    }

}


