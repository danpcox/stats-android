package com.e_overhaul.android.stats;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.e_overhaul.android.stats.api.FetchAvailableStatsTask;
import com.e_overhaul.android.stats.api.FetchWorkoutRoutinesTask;

public class MainActivity extends AppCompatActivity {
    private final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Log.v(LOG_TAG, "opOptionsItemSelected(" + id + ")");

        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.action_stat_list) {
            Intent intent = new Intent(this, StatListActivity.class);
            startActivity(intent);
        }
        if(id == R.id.action_days) {
            Toast.makeText(getApplicationContext(), "Fire Days Intent", Toast.LENGTH_SHORT).show();
            Log.v(LOG_TAG, "Open up days activity");
            Intent intent = new Intent(this, DayList.class);
            startActivity(intent);
        }
        if(id == R.id.action_refresh) {
            ArrayAdapter<String> mAvailableStatsAdapter = new ArrayAdapter<String>(this, R.layout.list_item_stats, R.id.list_item_forecast_textview);
            FetchAvailableStatsTask.resetList();
            FetchAvailableStatsTask fast = new FetchAvailableStatsTask(mAvailableStatsAdapter);
            fast.execute();
            FetchWorkoutRoutinesTask.resetWorkoutRoutineList();
            FetchWorkoutRoutinesTask fwrt = new FetchWorkoutRoutinesTask();
            fwrt.execute();
        }
        return super.onOptionsItemSelected(item);
    }
}
