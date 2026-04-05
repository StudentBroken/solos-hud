package com.google.android.gms.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/* JADX INFO: loaded from: classes67.dex */
public final class zzae {
    private static Comparator<byte[]> zzau = new zzaf();
    private List<byte[]> zzaq = new LinkedList();
    private List<byte[]> zzar = new ArrayList(64);
    private int zzas = 0;
    private final int zzat;

    public zzae(int i) {
        this.zzat = i;
    }

    private final synchronized void zzm() {
        while (this.zzas > this.zzat) {
            byte[] bArrRemove = this.zzaq.remove(0);
            this.zzar.remove(bArrRemove);
            this.zzas -= bArrRemove.length;
        }
    }

    public final synchronized void zza(byte[] bArr) {
        if (bArr != null) {
            if (bArr.length <= this.zzat) {
                this.zzaq.add(bArr);
                int iBinarySearch = Collections.binarySearch(this.zzar, bArr, zzau);
                if (iBinarySearch < 0) {
                    iBinarySearch = (-iBinarySearch) - 1;
                }
                this.zzar.add(iBinarySearch, bArr);
                this.zzas += bArr.length;
                zzm();
            }
        }
    }

    public final synchronized byte[] zzb(int i) {
        byte[] bArr;
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i3 >= this.zzar.size()) {
                bArr = new byte[i];
                break;
            }
            bArr = this.zzar.get(i3);
            if (bArr.length >= i) {
                this.zzas -= bArr.length;
                this.zzar.remove(i3);
                this.zzaq.remove(bArr);
                break;
            }
            i2 = i3 + 1;
        }
        return bArr;
    }
}
