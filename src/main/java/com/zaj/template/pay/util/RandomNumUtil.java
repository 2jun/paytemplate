package com.zaj.template.pay.util;

/**
 * @Author: aiJun
 * @Date: 2021/5/28 0028 10:03
 * @Version 1.0
 */
public class RandomNumUtil {
    public static String getOrderNo() {
        String trandNo = String.valueOf(Math.random() * 1000000.0D);
        trandNo = trandNo.substring(0, 4);
        trandNo = System.currentTimeMillis() + trandNo;
        return trandNo;
    }
}
