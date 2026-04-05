package android.support.wearable.complications;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.wearable.R;
import android.support.wearable.complications.ComplicationText;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(24)
public class TimeDifferenceText implements ComplicationText.TimeDependentText {
    private static final int ONLY_SHOW_DAYS_THRESHOLD = 10;
    private static final int SHORT_CHARACTER_LIMIT = 7;

    @Nullable
    private final TimeUnit mMinimumUnit;
    private final long mReferencePeriodEnd;
    private final long mReferencePeriodStart;
    private final boolean mShowNowText;
    private final int mStyle;

    public TimeDifferenceText(long referencePeriodStart, long referencePeriodEnd, int style, boolean showNowText, @Nullable TimeUnit minimumUnit) {
        this.mReferencePeriodStart = referencePeriodStart;
        this.mReferencePeriodEnd = referencePeriodEnd;
        this.mStyle = style;
        this.mShowNowText = showNowText;
        this.mMinimumUnit = minimumUnit;
    }

    @Override // android.support.wearable.complications.ComplicationText.TimeDependentText
    public CharSequence getText(Context context, long dateTimeMillis) {
        Resources res = context.getResources();
        long timeDifference = getTimeDifference(dateTimeMillis);
        if (timeDifference == 0 && this.mShowNowText) {
            return res.getString(R.string.time_difference_now);
        }
        switch (this.mStyle) {
            case 1:
                return buildStopwatchText(timeDifference, res);
            case 2:
                return buildShortSingleUnitText(timeDifference, res);
            case 3:
                return shortDualUnlessTooLong(timeDifference, res);
            case 4:
                return buildWordsSingleUnitText(timeDifference, res);
            case 5:
                return wordsSingleUnlessTooLong(timeDifference, res);
            default:
                return buildShortSingleUnitText(timeDifference, res);
        }
    }

    @Override // android.support.wearable.complications.ComplicationText.TimeDependentText
    public boolean returnsSameText(long dateTimeMills1, long dateTimeMills2) {
        long precision = getPrecision();
        return divRoundingUp(getTimeDifference(dateTimeMills1), precision) == divRoundingUp(getTimeDifference(dateTimeMills2), precision);
    }

    public long getPrecision() {
        long defaultPrecision;
        switch (this.mStyle) {
            case 1:
                defaultPrecision = TimeUnit.SECONDS.toMillis(1L);
                break;
            default:
                defaultPrecision = TimeUnit.MINUTES.toMillis(1L);
                break;
        }
        return this.mMinimumUnit == null ? defaultPrecision : Math.max(defaultPrecision, this.mMinimumUnit.toMillis(1L));
    }

    long getReferencePeriodStart() {
        return this.mReferencePeriodStart;
    }

    long getReferencePeriodEnd() {
        return this.mReferencePeriodEnd;
    }

    int getStyle() {
        return this.mStyle;
    }

    boolean shouldShowNowText() {
        return this.mShowNowText;
    }

    @Nullable
    TimeUnit getMinimumUnit() {
        return this.mMinimumUnit;
    }

    private long getTimeDifference(long dateTimeMillis) {
        if (dateTimeMillis < this.mReferencePeriodStart) {
            long timeDifference = this.mReferencePeriodStart - dateTimeMillis;
            return timeDifference;
        }
        if (dateTimeMillis <= this.mReferencePeriodEnd) {
            return 0L;
        }
        long timeDifference2 = dateTimeMillis - this.mReferencePeriodEnd;
        return timeDifference2;
    }

    private String buildShortSingleUnitText(long time, Resources res) {
        long timeRoundedToHours = roundUpToUnit(time, TimeUnit.HOURS);
        if (isGreaterOrEqual(this.mMinimumUnit, TimeUnit.DAYS) || days(timeRoundedToHours) > 0) {
            return buildShortDaysText(days(roundUpToUnit(time, TimeUnit.DAYS)), res);
        }
        long timeRoundedToMins = roundUpToUnit(time, TimeUnit.MINUTES);
        if (isGreaterOrEqual(this.mMinimumUnit, TimeUnit.HOURS) || hours(timeRoundedToMins) > 0) {
            return buildShortHoursText(hours(timeRoundedToHours), res);
        }
        return buildShortMinsText(minutes(timeRoundedToMins), res);
    }

    private String buildShortDualUnitText(long time, Resources res) {
        long timeRoundedToHours = roundUpToUnit(time, TimeUnit.HOURS);
        if (isGreaterOrEqual(this.mMinimumUnit, TimeUnit.DAYS) || days(timeRoundedToHours) >= 10) {
            return buildShortDaysText(days(roundUpToUnit(time, TimeUnit.DAYS)), res);
        }
        long timeRoundedToMins = roundUpToUnit(time, TimeUnit.MINUTES);
        if (days(timeRoundedToMins) > 0) {
            int hoursRoundedToHours = hours(timeRoundedToHours);
            if (hoursRoundedToHours > 0) {
                return buildShortDaysHoursText(days(timeRoundedToHours), hoursRoundedToHours, res);
            }
            return buildShortDaysText(days(timeRoundedToHours), res);
        }
        if (isGreaterOrEqual(this.mMinimumUnit, TimeUnit.HOURS)) {
            return buildShortHoursText(hours(timeRoundedToHours), res);
        }
        int hoursRoundedToMins = hours(timeRoundedToMins);
        int minutesRoundedToMins = minutes(timeRoundedToMins);
        if (hoursRoundedToMins > 0) {
            if (minutesRoundedToMins > 0) {
                return buildShortHoursMinsText(hoursRoundedToMins, minutesRoundedToMins, res);
            }
            return buildShortHoursText(hoursRoundedToMins, res);
        }
        return buildShortMinsText(minutes(timeRoundedToMins), res);
    }

    private String shortDualUnlessTooLong(long time, Resources res) {
        String shortDual = buildShortDualUnitText(time, res);
        return shortDual.length() <= 7 ? shortDual : buildShortSingleUnitText(time, res);
    }

    private String buildStopwatchText(long time, Resources res) {
        if (isGreaterOrEqual(this.mMinimumUnit, TimeUnit.DAYS)) {
            return buildShortDaysText(days(roundUpToUnit(time, TimeUnit.DAYS)), res);
        }
        long timeRoundedToMins = roundUpToUnit(time, TimeUnit.MINUTES);
        if (isGreaterOrEqual(this.mMinimumUnit, TimeUnit.HOURS) || days(timeRoundedToMins) > 0) {
            return buildShortDualUnitText(time, res);
        }
        long timeRoundedToSecs = roundUpToUnit(time, TimeUnit.SECONDS);
        if (isGreaterOrEqual(this.mMinimumUnit, TimeUnit.MINUTES) || hours(timeRoundedToSecs) > 0) {
            return String.format(Locale.US, "%d:%02d", Integer.valueOf(hours(timeRoundedToMins)), Integer.valueOf(minutes(timeRoundedToMins)));
        }
        return String.format(Locale.US, "%02d:%02d", Integer.valueOf(minutes(timeRoundedToSecs)), Integer.valueOf(seconds(timeRoundedToSecs)));
    }

    private String buildWordsSingleUnitText(long time, Resources res) {
        long timeRoundedToHours = roundUpToUnit(time, TimeUnit.HOURS);
        if (isGreaterOrEqual(this.mMinimumUnit, TimeUnit.DAYS) || days(timeRoundedToHours) > 0) {
            int daysRoundedToDays = days(roundUpToUnit(time, TimeUnit.DAYS));
            return res.getQuantityString(R.plurals.time_difference_words_days, daysRoundedToDays, Integer.valueOf(daysRoundedToDays));
        }
        long timeRoundedToMins = roundUpToUnit(time, TimeUnit.MINUTES);
        if (isGreaterOrEqual(this.mMinimumUnit, TimeUnit.HOURS) || hours(timeRoundedToMins) > 0) {
            int hoursRoundedToHours = hours(timeRoundedToHours);
            return res.getQuantityString(R.plurals.time_difference_words_hours, hoursRoundedToHours, Integer.valueOf(hoursRoundedToHours));
        }
        int mins = minutes(timeRoundedToMins);
        return res.getQuantityString(R.plurals.time_difference_words_minutes, mins, Integer.valueOf(mins));
    }

    private String wordsSingleUnlessTooLong(long time, Resources res) {
        String wordsSingle = buildWordsSingleUnitText(time, res);
        return wordsSingle.length() <= 7 ? wordsSingle : buildShortSingleUnitText(time, res);
    }

    private static String buildShortDaysText(int days, Resources res) {
        return res.getQuantityString(R.plurals.time_difference_short_days, days, Integer.valueOf(days));
    }

    private static String buildShortHoursText(int hours, Resources res) {
        return res.getQuantityString(R.plurals.time_difference_short_hours, hours, Integer.valueOf(hours));
    }

    private static String buildShortMinsText(int mins, Resources res) {
        return res.getQuantityString(R.plurals.time_difference_short_minutes, mins, Integer.valueOf(mins));
    }

    private static String buildShortDaysHoursText(int days, int hours, Resources res) {
        return res.getString(R.string.time_difference_short_days_and_hours, buildShortDaysText(days, res), buildShortHoursText(hours, res));
    }

    private static String buildShortHoursMinsText(int hours, int mins, Resources res) {
        return res.getString(R.string.time_difference_short_hours_and_minutes, buildShortHoursText(hours, res), buildShortMinsText(mins, res));
    }

    private static long roundUpToUnit(long durationMillis, TimeUnit unit) {
        long unitInMillis = unit.toMillis(1L);
        return divRoundingUp(durationMillis, unitInMillis) * unitInMillis;
    }

    private static long divRoundingUp(long num, long divisor) {
        return ((long) (num % divisor == 0 ? 0 : 1)) + (num / divisor);
    }

    private static int modToUnit(long durationMillis, TimeUnit unit) {
        return (int) ((durationMillis / unit.toMillis(1L)) % ((long) getUnitMaximum(unit)));
    }

    private static int days(long durationMillis) {
        return modToUnit(durationMillis, TimeUnit.DAYS);
    }

    private static int hours(long durationMillis) {
        return modToUnit(durationMillis, TimeUnit.HOURS);
    }

    private static int minutes(long durationMillis) {
        return modToUnit(durationMillis, TimeUnit.MINUTES);
    }

    private static int seconds(long durationMillis) {
        return modToUnit(durationMillis, TimeUnit.SECONDS);
    }

    private static boolean isGreaterOrEqual(@Nullable TimeUnit unit1, TimeUnit unit2) {
        return unit1 != null && unit1.toMillis(1L) >= unit2.toMillis(1L);
    }

    /* JADX INFO: renamed from: android.support.wearable.complications.TimeDifferenceText$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$java$util$concurrent$TimeUnit = new int[TimeUnit.values().length];

        static {
            try {
                $SwitchMap$java$util$concurrent$TimeUnit[TimeUnit.MILLISECONDS.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$java$util$concurrent$TimeUnit[TimeUnit.SECONDS.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$java$util$concurrent$TimeUnit[TimeUnit.MINUTES.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$java$util$concurrent$TimeUnit[TimeUnit.HOURS.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$java$util$concurrent$TimeUnit[TimeUnit.DAYS.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    private static int getUnitMaximum(TimeUnit unit) {
        switch (AnonymousClass1.$SwitchMap$java$util$concurrent$TimeUnit[unit.ordinal()]) {
            case 1:
                return 1000;
            case 2:
            case 3:
                return 60;
            case 4:
                return 24;
            case 5:
                return Integer.MAX_VALUE;
            default:
                String strValueOf = String.valueOf(unit);
                throw new IllegalArgumentException(new StringBuilder(String.valueOf(strValueOf).length() + 20).append("Unit not supported: ").append(strValueOf).toString());
        }
    }
}
