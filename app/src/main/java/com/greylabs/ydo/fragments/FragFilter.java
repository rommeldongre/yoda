package com.greylabs.ydo.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.greylabs.ydo.R;
import com.greylabs.ydo.adapters.AdapterExpandableList;
import com.greylabs.ydo.enums.StepFilterType;
import com.greylabs.ydo.models.Goal;
import com.greylabs.ydo.models.PendingStep;
import com.greylabs.ydo.utils.Constants;
import com.greylabs.ydo.utils.FilterUtility;
import com.greylabs.ydo.utils.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

//previously used but not now
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

        expListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                    int childPosition = ExpandableListView.getPackedPositionChild(id);

                    Logger.showMsg(context, groupPosition+" : "+childPosition);

                    // You now have everything that you would as if this was an OnChildClickListener()
                    // Add your logic here.

                    // Return true as we are handling the event.
                    return true;
                }
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