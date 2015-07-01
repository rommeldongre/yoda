package com.greylabs.yoda.enums;

import java.util.List;

/**
 * Created by Jaybhay Vijay on 6/30/2015.
 */
public enum TimeBoxOn {
    /**********************************************************************************************/
    // Enum Constants
    /**********************************************************************************************/
    DAILY("Daily",0),
    WEEKLY("Weekly",1),
    MONTHLY("Monthly",2),
    QUATERLY("Quaterly",3),
    YEARLY("Yearly",4);


    /**********************************************************************************************/
    // Enum properties
    /**********************************************************************************************/
    private String displayName;
    private int value;
    private List<SubValue> subValues;
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

    public List<SubValue> getSubValues() {
        return subValues;
    }

    public void setSubValues(List<SubValue> subValues) {
        this.subValues = subValues;
    }
    /**********************************************************************************************/
    // Constructors
    /**********************************************************************************************/
    private TimeBoxOn(String displayName,int value){
        this.displayName=displayName;
        this.value=value;
    }


    /**********************************************************************************************/
    // Methods
    /**********************************************************************************************/
    public TimeBoxOn getIntegerToEnumType(int id){
        TimeBoxOn timeBoxOn=null;
        switch (id){
            case 0: timeBoxOn= DAILY;break;
            case 1: timeBoxOn= WEEKLY;break;
            case 2: timeBoxOn= MONTHLY;break;
            case 3: timeBoxOn= QUATERLY;break;
            case 4: timeBoxOn= YEARLY;break;
        }
        return timeBoxOn;
    }

    public int getEnumToIntegerType(TimeBoxOn timeBoxOn){
        int intValue=0;
        switch (timeBoxOn){
            case DAILY:intValue=0;break;
            case WEEKLY:intValue=1;break;
            case MONTHLY:intValue=2;break;
            case QUATERLY:intValue=3;break;
            case YEARLY:intValue=4;break;
        }
        return intValue;
    }
}
