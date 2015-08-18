package com.greylabs.yoda.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.greylabs.yoda.R;
import com.greylabs.yoda.models.Goal;
import com.greylabs.yoda.models.PendingStep;
import com.greylabs.yoda.models.TimeBox;
import com.greylabs.yoda.scheduler.YodaCalendar;
import com.greylabs.yoda.utils.CalendarUtils;

import java.util.List;
import java.util.Map;

public class AdapterExpandableList extends BaseExpandableListAdapter {

    private Context context;
    private List<Goal> goalList; // header titles         string
    // child data in format of header title, child title         string string
    private Map<Long, List<PendingStep>> _listDataChild;

    public AdapterExpandableList(Context context, List<Goal> listDataHeader,
                                 Map<Long, List<PendingStep>> listChildData) {
        this.context = context;
        this.goalList = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
//        return this._listDataChild.get(this.goalList.get(groupPosition))
//                .get(childPosititon);
        Goal goal=this.goalList.get(groupPosition);
        return this._listDataChild.get(new Long(goal.getId()));
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

       // final String childText = ((PendingStep) getChild(groupPosition, childPosition)).getNickName();
        List<PendingStep> ps= (List<PendingStep>) getChild(groupPosition, childPosition);
        final PendingStep pendingStep=ps.get(childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);
        TextView tvETAOfStep = (TextView) convertView
                .findViewById(R.id.tvETAOfStepListItemActFilters);
        final CheckBox cbCompleted = (CheckBox) convertView
                .findViewById(R.id.cbListItemActFilters);

        txtListChild.setText(pendingStep.getNickName());
        tvETAOfStep.setText(CalendarUtils.getFormattedDateWithSlot(pendingStep.getStepDate()));

        if(pendingStep.getPendingStepStatus().equals(PendingStep.PendingStepStatus.COMPLETED)){
            cbCompleted.setChecked(true);
//            cbCompleted.setEnabled(false);
        }

        cbCompleted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.COMPLETED);
                    pendingStep.cancelAlarm();
                    pendingStep.save();
//                    cbCompleted.setEnabled(false);
                }else {
                    pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.TODO);
                    pendingStep.save();
                    Goal currentGoal = goalList.get(groupPosition);
                    TimeBox currentTimeBox = new TimeBox(context).get(currentGoal.getTimeBoxId());
                    YodaCalendar yodaCalendar = new YodaCalendar(context, currentTimeBox);
                    yodaCalendar.rescheduleSteps(currentGoal.getId());
                }
            }
        });
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        Goal goal=this.goalList.get(groupPosition);
        return this._listDataChild.get(new Long(goal.getId()))
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
//        lblListHeader.setTypeface(null, Typeface.BOLD);
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
