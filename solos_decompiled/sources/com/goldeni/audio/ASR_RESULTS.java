package com.goldeni.audio;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes39.dex */
public class ASR_RESULTS implements Parcelable {
    public static final Parcelable.Creator<ASR_RESULTS> CREATOR = new Parcelable.Creator<ASR_RESULTS>() { // from class: com.goldeni.audio.ASR_RESULTS.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ASR_RESULTS createFromParcel(Parcel in) {
            return new ASR_RESULTS(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ASR_RESULTS[] newArray(int arg0) {
            return null;
        }
    };
    private String dictation;
    private int dictationTotalResults;
    private int[] localConfidence;
    private String[] localPhrase;
    private int localTotalResults;

    public ASR_RESULTS() {
    }

    private ASR_RESULTS(Parcel in) {
        readFromParcel(in);
    }

    public void readFromParcel(Parcel in) {
        this.localTotalResults = in.readInt();
        this.dictationTotalResults = in.readInt();
        this.localPhrase = new String[this.localTotalResults];
        this.localConfidence = new int[this.localTotalResults];
        in.readStringArray(this.localPhrase);
        in.readIntArray(this.localConfidence);
        this.dictation = in.readString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.localTotalResults);
        out.writeInt(this.dictationTotalResults);
        out.writeStringArray(this.localPhrase);
        out.writeIntArray(this.localConfidence);
        out.writeString(this.dictation);
    }

    public int getLocalTotalResults() {
        return this.localTotalResults;
    }

    public void setLocalTotalResults(int totRes) {
        this.localTotalResults = totRes;
    }

    public String getLocalPhrase(int index) {
        return this.localPhrase[index];
    }

    public String[] getLocalPhrase() {
        return this.localPhrase;
    }

    public void setLocalPhrase(String[] inPhrase) {
        this.localPhrase = new String[inPhrase.length];
        System.arraycopy(inPhrase, 0, this.localPhrase, 0, inPhrase.length);
    }

    public int[] getLocalConfidence() {
        return this.localConfidence;
    }

    public int getLocalConfidence(int index) {
        return this.localConfidence[index];
    }

    public void setLocalConfidence(int[] conf) {
        this.localConfidence = conf;
    }

    public int getDictationTotalResults() {
        return this.dictationTotalResults;
    }

    public void setDictationTotalResults(int totRes) {
        this.dictationTotalResults = totRes;
    }

    public String getDictation() {
        String result = this.dictation.replace("goldeneye", "Golden-i");
        return result.replace("Goldeneye", "Golden-i").replace("golden-I", "Golden-i").replace("Golden-I", "Golden-i").replace("golden I", "Golden-i").replace("Golden I", "Golden-i");
    }

    public void setDictation(String dictation) {
        this.dictation = dictation;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return getClass().hashCode();
    }
}
