package com.e_overhaul.android.stats;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.support.v7.widget.ShareActionProvider;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.e_overhaul.android.stats.api.FetchSpecificStatDataTask;
import com.e_overhaul.android.stats.api.FetchStatImage;
import com.e_overhaul.android.stats.util.DataCache;

public class DetailActivity extends ActionBarActivity {
    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    public void onHeaderClick(View v) {
        Toast.makeText(this.getApplicationContext(), DataCache.getInstance().mCurrentStat, Toast.LENGTH_SHORT).show();
        Log.v(LOG_TAG, "Show Workout Details");
        Intent intent = new Intent(this, WorkoutDetail.class).putExtra(Intent.EXTRA_TEXT, DataCache.getInstance().mCurrentStat);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class DetailFragment extends Fragment {
        private static final String LOG_TAG = DetailFragment.class.getSimpleName();
        public ArrayAdapter<String> mStatsDetailAdapter = null;

        private String mStatString;

        public DetailFragment() {
            setHasOptionsMenu(false);
        }
        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.detailfragment, menu);
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Intent intent = getActivity().getIntent();
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            mStatsDetailAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_stat_details, R.id.list_stat_details_textview);
            ListView lv = (ListView)  rootView.findViewById(R.id.statListView);
            if(lv == null) {
                Log.e(LOG_TAG, "ListView is null");
            } else {
                lv.setAdapter(mStatsDetailAdapter);
            }
            if(intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
                mStatString = intent.getStringExtra(Intent.EXTRA_TEXT);
                ((TextView) rootView.findViewById(R.id.detail_text)).setText(mStatString);
                ImageView iv = (ImageView) rootView.findViewById(R.id.detailImageView);
                if(iv == null) {
                    Log.e(LOG_TAG, "Image View is null");
                }
                FetchStatImage fsi = new FetchStatImage(iv, getContext());
                fsi.execute(mStatString);
                FetchSpecificStatDataTask fssdt = new FetchSpecificStatDataTask(mStatsDetailAdapter);
                fssdt.execute(mStatString);
            }
            return rootView;
        }

        public void onHeaderClick(View v) {
            Toast.makeText(getContext(), mStatString, 5);
        }
    }
}
