package com.greylabs.yoda.utils;

import com.greylabs.yoda.enums.TimeBoxWhen;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Jaybhay Vijay on 7/15/2015.
 */
public class CalendarUtils {
    private final static String TAG="CalendarUtils";
    public static Date parseDate(String dateInString) {
        //SimpleDateFormat sdf=new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
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
    public static int getLastMonthOfQuarter(int currentMonth){
        Calendar calendar=Calendar.getInstance();
        int lastMonth=0;
        if(currentMonth>=Calendar.JANUARY && currentMonth<=Calendar.MARCH){
            lastMonth=Calendar.MARCH;
        }else if(currentMonth>=Calendar.APRIL && currentMonth<=Calendar.JUNE){
            lastMonth=Calendar.JUNE;
        }else if(currentMonth>=Calendar.JULY && currentMonth<=Calendar.SEPTEMBER){
            lastMonth=Calendar.SEPTEMBER;
        }else if(currentMonth>=Calendar.OCTOBER && currentMonth<=Calendar.DECEMBER){
            lastMonth=Calendar.DECEMBER;
        }
        return lastMonth;
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
    public static Set<TimeBoxWhen> getPossibleWhenTypesOfDay(){
        Calendar cal=Calendar.getInstance();
        Set<TimeBoxWhen> whens=new TreeSet<>();
        whens.add(TimeBoxWhen.EARLY_MORNING);whens.add(TimeBoxWhen.MORNING);
        whens.add(TimeBoxWhen.AFTERNOON);whens.add(TimeBoxWhen.EVENING);
        whens.add(TimeBoxWhen.NIGHT);whens.add(TimeBoxWhen.LATE_NIGHT);
        Iterator<TimeBoxWhen> itWhens=whens.iterator();
        int currentHour=cal.get(Calendar.HOUR_OF_DAY);
        while (itWhens.hasNext()){
            TimeBoxWhen when=itWhens.next();
            if(when.getStartTime()<currentHour)
                itWhens.remove();

        }
        return whens;
    }
    public static String getSqLiteDateFormat(Calendar cal){
        String  sqliteDate=cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DATE)+" " +
                cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE)+":"+cal.get(Calendar.SECOND);
        return sqliteDate;
    }
}
