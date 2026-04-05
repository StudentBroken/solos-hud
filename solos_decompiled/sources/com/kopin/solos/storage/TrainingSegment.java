package com.kopin.solos.storage;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;
import com.kopin.solos.storage.field.FieldHelper;
import com.kopin.solos.storage.field.FieldType;

/* JADX INFO: loaded from: classes54.dex */
public class TrainingSegment implements BaseColumns {
    static final String NAME = "TrainingSegment";
    long mId;
    final int mLoopCount;
    final int mStepCount;
    long mTrainingId;
    final SegmentType mType;

    public enum SegmentType {
        STEP,
        RAMP_UP,
        RAMP_DOWN,
        REPEAT;

        public static SegmentType get(String type) {
            switch (type) {
                case "rampUp":
                case "RampUp":
                    return RAMP_UP;
                case "repetition":
                case "Repetition":
                    return REPEAT;
                default:
                    return STEP;
            }
        }
    }

    public enum Field {
        TRAINING_ID(FieldType.INTEGER),
        STEP_COUNT(FieldType.REAL),
        LOOP_COUNT(FieldType.INTEGER),
        TYPE(FieldType.INTEGER);

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

    public TrainingSegment(int stepCount, int loopCount, SegmentType type) {
        this.mStepCount = stepCount;
        this.mLoopCount = loopCount;
        this.mType = type;
    }

    TrainingSegment(Cursor cursor) {
        this.mId = FieldHelper.getLong(cursor, "_id");
        this.mTrainingId = Field.TRAINING_ID.getLong(cursor);
        this.mStepCount = Field.STEP_COUNT.getInt(cursor);
        this.mLoopCount = Field.LOOP_COUNT.getInt(cursor);
        this.mType = SegmentType.values()[Field.TYPE.getInt(cursor)];
    }

    public void setTrainingId(long trainingId) {
        this.mTrainingId = trainingId;
    }

    public long addToDb() {
        this.mId = SQLHelper.addRow(NAME, getColumnValues());
        return this.mId;
    }

    private ContentValues getColumnValues() {
        ContentValues values = new ContentValues();
        values.put(Field.TRAINING_ID.name(), Long.valueOf(this.mTrainingId));
        values.put(Field.STEP_COUNT.name(), Integer.valueOf(this.mStepCount));
        values.put(Field.LOOP_COUNT.name(), Integer.valueOf(this.mLoopCount));
        values.put(Field.TYPE.name(), Integer.valueOf(this.mType.ordinal()));
        return values;
    }
}
