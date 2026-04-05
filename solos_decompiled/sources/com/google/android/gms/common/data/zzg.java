package com.google.android.gms.common.data;

import java.util.ArrayList;

/* JADX INFO: loaded from: classes3.dex */
public abstract class zzg<T> extends AbstractDataBuffer<T> {
    private boolean zzaFQ;
    private ArrayList<Integer> zzaFR;

    protected zzg(DataHolder dataHolder) {
        super(dataHolder);
        this.zzaFQ = false;
    }

    private final int zzaw(int i) {
        if (i < 0 || i >= this.zzaFR.size()) {
            throw new IllegalArgumentException(new StringBuilder(53).append("Position ").append(i).append(" is out of bounds for this buffer").toString());
        }
        return this.zzaFR.get(i).intValue();
    }

    private final void zzqR() {
        synchronized (this) {
            if (!this.zzaFQ) {
                int i = this.zzaCZ.zzaFI;
                this.zzaFR = new ArrayList<>();
                if (i > 0) {
                    this.zzaFR.add(0);
                    String strZzqQ = zzqQ();
                    String strZzd = this.zzaCZ.zzd(strZzqQ, 0, this.zzaCZ.zzat(0));
                    int i2 = 1;
                    while (i2 < i) {
                        int iZzat = this.zzaCZ.zzat(i2);
                        String strZzd2 = this.zzaCZ.zzd(strZzqQ, i2, iZzat);
                        if (strZzd2 == null) {
                            throw new NullPointerException(new StringBuilder(String.valueOf(strZzqQ).length() + 78).append("Missing value for markerColumn: ").append(strZzqQ).append(", at row: ").append(i2).append(", for window: ").append(iZzat).toString());
                        }
                        if (strZzd2.equals(strZzd)) {
                            strZzd2 = strZzd;
                        } else {
                            this.zzaFR.add(Integer.valueOf(i2));
                        }
                        i2++;
                        strZzd = strZzd2;
                    }
                }
                this.zzaFQ = true;
            }
        }
    }

    @Override // com.google.android.gms.common.data.AbstractDataBuffer, com.google.android.gms.common.data.DataBuffer
    public final T get(int i) {
        int iIntValue;
        zzqR();
        int iZzaw = zzaw(i);
        if (i < 0 || i == this.zzaFR.size()) {
            iIntValue = 0;
        } else {
            iIntValue = i == this.zzaFR.size() + (-1) ? this.zzaCZ.zzaFI - this.zzaFR.get(i).intValue() : this.zzaFR.get(i + 1).intValue() - this.zzaFR.get(i).intValue();
            if (iIntValue == 1) {
                this.zzaCZ.zzat(zzaw(i));
            }
        }
        return zzi(iZzaw, iIntValue);
    }

    @Override // com.google.android.gms.common.data.AbstractDataBuffer, com.google.android.gms.common.data.DataBuffer
    public int getCount() {
        zzqR();
        return this.zzaFR.size();
    }

    protected abstract T zzi(int i, int i2);

    protected abstract String zzqQ();
}
