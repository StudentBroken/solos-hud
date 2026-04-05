package com.kopin.pupil.tts;

import com.nuance.android.vocalizer.VocalizerVoice;

/* JADX INFO: loaded from: classes32.dex */
public class PupilVoice {
    final VocalizerVoice mVoice;

    PupilVoice(VocalizerVoice voice) {
        this.mVoice = voice;
    }

    public String getVoiceName() {
        return this.mVoice.getVoiceName();
    }

    public String getLanguage() {
        return this.mVoice.getLanguage();
    }

    public String getVoiceModel() {
        return "n/a";
    }

    public int getSampleRate() {
        return this.mVoice.getSampleRate();
    }

    public String getLanguageVersion() {
        return this.mVoice.getLanguageVersion();
    }

    public String getLanguageTLW() {
        return this.mVoice.getLanguageTLW();
    }

    public String getVoiceAge() {
        return this.mVoice.getVoiceAge();
    }

    public String getVoiceType() {
        return this.mVoice.getVoiceType();
    }

    public String getVoiceVersion() {
        return this.mVoice.getVoiceVersion();
    }

    public String toString() {
        return "Voice Name: " + getVoiceName() + " Sample Rate: " + getSampleRate() + " Voice Model: " + getVoiceModel() + " Language: " + getLanguage() + "  Lang Version: " + getLanguageVersion() + " Lang TLW: " + getLanguageTLW() + " Voice Age: " + getVoiceAge() + " Voice Type: " + getVoiceType() + " Voice Version: " + getVoiceVersion();
    }
}
