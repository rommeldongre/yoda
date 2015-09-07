package com.greylabs.yoda.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.greylabs.yoda.R;
import com.greylabs.yoda.adapters.AdapterExpandableList;
import com.greylabs.yoda.enums.StepFilterType;
import com.greylabs.yoda.models.Goal;
import com.greylabs.yoda.models.PendingStep;
import com.greylabs.yoda.utils.Constants;
import com.greylabs.yoda.utils.FilterUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FragFilter extends Fragment {

    Context context;
    StepFilterType scope;
    TextView tvEmptyView;

    AdapterExpandableList listAdapter;
    ExpandableListView expListView;
    List<Goal> listGoals;
    Map<Long, List<PendingStep>> filteredData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scope = (StepFilterType) getArguments().getSerializable(Constants.FILTER_SCOPE);
        context  = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_filter, null);
        initialize(view);
        return view;
    }

    private void initialize(View view) {

        tvEmptyView = (TextView) view.findViewById(R.id.tvEmptyViewFragFilter);
        expListView = (ExpandableListView) view.findViewById(R.id.lvExpFragFilter);
        listGoals=new ArrayList<>();
        getThinResultsFromLocal(scope);

        if(!filteredData.isEmpty()){
            listAdapter = new AdapterExpandableList(context, listGoals, filteredData);
            expListView.setAdapter(listAdapter);
        }
        //expand all the items
        if(!listGoals.isEmpty()){
            for(int i =0;i<listGoals.size();i++){
                expListView.expandGroup(i);
            }
        }
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                return false;
            }
        });
    }

    private void getThinResultsFromLocal(StepFilterType scope) {
        FilterUtility filterUtility = new FilterUtility(context);
        filteredData = filterUtility.getPendingSteps(scope);
        Set<Long> goalIdsSet = filteredData.keySet();
        for(long id:goalIdsSet){
            Goal goal = new Goal(context).get(id);
            listGoals.add(goal);
        }
        setEmptyViewVisibility();
    }

    public void setEmptyViewVisibility() {
        if (filteredData.isEmpty()) {
            expListView.setVisibility(View.GONE);
            tvEmptyView.setVisibility(View.VISIBLE);
        }
        else {
            expListView.setVisibility(View.VISIBLE);
            tvEmptyView.setVisibility(View.GONE);
        }
    }

    public void onCheckExpandableList(int groupPosition, int childPosition, final Goal goal) {
//        listGoals.remove(groupPosition);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                filteredData.remove(goal.getId());
                setEmptyViewVisibility();
            }
        });
    }
}