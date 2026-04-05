package com.kopin.pupil.tts;

import com.kopin.accessory.AudioCodec;

/* JADX INFO: loaded from: classes32.dex */
public interface IAudioCallback {
    void onAudioData(byte[] bArr, int i, AudioCodec audioCodec, int i2);

    void onAudioEnd(int i);

    void onAudioStart(int i);
}
