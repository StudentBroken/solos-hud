package com.kopin.accessory.utility;

import android.support.v4.view.MotionEventCompat;

/* JADX INFO: loaded from: classes14.dex */
public final class DecodeRLE {
    public static short[] decodePacket16(byte[] source, int width, int height) {
        if (source.length % 3 != 0) {
            throw new IllegalArgumentException("Length of source must be divisible by 3.");
        }
        short[] result = new short[width * height];
        int offset = 0;
        int end = source.length;
        for (int start = 0; start < end; start += 3) {
            short count = (short) (source[start] & 255);
            byte hi = source[start + 1];
            byte lo = source[start + 2];
            for (short i = 0; i < count; i = (short) (i + 1)) {
                result[offset + i] = (short) (((lo << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK) | (hi & 255));
            }
            offset += count;
        }
        return result;
    }
}
