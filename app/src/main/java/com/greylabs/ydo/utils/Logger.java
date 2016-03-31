package com.greylabs.ydo.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.greylabs.ydo.R;

public class Logger {
    public static boolean isDebug;
    public static boolean isWarning;
    public static boolean isError;
    static {
        isDebug = true;
        isError=true;
        isWarning=true;
    }
    private Logger(){}
    public static  void d(String tag, String message){
        if (isDebug)
            Log.d(tag, message);
    }

    public static void w(String tag,String message){
        if (isWarning)
            Log.w(tag, message);
    }

    public static void e(String tag,String message){
        if (isError)
            Log.e(tag, message);
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

    public static  void showSnack(Context context, View view, String message, int LENGTH){
        Snackbar snackbar = Snackbar
                .make(view, message, LENGTH);
//                .setAction("OK", mOnClickListener);
        snackbar.setActionTextColor(Color.YELLOW);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(context.getResources().getColor(R.color.ColorPrimary));
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }
}