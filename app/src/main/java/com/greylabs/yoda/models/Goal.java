package com.greylabs.yoda.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.greylabs.yoda.database.Database;
import com.greylabs.yoda.database.MetaData.TableTimeBox;
import com.greylabs.yoda.database.MetaData.TableGoal;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Jaybhay Vijay on 6/30/2015.
 */
public class Goal {
    /**********************************************************************************************/
    // Instance variables
    /**********************************************************************************************/
    private long id;
    private String nickName;
    private String objective;
    private String keyResult;
    private String reason;
    private String reward;
    private String buddyEmail;
    private byte status;
    private int order;
    private Timestamp dueDate;
    private long timeBoxId;
    private Database database;
    private Context context;


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

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public String getKeyResult() {
        return keyResult;
    }

    public void setKeyResult(String keyResult) {
        this.keyResult = keyResult;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getBuddyEmail() {
        return buddyEmail;
    }

    public void setBuddyEmail(String buddyEmail) {
        this.buddyEmail = buddyEmail;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Timestamp getDueDate() {
        return dueDate;
    }

    public void setDueDate(Timestamp dueDate) {
        this.dueDate = dueDate;
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
    public Goal(Context context){
        this.database=Database.getInstance(context);
        this.context=context;
        dueDate=new Timestamp(new Date().getTime());
    }

    /**********************************************************************************************/
    // Methods
    /**********************************************************************************************/

    @Override
    public String toString() {
        return "Goal{" +
                "id=" + id +
                ", nickName='" + nickName + '\'' +
                ", objective='" + objective + '\'' +
                ", keyResult='" + keyResult + '\'' +
                ", reason='" + reason + '\'' +
                ", reward='" + reward + '\'' +
                ", buddyEmail='" + buddyEmail + '\'' +
                ", status=" + status +
                ", order=" + order +
                ", dueDate=" + dueDate +
                ", timeBoxId=" + timeBoxId +
                '}';
    }
    public Goal get(long id){
        SQLiteDatabase db=database.getReadableDatabase();
        String query="select * " +
                " "+" from "+ TableGoal.goal+" " +
                " "+"where "+ TableGoal.id+" = "+id;
        Cursor c=db.rawQuery(query,null);
        if(c.moveToFirst()){
            do{
                this.id=c.getInt(c.getColumnIndex(TableGoal.id));
                this.nickName=c.getString(c.getColumnIndex(TableGoal.nickName));
                this.objective=c.getString(c.getColumnIndex(TableGoal.objective));
                this.keyResult=c.getString(c.getColumnIndex(TableGoal.keyResult));
                this.reason=c.getString(c.getColumnIndex(TableGoal.reason));
                this.reward=c.getString(c.getColumnIndex(TableGoal.buddyEmail));
                this.status=(byte)c.getInt(c.getColumnIndex(TableGoal.status));
                this.dueDate=Timestamp.valueOf(c.getString(c.getColumnIndex(TableGoal.dueDate)));
                this.timeBoxId=c.getInt(c.getColumnIndex(TableGoal.timeBoxId));
            }while (c.moveToNext());
        }
        c.close();
        return this;
    }

    public List<Goal> getAll(){
        ArrayList<Goal> goals=null;
        SQLiteDatabase db=database.getReadableDatabase();
        String query="select * " +
                " "+" from "+ TableGoal.goal+" ";
        Cursor c=db.rawQuery(query,null);
        if(c.moveToFirst()){
            goals=new ArrayList<>();
            do{
                Goal goal=new Goal(context);
                goal.id=c.getInt(c.getColumnIndex(TableGoal.id));
                goal.nickName=c.getString(c.getColumnIndex(TableGoal.nickName));
                goal.objective=c.getString(c.getColumnIndex(TableGoal.objective));
                goal.keyResult=c.getString(c.getColumnIndex(TableGoal.keyResult));
                goal.reason=c.getString(c.getColumnIndex(TableGoal.reason));
                goal.reward=c.getString(c.getColumnIndex(TableGoal.buddyEmail));
                goal.status=(byte)c.getInt(c.getColumnIndex(TableGoal.status));
                goal.dueDate=Timestamp.valueOf(c.getString(c.getColumnIndex(TableGoal.dueDate)));
                goal.timeBoxId=c.getInt(c.getColumnIndex(TableGoal.timeBoxId));

                goals.add(goal);
            }while (c.moveToNext());
        }
        c.close();
        return goals;
    }

    public long save() {
        SQLiteDatabase db=database.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(TableGoal.nickName,this.nickName);
        values.put(TableGoal.objective,this.objective);
        values.put(TableGoal.keyResult,this.keyResult);
        values.put(TableGoal.reason,this.reason);
        values.put(TableGoal.reward,this.reward);
        values.put(TableGoal.buddyEmail,this.buddyEmail);
        values.put(TableGoal.status,this.status);
        values.put(TableGoal.order,this.order);
        values.put(TableGoal.dueDate,this.dueDate.toString());
        values.put(TableGoal.timeBoxId, this.timeBoxId);
        long rowId;
        if (this.id!=0){
            values.put(TableGoal.id,this.id);
        }
        rowId=db.insertWithOnConflict(TableGoal.goal, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        this.id=rowId;
        return rowId;
    }

    public int delete(long id){
        SQLiteDatabase db=database.getWritableDatabase();
        int numOfRowAffected=db.delete(TableGoal.goal,TableGoal.id+"="+id,null);
        return numOfRowAffected;
    }



}
