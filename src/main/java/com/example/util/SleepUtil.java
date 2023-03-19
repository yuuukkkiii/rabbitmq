package com.example.util;



import java.util.concurrent.TimeUnit;

public class SleepUtil {
    public static void sleep(int second){
        try {
            Thread.sleep(1000*second);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
