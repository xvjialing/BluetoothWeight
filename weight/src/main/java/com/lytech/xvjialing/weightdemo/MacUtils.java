package com.lytech.xvjialing.weightdemo;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

/**
 * Created by xjl on 2017/1/5.
 */

public class MacUtils {
    public static String getMac()
    {
        String macSerial = null;
        String str = "";

        try
        {
            Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            for (; null != str;)
            {
                str = input.readLine();
                if (str != null)
                {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
            try {
                macSerial=macSerial.replace(":","");
            } catch (Exception e) {
                e.printStackTrace();
            }
            ir.close();
            input.close();
        } catch (IOException ex) {
            // 赋予默认值
            ex.printStackTrace();
        }
        return macSerial;
    }

    public static String getBuleToothAddress(String address){
        String mac=null;
        try {
            mac=address.trim().replace(":","");
            return  mac;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
