package com.kopin.pupil.aria.app;

import com.goldeni.audio.ASR_RESULTS;
import com.kopin.pupil.PupilSpeechRecognizer;
import com.kopin.pupil.aria.tts.AriaTTS;
import com.kopin.pupil.aria.tts.ConversationPoint;
import java.util.HashMap;

/* JADX INFO: loaded from: classes21.dex */
public abstract class BaseAriaApp extends AppState implements AriaTTS.TTSListener {
    private boolean isActive;
    private AppState mCurState;
    private final AriaAppHost mParent;
    private final PupilSpeechRecognizer.SpeechConfig mSpeechHost;
    private HashMap<String, AppState> mStates;

    public interface AriaAppHost {
        String getSpeechSubstitution(String str);

        boolean isIdle();

        void onAppFinished();

        void onAppStart(String str);

        void onAppState(String str, String str2, String str3);

        void requestAppState(BaseAriaApp baseAriaApp, String str);

        void requestWake();
    }

    public abstract HeadCommand getHeadCommand();

    public abstract String[] getKeywords();

    public BaseAriaApp(String identifier, String[] commands, AriaAppHost appHost, PupilSpeechRecognizer.SpeechConfig speechHost) {
        super(identifier, commands);
        this.mStates = new HashMap<>();
        this.isActive = false;
        this.mCurState = this;
        this.mParent = appHost;
        this.mSpeechHost = speechHost;
    }

    public boolean isEnabled() {
        return true;
    }

    public void enable() {
    }

    public void disable() {
    }

    public String getStatus() {
        return this.mCurState != this ? this.mCurState.TAG : this.isActive ? "active" : "idle";
    }

    public boolean needsToWho() {
        return false;
    }

    public boolean requestAudioIn() {
        return false;
    }

    @Override // com.kopin.pupil.aria.app.AppState
    protected void appFinished() {
        if (this.mParent != null) {
            this.mParent.onAppFinished();
        }
    }

    @Override // com.kopin.pupil.aria.app.AppState
    protected void requestState(String name) {
        if (this.mParent != null) {
            this.mParent.requestAppState(this, name);
        }
    }

    protected void addState(String id, AppState state) {
        state.setApp(this);
        this.mStates.put(id, state);
    }

    private void resetState() {
        setAppState(null);
    }

    @Override // com.kopin.pupil.aria.app.AppState
    protected void setAppState(String name) {
        if (this.mCurState != null) {
            this.mCurState.onExit();
        }
        this.mCurState = name != null ? this.mStates.get(name) : null;
        if (this.mCurState == null) {
            this.mCurState = this;
        }
        if (this.mCurState != null) {
            this.mCurState.onEnter();
        }
    }

    @Override // com.kopin.pupil.aria.app.AppState
    protected void setExtra(Object extra) {
        if (this.mCurState != this) {
            this.mCurState.setExtra(extra);
        }
    }

    @Override // com.kopin.pupil.aria.app.AppState
    protected void stateChanged() {
    }

    @Override // com.kopin.pupil.aria.app.AppState
    protected void appEvent(String message) {
        if (this.mParent != null) {
            this.mParent.onAppState(this.TAG, this.mCurState.TAG, message);
        }
    }

    @Override // com.kopin.pupil.vocon.Grammar
    public String compile() {
        return this.mCurState == this ? super.compile() : this.mCurState.compile();
    }

    @Override // com.kopin.pupil.vocon.Grammar
    public String compileDefinitions() {
        return this.mCurState == this ? super.compileDefinitions() : this.mCurState.compileDefinitions();
    }

    @Override // com.kopin.pupil.vocon.Grammar
    public boolean needsDictation() {
        return this.mCurState == this ? super.needsDictation() : this.mCurState.needsDictation();
    }

    public void onStart() {
        onStart(null);
        ConversationPoint conv = getOnStartSpeech();
        if (conv != null) {
            sayText(conv);
            return;
        }
        String text = getOnStartSpeechText();
        if (text != null) {
            sayText(AriaTTS.SayPriority.VERBOSE, text);
        }
    }

    public void onStart(String state) {
        this.isActive = true;
        setAppState(state);
    }

    public void onStart(String state, Object extra) {
        this.isActive = true;
        setAppState(state);
        setExtra(extra);
    }

    public void onStop() {
        this.isActive = false;
        setAppState(null);
    }

    public void onIdle() {
    }

    public boolean onButtonPress(boolean isShort) {
        return false;
    }

    protected ConversationPoint getOnStartSpeech() {
        return null;
    }

    protected String getOnStartSpeechText() {
        return null;
    }

    @Override // com.kopin.pupil.aria.app.AppState
    protected int sayText(AriaTTS.SayPriority priority, String text) {
        return sayText(priority, this.TAG, text);
    }

    protected int sayText(AriaTTS.SayPriority priority, String tag, String text) {
        return AriaTTS.sayText(priority, tag, text, this);
    }

    @Override // com.kopin.pupil.aria.app.AppState
    protected int sayText(ConversationPoint conv) {
        return AriaTTS.sayText(AriaTTS.SayPriority.VERBOSE, conv, this);
    }

    @Override // com.kopin.pupil.aria.app.AppState
    protected int sayText(AriaTTS.SayPriority priority, ConversationPoint conv) {
        return AriaTTS.sayText(priority, conv, this);
    }

    @Override // com.kopin.pupil.aria.tts.AriaTTS.TTSListener
    public void onStartSaying(int id, String tag, String finalText) {
        appEvent(finalText);
    }

    @Override // com.kopin.pupil.aria.tts.AriaTTS.TTSListener
    public void onFinishedSaying(int id, String tag) {
        if (this.mCurState == this) {
            onFinishedSaying(id);
        } else {
            this.mCurState.onFinishedSaying(id);
        }
    }

    public String getSpeechSubstitution(String tag, String subs) {
        if (this.mParent == null) {
            return null;
        }
        return this.mParent.getSpeechSubstitution(subs);
    }

    @Override // com.kopin.pupil.aria.app.AppState
    protected ASR_RESULTS getFullResults() {
        return this.mSpeechHost.getLastResults();
    }

    public boolean onCommand(int id, String cmd) {
        return this.mCurState.onCommandResult(cmd);
    }

    @Override // com.kopin.pupil.aria.app.AppState
    public boolean onCommandResult(String cmd) {
        return this.mCurState == this ? super.onCommandResult(cmd) : this.mCurState.onCommandResult(cmd);
    }

    @Override // com.kopin.pupil.aria.app.AppState
    public boolean onASRError(PupilSpeechRecognizer.ASRErrors code) {
        return this.mCurState == this ? super.onASRError(code) : this.mCurState.onASRError(code);
    }

    @Override // com.kopin.pupil.aria.app.AppState
    public boolean onASRWarn(PupilSpeechRecognizer.ASRWarnings code) {
        if (this.mCurState != this) {
            return super.onASRWarn(code);
        }
        sayText(AriaTTS.SayPriority.TERSE, CommonTTS.SORRY_DIDNT_UNDERSTAND);
        return false;
    }

    public void onAudioData(byte[] buf) {
    }

    protected void grammarChanged(boolean reset) {
        if (this.mSpeechHost != null) {
            this.mSpeechHost.setDictation(needsDictation());
            this.mSpeechHost.restartASR(true, reset);
        }
    }

    @Override // com.kopin.pupil.aria.app.AppState
    protected boolean isDictationAvailable() {
        if (this.mSpeechHost != null) {
            return this.mSpeechHost.isDictationAvailable();
        }
        return false;
    }

    @Override // com.kopin.pupil.aria.app.AppState
    public boolean holdIdle() {
        return this.mCurState == this ? super.holdIdle() : this.mCurState.holdIdle();
    }
}
