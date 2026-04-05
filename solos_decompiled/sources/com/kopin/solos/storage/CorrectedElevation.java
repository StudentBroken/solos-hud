package com.kopin.solos.storage;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;
import com.kopin.solos.storage.field.FieldHelper;
import com.kopin.solos.storage.field.FieldType;
import com.kopin.solos.storage.util.Utility;

/* JADX INFO: loaded from: classes54.dex */
public class CorrectedElevation implements BaseColumns {
    static final String NAME = "CorrectedElevation";
    static final String SQL_INSERT = "INSERT INTO CorrectedElevation (" + Field.COORD_ID + ", " + Field.CORRECTED_ALTITUDE + ") VALUES (?, ?)";
    private final double mAltitude;
    private final long mCoordId;
    private final long mTimestamp;

    public CorrectedElevation(long time, long coordId, double alt) {
        this.mTimestamp = time;
        this.mCoordId = coordId;
        this.mAltitude = alt;
    }

    void bind(SQLiteStatement insert) {
        insert.clearBindings();
        insert.bindLong(1, this.mCoordId);
        insert.bindDouble(2, this.mAltitude);
    }

    public long getTimestamp() {
        return this.mTimestamp;
    }

    public double getAltitude() {
        return this.mAltitude;
    }

    public double getAltitudeForLocale() {
        return Utility.convertToUserUnits(0, this.mAltitude);
    }

    enum Field {
        COORD_ID(FieldType.INTEGER),
        CORRECTED_ALTITUDE(FieldType.REAL);

        public FieldType fieldType;

        Field(FieldType field) {
            this.fieldType = field;
        }

        @Override // java.lang.Enum
        public String toString() {
            return name();
        }

        public int getInt(Cursor cursor) {
            return FieldHelper.getInt(cursor, name());
        }

        public long getLong(Cursor cursor) {
            return FieldHelper.getLong(cursor, name());
        }

        public double getDouble(Cursor cursor) {
            return FieldHelper.getDouble(cursor, name());
        }

        public String getString(Cursor cursor) {
            return FieldHelper.getString(cursor, name());
        }
    }

    static String getCreateFieldsQuery() {
        StringBuilder builder = new StringBuilder();
        for (Field field : Field.values()) {
            if (builder.length() > 0) {
                builder.append(", ");
            }
            builder.append(field.name()).append(" ").append(field.fieldType);
        }
        return builder.toString();
    }

    public String toString() {
        return "CorrectedElevation: " + getAltitude() + " m";
    }
}
