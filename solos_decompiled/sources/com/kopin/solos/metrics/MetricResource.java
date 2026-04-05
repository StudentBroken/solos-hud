package com.kopin.solos.metrics;

import com.kopin.solos.core.R;

/* JADX INFO: loaded from: classes37.dex */
public enum MetricResource {
    CADENCE(R.drawable.ic_cadence_icon),
    CALORIES(R.drawable.ic_calories_icon),
    DISTANCE(R.drawable.ic_distance_icon),
    ELEVATION(R.drawable.ic_elevation_icon),
    HEARTRATE(R.drawable.ic_heart_rate_icon),
    INTENSITY_FACTOR(R.drawable.ic_intensity_factor),
    POWER(R.drawable.ic_power_icon),
    SPEED(R.drawable.ic_speed_icon),
    OXYGENATION(R.drawable.ic_lungs),
    STRIDE(R.drawable.ic_stride),
    PACE(R.drawable.ic_pace_activity),
    STEP(R.drawable.ic_cadence_run),
    KICK(R.drawable.ic_run_power_headset),
    DERIVED_POWER(R.drawable.ic_normalized_power_activity);

    int mDrawable;

    MetricResource(int drawable) {
        this.mDrawable = 0;
        this.mDrawable = drawable;
    }

    public int getResourceImage() {
        return this.mDrawable;
    }
}
