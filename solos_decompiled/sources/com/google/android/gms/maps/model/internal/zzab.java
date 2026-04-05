package com.google.android.gms.maps.model.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.internal.zzed;
import com.google.android.gms.internal.zzef;
import com.google.android.gms.maps.model.Tile;

/* JADX INFO: loaded from: classes10.dex */
public final class zzab extends zzed implements zzz {
    zzab(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.maps.model.internal.ITileProviderDelegate");
    }

    @Override // com.google.android.gms.maps.model.internal.zzz
    public final Tile getTile(int i, int i2, int i3) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeInt(i);
        parcelZzY.writeInt(i2);
        parcelZzY.writeInt(i3);
        Parcel parcelZza = zza(1, parcelZzY);
        Tile tile = (Tile) zzef.zza(parcelZza, Tile.CREATOR);
        parcelZza.recycle();
        return tile;
    }
}
