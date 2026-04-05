package com.google.android.gms.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes67.dex */
final class aic implements Cloneable {
    private Object value;
    private aia<?, ?> zzcvc;
    private List<aii> zzcvd = new ArrayList();

    aic() {
    }

    private final byte[] toByteArray() throws IOException {
        byte[] bArr = new byte[zzn()];
        zza(ahx.zzJ(bArr));
        return bArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: zzMf, reason: merged with bridge method [inline-methods] */
    public aic clone() {
        int i = 0;
        aic aicVar = new aic();
        try {
            aicVar.zzcvc = this.zzcvc;
            if (this.zzcvd == null) {
                aicVar.zzcvd = null;
            } else {
                aicVar.zzcvd.addAll(this.zzcvd);
            }
            if (this.value != null) {
                if (this.value instanceof aif) {
                    aicVar.value = (aif) ((aif) this.value).clone();
                } else if (this.value instanceof byte[]) {
                    aicVar.value = ((byte[]) this.value).clone();
                } else if (this.value instanceof byte[][]) {
                    byte[][] bArr = (byte[][]) this.value;
                    byte[][] bArr2 = new byte[bArr.length][];
                    aicVar.value = bArr2;
                    for (int i2 = 0; i2 < bArr.length; i2++) {
                        bArr2[i2] = (byte[]) bArr[i2].clone();
                    }
                } else if (this.value instanceof boolean[]) {
                    aicVar.value = ((boolean[]) this.value).clone();
                } else if (this.value instanceof int[]) {
                    aicVar.value = ((int[]) this.value).clone();
                } else if (this.value instanceof long[]) {
                    aicVar.value = ((long[]) this.value).clone();
                } else if (this.value instanceof float[]) {
                    aicVar.value = ((float[]) this.value).clone();
                } else if (this.value instanceof double[]) {
                    aicVar.value = ((double[]) this.value).clone();
                } else if (this.value instanceof aif[]) {
                    aif[] aifVarArr = (aif[]) this.value;
                    aif[] aifVarArr2 = new aif[aifVarArr.length];
                    aicVar.value = aifVarArr2;
                    while (true) {
                        int i3 = i;
                        if (i3 >= aifVarArr.length) {
                            break;
                        }
                        aifVarArr2[i3] = (aif) aifVarArr[i3].clone();
                        i = i3 + 1;
                    }
                }
            }
            return aicVar;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof aic)) {
            return false;
        }
        aic aicVar = (aic) obj;
        if (this.value != null && aicVar.value != null) {
            if (this.zzcvc == aicVar.zzcvc) {
                return !this.zzcvc.zzcmA.isArray() ? this.value.equals(aicVar.value) : this.value instanceof byte[] ? Arrays.equals((byte[]) this.value, (byte[]) aicVar.value) : this.value instanceof int[] ? Arrays.equals((int[]) this.value, (int[]) aicVar.value) : this.value instanceof long[] ? Arrays.equals((long[]) this.value, (long[]) aicVar.value) : this.value instanceof float[] ? Arrays.equals((float[]) this.value, (float[]) aicVar.value) : this.value instanceof double[] ? Arrays.equals((double[]) this.value, (double[]) aicVar.value) : this.value instanceof boolean[] ? Arrays.equals((boolean[]) this.value, (boolean[]) aicVar.value) : Arrays.deepEquals((Object[]) this.value, (Object[]) aicVar.value);
            }
            return false;
        }
        if (this.zzcvd != null && aicVar.zzcvd != null) {
            return this.zzcvd.equals(aicVar.zzcvd);
        }
        try {
            return Arrays.equals(toByteArray(), aicVar.toByteArray());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public final int hashCode() {
        try {
            return Arrays.hashCode(toByteArray()) + 527;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    final void zza(ahx ahxVar) throws IOException {
        if (this.value != null) {
            this.zzcvc.zza(this.value, ahxVar);
            return;
        }
        for (aii aiiVar : this.zzcvd) {
            ahxVar.zzct(aiiVar.tag);
            ahxVar.zzL(aiiVar.zzbww);
        }
    }

    final void zza(aii aiiVar) {
        this.zzcvd.add(aiiVar);
    }

    /* JADX WARN: Multi-variable type inference failed */
    final <T> T zzb(aia<?, T> aiaVar) {
        if (this.value == null) {
            this.zzcvc = aiaVar;
            this.value = aiaVar.zzY(this.zzcvd);
            this.zzcvd = null;
        } else if (!this.zzcvc.equals(aiaVar)) {
            throw new IllegalStateException("Tried to getExtension with a different Extension.");
        }
        return (T) this.value;
    }

    final int zzn() {
        int length = 0;
        if (this.value != null) {
            return this.zzcvc.zzav(this.value);
        }
        Iterator<aii> it = this.zzcvd.iterator();
        while (true) {
            int i = length;
            if (!it.hasNext()) {
                return i;
            }
            aii next = it.next();
            length = next.zzbww.length + ahx.zzcu(next.tag) + 0 + i;
        }
    }
}
