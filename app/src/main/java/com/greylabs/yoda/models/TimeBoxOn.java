package com.greylabs.yoda.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.greylabs.yoda.database.Database;
import com.greylabs.yoda.database.MetaData.TableTimeBoxOn;
import com.greylabs.yoda.enums.Month;
import com.greylabs.yoda.enums.Quarter;
import com.greylabs.yoda.enums.SubValue;
import com.greylabs.yoda.enums.WeekDay;
import com.greylabs.yoda.enums.Year;

import java.util.ArrayList;
import java.util.List;
import static com.greylabs.yoda.enums.TimeBoxOn.*;

/**
 * Created by Jaybhay Vijay on 7/3/2015.
 */
public class TimeBoxOn {
    /**********************************************************************************************/
    // Instance variables
    /**********************************************************************************************/
    private long timeBoxId;
    private int onType;
    private List<SubValue> subValues;
    private Database database;
    private Context context;

    /**********************************************************************************************/
    // Geters and Setters
    /**********************************************************************************************/
    public List<SubValue> getSubValues() {
        return subValues;
    }

    public void setSubValues(List<SubValue> subValues) {
        this.subValues = subValues;
    }

    public int getOnType() {
        return onType;
    }

    public void setOnType(int onType) {
        this.onType = onType;
    }
    public long getTimeBoxId() {
        return timeBoxId;
    }

    public void setTimeBoxId(long timeBoxId) {
        this.timeBoxId = timeBoxId;
    }

    /**********************************************************************************************/
    // Constructors
    /**********************************************************************************************/
    public TimeBoxOn(Context context, long timeBoxId, int timeBoxOnType){
        database=Database.getInstance(context);
        this.context=context;
        this.timeBoxId=timeBoxId;
        this.onType=timeBoxOnType;
    }

    /**********************************************************************************************/
    // Methods
    /**********************************************************************************************/

    public List<SubValue> get(){
        List<SubValue> subValues=null;
        SQLiteDatabase db=database.getReadableDatabase();
        String query="select * " +
                " "+" from "+ TableTimeBoxOn.timeBoxOn+" " +
                " "+"where "+TableTimeBoxOn.id+" = "+timeBoxId;

        Cursor c=db.rawQuery(query,null);
        if(c.moveToFirst()){
            subValues=new ArrayList();
            do{
                int value=c.getInt(c.getColumnIndex(TableTimeBoxOn.id));
                subValues.add(getEnumTypeFromInteger(value, com.greylabs.yoda.enums.TimeBoxOn.getIntegerToEnumType(onType)));
            }while (c.moveToNext());
        }
        c.close();
        db.close();
        return subValues;
    }

    public long save(){
        delete();
        SQLiteDatabase db=database.getWritableDatabase();
        long rowId = 0;
        for(com.greylabs.yoda.enums.SubValue on:subValues) {
            ContentValues values = new ContentValues();
            values.put(TableTimeBoxOn.on, getIntegerFromEnumType(on,com.greylabs.yoda.enums.TimeBoxOn.getIntegerToEnumType(onType)));
            values.put(TableTimeBoxOn.id, timeBoxId);
            rowId += db.insert(TableTimeBoxOn.timeBoxOn, null, values);
        }
            db.close();
        return rowId;
    }

    public int delete(){
        SQLiteDatabase db=database.getWritableDatabase();
        int numOfRowAffected=db.delete(TableTimeBoxOn.timeBoxOn, TableTimeBoxOn.id + "=" + timeBoxId, null);
        db.close();
        return numOfRowAffected;
    }



    /**********************************************************************************************/
    // Utils
    /**********************************************************************************************/
    private SubValue getEnumTypeFromInteger(int value, com.greylabs.yoda.enums.TimeBoxOn onType){
        SubValue subValue=null;
        switch (onType){
            case DAILY:
                subValue=null;
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
                break;
            case WEEKLY:
                value= WeekDay.getEnumToIntegerType((WeekDay)subValue);
                break;
            case MONTHLY:
                value= Month.getEnumToIntegerType((Month) subValue);
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
