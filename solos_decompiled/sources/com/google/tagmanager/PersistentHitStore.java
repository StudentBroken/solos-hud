package com.google.tagmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWindow;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.text.TextUtils;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.tagmanager.SimpleNetworkDispatcher;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.http.impl.client.DefaultHttpClient;

/* JADX INFO: loaded from: classes49.dex */
class PersistentHitStore implements HitStore {
    private static final String DATABASE_FILENAME = "gtm_urls.db";
    static final long HIT_DISPATCH_RETRY_WINDOW = 14400000;
    private static final String HIT_ID_WHERE_CLAUSE = "hit_id=?";
    private Clock mClock;
    private final Context mContext;
    private final String mDatabaseName;
    private final UrlDatabaseHelper mDbHelper;
    private volatile Dispatcher mDispatcher;
    private long mLastDeleteStaleHitsTime;
    private final HitStoreStateListener mListener;

    @VisibleForTesting
    static final String HITS_TABLE = "gtm_hits";

    @VisibleForTesting
    static final String HIT_ID = "hit_id";

    @VisibleForTesting
    static final String HIT_TIME = "hit_time";

    @VisibleForTesting
    static final String HIT_URL = "hit_url";

    @VisibleForTesting
    static final String HIT_FIRST_DISPATCH_TIME = "hit_first_send_time";
    private static final String CREATE_HITS_TABLE = String.format("CREATE TABLE IF NOT EXISTS %s ( '%s' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, '%s' INTEGER NOT NULL, '%s' TEXT NOT NULL,'%s' INTEGER NOT NULL);", HITS_TABLE, HIT_ID, HIT_TIME, HIT_URL, HIT_FIRST_DISPATCH_TIME);

    PersistentHitStore(HitStoreStateListener listener, Context ctx) {
        this(listener, ctx, DATABASE_FILENAME);
    }

    @VisibleForTesting
    PersistentHitStore(HitStoreStateListener listener, Context ctx, String databaseName) {
        this.mContext = ctx.getApplicationContext();
        this.mDatabaseName = databaseName;
        this.mListener = listener;
        this.mClock = new Clock() { // from class: com.google.tagmanager.PersistentHitStore.1
            @Override // com.google.tagmanager.Clock
            public long currentTimeMillis() {
                return System.currentTimeMillis();
            }
        };
        this.mDbHelper = new UrlDatabaseHelper(this.mContext, this.mDatabaseName);
        this.mDispatcher = new SimpleNetworkDispatcher(new DefaultHttpClient(), this.mContext, new StoreDispatchListener());
        this.mLastDeleteStaleHitsTime = 0L;
    }

    @VisibleForTesting
    public void setClock(Clock clock) {
        this.mClock = clock;
    }

    @VisibleForTesting
    public UrlDatabaseHelper getDbHelper() {
        return this.mDbHelper;
    }

    @VisibleForTesting
    void setDispatcher(Dispatcher dispatcher) {
        this.mDispatcher = dispatcher;
    }

    @Override // com.google.tagmanager.HitStore
    public void putHit(long hitTimeInMilliseconds, String path) {
        deleteStaleHits();
        removeOldHitIfFull();
        writeHitToDatabase(hitTimeInMilliseconds, path);
    }

    private void removeOldHitIfFull() {
        int hitsOverLimit = (getNumStoredHits() - 2000) + 1;
        if (hitsOverLimit > 0) {
            List<String> hitsToDelete = peekHitIds(hitsOverLimit);
            Log.v("Store full, deleting " + hitsToDelete.size() + " hits to make room.");
            deleteHits((String[]) hitsToDelete.toArray(new String[0]));
        }
    }

    private void writeHitToDatabase(long hitTimeInMilliseconds, String path) {
        SQLiteDatabase db = getWritableDatabase("Error opening database for putHit");
        if (db != null) {
            ContentValues content = new ContentValues();
            content.put(HIT_TIME, Long.valueOf(hitTimeInMilliseconds));
            content.put(HIT_URL, path);
            content.put(HIT_FIRST_DISPATCH_TIME, (Integer) 0);
            try {
                db.insert(HITS_TABLE, null, content);
                this.mListener.reportStoreIsEmpty(false);
            } catch (SQLiteException e) {
                Log.w("Error storing hit");
            }
        }
    }

    List<String> peekHitIds(int maxHits) {
        List<String> hitIds = new ArrayList<>();
        if (maxHits <= 0) {
            Log.w("Invalid maxHits specified. Skipping");
        } else {
            SQLiteDatabase db = getWritableDatabase("Error opening database for peekHitIds.");
            if (db != null) {
                Cursor cursor = null;
                try {
                    try {
                        cursor = db.query(HITS_TABLE, new String[]{HIT_ID}, null, null, null, null, String.format("%s ASC", HIT_ID), Integer.toString(maxHits));
                        if (cursor.moveToFirst()) {
                            do {
                                hitIds.add(String.valueOf(cursor.getLong(0)));
                            } while (cursor.moveToNext());
                        }
                        if (cursor != null) {
                            cursor.close();
                        }
                    } catch (SQLiteException e) {
                        Log.w("Error in peekHits fetching hitIds: " + e.getMessage());
                        if (cursor != null) {
                            cursor.close();
                        }
                    }
                } catch (Throwable th) {
                    if (cursor != null) {
                        cursor.close();
                    }
                    throw th;
                }
            }
        }
        return hitIds;
    }

    public List<Hit> peekHits(int maxHits) throws Throwable {
        List<Hit> hits;
        List<Hit> hits2 = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase("Error opening database for peekHits");
        if (db == null) {
            return hits2;
        }
        Cursor cursor = null;
        try {
            try {
                cursor = db.query(HITS_TABLE, new String[]{HIT_ID, HIT_TIME, HIT_FIRST_DISPATCH_TIME}, null, null, null, null, String.format("%s ASC", HIT_ID), Integer.toString(maxHits));
                hits = new ArrayList<>();
            } catch (Throwable th) {
                th = th;
            }
        } catch (SQLiteException e) {
            e = e;
        }
        try {
            if (cursor.moveToFirst()) {
                do {
                    hits.add(new Hit(cursor.getLong(0), cursor.getLong(1), cursor.getLong(2)));
                } while (cursor.moveToNext());
            }
            if (cursor != null) {
                cursor.close();
            }
            int count = 0;
            try {
                try {
                    cursor = db.query(HITS_TABLE, new String[]{HIT_ID, HIT_URL}, null, null, null, null, String.format("%s ASC", HIT_ID), Integer.toString(maxHits));
                    if (cursor.moveToFirst()) {
                        do {
                            CursorWindow cw = ((SQLiteCursor) cursor).getWindow();
                            if (cw.getNumRows() > 0) {
                                hits.get(count).setHitUrl(cursor.getString(1));
                            } else {
                                Log.w(String.format("HitString for hitId %d too large.  Hit will be deleted.", Long.valueOf(hits.get(count).getHitId())));
                            }
                            count++;
                        } while (cursor.moveToNext());
                    }
                    if (cursor != null) {
                        cursor.close();
                    }
                } catch (SQLiteException e2) {
                    Log.w("Error in peekHits fetching hit url: " + e2.getMessage());
                    List<Hit> partialHits = new ArrayList<>();
                    boolean foundOneBadHit = false;
                    for (Hit hit : hits) {
                        if (TextUtils.isEmpty(hit.getHitUrl())) {
                            if (foundOneBadHit) {
                                break;
                            }
                            foundOneBadHit = true;
                        }
                        partialHits.add(hit);
                    }
                    if (cursor != null) {
                        cursor.close();
                    }
                    hits = partialHits;
                }
            } catch (Throwable th2) {
                if (cursor != null) {
                    cursor.close();
                }
                throw th2;
            }
        } catch (SQLiteException e3) {
            e = e3;
            hits2 = hits;
            Log.w("Error in peekHits fetching hitIds: " + e.getMessage());
            if (cursor != null) {
                cursor.close();
            }
            hits = hits2;
        } catch (Throwable th3) {
            th = th3;
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
        return hits;
    }

    @VisibleForTesting
    void setLastDeleteStaleHitsTime(long timeInMilliseconds) {
        this.mLastDeleteStaleHitsTime = timeInMilliseconds;
    }

    int deleteStaleHits() {
        long now = this.mClock.currentTimeMillis();
        if (now <= this.mLastDeleteStaleHitsTime + 86400000) {
            return 0;
        }
        this.mLastDeleteStaleHitsTime = now;
        SQLiteDatabase db = getWritableDatabase("Error opening database for deleteStaleHits.");
        if (db == null) {
            return 0;
        }
        long lastGoodTime = this.mClock.currentTimeMillis() - 2592000000L;
        int rslt = db.delete(HITS_TABLE, "HIT_TIME < ?", new String[]{Long.toString(lastGoodTime)});
        this.mListener.reportStoreIsEmpty(getNumStoredHits() == 0);
        return rslt;
    }

    void deleteHits(String[] hitIds) {
        SQLiteDatabase db;
        if (hitIds != null && hitIds.length != 0 && (db = getWritableDatabase("Error opening database for deleteHits.")) != null) {
            String whereClause = String.format("HIT_ID in (%s)", TextUtils.join(",", Collections.nCopies(hitIds.length, "?")));
            try {
                db.delete(HITS_TABLE, whereClause, hitIds);
                this.mListener.reportStoreIsEmpty(getNumStoredHits() == 0);
            } catch (SQLiteException e) {
                Log.w("Error deleting hits");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deleteHit(long hitId) {
        deleteHits(new String[]{String.valueOf(hitId)});
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setHitFirstDispatchTime(long hitId, long firstDispatchTime) {
        SQLiteDatabase db = getWritableDatabase("Error opening database for getNumStoredHits.");
        if (db != null) {
            ContentValues cv = new ContentValues();
            cv.put(HIT_FIRST_DISPATCH_TIME, Long.valueOf(firstDispatchTime));
            try {
                db.update(HITS_TABLE, cv, HIT_ID_WHERE_CLAUSE, new String[]{String.valueOf(hitId)});
            } catch (SQLiteException e) {
                Log.w("Error setting HIT_FIRST_DISPATCH_TIME for hitId: " + hitId);
                deleteHit(hitId);
            }
        }
    }

    int getNumStoredHits() {
        int numStoredHits = 0;
        SQLiteDatabase db = getWritableDatabase("Error opening database for getNumStoredHits.");
        if (db == null) {
            return 0;
        }
        Cursor cursor = null;
        try {
            try {
                cursor = db.rawQuery("SELECT COUNT(*) from gtm_hits", null);
                if (cursor.moveToFirst()) {
                    numStoredHits = (int) cursor.getLong(0);
                }
            } catch (SQLiteException e) {
                Log.w("Error getting numStoredHits");
                if (cursor != null) {
                    cursor.close();
                }
            }
            return numStoredHits;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    int getNumStoredUntriedHits() {
        int numStoredHits = 0;
        SQLiteDatabase db = getWritableDatabase("Error opening database for getNumStoredHits.");
        if (db == null) {
            return 0;
        }
        Cursor cursor = null;
        try {
            try {
                cursor = db.query(HITS_TABLE, new String[]{HIT_ID, HIT_FIRST_DISPATCH_TIME}, "hit_first_send_time=0", null, null, null, null);
                numStoredHits = cursor.getCount();
            } catch (SQLiteException e) {
                Log.w("Error getting num untried hits");
                if (cursor != null) {
                    cursor.close();
                }
            }
            return numStoredHits;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override // com.google.tagmanager.HitStore
    public void dispatch() throws Throwable {
        Log.v("GTM Dispatch running...");
        if (this.mDispatcher.okToDispatch()) {
            List<Hit> hits = peekHits(40);
            if (hits.isEmpty()) {
                Log.v("...nothing to dispatch");
                this.mListener.reportStoreIsEmpty(true);
            } else {
                this.mDispatcher.dispatchHits(hits);
                if (getNumStoredUntriedHits() > 0) {
                    ServiceManagerImpl.getInstance().dispatch();
                }
            }
        }
    }

    @VisibleForTesting
    class StoreDispatchListener implements SimpleNetworkDispatcher.DispatchListener {
        StoreDispatchListener() {
        }

        @Override // com.google.tagmanager.SimpleNetworkDispatcher.DispatchListener
        public void onHitDispatched(Hit hit) {
            PersistentHitStore.this.deleteHit(hit.getHitId());
        }

        @Override // com.google.tagmanager.SimpleNetworkDispatcher.DispatchListener
        public void onHitPermanentDispatchFailure(Hit hit) {
            PersistentHitStore.this.deleteHit(hit.getHitId());
            Log.v("Permanent failure dispatching hitId: " + hit.getHitId());
        }

        @Override // com.google.tagmanager.SimpleNetworkDispatcher.DispatchListener
        public void onHitTransientDispatchFailure(Hit hit) {
            long firstDispatchTime = hit.getHitFirstDispatchTime();
            if (firstDispatchTime == 0) {
                PersistentHitStore.this.setHitFirstDispatchTime(hit.getHitId(), PersistentHitStore.this.mClock.currentTimeMillis());
            } else if (PersistentHitStore.HIT_DISPATCH_RETRY_WINDOW + firstDispatchTime < PersistentHitStore.this.mClock.currentTimeMillis()) {
                PersistentHitStore.this.deleteHit(hit.getHitId());
                Log.v("Giving up on failed hitId: " + hit.getHitId());
            }
        }
    }

    @Override // com.google.tagmanager.HitStore
    public Dispatcher getDispatcher() {
        return this.mDispatcher;
    }

    @Override // com.google.tagmanager.HitStore
    public void close() {
        try {
            this.mDbHelper.getWritableDatabase().close();
            this.mDispatcher.close();
        } catch (SQLiteException e) {
            Log.w("Error opening database for close");
        }
    }

    @VisibleForTesting
    UrlDatabaseHelper getHelper() {
        return this.mDbHelper;
    }

    private SQLiteDatabase getWritableDatabase(String errorMessage) {
        try {
            SQLiteDatabase db = this.mDbHelper.getWritableDatabase();
            return db;
        } catch (SQLiteException e) {
            Log.w(errorMessage);
            return null;
        }
    }

    @VisibleForTesting
    class UrlDatabaseHelper extends SQLiteOpenHelper {
        private boolean mBadDatabase;
        private long mLastDatabaseCheckTime;

        boolean isBadDatabase() {
            return this.mBadDatabase;
        }

        void setBadDatabase(boolean badDatabase) {
            this.mBadDatabase = badDatabase;
        }

        UrlDatabaseHelper(Context context, String databaseName) {
            super(context, databaseName, (SQLiteDatabase.CursorFactory) null, 1);
            this.mLastDatabaseCheckTime = 0L;
        }

        private boolean tablePresent(String table, SQLiteDatabase db) {
            boolean zMoveToFirst;
            Cursor cursor = null;
            try {
                try {
                    cursor = db.query("SQLITE_MASTER", new String[]{"name"}, "name=?", new String[]{table}, null, null, null);
                    zMoveToFirst = cursor.moveToFirst();
                    if (cursor != null) {
                        cursor.close();
                    }
                } catch (SQLiteException e) {
                    Log.w("Error querying for table " + table);
                    if (cursor != null) {
                        cursor.close();
                    }
                    zMoveToFirst = false;
                }
                return zMoveToFirst;
            } catch (Throwable th) {
                if (cursor != null) {
                    cursor.close();
                }
                throw th;
            }
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public SQLiteDatabase getWritableDatabase() {
            if (this.mBadDatabase && this.mLastDatabaseCheckTime + 3600000 > PersistentHitStore.this.mClock.currentTimeMillis()) {
                throw new SQLiteException("Database creation failed");
            }
            SQLiteDatabase db = null;
            this.mBadDatabase = true;
            this.mLastDatabaseCheckTime = PersistentHitStore.this.mClock.currentTimeMillis();
            try {
                db = super.getWritableDatabase();
            } catch (SQLiteException e) {
                PersistentHitStore.this.mContext.getDatabasePath(PersistentHitStore.this.mDatabaseName).delete();
            }
            if (db == null) {
                db = super.getWritableDatabase();
            }
            this.mBadDatabase = false;
            return db;
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onOpen(SQLiteDatabase db) {
            if (Build.VERSION.SDK_INT < 15) {
                Cursor cursor = db.rawQuery("PRAGMA journal_mode=memory", null);
                try {
                    cursor.moveToFirst();
                } finally {
                    cursor.close();
                }
            }
            if (!tablePresent(PersistentHitStore.HITS_TABLE, db)) {
                db.execSQL(PersistentHitStore.CREATE_HITS_TABLE);
            } else {
                validateColumnsPresent(db);
            }
        }

        private void validateColumnsPresent(SQLiteDatabase db) {
            Cursor c = db.rawQuery("SELECT * FROM gtm_hits WHERE 0", null);
            Set<String> columns = new HashSet<>();
            try {
                String[] columnNames = c.getColumnNames();
                for (String str : columnNames) {
                    columns.add(str);
                }
                c.close();
                if (!columns.remove(PersistentHitStore.HIT_ID) || !columns.remove(PersistentHitStore.HIT_URL) || !columns.remove(PersistentHitStore.HIT_TIME) || !columns.remove(PersistentHitStore.HIT_FIRST_DISPATCH_TIME)) {
                    throw new SQLiteException("Database column missing");
                }
                if (!columns.isEmpty()) {
                    throw new SQLiteException("Database has extra columns");
                }
            } catch (Throwable th) {
                c.close();
                throw th;
            }
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onCreate(SQLiteDatabase db) {
            FutureApis.setOwnerOnlyReadWrite(db.getPath());
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
