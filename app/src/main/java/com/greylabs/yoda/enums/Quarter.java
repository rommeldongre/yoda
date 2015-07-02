package com.greylabs.yoda.enums;

public enum Quarter implements SubValue {
    /**********************************************************************************************/
    // Enum Constants
    /**********************************************************************************************/
    MONTH1("1 Month",0),
    MONTH2("2 Month",1),
    MONTH3("3 Month",2);


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
     Quarter(String displayName,int value){
        this.displayName=displayName;
        this.value=value;
    }


    /**********************************************************************************************/
    // Methods
    /**********************************************************************************************/
    public static Quarter getIntegerToEnumType(int id){
        Quarter quarter=null;
        switch (id){
            case 0: quarter= MONTH1;break;
            case 1: quarter= MONTH2;break;
            case 2: quarter= MONTH3;break;
        }
        return quarter;
    }

    public static int getEnumToIntegerType(Quarter quarter){
        int intValue=0;
        switch (quarter){
            case MONTH1:intValue=0;break;
            case MONTH2:intValue=1;break;
            case MONTH3:intValue=2;break;
        }
        return intValue;
    }
}
