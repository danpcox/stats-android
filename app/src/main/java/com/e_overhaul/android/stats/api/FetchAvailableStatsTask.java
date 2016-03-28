package com.e_overhaul.android.stats.api;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.e_overhaul.android.stats.util.DataCache;
import com.e_overhaul.android.stats.util.StatsParser;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by dcox on 3/1/16.
 */
public class FetchAvailableStatsTask extends AsyncTask<String, Void, List<String>>{

    private ArrayAdapter<String> _mStatListAdapter;
    private static List<String> _mAvailableStats;

    public static void resetList() {
        Log.v(LOG_TAG, "Resetting list son!");
        _mAvailableStats.clear();
    }


    public FetchAvailableStatsTask(ArrayAdapter<String> statListAdapter) {
        _mStatListAdapter = statListAdapter;
    }

    private static final String LOG_TAG = FetchAvailableStatsTask.class.getSimpleName();
    @Override
    protected List<String> doInBackground(String... params) {

        if(_mAvailableStats != null && !_mAvailableStats.isEmpty()) {
            Log.v(LOG_TAG, "Returning cached stats list");
            return _mAvailableStats;
        } else {
            Log.v(LOG_TAG, "_mAvailableStats is null, go get data");
        }
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jsonResult = null;
        int daysToLookBack = 12;
        String apiURI = "https://www.e-overhaul.com/stats/api/listStats.php";

        try {
            // Construct the URL for the stats query
            URL url = new URL(apiURI);


            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            Log.v(LOG_TAG, "Got get URL - " + apiURI);

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line = null;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            jsonResult = buffer.toString();
            Log.v(LOG_TAG, "jsonResult is - " + jsonResult);
            _mAvailableStats = StatsParser.getStatNames(jsonResult);
            DataCache.getInstance().mAvailableStats = _mAvailableStats;
            return _mAvailableStats;

        } catch (Exception e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }

        }
    }

    @Override
    protected void onPostExecute(List<String> strings) {
        Log.v(LOG_TAG, "In onPostExecute about to set adapter");
        _mStatListAdapter.clear();
        if(!strings.isEmpty()) {
            for(int i=0; i<strings.size(); i++){
                _mStatListAdapter.add(strings.get(i));
            }
        }
        super.onPostExecute(strings);
    }
}
