package com.greylabs.yoda.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.greylabs.yoda.database.Database;
import com.greylabs.yoda.database.MetaData.TableTimeBoxOn;
import com.greylabs.yoda.enums.*;
import com.greylabs.yoda.enums.Daily;

import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Jaybhay Vijay on 7/3/2015.
 */
public class TimeBoxOn {
    /**********************************************************************************************/
    // Instance variables
    /**********************************************************************************************/
    private long timeBoxId;
    private com.greylabs.yoda.enums.TimeBoxOn onType;
    private Set<SubValue> subValues;
    private Database database;
    private Context context;

    /**********************************************************************************************/
    // Geters and Setters
    /**********************************************************************************************/

    public void setOnType(com.greylabs.yoda.enums.TimeBoxOn onType) {
        this.onType = onType;
    }
    public com.greylabs.yoda.enums.TimeBoxOn getOnType() {
        return onType;
    }
    public void setSubValues(Set<SubValue> subValues) {
        this.subValues = subValues;
    }
    public Set<SubValue> getSubValues(){
        return this.subValues;
    }

    public long getTimeBoxId() {
        return timeBoxId;
    }

    public void setTimeBoxId(long timeBoxId) {
        this.timeBoxId = timeBoxId;
    }
    public void initDatabase(Context context){
        this.database=Database.getInstance(context);
    }
    /**********************************************************************************************/
    // Constructors
    /**********************************************************************************************/
    public TimeBoxOn(Context context, com.greylabs.yoda.enums.TimeBoxOn onType){
        database=Database.getInstance(context);
        this.context=context;
        this.onType=onType;
    }

    /**********************************************************************************************/
    // Methods
    /**********************************************************************************************/

    public Set<SubValue> get(){
        Set<SubValue>  subValues=new TreeSet<>();
        SQLiteDatabase db=database.getReadableDatabase();
        String query="select * " +
                " "+" from "+ TableTimeBoxOn.timeBoxOn+" " +
                " "+"where "+TableTimeBoxOn.id+" = "+timeBoxId;

        Cursor c=db.rawQuery(query,null);
        if(c.moveToFirst()){
            do{
                timeBoxId=c.getInt(c.getColumnIndex(TableTimeBoxOn.id));
                int on= c.getInt(c.getColumnIndex(TableTimeBoxOn.on));
                subValues.add(getEnumTypeFromInteger(on,onType));
            }while (c.moveToNext());
        }
        c.close();
        return subValues;
    }

    public long save(){
        delete();
        SQLiteDatabase db=database.getWritableDatabase();
        long rowId = 0;
        for(com.greylabs.yoda.enums.SubValue on:subValues) {
            ContentValues values = new ContentValues();
            values.put(TableTimeBoxOn.on, getIntegerFromEnumType(on,onType));
            values.put(TableTimeBoxOn.id, timeBoxId);
            rowId += db.insert(TableTimeBoxOn.timeBoxOn, null, values);
        }
        return rowId;
    }

    public int delete(){
        SQLiteDatabase db=database.getWritableDatabase();
        int numOfRowAffected=db.delete(TableTimeBoxOn.timeBoxOn, TableTimeBoxOn.id + "=" + timeBoxId, null);
        return numOfRowAffected;
    }

    @Override
    public String toString() {
        return "TimeBoxOn{" +
                "timeBoxId=" + timeBoxId +
                ", onType=" + onType +
                ", subValues=" + subValues +
                '}';
    }

    /**********************************************************************************************/
    // Utils
    /**********************************************************************************************/
    private SubValue getEnumTypeFromInteger(int value, com.greylabs.yoda.enums.TimeBoxOn onType){
        SubValue subValue=null;
        switch (onType){
            case DAILY:
                subValue= Daily.getIntegerToEnumType(value);
                break;
            case WEEKLY:
                subValue= WeekDay.getIntegerToEnumType(value);
                break;
            case MONTHLY:
                subValue= Month.getIntegerToEnumType(value);
                break;
            case QUATERLY:
                subValue= Quarter.getIntegerToEnumType(value);
                break;
            case YEARLY:
                subValue= Year.getIntegerToEnumType(value);
                break;
        }
        return subValue;
    }

    private int  getIntegerFromEnumType(SubValue subValue,com.greylabs.yoda.enums.TimeBoxOn onType){
        int value=0;
        switch (onType){
            case DAILY:
                value=Daily.getEnumToIntegerType((Daily)subValue);
                break;
            case WEEKLY:
                value= WeekDay.getEnumToIntegerType((WeekDay)subValue);
                break;
            case MONTHLY:
                value= Month.getEnumToIntegerType((Month)subValue);
                break;
            case QUATERLY:
                value= Quarter.getEnumToIntegerType((Quarter)subValue);
                break;
            case YEARLY:
                value= Year.getEnumToIntegerType((Year)subValue);
                break;
        }
        return value;
    }
}
