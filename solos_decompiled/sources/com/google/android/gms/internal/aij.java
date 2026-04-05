package com.google.android.gms.internal;

import java.io.IOException;

/* JADX INFO: loaded from: classes67.dex */
public final class aij {
    private static int zzcvi = 11;
    private static int zzcvj = 12;
    private static int zzcvk = 16;
    private static int zzcvl = 26;
    public static final int[] zzcvm = new int[0];
    public static final long[] zzcvn = new long[0];
    public static final float[] zzcvo = new float[0];
    private static double[] zzcvp = new double[0];
    public static final boolean[] zzcvq = new boolean[0];
    public static final String[] EMPTY_STRING_ARRAY = new String[0];
    public static final byte[][] zzcvr = new byte[0][];
    public static final byte[] zzcvs = new byte[0];

    public static final int zzb(ahw ahwVar, int i) throws IOException {
        int i2 = 1;
        int position = ahwVar.getPosition();
        ahwVar.zzcl(i);
        while (ahwVar.zzLQ() == i) {
            ahwVar.zzcl(i);
            i2++;
        }
        ahwVar.zzq(position, i);
        return i2;
    }
}
