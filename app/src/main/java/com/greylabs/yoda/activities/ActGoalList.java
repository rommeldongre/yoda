package com.greylabs.yoda.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.greylabs.yoda.R;
import com.greylabs.yoda.adapters.AdapterRecyclerViewActGoalList;
import com.greylabs.yoda.adapters.DragSortRecycler;
import com.greylabs.yoda.interfaces.OnClickOfRecyclerViewActGoalList;
import com.greylabs.yoda.models.Goal;
import com.greylabs.yoda.utils.Constants;
import com.greylabs.yoda.utils.Logger;

import java.util.ArrayList;

public class ActGoalList  extends ActionBarActivity implements OnClickOfRecyclerViewActGoalList {

    private Toolbar toolbar;
    TextView emptyViewActGoalList;
    ArrayList<Goal> goalArrayList;
    boolean isOperationEdit = false;
    Menu menu;

    RecyclerView recyclerView;
    AdapterRecyclerViewActGoalList mAdapter;
    LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_list);
        initialize();
    }

    private void initialize() {
        goalArrayList = new ArrayList<>();
        toolbar = (Toolbar) findViewById(R.id.toolBarActGoalList);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.titleActGoalList));

        emptyViewActGoalList = (TextView) findViewById(R.id.tvEmptyViewActGoalList);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerViewActGoalList);
        recyclerView.setHasFixedSize(true);
        getGoalArrayFromLocal();
        mAdapter = new AdapterRecyclerViewActGoalList(this, goalArrayList, isOperationEdit);
        recyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(null);

        DragSortRecycler dragSortRecycler = new DragSortRecycler();
        dragSortRecycler.setViewHandleId(R.id.btnHandleRecyclerItemActGoalList);
        dragSortRecycler.setFloatingAlpha(0.8F);
        dragSortRecycler.setFloatingBgColor(getResources().getColor(R.color.ColorPrimary));
//        setAutoScrollSpeed(float)
//        How fast it auto scrolls when you get to the top or bottom of the screen. Should acheive similar results across devices.
//
//        setAutoScrollWindow(float)
//        Sets where it starts to autoscroll, this is a fraction of the total height of the RecyclerView. So a value of 0.1 will mean that it will start scrolling at the bottom 10% and top 90% of the view.

        dragSortRecycler.setOnItemMovedListener(new DragSortRecycler.OnItemMovedListener() {
            @Override
            public void onItemMoved(int from, int to) {
                if (from != to)
                    goalArrayList.add(to, goalArrayList.remove(from));
                mAdapter.notifyDataSetChanged();
            }
        });

        recyclerView.addItemDecoration(dragSortRecycler);
        recyclerView.addOnItemTouchListener(dragSortRecycler);
        recyclerView.setOnScrollListener(dragSortRecycler.getScrollListener());
    }

    private void getGoalArrayFromLocal() {
        goalArrayList.clear();
        Goal goal = new Goal(this);
        if(goal.getAll() != null)
            goalArrayList.addAll(goal.getAll());
        checkForEmptyViewVisibility();
    }

    private void checkForEmptyViewVisibility() {
        if (goalArrayList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyViewActGoalList.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyViewActGoalList.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_act_goal_list, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                this.finish();
                break;
            case R.id.actionEditActGoalList :
                menu.findItem(R.id.actionEditActGoalList).setVisible(false);
                menu.findItem(R.id.actionSaveActGoalList).setVisible(true);
                isOperationEdit = true;
                mAdapter = new AdapterRecyclerViewActGoalList(this, goalArrayList, isOperationEdit);
                recyclerView.setAdapter(mAdapter);
                break;
            case R.id.actionSaveActGoalList :
                menu.findItem(R.id.actionEditActGoalList).setVisible(true);
                menu.findItem(R.id.actionSaveActGoalList).setVisible(false);
                isOperationEdit = false;
                mAdapter = new AdapterRecyclerViewActGoalList(this, goalArrayList, isOperationEdit);
                recyclerView.setAdapter(mAdapter);
                saveGoalsByNewOrder();
                Logger.showMsg(this, "Changes Saved");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveGoalsByNewOrder() {
        for(int i=0; i<goalArrayList.size(); i++ ){
            goalArrayList.get(i).setOrder(i+1);
            goalArrayList.get(i).save();
        }
    }

    @Override
    public void onClickRecyclerView(final int Position, String operation) {
        if(isOperationEdit){
            switch (operation){
                case Constants.OPERATION_EDIT :
                    Intent intent = new Intent(this, ActGoalDetails.class);
                    intent.putExtra(Constants.GOAL_OBJECT, goalArrayList.get(Position));
                    intent.putExtra(Constants.GOAL_ATTACHED_IN_EXTRAS, true);
                    startActivity(intent);
                    break;

                case Constants.OPERATION_DELETE :
                    AlertDialog.Builder alertLogout = new AlertDialog.Builder(this);
                    alertLogout.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            goalArrayList.get(Position).delete();
                            Logger.showMsg(ActGoalList.this, Constants.MSG_GOAL_DELETED);
                            getGoalArrayFromLocal();
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                    alertLogout.setNegativeButton("Cancel", null);
                    alertLogout.setMessage(Constants.MSG_DELETE_GOAL);
                    alertLogout.show();
                    break;
            }
        }else if(!isOperationEdit){
            Intent i = new Intent(this, ActStepList.class);
            i.putExtra(Constants.GOAL_ATTACHED_IN_EXTRAS, true);
            i.putExtra(Constants.GOAL_OBJECT, goalArrayList.get(Position));
            i.putExtra(Constants.CALLER, Constants.ACT_GOAL_LIST);
            startActivity(i);
        }
    }
}