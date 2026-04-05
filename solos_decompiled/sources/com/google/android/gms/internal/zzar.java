package com.google.android.gms.internal;

import java.io.UnsupportedEncodingException;

/* JADX INFO: loaded from: classes67.dex */
public class zzar extends zzp<String> {
    private final zzv<String> zzaE;

    public zzar(int i, String str, zzv<String> zzvVar, zzu zzuVar) {
        super(i, str, zzuVar);
        this.zzaE = zzvVar;
    }

    @Override // com.google.android.gms.internal.zzp
    protected final zzt<String> zza(zzn zznVar) {
        String str;
        try {
            str = new String(zznVar.data, zzam.zza(zznVar.zzy));
        } catch (UnsupportedEncodingException e) {
            str = new String(zznVar.data);
        }
        return zzt.zza(str, zzam.zzb(zznVar));
    }

    @Override // com.google.android.gms.internal.zzp
    protected final /* synthetic */ void zza(String str) {
        this.zzaE.zzb(str);
    }
}
