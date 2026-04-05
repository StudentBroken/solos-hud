package com.kopin.pupil.vocon;

import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class Dictation implements Parcelable {
    public static Parcelable.Creator<Dictation> CREATOR = new Parcelable.Creator<Dictation>() { // from class: com.kopin.pupil.vocon.Dictation.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Dictation[] newArray(int size) {
            return new Dictation[size];
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Dictation createFromParcel(Parcel source) {
            return new Dictation(source);
        }
    };
    private String mError;
    private JSONObject mOutput;

    public Dictation() {
        this.mOutput = null;
        this.mError = null;
    }

    public Dictation(JSONObject output, String error) {
        this.mOutput = null;
        this.mError = null;
        this.mOutput = output;
        this.mError = error;
    }

    public Dictation(Parcel in) {
        this.mOutput = null;
        this.mError = null;
        try {
            this.mOutput = new JSONObject(in.readString());
        } catch (JSONException e) {
        }
        this.mError = in.readString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.mOutput.toString());
        out.writeString(this.mError);
    }

    public JSONObject getOutput() {
        return this.mOutput;
    }

    public void setOutput(JSONObject output) {
        this.mOutput = output;
    }

    public String getError() {
        return this.mError;
    }

    public void setError(String error) {
        this.mError = error;
    }
}
