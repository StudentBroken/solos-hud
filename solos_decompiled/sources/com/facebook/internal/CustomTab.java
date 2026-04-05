package com.facebook.internal;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;

/* JADX INFO: loaded from: classes41.dex */
public class CustomTab {
    private Uri uri;

    public CustomTab(String action, Bundle parameters) {
        this.uri = Utility.buildUri(ServerProtocol.getDialogAuthority(), ServerProtocol.getAPIVersion() + "/" + ServerProtocol.DIALOG_PATH + action, parameters == null ? new Bundle() : parameters);
    }

    public void openCustomTab(Activity activity, String packageName) {
        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
        customTabsIntent.intent.setPackage(packageName);
        customTabsIntent.launchUrl(activity, this.uri);
    }
}
