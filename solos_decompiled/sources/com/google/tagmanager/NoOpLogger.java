package com.google.tagmanager;

import com.google.tagmanager.Logger;

/* JADX INFO: loaded from: classes49.dex */
class NoOpLogger implements Logger {
    NoOpLogger() {
    }

    @Override // com.google.tagmanager.Logger
    public void e(String message) {
    }

    @Override // com.google.tagmanager.Logger
    public void e(String message, Throwable t) {
    }

    @Override // com.google.tagmanager.Logger
    public void w(String message) {
    }

    @Override // com.google.tagmanager.Logger
    public void w(String message, Throwable t) {
    }

    @Override // com.google.tagmanager.Logger
    public void i(String message) {
    }

    @Override // com.google.tagmanager.Logger
    public void i(String message, Throwable t) {
    }

    @Override // com.google.tagmanager.Logger
    public void d(String message) {
    }

    @Override // com.google.tagmanager.Logger
    public void d(String message, Throwable t) {
    }

    @Override // com.google.tagmanager.Logger
    public void v(String message) {
    }

    @Override // com.google.tagmanager.Logger
    public void v(String message, Throwable t) {
    }

    @Override // com.google.tagmanager.Logger
    public Logger.LogLevel getLogLevel() {
        return Logger.LogLevel.NONE;
    }

    @Override // com.google.tagmanager.Logger
    public void setLogLevel(Logger.LogLevel logLevel) {
    }
}
