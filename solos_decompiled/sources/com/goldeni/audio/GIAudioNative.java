package com.goldeni.audio;

import android.util.Log;

/* JADX INFO: loaded from: classes39.dex */
public class GIAudioNative {
    public static final int AUDIO_ANALOGUE_GAIN = 0;
    public static final int AUDIO_AUTO_BALANCE = 30;
    public static final int AUDIO_CHANNEL_0 = 20;
    public static final int AUDIO_CHANNEL_1 = 21;
    public static final int AUDIO_CHANNEL_2 = 22;
    public static final int AUDIO_CHANNEL_3 = 23;
    public static final int AUDIO_CURRENT_CONFIGURATION = 29;
    public static final int AUDIO_FILTER = 3;
    public static final int AUDIO_FILTER_TIMER = 28;
    public static final int AUDIO_LEFT_CONFIGURATION_BALANCE = 4;
    public static final int AUDIO_NCS_ACTIVE_THRESHOLD = 13;
    public static final int AUDIO_NCS_DEBUG_LEVEL = 11;
    public static final int AUDIO_NCS_ENABLE = 10;
    public static final int AUDIO_NCS_LAMBDA_LTE = 18;
    public static final int AUDIO_NCS_LAMBDA_LTE_BIGGER = 16;
    public static final int AUDIO_NCS_LAMBDA_LTE_HIGHER_E = 17;
    public static final int AUDIO_NCS_NOISEFLOOR_LOWER_THRESHOLD = 15;
    public static final int AUDIO_NCS_NOISEFLOOR_UPPER_THRESHOLD = 14;
    public static final int AUDIO_NCS_NOISE_FLOOR = 27;
    public static final int AUDIO_NCS_TIMER = 26;
    public static final int AUDIO_NCS_VAD_SENSITIVITY = 12;
    public static final int AUDIO_POSTGAIN = 2;
    public static final int AUDIO_PREGAIN = 1;
    public static final int AUDIO_RECORDING_STATE = 25;
    public static final int AUDIO_RIGHT_CONFIGURATION_BALANCE = 5;
    public static final int AUDIO_SERIAL_NUMBER = 9;
    public static final int AUDIO_SNR_NOISEFLOOR_LOWLMT = 19;
    private static final String TAG = "GIAudioNative";

    public static native int[] GetAudioAnalysis();

    public static native int GetAudioParameter(int i);

    public static native String[] GetVersion();

    public static native int InjectAudioData(byte[] bArr, int i, int i2);

    public static native int Pause();

    public static native int RecordAudio(String str);

    public static native int Resume();

    public static native int SetAudioParameter(int i, int i2);

    public static native int Start();

    public static native int Stop();

    private GIAudioNative() {
    }

    static {
        try {
            System.loadLibrary("pal_core");
            System.loadLibrary("pal_audio");
        } catch (UnsatisfiedLinkError e) {
            Log.e(TAG, "Unable to load PAL Audio Libs", e);
        }
        try {
            System.loadLibrary("vocon3200_platform");
            System.loadLibrary("vocon3200_base");
            System.loadLibrary("vocon3200_asr");
            System.loadLibrary("vocon3200_pron");
            System.loadLibrary("vocon3200_sem");
            System.loadLibrary("vocon3200_sem3");
            System.loadLibrary("vocon3200_gram2");
            System.loadLibrary("vocon_ext_stream");
            System.loadLibrary("vocon_ext_heap");
            System.loadLibrary("vocon_ext_audioin");
            System.loadLibrary("vocon_ext_asr2sem");
        } catch (UnsatisfiedLinkError e2) {
            Log.e(TAG, "Unable to load VOCON Core Libs", e2);
        }
        try {
            System.loadLibrary("goldeni_audio");
        } catch (UnsatisfiedLinkError e3) {
            Log.e(TAG, "Unable to load Goldeni_Audio Lib", e3);
        }
    }
}
