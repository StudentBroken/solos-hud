package com.google.android.gms.wearable.internal;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.internal.zzbcl;
import com.google.android.gms.internal.zzbfi;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.CapabilityApi;
import com.google.android.gms.wearable.ChannelApi;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.WearableStatusCodes;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/* JADX INFO: loaded from: classes6.dex */
public final class zzfw extends com.google.android.gms.common.internal.zzaa<zzdn> {
    private final zzdp<Object> zzbTi;
    private final zzdp<Object> zzbTj;
    private final zzdp<ChannelApi.ChannelListener> zzbTk;
    private final zzdp<DataApi.DataListener> zzbTl;
    private final zzdp<MessageApi.MessageListener> zzbTm;
    private final zzdp<NodeApi.NodeListener> zzbTn;
    private final zzdp<Object> zzbTo;
    private final zzdp<CapabilityApi.CapabilityListener> zzbTp;
    private final zzgh zzbTq;
    private final ExecutorService zzbrZ;

    public zzfw(Context context, Looper looper, GoogleApiClient.ConnectionCallbacks connectionCallbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener, com.google.android.gms.common.internal.zzq zzqVar) {
        this(context, looper, connectionCallbacks, onConnectionFailedListener, zzqVar, Executors.newCachedThreadPool(), zzgh.zzbz(context));
    }

    private zzfw(Context context, Looper looper, GoogleApiClient.ConnectionCallbacks connectionCallbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener, com.google.android.gms.common.internal.zzq zzqVar, ExecutorService executorService, zzgh zzghVar) {
        super(context, looper, 14, zzqVar, connectionCallbacks, onConnectionFailedListener);
        this.zzbTi = new zzdp<>();
        this.zzbTj = new zzdp<>();
        this.zzbTk = new zzdp<>();
        this.zzbTl = new zzdp<>();
        this.zzbTm = new zzdp<>();
        this.zzbTn = new zzdp<>();
        this.zzbTo = new zzdp<>();
        this.zzbTp = new zzdp<>();
        this.zzbrZ = (ExecutorService) com.google.android.gms.common.internal.zzbr.zzu(executorService);
        this.zzbTq = zzghVar;
    }

    @Override // com.google.android.gms.common.internal.zzd
    protected final void zza(int i, IBinder iBinder, Bundle bundle, int i2) {
        if (Log.isLoggable("WearableClient", 2)) {
            Log.d("WearableClient", new StringBuilder(41).append("onPostInitHandler: statusCode ").append(i).toString());
        }
        if (i == 0) {
            this.zzbTi.zzam(iBinder);
            this.zzbTj.zzam(iBinder);
            this.zzbTk.zzam(iBinder);
            this.zzbTl.zzam(iBinder);
            this.zzbTm.zzam(iBinder);
            this.zzbTn.zzam(iBinder);
            this.zzbTo.zzam(iBinder);
            this.zzbTp.zzam(iBinder);
        }
        super.zza(i, iBinder, bundle, i2);
    }

    @Override // com.google.android.gms.common.internal.zzd, com.google.android.gms.common.api.Api.zze
    public final void zza(@NonNull com.google.android.gms.common.internal.zzj zzjVar) {
        if (!zzpc()) {
            try {
                Bundle bundle = getContext().getPackageManager().getApplicationInfo("com.google.android.wearable.app.cn", 128).metaData;
                int i = bundle != null ? bundle.getInt("com.google.android.wearable.api.version", 0) : 0;
                if (i < com.google.android.gms.common.zze.GOOGLE_PLAY_SERVICES_VERSION_CODE) {
                    Log.w("WearableClient", new StringBuilder(80).append("Android Wear out of date. Requires API version ").append(com.google.android.gms.common.zze.GOOGLE_PLAY_SERVICES_VERSION_CODE).append(" but found ").append(i).toString());
                    Context context = getContext();
                    Context context2 = getContext();
                    Intent intent = new Intent("com.google.android.wearable.app.cn.UPDATE_ANDROID_WEAR").setPackage("com.google.android.wearable.app.cn");
                    if (context2.getPackageManager().resolveActivity(intent, 65536) == null) {
                        intent = new Intent("android.intent.action.VIEW", Uri.parse("market://details").buildUpon().appendQueryParameter("id", "com.google.android.wearable.app.cn").build());
                    }
                    zza(zzjVar, 6, PendingIntent.getActivity(context, 0, intent, 0));
                    return;
                }
            } catch (PackageManager.NameNotFoundException e) {
                zza(zzjVar, 16, (PendingIntent) null);
                return;
            }
        }
        super.zza(zzjVar);
    }

    public final void zza(zzbcl<DataApi.GetFdForAssetResult> zzbclVar, Asset asset) throws RemoteException {
        ((zzdn) zzrd()).zza(new zzfn(zzbclVar), asset);
    }

    public final void zza(zzbcl<Status> zzbclVar, CapabilityApi.CapabilityListener capabilityListener) throws RemoteException {
        this.zzbTp.zza(this, zzbclVar, capabilityListener);
    }

    public final void zza(zzbcl<Status> zzbclVar, CapabilityApi.CapabilityListener capabilityListener, zzbfi<CapabilityApi.CapabilityListener> zzbfiVar, IntentFilter[] intentFilterArr) throws RemoteException {
        this.zzbTp.zza(this, zzbclVar, capabilityListener, zzga.zze(zzbfiVar, intentFilterArr));
    }

    public final void zza(zzbcl<Status> zzbclVar, ChannelApi.ChannelListener channelListener, zzbfi<ChannelApi.ChannelListener> zzbfiVar, String str, IntentFilter[] intentFilterArr) throws RemoteException {
        if (str == null) {
            this.zzbTk.zza(this, zzbclVar, channelListener, zzga.zzd(zzbfiVar, intentFilterArr));
        } else {
            this.zzbTk.zza(this, zzbclVar, new zzeu(str, channelListener), zzga.zza(zzbfiVar, str, intentFilterArr));
        }
    }

    public final void zza(zzbcl<Status> zzbclVar, ChannelApi.ChannelListener channelListener, String str) throws RemoteException {
        if (str == null) {
            this.zzbTk.zza(this, zzbclVar, channelListener);
        } else {
            this.zzbTk.zza(this, zzbclVar, new zzeu(str, channelListener));
        }
    }

    public final void zza(zzbcl<Status> zzbclVar, DataApi.DataListener dataListener) throws RemoteException {
        this.zzbTl.zza(this, zzbclVar, dataListener);
    }

    public final void zza(zzbcl<Status> zzbclVar, DataApi.DataListener dataListener, zzbfi<DataApi.DataListener> zzbfiVar, IntentFilter[] intentFilterArr) throws RemoteException {
        this.zzbTl.zza(this, zzbclVar, dataListener, zzga.zza(zzbfiVar, intentFilterArr));
    }

    public final void zza(zzbcl<Status> zzbclVar, MessageApi.MessageListener messageListener) throws RemoteException {
        this.zzbTm.zza(this, zzbclVar, messageListener);
    }

    public final void zza(zzbcl<Status> zzbclVar, MessageApi.MessageListener messageListener, zzbfi<MessageApi.MessageListener> zzbfiVar, IntentFilter[] intentFilterArr) throws RemoteException {
        this.zzbTm.zza(this, zzbclVar, messageListener, zzga.zzb(zzbfiVar, intentFilterArr));
    }

    public final void zza(zzbcl<Status> zzbclVar, NodeApi.NodeListener nodeListener) throws RemoteException {
        this.zzbTn.zza(this, zzbclVar, nodeListener);
    }

    public final void zza(zzbcl<Status> zzbclVar, NodeApi.NodeListener nodeListener, zzbfi<NodeApi.NodeListener> zzbfiVar, IntentFilter[] intentFilterArr) throws RemoteException {
        this.zzbTn.zza(this, zzbclVar, nodeListener, zzga.zzc(zzbfiVar, intentFilterArr));
    }

    public final void zza(zzbcl<DataApi.DataItemResult> zzbclVar, PutDataRequest putDataRequest) throws RemoteException {
        Iterator<Map.Entry<String, Asset>> it = putDataRequest.getAssets().entrySet().iterator();
        while (it.hasNext()) {
            Asset value = it.next().getValue();
            if (value.getData() == null && value.getDigest() == null && value.getFd() == null && value.getUri() == null) {
                String strValueOf = String.valueOf(putDataRequest.getUri());
                String strValueOf2 = String.valueOf(value);
                throw new IllegalArgumentException(new StringBuilder(String.valueOf(strValueOf).length() + 33 + String.valueOf(strValueOf2).length()).append("Put for ").append(strValueOf).append(" contains invalid asset: ").append(strValueOf2).toString());
            }
        }
        PutDataRequest putDataRequestZzt = PutDataRequest.zzt(putDataRequest.getUri());
        putDataRequestZzt.setData(putDataRequest.getData());
        if (putDataRequest.isUrgent()) {
            putDataRequestZzt.setUrgent();
        }
        ArrayList arrayList = new ArrayList();
        for (Map.Entry<String, Asset> entry : putDataRequest.getAssets().entrySet()) {
            Asset value2 = entry.getValue();
            if (value2.getData() != null) {
                try {
                    ParcelFileDescriptor[] parcelFileDescriptorArrCreatePipe = ParcelFileDescriptor.createPipe();
                    if (Log.isLoggable("WearableClient", 3)) {
                        String strValueOf3 = String.valueOf(value2);
                        String strValueOf4 = String.valueOf(parcelFileDescriptorArrCreatePipe[0]);
                        String strValueOf5 = String.valueOf(parcelFileDescriptorArrCreatePipe[1]);
                        Log.d("WearableClient", new StringBuilder(String.valueOf(strValueOf3).length() + 61 + String.valueOf(strValueOf4).length() + String.valueOf(strValueOf5).length()).append("processAssets: replacing data with FD in asset: ").append(strValueOf3).append(" read:").append(strValueOf4).append(" write:").append(strValueOf5).toString());
                    }
                    putDataRequestZzt.putAsset(entry.getKey(), Asset.createFromFd(parcelFileDescriptorArrCreatePipe[0]));
                    FutureTask futureTask = new FutureTask(new zzfx(this, parcelFileDescriptorArrCreatePipe[1], value2.getData()));
                    arrayList.add(futureTask);
                    this.zzbrZ.submit(futureTask);
                } catch (IOException e) {
                    String strValueOf6 = String.valueOf(putDataRequest);
                    throw new IllegalStateException(new StringBuilder(String.valueOf(strValueOf6).length() + 60).append("Unable to create ParcelFileDescriptor for asset in request: ").append(strValueOf6).toString(), e);
                }
            } else if (value2.getUri() != null) {
                try {
                    putDataRequestZzt.putAsset(entry.getKey(), Asset.createFromFd(getContext().getContentResolver().openFileDescriptor(value2.getUri(), "r")));
                } catch (FileNotFoundException e2) {
                    new zzfr(zzbclVar, arrayList).zza(new zzem(WearableStatusCodes.ASSET_UNAVAILABLE, null));
                    String strValueOf7 = String.valueOf(value2.getUri());
                    Log.w("WearableClient", new StringBuilder(String.valueOf(strValueOf7).length() + 28).append("Couldn't resolve asset URI: ").append(strValueOf7).toString());
                    return;
                }
            } else {
                putDataRequestZzt.putAsset(entry.getKey(), value2);
            }
        }
        ((zzdn) zzrd()).zza(new zzfr(zzbclVar, arrayList), putDataRequestZzt);
    }

    public final void zza(zzbcl<Status> zzbclVar, String str, Uri uri, long j, long j2) {
        try {
            ExecutorService executorService = this.zzbrZ;
            com.google.android.gms.common.internal.zzbr.zzu(zzbclVar);
            com.google.android.gms.common.internal.zzbr.zzu(str);
            com.google.android.gms.common.internal.zzbr.zzu(uri);
            com.google.android.gms.common.internal.zzbr.zzb(j >= 0, "startOffset is negative: %s", Long.valueOf(j));
            com.google.android.gms.common.internal.zzbr.zzb(j2 >= -1, "invalid length: %s", Long.valueOf(j2));
            executorService.execute(new zzfz(this, uri, zzbclVar, str, j, j2));
        } catch (RuntimeException e) {
            zzbclVar.zzr(new Status(8));
            throw e;
        }
    }

    public final void zza(zzbcl<Status> zzbclVar, String str, Uri uri, boolean z) {
        try {
            ExecutorService executorService = this.zzbrZ;
            com.google.android.gms.common.internal.zzbr.zzu(zzbclVar);
            com.google.android.gms.common.internal.zzbr.zzu(str);
            com.google.android.gms.common.internal.zzbr.zzu(uri);
            executorService.execute(new zzfy(this, uri, zzbclVar, z, str));
        } catch (RuntimeException e) {
            zzbclVar.zzr(new Status(8));
            throw e;
        }
    }

    @Override // com.google.android.gms.common.internal.zzd
    protected final /* synthetic */ IInterface zzd(IBinder iBinder) {
        if (iBinder == null) {
            return null;
        }
        IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.wearable.internal.IWearableService");
        return iInterfaceQueryLocalInterface instanceof zzdn ? (zzdn) iInterfaceQueryLocalInterface : new zzdo(iBinder);
    }

    @Override // com.google.android.gms.common.internal.zzd
    protected final String zzda() {
        return "com.google.android.gms.wearable.BIND";
    }

    @Override // com.google.android.gms.common.internal.zzd
    protected final String zzdb() {
        return "com.google.android.gms.wearable.internal.IWearableService";
    }

    @Override // com.google.android.gms.common.internal.zzd, com.google.android.gms.common.api.Api.zze
    public final boolean zzpc() {
        return !this.zzbTq.zzgo("com.google.android.wearable.app.cn");
    }

    @Override // com.google.android.gms.common.internal.zzd
    protected final String zzqX() {
        return this.zzbTq.zzgo("com.google.android.wearable.app.cn") ? "com.google.android.wearable.app.cn" : "com.google.android.gms";
    }
}
