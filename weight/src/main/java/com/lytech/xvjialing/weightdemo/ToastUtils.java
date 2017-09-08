package com.lytech.xvjialing.weightdemo;

import android.content.Context;
import android.widget.Toast;

/**
 * @Author : xjl
 * @Created : 2016-11-15
 * @E-mail : xvjialing@outlook.com
 * @Version : 1.0
 */
public class ToastUtils {
    private Toast mToast;

    public static void showToast(Context context, String message){
        Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
    }
}
