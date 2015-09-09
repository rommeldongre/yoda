package com.greylabs.yoda.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.greylabs.yoda.R;
import com.greylabs.yoda.interfaces.OnCheckExpandableList;
import com.greylabs.yoda.models.Goal;
import com.greylabs.yoda.models.PendingStep;
import com.greylabs.yoda.models.TimeBox;
import com.greylabs.yoda.scheduler.YodaCalendar;
import com.greylabs.yoda.utils.CalendarUtils;
import com.greylabs.yoda.views.TouchCheckBox;

import java.util.List;
import java.util.Map;

public class AdapterExpandableList extends BaseExpandableListAdapter {

    private Context context;
    private List<Goal> goalList;
    private Map<Long, List<PendingStep>> goalIdPendingStepMap;
    private OnCheckExpandableList onCheckExpandableList;
    public AdapterExpandableList(Context context, List<Goal> listDataHeader,
                                 Map<Long, List<PendingStep>> listChildData) {
        this.context = context;
        this.goalList = listDataHeader;
        this.goalIdPendingStepMap = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        Goal goal=this.goalList.get(groupPosition);
        return this.goalIdPendingStepMap.get(new Long(goal.getId()));
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final List<PendingStep> ps= (List<PendingStep>) getChild(groupPosition, childPosition);
        final PendingStep pendingStep=ps.get(childPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);
        TextView tvETAOfStep = (TextView) convertView
                .findViewById(R.id.tvETAOfStepListItemActFilters);
        final TouchCheckBox cbCompleted = (TouchCheckBox) convertView
                .findViewById(R.id.cbListItemActFilters);

        txtListChild.setText(pendingStep.getNickName());
        tvETAOfStep.setText(CalendarUtils.getFormattedDateWithSlot(pendingStep.getStepDate()));
        cbCompleted.invalidate();
        cbCompleted.setCircleColor(Integer.valueOf(pendingStep.getColorCode()));

        if(pendingStep.getPendingStepStatus().equals(PendingStep.PendingStepStatus.COMPLETED)){
            cbCompleted.setChecked(true);
        }else {
            cbCompleted.setChecked(false);
        }

        cbCompleted.setOnCheckedChangeListener(new TouchCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View buttonView, boolean isChecked) {
                if(buttonView.isPressed() && isChecked){
                    pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.COMPLETED);
                    pendingStep.cancelAlarm();
                    pendingStep.freeSlot();
                    pendingStep.save();
                    if(pendingStep.getPendingStepType()== PendingStep.PendingStepType.SPLIT_STEP ||
                            pendingStep.getPendingStepType()== PendingStep.PendingStepType.SERIES_STEP){
                        pendingStep.updateSubSteps();
                        pendingStep.freeSlots();
                    }
                    ps.remove(childPosition);
                    if(ps.size()==0){
                        Goal removedGoal = goalList.remove(groupPosition);
                        try {
                            onCheckExpandableList = (OnCheckExpandableList) context;
                        } catch (ClassCastException e) {
                            throw new ClassCastException(context.toString()
                                    + " must implement OnCheckExpandableList");
                        }
                        onCheckExpandableList.onCheckExpandableList(groupPosition, childPosition, removedGoal);
                    }
                    if(!goalList.isEmpty()) {
                        Goal currentGoal = goalList.get(groupPosition);
                        TimeBox currentTimeBox = new TimeBox(context).get(currentGoal.getTimeBoxId());
                        YodaCalendar yodaCalendar = new YodaCalendar(context, currentTimeBox);
                        yodaCalendar.rescheduleSteps(currentGoal.getId());
                    }
                    AdapterExpandableList.this.notifyDataSetChanged();
                }
//                else {
//                    pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.TODO);
//                    pendingStep.save();
//                    if(pendingStep.getPendingStepType()== PendingStep.PendingStepType.SPLIT_STEP ||
//                            pendingStep.getPendingStepType()== PendingStep.PendingStepType.SERIES_STEP){
//                        pendingStep.updateSubSteps();
//                    }
//                    Goal currentGoal = goalList.get(groupPosition);
//                    TimeBox currentTimeBox = new TimeBox(context).get(currentGoal.getTimeBoxId());
//                    YodaCalendar yodaCalendar = new YodaCalendar(context, currentTimeBox);
//                    yodaCalendar.rescheduleSteps(currentGoal.getId());
//                }
            }
        });
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        Goal goal=this.goalList.get(groupPosition);
        return this.goalIdPendingStepMap.get(new Long(goal.getId()))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.goalList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.goalList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String goalNickname = ((Goal) getGroup(groupPosition)).getNickName();
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setText(goalNickname);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
