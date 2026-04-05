package com.digits.sdk.android;

import android.app.Activity;
import android.os.Bundle;

/* JADX INFO: loaded from: classes18.dex */
public interface DigitsActivityDelegate extends ActivityLifecycle {
    int getLayoutId();

    void init(Activity activity, Bundle bundle);

    boolean isValid(Bundle bundle);
}
