package com.e_overhaul.android.stats;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.e_overhaul.android.stats.api.FetchAvailableStatsTask;
import com.e_overhaul.android.stats.api.FetchWorkoutRoutinesTask;
import com.e_overhaul.android.stats.util.DataCache;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    public ArrayAdapter<String> mAvailableStatsAdapter = null;
    private FetchAvailableStatsTask _FAST;
    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        Log.v(LOG_TAG, "onCreateView");


        mAvailableStatsAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_stats, R.id.list_item_forecast_textview);
        ListView lv = (ListView)  rootView.findViewById(R.id.listview_forecast);
        lv.setAdapter(mAvailableStatsAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String currentStat = mAvailableStatsAdapter.getItem(position);
                DataCache.getInstance().mCurrentStat = currentStat;
                Intent intent = new Intent(getActivity(), DetailActivity.class).putExtra(Intent.EXTRA_TEXT, currentStat);
                startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onStart() {
        Log.v(LOG_TAG, "onStart");
        FetchAvailableStatsTask fast = new FetchAvailableStatsTask(mAvailableStatsAdapter);
        fast.execute();
        FetchWorkoutRoutinesTask fwrt = new FetchWorkoutRoutinesTask();
        fwrt.execute();
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.v(LOG_TAG, "onResume");
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.refresh_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_refresh) {
            FetchAvailableStatsTask fast = new FetchAvailableStatsTask(mAvailableStatsAdapter);
            fast.execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

