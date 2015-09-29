package com.greylabs.yoda.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.greylabs.yoda.R;
import com.greylabs.yoda.interfaces.OnClickOfRecyclerViewFragFiltrNew;
import com.greylabs.yoda.models.PendingStep;
import com.greylabs.yoda.utils.CalendarUtils;
import com.greylabs.yoda.utils.Constants;
import com.greylabs.yoda.utils.Logger;
import com.greylabs.yoda.views.TouchCheckBox;

import java.util.ArrayList;

public class AdapterRecyclerViewFragFilterNew extends RecyclerView.Adapter<AdapterRecyclerViewFragFilterNew.ViewHolder> {

    static ArrayList<PendingStep> stepsArrayList;
    Context context;

    public AdapterRecyclerViewFragFilterNew(Context passedContext, ArrayList<PendingStep> stepsArrayList) {
        this.context = passedContext;
        this.stepsArrayList = stepsArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v;
        ViewHolder vhItem;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_frag_filter_new, parent, false);
        vhItem = new ViewHolder(v, viewType, context);
        return vhItem;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.checkBox.setCircleColor(Integer.valueOf(stepsArrayList.get(position).getColorCode()));
        holder.tvStepName.setText(stepsArrayList.get(position).getNickName());
        if (stepsArrayList.get(position).getStepDate() != null)
            holder.tvETAOfStep.setText(CalendarUtils.getFormattedDateWithSlot(stepsArrayList.get(position).getStepDate()));
        if (stepsArrayList.get(position).getPendingStepStatus().equals(PendingStep.PendingStepStatus.COMPLETED)) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return stepsArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, TouchCheckBox.OnCheckedChangeListener {
        OnClickOfRecyclerViewFragFiltrNew myOnClickRecyclerView;
        Context contxt;

        TouchCheckBox checkBox;
        TextView tvStepName, tvETAOfStep;
        CardView cardView;

        public ViewHolder(View itemView, int ViewType, Context c) {
            super(itemView);
            contxt = c;

//            itemView.setClickable(true);
//            itemView.setOnClickListener(this);

            checkBox = (TouchCheckBox) itemView.findViewById(R.id.cbRecyclerItemFragFilterNew);
            tvStepName = (TextView) itemView.findViewById(R.id.tvStepNameRecyclerItemFragFilterNew);
            tvETAOfStep = (TextView) itemView.findViewById(R.id.tvETAOfStepRecyclerItemFragFilterNew);
            cardView = (CardView) itemView.findViewById(R.id.cvRecyclerItemFragFilterNew);

            checkBox.setOnCheckedChangeListener(this);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            try {
                myOnClickRecyclerView = (OnClickOfRecyclerViewFragFiltrNew) contxt;
            } catch (ClassCastException e) {
                throw new ClassCastException(contxt.toString()
                        + " must implement OnClickOfRecyclerViewFragFilterNew");
            }
            switch (v.getId()){
                case R.id.cardViewActStepList :
            myOnClickRecyclerView.onClickRecyclerView(getPosition(), Constants.OPERATION_EDIT);
                    Logger.showMsg(contxt, stepsArrayList.get(getPosition()).getNickName());
//            onClickRecyclerView(getPosition(), Constants.OPERATION_EDIT);
                    break;
            }
        }

        @Override
        public void onCheckedChanged(View buttonView, boolean isChecked) {
            try {
                myOnClickRecyclerView = (OnClickOfRecyclerViewFragFiltrNew) contxt;
            } catch (ClassCastException e) {
                throw new ClassCastException(contxt.toString()
                        + " must implement OnClickOfRecyclerViewFragFilterNew");
            }
            if (buttonView.isPressed() && isChecked) {
                myOnClickRecyclerView.onClickRecyclerView(getPosition(), Constants.OPERATION_MARK_STEP_DONE);
//                onClickRecyclerView(getPosition(), Constants.OPERATION_MARK_STEP_DONE);
            } else if (buttonView.isPressed() && !isChecked) {
                myOnClickRecyclerView.onClickRecyclerView(getPosition(), Constants.OPERATION_MARK_STEP_UNDONE);
//                onClickRecyclerView(getPosition(), Constants.OPERATION_MARK_STEP_UNDONE);
            }
        }

//        public void onClickRecyclerView(int position, String operation) {
//            switch (operation) {
//                case Constants.OPERATION_EDIT:
////                    Intent intent = new Intent(contxt, ActAddNewStep.class);
////                    intent.putExtra(Constants.CALLER, Constants.ACT_STEP_LIST);
////                    intent.putExtra(Constants.STEP_OBJECT, stepArrayList.get(position));
////                    intent.putExtra(Constants.  OPERATION, Constants.OPERATION_EDIT);
////                    this.startActivity(intent);
//                    break;
//
//                case Constants.OPERATION_MARK_STEP_DONE:
//                    stepsArrayList.get(position).setPendingStepStatus(PendingStep.PendingStepStatus.COMPLETED);
//                    stepsArrayList.get(position).setUpdated(new DateTime(new Date()));
//                    stepsArrayList.get(position).freeSlot();
//                    stepsArrayList.get(position).setSlotId(0);
//                    stepsArrayList.get(position).save();
//                    stepsArrayList.get(position).cancelAlarm();
//                    rescheduleStepsOfCurrentGoal(position);
////                    getThinResultsFromLocal(scope);
////                    //sync
////                    GoogleSync.getInstance(context).sync();
//                    break;
//
//                case Constants.OPERATION_MARK_STEP_UNDONE:
//                    stepsArrayList.get(position).setPendingStepStatus(PendingStep.PendingStepStatus.TODO);
//                    stepsArrayList.get(position).setUpdated(new DateTime(new Date()));
//                    stepsArrayList.get(position).save();
//                    rescheduleStepsOfCurrentGoal(position);
////                    getThinResultsFromLocal(scope);
////                    //sync
////                    GoogleSync.getInstance(context).sync();
//                    break;
//            }
//        }
//
//        private void rescheduleStepsOfCurrentGoal(int position) {
//            Goal currentGoal = new Goal(contxt).get(stepsArrayList.get(position).getGoalId());
////        TimeBox timeBox = new TimeBox(context);
//            YodaCalendar yodaCalendar = new YodaCalendar(contxt, new TimeBox(contxt).get(currentGoal.getTimeBoxId()));
//            yodaCalendar.rescheduleSteps(currentGoal.getId());
//        }

    }
}