package io.fabric.sdk.android.services.persistence;

import android.content.SharedPreferences;

/* JADX INFO: loaded from: classes66.dex */
public interface PreferenceStore {
    SharedPreferences.Editor edit();

    SharedPreferences get();

    boolean save(SharedPreferences.Editor editor);
}
