package com.e_overhaul.android.stats;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.e_overhaul.android.stats.api.FetchSpecificStatDataTask;
import com.e_overhaul.android.stats.api.FetchStatImage;
import com.e_overhaul.android.stats.api.FetchWorkoutRoutinesTask;
import com.e_overhaul.android.stats.api.UpdateWorkoutRoutine;

/**
 * A placeholder fragment containing a simple view.
 */
public class WorkoutDetailFragment extends android.support.v4.app.Fragment {

    private static final String LOG_TAG = WorkoutDetailFragment.class.getSimpleName();
    public ArrayAdapter<String> mStatsDetailAdapter = null;

    private String _mWorkoutName;

    public WorkoutDetailFragment() {
        setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_workout_detail, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        final View rootView = inflater.inflate(R.layout.fragment_workout_detail, container, false);
        FetchWorkoutRoutinesTask fwt = new FetchWorkoutRoutinesTask();
        fwt.execute();
        if(intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            _mWorkoutName = intent.getStringExtra(Intent.EXTRA_TEXT);
            ((TextView) rootView.findViewById(R.id.workoutRoutineTextView)).setText(_mWorkoutName);
            List<WorkoutRoutine> routines = FetchWorkoutRoutinesTask.getWorkoutRoutines();
            for(int i=0; i<routines.size(); i++) {
                WorkoutRoutine wr = routines.get(i);
                if(wr._workoutName.equals(_mWorkoutName)) {
                    switch (wr._dayOfWeek) {
                    case "Sunday":
                        ((CheckBox) rootView.findViewById(R.id.checkBoxSunday)).setChecked(true);
                        break;
                    case "Monday":
                        ((CheckBox) rootView.findViewById(R.id.checkBoxMonday)).setChecked(true);
                        break;
                    case "Tuesday":
                        ((CheckBox) rootView.findViewById(R.id.checkBoxTuesday)).setChecked(true);
                        break;
                    case "Wednesday":
                        ((CheckBox) rootView.findViewById(R.id.checkBoxWednesday)).setChecked(true);
                        break;
                    case "Thursday":
                        ((CheckBox) rootView.findViewById(R.id.checkBoxThursday)).setChecked(true);
                        break;
                    case "Friday":
                        ((CheckBox) rootView.findViewById(R.id.checkBoxFriday)).setChecked(true);
                        break;
                    case "Saturday":
                        ((CheckBox) rootView.findViewById(R.id.checkBoxSaturday)).setChecked(true);
                        break;
                    default:
                    }
                }
            }
            ((CheckBox) rootView.findViewById(R.id.checkBoxSunday)).setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                    {
                        UpdateWorkoutRoutine upwr = new UpdateWorkoutRoutine(rootView);
                        if(isChecked) {
                            upwr.execute(_mWorkoutName, "Sunday", "on");
                        } else {
                            upwr.execute(_mWorkoutName, "Sunday", "off");
                        }

                    }
                });
            ((CheckBox) rootView.findViewById(R.id.checkBoxMonday)).setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                    {
                        UpdateWorkoutRoutine upwr = new UpdateWorkoutRoutine(rootView);
                        if(isChecked) {
                            upwr.execute(_mWorkoutName, "Monday", "on");
                        } else {
                            upwr.execute(_mWorkoutName, "Monday", "off");
                        }

                    }
                });
            ((CheckBox) rootView.findViewById(R.id.checkBoxTuesday)).setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                    {
                        UpdateWorkoutRoutine upwr = new UpdateWorkoutRoutine(rootView);
                        if(isChecked) {
                            upwr.execute(_mWorkoutName, "Tuesday", "on");
                        } else {
                            upwr.execute(_mWorkoutName, "Tuesday", "off");
                        }

                    }
                });
            ((CheckBox) rootView.findViewById(R.id.checkBoxWednesday)).setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                    {
                        UpdateWorkoutRoutine upwr = new UpdateWorkoutRoutine(rootView);
                        if(isChecked) {
                            upwr.execute(_mWorkoutName, "Wednesday", "on");
                        } else {
                            upwr.execute(_mWorkoutName, "Wednesday", "off");
                        }

                    }
                });
            ((CheckBox) rootView.findViewById(R.id.checkBoxThursday)).setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                    {
                        UpdateWorkoutRoutine upwr = new UpdateWorkoutRoutine(rootView);
                        if(isChecked) {
                            upwr.execute(_mWorkoutName, "Thursday", "on");
                        } else {
                            upwr.execute(_mWorkoutName, "Thursday", "off");
                        }

                    }
                });
            ((CheckBox) rootView.findViewById(R.id.checkBoxFriday)).setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                    {
                        UpdateWorkoutRoutine upwr = new UpdateWorkoutRoutine(rootView);
                        if(isChecked) {
                            upwr.execute(_mWorkoutName, "Friday", "on");
                        } else {
                            upwr.execute(_mWorkoutName, "Friday", "off");
                        }

                    }
                });
            ((CheckBox) rootView.findViewById(R.id.checkBoxSaturday)).setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                    {
                        UpdateWorkoutRoutine upwr = new UpdateWorkoutRoutine(rootView);
                        if(isChecked) {
                            upwr.execute(_mWorkoutName, "Saturday", "on");
                        } else {
                            upwr.execute(_mWorkoutName, "Saturday", "off");
                        }

                    }
                });
            
        }
        return rootView;
    }
}