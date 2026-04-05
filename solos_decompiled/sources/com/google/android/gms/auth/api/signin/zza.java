package com.google.android.gms.auth.api.signin;

import com.google.android.gms.common.api.Scope;
import java.util.Comparator;

/* JADX INFO: loaded from: classes3.dex */
final class zza implements Comparator<Scope> {
    zza() {
    }

    @Override // java.util.Comparator
    public final /* synthetic */ int compare(Scope scope, Scope scope2) {
        return scope.zzpn().compareTo(scope2.zzpn());
    }
}
