package com.google.android.gms.iid;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes56.dex */
final class zzd implements Parcelable.Creator<MessengerCompat> {
    zzd() {
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ MessengerCompat createFromParcel(Parcel parcel) {
        IBinder strongBinder = parcel.readStrongBinder();
        if (strongBinder != null) {
            return new MessengerCompat(strongBinder);
        }
        return null;
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ MessengerCompat[] newArray(int i) {
        return new MessengerCompat[i];
    }
}
