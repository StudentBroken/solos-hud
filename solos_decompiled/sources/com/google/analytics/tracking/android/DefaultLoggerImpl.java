package com.google.analytics.tracking.android;

import com.google.analytics.tracking.android.Logger;
import com.google.android.gms.common.util.VisibleForTesting;

/* JADX INFO: loaded from: classes49.dex */
class DefaultLoggerImpl implements Logger {

    @VisibleForTesting
    static final String LOG_TAG = "GAV3";
    private Logger.LogLevel mLogLevel = Logger.LogLevel.INFO;

    DefaultLoggerImpl() {
    }

    @Override // com.google.analytics.tracking.android.Logger
    public void verbose(String msg) {
        if (this.mLogLevel.ordinal() <= Logger.LogLevel.VERBOSE.ordinal()) {
            android.util.Log.v(LOG_TAG, formatMessage(msg));
        }
    }

    @Override // com.google.analytics.tracking.android.Logger
    public void info(String msg) {
        if (this.mLogLevel.ordinal() <= Logger.LogLevel.INFO.ordinal()) {
            android.util.Log.i(LOG_TAG, formatMessage(msg));
        }
    }

    @Override // com.google.analytics.tracking.android.Logger
    public void warn(String msg) {
        if (this.mLogLevel.ordinal() <= Logger.LogLevel.WARNING.ordinal()) {
            android.util.Log.w(LOG_TAG, formatMessage(msg));
        }
    }

    @Override // com.google.analytics.tracking.android.Logger
    public void error(String msg) {
        if (this.mLogLevel.ordinal() <= Logger.LogLevel.ERROR.ordinal()) {
            android.util.Log.e(LOG_TAG, formatMessage(msg));
        }
    }

    @Override // com.google.analytics.tracking.android.Logger
    public void error(Exception exception) {
        if (this.mLogLevel.ordinal() <= Logger.LogLevel.ERROR.ordinal()) {
            android.util.Log.e(LOG_TAG, null, exception);
        }
    }

    @Override // com.google.analytics.tracking.android.Logger
    public void setLogLevel(Logger.LogLevel level) {
        this.mLogLevel = level;
    }

    @Override // com.google.analytics.tracking.android.Logger
    public Logger.LogLevel getLogLevel() {
        return this.mLogLevel;
    }

    private String formatMessage(String msg) {
        return Thread.currentThread().toString() + ": " + msg;
    }
}
