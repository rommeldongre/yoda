package com.greylabs.yoda.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.greylabs.yoda.database.Database;
import com.greylabs.yoda.database.MetaData.TableTimeBox;
import com.greylabs.yoda.database.MetaData.TableTimeBoxOn;
import com.greylabs.yoda.database.MetaData.TableTimeBoxWhen;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jaybhay Vijay on 6/30/2015.
 */
public class TimeBox {
    /**********************************************************************************************/
    // Instance variables
    /**********************************************************************************************/
    private long id;
    private String nickName;
    private int timeBoxOnType;
    private int  timeBoxTillType;
    private Context context;
    private Database database;
    private TimeBoxOn timeBoxOn;
    private TimeBoxWhen timeBoxWhen;

    /**********************************************************************************************/
    //Getters and Setters
    /**********************************************************************************************/
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public int getTimeBoxOnType() {
        return timeBoxOnType;
    }

    public void setTimeBoxOnType(int timeBoxOnType) {
        this.timeBoxOnType = timeBoxOnType;
    }

    public int getTimeBoxTillType() {
        return timeBoxTillType;
    }

    public void setTimeBoxTillType(int timeBoxTillType) {
        this.timeBoxTillType = timeBoxTillType;
    }

    public TimeBoxOn getTimeBoxOn() {
        return timeBoxOn;
    }
    public void setTimeBoxOn(TimeBoxOn timeBoxOn) {
        this.timeBoxOn = timeBoxOn;
    }

    public TimeBoxWhen getTimeBoxWhen() {
        return timeBoxWhen;
    }

    public void setTimeBoxWhen(TimeBoxWhen timeBoxWhen) {
        this.timeBoxWhen = timeBoxWhen;
    }
    public void initDatabase(Context context){
        this.database=Database.getInstance(context);
    }
    /**********************************************************************************************/
    // Constructors
    /**********************************************************************************************/
    public TimeBox(Context context){
        this.context=context;
        this.database=Database.getInstance(context);
    }


    /**********************************************************************************************/
    // Methods
    /**********************************************************************************************/

    @Override
    public String toString() {
        return "TimeBox{" +
                "id=" + id +
                ", nickName='" + nickName + '\'' +
                ", timeBoxOnType=" + timeBoxOnType +
                ", timeBoxTillType=" + timeBoxTillType +
                '}';
    }

    public TimeBox get(long id){
        SQLiteDatabase db=database.getReadableDatabase();
        String query="select * " +
                " "+" from "+ TableTimeBox.timeBox+" " +
                " "+"where "+TableTimeBox.id+" = "+id;

        Cursor c=db.rawQuery(query,null);
        if(c.moveToFirst()){
            do{
                this.id=c.getInt(c.getColumnIndex(TableTimeBox.id));
                this.nickName=c.getString(c.getColumnIndex(TableTimeBox.nickName));
                this.timeBoxOnType=c.getInt(c.getColumnIndex(TableTimeBox.on));
                this.timeBoxTillType=c.getInt(c.getColumnIndex(TableTimeBox.till));
            }while (c.moveToNext());
        }
        c.close();
        //db.close();
        this.timeBoxOn=new TimeBoxOn(context,this.id,this.timeBoxOnType);
        this.timeBoxOn.setSubValues(this.timeBoxOn.get());
        this.timeBoxWhen=new TimeBoxWhen(context,this.id);
        this.timeBoxWhen.setWhenValues(this.timeBoxWhen.get());
        return this;
    }

    public List<TimeBox> getAll(){
        ArrayList<TimeBox> timeBoxes=null;
        SQLiteDatabase db=database.getReadableDatabase();
        String query="select * " +
                " "+" from "+ TableTimeBox.timeBox+" ";

        Cursor c=db.rawQuery(query, null);
        if(c.moveToFirst()){
            timeBoxes=new ArrayList<>();
            do{
                TimeBox timeBox=new TimeBox(context);
                timeBox.id=c.getInt(c.getColumnIndex(TableTimeBox.id));
                timeBox.nickName=c.getString(c.getColumnIndex(TableTimeBox.nickName));
                timeBox.timeBoxOnType=c.getInt(c.getColumnIndex(TableTimeBox.on));
                timeBox.timeBoxTillType=c.getInt(c.getColumnIndex(TableTimeBox.till));
                this.timeBoxOn=new TimeBoxOn(context,this.id,this.timeBoxOnType);
                this.timeBoxOn.setSubValues(this.timeBoxOn.get());
                this.timeBoxWhen=new TimeBoxWhen(context,this.id);
                this.timeBoxWhen.setWhenValues(this.timeBoxWhen.get());
                timeBoxes.add(timeBox);

            }while (c.moveToNext());
        }
        c.close();
        //db.close();
        return timeBoxes;
    }

    public long save(){
        SQLiteDatabase db=database.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(TableTimeBox.nickName,this.nickName);
        values.put(TableTimeBox.on, this.timeBoxOnType);
        values.put(TableTimeBox.till, this.timeBoxTillType);
        long rowId;
        if(this.id!=0){
            values.put(TableTimeBox.id,this.id);
        }
        rowId=db.insertWithOnConflict(TableTimeBox.timeBox, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        //db.close();
        this.timeBoxOn.setTimeBoxId(rowId);
        this.timeBoxOn.save();
        this.timeBoxWhen.setTimeBoxId(rowId);
        this.timeBoxWhen.save();
        this.id=rowId;
        return rowId;
    }

    public int delete(){
        SQLiteDatabase db=database.getWritableDatabase();
        int numOfRowAffected=db.delete(TableTimeBox.timeBox, TableTimeBox.id + "=" + id, null);
        //db.close();
        this.timeBoxOn.delete();
        this.timeBoxWhen.delete();
        return numOfRowAffected;
    }

    public long saveTimeBoxOn(long timeBoxId,List<Integer> timeBoxOns){
        SQLiteDatabase db=database.getWritableDatabase();
        long rowId=0;
        for(int val:timeBoxOns) {
            ContentValues values = new ContentValues();
            values.put(TableTimeBoxOn.id, timeBoxId);
            values.put(TableTimeBoxOn.on, val);
            rowId += db.insertWithOnConflict(TableTimeBoxOn.timeBoxOn, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        }
        db.close();
        return rowId;
    }

    public long saveTimeBoxWhen(long timeBoxId,List<Integer> timeBoxWhens){
        SQLiteDatabase db=database.getWritableDatabase();
        long rowId=0;
        for(int val:timeBoxWhens) {
            ContentValues values = new ContentValues();
            values.put(TableTimeBoxWhen.id, timeBoxId);
            values.put(TableTimeBoxWhen.when, val);
            rowId += db.insertWithOnConflict(TableTimeBoxWhen.timeBoxWhen, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        }
        db.close();
        return rowId;
    }
}
