package com.greylabs.yoda.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    public static int getQuarter(int currentMonth){
        switch(currentMonth){
            case Calendar.JANUARY:
                return 0;
            case Calendar.FEBRUARY:
                return 1;
            case Calendar.MARCH:
                return 2;

            case Calendar.APRIL:
                return 0;
            case Calendar.MAY:
                return 1;
            case Calendar.JUNE:
                return 2;

            case Calendar.JULY:
                return 0;
            case Calendar.AUGUST:
                return 1;
            case Calendar.SEPTEMBER:
                return 2;

            case Calendar.OCTOBER:
                return 0;
            case Calendar.NOVEMBER:
                return 1;
            case Calendar.DECEMBER:
                return 2;
        }
        return -1;
    }
    public static int getWeek(int day){
        if(day>=29 && day<=31)
            return -1;
        if(day>=1 && day<=7)
            return 1;
        else if(day>=8 && day<=15)
            return 2;
        else if(day>=16 && day<=22)
            return 3;
        else if(day>=23 && day<=28)
            return 4;
        return -1;
    }
}