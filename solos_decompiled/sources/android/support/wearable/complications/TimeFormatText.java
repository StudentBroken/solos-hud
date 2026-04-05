package android.support.wearable.complications;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.support.wearable.complications.ComplicationText;
import com.kopin.solos.storage.settings.Prefs;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(24)
public class TimeFormatText implements ComplicationText.TimeDependentText {
    private final Date mDate;
    private final SimpleDateFormat mDateFormat;
    private final int mStyle;
    private long mTimePrecision = -1;
    private final TimeZone mTimeZone;
    private static final String[][] DATE_TIME_FORMAT_SYMBOLS = {new String[]{"S", Prefs.UNIT_SPEED}, new String[]{Prefs.MODE_MANUAL}, new String[]{"H", "K", "h", "k"}, new String[]{"D", "E", "F", "c", Prefs.MODE_DISTANCE, "W", "w", "M", "y"}};
    private static final long[] DATE_TIME_FORMAT_PRECISION = {TimeUnit.SECONDS.toMillis(1), TimeUnit.MINUTES.toMillis(1), TimeUnit.HOURS.toMillis(1), TimeUnit.DAYS.toMillis(1)};

    public TimeFormatText(String format, int style, TimeZone timeZone) {
        this.mDateFormat = new SimpleDateFormat(format);
        this.mStyle = style;
        if (timeZone != null) {
            this.mDateFormat.setTimeZone(timeZone);
            this.mTimeZone = timeZone;
        } else {
            this.mTimeZone = this.mDateFormat.getTimeZone();
        }
        this.mDate = new Date();
    }

    @Override // android.support.wearable.complications.ComplicationText.TimeDependentText
    @SuppressLint({"DefaultLocale"})
    public CharSequence getText(Context context, long dateTimeMillis) {
        String formattedDate = this.mDateFormat.format(new Date(dateTimeMillis));
        switch (this.mStyle) {
            case 2:
                return formattedDate.toUpperCase();
            case 3:
                return formattedDate.toLowerCase();
            default:
                return formattedDate;
        }
    }

    @Override // android.support.wearable.complications.ComplicationText.TimeDependentText
    public boolean returnsSameText(long dateTimeMills1, long dateTimeMills2) {
        long precision = getPrecision();
        return (dateTimeMills1 + getOffset(dateTimeMills1)) / precision == (dateTimeMills2 + getOffset(dateTimeMills2)) / precision;
    }

    public long getPrecision() {
        if (this.mTimePrecision == -1) {
            String format = getDateFormatWithoutText(this.mDateFormat.toPattern());
            for (int i = 0; i < DATE_TIME_FORMAT_SYMBOLS.length && this.mTimePrecision == -1; i++) {
                int j = 0;
                while (true) {
                    if (j >= DATE_TIME_FORMAT_SYMBOLS[i].length) {
                        break;
                    }
                    if (!format.contains(DATE_TIME_FORMAT_SYMBOLS[i][j])) {
                        j++;
                    } else {
                        this.mTimePrecision = DATE_TIME_FORMAT_PRECISION[i];
                        break;
                    }
                }
            }
        }
        return this.mTimePrecision;
    }

    public String getFormatString() {
        return this.mDateFormat.toPattern();
    }

    public int getStyle() {
        return this.mStyle;
    }

    public TimeZone getTimeZone() {
        return this.mTimeZone;
    }

    private long getOffset(long date) {
        this.mDate.setTime(date);
        return this.mTimeZone.inDaylightTime(this.mDate) ? this.mTimeZone.getRawOffset() + this.mTimeZone.getDSTSavings() : this.mTimeZone.getRawOffset();
    }

    private String getDateFormatWithoutText(String format) {
        String result = "";
        boolean isTextPart = false;
        int index = 0;
        while (index < format.length()) {
            if (format.charAt(index) == '\'') {
                if (index + 1 < format.length() && format.charAt(index + 1) == '\'') {
                    index += 2;
                } else {
                    index++;
                    isTextPart = !isTextPart;
                }
            } else {
                if (!isTextPart) {
                    String strValueOf = String.valueOf(result);
                    result = new StringBuilder(String.valueOf(strValueOf).length() + 1).append(strValueOf).append(format.charAt(index)).toString();
                }
                index++;
            }
        }
        return result;
    }
}
