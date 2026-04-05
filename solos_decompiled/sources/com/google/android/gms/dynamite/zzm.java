package com.google.android.gms.dynamite;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.internal.zzed;
import com.google.android.gms.internal.zzef;

/* JADX INFO: loaded from: classes67.dex */
public final class zzm extends zzed implements zzl {
    zzm(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.dynamite.IDynamiteLoaderV2");
    }

    @Override // com.google.android.gms.dynamite.zzl
    public final IObjectWrapper zza(IObjectWrapper iObjectWrapper, String str, int i, IObjectWrapper iObjectWrapper2) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, iObjectWrapper);
        parcelZzY.writeString(str);
        parcelZzY.writeInt(i);
        zzef.zza(parcelZzY, iObjectWrapper2);
        Parcel parcelZza = zza(2, parcelZzY);
        IObjectWrapper iObjectWrapperZzM = IObjectWrapper.zza.zzM(parcelZza.readStrongBinder());
        parcelZza.recycle();
        return iObjectWrapperZzM;
    }
}
