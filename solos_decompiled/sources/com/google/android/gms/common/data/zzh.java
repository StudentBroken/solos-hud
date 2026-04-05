package com.google.android.gms.common.data;

import java.util.NoSuchElementException;

/* JADX INFO: loaded from: classes3.dex */
public final class zzh<T> extends zzb<T> {
    private T zzaFS;

    public zzh(DataBuffer<T> dataBuffer) {
        super(dataBuffer);
    }

    @Override // com.google.android.gms.common.data.zzb, java.util.Iterator
    public final T next() {
        if (!hasNext()) {
            throw new NoSuchElementException(new StringBuilder(46).append("Cannot advance the iterator beyond ").append(this.zzaFx).toString());
        }
        this.zzaFx++;
        if (this.zzaFx == 0) {
            this.zzaFS = this.zzaFw.get(0);
            if (!(this.zzaFS instanceof zzc)) {
                String strValueOf = String.valueOf(this.zzaFS.getClass());
                throw new IllegalStateException(new StringBuilder(String.valueOf(strValueOf).length() + 44).append("DataBuffer reference of type ").append(strValueOf).append(" is not movable").toString());
            }
        } else {
            ((zzc) this.zzaFS).zzar(this.zzaFx);
        }
        return this.zzaFS;
    }
}
