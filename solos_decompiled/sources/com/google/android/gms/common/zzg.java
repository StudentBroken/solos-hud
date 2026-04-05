package com.google.android.gms.common;

import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.internal.zzas;
import com.google.android.gms.common.internal.zzat;
import com.google.android.gms.common.internal.zzbr;
import com.google.android.gms.dynamic.IObjectWrapper;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/* JADX INFO: loaded from: classes67.dex */
abstract class zzg extends zzat {
    private int zzaAi;

    protected zzg(byte[] bArr) {
        if (bArr.length != 25) {
            int length = bArr.length;
            String strValueOf = String.valueOf(com.google.android.gms.common.util.zzn.zza(bArr, 0, bArr.length, false));
            Log.wtf("GoogleCertificates", new StringBuilder(String.valueOf(strValueOf).length() + 51).append("Cert hash data has incorrect length (").append(length).append("):\n").append(strValueOf).toString(), new Exception());
            bArr = Arrays.copyOfRange(bArr, 0, 25);
            zzbr.zzb(bArr.length == 25, new StringBuilder(55).append("cert hash data has incorrect length. length=").append(bArr.length).toString());
        }
        this.zzaAi = Arrays.hashCode(bArr);
    }

    protected static byte[] zzcs(String str) {
        try {
            return str.getBytes("ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    public boolean equals(Object obj) {
        IObjectWrapper iObjectWrapperZzoW;
        if (obj == null || !(obj instanceof zzas)) {
            return false;
        }
        try {
            zzas zzasVar = (zzas) obj;
            if (zzasVar.zzoX() == hashCode() && (iObjectWrapperZzoW = zzasVar.zzoW()) != null) {
                return Arrays.equals(getBytes(), (byte[]) com.google.android.gms.dynamic.zzn.zzE(iObjectWrapperZzoW));
            }
            return false;
        } catch (RemoteException e) {
            Log.e("GoogleCertificates", "Failed to get Google certificates from remote", e);
            return false;
        }
    }

    abstract byte[] getBytes();

    public int hashCode() {
        return this.zzaAi;
    }

    @Override // com.google.android.gms.common.internal.zzas
    public final IObjectWrapper zzoW() {
        return com.google.android.gms.dynamic.zzn.zzw(getBytes());
    }

    @Override // com.google.android.gms.common.internal.zzas
    public final int zzoX() {
        return hashCode();
    }
}
