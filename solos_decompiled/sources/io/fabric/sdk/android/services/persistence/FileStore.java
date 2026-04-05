package io.fabric.sdk.android.services.persistence;

import java.io.File;

/* JADX INFO: loaded from: classes66.dex */
public interface FileStore {
    File getCacheDir();

    File getExternalCacheDir();

    File getExternalFilesDir();

    File getFilesDir();
}
