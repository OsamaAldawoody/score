package com.example.osamaaldawoody.score;


import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.osamaaldawoody.score.data.appointmentContract.appointmentEntery;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    FloatingActionButton fab;

    Toolbar toolbar;

    ListView listView;

    private static final int appointment_loader = 0;

    appointmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(R.string.app_name);

//       toolbar = (Toolbar) findViewById(R.id.toolbar);
//       toolbar.setTitle("today appointment");


        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,editAppointment.class);
                startActivity(i);
            }
        });


        listView = (ListView) findViewById(R.id.list);


        //Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);

        adapter = new appointmentAdapter(this,null);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this,editAppointment.class);

                Uri currentUri = ContentUris.withAppendedId(appointmentEntery.CONTENT_URI,id);

                i.setData(currentUri);

                startActivity(i);
            }
        });
        //kick off loader
        getSupportLoaderManager().initLoader(appointment_loader,null,this);
    }


    private void deleteAllAppointments(){

        int row = getContentResolver().delete(appointmentEntery.CONTENT_URI,null,null);
    }
   @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                appointmentEntery._ID,
                appointmentEntery.COLUMN_APPOINTMENT_NAME,
                appointmentEntery.COLUMN_APPOINTMENT_DAY,
                appointmentEntery.COLUMN_APPOINTMENT_DATE,
                appointmentEntery.COLUMN_APPOINTMENT_time,
                appointmentEntery.COLUMN_APPOINTMENT_FIXED
        };

        return  new android.support.v4.content.CursorLoader(this,
                appointmentEntery.CONTENT_URI,   // The content URI of the words table
                projection,             // The columns to return for each row
                null,                   // Selection criteria
                null,                   // Selection criteria
                null);                  // The sort order for the returned rows


    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }



    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.action_delete_all_entries:
                deleteAllAppointments();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
