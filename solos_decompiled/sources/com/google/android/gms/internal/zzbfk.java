package com.google.android.gms.internal;

/* JADX INFO: loaded from: classes3.dex */
public final class zzbfk<L> {
    private final L mListener;
    private final String zzaER;

    zzbfk(L l, String str) {
        this.mListener = l;
        this.zzaER = str;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof zzbfk)) {
            return false;
        }
        zzbfk zzbfkVar = (zzbfk) obj;
        return this.mListener == zzbfkVar.mListener && this.zzaER.equals(zzbfkVar.zzaER);
    }

    public final int hashCode() {
        return (System.identityHashCode(this.mListener) * 31) + this.zzaER.hashCode();
    }
}
