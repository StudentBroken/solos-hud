package com.kopin.solos;

import android.app.Fragment;
import com.kopin.solos.Fragments.GhostRidePreviewFragment;
import com.kopin.solos.Fragments.TargetAveragesPreviewFragment;

/* JADX INFO: loaded from: classes24.dex */
public class RidePreview extends WorkoutSummaryActivity {
    @Override // com.kopin.solos.WorkoutSummaryActivity
    protected boolean cannotShow() {
        return getIntent() == null || getIntent().getLongExtra("ride_id", -1L) == -1;
    }

    @Override // com.kopin.solos.WorkoutSummaryActivity
    protected void setContentView() {
        setContentView(R.layout.activity_ride);
    }

    @Override // com.kopin.solos.WorkoutSummaryActivity
    protected int getFragmentContentViewId() {
        return R.id.preview_content;
    }

    @Override // com.kopin.solos.WorkoutSummaryActivity
    protected void prepareActionBarTabs() {
        if (this.mYourRideFragment != null) {
            this.mFragments.add(this.mYourRideFragment);
            addActionBarTab(getStringFromTheme(R.attr.strYourWorkout));
        }
        if (this.mSavedRide.getGhostRideId() != -1) {
            Fragment fragment = GhostRidePreviewFragment.newInstance(getIntent().getExtras(), this.mSavedRide.getGhostRideId());
            this.mFragments.add(fragment);
            addActionBarTab(R.string.compare);
        } else if (rideHasTargetAverageValues()) {
            Fragment fragment2 = TargetAveragesPreviewFragment.newInstance(getIntent().getExtras());
            this.mFragments.add(fragment2);
            addActionBarTab(R.string.target_averages);
        }
    }

    private boolean rideHasTargetAverageValues() {
        return (this.mSavedRide.getTargetAverageCadence() == Integer.MIN_VALUE || this.mSavedRide.getTargetAverageSpeedKm() == -2.147483648E9d || this.mSavedRide.getTargetAverageHeartrate() == Integer.MIN_VALUE || this.mSavedRide.getTargetAveragePower() == Integer.MIN_VALUE) ? false : true;
    }
}
