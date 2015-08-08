package com.greylabs.yoda.database;
import com.greylabs.yoda.models.PendingStep;

/**
 * Created by Jaybhay Vijay on 6/30/2015.
 */
public class MetaData {

    public static class TableGoal{
        public static final String goal="goal";//table name
        public static final String id="id";//pk
        public static final String stringId="stringId";
        public static final String nickName="nickName";
        public static final String objective="objective";
        public static final String keyResult="keyResult";
        public static final String reason="reason";
        public static final String reward="reward";
        public static final String buddyEmail="buddyEmail";
        public static final String status="status";
        public static final String order="orderValue";
        public static final String dueDate="dueDate";
        public static final String timeBoxId="timeBoxId";//FK to timebox

        public static final String createGoalTable=
                "create table if not exists  "+goal+" (" +
                        " "+id+" integer primary key autoincrement, " +
                        " "+stringId+" text ,"+
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
//        static Prefs prefs=Prefs.getInstance(Yoda.getContext());
//
//        public static final String createTrigger="" +
//                "create trigger updateStepsOnGoalDelete before delete on "+goal+" " +
//                "begin" +
//                "    update  "+TablePendingStep.pendingStep+" " +
//                "    set " +TablePendingStep.goalId+" = "+prefs.getStretchGoalId()+" "+
//                "    where "+"old."+id+"="+ TablePendingStep.pendingStep+"."+TablePendingStep.goalId+"" +
//                " end ; ";
    }

    public static class TablePendingStep{
        public static final String pendingStep="pendingStep";//table name
        public static final String id="id";
        public static final String stringId="stringId";
        public static final String nickName="nickName";
        public static final String priority="priority";
        public static final String time="time";
        public static final String type="type";
        public static final String stepCount="stepCount";
        public static final String status="status";
        public static final String skipCount="skipCount";
        public static final String stepDate="stepDate";//date meaning is different according to its status
        public static final String goalId="goalId";//FK to Goal
        public static final String goalStringId="goalStringId";//used for google task
        public static  final String slotId="slotId";
        public static final String subStepOf="subStepOf";
        public static final String createPendingStepTable="" +
                "create table if not exists  "+pendingStep+" ( " +
                " "+id+" integer primary key autoincrement," +
                " "+stringId+" text , "+
                " "+nickName+" text, " +
                " "+priority+" integer, " +
                " "+time+" integer, " +
                " "+type+" integer, " +
                " "+stepCount+" integer, " +
                " "+skipCount+" integer ," +
                " "+status+" integer ," +
                " "+stepDate+" text ,"+
                " "+goalStringId+" text ,"+
                " "+goalId+" integer, " +
                " "+slotId+" integer, " +
                " "+subStepOf+" integer, " +
                " "+"foreign key("+goalId+") references "+ TableGoal.goal+"("+ TableGoal.id+") ," +
                " "+"foreign key("+slotId+") references "+ TableSlot.slot+"("+ TableSlot.id+") , " +
                " "+"foreign key("+subStepOf+") references "+ TablePendingStep.pendingStep+"("+ TablePendingStep.id+") " +
                " )";

        public static final String createTriggerBeforeDelete="" +
                "create trigger if not exists  updateSlotOnPendingStepDelete  before delete on "+TableDay.day+" " +
                "begin" +
                "    delete from  "+TableSlot.slot+" " +
                "    where "+TableSlot.slot+"."+TableSlot.dayId+"="+"old."+TableDay.id +" ;" +
                " end;  ";

    }

    public static class TableCompletedStep{
        public static final String completedStep="completedStep";//table name
        public static final String id="id";//PK id of completed step
        public static final String series="series";
        public static final String stepCount="stepCount";

        public static final String createCompletedStepTable="" +
                "create table if not exists  "+completedStep+" ( " +
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
        public static final String on="onType";
        public static final String till="tillType";
        public static final String colorCode="colorCode";

        public static final String createTimeBoxTable="" +
                "create table  if not exists  "+timeBox+" ( " +
                " "+id+" integer primary key autoincrement, " +
                " "+nickName+" text ," +
                " "+on+" integer ," +
                " "+till+" integer ," +
                " "+colorCode+" text "+
                ")";


    }

    public static class TableTimeBoxOn{
        public static final String timeBoxOn="timeBoxOn";//table name
        public static final String id="id";//FK to TimeBox
        public static final String on="onType";


        public static final String createTimeBoxOnTable="" +
                "create table  if not exists  "+timeBoxOn+" ( " +
                " "+id+" integer , " +
                " "+on+" integer ," +
                " "+"foreign key("+id+") references "+ TableTimeBox.timeBox+"("+ TableTimeBox.id+") " +
                ")";
    }

    public static class TableTimeBoxWhen{
        public static final String timeBoxWhen="timeBoxWhen";//table name
        public static final String id="id";//FK to TimeBox
        public static final String when="whenType";

        public static final String createTimeBoxWhenTable="" +
                "create table  if not exists  "+timeBoxWhen+" ( " +
                " "+id+" integer , " +
                " "+when+" integer ," +
                " "+"foreign key("+id+") references "+ TableTimeBox.timeBox+"("+ TableTimeBox.id+") " +
                ")";
    }

    public static class TableDay{
        public static final String day="day";//table name
        public static  final String triggerDeleteSlotOnDayDelete="deleteSlotOnDayDelete";
        public static final String id="id";
        public static final String date="date";
        public static final String dayOfYear="dayOfYear";
        public static final String dayOfWeek="dayOfWeek";
        public static final String weekOfMonth="dayOfMonth";
        public static final String monthOfYear="monthOfYear";
        public static final String quarterOfYear="quarterOfYear";
        public static final String year="year";

        public static final String createDayTable=" " +
                " create table if not exists "+day+" ( " +
                " "+id+" integer primary key autoincrement, " +
                " "+date+" text , " +
                " "+dayOfYear+" integer , " +
                " "+dayOfWeek+" integer , " +
                " "+weekOfMonth+" integer , " +
                " "+monthOfYear+" integer , " +
                " "+quarterOfYear+" integer , " +
                " "+year+" integer " +
                " ) ";

        public static final String createTrigger="" +
                "create trigger if not exists  "+triggerDeleteSlotOnDayDelete+"  before delete on "+TableDay.day+" " +
                "begin" +
                "    delete from  "+TableSlot.slot+" " +
                "    where "+TableSlot.slot+"."+TableSlot.dayId+"="+"old."+TableDay.id +" ;" +
                " end;  ";
    }
    public static class TableSlot{
        public static final String slot="slot";//table name
        public static final String triggerUpdatePendingStepOnSlotDelete="updatePendingStepOnSlotDelete";
        public static final String id="id";
        public static final String when="whenType";
        public static final String scheduleDate="scheduleDate";
        public static final String time="maxTime";
        public static final String goalId="goalId";
        public static final String timeBoxId="timeBoxId";
        public static final String dayId="dayId";

        public static final String createSlotTable=" " +
                " create table if not exists  "+slot+" ( " +
                " "+id+" integer primary key autoincrement , " +
                " "+when+" integer ," +
                " "+scheduleDate+" text ," +
                " "+time+" integer , " +
                " "+goalId+" integer , " +
                " "+timeBoxId+" integer , " +
                " "+dayId+" integer , " +
                " "+"foreign key("+dayId+") references "+ TableDay.day+"("+ TableDay.id+") " +
                " )";

        public static final String createTrigger="" +
                "create trigger if not exists  "+ triggerUpdatePendingStepOnSlotDelete+ " before delete on "+TableSlot.slot+" " +
                "begin" +
                "    update  "+TablePendingStep.pendingStep+" set " +TablePendingStep.slotId+" = 0 , " +
                "     "+TablePendingStep.status+" = "+ PendingStep.PendingStepStatus.MISSED+"  "+
                "    where "+"old."+TableSlot.id+"="+TablePendingStep.pendingStep+"."+TablePendingStep.slotId+" ;" +
                " end;  ";
    }
}
