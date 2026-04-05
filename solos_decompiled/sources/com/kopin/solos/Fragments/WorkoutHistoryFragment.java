package com.kopin.solos.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.digits.sdk.vcard.VCardConfig;
import com.kopin.peloton.PelotonPrefs;
import com.kopin.solos.FinishRideActivity;
import com.kopin.solos.MainActivity;
import com.kopin.solos.R;
import com.kopin.solos.RideControl;
import com.kopin.solos.RidePreview;
import com.kopin.solos.RoutePreview;
import com.kopin.solos.ThemeActivity;
import com.kopin.solos.ThemeUtil;
import com.kopin.solos.common.BaseListFragment;
import com.kopin.solos.common.DialogUtils;
import com.kopin.solos.common.SportType;
import com.kopin.solos.config.Features;
import com.kopin.solos.menu.CustomActionProvider;
import com.kopin.solos.menu.CustomMenuItem;
import com.kopin.solos.menu.NavigationMenuAdapter;
import com.kopin.solos.share.Config;
import com.kopin.solos.share.Platforms;
import com.kopin.solos.share.Sync;
import com.kopin.solos.share.strava.StravaSync;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.Ride;
import com.kopin.solos.storage.Run;
import com.kopin.solos.storage.SQLHelper;
import com.kopin.solos.storage.ShareMap;
import com.kopin.solos.storage.Workout;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.util.Utility;
import com.kopin.solos.sync.SyncBarHelper;
import com.kopin.solos.view.ExpandableCardView;
import com.kopin.solos.view.swipelistview.BaseSwipeListViewListener;
import com.kopin.solos.view.swipelistview.SwipeListView;
import java.util.Calendar;

/* JADX INFO: loaded from: classes24.dex */
public abstract class WorkoutHistoryFragment extends BaseListFragment implements SyncBarHelper.UISyncRefresh {
    public static final int DATE_FLAGS = 524314;
    public static final int DATE_FLAGS_YEAR = 524304;
    public static final String EXTRA_GHOST_RIDE_SELECTION = "ride_selection";
    private static final int FILTER_ALL_ACTIVITIES = 0;
    private static final int FILTER_GHOSTS = 2;
    private static final int FILTER_IMPORTED = 4;
    private static final int FILTER_TARGETS = 3;
    private static final int FILTER_WORKOUTS = 1;
    private static final int MIN_CLICK_TIME = 400;
    private static final String TAG = "WorkoutHistoryFragment";
    private CustomActionProvider<NavigationMenuAdapter.NavigationMenuItem> mActivityFilterActionProvider;
    private CustomActionProvider.DefaultActionView mActivityFilterActionView;
    private RideAdapter mAdapter;
    private boolean mShowIncomplete;
    private Menu menu;
    private SyncBarHelper syncBarHelper;
    private boolean mRideActive = false;
    private boolean mRoutePicker = false;
    private boolean mGhostRideSelectionMode = false;
    private long lastClickTime = 0;
    private final CustomActionProvider.OnItemClickListener mActivityFilterMenuClickListener = new CustomActionProvider.OnItemClickListener() { // from class: com.kopin.solos.Fragments.WorkoutHistoryFragment.5
        @Override // com.kopin.solos.menu.CustomActionProvider.OnItemClickListener
        public void onItemClick(int position, CustomMenuItem menuItem) {
            Prefs.setActivitySelectionFilter(position);
            WorkoutHistoryFragment.this.mAdapter.changeCursor();
            WorkoutHistoryFragment.this.getActivity().invalidateOptionsMenu();
        }
    };
    BroadcastReceiver networkConnectivityReceiver = new BroadcastReceiver() { // from class: com.kopin.solos.Fragments.WorkoutHistoryFragment.6
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (WorkoutHistoryFragment.this.isVisible()) {
                WorkoutHistoryFragment.this.updateStravaMenuItem(WorkoutHistoryFragment.this.menu);
                WorkoutHistoryFragment.this.updateTrainingMenuItem(WorkoutHistoryFragment.this.menu);
            }
        }
    };
    private final StravaSync.DownloadListener mStravaDownloadListner = new StravaSync.DownloadListener() { // from class: com.kopin.solos.Fragments.WorkoutHistoryFragment.7
        @Override // com.kopin.solos.share.strava.StravaSync.DownloadListener
        public void onSuccess() {
            WorkoutHistoryFragment.this.refreshList();
        }

        @Override // com.kopin.solos.share.strava.StravaSync.DownloadListener
        public void onFailure() {
            Log.w(WorkoutHistoryFragment.TAG, "strava download fail");
            WorkoutHistoryFragment.this.refreshList();
        }
    };

    protected abstract int getActivityImageRes(Workout.Header header);

    protected abstract Cursor getAllWorkoutHeadersCursor(boolean z, boolean z2);

    protected abstract Cursor getImportedWorkoutHeadersCursor(boolean z);

    protected abstract Workout.Header getWorkout(Cursor cursor);

    protected abstract Cursor getWorkoutHeadersCursor(Workout.RideMode rideMode, boolean z);

    protected abstract boolean hasGhost(Workout.Header header);

    public static WorkoutHistoryFragment createFragmentForCurrentSport() {
        return LiveRide.getCurrentSport() == SportType.RIDE ? new RideHistoryFragment() : new RunHistoryFragment();
    }

    @Override // com.kopin.solos.common.BaseListFragment, android.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            this.mRideActive = args.getBoolean("rideActive", false);
            this.mRoutePicker = args.getBoolean(ThemeActivity.EXTRA_ROUTE_PREVIEW, false);
            this.mGhostRideSelectionMode = args.getBoolean(EXTRA_GHOST_RIDE_SELECTION, false);
        }
        this.mAdapter = new RideAdapter(getActivity());
        this.mAdapter.changeCursor();
        setListAdapter(this.mAdapter);
        setHasOptionsMenu(true);
        this.mActivityFilterActionView = new CustomActionProvider.DefaultActionView(getActivity());
        this.mActivityFilterActionProvider = new CustomActionProvider<>(getActivity());
    }

    @Override // com.kopin.solos.common.BaseListFragment, android.app.Fragment
    public void onStart() {
        super.onStart();
        if (this.syncBarHelper != null) {
            Sync.setSyncUpdateListener(this.syncBarHelper);
            Sync.setConnectionListener(this.syncBarHelper.connectionListener);
            Sync.reset();
            this.syncBarHelper.updateConnectivity(Utility.isNetworkAvailable(getActivity()));
        }
        if (Config.SYNC_PROVIDER != Platforms.None && Utility.isNetworkAvailable(getActivity())) {
            Sync.sync();
        }
        StravaSync.registerStravaListener(this.mStravaDownloadListner);
    }

    @Override // com.kopin.solos.common.BaseListFragment, android.app.Fragment
    public void onStop() {
        super.onStop();
        Sync.setSyncUpdateListener(null);
        Sync.setConnectionListener(null);
        StravaSync.unRegisterStravaListener(this.mStravaDownloadListner);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshList() {
        if (isStillRequired() && this.mAdapter != null) {
            getActivity().runOnUiThread(new Runnable() { // from class: com.kopin.solos.Fragments.WorkoutHistoryFragment.1
                @Override // java.lang.Runnable
                public void run() {
                    WorkoutHistoryFragment.this.mAdapter.changeCursor();
                    WorkoutHistoryFragment.this.mAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    @Override // android.app.ListFragment, android.app.Fragment
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.activity_ride_history, container, false);
        this.syncBarHelper = new SyncBarHelper(getActivity(), this, mView.findViewById(R.id.syncBar), (TextView) mView.findViewById(R.id.txtSyncOnline), (TextView) mView.findViewById(R.id.txtSyncItems), (ImageView) mView.findViewById(R.id.imgCloud), (ImageView) mView.findViewById(R.id.imgCloudIcon), (ImageView) mView.findViewById(R.id.imgShare));
        if (this.mRoutePicker) {
            ((TextView) mView.findViewById(android.R.id.empty)).setText(R.string.no_rides_with_route_msg1);
            ((TextView) mView.findViewById(R.id.empty_prompt)).setText(R.string.no_rides_with_route_msg2);
        }
        return mView;
    }

    @Override // android.app.ListFragment, android.app.Fragment
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btn_strt_ride).setVisibility(this.mRideActive ? 8 : 0);
        if (this.mRideActive) {
            setDynamicEmptyText();
        }
    }

    protected void setDynamicEmptyText() {
        ((TextView) getListView().getEmptyView()).setText(getString(R.string.activities_no_rides_live_ride_title));
        getListView().getEmptyView().setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        ((TextView) getListView().getEmptyView()).setGravity(17);
        ((TextView) getActivity().findViewById(R.id.empty_prompt)).setText(getString(R.string.activities_no_rides_live_ride_text));
    }

    @Override // com.kopin.solos.common.BaseListFragment, android.app.Fragment
    public void onResume() {
        super.onResume();
        Context context = getActivity();
        if (context != null) {
            context.registerReceiver(this.networkConnectivityReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        }
        new Handler().postDelayed(new Runnable() { // from class: com.kopin.solos.Fragments.WorkoutHistoryFragment.2
            @Override // java.lang.Runnable
            public void run() {
                if (WorkoutHistoryFragment.this.mAdapter != null) {
                    WorkoutHistoryFragment.this.mAdapter.changeCursor();
                    WorkoutHistoryFragment.this.mAdapter.notifyDataSetChanged();
                }
            }
        }, 500L);
    }

    @Override // com.kopin.solos.common.BaseListFragment, android.app.Fragment
    public void onPause() {
        super.onPause();
        Context context = getActivity();
        if (context != null) {
            context.unregisterReceiver(this.networkConnectivityReceiver);
        }
    }

    @Override // android.app.Fragment
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final SwipeListView listView = (SwipeListView) getListView();
        listView.setSwipeMode(1);
        listView.setSwipeListViewListener(new BaseSwipeListViewListener() { // from class: com.kopin.solos.Fragments.WorkoutHistoryFragment.3
            @Override // com.kopin.solos.view.swipelistview.BaseSwipeListViewListener, com.kopin.solos.view.swipelistview.SwipeListViewListener
            public void onStartOpen(int position, int action, boolean right) {
                listView.closeOpenedItems();
                super.onStartOpen(position, action, right);
            }

            @Override // com.kopin.solos.view.swipelistview.BaseSwipeListViewListener, com.kopin.solos.view.swipelistview.SwipeListViewListener
            public void onClickFrontView(int position) {
                View view = listView.getAdapter().getView(position, null, null);
                Workout.Header ride = (Workout.Header) view.getTag();
                if (ride != null) {
                    if (WorkoutHistoryFragment.this.mGhostRideSelectionMode) {
                        WorkoutHistoryFragment.this.selectGhostRide(ride.getId());
                    } else {
                        WorkoutHistoryFragment.this.openRide(ride, false, WorkoutHistoryFragment.this.mRoutePicker);
                    }
                }
            }
        });
    }

    @Override // android.app.ListFragment
    public void onListItemClick(ListView l, View v, int position, long id) {
        Workout.Header ride = (Workout.Header) v.getTag();
        if (ride != null) {
            if (this.mGhostRideSelectionMode) {
                selectGhostRide(ride.getId());
            } else {
                openRide(ride, false, this.mRoutePicker);
            }
        }
    }

    @Override // android.app.Fragment
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.history_menu, menu);
        menu.findItem(R.id.action_manage_routes).setVisible((this.mRoutePicker || this.mGhostRideSelectionMode) ? false : true);
        menu.findItem(R.id.action_import_strava).setVisible((this.mRoutePicker || this.mGhostRideSelectionMode) ? false : true);
        menu.findItem(R.id.action_incomplete).setVisible(Config.DEBUG && MainActivity.SHOW_DEBUG_MENUS);
        menu.findItem(R.id.action_training).setVisible(Features.IS_TRAINING_ENABLED);
        prepareActivityFilterActionMenu();
        menu.findItem(R.id.action_filter).setActionProvider(this.mActivityFilterActionProvider);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override // android.app.Fragment
    public void onPrepareOptionsMenu(Menu menu) {
        if (Config.DEBUG && MainActivity.SHOW_DEBUG_MENUS) {
            menu.findItem(R.id.action_incomplete).setTitle(this.mShowIncomplete ? "Show complete rides." : "Show incomplete rides.");
        }
        updateStravaMenuItem(menu);
        updateRouteMenuItem(menu);
        updateTrainingMenuItem(menu);
        updateFilterMenuItem(menu, Prefs.getActivitySelectionFilter());
        this.menu = menu;
        super.onPrepareOptionsMenu(menu);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateTrainingMenuItem(Menu menu) {
        MenuItem item;
        if (menu != null && (item = menu.findItem(R.id.action_training)) != null) {
            item.setIcon((Utility.isNetworkAvailable(getActivity()) && Platforms.TrainingPeaks.isLoggedIn(getActivity())) ? R.drawable.ic_workouts_apptheme : R.drawable.ic_workouts_grey);
            if (this.mRideActive) {
                item.setEnabled(false);
                item.getIcon().setAlpha(60);
            } else {
                item.setEnabled(true);
            }
        }
    }

    private void updateRouteMenuItem(Menu menu) {
        Drawable drawable;
        MenuItem item = menu.findItem(R.id.action_manage_routes);
        if (item != null && (drawable = item.getIcon()) != null) {
            if (this.mRideActive || Prefs.isWatchMode()) {
                item.setEnabled(false);
                drawable.setAlpha(60);
                return;
            }
            Cursor cursor = SQLHelper.getRoutesCursor(false);
            drawable.setAlpha(255);
            drawable.mutate();
            drawable.setColorFilter(getActivity().getResources().getColor(cursor.moveToNext() ? R.color.navigation_orange : R.color.unfocused_grey), PorterDuff.Mode.SRC_ATOP);
            cursor.close();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateStravaMenuItem(Menu menu) {
        Drawable drawable;
        if (menu != null) {
            MenuItem item = menu.findItem(R.id.action_import_strava);
            if (item != null && (drawable = menu.findItem(R.id.action_import_strava).getIcon()) != null) {
                drawable.mutate();
                int colorRes = (Utility.isNetworkAvailable(getActivity()) && Platforms.Strava.isLoggedIn(getActivity())) ? R.color.navigation_orange : R.color.unfocused_grey;
                drawable.setColorFilter(getActivity().getResources().getColor(colorRes), PorterDuff.Mode.SRC_ATOP);
            }
        }
    }

    private void updateFilterMenuItem(Menu menu, int selectedFilter) {
        MenuItem item;
        if (menu != null && (item = menu.findItem(R.id.action_filter)) != null) {
            item.setIcon(selectedFilter == 0 ? R.drawable.ic_no_filter : R.drawable.ic_filter);
            this.mActivityFilterActionView.setActiveColor(getContext().getColor(R.color.solos_orange));
            this.mActivityFilterActionView.setActive(true);
        }
    }

    @Override // com.kopin.solos.common.BaseListFragment, android.app.Fragment
    public boolean onOptionsItemSelected(MenuItem item) {
        if (super.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_incomplete /* 2131952533 */:
                this.mShowIncomplete = !this.mShowIncomplete;
                this.mAdapter.changeCursor();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void prepareActivityFilterActionMenu() {
        this.mActivityFilterActionProvider.setMenuAdapter(new NavigationMenuAdapter(getContext()));
        this.mActivityFilterActionProvider.setActionView(this.mActivityFilterActionView);
        this.mActivityFilterActionProvider.setOnPrepareListener(new CustomActionProvider.OnPrepareListener<NavigationMenuAdapter.NavigationMenuItem>() { // from class: com.kopin.solos.Fragments.WorkoutHistoryFragment.4
            @Override // com.kopin.solos.menu.CustomActionProvider.OnPrepareListener
            public void onPrepare(CustomActionProvider<NavigationMenuAdapter.NavigationMenuItem> actionProvider) {
                WorkoutHistoryFragment.this.mActivityFilterActionProvider.clear();
                int selectedFilter = Prefs.getActivitySelectionFilter();
                WorkoutHistoryFragment.this.addActivityFilterMenuItem(0, R.string.activity_filter_menu_item_all_activities, R.drawable.ic_no_filter, selectedFilter);
                WorkoutHistoryFragment.this.addActivityFilterMenuItem(1, R.string.activity_filter_menu_item_training, R.drawable.ic_workouts_small, selectedFilter);
                WorkoutHistoryFragment.this.addActivityFilterMenuItem(2, R.string.activity_filter_menu_item_ghost, R.drawable.ic_ghost_rider_icon, selectedFilter);
                WorkoutHistoryFragment.this.addActivityFilterMenuItem(3, R.string.activity_filter_menu_item_targets, R.drawable.ic_targets, selectedFilter);
                WorkoutHistoryFragment.this.addActivityFilterMenuItem(4, R.string.activity_filter_menu_item_imported, R.drawable.ic_imported, selectedFilter);
            }
        });
        this.mActivityFilterActionProvider.setOnItemClickListener(this.mActivityFilterMenuClickListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addActivityFilterMenuItem(int itemId, int titleRes, int drawableRes, int selected) {
        NavigationMenuAdapter.NavigationMenuItem item = new NavigationMenuAdapter.NavigationMenuItem(getString(titleRes), drawableRes, itemId, selected == itemId ? R.drawable.ic_tick : 0);
        item.setDismissOnTap(true);
        this.mActivityFilterActionProvider.addMenuItem(item);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Cursor getWorkoutHeaders(int filter) {
        switch (filter) {
            case 1:
                return getWorkoutHeadersCursor(Workout.RideMode.TRAINING, this.mRoutePicker);
            case 2:
                return getWorkoutHeadersCursor(Workout.RideMode.GHOST_RIDE, this.mRoutePicker);
            case 3:
                return getWorkoutHeadersCursor(Workout.RideMode.TARGETS, this.mRoutePicker);
            case 4:
                return getImportedWorkoutHeadersCursor(this.mRoutePicker);
            default:
                return getAllWorkoutHeadersCursor(this.mRoutePicker, this.mShowIncomplete);
        }
    }

    @Override // com.kopin.solos.sync.SyncBarHelper.UISyncRefresh
    public void refresh() {
        refreshList();
    }

    private class RideAdapter extends CursorAdapter {
        RideAdapter(Context context) {
            super(context, (Cursor) null, false);
        }

        @Override // android.widget.CursorAdapter
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return new ExpandableCardView(context);
        }

        @Override // android.widget.CursorAdapter
        public void bindView(View view, Context context, Cursor cursor) {
            Workout.Header ride = WorkoutHistoryFragment.this.getWorkout(cursor);
            int numLaps = SQLHelper.countLaps(ride.getId(), true);
            ExpandableCardView expandableCardView = (ExpandableCardView) view;
            ((TextView) expandableCardView.findViewById(R.id.card_back).findViewById(R.id.textView)).setText(ThemeUtil.getString(context, R.attr.strRemoveWorkout));
            expandableCardView.setTag(ride);
            expandableCardView.setTheme(cursor.getPosition() % 2 == 0);
            expandableCardView.setDate(ride.getActualStartTime());
            expandableCardView.setMetric1(Utility.getUserUnitLabel(context, 1));
            expandableCardView.setMetric2(R.string.caps_time);
            double dist = ride.getDistanceForLocale();
            expandableCardView.setValue1(Utility.formatDecimal(Double.valueOf(dist), dist >= 100.0d, 0, 1));
            expandableCardView.setValue2(Utility.formatTime(ride.getDuration()));
            expandableCardView.setCardName(Utility.formatTimeOnly(ride.getActualStartTime()));
            Calendar cal = Calendar.getInstance();
            cal.add(1, -1);
            boolean yearAgo = ride.getActualStartTime() < cal.getTime().getTime();
            expandableCardView.setTopTitle(DateUtils.formatDateTime(WorkoutHistoryFragment.this.getActivity(), ride.getActualStartTime(), yearAgo ? WorkoutHistoryFragment.DATE_FLAGS_YEAR : WorkoutHistoryFragment.DATE_FLAGS));
            int virtualWorkoutImageRes = 0;
            switch (ride.getMode()) {
                case GHOST_RIDE:
                    virtualWorkoutImageRes = R.drawable.ic_ghost_rider_icon;
                    break;
                case TRAINING:
                    virtualWorkoutImageRes = R.drawable.ic_workouts_small;
                    break;
            }
            expandableCardView.setWorkoutModeImage(virtualWorkoutImageRes);
            expandableCardView.setLaps(numLaps > 1);
            expandableCardView.setTitle(ride.getTitle());
            expandableCardView.setCalendarView(false);
            if (Platforms.Strava.wasImported(ride)) {
                expandableCardView.setImage(R.drawable.strava_imported);
            } else {
                expandableCardView.setImage(WorkoutHistoryFragment.this.getActivityImageRes(ride));
            }
            expandableCardView.disableOnClick();
            ImageButton deleteRideButton = (ImageButton) expandableCardView.findViewById(R.id.btnDeleteRide);
            deleteRideButton.setVisibility(0);
            deleteRideButton.setTag(ride);
            deleteRideButton.setEnabled(true);
            deleteRideButton.setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.Fragments.WorkoutHistoryFragment.RideAdapter.1
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    if (SystemClock.elapsedRealtime() - WorkoutHistoryFragment.this.lastClickTime > 400) {
                        WorkoutHistoryFragment.this.lastClickTime = SystemClock.elapsedRealtime();
                        Workout.Header ride2 = (Workout.Header) v.getTag();
                        RideAdapter.this.deleteRide(ride2, v);
                    }
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void deleteRide(Workout.Header ride, View view) {
            if (WorkoutHistoryFragment.this.hasGhost(ride) || (WorkoutHistoryFragment.this.mRideActive && LiveRide.getVirtualWorkoutId() == ride.getId())) {
                showAlertDialog();
                return;
            }
            view.setEnabled(false);
            SwipeListView listView = (SwipeListView) WorkoutHistoryFragment.this.getListView();
            listView.closeOpenedItems();
            WorkoutHistoryFragment.this.removeWorkout(ride);
            changeCursor();
            notifyDataSetChanged();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void cancelDelete(View view) {
            if (!WorkoutHistoryFragment.this.isFinishing()) {
                SwipeListView listView = (SwipeListView) WorkoutHistoryFragment.this.getListView();
                listView.closeOpenedItems();
                view.setEnabled(true);
            }
        }

        void deleteConfirmDialog(final Workout.Header ride, final View view) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() { // from class: com.kopin.solos.Fragments.WorkoutHistoryFragment.RideAdapter.2
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case -2:
                            RideAdapter.this.cancelDelete(view);
                            break;
                        case -1:
                            if (!WorkoutHistoryFragment.this.isFinishing()) {
                                WorkoutHistoryFragment.this.removeWorkout(ride);
                                RideAdapter.this.changeCursor();
                                RideAdapter.this.notifyDataSetChanged();
                            }
                            break;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(WorkoutHistoryFragment.this.getActivity());
            builder.setMessage(ThemeUtil.getString(WorkoutHistoryFragment.this.getActivity(), R.attr.strDialogDeleteWorkout)).setPositiveButton(R.string.dialog_button_yes, dialogClickListener).setNegativeButton(R.string.dialog_button_no, dialogClickListener);
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.kopin.solos.Fragments.WorkoutHistoryFragment.RideAdapter.3
                @Override // android.content.DialogInterface.OnCancelListener
                public void onCancel(DialogInterface dialog) {
                    RideAdapter.this.cancelDelete(view);
                }
            });
            builder.show();
        }

        void showAlertDialog() {
            AlertDialog dialog = DialogUtils.createDialog(WorkoutHistoryFragment.this.getActivity(), R.string.parent_ride_delete_dialog_title, R.string.parent_ride_delete_dialog_message, android.R.string.ok, (Runnable) null);
            dialog.setMessage(ThemeUtil.getString(WorkoutHistoryFragment.this.getActivity(), R.attr.strDialogDeleteParentWorkout));
            dialog.show();
            DialogUtils.setDialogTitleDivider(dialog);
        }

        public void changeCursor() {
            if (WorkoutHistoryFragment.this.isStillRequired()) {
                changeCursor(WorkoutHistoryFragment.this.getWorkoutHeaders(Prefs.getActivitySelectionFilter()));
            }
        }
    }

    public void openRide(Workout.Header ride, boolean newRide, boolean forRoute) {
        Class cls;
        Activity activity = getActivity();
        if (newRide) {
            cls = FinishRideActivity.class;
        } else {
            cls = forRoute ? RoutePreview.class : RidePreview.class;
        }
        Intent intent = new Intent(activity, (Class<?>) cls);
        intent.putExtra("ride_id", ride.getId());
        intent.putExtra(ThemeActivity.EXTRA_WORKOUT_TYPE, ride.getSportType().name());
        intent.putExtra(ThemeActivity.EXTRA_NEW_RIDE, newRide);
        intent.putExtra(ThemeActivity.EXTRA_RIDE_ACTIVE, LiveRide.isActiveRide());
        intent.putExtra(ThemeActivity.EXTRA_ROUTE_PREVIEW, forRoute);
        intent.setFlags(VCardConfig.FLAG_APPEND_TYPE_PARAM);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_right, R.anim.slide_left);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void selectGhostRide(long rideId) {
        Intent intent = new Intent(getActivity(), (Class<?>) MainActivity.class);
        intent.setFlags(VCardConfig.FLAG_APPEND_TYPE_PARAM);
        intent.putExtra("GhostRideId", rideId);
        RideControl.setWorkoutMode(Workout.RideMode.GHOST_RIDE, rideId);
        startActivity(intent);
    }

    protected void removeWorkout(Workout.Header workout) {
        if (workout instanceof Ride.Header) {
            Sync.removeWorkout(workout.getId(), SportType.RIDE);
        } else if (workout instanceof Run.Header) {
            Sync.removeWorkout(workout.getId(), SportType.RUN);
        }
        if (workout.getMode() == Workout.RideMode.TRAINING) {
            ShareMap.removeTraining(workout.getVirtualWorkoutId(), PelotonPrefs.getEmail(), Platforms.TrainingPeaks.getSharedKey());
        }
    }
}
