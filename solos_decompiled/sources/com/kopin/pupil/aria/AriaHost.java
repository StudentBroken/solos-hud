package com.kopin.pupil.aria;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import com.goldeni.audio.ASR_RESULTS;
import com.kopin.accessory.AudioCodec;
import com.kopin.accessory.packets.ActionType;
import com.kopin.pupil.PupilDevice;
import com.kopin.pupil.PupilSpeechRecognizer;
import com.kopin.pupil.aria.AriaController;
import com.kopin.pupil.aria.Home;
import com.kopin.pupil.aria.app.BaseAriaApp;
import com.kopin.pupil.aria.debug.WaveFileRecorder;
import com.kopin.pupil.aria.tts.AriaTTS;

/* JADX INFO: loaded from: classes43.dex */
public class AriaHost implements AriaController {
    private static final long IDLE_TIMEOUT_POLL = 5000;
    private static final String TAG = "Aria";
    private static PupilSpeechRecognizer mVoiceRecognition;
    private boolean isActive;
    private boolean isEnabled;
    private AriaStatus mClientCb;
    private final Context mContext;
    private Home mHomeApp;
    private long mLastVocon;
    private static AriaTTS.SayPriority[] TTS_NO_NAME_LEVELS = {AriaTTS.SayPriority.OFF, AriaTTS.SayPriority.FIRST_TIME, AriaTTS.SayPriority.TERSE, AriaTTS.SayPriority.HINT};
    private static AriaTTS.SayPriority[] TTS_HAS_NAME_LEVELS = {AriaTTS.SayPriority.OFF, AriaTTS.SayPriority.FIRST_TIME, AriaTTS.SayPriority.TERSE, AriaTTS.SayPriority.VERBOSE};
    private final VoConPolicy mPolicy = new VoConPolicy() { // from class: com.kopin.pupil.aria.AriaHost.1
        @Override // com.kopin.pupil.aria.VoConPolicy
        public boolean isEnabled() {
            return AriaHost.this.isEnabled && com.kopin.solos.common.config.Config.VOCON_ENABLED;
        }

        @Override // com.kopin.pupil.aria.VoConPolicy
        public boolean shouldWake() {
            return isEnabled();
        }

        @Override // com.kopin.pupil.aria.VoConPolicy
        public boolean shouldSleep() {
            return Config.SLEEP_ON_FINISHED && !Prefs.isVoconAlwaysOn();
        }
    };
    private final BaseAriaApp.AriaAppHost mAppHost = new BaseAriaApp.AriaAppHost() { // from class: com.kopin.pupil.aria.AriaHost.2
        @Override // com.kopin.pupil.aria.app.BaseAriaApp.AriaAppHost
        public void requestWake() {
            AriaHost.this.onWake(false);
        }

        @Override // com.kopin.pupil.aria.app.BaseAriaApp.AriaAppHost
        public void onAppFinished() {
            AriaHost.this.onSleep(AriaHost.this.mPolicy.shouldSleep());
        }

        @Override // com.kopin.pupil.aria.app.BaseAriaApp.AriaAppHost
        public void requestAppState(BaseAriaApp app, String state) {
        }

        @Override // com.kopin.pupil.aria.app.BaseAriaApp.AriaAppHost
        public void onAppStart(String app) {
            Log.d(AriaHost.TAG, "Starting app: " + app);
            if (AriaHost.this.mClientCb != null) {
                if (app == null || app.contentEquals("home")) {
                    AriaHost.this.mClientCb.onHome();
                } else {
                    AriaHost.this.mClientCb.onAppStart(app);
                }
            }
        }

        @Override // com.kopin.pupil.aria.app.BaseAriaApp.AriaAppHost
        public void onAppState(String app, String state, String description) {
            Log.d(AriaHost.TAG, app + ": " + state + ": " + description);
            if (AriaHost.this.mClientCb != null && description != null) {
                AriaHost.this.mClientCb.onAppStatus(app, description);
            }
        }

        @Override // com.kopin.pupil.aria.app.BaseAriaApp.AriaAppHost
        public boolean isIdle() {
            return false;
        }

        @Override // com.kopin.pupil.aria.app.BaseAriaApp.AriaAppHost
        public String getSpeechSubstitution(String subs) {
            return null;
        }
    };
    private final PupilSpeechRecognizer.SpeechConfig mSpeechConfig = new PupilSpeechRecognizer.SpeechConfig() { // from class: com.kopin.pupil.aria.AriaHost.3
        private boolean reqDictation = false;

        @Override // com.kopin.pupil.PupilSpeechRecognizer.SpeechConfig
        public ASR_RESULTS getLastResults() {
            if (AriaHost.mVoiceRecognition != null) {
                return AriaHost.mVoiceRecognition.getLastResults();
            }
            return null;
        }

        @Override // com.kopin.pupil.PupilSpeechRecognizer.SpeechConfig
        public void setSilenceTimeout(int silenceMillis) {
        }

        @Override // com.kopin.pupil.PupilSpeechRecognizer.SpeechConfig
        public void setDictation(boolean onOrOff) {
            this.reqDictation = onOrOff;
        }

        @Override // com.kopin.pupil.PupilSpeechRecognizer.SpeechConfig
        public void restartASR(boolean andReloadGrammar, boolean andRestartApps) {
            if (AriaHost.mVoiceRecognition != null && AriaHost.mVoiceRecognition.isListening()) {
                if (andRestartApps) {
                    AriaHost.this.mHomeApp.onStop();
                }
                AriaHost.mVoiceRecognition.stop();
                if (andRestartApps) {
                    AriaHost.this.mHomeApp.onStart();
                }
                if (andReloadGrammar) {
                    AriaHost.mVoiceRecognition.setGrammar(AriaHost.this.mHomeApp);
                }
                AriaHost.mVoiceRecognition.start(this.reqDictation);
                AriaHost.this.refreshMicStatus();
            }
            AriaHost.this.refreshAppStatus();
        }

        @Override // com.kopin.pupil.PupilSpeechRecognizer.SpeechConfig
        public boolean isDictationAvailable() {
            return false;
        }
    };
    private final Home.CommandHandlerObserver mCommandWatcher = new Home.CommandHandlerObserver() { // from class: com.kopin.pupil.aria.AriaHost.4
        @Override // com.kopin.pupil.aria.Home.CommandHandlerObserver
        public void onCommandHandled(String app, String cmd) {
            Log.d(AriaHost.TAG, "  Handled by: " + app);
            if (AriaHost.this.mClientCb != null) {
                AriaHost.this.mClientCb.onCommandRecognised(app, cmd);
            }
        }
    };
    private final PupilSpeechRecognizer.ResultListener mSpeechRecognisedListener = new PupilSpeechRecognizer.ResultListener() { // from class: com.kopin.pupil.aria.AriaHost.5
        @Override // com.kopin.pupil.PupilSpeechRecognizer.ResultListener
        public void onListening() {
            AriaHost.this.refreshMicStatus();
        }

        @Override // com.kopin.pupil.PupilSpeechRecognizer.ResultListener
        public boolean onResult(String text, PupilSpeechRecognizer.SpeechConfig configurator) {
            Log.d(AriaHost.TAG, "Command recognised: " + text);
            AriaHost.this.mLastVocon = System.currentTimeMillis();
            boolean reload = AriaHost.this.mHomeApp.onCommand(0, text, AriaHost.this.mCommandWatcher);
            if (reload) {
                AriaHost.mVoiceRecognition.setGrammar(AriaHost.this.mHomeApp);
            }
            AriaHost.this.refreshMicStatus();
            AriaHost.this.refreshAppStatus();
            return AriaHost.this.isActive;
        }

        @Override // com.kopin.pupil.PupilSpeechRecognizer.ResultListener
        public void onSilence() {
            AriaHost.this.mLastVocon = System.currentTimeMillis();
            AriaHost.this.mHomeApp.onSilence();
        }

        @Override // com.kopin.pupil.PupilSpeechRecognizer.ResultListener
        public void onWarning(PupilSpeechRecognizer.ASRWarnings code) {
            AriaHost.this.mLastVocon = System.currentTimeMillis();
            boolean shouldQuit = AriaHost.this.mHomeApp.onASRWarn(code);
            if (shouldQuit) {
                AriaHost.this.onSleep(true);
            }
        }

        @Override // com.kopin.pupil.PupilSpeechRecognizer.ResultListener
        public void onError(PupilSpeechRecognizer.ASRErrors code) {
            boolean restart = AriaHost.this.mHomeApp.onASRError(code);
            if (restart) {
                AriaHost.mVoiceRecognition.start(false);
            }
        }
    };
    private final Runnable mIdleTimer = new Runnable() { // from class: com.kopin.pupil.aria.AriaHost.6
        @Override // java.lang.Runnable
        public void run() {
            if (AriaHost.this.isActive) {
                Log.d(AriaHost.TAG, "IdleTimer active");
                if (!AriaHost.this.mHomeApp.holdIdle()) {
                    long now = System.currentTimeMillis();
                    Log.d(AriaHost.TAG, "  Idle not held, inactive time: " + (now - AriaHost.this.mLastVocon));
                    if (now - AriaHost.this.mLastVocon > Config.INACTIVITY_TIMEOUT) {
                        Log.d(AriaHost.TAG, "  Inactive timeout");
                        AriaHost.this.mHomeApp.onInactivity();
                        if (!Config.SLEEP_ON_INACTIVITY) {
                            AriaHost.this.mLastVocon = System.currentTimeMillis();
                        } else {
                            return;
                        }
                    }
                }
                AriaHost.this.mIdleHandler.postDelayed(AriaHost.this.mIdleTimer, 5000L);
            }
        }
    };
    private final Runnable mOnIdle = new Runnable() { // from class: com.kopin.pupil.aria.AriaHost.7
        @Override // java.lang.Runnable
        public void run() {
            AriaHost.this.mHomeApp.onIdle();
        }
    };
    private final BroadcastReceiver mDebugCommandReceiver = new BroadcastReceiver() { // from class: com.kopin.pupil.aria.AriaHost.8
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String cmd = intent.getStringExtra("command");
            if (cmd != null && !cmd.isEmpty()) {
                AriaHost.this.mSpeechRecognisedListener.onResult(cmd, AriaHost.this.mSpeechConfig);
            }
        }
    };
    private Handler mIdleHandler = new Handler();

    @Override // com.kopin.pupil.aria.AriaController
    public void setStatusListener(AriaStatus cb) {
        this.mClientCb = cb;
    }

    @Override // com.kopin.pupil.aria.AriaController
    public void setListeningMode(boolean pushToTalk, int idleTimeout) {
    }

    @Override // com.kopin.pupil.aria.AriaController
    public void setEnabled(boolean onOrOff) {
        this.isEnabled = onOrOff;
    }

    @Override // com.kopin.pupil.aria.AriaController
    public boolean isEnabled() {
        return this.mPolicy.isEnabled();
    }

    @Override // com.kopin.pupil.aria.AriaController
    public void start() {
        onWake(true);
    }

    @Override // com.kopin.pupil.aria.AriaController
    public void stop() {
        onSleep(true);
    }

    @Override // com.kopin.pupil.aria.AriaController
    public void setKeywordMode(boolean onOrOff) {
        this.mHomeApp.setKeywordMode(onOrOff);
    }

    @Override // com.kopin.pupil.aria.AriaController
    public boolean isListening() {
        if (mVoiceRecognition != null) {
            return mVoiceRecognition.isListening();
        }
        return false;
    }

    @Override // com.kopin.pupil.aria.AriaController
    public boolean hasError() {
        if (mVoiceRecognition != null) {
            return mVoiceRecognition.hasError();
        }
        return false;
    }

    @Override // com.kopin.pupil.aria.AriaController
    public boolean hasDictation() {
        if (mVoiceRecognition != null) {
            return mVoiceRecognition.hasDictation();
        }
        return false;
    }

    @Override // com.kopin.pupil.aria.AriaController
    public void addApp(String name, AriaController.AriaAppFactory app) {
        if (this.mHomeApp != null && app != null) {
            this.mHomeApp.addApp(name, app);
        }
    }

    public AriaHost(Context context, AriaController.AriaAppFactory homeApp) {
        this.mContext = context;
        Config.init(context);
        Prefs.init(context);
        AudioPlayer.init(context);
        this.mHomeApp = homeApp != null ? (Home) homeApp.createApp(context, this.mAppHost, this.mSpeechConfig, this.mPolicy) : new Home(context, null, this.mAppHost, this.mSpeechConfig, this.mPolicy, null);
        mVoiceRecognition = new PupilSpeechRecognizer(context);
        mVoiceRecognition.setListener(this.mSpeechRecognisedListener);
    }

    protected void finalize() throws Throwable {
        super.finalize();
        if (mVoiceRecognition != null) {
            mVoiceRecognition.shutdown();
        }
        this.mContext.unregisterReceiver(this.mDebugCommandReceiver);
    }

    @Override // com.kopin.pupil.aria.AriaController
    public String getStatus() {
        return this.mHomeApp.getAppStatus();
    }

    private void setTalkative(String name, int level) {
        AriaTTS.SayPriority def = (name == null || name.trim().isEmpty()) ? TTS_NO_NAME_LEVELS[level] : TTS_HAS_NAME_LEVELS[level];
        AriaTTS.setTalkativeLevel(def);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onWake(boolean userAction) {
        if (this.mPolicy.shouldWake() && Config.IS_PROVISIONED) {
            Log.d(TAG, "Wake");
            this.mHomeApp.onStart(userAction);
            refreshAppStatus();
            if (mVoiceRecognition != null) {
                mVoiceRecognition.setGrammar(this.mHomeApp);
                mVoiceRecognition.start(false);
            }
            AudioRecorder.start(this);
            refreshMicStatus();
            startIdleTimer();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSleep(boolean stopListening) {
        if (stopListening) {
            Log.d(TAG, "Sleep");
            this.isActive = false;
            boolean processing = isListening();
            AudioRecorder.stop();
            if (mVoiceRecognition != null) {
                mVoiceRecognition.stop();
            }
            refreshMicStatus();
            this.mHomeApp.onStop();
            if (processing) {
                PupilDevice.sendAction(ActionType.PLAY_PROCESSING_CHIME);
            }
        }
        refreshAppStatus();
        if (this.mClientCb != null) {
            this.mClientCb.onHome();
        }
        this.mIdleHandler.postDelayed(this.mOnIdle, 5000L);
    }

    private void refreshHeadsetStatus(boolean isConnected, boolean updateMode) {
        if (this.mClientCb != null) {
            this.mClientCb.onHeadsetStatusChanged(isConnected, updateMode);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshAppStatus() {
        if (this.mClientCb != null) {
            this.mClientCb.onAppStatusChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshMicStatus() {
        if (this.mClientCb != null) {
            this.mClientCb.onAudioStatusChanged();
        }
    }

    @Override // com.kopin.pupil.aria.AudioRecorder.AudioSink
    public void onAudioReceived(byte[] data) {
        if (mVoiceRecognition != null) {
            PupilSpeechRecognizer pupilSpeechRecognizer = mVoiceRecognition;
            AudioCodec audioCodec = AudioCodec.PCM;
            if (PupilDevice.isConnected()) {
            }
            pupilSpeechRecognizer.processAudio(data, audioCodec, 1);
        }
        this.mHomeApp.onAudioData(data);
        if (WaveFileRecorder.isActive()) {
            WaveFileRecorder.writeData(data);
        }
    }

    @Override // com.kopin.pupil.aria.AriaController
    public void onAudioEnd(int arg0) {
        AriaTTS.finishedSaying(arg0);
        AudioRecorder.unmuteRemote();
    }

    private void startIdleTimer() {
        this.isActive = true;
        if (Config.SLEEP_ON_INACTIVITY || Config.HOME_ON_INACTIVITY) {
            this.mLastVocon = System.currentTimeMillis();
            this.mIdleHandler.postDelayed(this.mIdleTimer, 5000L);
        }
    }
}
