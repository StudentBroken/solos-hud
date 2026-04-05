package io.fabric.sdk.android.services.settings;

/* JADX INFO: loaded from: classes66.dex */
public class FeaturesSettingsData {
    public final boolean collectAnalytics;
    public final boolean collectLoggedException;
    public final boolean collectReports;
    public final boolean promptEnabled;

    public FeaturesSettingsData(boolean promptEnabled, boolean collectLoggedException, boolean collectReports, boolean collectAnalytics) {
        this.promptEnabled = promptEnabled;
        this.collectLoggedException = collectLoggedException;
        this.collectReports = collectReports;
        this.collectAnalytics = collectAnalytics;
    }
}
