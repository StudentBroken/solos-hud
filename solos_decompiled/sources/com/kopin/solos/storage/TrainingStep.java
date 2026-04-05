package com.kopin.solos.storage;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;
import com.kopin.solos.storage.field.FieldHelper;
import com.kopin.solos.storage.field.FieldType;

/* JADX INFO: loaded from: classes54.dex */
public class TrainingStep implements BaseColumns {
    static final String NAME = "TrainingStep";
    final double mDistance;
    final long mDuration;
    long mId;
    final IntensityClass mIntensityClass;
    final String mNotes;
    long mSegmentId;
    final long mSequence;
    final String mTitle;
    long mTrainingId;
    final Trigger mTrigger;

    public enum Trigger {
        TIME,
        DISTANCE,
        MANUAL,
        TARGET_ACHIEVED
    }

    public enum IntensityClass {
        ACTIVE,
        WARM_UP,
        COOL_DOWN,
        RECOVER;

        public static IntensityClass get(String intensity) {
            switch (intensity) {
                case "Active":
                    return ACTIVE;
                case "WarmUp":
                    return WARM_UP;
                case "CoolDown":
                    return COOL_DOWN;
                default:
                    return RECOVER;
            }
        }
    }

    public enum Field {
        TRAINING_ID(FieldType.INTEGER),
        SEGMENT_ID(FieldType.INTEGER),
        TITLE(FieldType.TEXT),
        SEQUENCE(FieldType.INTEGER),
        DURATION(FieldType.INTEGER),
        DISTANCE(FieldType.REAL),
        INTENSITY_CLASS(FieldType.INTEGER),
        TRIGGER(FieldType.INTEGER),
        NOTES(FieldType.TEXT);

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

    public TrainingStep(String title, IntensityClass intensityClass, long sequence, long duration, double distance, Trigger trigger, String notes) {
        this.mTitle = title;
        this.mDuration = duration;
        this.mDistance = distance;
        this.mNotes = notes;
        this.mSequence = sequence;
        this.mTrigger = trigger;
        this.mIntensityClass = intensityClass;
    }

    TrainingStep(Cursor cursor) {
        this.mId = FieldHelper.getLong(cursor, "_id");
        this.mTrainingId = Field.TRAINING_ID.getLong(cursor);
        this.mSegmentId = Field.SEGMENT_ID.getLong(cursor);
        this.mTitle = Field.TITLE.getString(cursor);
        this.mDuration = Field.DURATION.getLong(cursor);
        this.mDistance = Field.DISTANCE.getDouble(cursor);
        this.mSequence = Field.SEQUENCE.getLong(cursor);
        this.mNotes = Field.NOTES.getString(cursor);
        this.mTrigger = Trigger.values()[Field.TRIGGER.getInt(cursor)];
        this.mIntensityClass = IntensityClass.values()[Field.INTENSITY_CLASS.getInt(cursor)];
    }

    public void setTrainingId(long trainingId) {
        this.mTrainingId = trainingId;
    }

    public void setSegmentId(long segmentId) {
        this.mSegmentId = segmentId;
    }

    public long addToDb() {
        this.mId = SQLHelper.addRow(NAME, getColumnValues());
        return this.mId;
    }

    private ContentValues getColumnValues() {
        ContentValues values = new ContentValues();
        values.put(Field.TRAINING_ID.name(), Long.valueOf(this.mTrainingId));
        values.put(Field.SEGMENT_ID.name(), Long.valueOf(this.mSegmentId));
        values.put(Field.TITLE.name(), this.mTitle);
        values.put(Field.NOTES.name(), this.mNotes);
        values.put(Field.DISTANCE.name(), Double.valueOf(this.mDistance));
        values.put(Field.SEQUENCE.name(), Long.valueOf(this.mSequence));
        values.put(Field.DURATION.name(), Long.valueOf(this.mDuration));
        values.put(Field.TRIGGER.name(), Integer.valueOf(this.mTrigger.ordinal()));
        values.put(Field.INTENSITY_CLASS.name(), Integer.valueOf(this.mIntensityClass.ordinal()));
        return values;
    }
}
