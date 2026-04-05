package com.google.android.gms.common.data;

import android.content.ContentValues;
import android.database.CharArrayBuffer;
import android.database.CursorIndexOutOfBoundsException;
import android.database.CursorWindow;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.google.android.gms.common.annotation.KeepName;
import com.google.android.gms.common.internal.zzbr;
import java.io.Closeable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes3.dex */
@KeepName
public final class DataHolder extends com.google.android.gms.common.internal.safeparcel.zza implements Closeable {
    public static final Parcelable.Creator<DataHolder> CREATOR = new zzf();
    private static final zza zzaFK = new zze(new String[0], null);
    private boolean mClosed;
    private final String[] zzaFD;
    private Bundle zzaFE;
    private final CursorWindow[] zzaFF;
    private final Bundle zzaFG;
    private int[] zzaFH;
    int zzaFI;
    private boolean zzaFJ;
    private int zzakw;
    private final int zzaxw;

    public static class zza {
        private final String[] zzaFD;
        private final ArrayList<HashMap<String, Object>> zzaFL;
        private final String zzaFM;
        private final HashMap<Object, Integer> zzaFN;
        private boolean zzaFO;
        private String zzaFP;

        private zza(String[] strArr, String str) {
            this.zzaFD = (String[]) zzbr.zzu(strArr);
            this.zzaFL = new ArrayList<>();
            this.zzaFM = str;
            this.zzaFN = new HashMap<>();
            this.zzaFO = false;
            this.zzaFP = null;
        }

        /* synthetic */ zza(String[] strArr, String str, zze zzeVar) {
            this(strArr, null);
        }

        public zza zza(ContentValues contentValues) {
            com.google.android.gms.common.internal.zzc.zzr(contentValues);
            HashMap<String, Object> map = new HashMap<>(contentValues.size());
            for (Map.Entry<String, Object> entry : contentValues.valueSet()) {
                map.put(entry.getKey(), entry.getValue());
            }
            return zza(map);
        }

        public zza zza(HashMap<String, Object> map) {
            Object obj;
            int iIntValue;
            com.google.android.gms.common.internal.zzc.zzr(map);
            if (this.zzaFM == null || (obj = map.get(this.zzaFM)) == null) {
                iIntValue = -1;
            } else {
                Integer num = this.zzaFN.get(obj);
                if (num == null) {
                    this.zzaFN.put(obj, Integer.valueOf(this.zzaFL.size()));
                    iIntValue = -1;
                } else {
                    iIntValue = num.intValue();
                }
            }
            if (iIntValue == -1) {
                this.zzaFL.add(map);
            } else {
                this.zzaFL.remove(iIntValue);
                this.zzaFL.add(iIntValue, map);
            }
            this.zzaFO = false;
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public final DataHolder zzav(int i) {
            return new DataHolder(this, 0, (Bundle) null, (zze) (0 == true ? 1 : 0));
        }
    }

    public static class zzb extends RuntimeException {
        public zzb(String str) {
            super(str);
        }
    }

    DataHolder(int i, String[] strArr, CursorWindow[] cursorWindowArr, int i2, Bundle bundle) {
        this.mClosed = false;
        this.zzaFJ = true;
        this.zzakw = i;
        this.zzaFD = strArr;
        this.zzaFF = cursorWindowArr;
        this.zzaxw = i2;
        this.zzaFG = bundle;
    }

    private DataHolder(zza zzaVar, int i, Bundle bundle) {
        this(zzaVar.zzaFD, zza(zzaVar, -1), i, (Bundle) null);
    }

    /* synthetic */ DataHolder(zza zzaVar, int i, Bundle bundle, zze zzeVar) {
        this(zzaVar, 0, null);
    }

    private DataHolder(String[] strArr, CursorWindow[] cursorWindowArr, int i, Bundle bundle) {
        this.mClosed = false;
        this.zzaFJ = true;
        this.zzakw = 1;
        this.zzaFD = (String[]) zzbr.zzu(strArr);
        this.zzaFF = (CursorWindow[]) zzbr.zzu(cursorWindowArr);
        this.zzaxw = i;
        this.zzaFG = bundle;
        zzqP();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static zza zza(String[] strArr) {
        return new zza(strArr, null, 0 == true ? 1 : 0);
    }

    private static CursorWindow[] zza(zza zzaVar, int i) {
        int i2;
        boolean z;
        CursorWindow cursorWindow;
        if (zzaVar.zzaFD.length == 0) {
            return new CursorWindow[0];
        }
        ArrayList arrayList = zzaVar.zzaFL;
        int size = arrayList.size();
        CursorWindow cursorWindow2 = new CursorWindow(false);
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add(cursorWindow2);
        cursorWindow2.setNumColumns(zzaVar.zzaFD.length);
        int i3 = 0;
        boolean z2 = false;
        while (i3 < size) {
            try {
                if (!cursorWindow2.allocRow()) {
                    Log.d("DataHolder", new StringBuilder(72).append("Allocating additional cursor window for large data set (row ").append(i3).append(")").toString());
                    cursorWindow2 = new CursorWindow(false);
                    cursorWindow2.setStartPosition(i3);
                    cursorWindow2.setNumColumns(zzaVar.zzaFD.length);
                    arrayList2.add(cursorWindow2);
                    if (!cursorWindow2.allocRow()) {
                        Log.e("DataHolder", "Unable to allocate row to hold data.");
                        arrayList2.remove(cursorWindow2);
                        return (CursorWindow[]) arrayList2.toArray(new CursorWindow[arrayList2.size()]);
                    }
                }
                Map map = (Map) arrayList.get(i3);
                boolean zPutDouble = true;
                for (int i4 = 0; i4 < zzaVar.zzaFD.length && zPutDouble; i4++) {
                    String str = zzaVar.zzaFD[i4];
                    Object obj = map.get(str);
                    if (obj == null) {
                        zPutDouble = cursorWindow2.putNull(i3, i4);
                    } else if (obj instanceof String) {
                        zPutDouble = cursorWindow2.putString((String) obj, i3, i4);
                    } else if (obj instanceof Long) {
                        zPutDouble = cursorWindow2.putLong(((Long) obj).longValue(), i3, i4);
                    } else if (obj instanceof Integer) {
                        zPutDouble = cursorWindow2.putLong(((Integer) obj).intValue(), i3, i4);
                    } else if (obj instanceof Boolean) {
                        zPutDouble = cursorWindow2.putLong(((Boolean) obj).booleanValue() ? 1L : 0L, i3, i4);
                    } else if (obj instanceof byte[]) {
                        zPutDouble = cursorWindow2.putBlob((byte[]) obj, i3, i4);
                    } else if (obj instanceof Double) {
                        zPutDouble = cursorWindow2.putDouble(((Double) obj).doubleValue(), i3, i4);
                    } else {
                        if (!(obj instanceof Float)) {
                            String strValueOf = String.valueOf(obj);
                            throw new IllegalArgumentException(new StringBuilder(String.valueOf(str).length() + 32 + String.valueOf(strValueOf).length()).append("Unsupported object for column ").append(str).append(": ").append(strValueOf).toString());
                        }
                        zPutDouble = cursorWindow2.putDouble(((Float) obj).floatValue(), i3, i4);
                    }
                }
                if (zPutDouble) {
                    i2 = i3;
                    z = false;
                    cursorWindow = cursorWindow2;
                } else {
                    if (z2) {
                        throw new zzb("Could not add the value to a new CursorWindow. The size of value may be larger than what a CursorWindow can handle.");
                    }
                    Log.d("DataHolder", new StringBuilder(74).append("Couldn't populate window data for row ").append(i3).append(" - allocating new window.").toString());
                    cursorWindow2.freeLastRow();
                    CursorWindow cursorWindow3 = new CursorWindow(false);
                    cursorWindow3.setStartPosition(i3);
                    cursorWindow3.setNumColumns(zzaVar.zzaFD.length);
                    arrayList2.add(cursorWindow3);
                    i2 = i3 - 1;
                    cursorWindow = cursorWindow3;
                    z = true;
                }
                z2 = z;
                cursorWindow2 = cursorWindow;
                i3 = i2 + 1;
            } catch (RuntimeException e) {
                int size2 = arrayList2.size();
                for (int i5 = 0; i5 < size2; i5++) {
                    ((CursorWindow) arrayList2.get(i5)).close();
                }
                throw e;
            }
        }
        return (CursorWindow[]) arrayList2.toArray(new CursorWindow[arrayList2.size()]);
    }

    public static DataHolder zzau(int i) {
        return new DataHolder(zzaFK, i, null);
    }

    private final void zzh(String str, int i) {
        if (this.zzaFE == null || !this.zzaFE.containsKey(str)) {
            String strValueOf = String.valueOf(str);
            throw new IllegalArgumentException(strValueOf.length() != 0 ? "No such column: ".concat(strValueOf) : new String("No such column: "));
        }
        if (isClosed()) {
            throw new IllegalArgumentException("Buffer is closed.");
        }
        if (i < 0 || i >= this.zzaFI) {
            throw new CursorIndexOutOfBoundsException(i, this.zzaFI);
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public final void close() {
        synchronized (this) {
            if (!this.mClosed) {
                this.mClosed = true;
                for (int i = 0; i < this.zzaFF.length; i++) {
                    this.zzaFF[i].close();
                }
            }
        }
    }

    protected final void finalize() throws Throwable {
        try {
            if (this.zzaFJ && this.zzaFF.length > 0 && !isClosed()) {
                close();
                String strValueOf = String.valueOf(toString());
                Log.e("DataBuffer", new StringBuilder(String.valueOf(strValueOf).length() + 178).append("Internal data leak within a DataBuffer object detected!  Be sure to explicitly call release() on all DataBuffer extending objects when you are done with them. (internal object: ").append(strValueOf).append(")").toString());
            }
        } finally {
            super.finalize();
        }
    }

    public final int getCount() {
        return this.zzaFI;
    }

    public final int getStatusCode() {
        return this.zzaxw;
    }

    public final boolean isClosed() {
        boolean z;
        synchronized (this) {
            z = this.mClosed;
        }
        return z;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 1, this.zzaFD, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 2, (Parcelable[]) this.zzaFF, i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 3, this.zzaxw);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 4, this.zzaFG, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 1000, this.zzakw);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }

    public final void zza(String str, int i, int i2, CharArrayBuffer charArrayBuffer) {
        zzh(str, i);
        this.zzaFF[i2].copyStringToBuffer(i, this.zzaFE.getInt(str), charArrayBuffer);
    }

    public final int zzat(int i) {
        int i2 = 0;
        zzbr.zzae(i >= 0 && i < this.zzaFI);
        while (true) {
            if (i2 >= this.zzaFH.length) {
                break;
            }
            if (i < this.zzaFH[i2]) {
                i2--;
                break;
            }
            i2++;
        }
        return i2 == this.zzaFH.length ? i2 - 1 : i2;
    }

    public final long zzb(String str, int i, int i2) {
        zzh(str, i);
        return this.zzaFF[i2].getLong(i, this.zzaFE.getInt(str));
    }

    public final int zzc(String str, int i, int i2) {
        zzh(str, i);
        return this.zzaFF[i2].getInt(i, this.zzaFE.getInt(str));
    }

    public final boolean zzcv(String str) {
        return this.zzaFE.containsKey(str);
    }

    public final String zzd(String str, int i, int i2) {
        zzh(str, i);
        return this.zzaFF[i2].getString(i, this.zzaFE.getInt(str));
    }

    public final boolean zze(String str, int i, int i2) {
        zzh(str, i);
        return Long.valueOf(this.zzaFF[i2].getLong(i, this.zzaFE.getInt(str))).longValue() == 1;
    }

    public final float zzf(String str, int i, int i2) {
        zzh(str, i);
        return this.zzaFF[i2].getFloat(i, this.zzaFE.getInt(str));
    }

    public final byte[] zzg(String str, int i, int i2) {
        zzh(str, i);
        return this.zzaFF[i2].getBlob(i, this.zzaFE.getInt(str));
    }

    public final boolean zzh(String str, int i, int i2) {
        zzh(str, i);
        return this.zzaFF[i2].isNull(i, this.zzaFE.getInt(str));
    }

    public final Bundle zzqL() {
        return this.zzaFG;
    }

    public final void zzqP() {
        this.zzaFE = new Bundle();
        for (int i = 0; i < this.zzaFD.length; i++) {
            this.zzaFE.putInt(this.zzaFD[i], i);
        }
        this.zzaFH = new int[this.zzaFF.length];
        int numRows = 0;
        for (int i2 = 0; i2 < this.zzaFF.length; i2++) {
            this.zzaFH[i2] = numRows;
            numRows += this.zzaFF[i2].getNumRows() - (numRows - this.zzaFF[i2].getStartPosition());
        }
        this.zzaFI = numRows;
    }
}
