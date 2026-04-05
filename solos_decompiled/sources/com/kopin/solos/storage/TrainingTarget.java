package com.kopin.solos.storage;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;
import com.kopin.solos.storage.field.FieldHelper;
import com.kopin.solos.storage.field.FieldType;
import com.kopin.solos.storage.util.MetricType;

/* JADX INFO: loaded from: classes54.dex */
public class TrainingTarget implements BaseColumns {
    static final String NAME = "TrainingTarget";
    long mId;
    final double mMaxTarget;
    final MetricType mMetricType;
    final double mMinTarget;
    long mStepId;
    final double mThresholdTarget;
    long mTrainingId;
    final TargetType mType;

    public enum TargetType {
        ABOVE,
        BELOW,
        RANGE
    }

    public enum Field {
        TRAINING_ID(FieldType.INTEGER),
        STEP_ID(FieldType.INTEGER),
        METRIC(FieldType.INTEGER),
        TYPE(FieldType.INTEGER),
        THRESHOLD(FieldType.REAL),
        MINIMUM(FieldType.REAL),
        MAXIMUM(FieldType.REAL);

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

    public TrainingTarget(MetricType metricType, TargetType targetType, double threshold, double minTarget, double maxTarget) {
        this.mMetricType = metricType;
        this.mType = targetType;
        this.mThresholdTarget = threshold;
        this.mMinTarget = minTarget;
        this.mMaxTarget = maxTarget;
    }

    TrainingTarget(Cursor cursor) {
        this.mId = FieldHelper.getLong(cursor, "_id");
        this.mTrainingId = Field.TRAINING_ID.getLong(cursor);
        this.mStepId = Field.STEP_ID.getLong(cursor);
        this.mMetricType = MetricType.values()[Field.METRIC.getInt(cursor)];
        this.mType = TargetType.values()[Field.TYPE.getInt(cursor)];
        this.mThresholdTarget = Field.THRESHOLD.getDouble(cursor);
        this.mMinTarget = Field.MINIMUM.getDouble(cursor);
        this.mMaxTarget = Field.MAXIMUM.getDouble(cursor);
    }

    public void setTrainingId(long trainingId) {
        this.mTrainingId = trainingId;
    }

    public void setStepId(long stepId) {
        this.mStepId = stepId;
    }

    public long addToDb() {
        this.mId = SQLHelper.addRow(NAME, getColumnValues());
        return this.mId;
    }

    private ContentValues getColumnValues() {
        ContentValues values = new ContentValues();
        values.put(Field.TRAINING_ID.name(), Long.valueOf(this.mTrainingId));
        values.put(Field.STEP_ID.name(), Long.valueOf(this.mStepId));
        values.put(Field.METRIC.name(), Integer.valueOf(this.mMetricType.ordinal()));
        values.put(Field.TYPE.name(), Integer.valueOf(this.mType.ordinal()));
        values.put(Field.THRESHOLD.name(), Double.valueOf(this.mThresholdTarget));
        values.put(Field.MINIMUM.name(), Double.valueOf(this.mMinTarget));
        values.put(Field.MAXIMUM.name(), Double.valueOf(this.mMaxTarget));
        return values;
    }
}
