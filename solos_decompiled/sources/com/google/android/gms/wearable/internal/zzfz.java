package com.google.android.gms.wearable.internal;

import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Log;
import com.digits.sdk.vcard.VCardConfig;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.internal.zzbcl;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/* JADX INFO: loaded from: classes6.dex */
final class zzfz implements Runnable {
    private /* synthetic */ String zzaks;
    private /* synthetic */ long zzbSo;
    private /* synthetic */ long zzbSp;
    private /* synthetic */ zzbcl zzbTs;
    private /* synthetic */ zzfw zzbTt;
    private /* synthetic */ Uri zzbzV;

    zzfz(zzfw zzfwVar, Uri uri, zzbcl zzbclVar, String str, long j, long j2) {
        this.zzbTt = zzfwVar;
        this.zzbzV = uri;
        this.zzbTs = zzbclVar;
        this.zzaks = str;
        this.zzbSo = j;
        this.zzbSp = j2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        if (Log.isLoggable("WearableClient", 2)) {
            Log.v("WearableClient", "Executing sendFileToChannelTask");
        }
        if (!"file".equals(this.zzbzV.getScheme())) {
            Log.w("WearableClient", "Channel.sendFile used with non-file URI");
            this.zzbTs.zzr(new Status(10, "Channel.sendFile used with non-file URI"));
            return;
        }
        File file = new File(this.zzbzV.getPath());
        try {
            ParcelFileDescriptor parcelFileDescriptorOpen = ParcelFileDescriptor.open(file, VCardConfig.FLAG_REFRAIN_QP_TO_NAME_PROPERTIES);
            try {
                try {
                    ((zzdn) this.zzbTt.zzrd()).zza(new zzfs(this.zzbTs), this.zzaks, parcelFileDescriptorOpen, this.zzbSo, this.zzbSp);
                } finally {
                    try {
                        parcelFileDescriptorOpen.close();
                    } catch (IOException e) {
                        Log.w("WearableClient", "Failed to close sourceFd", e);
                    }
                }
            } catch (RemoteException e2) {
                Log.w("WearableClient", "Channel.sendFile failed.", e2);
                this.zzbTs.zzr(new Status(8));
                try {
                    parcelFileDescriptorOpen.close();
                } catch (IOException e3) {
                    Log.w("WearableClient", "Failed to close sourceFd", e3);
                }
            }
        } catch (FileNotFoundException e4) {
            String strValueOf = String.valueOf(file);
            Log.w("WearableClient", new StringBuilder(String.valueOf(strValueOf).length() + 46).append("File couldn't be opened for Channel.sendFile: ").append(strValueOf).toString());
            this.zzbTs.zzr(new Status(13));
        }
    }
}
