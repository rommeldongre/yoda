package com.greylabs.ydo.apis;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.greylabs.ydo.R;
import com.greylabs.ydo.models.Goal;
import com.greylabs.ydo.models.PendingStep;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jaybhay Vijay on 8/14/2015.
 */
public abstract class TaskAccount implements AdapterView.OnItemClickListener{

    protected Context context;
    protected List<String> users;
    protected Dialog dialog;
    public TaskAccount(Context context){
        this.context=context;
    }
    public abstract void authenticate();
    public abstract Object getService();
    public abstract Object buildGoal(Goal goal);
    public abstract Object buildPendingStep(PendingStep pendingStep);
    public abstract Goal convertToGoal(Object taskListObject);
    public abstract PendingStep convertToPendingStep(Object taskObject);
    public Account[] getAccounts(Context context,String accountType) {
        AccountManager accountManager = AccountManager.get(context);
        return accountManager.getAccountsByType(accountType);
    }
    public void chooseAccountDialog(String accounttype,String message , AdapterView.OnItemClickListener listener,
                                    DialogInterface.OnDismissListener dismissListener
                                    ){
         users = new ArrayList();
        Account[] accounts =getAccounts(context,accounttype);
        users.clear();
        for (Account account : accounts) {
            users.add(account.name);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(message);


        ListView lv = new ListView(context);
        lv.setSelector(context.getResources().getDrawable(R.drawable.selector_radio_btn));

        ArrayAdapter<String> adapter = new ArrayAdapter<>
                (context, android.R.layout.simple_list_item_1, android.R.id.text1,
                        users);

        lv.setAdapter(adapter);
        lv.setOnItemClickListener(listener);
        builder.setView(lv);
        dialog = builder.create();
        dialog.setOnDismissListener(dismissListener);
        dialog.show();
    }
    public void dismissChooseAccountDialog() {
        dialog.dismiss();
    }
}
