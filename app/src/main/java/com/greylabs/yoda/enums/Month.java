package com.greylabs.yoda.enums;

/**
 * Created by Jaybhay Vijay on 6/30/2015.
 */
public enum Month  implements SubValue{
    /**********************************************************************************************/
    // Enum Constants
    /**********************************************************************************************/
    WEEK1("Week1",0),
    WEEK2("Week2",1),
    WEEK3("Week3",2),
    WEEK4("Week4",3);

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
    Month(String displayName,int value) {
        this.displayName = displayName;
        this.value=value;
    }

    /**********************************************************************************************/
    // Methods
    /**********************************************************************************************/
    public Month getIntegerToEnumType(int id){
        Month month=null;
        switch (id){
            case 0: month= WEEK1;break;
            case 1: month= WEEK2;break;
            case 2: month= WEEK3;break;
            case 3: month= WEEK4;break;
        }
        return month;
    }

    public int getEnumToIntegerType(Month month){
        int intValue=0;
        switch (month){
            case WEEK1:intValue=0;break;
            case WEEK2:intValue=1;break;
            case WEEK3:intValue=2;break;
            case WEEK4:intValue=3;break;
        }
        return intValue;
    }

}
