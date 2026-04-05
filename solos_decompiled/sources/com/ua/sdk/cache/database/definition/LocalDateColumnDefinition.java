package com.ua.sdk.cache.database.definition;

import android.content.ContentValues;
import android.database.Cursor;
import com.ua.sdk.LocalDate;
import com.ua.sdk.cache.database.LegacyEntityDatabase;

/* JADX INFO: loaded from: classes65.dex */
public class LocalDateColumnDefinition extends ColumnDefinition<LocalDate> {
    public LocalDateColumnDefinition(int columnIndex, String columnName) {
        super(columnIndex, columnName);
    }

    @Override // com.ua.sdk.cache.database.definition.ColumnDefinition
    public String getDbType() {
        return "TEXT";
    }

    @Override // com.ua.sdk.cache.database.definition.ColumnDefinition
    public Class<LocalDate> getObjectType() {
        return LocalDate.class;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.ua.sdk.cache.database.definition.ColumnDefinition
    public LocalDate read(Cursor cursor) {
        return LegacyEntityDatabase.readLocalDate(getColumnIndex(), cursor);
    }

    @Override // com.ua.sdk.cache.database.definition.ColumnDefinition
    public void write(LocalDate value, ContentValues contentValues) {
        LegacyEntityDatabase.writeLocalDate(contentValues, getColumnName(), value);
    }
}
