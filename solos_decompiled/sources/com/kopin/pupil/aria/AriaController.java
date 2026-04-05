package com.kopin.pupil.aria;

import android.content.Context;
import com.kopin.pupil.PupilSpeechRecognizer;
import com.kopin.pupil.aria.AudioRecorder;
import com.kopin.pupil.aria.app.BaseAriaApp;

/* JADX INFO: loaded from: classes43.dex */
public interface AriaController extends AudioRecorder.AudioSink {

    public interface AriaAppFactory {
        BaseAriaApp createApp(Context context, BaseAriaApp.AriaAppHost ariaAppHost, PupilSpeechRecognizer.SpeechConfig speechConfig, VoConPolicy voConPolicy);
    }

    void addApp(String str, AriaAppFactory ariaAppFactory);

    String getStatus();

    boolean hasDictation();

    boolean hasError();

    boolean isEnabled();

    boolean isListening();

    void onAudioEnd(int i);

    void setEnabled(boolean z);

    void setKeywordMode(boolean z);

    void setListeningMode(boolean z, int i);

    void setStatusListener(AriaStatus ariaStatus);

    void start();

    void stop();
}
