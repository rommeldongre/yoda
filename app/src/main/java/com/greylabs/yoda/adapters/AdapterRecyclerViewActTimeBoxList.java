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
import com.greylabs.yoda.interfaces.OnClickOfRecyclerViewActTimeboxList;
import com.greylabs.yoda.models.TimeBox;
import com.greylabs.yoda.utils.Constants;

import java.util.ArrayList;

public class AdapterRecyclerViewActTimeBoxList extends RecyclerView.Adapter<AdapterRecyclerViewActTimeBoxList.ViewHolder> {

    ArrayList<TimeBox> timeBoxArrayList;
    Context context;
    boolean isEditOperation = false;

    public AdapterRecyclerViewActTimeBoxList(Context passedContext, ArrayList<TimeBox> timeBoxArrayList, boolean isEditOperation)
    {
        this.context = passedContext;
        this.timeBoxArrayList = timeBoxArrayList;
        this.isEditOperation = isEditOperation;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v;
        ViewHolder vhItem;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_act_timebox_list, parent, false);
        vhItem = new ViewHolder(v, viewType, context);
        return vhItem;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvTimeBoxName.setText(timeBoxArrayList.get(position).getNickName());
        if(isEditOperation){
            holder.btnDeleteTimeBox.setVisibility(View.VISIBLE);
            holder.btnEditTimeBox.setVisibility(View.VISIBLE);
        }else {
            holder.btnDeleteTimeBox.setVisibility(View.GONE);
            holder.btnEditTimeBox.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return timeBoxArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        OnClickOfRecyclerViewActTimeboxList myOnClickRecyclerView;
        Context contxt;

        TextView tvTimeBoxName;
        Button btnEditTimeBox, btnDeleteTimeBox;
        CardView cardView;


        public ViewHolder(View itemView, int ViewType, Context c) {
            super(itemView);
            contxt = c;
            itemView.setClickable(true);
            itemView.setOnClickListener(this);

            tvTimeBoxName = (TextView)itemView.findViewById(R.id.tvGoalNameRecyclerItemActTimeBoxList);
            btnEditTimeBox = (Button) itemView.findViewById(R.id.btnEditTimeBoxRecyclerItemActTimeBoxList);
            btnDeleteTimeBox = (Button) itemView.findViewById(R.id.btnDeleteTimeBoxRecyclerItemActTimeBoxList);
            cardView = (CardView) itemView.findViewById(R.id.cardViewActTimeBoxList);

            btnEditTimeBox.setOnClickListener(this);
            btnDeleteTimeBox.setOnClickListener(this);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            try {
                myOnClickRecyclerView = (OnClickOfRecyclerViewActTimeboxList) contxt;
            } catch (ClassCastException e) {
                throw new ClassCastException(contxt.toString()
                        + " must implement OnClickOfRecyclerViewActTimeBoxList");
            }
            switch (v.getId()){
                case R.id.btnEditTimeBoxRecyclerItemActTimeBoxList :
                    myOnClickRecyclerView.onClickRecyclerView(getPosition(), Constants.OPERATION_EDIT);
                    break;

                case R.id.btnDeleteTimeBoxRecyclerItemActTimeBoxList :
                    myOnClickRecyclerView.onClickRecyclerView(getPosition(), Constants.OPERATION_DELETE);
                    break;

                case R.id.cardViewActTimeBoxList :
                    myOnClickRecyclerView.onClickRecyclerView(getPosition(), Constants.OPERATION_SHOW_STEPS);
                    break;
            }
        }
    }
}