package com.google.android.gms.common.data;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.util.Log;
import com.digits.sdk.vcard.VCardConfig;
import com.google.android.gms.common.internal.ReflectedParcelable;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes3.dex */
public class BitmapTeleporter extends com.google.android.gms.common.internal.safeparcel.zza implements ReflectedParcelable {
    public static final Parcelable.Creator<BitmapTeleporter> CREATOR = new zza();
    private ParcelFileDescriptor zzTR;
    private Bitmap zzaFt;
    private boolean zzaFu;
    private File zzaFv;
    private int zzakw;
    private int zzamt;

    BitmapTeleporter(int i, ParcelFileDescriptor parcelFileDescriptor, int i2) {
        this.zzakw = i;
        this.zzTR = parcelFileDescriptor;
        this.zzamt = i2;
        this.zzaFt = null;
        this.zzaFu = false;
    }

    public BitmapTeleporter(Bitmap bitmap) {
        this.zzakw = 1;
        this.zzTR = null;
        this.zzamt = 0;
        this.zzaFt = bitmap;
        this.zzaFu = true;
    }

    private static void zza(Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException e) {
            Log.w("BitmapTeleporter", "Could not close stream", e);
        }
    }

    private final FileOutputStream zzqN() {
        if (this.zzaFv == null) {
            throw new IllegalStateException("setTempDir() must be called before writing this object to a parcel");
        }
        try {
            File fileCreateTempFile = File.createTempFile("teleporter", ".tmp", this.zzaFv);
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(fileCreateTempFile);
                this.zzTR = ParcelFileDescriptor.open(fileCreateTempFile, VCardConfig.FLAG_REFRAIN_QP_TO_NAME_PROPERTIES);
                fileCreateTempFile.delete();
                return fileOutputStream;
            } catch (FileNotFoundException e) {
                throw new IllegalStateException("Temporary file is somehow already deleted");
            }
        } catch (IOException e2) {
            throw new IllegalStateException("Could not create temporary file", e2);
        }
    }

    public final void release() {
        if (this.zzaFu) {
            return;
        }
        try {
            this.zzTR.close();
        } catch (IOException e) {
            Log.w("BitmapTeleporter", "Could not close PFD", e);
        }
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        if (this.zzTR == null) {
            Bitmap bitmap = this.zzaFt;
            ByteBuffer byteBufferAllocate = ByteBuffer.allocate(bitmap.getRowBytes() * bitmap.getHeight());
            bitmap.copyPixelsToBuffer(byteBufferAllocate);
            byte[] bArrArray = byteBufferAllocate.array();
            DataOutputStream dataOutputStream = new DataOutputStream(zzqN());
            try {
                try {
                    dataOutputStream.writeInt(bArrArray.length);
                    dataOutputStream.writeInt(bitmap.getWidth());
                    dataOutputStream.writeInt(bitmap.getHeight());
                    dataOutputStream.writeUTF(bitmap.getConfig().toString());
                    dataOutputStream.write(bArrArray);
                } catch (IOException e) {
                    throw new IllegalStateException("Could not write into unlinked file", e);
                }
            } finally {
                zza(dataOutputStream);
            }
        }
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 1, this.zzakw);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 2, (Parcelable) this.zzTR, i | 1, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 3, this.zzamt);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
        this.zzTR = null;
    }

    public final void zzc(File file) {
        if (file == null) {
            throw new NullPointerException("Cannot set null temp directory");
        }
        this.zzaFv = file;
    }

    public final Bitmap zzqM() {
        if (!this.zzaFu) {
            DataInputStream dataInputStream = new DataInputStream(new ParcelFileDescriptor.AutoCloseInputStream(this.zzTR));
            try {
                try {
                    byte[] bArr = new byte[dataInputStream.readInt()];
                    int i = dataInputStream.readInt();
                    int i2 = dataInputStream.readInt();
                    Bitmap.Config configValueOf = Bitmap.Config.valueOf(dataInputStream.readUTF());
                    dataInputStream.read(bArr);
                    zza(dataInputStream);
                    ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
                    Bitmap bitmapCreateBitmap = Bitmap.createBitmap(i, i2, configValueOf);
                    bitmapCreateBitmap.copyPixelsFromBuffer(byteBufferWrap);
                    this.zzaFt = bitmapCreateBitmap;
                    this.zzaFu = true;
                } catch (IOException e) {
                    throw new IllegalStateException("Could not read from parcel file descriptor", e);
                }
            } catch (Throwable th) {
                zza(dataInputStream);
                throw th;
            }
        }
        return this.zzaFt;
    }
}
