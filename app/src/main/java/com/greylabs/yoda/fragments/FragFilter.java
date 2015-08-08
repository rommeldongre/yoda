package com.greylabs.yoda.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.greylabs.yoda.R;
import com.greylabs.yoda.adapters.AdapterExpandableList;
import com.greylabs.yoda.enums.StepFilterType;
import com.greylabs.yoda.models.Goal;
import com.greylabs.yoda.models.PendingStep;
import com.greylabs.yoda.utils.Constants;
import com.greylabs.yoda.utils.FilterUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FragFilter extends Fragment {

    Context context;
    StepFilterType scope;
    TextView tvEmptyView;
//    ArrayList<Goal> goalArrayList = new ArrayList<>();

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
        if(!listGoals.isEmpty()){
            for(int i =0;i<listGoals.size();i++){
                expListView.expandGroup(i);
            }
        }
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listGoals.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
//                Toast.makeText(context,
//                        listGoals.get(groupPosition) + " Expanded",
//                        Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
//                Toast.makeText(context,
//                        listGoals.get(groupPosition) + " Collapsed",
//                        Toast.LENGTH_SHORT).show();
            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
//                Toast.makeText(context, listGoals.get(groupPosition) + " : " + filteredData.get(
//                                listGoals.get(groupPosition)).get( childPosition), Toast.LENGTH_SHORT)
//                        .show();
                return false;
            }
        });
    }

    private void getThinResultsFromLocal(StepFilterType scope) {
//        if(goalArrayList.isEmpty()){
//            Goal goal = new Goal(context) ;
//            goalArrayList.addAll(goal.getAll());
//            mAdapter.notifyDataSetChanged();
//        }

        FilterUtility filterUtility = new FilterUtility(context);
        filteredData = filterUtility.getPendingSteps(scope);
        Set<Long> goalIdsSet = filteredData.keySet();
        for(long id:goalIdsSet){
            Goal goal = new Goal(context).get(id);
            listGoals.add(goal);
        }

        setEmptyViewVisibility();

//        listGoals = new ArrayList<String>();
//        filteredData = new HashMap<String, List<String>>();
//
//        // Adding child data
//        listGoals.add("Top 250");
//        listGoals.add("Now Showing");
//        listGoals.add("Coming Soon..");
//
//        // Adding child data
//        List<String> top250 = new ArrayList<String>();
//        top250.add("The Shawshank Redemption");
//        top250.add("The Godfather");
//        top250.add("The Godfather: Part II");
//        top250.add("Pulp Fiction");
//        top250.add("The Good, the Bad and the Ugly");
//        top250.add("The Dark Knight");
//        top250.add("12 Angry Men");
//
//        List<String> nowShowing = new ArrayList<String>();
//        nowShowing.add("The Conjuring");
//        nowShowing.add("Despicable Me 2");
//        nowShowing.add("Turbo");
//        nowShowing.add("Grown Ups 2");
//        nowShowing.add("Red 2");
//        nowShowing.add("The Wolverine");
//
//        List<String> comingSoon = new ArrayList<String>();
//        comingSoon.add("2 Guns");
//        comingSoon.add("The Smurfs 2");
//        comingSoon.add("The Spectacular Now");
//        comingSoon.add("The Canyons");
//        comingSoon.add("Europa Report");
//
//        filteredData.put(listGoals.get(0), top250); // Header, Child data
//        filteredData.put(listGoals.get(1), nowShowing);
//        filteredData.put(listGoals.get(2), comingSoon);
    }

    public void setEmptyViewVisibility() {
        if (filteredData.isEmpty()) {
//        if (goalArrayList.isEmpty()) {
//            mRecyclerView.setVisibility(View.GONE);
            expListView.setVisibility(View.GONE);
            tvEmptyView.setVisibility(View.VISIBLE);
        }
        else {
//            mRecyclerView.setVisibility(View.VISIBLE);
            expListView.setVisibility(View.VISIBLE);
            tvEmptyView.setVisibility(View.GONE);
        }
    }
}