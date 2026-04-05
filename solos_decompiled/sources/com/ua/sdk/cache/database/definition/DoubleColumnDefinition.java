package com.ua.sdk.cache.database.definition;

import android.content.ContentValues;
import android.database.Cursor;
import com.ua.sdk.cache.database.LegacyEntityDatabase;

/* JADX INFO: loaded from: classes65.dex */
public class DoubleColumnDefinition extends ColumnDefinition<Double> {
    public DoubleColumnDefinition(int columnIndex, String columnName) {
        super(columnIndex, columnName);
    }

    @Override // com.ua.sdk.cache.database.definition.ColumnDefinition
    public String getDbType() {
        return "REAL";
    }

    @Override // com.ua.sdk.cache.database.definition.ColumnDefinition
    public Class<Double> getObjectType() {
        return Double.class;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.ua.sdk.cache.database.definition.ColumnDefinition
    public Double read(Cursor cursor) {
        return LegacyEntityDatabase.readDouble(getColumnIndex(), cursor);
    }

    @Override // com.ua.sdk.cache.database.definition.ColumnDefinition
    public void write(Double value, ContentValues contentValues) {
        LegacyEntityDatabase.writeDouble(contentValues, getColumnName(), value);
    }
}
