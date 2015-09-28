package com.greylabs.yoda.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.api.client.util.DateTime;
import com.greylabs.yoda.R;
import com.greylabs.yoda.adapters.AdapterRecyclerViewFragFilterNew;
import com.greylabs.yoda.enums.StepFilterType;
import com.greylabs.yoda.models.Goal;
import com.greylabs.yoda.models.PendingStep;
import com.greylabs.yoda.models.TimeBox;
import com.greylabs.yoda.scheduler.YodaCalendar;
import com.greylabs.yoda.utils.Constants;
import com.greylabs.yoda.utils.FilterUtility;

import java.util.ArrayList;
import java.util.Date;

public class FragFilterNew extends Fragment {

    Context context;
    StepFilterType scope;
    TextView tvEmptyView;
    //    List<Goal> listGoals;
//    Map<Long, List<PendingStep>> filteredData;
    ArrayList<PendingStep> stepArrayList;

    RecyclerView recyclerView;
    AdapterRecyclerViewFragFilterNew mAdapter;
    LinearLayoutManager mLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scope = (StepFilterType) getArguments().getSerializable(Constants.FILTER_SCOPE);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_filter_new, null);
        initialize(view);
        return view;
    }

    private void initialize(View view) {

//        listGoals = new ArrayList<>();
        stepArrayList = new ArrayList<>();
        tvEmptyView = (TextView) view.findViewById(R.id.tvEmptyViewFragFilterNew);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerFragFilterNew);
        recyclerView.setHasFixedSize(true);

        mAdapter = new AdapterRecyclerViewFragFilterNew(context, stepArrayList);
        recyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(null);

        getThinResultsFromLocal(scope);
    }

    private void getThinResultsFromLocal(StepFilterType scope) {
        stepArrayList.clear();
        FilterUtility filterUtility = new FilterUtility(context);
        ArrayList<PendingStep> temp = filterUtility.getPendingStepsArrayList(scope);
        if (temp != null) {
            stepArrayList.addAll(temp);
        }
        mAdapter.notifyDataSetChanged();
        setEmptyViewVisibility();
    }

    public void setEmptyViewVisibility() {
        if (stepArrayList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            tvEmptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            tvEmptyView.setVisibility(View.GONE);
        }
    }

    public void onClickRecyclerView(int position, String operation) {
        switch (operation) {
            case Constants.OPERATION_EDIT:
//                Logger.showMsg(context, "EDIT");
//                Intent intent = new Intent(context, ActAddNewStep.class);
//                intent.putExtra(Constants.CALLER, Constants.ACT_STEP_LIST);
//                intent.putExtra(Constants.STEP_OBJECT, stepArrayList.get(position));
//                intent.putExtra(Constants.  OPERATION, Constants.OPERATION_EDIT);
//                this.startActivity(intent);
                break;

            case Constants.OPERATION_MARK_STEP_DONE:
                stepArrayList.get(position).setPendingStepStatus(PendingStep.PendingStepStatus.COMPLETED);
                stepArrayList.get(position).setUpdated(new DateTime(new Date()));
                stepArrayList.get(position).freeSlot();
                stepArrayList.get(position).setSlotId(0);
                stepArrayList.get(position).save();
                stepArrayList.get(position).cancelAlarm();
                rescheduleStepsOfCurrentGoal(position);
                getThinResultsFromLocal(scope);
                //sync
//                GoogleSync.getInstance(context).sync();
                break;

            case Constants.OPERATION_MARK_STEP_UNDONE:
                stepArrayList.get(position).setPendingStepStatus(PendingStep.PendingStepStatus.TODO);
                stepArrayList.get(position).setUpdated(new DateTime(new Date()));
                stepArrayList.get(position).save();
                rescheduleStepsOfCurrentGoal(position);
                getThinResultsFromLocal(scope);
                //sync
//                GoogleSync.getInstance(context).sync();
                break;
        }
    }

    private void rescheduleStepsOfCurrentGoal(int position) {
        Goal currentGoal = new Goal(context).get(stepArrayList.get(position).getGoalId());
//        TimeBox timeBox = new TimeBox(context);
        YodaCalendar yodaCalendar = new YodaCalendar(context, new TimeBox(context).get(currentGoal.getTimeBoxId()));
        yodaCalendar.rescheduleSteps(currentGoal.getId());
    }
}