package com.ua.sdk.cache.database.definition;

import android.content.ContentValues;
import android.database.Cursor;
import com.ua.sdk.cache.database.LegacyEntityDatabase;

/* JADX INFO: loaded from: classes65.dex */
public class BooleanColumnDefinition extends ColumnDefinition<Boolean> {
    public BooleanColumnDefinition(int columnIndex, String columnName) {
        super(columnIndex, columnName);
    }

    @Override // com.ua.sdk.cache.database.definition.ColumnDefinition
    public String getDbType() {
        return "INTEGER";
    }

    @Override // com.ua.sdk.cache.database.definition.ColumnDefinition
    public Class<Boolean> getObjectType() {
        return Boolean.class;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.ua.sdk.cache.database.definition.ColumnDefinition
    public Boolean read(Cursor cursor) {
        return LegacyEntityDatabase.readBoolean(getColumnIndex(), cursor);
    }

    @Override // com.ua.sdk.cache.database.definition.ColumnDefinition
    public void write(Boolean value, ContentValues contentValues) {
        LegacyEntityDatabase.writeBoolean(contentValues, getColumnName(), value);
    }
}
