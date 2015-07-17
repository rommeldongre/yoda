package com.greylabs.yoda.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.greylabs.yoda.R;
import com.greylabs.yoda.interfaces.OnClickOfRecyclerViewActGoalList;
import com.greylabs.yoda.interfaces.onClickOfRecyclerViewActChangeStepPriority;
import com.greylabs.yoda.models.Goal;
import com.greylabs.yoda.utils.Constants;

import java.util.ArrayList;

public class AdapterRecyclerViewActGoalList extends RecyclerView.Adapter<AdapterRecyclerViewActGoalList.ViewHolder> {

    ArrayList<Goal> goalArrayList;
    Context context;
    boolean isEditOperation = false;

    public AdapterRecyclerViewActGoalList(Context passedContext, ArrayList<Goal> goalArrayList, boolean isEditOperation)
    {
        this.context = passedContext;
        this.goalArrayList = goalArrayList;
        this.isEditOperation = isEditOperation;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v;
        ViewHolder vhItem;
//        switch (viewType){
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_act_goal_list, parent, false);
        vhItem = new ViewHolder(v, viewType, context);
        return vhItem;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//            switch (holder.Holderid) {
        holder.tvGoalName.setText(goalArrayList.get(position).getNickName());
        if(isEditOperation){
            holder.btnHandle.setVisibility(View.VISIBLE);
            holder.btnDeleteGoal.setVisibility(View.VISIBLE);
            holder.btnEditGoal.setVisibility(View.VISIBLE);
            holder.progressBar.setVisibility(View.GONE);
        }else {
            holder.btnDeleteGoal.setVisibility(View.GONE);
            holder.btnEditGoal.setVisibility(View.GONE);
            holder.btnHandle.setVisibility(View.GONE);
            holder.progressBar.setVisibility(View.VISIBLE);
        }
//        holder.btnDeleteGoal.getBackground().setColorFilter(R.color.white, PorterDuff.Mode.);
    }

    @Override
    public int getItemCount() {
        return goalArrayList.size();//+ 1;
    }

    @Override
    public int getItemViewType(int position) {
//        switch (goalArrayList.get(position).getType())
//        {
//            case Utilities.TYPE_AUDIO_CAPTURE : return 1;
//        }
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        int Holderid;
        OnClickOfRecyclerViewActGoalList myOnClickRecyclerView;
        Context contxt;

        TextView tvGoalName;
        Button btnEditGoal, btnDeleteGoal, btnHandle;
        ProgressBar progressBar;
        CardView cardView;


        public ViewHolder(View itemView, int ViewType, Context c) {
            super(itemView);
            contxt = c;
            itemView.setClickable(true);
            itemView.setOnClickListener(this);

//            switch (ViewType){
//                case Utilities.TYPE_INT_AUDIO_CAPTURE :
            tvGoalName = (TextView)itemView.findViewById(R.id.tvGoalNameRecyclerItemActGoalList);
            btnEditGoal = (Button) itemView.findViewById(R.id.btnEditGoalRecyclerItemActGoalList);
            btnDeleteGoal = (Button) itemView.findViewById(R.id.btnDeleteGoalRecyclerItemActGoalList);
            btnHandle =  (Button) itemView.findViewById(R.id.btnHandleRecyclerItemActGoalList);
            progressBar = (ProgressBar) itemView.findViewById(R.id.pbRecyclerItemActGoalList);
            cardView = (CardView) itemView.findViewById(R.id.cardViewActGoalList);
//            Holderid = 0;
//                    break;

            btnEditGoal.setOnClickListener(this);
            btnDeleteGoal.setOnClickListener(this);
            cardView.setOnClickListener(this);
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
                myOnClickRecyclerView = (OnClickOfRecyclerViewActGoalList) contxt;
            } catch (ClassCastException e) {
                throw new ClassCastException(contxt.toString()
                        + " must implement OnHeadlineSelectedListener");
            }
            switch (v.getId()){
                case R.id.btnEditGoalRecyclerItemActGoalList :
                    myOnClickRecyclerView.onClickRecyclerView(getPosition(), Constants.OPERATION_EDIT);
                    break;

                case R.id.btnDeleteGoalRecyclerItemActGoalList :
                    myOnClickRecyclerView.onClickRecyclerView(getPosition(), Constants.OPERATION_DELETE);
                    break;

                case R.id.cardViewActGoalList :
                    myOnClickRecyclerView.onClickRecyclerView(getPosition(), Constants.OPERATION_SHOW_STEPS);
                    break;
            }
        }
    }
}