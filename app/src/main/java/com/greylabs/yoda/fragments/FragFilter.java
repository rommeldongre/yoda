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
import com.greylabs.yoda.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FragFilter extends Fragment {

    Context context;
    String scope;
    TextView tvEmptyView;
//    ArrayList<Goal> goalArrayList = new ArrayList<>();

    AdapterExpandableList listAdapter;
    ExpandableListView expListView;
    List<String> listGoals;
    HashMap<String, List<String>> listDataChild;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scope = getArguments().getString(Constants.FILTER_SCOPE);
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

        getThinResultsFromLocal(scope);
        setEmptyViewVisibility();

        listAdapter = new AdapterExpandableList(context, listGoals, listDataChild);
        expListView.setAdapter(listAdapter);
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
                Toast.makeText(context,
                        listGoals.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(context,
                        listGoals.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(context,
                        listGoals.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listGoals.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });
    }

    private void getThinResultsFromLocal(String scope) {
//        if(goalArrayList.isEmpty()){
//            Goal goal = new Goal(context) ;
//            goalArrayList.addAll(goal.getAll());
//            mAdapter.notifyDataSetChanged();
//        }
        listGoals = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listGoals.add("Top 250");
        listGoals.add("Now Showing");
        listGoals.add("Coming Soon..");

        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("The Shawshank Redemption");
        top250.add("The Godfather");
        top250.add("The Godfather: Part II");
        top250.add("Pulp Fiction");
        top250.add("The Good, the Bad and the Ugly");
        top250.add("The Dark Knight");
        top250.add("12 Angry Men");

        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("The Conjuring");
        nowShowing.add("Despicable Me 2");
        nowShowing.add("Turbo");
        nowShowing.add("Grown Ups 2");
        nowShowing.add("Red 2");
        nowShowing.add("The Wolverine");

        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("2 Guns");
        comingSoon.add("The Smurfs 2");
        comingSoon.add("The Spectacular Now");
        comingSoon.add("The Canyons");
        comingSoon.add("Europa Report");

        listDataChild.put(listGoals.get(0), top250); // Header, Child data
        listDataChild.put(listGoals.get(1), nowShowing);
        listDataChild.put(listGoals.get(2), comingSoon);
    }

    public void setEmptyViewVisibility() {
        if (listGoals.isEmpty()) {
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