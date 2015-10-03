package com.greylabs.yoda.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.api.client.util.DateTime;
import com.greylabs.yoda.R;
import com.greylabs.yoda.apis.googleacc.GoogleSync;
import com.greylabs.yoda.enums.StepFilterType;
import com.greylabs.yoda.interfaces.OnClickOfRecyclerViewFragFilterFinal;
import com.greylabs.yoda.models.Goal;
import com.greylabs.yoda.models.PendingStep;
import com.greylabs.yoda.models.TimeBox;
import com.greylabs.yoda.scheduler.YodaCalendar;
import com.greylabs.yoda.utils.CalendarUtils;
import com.greylabs.yoda.utils.Constants;
import com.greylabs.yoda.views.TouchCheckBox;

import java.util.ArrayList;
import java.util.Date;

public class AdapterRecyclerViewFragFilterFinal extends RecyclerView.Adapter<AdapterRecyclerViewFragFilterFinal.ViewHolder> {

    ArrayList<PendingStep> stepsArrayList;
    Context context;
    StepFilterType scope;

    RecyclerView recyclerView;
    TextView tvEmptyView;

    public AdapterRecyclerViewFragFilterFinal(Context passedContext, ArrayList<PendingStep> stepsArrayList, StepFilterType scope, RecyclerView recyclerView, TextView tvEmptyView)
    {
        this.context = passedContext;
        this.stepsArrayList = stepsArrayList;
        this.scope = scope;
        this.recyclerView = recyclerView;
        this.tvEmptyView = tvEmptyView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        ViewHolder vhItem;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_frag_filter_final, parent, false);
        vhItem = new ViewHolder(v, viewType, context);
        return vhItem;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.checkBox.setCircleColor(Integer.valueOf(stepsArrayList.get(position).getColorCode()));
        holder.tvStepName.setText(stepsArrayList.get(position).getNickName());
        holder.tvETAOfStep.setText(CalendarUtils.getFormattedDateWithSlot(stepsArrayList.get(position).getStepDate()));
    }

    @Override
    public int getItemCount() {
        return stepsArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements TouchCheckBox.OnCheckedChangeListener {
        OnClickOfRecyclerViewFragFilterFinal myOnClickRecyclerView;
        Context contxt;

        TouchCheckBox checkBox;
        TextView tvStepName, tvETAOfStep;
        CardView cardView;

        public ViewHolder(View itemView, int ViewType, Context c) {
            super(itemView);
            contxt = c;
            itemView.setClickable(true);

            checkBox = (TouchCheckBox) itemView.findViewById(R.id.cbRecyclerItemFragFilterNew);
            tvStepName = (TextView) itemView.findViewById(R.id.tvStepNameRecyclerItemFragFilterNew);
            tvETAOfStep = (TextView) itemView.findViewById(R.id.tvETAOfStepRecyclerItemFragFilterNew);
            cardView = (CardView) itemView.findViewById(R.id.cvRecyclerItemFragFilterNew);

            checkBox.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(View buttonView, boolean isChecked) {
            try {
                myOnClickRecyclerView = (OnClickOfRecyclerViewFragFilterFinal) contxt;
            } catch (ClassCastException e) {
                throw new ClassCastException(contxt.toString()
                        + " must implement OnClickOfRecyclerViewFragFilterFinal");
            }

            PendingStep currentPendingStep = stepsArrayList.get(getPosition());
            stepsArrayList.remove(getPosition());
            setEmptyViewVisibility();
            notifyItemRemoved(getPosition());
            currentPendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.COMPLETED);
            currentPendingStep.setUpdated(new DateTime(new Date()));
            currentPendingStep.freeSlot();
            currentPendingStep.setSlotId(0);
            currentPendingStep.save();
            currentPendingStep.cancelAlarm();
            if(currentPendingStep.isExpire() == PendingStep.PendingStepExpire.EXPIRE){
                currentPendingStep.setDeleted(true);
                currentPendingStep.save();
            }
            rescheduleStepsOfCurrentGoal(currentPendingStep);
            //sync code
            GoogleSync.getInstance(contxt).sync();
            myOnClickRecyclerView.onClickRecyclerView(getPosition(), Constants.OPERATION_MARK_STEP_DONE, scope);
        }

        private void rescheduleStepsOfCurrentGoal(PendingStep currentPendingStep) {
            Goal currentGoal = new Goal(contxt).get(currentPendingStep.getGoalId());
            TimeBox currentTimeBox = new TimeBox(contxt).get(currentGoal.getTimeBoxId());
            YodaCalendar yodaCalendar = new YodaCalendar(contxt, currentTimeBox);
            yodaCalendar.rescheduleSteps(currentGoal.getId());
        }

        public void setEmptyViewVisibility() {
            if (stepsArrayList.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                tvEmptyView.setVisibility(View.VISIBLE);
            }
            else {
                recyclerView.setVisibility(View.VISIBLE);
                tvEmptyView.setVisibility(View.GONE);
            }
        }
    }
}