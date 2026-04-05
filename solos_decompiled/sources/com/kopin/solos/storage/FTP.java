package com.kopin.solos.storage;

import android.database.Cursor;
import android.provider.BaseColumns;
import com.kopin.solos.storage.Shared;
import com.kopin.solos.storage.field.FieldHelper;
import com.kopin.solos.storage.field.FieldType;

/* JADX INFO: loaded from: classes54.dex */
public class FTP implements BaseColumns, ISyncable {
    public static final String NAME = "FTP";
    public long mDate;
    public long mId;
    public ThresholdType mThresholdType;
    public double mValue;

    public enum ThresholdType {
        FTP,
        RUN_FTP,
        PEAK_HR;

        public static ThresholdType fromString(String type) {
            if (type != null && !type.isEmpty()) {
                if (type.equalsIgnoreCase(FTP.NAME)) {
                    return FTP;
                }
                if (type.equalsIgnoreCase("RUN_FTP")) {
                    return RUN_FTP;
                }
                if (type.equalsIgnoreCase("PEAK_HR")) {
                    return PEAK_HR;
                }
            }
            return FTP;
        }
    }

    enum Field {
        DATE(FieldType.INTEGER),
        VALUE(FieldType.REAL),
        THRESHOLD(FieldType.TEXT);

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

    public FTP(Cursor cursor) {
        this.mId = FieldHelper.getLong(cursor, "_id");
        this.mDate = Field.DATE.getLong(cursor);
        this.mValue = Field.VALUE.getDouble(cursor);
        this.mThresholdType = ThresholdType.fromString(Field.THRESHOLD.getString(cursor));
    }

    public FTP(long date, double value, ThresholdType type) {
        this.mDate = date;
        this.mValue = value;
        this.mThresholdType = type;
    }

    FTP() {
        this.mDate = System.currentTimeMillis();
    }

    public static String getCreateFieldsQuery() {
        StringBuilder builder = new StringBuilder();
        for (Field field : Field.values()) {
            if (builder.length() > 0) {
                builder.append(", ");
            }
            builder.append(field.name()).append(" ").append(field.fieldType);
            if (field.fieldType == FieldType.INTEGER) {
                builder.append(" DEFAULT 0");
            }
        }
        return builder.toString();
    }

    @Override // com.kopin.solos.storage.ISyncable
    public Shared toShared(int providerKey, String userName, String externalId) {
        return new Shared(this.mId, providerKey, userName, externalId, false, Shared.ShareType.FTP);
    }
}
