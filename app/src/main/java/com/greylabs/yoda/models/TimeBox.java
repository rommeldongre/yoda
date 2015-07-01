package com.greylabs.yoda.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.greylabs.yoda.database.Database;
import com.greylabs.yoda.database.MetaData.TableTimeBox;
import com.greylabs.yoda.database.MetaData.TableTimeBoxOn;
import com.greylabs.yoda.database.MetaData.TableTimeBoxWhen;

import com.greylabs.yoda.enums.SubValue;
import com.greylabs.yoda.enums.TimeBoxOn;
import com.greylabs.yoda.enums.TimeBoxTill;
import com.greylabs.yoda.enums.TimeBoxWhen;

import java.util.ArrayList;
import java.util.Date;
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
        db.close();
        return this;
    }

    public List<TimeBox> getAll(){
        ArrayList<TimeBox> timeBoxes=null;
        SQLiteDatabase db=database.getReadableDatabase();
        String query="select * " +
                " "+" from "+ TableTimeBox.timeBox+" " +
                " "+"where "+TableTimeBox.id+" = "+id;

        Cursor c=db.rawQuery(query,null);
        if(c.moveToFirst()){
            timeBoxes=new ArrayList<>();
            do{
                TimeBox timeBox=new TimeBox(context);
                timeBox.id=c.getInt(c.getColumnIndex(TableTimeBox.id));
                timeBox.nickName=c.getString(c.getColumnIndex(TableTimeBox.nickName));
                timeBox.timeBoxOnType=c.getInt(c.getColumnIndex(TableTimeBox.on));
                timeBox.timeBoxTillType=c.getInt(c.getColumnIndex(TableTimeBox.till));
                timeBoxes.add(timeBox);

            }while (c.moveToNext());
        }
        c.close();
        db.close();
        return timeBoxes;
    }

    public long save(){
        SQLiteDatabase db=database.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(TableTimeBox.id,this.id);
        values.put(TableTimeBox.nickName,this.nickName);
        values.put(TableTimeBox.on,this.timeBoxOnType);
        values.put(TableTimeBox.till, this.timeBoxTillType);

        long rowId=db.insertWithOnConflict(TableTimeBox.timeBox, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();

        return rowId;
    }

    public int delete(long id){
        SQLiteDatabase db=database.getWritableDatabase();
        int numOfRowAffected=db.delete(TableTimeBox.timeBox, TableTimeBox.id + "=" + id, null);
        db.close();
        return numOfRowAffected;
    }

    public List<Integer> getTimeBoxOn(long timeBoxId){
        ArrayList<Integer> timeBoxOnSubValues=null;
        SQLiteDatabase db=database.getReadableDatabase();
        String query="select * " +
                " "+" from "+ TableTimeBoxOn.timeBoxOn+" " +
                " "+"where "+TableTimeBoxOn.id+" = "+timeBoxId;

        Cursor c=db.rawQuery(query,null);
        if(c.moveToFirst()){
            timeBoxOnSubValues=new ArrayList<>();
            do{
                this.id=c.getInt(c.getColumnIndex(TableTimeBoxOn.id));
                int on=c.getInt(c.getColumnIndex(TableTimeBoxOn.on));
                timeBoxOnSubValues.add(new Integer(on));

            }while (c.moveToNext());
        }
        c.close();
        db.close();
        return timeBoxOnSubValues;
    }

    public List<Integer> getTimeBoxWhen(long timeBoxId){
        ArrayList<Integer> timeBoxWhenSubValues=null;
        SQLiteDatabase db=database.getReadableDatabase();
        String query="select * " +
                " "+" from "+ TableTimeBoxWhen.timeBoxWhen+" " +
                " "+"where "+TableTimeBoxWhen.id+" = "+timeBoxId;

        Cursor c=db.rawQuery(query,null);
        if(c.moveToFirst()){
            timeBoxWhenSubValues=new ArrayList<>();
            do{
                this.id=c.getInt(c.getColumnIndex(TableTimeBoxOn.id));
                int on=c.getInt(c.getColumnIndex(TableTimeBoxOn.on));
                timeBoxWhenSubValues.add(new Integer(on));

            }while (c.moveToNext());
        }
        c.close();
        db.close();
        return timeBoxWhenSubValues;
    }




}
