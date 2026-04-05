package com.ua.sdk.activitytype;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.ua.sdk.cache.database.LegacyEntityDatabase;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public class ActivityTypeDatabase extends LegacyEntityDatabase<ActivityType> {
    private static final String ACTIVITY_DATABASE_NAME = "activity_database";
    private static final int ACTIVITY_DATABASE_VERSION = 4;
    private static final String ACTIVITY_TABLE = "activity";
    private static final String CACHED_TABLE = "activity_types_cached";
    private static final int COL_ACCESSED_DATE_MS = 5;
    private static final int COL_ACTIVITY_ID = 1;
    private static final int COL_HAS_CHILDREN = 6;
    private static final int COL_ID = 0;
    private static final int COL_METS = 7;
    private static final int COL_METS_SPEED = 8;
    private static final int COL_NAME = 2;
    private static final int COL_PARENT_ID = 4;
    private static final int COL_SHORT_NAME = 3;
    private static final String GET_CACHE_QUERY = "SELECT cached, cached_date FROM activity_types_cached WHERE _id = 1";
    private static final String KEY_ACTIVITY_ID = "id";
    private static final String KEY_CACHED = "cached";
    private static final String KEY_CACHED_DATE = "cached_date";
    private static final String KEY_ID = "_id";
    private static final String KEY_NAME = "name";
    private static final int ROW_ID = 1;
    private static ActivityTypeDatabase mInstance;
    private static final String KEY_SHORT_NAME = "short_name";
    private static final String KEY_PARENT_ID = "parent_id";
    private static final String KEY_ACCESSED_DATE_MS = "accessed_date_ms";
    private static final String KEY_HAS_CHILDREN = "has_children";
    private static final String KEY_METS = "mets";
    private static final String KEY_METS_SPEED = "mets_speed";
    private static final String[] COLUMNS = {"_id", "id", "name", KEY_SHORT_NAME, KEY_PARENT_ID, KEY_ACCESSED_DATE_MS, KEY_HAS_CHILDREN, KEY_METS, KEY_METS_SPEED};

    public static ActivityTypeDatabase getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ActivityTypeDatabase(context.getApplicationContext());
        }
        return mInstance;
    }

    ActivityTypeDatabase(Context context) {
        super(context, ACTIVITY_DATABASE_NAME, "activity", COLUMNS, "id", 4);
    }

    @Override // com.ua.sdk.cache.database.LegacyEntityDatabase
    public void createEntityTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE activity(_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,id NUMERIC KEY,parent_id NUMERIC,name TEXT,short_name TEXT,accessed_date_ms NUMERIC,has_children BOOLEAN,mets NUMERIC,mets_speed TEXT)");
        db.execSQL("CREATE TABLE activity_types_cached(_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, cached INTEGER NOT NULL DEFAULT 0, cached_date INTEGER NOT NULL DEFAULT 0)");
    }

    @Override // com.ua.sdk.cache.database.LegacyEntityDatabase
    public void onEntityUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS activity");
        db.execSQL("DROP TABLE IF EXISTS activity_types_cached");
        createEntityTable(db);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.ua.sdk.cache.database.LegacyEntityDatabase
    public ContentValues getContentValuesFromEntity(ActivityType activityTypeImpl) {
        ContentValues values = new ContentValues();
        values.put("id", activityTypeImpl.getActivityId());
        values.put(KEY_PARENT_ID, activityTypeImpl.getParentActivityId());
        values.put("name", activityTypeImpl.getName());
        values.put(KEY_SHORT_NAME, activityTypeImpl.getShortName());
        values.put(KEY_METS, activityTypeImpl.getMetsValue());
        values.put(KEY_METS_SPEED, activityTypeImpl.getMetsSpeed());
        Date accessedDate = activityTypeImpl.getAccessedDate();
        if (accessedDate != null) {
            values.put(KEY_ACCESSED_DATE_MS, Long.valueOf(accessedDate.getTime()));
        }
        values.put(KEY_HAS_CHILDREN, Integer.valueOf(activityTypeImpl.hasChildren() ? 1 : 0));
        return values;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.ua.sdk.cache.database.LegacyEntityDatabase
    public ActivityTypeImpl getEntityFromCursor(Cursor c) {
        Date accessedDate = null;
        if (!c.isNull(5)) {
            long accessedDateMs = c.getLong(5);
            accessedDate = new Date(accessedDateMs);
        }
        ActivityTypeImpl activityTypeImpl = new ActivityTypeImpl(c.getString(1), c.getString(4), c.getString(2), c.getString(3), Double.valueOf(c.getDouble(7)), c.getString(8), Boolean.valueOf(c.getInt(6) == 1), accessedDate);
        activityTypeImpl.setLocalId(c.getLong(0));
        return activityTypeImpl;
    }

    private Cursor getReadOnlyCacheCursor() {
        Cursor c = getReadableDatabase().rawQuery(GET_CACHE_QUERY, null);
        if (c.getCount() == 0) {
            return null;
        }
        return c;
    }

    protected boolean isAllActivityTypeCacheSet() {
        boolean cacheSet = false;
        Cursor c = getReadOnlyCacheCursor();
        if (c != null) {
            try {
                c.moveToNext();
                cacheSet = c.getInt(c.getColumnIndex(KEY_CACHED)) == 1;
            } finally {
                c.close();
            }
        }
        return cacheSet;
    }

    protected long getCacheAge() {
        Cursor c = getReadOnlyCacheCursor();
        if (c == null) {
            return -1L;
        }
        try {
            c.moveToNext();
            long age = System.currentTimeMillis() - c.getLong(c.getColumnIndex(KEY_CACHED_DATE));
            return age;
        } finally {
            c.close();
        }
    }

    protected void setAllActivityTypesCached(boolean cached) {
        String query = "INSERT OR REPLACE INTO activity_types_cached (_id, cached, cached_date) VALUES (1, " + (cached ? 1 : 0) + ", " + System.currentTimeMillis() + ')';
        getWritableDatabase().execSQL(query);
        close();
    }

    /* JADX WARN: Generic types in debug info not equals: java.lang.Object != java.util.List<com.ua.sdk.activitytype.ActivityType> */
    protected List<ActivityType> getAllActivityTypes() {
        List<ActivityType> result = new ArrayList<>();
        if (!isAllActivityTypeCacheSet()) {
            return result;
        }
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor c = db.rawQuery("SELECT _id, id, name, short_name, parent_id, accessed_date_ms, has_children, mets, mets_speed FROM activity ORDER BY _id ASC", null);
            ArrayList<ActivityTypeImpl> activityTypes = new ArrayList<>();
            while (c.moveToNext()) {
                ActivityTypeImpl activityType = getEntityFromCursor(c);
                activityTypes.add(activityType);
            }
            setAllLinkMaps(db, activityTypes);
            List<ActivityType> result2 = new ArrayList<>(activityTypes);
            return result2;
        } finally {
            close();
        }
    }
}
