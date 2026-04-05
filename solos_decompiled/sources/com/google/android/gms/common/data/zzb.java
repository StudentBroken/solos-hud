package com.google.android.gms.common.data;

import com.google.android.gms.common.internal.zzbr;
import java.util.Iterator;
import java.util.NoSuchElementException;

/* JADX INFO: loaded from: classes3.dex */
public class zzb<T> implements Iterator<T> {
    protected final DataBuffer<T> zzaFw;
    protected int zzaFx = -1;

    public zzb(DataBuffer<T> dataBuffer) {
        this.zzaFw = (DataBuffer) zzbr.zzu(dataBuffer);
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this.zzaFx < this.zzaFw.getCount() + (-1);
    }

    @Override // java.util.Iterator
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException(new StringBuilder(46).append("Cannot advance the iterator beyond ").append(this.zzaFx).toString());
        }
        DataBuffer<T> dataBuffer = this.zzaFw;
        int i = this.zzaFx + 1;
        this.zzaFx = i;
        return dataBuffer.get(i);
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException("Cannot remove elements from a DataBufferIterator");
    }
}
