package com.kopin.pupil;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import com.goldeni.audio.ASR_RESULTS;
import com.kopin.accessory.AudioCodec;
import com.kopin.pupil.vocon.ExternalAudioReceiver;
import com.kopin.pupil.vocon.Grammar;
import com.kopin.pupil.vocon.Language;
import com.kopin.pupil.vocon.SpeechRecognizer;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class PupilSpeechRecognizer extends SpeechRecognizer {
    public static final int SILENCE_TIMEOUT_DEFAULT = -1;
    public static final int SILENCE_TIMEOUT_DICTATION = 1000;
    public static final int SILENCE_TIMEOUT_VOCON = 200;
    private final SpeechRecognizer.OnASRResultsListener mASRListener;
    private ConnectivityManager mConMan;
    private final SpeechConfig mConfigurator;
    private ASR_RESULTS mLastResults;
    private ResultListener mLocalListener;
    private boolean mRequestDictation;

    public enum ASRErrors {
        NONE,
        NO_CONNECTIVITY,
        BAD_GRAMMAR,
        UNKNOWN
    }

    public enum ASRWarnings {
        NONE,
        LOW_CONFIDENCE,
        TOO_QUIET,
        UNKNOWN
    }

    public interface ResultListener {
        void onError(ASRErrors aSRErrors);

        void onListening();

        boolean onResult(String str, SpeechConfig speechConfig);

        void onSilence();

        void onWarning(ASRWarnings aSRWarnings);
    }

    public interface SpeechConfig {
        ASR_RESULTS getLastResults();

        boolean isDictationAvailable();

        void restartASR(boolean z, boolean z2);

        void setDictation(boolean z);

        void setSilenceTimeout(int i);
    }

    public PupilSpeechRecognizer(Context context) {
        super(context, null, true);
        this.mRequestDictation = false;
        this.mConfigurator = new SpeechConfig() { // from class: com.kopin.pupil.PupilSpeechRecognizer.1
            @Override // com.kopin.pupil.PupilSpeechRecognizer.SpeechConfig
            public ASR_RESULTS getLastResults() {
                return PupilSpeechRecognizer.this.mLastResults;
            }

            @Override // com.kopin.pupil.PupilSpeechRecognizer.SpeechConfig
            public void restartASR(boolean andReloadGrammar, boolean andRestartApps) {
            }

            @Override // com.kopin.pupil.PupilSpeechRecognizer.SpeechConfig
            public void setSilenceTimeout(int silenceMillis) {
            }

            @Override // com.kopin.pupil.PupilSpeechRecognizer.SpeechConfig
            public void setDictation(boolean onOrOff) {
                PupilSpeechRecognizer.this.mRequestDictation = onOrOff;
            }

            @Override // com.kopin.pupil.PupilSpeechRecognizer.SpeechConfig
            public boolean isDictationAvailable() {
                return isDictationAvailable();
            }
        };
        this.mASRListener = new SpeechRecognizer.OnASRResultsListener() { // from class: com.kopin.pupil.PupilSpeechRecognizer.2
            @Override // com.kopin.pupil.vocon.SpeechRecognizer.OnASRResultsListener
            public void onASRStarted(boolean withDictation) {
                if (PupilSpeechRecognizer.this.mLocalListener != null) {
                    PupilSpeechRecognizer.this.mLocalListener.onListening();
                }
            }

            @Override // com.kopin.pupil.vocon.SpeechRecognizer.OnASRResultsListener
            public void onASRResults(SpeechRecognizer recognizer, ASR_RESULTS results, String command) {
                if (PupilSpeechRecognizer.this.mLocalListener != null) {
                    String dictation = null;
                    PupilSpeechRecognizer.this.mLastResults = results;
                    if (PupilSpeechRecognizer.this.mRequestDictation && PupilSpeechRecognizer.this.isDictationAvailable()) {
                        dictation = results.getDictation();
                        if (command != null && command.equalsIgnoreCase(dictation)) {
                            dictation = null;
                        }
                    }
                    PupilSpeechRecognizer.this.mRequestDictation = false;
                    ResultListener resultListener = PupilSpeechRecognizer.this.mLocalListener;
                    if (dictation == null || dictation.isEmpty()) {
                        dictation = command;
                    }
                    boolean restartASR = resultListener.onResult(dictation, PupilSpeechRecognizer.this.mConfigurator);
                    if (restartASR) {
                        PupilSpeechRecognizer.this.start(PupilSpeechRecognizer.this.mRequestDictation);
                    }
                }
            }

            @Override // com.kopin.pupil.vocon.SpeechRecognizer.OnASRResultsListener
            public void onASRNoResults(SpeechRecognizer recognizer, boolean lowConfidence) {
                if (PupilSpeechRecognizer.this.mLocalListener != null) {
                    PupilSpeechRecognizer.this.mLocalListener.onWarning(lowConfidence ? ASRWarnings.LOW_CONFIDENCE : ASRWarnings.TOO_QUIET);
                }
            }

            @Override // com.kopin.pupil.vocon.SpeechRecognizer.OnASRResultsListener
            public void onSilenceDetected() {
                if (PupilSpeechRecognizer.this.mLocalListener != null) {
                    PupilSpeechRecognizer.this.mLocalListener.onSilence();
                }
            }

            @Override // com.kopin.pupil.vocon.SpeechRecognizer.OnASRResultsListener
            public void onASRStopped(boolean withError, int err) {
                if (withError && PupilSpeechRecognizer.this.mLocalListener != null) {
                    PupilSpeechRecognizer.this.mLocalListener.onError(err == 10 ? ASRErrors.NO_CONNECTIVITY : ASRErrors.UNKNOWN);
                }
            }
        };
        changeListener(this.mASRListener);
        this.mConMan = (ConnectivityManager) context.getSystemService("connectivity");
    }

    public boolean isDictationAvailable() {
        NetworkInfo netInfo = this.mConMan.getActiveNetworkInfo();
        if (netInfo != null) {
            return netInfo.isConnectedOrConnecting();
        }
        return false;
    }

    public boolean isListening() {
        return ExternalAudioReceiver.isListening();
    }

    public boolean hasDictation() {
        return false;
    }

    public boolean hasError() {
        return false;
    }

    public void setListener(ResultListener callback) {
        this.mLocalListener = callback;
    }

    public void setGrammar(Grammar grammar) {
        setGrammar(grammar.compile());
        this.mRequestDictation = grammar.needsDictation();
    }

    @Override // com.kopin.pupil.vocon.SpeechRecognizer
    public void stop() {
        super.stop();
    }

    public void processAudio(byte[] data, AudioCodec codec, int channels) {
        switch (codec) {
            case PCM:
                ExternalAudioReceiver.sendAudioData(data, data.length, channels);
                break;
        }
    }

    public ASR_RESULTS getLastResults() {
        return this.mLastResults;
    }

    @TargetApi(19)
    public static boolean isValidAsr(String text) {
        Locale locale;
        if (Build.VERSION.SDK_INT >= 19 && (locale = Language.getDefaultLanguage()) != Language.CHINESE_CAN && locale != Language.CHINESE_MAN) {
            int i = 0;
            while (i < text.length()) {
                int codepoint = text.codePointAt(i);
                i += Character.charCount(codepoint);
                if (Character.isIdeographic(codepoint)) {
                    return false;
                }
            }
        }
        return true;
    }
}
