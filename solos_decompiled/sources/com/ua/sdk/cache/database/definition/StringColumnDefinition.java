package com.ua.sdk.cache.database.definition;

import android.content.ContentValues;
import android.database.Cursor;
import com.ua.sdk.cache.database.LegacyEntityDatabase;

/* JADX INFO: loaded from: classes65.dex */
public class StringColumnDefinition extends ColumnDefinition<String> {
    public StringColumnDefinition(int columnIndex, String columnName) {
        super(columnIndex, columnName);
    }

    @Override // com.ua.sdk.cache.database.definition.ColumnDefinition
    public String getDbType() {
        return "TEXT";
    }

    @Override // com.ua.sdk.cache.database.definition.ColumnDefinition
    public Class<String> getObjectType() {
        return String.class;
    }

    @Override // com.ua.sdk.cache.database.definition.ColumnDefinition
    public String read(Cursor cursor) {
        return LegacyEntityDatabase.readString(getColumnIndex(), cursor);
    }

    @Override // com.ua.sdk.cache.database.definition.ColumnDefinition
    public void write(String value, ContentValues contentValues) {
        LegacyEntityDatabase.writeString(contentValues, getColumnName(), value);
    }
}
