package com.kopin.solos;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import com.kopin.solos.Fragments.RidePreviewFragment;
import com.kopin.solos.navigation.AddRouteFragment;
import com.kopin.solos.share.Platforms;
import com.kopin.solos.storage.SavedWorkout;
import com.kopin.solos.storage.Workout;
import com.kopin.solos.util.CorrectedElevationHelper;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes24.dex */
public abstract class WorkoutSummaryActivity extends ThemeActivity {
    public static final String RIDE_ID = "Ride ID";
    public static boolean shouldPop = false;
    protected RidePreviewFragment mYourRideFragment;
    protected List<Fragment> mFragments = new ArrayList();
    private Platforms shareToPlatform = null;
    private String ADD_ROUTE_FRAG_TAG = "route_frag";
    private ActionBar.TabListener mTabListener = new ActionBar.TabListener() { // from class: com.kopin.solos.WorkoutSummaryActivity.1
        @Override // android.app.ActionBar.TabListener
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            if (!WorkoutSummaryActivity.this.isFinishing() && WorkoutSummaryActivity.this.getFragmentManager() != null && tab != null) {
                int pos = tab.getPosition();
                if (!WorkoutSummaryActivity.this.mFragments.get(pos).isAdded()) {
                    WorkoutSummaryActivity.this.getFragmentManager().beginTransaction().replace(WorkoutSummaryActivity.this.getFragmentContentViewId(), WorkoutSummaryActivity.this.mFragments.get(pos), "" + pos).commit();
                }
            }
        }

        @Override // android.app.ActionBar.TabListener
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        }

        @Override // android.app.ActionBar.TabListener
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        }
    };
    private final CorrectedElevationHelper.CorrectedElevationCallback correctedElevationCallback = new CorrectedElevationHelper.CorrectedElevationCallback() { // from class: com.kopin.solos.WorkoutSummaryActivity.2
        @Override // com.kopin.solos.util.CorrectedElevationHelper.CorrectedElevationCallback
        public void onComplete(boolean wasSuccess) {
            WorkoutSummaryActivity.this.runOnUiThread(new Runnable() { // from class: com.kopin.solos.WorkoutSummaryActivity.2.1
                @Override // java.lang.Runnable
                public void run() {
                    if (WorkoutSummaryActivity.this.mYourRideFragment.isVisible()) {
                        WorkoutSummaryActivity.this.mYourRideFragment.onCorrectedElevationResult();
                    }
                }
            });
        }
    };

    protected abstract boolean cannotShow();

    protected abstract int getFragmentContentViewId();

    protected abstract void prepareActionBarTabs();

    protected abstract void setContentView();

    @Override // com.kopin.solos.ThemeActivity, com.kopin.solos.common.BaseActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (cannotShow()) {
            finish();
            return;
        }
        Intent intent = getIntent();
        String platformKey = intent.getStringExtra("share_platform");
        this.shareToPlatform = Platforms.fromKey(platformKey);
        setContentView();
        long workoutId = intent.getLongExtra("ride_id", -1L);
        if (workoutId != -1) {
            this.mYourRideFragment = RidePreviewFragment.newInstance(intent.getExtras());
        }
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        prepareActionBarTabs();
        getFragmentManager().beginTransaction().replace(getFragmentContentViewId(), this.mFragments.get(0), "0").commit();
        if (this.mSavedRide != null) {
            if (getIntent().getBooleanExtra(ThemeActivity.EXTRA_NEW_RIDE, false)) {
                RideControl.saveWorkout(true, this.mSavedRide, this.correctedElevationCallback);
            } else {
                RideControl.updateWorkout(this.mSavedRide, this.correctedElevationCallback);
            }
        }
    }

    @Override // com.kopin.solos.common.BaseActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        setTabNavigationMode();
    }

    protected void addActionBarTab(CharSequence title) {
        ActionBar actionBar = getActionBar();
        actionBar.addTab(actionBar.newTab().setText(title).setTabListener(this.mTabListener));
    }

    protected void addActionBarTab(int res) {
        ActionBar actionBar = getActionBar();
        actionBar.addTab(actionBar.newTab().setText(res).setTabListener(this.mTabListener));
    }

    protected void setActionBarTitle(int res) {
        getActionBar().setTitle(res);
    }

    public void setTabNavigationMode() {
        if (getFragmentManager().findFragmentByTag(this.ADD_ROUTE_FRAG_TAG) == null && !isFinishing() && this.mFragments.size() > 1) {
            getActionBar().setNavigationMode(2);
        }
    }

    public void removeTabs() {
        getActionBar().setNavigationMode(0);
    }

    @Override // com.kopin.solos.common.BaseActivity, android.app.Activity
    public void onBackPressed() {
        performBack();
    }

    public void performBack() {
        if (!isFinishing() && !getFragmentManager().popBackStackImmediate()) {
            if (getActionBar().getNavigationMode() == 2 && getActionBar().getSelectedNavigationIndex() != 0) {
                getActionBar().setSelectedNavigationItem(0);
                return;
            } else {
                super.onBackPressed();
                return;
            }
        }
        setTabNavigationMode();
    }

    @Override // com.kopin.solos.common.BaseActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        if (super.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case android.R.id.home:
                if (shouldPop) {
                    shouldPop = false;
                    performBack();
                    return true;
                }
                for (Fragment fragment : this.mFragments) {
                    getFragmentManager().beginTransaction().remove(fragment).commit();
                }
                this.mFragments.clear();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override // com.kopin.solos.common.BaseActivity, android.app.Activity
    public void onActivityResult(int requestCode, int resultCode, Intent data) throws Throwable {
        super.onActivityResult(requestCode, resultCode, data);
        if (this.mYourRideFragment != null) {
            this.mYourRideFragment.onActivityResult(requestCode, resultCode, data);
        }
        if (resultCode == -1 && requestCode == RideInformationActivity.INFO_ROUTES_REQUEST_CODE) {
            showAddRouteFragment(this.mSavedRide);
        }
    }

    public void showAddRouteFragment(SavedWorkout theRide) {
        if (!isFinishing()) {
            getActionBar().setHomeButtonEnabled(true);
            getActionBar().setIcon(R.drawable.ic_launcher);
            AddRouteFragment rIF = new AddRouteFragment();
            rIF.setArguments(theRide);
            getFragmentManager().beginTransaction().replace(getFragmentContentViewId(), rIF, this.ADD_ROUTE_FRAG_TAG).addToBackStack("Ride ID").commit();
        }
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus && this.shareToPlatform != null) {
            this.mYourRideFragment.shareRide(this.shareToPlatform, false);
            this.shareToPlatform = null;
        }
    }

    public static Intent intentToLaunch(Context context, SavedWorkout workout, boolean isNew, Platforms shareToPlatform) {
        Class activityClass = workout.getWorkoutMode() == Workout.RideMode.TRAINING ? TrainingSummaryActivity.class : RidePreview.class;
        Intent intent = new Intent(context, activityClass);
        intent.putExtra("ride_id", workout.getId());
        intent.putExtra(ThemeActivity.EXTRA_WORKOUT_TYPE, workout.getSportType().name());
        intent.putExtra(ThemeActivity.EXTRA_NEW_RIDE, isNew);
        if (shareToPlatform != null) {
            intent.putExtra("share_platform", shareToPlatform.getKey());
        }
        return intent;
    }
}
