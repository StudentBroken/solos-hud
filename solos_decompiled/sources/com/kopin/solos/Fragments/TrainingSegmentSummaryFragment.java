package com.kopin.solos.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.kopin.solos.R;
import com.kopin.solos.common.BaseSupportFragment;
import com.kopin.solos.common.SportType;
import com.kopin.solos.graphics.GraphBuilder;
import com.kopin.solos.storage.Lap;
import com.kopin.solos.storage.Record;
import com.kopin.solos.storage.SavedRides;
import com.kopin.solos.storage.SavedTraining;
import com.kopin.solos.storage.SavedTrainingWorkouts;
import com.kopin.solos.storage.SavedWorkout;
import com.kopin.solos.storage.settings.UserProfile;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.MetricType;
import com.kopin.solos.storage.util.Utility;
import com.kopin.solos.view.graphics.GraphRenderer;
import com.kopin.solos.views.GraphLabels;
import com.kopin.solos.views.GraphView;
import com.kopin.solos.virtualworkout.Target;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes24.dex */
public class TrainingSegmentSummaryFragment extends BaseSupportFragment {
    private int mFlatSegmentPosition;
    private SavedTraining mSavedTraining;
    private SavedWorkout mSavedWorkout;
    private double mSegmentDistance;
    private long mSegmentDuration;
    private String mSegmentTitle;

    public static TrainingSegmentSummaryFragment newInstance(long workoutId, SportType sportType, int flatSegmentPosition) {
        TrainingSegmentSummaryFragment fragment = new TrainingSegmentSummaryFragment();
        Bundle args = new Bundle();
        args.putLong("workoutId", workoutId);
        args.putSerializable("sport", sportType);
        args.putInt("flatSegmentPosition", flatSegmentPosition);
        fragment.setArguments(args);
        return fragment;
    }

    @Override // com.kopin.solos.common.BaseSupportFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            SportType sport = (SportType) args.getSerializable("sport");
            this.mSavedWorkout = SavedRides.getWorkout(sport, args.getLong("workoutId"));
            this.mSavedTraining = SavedTrainingWorkouts.get(this.mSavedWorkout.getVirtualWorkoutId());
            this.mFlatSegmentPosition = args.getInt("flatSegmentPosition");
        }
    }

    @Override // android.support.v4.app.Fragment
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Lap.Saved workoutSegment;
        Pair<Integer, Integer> pair = this.mSavedTraining.getSegmentMap().get(this.mFlatSegmentPosition);
        int segmentPosition = ((Integer) pair.first).intValue();
        int loopIndex = ((Integer) pair.second).intValue();
        SavedTraining.Segment segment = this.mSavedTraining.getSegments().get(segmentPosition);
        int stepCount = segment.getStepCount();
        int offset = segment.getFlatStepListPosition() + (segment.getStepCount() * loopIndex);
        List<SavedTraining.Step> steps = this.mSavedTraining.getStepFlatList().subList(offset, offset + stepCount);
        List<Lap.Saved> laps = this.mSavedWorkout.getLaps(offset, stepCount);
        View view = inflater.inflate(R.layout.fragment_training_segment_summary, container, false);
        this.mSegmentTitle = segment.toString(getContext());
        if (segment.getLoopCount() > 1) {
            this.mSegmentTitle += " " + (loopIndex + 1);
        }
        if (laps.size() > 0) {
            workoutSegment = new Lap.Saved(this.mSavedWorkout.getId(), laps.get(0).getStartTime(), laps.get(laps.size() - 1).getEndTime());
        } else {
            workoutSegment = new Lap.Saved(this.mSavedWorkout.getId(), -2147483648L, -2147483648L);
        }
        FrameLayout primaryGraphLayout = (FrameLayout) view.findViewById(R.id.graphPrimaryMetric);
        addGraph(primaryGraphLayout, workoutSegment, steps, laps, this.mSavedTraining.getTrainingType(), 0);
        if (segment.getSecondaryMetric() != null) {
            FrameLayout secondaryGraphLayout = (FrameLayout) view.findViewById(R.id.graphSecondaryMetric);
            secondaryGraphLayout.setVisibility(0);
            addGraph(secondaryGraphLayout, workoutSegment, steps, laps, this.mSavedTraining.getSecondaryMetric(), 1);
        }
        ((TextView) view.findViewById(R.id.textSegmentTitle)).setText(this.mSegmentTitle);
        ExpandableListView listView = (ExpandableListView) view.findViewById(R.id.groupedList);
        listView.setAdapter(new StepTargetAdapter(getContext(), steps, laps));
        listView.expandGroup(0);
        return view;
    }

    private void setGraphData(GraphView graphView, MetricType metricType, Lap.Saved workoutSegment) {
        Record.MetricData metricData = Record.MetricData.CADENCE;
        GraphRenderer.GraphDataSet.GraphType graphType = GraphRenderer.GraphDataSet.GraphType.TARGET_SPEED;
        graphView.setLabelsY(7, GraphLabels.LabelConverter.DEFAULT);
        switch (metricType) {
            case AVERAGE_TARGET_CADENCE:
                metricData = Record.MetricData.CADENCE;
                graphType = GraphRenderer.GraphDataSet.GraphType.TARGET_CADENCE;
                graphView.setUnitY(R.string.caps_cadence);
                break;
            case AVERAGE_TARGET_STEP:
                metricData = Record.MetricData.CADENCE;
                graphType = GraphRenderer.GraphDataSet.GraphType.TARGET_STEP;
                graphView.setUnitY(R.string.caps_cadence_run);
                break;
            case AVERAGE_TARGET_HEARTRATE:
                metricData = Record.MetricData.HEARTRATE;
                graphType = GraphRenderer.GraphDataSet.GraphType.TARGET_HEARTRATE;
                graphView.setUnitY(R.string.caps_hr_bpm);
                break;
            case AVERAGE_TARGET_POWER:
                metricData = Record.MetricData.POWER;
                graphType = GraphRenderer.GraphDataSet.GraphType.TARGET_POWER;
                graphView.setUnitY(R.string.caps_power);
                break;
            case AVERAGE_TARGET_KICK:
                metricData = Record.MetricData.KICK;
                graphType = GraphRenderer.GraphDataSet.GraphType.TARGET_KICK;
                graphView.setUnitY(R.string.caps_power);
                break;
            case AVERAGE_TARGET_SPEED:
                metricData = Record.MetricData.SPEED;
                graphType = GraphRenderer.GraphDataSet.GraphType.TARGET_SPEED;
                String unitY = getString(R.string.caps_speed, Conversion.getUnitOfSpeed(getContext()));
                graphView.setUnitY(unitY, unitY);
                break;
            case AVERAGE_TARGET_PACE:
                metricData = Record.MetricData.PACE;
                graphType = GraphRenderer.GraphDataSet.GraphType.TARGET_PACE;
                String unitY2 = getString(R.string.caps_pace, Conversion.getUnitOfPaceShort(getContext()));
                graphView.setUnitY(unitY2, unitY2);
                graphView.setLabelsY(7, GraphLabels.LabelConverter.MINUTES);
                break;
        }
        graphView.setMin(0.0f, 0.0f);
        graphView.storeData(new GraphBuilder.GraphHelper(workoutSegment, GraphBuilder.getValueProviderFor(metricData), graphType));
        graphView.setLabelsX(4, GraphLabels.LabelConverter.TIME);
        graphView.setUnitX(R.string.caps_time_mins, R.string.caps_time_sec);
        graphView.setTextColor(getContext().getColor(R.color.element_background_light1));
    }

    private void addGraph(FrameLayout graphLayout, Lap.Saved workoutSegment, List<SavedTraining.Step> steps, List<Lap.Saved> laps, MetricType metricType, int targetIndex) {
        long stepStartTime;
        long estimatedStepDuration;
        long estimatedStepDuration2;
        GraphView graphView = new GraphView(getActivity());
        int i = -1;
        long stepEndTime = 0;
        double defaultAvgSpeed = UserProfile.getAverageSpeed(this.mSavedTraining.getSport());
        this.mSegmentDuration = 0L;
        this.mSegmentDistance = 0.0d;
        for (SavedTraining.Step step : steps) {
            i++;
            if (i >= laps.size()) {
                if (step.getDuration() > 0) {
                    estimatedStepDuration2 = step.getDuration() * 1000;
                } else {
                    estimatedStepDuration2 = Conversion.speedToTime(defaultAvgSpeed, step.getDistance());
                }
                stepStartTime = stepEndTime;
                stepEndTime = stepStartTime + estimatedStepDuration2;
            } else {
                Lap.Saved lap = laps.get(i);
                stepStartTime = lap.getStartTime();
                if (i < laps.size() - 1) {
                    stepEndTime = lap.getEndTime();
                } else {
                    if (step.getDuration() > 0) {
                        estimatedStepDuration = step.getDuration() * 1000;
                    } else {
                        estimatedStepDuration = Conversion.speedToTime(lap.getAverageSpeed(), step.getDistance()) * 1000;
                    }
                    stepEndTime = Math.max(lap.getEndTime(), stepStartTime + estimatedStepDuration);
                }
                this.mSegmentDuration += lap.getDuration();
                this.mSegmentDistance += lap.getDistance();
            }
            if (targetIndex < step.getTargets().size()) {
                SavedTraining.Target target = step.getTargets().get(targetIndex);
                switch (target.getType()) {
                    case ABOVE:
                    case BELOW:
                        double threshold = getValueForLocale(metricType, target.getThresholdTarget());
                        graphView.addAverage(GraphRenderer.GraphDataSet.GraphType.TARGET_AVERAGE_SECONDARY, threshold, stepStartTime, stepEndTime, null, getContext().getColor(R.color.average_line_color_target_primary), false);
                        break;
                    case RANGE:
                        double min = getValueForLocale(metricType, target.getMinTarget());
                        double max = getValueForLocale(metricType, target.getMaxTarget());
                        graphView.addRange(GraphRenderer.GraphDataSet.GraphType.TARGET_AVERAGE_SECONDARY, min, max, stepStartTime, stepEndTime, null, getContext().getColor(R.color.average_line_color_target_primary), false, false);
                        break;
                }
            }
        }
        setGraphData(graphView, metricType, workoutSegment);
        graphLayout.addView(graphView);
        graphView.onScreen();
    }

    private double getValueForLocale(MetricType metricType, double value) {
        switch (metricType) {
            case AVERAGE_TARGET_SPEED:
                return Conversion.speedForLocale(value);
            case AVERAGE_TARGET_PACE:
                return Conversion.paceForLocale(value);
            default:
                return value;
        }
    }

    private class StepTargetAdapter extends BaseExpandableListAdapter {
        Context context;
        List<Lap.Saved> laps;
        List<SavedTraining.Step> steps;

        StepTargetAdapter(Context context, List<SavedTraining.Step> steps, List<Lap.Saved> laps) {
            this.steps = new ArrayList();
            this.laps = new ArrayList();
            this.context = context;
            this.steps = steps;
            this.laps = laps;
        }

        @Override // android.widget.ExpandableListAdapter
        public int getGroupCount() {
            return 1;
        }

        @Override // android.widget.ExpandableListAdapter
        public int getChildrenCount(int groupPosition) {
            return this.steps.size() + 1;
        }

        @Override // android.widget.ExpandableListAdapter
        public String getGroup(int groupPosition) {
            return null;
        }

        @Override // android.widget.ExpandableListAdapter
        public SavedTraining.Step getChild(int groupPosition, int childPosition) {
            return this.steps.get(childPosition);
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
        public View getGroupView(int groupPosition, boolean b, View convertView, ViewGroup viewGroup) {
            if (convertView == null) {
                convertView = View.inflate(this.context, R.layout.view_grouped_list_header3, null);
            }
            ((TextView) convertView.findViewById(R.id.txtGroupTitle)).setText(TrainingSegmentSummaryFragment.this.mSegmentTitle);
            String subtitle = Utility.formatTime(TrainingSegmentSummaryFragment.this.mSegmentDuration) + "\n" + new DecimalFormat("0.##").format(Conversion.distanceForLocale(TrainingSegmentSummaryFragment.this.mSegmentDistance)) + " " + Conversion.getUnitOfDistance(TrainingSegmentSummaryFragment.this.getContext());
            ((TextView) convertView.findViewById(R.id.txtGroupSubTitle)).setText(subtitle);
            return convertView;
        }

        @Override // android.widget.ExpandableListAdapter
        public View getChildView(int groupPosition, int childPosition, boolean isExpanded, View convertView, ViewGroup viewGroup) {
            if (convertView == null) {
                convertView = View.inflate(this.context, R.layout.view_grouped_list_row3, null);
            }
            updateStepSummary(convertView, childPosition);
            return convertView;
        }

        @Override // android.widget.ExpandableListAdapter
        public boolean isChildSelectable(int i, int childPosition) {
            return false;
        }

        private void updateStepSummary(View view, int childPosition) {
            TextView textTitle = (TextView) view.findViewById(R.id.textTitle);
            if (childPosition == 0) {
                textTitle.setVisibility(8);
                return;
            }
            int childPosition2 = childPosition - 1;
            String strTime = "---";
            String strDistance = "---";
            SavedTraining.Step step = getChild(0, childPosition2);
            Lap.Saved lap = childPosition2 < this.laps.size() ? this.laps.get(childPosition2) : null;
            textTitle.setText((childPosition2 + 1) + ". " + step.getTitle());
            if (lap != null) {
                strTime = Utility.formatTime(lap.getDuration());
                double distance = Conversion.distanceForLocale(lap.getDistance());
                strDistance = new DecimalFormat("0.##").format(distance) + " " + Conversion.getUnitOfDistance(TrainingSegmentSummaryFragment.this.getContext());
            }
            TextView textTime = (TextView) view.findViewById(R.id.textTime);
            textTime.setTextColor(TrainingSegmentSummaryFragment.this.getContext().getColor(R.color.white));
            textTime.setText(strTime);
            TextView textDistance = (TextView) view.findViewById(R.id.textDistance);
            textDistance.setTextColor(TrainingSegmentSummaryFragment.this.getContext().getColor(R.color.white));
            textDistance.setText(strDistance);
            StringBuilder sbTarget = new StringBuilder();
            StringBuilder sbYou = new StringBuilder();
            for (SavedTraining.Target target : step.getTargets()) {
                sbTarget.append(formatTarget(target)).append("\n\n");
                sbYou.append(formatReal(target, lap)).append("<br/><br/>");
            }
            TextView textTarget = (TextView) view.findViewById(R.id.textTarget);
            textTarget.setTextColor(TrainingSegmentSummaryFragment.this.getContext().getColor(R.color.white));
            textTarget.setText(sbTarget.toString().trim());
            ((TextView) view.findViewById(R.id.textYou)).setText(Html.fromHtml(sbYou.toString().trim()));
        }

        private String formatTarget(SavedTraining.Target target) {
            switch (target.getType()) {
                case ABOVE:
                case BELOW:
                    String text = Utility.formatMetricWithUnit(this.context, target.getMetric(), target.getThresholdTarget());
                    return text;
                case RANGE:
                    String text2 = Utility.formatMetricRange(this.context, target.getMetric(), target.getMinTarget(), target.getMaxTarget());
                    return text2;
                default:
                    return "";
            }
        }

        private String formatReal(SavedTraining.Target target, Lap.Saved lap) {
            if (lap == null) {
                return "---";
            }
            double value = 0.0d;
            Target target1 = new Target(target.getThresholdTarget(), target.getMinTarget(), target.getMaxTarget());
            switch (target.getMetric()) {
                case AVERAGE_TARGET_CADENCE:
                case AVERAGE_TARGET_STEP:
                    double value2 = lap.getAverageCadence();
                    value = Math.round(value2);
                    break;
                case AVERAGE_TARGET_HEARTRATE:
                    double value3 = lap.getAverageHeartrate();
                    value = Math.round(value3);
                    break;
                case AVERAGE_TARGET_POWER:
                case AVERAGE_TARGET_KICK:
                    double value4 = lap.getAveragePower();
                    value = Math.round(value4);
                    break;
                case AVERAGE_TARGET_SPEED:
                    value = lap.getAverageSpeed();
                    break;
                case AVERAGE_TARGET_PACE:
                    value = lap.getAveragePace();
                    break;
            }
            String colorText = target1.isInRange(Double.valueOf(value)) ? "<font color='#01AD61A'>" : "<font color='#CC0000'>";
            return colorText + Utility.formatMetricWithUnit(this.context, target.getMetric(), value) + "</font>";
        }
    }
}
