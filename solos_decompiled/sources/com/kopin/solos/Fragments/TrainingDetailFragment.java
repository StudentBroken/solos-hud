package com.kopin.solos.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.digits.sdk.vcard.VCardConfig;
import com.kopin.solos.MainActivity;
import com.kopin.solos.R;
import com.kopin.solos.RideControl;
import com.kopin.solos.SetupActivity;
import com.kopin.solos.ThemeActivity;
import com.kopin.solos.TrainingSegmentSummaryActivity;
import com.kopin.solos.TrainingSummaryActivity;
import com.kopin.solos.common.BaseFragment;
import com.kopin.solos.common.SportType;
import com.kopin.solos.graphics.TrainingGraph;
import com.kopin.solos.sensors.Sensor;
import com.kopin.solos.sensors.SensorList;
import com.kopin.solos.share.Config;
import com.kopin.solos.share.Platforms;
import com.kopin.solos.share.TrainingCache;
import com.kopin.solos.storage.Lap;
import com.kopin.solos.storage.SavedRides;
import com.kopin.solos.storage.SavedTraining;
import com.kopin.solos.storage.SavedTrainingWorkouts;
import com.kopin.solos.storage.SavedWorkout;
import com.kopin.solos.storage.Workout;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.MetricType;
import com.kopin.solos.storage.util.Utility;
import com.kopin.solos.views.LongTextDialog;
import java.text.DecimalFormat;
import java.util.List;

/* JADX INFO: loaded from: classes24.dex */
public class TrainingDetailFragment extends BaseFragment {
    public static final String TRAINING_ID_KEY = "training_id";
    private SavedTraining mTraining;
    private long mWorkoutId = -1;
    View.OnClickListener clickListener = new View.OnClickListener() { // from class: com.kopin.solos.Fragments.TrainingDetailFragment.1
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnDesc /* 2131952215 */:
                case R.id.btnNotes /* 2131952518 */:
                    new LongTextDialog(TrainingDetailFragment.this.getActivity()).show((String) v.getTag());
                    break;
                case R.id.btnStartWorkout /* 2131952218 */:
                    TrainingDetailFragment.this.selectTraining();
                    break;
            }
        }
    };
    ExpandableListView.OnGroupClickListener groupClickListener = new ExpandableListView.OnGroupClickListener() { // from class: com.kopin.solos.Fragments.TrainingDetailFragment.2
        @Override // android.widget.ExpandableListView.OnGroupClickListener
        public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
            TrainingDetailFragment.this.showSegmentDetails(((Integer) view.getTag()).intValue());
            return true;
        }
    };
    TrainingGraph.GraphGestureListener mTrainingGraphGestureListener = new TrainingGraph.GraphGestureListener() { // from class: com.kopin.solos.Fragments.TrainingDetailFragment.3
        @Override // com.kopin.solos.graphics.TrainingGraph.GraphGestureListener
        public void onStepClick(int stepPosition) {
            TrainingDetailFragment.this.showSegmentDetails(stepPosition);
        }

        @Override // com.kopin.solos.graphics.TrainingGraph.GraphGestureListener
        public void onFlingLeft() {
        }

        @Override // com.kopin.solos.graphics.TrainingGraph.GraphGestureListener
        public void onFlingRight() {
        }
    };

    @Override // com.kopin.solos.common.BaseFragment, android.preference.PreferenceFragment, android.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        this.mWorkoutId = args.getLong("ride_id", -1L);
        if (this.mWorkoutId != -1) {
            SportType sportType = SportType.get(args.getString(ThemeActivity.EXTRA_WORKOUT_TYPE));
            long trainingId = SavedRides.getWorkout(sportType, this.mWorkoutId).getVirtualWorkoutId();
            this.mTraining = SavedTrainingWorkouts.get(trainingId);
        } else {
            String externalId = args.getString(TrainingSummaryActivity.TRAINING_EXTERNAL_ID);
            this.mTraining = TrainingCache.get(Platforms.TrainingPeaks, externalId);
        }
    }

    @Override // com.kopin.solos.common.BaseFragment, android.preference.PreferenceFragment, android.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_training_detail, container, false);
        ((ImageView) view.findViewById(R.id.icon)).setImageResource(this.mTraining.getPrimaryMetricImageRes());
        TrainingGraph trainingGraph = (TrainingGraph) view.findViewById(R.id.graph);
        ExpandableListView listView = (ExpandableListView) view.findViewById(R.id.groupedList);
        listView.setAdapter(new StepAdapter(getActivity(), this.mTraining.getSegments()));
        if (this.mWorkoutId != -1) {
            listView.setGroupIndicator(null);
            listView.setOnGroupClickListener(this.groupClickListener);
            SavedWorkout savedWorkout = SavedRides.getWorkout(this.mTraining.getSport(), this.mWorkoutId);
            trainingGraph.setTraining(this.mTraining, savedWorkout.getLaps().size(), this.mTrainingGraphGestureListener);
            ((TextView) view.findViewById(R.id.textWorkoutDetail)).setText(R.string.workout_performance);
        } else {
            trainingGraph.setTraining(this.mTraining, this.mTraining.getStepFlatList().size());
        }
        ((TextView) view.findViewById(R.id.textTitle)).setText(this.mTraining.getTitle());
        String description = this.mTraining.getDescription();
        if (description != null && !description.isEmpty()) {
            boolean enableDesc = description.length() > getResources().getInteger(R.integer.training_desc_text_max_ems);
            Button btnDesc = (Button) view.findViewById(R.id.btnDesc);
            btnDesc.setVisibility(0);
            btnDesc.setEnabled(enableDesc);
            btnDesc.setText(description);
            if (enableDesc) {
                btnDesc.setTag(description);
                btnDesc.setOnClickListener(this.clickListener);
            } else {
                btnDesc.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }
        }
        String notSupported = textNotSupported();
        if (notSupported != null) {
            TextView textView = (TextView) view.findViewById(R.id.textNotSupported);
            textView.setVisibility(0);
            textView.setText(notSupported);
        } else if (this.mWorkoutId == -1) {
            View btnStart = view.findViewById(R.id.btnStartWorkout);
            btnStart.setVisibility(0);
            btnStart.setOnClickListener(this.clickListener);
        }
        return view;
    }

    private String textNotSupported() {
        switch (this.mTraining.getTrainingType()) {
            case PERCEIVED_EXERTION_RATING:
                return getString(R.string.training_not_supported, new Object[]{getString(R.string.rpe)});
            default:
                return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showSegmentDetails(int flatStepPosition) {
        Intent intent = new Intent(getActivity(), (Class<?>) TrainingSegmentSummaryActivity.class);
        intent.putExtra("ride_id", this.mWorkoutId);
        intent.putExtra(ThemeActivity.EXTRA_WORKOUT_TYPE, this.mTraining.getSport());
        intent.putExtra(TrainingSegmentSummaryActivity.TRAINING_FLAT_STEP_POSITION_KEY, flatStepPosition);
        startActivity(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void selectTraining() {
        SportType sportType = this.mTraining.getSport();
        if (!Prefs.getSetupComplete(this.mTraining.getSport())) {
            SetupActivity.showSetupDialog(getActivity(), sportType);
            return;
        }
        String sensorWarning = sensorWarning();
        if (sensorWarning != null) {
            displayErrorDialog(sensorWarning);
            return;
        }
        long trainingId = SavedTrainingWorkouts.add(this.mTraining);
        RideControl.setWorkoutMode(Workout.RideMode.TRAINING, trainingId);
        Intent intent = new Intent(getActivity(), (Class<?>) MainActivity.class);
        intent.putExtra("workout_mode", this.mTraining.getSport().ordinal());
        intent.setFlags(VCardConfig.FLAG_APPEND_TYPE_PARAM);
        intent.putExtra("TrainingId", trainingId);
        startActivity(intent);
    }

    private String sensorWarning() {
        if (Config.FAKE_DATA) {
            return null;
        }
        MetricType trainingType = this.mTraining.getTrainingType();
        Sensor.DataType dataType = trainingType.getSensorType();
        int sensorNameRes = R.string.sensor_desc_unknown;
        boolean canWorkWithGPS = false;
        switch (dataType) {
            case SPEED:
                canWorkWithGPS = RideControl.isGPSAvailable();
                sensorNameRes = R.string.speed;
                break;
            case PACE:
                canWorkWithGPS = RideControl.isGPSAvailable();
                sensorNameRes = R.string.pace;
                break;
            case HEARTRATE:
                sensorNameRes = R.string.heart;
                break;
            case POWER:
            case KICK:
                sensorNameRes = R.string.power;
                break;
            case CADENCE:
            case STEP:
                sensorNameRes = R.string.cadence;
                break;
        }
        if (SensorList.isSensorConnected(dataType) || canWorkWithGPS) {
            return null;
        }
        return getString(R.string.dialog_training_no_sensor, new Object[]{getString(sensorNameRes)});
    }

    private class StepAdapter extends BaseExpandableListAdapter {
        Context context;
        List<SavedTraining.Segment> segments;

        StepAdapter(Context context, List<SavedTraining.Segment> segments) {
            this.segments = segments;
            this.context = context;
        }

        @Override // android.widget.ExpandableListAdapter
        public int getGroupCount() {
            return this.segments.size();
        }

        @Override // android.widget.ExpandableListAdapter
        public int getChildrenCount(int groupPosition) {
            return this.segments.get(groupPosition).getSteps().size();
        }

        @Override // android.widget.ExpandableListAdapter
        public SavedTraining.Segment getGroup(int groupPosition) {
            return this.segments.get(groupPosition);
        }

        @Override // android.widget.ExpandableListAdapter
        public SavedTraining.Step getChild(int groupPosition, int childPosition) {
            return this.segments.get(groupPosition).getSteps().get(childPosition);
        }

        @Override // android.widget.ExpandableListAdapter
        public long getGroupId(int groupPosition) {
            return 0L;
        }

        @Override // android.widget.ExpandableListAdapter
        public long getChildId(int groupPosition, int childPosition) {
            return 0L;
        }

        @Override // android.widget.ExpandableListAdapter
        public boolean hasStableIds() {
            return false;
        }

        @Override // android.widget.ExpandableListAdapter
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(this.context, R.layout.view_list_group_header2, null);
            }
            updateSegmentView(convertView, groupPosition);
            return convertView;
        }

        @Override // android.widget.ExpandableListAdapter
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(this.context, R.layout.view_grouped_list_row2, null);
            }
            updateStepView(convertView, getChild(groupPosition, childPosition), childPosition);
            return convertView;
        }

        @Override // android.widget.ExpandableListAdapter
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }

        private void updateSegmentView(View view, int groupPosition) {
            SavedTraining.Segment segment = getGroup(groupPosition);
            TextView textTitle = (TextView) view.findViewById(R.id.txtGroupTitle);
            textTitle.setText(segment.toString(TrainingDetailFragment.this.getActivity()));
            TextView textSubTitle = (TextView) view.findViewById(R.id.txtGroupSubTitle);
            if (TrainingDetailFragment.this.mWorkoutId != -1) {
                updateSubtitle(groupPosition, textSubTitle);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) textTitle.getLayoutParams();
                params.leftMargin = 0;
                view.findViewById(R.id.imageChevron).setVisibility(0);
                view.setTag(Integer.valueOf(segment.getFlatStepListPosition()));
            }
            switch (segment.getPrimaryTrigger()) {
                case DISTANCE:
                    double distance = Conversion.distanceForLocale(segment.getApproxDistance());
                    String distStr = new DecimalFormat("0.##").format(distance) + " " + Conversion.getUnitOfDistance(view.getContext());
                    textSubTitle.setText(distStr);
                    break;
                case TIME:
                    String timeStr = Utility.formatTime(segment.getApproxDuration() * 1000);
                    textSubTitle.setText(timeStr);
                    break;
                default:
                    textSubTitle.setVisibility(4);
                    break;
            }
        }

        private void updateStepView(View view, SavedTraining.Step step, int position) {
            ((TextView) view.findViewById(R.id.txtTitle)).setText((position + 1) + ". " + step.toString(TrainingDetailFragment.this.getActivity()));
            view.findViewById(R.id.txtSubTitle).setVisibility(step.isManualLap() ? 0 : 8);
            String notes = step.getNotes();
            if (notes != null && !notes.isEmpty()) {
                View btn = view.findViewById(R.id.btnNotes);
                btn.setVisibility(0);
                btn.setTag(notes);
                btn.setOnClickListener(TrainingDetailFragment.this.clickListener);
                return;
            }
            view.findViewById(R.id.btnNotes).setVisibility(4);
        }

        private void updateSubtitle(int segmentPosition, TextView textSubTitle) {
            String text = "Incomplete";
            SavedTraining.Segment segment = getGroup(segmentPosition);
            int flatListPosition = segment.getFlatStepListPosition();
            int stepCount = segment.getStepCount() * segment.getLoopCount();
            SavedWorkout workout = SavedRides.getWorkout(TrainingDetailFragment.this.mTraining.getSport(), TrainingDetailFragment.this.mWorkoutId);
            if (workout != null) {
                List<Lap.Saved> laps = workout.getLaps(flatListPosition, stepCount);
                List<SavedTraining.Step> steps = TrainingDetailFragment.this.mTraining.getStepFlatList();
                long duration = 0;
                double distance = 0.0d;
                if (flatListPosition < steps.size()) {
                    for (int i = 0; i < stepCount; i++) {
                        if (i < laps.size()) {
                            Lap.Saved lap = laps.get(i);
                            duration += lap.getDuration();
                            distance += lap.getDistance();
                        }
                    }
                }
                String distStr = new DecimalFormat("0.##").format(Conversion.distanceForLocale(distance)) + " " + Conversion.getUnitOfDistance(TrainingDetailFragment.this.getContext());
                text = Utility.formatTime(duration) + "\n" + distStr;
            }
            textSubTitle.setText(text);
        }
    }

    private void displayErrorDialog(String message) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() { // from class: com.kopin.solos.Fragments.TrainingDetailFragment.4
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message).setPositiveButton(R.string.dialog_button_ok, dialogClickListener).show();
    }
}
