package com.lytech.xvjialing.weightdemo;

import java.util.UUID;

/**
 * Created by xjl on 17-5-22.
 */

public interface Constans {
    public static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");  //这条是蓝牙串口通用的UUID，不要更改

    public static final String DeviceName="YaoHuaL61";
    public static final String DeviceName2="HC-06";

    public static final int REQUEST_ENABLE_BT = 9;
    public static final int FOUNED_DEVICE = 4;
    public static final int CANNNOT_FOUNED_DEVICE = 10;

    public static final int CONNECTED_DEVICE = 5;
    public static final int CENNCTED_DEVICE_FIELD = 6;
    public static final int DISCONNECTED = 7;
    public static final int READ_DATA=8;
}
