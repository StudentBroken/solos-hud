package com.google.android.gms.wearable.internal;

import android.os.ParcelFileDescriptor;
import android.util.Log;
import java.io.IOException;
import java.util.concurrent.Callable;

/* JADX INFO: loaded from: classes6.dex */
final class zzfx implements Callable<Boolean> {
    private /* synthetic */ byte[] zzbKS;
    private /* synthetic */ ParcelFileDescriptor zzbTr;

    zzfx(zzfw zzfwVar, ParcelFileDescriptor parcelFileDescriptor, byte[] bArr) {
        this.zzbTr = parcelFileDescriptor;
        this.zzbKS = bArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @Override // java.util.concurrent.Callable
    /* JADX INFO: renamed from: zzDU, reason: merged with bridge method [inline-methods] */
    public final Boolean call() {
        if (Log.isLoggable("WearableClient", 3)) {
            String strValueOf = String.valueOf(this.zzbTr);
            Log.d("WearableClient", new StringBuilder(String.valueOf(strValueOf).length() + 36).append("processAssets: writing data to FD : ").append(strValueOf).toString());
        }
        ParcelFileDescriptor.AutoCloseOutputStream autoCloseOutputStream = new ParcelFileDescriptor.AutoCloseOutputStream(this.zzbTr);
        try {
            try {
                autoCloseOutputStream.write(this.zzbKS);
                autoCloseOutputStream.flush();
                if (Log.isLoggable("WearableClient", 3)) {
                    String strValueOf2 = String.valueOf(this.zzbTr);
                    Log.d("WearableClient", new StringBuilder(String.valueOf(strValueOf2).length() + 27).append("processAssets: wrote data: ").append(strValueOf2).toString());
                }
                try {
                    if (Log.isLoggable("WearableClient", 3)) {
                        String strValueOf3 = String.valueOf(this.zzbTr);
                        Log.d("WearableClient", new StringBuilder(String.valueOf(strValueOf3).length() + 24).append("processAssets: closing: ").append(strValueOf3).toString());
                    }
                    autoCloseOutputStream.close();
                    return true;
                } catch (IOException e) {
                    return true;
                }
            } catch (IOException e2) {
                String strValueOf4 = String.valueOf(this.zzbTr);
                Log.w("WearableClient", new StringBuilder(String.valueOf(strValueOf4).length() + 36).append("processAssets: writing data failed: ").append(strValueOf4).toString());
                return false;
            }
        } finally {
            try {
                if (Log.isLoggable("WearableClient", 3)) {
                    String strValueOf5 = String.valueOf(this.zzbTr);
                    Log.d("WearableClient", new StringBuilder(String.valueOf(strValueOf5).length() + 24).append("processAssets: closing: ").append(strValueOf5).toString());
                }
                autoCloseOutputStream.close();
            } catch (IOException e3) {
            }
        }
    }
}
