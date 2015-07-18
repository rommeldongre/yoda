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
import com.greylabs.yoda.adapters.DragSortRecycler;
import com.greylabs.yoda.adapters.AdapterRecyclerViewActStepList;
import com.greylabs.yoda.interfaces.onClickOfRecyclerViewActStepList;
import com.greylabs.yoda.models.Goal;
import com.greylabs.yoda.models.PendingStep;
import com.greylabs.yoda.utils.Constants;
import com.greylabs.yoda.utils.Logger;

import java.util.ArrayList;


public class ActStepList extends ActionBarActivity implements onClickOfRecyclerViewActStepList {

    private Toolbar toolbar;
    TextView emptyViewActChangeStepPriority;
    ArrayList<PendingStep> stepArrayList;
    boolean isOperationEdit = false;
    Menu menu;
    Goal currentGoal;
    String caller;

    RecyclerView recyclerView;
    AdapterRecyclerViewActStepList mAdapter;
    LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_step_priority);
        initialize();
    }

    private void initialize() {
        stepArrayList = new ArrayList<>();
        Intent i = getIntent();
        if(i.getBooleanExtra(Constants.GOAL_ATTACHED_IN_EXTRAS, false))
            currentGoal = (Goal) i.getExtras().getSerializable(Constants.GOAL_OBJECT);

        toolbar = (Toolbar) findViewById(R.id.toolBarActChangeStepPriority);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(currentGoal != null){
            getSupportActionBar().setTitle(currentGoal.getNickName());
        }else{
            getSupportActionBar().setTitle(getResources().getString(R.string.titleActChangeStepPriority));
        }

        caller = i.getStringExtra(Constants.CALLER);
        switch (caller){
            case Constants.ACT_ADD_NEW_STEP :
                isOperationEdit = true;
                stepArrayList = (ArrayList<PendingStep>) i.getSerializableExtra(Constants.STEP_ARRAY_LIST);
                break;

            case Constants.ACT_GOAL_LIST :
                getStepArrayFromLocal();
                break;
        }
        emptyViewActChangeStepPriority = (TextView) findViewById(R.id.tvEmptyViewActChangeStepPriority);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerActChangeStepPriority);
        recyclerView.setHasFixedSize(true);
        mAdapter = new AdapterRecyclerViewActStepList(this, stepArrayList, isOperationEdit, caller);
        recyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(null);

        DragSortRecycler dragSortRecycler = new DragSortRecycler();
        dragSortRecycler.setViewHandleId(R.id.btnHandleRecyclerItemActStepList);
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
                if(from != to)
                    stepArrayList.add(to, stepArrayList.remove(from));
                mAdapter.notifyDataSetChanged();
            }
        });

        recyclerView.addItemDecoration(dragSortRecycler);
        recyclerView.addOnItemTouchListener(dragSortRecycler);
        recyclerView.setOnScrollListener(dragSortRecycler.getScrollListener());
    }


    private void getStepArrayFromLocal() {
        stepArrayList.clear();
        PendingStep pendingStep = new PendingStep(this);
        if(currentGoal != null && pendingStep.getAll(currentGoal.getId()) != null)
            stepArrayList.addAll(pendingStep.getAll(currentGoal.getId()));
        checkForEmptyViewVisibility();
    }

    private void checkForEmptyViewVisibility() {
        if (stepArrayList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyViewActChangeStepPriority.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyViewActChangeStepPriority.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_act_change_step_priority, menu);
        this.menu = menu;
        if(caller.equals(Constants.ACT_ADD_NEW_STEP)){
            menu.findItem(R.id.actionEditActStepList).setVisible(false);
            menu.findItem(R.id.actionSaveActStepList).setVisible(true);
        }else if(caller.equals(Constants.ACT_GOAL_LIST)){

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                this.finish();
                break;
            case R.id.actionEditActStepList :
                menu.findItem(R.id.actionEditActStepList).setVisible(false);
                menu.findItem(R.id.actionSaveActStepList).setVisible(true);
                isOperationEdit = true;
                mAdapter = new AdapterRecyclerViewActStepList(this, stepArrayList, isOperationEdit, caller);
                recyclerView.setAdapter(mAdapter);
                break;
            case R.id.actionSaveActStepList :
                switch (caller){
                    case Constants.ACT_GOAL_LIST :
                        menu.findItem(R.id.actionEditActStepList).setVisible(true);
                        menu.findItem(R.id.actionSaveActStepList).setVisible(false);
                        isOperationEdit = false;
                        mAdapter = new AdapterRecyclerViewActStepList(this, stepArrayList, isOperationEdit, caller);
                        recyclerView.setAdapter(mAdapter);
                        saveStepsByNewOrder();
                        Logger.showMsg(this, "Changes Saved");
                        break;

                    case Constants.ACT_ADD_NEW_STEP :
                        // send the arrayList in intent back to the ActAddStep
                        Intent secIntent = new Intent();
                        secIntent.putExtra(Constants.STEPS_ARRAY_LIST_WITH_NEW_PRIORITIES, getStepsByNewOrder());
                        setResult(Constants.RESULTCODE_OF_ACT_STEP_LIST, secIntent);
                        Logger.showMsg(this, "Changes Saved");
                        this.finish();
                        break;
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveStepsByNewOrder() {
        for(int i=0; i<stepArrayList.size(); i++ ){
            stepArrayList.get(i).setPriority(i + 1);
            stepArrayList.get(i).save();
        }
    }

    private ArrayList<PendingStep> getStepsByNewOrder() {
        for(int i=0; i<stepArrayList.size(); i++ ){
            stepArrayList.get(i).setPriority(i + 1);
        }
        return stepArrayList;
    }

    @Override
    public void onClickRecyclerView(final int Position, String operation) {
        switch (operation){
            case Constants.OPERATION_EDIT :
                Intent intent = new Intent(this, ActAddNewStep.class);
                intent.putExtra(Constants.CALLER, Constants.ACT_STEP_LIST);
                intent.putExtra(Constants.STEP_ATTACHED_IN_EXTRAS, true);
                this.startActivity(intent);
                break;

            case Constants.OPERATION_DELETE :
                AlertDialog.Builder alertLogout = new AlertDialog.Builder(this);
                alertLogout.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        stepArrayList.get(Position).delete();
                        Logger.showMsg(ActStepList.this, Constants.MSG_STEP_DELETED);
//                        getStepArrayFromLocal();
//                        mAdapter.notifyDataSetChanged();
                        mAdapter.notifyItemRemoved(Position);
                    }
                });
                alertLogout.setNegativeButton("Cancel", null);
                alertLogout.setMessage(Constants.MSG_DELETE_STEP);
                alertLogout.show();
                break;
        }
    }
}