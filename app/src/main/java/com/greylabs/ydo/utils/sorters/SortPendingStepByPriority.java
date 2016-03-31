package com.greylabs.ydo.utils.sorters;

import com.greylabs.ydo.models.PendingStep;

import java.util.Comparator;

/**
 * Created by Jaybhay Vijay on 7/18/2015.
 */
public class SortPendingStepByPriority implements Comparator<PendingStep> {

    @Override
    public int compare(PendingStep pendingStep1, PendingStep pendingStep2) {
        return pendingStep1.getPriority()-pendingStep2.getPriority();
    }
}
