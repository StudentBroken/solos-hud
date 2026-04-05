package com.google.android.gms.maps.internal;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.wearable.activity.WearableActivity;

/* JADX INFO: loaded from: classes10.dex */
public final class zzbw {
    private zzbw() {
    }

    public static void zza(Bundle bundle, String str, Parcelable parcelable) {
        bundle.setClassLoader(zzbw.class.getClassLoader());
        Bundle bundle2 = bundle.getBundle("map_state");
        if (bundle2 == null) {
            bundle2 = new Bundle();
        }
        bundle2.setClassLoader(zzbw.class.getClassLoader());
        bundle2.putParcelable(str, parcelable);
        bundle.putBundle("map_state", bundle2);
    }

    public static void zzd(Bundle bundle, Bundle bundle2) {
        if (bundle == null || bundle2 == null) {
            return;
        }
        Parcelable parcelableZzg = zzg(bundle, "MapOptions");
        if (parcelableZzg != null) {
            zza(bundle2, "MapOptions", parcelableZzg);
        }
        Parcelable parcelableZzg2 = zzg(bundle, "StreetViewPanoramaOptions");
        if (parcelableZzg2 != null) {
            zza(bundle2, "StreetViewPanoramaOptions", parcelableZzg2);
        }
        Parcelable parcelableZzg3 = zzg(bundle, "camera");
        if (parcelableZzg3 != null) {
            zza(bundle2, "camera", parcelableZzg3);
        }
        if (bundle.containsKey("position")) {
            bundle2.putString("position", bundle.getString("position"));
        }
        if (bundle.containsKey(WearableActivity.EXTRA_LOWBIT_AMBIENT)) {
            bundle2.putBoolean(WearableActivity.EXTRA_LOWBIT_AMBIENT, bundle.getBoolean(WearableActivity.EXTRA_LOWBIT_AMBIENT, false));
        }
    }

    private static <T extends Parcelable> T zzg(Bundle bundle, String str) {
        if (bundle == null) {
            return null;
        }
        bundle.setClassLoader(zzbw.class.getClassLoader());
        Bundle bundle2 = bundle.getBundle("map_state");
        if (bundle2 == null) {
            return null;
        }
        bundle2.setClassLoader(zzbw.class.getClassLoader());
        return (T) bundle2.getParcelable(str);
    }
}
