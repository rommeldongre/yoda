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
import com.greylabs.yoda.models.Goal;
import com.greylabs.yoda.utils.CalendarUtils;
import com.greylabs.yoda.utils.Constants;
import com.greylabs.yoda.views.CircleView;

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
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_act_goal_list, parent, false);
        vhItem = new ViewHolder(v, viewType, context);
        return vhItem;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvGoalName.setText(goalArrayList.get(position).getNickName());
        holder.tvEndDateGoal.setText(CalendarUtils.getFormattedDateWithSlot(goalArrayList.get(position).getDueDate()));
        holder.btnBullet.setFillColor(Integer.parseInt(goalArrayList.get(position).getColorCode()));
        holder.btnBullet.setTitleText(String.valueOf(goalArrayList.get(position).getRemainingStepCount()));
        holder.progressBar.setProgress((int)goalArrayList.get(position).getGoalProgress());
        if(isEditOperation){
            holder.btnHandle.setVisibility(View.VISIBLE);
            holder.btnDeleteGoal.setVisibility(View.VISIBLE);
            holder.btnEditGoal.setVisibility(View.VISIBLE);
        }else {
            holder.btnBullet.setVisibility(View.VISIBLE);
            holder.btnDeleteGoal.setVisibility(View.GONE);
            holder.btnEditGoal.setVisibility(View.GONE);
            holder.btnHandle.setVisibility(View.GONE);
        }
        if(goalArrayList.get(position).getNickName().equals(Constants.NICKNAME_STRETCH_GOAL)){
            holder.btnDeleteGoal.setVisibility(View.GONE);
            holder.btnEditGoal.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return goalArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        int Holderid;
        OnClickOfRecyclerViewActGoalList myOnClickRecyclerView;
        Context contxt;

        TextView tvGoalName, tvEndDateGoal;
        Button btnEditGoal, btnDeleteGoal, btnHandle;
        CircleView btnBullet;
        ProgressBar progressBar;
        CardView cardView;


        public ViewHolder(View itemView, int ViewType, Context c) {
            super(itemView);
            contxt = c;
            itemView.setClickable(true);
            itemView.setOnClickListener(this);

            btnBullet = (CircleView) itemView.findViewById(R.id.btnBulletRecyclerItemActGoalList);
            tvGoalName = (TextView)itemView.findViewById(R.id.tvGoalNameRecyclerItemActGoalList);
            tvEndDateGoal = (TextView)itemView.findViewById(R.id.tvEndDateOfGoalRecyclerItemActGoalList);
            btnEditGoal = (Button) itemView.findViewById(R.id.btnEditGoalRecyclerItemActGoalList);
            btnDeleteGoal = (Button) itemView.findViewById(R.id.btnDeleteGoalRecyclerItemActGoalList);
            btnHandle =  (Button) itemView.findViewById(R.id.btnHandleRecyclerItemActGoalList);
            progressBar = (ProgressBar) itemView.findViewById(R.id.pbRecyclerItemActGoalList);
            cardView = (CardView) itemView.findViewById(R.id.cardViewActGoalList);

            btnEditGoal.setOnClickListener(this);
            btnDeleteGoal.setOnClickListener(this);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
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