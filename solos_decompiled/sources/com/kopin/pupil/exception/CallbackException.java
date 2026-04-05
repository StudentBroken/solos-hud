package com.kopin.pupil.exception;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes25.dex */
public class CallbackException extends RemoteException implements Parcelable {
    public static final Parcelable.Creator<CallbackException> CREATOR = new Parcelable.Creator<CallbackException>() { // from class: com.kopin.pupil.exception.CallbackException.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CallbackException[] newArray(int size) {
            return new CallbackException[size];
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CallbackException createFromParcel(Parcel source) {
            return new CallbackException(source);
        }
    };

    public CallbackException(Parcel in) {
        super(in.readString());
    }

    public CallbackException(String message) {
        super(message);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getMessage());
    }
}
