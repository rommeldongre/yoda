package com.greylabs.ydo.enums;

import java.util.Calendar;

public enum WeekDay implements SubValue {
    /**********************************************************************************************/
    // Enum Constants
    /**********************************************************************************************/
    SUNDAY("Sun","S", Calendar.SUNDAY),
    MONDAY("Mon","M",Calendar.MONDAY),
    TUESDAY("Tue","T",Calendar.TUESDAY),
    WEDNESDAY("Wed","W",Calendar.WEDNESDAY),
    THURSDAY("Thu","T",Calendar.THURSDAY),
    FRIDAY("Fri","F",Calendar.FRIDAY),
    SATURDAY("Sat","S",Calendar.SATURDAY);

    /**********************************************************************************************/
    // Enum properties
    /**********************************************************************************************/
    private String displayName;
    private String shortName;
    private int value;

    /**********************************************************************************************/
    //Getters and Setters
    /**********************************************************************************************/

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
    /**********************************************************************************************/
    // Constructors
    /**********************************************************************************************/
    WeekDay(String displayName, String shortName,int value) {
        this.displayName = displayName;
        this.shortName=shortName;
        this.value=value;
    }

    /**********************************************************************************************/
    // Methods
    /**********************************************************************************************/
    public static  WeekDay getIntegerToEnumType(int id){
        WeekDay weekDay=null;
        switch (id){
            case 1: weekDay= SUNDAY;break;
            case 2: weekDay= MONDAY;break;
            case 3: weekDay= TUESDAY;break;
            case 4: weekDay= WEDNESDAY;break;
            case 5: weekDay= THURSDAY;break;
            case 6: weekDay= FRIDAY;break;
            case 7: weekDay=SATURDAY;break;
        }
        return weekDay;
    }

    public static int getEnumToIntegerType(WeekDay weekDay){
        int intValue=0;
        switch (weekDay){
            case SUNDAY:intValue=SUNDAY.getValue();break;
            case MONDAY:intValue=MONDAY.getValue();break;
            case TUESDAY:intValue=TUESDAY.getValue();break;
            case WEDNESDAY:intValue=WEDNESDAY.getValue();break;
            case THURSDAY:intValue=THURSDAY.getValue();break;
            case FRIDAY:intValue=FRIDAY.getValue();break;
            case SATURDAY:intValue=SATURDAY.getValue();break;
        }
        return intValue;
    }
}
