package com.greylabs.yoda.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Logger {
    private static boolean isLoggingTrue;
    static {
        isLoggingTrue = true;
    }
    private Logger(){}
    public static  void log(String tag,String message){
            if (isLoggingTrue)
                Log.d(tag, message);
    }

    public static  void showMsg(Context context,String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}