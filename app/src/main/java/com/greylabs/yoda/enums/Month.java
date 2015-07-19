package com.greylabs.yoda.enums;

public enum Month  implements SubValue{
    /**********************************************************************************************/
    // Enum Constants
    /**********************************************************************************************/
    WEEK1("Week1",1),
    WEEK2("Week2",2),
    WEEK3("Week3",3),
    WEEK4("Week4",4);

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
    public static Month getIntegerToEnumType(int id){
        Month month=null;
        switch (id){
            case 1: month= WEEK1;break;
            case 2: month= WEEK2;break;
            case 3: month= WEEK3;break;
            case 4: month= WEEK4;break;
        }
        return month;
    }

    public static int getEnumToIntegerType(Month month){
        int intValue=0;
        switch (month){
            case WEEK1:intValue=WEEK1.getValue();break;
            case WEEK2:intValue=WEEK2.getValue();break;
            case WEEK3:intValue=WEEK3.getValue();break;
            case WEEK4:intValue=WEEK4.getValue();break;
        }
        return intValue;
    }

}
