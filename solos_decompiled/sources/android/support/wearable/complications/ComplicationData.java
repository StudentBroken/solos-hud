package android.support.wearable.complications;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.graphics.drawable.Icon;
import android.os.BadParcelableException;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(24)
public class ComplicationData implements Parcelable {
    private static final String FIELD_END_TIME = "END_TIME";
    private static final String FIELD_START_TIME = "START_TIME";
    public static final int IMAGE_STYLE_ICON = 2;
    public static final int IMAGE_STYLE_PHOTO = 1;
    private static final String TAG = "ComplicationData";
    public static final int TYPE_EMPTY = 2;
    public static final int TYPE_ICON = 6;
    public static final int TYPE_LARGE_IMAGE = 8;
    public static final int TYPE_LONG_TEXT = 4;
    public static final int TYPE_NOT_CONFIGURED = 1;
    public static final int TYPE_NO_DATA = 10;
    public static final int TYPE_NO_PERMISSION = 9;
    public static final int TYPE_RANGED_VALUE = 5;
    public static final int TYPE_SHORT_TEXT = 3;
    public static final int TYPE_SMALL_IMAGE = 7;
    private final Bundle mFields;
    private final int mType;
    private static final String FIELD_SHORT_TEXT = "SHORT_TEXT";
    private static final String FIELD_LONG_TEXT = "LONG_TEXT";
    private static final String FIELD_VALUE = "VALUE";
    private static final String FIELD_MIN_VALUE = "MIN_VALUE";
    private static final String FIELD_MAX_VALUE = "MAX_VALUE";
    private static final String FIELD_ICON = "ICON";
    private static final String FIELD_SMALL_IMAGE = "SMALL_IMAGE";
    private static final String FIELD_IMAGE_STYLE = "IMAGE_STYLE";
    private static final String FIELD_LARGE_IMAGE = "LARGE_IMAGE";
    private static final String[][] REQUIRED_FIELDS = {null, new String[0], new String[0], new String[]{FIELD_SHORT_TEXT}, new String[]{FIELD_LONG_TEXT}, new String[]{FIELD_VALUE, FIELD_MIN_VALUE, FIELD_MAX_VALUE}, new String[]{FIELD_ICON}, new String[]{FIELD_SMALL_IMAGE, FIELD_IMAGE_STYLE}, new String[]{FIELD_LARGE_IMAGE}, new String[0], new String[0]};
    private static final String FIELD_SHORT_TITLE = "SHORT_TITLE";
    private static final String FIELD_ICON_BURN_IN_PROTECTION = "ICON_BURN_IN_PROTECTION";
    private static final String FIELD_TAP_ACTION = "TAP_ACTION";
    private static final String FIELD_LONG_TITLE = "LONG_TITLE";
    private static final String[][] OPTIONAL_FIELDS = {null, new String[0], new String[0], new String[]{FIELD_SHORT_TITLE, FIELD_ICON, FIELD_ICON_BURN_IN_PROTECTION, FIELD_TAP_ACTION}, new String[]{FIELD_LONG_TITLE, FIELD_ICON, FIELD_ICON_BURN_IN_PROTECTION, FIELD_SMALL_IMAGE, FIELD_IMAGE_STYLE, FIELD_TAP_ACTION}, new String[]{FIELD_SHORT_TEXT, FIELD_SHORT_TITLE, FIELD_ICON, FIELD_ICON_BURN_IN_PROTECTION, FIELD_TAP_ACTION}, new String[]{FIELD_TAP_ACTION, FIELD_ICON_BURN_IN_PROTECTION}, new String[]{FIELD_TAP_ACTION}, new String[]{FIELD_TAP_ACTION}, new String[]{FIELD_SHORT_TEXT, FIELD_SHORT_TITLE, FIELD_ICON, FIELD_ICON_BURN_IN_PROTECTION}, new String[0]};
    public static final Parcelable.Creator<ComplicationData> CREATOR = new Parcelable.Creator<ComplicationData>() { // from class: android.support.wearable.complications.ComplicationData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ComplicationData createFromParcel(Parcel source) {
            return new ComplicationData(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ComplicationData[] newArray(int size) {
            return new ComplicationData[size];
        }
    };

    @Retention(RetentionPolicy.SOURCE)
    public @interface ComplicationType {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ImageStyle {
    }

    private ComplicationData(Builder builder) {
        this.mType = builder.mType;
        this.mFields = builder.mFields;
    }

    private ComplicationData(Parcel in) {
        this.mType = in.readInt();
        this.mFields = in.readBundle(getClass().getClassLoader());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mType);
        dest.writeBundle(this.mFields);
    }

    public int getType() {
        return this.mType;
    }

    public boolean isActive(long dateTimeMillis) {
        return dateTimeMillis >= this.mFields.getLong(FIELD_START_TIME, 0L) && dateTimeMillis <= this.mFields.getLong(FIELD_END_TIME, Long.MAX_VALUE);
    }

    public float getValue() {
        checkFieldValidForTypeWithoutThrowingException(FIELD_VALUE, this.mType);
        return this.mFields.getFloat(FIELD_VALUE);
    }

    public float getMinValue() {
        checkFieldValidForTypeWithoutThrowingException(FIELD_MIN_VALUE, this.mType);
        return this.mFields.getFloat(FIELD_MIN_VALUE);
    }

    public float getMaxValue() {
        checkFieldValidForTypeWithoutThrowingException(FIELD_MAX_VALUE, this.mType);
        return this.mFields.getFloat(FIELD_MAX_VALUE);
    }

    public ComplicationText getShortTitle() {
        checkFieldValidForTypeWithoutThrowingException(FIELD_SHORT_TITLE, this.mType);
        return (ComplicationText) getParcelableField(FIELD_SHORT_TITLE);
    }

    public ComplicationText getShortText() {
        checkFieldValidForTypeWithoutThrowingException(FIELD_SHORT_TEXT, this.mType);
        return (ComplicationText) getParcelableField(FIELD_SHORT_TEXT);
    }

    public ComplicationText getLongTitle() {
        checkFieldValidForTypeWithoutThrowingException(FIELD_LONG_TITLE, this.mType);
        return (ComplicationText) getParcelableField(FIELD_LONG_TITLE);
    }

    public ComplicationText getLongText() {
        checkFieldValidForTypeWithoutThrowingException(FIELD_LONG_TEXT, this.mType);
        return (ComplicationText) getParcelableField(FIELD_LONG_TEXT);
    }

    public Icon getIcon() {
        checkFieldValidForTypeWithoutThrowingException(FIELD_ICON, this.mType);
        return (Icon) getParcelableField(FIELD_ICON);
    }

    public Icon getBurnInProtectionIcon() {
        checkFieldValidForTypeWithoutThrowingException(FIELD_ICON_BURN_IN_PROTECTION, this.mType);
        return (Icon) getParcelableField(FIELD_ICON_BURN_IN_PROTECTION);
    }

    public Icon getSmallImage() {
        checkFieldValidForTypeWithoutThrowingException(FIELD_SMALL_IMAGE, this.mType);
        return (Icon) getParcelableField(FIELD_SMALL_IMAGE);
    }

    public int getImageStyle() {
        checkFieldValidForTypeWithoutThrowingException(FIELD_IMAGE_STYLE, this.mType);
        return this.mFields.getInt(FIELD_IMAGE_STYLE);
    }

    public Icon getLargeImage() {
        checkFieldValidForTypeWithoutThrowingException(FIELD_LARGE_IMAGE, this.mType);
        return (Icon) getParcelableField(FIELD_LARGE_IMAGE);
    }

    public PendingIntent getTapAction() {
        checkFieldValidForTypeWithoutThrowingException(FIELD_TAP_ACTION, this.mType);
        return (PendingIntent) getParcelableField(FIELD_TAP_ACTION);
    }

    private static boolean isFieldValidForType(String field, int type) {
        for (String requiredField : REQUIRED_FIELDS[type]) {
            if (requiredField.equals(field)) {
                return true;
            }
        }
        for (String optionalField : OPTIONAL_FIELDS[type]) {
            if (optionalField.equals(field)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isTypeSupported(int type) {
        return 1 <= type && type <= REQUIRED_FIELDS.length;
    }

    private static void checkFieldValidForTypeWithoutThrowingException(String field, int type) {
        if (!isTypeSupported(type)) {
            Log.w(TAG, new StringBuilder(38).append("Type ").append(type).append(" can not be recognized").toString());
        } else if (!isFieldValidForType(field, type) && Log.isLoggable(TAG, 3)) {
            Log.d(TAG, new StringBuilder(String.valueOf(field).length() + 44).append("Field ").append(field).append(" is not supported for type ").append(type).toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void checkFieldValidForTypeThrowingException(String field, int type) {
        if (!isTypeSupported(type)) {
            throw new IllegalStateException(new StringBuilder(38).append("Type ").append(type).append(" can not be recognized").toString());
        }
        if (!isFieldValidForType(field, type)) {
            throw new IllegalStateException(new StringBuilder(String.valueOf(field).length() + 44).append("Field ").append(field).append(" is not supported for type ").append(type).toString());
        }
    }

    private <T extends Parcelable> T getParcelableField(String str) {
        try {
            return (T) this.mFields.getParcelable(str);
        } catch (BadParcelableException e) {
            Log.w(TAG, "Could not unparcel ComplicationData. Provider apps must exclude wearable support complication classes from proguard.", e);
            return null;
        }
    }

    public String toString() {
        int i = this.mType;
        String strValueOf = String.valueOf(this.mFields);
        return new StringBuilder(String.valueOf(strValueOf).length() + 45).append("ComplicationData{mType=").append(i).append(", mFields=").append(strValueOf).append("}").toString();
    }

    public static final class Builder {
        private final Bundle mFields = new Bundle();
        private final int mType;

        public Builder(int type) {
            this.mType = type;
            if (type == 7 || type == 4) {
                setImageStyle(1);
            }
        }

        public Builder setStartTime(long startTime) {
            this.mFields.putLong(ComplicationData.FIELD_START_TIME, startTime);
            return this;
        }

        public Builder setEndTime(long endTime) {
            this.mFields.putLong(ComplicationData.FIELD_END_TIME, endTime);
            return this;
        }

        public Builder setValue(float value) {
            putFloatField(ComplicationData.FIELD_VALUE, value);
            return this;
        }

        public Builder setMinValue(float minValue) {
            putFloatField(ComplicationData.FIELD_MIN_VALUE, minValue);
            return this;
        }

        public Builder setMaxValue(float maxValue) {
            putFloatField(ComplicationData.FIELD_MAX_VALUE, maxValue);
            return this;
        }

        public Builder setLongTitle(ComplicationText longTitle) {
            putFieldIfNotNull(ComplicationData.FIELD_LONG_TITLE, longTitle);
            return this;
        }

        public Builder setLongText(ComplicationText longText) {
            putFieldIfNotNull(ComplicationData.FIELD_LONG_TEXT, longText);
            return this;
        }

        public Builder setShortTitle(ComplicationText shortTitle) {
            putFieldIfNotNull(ComplicationData.FIELD_SHORT_TITLE, shortTitle);
            return this;
        }

        public Builder setShortText(ComplicationText shortText) {
            putFieldIfNotNull(ComplicationData.FIELD_SHORT_TEXT, shortText);
            return this;
        }

        public Builder setIcon(Icon icon) {
            putFieldIfNotNull(ComplicationData.FIELD_ICON, icon);
            return this;
        }

        public Builder setBurnInProtectionIcon(Icon icon) {
            putFieldIfNotNull(ComplicationData.FIELD_ICON_BURN_IN_PROTECTION, icon);
            return this;
        }

        public Builder setSmallImage(Icon smallImage) {
            putFieldIfNotNull(ComplicationData.FIELD_SMALL_IMAGE, smallImage);
            return this;
        }

        public Builder setImageStyle(int imageStyle) {
            putIntField(ComplicationData.FIELD_IMAGE_STYLE, imageStyle);
            return this;
        }

        public Builder setLargeImage(Icon largeImage) {
            putFieldIfNotNull(ComplicationData.FIELD_LARGE_IMAGE, largeImage);
            return this;
        }

        public Builder setTapAction(PendingIntent pendingIntent) {
            putFieldIfNotNull(ComplicationData.FIELD_TAP_ACTION, pendingIntent);
            return this;
        }

        public ComplicationData build() {
            for (String requiredField : ComplicationData.REQUIRED_FIELDS[this.mType]) {
                if (!this.mFields.containsKey(requiredField)) {
                    throw new IllegalStateException(new StringBuilder(String.valueOf(requiredField).length() + 39).append("Field ").append(requiredField).append(" is required for type ").append(this.mType).toString());
                }
                if (this.mFields.containsKey(ComplicationData.FIELD_ICON_BURN_IN_PROTECTION) && !this.mFields.containsKey(ComplicationData.FIELD_ICON)) {
                    throw new IllegalStateException("Field ICON must be provided when field ICON_BURN_IN_PROTECTION is provided.");
                }
            }
            return new ComplicationData(this);
        }

        private void putIntField(String field, int value) {
            ComplicationData.checkFieldValidForTypeThrowingException(field, this.mType);
            this.mFields.putInt(field, value);
        }

        private void putFloatField(String field, float value) {
            ComplicationData.checkFieldValidForTypeThrowingException(field, this.mType);
            this.mFields.putFloat(field, value);
        }

        private void putFieldIfNotNull(String field, Object obj) {
            ComplicationData.checkFieldValidForTypeThrowingException(field, this.mType);
            if (obj == null) {
                this.mFields.remove(field);
                return;
            }
            if (obj instanceof String) {
                this.mFields.putString(field, (String) obj);
            } else if (obj instanceof Parcelable) {
                this.mFields.putParcelable(field, (Parcelable) obj);
            } else {
                String strValueOf = String.valueOf(obj.getClass());
                throw new IllegalArgumentException(new StringBuilder(String.valueOf(strValueOf).length() + 24).append("Unexpected object type: ").append(strValueOf).toString());
            }
        }
    }
}
