package com.google.android.gms.internal;

import com.google.android.gms.internal.ahz;
import java.io.IOException;
import java.util.List;

/* JADX INFO: loaded from: classes67.dex */
public final class aia<M extends ahz<M>, T> {
    public final int tag;
    protected final Class<T> zzcmA;
    private int type = 11;
    protected final boolean zzcuX = false;

    private aia(int i, Class<T> cls, int i2, boolean z) {
        this.zzcmA = cls;
        this.tag = i2;
    }

    public static <M extends ahz<M>, T extends aif> aia<M, T> zza(int i, Class<T> cls, long j) {
        return new aia<>(11, cls, (int) j, false);
    }

    private final Object zzb(ahw ahwVar) {
        Class<T> cls = this.zzcmA;
        try {
            switch (this.type) {
                case 10:
                    aif aifVar = (aif) cls.newInstance();
                    ahwVar.zza(aifVar, this.tag >>> 3);
                    return aifVar;
                case 11:
                    aif aifVar2 = (aif) cls.newInstance();
                    ahwVar.zzb(aifVar2);
                    return aifVar2;
                default:
                    throw new IllegalArgumentException(new StringBuilder(24).append("Unknown type ").append(this.type).toString());
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Error reading extension field", e);
        } catch (IllegalAccessException e2) {
            String strValueOf = String.valueOf(cls);
            throw new IllegalArgumentException(new StringBuilder(String.valueOf(strValueOf).length() + 33).append("Error creating instance of class ").append(strValueOf).toString(), e2);
        } catch (InstantiationException e3) {
            String strValueOf2 = String.valueOf(cls);
            throw new IllegalArgumentException(new StringBuilder(String.valueOf(strValueOf2).length() + 33).append("Error creating instance of class ").append(strValueOf2).toString(), e3);
        }
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof aia)) {
            return false;
        }
        aia aiaVar = (aia) obj;
        return this.type == aiaVar.type && this.zzcmA == aiaVar.zzcmA && this.tag == aiaVar.tag;
    }

    public final int hashCode() {
        return (((((this.type + 1147) * 31) + this.zzcmA.hashCode()) * 31) + this.tag) * 31;
    }

    final T zzY(List<aii> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return this.zzcmA.cast(zzb(ahw.zzI(list.get(list.size() - 1).zzbww)));
    }

    protected final void zza(Object obj, ahx ahxVar) {
        try {
            ahxVar.zzct(this.tag);
            switch (this.type) {
                case 10:
                    int i = this.tag >>> 3;
                    ((aif) obj).zza(ahxVar);
                    ahxVar.zzt(i, 4);
                    return;
                case 11:
                    ahxVar.zzc((aif) obj);
                    return;
                default:
                    throw new IllegalArgumentException(new StringBuilder(24).append("Unknown type ").append(this.type).toString());
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    protected final int zzav(Object obj) {
        int i = this.tag >>> 3;
        switch (this.type) {
            case 10:
                return (ahx.zzcs(i) << 1) + ((aif) obj).zzMl();
            case 11:
                return ahx.zzb(i, (aif) obj);
            default:
                throw new IllegalArgumentException(new StringBuilder(24).append("Unknown type ").append(this.type).toString());
        }
    }
}
