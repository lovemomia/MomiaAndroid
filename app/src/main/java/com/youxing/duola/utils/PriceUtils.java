package com.youxing.duola.utils;

import java.text.DecimalFormat;

/**
 * Created by Jun Deng on 15/8/6.
 */
public class PriceUtils {

    public static String formatPriceString(double price) {
        int iprice = (int)price;
        if (price != iprice) {
            DecimalFormat df = new DecimalFormat("###0.00");
            return String.valueOf(df.format(price));
        } else {
            return String.valueOf(iprice);
        }
    }

}
