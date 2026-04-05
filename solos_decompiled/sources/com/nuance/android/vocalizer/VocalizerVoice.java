package com.nuance.android.vocalizer;

import android.util.Log;

/* JADX INFO: loaded from: classes16.dex */
public class VocalizerVoice {
    private String mVoiceName = "";
    private String mLanguage = "";
    private String mVoiceOperatingPoint = "";
    private int mSampleRate = 0;
    private String mLanguageVersion = "";
    private String mLanguageTLW = "";
    private int mLangId = 0;
    private String mVoiceAge = "";
    private String mVoiceType = "";
    private String mVoiceVersion = "";

    public String getVoiceName() {
        return this.mVoiceName;
    }

    public String getLanguage() {
        return this.mLanguage;
    }

    public String getVoiceOperatingPoint() {
        return this.mVoiceOperatingPoint;
    }

    public int getSampleRate() {
        return this.mSampleRate;
    }

    public String getLanguageVersion() {
        return this.mLanguageVersion;
    }

    public String getLanguageTLW() {
        return this.mLanguageTLW;
    }

    protected int getLangId() {
        return this.mLangId;
    }

    public String getVoiceAge() {
        return this.mVoiceAge;
    }

    public String getVoiceType() {
        return this.mVoiceType;
    }

    public String getVoiceVersion() {
        return this.mVoiceVersion;
    }

    protected void print(String str) {
        Log.v(str, "Voice Name: " + getVoiceName() + " Sample Rate: " + getSampleRate() + " Voice Operating Point: " + getVoiceOperatingPoint() + " Language: " + getLanguage());
        Log.v(str, "  Lang Version: " + getLanguageVersion() + " Lang TLW: " + getLanguageTLW() + " LangId: " + getLangId() + " Voice Age: " + getVoiceAge() + " Voice Type: " + getVoiceType() + " Voice Version: " + getVoiceVersion());
    }

    public boolean equals(VocalizerVoice vocalizerVoice) {
        return vocalizerVoice.getVoiceName().compareToIgnoreCase(this.mVoiceName) == 0 && vocalizerVoice.getLanguage().compareToIgnoreCase(this.mLanguage) == 0 && vocalizerVoice.getVoiceOperatingPoint().compareToIgnoreCase(this.mVoiceOperatingPoint) == 0 && vocalizerVoice.getSampleRate() == this.mSampleRate;
    }
}
