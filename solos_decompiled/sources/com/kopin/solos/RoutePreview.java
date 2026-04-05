package com.kopin.solos;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import com.facebook.appevents.AppEventsConstants;
import com.kopin.solos.Fragments.RoutePreviewFragment;
import com.kopin.solos.Fragments.WorkoutHistoryFragment;
import com.kopin.solos.common.permission.Permission;
import com.kopin.solos.common.permission.PermissionUtil;
import com.kopin.solos.navigation.AddRouteFragment;
import com.kopin.solos.storage.SavedWorkout;

/* JADX INFO: loaded from: classes24.dex */
public class RoutePreview extends ThemeActivity {
    public static final String EXTRA_RIDE_ID = "ride_id";
    public static final String RIDE_ID = "Ride ID";
    public static boolean shouldPop = false;
    private boolean hasTabs = false;

    @Override // com.kopin.solos.ThemeActivity, com.kopin.solos.common.BaseActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride);
        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }
        long ride = getIntent().getLongExtra("ride_id", -1L);
        if (ride == -1) {
            getActionBar().setNavigationMode(0);
            getActionBar().setDisplayShowTitleEnabled(true);
            getActionBar().setTitle(R.string.navigation_menu_item_ride);
            showActivitiesFragment();
            return;
        }
        getActionBar().setTitle(R.string.navigate_your_route);
        showRoutePreview();
    }

    private boolean rideHasTargetAverageValues() {
        return (this.mSavedRide.getTargetAverageCadence() == Integer.MIN_VALUE || this.mSavedRide.getTargetAverageSpeedKm() == -2.147483648E9d || this.mSavedRide.getTargetAverageHeartrate() == Integer.MIN_VALUE || this.mSavedRide.getTargetAveragePower() == Integer.MIN_VALUE) ? false : true;
    }

    public void addTabs() {
        if (this.hasTabs) {
            getActionBar().setNavigationMode(2);
        }
    }

    @Override // com.kopin.solos.common.BaseActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override // com.kopin.solos.common.BaseActivity, android.app.Activity
    public void onResume() {
        super.onResume();
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
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override // com.kopin.solos.common.BaseActivity, android.app.Activity
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && requestCode == RideInformationActivity.INFO_ROUTES_REQUEST_CODE) {
            showAddRouteFragment(this.mSavedRide);
        }
    }

    @Override // com.kopin.solos.common.BaseActivity, android.app.Activity
    public void onBackPressed() {
        performBack();
    }

    public void performBack() {
        if (!isFinishing() && !getFragmentManager().popBackStackImmediate()) {
            if (getActionBar().getNavigationMode() == 2 && getActionBar().getSelectedNavigationIndex() != 0) {
                getActionBar().setSelectedNavigationItem(0);
            } else {
                super.onBackPressed();
            }
        }
    }

    public void showRoutePreview() {
        if (!PermissionUtil.askPermission(this, Permission.ACCESS_FINE_LOCATION)) {
            Bundle bundle = getIntent().getExtras();
            bundle.putLong("ride_id", this.mSavedRide.getId());
            Fragment mRoutePreviewFragment = RoutePreviewFragment.newInstance(bundle);
            getFragmentManager().beginTransaction().replace(R.id.preview_content, mRoutePreviewFragment, "3").commit();
        }
    }

    public void showAddRouteFragment(SavedWorkout theRide) {
        if (!isFinishing()) {
            getActionBar().setHomeButtonEnabled(true);
            getActionBar().setIcon(R.drawable.ic_launcher);
            AddRouteFragment rIF = new AddRouteFragment();
            Bundle bundle = new Bundle();
            bundle.putLong("ride_id", theRide.getId());
            rIF.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.preview_content, rIF).addToBackStack("Ride ID").commit();
        }
    }

    public void showActivitiesFragment() {
        if (!isFinishing()) {
            getActionBar().setIcon(R.drawable.ic_launcher);
            Fragment mRideListFragment = WorkoutHistoryFragment.createFragmentForCurrentSport();
            Bundle args = new Bundle();
            args.putBoolean(ThemeActivity.EXTRA_ROUTE_PREVIEW, true);
            mRideListFragment.setArguments(args);
            getFragmentManager().beginTransaction().replace(R.id.preview_content, mRideListFragment, AppEventsConstants.EVENT_PARAM_VALUE_YES).commit();
        }
    }

    public void setRouteFromTargets() {
        if (!isFinishing()) {
            showRoutePreview();
        }
    }

    public static Intent intentToLaunch(Context context, SavedWorkout workout) {
        Intent intent = new Intent(context, (Class<?>) RoutePreview.class);
        intent.putExtra("ride_id", workout.getId());
        intent.putExtra(ThemeActivity.EXTRA_WORKOUT_TYPE, workout.getSportType().name());
        intent.putExtra(ThemeActivity.EXTRA_ROUTE_PREVIEW, true);
        return intent;
    }

    @Override // com.kopin.solos.common.BaseActivity, android.app.Activity
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (!isFinishing()) {
            if (PermissionUtil.granted(Permission.ACCESS_FINE_LOCATION, permissions, grantResults)) {
                showRoutePreview();
            } else {
                finish();
            }
        }
    }
}
