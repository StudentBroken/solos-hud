package com.ua.sdk.cache.database.definition;

import android.content.ContentValues;
import android.database.Cursor;
import com.ua.sdk.cache.database.LegacyEntityDatabase;

/* JADX INFO: loaded from: classes65.dex */
public class LocalIdColumnDefinition extends ColumnDefinition<Long> {
    public LocalIdColumnDefinition(int columnIndex, String columnName) {
        super(columnIndex, columnName);
    }

    @Override // com.ua.sdk.cache.database.definition.ColumnDefinition
    public String getDbType() {
        return "INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL";
    }

    @Override // com.ua.sdk.cache.database.definition.ColumnDefinition
    public Class<Long> getObjectType() {
        return Long.class;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.ua.sdk.cache.database.definition.ColumnDefinition
    public Long read(Cursor cursor) {
        return LegacyEntityDatabase.readLong(getColumnIndex(), cursor);
    }

    @Override // com.ua.sdk.cache.database.definition.ColumnDefinition
    public void write(Long value, ContentValues contentValues) {
        LegacyEntityDatabase.writeLong(contentValues, getColumnName(), value);
    }
}
