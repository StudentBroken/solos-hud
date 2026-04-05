package com.kopin.core;

import android.graphics.Bitmap;

/* JADX INFO: loaded from: classes5.dex */
public class Core {
    static final int FLAG_DECODE = 1;
    static final int FLAG_ENCODE = 0;

    public static native byte[] Downsample16000(byte[] bArr, int i, int i2, int i3, short s, byte b);

    public static native int convertToRGB111(Bitmap bitmap, int i, int i2, int i3, int i4, byte[] bArr, boolean z);

    public static native int convertToRGB565(Bitmap bitmap, int i, int i2, int i3, int i4, byte[] bArr, boolean z);

    public static native int convertToRLE(Bitmap bitmap, int i, int i2, int i3, int i4, byte[] bArr, boolean z);

    static native int decode(long j, byte[] bArr, int i, int i2, byte[] bArr2, int i3, int i4, int[] iArr);

    static native void destroySBC(long j);

    static native int encode(long j, byte[] bArr, int i, int i2, byte[] bArr2, int i3, int i4, int[] iArr);

    static native int getCodeSize(long j);

    static native long initialiseMSBC(int i);

    static native long initialiseSBC(int i);

    static native void setEndianess(long j, byte b);

    static native void setSBCValues(long j, int i, byte b, byte b2, byte b3, byte b4, byte b5);

    static {
        try {
            System.loadLibrary("native_library");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int convertToRLE(Bitmap bitmap, int x, int y, int width, int height, byte[] data) {
        return convertToRLE(bitmap, x, y, width, height, data, true);
    }
}
