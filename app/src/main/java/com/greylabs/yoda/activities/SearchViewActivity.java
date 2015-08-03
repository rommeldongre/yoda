package com.greylabs.yoda.activities;

import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.SearchView;
import android.widget.TextView;

import com.greylabs.yoda.R;

public class SearchViewActivity extends Activity {

    private TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);

        resultText = (TextView)findViewById(R.id.searchViewResult);

        setupSearchView();
    }

    private void setupSearchView() {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) findViewById(R.id.searchView);
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
        searchView.setSearchableInfo(searchableInfo);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (ContactsContract.Intents.SEARCH_SUGGESTION_CLICKED.equals(intent.getAction())) {
            //handles suggestion clicked query
            String displayName = getDisplayNameForContact(intent);
            resultText.setText(displayName);
        } else if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            // handles a search query
            String query = intent.getStringExtra(SearchManager.QUERY);
            resultText.setText("should search for query: '" + query + "'...");
        }
    }

    private String getDisplayNameForContact(Intent intent) {
//        Cursor phoneCursor = getContentResolver().query(intent.getData(), null, null, null, null);
////        Cursor phoneCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
////                new String[]{ContactsContract.CommonDataKinds.Email.DATA},
////                null, null, null);
//        phoneCursor.moveToFirst();
//        int idDisplayName = phoneCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
//        int idMailID = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);
//        String name = phoneCursor.getString(idDisplayName);
////        String mailID = phoneCursor.getString(idMailID);
//        phoneCursor.close();
//        return name;

        String emailIdOfContact = null;
//        int emailType = ContactsContract.CommonDataKinds.Email.TYPE_WORK;
//        String contactName = null;

        ContentResolver cr = getContentResolver();
        Cursor phoneCursor = cr.query(intent.getData(), null, null, null, null);
        phoneCursor.moveToFirst();

//        String name = phoneCursor
//                .getString(phoneCursor
//                        .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

        String id = phoneCursor.getString(phoneCursor
                .getColumnIndex(BaseColumns._ID));
        phoneCursor.close();

        Cursor emailsCursor = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + id, null, null);
        if(emailsCursor.getCount()>0){
            emailsCursor.moveToFirst();
            emailIdOfContact = emailsCursor.getString(emailsCursor
                    .getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));

//        emailType = emailsCursor.getInt(emailsCursor
//                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
        }
        emailsCursor.close();
        return emailIdOfContact;
    }
}
