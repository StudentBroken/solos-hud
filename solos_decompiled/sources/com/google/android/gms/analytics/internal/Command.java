package com.google.android.gms.analytics.internal;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes49.dex */
public class Command implements Parcelable {
    public static final String APPEND_CACHE_BUSTER = "appendCacheBuster";
    public static final String APPEND_QUEUE_TIME = "appendQueueTime";
    public static final String APPEND_VERSION = "appendVersion";
    public static final Parcelable.Creator<Command> CREATOR = new Parcelable.Creator<Command>() { // from class: com.google.android.gms.analytics.internal.Command.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Command createFromParcel(Parcel in) {
            return new Command(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Command[] newArray(int size) {
            return new Command[size];
        }
    };
    private String id;
    private String urlParam;
    private String value;

    public Command(String id, String urlParam, String value) {
        this.id = id;
        this.urlParam = urlParam;
        this.value = value;
    }

    public String getId() {
        return this.id;
    }

    public String getUrlParam() {
        return this.urlParam;
    }

    public String getValue() {
        return this.value;
    }

    public Command() {
    }

    Command(Parcel in) {
        readFromParcel(in);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.id);
        out.writeString(this.urlParam);
        out.writeString(this.value);
    }

    private void readFromParcel(Parcel in) {
        this.id = in.readString();
        this.urlParam = in.readString();
        this.value = in.readString();
    }
}
