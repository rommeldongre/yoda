package com.greylabs.yoda.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.greylabs.yoda.R;
import com.greylabs.yoda.interfaces.onClickOfRecyclerViewActStepList;
import com.greylabs.yoda.models.PendingStep;
import com.greylabs.yoda.utils.CalendarUtils;
import com.greylabs.yoda.utils.Constants;
import com.greylabs.yoda.views.TouchCheckBox;

import java.util.ArrayList;

public class AdapterRecyclerViewActStepList extends RecyclerView.Adapter<AdapterRecyclerViewActStepList.ViewHolder> {

    private static final PendingStep.PendingStepType TYPE_SERIES_STEP = PendingStep.PendingStepType.SERIES_STEP;
    private static final PendingStep.PendingStepType TYPE_SPLIT_STEP = PendingStep.PendingStepType.SPLIT_STEP;
    ArrayList<PendingStep> stepsArrayList;
    Context context;
    boolean isEditOperation = false;
    String caller;

    public AdapterRecyclerViewActStepList(Context passedContext, ArrayList<PendingStep> stepsArrayList, boolean isEditOperation, String caller)
    {
        this.context = passedContext;
        this.stepsArrayList = stepsArrayList;
        this.isEditOperation = isEditOperation;
        this.caller = caller;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v;
        ViewHolder vhItem;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_act_step_list, parent, false);
        vhItem = new ViewHolder(v, viewType, context);
        return vhItem;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String noOfSteps="";
        holder.checkBox.setCircleColor(Integer.valueOf(stepsArrayList.get(position).getColorCode()));
        if(stepsArrayList.get(position).getPendingStepType().equals(TYPE_SERIES_STEP)
                ||stepsArrayList.get(position).getPendingStepType().equals(TYPE_SPLIT_STEP) ){
            noOfSteps = String.valueOf(stepsArrayList.get(position).getStepCount());
            holder.tvStepName.setText(stepsArrayList.get(position).getNickName()+" - "+noOfSteps+" session");
        }else {
            holder.tvStepName.setText(stepsArrayList.get(position).getNickName());
        }
        if(stepsArrayList.get(position).getStepDate()!=null && stepsArrayList.get(position).getPendingStepStatus()!= PendingStep.PendingStepStatus.UNSCHEDULED)
            holder.tvETAOfStep.setText(CalendarUtils.getFormattedDateWithSlot(stepsArrayList.get(position).getStepDate()));
       else{
            holder.tvETAOfStep.setText("Not Scheduled");
        }
        if(stepsArrayList.get(position).getPendingStepStatus().equals(PendingStep.PendingStepStatus.COMPLETED)){
            holder.checkBox.setChecked(true);
        }else {
            holder.checkBox.setChecked(false);
        }
        if(isEditOperation){
            holder.btnHandle.setVisibility(View.VISIBLE);
            switch (caller){
                case Constants.ACT_GOAL_LIST :
                    holder.btnDeleteStep.setVisibility(View.VISIBLE);
                    break;

                case Constants.ACT_ADD_NEW_STEP :
                    holder.btnDeleteStep.setVisibility(View.GONE);
                    break;
            }
        }else {
            holder.btnDeleteStep.setVisibility(View.GONE);
            holder.btnHandle.setVisibility(View.GONE);
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
        int Holderid;
        onClickOfRecyclerViewActStepList myOnClickRecyclerView;
        Context contxt;

        TouchCheckBox checkBox;
        TextView tvStepName, tvETAOfStep;
        Button btnDeleteStep, btnHandle;
        CardView cardView;

        public ViewHolder(View itemView, int ViewType, Context c) {
            super(itemView);
            contxt = c;
            itemView.setClickable(true);
            itemView.setOnClickListener(this);

            checkBox = (TouchCheckBox) itemView.findViewById(R.id.cbRecyclerItemActStepList);
            tvStepName = (TextView)itemView.findViewById(R.id.tvStepNameRecyclerItemActStepList);
            tvETAOfStep = (TextView)itemView.findViewById(R.id.tvETAOfStepRecyclerItemActStepList);
            btnDeleteStep = (Button) itemView.findViewById(R.id.btnDeleteStepRecyclerItemActStepList);
            btnHandle =  (Button) itemView.findViewById(R.id.btnHandleRecyclerItemActStepList);
            cardView = (CardView) itemView.findViewById(R.id.cardViewActStepList);

            checkBox.setOnCheckedChangeListener(this);
            btnDeleteStep.setOnClickListener(this);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            try {
                myOnClickRecyclerView = (onClickOfRecyclerViewActStepList) contxt;
            } catch (ClassCastException e) {
                throw new ClassCastException(contxt.toString()
                        + " must implement OnHeadlineSelectedListener");
            }
            switch (v.getId()){
                case R.id.btnDeleteStepRecyclerItemActStepList :
                    myOnClickRecyclerView.onClickRecyclerView(getPosition(), Constants.OPERATION_DELETE);
                    break;

                case R.id.cardViewActStepList :
                    myOnClickRecyclerView.onClickRecyclerView(getPosition(), Constants.OPERATION_EDIT);
                    break;
            }
        }

        @Override
        public void onCheckedChanged(View buttonView, boolean isChecked) {
            try {
                myOnClickRecyclerView = (onClickOfRecyclerViewActStepList) contxt;
            } catch (ClassCastException e) {
                throw new ClassCastException(contxt.toString()
                        + " must implement OnHeadlineSelectedListener");
            }
            if(buttonView.isPressed()&&isChecked){
                myOnClickRecyclerView.onClickRecyclerView(getPosition(), Constants.OPERATION_MARK_STEP_DONE);
            }else if(buttonView.isPressed()&& !isChecked){
                myOnClickRecyclerView.onClickRecyclerView(getPosition(), Constants.OPERATION_MARK_STEP_UNDONE);
            }
        }
    }
}