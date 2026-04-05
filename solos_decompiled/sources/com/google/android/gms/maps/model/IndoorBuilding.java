package com.google.android.gms.maps.model;

import android.os.IBinder;
import android.os.RemoteException;
import com.google.android.gms.common.internal.zzbr;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes10.dex */
public final class IndoorBuilding {
    private final com.google.android.gms.maps.model.internal.zzj zzbnA;

    public IndoorBuilding(com.google.android.gms.maps.model.internal.zzj zzjVar) {
        this.zzbnA = (com.google.android.gms.maps.model.internal.zzj) zzbr.zzu(zzjVar);
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof IndoorBuilding)) {
            return false;
        }
        try {
            return this.zzbnA.zzb(((IndoorBuilding) obj).zzbnA);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final int getActiveLevelIndex() {
        try {
            return this.zzbnA.getActiveLevelIndex();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final int getDefaultLevelIndex() {
        try {
            return this.zzbnA.getActiveLevelIndex();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final List<IndoorLevel> getLevels() {
        try {
            List<IBinder> levels = this.zzbnA.getLevels();
            ArrayList arrayList = new ArrayList(levels.size());
            Iterator<IBinder> it = levels.iterator();
            while (it.hasNext()) {
                arrayList.add(new IndoorLevel(com.google.android.gms.maps.model.internal.zzn.zzae(it.next())));
            }
            return arrayList;
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final int hashCode() {
        try {
            return this.zzbnA.hashCodeRemote();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final boolean isUnderground() {
        try {
            return this.zzbnA.isUnderground();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }
}
