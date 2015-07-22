package com.greylabs.yoda.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.greylabs.yoda.R;
import com.greylabs.yoda.interfaces.OnClickOfRecyclerViewActGoalList;
import com.greylabs.yoda.models.Goal;
import com.greylabs.yoda.utils.Constants;
import com.greylabs.yoda.views.StepView;

import java.util.ArrayList;

public class AdapterRecyclerViewFragFilter extends RecyclerView.Adapter<AdapterRecyclerViewFragFilter.ViewHolder> {

    ArrayList<Goal> goalArrayList;
    Context context;

    public AdapterRecyclerViewFragFilter(Context passedContext, ArrayList<Goal> goalArrayList)
    {
        this.context = passedContext;
        this.goalArrayList = goalArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        ViewHolder vhItem;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_frag_filter, parent, false);
        vhItem = new ViewHolder(v, viewType, context);
        return vhItem;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvGoalName.setText(goalArrayList.get(position).getNickName());
        for(int i=0; i<3;i++){
            holder.expandableLL.addView(new StepView(context, goalArrayList.get(position)));
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
        OnClickOfRecyclerViewActGoalList myOnClickRecyclerView;
        Context contxt;

        TextView tvGoalName;
        Button btnShowSteps, btnHideSteps;
        LinearLayout expandableLL;
//        CardView cardView;

        public ViewHolder(View itemView, int ViewType, Context c) {
            super(itemView);
            contxt = c;
            itemView.setClickable(true);
            itemView.setOnClickListener(this);

            tvGoalName = (TextView)itemView.findViewById(R.id.tvGoalNAmeRecyclerItemFragFilter);
            btnShowSteps = (Button) itemView.findViewById(R.id.btnShowStepsRecyclerItemFragFilter);
            btnHideSteps = (Button) itemView.findViewById(R.id.btnHideStepsRecyclerItemFragFilter);
            expandableLL = (LinearLayout) itemView.findViewById(R.id.expandableLLRecyclerItemFragFilter);
//            cardView = (CardView) itemView.findViewById(R.id.cardViewActGoalList);

            btnShowSteps.setOnClickListener(this);
            btnHideSteps.setOnClickListener(this);
//            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnShowStepsRecyclerItemFragFilter :
                    btnShowSteps.setVisibility(View.GONE);
                    btnHideSteps.setVisibility(View.VISIBLE);
                    expandableLL.setVisibility(View.VISIBLE);
                    break;

                case R.id.btnHideStepsRecyclerItemFragFilter :
                    btnHideSteps.setVisibility(View.GONE);
                    btnShowSteps.setVisibility(View.VISIBLE);
                    expandableLL.setVisibility(View.GONE);
                    break;

//                case R.id.cardViewActGoalList :
//                    myOnClickRecyclerView.onClickRecyclerView(getPosition(), Constants.OPERATION_SHOW_STEPS);
//                    break;
            }
        }
    }
}