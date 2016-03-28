package com.e_overhaul.android.stats;

import java.util.List;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.e_overhaul.android.stats.api.FetchAvailableStatsTask;
import com.e_overhaul.android.stats.api.UpdateWorkoutRoutine;
import com.e_overhaul.android.stats.util.DataCache;

/**
 * A placeholder fragment containing a simple view.
 */
public class DayDetailFragment extends android.support.v4.app.Fragment {

    private String mCurrentDay;
    public DayDetailFragment () {
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_day_detail, container, false);
        Intent intent = getActivity().getIntent();
        if(intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            mCurrentDay = intent.getStringExtra(Intent.EXTRA_TEXT);
            ((TextView) rootView.findViewById(R.id.dayDetailsTextView)).setText(mCurrentDay);
            RelativeLayout rl = (RelativeLayout)rootView.findViewById(R.id.dayDetailLayout);
            int currentID = R.id.dayDetailsTextView;
            List<String> availableStats = DataCache.getInstance().mAvailableStats;
            List<WorkoutRoutine> currentWorkoutRoutines = DataCache.getInstance().mWorkoutRoutines;
            for(int i=0; i<availableStats.size(); i++) {
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp.addRule(RelativeLayout.BELOW, currentID);
                LayoutInflater checkboxInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                CheckBox cb = (CheckBox) checkboxInflater.inflate(R.layout.checkbox_day_list, null);
                cb.setId(2131 + i);
                cb.setText(availableStats.get(i));
                cb.setLayoutParams(lp);
                for(int j=0; j<currentWorkoutRoutines.size(); j++) {
                    WorkoutRoutine wr = currentWorkoutRoutines.get(j);
                    if(wr._dayOfWeek.equals(mCurrentDay) && wr._workoutName.equals(availableStats.get(i))) {
                        cb.setChecked(true);
                    }
                }
                cb.setOnCheckedChangeListener(
                    new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged (CompoundButton buttonView, boolean isChecked) {
                            UpdateWorkoutRoutine upwr = new UpdateWorkoutRoutine(rootView);
                            if (isChecked) {
                                upwr.execute(buttonView.getText().toString(), mCurrentDay, "on");
                            } else {
                                upwr.execute(buttonView.getText().toString(), mCurrentDay, "off");
                            }
                        }
                    });
                rl.addView(cb);
                currentID = 2131+i;
            }
        }

        return rootView;
    }
}
