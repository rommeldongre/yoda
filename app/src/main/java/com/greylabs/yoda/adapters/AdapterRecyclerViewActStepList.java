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

import java.util.ArrayList;

public class AdapterRecyclerViewActStepList extends RecyclerView.Adapter<AdapterRecyclerViewActStepList.ViewHolder> {

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
        if(stepsArrayList.get(position).getPendingStepType().equals(PendingStep.PendingStepType.SERIES_STEP)){
            noOfSteps = String.valueOf(stepsArrayList.get(position).getStepCount());
            holder.tvStepName.setText(stepsArrayList.get(position).getNickName()+" - "+noOfSteps+" session");
        }else {
            holder.tvStepName.setText(stepsArrayList.get(position).getNickName());
        }

        if(stepsArrayList.get(position).getStepDate()!=null)
            holder.tvETAOfStep.setText(CalendarUtils.getFormattedDateWithSlot(stepsArrayList.get(position).getStepDate()));
        if(stepsArrayList.get(position).getPendingStepStatus().equals(PendingStep.PendingStepStatus.COMPLETED)){
            holder.checkBox.setChecked(true);
            holder.checkBox.setBackgroundColor(Integer.valueOf(stepsArrayList.get(position).getColorCode()));
//            holder.checkBox.setEnabled(false);
        }
        if(isEditOperation){
            holder.btnHandle.setVisibility(View.VISIBLE);
            switch (caller){
                case Constants.ACT_GOAL_LIST :
                    holder.btnDeleteStep.setVisibility(View.VISIBLE);
//                    holder.btnEditStep.setVisibility(View.VISIBLE);
                    break;

                case Constants.ACT_ADD_NEW_STEP :
                    holder.btnDeleteStep.setVisibility(View.GONE);
//                    holder.btnEditStep.setVisibility(View.GONE);
                    break;
            }
        }else {
            holder.btnDeleteStep.setVisibility(View.GONE);
            holder.btnHandle.setVisibility(View.GONE);
//            holder.btnEditStep.setVisibility(View.GONE);
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

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
        int Holderid;
        onClickOfRecyclerViewActStepList myOnClickRecyclerView;
        Context contxt;

        CheckBox checkBox;
        TextView tvStepName, tvETAOfStep;
        Button btnDeleteStep, btnHandle;//btnEditStep,
        CardView cardView;

        public ViewHolder(View itemView, int ViewType, Context c) {
            super(itemView);
            contxt = c;
            itemView.setClickable(true);
            itemView.setOnClickListener(this);

            checkBox = (CheckBox) itemView.findViewById(R.id.cbRecyclerItemActStepList);
            tvStepName = (TextView)itemView.findViewById(R.id.tvStepNameRecyclerItemActStepList);
            tvETAOfStep = (TextView)itemView.findViewById(R.id.tvETAOfStepRecyclerItemActStepList);
            btnDeleteStep = (Button) itemView.findViewById(R.id.btnDeleteStepRecyclerItemActStepList);
            btnHandle =  (Button) itemView.findViewById(R.id.btnHandleRecyclerItemActStepList);
//            btnEditStep = (Button) itemView.findViewById(R.id.btnEditStepRecyclerItemActStepList);
            cardView = (CardView) itemView.findViewById(R.id.cardViewActStepList);

            checkBox.setOnCheckedChangeListener(this);
            btnDeleteStep.setOnClickListener(this);
            cardView.setOnClickListener(this);
//            btnEditStep.setOnClickListener(this);
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
//                case R.id.btnEditStepRecyclerItemActStepList :
//                    myOnClickRecyclerView.onClickRecyclerView(getPosition(), Constants.OPERATION_EDIT);
//                    break;
//
                case R.id.btnDeleteStepRecyclerItemActStepList :
                    myOnClickRecyclerView.onClickRecyclerView(getPosition(), Constants.OPERATION_DELETE);
                    break;

                case R.id.cardViewActStepList :
                    myOnClickRecyclerView.onClickRecyclerView(getPosition(), Constants.OPERATION_EDIT);
                    break;
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            try {
                myOnClickRecyclerView = (onClickOfRecyclerViewActStepList) contxt;
            } catch (ClassCastException e) {
                throw new ClassCastException(contxt.toString()
                        + " must implement OnHeadlineSelectedListener");
            }
            if(isChecked){
                myOnClickRecyclerView.onClickRecyclerView(getPosition(), Constants.OPERATION_MARK_STEP_DONE);
//                buttonView.setEnabled(false);
            }else {
                myOnClickRecyclerView.onClickRecyclerView(getPosition(), Constants.OPERATION_MARK_STEP_UNDONE);
            }
        }
    }
}