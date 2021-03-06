package com.greylabs.ydo.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.greylabs.ydo.R;
import com.greylabs.ydo.models.TimeBox;
import com.greylabs.ydo.views.CircleView;

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
            row = ((Activity)context).getLayoutInflater().inflate(R.layout.spinner_item_timebox, parent, false);
            tvTimeBoxName = (TextView) row.findViewById(R.id.tvTimeBoxNameTimeBoxSpinnerItemActAddNewGoal);
            circleView = (CircleView) row.findViewById(R.id.btnBulletTimeBoxSpinnerItemActAddNewGoal);
        }
        else {
            row = convertView;
            tvTimeBoxName = (TextView) row.findViewById(R.id.tvTimeBoxNameTimeBoxSpinnerItemActAddNewGoal);
            circleView = (CircleView) row.findViewById(R.id.btnBulletTimeBoxSpinnerItemActAddNewGoal);
        }
        tvTimeBoxName.setText(timeBoxArrayList.get(position).getNickName());
        circleView.setFillColor(Integer.parseInt(timeBoxArrayList.get(position).getColorCode()));
        circleView.setShowTitle(false);
        return row;
    }
}