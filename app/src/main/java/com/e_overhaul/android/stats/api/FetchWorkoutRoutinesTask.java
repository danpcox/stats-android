package com.e_overhaul.android.stats.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.e_overhaul.android.stats.WorkoutRoutine;
import com.e_overhaul.android.stats.util.DataCache;
import com.e_overhaul.android.stats.util.StatsParser;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by dcox on 3/1/16.
 */
public class FetchWorkoutRoutinesTask extends AsyncTask<List<String>, Void, List<WorkoutRoutine>>{

    private ArrayAdapter<String> _mStatListAdapter;
    private static List<WorkoutRoutine> _mWorkoutRoutines;

    public static List<WorkoutRoutine> getWorkoutRoutines() {
        return _mWorkoutRoutines;
    }
    public static void resetWorkoutRoutineList() {
        Log.v(LOG_TAG, "Resetting list son!");
        _mWorkoutRoutines.clear();
    }

    public FetchWorkoutRoutinesTask () {
//        _mStatListAdapter = statListAdapter;
//        _mWorkoutRoutines = new ArrayList<WorkoutRoutine>();
    }

    private static final String LOG_TAG = FetchWorkoutRoutinesTask.class.getSimpleName();

    @Override
    protected List<WorkoutRoutine> doInBackground (List<String>... params) {
        if(_mWorkoutRoutines != null && !_mWorkoutRoutines.isEmpty()) {
            Log.v(LOG_TAG, "Returning cached Workout Routines");
            return _mWorkoutRoutines;
        } else {
            Log.v(LOG_TAG, "_mWorkoutRoutines is null, go get data");
        }
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jsonResult = null;
        int daysToLookBack = 12;
        String apiURI = "https://www.e-overhaul.com/stats/api/listRoutines.php";

        try {
            // Construct the URL for the stats query
            URL url = new URL(apiURI);
            // Create the request to /api/listRoutines.php
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
            _mWorkoutRoutines = StatsParser.getWorkoutRoutines(jsonResult);
            DataCache.getInstance().mWorkoutRoutines = _mWorkoutRoutines;
            return _mWorkoutRoutines;

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
    protected void onPostExecute(List<WorkoutRoutine> data) {
        Log.v(LOG_TAG, "In onPostExecute about to set adapter");
//        _mStatListAdapter.clear();
        super.onPostExecute(data);
    }

}
