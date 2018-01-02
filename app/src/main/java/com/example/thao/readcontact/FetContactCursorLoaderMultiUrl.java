package com.example.thao.readcontact;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by luu.phuong.thao on 10/10/2016.
 */

public class FetContactCursorLoaderMultiUrl  extends Activity implements
        SearchView.OnQueryTextListener, LoaderManager.LoaderCallbacks<Cursor> {
    public TextView outputText;
    ListView listView;
    ArrayList<Contacts> contactsList;
    ContactAdapter adapter;


    SimpleCursorAdapter simpleCursorAdapter;
    String cursorFilter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //outputText = (TextView) findViewById(R.id.textView1);
        listView = (ListView)findViewById(R.id.listView);
        contactsList = new ArrayList<>();
        adapter = new ContactAdapter(contactsList,getApplicationContext());

        String[] from = new String[]{
                ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,
                ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME,
                ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID};


        int[] to = new int[]{
                R.id.name,
                R.id.phone,
                R.id.email,
                R.id.id};

        simpleCursorAdapter = new SimpleCursorAdapter(this,
                R.layout.contact_item,
                null,
                from,
                to,
                0);

        listView.setAdapter(simpleCursorAdapter);
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(0, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.activity_main, menu);
        MenuItem item = menu.add("Search");
        item.setIcon(android.R.drawable.ic_menu_search);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        SearchView searchView = new SearchView(FetContactCursorLoaderMultiUrl.this);
        searchView.setOnQueryTextListener(this);
        item.setActionView(searchView);

        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
        Log.d("thao","onCreateLoader");

        String select = ContactsContract.Data.MIMETYPE + " = ?";
        String[] condition = new String[]{ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE};
        String[] projection = new String[]{ContactsContract.CommonDataKinds.StructuredName._ID,
        ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID,
        ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
        ContactsContract.CommonDataKinds.StructuredName.FULL_NAME_STYLE,
        ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,
        ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME,
        ContactsContract.CommonDataKinds.StructuredName.PREFIX,
        ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME,
        ContactsContract.CommonDataKinds.StructuredName.SUFFIX,
        ContactsContract.CommonDataKinds.StructuredName.PHONETIC_GIVEN_NAME,
        ContactsContract.CommonDataKinds.StructuredName.PHONETIC_NAME_STYLE,
        ContactsContract.CommonDataKinds.StructuredName.PHONETIC_MIDDLE_NAME,
        ContactsContract.CommonDataKinds.StructuredName.PHONETIC_FAMILY_NAME,
        };
            CursorLoader cursorLoader = new CursorLoader(
                    this,
                    ContactsContract.Data.CONTENT_URI,
                    projection,
                    select,
                    condition,
                    ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME
            );
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> lo, Cursor arg1) {
        simpleCursorAdapter.swapCursor(arg1);
        Log.d("thao","onLoader finnish");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {
        simpleCursorAdapter.swapCursor(null);
    }

    @Override
    public boolean onQueryTextChange(String arg0) {
        cursorFilter = !TextUtils.isEmpty(arg0) ? arg0 : null;
        getLoaderManager().restartLoader(0, null, this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String arg0) {
        // TODO Auto-generated method stub
        return false;
    }

}

