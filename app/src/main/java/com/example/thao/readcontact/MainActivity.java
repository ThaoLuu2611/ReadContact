package com.example.thao.readcontact;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    ArrayList<DataModel> dataModels;
    ListView listView;
    private static CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

    }

    public void onShowCustomAdapter(View v){
        startActivity(new Intent(this,FreeStyleListView.class));
    }
    public void onShowContactAdapter(View v){
        startActivity(new Intent(this, FetContact.class));
    }
    public void onShowContactCursorloaderAdapter(View v){
        startActivity(new Intent(this,FetContactCursorLoader.class));
    }
    public void onShowContactCursorProjection(View v){
        startActivity(new Intent(this,LoadContactProjection.class));
    }
    public void onShowContactMultiUrl(View v){
        startActivity(new Intent(this, FetContactCursorLoaderMultiUrl.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Log.d("thao","option selected id = "+id);

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }
}
