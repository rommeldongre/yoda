package com.greylabs.yoda.utils.sorters;

import android.content.Context;

import com.greylabs.yoda.models.Day;
import com.greylabs.yoda.models.Slot;

import java.util.Comparator;

/**
 * Created by Jaybhay Vijay on 7/18/2015.
 */
public class SortByDate implements Comparator<Slot> {

    @Override
    public int compare(Slot slot1, Slot slot2) {
        if(slot1.getScheduleDate().compareTo(slot2.getScheduleDate())>0)
            return 1;
        else if(slot1.getScheduleDate().compareTo(slot2.getScheduleDate())<0)
            return -1;
        return 0;
    }
}
