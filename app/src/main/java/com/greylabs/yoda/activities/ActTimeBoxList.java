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
import com.greylabs.yoda.adapters.AdapterRecyclerViewActTimeBoxList;
import com.greylabs.yoda.adapters.DragSortRecycler;
import com.greylabs.yoda.interfaces.OnClickOfRecyclerViewActGoalList;
import com.greylabs.yoda.interfaces.OnClickOfRecyclerViewActTimeboxList;
import com.greylabs.yoda.models.TimeBox;
import com.greylabs.yoda.utils.Constants;
import com.greylabs.yoda.utils.Logger;

import java.util.ArrayList;

public class ActTimeBoxList extends ActionBarActivity implements OnClickOfRecyclerViewActTimeboxList {

    private Toolbar toolbar;
    TextView emptyViewActTimeBoxList;
    ArrayList<TimeBox>  timeBoxArrayList = new ArrayList<>();
    boolean isOperationEdit = false;
    Menu menu;

    RecyclerView recyclerView;
    AdapterRecyclerViewActTimeBoxList mAdapter;
    LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timebox_list);
        initialize();
    }

    private void initialize() {
        toolbar = (Toolbar) findViewById(R.id.toolBarActTimeBoxList);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.titleActTimeBoxList));

        emptyViewActTimeBoxList = (TextView) findViewById(R.id.tvEmptyViewActTimeBoxList);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerViewActTimeBoxList);
        recyclerView.setHasFixedSize(true);
        getTimeBoxArrayFromLocal();
        mAdapter = new AdapterRecyclerViewActTimeBoxList(this, timeBoxArrayList, isOperationEdit);
        recyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(null);

//        DragSortRecycler dragSortRecycler = new DragSortRecycler();
//        dragSortRecycler.setViewHandleId(R.id.btnHandleRecyclerItemActGo);
//        dragSortRecycler.setFloatingAlpha(0.8F);
//        dragSortRecycler.setFloatingBgColor(getResources().getColor(R.color.ColorPrimary));
//        setAutoScrollSpeed(float)
//        How fast it auto scrolls when you get to the top or bottom of the screen. Should acheive similar results across devices.
//
//        setAutoScrollWindow(float)
//        Sets where it starts to autoscroll, this is a fraction of the total height of the RecyclerView. So a value of 0.1 will mean that it will start scrolling at the bottom 10% and top 90% of the view.
//        dragSortRecycler.setOnItemMovedListener(new DragSortRecycler.OnItemMovedListener() {
//            @Override
//            public void onItemMoved(int from, int to) {
//                if (from != to)
//                    timeBoxArrayList.add(to, timeBoxArrayList.remove(from));
//                mAdapter.notifyDataSetChanged();
//            }
//        });
//        recyclerView.addItemDecoration(dragSortRecycler);
//        recyclerView.addOnItemTouchListener(dragSortRecycler);
//        recyclerView.setOnScrollListener(dragSortRecycler.getScrollListener());
    }

    private void getTimeBoxArrayFromLocal() {
        timeBoxArrayList.clear();
        TimeBox timeBox = new TimeBox(this);
        if(timeBox.getAll() != null)
            timeBoxArrayList.addAll(timeBox.getAll());
        checkForEmptyViewVisibility();
    }

    private void checkForEmptyViewVisibility() {
        if (timeBoxArrayList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyViewActTimeBoxList.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyViewActTimeBoxList.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_act_timebox_list, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                if(isOperationEdit){
                    menu.findItem(R.id.actionEditActTimeBoxList).setVisible(true);
                    menu.findItem(R.id.actionAddActTimeBoxList).setVisible(true);
                    isOperationEdit = false;
                    mAdapter = new AdapterRecyclerViewActTimeBoxList(this, timeBoxArrayList, isOperationEdit);
                    recyclerView.setAdapter(mAdapter);
                } else{
                    this.finish();
                }
                break;
            case R.id.actionEditActTimeBoxList :
                menu.findItem(R.id.actionEditActTimeBoxList).setVisible(false);
                menu.findItem(R.id.actionAddActTimeBoxList).setVisible(false);
                isOperationEdit = true;
                mAdapter = new AdapterRecyclerViewActTimeBoxList(this, timeBoxArrayList, isOperationEdit);
                recyclerView.setAdapter(mAdapter);
                break;
            case R.id.actionAddActTimeBoxList :
                startActivity(new Intent(this, ActAddTimeBox.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClickRecyclerView(final int Position, String operation) {
        if(isOperationEdit){
            switch (operation){
                case Constants.OPERATION_EDIT :
                    Intent intent = new Intent(this, ActAddTimeBox.class);
                    intent.putExtra(Constants.TIMEBOX_OBJECT, timeBoxArrayList.get(Position));
                    startActivity(intent);
                    break;

                case Constants.OPERATION_DELETE :
                    AlertDialog.Builder alertLogout = new AlertDialog.Builder(this);
                    alertLogout.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            timeBoxArrayList.get(Position).delete();
                            Logger.showMsg(ActTimeBoxList.this, Constants.MSG_TIMEBOX_DELETED);
                            getTimeBoxArrayFromLocal();
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                    alertLogout.setNegativeButton("Cancel", null);
                    alertLogout.setMessage(Constants.MSG_DELETE_TIMEBOX);
                    alertLogout.show();
                    break;
            }
        }
    }
}