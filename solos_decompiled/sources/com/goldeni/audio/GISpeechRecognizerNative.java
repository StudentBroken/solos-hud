package com.goldeni.audio;

/* JADX INFO: loaded from: classes39.dex */
public class GISpeechRecognizerNative {
    public static final int ERROR_GRAMMAR_IS_EMPTY = -3;
    public static final int ERROR_GRAMMAR_IS_FULL = -4;
    public static final int ERROR_RECOGNIZER_NOT_INITIALIZED = -1;
    public static final int ERROR_RECOGNIZER_NOT_STOPPED = -2;
    public static final int INTERNAL_STATE_FINISHED = 40;
    public static final int INTERNAL_STATE_LISTENING_DISTRIBUTED = 20;
    public static final int INTERNAL_STATE_LISTENING_LOCAL = 10;
    public static final int INTERNAL_STATE_PROCESSING = 30;
    public static final int RECOGNIZER_CALLBACK_ERROR = 40;
    public static final int RECOGNIZER_CALLBACK_MESSAGE = 0;
    public static final int RECOGNIZER_CALLBACK_STATE_CHANGED = 10;
    public static final int RECOGNIZER_CALLBACK_STOPPED_WITH_NO_RESULT = 30;
    public static final int RECOGNIZER_CALLBACK_STOPPED_WITH_RESULT = 20;
    public static final int SUCCESS = 1;

    public static native int AddGrammar(String str);

    public static native int DeleteGrammar();

    public static native int Destroy();

    public static native int GetResults(ASR_RESULTS asr_results);

    public static native String[] GetVersion();

    public static native int Init(String str, String str2, String str3, String str4, GISpeechRecognizerListener gISpeechRecognizerListener);

    public static native int SetBNFGrammar(String str);

    public static native int SetDictationLanguage(String str);

    public static native int Start(int i, boolean z);

    public static native int Stop();

    public static native int setMinimumConfidenceLevel(int i);
}
