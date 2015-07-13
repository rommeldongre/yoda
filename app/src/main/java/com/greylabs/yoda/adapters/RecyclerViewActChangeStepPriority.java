package com.greylabs.yoda.adapters;

import android.content.Context;
import android.graphics.AvoidXfermode;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.greylabs.yoda.R;
import com.greylabs.yoda.interfaces.OnClickOfRecyclerViewActChangeStepPriority;
import com.greylabs.yoda.models.PendingStep;
import com.greylabs.yoda.utils.Constants;
import com.greylabs.yoda.utils.Logger;

import java.util.ArrayList;

public class RecyclerViewActChangeStepPriority extends RecyclerView.Adapter<RecyclerViewActChangeStepPriority.ViewHolder> {

    ArrayList<PendingStep> stepsArrayList;
    Context context;

    public RecyclerViewActChangeStepPriority(Context passedContext, ArrayList<PendingStep> stepsArrayList)
    {
        this.context = passedContext;
        this.stepsArrayList = stepsArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v;
        ViewHolder vhItem;
//        switch (viewType){
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_act_change_step_priority, parent, false);
        vhItem = new ViewHolder(v, viewType, context);
        return vhItem;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//            switch (holder.Holderid) {
        holder.tvStepName.setText(stepsArrayList.get(position).getNickName());
//        holder.btnDeleteStep.getBackground().setColorFilter(R.color.white, PorterDuff.Mode.);
    }

    @Override
    public int getItemCount() {
        return stepsArrayList.size();//+ 1;
    }

    @Override
    public int getItemViewType(int position) {
//        switch (stepsArrayList.get(position).getType())
//        {
//            case Utilities.TYPE_AUDIO_CAPTURE : return 1;
//        }
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        int Holderid;
        OnClickOfRecyclerViewActChangeStepPriority myOnClickRecyclerView;
        Context contxt;

        TextView tvStepName;
        Button btnEditStep, btnDeleteStep;

        public ViewHolder(View itemView, int ViewType, Context c) {
            super(itemView);
            contxt = c;
            itemView.setClickable(true);
            itemView.setOnClickListener(this);

//            switch (ViewType){
//                case Utilities.TYPE_INT_AUDIO_CAPTURE :
            tvStepName = (TextView)itemView.findViewById(R.id.tvStepNameRecyclerItemActChangeStepPriority);
            btnEditStep = (Button) itemView.findViewById(R.id.btnEditStepRecyclerItemActChangeStepPriority);
            btnDeleteStep = (Button) itemView.findViewById(R.id.btnDeleteStepRecyclerItemActChangeStepPriority);
//            Holderid = 0;
//                    break;

            btnEditStep.setOnClickListener(this);
            btnDeleteStep.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

//            switch (v.getId()){
//                case R.id.btnEditStepRecyclerItemActChangeStepPriority :
//                    Logger.showMsg(contxt, "edit "+getPosition());
//                    break;
//
//                case R.id.btnDeleteStepRecyclerItemActChangeStepPriority :
//                    Logger.showMsg(contxt, "delete "+getPosition());
//                    break;
//            }

            try {
                myOnClickRecyclerView = (OnClickOfRecyclerViewActChangeStepPriority) contxt;
            } catch (ClassCastException e) {
                throw new ClassCastException(contxt.toString()
                        + " must implement OnHeadlineSelectedListener");
            }
            switch (v.getId()){
                case R.id.btnEditStepRecyclerItemActChangeStepPriority :
                    myOnClickRecyclerView.onClickRecyclerView(getPosition(), Constants.OPERATION_EDIT);
                    break;

                case R.id.btnDeleteStepRecyclerItemActChangeStepPriority :
                    myOnClickRecyclerView.onClickRecyclerView(getPosition(), Constants.OPERATION_DELETE);
                    break;
            }
        }
    }
}