package com.kopin.pupil.aria.app;

import com.goldeni.audio.ASR_RESULTS;
import com.kopin.pupil.PupilSpeechRecognizer;
import com.kopin.pupil.aria.tts.AriaTTS;
import com.kopin.pupil.aria.tts.ConversationPoint;
import com.kopin.pupil.vocon.Grammar;

/* JADX INFO: loaded from: classes21.dex */
public class AppState extends Grammar {
    final String TAG;
    private BaseAriaApp mApp;

    public AppState(String identifier) {
        super(identifier);
        this.TAG = identifier;
    }

    public AppState(String identifier, String[] commands) {
        super(identifier, commands);
        this.TAG = identifier;
    }

    protected ConversationPoint getOnEnterSpeech() {
        return null;
    }

    protected String getOnEnterSpeechText() {
        return null;
    }

    protected void onEnter() {
        ConversationPoint conv = getOnEnterSpeech();
        if (conv != null) {
            sayText(conv);
        } else {
            String text = getOnEnterSpeechText();
            if (text != null) {
                sayText(AriaTTS.SayPriority.VERBOSE, text);
            }
        }
        stateChanged();
    }

    protected void onExit() {
    }

    void setApp(BaseAriaApp app) {
        this.mApp = app;
    }

    protected void setAppState(String name) {
        if (this.mApp != null) {
            this.mApp.setAppState(name);
        }
    }

    protected void stateChanged() {
        if (this.mApp != null) {
            this.mApp.stateChanged();
        }
    }

    protected void appEvent(String message) {
        if (this.mApp != null) {
            this.mApp.appEvent(message);
        }
    }

    protected void appFinished() {
        if (this.mApp != null) {
            this.mApp.appFinished();
        }
    }

    protected void requestState(String name) {
        if (this.mApp != null) {
            this.mApp.requestState(name);
        }
    }

    protected void callApp(String app, String state, Object cb, String returnTo) {
        if (this.mApp != null) {
            this.mApp.callApp(app, state, cb, returnTo);
        }
    }

    protected void setExtra(Object extra) {
    }

    protected int sayText(AriaTTS.SayPriority priority, String text) {
        if (this.mApp == null) {
            return -1;
        }
        return this.mApp.sayText(priority, this.TAG, text);
    }

    protected int sayText(ConversationPoint conv) {
        if (this.mApp == null) {
            return -1;
        }
        return this.mApp.sayText(conv);
    }

    protected int sayText(AriaTTS.SayPriority priority, ConversationPoint conv) {
        if (this.mApp == null) {
            return -1;
        }
        return this.mApp.sayText(priority, conv);
    }

    public void onFinishedSaying(int id) {
    }

    public boolean onCommandResult(String cmd) {
        return false;
    }

    public void onSilence() {
    }

    public boolean onASRError(PupilSpeechRecognizer.ASRErrors code) {
        return false;
    }

    public boolean onASRWarn(PupilSpeechRecognizer.ASRWarnings code) {
        return false;
    }

    protected ASR_RESULTS getFullResults() {
        if (this.mApp != null) {
            return this.mApp.getFullResults();
        }
        return null;
    }

    protected boolean isDictationAvailable() {
        if (this.mApp != null) {
            return this.mApp.isDictationAvailable();
        }
        return false;
    }

    public boolean holdIdle() {
        return false;
    }
}
