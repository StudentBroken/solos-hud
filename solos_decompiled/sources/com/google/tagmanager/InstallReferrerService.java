package com.google.tagmanager;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import com.google.analytics.tracking.android.CampaignTrackingService;
import com.google.android.gms.common.util.VisibleForTesting;

/* JADX INFO: loaded from: classes49.dex */
public final class InstallReferrerService extends IntentService {

    @VisibleForTesting
    Context contextOverride;

    @VisibleForTesting
    CampaignTrackingService gaService;

    public InstallReferrerService(String name) {
        super(name);
    }

    public InstallReferrerService() {
        super("InstallReferrerService");
    }

    @Override // android.app.IntentService
    protected void onHandleIntent(Intent intent) {
        String referrer = intent.getStringExtra("referrer");
        Context context = this.contextOverride != null ? this.contextOverride : getApplicationContext();
        InstallReferrerUtil.saveInstallReferrer(context, referrer);
        forwardToGoogleAnalytics(context, intent);
    }

    private void forwardToGoogleAnalytics(Context context, Intent intent) {
        if (this.gaService == null) {
            this.gaService = new CampaignTrackingService();
        }
        this.gaService.processIntent(context, intent);
    }
}
