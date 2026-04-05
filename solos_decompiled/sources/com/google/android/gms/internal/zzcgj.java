package com.google.android.gms.internal;

import java.util.Iterator;

/* JADX INFO: loaded from: classes36.dex */
final class zzcgj implements Iterator<String> {
    private Iterator<String> zzbpO;
    private /* synthetic */ zzcgi zzbpP;

    zzcgj(zzcgi zzcgiVar) {
        this.zzbpP = zzcgiVar;
        this.zzbpO = this.zzbpP.zzbpN.keySet().iterator();
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        return this.zzbpO.hasNext();
    }

    @Override // java.util.Iterator
    public final /* synthetic */ String next() {
        return this.zzbpO.next();
    }

    @Override // java.util.Iterator
    public final void remove() {
        throw new UnsupportedOperationException("Remove not supported");
    }
}
