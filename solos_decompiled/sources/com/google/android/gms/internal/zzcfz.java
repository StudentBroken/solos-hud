package com.google.android.gms.internal;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.annotation.WorkerThread;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import bolts.MeasurementEvent;
import com.facebook.share.internal.ShareConstants;
import com.google.android.gms.common.internal.zzbr;
import com.google.android.gms.measurement.AppMeasurement;
import com.google.firebase.analytics.FirebaseAnalytics;
import io.fabric.sdk.android.services.common.CommonUtils;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes36.dex */
final class zzcfz extends zzciv {
    private static final Map<String, String> zzbpr;
    private static final Map<String, String> zzbps;
    private static final Map<String, String> zzbpt;
    private static final Map<String, String> zzbpu;
    private static final Map<String, String> zzbpv;
    private final zzcgc zzbpw;
    private final zzckr zzbpx;

    static {
        ArrayMap arrayMap = new ArrayMap(1);
        zzbpr = arrayMap;
        arrayMap.put(FirebaseAnalytics.Param.ORIGIN, "ALTER TABLE user_attributes ADD COLUMN origin TEXT;");
        ArrayMap arrayMap2 = new ArrayMap(18);
        zzbps = arrayMap2;
        arrayMap2.put("app_version", "ALTER TABLE apps ADD COLUMN app_version TEXT;");
        zzbps.put("app_store", "ALTER TABLE apps ADD COLUMN app_store TEXT;");
        zzbps.put("gmp_version", "ALTER TABLE apps ADD COLUMN gmp_version INTEGER;");
        zzbps.put("dev_cert_hash", "ALTER TABLE apps ADD COLUMN dev_cert_hash INTEGER;");
        zzbps.put("measurement_enabled", "ALTER TABLE apps ADD COLUMN measurement_enabled INTEGER;");
        zzbps.put("last_bundle_start_timestamp", "ALTER TABLE apps ADD COLUMN last_bundle_start_timestamp INTEGER;");
        zzbps.put("day", "ALTER TABLE apps ADD COLUMN day INTEGER;");
        zzbps.put("daily_public_events_count", "ALTER TABLE apps ADD COLUMN daily_public_events_count INTEGER;");
        zzbps.put("daily_events_count", "ALTER TABLE apps ADD COLUMN daily_events_count INTEGER;");
        zzbps.put("daily_conversions_count", "ALTER TABLE apps ADD COLUMN daily_conversions_count INTEGER;");
        zzbps.put("remote_config", "ALTER TABLE apps ADD COLUMN remote_config BLOB;");
        zzbps.put("config_fetched_time", "ALTER TABLE apps ADD COLUMN config_fetched_time INTEGER;");
        zzbps.put("failed_config_fetch_time", "ALTER TABLE apps ADD COLUMN failed_config_fetch_time INTEGER;");
        zzbps.put("app_version_int", "ALTER TABLE apps ADD COLUMN app_version_int INTEGER;");
        zzbps.put("firebase_instance_id", "ALTER TABLE apps ADD COLUMN firebase_instance_id TEXT;");
        zzbps.put("daily_error_events_count", "ALTER TABLE apps ADD COLUMN daily_error_events_count INTEGER;");
        zzbps.put("daily_realtime_events_count", "ALTER TABLE apps ADD COLUMN daily_realtime_events_count INTEGER;");
        zzbps.put("health_monitor_sample", "ALTER TABLE apps ADD COLUMN health_monitor_sample TEXT;");
        zzbps.put("android_id", "ALTER TABLE apps ADD COLUMN android_id INTEGER;");
        ArrayMap arrayMap3 = new ArrayMap(1);
        zzbpt = arrayMap3;
        arrayMap3.put("realtime", "ALTER TABLE raw_events ADD COLUMN realtime INTEGER;");
        ArrayMap arrayMap4 = new ArrayMap(1);
        zzbpu = arrayMap4;
        arrayMap4.put("has_realtime", "ALTER TABLE queue ADD COLUMN has_realtime INTEGER;");
        ArrayMap arrayMap5 = new ArrayMap(1);
        zzbpv = arrayMap5;
        arrayMap5.put("previous_install_count", "ALTER TABLE app2 ADD COLUMN previous_install_count INTEGER;");
    }

    zzcfz(zzchx zzchxVar) {
        super(zzchxVar);
        this.zzbpx = new zzckr(zzkp());
        this.zzbpw = new zzcgc(this, getContext(), zzcfy.zzxB());
    }

    @WorkerThread
    private final long zza(String str, String[] strArr, long j) {
        Cursor cursorRawQuery = null;
        try {
            try {
                cursorRawQuery = getWritableDatabase().rawQuery(str, strArr);
                if (cursorRawQuery.moveToFirst()) {
                    j = cursorRawQuery.getLong(0);
                } else if (cursorRawQuery != null) {
                    cursorRawQuery.close();
                }
                return j;
            } catch (SQLiteException e) {
                zzwE().zzyv().zze("Database error", str, e);
                throw e;
            }
        } finally {
            if (cursorRawQuery != null) {
                cursorRawQuery.close();
            }
        }
    }

    @WorkerThread
    private final Object zza(Cursor cursor, int i) {
        int type = cursor.getType(i);
        switch (type) {
            case 0:
                zzwE().zzyv().log("Loaded invalid null value from database");
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                zzwE().zzyv().log("Loaded invalid blob type value, ignoring it");
                break;
            default:
                zzwE().zzyv().zzj("Loaded invalid unknown value type, ignoring it", Integer.valueOf(type));
                break;
        }
        return null;
    }

    @WorkerThread
    private static void zza(ContentValues contentValues, String str, Object obj) {
        zzbr.zzcF(str);
        zzbr.zzu(obj);
        if (obj instanceof String) {
            contentValues.put(str, (String) obj);
        } else if (obj instanceof Long) {
            contentValues.put(str, (Long) obj);
        } else {
            if (!(obj instanceof Double)) {
                throw new IllegalArgumentException("Invalid value type");
            }
            contentValues.put(str, (Double) obj);
        }
    }

    static void zza(zzcgx zzcgxVar, SQLiteDatabase sQLiteDatabase) {
        if (zzcgxVar == null) {
            throw new IllegalArgumentException("Monitor must not be null");
        }
        File file = new File(sQLiteDatabase.getPath());
        if (!file.setReadable(false, false)) {
            zzcgxVar.zzyx().log("Failed to turn off database read permission");
        }
        if (!file.setWritable(false, false)) {
            zzcgxVar.zzyx().log("Failed to turn off database write permission");
        }
        if (!file.setReadable(true, true)) {
            zzcgxVar.zzyx().log("Failed to turn on database read permission for owner");
        }
        if (file.setWritable(true, true)) {
            return;
        }
        zzcgxVar.zzyx().log("Failed to turn on database write permission for owner");
    }

    @WorkerThread
    static void zza(zzcgx zzcgxVar, SQLiteDatabase sQLiteDatabase, String str, String str2, String str3, Map<String, String> map) throws SQLiteException {
        if (zzcgxVar == null) {
            throw new IllegalArgumentException("Monitor must not be null");
        }
        if (!zza(zzcgxVar, sQLiteDatabase, str)) {
            sQLiteDatabase.execSQL(str2);
        }
        try {
            zza(zzcgxVar, sQLiteDatabase, str, str3, map);
        } catch (SQLiteException e) {
            zzcgxVar.zzyv().zzj("Failed to verify columns on table that was just created", str);
            throw e;
        }
    }

    @WorkerThread
    private static void zza(zzcgx zzcgxVar, SQLiteDatabase sQLiteDatabase, String str, String str2, Map<String, String> map) throws SQLiteException {
        if (zzcgxVar == null) {
            throw new IllegalArgumentException("Monitor must not be null");
        }
        Set<String> setZzb = zzb(sQLiteDatabase, str);
        for (String str3 : str2.split(",")) {
            if (!setZzb.remove(str3)) {
                throw new SQLiteException(new StringBuilder(String.valueOf(str).length() + 35 + String.valueOf(str3).length()).append("Table ").append(str).append(" is missing required column: ").append(str3).toString());
            }
        }
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (!setZzb.remove(entry.getKey())) {
                    sQLiteDatabase.execSQL(entry.getValue());
                }
            }
        }
        if (setZzb.isEmpty()) {
            return;
        }
        zzcgxVar.zzyx().zze("Table has extra columns. table, columns", str, TextUtils.join(", ", setZzb));
    }

    @WorkerThread
    private static boolean zza(zzcgx zzcgxVar, SQLiteDatabase sQLiteDatabase, String str) throws Throwable {
        Cursor cursorQuery;
        boolean zMoveToFirst;
        Cursor cursor = null;
        try {
            if (zzcgxVar == null) {
                throw new IllegalArgumentException("Monitor must not be null");
            }
            try {
                cursorQuery = sQLiteDatabase.query("SQLITE_MASTER", new String[]{"name"}, "name=?", new String[]{str}, null, null, null);
                try {
                    zMoveToFirst = cursorQuery.moveToFirst();
                    if (cursorQuery != null) {
                        cursorQuery.close();
                    }
                } catch (SQLiteException e) {
                    e = e;
                    zzcgxVar.zzyx().zze("Error querying for table", str, e);
                    if (cursorQuery != null) {
                        cursorQuery.close();
                    }
                    zMoveToFirst = false;
                }
            } catch (SQLiteException e2) {
                e = e2;
                cursorQuery = null;
            } catch (Throwable th) {
                th = th;
            }
            return zMoveToFirst;
        } catch (Throwable th2) {
            th = th2;
            cursor = cursorQuery;
        }
        if (cursor != null) {
            cursor.close();
        }
        throw th;
    }

    @WorkerThread
    private final boolean zza(String str, int i, zzckz zzckzVar) {
        zzkC();
        zzjB();
        zzbr.zzcF(str);
        zzbr.zzu(zzckzVar);
        if (TextUtils.isEmpty(zzckzVar.zzbuR)) {
            zzwE().zzyx().zzd("Event filter had no event name. Audience definition ignored. appId, audienceId, filterId", zzcgx.zzea(str), Integer.valueOf(i), String.valueOf(zzckzVar.zzbuQ));
            return false;
        }
        try {
            byte[] bArr = new byte[zzckzVar.zzMl()];
            ahx ahxVarZzc = ahx.zzc(bArr, 0, bArr.length);
            zzckzVar.zza(ahxVarZzc);
            ahxVarZzc.zzMc();
            ContentValues contentValues = new ContentValues();
            contentValues.put("app_id", str);
            contentValues.put("audience_id", Integer.valueOf(i));
            contentValues.put("filter_id", zzckzVar.zzbuQ);
            contentValues.put(MeasurementEvent.MEASUREMENT_EVENT_NAME_KEY, zzckzVar.zzbuR);
            contentValues.put(ShareConstants.WEB_DIALOG_PARAM_DATA, bArr);
            try {
                if (getWritableDatabase().insertWithOnConflict("event_filters", null, contentValues, 5) == -1) {
                    zzwE().zzyv().zzj("Failed to insert event filter (got -1). appId", zzcgx.zzea(str));
                }
                return true;
            } catch (SQLiteException e) {
                zzwE().zzyv().zze("Error storing event filter. appId", zzcgx.zzea(str), e);
                return false;
            }
        } catch (IOException e2) {
            zzwE().zzyv().zze("Configuration loss. Failed to serialize event filter. appId", zzcgx.zzea(str), e2);
            return false;
        }
    }

    @WorkerThread
    private final boolean zza(String str, int i, zzclc zzclcVar) {
        zzkC();
        zzjB();
        zzbr.zzcF(str);
        zzbr.zzu(zzclcVar);
        if (TextUtils.isEmpty(zzclcVar.zzbvg)) {
            zzwE().zzyx().zzd("Property filter had no property name. Audience definition ignored. appId, audienceId, filterId", zzcgx.zzea(str), Integer.valueOf(i), String.valueOf(zzclcVar.zzbuQ));
            return false;
        }
        try {
            byte[] bArr = new byte[zzclcVar.zzMl()];
            ahx ahxVarZzc = ahx.zzc(bArr, 0, bArr.length);
            zzclcVar.zza(ahxVarZzc);
            ahxVarZzc.zzMc();
            ContentValues contentValues = new ContentValues();
            contentValues.put("app_id", str);
            contentValues.put("audience_id", Integer.valueOf(i));
            contentValues.put("filter_id", zzclcVar.zzbuQ);
            contentValues.put("property_name", zzclcVar.zzbvg);
            contentValues.put(ShareConstants.WEB_DIALOG_PARAM_DATA, bArr);
            try {
                if (getWritableDatabase().insertWithOnConflict("property_filters", null, contentValues, 5) != -1) {
                    return true;
                }
                zzwE().zzyv().zzj("Failed to insert property filter (got -1). appId", zzcgx.zzea(str));
                return false;
            } catch (SQLiteException e) {
                zzwE().zzyv().zze("Error storing property filter. appId", zzcgx.zzea(str), e);
                return false;
            }
        } catch (IOException e2) {
            zzwE().zzyv().zze("Configuration loss. Failed to serialize property filter. appId", zzcgx.zzea(str), e2);
            return false;
        }
    }

    @WorkerThread
    private final long zzb(String str, String[] strArr) {
        Cursor cursor = null;
        try {
            try {
                Cursor cursorRawQuery = getWritableDatabase().rawQuery(str, strArr);
                if (!cursorRawQuery.moveToFirst()) {
                    throw new SQLiteException("Database returned empty set");
                }
                long j = cursorRawQuery.getLong(0);
                if (cursorRawQuery != null) {
                    cursorRawQuery.close();
                }
                return j;
            } catch (SQLiteException e) {
                zzwE().zzyv().zze("Database error", str, e);
                throw e;
            }
        } catch (Throwable th) {
            if (0 != 0) {
                cursor.close();
            }
            throw th;
        }
    }

    @WorkerThread
    private static Set<String> zzb(SQLiteDatabase sQLiteDatabase, String str) {
        HashSet hashSet = new HashSet();
        Cursor cursorRawQuery = sQLiteDatabase.rawQuery(new StringBuilder(String.valueOf(str).length() + 22).append("SELECT * FROM ").append(str).append(" LIMIT 0").toString(), null);
        try {
            Collections.addAll(hashSet, cursorRawQuery.getColumnNames());
            return hashSet;
        } finally {
            cursorRawQuery.close();
        }
    }

    private final boolean zzc(String str, List<Integer> list) {
        zzbr.zzcF(str);
        zzkC();
        zzjB();
        SQLiteDatabase writableDatabase = getWritableDatabase();
        try {
            long jZzb = zzb("select count(1) from audience_filter_values where app_id=?", new String[]{str});
            int iMax = Math.max(0, Math.min(2000, zzwG().zzb(str, zzcgn.zzbqE)));
            if (jZzb <= iMax) {
                return false;
            }
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                Integer num = list.get(i);
                if (num == null || !(num instanceof Integer)) {
                    return false;
                }
                arrayList.add(Integer.toString(num.intValue()));
            }
            String strValueOf = String.valueOf(TextUtils.join(",", arrayList));
            String string = new StringBuilder(String.valueOf(strValueOf).length() + 2).append("(").append(strValueOf).append(")").toString();
            return writableDatabase.delete("audience_filter_values", new StringBuilder(String.valueOf(string).length() + 140).append("audience_id in (select audience_id from audience_filter_values where app_id=? and audience_id not in ").append(string).append(" order by rowid desc limit -1 offset ?)").toString(), new String[]{str, Integer.toString(iMax)}) > 0;
        } catch (SQLiteException e) {
            zzwE().zzyv().zze("Database error querying filters. appId", zzcgx.zzea(str), e);
            return false;
        }
    }

    private final boolean zzyj() {
        return getContext().getDatabasePath(zzcfy.zzxB()).exists();
    }

    @WorkerThread
    public final void beginTransaction() {
        zzkC();
        getWritableDatabase().beginTransaction();
    }

    @WorkerThread
    public final void endTransaction() {
        zzkC();
        getWritableDatabase().endTransaction();
    }

    @WorkerThread
    final SQLiteDatabase getWritableDatabase() {
        zzjB();
        try {
            return this.zzbpw.getWritableDatabase();
        } catch (SQLiteException e) {
            zzwE().zzyx().zzj("Error opening database", e);
            throw e;
        }
    }

    @WorkerThread
    public final void setTransactionSuccessful() {
        zzkC();
        getWritableDatabase().setTransactionSuccessful();
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x009c  */
    @android.support.annotation.WorkerThread
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final com.google.android.gms.internal.zzcgh zzE(java.lang.String r13, java.lang.String r14) {
        /*
            r12 = this;
            r10 = 0
            com.google.android.gms.common.internal.zzbr.zzcF(r13)
            com.google.android.gms.common.internal.zzbr.zzcF(r14)
            r12.zzjB()
            r12.zzkC()
            android.database.sqlite.SQLiteDatabase r0 = r12.getWritableDatabase()     // Catch: android.database.sqlite.SQLiteException -> L77 java.lang.Throwable -> L99
            java.lang.String r1 = "events"
            r2 = 3
            java.lang.String[] r2 = new java.lang.String[r2]     // Catch: android.database.sqlite.SQLiteException -> L77 java.lang.Throwable -> L99
            r3 = 0
            java.lang.String r4 = "lifetime_count"
            r2[r3] = r4     // Catch: android.database.sqlite.SQLiteException -> L77 java.lang.Throwable -> L99
            r3 = 1
            java.lang.String r4 = "current_bundle_count"
            r2[r3] = r4     // Catch: android.database.sqlite.SQLiteException -> L77 java.lang.Throwable -> L99
            r3 = 2
            java.lang.String r4 = "last_fire_timestamp"
            r2[r3] = r4     // Catch: android.database.sqlite.SQLiteException -> L77 java.lang.Throwable -> L99
            java.lang.String r3 = "app_id=? and name=?"
            r4 = 2
            java.lang.String[] r4 = new java.lang.String[r4]     // Catch: android.database.sqlite.SQLiteException -> L77 java.lang.Throwable -> L99
            r5 = 0
            r4[r5] = r13     // Catch: android.database.sqlite.SQLiteException -> L77 java.lang.Throwable -> L99
            r5 = 1
            r4[r5] = r14     // Catch: android.database.sqlite.SQLiteException -> L77 java.lang.Throwable -> L99
            r5 = 0
            r6 = 0
            r7 = 0
            android.database.Cursor r11 = r0.query(r1, r2, r3, r4, r5, r6, r7)     // Catch: android.database.sqlite.SQLiteException -> L77 java.lang.Throwable -> L99
            boolean r0 = r11.moveToFirst()     // Catch: java.lang.Throwable -> La0 android.database.sqlite.SQLiteException -> La6
            if (r0 != 0) goto L44
            if (r11 == 0) goto L42
            r11.close()
        L42:
            r1 = r10
        L43:
            return r1
        L44:
            r0 = 0
            long r4 = r11.getLong(r0)     // Catch: java.lang.Throwable -> La0 android.database.sqlite.SQLiteException -> La6
            r0 = 1
            long r6 = r11.getLong(r0)     // Catch: java.lang.Throwable -> La0 android.database.sqlite.SQLiteException -> La6
            r0 = 2
            long r8 = r11.getLong(r0)     // Catch: java.lang.Throwable -> La0 android.database.sqlite.SQLiteException -> La6
            com.google.android.gms.internal.zzcgh r1 = new com.google.android.gms.internal.zzcgh     // Catch: java.lang.Throwable -> La0 android.database.sqlite.SQLiteException -> La6
            r2 = r13
            r3 = r14
            r1.<init>(r2, r3, r4, r6, r8)     // Catch: java.lang.Throwable -> La0 android.database.sqlite.SQLiteException -> La6
            boolean r0 = r11.moveToNext()     // Catch: java.lang.Throwable -> La0 android.database.sqlite.SQLiteException -> La6
            if (r0 == 0) goto L71
            com.google.android.gms.internal.zzcgx r0 = r12.zzwE()     // Catch: java.lang.Throwable -> La0 android.database.sqlite.SQLiteException -> La6
            com.google.android.gms.internal.zzcgz r0 = r0.zzyv()     // Catch: java.lang.Throwable -> La0 android.database.sqlite.SQLiteException -> La6
            java.lang.String r2 = "Got multiple records for event aggregates, expected one. appId"
            java.lang.Object r3 = com.google.android.gms.internal.zzcgx.zzea(r13)     // Catch: java.lang.Throwable -> La0 android.database.sqlite.SQLiteException -> La6
            r0.zzj(r2, r3)     // Catch: java.lang.Throwable -> La0 android.database.sqlite.SQLiteException -> La6
        L71:
            if (r11 == 0) goto L43
            r11.close()
            goto L43
        L77:
            r0 = move-exception
            r1 = r10
        L79:
            com.google.android.gms.internal.zzcgx r2 = r12.zzwE()     // Catch: java.lang.Throwable -> La3
            com.google.android.gms.internal.zzcgz r2 = r2.zzyv()     // Catch: java.lang.Throwable -> La3
            java.lang.String r3 = "Error querying events. appId"
            java.lang.Object r4 = com.google.android.gms.internal.zzcgx.zzea(r13)     // Catch: java.lang.Throwable -> La3
            com.google.android.gms.internal.zzcgv r5 = r12.zzwz()     // Catch: java.lang.Throwable -> La3
            java.lang.String r5 = r5.zzdX(r14)     // Catch: java.lang.Throwable -> La3
            r2.zzd(r3, r4, r5, r0)     // Catch: java.lang.Throwable -> La3
            if (r1 == 0) goto L97
            r1.close()
        L97:
            r1 = r10
            goto L43
        L99:
            r0 = move-exception
        L9a:
            if (r10 == 0) goto L9f
            r10.close()
        L9f:
            throw r0
        La0:
            r0 = move-exception
            r10 = r11
            goto L9a
        La3:
            r0 = move-exception
            r10 = r1
            goto L9a
        La6:
            r0 = move-exception
            r1 = r11
            goto L79
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcfz.zzE(java.lang.String, java.lang.String):com.google.android.gms.internal.zzcgh");
    }

    @WorkerThread
    public final void zzF(String str, String str2) {
        zzbr.zzcF(str);
        zzbr.zzcF(str2);
        zzjB();
        zzkC();
        try {
            zzwE().zzyB().zzj("Deleted user attribute rows", Integer.valueOf(getWritableDatabase().delete("user_attributes", "app_id=? and name=?", new String[]{str, str2})));
        } catch (SQLiteException e) {
            zzwE().zzyv().zzd("Error deleting user attribute. appId", zzcgx.zzea(str), zzwz().zzdZ(str2), e);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x009c  */
    @android.support.annotation.WorkerThread
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final com.google.android.gms.internal.zzckw zzG(java.lang.String r10, java.lang.String r11) {
        /*
            r9 = this;
            r8 = 0
            com.google.android.gms.common.internal.zzbr.zzcF(r10)
            com.google.android.gms.common.internal.zzbr.zzcF(r11)
            r9.zzjB()
            r9.zzkC()
            android.database.sqlite.SQLiteDatabase r0 = r9.getWritableDatabase()     // Catch: android.database.sqlite.SQLiteException -> L77 java.lang.Throwable -> L99
            java.lang.String r1 = "user_attributes"
            r2 = 3
            java.lang.String[] r2 = new java.lang.String[r2]     // Catch: android.database.sqlite.SQLiteException -> L77 java.lang.Throwable -> L99
            r3 = 0
            java.lang.String r4 = "set_timestamp"
            r2[r3] = r4     // Catch: android.database.sqlite.SQLiteException -> L77 java.lang.Throwable -> L99
            r3 = 1
            java.lang.String r4 = "value"
            r2[r3] = r4     // Catch: android.database.sqlite.SQLiteException -> L77 java.lang.Throwable -> L99
            r3 = 2
            java.lang.String r4 = "origin"
            r2[r3] = r4     // Catch: android.database.sqlite.SQLiteException -> L77 java.lang.Throwable -> L99
            java.lang.String r3 = "app_id=? and name=?"
            r4 = 2
            java.lang.String[] r4 = new java.lang.String[r4]     // Catch: android.database.sqlite.SQLiteException -> L77 java.lang.Throwable -> L99
            r5 = 0
            r4[r5] = r10     // Catch: android.database.sqlite.SQLiteException -> L77 java.lang.Throwable -> L99
            r5 = 1
            r4[r5] = r11     // Catch: android.database.sqlite.SQLiteException -> L77 java.lang.Throwable -> L99
            r5 = 0
            r6 = 0
            r7 = 0
            android.database.Cursor r7 = r0.query(r1, r2, r3, r4, r5, r6, r7)     // Catch: android.database.sqlite.SQLiteException -> L77 java.lang.Throwable -> L99
            boolean r0 = r7.moveToFirst()     // Catch: java.lang.Throwable -> La0 android.database.sqlite.SQLiteException -> La6
            if (r0 != 0) goto L44
            if (r7 == 0) goto L42
            r7.close()
        L42:
            r0 = r8
        L43:
            return r0
        L44:
            r0 = 0
            long r4 = r7.getLong(r0)     // Catch: java.lang.Throwable -> La0 android.database.sqlite.SQLiteException -> La6
            r0 = 1
            java.lang.Object r6 = r9.zza(r7, r0)     // Catch: java.lang.Throwable -> La0 android.database.sqlite.SQLiteException -> La6
            r0 = 2
            java.lang.String r2 = r7.getString(r0)     // Catch: java.lang.Throwable -> La0 android.database.sqlite.SQLiteException -> La6
            com.google.android.gms.internal.zzckw r0 = new com.google.android.gms.internal.zzckw     // Catch: java.lang.Throwable -> La0 android.database.sqlite.SQLiteException -> La6
            r1 = r10
            r3 = r11
            r0.<init>(r1, r2, r3, r4, r6)     // Catch: java.lang.Throwable -> La0 android.database.sqlite.SQLiteException -> La6
            boolean r1 = r7.moveToNext()     // Catch: java.lang.Throwable -> La0 android.database.sqlite.SQLiteException -> La6
            if (r1 == 0) goto L71
            com.google.android.gms.internal.zzcgx r1 = r9.zzwE()     // Catch: java.lang.Throwable -> La0 android.database.sqlite.SQLiteException -> La6
            com.google.android.gms.internal.zzcgz r1 = r1.zzyv()     // Catch: java.lang.Throwable -> La0 android.database.sqlite.SQLiteException -> La6
            java.lang.String r2 = "Got multiple records for user property, expected one. appId"
            java.lang.Object r3 = com.google.android.gms.internal.zzcgx.zzea(r10)     // Catch: java.lang.Throwable -> La0 android.database.sqlite.SQLiteException -> La6
            r1.zzj(r2, r3)     // Catch: java.lang.Throwable -> La0 android.database.sqlite.SQLiteException -> La6
        L71:
            if (r7 == 0) goto L43
            r7.close()
            goto L43
        L77:
            r0 = move-exception
            r1 = r8
        L79:
            com.google.android.gms.internal.zzcgx r2 = r9.zzwE()     // Catch: java.lang.Throwable -> La3
            com.google.android.gms.internal.zzcgz r2 = r2.zzyv()     // Catch: java.lang.Throwable -> La3
            java.lang.String r3 = "Error querying user property. appId"
            java.lang.Object r4 = com.google.android.gms.internal.zzcgx.zzea(r10)     // Catch: java.lang.Throwable -> La3
            com.google.android.gms.internal.zzcgv r5 = r9.zzwz()     // Catch: java.lang.Throwable -> La3
            java.lang.String r5 = r5.zzdZ(r11)     // Catch: java.lang.Throwable -> La3
            r2.zzd(r3, r4, r5, r0)     // Catch: java.lang.Throwable -> La3
            if (r1 == 0) goto L97
            r1.close()
        L97:
            r0 = r8
            goto L43
        L99:
            r0 = move-exception
        L9a:
            if (r8 == 0) goto L9f
            r8.close()
        L9f:
            throw r0
        La0:
            r0 = move-exception
            r8 = r7
            goto L9a
        La3:
            r0 = move-exception
            r8 = r1
            goto L9a
        La6:
            r0 = move-exception
            r1 = r7
            goto L79
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcfz.zzG(java.lang.String, java.lang.String):com.google.android.gms.internal.zzckw");
    }

    public final void zzG(List<Long> list) {
        zzbr.zzu(list);
        zzjB();
        zzkC();
        StringBuilder sb = new StringBuilder("rowid in (");
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= list.size()) {
                break;
            }
            if (i2 != 0) {
                sb.append(",");
            }
            sb.append(list.get(i2).longValue());
            i = i2 + 1;
        }
        sb.append(")");
        int iDelete = getWritableDatabase().delete("raw_events", sb.toString(), null);
        if (iDelete != list.size()) {
            zzwE().zzyv().zze("Deleted fewer rows from raw events table than expected", Integer.valueOf(iDelete), Integer.valueOf(list.size()));
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x014d  */
    @android.support.annotation.WorkerThread
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final com.google.android.gms.internal.zzcfw zzH(java.lang.String r22, java.lang.String r23) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 347
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcfz.zzH(java.lang.String, java.lang.String):com.google.android.gms.internal.zzcfw");
    }

    @WorkerThread
    public final int zzI(String str, String str2) {
        zzbr.zzcF(str);
        zzbr.zzcF(str2);
        zzjB();
        zzkC();
        try {
            return getWritableDatabase().delete("conditional_properties", "app_id=? and name=?", new String[]{str, str2});
        } catch (SQLiteException e) {
            zzwE().zzyv().zzd("Error deleting conditional property", zzcgx.zzea(str), zzwz().zzdZ(str2), e);
            return 0;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:34:0x00b7  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    final java.util.Map<java.lang.Integer, java.util.List<com.google.android.gms.internal.zzckz>> zzJ(java.lang.String r11, java.lang.String r12) {
        /*
            r10 = this;
            r9 = 0
            r10.zzkC()
            r10.zzjB()
            com.google.android.gms.common.internal.zzbr.zzcF(r11)
            com.google.android.gms.common.internal.zzbr.zzcF(r12)
            android.support.v4.util.ArrayMap r8 = new android.support.v4.util.ArrayMap
            r8.<init>()
            android.database.sqlite.SQLiteDatabase r0 = r10.getWritableDatabase()
            java.lang.String r1 = "event_filters"
            r2 = 2
            java.lang.String[] r2 = new java.lang.String[r2]     // Catch: java.lang.Throwable -> Lb3 android.database.sqlite.SQLiteException -> Lbd
            r3 = 0
            java.lang.String r4 = "audience_id"
            r2[r3] = r4     // Catch: java.lang.Throwable -> Lb3 android.database.sqlite.SQLiteException -> Lbd
            r3 = 1
            java.lang.String r4 = "data"
            r2[r3] = r4     // Catch: java.lang.Throwable -> Lb3 android.database.sqlite.SQLiteException -> Lbd
            java.lang.String r3 = "app_id=? AND event_name=?"
            r4 = 2
            java.lang.String[] r4 = new java.lang.String[r4]     // Catch: java.lang.Throwable -> Lb3 android.database.sqlite.SQLiteException -> Lbd
            r5 = 0
            r4[r5] = r11     // Catch: java.lang.Throwable -> Lb3 android.database.sqlite.SQLiteException -> Lbd
            r5 = 1
            r4[r5] = r12     // Catch: java.lang.Throwable -> Lb3 android.database.sqlite.SQLiteException -> Lbd
            r5 = 0
            r6 = 0
            r7 = 0
            android.database.Cursor r1 = r0.query(r1, r2, r3, r4, r5, r6, r7)     // Catch: java.lang.Throwable -> Lb3 android.database.sqlite.SQLiteException -> Lbd
            boolean r0 = r1.moveToFirst()     // Catch: android.database.sqlite.SQLiteException -> L9a java.lang.Throwable -> Lbb
            if (r0 != 0) goto L47
            java.util.Map r0 = java.util.Collections.emptyMap()     // Catch: android.database.sqlite.SQLiteException -> L9a java.lang.Throwable -> Lbb
            if (r1 == 0) goto L46
            r1.close()
        L46:
            return r0
        L47:
            r0 = 1
            byte[] r0 = r1.getBlob(r0)     // Catch: android.database.sqlite.SQLiteException -> L9a java.lang.Throwable -> Lbb
            r2 = 0
            int r3 = r0.length     // Catch: android.database.sqlite.SQLiteException -> L9a java.lang.Throwable -> Lbb
            com.google.android.gms.internal.ahw r0 = com.google.android.gms.internal.ahw.zzb(r0, r2, r3)     // Catch: android.database.sqlite.SQLiteException -> L9a java.lang.Throwable -> Lbb
            com.google.android.gms.internal.zzckz r2 = new com.google.android.gms.internal.zzckz     // Catch: android.database.sqlite.SQLiteException -> L9a java.lang.Throwable -> Lbb
            r2.<init>()     // Catch: android.database.sqlite.SQLiteException -> L9a java.lang.Throwable -> Lbb
            r2.zza(r0)     // Catch: java.io.IOException -> L87 android.database.sqlite.SQLiteException -> L9a java.lang.Throwable -> Lbb
            r0 = 0
            int r3 = r1.getInt(r0)     // Catch: android.database.sqlite.SQLiteException -> L9a java.lang.Throwable -> Lbb
            java.lang.Integer r0 = java.lang.Integer.valueOf(r3)     // Catch: android.database.sqlite.SQLiteException -> L9a java.lang.Throwable -> Lbb
            java.lang.Object r0 = r8.get(r0)     // Catch: android.database.sqlite.SQLiteException -> L9a java.lang.Throwable -> Lbb
            java.util.List r0 = (java.util.List) r0     // Catch: android.database.sqlite.SQLiteException -> L9a java.lang.Throwable -> Lbb
            if (r0 != 0) goto L77
            java.util.ArrayList r0 = new java.util.ArrayList     // Catch: android.database.sqlite.SQLiteException -> L9a java.lang.Throwable -> Lbb
            r0.<init>()     // Catch: android.database.sqlite.SQLiteException -> L9a java.lang.Throwable -> Lbb
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch: android.database.sqlite.SQLiteException -> L9a java.lang.Throwable -> Lbb
            r8.put(r3, r0)     // Catch: android.database.sqlite.SQLiteException -> L9a java.lang.Throwable -> Lbb
        L77:
            r0.add(r2)     // Catch: android.database.sqlite.SQLiteException -> L9a java.lang.Throwable -> Lbb
        L7a:
            boolean r0 = r1.moveToNext()     // Catch: android.database.sqlite.SQLiteException -> L9a java.lang.Throwable -> Lbb
            if (r0 != 0) goto L47
            if (r1 == 0) goto L85
            r1.close()
        L85:
            r0 = r8
            goto L46
        L87:
            r0 = move-exception
            com.google.android.gms.internal.zzcgx r2 = r10.zzwE()     // Catch: android.database.sqlite.SQLiteException -> L9a java.lang.Throwable -> Lbb
            com.google.android.gms.internal.zzcgz r2 = r2.zzyv()     // Catch: android.database.sqlite.SQLiteException -> L9a java.lang.Throwable -> Lbb
            java.lang.String r3 = "Failed to merge filter. appId"
            java.lang.Object r4 = com.google.android.gms.internal.zzcgx.zzea(r11)     // Catch: android.database.sqlite.SQLiteException -> L9a java.lang.Throwable -> Lbb
            r2.zze(r3, r4, r0)     // Catch: android.database.sqlite.SQLiteException -> L9a java.lang.Throwable -> Lbb
            goto L7a
        L9a:
            r0 = move-exception
        L9b:
            com.google.android.gms.internal.zzcgx r2 = r10.zzwE()     // Catch: java.lang.Throwable -> Lbb
            com.google.android.gms.internal.zzcgz r2 = r2.zzyv()     // Catch: java.lang.Throwable -> Lbb
            java.lang.String r3 = "Database error querying filters. appId"
            java.lang.Object r4 = com.google.android.gms.internal.zzcgx.zzea(r11)     // Catch: java.lang.Throwable -> Lbb
            r2.zze(r3, r4, r0)     // Catch: java.lang.Throwable -> Lbb
            if (r1 == 0) goto Lb1
            r1.close()
        Lb1:
            r0 = r9
            goto L46
        Lb3:
            r0 = move-exception
            r1 = r9
        Lb5:
            if (r1 == 0) goto Lba
            r1.close()
        Lba:
            throw r0
        Lbb:
            r0 = move-exception
            goto Lb5
        Lbd:
            r0 = move-exception
            r1 = r9
            goto L9b
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcfz.zzJ(java.lang.String, java.lang.String):java.util.Map");
    }

    /* JADX WARN: Removed duplicated region for block: B:34:0x00b7  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    final java.util.Map<java.lang.Integer, java.util.List<com.google.android.gms.internal.zzclc>> zzK(java.lang.String r11, java.lang.String r12) {
        /*
            r10 = this;
            r9 = 0
            r10.zzkC()
            r10.zzjB()
            com.google.android.gms.common.internal.zzbr.zzcF(r11)
            com.google.android.gms.common.internal.zzbr.zzcF(r12)
            android.support.v4.util.ArrayMap r8 = new android.support.v4.util.ArrayMap
            r8.<init>()
            android.database.sqlite.SQLiteDatabase r0 = r10.getWritableDatabase()
            java.lang.String r1 = "property_filters"
            r2 = 2
            java.lang.String[] r2 = new java.lang.String[r2]     // Catch: java.lang.Throwable -> Lb3 android.database.sqlite.SQLiteException -> Lbd
            r3 = 0
            java.lang.String r4 = "audience_id"
            r2[r3] = r4     // Catch: java.lang.Throwable -> Lb3 android.database.sqlite.SQLiteException -> Lbd
            r3 = 1
            java.lang.String r4 = "data"
            r2[r3] = r4     // Catch: java.lang.Throwable -> Lb3 android.database.sqlite.SQLiteException -> Lbd
            java.lang.String r3 = "app_id=? AND property_name=?"
            r4 = 2
            java.lang.String[] r4 = new java.lang.String[r4]     // Catch: java.lang.Throwable -> Lb3 android.database.sqlite.SQLiteException -> Lbd
            r5 = 0
            r4[r5] = r11     // Catch: java.lang.Throwable -> Lb3 android.database.sqlite.SQLiteException -> Lbd
            r5 = 1
            r4[r5] = r12     // Catch: java.lang.Throwable -> Lb3 android.database.sqlite.SQLiteException -> Lbd
            r5 = 0
            r6 = 0
            r7 = 0
            android.database.Cursor r1 = r0.query(r1, r2, r3, r4, r5, r6, r7)     // Catch: java.lang.Throwable -> Lb3 android.database.sqlite.SQLiteException -> Lbd
            boolean r0 = r1.moveToFirst()     // Catch: android.database.sqlite.SQLiteException -> L9a java.lang.Throwable -> Lbb
            if (r0 != 0) goto L47
            java.util.Map r0 = java.util.Collections.emptyMap()     // Catch: android.database.sqlite.SQLiteException -> L9a java.lang.Throwable -> Lbb
            if (r1 == 0) goto L46
            r1.close()
        L46:
            return r0
        L47:
            r0 = 1
            byte[] r0 = r1.getBlob(r0)     // Catch: android.database.sqlite.SQLiteException -> L9a java.lang.Throwable -> Lbb
            r2 = 0
            int r3 = r0.length     // Catch: android.database.sqlite.SQLiteException -> L9a java.lang.Throwable -> Lbb
            com.google.android.gms.internal.ahw r0 = com.google.android.gms.internal.ahw.zzb(r0, r2, r3)     // Catch: android.database.sqlite.SQLiteException -> L9a java.lang.Throwable -> Lbb
            com.google.android.gms.internal.zzclc r2 = new com.google.android.gms.internal.zzclc     // Catch: android.database.sqlite.SQLiteException -> L9a java.lang.Throwable -> Lbb
            r2.<init>()     // Catch: android.database.sqlite.SQLiteException -> L9a java.lang.Throwable -> Lbb
            r2.zza(r0)     // Catch: java.io.IOException -> L87 android.database.sqlite.SQLiteException -> L9a java.lang.Throwable -> Lbb
            r0 = 0
            int r3 = r1.getInt(r0)     // Catch: android.database.sqlite.SQLiteException -> L9a java.lang.Throwable -> Lbb
            java.lang.Integer r0 = java.lang.Integer.valueOf(r3)     // Catch: android.database.sqlite.SQLiteException -> L9a java.lang.Throwable -> Lbb
            java.lang.Object r0 = r8.get(r0)     // Catch: android.database.sqlite.SQLiteException -> L9a java.lang.Throwable -> Lbb
            java.util.List r0 = (java.util.List) r0     // Catch: android.database.sqlite.SQLiteException -> L9a java.lang.Throwable -> Lbb
            if (r0 != 0) goto L77
            java.util.ArrayList r0 = new java.util.ArrayList     // Catch: android.database.sqlite.SQLiteException -> L9a java.lang.Throwable -> Lbb
            r0.<init>()     // Catch: android.database.sqlite.SQLiteException -> L9a java.lang.Throwable -> Lbb
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch: android.database.sqlite.SQLiteException -> L9a java.lang.Throwable -> Lbb
            r8.put(r3, r0)     // Catch: android.database.sqlite.SQLiteException -> L9a java.lang.Throwable -> Lbb
        L77:
            r0.add(r2)     // Catch: android.database.sqlite.SQLiteException -> L9a java.lang.Throwable -> Lbb
        L7a:
            boolean r0 = r1.moveToNext()     // Catch: android.database.sqlite.SQLiteException -> L9a java.lang.Throwable -> Lbb
            if (r0 != 0) goto L47
            if (r1 == 0) goto L85
            r1.close()
        L85:
            r0 = r8
            goto L46
        L87:
            r0 = move-exception
            com.google.android.gms.internal.zzcgx r2 = r10.zzwE()     // Catch: android.database.sqlite.SQLiteException -> L9a java.lang.Throwable -> Lbb
            com.google.android.gms.internal.zzcgz r2 = r2.zzyv()     // Catch: android.database.sqlite.SQLiteException -> L9a java.lang.Throwable -> Lbb
            java.lang.String r3 = "Failed to merge filter"
            java.lang.Object r4 = com.google.android.gms.internal.zzcgx.zzea(r11)     // Catch: android.database.sqlite.SQLiteException -> L9a java.lang.Throwable -> Lbb
            r2.zze(r3, r4, r0)     // Catch: android.database.sqlite.SQLiteException -> L9a java.lang.Throwable -> Lbb
            goto L7a
        L9a:
            r0 = move-exception
        L9b:
            com.google.android.gms.internal.zzcgx r2 = r10.zzwE()     // Catch: java.lang.Throwable -> Lbb
            com.google.android.gms.internal.zzcgz r2 = r2.zzyv()     // Catch: java.lang.Throwable -> Lbb
            java.lang.String r3 = "Database error querying filters. appId"
            java.lang.Object r4 = com.google.android.gms.internal.zzcgx.zzea(r11)     // Catch: java.lang.Throwable -> Lbb
            r2.zze(r3, r4, r0)     // Catch: java.lang.Throwable -> Lbb
            if (r1 == 0) goto Lb1
            r1.close()
        Lb1:
            r0 = r9
            goto L46
        Lb3:
            r0 = move-exception
            r1 = r9
        Lb5:
            if (r1 == 0) goto Lba
            r1.close()
        Lba:
            throw r0
        Lbb:
            r0 = move-exception
            goto Lb5
        Lbd:
            r0 = move-exception
            r1 = r9
            goto L9b
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcfz.zzK(java.lang.String, java.lang.String):java.util.Map");
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x00b2 A[Catch: all -> 0x00e7, SQLiteException -> 0x00ec, TRY_LEAVE, TryCatch #0 {all -> 0x00e7, blocks: (B:3:0x0017, B:5:0x004a, B:7:0x0074, B:11:0x008b, B:13:0x00b2, B:15:0x00c8, B:19:0x00d2), top: B:27:0x0017 }] */
    /* JADX WARN: Removed duplicated region for block: B:15:0x00c8 A[Catch: all -> 0x00e7, SQLiteException -> 0x00ec, TRY_ENTER, TRY_LEAVE, TryCatch #0 {all -> 0x00e7, blocks: (B:3:0x0017, B:5:0x004a, B:7:0x0074, B:11:0x008b, B:13:0x00b2, B:15:0x00c8, B:19:0x00d2), top: B:27:0x0017 }] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:20:0x00e3 -> B:26:0x0089). Please report as a decompilation issue!!! */
    @android.support.annotation.WorkerThread
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected final long zzL(java.lang.String r13, java.lang.String r14) {
        /*
            Method dump skipped, instruction units count: 238
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcfz.zzL(java.lang.String, java.lang.String):long");
    }

    public final long zza(zzcll zzcllVar) throws IOException {
        long jZzo;
        zzjB();
        zzkC();
        zzbr.zzu(zzcllVar);
        zzbr.zzcF(zzcllVar.zzaK);
        try {
            byte[] bArr = new byte[zzcllVar.zzMl()];
            ahx ahxVarZzc = ahx.zzc(bArr, 0, bArr.length);
            zzcllVar.zza(ahxVarZzc);
            ahxVarZzc.zzMc();
            zzckx zzckxVarZzwA = zzwA();
            zzbr.zzu(bArr);
            zzckxVarZzwA.zzjB();
            MessageDigest messageDigestZzbE = zzckx.zzbE(CommonUtils.MD5_INSTANCE);
            if (messageDigestZzbE == null) {
                zzckxVarZzwA.zzwE().zzyv().log("Failed to get MD5");
                jZzo = 0;
            } else {
                jZzo = zzckx.zzo(messageDigestZzbE.digest(bArr));
            }
            ContentValues contentValues = new ContentValues();
            contentValues.put("app_id", zzcllVar.zzaK);
            contentValues.put("metadata_fingerprint", Long.valueOf(jZzo));
            contentValues.put("metadata", bArr);
            try {
                getWritableDatabase().insertWithOnConflict("raw_events_metadata", null, contentValues, 4);
                return jZzo;
            } catch (SQLiteException e) {
                zzwE().zzyv().zze("Error storing raw event metadata. appId", zzcgx.zzea(zzcllVar.zzaK), e);
                throw e;
            }
        } catch (IOException e2) {
            zzwE().zzyv().zze("Data loss. Failed to serialize event metadata. appId", zzcgx.zzea(zzcllVar.zzaK), e2);
            throw e2;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:38:0x0135  */
    @android.support.annotation.WorkerThread
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final com.google.android.gms.internal.zzcga zza(long r12, java.lang.String r14, boolean r15, boolean r16, boolean r17, boolean r18, boolean r19) {
        /*
            Method dump skipped, instruction units count: 317
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcfz.zza(long, java.lang.String, boolean, boolean, boolean, boolean, boolean):com.google.android.gms.internal.zzcga");
    }

    @WorkerThread
    public final void zza(zzcfs zzcfsVar) {
        zzbr.zzu(zzcfsVar);
        zzjB();
        zzkC();
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", zzcfsVar.zzhk());
        contentValues.put("app_instance_id", zzcfsVar.getAppInstanceId());
        contentValues.put("gmp_app_id", zzcfsVar.getGmpAppId());
        contentValues.put("resettable_device_id_hash", zzcfsVar.zzwI());
        contentValues.put("last_bundle_index", Long.valueOf(zzcfsVar.zzwR()));
        contentValues.put("last_bundle_start_timestamp", Long.valueOf(zzcfsVar.zzwK()));
        contentValues.put("last_bundle_end_timestamp", Long.valueOf(zzcfsVar.zzwL()));
        contentValues.put("app_version", zzcfsVar.zzjG());
        contentValues.put("app_store", zzcfsVar.zzwN());
        contentValues.put("gmp_version", Long.valueOf(zzcfsVar.zzwO()));
        contentValues.put("dev_cert_hash", Long.valueOf(zzcfsVar.zzwP()));
        contentValues.put("measurement_enabled", Boolean.valueOf(zzcfsVar.zzwQ()));
        contentValues.put("day", Long.valueOf(zzcfsVar.zzwV()));
        contentValues.put("daily_public_events_count", Long.valueOf(zzcfsVar.zzwW()));
        contentValues.put("daily_events_count", Long.valueOf(zzcfsVar.zzwX()));
        contentValues.put("daily_conversions_count", Long.valueOf(zzcfsVar.zzwY()));
        contentValues.put("config_fetched_time", Long.valueOf(zzcfsVar.zzwS()));
        contentValues.put("failed_config_fetch_time", Long.valueOf(zzcfsVar.zzwT()));
        contentValues.put("app_version_int", Long.valueOf(zzcfsVar.zzwM()));
        contentValues.put("firebase_instance_id", zzcfsVar.zzwJ());
        contentValues.put("daily_error_events_count", Long.valueOf(zzcfsVar.zzxa()));
        contentValues.put("daily_realtime_events_count", Long.valueOf(zzcfsVar.zzwZ()));
        contentValues.put("health_monitor_sample", zzcfsVar.zzxb());
        contentValues.put("android_id", Long.valueOf(zzcfsVar.zzxd()));
        try {
            SQLiteDatabase writableDatabase = getWritableDatabase();
            if (writableDatabase.update("apps", contentValues, "app_id = ?", new String[]{zzcfsVar.zzhk()}) == 0 && writableDatabase.insertWithOnConflict("apps", null, contentValues, 5) == -1) {
                zzwE().zzyv().zzj("Failed to insert/update app (got -1). appId", zzcgx.zzea(zzcfsVar.zzhk()));
            }
        } catch (SQLiteException e) {
            zzwE().zzyv().zze("Error storing app. appId", zzcgx.zzea(zzcfsVar.zzhk()), e);
        }
    }

    @WorkerThread
    public final void zza(zzcgh zzcghVar) {
        zzbr.zzu(zzcghVar);
        zzjB();
        zzkC();
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", zzcghVar.mAppId);
        contentValues.put("name", zzcghVar.mName);
        contentValues.put("lifetime_count", Long.valueOf(zzcghVar.zzbpK));
        contentValues.put("current_bundle_count", Long.valueOf(zzcghVar.zzbpL));
        contentValues.put("last_fire_timestamp", Long.valueOf(zzcghVar.zzbpM));
        try {
            if (getWritableDatabase().insertWithOnConflict("events", null, contentValues, 5) == -1) {
                zzwE().zzyv().zzj("Failed to insert/update event aggregates (got -1). appId", zzcgx.zzea(zzcghVar.mAppId));
            }
        } catch (SQLiteException e) {
            zzwE().zzyv().zze("Error storing event aggregates. appId", zzcgx.zzea(zzcghVar.mAppId), e);
        }
    }

    @WorkerThread
    final void zza(String str, zzcky[] zzckyVarArr) {
        boolean z;
        zzkC();
        zzjB();
        zzbr.zzcF(str);
        zzbr.zzu(zzckyVarArr);
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.beginTransaction();
        try {
            zzkC();
            zzjB();
            zzbr.zzcF(str);
            SQLiteDatabase writableDatabase2 = getWritableDatabase();
            writableDatabase2.delete("property_filters", "app_id=?", new String[]{str});
            writableDatabase2.delete("event_filters", "app_id=?", new String[]{str});
            for (zzcky zzckyVar : zzckyVarArr) {
                zzkC();
                zzjB();
                zzbr.zzcF(str);
                zzbr.zzu(zzckyVar);
                zzbr.zzu(zzckyVar.zzbuO);
                zzbr.zzu(zzckyVar.zzbuN);
                if (zzckyVar.zzbuM == null) {
                    zzwE().zzyx().zzj("Audience with no ID. appId", zzcgx.zzea(str));
                } else {
                    int iIntValue = zzckyVar.zzbuM.intValue();
                    zzckz[] zzckzVarArr = zzckyVar.zzbuO;
                    int length = zzckzVarArr.length;
                    int i = 0;
                    while (true) {
                        if (i >= length) {
                            zzclc[] zzclcVarArr = zzckyVar.zzbuN;
                            int length2 = zzclcVarArr.length;
                            int i2 = 0;
                            while (true) {
                                if (i2 >= length2) {
                                    zzckz[] zzckzVarArr2 = zzckyVar.zzbuO;
                                    int length3 = zzckzVarArr2.length;
                                    int i3 = 0;
                                    while (true) {
                                        if (i3 >= length3) {
                                            z = true;
                                            break;
                                        } else {
                                            if (!zza(str, iIntValue, zzckzVarArr2[i3])) {
                                                z = false;
                                                break;
                                            }
                                            i3++;
                                        }
                                    }
                                    if (z) {
                                        zzclc[] zzclcVarArr2 = zzckyVar.zzbuN;
                                        int length4 = zzclcVarArr2.length;
                                        int i4 = 0;
                                        while (true) {
                                            if (i4 >= length4) {
                                                break;
                                            }
                                            if (!zza(str, iIntValue, zzclcVarArr2[i4])) {
                                                z = false;
                                                break;
                                            }
                                            i4++;
                                        }
                                    }
                                    if (!z) {
                                        zzkC();
                                        zzjB();
                                        zzbr.zzcF(str);
                                        SQLiteDatabase writableDatabase3 = getWritableDatabase();
                                        writableDatabase3.delete("property_filters", "app_id=? and audience_id=?", new String[]{str, String.valueOf(iIntValue)});
                                        writableDatabase3.delete("event_filters", "app_id=? and audience_id=?", new String[]{str, String.valueOf(iIntValue)});
                                    }
                                } else {
                                    if (zzclcVarArr[i2].zzbuQ == null) {
                                        zzwE().zzyx().zze("Property filter with no ID. Audience definition ignored. appId, audienceId", zzcgx.zzea(str), zzckyVar.zzbuM);
                                        break;
                                    }
                                    i2++;
                                }
                            }
                        } else {
                            if (zzckzVarArr[i].zzbuQ == null) {
                                zzwE().zzyx().zze("Event filter with no ID. Audience definition ignored. appId, audienceId", zzcgx.zzea(str), zzckyVar.zzbuM);
                                break;
                            }
                            i++;
                        }
                    }
                }
            }
            ArrayList arrayList = new ArrayList();
            for (zzcky zzckyVar2 : zzckyVarArr) {
                arrayList.add(zzckyVar2.zzbuM);
            }
            zzc(str, arrayList);
            writableDatabase.setTransactionSuccessful();
        } finally {
            writableDatabase.endTransaction();
        }
    }

    @WorkerThread
    public final boolean zza(zzcfw zzcfwVar) {
        zzbr.zzu(zzcfwVar);
        zzjB();
        zzkC();
        if (zzG(zzcfwVar.packageName, zzcfwVar.zzbph.name) == null) {
            long jZzb = zzb("SELECT COUNT(1) FROM conditional_properties WHERE app_id=?", new String[]{zzcfwVar.packageName});
            zzcfy.zzxu();
            if (jZzb >= 1000) {
                return false;
            }
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", zzcfwVar.packageName);
        contentValues.put(FirebaseAnalytics.Param.ORIGIN, zzcfwVar.zzbpg);
        contentValues.put("name", zzcfwVar.zzbph.name);
        zza(contentValues, FirebaseAnalytics.Param.VALUE, zzcfwVar.zzbph.getValue());
        contentValues.put("active", Boolean.valueOf(zzcfwVar.zzbpj));
        contentValues.put("trigger_event_name", zzcfwVar.zzbpk);
        contentValues.put("trigger_timeout", Long.valueOf(zzcfwVar.zzbpm));
        zzwA();
        contentValues.put("timed_out_event", zzckx.zza(zzcfwVar.zzbpl));
        contentValues.put("creation_timestamp", Long.valueOf(zzcfwVar.zzbpi));
        zzwA();
        contentValues.put("triggered_event", zzckx.zza(zzcfwVar.zzbpn));
        contentValues.put("triggered_timestamp", Long.valueOf(zzcfwVar.zzbph.zzbuC));
        contentValues.put("time_to_live", Long.valueOf(zzcfwVar.zzbpo));
        zzwA();
        contentValues.put("expired_event", zzckx.zza(zzcfwVar.zzbpp));
        try {
            if (getWritableDatabase().insertWithOnConflict("conditional_properties", null, contentValues, 5) == -1) {
                zzwE().zzyv().zzj("Failed to insert/update conditional user property (got -1)", zzcgx.zzea(zzcfwVar.packageName));
            }
        } catch (SQLiteException e) {
            zzwE().zzyv().zze("Error storing conditional user property", zzcgx.zzea(zzcfwVar.packageName), e);
        }
        return true;
    }

    public final boolean zza(zzcgg zzcggVar, long j, boolean z) {
        zzjB();
        zzkC();
        zzbr.zzu(zzcggVar);
        zzbr.zzcF(zzcggVar.mAppId);
        zzcli zzcliVar = new zzcli();
        zzcliVar.zzbvC = Long.valueOf(zzcggVar.zzbpI);
        zzcliVar.zzbvA = new zzclj[zzcggVar.zzbpJ.size()];
        int i = 0;
        for (String str : zzcggVar.zzbpJ) {
            zzclj zzcljVar = new zzclj();
            zzcliVar.zzbvA[i] = zzcljVar;
            zzcljVar.name = str;
            zzwA().zza(zzcljVar, zzcggVar.zzbpJ.get(str));
            i++;
        }
        try {
            byte[] bArr = new byte[zzcliVar.zzMl()];
            ahx ahxVarZzc = ahx.zzc(bArr, 0, bArr.length);
            zzcliVar.zza(ahxVarZzc);
            ahxVarZzc.zzMc();
            zzwE().zzyB().zze("Saving event, name, data size", zzwz().zzdX(zzcggVar.mName), Integer.valueOf(bArr.length));
            ContentValues contentValues = new ContentValues();
            contentValues.put("app_id", zzcggVar.mAppId);
            contentValues.put("name", zzcggVar.mName);
            contentValues.put(AppMeasurement.Param.TIMESTAMP, Long.valueOf(zzcggVar.zzayU));
            contentValues.put("metadata_fingerprint", Long.valueOf(j));
            contentValues.put(ShareConstants.WEB_DIALOG_PARAM_DATA, bArr);
            contentValues.put("realtime", Integer.valueOf(z ? 1 : 0));
            try {
                if (getWritableDatabase().insert("raw_events", null, contentValues) != -1) {
                    return true;
                }
                zzwE().zzyv().zzj("Failed to insert raw event (got -1). appId", zzcgx.zzea(zzcggVar.mAppId));
                return false;
            } catch (SQLiteException e) {
                zzwE().zzyv().zze("Error storing raw event. appId", zzcgx.zzea(zzcggVar.mAppId), e);
                return false;
            }
        } catch (IOException e2) {
            zzwE().zzyv().zze("Data loss. Failed to serialize event params/data. appId", zzcgx.zzea(zzcggVar.mAppId), e2);
            return false;
        }
    }

    @WorkerThread
    public final boolean zza(zzckw zzckwVar) {
        zzbr.zzu(zzckwVar);
        zzjB();
        zzkC();
        if (zzG(zzckwVar.mAppId, zzckwVar.mName) == null) {
            if (zzckx.zzep(zzckwVar.mName)) {
                long jZzb = zzb("select count(1) from user_attributes where app_id=? and name not like '!_%' escape '!'", new String[]{zzckwVar.mAppId});
                zzcfy.zzxr();
                if (jZzb >= 25) {
                    return false;
                }
            } else {
                long jZzb2 = zzb("select count(1) from user_attributes where app_id=? and origin=? AND name like '!_%' escape '!'", new String[]{zzckwVar.mAppId, zzckwVar.mOrigin});
                zzcfy.zzxt();
                if (jZzb2 >= 25) {
                    return false;
                }
            }
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", zzckwVar.mAppId);
        contentValues.put(FirebaseAnalytics.Param.ORIGIN, zzckwVar.mOrigin);
        contentValues.put("name", zzckwVar.mName);
        contentValues.put("set_timestamp", Long.valueOf(zzckwVar.zzbuG));
        zza(contentValues, FirebaseAnalytics.Param.VALUE, zzckwVar.mValue);
        try {
            if (getWritableDatabase().insertWithOnConflict("user_attributes", null, contentValues, 5) == -1) {
                zzwE().zzyv().zzj("Failed to insert/update user property (got -1). appId", zzcgx.zzea(zzckwVar.mAppId));
            }
        } catch (SQLiteException e) {
            zzwE().zzyv().zze("Error storing user property. appId", zzcgx.zzea(zzckwVar.mAppId), e);
        }
        return true;
    }

    @WorkerThread
    public final boolean zza(zzcll zzcllVar, boolean z) {
        zzjB();
        zzkC();
        zzbr.zzu(zzcllVar);
        zzbr.zzcF(zzcllVar.zzaK);
        zzbr.zzu(zzcllVar.zzbvM);
        zzyd();
        long jCurrentTimeMillis = zzkp().currentTimeMillis();
        if (zzcllVar.zzbvM.longValue() < jCurrentTimeMillis - zzcfy.zzxF() || zzcllVar.zzbvM.longValue() > zzcfy.zzxF() + jCurrentTimeMillis) {
            zzwE().zzyx().zzd("Storing bundle outside of the max uploading time span. appId, now, timestamp", zzcgx.zzea(zzcllVar.zzaK), Long.valueOf(jCurrentTimeMillis), zzcllVar.zzbvM);
        }
        try {
            byte[] bArr = new byte[zzcllVar.zzMl()];
            ahx ahxVarZzc = ahx.zzc(bArr, 0, bArr.length);
            zzcllVar.zza(ahxVarZzc);
            ahxVarZzc.zzMc();
            byte[] bArrZzm = zzwA().zzm(bArr);
            zzwE().zzyB().zzj("Saving bundle, size", Integer.valueOf(bArrZzm.length));
            ContentValues contentValues = new ContentValues();
            contentValues.put("app_id", zzcllVar.zzaK);
            contentValues.put("bundle_end_timestamp", zzcllVar.zzbvM);
            contentValues.put(ShareConstants.WEB_DIALOG_PARAM_DATA, bArrZzm);
            contentValues.put("has_realtime", Integer.valueOf(z ? 1 : 0));
            try {
                if (getWritableDatabase().insert("queue", null, contentValues) != -1) {
                    return true;
                }
                zzwE().zzyv().zzj("Failed to insert bundle (got -1). appId", zzcgx.zzea(zzcllVar.zzaK));
                return false;
            } catch (SQLiteException e) {
                zzwE().zzyv().zze("Error storing bundle. appId", zzcgx.zzea(zzcllVar.zzaK), e);
                return false;
            }
        } catch (IOException e2) {
            zzwE().zzyv().zze("Data loss. Failed to serialize bundle. appId", zzcgx.zzea(zzcllVar.zzaK), e2);
            return false;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x0059  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.String zzaa(long r8) throws java.lang.Throwable {
        /*
            r7 = this;
            r0 = 0
            r7.zzjB()
            r7.zzkC()
            android.database.sqlite.SQLiteDatabase r1 = r7.getWritableDatabase()     // Catch: android.database.sqlite.SQLiteException -> L3f java.lang.Throwable -> L54
            java.lang.String r2 = "select app_id from apps where app_id in (select distinct app_id from raw_events) and config_fetched_time < ? order by failed_config_fetch_time limit 1;"
            r3 = 1
            java.lang.String[] r3 = new java.lang.String[r3]     // Catch: android.database.sqlite.SQLiteException -> L3f java.lang.Throwable -> L54
            r4 = 0
            java.lang.String r5 = java.lang.String.valueOf(r8)     // Catch: android.database.sqlite.SQLiteException -> L3f java.lang.Throwable -> L54
            r3[r4] = r5     // Catch: android.database.sqlite.SQLiteException -> L3f java.lang.Throwable -> L54
            android.database.Cursor r2 = r1.rawQuery(r2, r3)     // Catch: android.database.sqlite.SQLiteException -> L3f java.lang.Throwable -> L54
            boolean r1 = r2.moveToFirst()     // Catch: java.lang.Throwable -> L5d android.database.sqlite.SQLiteException -> L5f
            if (r1 != 0) goto L34
            com.google.android.gms.internal.zzcgx r1 = r7.zzwE()     // Catch: java.lang.Throwable -> L5d android.database.sqlite.SQLiteException -> L5f
            com.google.android.gms.internal.zzcgz r1 = r1.zzyB()     // Catch: java.lang.Throwable -> L5d android.database.sqlite.SQLiteException -> L5f
            java.lang.String r3 = "No expired configs for apps with pending events"
            r1.log(r3)     // Catch: java.lang.Throwable -> L5d android.database.sqlite.SQLiteException -> L5f
            if (r2 == 0) goto L33
            r2.close()
        L33:
            return r0
        L34:
            r1 = 0
            java.lang.String r0 = r2.getString(r1)     // Catch: java.lang.Throwable -> L5d android.database.sqlite.SQLiteException -> L5f
            if (r2 == 0) goto L33
            r2.close()
            goto L33
        L3f:
            r1 = move-exception
            r2 = r0
        L41:
            com.google.android.gms.internal.zzcgx r3 = r7.zzwE()     // Catch: java.lang.Throwable -> L5d
            com.google.android.gms.internal.zzcgz r3 = r3.zzyv()     // Catch: java.lang.Throwable -> L5d
            java.lang.String r4 = "Error selecting expired configs"
            r3.zzj(r4, r1)     // Catch: java.lang.Throwable -> L5d
            if (r2 == 0) goto L33
            r2.close()
            goto L33
        L54:
            r1 = move-exception
            r2 = r0
            r0 = r1
        L57:
            if (r2 == 0) goto L5c
            r2.close()
        L5c:
            throw r0
        L5d:
            r0 = move-exception
            goto L57
        L5f:
            r1 = move-exception
            goto L41
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcfz.zzaa(long):java.lang.String");
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x0085, code lost:
    
        zzwE().zzyv().zzj("Read more than the max allowed conditional properties, ignoring extra", java.lang.Integer.valueOf(com.google.android.gms.internal.zzcfy.zzxu()));
     */
    /* JADX WARN: Removed duplicated region for block: B:32:0x016c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.util.List<com.google.android.gms.internal.zzcfw> zzc(java.lang.String r24, java.lang.String[] r25) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 378
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcfz.zzc(java.lang.String, java.lang.String[]):java.util.List");
    }

    /* JADX WARN: Removed duplicated region for block: B:32:0x00b0  */
    @android.support.annotation.WorkerThread
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.util.List<com.google.android.gms.internal.zzckw> zzdQ(java.lang.String r12) {
        /*
            r11 = this;
            r10 = 0
            com.google.android.gms.common.internal.zzbr.zzcF(r12)
            r11.zzjB()
            r11.zzkC()
            java.util.ArrayList r9 = new java.util.ArrayList
            r9.<init>()
            android.database.sqlite.SQLiteDatabase r0 = r11.getWritableDatabase()     // Catch: java.lang.Throwable -> Lad android.database.sqlite.SQLiteException -> Lba
            java.lang.String r1 = "user_attributes"
            r2 = 4
            java.lang.String[] r2 = new java.lang.String[r2]     // Catch: java.lang.Throwable -> Lad android.database.sqlite.SQLiteException -> Lba
            r3 = 0
            java.lang.String r4 = "name"
            r2[r3] = r4     // Catch: java.lang.Throwable -> Lad android.database.sqlite.SQLiteException -> Lba
            r3 = 1
            java.lang.String r4 = "origin"
            r2[r3] = r4     // Catch: java.lang.Throwable -> Lad android.database.sqlite.SQLiteException -> Lba
            r3 = 2
            java.lang.String r4 = "set_timestamp"
            r2[r3] = r4     // Catch: java.lang.Throwable -> Lad android.database.sqlite.SQLiteException -> Lba
            r3 = 3
            java.lang.String r4 = "value"
            r2[r3] = r4     // Catch: java.lang.Throwable -> Lad android.database.sqlite.SQLiteException -> Lba
            java.lang.String r3 = "app_id=?"
            r4 = 1
            java.lang.String[] r4 = new java.lang.String[r4]     // Catch: java.lang.Throwable -> Lad android.database.sqlite.SQLiteException -> Lba
            r5 = 0
            r4[r5] = r12     // Catch: java.lang.Throwable -> Lad android.database.sqlite.SQLiteException -> Lba
            r5 = 0
            r6 = 0
            java.lang.String r7 = "rowid"
            int r8 = com.google.android.gms.internal.zzcfy.zzxs()     // Catch: java.lang.Throwable -> Lad android.database.sqlite.SQLiteException -> Lba
            java.lang.String r8 = java.lang.String.valueOf(r8)     // Catch: java.lang.Throwable -> Lad android.database.sqlite.SQLiteException -> Lba
            android.database.Cursor r7 = r0.query(r1, r2, r3, r4, r5, r6, r7, r8)     // Catch: java.lang.Throwable -> Lad android.database.sqlite.SQLiteException -> Lba
            boolean r0 = r7.moveToFirst()     // Catch: android.database.sqlite.SQLiteException -> L93 java.lang.Throwable -> Lb4
            if (r0 != 0) goto L51
            if (r7 == 0) goto L4f
            r7.close()
        L4f:
            r0 = r9
        L50:
            return r0
        L51:
            r0 = 0
            java.lang.String r3 = r7.getString(r0)     // Catch: android.database.sqlite.SQLiteException -> L93 java.lang.Throwable -> Lb4
            r0 = 1
            java.lang.String r2 = r7.getString(r0)     // Catch: android.database.sqlite.SQLiteException -> L93 java.lang.Throwable -> Lb4
            if (r2 != 0) goto L5f
            java.lang.String r2 = ""
        L5f:
            r0 = 2
            long r4 = r7.getLong(r0)     // Catch: android.database.sqlite.SQLiteException -> L93 java.lang.Throwable -> Lb4
            r0 = 3
            java.lang.Object r6 = r11.zza(r7, r0)     // Catch: android.database.sqlite.SQLiteException -> L93 java.lang.Throwable -> Lb4
            if (r6 != 0) goto L89
            com.google.android.gms.internal.zzcgx r0 = r11.zzwE()     // Catch: android.database.sqlite.SQLiteException -> L93 java.lang.Throwable -> Lb4
            com.google.android.gms.internal.zzcgz r0 = r0.zzyv()     // Catch: android.database.sqlite.SQLiteException -> L93 java.lang.Throwable -> Lb4
            java.lang.String r1 = "Read invalid user property value, ignoring it. appId"
            java.lang.Object r2 = com.google.android.gms.internal.zzcgx.zzea(r12)     // Catch: android.database.sqlite.SQLiteException -> L93 java.lang.Throwable -> Lb4
            r0.zzj(r1, r2)     // Catch: android.database.sqlite.SQLiteException -> L93 java.lang.Throwable -> Lb4
        L7c:
            boolean r0 = r7.moveToNext()     // Catch: android.database.sqlite.SQLiteException -> L93 java.lang.Throwable -> Lb4
            if (r0 != 0) goto L51
            if (r7 == 0) goto L87
            r7.close()
        L87:
            r0 = r9
            goto L50
        L89:
            com.google.android.gms.internal.zzckw r0 = new com.google.android.gms.internal.zzckw     // Catch: android.database.sqlite.SQLiteException -> L93 java.lang.Throwable -> Lb4
            r1 = r12
            r0.<init>(r1, r2, r3, r4, r6)     // Catch: android.database.sqlite.SQLiteException -> L93 java.lang.Throwable -> Lb4
            r9.add(r0)     // Catch: android.database.sqlite.SQLiteException -> L93 java.lang.Throwable -> Lb4
            goto L7c
        L93:
            r0 = move-exception
            r1 = r7
        L95:
            com.google.android.gms.internal.zzcgx r2 = r11.zzwE()     // Catch: java.lang.Throwable -> Lb7
            com.google.android.gms.internal.zzcgz r2 = r2.zzyv()     // Catch: java.lang.Throwable -> Lb7
            java.lang.String r3 = "Error querying user properties. appId"
            java.lang.Object r4 = com.google.android.gms.internal.zzcgx.zzea(r12)     // Catch: java.lang.Throwable -> Lb7
            r2.zze(r3, r4, r0)     // Catch: java.lang.Throwable -> Lb7
            if (r1 == 0) goto Lab
            r1.close()
        Lab:
            r0 = r10
            goto L50
        Lad:
            r0 = move-exception
        Lae:
            if (r10 == 0) goto Lb3
            r10.close()
        Lb3:
            throw r0
        Lb4:
            r0 = move-exception
            r10 = r7
            goto Lae
        Lb7:
            r0 = move-exception
            r10 = r1
            goto Lae
        Lba:
            r0 = move-exception
            r1 = r10
            goto L95
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcfz.zzdQ(java.lang.String):java.util.List");
    }

    @WorkerThread
    public final zzcfs zzdR(String str) {
        Cursor cursorQuery;
        zzbr.zzcF(str);
        zzjB();
        zzkC();
        try {
            cursorQuery = getWritableDatabase().query("apps", new String[]{"app_instance_id", "gmp_app_id", "resettable_device_id_hash", "last_bundle_index", "last_bundle_start_timestamp", "last_bundle_end_timestamp", "app_version", "app_store", "gmp_version", "dev_cert_hash", "measurement_enabled", "day", "daily_public_events_count", "daily_events_count", "daily_conversions_count", "config_fetched_time", "failed_config_fetch_time", "app_version_int", "firebase_instance_id", "daily_error_events_count", "daily_realtime_events_count", "health_monitor_sample", "android_id"}, "app_id=?", new String[]{str}, null, null, null);
            try {
                try {
                    if (!cursorQuery.moveToFirst()) {
                        if (cursorQuery != null) {
                            cursorQuery.close();
                        }
                        return null;
                    }
                    zzcfs zzcfsVar = new zzcfs(this.zzboi, str);
                    zzcfsVar.zzdH(cursorQuery.getString(0));
                    zzcfsVar.zzdI(cursorQuery.getString(1));
                    zzcfsVar.zzdJ(cursorQuery.getString(2));
                    zzcfsVar.zzQ(cursorQuery.getLong(3));
                    zzcfsVar.zzL(cursorQuery.getLong(4));
                    zzcfsVar.zzM(cursorQuery.getLong(5));
                    zzcfsVar.setAppVersion(cursorQuery.getString(6));
                    zzcfsVar.zzdL(cursorQuery.getString(7));
                    zzcfsVar.zzO(cursorQuery.getLong(8));
                    zzcfsVar.zzP(cursorQuery.getLong(9));
                    zzcfsVar.setMeasurementEnabled((cursorQuery.isNull(10) ? 1 : cursorQuery.getInt(10)) != 0);
                    zzcfsVar.zzT(cursorQuery.getLong(11));
                    zzcfsVar.zzU(cursorQuery.getLong(12));
                    zzcfsVar.zzV(cursorQuery.getLong(13));
                    zzcfsVar.zzW(cursorQuery.getLong(14));
                    zzcfsVar.zzR(cursorQuery.getLong(15));
                    zzcfsVar.zzS(cursorQuery.getLong(16));
                    zzcfsVar.zzN(cursorQuery.isNull(17) ? -2147483648L : cursorQuery.getInt(17));
                    zzcfsVar.zzdK(cursorQuery.getString(18));
                    zzcfsVar.zzY(cursorQuery.getLong(19));
                    zzcfsVar.zzX(cursorQuery.getLong(20));
                    zzcfsVar.zzdM(cursorQuery.getString(21));
                    zzcfsVar.zzZ(cursorQuery.isNull(22) ? 0L : cursorQuery.getLong(22));
                    zzcfsVar.zzwH();
                    if (cursorQuery.moveToNext()) {
                        zzwE().zzyv().zzj("Got multiple records for app, expected one. appId", zzcgx.zzea(str));
                    }
                    if (cursorQuery == null) {
                        return zzcfsVar;
                    }
                    cursorQuery.close();
                    return zzcfsVar;
                } catch (SQLiteException e) {
                    e = e;
                    zzwE().zzyv().zze("Error querying app. appId", zzcgx.zzea(str), e);
                    if (cursorQuery != null) {
                        cursorQuery.close();
                    }
                    return null;
                }
            } catch (Throwable th) {
                th = th;
            }
            th = th;
        } catch (SQLiteException e2) {
            e = e2;
            cursorQuery = null;
        } catch (Throwable th2) {
            th = th2;
            cursorQuery = null;
        }
        if (cursorQuery != null) {
            cursorQuery.close();
        }
        throw th;
    }

    public final long zzdS(String str) {
        zzbr.zzcF(str);
        zzjB();
        zzkC();
        try {
            return getWritableDatabase().delete("raw_events", "rowid in (select rowid from raw_events where app_id=? order by rowid desc limit -1 offset ?)", new String[]{str, String.valueOf(Math.max(0, Math.min(1000000, zzwG().zzb(str, zzcgn.zzbqo))))});
        } catch (SQLiteException e) {
            zzwE().zzyv().zze("Error deleting over the limit events. appId", zzcgx.zzea(str), e);
            return 0L;
        }
    }

    @WorkerThread
    public final byte[] zzdT(String str) throws Throwable {
        Cursor cursorQuery;
        zzbr.zzcF(str);
        zzjB();
        zzkC();
        try {
            cursorQuery = getWritableDatabase().query("apps", new String[]{"remote_config"}, "app_id=?", new String[]{str}, null, null, null);
            try {
                try {
                    if (!cursorQuery.moveToFirst()) {
                        if (cursorQuery != null) {
                            cursorQuery.close();
                        }
                        return null;
                    }
                    byte[] blob = cursorQuery.getBlob(0);
                    if (cursorQuery.moveToNext()) {
                        zzwE().zzyv().zzj("Got multiple records for app config, expected one. appId", zzcgx.zzea(str));
                    }
                    if (cursorQuery == null) {
                        return blob;
                    }
                    cursorQuery.close();
                    return blob;
                } catch (SQLiteException e) {
                    e = e;
                    zzwE().zzyv().zze("Error querying remote config. appId", zzcgx.zzea(str), e);
                    if (cursorQuery != null) {
                        cursorQuery.close();
                    }
                    return null;
                }
            } catch (Throwable th) {
                th = th;
            }
            th = th;
        } catch (SQLiteException e2) {
            e = e2;
            cursorQuery = null;
        } catch (Throwable th2) {
            th = th2;
            cursorQuery = null;
        }
        if (cursorQuery != null) {
            cursorQuery.close();
        }
        throw th;
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x009d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    final java.util.Map<java.lang.Integer, com.google.android.gms.internal.zzclm> zzdU(java.lang.String r10) {
        /*
            r9 = this;
            r8 = 0
            r9.zzkC()
            r9.zzjB()
            com.google.android.gms.common.internal.zzbr.zzcF(r10)
            android.database.sqlite.SQLiteDatabase r0 = r9.getWritableDatabase()
            java.lang.String r1 = "audience_filter_values"
            r2 = 2
            java.lang.String[] r2 = new java.lang.String[r2]     // Catch: java.lang.Throwable -> L99 android.database.sqlite.SQLiteException -> La3
            r3 = 0
            java.lang.String r4 = "audience_id"
            r2[r3] = r4     // Catch: java.lang.Throwable -> L99 android.database.sqlite.SQLiteException -> La3
            r3 = 1
            java.lang.String r4 = "current_results"
            r2[r3] = r4     // Catch: java.lang.Throwable -> L99 android.database.sqlite.SQLiteException -> La3
            java.lang.String r3 = "app_id=?"
            r4 = 1
            java.lang.String[] r4 = new java.lang.String[r4]     // Catch: java.lang.Throwable -> L99 android.database.sqlite.SQLiteException -> La3
            r5 = 0
            r4[r5] = r10     // Catch: java.lang.Throwable -> L99 android.database.sqlite.SQLiteException -> La3
            r5 = 0
            r6 = 0
            r7 = 0
            android.database.Cursor r1 = r0.query(r1, r2, r3, r4, r5, r6, r7)     // Catch: java.lang.Throwable -> L99 android.database.sqlite.SQLiteException -> La3
            boolean r0 = r1.moveToFirst()     // Catch: android.database.sqlite.SQLiteException -> L80 java.lang.Throwable -> La1
            if (r0 != 0) goto L39
            if (r1 == 0) goto L37
            r1.close()
        L37:
            r0 = r8
        L38:
            return r0
        L39:
            android.support.v4.util.ArrayMap r0 = new android.support.v4.util.ArrayMap     // Catch: android.database.sqlite.SQLiteException -> L80 java.lang.Throwable -> La1
            r0.<init>()     // Catch: android.database.sqlite.SQLiteException -> L80 java.lang.Throwable -> La1
        L3e:
            r2 = 0
            int r2 = r1.getInt(r2)     // Catch: android.database.sqlite.SQLiteException -> L80 java.lang.Throwable -> La1
            r3 = 1
            byte[] r3 = r1.getBlob(r3)     // Catch: android.database.sqlite.SQLiteException -> L80 java.lang.Throwable -> La1
            r4 = 0
            int r5 = r3.length     // Catch: android.database.sqlite.SQLiteException -> L80 java.lang.Throwable -> La1
            com.google.android.gms.internal.ahw r3 = com.google.android.gms.internal.ahw.zzb(r3, r4, r5)     // Catch: android.database.sqlite.SQLiteException -> L80 java.lang.Throwable -> La1
            com.google.android.gms.internal.zzclm r4 = new com.google.android.gms.internal.zzclm     // Catch: android.database.sqlite.SQLiteException -> L80 java.lang.Throwable -> La1
            r4.<init>()     // Catch: android.database.sqlite.SQLiteException -> L80 java.lang.Throwable -> La1
            r4.zza(r3)     // Catch: java.io.IOException -> L69 android.database.sqlite.SQLiteException -> L80 java.lang.Throwable -> La1
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch: android.database.sqlite.SQLiteException -> L80 java.lang.Throwable -> La1
            r0.put(r2, r4)     // Catch: android.database.sqlite.SQLiteException -> L80 java.lang.Throwable -> La1
        L5d:
            boolean r2 = r1.moveToNext()     // Catch: android.database.sqlite.SQLiteException -> L80 java.lang.Throwable -> La1
            if (r2 != 0) goto L3e
            if (r1 == 0) goto L38
            r1.close()
            goto L38
        L69:
            r3 = move-exception
            com.google.android.gms.internal.zzcgx r4 = r9.zzwE()     // Catch: android.database.sqlite.SQLiteException -> L80 java.lang.Throwable -> La1
            com.google.android.gms.internal.zzcgz r4 = r4.zzyv()     // Catch: android.database.sqlite.SQLiteException -> L80 java.lang.Throwable -> La1
            java.lang.String r5 = "Failed to merge filter results. appId, audienceId, error"
            java.lang.Object r6 = com.google.android.gms.internal.zzcgx.zzea(r10)     // Catch: android.database.sqlite.SQLiteException -> L80 java.lang.Throwable -> La1
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch: android.database.sqlite.SQLiteException -> L80 java.lang.Throwable -> La1
            r4.zzd(r5, r6, r2, r3)     // Catch: android.database.sqlite.SQLiteException -> L80 java.lang.Throwable -> La1
            goto L5d
        L80:
            r0 = move-exception
        L81:
            com.google.android.gms.internal.zzcgx r2 = r9.zzwE()     // Catch: java.lang.Throwable -> La1
            com.google.android.gms.internal.zzcgz r2 = r2.zzyv()     // Catch: java.lang.Throwable -> La1
            java.lang.String r3 = "Database error querying filter results. appId"
            java.lang.Object r4 = com.google.android.gms.internal.zzcgx.zzea(r10)     // Catch: java.lang.Throwable -> La1
            r2.zze(r3, r4, r0)     // Catch: java.lang.Throwable -> La1
            if (r1 == 0) goto L97
            r1.close()
        L97:
            r0 = r8
            goto L38
        L99:
            r0 = move-exception
            r1 = r8
        L9b:
            if (r1 == 0) goto La0
            r1.close()
        La0:
            throw r0
        La1:
            r0 = move-exception
            goto L9b
        La3:
            r0 = move-exception
            r1 = r8
            goto L81
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcfz.zzdU(java.lang.String):java.util.Map");
    }

    public final long zzdV(String str) {
        zzbr.zzcF(str);
        return zza("select count(1) from events where app_id=? and name not like '!_%' escape '!'", new String[]{str}, 0L);
    }

    /* JADX WARN: Code restructure failed: missing block: B:19:0x0097, code lost:
    
        zzwE().zzyv().zzj("Read more than the max allowed user properties, ignoring excess", java.lang.Integer.valueOf(com.google.android.gms.internal.zzcfy.zzxs()));
     */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0109  */
    @android.support.annotation.WorkerThread
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.util.List<com.google.android.gms.internal.zzckw> zzh(java.lang.String r12, java.lang.String r13, java.lang.String r14) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 281
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcfz.zzh(java.lang.String, java.lang.String, java.lang.String):java.util.List");
    }

    @WorkerThread
    public final List<zzcfw> zzi(String str, String str2, String str3) {
        zzbr.zzcF(str);
        zzjB();
        zzkC();
        ArrayList arrayList = new ArrayList(3);
        arrayList.add(str);
        StringBuilder sb = new StringBuilder("app_id=?");
        if (!TextUtils.isEmpty(str2)) {
            arrayList.add(str2);
            sb.append(" and origin=?");
        }
        if (!TextUtils.isEmpty(str3)) {
            arrayList.add(String.valueOf(str3).concat("*"));
            sb.append(" and name glob ?");
        }
        return zzc(sb.toString(), (String[]) arrayList.toArray(new String[arrayList.size()]));
    }

    @Override // com.google.android.gms.internal.zzciv
    protected final void zzjC() {
    }

    /* JADX WARN: Removed duplicated region for block: B:46:0x00e7  */
    @android.support.annotation.WorkerThread
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.util.List<android.util.Pair<com.google.android.gms.internal.zzcll, java.lang.Long>> zzl(java.lang.String r12, int r13, int r14) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 246
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcfz.zzl(java.lang.String, int, int):java.util.List");
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x003d  */
    @android.support.annotation.WorkerThread
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.String zzyb() throws java.lang.Throwable {
        /*
            r5 = this;
            r0 = 0
            android.database.sqlite.SQLiteDatabase r1 = r5.getWritableDatabase()
            java.lang.String r2 = "select app_id from queue order by has_realtime desc, rowid asc limit 1;"
            r3 = 0
            android.database.Cursor r2 = r1.rawQuery(r2, r3)     // Catch: android.database.sqlite.SQLiteException -> L23 java.lang.Throwable -> L38
            boolean r1 = r2.moveToFirst()     // Catch: java.lang.Throwable -> L41 android.database.sqlite.SQLiteException -> L43
            if (r1 == 0) goto L1d
            r1 = 0
            java.lang.String r0 = r2.getString(r1)     // Catch: java.lang.Throwable -> L41 android.database.sqlite.SQLiteException -> L43
            if (r2 == 0) goto L1c
            r2.close()
        L1c:
            return r0
        L1d:
            if (r2 == 0) goto L1c
            r2.close()
            goto L1c
        L23:
            r1 = move-exception
            r2 = r0
        L25:
            com.google.android.gms.internal.zzcgx r3 = r5.zzwE()     // Catch: java.lang.Throwable -> L41
            com.google.android.gms.internal.zzcgz r3 = r3.zzyv()     // Catch: java.lang.Throwable -> L41
            java.lang.String r4 = "Database error getting next bundle app id"
            r3.zzj(r4, r1)     // Catch: java.lang.Throwable -> L41
            if (r2 == 0) goto L1c
            r2.close()
            goto L1c
        L38:
            r1 = move-exception
            r2 = r0
            r0 = r1
        L3b:
            if (r2 == 0) goto L40
            r2.close()
        L40:
            throw r0
        L41:
            r0 = move-exception
            goto L3b
        L43:
            r1 = move-exception
            goto L25
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcfz.zzyb():java.lang.String");
    }

    public final boolean zzyc() {
        return zzb("select count(1) > 0 from queue where has_realtime = 1", (String[]) null) != 0;
    }

    @WorkerThread
    final void zzyd() {
        int iDelete;
        zzjB();
        zzkC();
        if (zzyj()) {
            long j = zzwF().zzbrr.get();
            long jElapsedRealtime = zzkp().elapsedRealtime();
            if (Math.abs(jElapsedRealtime - j) > zzcfy.zzxG()) {
                zzwF().zzbrr.set(jElapsedRealtime);
                zzjB();
                zzkC();
                if (!zzyj() || (iDelete = getWritableDatabase().delete("queue", "abs(bundle_end_timestamp - ?) > cast(? as integer)", new String[]{String.valueOf(zzkp().currentTimeMillis()), String.valueOf(zzcfy.zzxF())})) <= 0) {
                    return;
                }
                zzwE().zzyB().zzj("Deleted stale rows. rowsDeleted", Integer.valueOf(iDelete));
            }
        }
    }

    @WorkerThread
    public final long zzye() {
        return zza("select max(bundle_end_timestamp) from queue", (String[]) null, 0L);
    }

    @WorkerThread
    public final long zzyf() {
        return zza("select max(timestamp) from raw_events", (String[]) null, 0L);
    }

    public final boolean zzyg() {
        return zzb("select count(1) > 0 from raw_events", (String[]) null) != 0;
    }

    public final boolean zzyh() {
        return zzb("select count(1) > 0 from raw_events where realtime = 1", (String[]) null) != 0;
    }

    public final long zzyi() {
        long j = -1;
        Cursor cursorRawQuery = null;
        try {
            try {
                cursorRawQuery = getWritableDatabase().rawQuery("select rowid from raw_events order by rowid desc limit 1;", null);
                if (cursorRawQuery.moveToFirst()) {
                    j = cursorRawQuery.getLong(0);
                    if (cursorRawQuery != null) {
                        cursorRawQuery.close();
                    }
                }
            } catch (SQLiteException e) {
                zzwE().zzyv().zzj("Error querying raw events", e);
                if (cursorRawQuery != null) {
                    cursorRawQuery.close();
                }
            }
            return j;
        } finally {
            if (cursorRawQuery != null) {
                cursorRawQuery.close();
            }
        }
    }
}
