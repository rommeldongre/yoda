package com.greylabs.yoda.enums;

import java.util.Calendar;
import java.util.Set;
import java.util.TreeSet;

public enum TimeBoxWhen {
    /**********************************************************************************************/
    // Enum Constants
    /**********************************************************************************************/
    EARLY_MORNING("Early Morning",6, 9,0),
    MORNING("Morning",9, 11,1),
    AFTERNOON("Afternoon",12, 14,2),
    EVENING("Evening" ,15, 17,3),
    NIGHT("Night",18, 20,4),
    LATE_NIGHT("Late Night",21, 23,5);

    /**********************************************************************************************/
    // Enum properties
    /**********************************************************************************************/
    private String displayName;
    private int value;
    private int startTime;
    private int endTime;

    /**********************************************************************************************/
    //Getters and Setters
    /**********************************************************************************************/

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    /**********************************************************************************************/
    // Constructors
    /**********************************************************************************************/
    TimeBoxWhen(String displayName, int startTime, int endTime,int value) {
        this.displayName = displayName;
        this.value=value;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**********************************************************************************************/
    // Methods
    /**********************************************************************************************/
    public static TimeBoxWhen getWhen(Calendar calendar){
        Calendar calStart=Calendar.getInstance();
        calStart.set(Calendar.MINUTE,0);calStart.set(Calendar.SECOND,0);calStart.set(Calendar.MILLISECOND,0);
        Calendar calEnd=Calendar.getInstance();
        calEnd.set(Calendar.MINUTE, 0);calEnd.set(Calendar.SECOND, 0);calEnd.set(Calendar.MILLISECOND, 0);


        Set<TimeBoxWhen> whens=new TreeSet<>();
        whens.add(EARLY_MORNING);whens.add(MORNING);whens.add(AFTERNOON);
        whens.add(EVENING);whens.add(NIGHT);whens.add(LATE_NIGHT);
        TimeBoxWhen timeBoxWhen = TimeBoxWhen.EARLY_MORNING;
        for(TimeBoxWhen when:whens){
            calStart.set(Calendar.HOUR_OF_DAY,when.getStartTime());
            calEnd.set(Calendar.HOUR_OF_DAY,when.getEndTime());
            calEnd.add(Calendar.MINUTE, -1);
            if(calendar.getTime().compareTo(calStart.getTime())>0 && calendar.getTime().compareTo(calEnd.getTime())<0)
                timeBoxWhen = when;
//                return when;
        }
        return timeBoxWhen;
    }
    public static  TimeBoxWhen getIntegerToEnumType(int id){
        TimeBoxWhen timeBoxWhen=null;
        switch (id){
            case 0: timeBoxWhen= EARLY_MORNING;break;
            case 1: timeBoxWhen= MORNING;break;
            case 2: timeBoxWhen= AFTERNOON;break;
            case 3: timeBoxWhen= EVENING;break;
            case 4: timeBoxWhen= NIGHT;break;
            case 5: timeBoxWhen= LATE_NIGHT;break;
        }
        return timeBoxWhen;
    }

    public static int getEnumToIntegerType(TimeBoxWhen timeBoxWhen){
        int intValue=0;
        switch (timeBoxWhen){
            case EARLY_MORNING:intValue=0;break;
            case MORNING:intValue=1;break;
            case AFTERNOON:intValue=2;break;
            case EVENING:intValue=3;break;
            case NIGHT:intValue=4;break;
            case LATE_NIGHT:intValue=5;break;
        }
        return intValue;
    }
}
