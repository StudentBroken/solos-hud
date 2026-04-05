package com.google.android.gms.internal;

import com.kopin.solos.view.graphics.Bar;
import java.io.IOException;

/* JADX INFO: loaded from: classes35.dex */
public final class ajg extends aif {
    public String zzcxb = "";
    public String zzcxc = "";
    public long zzcxd = 0;
    public String zzcxe = "";
    public long zzcxf = 0;
    public long zzaLx = 0;
    public String zzcxg = "";
    public String zzcxh = "";
    public String zzcxi = "";
    public String zzcxj = "";
    public String zzcxk = "";
    public int zzcxl = 0;
    public ajf[] zzcxm = ajf.zzMx();

    public ajg() {
        this.zzcvf = -1;
    }

    public static ajg zzM(byte[] bArr) throws aie {
        return (ajg) aif.zza(new ajg(), bArr);
    }

    @Override // com.google.android.gms.internal.aif
    public final /* synthetic */ aif zza(ahw ahwVar) throws IOException {
        while (true) {
            int iZzLQ = ahwVar.zzLQ();
            switch (iZzLQ) {
                case 0:
                    break;
                case 10:
                    this.zzcxb = ahwVar.readString();
                    break;
                case 18:
                    this.zzcxc = ahwVar.readString();
                    break;
                case 24:
                    this.zzcxd = ahwVar.zzLR();
                    break;
                case 34:
                    this.zzcxe = ahwVar.readString();
                    break;
                case 40:
                    this.zzcxf = ahwVar.zzLR();
                    break;
                case Bar.DEFAULT_HEIGHT /* 48 */:
                    this.zzaLx = ahwVar.zzLR();
                    break;
                case 58:
                    this.zzcxg = ahwVar.readString();
                    break;
                case 66:
                    this.zzcxh = ahwVar.readString();
                    break;
                case 74:
                    this.zzcxi = ahwVar.readString();
                    break;
                case 82:
                    this.zzcxj = ahwVar.readString();
                    break;
                case 90:
                    this.zzcxk = ahwVar.readString();
                    break;
                case 96:
                    this.zzcxl = ahwVar.zzLS();
                    break;
                case 106:
                    int iZzb = aij.zzb(ahwVar, 106);
                    int length = this.zzcxm == null ? 0 : this.zzcxm.length;
                    ajf[] ajfVarArr = new ajf[iZzb + length];
                    if (length != 0) {
                        System.arraycopy(this.zzcxm, 0, ajfVarArr, 0, length);
                    }
                    while (length < ajfVarArr.length - 1) {
                        ajfVarArr[length] = new ajf();
                        ahwVar.zzb(ajfVarArr[length]);
                        ahwVar.zzLQ();
                        length++;
                    }
                    ajfVarArr[length] = new ajf();
                    ahwVar.zzb(ajfVarArr[length]);
                    this.zzcxm = ajfVarArr;
                    break;
                default:
                    if (!ahwVar.zzcl(iZzLQ)) {
                    }
                    break;
            }
        }
        return this;
    }

    @Override // com.google.android.gms.internal.aif
    public final void zza(ahx ahxVar) throws IOException {
        if (this.zzcxb != null && !this.zzcxb.equals("")) {
            ahxVar.zzl(1, this.zzcxb);
        }
        if (this.zzcxc != null && !this.zzcxc.equals("")) {
            ahxVar.zzl(2, this.zzcxc);
        }
        if (this.zzcxd != 0) {
            ahxVar.zzb(3, this.zzcxd);
        }
        if (this.zzcxe != null && !this.zzcxe.equals("")) {
            ahxVar.zzl(4, this.zzcxe);
        }
        if (this.zzcxf != 0) {
            ahxVar.zzb(5, this.zzcxf);
        }
        if (this.zzaLx != 0) {
            ahxVar.zzb(6, this.zzaLx);
        }
        if (this.zzcxg != null && !this.zzcxg.equals("")) {
            ahxVar.zzl(7, this.zzcxg);
        }
        if (this.zzcxh != null && !this.zzcxh.equals("")) {
            ahxVar.zzl(8, this.zzcxh);
        }
        if (this.zzcxi != null && !this.zzcxi.equals("")) {
            ahxVar.zzl(9, this.zzcxi);
        }
        if (this.zzcxj != null && !this.zzcxj.equals("")) {
            ahxVar.zzl(10, this.zzcxj);
        }
        if (this.zzcxk != null && !this.zzcxk.equals("")) {
            ahxVar.zzl(11, this.zzcxk);
        }
        if (this.zzcxl != 0) {
            ahxVar.zzr(12, this.zzcxl);
        }
        if (this.zzcxm != null && this.zzcxm.length > 0) {
            for (int i = 0; i < this.zzcxm.length; i++) {
                ajf ajfVar = this.zzcxm[i];
                if (ajfVar != null) {
                    ahxVar.zza(13, ajfVar);
                }
            }
        }
        super.zza(ahxVar);
    }

    @Override // com.google.android.gms.internal.aif
    protected final int zzn() {
        int iZzn = super.zzn();
        if (this.zzcxb != null && !this.zzcxb.equals("")) {
            iZzn += ahx.zzm(1, this.zzcxb);
        }
        if (this.zzcxc != null && !this.zzcxc.equals("")) {
            iZzn += ahx.zzm(2, this.zzcxc);
        }
        if (this.zzcxd != 0) {
            iZzn += ahx.zze(3, this.zzcxd);
        }
        if (this.zzcxe != null && !this.zzcxe.equals("")) {
            iZzn += ahx.zzm(4, this.zzcxe);
        }
        if (this.zzcxf != 0) {
            iZzn += ahx.zze(5, this.zzcxf);
        }
        if (this.zzaLx != 0) {
            iZzn += ahx.zze(6, this.zzaLx);
        }
        if (this.zzcxg != null && !this.zzcxg.equals("")) {
            iZzn += ahx.zzm(7, this.zzcxg);
        }
        if (this.zzcxh != null && !this.zzcxh.equals("")) {
            iZzn += ahx.zzm(8, this.zzcxh);
        }
        if (this.zzcxi != null && !this.zzcxi.equals("")) {
            iZzn += ahx.zzm(9, this.zzcxi);
        }
        if (this.zzcxj != null && !this.zzcxj.equals("")) {
            iZzn += ahx.zzm(10, this.zzcxj);
        }
        if (this.zzcxk != null && !this.zzcxk.equals("")) {
            iZzn += ahx.zzm(11, this.zzcxk);
        }
        if (this.zzcxl != 0) {
            iZzn += ahx.zzs(12, this.zzcxl);
        }
        if (this.zzcxm == null || this.zzcxm.length <= 0) {
            return iZzn;
        }
        int iZzb = iZzn;
        for (int i = 0; i < this.zzcxm.length; i++) {
            ajf ajfVar = this.zzcxm[i];
            if (ajfVar != null) {
                iZzb += ahx.zzb(13, ajfVar);
            }
        }
        return iZzb;
    }
}
