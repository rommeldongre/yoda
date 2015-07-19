package com.greylabs.yoda.utils;
import com.greylabs.yoda.database.MetaData;
import com.greylabs.yoda.database.MetaData.TableTimeBox;
import com.greylabs.yoda.database.MetaData.TableTimeBoxWhen;
import com.greylabs.yoda.enums.Month;
import com.greylabs.yoda.enums.Quarter;
import com.greylabs.yoda.enums.SubValue;
import com.greylabs.yoda.enums.TimeBoxTill;
import com.greylabs.yoda.enums.WeekDay;
import com.greylabs.yoda.enums.Year;
import com.greylabs.yoda.models.TimeBox;
import com.greylabs.yoda.models.TimeBoxOn;
import com.greylabs.yoda.models.TimeBoxWhen;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Jaybhay Vijay on 7/15/2015.
 */
public class WhereConditionBuilder {

    /**
     * This method build where condition based on timebox object
     * @return where string cossits of criteria for filtering slots of calendar and null when timebox
     * is FreeTime(Wasted)
     */
    public static String buildWhereCondition(TimeBox timeBox){
        String whereClause="";
        if(timeBox.getTimeBoxOn().getOnType()!= com.greylabs.yoda.enums.TimeBoxOn.DAILY
            || timeBox.getTillType()==TimeBoxTill.FOREVER) {
            whereClause+="where "+buildWhen(timeBox.getTimeBoxWhen())+" and " +
                    " "+buildOn(timeBox.getTimeBoxOn())+" and " +
                    " "+buildTill(timeBox.getTillType());
        }
        return whereClause;

    }
    private static String buildWhen(TimeBoxWhen timeBoxWhen){
        String strWhen="(";
        Set<com.greylabs.yoda.enums.TimeBoxWhen> whens=timeBoxWhen.getWhenValues();
        Iterator it=whens.iterator();
        while (it.hasNext()){
            com.greylabs.yoda.enums.TimeBoxWhen when=(com.greylabs.yoda.enums.TimeBoxWhen)it.next();
            strWhen+=TableTimeBoxWhen.timeBoxWhen+"="+when.getValue()+" or ";
        }
        strWhen=strWhen.substring(0,strWhen.lastIndexOf("or"));
        strWhen+=strWhen+")";

        return strWhen;
    }
    private static String buildTill(TimeBoxTill timeBoxTill){
        if(timeBoxTill.getValue()==TimeBoxTill.FOREVER.getValue())
            return "";
        else
            return "(" +TableTimeBox.till+" = "+timeBoxTill.getValue()+")";

    }
    private static String buildOn(TimeBoxOn timeBoxOn){
        String strOn="( ";
        Set<SubValue> ons=timeBoxOn.getSubValues();
        switch (timeBoxOn.getOnType()){
            case WEEKLY:
                for (SubValue subValue:ons){
                    WeekDay weekDay=(WeekDay)subValue;
                    strOn+= MetaData.TableDay.dayOfWeek+"="+weekDay.getValue()+" or ";
                }
                strOn=strOn.substring(0,strOn.lastIndexOf("or"));
                strOn+=")";
                break;
            case MONTHLY:
                for (SubValue subValue:ons){
                    Month month=(Month)subValue;
                    strOn+= MetaData.TableDay.weekOfMonth+"="+month.getValue()+" or ";
                }
                strOn=strOn.substring(0,strOn.lastIndexOf("or"));
                strOn+=")";
                break;
            case QUATERLY:
                for (SubValue subValue:ons){
                    Quarter quarter=(Quarter)subValue;
                    strOn+= MetaData.TableDay.quarterOfYear+"="+quarter.getValue()+" or ";
                }
                strOn=strOn.substring(0,strOn.lastIndexOf("or"));
                strOn+=")";
                break;
            case YEARLY:
                for (SubValue subValue:ons){
                    Year year=(Year)subValue;
                    strOn+= MetaData.TableDay.monthOfYear+"="+year.getValue()+" or ";
                }
                strOn=strOn.substring(0,strOn.lastIndexOf("or"));
                strOn+=")";
                break;
        }
        return strOn;
    }
}
