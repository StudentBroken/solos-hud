package com.digits.sdk.android;

import android.app.Activity;

/* JADX INFO: loaded from: classes18.dex */
interface ActivityLifecycle {
    void onActivityResult(int i, int i2, Activity activity);

    void onDestroy();

    void onResume();
}
