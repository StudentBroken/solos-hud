package com.google.android.gms.wearable.internal;

import android.content.IntentFilter;
import android.net.Uri;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wearable.CapabilityApi;

/* JADX INFO: loaded from: classes6.dex */
public final class zzo implements CapabilityApi {
    private static PendingResult<Status> zza(GoogleApiClient googleApiClient, CapabilityApi.CapabilityListener capabilityListener, IntentFilter[] intentFilterArr) {
        return zzb.zza(googleApiClient, new zzt(intentFilterArr), capabilityListener);
    }

    @Override // com.google.android.gms.wearable.CapabilityApi
    public final PendingResult<Status> addCapabilityListener(GoogleApiClient googleApiClient, CapabilityApi.CapabilityListener capabilityListener, String str) {
        String strConcat;
        com.google.android.gms.common.internal.zzbr.zzb(str != null, "capability must not be null");
        zzv zzvVar = new zzv(capabilityListener, str);
        IntentFilter intentFilterZzgn = zzez.zzgn(CapabilityApi.ACTION_CAPABILITY_CHANGED);
        if (str.startsWith("/")) {
            strConcat = str;
        } else {
            String strValueOf = String.valueOf(str);
            strConcat = strValueOf.length() != 0 ? "/".concat(strValueOf) : new String("/");
        }
        intentFilterZzgn.addDataPath(strConcat, 0);
        return zza(googleApiClient, zzvVar, new IntentFilter[]{intentFilterZzgn});
    }

    @Override // com.google.android.gms.wearable.CapabilityApi
    public final PendingResult<Status> addListener(GoogleApiClient googleApiClient, CapabilityApi.CapabilityListener capabilityListener, Uri uri, int i) {
        com.google.android.gms.common.internal.zzbr.zzb(uri != null, "uri must not be null");
        com.google.android.gms.common.internal.zzbr.zzb(i == 0 || i == 1, "invalid filter type");
        return zza(googleApiClient, capabilityListener, new IntentFilter[]{zzez.zza(CapabilityApi.ACTION_CAPABILITY_CHANGED, uri, i)});
    }

    @Override // com.google.android.gms.wearable.CapabilityApi
    public final PendingResult<CapabilityApi.AddLocalCapabilityResult> addLocalCapability(GoogleApiClient googleApiClient, String str) {
        return googleApiClient.zzd(new zzr(this, googleApiClient, str));
    }

    @Override // com.google.android.gms.wearable.CapabilityApi
    public final PendingResult<CapabilityApi.GetAllCapabilitiesResult> getAllCapabilities(GoogleApiClient googleApiClient, int i) {
        boolean z = true;
        if (i != 0 && i != 1) {
            z = false;
        }
        com.google.android.gms.common.internal.zzbr.zzaf(z);
        return googleApiClient.zzd(new zzq(this, googleApiClient, i));
    }

    @Override // com.google.android.gms.wearable.CapabilityApi
    public final PendingResult<CapabilityApi.GetCapabilityResult> getCapability(GoogleApiClient googleApiClient, String str, int i) {
        boolean z = true;
        if (i != 0 && i != 1) {
            z = false;
        }
        com.google.android.gms.common.internal.zzbr.zzaf(z);
        return googleApiClient.zzd(new zzp(this, googleApiClient, str, i));
    }

    @Override // com.google.android.gms.wearable.CapabilityApi
    public final PendingResult<Status> removeCapabilityListener(GoogleApiClient googleApiClient, CapabilityApi.CapabilityListener capabilityListener, String str) {
        return googleApiClient.zzd(new zzz(googleApiClient, new zzv(capabilityListener, str), null));
    }

    @Override // com.google.android.gms.wearable.CapabilityApi
    public final PendingResult<Status> removeListener(GoogleApiClient googleApiClient, CapabilityApi.CapabilityListener capabilityListener) {
        return googleApiClient.zzd(new zzz(googleApiClient, capabilityListener, null));
    }

    @Override // com.google.android.gms.wearable.CapabilityApi
    public final PendingResult<CapabilityApi.RemoveLocalCapabilityResult> removeLocalCapability(GoogleApiClient googleApiClient, String str) {
        return googleApiClient.zzd(new zzs(this, googleApiClient, str));
    }
}
