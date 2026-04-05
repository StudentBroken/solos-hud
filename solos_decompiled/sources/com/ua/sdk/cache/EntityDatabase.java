package com.ua.sdk.cache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.os.Build;
import android.provider.BaseColumns;
import com.ua.oss.de.greenrobot.dao.DbUtils;
import com.ua.sdk.Entity;
import com.ua.sdk.EntityList;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.EntityRef;
import com.ua.sdk.LocalDate;
import com.ua.sdk.Reference;
import com.ua.sdk.UaLog;
import com.ua.sdk.cache.DiskCache;
import com.ua.sdk.cache.database.definition.ColumnDefinition;
import com.ua.sdk.internal.AbstractEntityList;
import com.ua.sdk.internal.ApiTransferObject;
import com.ua.sdk.internal.Link;
import com.ua.sdk.internal.LinkEntityRef;
import com.ua.sdk.internal.LinkListRef;
import com.ua.sdk.internal.Precondition;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes65.dex */
public abstract class EntityDatabase<T extends Entity> extends SQLiteOpenHelper implements DiskCache<T> {
    protected static final int ENTITY_DATABASE_VERSION = 0;
    protected final Context mContext;
    private final String[] mEntityCols;
    private final String mEntityKeyCol;
    private final String mEntityName;
    protected final String mEntityTable;
    private final String mLinksTable;
    private final String mListJoinTable;
    private final String mListTable;
    protected final String mMetaTable;
    protected static final Integer STATE_SYNCED = 0;
    protected static final Integer STATE_CREATED = 1;
    protected static final Integer STATE_MODIFIED = 2;
    protected static final Integer STATE_DELETED = 4;

    public static final class LINKS {
        public static final String TABLE_SUFFIX = "_links";

        public static final class COLS implements BaseColumns {
            public static final String ENTITY_ID = "entity_id";
            public static final String ENTITY_LIST_ID = "entity_list_id";
            public static final String HREF = "link_href";
            public static final String ID = "link_id";
            public static final String KEY = "link_key";
            public static final String NAME = "link_name";
            public static final String _ID = "_id";
        }
    }

    public static final class LIST {
        public static final String TABLE_SUFFIX = "_list";

        public static final class COLS {
            public static final String REMOTE_ID = "remote_id";
            public static final String TOTAL_COUNT = "total_count";
            public static final String _ID = "_id";
        }
    }

    public static final class LIST_JOIN {
        public static final String TABLE_SUFFIX = "_list_join";

        public static final class COLS {
            public static final String ENTITY_ID = "entity_id";
            public static final String ENTITY_LIST_ID = "entity_list_id";
            public static final String _ID = "_id";
        }
    }

    public static final class META {
        public static final String TABLE_SUFFIX = "_meta";

        public static final class COLS {
            public static final String ENTITY_ID = "entity_id";
            public static final String ENTITY_LIST_ID = "entity_list_id";
            public static final String OPTIONS = "options";
            public static final String PENDING_OPERATION = "pending_operation";
            public static final String UPDATE_TIME = "update_time";
            public static final String _ID = "_id";
        }
    }

    protected abstract AbstractEntityList<T> createEntityList(long j, String str, int i);

    protected abstract void createEntityTable(SQLiteDatabase sQLiteDatabase);

    protected abstract ContentValues getContentValuesFromEntity(T t);

    protected abstract T getEntityFromCursor(Cursor cursor);

    public abstract void onEntityUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2);

    protected EntityDatabase(Context context, String entityName, String databaseName, String[] entityCols, String entityKeyCol, int version) {
        super(context, databaseName, (SQLiteDatabase.CursorFactory) null, getCombinedVersion(0, version));
        this.mEntityName = entityName;
        this.mEntityTable = entityName + "_entity";
        this.mEntityKeyCol = entityKeyCol;
        this.mEntityCols = entityCols;
        this.mContext = context;
        this.mMetaTable = entityName + META.TABLE_SUFFIX;
        this.mListTable = entityName + LIST.TABLE_SUFFIX;
        this.mListJoinTable = entityName + LIST_JOIN.TABLE_SUFFIX;
        this.mLinksTable = entityName + LINKS.TABLE_SUFFIX;
        if (Arrays.binarySearch(entityCols, "_id") < 0) {
            throw new IllegalArgumentException("entityCols do not contain _id");
        }
    }

    protected void executeSqlScript(SQLiteDatabase db, String scriptLocation, String errorMsg) {
        try {
            int statements = DbUtils.executeSqlScript(this.mContext, db, this.mEntityName, scriptLocation);
            UaLog.debug("Ran %d statements.", Integer.valueOf(statements));
        } catch (IOException e) {
            UaLog.error(errorMsg, (Throwable) e);
            throw new RuntimeException(errorMsg, e);
        }
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onConfigure(SQLiteDatabase db) {
        if (Build.VERSION.SDK_INT >= 16) {
            db.setForeignKeyConstraintsEnabled(true);
        } else {
            db.execSQL("PRAGMA foreign_keys=ON");
        }
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public final void onCreate(SQLiteDatabase db) {
        createEntityTable(db);
        createMetaTables(db);
    }

    private void createMetaTables(SQLiteDatabase db) {
        executeSqlScript(db, "cache/entity_common/1_entity_common_create_table.sql", String.format("Fatal error, unable to initialize meta tables for %s database.", this.mEntityName));
    }

    protected void deleteEntitiesWithNullRemoteId(SQLiteDatabase db) {
        if (this.mEntityKeyCol != null) {
            db.execSQL("DELETE FROM " + this.mEntityTable + " WHERE " + this.mEntityKeyCol + " IS NULL AND _id IN ( SELECT entity_id FROM " + this.mMetaTable + " WHERE " + META.COLS.PENDING_OPERATION + " = " + STATE_SYNCED + ")");
        }
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public final void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        int newMyVersion = getMyVersion(newVersion);
        int oldMyVersion = getMyVersion(oldVersion);
        if (newMyVersion > oldMyVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + this.mEntityName + LINKS.TABLE_SUFFIX);
            db.execSQL("DROP TABLE IF EXISTS " + this.mEntityName + META.TABLE_SUFFIX);
            db.execSQL("DROP TABLE IF EXISTS " + this.mEntityName + LIST.TABLE_SUFFIX);
            db.execSQL("DROP TABLE IF EXISTS " + this.mEntityName + LIST_JOIN.TABLE_SUFFIX);
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
        return version & 32767;
    }

    private static int getSubVersion(int version) {
        return (536838144 & version) >> 15;
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

    private void insertOrReplaceMetadataAfterFetch(SQLiteDatabase db, String foreignKeyCol, long foreignKey, int options) {
        insertOrReplaceMetadata(db, foreignKeyCol, foreignKey, DiskCache.State.SYNCED, options, true);
    }

    private void insertOrUpdateMetadataState(SQLiteDatabase db, String foreignKeyCol, long foreignKey, DiskCache.State state, int options) {
        String whereClause;
        ContentValues values = new ContentValues();
        values.put(META.COLS.PENDING_OPERATION, stateToDatabaseValue(state));
        values.put(META.COLS.OPTIONS, Integer.valueOf(options));
        if (foreignKeyCol.equals("entity_id")) {
            whereClause = "options=? AND entity_id=? AND entity_list_id IS NULL";
        } else {
            whereClause = "options=? AND entity_list_id=? AND entity_id IS NULL";
        }
        String[] whereArgs = {String.valueOf(options), String.valueOf(foreignKey)};
        int updated = db.update(this.mMetaTable, values, whereClause, whereArgs);
        if (updated == 0) {
            values.put(foreignKeyCol, Long.valueOf(foreignKey));
            db.insert(this.mMetaTable, null, values);
        }
    }

    private void insertOrReplaceMetadata(SQLiteDatabase db, String foreignKeyCol, long foreignKey, DiskCache.State state, int options, boolean updateLastUpdatedTime) {
        String whereClause;
        ContentValues values = new ContentValues();
        values.put(META.COLS.PENDING_OPERATION, stateToDatabaseValue(state));
        values.put(META.COLS.OPTIONS, Integer.valueOf(options));
        values.put(foreignKeyCol, Long.valueOf(foreignKey));
        if (updateLastUpdatedTime) {
            values.put(META.COLS.UPDATE_TIME, Long.valueOf(System.currentTimeMillis()));
        }
        if (foreignKeyCol.equals("entity_id")) {
            whereClause = "options=? AND entity_id=? AND entity_list_id IS NULL";
        } else {
            whereClause = "options=? AND entity_list_id=? AND entity_id IS NULL";
        }
        String[] whereArgs = {String.valueOf(options), String.valueOf(foreignKey)};
        Cursor c = db.query(this.mMetaTable, null, whereClause, whereArgs, null, null, null);
        try {
            if (c.getCount() > 0) {
                c.moveToFirst();
                long rowId = c.getLong(0);
                long result = db.update(this.mMetaTable, values, "_id=?", new String[]{String.valueOf(rowId)});
                UaLog.debug("Updated %s metadata rows", Long.valueOf(result));
                if (result > 1) {
                    UaLog.warn("Updated more than 1 entity meta row! Updated: " + result);
                }
            } else {
                UaLog.debug("Inserting metadata values: " + values);
                long result2 = db.insert(this.mMetaTable, null, values);
                if (result2 == -1) {
                    throw new RuntimeException("Fatal error! Could not store metadata for entity.");
                }
                UaLog.debug("Inserted or overwrote metadata row: " + result2);
            }
        } finally {
            c.close();
        }
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

    private void bulkInsertLinks(SQLiteDatabase db, String foreignKeyCol, long foreignKey, ApiTransferObject entity) {
        String sql = "INSERT INTO " + this.mLinksTable + "(" + foreignKeyCol + ", " + LINKS.COLS.KEY + ", " + LINKS.COLS.HREF + ", " + LINKS.COLS.ID + ", " + LINKS.COLS.NAME + ") VALUES (?,?,?,?,?);";
        SQLiteStatement statement = db.compileStatement(sql);
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
        long localId;
        try {
            db = getReadableDatabase();
            localId = -1;
        } catch (Exception e) {
            UaLog.error("Unable to get link map.", (Throwable) e);
            mapEmptyMap = Collections.emptyMap();
        }
        try {
            if (ref instanceof EntityRef) {
                localId = getEntityLocalId(db, (EntityRef) ref);
            } else if (ref instanceof EntityListRef) {
                localId = getListLocalId(db, (EntityListRef) ref);
            }
            if (localId < 0) {
                mapEmptyMap = Collections.emptyMap();
            } else {
                mapEmptyMap = getLinkMap(db, getForeignKeyCol(ref), localId);
                close();
            }
        } finally {
            close();
        }
        return mapEmptyMap;
    }

    protected Map<String, ArrayList<Link>> getLinkMap(SQLiteDatabase db, String foreignKeyCol, long id) {
        Cursor linksCursor = db.query(this.mLinksTable, null, foreignKeyCol + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        Map<String, ArrayList<Link>> linkMap = new HashMap<>();
        while (linksCursor.moveToNext()) {
            try {
                String relation = linksCursor.getString(linksCursor.getColumnIndex(LINKS.COLS.KEY));
                String href = linksCursor.getString(linksCursor.getColumnIndex(LINKS.COLS.HREF));
                String linkId = linksCursor.getString(linksCursor.getColumnIndex(LINKS.COLS.ID));
                String name = linksCursor.getString(linksCursor.getColumnIndex(LINKS.COLS.NAME));
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
        Cursor cursorQuery = sQLiteDatabase.query(this.mLinksTable, null, null, null, null, null, "entity_id ASC", null);
        try {
            int columnIndex = cursorQuery.getColumnIndex("entity_id");
            int columnIndex2 = cursorQuery.getColumnIndex(LINKS.COLS.KEY);
            int columnIndex3 = cursorQuery.getColumnIndex(LINKS.COLS.ID);
            int columnIndex4 = cursorQuery.getColumnIndex(LINKS.COLS.HREF);
            int columnIndex5 = cursorQuery.getColumnIndex(LINKS.COLS.NAME);
            long j = -1;
            long localId = -1;
            int i = 0;
            int size = list.size();
            ApiTransferObject apiTransferObject = null;
            Map<String, ArrayList<Link>> map = null;
            Link link = null;
            String string = null;
            while (i < size && (link != null || !cursorQuery.isLast())) {
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
        SQLiteDatabase db;
        try {
            db = getWritableDatabase();
        } catch (Exception e) {
            UaLog.error("Unable to delete all entities.", (Throwable) e);
        }
        try {
            preDeleteAll(db);
            deleteAllEntities(db);
            deleteAllLinks(db);
            deleteAllMetadata(db);
            deleteAllLists(db);
        } finally {
            close();
        }
    }

    protected void deleteAllLinksWithId(SQLiteDatabase db, String foreignKeyCol, long id) {
        if (id >= 0) {
            db.delete(this.mLinksTable, foreignKeyCol + "=?", new String[]{Long.toString(id)});
        }
    }

    protected void deleteAllLinks(SQLiteDatabase db) {
        db.execSQL("DELETE FROM " + this.mLinksTable);
    }

    protected void deleteAllLists(SQLiteDatabase db) {
        db.execSQL("DELETE FROM " + this.mListTable);
        db.execSQL("DELETE FROM " + this.mListJoinTable);
    }

    protected void deleteAllMetadata(SQLiteDatabase db) {
        db.execSQL("DELETE FROM " + this.mMetaTable);
    }

    protected void deleteAllMetadataWithId(SQLiteDatabase db, String foreignKeyCol, long id) {
        if (id >= 0) {
            db.delete(this.mMetaTable, foreignKeyCol + "=?", new String[]{Long.toString(id)});
        }
    }

    protected long getEntityLocalId(SQLiteDatabase db, EntityRef ref) {
        String id;
        long localId = -1;
        if ((ref instanceof LinkEntityRef) && ((LinkEntityRef) ref).checkCache()) {
            long id2 = ((LinkEntityRef) ref).getLocalId();
            if (id2 >= 0) {
                localId = id2;
            }
        }
        if (localId == -1 && ref != null && (id = ref.getId()) != null) {
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

    private long getListLocalId(SQLiteDatabase db, EntityListRef ref) {
        String id;
        long localId = -1;
        if ((ref instanceof LinkListRef) && ((LinkListRef) ref).checkCache()) {
            long id2 = ((LinkListRef) ref).getLocalId();
            if (id2 >= 0) {
                localId = id2;
            }
        }
        if (localId == -1 && (id = ref.getHref()) != null) {
            Cursor cursor = db.query(this.mListTable, new String[]{"_id"}, "remote_id= '" + id + "'", null, null, null, null);
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

    /* JADX WARN: Removed duplicated region for block: B:17:0x003a A[Catch: Exception -> 0x00c0, all -> 0x00ca, TRY_LEAVE, TryCatch #0 {Exception -> 0x00c0, blocks: (B:5:0x000c, B:7:0x0012, B:13:0x0023, B:15:0x0032, B:17:0x003a, B:25:0x004e, B:32:0x00aa, B:35:0x00b2, B:39:0x00bc, B:40:0x00bf), top: B:47:0x000c, outer: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0046  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x004e A[Catch: Exception -> 0x00c0, all -> 0x00ca, TRY_ENTER, TRY_LEAVE, TryCatch #0 {Exception -> 0x00c0, blocks: (B:5:0x000c, B:7:0x0012, B:13:0x0023, B:15:0x0032, B:17:0x003a, B:25:0x004e, B:32:0x00aa, B:35:0x00b2, B:39:0x00bc, B:40:0x00bf), top: B:47:0x000c, outer: #1 }] */
    @Override // com.ua.sdk.cache.DiskCache
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized T get(com.ua.sdk.Reference r21) {
        /*
            Method dump skipped, instruction units count: 207
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ua.sdk.cache.EntityDatabase.get(com.ua.sdk.Reference):com.ua.sdk.Entity");
    }

    @Override // com.ua.sdk.cache.DiskCache
    public synchronized EntityList<T> getList(Reference ref) {
        AbstractEntityList<T> entityList;
        Precondition.isNotNull(ref, "ref");
        String id = ref.getHref();
        if (id == null) {
            entityList = null;
        } else {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.query(this.mListTable, new String[]{"_id", LIST.COLS.REMOTE_ID, LIST.COLS.TOTAL_COUNT}, LIST.COLS.REMOTE_ID + "=?", new String[]{id}, null, null, null, null);
            entityList = null;
            long localId = -1;
            if (cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        localId = cursor.getLong(0);
                        entityList = createEntityList(localId, cursor.getString(1), cursor.getInt(2));
                    }
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }
            if (entityList == null) {
                entityList = null;
            } else {
                Map<String, ArrayList<Link>> linkMap = getLinkMap(db, "entity_list_id", localId);
                entityList.setLinkMap(linkMap);
                getEntitiesForList(db, entityList, localId);
            }
        }
        return entityList;
    }

    void getEntitiesForList(SQLiteDatabase db, AbstractEntityList<T> list, long listLocalId) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        for (int i = 0; i < this.mEntityCols.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append("e.");
            sb.append(this.mEntityCols[i]);
        }
        sb.append(" FROM ");
        sb.append(this.mListJoinTable);
        sb.append(" AS j INNER JOIN ");
        sb.append(this.mEntityTable);
        sb.append(" AS e ON j.");
        sb.append("entity_id");
        sb.append(" = e._id WHERE j.");
        sb.append("entity_list_id");
        sb.append(" = ");
        sb.append(listLocalId);
        sb.append(" ORDER BY j._id ASC;");
        Cursor cursor = db.rawQuery(sb.toString(), null);
        ArrayList<ApiTransferObject> entities = new ArrayList<>();
        while (cursor.moveToNext()) {
            try {
                Object entityFromCursor = getEntityFromCursor(cursor);
                list.add(entityFromCursor);
                entities.add((ApiTransferObject) entityFromCursor);
            } catch (Throwable th) {
                cursor.close();
                throw th;
            }
        }
        cursor.close();
        Collections.sort(entities, new Comparator<ApiTransferObject>() { // from class: com.ua.sdk.cache.EntityDatabase.1
            @Override // java.util.Comparator
            public int compare(ApiTransferObject lhs, ApiTransferObject rhs) {
                return (int) (lhs.getLocalId() - rhs.getLocalId());
            }
        });
        setAllLinkMaps(db, entities);
    }

    protected ContentValues getContentValuesFromEntity(long localId, T entity) {
        Reference ref;
        String id;
        ContentValues values = getContentValuesFromEntity(entity);
        if (localId >= 0) {
            values.put("_id", Long.valueOf(localId));
        } else {
            values.remove("_id");
        }
        if (this.mEntityKeyCol != null && values.get(this.mEntityKeyCol) == null && (ref = entity.getRef()) != null && (id = ref.getId()) != null) {
            values.put(this.mEntityKeyCol, id);
        }
        return values;
    }

    protected void postSaveEntity(long localId, T entity) {
    }

    protected void preDeleteEntity(long localId) {
    }

    protected void preDeleteAll(SQLiteDatabase db) {
    }

    protected long deleteEntity(SQLiteDatabase db, EntityRef<T> ref) {
        int rowsChanged;
        long localId = getEntityLocalId(db, ref);
        preDeleteEntity(localId);
        if (localId >= 0 && (rowsChanged = db.delete(this.mEntityTable, "_id=?", new String[]{Long.toString(localId)})) != 1) {
            UaLog.error("Failed to delete entity. refType=%s id=%s rowsChanged=%s", ref.getClass().getSimpleName(), Long.valueOf(localId), Integer.valueOf(rowsChanged));
        }
        return localId;
    }

    public long deleteList(SQLiteDatabase db, EntityListRef<T> ref) {
        int rowsChanged;
        long localId = getListLocalId(db, ref);
        if (localId >= 0 && (rowsChanged = db.delete(this.mListTable, "_id=?", new String[]{Long.toString(localId)})) != 1) {
            UaLog.error("Failed to delete entity list. refType=%s id=%s rowsChanged=%s", ref.getClass().getSimpleName(), Long.valueOf(localId), Integer.valueOf(rowsChanged));
        }
        return localId;
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected long insert(SQLiteDatabase db, T t) {
        ContentValues values = getContentValuesFromEntity(-1L, t);
        long localId = db.insert(this.mEntityTable, null, values);
        deleteAllLinksWithId(db, "entity_id", localId);
        if (t instanceof ApiTransferObject) {
            ((ApiTransferObject) t).setLocalId(localId);
            bulkInsertLinks(db, "entity_id", localId, (ApiTransferObject) t);
        }
        postSaveEntity(localId, t);
        return localId;
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void update(SQLiteDatabase db, long localId, T t) {
        ContentValues values = getContentValuesFromEntity(localId, t);
        int rowsChanged = db.update(this.mEntityTable, values, "_id=" + localId, null);
        if (rowsChanged >= 1) {
            deleteAllLinksWithId(db, "entity_id", localId);
            if (t instanceof ApiTransferObject) {
                ((ApiTransferObject) t).setLocalId(localId);
                bulkInsertLinks(db, "entity_id", localId, (ApiTransferObject) t);
            }
        } else {
            UaLog.error("Failed to update entity. type=%s id=%s rowsChanged=%s", t.getClass().getSimpleName(), Long.valueOf(localId), Integer.valueOf(rowsChanged));
        }
        postSaveEntity(localId, t);
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected long insertOrUpdate(SQLiteDatabase db, T t) {
        long localId = getEntityLocalId(db, t.getRef());
        ContentValues values = getContentValuesFromEntity(localId, t);
        if (localId > 0) {
            UaLog.debug("Updating existing entity with localId=%s", Long.valueOf(localId));
            db.update(this.mEntityTable, values, "_id=?", new String[]{String.valueOf(localId)});
        } else {
            UaLog.debug("Adding a new entity using values: %s", values);
            localId = db.insert(this.mEntityTable, null, values);
        }
        deleteAllLinksWithId(db, "entity_id", localId);
        if (t instanceof ApiTransferObject) {
            ((ApiTransferObject) t).setLocalId(localId);
            bulkInsertLinks(db, "entity_id", localId, (ApiTransferObject) t);
        }
        postSaveEntity(localId, t);
        return localId;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private long insertOrUpdateList(SQLiteDatabase db, EntityListRef<T> requestRef, EntityList<T> entityList) {
        Precondition.isNotNull(requestRef, "entityList.getRef()");
        String cachedHref = requestRef.getHref();
        Precondition.isNotNull(cachedHref, "entityList.getRef().getHref()");
        String sql = "INSERT OR REPLACE INTO " + this.mListTable + "(" + LIST.COLS.REMOTE_ID + ", " + LIST.COLS.TOTAL_COUNT + ") VALUES (?,?);";
        SQLiteStatement statement = db.compileStatement(sql);
        try {
            nullSafeBind(statement, 1, cachedHref);
            nullSafeBind(statement, 2, String.valueOf(entityList.getTotalCount()));
            long localId = statement.executeInsert();
            statement.close();
            bulkInsertLinks(db, "entity_list_id", localId, (ApiTransferObject) entityList);
            return localId;
        } catch (Throwable th) {
            statement.close();
            throw th;
        }
    }

    protected void deleteAllEntities(SQLiteDatabase db) {
        db.execSQL("DELETE FROM " + this.mEntityTable);
    }

    protected int getEntityOptions(Reference ref) {
        if (ref instanceof LinkEntityRef) {
            int result = ((LinkEntityRef) ref).getOptions();
            return result;
        }
        UaLog.warn("Entity's ref not an instance of LinkEntityRef, so options aren't available (using default of 0).");
        return 0;
    }

    @Override // com.ua.sdk.cache.DiskCache
    public synchronized void updateAfterFetch(T entity) {
        SQLiteDatabase db = null;
        try {
            try {
                db = getWritableDatabase();
                db.beginTransaction();
                long localId = insertOrUpdate(db, entity);
                insertOrReplaceMetadataAfterFetch(db, "entity_id", localId, getEntityOptions(entity.getRef()));
                db.setTransactionSuccessful();
            } catch (Exception e) {
                UaLog.error("Failed to update the cache after fetch.", (Throwable) e);
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
                if (!partial) {
                    long listLocalId = insertOrUpdateList(db, requestRef, entities);
                    insertOrReplaceMetadataAfterFetch(db, "entity_list_id", listLocalId, 0);
                    int size = entities.getSize();
                    long[] entityLocalIds = new long[size];
                    for (int i = 0; i < size; i++) {
                        T entity = entities.get(i);
                        long entityLocalId = getEntityLocalId(db, entity.getRef());
                        if (entityLocalId >= 0) {
                            update(db, entityLocalId, entity);
                        } else {
                            entityLocalId = insert(db, entity);
                        }
                        entityLocalIds[i] = entityLocalId;
                        insertOrReplaceMetadataAfterFetch(db, "entity_id", entityLocalId, getEntityOptions(entity.getRef()));
                    }
                    deleteListJoins(db, listLocalId);
                    bulkInsertListJoin(db, listLocalId, entityLocalIds);
                } else {
                    for (T entity2 : entities.getAll()) {
                        long localId = getEntityLocalId(db, entity2.getRef());
                        if (localId >= 0) {
                            update(db, localId, entity2);
                        }
                    }
                }
                db.setTransactionSuccessful();
            } catch (Exception e) {
                UaLog.error("Failed to update the cache after fetch.", (Throwable) e);
                if (db != null) {
                    endTransaction(db);
                }
                close();
            }
        } finally {
            if (0 != 0) {
                endTransaction(null);
            }
            close();
        }
    }

    private void deleteListJoins(SQLiteDatabase db, long listLocalId) {
        db.execSQL("DELETE FROM " + this.mListJoinTable + " WHERE entity_list_id = " + listLocalId);
    }

    private void bulkInsertListJoin(SQLiteDatabase db, long listLocalId, long[] entityLocalIds) {
        if (entityLocalIds != null && entityLocalIds.length != 0) {
            String sql = "INSERT OR IGNORE INTO " + this.mListJoinTable + " (entity_list_id, entity_id) VALUES (?,?);";
            SQLiteStatement statement = db.compileStatement(sql);
            for (long entityLocalId : entityLocalIds) {
                try {
                    statement.clearBindings();
                    statement.bindLong(1, listLocalId);
                    statement.bindLong(2, entityLocalId);
                    statement.execute();
                } finally {
                    statement.close();
                }
            }
        }
    }

    private static void endTransaction(SQLiteDatabase db) {
        try {
            db.endTransaction();
        } catch (Exception e) {
            UaLog.error("Failed to end transaction.", (Throwable) e);
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
                insertOrReplaceMetadataAfterFetch(db, "entity_id", localId, getEntityOptions(entity.getRef()));
                db.setTransactionSuccessful();
            } catch (Exception e) {
                UaLog.error("Failed to update the cache after create", (Throwable) e);
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
                insertOrUpdateMetadataState(db, "entity_id", localId, DiskCache.State.CREATED, getEntityOptions(entity.getRef()));
                db.setTransactionSuccessful();
            } catch (Exception e) {
                UaLog.error("Failed to put in cache for create.", (Throwable) e);
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
                insertOrUpdateMetadataState(db, "entity_id", localId, DiskCache.State.MODIFIED, getEntityOptions(entity.getRef()));
                db.setTransactionSuccessful();
            } catch (Exception e) {
                UaLog.error("Failed to put in cache for save.", (Throwable) e);
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
    public synchronized void updateAfterSave(T entity) {
        SQLiteDatabase db = null;
        try {
            try {
                db = getWritableDatabase();
                db.beginTransaction();
                long localId = insertOrUpdate(db, entity);
                insertOrReplaceMetadataAfterFetch(db, "entity_id", localId, getEntityOptions(entity.getRef()));
                db.setTransactionSuccessful();
            } catch (Exception e) {
                UaLog.error("Failed to update the cache after save", (Throwable) e);
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
            try {
                SQLiteDatabase db = getWritableDatabase();
                long localId = getEntityLocalId(db, (EntityRef) ref);
                insertOrUpdateMetadataState(db, "entity_id", localId, DiskCache.State.DELETED, getEntityOptions(ref));
            } catch (Exception e) {
                UaLog.error("Failed to mark the cache for delete.", (Throwable) e);
                close();
            }
        } finally {
            close();
        }
    }

    @Override // com.ua.sdk.cache.DiskCache
    public synchronized void delete(Reference ref) {
        SQLiteDatabase db = null;
        try {
            try {
                db = getWritableDatabase();
                db.beginTransaction();
                long localId = deleteEntity(db, (EntityRef) ref);
                deleteAllMetadataWithId(db, "entity_id", localId);
                deleteAllLinksWithId(db, "entity_id", localId);
                db.setTransactionSuccessful();
            } catch (Exception e) {
                UaLog.error("Failed to update the cache after delete", (Throwable) e);
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
    public synchronized void deleteList(EntityListRef<T> ref) {
        SQLiteDatabase db = null;
        try {
            try {
                db = getWritableDatabase();
                db.beginTransaction();
                long localId = deleteList(db, ref);
                deleteAllMetadataWithId(db, "entity_list_id", localId);
                deleteAllLinksWithId(db, "entity_list_id", localId);
                db.setTransactionSuccessful();
            } catch (Exception e) {
                UaLog.error("Failed to update the cache after delete", (Throwable) e);
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

    private static String getForeignKeyCol(Reference ref) {
        return ref instanceof EntityListRef ? "entity_list_id" : "entity_id";
    }

    @Override // com.ua.sdk.cache.DiskCache
    public synchronized long getLastSynced(Reference ref) {
        long lastEntityListSynced = -1;
        synchronized (this) {
            if (ref != null) {
                if (ref instanceof EntityRef) {
                    lastEntityListSynced = getLastEntitySynced((EntityRef) ref);
                } else if (ref instanceof EntityListRef) {
                    lastEntityListSynced = getLastEntityListSynced((EntityListRef) ref);
                } else {
                    UaLog.error("Unknown reference type: " + ref.getClass().getCanonicalName());
                }
            }
        }
        return lastEntityListSynced;
    }

    private synchronized long getLastEntityListSynced(EntityListRef ref) {
        long lastSynced;
        lastSynced = -1;
        if (ref != null) {
            Cursor c = null;
            try {
                try {
                    SQLiteDatabase db = getReadableDatabase();
                    long localId = getListLocalId(db, ref);
                    if (localId >= 0) {
                        c = db.query(this.mMetaTable, new String[]{META.COLS.UPDATE_TIME}, getForeignKeyCol(ref) + "=?", new String[]{Long.toString(localId)}, null, null, null);
                        if (c.moveToFirst() && !c.isNull(0)) {
                            lastSynced = c.getLong(0);
                        }
                    }
                } catch (Exception e) {
                    UaLog.error("Unable to get last synced time.", (Throwable) e);
                    if (c != null) {
                        c.close();
                    }
                    close();
                }
            } finally {
                if (0 != 0) {
                    c.close();
                }
                close();
            }
        }
        return lastSynced;
    }

    private synchronized long getLastEntitySynced(EntityRef ref) {
        long lastSynced;
        lastSynced = -1;
        if (ref != null) {
            Cursor c = null;
            try {
                try {
                    SQLiteDatabase db = getReadableDatabase();
                    long localId = getEntityLocalId(db, ref);
                    if (localId >= 0) {
                        c = db.query(this.mMetaTable, new String[]{META.COLS.UPDATE_TIME}, getForeignKeyCol(ref) + "=?", new String[]{Long.toString(localId)}, null, null, null);
                        if (c.moveToFirst() && !c.isNull(0)) {
                            lastSynced = c.getLong(0);
                        }
                    }
                } catch (Exception e) {
                    UaLog.error("Unable to get last synced time.", (Throwable) e);
                    if (c != null) {
                        c.close();
                    }
                    close();
                }
            } finally {
                if (0 != 0) {
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
                long localId = getEntityLocalId(db, (EntityRef) ref);
                if (localId >= 0) {
                    c = db.query(this.mMetaTable, new String[]{META.COLS.PENDING_OPERATION}, getForeignKeyCol(ref) + "=?", new String[]{Long.toString(localId)}, null, null, null);
                    if (c.moveToFirst() && !c.isNull(0)) {
                        state = stateFromDatabaseValue(c.getInt(0));
                    }
                }
            } catch (Exception e) {
                UaLog.error("Unable to get cache state.", (Throwable) e);
                if (c != null) {
                    c.close();
                }
                close();
            }
        } finally {
            if (c != null) {
                c.close();
            }
            close();
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
