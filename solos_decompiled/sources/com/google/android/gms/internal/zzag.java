package com.google.android.gms.internal;

import android.os.SystemClock;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes67.dex */
public final class zzag implements zzb {
    private final Map<String, zzai> zzav;
    private long zzaw;
    private final File zzax;
    private final int zzay;

    public zzag(File file) {
        this(file, 5242880);
    }

    private zzag(File file, int i) {
        this.zzav = new LinkedHashMap(16, 0.75f, true);
        this.zzaw = 0L;
        this.zzax = file;
        this.zzay = 5242880;
    }

    private final synchronized void remove(String str) {
        boolean zDelete = zze(str).delete();
        zzai zzaiVar = this.zzav.get(str);
        if (zzaiVar != null) {
            this.zzaw -= zzaiVar.zzaz;
            this.zzav.remove(str);
        }
        if (!zDelete) {
            zzab.zzb("Could not delete cache entry for key=%s, filename=%s", str, zzd(str));
        }
    }

    private static int zza(InputStream inputStream) throws IOException {
        int i = inputStream.read();
        if (i == -1) {
            throw new EOFException();
        }
        return i;
    }

    static void zza(OutputStream outputStream, int i) throws IOException {
        outputStream.write(i & 255);
        outputStream.write((i >> 8) & 255);
        outputStream.write((i >> 16) & 255);
        outputStream.write(i >>> 24);
    }

    static void zza(OutputStream outputStream, long j) throws IOException {
        outputStream.write((byte) j);
        outputStream.write((byte) (j >>> 8));
        outputStream.write((byte) (j >>> 16));
        outputStream.write((byte) (j >>> 24));
        outputStream.write((byte) (j >>> 32));
        outputStream.write((byte) (j >>> 40));
        outputStream.write((byte) (j >>> 48));
        outputStream.write((byte) (j >>> 56));
    }

    static void zza(OutputStream outputStream, String str) throws IOException {
        byte[] bytes = str.getBytes("UTF-8");
        zza(outputStream, bytes.length);
        outputStream.write(bytes, 0, bytes.length);
    }

    private final void zza(String str, zzai zzaiVar) {
        if (this.zzav.containsKey(str)) {
            this.zzaw = (zzaiVar.zzaz - this.zzav.get(str).zzaz) + this.zzaw;
        } else {
            this.zzaw += zzaiVar.zzaz;
        }
        this.zzav.put(str, zzaiVar);
    }

    private static byte[] zza(InputStream inputStream, int i) throws IOException {
        byte[] bArr = new byte[i];
        int i2 = 0;
        while (i2 < i) {
            int i3 = inputStream.read(bArr, i2, i - i2);
            if (i3 == -1) {
                break;
            }
            i2 += i3;
        }
        if (i2 != i) {
            throw new IOException(new StringBuilder(50).append("Expected ").append(i).append(" bytes, read ").append(i2).append(" bytes").toString());
        }
        return bArr;
    }

    static int zzb(InputStream inputStream) throws IOException {
        return zza(inputStream) | 0 | (zza(inputStream) << 8) | (zza(inputStream) << 16) | (zza(inputStream) << 24);
    }

    static long zzc(InputStream inputStream) throws IOException {
        return 0 | (((long) zza(inputStream)) & 255) | ((((long) zza(inputStream)) & 255) << 8) | ((((long) zza(inputStream)) & 255) << 16) | ((((long) zza(inputStream)) & 255) << 24) | ((((long) zza(inputStream)) & 255) << 32) | ((((long) zza(inputStream)) & 255) << 40) | ((((long) zza(inputStream)) & 255) << 48) | ((((long) zza(inputStream)) & 255) << 56);
    }

    static String zzd(InputStream inputStream) throws IOException {
        return new String(zza(inputStream, (int) zzc(inputStream)), "UTF-8");
    }

    private static String zzd(String str) {
        int length = str.length() / 2;
        String strValueOf = String.valueOf(String.valueOf(str.substring(0, length).hashCode()));
        String strValueOf2 = String.valueOf(String.valueOf(str.substring(length).hashCode()));
        return strValueOf2.length() != 0 ? strValueOf.concat(strValueOf2) : new String(strValueOf);
    }

    private final File zze(String str) {
        return new File(this.zzax, zzd(str));
    }

    static Map<String, String> zze(InputStream inputStream) throws IOException {
        int iZzb = zzb(inputStream);
        Map<String, String> mapEmptyMap = iZzb == 0 ? Collections.emptyMap() : new HashMap<>(iZzb);
        for (int i = 0; i < iZzb; i++) {
            mapEmptyMap.put(zzd(inputStream).intern(), zzd(inputStream).intern());
        }
        return mapEmptyMap;
    }

    @Override // com.google.android.gms.internal.zzb
    public final synchronized void initialize() {
        BufferedInputStream bufferedInputStream;
        if (this.zzax.exists()) {
            File[] fileArrListFiles = this.zzax.listFiles();
            if (fileArrListFiles != null) {
                for (File file : fileArrListFiles) {
                    BufferedInputStream bufferedInputStream2 = null;
                    try {
                        bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
                        try {
                            try {
                                zzai zzaiVarZzf = zzai.zzf(bufferedInputStream);
                                zzaiVarZzf.zzaz = file.length();
                                zza(zzaiVarZzf.key, zzaiVarZzf);
                                try {
                                    bufferedInputStream.close();
                                } catch (IOException e) {
                                }
                            } catch (Throwable th) {
                                bufferedInputStream2 = bufferedInputStream;
                                th = th;
                                if (bufferedInputStream2 != null) {
                                    try {
                                        bufferedInputStream2.close();
                                    } catch (IOException e2) {
                                    }
                                }
                                throw th;
                            }
                        } catch (IOException e3) {
                            if (file != null) {
                                file.delete();
                            }
                            if (bufferedInputStream != null) {
                                try {
                                    bufferedInputStream.close();
                                } catch (IOException e4) {
                                }
                            }
                        }
                    } catch (IOException e5) {
                        bufferedInputStream = null;
                    } catch (Throwable th2) {
                        th = th2;
                    }
                }
            }
        } else if (!this.zzax.mkdirs()) {
            zzab.zzc("Unable to create cache dir %s", this.zzax.getAbsolutePath());
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:53:0x00ac A[EXC_TOP_SPLITTER, SYNTHETIC] */
    @Override // com.google.android.gms.internal.zzb
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final synchronized com.google.android.gms.internal.zzc zza(java.lang.String r11) {
        /*
            r10 = this;
            r1 = 0
            monitor-enter(r10)
            java.util.Map<java.lang.String, com.google.android.gms.internal.zzai> r0 = r10.zzav     // Catch: java.lang.Throwable -> Lb0
            java.lang.Object r0 = r0.get(r11)     // Catch: java.lang.Throwable -> Lb0
            com.google.android.gms.internal.zzai r0 = (com.google.android.gms.internal.zzai) r0     // Catch: java.lang.Throwable -> Lb0
            if (r0 != 0) goto Lf
            r0 = r1
        Ld:
            monitor-exit(r10)
            return r0
        Lf:
            java.io.File r4 = r10.zze(r11)     // Catch: java.lang.Throwable -> Lb0
            com.google.android.gms.internal.zzaj r3 = new com.google.android.gms.internal.zzaj     // Catch: java.io.IOException -> L5c java.lang.NegativeArraySizeException -> L81 java.lang.Throwable -> La8
            java.io.BufferedInputStream r2 = new java.io.BufferedInputStream     // Catch: java.io.IOException -> L5c java.lang.NegativeArraySizeException -> L81 java.lang.Throwable -> La8
            java.io.FileInputStream r5 = new java.io.FileInputStream     // Catch: java.io.IOException -> L5c java.lang.NegativeArraySizeException -> L81 java.lang.Throwable -> La8
            r5.<init>(r4)     // Catch: java.io.IOException -> L5c java.lang.NegativeArraySizeException -> L81 java.lang.Throwable -> La8
            r2.<init>(r5)     // Catch: java.io.IOException -> L5c java.lang.NegativeArraySizeException -> L81 java.lang.Throwable -> La8
            r5 = 0
            r3.<init>(r2)     // Catch: java.io.IOException -> L5c java.lang.NegativeArraySizeException -> L81 java.lang.Throwable -> La8
            com.google.android.gms.internal.zzai.zzf(r3)     // Catch: java.lang.Throwable -> Lb7 java.lang.NegativeArraySizeException -> Lbc java.io.IOException -> Lbe
            long r6 = r4.length()     // Catch: java.lang.Throwable -> Lb7 java.lang.NegativeArraySizeException -> Lbc java.io.IOException -> Lbe
            int r2 = com.google.android.gms.internal.zzaj.zza(r3)     // Catch: java.lang.Throwable -> Lb7 java.lang.NegativeArraySizeException -> Lbc java.io.IOException -> Lbe
            long r8 = (long) r2     // Catch: java.lang.Throwable -> Lb7 java.lang.NegativeArraySizeException -> Lbc java.io.IOException -> Lbe
            long r6 = r6 - r8
            int r2 = (int) r6     // Catch: java.lang.Throwable -> Lb7 java.lang.NegativeArraySizeException -> Lbc java.io.IOException -> Lbe
            byte[] r5 = zza(r3, r2)     // Catch: java.lang.Throwable -> Lb7 java.lang.NegativeArraySizeException -> Lbc java.io.IOException -> Lbe
            com.google.android.gms.internal.zzc r2 = new com.google.android.gms.internal.zzc     // Catch: java.lang.Throwable -> Lb7 java.lang.NegativeArraySizeException -> Lbc java.io.IOException -> Lbe
            r2.<init>()     // Catch: java.lang.Throwable -> Lb7 java.lang.NegativeArraySizeException -> Lbc java.io.IOException -> Lbe
            r2.data = r5     // Catch: java.lang.Throwable -> Lb7 java.lang.NegativeArraySizeException -> Lbc java.io.IOException -> Lbe
            java.lang.String r5 = r0.zza     // Catch: java.lang.Throwable -> Lb7 java.lang.NegativeArraySizeException -> Lbc java.io.IOException -> Lbe
            r2.zza = r5     // Catch: java.lang.Throwable -> Lb7 java.lang.NegativeArraySizeException -> Lbc java.io.IOException -> Lbe
            long r6 = r0.zzb     // Catch: java.lang.Throwable -> Lb7 java.lang.NegativeArraySizeException -> Lbc java.io.IOException -> Lbe
            r2.zzb = r6     // Catch: java.lang.Throwable -> Lb7 java.lang.NegativeArraySizeException -> Lbc java.io.IOException -> Lbe
            long r6 = r0.zzc     // Catch: java.lang.Throwable -> Lb7 java.lang.NegativeArraySizeException -> Lbc java.io.IOException -> Lbe
            r2.zzc = r6     // Catch: java.lang.Throwable -> Lb7 java.lang.NegativeArraySizeException -> Lbc java.io.IOException -> Lbe
            long r6 = r0.zzd     // Catch: java.lang.Throwable -> Lb7 java.lang.NegativeArraySizeException -> Lbc java.io.IOException -> Lbe
            r2.zzd = r6     // Catch: java.lang.Throwable -> Lb7 java.lang.NegativeArraySizeException -> Lbc java.io.IOException -> Lbe
            long r6 = r0.zze     // Catch: java.lang.Throwable -> Lb7 java.lang.NegativeArraySizeException -> Lbc java.io.IOException -> Lbe
            r2.zze = r6     // Catch: java.lang.Throwable -> Lb7 java.lang.NegativeArraySizeException -> Lbc java.io.IOException -> Lbe
            java.util.Map<java.lang.String, java.lang.String> r0 = r0.zzf     // Catch: java.lang.Throwable -> Lb7 java.lang.NegativeArraySizeException -> Lbc java.io.IOException -> Lbe
            r2.zzf = r0     // Catch: java.lang.Throwable -> Lb7 java.lang.NegativeArraySizeException -> Lbc java.io.IOException -> Lbe
            r3.close()     // Catch: java.io.IOException -> L59 java.lang.Throwable -> Lb0
            r0 = r2
            goto Ld
        L59:
            r0 = move-exception
            r0 = r1
            goto Ld
        L5c:
            r0 = move-exception
            r2 = r1
        L5e:
            java.lang.String r3 = "%s: %s"
            r5 = 2
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch: java.lang.Throwable -> Lb9
            r6 = 0
            java.lang.String r4 = r4.getAbsolutePath()     // Catch: java.lang.Throwable -> Lb9
            r5[r6] = r4     // Catch: java.lang.Throwable -> Lb9
            r4 = 1
            java.lang.String r0 = r0.toString()     // Catch: java.lang.Throwable -> Lb9
            r5[r4] = r0     // Catch: java.lang.Throwable -> Lb9
            com.google.android.gms.internal.zzab.zzb(r3, r5)     // Catch: java.lang.Throwable -> Lb9
            r10.remove(r11)     // Catch: java.lang.Throwable -> Lb9
            if (r2 == 0) goto L7c
            r2.close()     // Catch: java.io.IOException -> L7e java.lang.Throwable -> Lb0
        L7c:
            r0 = r1
            goto Ld
        L7e:
            r0 = move-exception
            r0 = r1
            goto Ld
        L81:
            r0 = move-exception
            r3 = r1
        L83:
            java.lang.String r2 = "%s: %s"
            r5 = 2
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch: java.lang.Throwable -> Lb7
            r6 = 0
            java.lang.String r4 = r4.getAbsolutePath()     // Catch: java.lang.Throwable -> Lb7
            r5[r6] = r4     // Catch: java.lang.Throwable -> Lb7
            r4 = 1
            java.lang.String r0 = r0.toString()     // Catch: java.lang.Throwable -> Lb7
            r5[r4] = r0     // Catch: java.lang.Throwable -> Lb7
            com.google.android.gms.internal.zzab.zzb(r2, r5)     // Catch: java.lang.Throwable -> Lb7
            r10.remove(r11)     // Catch: java.lang.Throwable -> Lb7
            if (r3 == 0) goto La1
            r3.close()     // Catch: java.io.IOException -> La4 java.lang.Throwable -> Lb0
        La1:
            r0 = r1
            goto Ld
        La4:
            r0 = move-exception
            r0 = r1
            goto Ld
        La8:
            r0 = move-exception
            r3 = r1
        Laa:
            if (r3 == 0) goto Laf
            r3.close()     // Catch: java.lang.Throwable -> Lb0 java.io.IOException -> Lb3
        Laf:
            throw r0     // Catch: java.lang.Throwable -> Lb0
        Lb0:
            r0 = move-exception
            monitor-exit(r10)
            throw r0
        Lb3:
            r0 = move-exception
            r0 = r1
            goto Ld
        Lb7:
            r0 = move-exception
            goto Laa
        Lb9:
            r0 = move-exception
            r3 = r2
            goto Laa
        Lbc:
            r0 = move-exception
            goto L83
        Lbe:
            r0 = move-exception
            r2 = r3
            goto L5e
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzag.zza(java.lang.String):com.google.android.gms.internal.zzc");
    }

    @Override // com.google.android.gms.internal.zzb
    public final synchronized void zza(String str, zzc zzcVar) {
        BufferedOutputStream bufferedOutputStream;
        zzai zzaiVar;
        int i;
        int i2 = 0;
        synchronized (this) {
            if (this.zzaw + ((long) zzcVar.data.length) >= this.zzay) {
                if (zzab.DEBUG) {
                    zzab.zza("Pruning old cache entries.", new Object[0]);
                }
                long j = this.zzaw;
                long jElapsedRealtime = SystemClock.elapsedRealtime();
                Iterator<Map.Entry<String, zzai>> it = this.zzav.entrySet().iterator();
                while (true) {
                    if (!it.hasNext()) {
                        i = i2;
                        break;
                    }
                    zzai value = it.next().getValue();
                    if (zze(value.key).delete()) {
                        this.zzaw -= value.zzaz;
                    } else {
                        zzab.zzb("Could not delete cache entry for key=%s, filename=%s", value.key, zzd(value.key));
                    }
                    it.remove();
                    i = i2 + 1;
                    if (this.zzaw + ((long) r2) < this.zzay * 0.9f) {
                        break;
                    } else {
                        i2 = i;
                    }
                }
                if (zzab.DEBUG) {
                    zzab.zza("pruned %d files, %d bytes, %d ms", Integer.valueOf(i), Long.valueOf(this.zzaw - j), Long.valueOf(SystemClock.elapsedRealtime() - jElapsedRealtime));
                }
            }
            File fileZze = zze(str);
            try {
                bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(fileZze));
                zzaiVar = new zzai(str, zzcVar);
            } catch (IOException e) {
                if (!fileZze.delete()) {
                    zzab.zzb("Could not clean up file %s", fileZze.getAbsolutePath());
                }
            }
            if (!zzaiVar.zza(bufferedOutputStream)) {
                bufferedOutputStream.close();
                zzab.zzb("Failed to write header for %s", fileZze.getAbsolutePath());
                throw new IOException();
            }
            bufferedOutputStream.write(zzcVar.data);
            bufferedOutputStream.close();
            zza(str, zzaiVar);
        }
    }
}
