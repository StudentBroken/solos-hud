package com.digits.sdk.android;

import android.os.Bundle;

/* JADX INFO: loaded from: classes18.dex */
class BundleManager {
    BundleManager() {
    }

    static boolean assertContains(Bundle bundle, String... keys) {
        if (bundle == null || keys == null) {
            return false;
        }
        for (String key : keys) {
            if (!bundle.containsKey(key)) {
                return false;
            }
        }
        return true;
    }
}
