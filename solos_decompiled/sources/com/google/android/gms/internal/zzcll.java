package com.google.android.gms.internal;

import com.kopin.solos.AppService;
import com.kopin.solos.view.graphics.Bar;
import com.twitter.sdk.android.core.internal.TwitterApiConstants;
import java.io.IOException;

/* JADX INFO: loaded from: classes36.dex */
public final class zzcll extends ahz<zzcll> {
    private static volatile zzcll[] zzbvG;
    public Integer zzbvH = null;
    public zzcli[] zzbvI = zzcli.zzzz();
    public zzcln[] zzbvJ = zzcln.zzzC();
    public Long zzbvK = null;
    public Long zzbvL = null;
    public Long zzbvM = null;
    public Long zzbvN = null;
    public Long zzbvO = null;
    public String zzbvP = null;
    public String zzbb = null;
    public String zzbvQ = null;
    public String zzbvR = null;
    public Integer zzbvS = null;
    public String zzboV = null;
    public String zzaK = null;
    public String zzbha = null;
    public Long zzbvT = null;
    public Long zzbvU = null;
    public String zzbvV = null;
    public Boolean zzbvW = null;
    public String zzbvX = null;
    public Long zzbvY = null;
    public Integer zzbvZ = null;
    public String zzboY = null;
    public String zzboU = null;
    public Boolean zzbwa = null;
    public zzclh[] zzbwb = zzclh.zzzy();
    public String zzbpc = null;
    public Integer zzbwc = null;
    private Integer zzbwd = null;
    private Integer zzbwe = null;
    public Long zzbwf = null;
    public Long zzbwg = null;
    public String zzbwh = null;

    public zzcll() {
        this.zzcuW = null;
        this.zzcvf = -1;
    }

    public static zzcll[] zzzB() {
        if (zzbvG == null) {
            synchronized (aid.zzcve) {
                if (zzbvG == null) {
                    zzbvG = new zzcll[0];
                }
            }
        }
        return zzbvG;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzcll)) {
            return false;
        }
        zzcll zzcllVar = (zzcll) obj;
        if (this.zzbvH == null) {
            if (zzcllVar.zzbvH != null) {
                return false;
            }
        } else if (!this.zzbvH.equals(zzcllVar.zzbvH)) {
            return false;
        }
        if (aid.equals(this.zzbvI, zzcllVar.zzbvI) && aid.equals(this.zzbvJ, zzcllVar.zzbvJ)) {
            if (this.zzbvK == null) {
                if (zzcllVar.zzbvK != null) {
                    return false;
                }
            } else if (!this.zzbvK.equals(zzcllVar.zzbvK)) {
                return false;
            }
            if (this.zzbvL == null) {
                if (zzcllVar.zzbvL != null) {
                    return false;
                }
            } else if (!this.zzbvL.equals(zzcllVar.zzbvL)) {
                return false;
            }
            if (this.zzbvM == null) {
                if (zzcllVar.zzbvM != null) {
                    return false;
                }
            } else if (!this.zzbvM.equals(zzcllVar.zzbvM)) {
                return false;
            }
            if (this.zzbvN == null) {
                if (zzcllVar.zzbvN != null) {
                    return false;
                }
            } else if (!this.zzbvN.equals(zzcllVar.zzbvN)) {
                return false;
            }
            if (this.zzbvO == null) {
                if (zzcllVar.zzbvO != null) {
                    return false;
                }
            } else if (!this.zzbvO.equals(zzcllVar.zzbvO)) {
                return false;
            }
            if (this.zzbvP == null) {
                if (zzcllVar.zzbvP != null) {
                    return false;
                }
            } else if (!this.zzbvP.equals(zzcllVar.zzbvP)) {
                return false;
            }
            if (this.zzbb == null) {
                if (zzcllVar.zzbb != null) {
                    return false;
                }
            } else if (!this.zzbb.equals(zzcllVar.zzbb)) {
                return false;
            }
            if (this.zzbvQ == null) {
                if (zzcllVar.zzbvQ != null) {
                    return false;
                }
            } else if (!this.zzbvQ.equals(zzcllVar.zzbvQ)) {
                return false;
            }
            if (this.zzbvR == null) {
                if (zzcllVar.zzbvR != null) {
                    return false;
                }
            } else if (!this.zzbvR.equals(zzcllVar.zzbvR)) {
                return false;
            }
            if (this.zzbvS == null) {
                if (zzcllVar.zzbvS != null) {
                    return false;
                }
            } else if (!this.zzbvS.equals(zzcllVar.zzbvS)) {
                return false;
            }
            if (this.zzboV == null) {
                if (zzcllVar.zzboV != null) {
                    return false;
                }
            } else if (!this.zzboV.equals(zzcllVar.zzboV)) {
                return false;
            }
            if (this.zzaK == null) {
                if (zzcllVar.zzaK != null) {
                    return false;
                }
            } else if (!this.zzaK.equals(zzcllVar.zzaK)) {
                return false;
            }
            if (this.zzbha == null) {
                if (zzcllVar.zzbha != null) {
                    return false;
                }
            } else if (!this.zzbha.equals(zzcllVar.zzbha)) {
                return false;
            }
            if (this.zzbvT == null) {
                if (zzcllVar.zzbvT != null) {
                    return false;
                }
            } else if (!this.zzbvT.equals(zzcllVar.zzbvT)) {
                return false;
            }
            if (this.zzbvU == null) {
                if (zzcllVar.zzbvU != null) {
                    return false;
                }
            } else if (!this.zzbvU.equals(zzcllVar.zzbvU)) {
                return false;
            }
            if (this.zzbvV == null) {
                if (zzcllVar.zzbvV != null) {
                    return false;
                }
            } else if (!this.zzbvV.equals(zzcllVar.zzbvV)) {
                return false;
            }
            if (this.zzbvW == null) {
                if (zzcllVar.zzbvW != null) {
                    return false;
                }
            } else if (!this.zzbvW.equals(zzcllVar.zzbvW)) {
                return false;
            }
            if (this.zzbvX == null) {
                if (zzcllVar.zzbvX != null) {
                    return false;
                }
            } else if (!this.zzbvX.equals(zzcllVar.zzbvX)) {
                return false;
            }
            if (this.zzbvY == null) {
                if (zzcllVar.zzbvY != null) {
                    return false;
                }
            } else if (!this.zzbvY.equals(zzcllVar.zzbvY)) {
                return false;
            }
            if (this.zzbvZ == null) {
                if (zzcllVar.zzbvZ != null) {
                    return false;
                }
            } else if (!this.zzbvZ.equals(zzcllVar.zzbvZ)) {
                return false;
            }
            if (this.zzboY == null) {
                if (zzcllVar.zzboY != null) {
                    return false;
                }
            } else if (!this.zzboY.equals(zzcllVar.zzboY)) {
                return false;
            }
            if (this.zzboU == null) {
                if (zzcllVar.zzboU != null) {
                    return false;
                }
            } else if (!this.zzboU.equals(zzcllVar.zzboU)) {
                return false;
            }
            if (this.zzbwa == null) {
                if (zzcllVar.zzbwa != null) {
                    return false;
                }
            } else if (!this.zzbwa.equals(zzcllVar.zzbwa)) {
                return false;
            }
            if (!aid.equals(this.zzbwb, zzcllVar.zzbwb)) {
                return false;
            }
            if (this.zzbpc == null) {
                if (zzcllVar.zzbpc != null) {
                    return false;
                }
            } else if (!this.zzbpc.equals(zzcllVar.zzbpc)) {
                return false;
            }
            if (this.zzbwc == null) {
                if (zzcllVar.zzbwc != null) {
                    return false;
                }
            } else if (!this.zzbwc.equals(zzcllVar.zzbwc)) {
                return false;
            }
            if (this.zzbwd == null) {
                if (zzcllVar.zzbwd != null) {
                    return false;
                }
            } else if (!this.zzbwd.equals(zzcllVar.zzbwd)) {
                return false;
            }
            if (this.zzbwe == null) {
                if (zzcllVar.zzbwe != null) {
                    return false;
                }
            } else if (!this.zzbwe.equals(zzcllVar.zzbwe)) {
                return false;
            }
            if (this.zzbwf == null) {
                if (zzcllVar.zzbwf != null) {
                    return false;
                }
            } else if (!this.zzbwf.equals(zzcllVar.zzbwf)) {
                return false;
            }
            if (this.zzbwg == null) {
                if (zzcllVar.zzbwg != null) {
                    return false;
                }
            } else if (!this.zzbwg.equals(zzcllVar.zzbwg)) {
                return false;
            }
            if (this.zzbwh == null) {
                if (zzcllVar.zzbwh != null) {
                    return false;
                }
            } else if (!this.zzbwh.equals(zzcllVar.zzbwh)) {
                return false;
            }
            return (this.zzcuW == null || this.zzcuW.isEmpty()) ? zzcllVar.zzcuW == null || zzcllVar.zzcuW.isEmpty() : this.zzcuW.equals(zzcllVar.zzcuW);
        }
        return false;
    }

    public final int hashCode() {
        int iHashCode = 0;
        int iHashCode2 = ((this.zzbwh == null ? 0 : this.zzbwh.hashCode()) + (((this.zzbwg == null ? 0 : this.zzbwg.hashCode()) + (((this.zzbwf == null ? 0 : this.zzbwf.hashCode()) + (((this.zzbwe == null ? 0 : this.zzbwe.hashCode()) + (((this.zzbwd == null ? 0 : this.zzbwd.hashCode()) + (((this.zzbwc == null ? 0 : this.zzbwc.hashCode()) + (((this.zzbpc == null ? 0 : this.zzbpc.hashCode()) + (((((this.zzbwa == null ? 0 : this.zzbwa.hashCode()) + (((this.zzboU == null ? 0 : this.zzboU.hashCode()) + (((this.zzboY == null ? 0 : this.zzboY.hashCode()) + (((this.zzbvZ == null ? 0 : this.zzbvZ.hashCode()) + (((this.zzbvY == null ? 0 : this.zzbvY.hashCode()) + (((this.zzbvX == null ? 0 : this.zzbvX.hashCode()) + (((this.zzbvW == null ? 0 : this.zzbvW.hashCode()) + (((this.zzbvV == null ? 0 : this.zzbvV.hashCode()) + (((this.zzbvU == null ? 0 : this.zzbvU.hashCode()) + (((this.zzbvT == null ? 0 : this.zzbvT.hashCode()) + (((this.zzbha == null ? 0 : this.zzbha.hashCode()) + (((this.zzaK == null ? 0 : this.zzaK.hashCode()) + (((this.zzboV == null ? 0 : this.zzboV.hashCode()) + (((this.zzbvS == null ? 0 : this.zzbvS.hashCode()) + (((this.zzbvR == null ? 0 : this.zzbvR.hashCode()) + (((this.zzbvQ == null ? 0 : this.zzbvQ.hashCode()) + (((this.zzbb == null ? 0 : this.zzbb.hashCode()) + (((this.zzbvP == null ? 0 : this.zzbvP.hashCode()) + (((this.zzbvO == null ? 0 : this.zzbvO.hashCode()) + (((this.zzbvN == null ? 0 : this.zzbvN.hashCode()) + (((this.zzbvM == null ? 0 : this.zzbvM.hashCode()) + (((this.zzbvL == null ? 0 : this.zzbvL.hashCode()) + (((this.zzbvK == null ? 0 : this.zzbvK.hashCode()) + (((((((this.zzbvH == null ? 0 : this.zzbvH.hashCode()) + ((getClass().getName().hashCode() + 527) * 31)) * 31) + aid.hashCode(this.zzbvI)) * 31) + aid.hashCode(this.zzbvJ)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31) + aid.hashCode(this.zzbwb)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31;
        if (this.zzcuW != null && !this.zzcuW.isEmpty()) {
            iHashCode = this.zzcuW.hashCode();
        }
        return iHashCode2 + iHashCode;
    }

    @Override // com.google.android.gms.internal.aif
    public final /* synthetic */ aif zza(ahw ahwVar) throws IOException {
        while (true) {
            int iZzLQ = ahwVar.zzLQ();
            switch (iZzLQ) {
                case 0:
                    break;
                case 8:
                    this.zzbvH = Integer.valueOf(ahwVar.zzLV());
                    break;
                case 18:
                    int iZzb = aij.zzb(ahwVar, 18);
                    int length = this.zzbvI == null ? 0 : this.zzbvI.length;
                    zzcli[] zzcliVarArr = new zzcli[iZzb + length];
                    if (length != 0) {
                        System.arraycopy(this.zzbvI, 0, zzcliVarArr, 0, length);
                    }
                    while (length < zzcliVarArr.length - 1) {
                        zzcliVarArr[length] = new zzcli();
                        ahwVar.zzb(zzcliVarArr[length]);
                        ahwVar.zzLQ();
                        length++;
                    }
                    zzcliVarArr[length] = new zzcli();
                    ahwVar.zzb(zzcliVarArr[length]);
                    this.zzbvI = zzcliVarArr;
                    break;
                case 26:
                    int iZzb2 = aij.zzb(ahwVar, 26);
                    int length2 = this.zzbvJ == null ? 0 : this.zzbvJ.length;
                    zzcln[] zzclnVarArr = new zzcln[iZzb2 + length2];
                    if (length2 != 0) {
                        System.arraycopy(this.zzbvJ, 0, zzclnVarArr, 0, length2);
                    }
                    while (length2 < zzclnVarArr.length - 1) {
                        zzclnVarArr[length2] = new zzcln();
                        ahwVar.zzb(zzclnVarArr[length2]);
                        ahwVar.zzLQ();
                        length2++;
                    }
                    zzclnVarArr[length2] = new zzcln();
                    ahwVar.zzb(zzclnVarArr[length2]);
                    this.zzbvJ = zzclnVarArr;
                    break;
                case 32:
                    this.zzbvK = Long.valueOf(ahwVar.zzLW());
                    break;
                case 40:
                    this.zzbvL = Long.valueOf(ahwVar.zzLW());
                    break;
                case Bar.DEFAULT_HEIGHT /* 48 */:
                    this.zzbvM = Long.valueOf(ahwVar.zzLW());
                    break;
                case 56:
                    this.zzbvO = Long.valueOf(ahwVar.zzLW());
                    break;
                case 66:
                    this.zzbvP = ahwVar.readString();
                    break;
                case 74:
                    this.zzbb = ahwVar.readString();
                    break;
                case 82:
                    this.zzbvQ = ahwVar.readString();
                    break;
                case 90:
                    this.zzbvR = ahwVar.readString();
                    break;
                case 96:
                    this.zzbvS = Integer.valueOf(ahwVar.zzLV());
                    break;
                case 106:
                    this.zzboV = ahwVar.readString();
                    break;
                case 114:
                    this.zzaK = ahwVar.readString();
                    break;
                case 130:
                    this.zzbha = ahwVar.readString();
                    break;
                case 136:
                    this.zzbvT = Long.valueOf(ahwVar.zzLW());
                    break;
                case TwitterApiConstants.Errors.ALREADY_UNFAVORITED /* 144 */:
                    this.zzbvU = Long.valueOf(ahwVar.zzLW());
                    break;
                case 154:
                    this.zzbvV = ahwVar.readString();
                    break;
                case 160:
                    this.zzbvW = Boolean.valueOf(ahwVar.zzLT());
                    break;
                case 170:
                    this.zzbvX = ahwVar.readString();
                    break;
                case 176:
                    this.zzbvY = Long.valueOf(ahwVar.zzLW());
                    break;
                case 184:
                    this.zzbvZ = Integer.valueOf(ahwVar.zzLV());
                    break;
                case 194:
                    this.zzboY = ahwVar.readString();
                    break;
                case 202:
                    this.zzboU = ahwVar.readString();
                    break;
                case 208:
                    this.zzbvN = Long.valueOf(ahwVar.zzLW());
                    break;
                case 224:
                    this.zzbwa = Boolean.valueOf(ahwVar.zzLT());
                    break;
                case 234:
                    int iZzb3 = aij.zzb(ahwVar, 234);
                    int length3 = this.zzbwb == null ? 0 : this.zzbwb.length;
                    zzclh[] zzclhVarArr = new zzclh[iZzb3 + length3];
                    if (length3 != 0) {
                        System.arraycopy(this.zzbwb, 0, zzclhVarArr, 0, length3);
                    }
                    while (length3 < zzclhVarArr.length - 1) {
                        zzclhVarArr[length3] = new zzclh();
                        ahwVar.zzb(zzclhVarArr[length3]);
                        ahwVar.zzLQ();
                        length3++;
                    }
                    zzclhVarArr[length3] = new zzclh();
                    ahwVar.zzb(zzclhVarArr[length3]);
                    this.zzbwb = zzclhVarArr;
                    break;
                case 242:
                    this.zzbpc = ahwVar.readString();
                    break;
                case 248:
                    this.zzbwc = Integer.valueOf(ahwVar.zzLV());
                    break;
                case 256:
                    this.zzbwd = Integer.valueOf(ahwVar.zzLV());
                    break;
                case AppService.NOTIFICATION_TTS /* 264 */:
                    this.zzbwe = Integer.valueOf(ahwVar.zzLV());
                    break;
                case 280:
                    this.zzbwf = Long.valueOf(ahwVar.zzLW());
                    break;
                case 288:
                    this.zzbwg = Long.valueOf(ahwVar.zzLW());
                    break;
                case 298:
                    this.zzbwh = ahwVar.readString();
                    break;
                default:
                    if (!super.zza(ahwVar, iZzLQ)) {
                    }
                    break;
            }
        }
        return this;
    }

    @Override // com.google.android.gms.internal.ahz, com.google.android.gms.internal.aif
    public final void zza(ahx ahxVar) throws IOException {
        if (this.zzbvH != null) {
            ahxVar.zzr(1, this.zzbvH.intValue());
        }
        if (this.zzbvI != null && this.zzbvI.length > 0) {
            for (int i = 0; i < this.zzbvI.length; i++) {
                zzcli zzcliVar = this.zzbvI[i];
                if (zzcliVar != null) {
                    ahxVar.zza(2, zzcliVar);
                }
            }
        }
        if (this.zzbvJ != null && this.zzbvJ.length > 0) {
            for (int i2 = 0; i2 < this.zzbvJ.length; i2++) {
                zzcln zzclnVar = this.zzbvJ[i2];
                if (zzclnVar != null) {
                    ahxVar.zza(3, zzclnVar);
                }
            }
        }
        if (this.zzbvK != null) {
            ahxVar.zzb(4, this.zzbvK.longValue());
        }
        if (this.zzbvL != null) {
            ahxVar.zzb(5, this.zzbvL.longValue());
        }
        if (this.zzbvM != null) {
            ahxVar.zzb(6, this.zzbvM.longValue());
        }
        if (this.zzbvO != null) {
            ahxVar.zzb(7, this.zzbvO.longValue());
        }
        if (this.zzbvP != null) {
            ahxVar.zzl(8, this.zzbvP);
        }
        if (this.zzbb != null) {
            ahxVar.zzl(9, this.zzbb);
        }
        if (this.zzbvQ != null) {
            ahxVar.zzl(10, this.zzbvQ);
        }
        if (this.zzbvR != null) {
            ahxVar.zzl(11, this.zzbvR);
        }
        if (this.zzbvS != null) {
            ahxVar.zzr(12, this.zzbvS.intValue());
        }
        if (this.zzboV != null) {
            ahxVar.zzl(13, this.zzboV);
        }
        if (this.zzaK != null) {
            ahxVar.zzl(14, this.zzaK);
        }
        if (this.zzbha != null) {
            ahxVar.zzl(16, this.zzbha);
        }
        if (this.zzbvT != null) {
            ahxVar.zzb(17, this.zzbvT.longValue());
        }
        if (this.zzbvU != null) {
            ahxVar.zzb(18, this.zzbvU.longValue());
        }
        if (this.zzbvV != null) {
            ahxVar.zzl(19, this.zzbvV);
        }
        if (this.zzbvW != null) {
            ahxVar.zzk(20, this.zzbvW.booleanValue());
        }
        if (this.zzbvX != null) {
            ahxVar.zzl(21, this.zzbvX);
        }
        if (this.zzbvY != null) {
            ahxVar.zzb(22, this.zzbvY.longValue());
        }
        if (this.zzbvZ != null) {
            ahxVar.zzr(23, this.zzbvZ.intValue());
        }
        if (this.zzboY != null) {
            ahxVar.zzl(24, this.zzboY);
        }
        if (this.zzboU != null) {
            ahxVar.zzl(25, this.zzboU);
        }
        if (this.zzbvN != null) {
            ahxVar.zzb(26, this.zzbvN.longValue());
        }
        if (this.zzbwa != null) {
            ahxVar.zzk(28, this.zzbwa.booleanValue());
        }
        if (this.zzbwb != null && this.zzbwb.length > 0) {
            for (int i3 = 0; i3 < this.zzbwb.length; i3++) {
                zzclh zzclhVar = this.zzbwb[i3];
                if (zzclhVar != null) {
                    ahxVar.zza(29, zzclhVar);
                }
            }
        }
        if (this.zzbpc != null) {
            ahxVar.zzl(30, this.zzbpc);
        }
        if (this.zzbwc != null) {
            ahxVar.zzr(31, this.zzbwc.intValue());
        }
        if (this.zzbwd != null) {
            ahxVar.zzr(32, this.zzbwd.intValue());
        }
        if (this.zzbwe != null) {
            ahxVar.zzr(33, this.zzbwe.intValue());
        }
        if (this.zzbwf != null) {
            ahxVar.zzb(35, this.zzbwf.longValue());
        }
        if (this.zzbwg != null) {
            ahxVar.zzb(36, this.zzbwg.longValue());
        }
        if (this.zzbwh != null) {
            ahxVar.zzl(37, this.zzbwh);
        }
        super.zza(ahxVar);
    }

    @Override // com.google.android.gms.internal.ahz, com.google.android.gms.internal.aif
    protected final int zzn() {
        int iZzn = super.zzn();
        if (this.zzbvH != null) {
            iZzn += ahx.zzs(1, this.zzbvH.intValue());
        }
        if (this.zzbvI != null && this.zzbvI.length > 0) {
            int iZzb = iZzn;
            for (int i = 0; i < this.zzbvI.length; i++) {
                zzcli zzcliVar = this.zzbvI[i];
                if (zzcliVar != null) {
                    iZzb += ahx.zzb(2, zzcliVar);
                }
            }
            iZzn = iZzb;
        }
        if (this.zzbvJ != null && this.zzbvJ.length > 0) {
            int iZzb2 = iZzn;
            for (int i2 = 0; i2 < this.zzbvJ.length; i2++) {
                zzcln zzclnVar = this.zzbvJ[i2];
                if (zzclnVar != null) {
                    iZzb2 += ahx.zzb(3, zzclnVar);
                }
            }
            iZzn = iZzb2;
        }
        if (this.zzbvK != null) {
            iZzn += ahx.zze(4, this.zzbvK.longValue());
        }
        if (this.zzbvL != null) {
            iZzn += ahx.zze(5, this.zzbvL.longValue());
        }
        if (this.zzbvM != null) {
            iZzn += ahx.zze(6, this.zzbvM.longValue());
        }
        if (this.zzbvO != null) {
            iZzn += ahx.zze(7, this.zzbvO.longValue());
        }
        if (this.zzbvP != null) {
            iZzn += ahx.zzm(8, this.zzbvP);
        }
        if (this.zzbb != null) {
            iZzn += ahx.zzm(9, this.zzbb);
        }
        if (this.zzbvQ != null) {
            iZzn += ahx.zzm(10, this.zzbvQ);
        }
        if (this.zzbvR != null) {
            iZzn += ahx.zzm(11, this.zzbvR);
        }
        if (this.zzbvS != null) {
            iZzn += ahx.zzs(12, this.zzbvS.intValue());
        }
        if (this.zzboV != null) {
            iZzn += ahx.zzm(13, this.zzboV);
        }
        if (this.zzaK != null) {
            iZzn += ahx.zzm(14, this.zzaK);
        }
        if (this.zzbha != null) {
            iZzn += ahx.zzm(16, this.zzbha);
        }
        if (this.zzbvT != null) {
            iZzn += ahx.zze(17, this.zzbvT.longValue());
        }
        if (this.zzbvU != null) {
            iZzn += ahx.zze(18, this.zzbvU.longValue());
        }
        if (this.zzbvV != null) {
            iZzn += ahx.zzm(19, this.zzbvV);
        }
        if (this.zzbvW != null) {
            this.zzbvW.booleanValue();
            iZzn += ahx.zzcs(20) + 1;
        }
        if (this.zzbvX != null) {
            iZzn += ahx.zzm(21, this.zzbvX);
        }
        if (this.zzbvY != null) {
            iZzn += ahx.zze(22, this.zzbvY.longValue());
        }
        if (this.zzbvZ != null) {
            iZzn += ahx.zzs(23, this.zzbvZ.intValue());
        }
        if (this.zzboY != null) {
            iZzn += ahx.zzm(24, this.zzboY);
        }
        if (this.zzboU != null) {
            iZzn += ahx.zzm(25, this.zzboU);
        }
        if (this.zzbvN != null) {
            iZzn += ahx.zze(26, this.zzbvN.longValue());
        }
        if (this.zzbwa != null) {
            this.zzbwa.booleanValue();
            iZzn += ahx.zzcs(28) + 1;
        }
        if (this.zzbwb != null && this.zzbwb.length > 0) {
            for (int i3 = 0; i3 < this.zzbwb.length; i3++) {
                zzclh zzclhVar = this.zzbwb[i3];
                if (zzclhVar != null) {
                    iZzn += ahx.zzb(29, zzclhVar);
                }
            }
        }
        if (this.zzbpc != null) {
            iZzn += ahx.zzm(30, this.zzbpc);
        }
        if (this.zzbwc != null) {
            iZzn += ahx.zzs(31, this.zzbwc.intValue());
        }
        if (this.zzbwd != null) {
            iZzn += ahx.zzs(32, this.zzbwd.intValue());
        }
        if (this.zzbwe != null) {
            iZzn += ahx.zzs(33, this.zzbwe.intValue());
        }
        if (this.zzbwf != null) {
            iZzn += ahx.zze(35, this.zzbwf.longValue());
        }
        if (this.zzbwg != null) {
            iZzn += ahx.zze(36, this.zzbwg.longValue());
        }
        return this.zzbwh != null ? iZzn + ahx.zzm(37, this.zzbwh) : iZzn;
    }
}
