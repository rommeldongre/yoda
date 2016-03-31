package com.greylabs.ydo.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.greylabs.ydo.R;
import com.greylabs.ydo.adapters.AdapterRecyclerViewFragFilterFinal;
import com.greylabs.ydo.enums.StepFilterType;
import com.greylabs.ydo.models.PendingStep;
import com.greylabs.ydo.utils.Constants;
import com.greylabs.ydo.utils.FilterUtility;

import java.util.ArrayList;
import java.util.List;

public class FragFilterFinal extends Fragment {

    Context context;
    StepFilterType scope;
    TextView tvEmptyView;

    RecyclerView recyclerView;
    AdapterRecyclerViewFragFilterFinal mAdapter;
    LinearLayoutManager mLayoutManager;
    ArrayList<PendingStep> pendingStepArrayList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scope = (StepFilterType) getArguments().getSerializable(Constants.FILTER_SCOPE);
        context  = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_filter_final, null);
        initialize(view);
        return view;
    }

    private void initialize(View view) {

        tvEmptyView = (TextView) view.findViewById(R.id.tvEmptyViewFragFilter);
        recyclerView = (RecyclerView) view.findViewById(R.id.RecyclerViewFragFilterFinal);
        recyclerView.setHasFixedSize(true);
        getThinResultsFromLocal(scope);

        mAdapter = new AdapterRecyclerViewFragFilterFinal(context, pendingStepArrayList, scope, recyclerView, tvEmptyView);
        recyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(null);
    }

    private void getThinResultsFromLocal(StepFilterType scope) {
        if(pendingStepArrayList!=null){
            pendingStepArrayList.clear();
        }
        FilterUtility filterUtility = new FilterUtility(context);
        List<PendingStep> temp=filterUtility.getPendingStepsArrayList(scope);
        if(temp!=null) {
            pendingStepArrayList.addAll(temp);
        }
        setEmptyViewVisibility();
    }

    public void setEmptyViewVisibility() {
        if (pendingStepArrayList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            tvEmptyView.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            tvEmptyView.setVisibility(View.GONE);
        }
    }

    public void onClickRecyclerView(int position, String operation) {
//        pendingStepArrayList.remove(position);
//        mAdapter.notifyDataSetChanged();
    }
}