package com.kopin.solos.Fragments;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.digits.sdk.vcard.VCardConfig;
import com.kopin.solos.MainActivity;
import com.kopin.solos.R;
import com.kopin.solos.RideInformationActivity;
import com.kopin.solos.RidePreview;
import com.kopin.solos.RoutePreview;
import com.kopin.solos.ThemeActivity;
import com.kopin.solos.ThemeUtil;
import com.kopin.solos.common.config.MetricDataType;
import com.kopin.solos.graphics.GraphBuilder;
import com.kopin.solos.share.Config;
import com.kopin.solos.share.Platforms;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.Record;
import com.kopin.solos.storage.Ride;
import com.kopin.solos.storage.SavedRide;
import com.kopin.solos.storage.SavedRun;
import com.kopin.solos.storage.settings.ConfigMetrics;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.Utility;
import com.kopin.solos.util.PaceUtil;
import com.kopin.solos.view.ExpandableCardLayout;
import com.kopin.solos.view.ExpandableCardView;
import com.kopin.solos.view.graphics.GraphRenderer;
import com.kopin.solos.views.GraphLabels;
import com.kopin.solos.views.GraphView;
import java.text.SimpleDateFormat;
import java.util.Locale;

/* JADX INFO: loaded from: classes24.dex */
public class TargetAveragesPreviewFragment extends WorkoutFragment {
    private static final MetricDataType[] RIDE_ROWS = {MetricDataType.SPEED, MetricDataType.CADENCE, MetricDataType.HEART_RATE, MetricDataType.POWER};
    private static final MetricDataType[] RUN_ROWS = {MetricDataType.PACE, MetricDataType.STEP, MetricDataType.HEART_RATE, MetricDataType.KICK};
    private Context mContext;
    private ExpandableCardLayout mLayout;
    private boolean mNewRide;
    private boolean mRideActive = false;
    private SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

    public static TargetAveragesPreviewFragment newInstance(Bundle args) {
        TargetAveragesPreviewFragment fragment = new TargetAveragesPreviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override // com.kopin.solos.Fragments.WorkoutFragment, com.kopin.solos.common.SafeFragment, android.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mRideActive = arguments.getBoolean(ThemeActivity.EXTRA_RIDE_ACTIVE, false);
            this.mNewRide = arguments.getBoolean(ThemeActivity.EXTRA_NEW_RIDE, false);
        }
        setHasOptionsMenu(true);
    }

    @Override // com.kopin.solos.common.SafeFragment, android.app.Fragment
    public void onResume() {
        super.onResume();
        if (this.mWorkout != null) {
            getActivity().getActionBar().setTitle(this.mNewRide ? ThemeUtil.getString(getActivity(), R.attr.strWorkout) : getString(R.string.ride_preview_back));
        }
        ((RidePreview) getActivity()).setTabNavigationMode();
    }

    @Override // com.kopin.solos.common.SafeFragment, android.app.Fragment
    public void onPause() {
        super.onPause();
    }

    @Override // com.kopin.solos.common.SafeFragment, android.app.Fragment
    public void onDestroy() {
        super.onDestroy();
    }

    @Override // android.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override // android.app.Fragment, android.content.ComponentCallbacks
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override // android.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ride_preview, container, false);
        this.mLayout = (ExpandableCardLayout) view.findViewById(R.id.cards_container);
        this.mLayout.removeAllViews();
        populateView();
        return view;
    }

    @Override // android.app.Fragment
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_share, menu);
        menu.findItem(R.id.menuShare).setVisible(false);
        menu.findItem(R.id.menuRideInfo).setVisible(this.mWorkout == null || !Platforms.Strava.wasImported(this.mWorkout));
        if (Config.DEBUG) {
            menu.findItem(R.id.menuGhostRider).setVisible(this.mRideActive ? false : true);
        }
        menu.findItem(R.id.action_dump_to_csv).setVisible(false);
        Drawable drawable = menu.findItem(R.id.menuNav).getIcon();
        if (drawable != null) {
            drawable.mutate();
            drawable.setColorFilter(getActivity().getResources().getColor(R.color.unfocused_grey), PorterDuff.Mode.SRC_ATOP);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override // android.app.Fragment
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menuGhostRider).setVisible(!LiveRide.isActiveRide());
        menu.findItem(R.id.menuNav).setVisible(this.mWorkout.hasLocations() && !LiveRide.isActiveRide());
        super.onPrepareOptionsMenu(menu);
    }

    @Override // com.kopin.solos.common.SafeFragment, android.app.Fragment
    public boolean onOptionsItemSelected(MenuItem item) {
        if (super.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.menuGhostRider /* 2131952551 */:
                Intent intent = new Intent(this.mContext, (Class<?>) MainActivity.class);
                intent.setFlags(VCardConfig.FLAG_APPEND_TYPE_PARAM);
                intent.putExtra("GhostRideId", this.mWorkout.getId());
                startActivity(intent);
                return true;
            case R.id.menuNav /* 2131952552 */:
                Intent intent2 = RoutePreview.intentToLaunch(getActivity(), this.mWorkout);
                intent2.setFlags(VCardConfig.FLAG_APPEND_TYPE_PARAM);
                startActivity(intent2);
                getActivity().overridePendingTransition(R.anim.slide_right, R.anim.slide_left);
                return true;
            case R.id.menuShare /* 2131952553 */:
            default:
                return super.onOptionsItemSelected(item);
            case R.id.menuRideInfo /* 2131952554 */:
                RideInformationActivity.showRideInformation(getActivity(), this.mWorkout);
                return true;
        }
    }

    @Override // android.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mContext = activity.getBaseContext();
    }

    private ExpandableCardView getView(boolean light, int cardNameRes, String unit, int iconRes) {
        ExpandableCardView expandableCardView = new ExpandableCardView(getActivity());
        expandableCardView.setExpandableViewToLoading();
        expandableCardView.setTheme(light);
        expandableCardView.setExpandableViewToLoading();
        expandableCardView.setCollapsedContainerMaxHeight();
        expandableCardView.setCalendarView(false);
        expandableCardView.setCardName(this.mContext.getString(cardNameRes));
        expandableCardView.setCardUm(unit);
        expandableCardView.setImage(iconRes);
        expandableCardView.setMetric1(R.string.you);
        expandableCardView.setMetric2(R.string.target);
        return expandableCardView;
    }

    private void populateView() {
        MetricDataType[] metricRows;
        boolean light = true;
        enableTransitionType(this.mLayout);
        if (this.mWorkout != null) {
            switch (this.mWorkout.getSportType()) {
                case RIDE:
                    metricRows = RIDE_ROWS;
                    break;
                case RUN:
                    metricRows = RUN_ROWS;
                    break;
                default:
                    metricRows = new MetricDataType[0];
                    break;
            }
            for (MetricDataType row : metricRows) {
                if (ConfigMetrics.isMetricEnabled(row)) {
                    light = populateRowView(row, light);
                }
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private boolean populateRowView(MetricDataType metric, boolean light) {
        switch (metric) {
            case CADENCE:
                if (Ride.hasData(this.mWorkout.getAverageCadence())) {
                    light = !light;
                    ExpandableCardView cadence = getView(light, R.string.vc_title_average_cadence, "(" + this.mContext.getString(R.string.cadence_unit) + ")", R.drawable.ic_cadence_icon);
                    populateCadence(cadence);
                }
                return light;
            case SPEED:
                if (Ride.hasData(this.mWorkout.getAverageSpeed())) {
                    String unitOfSpeed = Utility.getUserUnitLabel(getActivity(), 2);
                    light = !light;
                    ExpandableCardView speed = getView(light, R.string.vc_title_ghost_average_speed, "(" + unitOfSpeed + ")", R.drawable.ic_speed_icon);
                    populateSpeed(speed, unitOfSpeed);
                }
                return light;
            case POWER:
                if (Ride.hasData(this.mWorkout.getAveragePower())) {
                    light = !light;
                    ExpandableCardView power = getView(light, R.string.vc_title_ghost_average_power, "(" + this.mContext.getString(R.string.power_unit) + ")", R.drawable.ic_power_icon);
                    populatePower(power);
                }
                return light;
            case KICK:
                if (Ride.hasData(this.mWorkout.getAveragePower())) {
                    light = !light;
                    ExpandableCardView power2 = getView(light, R.string.vc_title_ghost_average_power, "(" + this.mContext.getString(R.string.power_unit) + ")", R.drawable.ic_run_power);
                    populatePower(power2);
                }
                return light;
            case HEART_RATE:
                if (Ride.hasData(this.mWorkout.getAverageHeartrate())) {
                    light = !light;
                    ExpandableCardView heartRate = getView(light, R.string.vc_title_average_heartrate, "(" + this.mContext.getString(R.string.heart_unit) + ")", R.drawable.ic_heart_rate_icon);
                    populateHeartRate(heartRate);
                }
                return light;
            case PACE:
                if (Ride.hasData(this.mWorkout.getAveragePace())) {
                    String unitOfPace = Conversion.getUnitOfPace(getActivity());
                    light = !light;
                    ExpandableCardView stride = getView(light, R.string.vc_title_average_pace, "(" + unitOfPace + ")", R.drawable.ic_pace_activity);
                    populatePace(stride, unitOfPace);
                }
                return light;
            case STEP:
                if (Ride.hasData(this.mWorkout.getAverageCadence())) {
                    light = !light;
                    ExpandableCardView cadence2 = getView(light, R.string.vc_title_average_cadence, "(" + this.mContext.getString(R.string.unit_cadence_run) + ")", R.drawable.ic_cadence_run_activity);
                    populateStep(cadence2);
                }
                return light;
            default:
                return light;
        }
    }

    private void populateCadence(ExpandableCardView cadence) {
        GraphView cadenceView = new GraphView(getActivity());
        int targetCadence = this.mWorkout.getTargetAverageCadence();
        double averageCadence = this.mWorkout.getAverageCadence();
        if (averageCadence > 0.0d) {
            cadence.setValue1(String.format("%.1f", Double.valueOf(averageCadence)));
            cadenceView.addAverage(GraphRenderer.GraphDataSet.GraphType.TARGET_AVERAGE_PRIMARY, averageCadence, getString(R.string.you), getResources().getColor(((double) targetCadence) >= averageCadence ? R.color.average_line_color_target_secondary_less : R.color.average_line_color_target_secondary_greater), false);
        } else {
            cadence.setValue1(getString(R.string.caps_no_data));
        }
        cadence.setValue2("" + targetCadence);
        cadenceView.addAverage(GraphRenderer.GraphDataSet.GraphType.TARGET_AVERAGE_SECONDARY, targetCadence, getString(R.string.target), getResources().getColor(R.color.average_line_color_target_primary), false);
        cadence.setLoadingView(cadenceView);
        cadenceView.setMin(0.0f, 0.0f);
        cadenceView.storeData(new GraphBuilder.GraphHelper(this.mWorkout, GraphBuilder.getValueProviderFor(Record.MetricData.CADENCE), GraphRenderer.GraphDataSet.GraphType.TARGET_CADENCE));
        cadenceView.setLabelsX(4, GraphLabels.LabelConverter.TIME);
        cadenceView.setLabelsY(7, GraphLabels.LabelConverter.DEFAULT);
        cadenceView.setUnitX(R.string.caps_time_mins, R.string.caps_time_sec);
        cadenceView.setUnitY(R.string.caps_cadence);
        this.mLayout.addView(cadence);
    }

    private void populateStep(ExpandableCardView cadence) {
        GraphView cadenceView = new GraphView(getActivity());
        int targetCadence = this.mWorkout.getTargetAverageCadence();
        double averageCadence = this.mWorkout.getAverageCadence();
        if (averageCadence > 0.0d) {
            cadence.setValue1(String.format("%.1f", Double.valueOf(averageCadence)));
            cadenceView.addAverage(GraphRenderer.GraphDataSet.GraphType.TARGET_AVERAGE_PRIMARY, averageCadence, getString(R.string.you), getResources().getColor(((double) targetCadence) >= averageCadence ? R.color.average_line_color_target_secondary_less : R.color.average_line_color_target_secondary_greater), false);
        } else {
            cadence.setValue1(getString(R.string.caps_no_data));
        }
        cadence.setValue2("" + targetCadence);
        cadenceView.addAverage(GraphRenderer.GraphDataSet.GraphType.TARGET_AVERAGE_SECONDARY, targetCadence, getString(R.string.target), getResources().getColor(R.color.average_line_color_target_primary), false);
        cadence.setLoadingView(cadenceView);
        cadenceView.setMin(0.0f, 0.0f);
        cadenceView.storeData(new GraphBuilder.GraphHelper(this.mWorkout, GraphBuilder.getValueProviderFor(Record.MetricData.CADENCE), GraphRenderer.GraphDataSet.GraphType.TARGET_CADENCE));
        cadenceView.setLabelsX(4, GraphLabels.LabelConverter.TIME);
        cadenceView.setLabelsY(7, GraphLabels.LabelConverter.DEFAULT);
        cadenceView.setUnitX(R.string.caps_time_mins, R.string.caps_time_sec);
        cadenceView.setUnitY(R.string.caps_cadence);
        this.mLayout.addView(cadence);
    }

    private void populateSpeed(ExpandableCardView speed, String unitOfSpeed) {
        GraphView speedView = new GraphView(getActivity());
        double avgSpeed = this.mWorkout.getAverageSpeedForLocale();
        double avgTargetSpeed = Utility.roundToLong(Prefs.isMetric() ? this.mWorkout.getTargetAverageSpeedKm() : this.mWorkout.getTargetAverageSpeedKm() * 0.621d);
        if (avgSpeed > 0.0d) {
            speed.setValue1(String.format("%.1f", Double.valueOf(avgSpeed)));
            speedView.addAverage(GraphRenderer.GraphDataSet.GraphType.TARGET_AVERAGE_PRIMARY, avgSpeed, getString(R.string.you), getResources().getColor(avgTargetSpeed >= avgSpeed ? R.color.average_line_color_target_secondary_less : R.color.average_line_color_target_secondary_greater), false);
        } else {
            speed.setValue1(getString(R.string.caps_no_data));
        }
        speed.setValue2(Utility.trimDecimalPlaces(avgTargetSpeed, 1, false));
        speedView.addAverage(GraphRenderer.GraphDataSet.GraphType.TARGET_AVERAGE_SECONDARY, avgTargetSpeed, getString(R.string.target), getResources().getColor(R.color.average_line_color_target_primary), false);
        speed.setLoadingView(speedView);
        speedView.setMin(0.0f, 0.0f);
        speedView.storeData(new GraphBuilder.GraphHelper(this.mWorkout, GraphBuilder.getValueProviderFor(Record.MetricData.SPEED), GraphRenderer.GraphDataSet.GraphType.TARGET_SPEED));
        speedView.setLabelsX(4, GraphLabels.LabelConverter.TIME);
        speedView.setLabelsY(7, GraphLabels.LabelConverter.DEFAULT);
        speedView.setUnitX(R.string.caps_time_mins, R.string.caps_time_sec);
        String unitX = getString(R.string.caps_speed, new Object[]{unitOfSpeed});
        speedView.setUnitY(unitX, unitX);
        this.mLayout.addView(speed);
    }

    private void populatePower(ExpandableCardView power) {
        GraphView powerView = new GraphView(getActivity());
        double avgPower = this.mWorkout.getAveragePower();
        double avgTargetPower = this.mWorkout.getTargetAveragePower();
        if (avgPower > 0.0d) {
            power.setValue1(String.format("%.1f", Double.valueOf(avgPower)));
            powerView.addAverage(GraphRenderer.GraphDataSet.GraphType.TARGET_AVERAGE_PRIMARY, avgPower, getString(R.string.you), getResources().getColor(avgTargetPower >= avgPower ? R.color.average_line_color_target_secondary_less : R.color.average_line_color_target_secondary_greater), false);
        } else {
            power.setValue1(getString(R.string.caps_no_data));
        }
        power.setValue2("" + avgTargetPower);
        powerView.addAverage(GraphRenderer.GraphDataSet.GraphType.TARGET_AVERAGE_SECONDARY, avgTargetPower, getString(R.string.target), getResources().getColor(R.color.average_line_color_target_primary), false);
        power.setLoadingView(powerView);
        powerView.setMin(0.0f, 0.0f);
        if (this.mWorkout instanceof SavedRide) {
            powerView.storeData(new GraphBuilder.GraphHelper(this.mWorkout, GraphBuilder.getValueProviderFor(Record.MetricData.POWER), GraphRenderer.GraphDataSet.GraphType.TARGET_POWER));
        } else if (this.mWorkout instanceof SavedRun) {
            powerView.storeData(new GraphBuilder.GraphHelper(this.mWorkout, GraphBuilder.getValueProviderFor(Record.MetricData.KICK), GraphRenderer.GraphDataSet.GraphType.TARGET_KICK));
        }
        powerView.setLabelsX(4, GraphLabels.LabelConverter.TIME);
        powerView.setLabelsY(7, GraphLabels.LabelConverter.DEFAULT);
        powerView.setUnitX(R.string.caps_time_mins, R.string.caps_time_sec);
        powerView.setUnitY(R.string.caps_power);
        this.mLayout.addView(power);
    }

    private void populateHeartRate(ExpandableCardView heartRate) {
        GraphView heartRateView = new GraphView(getActivity());
        double avgHeartRate = this.mWorkout.getAverageHeartrate();
        double avgTargetHeartRate = this.mWorkout.getTargetAverageHeartrate();
        if (avgHeartRate > 0.0d) {
            heartRate.setValue1(String.valueOf(avgHeartRate));
            heartRateView.addAverage(GraphRenderer.GraphDataSet.GraphType.TARGET_AVERAGE_PRIMARY, avgHeartRate, getString(R.string.you), getResources().getColor(avgTargetHeartRate >= avgHeartRate ? R.color.average_line_color_target_secondary_less : R.color.average_line_color_target_secondary_greater), false);
        } else {
            heartRate.setValue1(getString(R.string.caps_no_data));
        }
        heartRate.setValue2("" + this.mWorkout.getTargetAverageHeartrate());
        heartRateView.addAverage(GraphRenderer.GraphDataSet.GraphType.TARGET_AVERAGE_SECONDARY, avgTargetHeartRate, getString(R.string.target), getResources().getColor(R.color.average_line_color_target_primary), false);
        heartRate.setLoadingView(heartRateView);
        heartRateView.setMin(0.0f, 0.0f);
        heartRateView.storeData(new GraphBuilder.GraphHelper(this.mWorkout, GraphBuilder.getValueProviderFor(Record.MetricData.HEARTRATE), GraphRenderer.GraphDataSet.GraphType.TARGET_HEARTRATE));
        heartRateView.setLabelsX(4, GraphLabels.LabelConverter.TIME);
        heartRateView.setLabelsY(7, GraphLabels.LabelConverter.DEFAULT);
        heartRateView.setUnitX(R.string.caps_time_mins, R.string.caps_time_sec);
        heartRateView.setUnitY(R.string.caps_hr_bpm);
        this.mLayout.addView(heartRate);
    }

    private void populatePace(ExpandableCardView pace, String unitOfPace) {
        GraphView paceView = new GraphView(getActivity());
        double avg = this.mWorkout.getAveragePaceForLocale();
        double avgTargetMinPerKm = ((SavedRun) this.mWorkout).getTargetAveragePaceMinutesPerKm();
        double avgTarget = Prefs.isMetric() ? avgTargetMinPerKm : Conversion.minutesPerKmToMinutesPerMile(avgTargetMinPerKm);
        if (avg > 0.0d) {
            pace.setValue1(PaceUtil.formatPace(avg));
            paceView.addAverage(GraphRenderer.GraphDataSet.GraphType.TARGET_AVERAGE_PRIMARY, avg, getString(R.string.you), getResources().getColor(avgTarget >= avg ? R.color.average_line_color_target_secondary_less : R.color.average_line_color_target_secondary_greater), false);
        } else {
            pace.setValue1(getString(R.string.caps_no_data));
        }
        pace.setValue2(PaceUtil.formatPace(avgTarget));
        paceView.addAverage(GraphRenderer.GraphDataSet.GraphType.TARGET_AVERAGE_SECONDARY, avgTarget, getString(R.string.target), getResources().getColor(R.color.average_line_color_target_primary), false);
        pace.setLoadingView(paceView);
        paceView.setMin(0.0f, 0.0f);
        paceView.storeData(new GraphBuilder.GraphHelper(this.mWorkout, GraphBuilder.getValueProviderFor(Record.MetricData.PACE), GraphRenderer.GraphDataSet.GraphType.TARGET_PACE));
        paceView.setLabelsX(4, GraphLabels.LabelConverter.TIME);
        paceView.setLabelsY(7, GraphLabels.LabelConverter.MINUTES);
        paceView.setUnitX(R.string.caps_time_mins, R.string.caps_time_sec);
        String unitX = getString(R.string.caps_pace, new Object[]{unitOfPace});
        paceView.setUnitY(unitX, unitX);
        this.mLayout.addView(pace);
    }

    private void enableTransitionType(ViewGroup viewGroup) {
        LayoutTransition layoutTransition = viewGroup.getLayoutTransition();
        if (layoutTransition != null) {
            layoutTransition.enableTransitionType(4);
            layoutTransition.setDuration(1000L);
        }
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof ViewGroup) {
                enableTransitionType((ViewGroup) child);
            }
        }
    }
}
