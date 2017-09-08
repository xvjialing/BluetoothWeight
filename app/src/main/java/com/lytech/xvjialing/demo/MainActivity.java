package com.lytech.xvjialing.demo;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.lytech.xvjialing.weightdemo.Constans;
import com.lytech.xvjialing.weightdemo.ToastUtils;
import com.lytech.xvjialing.weightdemo.WeightUtlis;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView tv_weight;

    private WeightUtlis weightUtlis;

    private double tempWeight=0;
    private double recentWeight;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
//                找到相应设备
                case Constans.FOUNED_DEVICE:
                    weightUtlis.connectBluetooth();
                    Log.d(TAG,"FOUNED_DEVICE");
                    break;
//                无法找到相应设备
                case Constans.CANNNOT_FOUNED_DEVICE:
                    Log.d(TAG,"CANNNOT_FOUNED_DEVICE");
                    ToastUtils.showToast(MainActivity.this,"无法找到设备");
                    break;
//                已连接相应设备，开始读取数据
                case Constans.CONNECTED_DEVICE:
                    Log.d(TAG,"CONNECTED_DEVICE");
                    weightUtlis.readData();
                    break;
//                设备连接失败
                case Constans.CENNCTED_DEVICE_FIELD:
                    Log.d(TAG,"CENNCTED_DEVICE_FIELD");
                    ToastUtils.showToast(MainActivity.this,"连接设备失败");
                    break;
//                设备断开连接
                case Constans.DISCONNECTED:
                    Log.d(TAG,"DISCONNECTED");
                    ToastUtils.showToast(MainActivity.this,"蓝牙断开");
                    break;
//                收到重量数据
                case Constans.READ_DATA:
                    try{
                        double weight=(Double)msg.obj*2;
                        if (!TextUtils.equals(String.valueOf(weight),String.valueOf(tempWeight))){
                            recentWeight=weight;
                            tv_weight.setText(String.valueOf(weight));
                            tempWeight=weight;
                        }
                    }catch(Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_weight = (TextView) findViewById(R.id.tv_weight);

        weightUtlis = new WeightUtlis(MainActivity.this,handler);
        weightUtlis.searchBluetooth();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (weightUtlis.isConnect()) {
                weightUtlis.closeBluetooth();
            }
            weightUtlis=null;
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
