package com.google.android.gms.internal;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.support.annotation.WorkerThread;
import com.kopin.pupil.aria.messages.Messages;

/* JADX INFO: loaded from: classes36.dex */
@TargetApi(11)
final class zzcgu extends SQLiteOpenHelper {
    private /* synthetic */ zzcgt zzbqL;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    zzcgu(zzcgt zzcgtVar, Context context, String str) {
        super(context, str, (SQLiteDatabase.CursorFactory) null, 1);
        this.zzbqL = zzcgtVar;
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    @WorkerThread
    public final SQLiteDatabase getWritableDatabase() {
        try {
            return super.getWritableDatabase();
        } catch (SQLiteException e) {
            if (Build.VERSION.SDK_INT >= 11 && (e instanceof SQLiteDatabaseLockedException)) {
                throw e;
            }
            this.zzbqL.zzwE().zzyv().log("Opening the local database failed, dropping and recreating it");
            String strZzxC = zzcfy.zzxC();
            if (!this.zzbqL.getContext().getDatabasePath(strZzxC).delete()) {
                this.zzbqL.zzwE().zzyv().zzj("Failed to delete corrupted local db file", strZzxC);
            }
            try {
                return super.getWritableDatabase();
            } catch (SQLiteException e2) {
                this.zzbqL.zzwE().zzyv().zzj("Failed to open local database. Events will bypass local storage", e2);
                return null;
            }
        }
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    @WorkerThread
    public final void onCreate(SQLiteDatabase sQLiteDatabase) {
        zzcfz.zza(this.zzbqL.zzwE(), sQLiteDatabase);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    @WorkerThread
    public final void onDowngrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    @WorkerThread
    public final void onOpen(SQLiteDatabase sQLiteDatabase) {
        if (Build.VERSION.SDK_INT < 15) {
            Cursor cursorRawQuery = sQLiteDatabase.rawQuery("PRAGMA journal_mode=memory", null);
            try {
                cursorRawQuery.moveToFirst();
            } finally {
                cursorRawQuery.close();
            }
        }
        zzcfz.zza(this.zzbqL.zzwE(), sQLiteDatabase, Messages.APP_NAME, "create table if not exists messages ( type INTEGER NOT NULL, entry BLOB NOT NULL)", "type,entry", null);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    @WorkerThread
    public final void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }
}
