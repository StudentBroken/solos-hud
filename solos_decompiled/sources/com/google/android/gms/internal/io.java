package com.google.android.gms.internal;

import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

/* JADX INFO: loaded from: classes6.dex */
public final class io {
    private static int zza(String str, is[] isVarArr) {
        int i = 14;
        for (is isVar : isVarArr) {
            if (i != 14) {
                if (isVar.type != i) {
                    throw new IllegalArgumentException(new StringBuilder(String.valueOf(str).length() + 126).append("The ArrayList elements should all be the same type, but ArrayList with key ").append(str).append(" contains items of type ").append(i).append(" and ").append(isVar.type).toString());
                }
            } else if (isVar.type == 9 || isVar.type == 2 || isVar.type == 6) {
                i = isVar.type;
            } else if (isVar.type != 14) {
                throw new IllegalArgumentException(new StringBuilder(String.valueOf(str).length() + 48).append("Unexpected TypedValue type: ").append(isVar.type).append(" for key ").append(str).toString());
            }
        }
        return i;
    }

    public static ip zza(DataMap dataMap) {
        iq iqVar = new iq();
        ArrayList arrayList = new ArrayList();
        iqVar.zzbTJ = zza(dataMap, arrayList);
        return new ip(iqVar, arrayList);
    }

    private static is zza(List<Asset> list, Object obj) {
        int i;
        int i2 = 0;
        is isVar = new is();
        if (obj == null) {
            isVar.type = 14;
            return isVar;
        }
        isVar.zzbTN = new it();
        if (obj instanceof String) {
            isVar.type = 2;
            isVar.zzbTN.zzbTP = (String) obj;
        } else if (obj instanceof Integer) {
            isVar.type = 6;
            isVar.zzbTN.zzbTT = ((Integer) obj).intValue();
        } else if (obj instanceof Long) {
            isVar.type = 5;
            isVar.zzbTN.zzbTS = ((Long) obj).longValue();
        } else if (obj instanceof Double) {
            isVar.type = 3;
            isVar.zzbTN.zzbTQ = ((Double) obj).doubleValue();
        } else if (obj instanceof Float) {
            isVar.type = 4;
            isVar.zzbTN.zzbTR = ((Float) obj).floatValue();
        } else if (obj instanceof Boolean) {
            isVar.type = 8;
            isVar.zzbTN.zzbTV = ((Boolean) obj).booleanValue();
        } else if (obj instanceof Byte) {
            isVar.type = 7;
            isVar.zzbTN.zzbTU = ((Byte) obj).byteValue();
        } else if (obj instanceof byte[]) {
            isVar.type = 1;
            isVar.zzbTN.zzbTO = (byte[]) obj;
        } else if (obj instanceof String[]) {
            isVar.type = 11;
            isVar.zzbTN.zzbTY = (String[]) obj;
        } else if (obj instanceof long[]) {
            isVar.type = 12;
            isVar.zzbTN.zzbTZ = (long[]) obj;
        } else if (obj instanceof float[]) {
            isVar.type = 15;
            isVar.zzbTN.zzbUa = (float[]) obj;
        } else if (obj instanceof Asset) {
            isVar.type = 13;
            it itVar = isVar.zzbTN;
            list.add((Asset) obj);
            itVar.zzbUb = list.size() - 1;
        } else if (obj instanceof DataMap) {
            isVar.type = 9;
            DataMap dataMap = (DataMap) obj;
            TreeSet treeSet = new TreeSet(dataMap.keySet());
            ir[] irVarArr = new ir[treeSet.size()];
            Iterator it = treeSet.iterator();
            while (true) {
                int i3 = i2;
                if (!it.hasNext()) {
                    break;
                }
                String str = (String) it.next();
                irVarArr[i3] = new ir();
                irVarArr[i3].name = str;
                irVarArr[i3].zzbTL = zza(list, dataMap.get(str));
                i2 = i3 + 1;
            }
            isVar.zzbTN.zzbTW = irVarArr;
        } else {
            if (!(obj instanceof ArrayList)) {
                String strValueOf = String.valueOf(obj.getClass().getSimpleName());
                throw new RuntimeException(strValueOf.length() != 0 ? "newFieldValueFromValue: unexpected value ".concat(strValueOf) : new String("newFieldValueFromValue: unexpected value "));
            }
            isVar.type = 10;
            ArrayList arrayList = (ArrayList) obj;
            is[] isVarArr = new is[arrayList.size()];
            Object obj2 = null;
            int size = arrayList.size();
            int i4 = 0;
            int i5 = 14;
            while (i4 < size) {
                Object obj3 = arrayList.get(i4);
                is isVarZza = zza(list, obj3);
                if (isVarZza.type != 14 && isVarZza.type != 2 && isVarZza.type != 6 && isVarZza.type != 9) {
                    String strValueOf2 = String.valueOf(obj3.getClass());
                    throw new IllegalArgumentException(new StringBuilder(String.valueOf(strValueOf2).length() + 130).append("The only ArrayList element types supported by DataBundleUtil are String, Integer, Bundle, and null, but this ArrayList contains a ").append(strValueOf2).toString());
                }
                if (i5 == 14 && isVarZza.type != 14) {
                    i = isVarZza.type;
                } else {
                    if (isVarZza.type != i5) {
                        String strValueOf3 = String.valueOf(obj2.getClass());
                        String strValueOf4 = String.valueOf(obj3.getClass());
                        throw new IllegalArgumentException(new StringBuilder(String.valueOf(strValueOf3).length() + 80 + String.valueOf(strValueOf4).length()).append("ArrayList elements must all be of the sameclass, but this one contains a ").append(strValueOf3).append(" and a ").append(strValueOf4).toString());
                    }
                    obj3 = obj2;
                    i = i5;
                }
                isVarArr[i4] = isVarZza;
                i4++;
                i5 = i;
                obj2 = obj3;
            }
            isVar.zzbTN.zzbTX = isVarArr;
        }
        return isVar;
    }

    public static DataMap zza(ip ipVar) {
        DataMap dataMap = new DataMap();
        for (ir irVar : ipVar.zzbTH.zzbTJ) {
            zza(ipVar.zzbTI, dataMap, irVar.name, irVar.zzbTL);
        }
        return dataMap;
    }

    private static ArrayList zza(List<Asset> list, it itVar, int i) {
        ArrayList arrayList = new ArrayList(itVar.zzbTX.length);
        for (is isVar : itVar.zzbTX) {
            if (isVar.type == 14) {
                arrayList.add(null);
            } else if (i == 9) {
                DataMap dataMap = new DataMap();
                ir[] irVarArr = isVar.zzbTN.zzbTW;
                for (ir irVar : irVarArr) {
                    zza(list, dataMap, irVar.name, irVar.zzbTL);
                }
                arrayList.add(dataMap);
            } else if (i == 2) {
                arrayList.add(isVar.zzbTN.zzbTP);
            } else {
                if (i != 6) {
                    throw new IllegalArgumentException(new StringBuilder(39).append("Unexpected typeOfArrayList: ").append(i).toString());
                }
                arrayList.add(Integer.valueOf(isVar.zzbTN.zzbTT));
            }
        }
        return arrayList;
    }

    private static void zza(List<Asset> list, DataMap dataMap, String str, is isVar) {
        int i = isVar.type;
        if (i == 14) {
            dataMap.putString(str, null);
            return;
        }
        it itVar = isVar.zzbTN;
        if (i == 1) {
            dataMap.putByteArray(str, itVar.zzbTO);
            return;
        }
        if (i == 11) {
            dataMap.putStringArray(str, itVar.zzbTY);
            return;
        }
        if (i == 12) {
            dataMap.putLongArray(str, itVar.zzbTZ);
            return;
        }
        if (i == 15) {
            dataMap.putFloatArray(str, itVar.zzbUa);
            return;
        }
        if (i == 2) {
            dataMap.putString(str, itVar.zzbTP);
            return;
        }
        if (i == 3) {
            dataMap.putDouble(str, itVar.zzbTQ);
            return;
        }
        if (i == 4) {
            dataMap.putFloat(str, itVar.zzbTR);
            return;
        }
        if (i == 5) {
            dataMap.putLong(str, itVar.zzbTS);
            return;
        }
        if (i == 6) {
            dataMap.putInt(str, itVar.zzbTT);
            return;
        }
        if (i == 7) {
            dataMap.putByte(str, (byte) itVar.zzbTU);
            return;
        }
        if (i == 8) {
            dataMap.putBoolean(str, itVar.zzbTV);
            return;
        }
        if (i == 13) {
            if (list == null) {
                String strValueOf = String.valueOf(str);
                throw new RuntimeException(strValueOf.length() != 0 ? "populateBundle: unexpected type for: ".concat(strValueOf) : new String("populateBundle: unexpected type for: "));
            }
            dataMap.putAsset(str, list.get((int) itVar.zzbUb));
            return;
        }
        if (i == 9) {
            DataMap dataMap2 = new DataMap();
            for (ir irVar : itVar.zzbTW) {
                zza(list, dataMap2, irVar.name, irVar.zzbTL);
            }
            dataMap.putDataMap(str, dataMap2);
            return;
        }
        if (i != 10) {
            throw new RuntimeException(new StringBuilder(43).append("populateBundle: unexpected type ").append(i).toString());
        }
        int iZza = zza(str, itVar.zzbTX);
        ArrayList<Integer> arrayListZza = zza(list, itVar, iZza);
        if (iZza == 14) {
            dataMap.putStringArrayList(str, arrayListZza);
            return;
        }
        if (iZza == 9) {
            dataMap.putDataMapArrayList(str, arrayListZza);
        } else if (iZza == 2) {
            dataMap.putStringArrayList(str, arrayListZza);
        } else {
            if (iZza != 6) {
                throw new IllegalStateException(new StringBuilder(39).append("Unexpected typeOfArrayList: ").append(iZza).toString());
            }
            dataMap.putIntegerArrayList(str, arrayListZza);
        }
    }

    private static ir[] zza(DataMap dataMap, List<Asset> list) {
        TreeSet treeSet = new TreeSet(dataMap.keySet());
        ir[] irVarArr = new ir[treeSet.size()];
        int i = 0;
        Iterator it = treeSet.iterator();
        while (true) {
            int i2 = i;
            if (!it.hasNext()) {
                return irVarArr;
            }
            String str = (String) it.next();
            Object obj = dataMap.get(str);
            irVarArr[i2] = new ir();
            irVarArr[i2].name = str;
            irVarArr[i2].zzbTL = zza(list, obj);
            i = i2 + 1;
        }
    }
}
