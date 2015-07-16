package com.greylabs.yoda.utils;
import com.greylabs.yoda.database.MetaData.TableTimeBox;
import com.greylabs.yoda.database.MetaData.TableTimeBoxWhen;
import com.greylabs.yoda.enums.TimeBoxTill;
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
     * This method build where condtion based on timebox object
     * @return where string cossits of criteria for filtering slots of calendar and null when timebox
     * is FreeTime(Wasted)
     */

    public static String buildWhereCondition(TimeBox timeBox){
        return null;

    }
    private static String buildWhen(TimeBoxWhen timeBoxWhen){
        String strWhen="(";
        Set<com.greylabs.yoda.enums.TimeBoxWhen> whens=timeBoxWhen.getWhenValues();
        Iterator it=whens.iterator();
        while (it.hasNext()){
            com.greylabs.yoda.enums.TimeBoxWhen when=(com.greylabs.yoda.enums.TimeBoxWhen)it.next();
            strWhen+=TableTimeBoxWhen.timeBoxWhen+"="+when.getValue()+" or ";
        }
        strWhen+=strWhen.substring(0,strWhen.lastIndexOf("or"));
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
        String strOn="(";
//        switch (timeBoxOn.getOnType()){
//            case TimeBoxOn
//        }
        return strOn;
    }
}
