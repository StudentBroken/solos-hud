package com.ua.sdk.cache.database.definition;

import android.content.ContentValues;
import android.database.Cursor;
import com.ua.sdk.cache.database.LegacyEntityDatabase;
import java.util.Date;

/* JADX INFO: loaded from: classes65.dex */
public class DateColumnDefinition extends ColumnDefinition<Date> {
    public DateColumnDefinition(int columnIndex, String columnName) {
        super(columnIndex, columnName);
    }

    @Override // com.ua.sdk.cache.database.definition.ColumnDefinition
    public String getDbType() {
        return "INTEGER";
    }

    @Override // com.ua.sdk.cache.database.definition.ColumnDefinition
    public Class<Date> getObjectType() {
        return Date.class;
    }

    @Override // com.ua.sdk.cache.database.definition.ColumnDefinition
    public Date read(Cursor cursor) {
        return LegacyEntityDatabase.readDate(getColumnIndex(), cursor);
    }

    @Override // com.ua.sdk.cache.database.definition.ColumnDefinition
    public void write(Date value, ContentValues contentValues) {
        LegacyEntityDatabase.writeDate(contentValues, getColumnName(), value);
    }
}
