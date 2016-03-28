package com.e_overhaul.android.stats.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.e_overhaul.android.stats.WorkoutRoutine;

/**
 * Created by dcox on 3/2/16.
 */
public class StatsParser {
    private static final String LOG_TAG = StatsParser.class.getSimpleName();
    public static List<String> getStatValues(String statJsonStr) throws JSONException {
        List<String> retList = new ArrayList<String>();
        JSONObject stats = new JSONObject(statJsonStr);
        JSONArray statAry = stats.getJSONArray("stats");
        for(int i=0; i<statAry.length(); i++) {
            JSONObject statObj = statAry.getJSONObject(i);
            String dater = statObj.getString("date");
            Integer value = statObj.getInt("value");
            retList.add(dater + " - " + value);
        }
        Log.v(LOG_TAG, "Returning array of size: " + retList.size());
        return retList;
    }

    public static List<String> getStatNames(String statJsonStr) throws JSONException {
        List<String> retList = new ArrayList<String>();
        JSONObject stats = new JSONObject(statJsonStr);
        JSONArray statAry = stats.getJSONArray("stats");
        Log.v(LOG_TAG, "statAry Size is : " + statAry.length());
        for(int i=0; i<statAry.length(); i++) {
            String statName = (String)statAry.get(i);
//            Log.v(LOG_TAG, "Stat Name: " + statName);
            retList.add(statName);
        }
        Log.v(LOG_TAG, "Returning array of size: " + retList.size());
        return retList;
    }

    public static List<WorkoutRoutine> getWorkoutRoutines(String jsonStr) throws JSONException {
        List<WorkoutRoutine> retVal = new ArrayList<WorkoutRoutine>();
        JSONObject routines = new JSONObject(jsonStr);
        JSONArray routinesArray = routines.getJSONArray("routine");
        for(int i=0; i<routinesArray.length(); i++) {
            JSONObject jObj = routinesArray.getJSONObject(i);
            retVal.add(new WorkoutRoutine(jObj.getString("workout"), jObj.getString("day_of_week")));
            Log.v(LOG_TAG, "Adding WorkoutRoutine " + jObj.getString("workout") + " : " + jObj.getString("day_of_week"));
        }
        Log.v(LOG_TAG, "Returning array of size: " + retVal.size());
        return retVal;
    }

    public static boolean isRequestSuccessful(String jsonStr) throws JSONException {
        JSONObject returnVal = new JSONObject(jsonStr);
        if(returnVal.has("error")) {
            return false;
        }
        return true;
    }

}
