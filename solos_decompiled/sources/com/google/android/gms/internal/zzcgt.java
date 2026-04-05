package com.google.android.gms.internal;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteFullException;
import android.os.Build;
import android.os.Parcel;
import android.os.SystemClock;
import android.support.annotation.WorkerThread;
import com.facebook.share.internal.ShareConstants;
import com.kopin.pupil.aria.messages.Messages;

/* JADX INFO: loaded from: classes36.dex */
public final class zzcgt extends zzciv {
    private final zzcgu zzbqJ;
    private boolean zzbqK;

    zzcgt(zzchx zzchxVar) {
        super(zzchxVar);
        this.zzbqJ = new zzcgu(this, super.getContext(), zzcfy.zzxC());
    }

    @WorkerThread
    private final SQLiteDatabase getWritableDatabase() {
        if (this.zzbqK) {
            return null;
        }
        SQLiteDatabase writableDatabase = this.zzbqJ.getWritableDatabase();
        if (writableDatabase != null) {
            return writableDatabase;
        }
        this.zzbqK = true;
        return null;
    }

    @WorkerThread
    @TargetApi(11)
    private final boolean zza(int i, byte[] bArr) {
        super.zzwo();
        super.zzjB();
        if (this.zzbqK) {
            return false;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(ShareConstants.MEDIA_TYPE, Integer.valueOf(i));
        contentValues.put("entry", bArr);
        zzcfy.zzxM();
        int i2 = 5;
        for (int i3 = 0; i3 < 5; i3++) {
            SQLiteDatabase sQLiteDatabase = null;
            Cursor cursor = null;
            try {
                try {
                    SQLiteDatabase writableDatabase = getWritableDatabase();
                    if (writableDatabase == null) {
                        this.zzbqK = true;
                        if (writableDatabase != null) {
                            writableDatabase.close();
                        }
                        return false;
                    }
                    writableDatabase.beginTransaction();
                    long j = 0;
                    Cursor cursorRawQuery = writableDatabase.rawQuery("select count(1) from messages", null);
                    if (cursorRawQuery != null && cursorRawQuery.moveToFirst()) {
                        j = cursorRawQuery.getLong(0);
                    }
                    if (j >= 100000) {
                        super.zzwE().zzyv().log("Data loss, local db full");
                        long j2 = (100000 - j) + 1;
                        long jDelete = writableDatabase.delete(Messages.APP_NAME, "rowid in (select rowid from messages order by rowid asc limit ?)", new String[]{Long.toString(j2)});
                        if (jDelete != j2) {
                            super.zzwE().zzyv().zzd("Different delete count than expected in local db. expected, received, difference", Long.valueOf(j2), Long.valueOf(jDelete), Long.valueOf(j2 - jDelete));
                        }
                    }
                    writableDatabase.insertOrThrow(Messages.APP_NAME, null, contentValues);
                    writableDatabase.setTransactionSuccessful();
                    writableDatabase.endTransaction();
                    if (cursorRawQuery != null) {
                        cursorRawQuery.close();
                    }
                    if (writableDatabase != null) {
                        writableDatabase.close();
                    }
                    return true;
                } catch (SQLiteException e) {
                    if (Build.VERSION.SDK_INT < 11 || !(e instanceof SQLiteDatabaseLockedException)) {
                        if (0 != 0 && sQLiteDatabase.inTransaction()) {
                            sQLiteDatabase.endTransaction();
                        }
                        super.zzwE().zzyv().zzj("Error writing entry to local database", e);
                        this.zzbqK = true;
                    } else {
                        SystemClock.sleep(i2);
                        i2 += 20;
                    }
                    if (0 != 0) {
                        cursor.close();
                    }
                    if (0 != 0) {
                        sQLiteDatabase.close();
                    }
                }
            } catch (SQLiteFullException e2) {
                try {
                    super.zzwE().zzyv().zzj("Error writing entry to local database", e2);
                    this.zzbqK = true;
                    if (0 != 0) {
                        cursor.close();
                    }
                    if (0 != 0) {
                        sQLiteDatabase.close();
                    }
                } catch (Throwable th) {
                    if (0 != 0) {
                        cursor.close();
                    }
                    if (0 != 0) {
                        sQLiteDatabase.close();
                    }
                    throw th;
                }
            }
        }
        super.zzwE().zzyx().log("Failed to write entry to local database");
        return false;
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    public final boolean zza(zzcgl zzcglVar) {
        Parcel parcelObtain = Parcel.obtain();
        zzcglVar.writeToParcel(parcelObtain, 0);
        byte[] bArrMarshall = parcelObtain.marshall();
        parcelObtain.recycle();
        if (bArrMarshall.length <= 131072) {
            return zza(0, bArrMarshall);
        }
        super.zzwE().zzyx().log("Event is too long for local database. Sending event directly to service");
        return false;
    }

    public final boolean zza(zzcku zzckuVar) {
        Parcel parcelObtain = Parcel.obtain();
        zzckuVar.writeToParcel(parcelObtain, 0);
        byte[] bArrMarshall = parcelObtain.marshall();
        parcelObtain.recycle();
        if (bArrMarshall.length <= 131072) {
            return zza(1, bArrMarshall);
        }
        super.zzwE().zzyx().log("User property too long for local database. Sending directly to service");
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:63:0x00fd  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0102  */
    @android.annotation.TargetApi(11)
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.util.List<com.google.android.gms.common.internal.safeparcel.zza> zzbo(int r15) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 542
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcgt.zzbo(int):java.util.List");
    }

    public final boolean zzc(zzcfw zzcfwVar) {
        super.zzwA();
        byte[] bArrZza = zzckx.zza(zzcfwVar);
        if (bArrZza.length <= 131072) {
            return zza(2, bArrZza);
        }
        super.zzwE().zzyx().log("Conditional user property too long for local database. Sending directly to service");
        return false;
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ void zzjB() {
        super.zzjB();
    }

    @Override // com.google.android.gms.internal.zzciv
    protected final void zzjC() {
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ com.google.android.gms.common.util.zzf zzkp() {
        return super.zzkp();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzckx zzwA() {
        return super.zzwA();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzchr zzwB() {
        return super.zzwB();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzckm zzwC() {
        return super.zzwC();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzchs zzwD() {
        return super.zzwD();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcgx zzwE() {
        return super.zzwE();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzchi zzwF() {
        return super.zzwF();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcfy zzwG() {
        return super.zzwG();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ void zzwn() {
        super.zzwn();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ void zzwo() {
        super.zzwo();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ void zzwp() {
        super.zzwp();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcfo zzwq() {
        return super.zzwq();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcfv zzwr() {
        return super.zzwr();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcix zzws() {
        return super.zzws();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcgs zzwt() {
        return super.zzwt();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcgf zzwu() {
        return super.zzwu();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcjp zzwv() {
        return super.zzwv();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcjl zzww() {
        return super.zzww();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcgt zzwx() {
        return super.zzwx();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcfz zzwy() {
        return super.zzwy();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcgv zzwz() {
        return super.zzwz();
    }
}
