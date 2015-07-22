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

import com.greylabs.yoda.R;
import com.greylabs.yoda.adapters.AdapterRecyclerViewFragFilter;
import com.greylabs.yoda.models.Goal;
import com.greylabs.yoda.utils.Constants;

import java.util.ArrayList;

public class FragFilter extends Fragment {

    Context context;
    String scope;
    TextView tvEmptyView;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Goal> goalArrayList = new ArrayList<>();

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
        mRecyclerView = (RecyclerView) view.findViewById(R.id.RecyclerViewFragFilter);
        tvEmptyView = (TextView) view.findViewById(R.id.tvEmptyViewFragFilter);
        mRecyclerView.setHasFixedSize(false);
        mAdapter = new AdapterRecyclerViewFragFilter(context, goalArrayList);
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        getThinResultsFromLocal();
        setEmptyViewVisibility();
    }

    private void getThinResultsFromLocal() {
        if(goalArrayList.isEmpty()){
            Goal goal = new Goal(context) ;
            goalArrayList.addAll(goal.getAll());
            mAdapter.notifyDataSetChanged();
        }
    }

    public void setEmptyViewVisibility() {
        if (goalArrayList.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            tvEmptyView.setVisibility(View.VISIBLE);
        }
        else {
            mRecyclerView.setVisibility(View.VISIBLE);
            tvEmptyView.setVisibility(View.GONE);
        }
    }
}