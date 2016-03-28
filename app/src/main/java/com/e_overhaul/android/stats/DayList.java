package com.e_overhaul.android.stats;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class DayList extends AppCompatActivity {
    private final String LOG_TAG = DayList.class.getSimpleName();

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_list);
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

        if(id == R.id.action_main) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
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

        return super.onOptionsItemSelected(item);
    }

    public void openSundayDayDetails(View view) {
        Intent intent = new Intent(this, DayDetail.class).putExtra(Intent.EXTRA_TEXT, "Sunday");
        startActivity(intent);
    }
    public void openMondayDayDetails(View view) {
        Intent intent = new Intent(this, DayDetail.class).putExtra(Intent.EXTRA_TEXT, "Monday");
        startActivity(intent);
    }
    public void openTuesdayDayDetails(View view) {
        Intent intent = new Intent(this, DayDetail.class).putExtra(Intent.EXTRA_TEXT, "Tuesday");
        startActivity(intent);
    }
    public void openWednesdayDayDetails(View view) {
        Intent intent = new Intent(this, DayDetail.class).putExtra(Intent.EXTRA_TEXT, "Wednesday");
        startActivity(intent);
    }
    public void openThursdayDayDetails(View view) {
        Intent intent = new Intent(this, DayDetail.class).putExtra(Intent.EXTRA_TEXT, "Thursday");
        startActivity(intent);
    }
    public void openFridayDayDetails(View view) {
        Intent intent = new Intent(this, DayDetail.class).putExtra(Intent.EXTRA_TEXT, "Friday");
        startActivity(intent);
    }
    public void openSaturdayDayDetails(View view) {
        Intent intent = new Intent(this, DayDetail.class).putExtra(Intent.EXTRA_TEXT, "Saturday");
        startActivity(intent);
    }

}
