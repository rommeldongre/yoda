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
import com.greylabs.yoda.adapters.RecyclerViewActChangeStepPriority;
import com.greylabs.yoda.interfaces.OnClickOfRecyclerViewActChangeStepPriority;
import com.greylabs.yoda.models.Goal;
import com.greylabs.yoda.models.PendingStep;
import com.greylabs.yoda.utils.Constants;
import com.greylabs.yoda.utils.Logger;

import java.util.ArrayList;


public class ActChangeStepPriority extends ActionBarActivity implements OnClickOfRecyclerViewActChangeStepPriority {

    private Toolbar toolbar;
    TextView emptyViewActChangeStepPriority;
    ArrayList<PendingStep> stepArrayList;
    Goal currentGoal;

    RecyclerView recyclerView;
    RecyclerViewActChangeStepPriority mAdapter;
    LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_step_priority);

//        if(IS_DRAFT){
//            currentAssetForm  = (Form)i.getSerializableExtra("FORM");
//            currentAFO = (AssetFormObject) i.getSerializableExtra("ASSET_FORM_OBJECT");
//            if(currentAssetForm.getFORM_FIELD_JSON() != null){
//                newJSONArrayFromString  = CombineJSONArrays.combine(currentAssetForm.getFORM_FIELD_JSON(), currentAFO.getASSET_FORM_OBJECT_FIELD_VALUE_JSON());
//                widgetArrayList = JSONtoWidgetObjects.getWidgetObjectsArray(newJSONArrayFromString);
//            }
//        }else {
//            currentAssetForm  = (Form)i.getSerializableExtra("FORM");
//            try {
//                newJSONArrayFromString  = new JSONArray(currentAssetForm.getFORM_FIELD_JSON());
//                Utilities.sysout(""+newJSONArrayFromString);                            //******************to be deleted
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            widgetArrayList = JSONtoWidgetObjects.getWidgetObjectsArray(newJSONArrayFromString);
//        }
        initialize();
    }

    private void initialize() {
        stepArrayList = new ArrayList<PendingStep>();
        Intent i = getIntent();
        currentGoal = (Goal) i.getExtras().getSerializable(Constants.GOAL_OBJECT);

        toolbar = (Toolbar) findViewById(R.id.toolBarActChangeStepPriority);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.titleActChangeStepPriority));

        emptyViewActChangeStepPriority = (TextView) findViewById(R.id.tvEmptyViewActChangeStepPriority);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerActChangeStepPriority);
        recyclerView.setHasFixedSize(true);
        getStepArrayFromLocal();
        mAdapter = new RecyclerViewActChangeStepPriority(this, stepArrayList);
        recyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(null);

        DragSortRecycler dragSortRecycler = new DragSortRecycler();
        dragSortRecycler.setViewHandleId(R.id.tvStepNameRecyclerItemActChangeStepPriority);
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
//                Logger.showMsg(ActChangeStepPriority.this, "onItemMoved " + from + " to " + to);
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
        if(pendingStep.getAll(currentGoal.getId()) != null)
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                Intent intent1 = new Intent();
                intent1.putExtra(Constants.PRIORITY_CHANGED, false);
                setResult(1, intent1);
                this.finish();
                break;
            case R.id.actionSavePriorityActChangeStepPriority :
                Intent intent2 = new Intent();
                intent2.putExtra(Constants.PRIORITY_CHANGED, true);
                intent2.putExtra(Constants.STEPS_ARRAY_LIST_WITH_NEW_PRIORITIES, stepArrayList);
                setResult(1, intent2);
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClickRecyclerView(final int Position, String operation) {
//        int position = po
        switch (operation){
            case Constants.OPERATION_EDIT :
                break;

            case Constants.OPERATION_DELETE :
                AlertDialog.Builder alertLogout = new AlertDialog.Builder(this);
                alertLogout.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        stepArrayList.get(Position).delete(stepArrayList.get(Position).getId());
                        Logger.showMsg(ActChangeStepPriority.this, Constants.MSG_STEP_DELETED);
                        getStepArrayFromLocal();
                        mAdapter.notifyDataSetChanged();
                    }
                });
                alertLogout.setNegativeButton("Cancel", null);
                alertLogout.setMessage(Constants.MSG_DELETE_STEP);
                alertLogout.show();
                break;
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        // check if the request code is same as what is passed  here it is 2
//        if(resultCode == RESULT_CANCELED){
//
//        }else if(requestCode==2)
//        {
//            if(data.getStringExtra("clickedbutton").equals("done")){
//                widgetArrayList.clear();
//                widgetArrayList.addAll((ArrayList<Widget>) data.getSerializableExtra("widgetarraylist"));
//                mAdapter.notifyDataSetChanged();
//            }
//        }
//    }

}