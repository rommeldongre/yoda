package com.greylabs.ydo.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
import android.widget.TextView;

import com.greylabs.ydo.R;
import com.greylabs.ydo.models.Goal;
import com.greylabs.ydo.views.CircleView;

import java.util.List;

public class AdapterGoalSpinner extends BaseAdapter {

    Context context;
    List<Goal> goalsArrayList;

    public AdapterGoalSpinner(Context context, List<Goal> goalsArrayList) {
        this.context = context;
        this.goalsArrayList = goalsArrayList;
    }

    @Override
    public int getCount() {
        return goalsArrayList.size();
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
        TextView tvGoalName;
        CircleView circleView;

        if( convertView == null ) {
            row = ((Activity)context).getLayoutInflater().inflate(R.layout.spinner_item_goal, parent, false);
            tvGoalName = (TextView) row.findViewById(R.id.tvGoalNameSpinnerItemActAddNewStep);
            circleView = (CircleView) row.findViewById(R.id.btnBulletSpinnerItemActAddNewStep);
        }
        else {
            row = convertView;
            tvGoalName = (TextView) row.findViewById(R.id.tvGoalNameSpinnerItemActAddNewStep);
            circleView = (CircleView) row.findViewById(R.id.btnBulletSpinnerItemActAddNewStep);
        }
        tvGoalName.setText(goalsArrayList.get(position).getNickName());
        if(!goalsArrayList.get(position).getColorCode().isEmpty()){
            circleView.setFillColor(Integer.parseInt(goalsArrayList.get(position).getColorCode()));
        }else{
            circleView.setFillColor(context.getResources().getColor(R.color.ColorPrimary));
        }
        circleView.setShowTitle(false);
        return row;
    }
}