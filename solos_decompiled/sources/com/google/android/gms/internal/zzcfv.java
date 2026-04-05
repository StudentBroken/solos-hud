package com.google.android.gms.internal;

import android.content.ContentValues;
import android.database.sqlite.SQLiteException;
import android.support.annotation.WorkerThread;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import com.google.android.gms.common.internal.zzbr;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.PatternSyntaxException;

/* JADX INFO: loaded from: classes36.dex */
final class zzcfv extends zzciv {
    zzcfv(zzchx zzchxVar) {
        super(zzchxVar);
    }

    private final Boolean zza(double d, zzclb zzclbVar) {
        try {
            return zza(new BigDecimal(d), zzclbVar, Math.ulp(d));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private final Boolean zza(long j, zzclb zzclbVar) {
        try {
            return zza(new BigDecimal(j), zzclbVar, 0.0d);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final Boolean zza(zzckz zzckzVar, zzcli zzcliVar, long j) {
        Boolean boolZza;
        if (zzckzVar.zzbuU != null) {
            Boolean boolZza2 = zza(j, zzckzVar.zzbuU);
            if (boolZza2 == null) {
                return null;
            }
            if (!boolZza2.booleanValue()) {
                return false;
            }
        }
        HashSet hashSet = new HashSet();
        for (zzcla zzclaVar : zzckzVar.zzbuS) {
            if (TextUtils.isEmpty(zzclaVar.zzbuZ)) {
                zzwE().zzyx().zzj("null or empty param name in filter. event", zzwz().zzdX(zzcliVar.name));
                return null;
            }
            hashSet.add(zzclaVar.zzbuZ);
        }
        ArrayMap arrayMap = new ArrayMap();
        for (zzclj zzcljVar : zzcliVar.zzbvA) {
            if (hashSet.contains(zzcljVar.name)) {
                if (zzcljVar.zzbvE != null) {
                    arrayMap.put(zzcljVar.name, zzcljVar.zzbvE);
                } else if (zzcljVar.zzbuF != null) {
                    arrayMap.put(zzcljVar.name, zzcljVar.zzbuF);
                } else {
                    if (zzcljVar.zzaIH == null) {
                        zzwE().zzyx().zze("Unknown value for param. event, param", zzwz().zzdX(zzcliVar.name), zzwz().zzdY(zzcljVar.name));
                        return null;
                    }
                    arrayMap.put(zzcljVar.name, zzcljVar.zzaIH);
                }
            }
        }
        for (zzcla zzclaVar2 : zzckzVar.zzbuS) {
            boolean zEquals = Boolean.TRUE.equals(zzclaVar2.zzbuY);
            String str = zzclaVar2.zzbuZ;
            if (TextUtils.isEmpty(str)) {
                zzwE().zzyx().zzj("Event has empty param name. event", zzwz().zzdX(zzcliVar.name));
                return null;
            }
            V v = arrayMap.get(str);
            if (v instanceof Long) {
                if (zzclaVar2.zzbuX == null) {
                    zzwE().zzyx().zze("No number filter for long param. event, param", zzwz().zzdX(zzcliVar.name), zzwz().zzdY(str));
                    return null;
                }
                Boolean boolZza3 = zza(((Long) v).longValue(), zzclaVar2.zzbuX);
                if (boolZza3 == null) {
                    return null;
                }
                if ((!boolZza3.booleanValue()) ^ zEquals) {
                    return false;
                }
            } else if (v instanceof Double) {
                if (zzclaVar2.zzbuX == null) {
                    zzwE().zzyx().zze("No number filter for double param. event, param", zzwz().zzdX(zzcliVar.name), zzwz().zzdY(str));
                    return null;
                }
                Boolean boolZza4 = zza(((Double) v).doubleValue(), zzclaVar2.zzbuX);
                if (boolZza4 == null) {
                    return null;
                }
                if ((!boolZza4.booleanValue()) ^ zEquals) {
                    return false;
                }
            } else {
                if (!(v instanceof String)) {
                    if (v == 0) {
                        zzwE().zzyB().zze("Missing param for filter. event, param", zzwz().zzdX(zzcliVar.name), zzwz().zzdY(str));
                        return false;
                    }
                    zzwE().zzyx().zze("Unknown param type. event, param", zzwz().zzdX(zzcliVar.name), zzwz().zzdY(str));
                    return null;
                }
                if (zzclaVar2.zzbuW != null) {
                    boolZza = zza((String) v, zzclaVar2.zzbuW);
                } else {
                    if (zzclaVar2.zzbuX == null) {
                        zzwE().zzyx().zze("No filter for String param. event, param", zzwz().zzdX(zzcliVar.name), zzwz().zzdY(str));
                        return null;
                    }
                    if (!zzckx.zzeA((String) v)) {
                        zzwE().zzyx().zze("Invalid param value for number filter. event, param", zzwz().zzdX(zzcliVar.name), zzwz().zzdY(str));
                        return null;
                    }
                    boolZza = zza((String) v, zzclaVar2.zzbuX);
                }
                if (boolZza == null) {
                    return null;
                }
                if ((!boolZza.booleanValue()) ^ zEquals) {
                    return false;
                }
            }
        }
        return true;
    }

    private static Boolean zza(Boolean bool, boolean z) {
        if (bool == null) {
            return null;
        }
        return Boolean.valueOf(bool.booleanValue() ^ z);
    }

    private final Boolean zza(String str, int i, boolean z, String str2, List<String> list, String str3) {
        if (str == null) {
            return null;
        }
        if (i == 6) {
            if (list == null || list.size() == 0) {
                return null;
            }
        } else if (str2 == null) {
            return null;
        }
        if (!z && i != 1) {
            str = str.toUpperCase(Locale.ENGLISH);
        }
        switch (i) {
            case 1:
                try {
                } catch (PatternSyntaxException e) {
                    zzwE().zzyx().zzj("Invalid regular expression in REGEXP audience filter. expression", str3);
                    return null;
                }
                break;
        }
        return null;
    }

    private final Boolean zza(String str, zzclb zzclbVar) {
        if (!zzckx.zzeA(str)) {
            return null;
        }
        try {
            return zza(new BigDecimal(str), zzclbVar, 0.0d);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private final Boolean zza(String str, zzcld zzcldVar) {
        List<String> arrayList;
        zzbr.zzu(zzcldVar);
        if (str == null || zzcldVar.zzbvi == null || zzcldVar.zzbvi.intValue() == 0) {
            return null;
        }
        if (zzcldVar.zzbvi.intValue() == 6) {
            if (zzcldVar.zzbvl == null || zzcldVar.zzbvl.length == 0) {
                return null;
            }
        } else if (zzcldVar.zzbvj == null) {
            return null;
        }
        int iIntValue = zzcldVar.zzbvi.intValue();
        boolean z = zzcldVar.zzbvk != null && zzcldVar.zzbvk.booleanValue();
        String upperCase = (z || iIntValue == 1 || iIntValue == 6) ? zzcldVar.zzbvj : zzcldVar.zzbvj.toUpperCase(Locale.ENGLISH);
        if (zzcldVar.zzbvl == null) {
            arrayList = null;
        } else {
            String[] strArr = zzcldVar.zzbvl;
            if (z) {
                arrayList = Arrays.asList(strArr);
            } else {
                arrayList = new ArrayList<>();
                for (String str2 : strArr) {
                    arrayList.add(str2.toUpperCase(Locale.ENGLISH));
                }
            }
        }
        return zza(str, iIntValue, z, upperCase, arrayList, iIntValue == 1 ? upperCase : null);
    }

    private static Boolean zza(BigDecimal bigDecimal, zzclb zzclbVar, double d) {
        BigDecimal bigDecimal2;
        BigDecimal bigDecimal3;
        BigDecimal bigDecimal4;
        zzbr.zzu(zzclbVar);
        if (zzclbVar.zzbva == null || zzclbVar.zzbva.intValue() == 0) {
            return null;
        }
        if (zzclbVar.zzbva.intValue() == 4) {
            if (zzclbVar.zzbvd == null || zzclbVar.zzbve == null) {
                return null;
            }
        } else if (zzclbVar.zzbvc == null) {
            return null;
        }
        int iIntValue = zzclbVar.zzbva.intValue();
        if (zzclbVar.zzbva.intValue() == 4) {
            if (!zzckx.zzeA(zzclbVar.zzbvd) || !zzckx.zzeA(zzclbVar.zzbve)) {
                return null;
            }
            try {
                bigDecimal2 = new BigDecimal(zzclbVar.zzbvd);
                bigDecimal4 = new BigDecimal(zzclbVar.zzbve);
                bigDecimal3 = null;
            } catch (NumberFormatException e) {
                return null;
            }
        } else {
            if (!zzckx.zzeA(zzclbVar.zzbvc)) {
                return null;
            }
            try {
                bigDecimal2 = null;
                bigDecimal3 = new BigDecimal(zzclbVar.zzbvc);
                bigDecimal4 = null;
            } catch (NumberFormatException e2) {
                return null;
            }
        }
        if (iIntValue != 4) {
            if (bigDecimal3 != null) {
            }
            return null;
        }
        if (bigDecimal2 == null) {
            return null;
        }
        switch (iIntValue) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                if (d == 0.0d) {
                }
                break;
            case 4:
                break;
        }
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @WorkerThread
    final zzclh[] zza(String str, zzcli[] zzcliVarArr, zzcln[] zzclnVarArr) {
        Map<Integer, List<zzclc>> map;
        Boolean boolZza;
        zzcgh zzcghVarZzyq;
        Map<Integer, List<zzckz>> map2;
        zzbr.zzcF(str);
        HashSet hashSet = new HashSet();
        ArrayMap arrayMap = new ArrayMap();
        ArrayMap arrayMap2 = new ArrayMap();
        ArrayMap arrayMap3 = new ArrayMap();
        Map<Integer, zzclm> mapZzdU = zzwy().zzdU(str);
        if (mapZzdU != null) {
            Iterator<Integer> it = mapZzdU.keySet().iterator();
            while (it.hasNext()) {
                int iIntValue = it.next().intValue();
                zzclm zzclmVar = mapZzdU.get(Integer.valueOf(iIntValue));
                BitSet bitSet = (BitSet) arrayMap2.get(Integer.valueOf(iIntValue));
                BitSet bitSet2 = (BitSet) arrayMap3.get(Integer.valueOf(iIntValue));
                if (bitSet == null) {
                    bitSet = new BitSet();
                    arrayMap2.put(Integer.valueOf(iIntValue), bitSet);
                    bitSet2 = new BitSet();
                    arrayMap3.put(Integer.valueOf(iIntValue), bitSet2);
                }
                for (int i = 0; i < (zzclmVar.zzbwi.length << 6); i++) {
                    if (zzckx.zza(zzclmVar.zzbwi, i)) {
                        zzwE().zzyB().zze("Filter already evaluated. audience ID, filter ID", Integer.valueOf(iIntValue), Integer.valueOf(i));
                        bitSet2.set(i);
                        if (zzckx.zza(zzclmVar.zzbwj, i)) {
                            bitSet.set(i);
                        }
                    }
                }
                zzclh zzclhVar = new zzclh();
                arrayMap.put(Integer.valueOf(iIntValue), zzclhVar);
                zzclhVar.zzbvy = false;
                zzclhVar.zzbvx = zzclmVar;
                zzclhVar.zzbvw = new zzclm();
                zzclhVar.zzbvw.zzbwj = zzckx.zza(bitSet);
                zzclhVar.zzbvw.zzbwi = zzckx.zza(bitSet2);
            }
        }
        if (zzcliVarArr != null) {
            ArrayMap arrayMap4 = new ArrayMap();
            int length = zzcliVarArr.length;
            int i2 = 0;
            while (true) {
                int i3 = i2;
                if (i3 >= length) {
                    break;
                }
                zzcli zzcliVar = zzcliVarArr[i3];
                zzcgh zzcghVarZzE = zzwy().zzE(str, zzcliVar.name);
                if (zzcghVarZzE == null) {
                    zzwE().zzyx().zze("Event aggregate wasn't created during raw event logging. appId, event", zzcgx.zzea(str), zzwz().zzdX(zzcliVar.name));
                    zzcghVarZzyq = new zzcgh(str, zzcliVar.name, 1L, 1L, zzcliVar.zzbvB.longValue());
                } else {
                    zzcghVarZzyq = zzcghVarZzE.zzyq();
                }
                zzwy().zza(zzcghVarZzyq);
                long j = zzcghVarZzyq.zzbpK;
                Map<Integer, List<zzckz>> map3 = (Map) arrayMap4.get(zzcliVar.name);
                if (map3 == null) {
                    Map<Integer, List<zzckz>> mapZzJ = zzwy().zzJ(str, zzcliVar.name);
                    if (mapZzJ == null) {
                        mapZzJ = new ArrayMap<>();
                    }
                    arrayMap4.put(zzcliVar.name, mapZzJ);
                    map2 = mapZzJ;
                } else {
                    map2 = map3;
                }
                Iterator<Integer> it2 = map2.keySet().iterator();
                while (it2.hasNext()) {
                    int iIntValue2 = it2.next().intValue();
                    if (hashSet.contains(Integer.valueOf(iIntValue2))) {
                        zzwE().zzyB().zzj("Skipping failed audience ID", Integer.valueOf(iIntValue2));
                    } else {
                        zzclh zzclhVar2 = (zzclh) arrayMap.get(Integer.valueOf(iIntValue2));
                        BitSet bitSet3 = (BitSet) arrayMap2.get(Integer.valueOf(iIntValue2));
                        BitSet bitSet4 = (BitSet) arrayMap3.get(Integer.valueOf(iIntValue2));
                        if (zzclhVar2 == null) {
                            zzclh zzclhVar3 = new zzclh();
                            arrayMap.put(Integer.valueOf(iIntValue2), zzclhVar3);
                            zzclhVar3.zzbvy = true;
                            bitSet3 = new BitSet();
                            arrayMap2.put(Integer.valueOf(iIntValue2), bitSet3);
                            bitSet4 = new BitSet();
                            arrayMap3.put(Integer.valueOf(iIntValue2), bitSet4);
                        }
                        for (zzckz zzckzVar : map2.get(Integer.valueOf(iIntValue2))) {
                            if (zzwE().zzz(2)) {
                                zzwE().zzyB().zzd("Evaluating filter. audience, filter, event", Integer.valueOf(iIntValue2), zzckzVar.zzbuQ, zzwz().zzdX(zzckzVar.zzbuR));
                                zzwE().zzyB().zzj("Filter definition", zzwz().zza(zzckzVar));
                            }
                            if (zzckzVar.zzbuQ == null || zzckzVar.zzbuQ.intValue() > 256) {
                                zzwE().zzyx().zze("Invalid event filter ID. appId, id", zzcgx.zzea(str), String.valueOf(zzckzVar.zzbuQ));
                            } else if (bitSet3.get(zzckzVar.zzbuQ.intValue())) {
                                zzwE().zzyB().zze("Event filter already evaluated true. audience ID, filter ID", Integer.valueOf(iIntValue2), zzckzVar.zzbuQ);
                            } else {
                                Boolean boolZza2 = zza(zzckzVar, zzcliVar, j);
                                zzwE().zzyB().zzj("Event filter result", boolZza2 == null ? "null" : boolZza2);
                                if (boolZza2 == null) {
                                    hashSet.add(Integer.valueOf(iIntValue2));
                                } else {
                                    bitSet4.set(zzckzVar.zzbuQ.intValue());
                                    if (boolZza2.booleanValue()) {
                                        bitSet3.set(zzckzVar.zzbuQ.intValue());
                                    }
                                }
                            }
                        }
                    }
                }
                i2 = i3 + 1;
            }
        }
        if (zzclnVarArr != null) {
            ArrayMap arrayMap5 = new ArrayMap();
            for (zzcln zzclnVar : zzclnVarArr) {
                Map<Integer, List<zzclc>> map4 = (Map) arrayMap5.get(zzclnVar.name);
                if (map4 == null) {
                    Map<Integer, List<zzclc>> mapZzK = zzwy().zzK(str, zzclnVar.name);
                    if (mapZzK == null) {
                        mapZzK = new ArrayMap<>();
                    }
                    arrayMap5.put(zzclnVar.name, mapZzK);
                    map = mapZzK;
                } else {
                    map = map4;
                }
                Iterator<Integer> it3 = map.keySet().iterator();
                while (it3.hasNext()) {
                    int iIntValue3 = it3.next().intValue();
                    if (hashSet.contains(Integer.valueOf(iIntValue3))) {
                        zzwE().zzyB().zzj("Skipping failed audience ID", Integer.valueOf(iIntValue3));
                    } else {
                        zzclh zzclhVar4 = (zzclh) arrayMap.get(Integer.valueOf(iIntValue3));
                        BitSet bitSet5 = (BitSet) arrayMap2.get(Integer.valueOf(iIntValue3));
                        BitSet bitSet6 = (BitSet) arrayMap3.get(Integer.valueOf(iIntValue3));
                        if (zzclhVar4 == null) {
                            zzclh zzclhVar5 = new zzclh();
                            arrayMap.put(Integer.valueOf(iIntValue3), zzclhVar5);
                            zzclhVar5.zzbvy = true;
                            bitSet5 = new BitSet();
                            arrayMap2.put(Integer.valueOf(iIntValue3), bitSet5);
                            bitSet6 = new BitSet();
                            arrayMap3.put(Integer.valueOf(iIntValue3), bitSet6);
                        }
                        for (zzclc zzclcVar : map.get(Integer.valueOf(iIntValue3))) {
                            if (zzwE().zzz(2)) {
                                zzwE().zzyB().zzd("Evaluating filter. audience, filter, property", Integer.valueOf(iIntValue3), zzclcVar.zzbuQ, zzwz().zzdZ(zzclcVar.zzbvg));
                                zzwE().zzyB().zzj("Filter definition", zzwz().zza(zzclcVar));
                            }
                            if (zzclcVar.zzbuQ == null || zzclcVar.zzbuQ.intValue() > 256) {
                                zzwE().zzyx().zze("Invalid property filter ID. appId, id", zzcgx.zzea(str), String.valueOf(zzclcVar.zzbuQ));
                                hashSet.add(Integer.valueOf(iIntValue3));
                                break;
                            }
                            if (bitSet5.get(zzclcVar.zzbuQ.intValue())) {
                                zzwE().zzyB().zze("Property filter already evaluated true. audience ID, filter ID", Integer.valueOf(iIntValue3), zzclcVar.zzbuQ);
                            } else {
                                zzcla zzclaVar = zzclcVar.zzbvh;
                                if (zzclaVar == null) {
                                    zzwE().zzyx().zzj("Missing property filter. property", zzwz().zzdZ(zzclnVar.name));
                                    boolZza = null;
                                } else {
                                    boolean zEquals = Boolean.TRUE.equals(zzclaVar.zzbuY);
                                    if (zzclnVar.zzbvE != null) {
                                        if (zzclaVar.zzbuX == null) {
                                            zzwE().zzyx().zzj("No number filter for long property. property", zzwz().zzdZ(zzclnVar.name));
                                            boolZza = null;
                                        } else {
                                            boolZza = zza(zza(zzclnVar.zzbvE.longValue(), zzclaVar.zzbuX), zEquals);
                                        }
                                    } else if (zzclnVar.zzbuF != null) {
                                        if (zzclaVar.zzbuX == null) {
                                            zzwE().zzyx().zzj("No number filter for double property. property", zzwz().zzdZ(zzclnVar.name));
                                            boolZza = null;
                                        } else {
                                            boolZza = zza(zza(zzclnVar.zzbuF.doubleValue(), zzclaVar.zzbuX), zEquals);
                                        }
                                    } else if (zzclnVar.zzaIH == null) {
                                        zzwE().zzyx().zzj("User property has no value, property", zzwz().zzdZ(zzclnVar.name));
                                        boolZza = null;
                                    } else if (zzclaVar.zzbuW == null) {
                                        if (zzclaVar.zzbuX == null) {
                                            zzwE().zzyx().zzj("No string or number filter defined. property", zzwz().zzdZ(zzclnVar.name));
                                        } else if (zzckx.zzeA(zzclnVar.zzaIH)) {
                                            boolZza = zza(zza(zzclnVar.zzaIH, zzclaVar.zzbuX), zEquals);
                                        } else {
                                            zzwE().zzyx().zze("Invalid user property value for Numeric number filter. property, value", zzwz().zzdZ(zzclnVar.name), zzclnVar.zzaIH);
                                        }
                                        boolZza = null;
                                    } else {
                                        boolZza = zza(zza(zzclnVar.zzaIH, zzclaVar.zzbuW), zEquals);
                                    }
                                }
                                zzwE().zzyB().zzj("Property filter result", boolZza == null ? "null" : boolZza);
                                if (boolZza == null) {
                                    hashSet.add(Integer.valueOf(iIntValue3));
                                } else {
                                    bitSet6.set(zzclcVar.zzbuQ.intValue());
                                    if (boolZza.booleanValue()) {
                                        bitSet5.set(zzclcVar.zzbuQ.intValue());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        zzclh[] zzclhVarArr = new zzclh[arrayMap2.size()];
        Iterator it4 = arrayMap2.keySet().iterator();
        int i4 = 0;
        while (it4.hasNext()) {
            int iIntValue4 = ((Integer) it4.next()).intValue();
            if (!hashSet.contains(Integer.valueOf(iIntValue4))) {
                zzclh zzclhVar6 = (zzclh) arrayMap.get(Integer.valueOf(iIntValue4));
                zzclh zzclhVar7 = zzclhVar6 == null ? new zzclh() : zzclhVar6;
                int i5 = i4 + 1;
                zzclhVarArr[i4] = zzclhVar7;
                zzclhVar7.zzbuM = Integer.valueOf(iIntValue4);
                zzclhVar7.zzbvw = new zzclm();
                zzclhVar7.zzbvw.zzbwj = zzckx.zza((BitSet) arrayMap2.get(Integer.valueOf(iIntValue4)));
                zzclhVar7.zzbvw.zzbwi = zzckx.zza((BitSet) arrayMap3.get(Integer.valueOf(iIntValue4)));
                zzcfz zzcfzVarZzwy = zzwy();
                zzclm zzclmVar2 = zzclhVar7.zzbvw;
                zzcfzVarZzwy.zzkC();
                zzcfzVarZzwy.zzjB();
                zzbr.zzcF(str);
                zzbr.zzu(zzclmVar2);
                try {
                    byte[] bArr = new byte[zzclmVar2.zzMl()];
                    ahx ahxVarZzc = ahx.zzc(bArr, 0, bArr.length);
                    zzclmVar2.zza(ahxVarZzc);
                    ahxVarZzc.zzMc();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("app_id", str);
                    contentValues.put("audience_id", Integer.valueOf(iIntValue4));
                    contentValues.put("current_results", bArr);
                    try {
                        if (zzcfzVarZzwy.getWritableDatabase().insertWithOnConflict("audience_filter_values", null, contentValues, 5) == -1) {
                            zzcfzVarZzwy.zzwE().zzyv().zzj("Failed to insert filter results (got -1). appId", zzcgx.zzea(str));
                        }
                        i4 = i5;
                    } catch (SQLiteException e) {
                        zzcfzVarZzwy.zzwE().zzyv().zze("Error storing filter results. appId", zzcgx.zzea(str), e);
                        i4 = i5;
                    }
                } catch (IOException e2) {
                    zzcfzVarZzwy.zzwE().zzyv().zze("Configuration loss. Failed to serialize filter results. appId", zzcgx.zzea(str), e2);
                    i4 = i5;
                }
            }
        }
        return (zzclh[]) Arrays.copyOf(zzclhVarArr, i4);
    }

    @Override // com.google.android.gms.internal.zzciv
    protected final void zzjC() {
    }
}
