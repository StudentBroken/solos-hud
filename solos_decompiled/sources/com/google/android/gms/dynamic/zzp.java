package com.google.android.gms.dynamic;

import android.content.Context;
import android.os.IBinder;
import com.google.android.gms.common.internal.zzbr;

/* JADX INFO: loaded from: classes67.dex */
public abstract class zzp<T> {
    private final String zzaSG;
    private T zzaSH;

    protected zzp(String str) {
        this.zzaSG = str;
    }

    protected final T zzaS(Context context) throws zzq {
        if (this.zzaSH == null) {
            zzbr.zzu(context);
            Context remoteContext = com.google.android.gms.common.zzo.getRemoteContext(context);
            if (remoteContext == null) {
                throw new zzq("Could not get remote context.");
            }
            try {
                this.zzaSH = zzb((IBinder) remoteContext.getClassLoader().loadClass(this.zzaSG).newInstance());
            } catch (ClassNotFoundException e) {
                throw new zzq("Could not load creator class.", e);
            } catch (IllegalAccessException e2) {
                throw new zzq("Could not access creator.", e2);
            } catch (InstantiationException e3) {
                throw new zzq("Could not instantiate creator.", e3);
            }
        }
        return this.zzaSH;
    }

    protected abstract T zzb(IBinder iBinder);
}
