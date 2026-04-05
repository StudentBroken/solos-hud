package com.google.tagmanager;

import com.google.tagmanager.Logger;

/* JADX INFO: loaded from: classes49.dex */
class DefaultLogger implements Logger {
    private static final String LOG_TAG = "GoogleTagManager";
    private Logger.LogLevel mLogLevel = Logger.LogLevel.WARNING;

    DefaultLogger() {
    }

    @Override // com.google.tagmanager.Logger
    public void e(String message) {
        if (this.mLogLevel.ordinal() <= Logger.LogLevel.ERROR.ordinal()) {
            android.util.Log.e(LOG_TAG, message);
        }
    }

    @Override // com.google.tagmanager.Logger
    public void e(String message, Throwable t) {
        if (this.mLogLevel.ordinal() <= Logger.LogLevel.ERROR.ordinal()) {
            android.util.Log.e(LOG_TAG, message, t);
        }
    }

    @Override // com.google.tagmanager.Logger
    public void w(String message) {
        if (this.mLogLevel.ordinal() <= Logger.LogLevel.WARNING.ordinal()) {
            android.util.Log.w(LOG_TAG, message);
        }
    }

    @Override // com.google.tagmanager.Logger
    public void w(String message, Throwable t) {
        if (this.mLogLevel.ordinal() <= Logger.LogLevel.WARNING.ordinal()) {
            android.util.Log.w(LOG_TAG, message, t);
        }
    }

    @Override // com.google.tagmanager.Logger
    public void i(String message) {
        if (this.mLogLevel.ordinal() <= Logger.LogLevel.INFO.ordinal()) {
            android.util.Log.i(LOG_TAG, message);
        }
    }

    @Override // com.google.tagmanager.Logger
    public void i(String message, Throwable t) {
        if (this.mLogLevel.ordinal() <= Logger.LogLevel.INFO.ordinal()) {
            android.util.Log.i(LOG_TAG, message, t);
        }
    }

    @Override // com.google.tagmanager.Logger
    public void d(String message) {
        if (this.mLogLevel.ordinal() <= Logger.LogLevel.DEBUG.ordinal()) {
            android.util.Log.d(LOG_TAG, message);
        }
    }

    @Override // com.google.tagmanager.Logger
    public void d(String message, Throwable t) {
        if (this.mLogLevel.ordinal() <= Logger.LogLevel.DEBUG.ordinal()) {
            android.util.Log.d(LOG_TAG, message, t);
        }
    }

    @Override // com.google.tagmanager.Logger
    public void v(String message) {
        if (this.mLogLevel.ordinal() <= Logger.LogLevel.VERBOSE.ordinal()) {
            android.util.Log.v(LOG_TAG, message);
        }
    }

    @Override // com.google.tagmanager.Logger
    public void v(String message, Throwable t) {
        if (this.mLogLevel.ordinal() <= Logger.LogLevel.VERBOSE.ordinal()) {
            android.util.Log.v(LOG_TAG, message, t);
        }
    }

    @Override // com.google.tagmanager.Logger
    public Logger.LogLevel getLogLevel() {
        return this.mLogLevel;
    }

    @Override // com.google.tagmanager.Logger
    public void setLogLevel(Logger.LogLevel logLevel) {
        this.mLogLevel = logLevel;
    }
}
