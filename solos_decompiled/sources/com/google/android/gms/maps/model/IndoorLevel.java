package com.google.android.gms.maps.model;

import android.os.RemoteException;
import com.google.android.gms.common.internal.zzbr;

/* JADX INFO: loaded from: classes10.dex */
public final class IndoorLevel {
    private final com.google.android.gms.maps.model.internal.zzm zzbnB;

    public IndoorLevel(com.google.android.gms.maps.model.internal.zzm zzmVar) {
        this.zzbnB = (com.google.android.gms.maps.model.internal.zzm) zzbr.zzu(zzmVar);
    }

    public final void activate() {
        try {
            this.zzbnB.activate();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof IndoorLevel)) {
            return false;
        }
        try {
            return this.zzbnB.zza(((IndoorLevel) obj).zzbnB);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final String getName() {
        try {
            return this.zzbnB.getName();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final String getShortName() {
        try {
            return this.zzbnB.getShortName();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final int hashCode() {
        try {
            return this.zzbnB.hashCodeRemote();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }
}
