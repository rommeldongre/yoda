package com.greylabs.yoda.database;

import com.greylabs.yoda.models.CompletedStep;
import com.greylabs.yoda.models.PendingStep;
import com.greylabs.yoda.models.TimeBox;

/**
 * Created by Jaybhay Vijay on 6/30/2015.
 */
public class MetaData {

    public static class TableGoal{
        public static final String goal="goal";//table name
        public static final String id="id";//pk
        public static final String nickName="nickName";
        public static final String objective="objective";
        public static final String keyResult="keyResult";
        public static final String reason="reason";
        public static final String reward="reward";
        public static final String buddyEmail="buddyEmail";
        public static final String status="status";
        public static final String order="order";
        public static final String dueDate="dueDate";
        public static final String timeBoxId="timeBoxId";//FK to timebox

        public static final String createGoalTable=
                "create table "+goal+" (" +
                        " "+id+" integer primary key autoincrement, " +
                        " "+nickName+" text, " +
                        " "+objective+" text, " +
                        " "+keyResult+" text, " +
                        " "+reason+" text, " +
                        " "+reward+" text, " +
                        " "+buddyEmail+" text, " +
                        " "+status+" integer, " +
                        " "+order+" integer, " +
                        " "+dueDate+" text, " +
                        " "+timeBoxId+" text, " +
                        " "+"foreign key("+timeBoxId+") references "+ TableTimeBox.timeBox+"("+ TableTimeBox.id+") " +
                        ")";
//        db.execSQL("CREATE TRIGGER delete_days_with track BEFORE DELETE ON track "
//                +  "FOR EACH ROW BEGIN"
//                +         " DELETE FROM days WHERE track.day_id = days.day_id "
//                +  "END;");
        //create Trigger to delete Steps when Goal is deleted.
        //this is same effect as Cascade on delete. This feature is not supported
        // below api level 16. To support all api levels we use Triggers

        public static final String createTrigger="" +
                "create trigger deleteStepsOnGoalDelete " +goal+" before delete on "+goal+" " +
                "for each row " +
                "begin" +
                "    delete from  "+TablePendingStep.pendingStep+" " +
                "    where "+goal+"."+id+"="+ TablePendingStep.pendingStep+"."+TablePendingStep.goalId+"" +
                "end  ";
    }

    public static class TablePendingStep{
        public static final String pendingStep="pendingStep";//table name
        public static final String id="id";
        public static final String nickName="nickName";
        public static final String priority="priority";
        public static final String time="time";
        public static final String series="series";
        public static final String stepCount="stepCount";
        public static final String skipCount="skipCount";
        public static final String goalId="goalId";//FK to Goal

        public static final String createPendingStepTable="" +
                "create table "+pendingStep+" ( " +
                " "+id+" integer primary key autoincrement," +
                " "+nickName+" text, " +
                " "+priority+" integer, " +
                " "+time+" integer, " +
                " "+series+" integer, " +
                " "+stepCount+" integer, " +
                " "+skipCount+" integer ," +
                " "+goalId+" integer, " +
                " "+"foreign key("+goalId+") references "+ TableGoal.goal+"("+ TableGoal.id+") " +
                " )";

    }

    public static class TableCompletedStep{
        public static final String completedStep="completedStep";//table name
        public static final String id="id";//PK id of completed step
        public static final String series="series";
        public static final String stepCount="stepCount";

        public static final String createCompletedStepTable="" +
                "create table "+completedStep+" ( " +
                " "+id+" integer ," +
                " "+series+" integer, " +
                " "+stepCount+" integer, " +
                " "+"foreign key("+id+") references "+ TableCompletedStep.id+"("+ TableCompletedStep.id+") " +
                ")";
    }

    public static class TableTimeBox{
        public static final String timeBox="timeBox";//table name
        public static final String id="id";
        public static final String nickName="nickName";
        public static final String on="on";
        public static final String till="till";

        public static final String createTimeBoxTable="" +
                "create table  "+timeBox+" ( " +
                " "+id+" integer primary key autoincrement, " +
                " "+nickName+" text ," +
                " "+on+" integer ," +
                " "+till+" integer " +
                ")";


    }

    public static class TableTimeBoxOn{
        public static final String timeBoxOn="timeBoxOn";//table name
        public static final String id="id";//FK to TimeBox
        public static final String on="on";


        public static final String createTimeBoxOnTable="" +
                "create table  "+timeBoxOn+" ( " +
                " "+id+" integer primary key autoincrement, " +
                " "+on+" integer ," +
                " "+"foreign key("+id+") references "+ TableTimeBox.timeBox+"("+ TableTimeBox.id+") " +
                ")";
    }

    public static class TableTimeBoxWhen{
        public static final String timeBoxWhen="timeBoxWhen";//table name
        public static final String id="id";//FK to TimeBox
        public static final String when="when";

        public static final String createTimeBoxWhenTable="" +
                "create table  "+timeBoxWhen+" ( " +
                " "+id+" integer primary key autoincrement, " +
                " "+when+" integer ," +
                " "+"foreign key("+id+") references "+ TableTimeBox.timeBox+"("+ TableTimeBox.id+") " +
                ")";
    }
}
