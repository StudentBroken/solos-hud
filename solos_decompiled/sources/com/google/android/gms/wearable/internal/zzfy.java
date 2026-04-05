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
final class zzfy implements Runnable {
    private /* synthetic */ String zzaks;
    private /* synthetic */ boolean zzbSn;
    private /* synthetic */ zzbcl zzbTs;
    private /* synthetic */ zzfw zzbTt;
    private /* synthetic */ Uri zzbzV;

    zzfy(zzfw zzfwVar, Uri uri, zzbcl zzbclVar, boolean z, String str) {
        this.zzbTt = zzfwVar;
        this.zzbzV = uri;
        this.zzbTs = zzbclVar;
        this.zzbSn = z;
        this.zzaks = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        if (Log.isLoggable("WearableClient", 2)) {
            Log.v("WearableClient", "Executing receiveFileFromChannelTask");
        }
        if (!"file".equals(this.zzbzV.getScheme())) {
            Log.w("WearableClient", "Channel.receiveFile used with non-file URI");
            this.zzbTs.zzr(new Status(10, "Channel.receiveFile used with non-file URI"));
            return;
        }
        File file = new File(this.zzbzV.getPath());
        try {
            ParcelFileDescriptor parcelFileDescriptorOpen = ParcelFileDescriptor.open(file, (this.zzbSn ? VCardConfig.FLAG_REFRAIN_PHONE_NUMBER_FORMATTING : 0) | 671088640);
            try {
                try {
                    ((zzdn) this.zzbTt.zzrd()).zza(new zzfv(this.zzbTs), this.zzaks, parcelFileDescriptorOpen);
                    try {
                        parcelFileDescriptorOpen.close();
                    } catch (IOException e) {
                        Log.w("WearableClient", "Failed to close targetFd", e);
                    }
                } catch (RemoteException e2) {
                    Log.w("WearableClient", "Channel.receiveFile failed.", e2);
                    this.zzbTs.zzr(new Status(8));
                    try {
                        parcelFileDescriptorOpen.close();
                    } catch (IOException e3) {
                        Log.w("WearableClient", "Failed to close targetFd", e3);
                    }
                }
            } catch (Throwable th) {
                try {
                    parcelFileDescriptorOpen.close();
                } catch (IOException e4) {
                    Log.w("WearableClient", "Failed to close targetFd", e4);
                }
                throw th;
            }
        } catch (FileNotFoundException e5) {
            String strValueOf = String.valueOf(file);
            Log.w("WearableClient", new StringBuilder(String.valueOf(strValueOf).length() + 49).append("File couldn't be opened for Channel.receiveFile: ").append(strValueOf).toString());
            this.zzbTs.zzr(new Status(13));
        }
    }
}
