package com.greylabs.yoda.enums;

/**
 * Created by Jaybhay Vijay on 6/30/2015.
 */
public enum WeekDay implements SubValue {
    /**********************************************************************************************/
    // Enum Constants
    /**********************************************************************************************/
    SUNDAY("Sunday","S",0),
    MONDAY("Monday","M",1),
    TUESDAY("Tuesday","T",2),
    WEDNESDAY("Wednesday","W",3),
    THURSDAY("Thursday","T",4),
    FRIDAY("Friday","F",5),
    SATURDAY("Saturday","S",6);

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
            case 0: weekDay= SUNDAY;break;
            case 1: weekDay= MONDAY;break;
            case 2: weekDay= TUESDAY;break;
            case 3: weekDay= WEDNESDAY;break;
            case 4: weekDay= THURSDAY;break;
            case 5: weekDay= FRIDAY;break;
            case 6: weekDay=SATURDAY;break;
        }
        return weekDay;
    }

    public static int getEnumToIntegerType(WeekDay weekDay){
        int intValue=0;
        switch (weekDay){
            case SUNDAY:intValue=0;break;
            case MONDAY:intValue=1;break;
            case TUESDAY:intValue=2;break;
            case WEDNESDAY:intValue=3;break;
            case THURSDAY:intValue=4;break;
            case FRIDAY:intValue=5;break;
            case SATURDAY:intValue=6;break;
        }
        return intValue;
    }
}
