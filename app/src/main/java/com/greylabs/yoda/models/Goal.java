package com.greylabs.yoda.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.greylabs.yoda.database.Database;
import com.greylabs.yoda.database.MetaData;
import com.greylabs.yoda.database.MetaData.TableGoal;
import com.greylabs.yoda.database.MetaData.TablePendingStep;
import com.greylabs.yoda.utils.CalendarUtils;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Goal implements Serializable{
    /**********************************************************************************************/
    // Instance variables
    /**********************************************************************************************/
    private long id;
    private String stringId="";//used for google tasks.
    private String nickName;
    private String objective;
    private String keyResult;
    private String reason;
    private String reward;
    private String buddyEmail;
    private byte status;
    private int order;
    private int progress;//progress of goal in percent
    private Date dueDate;
    private long timeBoxId;
    transient private Database database;
    transient private Context context;


    /**********************************************************************************************/
    //Getters and Setters
    /**********************************************************************************************/
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStringId() {
        return stringId;
    }

    public void setStringId(String stringId) {
        this.stringId = stringId;
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
    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public long getTimeBoxId() {
        return timeBoxId;
    }
    public void initDatabase(Context context){
        this.database=Database.getInstance(context);
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
                this.stringId=c.getString(c.getColumnIndex(TableGoal.stringId));
                this.nickName=c.getString(c.getColumnIndex(TableGoal.nickName));
                this.objective=c.getString(c.getColumnIndex(TableGoal.objective));
                this.keyResult=c.getString(c.getColumnIndex(TableGoal.keyResult));
                this.reason=c.getString(c.getColumnIndex(TableGoal.reason));
                this.reward=c.getString(c.getColumnIndex(TableGoal.buddyEmail));
                this.status=(byte)c.getInt(c.getColumnIndex(TableGoal.status));
                this.dueDate= CalendarUtils.parseDate(c.getString(c.getColumnIndex(TableGoal.dueDate)));
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
                goal.stringId=c.getString(c.getColumnIndex(TableGoal.stringId));
                goal.nickName=c.getString(c.getColumnIndex(TableGoal.nickName));
                goal.objective=c.getString(c.getColumnIndex(TableGoal.objective));
                goal.keyResult=c.getString(c.getColumnIndex(TableGoal.keyResult));
                goal.reason=c.getString(c.getColumnIndex(TableGoal.reason));
                goal.reward=c.getString(c.getColumnIndex(TableGoal.buddyEmail));
                goal.status=(byte)c.getInt(c.getColumnIndex(TableGoal.status));
                goal.dueDate=CalendarUtils.parseDate(c.getString(c.getColumnIndex(TableGoal.dueDate)));
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
        values.put(TableGoal.stringId,this.stringId);
        values.put(TableGoal.nickName,this.nickName);
        values.put(TableGoal.objective,this.objective);
        values.put(TableGoal.keyResult,this.keyResult);
        values.put(TableGoal.reason,this.reason);
        values.put(TableGoal.reward,this.reward);
        values.put(TableGoal.buddyEmail,this.buddyEmail);
        values.put(TableGoal.status,this.status);
        values.put(TableGoal.order, this.order);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.SECOND, 0);cal.set(Calendar.MINUTE, 0);cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.setTime(this.getDueDate());
        values.put(TableGoal.dueDate, CalendarUtils.getSqLiteDateFormat(cal));
        values.put(TableGoal.timeBoxId, this.timeBoxId);
        long rowId;
        if (this.id!=0){
            values.put(TableGoal.id,this.id);
        }
        rowId=db.insertWithOnConflict(TableGoal.goal, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        this.id=rowId;
        return rowId;
    }

    public int delete(){
        SQLiteDatabase db=database.getWritableDatabase();
        int numOfRowAffected=db.delete(TableGoal.goal,TableGoal.id+"="+id,null);
        return numOfRowAffected;
    }

    /**
     * This method computes the completion of goal in terms of the percent
     * @return percentage of goal completion
     */
    public float getGoalProgress(){
        int totalStepsCompleted=0;
        int totalStepsUnCompleted=0;
        SQLiteDatabase db=database.getReadableDatabase();
        //this query returns sum of time of all steps that are present in the Complpeted Step table
        String sumOfTimeComplete=" select count(*) as totalStepsCompleted  " +
                " "+" from "+TablePendingStep.pendingStep+" " +
                " "+" where ("+TablePendingStep.type+"= "+ PendingStep.PendingStepType.SINGLE_STEP+" or " +
                " "+TablePendingStep.type+"= "+ PendingStep.PendingStepType.SUB_STEP.ordinal()+" ) and " +
                " " +TablePendingStep.status+" = "+PendingStep.PendingStepStatus.COMPLETED.ordinal();
        Cursor c=db.rawQuery(sumOfTimeComplete,null);
        if(c.moveToFirst()){
            do{
                totalStepsCompleted=c.getInt(c.getColumnIndex("sumTime"));
            }while(c.moveToNext());
        }
        c.close();
        String sumOfTimePending=" select count(*) as totalStepsUnCompleted  " +
                " "+" from "+TablePendingStep.pendingStep+" " +
                " "+" where ("+TablePendingStep.type+"= "+ PendingStep.PendingStepType.SINGLE_STEP+" or " +
                " "+TablePendingStep.type+"= "+ PendingStep.PendingStepType.SUB_STEP.ordinal()+" ) and (" +
                " " +TablePendingStep.status+" = "+PendingStep.PendingStepStatus.TODO.ordinal()+ " )";
         c=db.rawQuery(sumOfTimePending,null);
        if(c.moveToFirst()){
            do{
                totalStepsUnCompleted=c.getInt(c.getColumnIndex("sumTime"));
            }while(c.moveToNext());
        }
        c.close();

        float percentage=(totalStepsCompleted/(totalStepsCompleted+totalStepsUnCompleted))*100;
        return percentage;
    }

    public int getStepCount(){
        int stepCount=0;
        String query="select count(*) as stepCount" +
                " "+" from "+TablePendingStep.pendingStep+" " +
                " "+" where "+TablePendingStep.goalId+" = "+this.id+" " +
                " "+" and "+TablePendingStep.type+"!="+ PendingStep.PendingStepType.SUB_STEP.ordinal();
        SQLiteDatabase db=database.getReadableDatabase();
        Cursor c=db.rawQuery(query,null);
        c.moveToFirst();
        stepCount=c.getInt(c.getColumnIndex("stepCount"));
        return stepCount;
    }

    public long getGoalId(long timeBoxId){
        long goalId=0;
        String query="select id " +
                " "+" from "+TableGoal.goal+" " +
                " "+" where "+TableGoal.timeBoxId+" = "+timeBoxId;
        SQLiteDatabase db=database.getReadableDatabase();
        Cursor c=db.rawQuery(query,null);
        if(c.moveToFirst()){
            goalId=c.getLong(c.getColumnIndex(TableGoal.id));
        }
        return goalId;
    }
    public String getColorCode(){
        String colorCode="";
        String query="select colorCode " +
                " "+" from "+ MetaData.TableTimeBox.timeBox+" " +
                " "+" where "+ MetaData.TableTimeBox.id+" = "+this.timeBoxId;
        SQLiteDatabase db=database.getReadableDatabase();
        Cursor c=db.rawQuery(query,null);
        if(c.moveToFirst()){
            colorCode=c.getString(c.getColumnIndex(MetaData.TableTimeBox.colorCode));
        }
        return colorCode;
    }
}
