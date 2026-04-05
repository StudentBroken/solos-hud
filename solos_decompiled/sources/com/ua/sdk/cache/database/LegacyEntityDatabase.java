package com.ua.sdk.cache.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import com.ua.sdk.EntityList;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.LocalDate;
import com.ua.sdk.Reference;
import com.ua.sdk.Resource;
import com.ua.sdk.UaLog;
import com.ua.sdk.cache.DiskCache;
import com.ua.sdk.cache.database.definition.ColumnDefinition;
import com.ua.sdk.internal.ApiTransferObject;
import com.ua.sdk.internal.Link;
import com.ua.sdk.internal.LinkEntityRef;
import com.ua.sdk.internal.Precondition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes65.dex */
public abstract class LegacyEntityDatabase<T extends Resource> extends SQLiteOpenHelper implements DiskCache<T> {
    protected static final int ENTITY_DATABASE_VERSION = 0;
    private static final String KEY_LINK_FOREIGN_KEY = "foreign_key";
    private static final String KEY_LINK_HREF = "href";
    private static final String KEY_LINK_ID = "id";
    private static final String KEY_LINK_NAME = "name";
    private static final String KEY_LINK_RELATION = "relation";
    private static final String KEY_META_ID = "id";
    private static final String KEY_META_LAST_UPDATE_TIME_MS = "last_update_time_ms";
    private static final String KEY_META_PENDING_OPERATION = "pending_operation";
    private static final String TABLE_LINKS = "links";
    private static final String TABLE_META = "meta";
    private final String[] mEntityCols;
    private final String mEntityKeyCol;
    private final String mEntityTable;
    private static final Integer STATE_SYNCED = 0;
    private static final Integer STATE_CREATED = 1;
    private static final Integer STATE_MODIFIED = 2;
    private static final Integer STATE_DELETED = 4;

    protected abstract void createEntityTable(SQLiteDatabase sQLiteDatabase);

    protected abstract ContentValues getContentValuesFromEntity(T t);

    protected abstract T getEntityFromCursor(Cursor cursor);

    public abstract void onEntityUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2);

    protected LegacyEntityDatabase(Context context, String databaseName, String entityTable, String[] entityCols, String entityKeyCol, int version) {
        super(context, databaseName, (SQLiteDatabase.CursorFactory) null, getCombinedVersion(0, version));
        this.mEntityTable = entityTable;
        this.mEntityKeyCol = entityKeyCol;
        this.mEntityCols = entityCols;
        if (Arrays.binarySearch(entityCols, "_id") < 0) {
            throw new IllegalArgumentException("entityCols do not contain _id");
        }
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public final void onCreate(SQLiteDatabase db) {
        createMetaTables(db);
        createEntityTable(db);
    }

    private void createMetaTables(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE links(foreign_key INTEGER NOT NULL,relation TEXT,href TEXT,id TEXT,name TEXT)");
        db.execSQL("CREATE TABLE meta(id INTEGER PRIMARY KEY UNIQUE NOT NULL,pending_operation NUMERIC,last_update_time_ms NUMERIC)");
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public final void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        int newMyVersion = getMyVersion(newVersion);
        int oldMyVersion = getMyVersion(oldVersion);
        if (newMyVersion > oldMyVersion) {
            db.execSQL("DROP TABLE IF EXISTS links");
            db.execSQL("DROP TABLE IF EXISTS meta");
            createMetaTables(db);
        }
        int newSubVersion = getSubVersion(newVersion);
        int oldSubVersion = getSubVersion(oldVersion);
        if (newSubVersion > oldSubVersion) {
            onEntityUpgrade(db, oldSubVersion, newSubVersion);
        }
    }

    private static int getCombinedVersion(int myVersion, int subVersion) {
        return (subVersion << 15) | myVersion;
    }

    private static int getMyVersion(int version) {
        return version & 8191;
    }

    private static int getSubVersion(int version) {
        return (536805376 & version) >> 15;
    }

    public static String buildCreateStatement(String tableName, ColumnDefinition[] columnDefinitions) {
        StringBuilder answer = new StringBuilder();
        answer.append("CREATE TABLE ");
        answer.append(tableName);
        answer.append(" (");
        for (int i = 0; i < columnDefinitions.length; i++) {
            ColumnDefinition col = columnDefinitions[i];
            answer.append(col.getColumnName());
            answer.append(" ");
            answer.append(col.getDbType());
            if (i + 1 < columnDefinitions.length) {
                answer.append(",");
            }
        }
        answer.append(")");
        return answer.toString();
    }

    public static String[] buildColumnNames(ColumnDefinition[] columnDefinitions) {
        String[] answer = new String[columnDefinitions.length];
        for (int i = 0; i < columnDefinitions.length; i++) {
            ColumnDefinition col = columnDefinitions[i];
            answer[i] = col.getColumnName();
        }
        return answer;
    }

    private void insertOrReplaceMetadataAfterFetch(SQLiteDatabase db, long foreignKey) {
        insertOrReplaceMetadata(db, foreignKey, System.currentTimeMillis(), DiskCache.State.SYNCED);
    }

    private void insertOrReplaceMetadata(SQLiteDatabase db, long foreignKey, DiskCache.State state) {
        ContentValues values = new ContentValues();
        values.put("pending_operation", stateToDatabaseValue(state));
        int updated = db.update(TABLE_META, values, "id=?", new String[]{String.valueOf(foreignKey)});
        if (updated == 0) {
            values.put("id", Long.valueOf(foreignKey));
            db.insert(TABLE_META, null, values);
        }
    }

    private void insertOrReplaceMetadata(SQLiteDatabase db, long foreignKey, long lastUpdateFromServerMs, DiskCache.State state) {
        ContentValues values = new ContentValues();
        values.put("id", Long.valueOf(foreignKey));
        values.put("pending_operation", stateToDatabaseValue(state));
        values.put(KEY_META_LAST_UPDATE_TIME_MS, Long.valueOf(lastUpdateFromServerMs));
        db.insertWithOnConflict(TABLE_META, null, values, 5);
    }

    private Integer stateToDatabaseValue(DiskCache.State state) {
        if (state == null) {
            return null;
        }
        switch (state) {
        }
        return null;
    }

    private DiskCache.State stateFromDatabaseValue(int state) {
        if (state == STATE_CREATED.intValue()) {
            return DiskCache.State.CREATED;
        }
        if (state == STATE_DELETED.intValue()) {
            return DiskCache.State.DELETED;
        }
        if (state == STATE_MODIFIED.intValue()) {
            return DiskCache.State.MODIFIED;
        }
        if (state == STATE_SYNCED.intValue()) {
            return DiskCache.State.SYNCED;
        }
        return DiskCache.State.NONE;
    }

    private void bulkInsertLinks(SQLiteDatabase db, long foreignKey, ApiTransferObject entity) {
        SQLiteStatement statement = db.compileStatement("INSERT INTO links VALUES (?,?,?,?,?);");
        try {
            Map<String, ArrayList<Link>> linkMap = entity.getLinkMap();
            if (linkMap != null) {
                for (String key : linkMap.keySet()) {
                    List<Link> links = linkMap.get(key);
                    for (Link link : links) {
                        statement.clearBindings();
                        statement.bindLong(1, foreignKey);
                        statement.bindString(2, key);
                        nullSafeBind(statement, 3, link.getHref());
                        nullSafeBind(statement, 4, link.getId());
                        nullSafeBind(statement, 5, link.getName());
                        statement.execute();
                    }
                }
            }
        } finally {
            statement.close();
        }
    }

    private void nullSafeBind(SQLiteStatement statement, int index, String value) {
        if (value != null) {
            statement.bindString(index, value);
        } else {
            statement.bindNull(index);
        }
    }

    public synchronized Map<String, ArrayList<Link>> getLinkMap(Reference ref) {
        Map<String, ArrayList<Link>> mapEmptyMap;
        SQLiteDatabase db;
        try {
            db = getReadableDatabase();
        } catch (Throwable t) {
            UaLog.error("Unable to get link map.", t);
            mapEmptyMap = Collections.emptyMap();
        }
        try {
            long localId = getLocalId(db, ref);
            if (localId < 0) {
                mapEmptyMap = Collections.emptyMap();
            } else {
                mapEmptyMap = getLinkMap(db, localId);
                close();
            }
        } finally {
            close();
        }
        return mapEmptyMap;
    }

    protected Map<String, ArrayList<Link>> getLinkMap(SQLiteDatabase db, long id) {
        Cursor linksCursor = db.query(TABLE_LINKS, null, "foreign_key=?", new String[]{String.valueOf(id)}, null, null, null, null);
        Map<String, ArrayList<Link>> linkMap = new HashMap<>();
        while (linksCursor.moveToNext()) {
            try {
                String relation = linksCursor.getString(linksCursor.getColumnIndex(KEY_LINK_RELATION));
                String href = linksCursor.getString(linksCursor.getColumnIndex("href"));
                String linkId = linksCursor.getString(linksCursor.getColumnIndex("id"));
                String name = linksCursor.getString(linksCursor.getColumnIndex("name"));
                Link link = new Link(href, linkId, name);
                ArrayList<Link> links = linkMap.get(relation);
                if (links == null) {
                    links = new ArrayList<>();
                    linkMap.put(relation, links);
                }
                links.add(link);
            } finally {
                linksCursor.close();
            }
        }
        return linkMap;
    }

    protected <T extends ApiTransferObject> void setAllLinkMaps(SQLiteDatabase sQLiteDatabase, List<T> list) {
        Cursor cursorQuery = sQLiteDatabase.query(TABLE_LINKS, null, null, null, null, null, "foreign_key ASC", null);
        try {
            int columnIndex = cursorQuery.getColumnIndex(KEY_LINK_FOREIGN_KEY);
            int columnIndex2 = cursorQuery.getColumnIndex(KEY_LINK_RELATION);
            int columnIndex3 = cursorQuery.getColumnIndex("id");
            int columnIndex4 = cursorQuery.getColumnIndex("href");
            int columnIndex5 = cursorQuery.getColumnIndex("name");
            long j = -1;
            long localId = -1;
            int i = 0;
            int size = list.size();
            ApiTransferObject apiTransferObject = null;
            Map<String, ArrayList<Link>> map = null;
            Link link = null;
            String string = null;
            while (i < size && !cursorQuery.isLast()) {
                if (apiTransferObject == null) {
                    apiTransferObject = (ApiTransferObject) list.get(i);
                    map = new HashMap<>();
                    localId = apiTransferObject.getLocalId();
                }
                if (link == null) {
                    cursorQuery.moveToNext();
                    j = cursorQuery.getLong(columnIndex);
                    string = cursorQuery.getString(columnIndex2);
                    link = new Link(cursorQuery.getString(columnIndex4), cursorQuery.getString(columnIndex3), cursorQuery.getString(columnIndex5));
                }
                if (localId == j) {
                    ArrayList<Link> arrayList = map.get(string);
                    if (arrayList == null) {
                        arrayList = new ArrayList<>();
                        map.put(string, arrayList);
                    }
                    arrayList.add(link);
                    link = null;
                } else if (localId < j) {
                    if (!map.isEmpty()) {
                        apiTransferObject.setLinkMap(map);
                    }
                    apiTransferObject = null;
                    map = null;
                    i++;
                } else {
                    link = null;
                }
            }
            if (apiTransferObject != null && map != null) {
                apiTransferObject.setLinkMap(map);
            }
        } finally {
            cursorQuery.close();
        }
    }

    public synchronized void deleteAll() {
        try {
            SQLiteDatabase db = getWritableDatabase();
            try {
                deleteAllEntities(db);
                deleteAllLinks(db);
                deleteAllMetadata(db);
            } finally {
                close();
            }
        } catch (Throwable t) {
            UaLog.error("Unable to delete all entities.", t);
        }
    }

    protected void deleteAllLinksWithId(SQLiteDatabase db, long id) {
        if (id >= 0) {
            db.delete(TABLE_LINKS, "foreign_key=?", new String[]{Long.toString(id)});
        }
    }

    protected void deleteAllLinks(SQLiteDatabase db) {
        db.execSQL("DELETE FROM links");
    }

    protected void deleteAllMetadata(SQLiteDatabase db) {
        db.execSQL("DELETE FROM meta");
    }

    protected void deleteAllMetadataWithId(SQLiteDatabase db, long id) {
        if (id >= 0) {
            db.delete(TABLE_META, "id=?", new String[]{Long.toString(id)});
        }
    }

    protected long getLocalId(SQLiteDatabase db, Reference ref) {
        String id;
        long localId = -1;
        if ((ref instanceof LinkEntityRef) && ((LinkEntityRef) ref).checkCache()) {
            long id2 = ((LinkEntityRef) ref).getLocalId();
            if (id2 >= 0) {
                localId = id2;
            }
        }
        if (localId == -1 && (id = ref.getId()) != null) {
            Cursor cursor = db.query(this.mEntityTable, new String[]{"_id"}, this.mEntityKeyCol + "= '" + id + "'", null, null, null, null);
            try {
                if (cursor.moveToFirst()) {
                    localId = cursor.getLong(0);
                }
            } finally {
                cursor.close();
            }
        }
        return localId;
    }

    @Override // com.ua.sdk.cache.DiskCache
    public synchronized T get(Reference reference) {
        Object obj;
        SQLiteDatabase readableDatabase;
        Precondition.isNotNull(reference, "ref");
        obj = null;
        Cursor cursorQuery = null;
        boolean z = true;
        String str = null;
        String id = null;
        try {
            try {
                if (reference instanceof LinkEntityRef) {
                    if (!((LinkEntityRef) reference).checkCache()) {
                        z = false;
                    } else {
                        long localId = ((LinkEntityRef) reference).getLocalId();
                        if (localId >= 0) {
                            str = "_id";
                            id = String.valueOf(localId);
                        }
                    }
                }
                if (z) {
                    if (str == null) {
                        str = this.mEntityKeyCol;
                        id = reference.getId();
                    }
                    if (id == null) {
                        z = false;
                    }
                }
                if (z && (cursorQuery = (readableDatabase = getReadableDatabase()).query(this.mEntityTable, this.mEntityCols, str + "=?", new String[]{id}, null, null, null, null)) != null && cursorQuery.moveToFirst()) {
                    obj = (T) getEntityFromCursor(cursorQuery);
                    if (obj instanceof ApiTransferObject) {
                        ((ApiTransferObject) obj).setLinkMap(getLinkMap(readableDatabase, cursorQuery.getLong(cursorQuery.getColumnIndex("_id"))));
                    }
                }
            } finally {
                if (0 != 0) {
                    cursorQuery.close();
                }
                close();
            }
        } catch (Throwable th) {
            UaLog.error("Unable to get entity.", th);
            if (cursorQuery != null) {
                cursorQuery.close();
            }
            close();
        }
        return (T) obj;
    }

    @Override // com.ua.sdk.cache.DiskCache
    public EntityList<T> getList(Reference ref) {
        return null;
    }

    protected ContentValues getContentValuesFromEntity(long localId, T entity) {
        ContentValues values = getContentValuesFromEntity(entity);
        if (localId >= 0) {
            values.put("_id", Long.valueOf(localId));
        } else if (values.containsKey("_id")) {
            values.remove("_id");
        }
        return values;
    }

    protected long delete(SQLiteDatabase db, Reference ref) {
        int rowsChanged;
        long localId = getLocalId(db, ref);
        if (localId >= 0 && (rowsChanged = db.delete(this.mEntityTable, "_id=?", new String[]{Long.toString(localId)})) != 1) {
            UaLog.error("Failed to delete entity. refType=%s id=%s rowsChanged=%s", ref.getClass().getSimpleName(), Long.valueOf(localId), Integer.valueOf(rowsChanged));
        }
        return localId;
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected long insert(SQLiteDatabase db, T t) {
        ContentValues values = getContentValuesFromEntity(-1L, t);
        long localId = db.insert(this.mEntityTable, "_id", values);
        deleteAllLinksWithId(db, localId);
        if (t instanceof ApiTransferObject) {
            ((ApiTransferObject) t).setLocalId(localId);
            bulkInsertLinks(db, localId, (ApiTransferObject) t);
        }
        return localId;
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void update(SQLiteDatabase db, long localId, T t) {
        ContentValues values = getContentValuesFromEntity(localId, t);
        int rowsChanged = db.update(this.mEntityTable, values, "_id=" + localId, null);
        if (rowsChanged >= 1) {
            deleteAllLinksWithId(db, localId);
            if (t instanceof ApiTransferObject) {
                ((ApiTransferObject) t).setLocalId(localId);
                bulkInsertLinks(db, localId, (ApiTransferObject) t);
                return;
            }
            return;
        }
        UaLog.error("Failed to update entity. type=%s id=%s rowsChanged=%s", t.getClass().getSimpleName(), Long.valueOf(localId), Integer.valueOf(rowsChanged));
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected long insertOrUpdate(SQLiteDatabase db, T t) {
        ContentValues values = getContentValuesFromEntity(getLocalId(db, t.getRef()), t);
        long localId = db.insertWithOnConflict(this.mEntityTable, "_id", values, 5);
        deleteAllLinksWithId(db, localId);
        if (t instanceof ApiTransferObject) {
            ((ApiTransferObject) t).setLocalId(localId);
            bulkInsertLinks(db, localId, (ApiTransferObject) t);
        }
        return localId;
    }

    protected void deleteAllEntities(SQLiteDatabase db) {
        db.execSQL("DELETE FROM " + this.mEntityTable);
    }

    @Override // com.ua.sdk.cache.DiskCache
    public synchronized void updateAfterFetch(T entity) {
        SQLiteDatabase db = null;
        try {
            try {
                db = getWritableDatabase();
                db.beginTransaction();
                long localId = insertOrUpdate(db, entity);
                insertOrReplaceMetadataAfterFetch(db, localId);
                db.setTransactionSuccessful();
            } catch (Throwable t) {
                UaLog.error("Failed to update the cache after fetch.", t);
                if (db != null) {
                    db.endTransaction();
                }
                close();
            }
        } finally {
            if (db != null) {
                db.endTransaction();
            }
            close();
        }
    }

    @Override // com.ua.sdk.cache.DiskCache
    public synchronized void updateAfterFetch(EntityListRef<T> requestRef, EntityList<T> entities, boolean partial) {
        SQLiteDatabase db = null;
        try {
            try {
                db = getWritableDatabase();
                db.beginTransaction();
                for (T entity : entities.getAll()) {
                    long localId = getLocalId(db, entity.getRef());
                    if (partial) {
                        if (localId >= 0) {
                            update(db, localId, entity);
                        }
                    } else if (localId >= 0) {
                        update(db, localId, entity);
                        insertOrReplaceMetadataAfterFetch(db, localId);
                    } else {
                        insertOrReplaceMetadataAfterFetch(db, insert(db, entity));
                    }
                }
                db.setTransactionSuccessful();
                if (db != null) {
                    endTransaction(db);
                }
                close();
            } catch (Throwable t) {
                UaLog.error("Failed to update the cache after fetch.", t);
            }
        } finally {
            if (db != null) {
                endTransaction(db);
            }
            close();
        }
    }

    private static void endTransaction(SQLiteDatabase db) {
        try {
            db.endTransaction();
        } catch (Throwable e) {
            UaLog.error("Failed to end transaction.", e);
        }
    }

    @Override // com.ua.sdk.cache.DiskCache
    public synchronized void updateAfterCreate(long localId, T entity) {
        SQLiteDatabase db = null;
        try {
            try {
                db = getWritableDatabase();
                db.beginTransaction();
                update(db, localId, entity);
                insertOrReplaceMetadataAfterFetch(db, localId);
                db.setTransactionSuccessful();
            } catch (Throwable t) {
                UaLog.error("Failed to update the cache after create", t);
                if (db != null) {
                    endTransaction(db);
                }
                close();
            }
        } finally {
            if (db != null) {
                endTransaction(db);
            }
            close();
        }
    }

    @Override // com.ua.sdk.cache.DiskCache
    public synchronized long putForCreate(T entity) {
        long localId;
        localId = -1;
        SQLiteDatabase db = null;
        try {
            try {
                db = getWritableDatabase();
                db.beginTransaction();
                localId = insertOrUpdate(db, entity);
                insertOrReplaceMetadata(db, localId, DiskCache.State.CREATED);
                db.setTransactionSuccessful();
            } catch (Throwable t) {
                UaLog.error("Failed to put in cache for create.", t);
                if (db != null) {
                    endTransaction(db);
                }
                close();
            }
        } finally {
            if (db != null) {
                endTransaction(db);
            }
            close();
        }
        return localId;
    }

    @Override // com.ua.sdk.cache.DiskCache
    public synchronized void putForSave(T entity) {
        SQLiteDatabase db = null;
        try {
            try {
                db = getWritableDatabase();
                db.beginTransaction();
                long localId = insertOrUpdate(db, entity);
                insertOrReplaceMetadata(db, localId, DiskCache.State.MODIFIED);
                db.setTransactionSuccessful();
                if (db != null) {
                    endTransaction(db);
                }
                close();
            } catch (Throwable t) {
                UaLog.error("Failed to put in cache for save.", t);
                if (db != null) {
                    endTransaction(db);
                }
                close();
            }
        } catch (Throwable th) {
            if (db != null) {
                endTransaction(db);
            }
            close();
            throw th;
        }
    }

    @Override // com.ua.sdk.cache.DiskCache
    public synchronized void updateAfterSave(T entity) {
        SQLiteDatabase db = null;
        try {
            try {
                db = getWritableDatabase();
                db.beginTransaction();
                long localId = insertOrUpdate(db, entity);
                insertOrReplaceMetadataAfterFetch(db, localId);
                db.setTransactionSuccessful();
            } catch (Throwable t) {
                UaLog.error("Failed to update the cache after save", t);
                if (db != null) {
                    endTransaction(db);
                }
                close();
            }
        } finally {
            if (db != null) {
                endTransaction(db);
            }
            close();
        }
    }

    @Override // com.ua.sdk.cache.DiskCache
    public synchronized void markForDelete(Reference ref) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            long localId = getLocalId(db, ref);
            insertOrReplaceMetadata(db, localId, DiskCache.State.DELETED);
        } catch (Throwable t) {
            UaLog.error("Failed to mark the cache for delete.", t);
        } finally {
        }
    }

    @Override // com.ua.sdk.cache.DiskCache
    public synchronized void delete(Reference ref) {
        SQLiteDatabase db = null;
        try {
            try {
                db = getWritableDatabase();
                db.beginTransaction();
                long localId = delete(db, ref);
                deleteAllMetadataWithId(db, localId);
                deleteAllLinksWithId(db, localId);
                db.setTransactionSuccessful();
                if (db != null) {
                    endTransaction(db);
                }
                close();
            } catch (Throwable t) {
                UaLog.error("Failed to update the cache after delete", t);
                if (db != null) {
                    endTransaction(db);
                }
                close();
            }
        } catch (Throwable th) {
            if (db != null) {
                endTransaction(db);
            }
            close();
            throw th;
        }
    }

    @Override // com.ua.sdk.cache.DiskCache
    public void deleteList(EntityListRef<T> ref) {
    }

    @Override // com.ua.sdk.cache.DiskCache
    public synchronized long getLastSynced(Reference ref) {
        long lastSynced;
        lastSynced = -1;
        if (ref != null) {
            Cursor c = null;
            try {
                try {
                    SQLiteDatabase db = getReadableDatabase();
                    long localId = getLocalId(db, ref);
                    if (localId >= 0) {
                        c = db.query(TABLE_META, new String[]{KEY_META_LAST_UPDATE_TIME_MS}, "id=?", new String[]{Long.toString(localId)}, null, null, null);
                        if (c.moveToFirst() && !c.isNull(0)) {
                            lastSynced = c.getLong(0);
                        }
                    }
                } catch (Throwable t) {
                    UaLog.error("Unable to get last synced time.", t);
                    if (c != null) {
                        c.close();
                    }
                    close();
                    return lastSynced;
                }
            } finally {
                if (c != null) {
                    c.close();
                }
                close();
            }
        }
        return lastSynced;
    }

    @Override // com.ua.sdk.cache.DiskCache
    public long getCacheAge(Reference ref) {
        if (ref == null) {
            return -1L;
        }
        long lastSynced = getLastSynced(ref);
        if (lastSynced >= 0) {
            return System.currentTimeMillis() - lastSynced;
        }
        return -1L;
    }

    @Override // com.ua.sdk.cache.DiskCache
    public synchronized DiskCache.State getState(Reference ref) {
        DiskCache.State state;
        state = DiskCache.State.NONE;
        Cursor c = null;
        try {
            try {
                SQLiteDatabase db = getReadableDatabase();
                long localId = getLocalId(db, ref);
                if (localId >= 0) {
                    c = db.query(TABLE_META, new String[]{"pending_operation"}, "id=?", new String[]{Long.toString(localId)}, null, null, null);
                    if (c.moveToFirst() && !c.isNull(0)) {
                        state = stateFromDatabaseValue(c.getInt(0));
                    }
                }
                if (c != null) {
                    c.close();
                }
                close();
            } catch (Throwable t) {
                UaLog.error("Unable to get cache state.", t);
                if (c != null) {
                    c.close();
                }
                close();
            }
        } catch (Throwable th) {
            if (c != null) {
                c.close();
            }
            close();
            throw th;
        }
        return state;
    }

    public static String readString(int column, Cursor c) {
        if (c.isNull(column)) {
            return null;
        }
        return c.getString(column);
    }

    public static Boolean readBoolean(int column, Cursor c) {
        if (c.isNull(column)) {
            return null;
        }
        return Boolean.valueOf(c.getInt(column) == 1);
    }

    public static Integer readInteger(int column, Cursor c) {
        if (c.isNull(column)) {
            return null;
        }
        return Integer.valueOf(c.getInt(column));
    }

    public static Long readLong(int column, Cursor c) {
        if (c.isNull(column)) {
            return null;
        }
        return Long.valueOf(c.getLong(column));
    }

    public static Double readDouble(int column, Cursor c) {
        if (c.isNull(column)) {
            return null;
        }
        return Double.valueOf(c.getDouble(column));
    }

    public static Date readDate(int column, Cursor c) {
        if (c.isNull(column)) {
            return null;
        }
        long ms = c.getLong(column);
        return new Date(ms);
    }

    public static LocalDate readLocalDate(int column, Cursor c) {
        if (c.isNull(column)) {
            return null;
        }
        return LocalDate.fromString(c.getString(column));
    }

    /* JADX WARN: Incorrect return type in method signature: <T:Ljava/lang/Enum<TT;>;>(ILandroid/database/Cursor;Ljava/lang/Class<TT;>;)TT; */
    public static Enum readEnum(int column, Cursor c, Class cls) {
        if (c.isNull(column)) {
            return null;
        }
        return Enum.valueOf(cls, c.getString(column));
    }

    public static void writeString(ContentValues values, String key, String value) {
        if (value == null) {
            values.putNull(key);
        } else {
            values.put(key, value);
        }
    }

    public static void writeBoolean(ContentValues values, String key, Boolean value) {
        if (value == null) {
            values.putNull(key);
        } else {
            values.put(key, value);
        }
    }

    public static void writeInteger(ContentValues values, String key, Integer value) {
        if (value == null) {
            values.putNull(key);
        } else {
            values.put(key, value);
        }
    }

    public static void writeLong(ContentValues values, String key, Long value) {
        if (value == null) {
            values.putNull(key);
        } else {
            values.put(key, value);
        }
    }

    public static void writeDouble(ContentValues values, String key, Double value) {
        if (value == null) {
            values.putNull(key);
        } else {
            values.put(key, value);
        }
    }

    public static void writeDate(ContentValues values, String key, Date date) {
        if (date == null) {
            values.putNull(key);
        } else {
            values.put(key, Long.valueOf(date.getTime()));
        }
    }

    public static void writeLocalDate(ContentValues values, String key, LocalDate date) {
        if (date == null) {
            values.putNull(key);
        } else {
            values.put(key, date.toString());
        }
    }

    public static void writeEnum(ContentValues values, String key, Enum<?> value) {
        if (value == null) {
            values.putNull(key);
        } else {
            values.put(key, value.toString());
        }
    }
}
