package com.youxing.duola.utils;

import java.text.DecimalFormat;

/**
 * Created by Jun Deng on 15/8/6.
 */
public class PriceUtils {

    public static String formatPriceString(double price) {
        DecimalFormat df = new DecimalFormat("###0.00");
        String s = df.format(price);
        if(s.indexOf(".") > 0){
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

}
