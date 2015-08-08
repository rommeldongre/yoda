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

import java.util.List;
import java.util.Map;

public class AdapterExpandableList extends BaseExpandableListAdapter {

    private Context context;
    private List<Goal> _listDataHeader; // header titles         string
    // child data in format of header title, child title         string string
    private Map<Long, List<PendingStep>> _listDataChild;

    public AdapterExpandableList(Context context, List<Goal> listDataHeader,
                                 Map<Long, List<PendingStep>> listChildData) {
        this.context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
//        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
//                .get(childPosititon);
        Goal goal=this._listDataHeader.get(groupPosition);
        return this._listDataChild.get(new Long(goal.getId()));
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
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
        tvETAOfStep.setText(pendingStep.getStepDate().toString());

        if(pendingStep.getPendingStepStatus().equals(PendingStep.PendingStepStatus.COMPLETED)){
            cbCompleted.setChecked(true);
            cbCompleted.setEnabled(false);
        }

        cbCompleted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.COMPLETED);
                    pendingStep.cancelAlarm();
                    pendingStep.save();
                    cbCompleted.setEnabled(false);
                }
            }
        });
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        Goal goal=this._listDataHeader.get(groupPosition);
        return this._listDataChild.get(new Long(goal.getId()))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
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
