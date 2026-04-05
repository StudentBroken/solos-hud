package com.kopin.solos;

import android.content.Intent;
import android.os.Bundle;
import com.kopin.solos.common.BaseActivity;
import com.kopin.solos.common.SportType;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.SavedRides;
import com.kopin.solos.storage.SavedWorkout;

/* JADX INFO: loaded from: classes24.dex */
public class ThemeActivity extends BaseActivity {
    public static final String EXTRA_NEW_RIDE = "new_ride";
    public static final String EXTRA_RIDE_ACTIVE = "ride_active";
    public static final String EXTRA_RIDE_ID = "ride_id";
    public static final String EXTRA_ROUTE_PREVIEW = "show_route";
    public static final String EXTRA_WORKOUT_TYPE = "ride_or_run";
    protected boolean hasTabs;
    protected SavedWorkout mSavedRide;

    @Override // com.kopin.solos.common.BaseActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        SportType sportType = (intent == null || !intent.hasExtra(EXTRA_WORKOUT_TYPE)) ? LiveRide.getCurrentSport() : SportType.valueOf(intent.getStringExtra(EXTRA_WORKOUT_TYPE));
        setTheme(ThemeUtil.getTheme(sportType));
        if (intent != null && intent.hasExtra("ride_id")) {
            this.mSavedRide = SavedRides.getWorkout(sportType, intent.getLongExtra("ride_id", -1L));
        }
    }

    public CharSequence getStringFromTheme(int attr) {
        return ThemeUtil.getString(this, attr);
    }

    public int getResourceIdFromTheme(int attr) {
        return ThemeUtil.getResourceId(this, attr);
    }
}
