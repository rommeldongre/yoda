package com.greylabs.yoda.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.greylabs.yoda.R;

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

    public static  void showSnack(Context context, View view, String message){
        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_SHORT);
//                .setAction("OK", mOnClickListener);
        snackbar.setActionTextColor(Color.YELLOW);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(context.getResources().getColor(R.color.ColorPrimary));
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }
}