package com.greylabs.yoda.enums;

public enum TimeBoxTill {
    /**********************************************************************************************/
    // Enum Constants
    /**********************************************************************************************/
    WEEK("Week",0),
    MONTH("Month",1),
    QUARTER("Quarter",2),
    YEAR("Year",3),
    FOREVER("Forever",4);


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
    TimeBoxTill(String displayName,int value) {
        this.displayName = displayName;
        this.value=value;
    }

    /**********************************************************************************************/
    // Methods
    /**********************************************************************************************/
    public static TimeBoxTill getIntegerToEnumType(int id){
        TimeBoxTill timeBoxTill=null;
        switch (id){
            case 0: timeBoxTill= WEEK;break;
            case 1: timeBoxTill= MONTH;break;
            case 2: timeBoxTill= QUARTER;break;
            case 3: timeBoxTill= YEAR;break;
            case 4: timeBoxTill= FOREVER;break;
        }
        return timeBoxTill;
    }

    public static int getEnumToIntegerType(TimeBoxTill timeBoxTill){
        int intValue=0;
        switch (timeBoxTill){
            case WEEK:intValue=0;break;
            case MONTH:intValue=1;break;
            case QUARTER:intValue=2;break;
            case YEAR:intValue=3;break;
            case FOREVER:intValue=4;break;
        }
        return intValue;
    }
}
