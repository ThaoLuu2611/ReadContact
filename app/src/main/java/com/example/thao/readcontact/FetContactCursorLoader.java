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

public class FetContactCursorLoader  extends Activity implements
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
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.PHOTO_ID};


        int[] to = new int[]{
                android.R.id.text1,
                android.R.id.text2};

        simpleCursorAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2,
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
        SearchView searchView = new SearchView(FetContactCursorLoader.this);
        searchView.setOnQueryTextListener(this);
        item.setActionView(searchView);

        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
        Log.d("thao","onCreateLoader");
        Uri baseUri;
        if (cursorFilter != null) {
            Log.d("thao","onCreateLoader != null");
            baseUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI,
                    Uri.encode(cursorFilter));
            Log.d("thao","content filter uri");
        } else {
            baseUri = ContactsContract.Contacts.CONTENT_URI;
            Log.d("thao","onCreateLoader = null");
        }

        String select = "((" + ContactsContract.Contacts._ID + " NOTNULL) AND ("
                + ContactsContract.Contacts.DISPLAY_NAME + " != '' ))";

        String[] projection = new String[] {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.CONTACT_STATUS,
                ContactsContract.Contacts.CONTACT_PRESENCE,
                ContactsContract.Contacts.PHOTO_ID,
                ContactsContract.Contacts.LOOKUP_KEY,
        };

        CursorLoader cursorLoader = new CursorLoader(
                this,
                baseUri,
                projection,
                select,
                null,
                ContactsContract.Contacts.DISPLAY_NAME);

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

