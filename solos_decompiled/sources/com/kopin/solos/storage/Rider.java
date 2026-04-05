package com.kopin.solos.storage;

import android.database.Cursor;
import android.provider.BaseColumns;
import com.kopin.solos.storage.field.FieldHelper;
import com.kopin.solos.storage.field.FieldType;
import com.kopin.solos.storage.settings.UserProfile;

/* JADX INFO: loaded from: classes54.dex */
public class Rider implements BaseColumns {
    public static final String TABLE_NAME = "Rider";
    public UserProfile.Gender gender;
    int mAge;
    long mId;
    double mWeight;
    public String name;

    public enum Field {
        AGE(FieldType.INTEGER),
        WEIGHT(FieldType.REAL),
        USER_NAME(FieldType.TEXT),
        GENDER(FieldType.INTEGER);

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

    public Rider(Cursor cursor) {
        this.name = "";
        this.gender = UserProfile.Gender.UNKNOWN;
        this.mId = FieldHelper.getLong(cursor, "_id");
        this.mAge = Field.AGE.getInt(cursor);
        this.mWeight = Field.WEIGHT.getDouble(cursor);
        this.name = Field.USER_NAME.getString(cursor);
        this.gender = UserProfile.Gender.get(Field.GENDER.getInt(cursor));
    }

    public Rider(int age, double weight, String name, UserProfile.Gender gender) {
        this.name = "";
        this.gender = UserProfile.Gender.UNKNOWN;
        this.mAge = age;
        this.mWeight = weight;
        this.name = name;
        this.gender = gender;
    }

    public long getId() {
        return this.mId;
    }

    public int getAge() {
        return this.mAge;
    }

    public double getWeight() {
        return this.mWeight;
    }

    public boolean equals(Object o) {
        return false;
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
}
