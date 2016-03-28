package com.e_overhaul.android.stats.api;

import android.net.Uri;
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

import com.e_overhaul.android.stats.util.StatsParser;

/**
 * Created by dcox on 3/1/16.
 */
public class FetchSpecificStatDataTask extends AsyncTask<String, Void, List<String>>{

    private ArrayAdapter<String> _mStatListAdapter;
    private static String _mStat;
    final String STAT_PARAM = "stat";
    final String DAYS_PARAM = "days";

    public FetchSpecificStatDataTask(ArrayAdapter<String> statListAdapter) {
        _mStatListAdapter = statListAdapter;
    }

    public static String getCurrentStat() {
        return _mStat;
    }
    private final String LOG_TAG = FetchSpecificStatDataTask.class.getSimpleName();
    @Override
    protected List<String> doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jsonResult = null;
        String stat = params[0];
        _mStat = stat;
        int daysToLookBack = 12;
        String apiURI = "https://www.e-overhaul.com/stats/api/statHistory.php?";

        try {
            // Construct the URL for the stats query
            Uri builtUri = Uri.parse(apiURI).buildUpon()
                    .appendQueryParameter(STAT_PARAM, params[0])
                    .appendQueryParameter(DAYS_PARAM, Integer.toString(daysToLookBack))
                    .build();
            URL url = new URL(builtUri.toString());


            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            Log.v(LOG_TAG, "Got get URL - " + builtUri.toString());

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
            return StatsParser.getStatValues(jsonResult);

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
        try {
            _mStatListAdapter.clear();
            if(!strings.isEmpty()) {
                Log.v(LOG_TAG, "onPostExecute");
                for(int i=0; i<strings.size(); i++){
                    _mStatListAdapter.add(strings.get(i));
                }
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error ", e);
        }
        super.onPostExecute(strings);
    }
}
