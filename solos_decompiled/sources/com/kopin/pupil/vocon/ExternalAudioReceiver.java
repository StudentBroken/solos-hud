package com.kopin.pupil.vocon;

import com.goldeni.audio.GIAudioNative;

/* JADX INFO: loaded from: classes.dex */
public class ExternalAudioReceiver {
    static boolean mIsListening = false;

    public static boolean isListening() {
        return mIsListening;
    }

    public static void setIsListening(boolean value) {
        mIsListening = value;
    }

    public static byte[] createStereoChannelFromMono(byte[] bufferMonoSample) {
        byte[] bufferToSend = null;
        if (bufferMonoSample != null && bufferMonoSample.length > 0) {
            bufferToSend = new byte[bufferMonoSample.length * 2];
            int destination = 0;
            for (int i = 0; i < bufferMonoSample.length; i += 2) {
                int destination2 = destination + 1;
                bufferToSend[destination] = bufferMonoSample[i];
                int destination3 = destination2 + 1;
                bufferToSend[destination2] = bufferMonoSample[i + 1];
                int destination4 = destination3 + 1;
                bufferToSend[destination3] = bufferMonoSample[i];
                destination = destination4 + 1;
                bufferToSend[destination4] = bufferMonoSample[i + 1];
            }
        }
        return bufferToSend;
    }

    public static boolean sendAudioData(byte[] audio, int length, int channels) {
        if (!isListening() || audio == null) {
            return false;
        }
        int i = (length / 2) / channels;
        int result = GIAudioNative.InjectAudioData(audio, channels, 0);
        return result == 1;
    }
}
