package com.kopin.solos.Fragments;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
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
import com.kopin.solos.RoutePreview;
import com.kopin.solos.common.config.MetricDataType;
import com.kopin.solos.graphics.GraphBuilder;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.Record;
import com.kopin.solos.storage.Ride;
import com.kopin.solos.storage.SavedRides;
import com.kopin.solos.storage.SavedWorkout;
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
public class GhostRidePreviewFragment extends WorkoutFragment {
    private static final String GHOST_RIDE_ID = "ghost_ride_id";
    private static final MetricDataType[] RIDE_ROWS = {MetricDataType.TIME, MetricDataType.SPEED, MetricDataType.CADENCE, MetricDataType.HEART_RATE, MetricDataType.POWER};
    private static final MetricDataType[] RUN_ROWS = {MetricDataType.TIME, MetricDataType.PACE, MetricDataType.STEP, MetricDataType.SPEED, MetricDataType.HEART_RATE, MetricDataType.KICK};
    private static final String TAG = "RidePreviewFragment";
    private Context mContext;
    private long mGhostRideId;
    private ExpandableCardLayout mLayout;
    private SavedWorkout mParentRide = null;
    private SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

    public static GhostRidePreviewFragment newInstance(Bundle args, long ghostRideId) {
        GhostRidePreviewFragment fragment = new GhostRidePreviewFragment();
        args.putLong(GHOST_RIDE_ID, ghostRideId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override // com.kopin.solos.Fragments.WorkoutFragment, com.kopin.solos.common.SafeFragment, android.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mGhostRideId = arguments.getLong(GHOST_RIDE_ID, -1L);
            if (this.mWorkout != null) {
                getActivity().setTitle(this.mWorkout.getTitle());
                switch (this.mWorkout.getSportType()) {
                    case RIDE:
                        this.mParentRide = SavedRides.getRide(this.mGhostRideId);
                        break;
                    case RUN:
                        this.mParentRide = SavedRides.getRun(this.mGhostRideId);
                        break;
                }
            }
        }
        setHasOptionsMenu(true);
    }

    @Override // com.kopin.solos.common.SafeFragment, android.app.Fragment
    public void onResume() {
        super.onResume();
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
                startActivity(RoutePreview.intentToLaunch(getActivity(), this.mWorkout));
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

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0055  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void populateView() {
        /*
            Method dump skipped, instruction units count: 266
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.kopin.solos.Fragments.GhostRidePreviewFragment.populateView():void");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private ExpandableCardView getViewFor(MetricDataType metric, boolean light) {
        ExpandableCardView view = new ExpandableCardView(getActivity());
        view.setTheme(light);
        view.setCalendarView(false);
        view.setExpandableViewToLoading();
        view.setCollapsedContainerMaxHeight();
        view.setMetric1(R.string.you);
        view.setMetric2(R.string.ghost);
        switch (metric) {
            case TIME:
                view.disableOnClick();
                view.setCardChevronVisible(8);
                view.setImage(R.drawable.ic_time);
                view.setCardName(this.mContext.getString(R.string.elapsed_time));
                view.setValue1(Utility.formatTime(this.mWorkout.getDuration()));
                view.setValue1Size(25);
                view.setValue2(Utility.formatTime(this.mParentRide.getDuration()));
                view.setValue2Size(25);
                return view;
            case CADENCE:
                view.setCardName(this.mContext.getString(R.string.vc_title_average_cadence));
                view.setCardUm("(" + this.mContext.getString(R.string.cadence_unit) + ")");
                view.setImage(R.drawable.ic_cadence_icon);
                view.setValue1(Ride.hasData(this.mWorkout.getAverageCadence()) ? String.format("%.1f", Double.valueOf(Math.abs(this.mWorkout.getAverageCadence()))) : "-.-");
                view.setValue2(Ride.hasData(this.mParentRide.getAverageCadence()) ? String.format("%.1f", Double.valueOf(Math.abs(this.mParentRide.getAverageCadence()))) : "-.-");
                GraphView graphView = getGraphFor(metric);
                view.setLoadingView(graphView);
                return view;
            case STEP:
                view.setCardName(this.mContext.getString(R.string.vc_title_average_cadence));
                view.setCardUm("(" + this.mContext.getString(R.string.unit_cadence_run_short) + ")");
                view.setImage(R.drawable.ic_cadence_run_activity);
                view.setValue1(Ride.hasData(this.mWorkout.getAverageCadence()) ? String.format("%.1f", Double.valueOf(Math.abs(this.mWorkout.getAverageCadence()))) : "-.-");
                view.setValue2(Ride.hasData(this.mParentRide.getAverageCadence()) ? String.format("%.1f", Double.valueOf(Math.abs(this.mParentRide.getAverageCadence()))) : "-.-");
                GraphView graphView2 = getGraphFor(metric);
                view.setLoadingView(graphView2);
                return view;
            case SPEED:
                String unitOfSpeed = Utility.getUserUnitLabel(getActivity(), 2);
                view.setCardName(this.mContext.getString(R.string.vc_title_ghost_average_speed));
                view.setCardUm("(" + unitOfSpeed + ")");
                view.setImage(R.drawable.ic_speed_icon);
                view.setValue1(Ride.hasData(this.mWorkout.getAverageSpeed()) ? String.format("%.1f", Double.valueOf(Math.abs(this.mWorkout.getAverageSpeedForLocale()))) : "-.-");
                view.setValue2(Ride.hasData(this.mParentRide.getAverageSpeed()) ? String.format("%.1f", Double.valueOf(Math.abs(this.mParentRide.getAverageSpeedForLocale()))) : "-.-");
                GraphView graphView22 = getGraphFor(metric);
                view.setLoadingView(graphView22);
                return view;
            case POWER:
                view.setCardName(this.mContext.getString(R.string.vc_title_ghost_average_power));
                view.setCardUm("(" + this.mContext.getString(R.string.power_unit) + ")");
                view.setImage(R.drawable.ic_power_icon);
                view.setValue1(Ride.hasData(this.mWorkout.getAveragePower()) ? String.format("%.1f", Double.valueOf(Math.abs(this.mWorkout.getAveragePower()))) : "-.-");
                view.setValue2(Ride.hasData(this.mParentRide.getAveragePower()) ? String.format("%.1f", Double.valueOf(Math.abs(this.mParentRide.getAveragePower()))) : "-.-");
                GraphView graphView222 = getGraphFor(metric);
                view.setLoadingView(graphView222);
                return view;
            case KICK:
                view.setCardName(this.mContext.getString(R.string.vc_title_ghost_average_power));
                view.setCardUm("(" + this.mContext.getString(R.string.power_unit) + ")");
                view.setImage(R.drawable.ic_run_power);
                view.setValue1(Ride.hasData(this.mWorkout.getAveragePower()) ? String.format("%.1f", Double.valueOf(Math.abs(this.mWorkout.getAveragePower()))) : "-.-");
                view.setValue2(Ride.hasData(this.mParentRide.getAveragePower()) ? String.format("%.1f", Double.valueOf(Math.abs(this.mParentRide.getAveragePower()))) : "-.-");
                GraphView graphView2222 = getGraphFor(metric);
                view.setLoadingView(graphView2222);
                return view;
            case HEART_RATE:
                view.setCardName("AVG. HR");
                view.setCardUm("(" + this.mContext.getString(R.string.heart_unit) + ")");
                view.setImage(R.drawable.ic_heart_rate_icon);
                view.setValue1(Ride.hasData(this.mWorkout.getAverageHeartrate()) ? String.valueOf(this.mWorkout.getAverageHeartrate()) : "0");
                view.setValue2(Ride.hasData(this.mParentRide.getAverageHeartrate()) ? String.valueOf(this.mParentRide.getAverageHeartrate()) : "0");
                GraphView graphView22222 = getGraphFor(metric);
                view.setLoadingView(graphView22222);
                return view;
            case OXYGEN:
                view.setCardName(this.mContext.getString(R.string.vc_title_ghost_average_oxygen));
                view.setCardUm("(" + getString(R.string.oxygen_unit) + ")");
                view.setImage(R.drawable.ic_lungs);
                view.setValue1(Ride.hasData(this.mWorkout.getAverageOxygen()) ? String.valueOf(this.mWorkout.getAverageOxygen()) : "-.-");
                view.setValue2(Ride.hasData(this.mParentRide.getAverageOxygen()) ? String.valueOf(this.mParentRide.getAverageOxygen()) : "-.-");
                GraphView graphView222222 = getGraphFor(metric);
                view.setLoadingView(graphView222222);
                return view;
            case PACE:
                String unitOfPace = Conversion.getUnitOfPace(getActivity());
                view.setCardName(this.mContext.getString(R.string.vc_title_average_pace));
                view.setCardUm("(" + unitOfPace + ")");
                view.setImage(R.drawable.ic_pace_activity);
                view.setValue1(Ride.hasData(this.mWorkout.getAveragePace()) ? PaceUtil.formatPace(Math.abs(this.mWorkout.getAveragePaceForLocale())) : "-.-");
                view.setValue2(Ride.hasData(this.mParentRide.getAveragePace()) ? PaceUtil.formatPace(Math.abs(this.mParentRide.getAveragePaceForLocale())) : "-.-");
                GraphView graphView2222222 = getGraphFor(metric);
                view.setLoadingView(graphView2222222);
                return view;
            default:
                GraphView graphView22222222 = getGraphFor(metric);
                view.setLoadingView(graphView22222222);
                return view;
        }
    }

    private GraphView getGraphFor(MetricDataType metric) {
        GraphView graph = new GraphView(getActivity());
        graph.setMin(0.0f, 0.0f);
        GraphBuilder.GraphValue valuesProvider = null;
        GraphRenderer.GraphDataSet.GraphType graphType = GraphRenderer.GraphDataSet.GraphType.DEFAULT;
        String unitStrY = null;
        int graphColor = ViewCompat.MEASURED_STATE_MASK;
        graph.setLabelsY(7, GraphLabels.LabelConverter.DEFAULT);
        switch (metric) {
            case CADENCE:
                valuesProvider = GraphBuilder.getValueProviderFor(Record.MetricData.CADENCE);
                graphType = GraphRenderer.GraphDataSet.GraphType.CADENCE;
                graphColor = getResources().getColor(R.color.cadence_color);
                unitStrY = getString(R.string.caps_cadence);
                break;
            case STEP:
                valuesProvider = GraphBuilder.getValueProviderFor(Record.MetricData.STEP);
                graphType = GraphRenderer.GraphDataSet.GraphType.STEP;
                graphColor = getResources().getColor(R.color.cadence_color);
                unitStrY = getString(R.string.caps_cadence_run);
                break;
            case SPEED:
                String unitOfSpeed = Utility.getUserUnitLabel(getActivity(), 2);
                valuesProvider = GraphBuilder.getValueProviderFor(Record.MetricData.SPEED);
                graphType = GraphRenderer.GraphDataSet.GraphType.SPEED;
                graphColor = getResources().getColor(R.color.speed_color);
                unitStrY = getString(R.string.caps_speed, new Object[]{unitOfSpeed});
                break;
            case POWER:
                valuesProvider = GraphBuilder.getValueProviderFor(Record.MetricData.POWER);
                graphType = GraphRenderer.GraphDataSet.GraphType.POWER;
                graphColor = getResources().getColor(R.color.power_color);
                unitStrY = getString(R.string.caps_power);
                break;
            case KICK:
                valuesProvider = GraphBuilder.getValueProviderFor(Record.MetricData.KICK);
                graphType = GraphRenderer.GraphDataSet.GraphType.KICK;
                graphColor = getResources().getColor(R.color.power_color);
                unitStrY = getString(R.string.caps_power);
                break;
            case HEART_RATE:
                valuesProvider = GraphBuilder.getValueProviderFor(Record.MetricData.HEARTRATE);
                graphType = GraphRenderer.GraphDataSet.GraphType.HEARTRATE;
                graphColor = getResources().getColor(R.color.heartrate_color);
                unitStrY = getString(R.string.caps_hr_bpm);
                break;
            case OXYGEN:
                valuesProvider = GraphBuilder.getValueProviderFor(Record.MetricData.OXYGEN);
                graphType = GraphRenderer.GraphDataSet.GraphType.OXYGENATION;
                graphColor = getResources().getColor(R.color.oxygen_color);
                unitStrY = getString(R.string.caps_oxygen);
                break;
            case PACE:
                String unitOfPace = Conversion.getUnitOfPace(getActivity());
                valuesProvider = GraphBuilder.getValueProviderFor(Record.MetricData.PACE);
                graphType = GraphRenderer.GraphDataSet.GraphType.PACE;
                graphColor = getResources().getColor(R.color.speed_color);
                unitStrY = getString(R.string.caps_pace, new Object[]{unitOfPace});
                graph.setLabelsY(7, GraphLabels.LabelConverter.MINUTES);
                break;
        }
        graph.storeData(new GraphBuilder.GraphHelper(this.mParentRide, valuesProvider, GraphRenderer.GraphDataSet.GraphType.BACKGROUND_GHOST), new GraphBuilder.GraphHelper(this.mWorkout, valuesProvider, graphType));
        graph.addBubble(GraphRenderer.GraphDataSet.GraphType.BACKGROUND_GHOST, new GraphRenderer.GraphBubble(getString(R.string.ghost), (int) getResources().getDimension(R.dimen.bubble_outside), Color.parseColor("#FF005283"), Color.parseColor("#FFFFFFFF"), false));
        graph.addBubble(GraphRenderer.GraphDataSet.GraphType.STRIDE, new GraphRenderer.GraphBubble(getString(R.string.you), (int) getResources().getDimension(R.dimen.bubble_outside), graphColor, Color.parseColor("#FF000000"), false));
        graph.setLabelsX(4, GraphLabels.LabelConverter.TIME);
        graph.setUnitX(R.string.caps_time_mins, R.string.caps_time_sec);
        graph.setUnitY(unitStrY, unitStrY);
        return graph;
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
