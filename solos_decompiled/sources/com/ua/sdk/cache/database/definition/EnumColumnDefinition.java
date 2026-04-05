package com.ua.sdk.cache.database.definition;

import android.content.ContentValues;
import android.database.Cursor;
import com.ua.sdk.cache.database.LegacyEntityDatabase;
import java.lang.Enum;

/* JADX INFO: loaded from: classes65.dex */
public class EnumColumnDefinition<T extends Enum<T>> extends ColumnDefinition<T> {
    private Class<T> clazz;

    public EnumColumnDefinition(int columnIndex, String columnName, Class<T> clazz) {
        super(columnIndex, columnName);
        this.clazz = clazz;
    }

    @Override // com.ua.sdk.cache.database.definition.ColumnDefinition
    public String getDbType() {
        return "TEXT";
    }

    @Override // com.ua.sdk.cache.database.definition.ColumnDefinition
    public Class<T> getObjectType() {
        return this.clazz;
    }

    @Override // com.ua.sdk.cache.database.definition.ColumnDefinition
    public T read(Cursor cursor) {
        return (T) LegacyEntityDatabase.readEnum(getColumnIndex(), cursor, this.clazz);
    }

    @Override // com.ua.sdk.cache.database.definition.ColumnDefinition
    public void write(T value, ContentValues contentValues) {
        LegacyEntityDatabase.writeEnum(contentValues, getColumnName(), value);
    }
}
