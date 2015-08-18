package com.greylabs.yoda.enums;

public enum Year implements SubValue{
    /**********************************************************************************************/
    // Enum Constants
    /**********************************************************************************************/
    JANUARY("Jan","Jan",0),
    FEBRUARY("Feb","Feb",1),
    MARCH("Mar","Mar",2),
    APRIL("Apr","Apr",3),
    MAY("May","May",4),
    JUNE("Jun","Jun",5),
    JULY("Jul","Jul",6),
    AUGUST("Aug","Aug",7),
    SEPTEMBER("Sep","Sep",8),
    OCTOBER("Oct","Oct",9),
    NOVEMBER("Nov","Nov",10),
    DECEMBER("Dec","Dec",11);



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
    Year(String displayName,String shortName,int value){
        this.displayName=displayName;
        this.shortName=shortName;
        this.value=value;
    }

    /**********************************************************************************************/
    // Methods
    /**********************************************************************************************/
    public static  Year getIntegerToEnumType(int id){
        Year year=null;
        switch (id){
            case 0: year= JANUARY;break;
            case 1: year= FEBRUARY;break;
            case 2: year= MARCH;break;
            case 3: year= APRIL;break;
            case 4: year= MAY;break;
            case 5: year= JUNE;break;

            case 6: year= JULY;break;
            case 7: year= AUGUST;break;
            case 8: year= SEPTEMBER;break;
            case 9: year= OCTOBER;break;
            case 10: year= NOVEMBER;break;
            case 11: year= DECEMBER;break;
        }
        return year;
    }

    public static  int getEnumToIntegerType(Year year){
        int intValue=0;
        switch (year){
            case JANUARY:intValue=0;break;
            case FEBRUARY:intValue=1;break;
            case MARCH:intValue=2;break;
            case APRIL:intValue=3;break;
            case MAY:intValue=4;break;
            case JUNE:intValue=5;break;

            case JULY:intValue=6;break;
            case AUGUST:intValue=7;break;
            case SEPTEMBER:intValue=8;break;
            case OCTOBER:intValue=9;break;
            case NOVEMBER:intValue=10;break;
            case DECEMBER:intValue=11;break;
        }
        return intValue;
    }
}
