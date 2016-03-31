package com.greylabs.ydo.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by Jaybhay Vijay on 7/21/2015.
 */
public abstract class AysncTaskWithProgressBar extends AsyncTask<Object,Void,Void>{
    public final static int SUCCESS=1;
    public final static int FAILURE=0;

    protected ProgressDialog progressDialog;
    protected Object object;
    protected Context context;
    protected String message;

    public AysncTaskWithProgressBar(Context context,Object object,String message){
        this.context=context;
        this.object=object;
        this.message=message;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog=new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    @Override
    protected Void doInBackground(Object... objects) {
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progressDialog.dismiss();
    }
}
