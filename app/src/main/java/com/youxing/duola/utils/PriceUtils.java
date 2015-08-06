package com.youxing.duola.utils;

/**
 * Created by Jun Deng on 15/8/6.
 */
public class PriceUtils {

    public static String formatPriceString(double price) {
        int iprice = (int)price;
        if (price != iprice) {
            return String.valueOf(price);
        } else {
            return String.valueOf(iprice);
        }
    }

}
