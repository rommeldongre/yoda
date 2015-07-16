package com.greylabs.yoda.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Jaybhay Vijay on 7/15/2015.
 */
public class CalendarUtils {
    private final static String TAG="CalendarUtils";
    public static Date parseDate(String dateInString) {
        SimpleDateFormat sdf=new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
        try {
            return sdf.parse(dateInString);
        } catch (ParseException e) {
            e.printStackTrace();
            Logger.log(TAG,"Unable to parse date.");
        }
        return null;
    }
}
