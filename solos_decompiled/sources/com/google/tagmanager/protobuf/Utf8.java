package com.google.tagmanager.protobuf;

/* JADX INFO: loaded from: classes49.dex */
final class Utf8 {
    public static final int COMPLETE = 0;
    public static final int MALFORMED = -1;

    private Utf8() {
    }

    public static boolean isValidUtf8(byte[] bytes) {
        return isValidUtf8(bytes, 0, bytes.length);
    }

    public static boolean isValidUtf8(byte[] bytes, int index, int limit) {
        return partialIsValidUtf8(bytes, index, limit) == 0;
    }

    /* JADX WARN: Code restructure failed: missing block: B:29:0x0044, code lost:
    
        if (r10[r3] > (-65)) goto L30;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x007f, code lost:
    
        if (r10[r3] > (-65)) goto L48;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static int partialIsValidUtf8(int r9, byte[] r10, int r11, int r12) {
        /*
            r8 = -32
            r6 = -96
            r4 = -1
            r7 = -65
            if (r9 == 0) goto L84
            if (r11 < r12) goto Lc
        Lb:
            return r9
        Lc:
            byte r0 = (byte) r9
            if (r0 >= r8) goto L1c
            r5 = -62
            if (r0 < r5) goto L1a
            int r3 = r11 + 1
            r5 = r10[r11]
            if (r5 <= r7) goto L83
            r11 = r3
        L1a:
            r9 = r4
            goto Lb
        L1c:
            r5 = -16
            if (r0 >= r5) goto L48
            int r5 = r9 >> 8
            r5 = r5 ^ (-1)
            byte r1 = (byte) r5
            if (r1 != 0) goto L33
            int r3 = r11 + 1
            r1 = r10[r11]
            if (r3 < r12) goto L34
            int r9 = incompleteStateFor(r0, r1)
            r11 = r3
            goto Lb
        L33:
            r3 = r11
        L34:
            if (r1 > r7) goto L8b
            if (r0 != r8) goto L3a
            if (r1 < r6) goto L8b
        L3a:
            r5 = -19
            if (r0 != r5) goto L40
            if (r1 >= r6) goto L8b
        L40:
            int r11 = r3 + 1
            r5 = r10[r3]
            if (r5 <= r7) goto L84
        L46:
            r9 = r4
            goto Lb
        L48:
            int r5 = r9 >> 8
            r5 = r5 ^ (-1)
            byte r1 = (byte) r5
            r2 = 0
            if (r1 != 0) goto L5c
            int r3 = r11 + 1
            r1 = r10[r11]
            if (r3 < r12) goto L60
            int r9 = incompleteStateFor(r0, r1)
            r11 = r3
            goto Lb
        L5c:
            int r5 = r9 >> 16
            byte r2 = (byte) r5
            r3 = r11
        L60:
            if (r2 != 0) goto L6e
            int r11 = r3 + 1
            r2 = r10[r3]
            if (r11 < r12) goto L6d
            int r9 = incompleteStateFor(r0, r1, r2)
            goto Lb
        L6d:
            r3 = r11
        L6e:
            if (r1 > r7) goto L89
            int r5 = r0 << 28
            int r6 = r1 + 112
            int r5 = r5 + r6
            int r5 = r5 >> 30
            if (r5 != 0) goto L89
            if (r2 > r7) goto L89
            int r11 = r3 + 1
            r5 = r10[r3]
            if (r5 <= r7) goto L84
        L81:
            r9 = r4
            goto Lb
        L83:
            r11 = r3
        L84:
            int r9 = partialIsValidUtf8(r10, r11, r12)
            goto Lb
        L89:
            r11 = r3
            goto L81
        L8b:
            r11 = r3
            goto L46
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.tagmanager.protobuf.Utf8.partialIsValidUtf8(int, byte[], int, int):int");
    }

    public static int partialIsValidUtf8(byte[] bytes, int index, int limit) {
        while (index < limit && bytes[index] >= 0) {
            index++;
        }
        if (index >= limit) {
            return 0;
        }
        return partialIsValidUtf8NonAscii(bytes, index, limit);
    }

    private static int partialIsValidUtf8NonAscii(byte[] bytes, int index, int limit) {
        int index2;
        int index3;
        int index4 = index;
        while (index4 < limit) {
            int index5 = index4 + 1;
            int byte1 = bytes[index4];
            if (byte1 < 0) {
                if (byte1 < -32) {
                    if (index5 < limit) {
                        if (byte1 >= -62) {
                            index2 = index5 + 1;
                            if (bytes[index5] > -65) {
                            }
                        }
                        return -1;
                    }
                    return byte1;
                }
                if (byte1 < -16) {
                    if (index5 >= limit - 1) {
                        return incompleteStateFor(bytes, index5, limit);
                    }
                    int index6 = index5 + 1;
                    int byte2 = bytes[index5];
                    if (byte2 > -65 || ((byte1 == -32 && byte2 < -96) || (byte1 == -19 && byte2 >= -96))) {
                        return -1;
                    }
                    index3 = index6 + 1;
                    if (bytes[index6] > -65) {
                        return -1;
                    }
                    index4 = index3;
                } else {
                    if (index5 >= limit - 2) {
                        return incompleteStateFor(bytes, index5, limit);
                    }
                    index2 = index5 + 1;
                    int byte22 = bytes[index5];
                    if (byte22 <= -65 && (((byte1 << 28) + (byte22 + 112)) >> 30) == 0) {
                        int index7 = index2 + 1;
                        if (bytes[index2] > -65) {
                            return -1;
                        }
                        index2 = index7 + 1;
                        if (bytes[index7] > -65) {
                        }
                    }
                    return -1;
                }
                index3 = index2;
                index4 = index3;
            } else {
                index4 = index5;
            }
        }
        return 0;
    }

    private static int incompleteStateFor(int byte1) {
        if (byte1 > -12) {
            return -1;
        }
        return byte1;
    }

    private static int incompleteStateFor(int byte1, int byte2) {
        if (byte1 > -12 || byte2 > -65) {
            return -1;
        }
        return (byte2 << 8) ^ byte1;
    }

    private static int incompleteStateFor(int byte1, int byte2, int byte3) {
        if (byte1 > -12 || byte2 > -65 || byte3 > -65) {
            return -1;
        }
        return ((byte2 << 8) ^ byte1) ^ (byte3 << 16);
    }

    private static int incompleteStateFor(byte[] bytes, int index, int limit) {
        int byte1 = bytes[index - 1];
        switch (limit - index) {
            case 0:
                return incompleteStateFor(byte1);
            case 1:
                return incompleteStateFor(byte1, bytes[index]);
            case 2:
                return incompleteStateFor(byte1, bytes[index], bytes[index + 1]);
            default:
                throw new AssertionError();
        }
    }
}
