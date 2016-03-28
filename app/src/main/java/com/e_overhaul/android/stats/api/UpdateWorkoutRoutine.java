package com.e_overhaul.android.stats.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.e_overhaul.android.stats.WorkoutRoutine;
import com.e_overhaul.android.stats.util.DataCache;
import com.e_overhaul.android.stats.util.StatsParser;

/**
 * Created by dcox on 3/1/16.
 */
public class UpdateWorkoutRoutine extends AsyncTask<String, Void, Void>{

    private View mView;
    public UpdateWorkoutRoutine (View view) {
        mView = view;
    }

    private final String LOG_TAG = UpdateWorkoutRoutine.class.getSimpleName();

    public static boolean mWasSuccessful = false;
    @Override
    protected Void doInBackground (String... params) {
        String routineName, dayOfWeek, onOrOff;
        routineName = params[0];
        dayOfWeek = params[1];
        onOrOff = params[2];
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jsonResult = null;
        final String WORKOUT_NAME_PARAM="workout";
        final String WORKOUT_DAY_PARAM="dayOfWeek";
        final String WORKOUT_STATUS_PARAM="active";

        try {
            String imageURI = "https://www.e-overhaul.com/stats/api/updateWorkoutRoutine.php?";
            // Construct the URL for the stats query
            Uri builtUri = Uri.parse(imageURI).buildUpon()
                              .appendQueryParameter(WORKOUT_NAME_PARAM, routineName)
                              .appendQueryParameter(WORKOUT_DAY_PARAM, dayOfWeek)
                              .appendQueryParameter(WORKOUT_STATUS_PARAM, onOrOff)
                    .build();
            Log.v(LOG_TAG, "Got get URL - " + builtUri.toString());
            // Construct the URL for the stats query
            URL url = new URL(builtUri.toString());
            // Create the request to /api/listRoutines.php
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

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
            mWasSuccessful = StatsParser.isRequestSuccessful(jsonResult);
            if(mWasSuccessful) {
                if(onOrOff.toLowerCase().equals("on")) {
                    DataCache.getInstance().mWorkoutRoutines.add(new WorkoutRoutine(routineName, dayOfWeek));
                } else {
                    List<WorkoutRoutine> currentWorkouts = DataCache.getInstance().mWorkoutRoutines;
                    List<WorkoutRoutine> newWorkouts = new ArrayList<WorkoutRoutine>();
                    for(int k=0; k<currentWorkouts.size(); k++) {
                        WorkoutRoutine wr = currentWorkouts.get(k);
                        if(wr._workoutName.equals(routineName) && wr._dayOfWeek.equals(dayOfWeek)) {

                        } else {
                            newWorkouts.add(wr);
                        }
                    }
                    DataCache.getInstance().mWorkoutRoutines = newWorkouts;
                }
            }

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
        return null;
    }

    @Override
    protected void onPostExecute(Void d) {
        Log.v(LOG_TAG, "In onPostExecute about to set adapter");
        if(mView != null) {
            Toast.makeText(mView.getContext(), "Post to update workout routine: " + mWasSuccessful, Toast.LENGTH_SHORT).show();
        }
        super.onPostExecute(d);
    }

}
