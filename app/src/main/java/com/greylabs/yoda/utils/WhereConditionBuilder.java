package com.greylabs.yoda.utils;
import com.greylabs.yoda.database.MetaData;
import com.greylabs.yoda.database.MetaData.TableDay;
import com.greylabs.yoda.enums.Month;
import com.greylabs.yoda.enums.Quarter;
import com.greylabs.yoda.enums.SubValue;
import com.greylabs.yoda.enums.TimeBoxTill;
import com.greylabs.yoda.enums.WeekDay;
import com.greylabs.yoda.enums.Year;
import com.greylabs.yoda.models.TimeBox;
import com.greylabs.yoda.models.TimeBoxOn;
import com.greylabs.yoda.models.TimeBoxWhen;
import java.util.Calendar;
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
        if(timeBox.getNickName().equals(Constants.NICKNAME_UNPLANNED_TIMEBOX))
            return whereClause;
        if(timeBox.getTillType()!=TimeBoxTill.FOREVER) {
            if(timeBox.getTimeBoxOn().getOnType()!= com.greylabs.yoda.enums.TimeBoxOn.DAILY) {
                whereClause += "where " + buildWhen(timeBox.getTimeBoxWhen()) + " and " +
                        " " + buildOn(timeBox.getTimeBoxOn()) + " and " + buildTill(timeBox.getTillType(), timeBox.getTimeBoxOn());
            }else{
                whereClause += "where " + buildWhen(timeBox.getTimeBoxWhen()) +
                         " and " + buildTill(timeBox.getTillType(), timeBox.getTimeBoxOn());
            }
        }else{
            if(timeBox.getTimeBoxOn().getOnType()!= com.greylabs.yoda.enums.TimeBoxOn.DAILY) {
                whereClause += "where " + buildWhen(timeBox.getTimeBoxWhen()) + " and " +
                        " " + buildOn(timeBox.getTimeBoxOn());
            }else{
                whereClause += "where " + buildWhen(timeBox.getTimeBoxWhen()) ;
            }
        }
        return whereClause;

    }
    private static String buildWhen(TimeBoxWhen timeBoxWhen){
        String strWhen="(";
        Set<com.greylabs.yoda.enums.TimeBoxWhen> whens=timeBoxWhen.getWhenValues();
        for(com.greylabs.yoda.enums.TimeBoxWhen when:whens){
            strWhen+= MetaData.TableSlot.when+"="+when.getValue()+" or ";
        }
        strWhen=strWhen.substring(0,strWhen.lastIndexOf("or"));
        strWhen=strWhen+")";

        return strWhen;
    }
    private static String buildTill(TimeBoxTill timeBoxTill,TimeBoxOn timeBoxOn){
        String strTill="";
        Calendar calendar=Calendar.getInstance();
        if(timeBoxTill.getValue()!=TimeBoxTill.FOREVER.getValue())
        {
            switch (timeBoxTill){
                case WEEK:
                    strTill = "( "+TableDay.weekOfMonth+"="+CalendarUtils.getWeek(calendar.get(Calendar.DAY_OF_MONTH))+" " +
                                 " and " +TableDay.monthOfYear+"="+calendar.get(Calendar.MONTH)+" " +
                                 " and "+TableDay.year+"="+calendar.get(Calendar.YEAR)+" "+
                               " )";
                    break;
                case MONTH:
                    strTill = "( "+TableDay.monthOfYear+"="+calendar.get(Calendar.MONTH)+" " +
                            " and " +TableDay.year+"="+calendar.get(Calendar.YEAR)+" "+
                            ")";
                    break;
                case QUARTER:
                    int lastMonth=CalendarUtils.getLastMonthOfQuarter(calendar.get(Calendar.MONTH));
                    if(lastMonth!=0) {
                        strTill = "( ";
                        for (int i = calendar.get(Calendar.MONTH); i <= lastMonth; i++) {
                            strTill+= TableDay.monthOfYear+"="+calendar.get(Calendar.MONTH)+" or ";
                            calendar.add(Calendar.MONTH,1);
                        }
                        strTill=strTill.substring(0,strTill.lastIndexOf("or"));
                        strTill+=" )";
                        calendar.add(Calendar.MONTH,-1);
                        strTill="("+strTill+" and "+TableDay.year+"="+calendar.get(Calendar.YEAR)+" )";
                    }
                    break;
                case YEAR:
                    strTill="( "+calendar.get(Calendar.YEAR)+" )";
                    break;
            }

        }
        return strTill;
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
