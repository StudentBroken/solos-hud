package com.kopin.solos.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import com.kopin.solos.storage.Shared;
import com.kopin.solos.storage.field.FieldHelper;
import com.kopin.solos.storage.field.FieldType;

/* JADX INFO: loaded from: classes54.dex */
public class Bike implements BaseColumns, ISyncable {
    public static final String TABLE = "Bike";
    boolean mActive;
    long mId;
    String mName;
    BikeType mType;
    double mWeight;
    int mWheelSize;

    public enum BikeType {
        DEFAULT(R.string.bike_type_default, 2100, 11.0d),
        HYBRID(R.string.bike_type_hybrid, 2100, 12.5d),
        MOUNTAIN(R.string.bike_type_mountain, 2150, 13.5d),
        ROAD(R.string.bike_type_road, 2100, 10.0d);

        public static final BikeType[] VALUES = values();
        private int bikeTypeResId;
        double weight;
        private int wheelSize;

        BikeType(int resId, int wheelSize, double weight) {
            this.bikeTypeResId = resId;
            this.wheelSize = wheelSize;
            this.weight = weight;
        }

        public String toString(Context context) {
            return context.getString(this.bikeTypeResId);
        }

        public int defaultWheelSize() {
            return this.wheelSize;
        }

        public double defaultWeight() {
            return this.weight;
        }

        public static String[] toStringArray(Context context) {
            return new String[]{context.getString(R.string.bike_type_default), context.getString(R.string.bike_type_hybrid), context.getString(R.string.bike_type_mountain), context.getString(R.string.bike_type_road)};
        }
    }

    public enum Field {
        NAME(FieldType.TEXT),
        TYPE(FieldType.INTEGER),
        WHEEL_SIZE(FieldType.INTEGER),
        WEIGHT(FieldType.REAL),
        ACTIVE(FieldType.INTEGER);

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

    public static Bike getDefaultBike(Context context) {
        return new Bike(BikeType.DEFAULT.toString(context), BikeType.DEFAULT, BikeType.DEFAULT.defaultWheelSize(), BikeType.DEFAULT.defaultWeight());
    }

    public Bike(Cursor cursor) {
        this.mActive = true;
        this.mType = BikeType.DEFAULT;
        this.mId = FieldHelper.getLong(cursor, "_id");
        this.mName = Field.NAME.getString(cursor);
        this.mType = BikeType.VALUES[Field.TYPE.getInt(cursor)];
        this.mWheelSize = Field.WHEEL_SIZE.getInt(cursor);
        this.mWeight = Field.WEIGHT.getDouble(cursor);
        this.mActive = Field.ACTIVE.getInt(cursor) > 0;
    }

    public Bike(String bikeTitle, BikeType bikeType, int wheelSize, double bikeWeight) {
        this(bikeTitle, bikeType, wheelSize, bikeWeight, true);
    }

    public Bike(String bikeTitle, BikeType bikeType, int wheelSize, double bikeWeight, boolean active) {
        this.mActive = true;
        this.mType = BikeType.DEFAULT;
        this.mName = bikeTitle;
        this.mType = bikeType;
        this.mWheelSize = wheelSize;
        this.mWeight = bikeWeight;
        this.mActive = active;
    }

    public Bike(ContentValues values) {
        this.mActive = true;
        this.mType = BikeType.DEFAULT;
        if (values.containsKey(Field.NAME.name())) {
            this.mName = values.getAsString(Field.NAME.name());
        }
        if (values.containsKey(Field.WHEEL_SIZE.name())) {
            this.mWheelSize = values.getAsDouble(Field.WHEEL_SIZE.name()).intValue();
        }
        if (values.containsKey(Field.WEIGHT.name())) {
            this.mWeight = values.getAsDouble(Field.WEIGHT.name()).doubleValue();
        }
    }

    Bike(String name, BikeType type) {
        this(name, type, type.defaultWheelSize(), type.defaultWeight());
    }

    public long getId() {
        return this.mId;
    }

    public String getName() {
        return (this.mName == null || this.mName.isEmpty()) ? SQLHelper.getString(this.mType.bikeTypeResId) : this.mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public BikeType getType() {
        return this.mType;
    }

    public void setType(BikeType bikeType) {
        this.mType = bikeType;
    }

    public int getWheelSize() {
        return this.mWheelSize;
    }

    public void setWheelSize(int wheelSize) {
        this.mWheelSize = wheelSize;
    }

    public double getWeight() {
        return this.mWeight;
    }

    public void setWeight(double weight) {
        this.mWeight = weight;
    }

    public boolean isActive() {
        return this.mActive;
    }

    public void setActive(boolean active) {
        this.mActive = active;
    }

    public boolean equals(Object o) {
        return false;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(Field.NAME.name(), this.mName);
        values.put(Field.WHEEL_SIZE.name(), Integer.valueOf(this.mWheelSize));
        values.put(Field.WEIGHT.name(), Double.valueOf(this.mWeight));
        return values;
    }

    public static String getCreateFieldsQuery() {
        StringBuilder builder = new StringBuilder();
        for (Field field : Field.values()) {
            if (builder.length() > 0) {
                builder.append(", ");
            }
            builder.append(field.name()).append(" ").append(field.fieldType);
        }
        return builder.toString();
    }

    @Override // com.kopin.solos.storage.ISyncable
    public Shared toShared(int providerKey, String userName, String externalId) {
        return new Shared(this.mId, providerKey, userName, externalId, false, Shared.ShareType.BIKE);
    }

    public String toString() {
        return "Bike " + this.mId + ", " + this.mName + ", " + this.mType.name();
    }
}
