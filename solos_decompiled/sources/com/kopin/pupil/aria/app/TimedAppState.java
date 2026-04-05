package com.kopin.pupil.aria.app;

import android.util.Log;
import com.kopin.pupil.PupilSpeechRecognizer;

/* JADX INFO: loaded from: classes21.dex */
public class TimedAppState extends AppState {
    public static final long DEFAULT_CONFIRM_TIMEOUT = 10000;
    private final String mReturnState;
    private long mStartTime;
    private final long mTimeout;

    protected TimedAppState(String id, String[] grammar, String returnToState) {
        this(id, grammar, DEFAULT_CONFIRM_TIMEOUT, returnToState);
    }

    protected TimedAppState(String id, String[] grammar, long timeout, String returnToState) {
        super(id, grammar);
        this.mTimeout = timeout;
        this.mReturnState = returnToState;
    }

    @Override // com.kopin.pupil.aria.app.AppState
    protected void onEnter() {
        super.onEnter();
        this.mStartTime = System.currentTimeMillis();
        Log.d("TimedAppState", "Entering AppState: " + this.TAG);
    }

    @Override // com.kopin.pupil.aria.app.AppState
    public boolean holdIdle() {
        if (!checkTimeout()) {
            return true;
        }
        timedOut();
        return false;
    }

    private boolean checkTimeout() {
        long now = System.currentTimeMillis();
        Log.d("TimedAppState", "AppState: " + this.TAG + " time inactive: " + (now - this.mStartTime));
        return now - this.mStartTime > this.mTimeout;
    }

    protected boolean onTimedOut() {
        return false;
    }

    private void timedOut() {
        if (!onTimedOut()) {
            if (this.mReturnState == null) {
                appFinished();
            } else {
                setAppState(this.mReturnState);
            }
        }
    }

    @Override // com.kopin.pupil.aria.app.AppState
    public boolean onASRWarn(PupilSpeechRecognizer.ASRWarnings code) {
        Log.d("TimedAppState", "AppState: " + this.TAG + " onASRWarn");
        if (!checkTimeout()) {
            return super.onASRWarn(code);
        }
        timedOut();
        return false;
    }

    @Override // com.kopin.pupil.aria.app.AppState
    public void onSilence() {
        Log.d("TimedAppState", "AppState: " + this.TAG + " onSilence");
        if (checkTimeout()) {
            timedOut();
        } else {
            super.onSilence();
        }
    }
}
