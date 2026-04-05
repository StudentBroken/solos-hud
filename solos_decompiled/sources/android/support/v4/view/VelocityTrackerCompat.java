package android.support.v4.view;

import android.view.VelocityTracker;

/* JADX INFO: loaded from: classes27.dex */
@Deprecated
public final class VelocityTrackerCompat {
    @Deprecated
    public static float getXVelocity(VelocityTracker tracker, int pointerId) {
        return tracker.getXVelocity(pointerId);
    }

    @Deprecated
    public static float getYVelocity(VelocityTracker tracker, int pointerId) {
        return tracker.getYVelocity(pointerId);
    }

    private VelocityTrackerCompat() {
    }
}
