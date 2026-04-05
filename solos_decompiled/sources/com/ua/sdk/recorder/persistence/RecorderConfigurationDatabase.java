package com.ua.sdk.recorder.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.ua.sdk.UaLog;
import com.ua.sdk.cache.database.definition.ColumnDefinition;
import com.ua.sdk.recorder.DataSourceConfigurationList;
import com.ua.sdk.recorder.RecorderConfiguration;
import java.util.Date;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public class RecorderConfigurationDatabase extends SQLiteOpenHelper {
    public static final String DB_NAME = "record_configuration";
    public static final String DB_TABLE = "recorders";
    private static final int DB_VERSION = 1;
    private static RecorderConfigurationDatabase instance;

    public static RecorderConfigurationDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new RecorderConfigurationDatabase(context);
        }
        return instance;
    }

    public RecorderConfigurationDatabase(Context context) {
        super(context, DB_NAME, (SQLiteDatabase.CursorFactory) null, 1);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase db) {
        String createStatement = buildCreateStatement(DB_TABLE, RecorderConfigurationDatabaseMapper.COLUMNS);
        db.execSQL(createStatement);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS recorders");
        onCreate(db);
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

    private static void endTransaction(SQLiteDatabase db) {
        try {
            db.endTransaction();
        } catch (Throwable e) {
            UaLog.error("Failed to end transaction.", e);
        }
    }

    public void insert(String name, String userId, String activityTypeId, Date createDate, Date updateDate, DataSourceConfigurationList configuration) {
        SQLiteDatabase db = null;
        try {
            try {
                db = getWritableDatabase();
                db.beginTransaction();
                db.insertOrThrow(DB_TABLE, "_id", RecorderConfigurationDatabaseMapper.getContentValues(name, userId, activityTypeId, createDate, updateDate, configuration));
                db.setTransactionSuccessful();
                if (db != null) {
                    endTransaction(db);
                }
            } catch (Throwable t) {
                UaLog.error("Unable to insert row into table", t);
                if (db != null) {
                    endTransaction(db);
                }
            }
        } catch (Throwable th) {
            if (db != null) {
                endTransaction(db);
            }
            throw th;
        }
    }

    public void delete(String name) {
        SQLiteDatabase db = null;
        try {
            try {
                db = getWritableDatabase();
                db.beginTransaction();
                if (name != null) {
                    int deleteRows = db.delete(DB_TABLE, "name = ?", new String[]{name});
                    if (deleteRows == 0) {
                        UaLog.info("Failed to delete recorder with name " + name);
                    }
                }
                db.setTransactionSuccessful();
                if (db != null) {
                    endTransaction(db);
                }
            } catch (Throwable t) {
                UaLog.error("Unable to delete row", t);
                if (db != null) {
                    endTransaction(db);
                }
            }
        } catch (Throwable th) {
            if (db != null) {
                endTransaction(db);
            }
            throw th;
        }
    }

    public void update(String name, Date updateDate, DataSourceConfigurationList configuration) {
        SQLiteDatabase db = null;
        try {
            try {
                db = getWritableDatabase();
                db.beginTransaction();
                if (name != null && updateDate != null) {
                    db.update(DB_TABLE, RecorderConfigurationDatabaseMapper.getUpdateValues(updateDate, configuration), "name = ?", new String[]{name});
                }
                db.setTransactionSuccessful();
                if (db != null) {
                    db.endTransaction();
                }
            } catch (Throwable t) {
                UaLog.error("Unable to update entry", t);
                if (db != null) {
                    db.endTransaction();
                }
            }
        } catch (Throwable th) {
            if (db != null) {
                db.endTransaction();
            }
            throw th;
        }
    }

    public List<RecorderConfiguration> getAllEntries(String userId) {
        SQLiteDatabase db = getReadableDatabase();
        return RecorderConfigurationDatabaseMapper.getCachedConfigurations(db.query(DB_TABLE, buildColumnNames(RecorderConfigurationDatabaseMapper.COLUMNS), "user_id = ?", new String[]{userId}, null, null, null));
    }

    public RecorderConfiguration get(String userId, String name) {
        SQLiteDatabase db = getReadableDatabase();
        return RecorderConfigurationDatabaseMapper.getCachedConfigurations(db.query(DB_TABLE, buildColumnNames(RecorderConfigurationDatabaseMapper.COLUMNS), "user_id = ? AND name = ?", new String[]{userId, name}, null, null, null)).get(0);
    }
}
