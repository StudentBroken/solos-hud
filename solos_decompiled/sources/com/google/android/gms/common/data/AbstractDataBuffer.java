package com.google.android.gms.common.data;

import android.os.Bundle;
import java.util.Iterator;

/* JADX INFO: loaded from: classes3.dex */
public abstract class AbstractDataBuffer<T> implements DataBuffer<T> {
    protected final DataHolder zzaCZ;

    protected AbstractDataBuffer(DataHolder dataHolder) {
        this.zzaCZ = dataHolder;
    }

    @Override // com.google.android.gms.common.data.DataBuffer
    @Deprecated
    public final void close() {
        release();
    }

    @Override // com.google.android.gms.common.data.DataBuffer
    public abstract T get(int i);

    @Override // com.google.android.gms.common.data.DataBuffer
    public int getCount() {
        if (this.zzaCZ == null) {
            return 0;
        }
        return this.zzaCZ.zzaFI;
    }

    @Override // com.google.android.gms.common.data.DataBuffer
    @Deprecated
    public boolean isClosed() {
        return this.zzaCZ == null || this.zzaCZ.isClosed();
    }

    @Override // com.google.android.gms.common.data.DataBuffer, java.lang.Iterable
    public Iterator<T> iterator() {
        return new zzb(this);
    }

    @Override // com.google.android.gms.common.data.DataBuffer, com.google.android.gms.common.api.Releasable
    public void release() {
        if (this.zzaCZ != null) {
            this.zzaCZ.close();
        }
    }

    @Override // com.google.android.gms.common.data.DataBuffer
    public Iterator<T> singleRefIterator() {
        return new zzh(this);
    }

    @Override // com.google.android.gms.common.data.DataBuffer
    public final Bundle zzqL() {
        return this.zzaCZ.zzqL();
    }
}
