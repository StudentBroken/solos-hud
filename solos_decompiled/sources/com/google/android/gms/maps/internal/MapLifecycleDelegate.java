package com.google.android.gms.maps.internal;

import com.google.android.gms.dynamic.LifecycleDelegate;
import com.google.android.gms.maps.OnMapReadyCallback;

/* JADX INFO: loaded from: classes10.dex */
public interface MapLifecycleDelegate extends LifecycleDelegate {
    void getMapAsync(OnMapReadyCallback onMapReadyCallback);
}
