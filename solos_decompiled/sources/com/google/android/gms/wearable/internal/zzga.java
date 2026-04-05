package com.google.android.gms.wearable.internal;

import android.content.IntentFilter;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.internal.zzbfi;
import com.google.android.gms.wearable.CapabilityApi;
import com.google.android.gms.wearable.ChannelApi;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.NodeApi;
import java.util.List;

/* JADX INFO: loaded from: classes6.dex */
public final class zzga<T> extends zzdl {
    private final IntentFilter[] zzbSY;
    private zzbfi<ChannelApi.ChannelListener> zzbTA;
    private zzbfi<CapabilityApi.CapabilityListener> zzbTB;
    private final String zzbTC;
    private zzbfi<Object> zzbTu;
    private zzbfi<Object> zzbTv;
    private zzbfi<DataApi.DataListener> zzbTw;
    private zzbfi<MessageApi.MessageListener> zzbTx;
    private zzbfi<NodeApi.NodeListener> zzbTy;
    private zzbfi<Object> zzbTz;

    private zzga(IntentFilter[] intentFilterArr, String str) {
        this.zzbSY = (IntentFilter[]) com.google.android.gms.common.internal.zzbr.zzu(intentFilterArr);
        this.zzbTC = str;
    }

    public static zzga<ChannelApi.ChannelListener> zza(zzbfi<ChannelApi.ChannelListener> zzbfiVar, String str, IntentFilter[] intentFilterArr) {
        zzga<ChannelApi.ChannelListener> zzgaVar = new zzga<>(intentFilterArr, (String) com.google.android.gms.common.internal.zzbr.zzu(str));
        ((zzga) zzgaVar).zzbTA = (zzbfi) com.google.android.gms.common.internal.zzbr.zzu(zzbfiVar);
        return zzgaVar;
    }

    public static zzga<DataApi.DataListener> zza(zzbfi<DataApi.DataListener> zzbfiVar, IntentFilter[] intentFilterArr) {
        zzga<DataApi.DataListener> zzgaVar = new zzga<>(intentFilterArr, null);
        ((zzga) zzgaVar).zzbTw = (zzbfi) com.google.android.gms.common.internal.zzbr.zzu(zzbfiVar);
        return zzgaVar;
    }

    public static zzga<MessageApi.MessageListener> zzb(zzbfi<MessageApi.MessageListener> zzbfiVar, IntentFilter[] intentFilterArr) {
        zzga<MessageApi.MessageListener> zzgaVar = new zzga<>(intentFilterArr, null);
        ((zzga) zzgaVar).zzbTx = (zzbfi) com.google.android.gms.common.internal.zzbr.zzu(zzbfiVar);
        return zzgaVar;
    }

    public static zzga<NodeApi.NodeListener> zzc(zzbfi<NodeApi.NodeListener> zzbfiVar, IntentFilter[] intentFilterArr) {
        zzga<NodeApi.NodeListener> zzgaVar = new zzga<>(intentFilterArr, null);
        ((zzga) zzgaVar).zzbTy = (zzbfi) com.google.android.gms.common.internal.zzbr.zzu(zzbfiVar);
        return zzgaVar;
    }

    public static zzga<ChannelApi.ChannelListener> zzd(zzbfi<ChannelApi.ChannelListener> zzbfiVar, IntentFilter[] intentFilterArr) {
        zzga<ChannelApi.ChannelListener> zzgaVar = new zzga<>(intentFilterArr, null);
        ((zzga) zzgaVar).zzbTA = (zzbfi) com.google.android.gms.common.internal.zzbr.zzu(zzbfiVar);
        return zzgaVar;
    }

    public static zzga<CapabilityApi.CapabilityListener> zze(zzbfi<CapabilityApi.CapabilityListener> zzbfiVar, IntentFilter[] intentFilterArr) {
        zzga<CapabilityApi.CapabilityListener> zzgaVar = new zzga<>(intentFilterArr, null);
        ((zzga) zzgaVar).zzbTB = (zzbfi) com.google.android.gms.common.internal.zzbr.zzu(zzbfiVar);
        return zzgaVar;
    }

    private static void zzk(zzbfi<?> zzbfiVar) {
        if (zzbfiVar != null) {
            zzbfiVar.clear();
        }
    }

    public final void clear() {
        zzk(null);
        this.zzbTu = null;
        zzk(null);
        this.zzbTv = null;
        zzk(this.zzbTw);
        this.zzbTw = null;
        zzk(this.zzbTx);
        this.zzbTx = null;
        zzk(this.zzbTy);
        this.zzbTy = null;
        zzk(null);
        this.zzbTz = null;
        zzk(this.zzbTA);
        this.zzbTA = null;
        zzk(this.zzbTB);
        this.zzbTB = null;
    }

    @Override // com.google.android.gms.wearable.internal.zzdk
    public final void onConnectedNodes(List<zzeg> list) {
    }

    public final IntentFilter[] zzDV() {
        return this.zzbSY;
    }

    public final String zzDW() {
        return this.zzbTC;
    }

    @Override // com.google.android.gms.wearable.internal.zzdk
    public final void zzS(DataHolder dataHolder) {
        if (this.zzbTw != null) {
            this.zzbTw.zza(new zzgb(dataHolder));
        } else {
            dataHolder.close();
        }
    }

    @Override // com.google.android.gms.wearable.internal.zzdk
    public final void zza(zzaa zzaaVar) {
        if (this.zzbTB != null) {
            this.zzbTB.zza(new zzgg(zzaaVar));
        }
    }

    @Override // com.google.android.gms.wearable.internal.zzdk
    public final void zza(zzai zzaiVar) {
        if (this.zzbTA != null) {
            this.zzbTA.zza(new zzgf(zzaiVar));
        }
    }

    @Override // com.google.android.gms.wearable.internal.zzdk
    public final void zza(zzdx zzdxVar) {
        if (this.zzbTx != null) {
            this.zzbTx.zza(new zzgc(zzdxVar));
        }
    }

    @Override // com.google.android.gms.wearable.internal.zzdk
    public final void zza(zzeg zzegVar) {
        if (this.zzbTy != null) {
            this.zzbTy.zza(new zzgd(zzegVar));
        }
    }

    @Override // com.google.android.gms.wearable.internal.zzdk
    public final void zza(zzi zziVar) {
    }

    @Override // com.google.android.gms.wearable.internal.zzdk
    public final void zza(zzl zzlVar) {
    }

    @Override // com.google.android.gms.wearable.internal.zzdk
    public final void zzb(zzeg zzegVar) {
        if (this.zzbTy != null) {
            this.zzbTy.zza(new zzge(zzegVar));
        }
    }
}
