package com.kopin.pupil.tts;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.kopin.accessory.AudioCodec;
import com.kopin.core.Core;
import com.nuance.android.vocalizer.VocalizerAudioOutputListener;
import com.nuance.android.vocalizer.VocalizerAudioSettings;
import com.nuance.android.vocalizer.VocalizerEngine;
import com.nuance.android.vocalizer.VocalizerEngineListener;
import com.nuance.android.vocalizer.VocalizerSpeechItem;
import com.nuance.android.vocalizer.VocalizerVoice;
import java.util.Locale;

/* JADX INFO: loaded from: classes32.dex */
public final class TTS implements VocalizerEngineListener, VocalizerAudioOutputListener {
    private static final boolean DEBUG = false;
    private static final int DESIRED_SAMPLE_RATE = 16000;
    public static final int SAMPLE_RATE = 22050;
    private static final String TAG = "TTS";
    private Context mContext;
    private VocalizerEngine mEngine;
    private Handler mHandler;
    private long mHandlerThreadId;
    private IAudioCallback mAudioCallback = null;
    private int mID = 0;
    private VocalizerVoice mCurrentVoice = null;

    public TTS(Context context) {
        this.mHandler = null;
        this.mEngine = null;
        this.mContext = null;
        if (this.mEngine == null) {
            this.mContext = context;
            this.mEngine = new VocalizerEngine(context);
            this.mEngine.setListener(this);
            this.mEngine.initialize();
        } else {
            this.mEngine.setListener(this);
        }
        VocalizerAudioSettings audioSettings = new VocalizerAudioSettings(SAMPLE_RATE, 1);
        this.mEngine.setCustomAudioSettings(audioSettings);
        this.mEngine.setAudioOutputListener(this, true);
        this.mHandler = new Handler();
        this.mHandlerThreadId = Thread.currentThread().getId();
    }

    public void setAudioCallback(IAudioCallback audioCallback) {
        this.mAudioCallback = audioCallback;
    }

    public void speak(final int id, final String command) {
        if (this.mEngine == null) {
            return;
        }
        if (Thread.currentThread().getId() != this.mHandlerThreadId) {
            this.mHandler.post(new Runnable() { // from class: com.kopin.pupil.tts.TTS.1
                @Override // java.lang.Runnable
                public void run() {
                    TTS.this.speak(id, command);
                }
            });
            return;
        }
        if (this.mCurrentVoice == null) {
            this.mCurrentVoice = findVoice();
            Log.v(TAG, "Loading Default Voice: " + (this.mCurrentVoice == null ? "null" : this.mCurrentVoice.getVoiceName()));
        }
        if (this.mEngine.getCurrentVoice() != this.mCurrentVoice && !this.mEngine.loadVoice(this.mCurrentVoice)) {
            Log.e(TAG, "Failed to load voice.");
            return;
        }
        this.mEngine.setListener(this);
        VocalizerAudioSettings audioSettings = new VocalizerAudioSettings(SAMPLE_RATE, 1);
        this.mEngine.setCustomAudioSettings(audioSettings);
        this.mEngine.setAudioOutputListener(this, true);
        this.mID = id;
        VocalizerSpeechItem item = this.mEngine.speak(command, false, String.valueOf(id));
        if (item == null) {
            this.mID = 0;
        }
    }

    public void stop() {
        if (Thread.currentThread().getId() != this.mHandlerThreadId) {
            this.mHandler.post(new Runnable() { // from class: com.kopin.pupil.tts.TTS.2
                @Override // java.lang.Runnable
                public void run() {
                    TTS.this.mEngine.stop();
                }
            });
        } else {
            this.mEngine.stop();
        }
    }

    @Override // com.nuance.android.vocalizer.VocalizerEngineListener
    public void onSpeakElementStarted(String id) {
        this.mAudioCallback.onAudioStart(this.mID);
    }

    @Override // com.nuance.android.vocalizer.VocalizerEngineListener
    public void onSpeakElementCompleted(String id) {
        int tmpId = this.mID;
        this.mID = 0;
        this.mAudioCallback.onAudioEnd(tmpId);
    }

    @Override // com.nuance.android.vocalizer.VocalizerEngineListener
    public void onStateChanged(int state) {
        Log.v(TAG, "New State: " + Integer.toString(state));
        if (state == 6) {
            onSpeakElementCompleted(NotificationCompat.CATEGORY_ERROR);
        }
    }

    @Override // com.nuance.android.vocalizer.VocalizerEngineListener
    public void onVoiceListChanged() {
        Log.v(TAG, "Voice List Changed");
    }

    @Override // com.nuance.android.vocalizer.VocalizerAudioOutputListener
    public void onAudioData(VocalizerAudioSettings settings, short[] audioData) {
        byte[] audioByteData = new byte[audioData.length * 2];
        for (int i = 0; i < audioData.length; i++) {
            int index = i << 1;
            audioByteData[index] = (byte) audioData[i];
            audioByteData[index + 1] = (byte) ((audioData[i] >> 8) & 255);
        }
        if (settings.mRate > DESIRED_SAMPLE_RATE) {
            byte[] downSampledData = Core.Downsample16000(audioByteData, 0, audioByteData.length, settings.mRate, (short) 16, (byte) 1);
            if (this.mAudioCallback != null) {
                this.mAudioCallback.onAudioData(downSampledData, downSampledData.length, AudioCodec.PCM, DESIRED_SAMPLE_RATE);
                return;
            }
            return;
        }
        if (this.mAudioCallback != null) {
            this.mAudioCallback.onAudioData(audioByteData, audioByteData.length, AudioCodec.PCM, SAMPLE_RATE);
        }
    }

    private VocalizerVoice findVoice() {
        Locale currentLocale = this.mContext.getResources().getConfiguration().locale;
        String langCode = currentLocale.getLanguage();
        if (langCode.equalsIgnoreCase("zh")) {
            langCode = "mn";
        } else if (langCode.equalsIgnoreCase("ja")) {
            langCode = "jp";
        }
        VocalizerVoice[] voices = this.mEngine.getVoiceList();
        if (voices == null || voices.length == 0) {
            return null;
        }
        for (VocalizerVoice voice : voices) {
            String languageTLW = voice.getLanguageTLW();
            if (languageTLW.toLowerCase(Locale.ENGLISH).startsWith(langCode)) {
                return voice;
            }
        }
        return voices[0];
    }

    public PupilVoice[] getVoiceList() {
        VocalizerVoice[] voices = this.mEngine.getVoiceList();
        if (voices == null) {
            return new PupilVoice[0];
        }
        PupilVoice[] ret = new PupilVoice[voices.length];
        for (int i = 0; i < voices.length; i++) {
            ret[i] = new PupilVoice(voices[i]);
        }
        return ret;
    }

    public void selectVoice(PupilVoice voice) {
        if (this.mEngine.getCurrentVoice() != voice.mVoice) {
            Log.v(TAG, "Loading Voice: " + voice.toString());
            this.mCurrentVoice = voice.mVoice;
            if (!this.mEngine.loadVoice(voice.mVoice)) {
                Log.e(TAG, "Failed to load voice.");
            }
        }
    }
}
