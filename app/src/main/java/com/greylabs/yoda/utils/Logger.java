package com.greylabs.yoda.utils;

import android.util.Log;

/**
 * Created by Jaybhay Vijay on 7/1/2015.
 */
public class Logger {
    private static boolean isLoggingTrue;
    static {
        isLoggingTrue=true;
    }
    private Logger(){}
    public static  void log(String tag,String message){
            if (isLoggingTrue)
                Log.d(tag, message);
    }
}
