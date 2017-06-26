package com.auge.execute.utils;

/**
 * Created by lixun on 2017/6/24.
 */
public class Utils {

    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
