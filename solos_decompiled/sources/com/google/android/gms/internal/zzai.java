package com.google.android.gms.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/* JADX INFO: loaded from: classes67.dex */
final class zzai {
    public String key;
    public String zza;
    public long zzaz;
    public long zzb;
    public long zzc;
    public long zzd;
    public long zze;
    public Map<String, String> zzf;

    private zzai() {
    }

    public zzai(String str, zzc zzcVar) {
        this.key = str;
        this.zzaz = zzcVar.data.length;
        this.zza = zzcVar.zza;
        this.zzb = zzcVar.zzb;
        this.zzc = zzcVar.zzc;
        this.zzd = zzcVar.zzd;
        this.zze = zzcVar.zze;
        this.zzf = zzcVar.zzf;
    }

    public static zzai zzf(InputStream inputStream) throws IOException {
        zzai zzaiVar = new zzai();
        if (zzag.zzb(inputStream) != 538247942) {
            throw new IOException();
        }
        zzaiVar.key = zzag.zzd(inputStream);
        zzaiVar.zza = zzag.zzd(inputStream);
        if (zzaiVar.zza.equals("")) {
            zzaiVar.zza = null;
        }
        zzaiVar.zzb = zzag.zzc(inputStream);
        zzaiVar.zzc = zzag.zzc(inputStream);
        zzaiVar.zzd = zzag.zzc(inputStream);
        zzaiVar.zze = zzag.zzc(inputStream);
        zzaiVar.zzf = zzag.zze(inputStream);
        return zzaiVar;
    }

    public final boolean zza(OutputStream outputStream) {
        try {
            zzag.zza(outputStream, 538247942);
            zzag.zza(outputStream, this.key);
            zzag.zza(outputStream, this.zza == null ? "" : this.zza);
            zzag.zza(outputStream, this.zzb);
            zzag.zza(outputStream, this.zzc);
            zzag.zza(outputStream, this.zzd);
            zzag.zza(outputStream, this.zze);
            Map<String, String> map = this.zzf;
            if (map != null) {
                zzag.zza(outputStream, map.size());
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    zzag.zza(outputStream, entry.getKey());
                    zzag.zza(outputStream, entry.getValue());
                }
            } else {
                zzag.zza(outputStream, 0);
            }
            outputStream.flush();
            return true;
        } catch (IOException e) {
            zzab.zzb("%s", e.toString());
            return false;
        }
    }
}
