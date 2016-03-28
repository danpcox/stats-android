package com.e_overhaul.android.stats.util;

import java.util.List;

import com.e_overhaul.android.stats.WorkoutRoutine;

/**
 * Created by dcox on 3/16/16.
 */


public class DataCache {
    private static DataCache instance = null;
    public String mCurrentStat;
    public List<String> mAvailableStats = null;
    public List<WorkoutRoutine> mWorkoutRoutines = null;
    protected DataCache() {
        // Exists only to defeat instantiation.
    }
    public static DataCache getInstance() {
        if(instance == null) {
            instance = new DataCache();
        }
        return instance;
    }
}