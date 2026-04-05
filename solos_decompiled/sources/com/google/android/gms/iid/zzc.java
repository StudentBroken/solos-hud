package com.google.android.gms.iid;

import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.internal.zzed;
import com.google.android.gms.internal.zzef;

/* JADX INFO: loaded from: classes56.dex */
public final class zzc extends zzed implements zzb {
    zzc(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.iid.IMessengerCompat");
    }

    @Override // com.google.android.gms.iid.zzb
    public final void send(Message message) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, message);
        zzc(1, parcelZzY);
    }
}
