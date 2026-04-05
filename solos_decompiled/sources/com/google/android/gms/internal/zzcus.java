package com.google.android.gms.internal;

import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Scope;

/* JADX INFO: loaded from: classes3.dex */
public final class zzcus {
    private static Api.zzf<zzcvg> zzajT = new Api.zzf<>();
    private static Api.zzf<zzcvg> zzbCN = new Api.zzf<>();
    public static final Api.zza<zzcvg, zzcux> zzajU = new zzcut();
    private static Api.zza<zzcvg, zzcuv> zzbCO = new zzcuu();
    private static Scope zzalX = new Scope(Scopes.PROFILE);
    private static Scope zzalY = new Scope("email");
    public static final Api<zzcux> API = new Api<>("SignIn.API", zzajU, zzajT);
    private static Api<zzcuv> zzaMg = new Api<>("SignIn.INTERNAL_API", zzbCO, zzbCN);
}
