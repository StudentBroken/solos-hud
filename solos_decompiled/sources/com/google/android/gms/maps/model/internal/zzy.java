package com.google.android.gms.maps.model.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.internal.zzed;
import com.google.android.gms.internal.zzef;

/* JADX INFO: loaded from: classes10.dex */
public final class zzy extends zzed implements zzw {
    zzy(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.maps.model.internal.ITileOverlayDelegate");
    }

    @Override // com.google.android.gms.maps.model.internal.zzw
    public final void clearTileCache() throws RemoteException {
        zzb(2, zzY());
    }

    @Override // com.google.android.gms.maps.model.internal.zzw
    public final boolean getFadeIn() throws RemoteException {
        Parcel parcelZza = zza(11, zzY());
        boolean zZza = zzef.zza(parcelZza);
        parcelZza.recycle();
        return zZza;
    }

    @Override // com.google.android.gms.maps.model.internal.zzw
    public final String getId() throws RemoteException {
        Parcel parcelZza = zza(3, zzY());
        String string = parcelZza.readString();
        parcelZza.recycle();
        return string;
    }

    @Override // com.google.android.gms.maps.model.internal.zzw
    public final float getTransparency() throws RemoteException {
        Parcel parcelZza = zza(13, zzY());
        float f = parcelZza.readFloat();
        parcelZza.recycle();
        return f;
    }

    @Override // com.google.android.gms.maps.model.internal.zzw
    public final float getZIndex() throws RemoteException {
        Parcel parcelZza = zza(5, zzY());
        float f = parcelZza.readFloat();
        parcelZza.recycle();
        return f;
    }

    @Override // com.google.android.gms.maps.model.internal.zzw
    public final int hashCodeRemote() throws RemoteException {
        Parcel parcelZza = zza(9, zzY());
        int i = parcelZza.readInt();
        parcelZza.recycle();
        return i;
    }

    @Override // com.google.android.gms.maps.model.internal.zzw
    public final boolean isVisible() throws RemoteException {
        Parcel parcelZza = zza(7, zzY());
        boolean zZza = zzef.zza(parcelZza);
        parcelZza.recycle();
        return zZza;
    }

    @Override // com.google.android.gms.maps.model.internal.zzw
    public final void remove() throws RemoteException {
        zzb(1, zzY());
    }

    @Override // com.google.android.gms.maps.model.internal.zzw
    public final void setFadeIn(boolean z) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, z);
        zzb(10, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.zzw
    public final void setTransparency(float f) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeFloat(f);
        zzb(12, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.zzw
    public final void setVisible(boolean z) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, z);
        zzb(6, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.zzw
    public final void setZIndex(float f) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeFloat(f);
        zzb(4, parcelZzY);
    }

    @Override // com.google.android.gms.maps.model.internal.zzw
    public final boolean zza(zzw zzwVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zzwVar);
        Parcel parcelZza = zza(8, parcelZzY);
        boolean zZza = zzef.zza(parcelZza);
        parcelZza.recycle();
        return zZza;
    }
}
