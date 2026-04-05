package android.support.wearable.complications;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(24)
public class ComplicationText implements Parcelable {
    public static final Parcelable.Creator<ComplicationText> CREATOR = new Parcelable.Creator<ComplicationText>() { // from class: android.support.wearable.complications.ComplicationText.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ComplicationText createFromParcel(Parcel in) {
            return new ComplicationText(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ComplicationText[] newArray(int size) {
            return new ComplicationText[size];
        }
    };
    public static final int DIFFERENCE_STYLE_SHORT_DUAL_UNIT = 3;
    public static final int DIFFERENCE_STYLE_SHORT_SINGLE_UNIT = 2;
    public static final int DIFFERENCE_STYLE_SHORT_WORDS_SINGLE_UNIT = 5;
    public static final int DIFFERENCE_STYLE_STOPWATCH = 1;
    public static final int DIFFERENCE_STYLE_WORDS_SINGLE_UNIT = 4;
    public static final int FORMAT_STYLE_DEFAULT = 1;
    public static final int FORMAT_STYLE_LOWER_CASE = 3;
    public static final int FORMAT_STYLE_UPPER_CASE = 2;
    private static final String KEY_DIFFERENCE_MINIMUM_UNIT = "minimum_unit";
    private static final String KEY_DIFFERENCE_PERIOD_END = "difference_period_end";
    private static final String KEY_DIFFERENCE_PERIOD_START = "difference_period_start";
    private static final String KEY_DIFFERENCE_SHOW_NOW_TEXT = "show_now_text";
    private static final String KEY_DIFFERENCE_STYLE = "difference_style";
    private static final String KEY_FORMAT_FORMAT_STRING = "format_format_string";
    private static final String KEY_FORMAT_STYLE = "format_style";
    private static final String KEY_FORMAT_TIME_ZONE = "format_time_zone";
    private static final String KEY_SURROUNDING_STRING = "surrounding_string";
    private CharSequence mDependentTextCache;
    private long mDependentTextCacheTime;
    private final CharSequence mSurroundingText;
    private final TimeDependentText mTimeDependentText;

    interface TimeDependentText {
        CharSequence getText(Context context, long j);

        boolean returnsSameText(long j, long j2);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface TimeDifferenceStyle {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface TimeFormatStyle {
    }

    private ComplicationText(CharSequence surroundingText, TimeDependentText timeDependentText) {
        this.mSurroundingText = surroundingText;
        this.mTimeDependentText = timeDependentText;
        checkFields();
    }

    private ComplicationText(Parcel in) {
        Bundle bundle = in.readBundle(getClass().getClassLoader());
        this.mSurroundingText = bundle.getCharSequence(KEY_SURROUNDING_STRING);
        if (bundle.containsKey(KEY_DIFFERENCE_STYLE) && bundle.containsKey(KEY_DIFFERENCE_PERIOD_START) && bundle.containsKey(KEY_DIFFERENCE_PERIOD_END)) {
            this.mTimeDependentText = new TimeDifferenceText(bundle.getLong(KEY_DIFFERENCE_PERIOD_START), bundle.getLong(KEY_DIFFERENCE_PERIOD_END), bundle.getInt(KEY_DIFFERENCE_STYLE), bundle.getBoolean(KEY_DIFFERENCE_SHOW_NOW_TEXT, true), timeUnitFromName(bundle.getString(KEY_DIFFERENCE_MINIMUM_UNIT)));
        } else if (bundle.containsKey(KEY_FORMAT_FORMAT_STRING) && bundle.containsKey(KEY_FORMAT_STYLE)) {
            TimeZone timeZone = bundle.containsKey(KEY_FORMAT_TIME_ZONE) ? TimeZone.getTimeZone(bundle.getString(KEY_FORMAT_TIME_ZONE)) : null;
            this.mTimeDependentText = new TimeFormatText(bundle.getString(KEY_FORMAT_FORMAT_STRING), bundle.getInt(KEY_FORMAT_STYLE), timeZone);
        } else {
            this.mTimeDependentText = null;
        }
        checkFields();
    }

    private static TimeUnit timeUnitFromName(@Nullable String name) {
        if (name == null) {
            return null;
        }
        try {
            return TimeUnit.valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private void checkFields() {
        if (this.mSurroundingText == null && this.mTimeDependentText == null) {
            throw new IllegalStateException("One of mSurroundingText and mTimeDependentText must be non-null");
        }
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        Bundle bundle = new Bundle();
        bundle.putCharSequence(KEY_SURROUNDING_STRING, this.mSurroundingText);
        if (this.mTimeDependentText instanceof TimeDifferenceText) {
            TimeDifferenceText timeDiffText = (TimeDifferenceText) this.mTimeDependentText;
            bundle.putLong(KEY_DIFFERENCE_PERIOD_START, timeDiffText.getReferencePeriodStart());
            bundle.putLong(KEY_DIFFERENCE_PERIOD_END, timeDiffText.getReferencePeriodEnd());
            bundle.putInt(KEY_DIFFERENCE_STYLE, timeDiffText.getStyle());
            bundle.putBoolean(KEY_DIFFERENCE_SHOW_NOW_TEXT, timeDiffText.shouldShowNowText());
            if (timeDiffText.getMinimumUnit() != null) {
                bundle.putString(KEY_DIFFERENCE_MINIMUM_UNIT, timeDiffText.getMinimumUnit().name());
            }
        } else if (this.mTimeDependentText instanceof TimeFormatText) {
            TimeFormatText timeFormatText = (TimeFormatText) this.mTimeDependentText;
            bundle.putString(KEY_FORMAT_FORMAT_STRING, timeFormatText.getFormatString());
            bundle.putInt(KEY_FORMAT_STYLE, timeFormatText.getStyle());
            TimeZone timeZone = timeFormatText.getTimeZone();
            if (timeZone != null) {
                bundle.putString(KEY_FORMAT_TIME_ZONE, timeZone.getID());
            }
        }
        out.writeBundle(bundle);
    }

    public CharSequence getText(Context context, long dateTimeMillis) {
        CharSequence timeDependentPart;
        if (this.mTimeDependentText == null) {
            return this.mSurroundingText;
        }
        if (this.mDependentTextCache != null && this.mTimeDependentText.returnsSameText(this.mDependentTextCacheTime, dateTimeMillis)) {
            timeDependentPart = this.mDependentTextCache;
        } else {
            timeDependentPart = this.mTimeDependentText.getText(context, dateTimeMillis);
            this.mDependentTextCacheTime = dateTimeMillis;
            this.mDependentTextCache = timeDependentPart;
        }
        return this.mSurroundingText != null ? TextUtils.expandTemplate(this.mSurroundingText, timeDependentPart) : timeDependentPart;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public static ComplicationText plainText(CharSequence text) {
        return new ComplicationText(text, (TimeDependentText) null);
    }

    public static final class TimeDifferenceBuilder {
        private static final long NO_PERIOD_END = Long.MAX_VALUE;
        private static final long NO_PERIOD_START = 0;
        private TimeUnit mMinimumUnit;
        private Boolean mShowNowText;
        private CharSequence mSurroundingText;
        private long mReferencePeriodStart = 0;
        private long mReferencePeriodEnd = NO_PERIOD_END;
        private int mStyle = 3;

        public TimeDifferenceBuilder setReferencePeriodStart(long refPeriodStart) {
            if (refPeriodStart < 0) {
                throw new IllegalArgumentException("Reference period start cannot be negative");
            }
            this.mReferencePeriodStart = refPeriodStart;
            return this;
        }

        public TimeDifferenceBuilder setReferencePeriodEnd(long refPeriodEnd) {
            if (refPeriodEnd < 0) {
                throw new IllegalArgumentException("Reference period end cannot be negative");
            }
            this.mReferencePeriodEnd = refPeriodEnd;
            return this;
        }

        public TimeDifferenceBuilder setStyle(int style) {
            this.mStyle = style;
            return this;
        }

        public TimeDifferenceBuilder setSurroundingText(CharSequence surroundingText) {
            this.mSurroundingText = surroundingText;
            return this;
        }

        public TimeDifferenceBuilder setShowNowText(boolean showNowText) {
            this.mShowNowText = Boolean.valueOf(showNowText);
            return this;
        }

        public TimeDifferenceBuilder setMinimumUnit(@Nullable TimeUnit minimumUnit) {
            this.mMinimumUnit = minimumUnit;
            return this;
        }

        public ComplicationText build() {
            if (this.mReferencePeriodEnd < this.mReferencePeriodStart) {
                throw new IllegalStateException("Reference period end must not be before start.");
            }
            boolean showNowText = this.mShowNowText == null ? getDefaultShowNowTextForStyle(this.mStyle) : this.mShowNowText.booleanValue();
            return new ComplicationText(this.mSurroundingText, new TimeDifferenceText(this.mReferencePeriodStart, this.mReferencePeriodEnd, this.mStyle, showNowText, this.mMinimumUnit));
        }

        private static boolean getDefaultShowNowTextForStyle(int style) {
            switch (style) {
                case 1:
                    return false;
                default:
                    return true;
            }
        }
    }

    public static final class TimeFormatBuilder {
        private String mFormat;
        private int mStyle = 1;
        private CharSequence mSurroundingText;
        private TimeZone mTimeZone;

        public TimeFormatBuilder setFormat(String format) {
            this.mFormat = format;
            return this;
        }

        public TimeFormatBuilder setStyle(int style) {
            this.mStyle = style;
            return this;
        }

        public TimeFormatBuilder setSurroundingText(CharSequence surroundingText) {
            this.mSurroundingText = surroundingText;
            return this;
        }

        public TimeFormatBuilder setTimeZone(TimeZone timeZone) {
            this.mTimeZone = timeZone;
            return this;
        }

        public ComplicationText build() {
            if (this.mFormat == null) {
                throw new IllegalStateException("Format must be specified.");
            }
            return new ComplicationText(this.mSurroundingText, new TimeFormatText(this.mFormat, this.mStyle, this.mTimeZone));
        }
    }

    public static CharSequence getText(Context context, ComplicationText complicationText, long dateTimeMillis) {
        if (complicationText == null) {
            return null;
        }
        return complicationText.getText(context, dateTimeMillis);
    }
}
