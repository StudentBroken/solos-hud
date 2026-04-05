package com.google.android.gms.common.internal;

import com.google.android.gms.common.api.Scope;
import java.util.Collections;
import java.util.Set;

/* JADX INFO: loaded from: classes3.dex */
public final class zzr {
    public final Set<Scope> zzamg;

    public zzr(Set<Scope> set) {
        zzbr.zzu(set);
        this.zzamg = Collections.unmodifiableSet(set);
    }
}
