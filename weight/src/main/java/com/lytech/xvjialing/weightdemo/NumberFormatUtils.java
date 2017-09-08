package com.lytech.xvjialing.weightdemo;


/**
 * Created by xjl on 17-4-24.
 */

public class NumberFormatUtils {

    public static double transfer(double weight){

//        NumberFormat nf = NumberFormat.getNumberInstance();
//
//
//        // 保留两位小数
//        nf.setMaximumFractionDigits(1);
//
//
//        // 如果不需要四舍五入，可以使用RoundingMode.DOWN
////                nf.setRoundingMode(RoundingMode.UP);
//        nf.setRoundingMode(RoundingMode.DOWN);
//
//        return Double.parseDouble(nf.format(weight));

        try {
            return Double.parseDouble(new java.text.DecimalFormat("#.0").format(weight));
        }catch (Exception e){
            e.printStackTrace();
        }

        return 0.0;
    }
}
