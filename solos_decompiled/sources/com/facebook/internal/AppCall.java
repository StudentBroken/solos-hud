package com.facebook.internal;

import android.content.Intent;
import java.util.UUID;

/* JADX INFO: loaded from: classes41.dex */
public class AppCall {
    private static AppCall currentPendingCall;
    private UUID callId;
    private int requestCode;
    private Intent requestIntent;

    public static AppCall getCurrentPendingCall() {
        return currentPendingCall;
    }

    public static synchronized AppCall finishPendingCall(UUID callId, int requestCode) {
        AppCall pendingCall;
        pendingCall = getCurrentPendingCall();
        if (pendingCall != null && pendingCall.getCallId().equals(callId) && pendingCall.getRequestCode() == requestCode) {
            setCurrentPendingCall(null);
        } else {
            pendingCall = null;
        }
        return pendingCall;
    }

    private static synchronized boolean setCurrentPendingCall(AppCall appCall) {
        AppCall oldAppCall;
        oldAppCall = getCurrentPendingCall();
        currentPendingCall = appCall;
        return oldAppCall != null;
    }

    public AppCall(int requestCode) {
        this(requestCode, UUID.randomUUID());
    }

    public AppCall(int requestCode, UUID callId) {
        this.callId = callId;
        this.requestCode = requestCode;
    }

    public Intent getRequestIntent() {
        return this.requestIntent;
    }

    public UUID getCallId() {
        return this.callId;
    }

    public int getRequestCode() {
        return this.requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public void setRequestIntent(Intent requestIntent) {
        this.requestIntent = requestIntent;
    }

    public boolean setPending() {
        return setCurrentPendingCall(this);
    }
}
