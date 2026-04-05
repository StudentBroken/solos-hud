package com.ua.sdk.cache.database.definition;

import android.content.ContentValues;
import android.database.Cursor;
import com.ua.sdk.cache.database.LegacyEntityDatabase;

/* JADX INFO: loaded from: classes65.dex */
public class IntegerColumnDefinition extends ColumnDefinition<Integer> {
    public IntegerColumnDefinition(int columnIndex, String columnName) {
        super(columnIndex, columnName);
    }

    @Override // com.ua.sdk.cache.database.definition.ColumnDefinition
    public String getDbType() {
        return "INTEGER";
    }

    @Override // com.ua.sdk.cache.database.definition.ColumnDefinition
    public Class<Integer> getObjectType() {
        return Integer.class;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.ua.sdk.cache.database.definition.ColumnDefinition
    public Integer read(Cursor cursor) {
        return LegacyEntityDatabase.readInteger(getColumnIndex(), cursor);
    }

    @Override // com.ua.sdk.cache.database.definition.ColumnDefinition
    public void write(Integer value, ContentValues contentValues) {
        LegacyEntityDatabase.writeInteger(contentValues, getColumnName(), value);
    }
}
