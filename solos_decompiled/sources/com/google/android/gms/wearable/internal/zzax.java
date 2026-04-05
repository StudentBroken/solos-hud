package com.google.android.gms.wearable.internal;

import android.util.Log;
import com.google.android.gms.wearable.ChannelIOException;
import java.io.IOException;
import java.io.OutputStream;

/* JADX INFO: loaded from: classes6.dex */
public final class zzax extends OutputStream {
    private volatile zzah zzbSt;
    private final OutputStream zzbSv;

    public zzax(OutputStream outputStream) {
        this.zzbSv = (OutputStream) com.google.android.gms.common.internal.zzbr.zzu(outputStream);
    }

    private final IOException zza(IOException iOException) {
        zzah zzahVar = this.zzbSt;
        if (zzahVar == null) {
            return iOException;
        }
        if (Log.isLoggable("ChannelOutputStream", 2)) {
            Log.v("ChannelOutputStream", "Caught IOException, but channel has been closed. Translating to ChannelIOException.", iOException);
        }
        return new ChannelIOException("Channel closed unexpectedly before stream was finished", zzahVar.zzbSj, zzahVar.zzbSk);
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public final void close() throws IOException {
        try {
            this.zzbSv.close();
        } catch (IOException e) {
            throw zza(e);
        }
    }

    @Override // java.io.OutputStream, java.io.Flushable
    public final void flush() throws IOException {
        try {
            this.zzbSv.flush();
        } catch (IOException e) {
            throw zza(e);
        }
    }

    @Override // java.io.OutputStream
    public final void write(int i) throws IOException {
        try {
            this.zzbSv.write(i);
        } catch (IOException e) {
            throw zza(e);
        }
    }

    @Override // java.io.OutputStream
    public final void write(byte[] bArr) throws IOException {
        try {
            this.zzbSv.write(bArr);
        } catch (IOException e) {
            throw zza(e);
        }
    }

    @Override // java.io.OutputStream
    public final void write(byte[] bArr, int i, int i2) throws IOException {
        try {
            this.zzbSv.write(bArr, i, i2);
        } catch (IOException e) {
            throw zza(e);
        }
    }

    final void zzc(zzah zzahVar) {
        this.zzbSt = zzahVar;
    }
}
