package com.greylabs.yoda.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.greylabs.yoda.R;
import com.greylabs.yoda.models.TimeBox;
import com.greylabs.yoda.views.CircleView;

import java.util.List;

public class AdapterTimeBoxSpinner extends BaseAdapter {

    Context context;
    List<TimeBox> timeBoxArrayList;

    public AdapterTimeBoxSpinner(Context context, List<TimeBox> timeBoxArrayList) {
        this.context = context;
        this.timeBoxArrayList = timeBoxArrayList;
    }

    @Override
    public int getCount() {
        return timeBoxArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        TextView tvTimeBoxName;
        CircleView circleView;

        if( convertView == null ) {
            row = ((Activity)context).getLayoutInflater().inflate(R.layout.timebox_spinner_item, parent, false);
            tvTimeBoxName = (TextView) row.findViewById(R.id.tvTimeBoxNameTimeBoxSpinnerItemActAddNewGoal);
            circleView = (CircleView) row.findViewById(R.id.btnBulletTimeBoxSpinnerItemActAddNewGoal);
        }
        else {
            row = convertView;
            tvTimeBoxName = (TextView) row.findViewById(R.id.tvTimeBoxNameTimeBoxSpinnerItemActAddNewGoal);
            circleView = (CircleView) row.findViewById(R.id.btnBulletTimeBoxSpinnerItemActAddNewGoal);
        }
        tvTimeBoxName.setText(timeBoxArrayList.get(position).getNickName());
        circleView.setFillColor(context.getResources().getColor(R.color.ColorPrimary));
        circleView.setShowTitle(false);
        return row;
    }
}