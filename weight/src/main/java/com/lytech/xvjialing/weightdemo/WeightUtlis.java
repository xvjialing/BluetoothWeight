package com.lytech.xvjialing.weightdemo;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WeightUtlis {
    private Context context;
    private Handler mHandler;
    private double coefficient=1.0;

    private static final String TAG=WeightUtlis.class.getSimpleName();

    private BluetoothAdapter mBluetoothAdapter=null;
    private BluetoothDevice mBlueToothDevice=null;
    private BluetoothSocket bluetoothSocket=null;


    byte[] buffer = new byte[1024];
    int bytes;

    int count=0;

    private InputStream inputStream;

    private String tempWeight="";
    private double recWeight=0.0;

    public WeightUtlis(Context context, Handler handler) {
        this.context = context;
        this.mHandler = handler;
    }

    public void searchBluetooth() {

        Log.d(TAG, "searchBluetooth: SEARCHSTART");
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mBluetoothAdapter == null) {
                    // Device does not support Bluetooth
                }
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            context.getApplicationContext().startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            ((Activity) context).startActivityForResult(enableBtIntent, Constans.REQUEST_ENABLE_BT);
                }
                Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
                for (BluetoothDevice device:pairedDevices){
                    Log.d(TAG,device.toString());
                    Log.d(TAG,device.getName()+"--"+device.getType()+"--"+device.getAddress());
                    Log.d(TAG, MacUtils.getBuleToothAddress(device.getAddress()));
                }
                // If there are paired devices
                if (pairedDevices.size() > 0) {
                    // Loop through paired devices
                    for (BluetoothDevice device : pairedDevices) {
                        if (device.getName().equals(Constans.DeviceName)||device.getName().equals(Constans.DeviceName2)){
                            mBlueToothDevice=device;
                        }

                    }
                }

                if (mBlueToothDevice!=null){
                    Map<String,String> map=new HashMap();
                    map.put("mac",MacUtils.getBuleToothAddress(mBlueToothDevice.getAddress()));

                    RequestUtils requestUtils=new RequestUtils();
                    requestUtils.requestWithoutCookie(map,"http://121.196.194.14/recyclerabbit/Home/Rabbit/getWeightCoefficient")
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<String>() {
                                @Override
                                public void onCompleted() {
                                    Log.d(TAG, "onCompleted: get coefficient succues!");
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.d(TAG, "onError: "+e.getMessage());
                                }

                                @Override
                                public void onNext(String s) {
                                    Log.d(TAG, "onNext: "+s);
                                    WeightCoefficientBean weightCoefficient= JSON.parseObject(s,WeightCoefficientBean.class);
                                    if (weightCoefficient.isLp()){
                                        coefficient= Double.parseDouble(weightCoefficient.getData().getDatalist().getCoefficient());
                                    }

                                }
                            });
                    Message message=new Message();
                    message.what=Constans.FOUNED_DEVICE;
                    mHandler.sendMessage(message);
                } else{
                    Message message=new Message();
                    message.what=Constans.CANNNOT_FOUNED_DEVICE;
                    mHandler.sendMessage(message);
                }
            }
        }).start();


    }

    public boolean isConnect(){
        return bluetoothSocket.isConnected();
    }

    public void connectBluetooth() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    bluetoothSocket = mBlueToothDevice.createRfcommSocketToServiceRecord(Constans.MY_UUID);
                    bluetoothSocket.connect();
                    Message message=new Message();
                    message.what=Constans.CONNECTED_DEVICE;
                    mHandler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                    Message message=new Message();
                    message.what=Constans.CENNCTED_DEVICE_FIELD;
                    mHandler.sendMessage(message);
                }
            }
        }).start();
    }


    public void readData() {

        Log.d(TAG,"read_data");
        new Thread(new Runnable() {
            @Override
            public void run() {

                    try {
                        while (bluetoothSocket.isConnected()){
                        inputStream=bluetoothSocket.getInputStream();
                        count=inputStream.available();

                        bytes=inputStream.read(buffer);
                        String readMessage = new String(buffer, 0, bytes);
//                            Log.d("weight",reverse2(readMessage));
                        if ((readMessage.length()==8)&&(readMessage.contains("="))){
//                            Log.d(TAG, readMessage+"");
                            readMessage=reverse2(readMessage.substring(2,7));
                            if (!readMessage.equals(tempWeight)){
                                try {
                                    recWeight= NumberFormatUtils.transfer(Double.parseDouble(readMessage)*coefficient);
                                    Message message=new Message();
                                    message.what=Constans.READ_DATA;
                                    message.obj=recWeight;
                                    mHandler.sendMessage(message);
                                    tempWeight=readMessage;
                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                            }

                        }
//                        Log.d(TAG,readMessage);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.d(TAG,e.getMessage());
                        Message message =new Message();
                        message.what=Constans.DISCONNECTED;
                        mHandler.sendMessage(message);
                    }
            }
        }).start();
    }

    public void closeBluetooth(){
        try {
            if (bluetoothSocket.isConnected()){
                bluetoothSocket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
//        bluetoothSocket=null;
//        mBlueToothDevice=null;
//        mBluetoothAdapter=null;
    }

    private String reverse2(String s) {
        int length = s.length();
        String reverse = "";
        for (int i = 0; i < length; i++)
            reverse = s.charAt(i) + reverse;

        return reverse;
    }

}
