package com.greylabs.yoda.enums;

/**
 * Created by Jaybhay Vijay on 7/16/2015.
 */
public enum Daily implements SubValue {
    /**********************************************************************************************/
    // Enum Constants
    /**********************************************************************************************/
    DAILY("Daily",0);
    /**********************************************************************************************/
    // Enum properties
    /**********************************************************************************************/
    private String displayName;
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
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    /**********************************************************************************************/
    // Constructors
    /**********************************************************************************************/
    Daily(String displayName,int value) {
        this.displayName = displayName;
        this.value=value;
    }

    /**********************************************************************************************/
    // Methods
    /**********************************************************************************************/
    public static Daily getIntegerToEnumType(int id){
        Daily daily=null;
        switch (id){
            case 0: daily= DAILY;break;
        }
        return daily;
    }

    public static int getEnumToIntegerType(Daily daily){
        int intValue=0;
        switch (daily){
            case DAILY:intValue=DAILY.getValue();break;
        }
        return intValue;
    }


}
