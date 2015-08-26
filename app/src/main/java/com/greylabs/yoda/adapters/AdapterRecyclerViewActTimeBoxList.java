package com.greylabs.yoda.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.greylabs.yoda.R;
import com.greylabs.yoda.interfaces.OnClickOfRecyclerViewActTimeboxList;
import com.greylabs.yoda.models.Slot;
import com.greylabs.yoda.models.TimeBox;
import com.greylabs.yoda.utils.Constants;
import com.greylabs.yoda.views.CircleView;

import java.util.ArrayList;

public class AdapterRecyclerViewActTimeBoxList extends RecyclerView.Adapter<AdapterRecyclerViewActTimeBoxList.ViewHolder> {

    ArrayList<TimeBox> timeBoxArrayList;
    Context context;
    Slot slot;
    boolean isEditOperation = false;

    public AdapterRecyclerViewActTimeBoxList(Context passedContext, ArrayList<TimeBox> timeBoxArrayList, boolean isEditOperation)
    {
        this.context = passedContext;
        this.timeBoxArrayList = timeBoxArrayList;
        this.isEditOperation = isEditOperation;
        this.slot = new Slot(context);
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
        int totalSlots = slot.getTotalSlotCount(timeBoxArrayList.get(position).getId());
        String slots = totalSlots - slot.getAvailableSlotCount(timeBoxArrayList.get(position).getId())+"/"+
                totalSlots +" Slots";
        holder.tvSlots.setText(slots);
        if(timeBoxArrayList.get(position).isActive()){
            holder.tvTimeBoxName.setTypeface(null, Typeface.ITALIC);
            holder.tvAttachedGoalName.setText(timeBoxArrayList.get(position).getGoalName());
        }else{
            holder.tvTimeBoxName.setTypeface(null, Typeface.BOLD);
            holder.tvAttachedGoalName.setText("");
        }
        if(timeBoxArrayList.get(position).getNickName().equals(Constants.NICKNAME_UNPLANNED_TIMEBOX)){
            holder.btnDeleteTimeBox.setVisibility(View.GONE);
            holder.tvSlots.setText("");
        }
        holder.circleView.setShowTitle(false);
        holder.circleView.setFillColor(Integer.parseInt(timeBoxArrayList.get(position).getColorCode()));
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

        CircleView circleView;
        TextView tvTimeBoxName, tvAttachedGoalName, tvSlots;
        Button btnDeleteTimeBox;
        CardView cardView;


        public ViewHolder(View itemView, int ViewType, Context c) {
            super(itemView);
            contxt = c;
            itemView.setClickable(true);
            itemView.setOnClickListener(this);

            circleView = (CircleView) itemView.findViewById(R.id.btnBulletRecyclerItemActTimeBoxList);
            tvTimeBoxName = (TextView)itemView.findViewById(R.id.tvTBNameRecyclerItemActTimeBoxList);
            tvAttachedGoalName = (TextView)itemView.findViewById(R.id.tvGoalNameRecyclerItemActTimeBoxList);
            tvSlots = (TextView)itemView.findViewById(R.id.tvSlotsRecyclerItemActTimeBoxList);
            btnDeleteTimeBox = (Button) itemView.findViewById(R.id.btnDeleteTimeBoxRecyclerItemActTimeBoxList);
            cardView = (CardView) itemView.findViewById(R.id.cardViewActTimeBoxList);

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
                case R.id.btnDeleteTimeBoxRecyclerItemActTimeBoxList :
                    myOnClickRecyclerView.onClickRecyclerView(getPosition(), Constants.OPERATION_DELETE);
                    break;

                case R.id.cardViewActTimeBoxList :
                    myOnClickRecyclerView.onClickRecyclerView(getPosition(), Constants.OPERATION_EDIT);
                    break;
            }
        }
    }
}