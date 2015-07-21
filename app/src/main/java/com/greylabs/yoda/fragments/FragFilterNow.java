package com.greylabs.yoda.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.greylabs.yoda.R;

public class FragFilterNow extends Fragment {

    Context context;
//    RecyclerView mRecyclerView;
//    RecyclerView.Adapter mAdapter;
//    RecyclerView.LayoutManager mLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        currentApp= (App) getArguments().getSerializable("currentApp");
        context  = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_filter_now, null);
//        ivEmptyView = (ImageView) view.findViewById(R.id.ivEmptyViewFragNotification);
        initialize(view);
        return view;
    }

    private void initialize(View view) {
//        mRecyclerView = (RecyclerView) view.findViewById(R.id.RecyclerViewFragNotification);
//        mRecyclerView.setHasFixedSize(false);
//        mAdapter = new AdapterRecyclerViewFragNotification(notificationArrayList,context);
//        mRecyclerView.setAdapter(mAdapter);
//        mLayoutManager = new LinearLayoutManager(context);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        setEmptyViewVisibility();
    }

    public void setEmptyViewVisibility() {
//        if (notificationArrayList.isEmpty()) {
//            mRecyclerView.setVisibility(View.GONE);
//            ivEmptyView.setVisibility(View.VISIBLE);
//        }
//        else {
//            mRecyclerView.setVisibility(View.VISIBLE);
//            ivEmptyView.setVisibility(View.GONE);
//        }
    }
}