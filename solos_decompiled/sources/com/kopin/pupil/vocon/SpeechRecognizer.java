package com.kopin.pupil.vocon;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.goldeni.audio.ASR_RESULTS;
import com.goldeni.audio.GIAudioNative;
import com.goldeni.audio.GISpeechRecognizerListener;
import com.goldeni.audio.GISpeechRecognizerNative;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class SpeechRecognizer implements GISpeechRecognizerListener {
    private static final int ASR_DICTATION_START = 4;
    private static final int ASR_INITIALZE = 0;
    private static final int ASR_RESTART = 5;
    private static final int ASR_SHUTDOWN = 3;
    private static final int ASR_START = 1;
    private static final int ASR_STOP = 2;
    private static final String CHINESE_CLC = "clc_mnc_cfg3_v6_0_5.dat";
    private static final String CHINESE_LANG = "acmod5_4000_mnc_gen_car_f16_v1_0_0.dat";
    private static final boolean DEBUG = true;
    public static final int DEFAULT_DEBUG_LEVEL = 5;
    private static final String DEFAULT_GRAMMAR = "main menu";
    private static final String DEFAULT_LANGUAGE = "en_US";
    public static final int DEFAULT_NCS_BALANCE = 0;
    public static final int DEFAULT_POSTGAIN_LEVEL = 0;
    public static final int DEFAULT_PREGAIN_LEVEL = 40;
    public static final int DEFAULT_VAD_THRESHOLD = 25000;
    private static final String ENGLISH_CLC = "clc_enu_cfg3_v6_0_2.dat";
    private static final String ENGLISH_LANG = "acmod5_4000_enu_gen_car_f16_v2_0_0.dat";
    private static final String JAPANESE_CLC = "clc_jpj_cfg3_v6_0_3.dat";
    private static final String JAPANESE_LANG = "acmod5_4000_jpj_gen_car_f16_v2_0_0.dat";
    private static final String KOREAN_CLC = "clc_kok_cfg3_v1_0_5.dat";
    private static final String KOREAN_LANG = "acmod4_900_kok_gen_car_f16_v1_0_1.dat";
    private static final int MAX_GRAMMAR_SIZE = 4950;
    private static final boolean NUANCE_DEBUG = true;
    private static final String TAG = "PupilVocon";
    private Context mContext;
    private MyHandler mHandler;
    private HandlerThread mHandlerThread;
    private OnASRResultsListener mResultListener;
    private String mCurrentGrammar = DEFAULT_GRAMMAR;
    private String mDictationLanguage = DEFAULT_LANGUAGE;
    private boolean isProcessing = false;
    private OutputStreamWriter mLog = null;
    private int mLogCount = 0;
    public Locale mCurrentLocale = null;

    public interface OnASRResultsListener {
        void onASRNoResults(SpeechRecognizer speechRecognizer, boolean z);

        void onASRResults(SpeechRecognizer speechRecognizer, ASR_RESULTS asr_results, String str);

        void onASRStarted(boolean z);

        void onASRStopped(boolean z, int i);

        void onSilenceDetected();
    }

    static /* synthetic */ int access$308(SpeechRecognizer x0) {
        int i = x0.mLogCount;
        x0.mLogCount = i + 1;
        return i;
    }

    public SpeechRecognizer(Context context, OnASRResultsListener listener, boolean useExternalAudio) {
        this.mResultListener = null;
        this.mContext = null;
        this.mHandlerThread = null;
        this.mHandler = null;
        String[] versions = GIAudioNative.GetVersion();
        Log.d(TAG, "Loading GIAudio version: " + versions[0] + "/" + versions[1]);
        this.mResultListener = listener;
        this.mContext = context;
        this.mHandlerThread = new HandlerThread("VOCON Handler Thread");
        this.mHandlerThread.start();
        this.mHandler = new MyHandler(this.mHandlerThread.getLooper(), this);
        Message msg = new Message();
        msg.what = 0;
        this.mHandler.sendMessage(msg);
    }

    public void changeListener(OnASRResultsListener listener) {
        if (this.mLog != null) {
            try {
                this.mLog.close();
                this.mLog = null;
            } catch (IOException e) {
            }
        }
        try {
            this.mLog = new OutputStreamWriter(new FileOutputStream(new File(isExternalStorageWritable() ? this.mContext.getExternalFilesDir(null) : this.mContext.getFilesDir(), "nuance.log")));
            this.mLogCount = 0;
            this.mLog.write(String.valueOf(this.mLogCount) + "\n");
            this.mLog.flush();
        } catch (IOException e2) {
        }
        this.mResultListener = listener;
    }

    public void startASR(String... grammar) {
        updateGrammar(grammar);
        Log.v(TAG, "Starting ASR engine");
        this.mHandler.sendEmptyMessage(1);
    }

    public void startASR(String grammar) {
        updateGrammar(grammar);
        Log.v(TAG, "Starting ASR engine");
        this.mHandler.sendEmptyMessage(1);
    }

    public void startASRWithDictation(String language, String... grammar) {
        updateGrammar(grammar);
        updateDication(language);
        Log.v(TAG, "Starting ASR with dictation engine");
        this.mHandler.sendEmptyMessage(4);
    }

    public void setDictationLanguage(String language) {
        updateDication(language);
    }

    public void setGrammar(String grammar) {
        this.mCurrentGrammar = grammar;
    }

    public void start(boolean withDictation) {
        this.mHandler.sendEmptyMessage(withDictation ? 4 : 1);
    }

    public void stop() {
        ExternalAudioReceiver.setIsListening(false);
        GISpeechRecognizerNative.Stop();
    }

    public void shutdown() {
        try {
            this.mLog.close();
        } catch (IOException e) {
        }
        this.mLog = null;
        this.mHandler.sendEmptyMessage(3);
    }

    @Override // com.goldeni.audio.GISpeechRecognizerListener
    public void onSpeechRecognitionEvent(int msgType, String message, int arg) {
        Log.v(TAG, "Message Type = " + getMessageType(msgType) + " Message = " + message + " MessageArg = " + arg);
        switch (msgType) {
            case 10:
                if (this.isProcessing) {
                    if ((arg == 10 || arg == 20) && this.mResultListener != null) {
                        this.mResultListener.onASRNoResults(this, true);
                    }
                    this.isProcessing = false;
                    break;
                } else {
                    this.isProcessing = arg == 30;
                    if (this.isProcessing && this.mResultListener != null) {
                        this.mResultListener.onSilenceDetected();
                        break;
                    }
                }
                break;
            case 20:
                ExternalAudioReceiver.setIsListening(false);
                Log.v(TAG, "MESSAGE RECOGNIZED: " + message);
                if (handleResults()) {
                    Log.v(TAG, "Re-start engine");
                    Message msg = new Message();
                    msg.what = 5;
                    this.mHandler.sendMessageDelayed(msg, 200L);
                }
                break;
            case 30:
                if (ExternalAudioReceiver.isListening()) {
                    Log.v(TAG, "Re-start engine");
                    Message msg2 = new Message();
                    msg2.what = 5;
                    this.mHandler.sendMessageDelayed(msg2, 200L);
                } else {
                    Log.v(TAG, "Engine stopped");
                    if (this.mResultListener != null) {
                        this.mResultListener.onASRStopped(false, 0);
                    }
                }
                break;
            case 40:
                if (this.mResultListener != null) {
                    this.mResultListener.onASRStopped(true, arg);
                }
                break;
        }
    }

    private void updateGrammar(String... grammar) {
        String newCommands = "";
        for (int i = 0; i < grammar.length; i++) {
            if (i == 0) {
                newCommands = grammar[i];
            } else {
                newCommands = newCommands + "|" + grammar[i];
            }
        }
        updateGrammar(newCommands);
    }

    private void updateGrammar(String newCommands) {
        if (newCommands.length() > MAX_GRAMMAR_SIZE) {
            newCommands = newCommands.substring(0, MAX_GRAMMAR_SIZE);
        }
        this.mCurrentGrammar = newCommands;
    }

    private void updateDication(String language) {
        this.mLogCount++;
        try {
            this.mLog.write(String.valueOf(this.mLogCount) + "\n");
            this.mLog.flush();
        } catch (IOException e) {
        }
        if (language == null || !language.isEmpty()) {
        }
    }

    private boolean handleResults() {
        ASR_RESULTS asrResults = new ASR_RESULTS();
        GISpeechRecognizerNative.GetResults(asrResults);
        Log.v(TAG, "Local Results = " + asrResults.getLocalTotalResults());
        for (int i = 0; i < asrResults.getLocalTotalResults(); i++) {
            try {
                Log.v(TAG, String.format("  '%s' [%d]", asrResults.getLocalPhrase(i), Integer.valueOf(asrResults.getLocalConfidence(i))));
            } catch (Exception e) {
            }
        }
        Log.v(TAG, "Remote Results = " + asrResults.getDictationTotalResults());
        try {
            Log.v(TAG, String.format("  '%s'", asrResults.getDictation()));
        } catch (Exception e2) {
        }
        if (this.mResultListener != null) {
            String phrase = "";
            try {
                phrase = asrResults.getLocalPhrase(0);
            } catch (Exception e3) {
            }
            try {
                this.mResultListener.onASRResults(this, asrResults, phrase);
            } catch (Exception e4) {
                e4.printStackTrace();
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initializeSpeechEngine() {
        Locale currentLocale = this.mContext.getResources().getConfiguration().locale;
        this.mCurrentLocale = currentLocale;
        String cacheFolder = this.mContext.getExternalCacheDir().getAbsolutePath() + "/vocon/" + currentLocale.getLanguage() + "/";
        File cache = new File(cacheFolder);
        if (!cache.exists() && !cache.mkdirs()) {
            Log.e(TAG, "Failed to create cache.");
        }
        File configFile = new File(cache, "vocon3200_asr.dat");
        if (!configFile.exists()) {
            copyFileFromAsset("vocon3200_asr.dat", configFile);
        }
        Locale cleanLocale = Language.sanitise(currentLocale.toString());
        this.mDictationLanguage = cleanLocale.toString();
        String lang = getLang(currentLocale);
        String clc = getCLC(currentLocale);
        File sdPath = isExternalStorageWritable() ? this.mContext.getExternalFilesDir(null) : this.mContext.getFilesDir();
        File langFile = new File(sdPath, lang);
        if (!langFile.exists()) {
            copyFileFromAsset(lang, langFile);
        }
        File clcFile = new File(sdPath, clc);
        if (!clcFile.exists()) {
            copyFileFromAsset(clc, clcFile);
        }
        Log.d(TAG, "Initialising for language: " + this.mDictationLanguage);
        Log.d(TAG, "  Lang file: " + langFile.getAbsolutePath());
        Log.d(TAG, "  CLC file: " + clcFile.getAbsolutePath());
        GISpeechRecognizerNative.Init(langFile.getAbsolutePath(), clcFile.getAbsolutePath(), this.mDictationLanguage, cacheFolder, this);
        GIAudioNative.SetAudioParameter(1, 40);
        GIAudioNative.SetAudioParameter(2, 0);
        GIAudioNative.SetAudioParameter(30, 0);
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return "mounted".equals(state);
    }

    private void copyFileFromAsset(String asset, File dest) {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = this.mContext.getAssets().open(asset);
            OutputStream out2 = new FileOutputStream(dest);
            try {
                byte[] buffer = new byte[1024];
                while (true) {
                    int read = in.read(buffer);
                    if (read == -1) {
                        break;
                    } else {
                        out2.write(buffer, 0, read);
                    }
                }
                out = out2;
            } catch (IOException e) {
                out = out2;
                Log.e(TAG, "Failed to copy " + asset + " to sdcard");
            }
        } catch (IOException e2) {
        }
        if (in != null) {
            try {
                in.close();
            } catch (IOException e3) {
            }
        }
        if (out != null) {
            try {
                out.close();
            } catch (IOException e4) {
            }
        }
    }

    private String getLang(Locale locale) {
        if (Locale.CHINA.getLanguage().equals(locale.getLanguage())) {
            return CHINESE_LANG;
        }
        if (Locale.JAPAN.getLanguage().equals(locale.getLanguage())) {
            return JAPANESE_LANG;
        }
        return ENGLISH_LANG;
    }

    private String getCLC(Locale locale) {
        if (Locale.CHINA.getLanguage().equals(locale.getLanguage())) {
            return CHINESE_CLC;
        }
        if (Locale.JAPAN.getLanguage().equals(locale.getLanguage())) {
            return JAPANESE_CLC;
        }
        return ENGLISH_CLC;
    }

    private String getMessageType(int msgType) {
        switch (msgType) {
            case -3:
                return "ERROR_GRAMMAR_IS_EMPTY";
            case 0:
                return "RECOGNIZER_CALLBACK_MESSAGE/RECOGNIZER_CALLBACK_STATE_CHANGED";
            case 10:
                return "INTERNAL_STATE_LISTENING_LOCAL";
            case 20:
                return "RECOGNIZER_CALLBACK_STOPPED_WITH_RESULT";
            case 30:
                return "RECOGNIZER_CALLBACK_STOPPED_WITH_NO_RESULT";
            case 40:
                return "RECOGNIZER_CALLBACK_ERROR";
            default:
                return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onStarted(boolean withDictation) {
        if (this.mResultListener != null) {
            this.mResultListener.onASRStarted(withDictation);
        }
    }

    static class MyHandler extends Handler {
        private SpeechRecognizer mRecogEngine;

        public MyHandler(Looper looper, SpeechRecognizer recog) {
            super(looper);
            this.mRecogEngine = null;
            this.mRecogEngine = recog;
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            removeMessages(msg.what);
            switch (msg.what) {
                case 0:
                    ExternalAudioReceiver.setIsListening(false);
                    Log.v(SpeechRecognizer.TAG, "Initialize ASR Engine");
                    this.mRecogEngine.initializeSpeechEngine();
                    return;
                case 1:
                    Log.v(SpeechRecognizer.TAG, "Grammar: " + this.mRecogEngine.mCurrentGrammar);
                    GISpeechRecognizerNative.Stop();
                    if (this.mRecogEngine.mCurrentGrammar.startsWith("#BNF")) {
                        GISpeechRecognizerNative.SetBNFGrammar(this.mRecogEngine.mCurrentGrammar);
                    } else {
                        GISpeechRecognizerNative.DeleteGrammar();
                        GISpeechRecognizerNative.AddGrammar(this.mRecogEngine.mCurrentGrammar);
                    }
                    break;
                case 2:
                    ExternalAudioReceiver.setIsListening(false);
                    GISpeechRecognizerNative.Stop();
                    return;
                case 3:
                    ExternalAudioReceiver.setIsListening(false);
                    GISpeechRecognizerNative.Destroy();
                    return;
                case 4:
                    SpeechRecognizer.access$308(this.mRecogEngine);
                    try {
                        this.mRecogEngine.mLog.write(String.valueOf(this.mRecogEngine.mLogCount) + "\n");
                        this.mRecogEngine.mLog.flush();
                        break;
                    } catch (IOException e) {
                    }
                    GISpeechRecognizerNative.SetDictationLanguage(this.mRecogEngine.mDictationLanguage);
                    Log.v(SpeechRecognizer.TAG, "Grammar: " + this.mRecogEngine.mCurrentGrammar);
                    GISpeechRecognizerNative.Stop();
                    GISpeechRecognizerNative.DeleteGrammar();
                    GISpeechRecognizerNative.AddGrammar(this.mRecogEngine.mCurrentGrammar);
                    int result = GISpeechRecognizerNative.Start(500, true);
                    if (result >= 0) {
                        Log.v(SpeechRecognizer.TAG, "Starting ASR with Dictation Engine");
                        ExternalAudioReceiver.setIsListening(true);
                        this.mRecogEngine.onStarted(true);
                        return;
                    }
                    return;
                case 5:
                    break;
                default:
                    Log.e(SpeechRecognizer.TAG, "Unkown message sent to handler");
                    return;
            }
            int result2 = GISpeechRecognizerNative.Start(200, false);
            if (result2 >= 0) {
                Log.v(SpeechRecognizer.TAG, "ASR Engine started");
                ExternalAudioReceiver.setIsListening(true);
                this.mRecogEngine.onStarted(false);
            }
        }
    }
}
