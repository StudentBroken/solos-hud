package com.kopin.solos.storage;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;
import com.kopin.solos.common.SportType;
import com.kopin.solos.storage.field.FieldHelper;
import com.kopin.solos.storage.field.FieldType;
import com.kopin.solos.storage.util.MetricType;

/* JADX INFO: loaded from: classes54.dex */
public class Training implements BaseColumns {
    public static final String NAME = "Training";
    String mDescription;
    double mDistance;
    long mDuration;
    long mId;
    SportType mSportType;
    String mTitle;
    MetricType mTrainingType;

    public enum Field {
        TITLE(FieldType.TEXT),
        DESCRIPTION(FieldType.TEXT),
        SPORT(FieldType.INTEGER),
        DURATION(FieldType.INTEGER),
        DISTANCE(FieldType.REAL),
        TRAINING_TYPE(FieldType.INTEGER);

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

        public float getFloat(Cursor cursor) {
            return FieldHelper.getFloat(cursor, name());
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

    public Training(long id, String title, String description, long duration, double distance, SportType sport, MetricType trainingType) {
        this.mId = id;
        this.mTitle = title;
        this.mDescription = description;
        this.mDuration = duration;
        this.mDistance = distance;
        this.mSportType = sport;
        this.mTrainingType = trainingType;
    }

    public Training(Cursor cursor) {
        this.mId = FieldHelper.getLong(cursor, "_id");
        this.mTitle = Field.TITLE.getString(cursor);
        this.mDescription = Field.DESCRIPTION.getString(cursor);
        this.mDuration = Field.DURATION.getLong(cursor);
        this.mDistance = Field.DISTANCE.getDouble(cursor);
        this.mSportType = SportType.values()[Field.SPORT.getInt(cursor)];
        this.mTrainingType = MetricType.values()[Field.TRAINING_TYPE.getInt(cursor)];
    }

    static void removeFromDb(long id) {
        SQLHelper.removeTraining(id);
    }

    public long addToBb() {
        return SQLHelper.addRow(NAME, getColumnValues());
    }

    private ContentValues getColumnValues() {
        ContentValues values = new ContentValues();
        values.put(Field.TITLE.name(), this.mTitle);
        values.put(Field.DESCRIPTION.name(), this.mDescription);
        values.put(Field.SPORT.name(), Integer.valueOf(this.mSportType.ordinal()));
        values.put(Field.DISTANCE.name(), Double.valueOf(this.mDistance));
        values.put(Field.DURATION.name(), Long.valueOf(this.mDuration));
        values.put(Field.TRAINING_TYPE.name(), Integer.valueOf(this.mTrainingType.ordinal()));
        return values;
    }
}
