package com.greylabs.yoda.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.greylabs.yoda.database.Database;
import com.greylabs.yoda.database.MetaData.TableTimeBoxWhen;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

public class TimeBoxWhen implements Serializable{

    /**********************************************************************************************/
    // Instance variables
    /**********************************************************************************************/
    private long timeBoxId;
    private Set<com.greylabs.yoda.enums.TimeBoxWhen> whenValues;
    transient private Database database;
    transient private Context context;

    /**********************************************************************************************/
    // Geters and Setters
    /**********************************************************************************************/
    public long getTimeBoxId() {
        return timeBoxId;
    }

    public void setTimeBoxId(long timeBoxId) {
        this.timeBoxId = timeBoxId;
    }

    public void setWhenValues(Set<com.greylabs.yoda.enums.TimeBoxWhen> whenValues) {
        this.whenValues = whenValues;
    }
    public void initDatabase(Context context){
        this.database=Database.getInstance(context);
    }

    public Set<com.greylabs.yoda.enums.TimeBoxWhen> getWhenValues(){
        return this.whenValues;
    }

    /**********************************************************************************************/
    // Constructors
    /**********************************************************************************************/
    public TimeBoxWhen(Context context){
        database=Database.getInstance(context);
        this.context=context;
    }

    /**********************************************************************************************/
    // Methods
    /**********************************************************************************************/
    public  Set<com.greylabs.yoda.enums.TimeBoxWhen> get(){
        Set<com.greylabs.yoda.enums.TimeBoxWhen> whenValues=null;
        SQLiteDatabase db=database.getReadableDatabase();
        String query="select * " +
                " "+" from "+ TableTimeBoxWhen.timeBoxWhen+" " +
                " "+"where "+ TableTimeBoxWhen.id+" = "+this.timeBoxId;

        Cursor c=db.rawQuery(query,null);
        if(c.moveToFirst()){
            whenValues=new TreeSet<>();
            do{
                int value=c.getInt(c.getColumnIndex(TableTimeBoxWhen.when));

                whenValues.add(com.greylabs.yoda.enums.TimeBoxWhen.getIntegerToEnumType(value));
            }while (c.moveToNext());
        }
        c.close();
        return whenValues;
    }

    public long save(){
        delete();
        SQLiteDatabase db=database.getWritableDatabase();
        long rowId=0;
        for(com.greylabs.yoda.enums.TimeBoxWhen when:whenValues) {
            ContentValues values = new ContentValues();
            values.put(TableTimeBoxWhen.when, com.greylabs.yoda.enums.TimeBoxWhen.getEnumToIntegerType(when));
            values.put(TableTimeBoxWhen.id, timeBoxId);
            rowId += db.insert(TableTimeBoxWhen.timeBoxWhen, null, values);
        }
        return rowId;
    }

    public int delete(){
        SQLiteDatabase db=database.getWritableDatabase();
        int numOfRowAffected=db.delete(TableTimeBoxWhen.timeBoxWhen,TableTimeBoxWhen.id + "=" + timeBoxId, null);
        return numOfRowAffected;
    }

    @Override
    public String toString() {
        return "TimeBoxWhen{" +
                "timeBoxId=" + timeBoxId +
                ", whenValues=" + whenValues +
                '}';
    }
    /**********************************************************************************************/
    // Utils
    /**********************************************************************************************/
}
