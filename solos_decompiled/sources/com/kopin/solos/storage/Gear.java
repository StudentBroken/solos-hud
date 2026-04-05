package com.kopin.solos.storage;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;
import com.kopin.solos.storage.FTP;
import com.kopin.solos.storage.Shared;
import com.kopin.solos.storage.field.FieldHelper;
import com.kopin.solos.storage.field.FieldType;

/* JADX INFO: loaded from: classes54.dex */
public class Gear implements BaseColumns, ISyncable {
    public static final String TABLE = "Gear";
    boolean mActive;
    boolean mCurrentGear;
    long mId;
    long mLifeSpanKm;
    String mName;
    long mTimeStamp;

    enum Field {
        CURRENT_GEAR(FieldType.INTEGER),
        GEAR_STATUS(FieldType.INTEGER),
        NAME(FieldType.TEXT),
        TIMESTAMP(FieldType.INTEGER),
        LIFESPAN_KM(FieldType.REAL);

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

    public static Gear getDefaultGear() {
        Gear gear = new Gear();
        gear.setName("Default Gear");
        return gear;
    }

    public Gear() {
        this.mActive = true;
        this.mCurrentGear = true;
        this.mLifeSpanKm = 800L;
        this.mTimeStamp = System.currentTimeMillis();
    }

    public Gear(Cursor cursor) {
        this.mActive = true;
        this.mCurrentGear = true;
        this.mLifeSpanKm = 800L;
        this.mTimeStamp = System.currentTimeMillis();
        this.mId = FieldHelper.getLong(cursor, "_id");
        this.mName = Field.NAME.getString(cursor);
        this.mActive = Field.GEAR_STATUS.getInt(cursor) == 1;
        this.mCurrentGear = Field.CURRENT_GEAR.getInt(cursor) == 1;
        this.mLifeSpanKm = (long) Field.LIFESPAN_KM.getDouble(cursor);
        this.mTimeStamp = Field.TIMESTAMP.getLong(cursor);
    }

    public long getId() {
        return this.mId;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getName() {
        return (this.mName == null || this.mName.isEmpty()) ? "Default Gear" : this.mName;
    }

    public long getTimeStamp() {
        return this.mTimeStamp;
    }

    public long getLifeSpan() {
        return this.mLifeSpanKm;
    }

    public boolean isActive() {
        return this.mActive;
    }

    public void setActive(boolean active) {
        this.mActive = active;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(Field.CURRENT_GEAR.name(), Integer.valueOf(this.mCurrentGear ? 1 : 0));
        values.put(Field.GEAR_STATUS.name(), Integer.valueOf(this.mActive ? 1 : 0));
        values.put(Field.NAME.name(), this.mName);
        values.put(Field.TIMESTAMP.name(), Long.valueOf(this.mTimeStamp));
        values.put(Field.LIFESPAN_KM.name(), Float.valueOf(this.mLifeSpanKm));
        return values;
    }

    public static String getCreateFieldsQuery() {
        StringBuilder builder = new StringBuilder();
        for (FTP.Field field : FTP.Field.values()) {
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
        return new Shared(this.mId, providerKey, userName, externalId, false, Shared.ShareType.GEAR);
    }
}
