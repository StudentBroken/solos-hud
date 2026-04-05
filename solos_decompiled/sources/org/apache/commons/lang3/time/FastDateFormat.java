package org.apache.commons.lang3.time;

import android.support.v4.view.MotionEventCompat;
import com.kopin.solos.share.Sync;
import com.kopin.solos.storage.util.TimeHelper;
import com.twitter.sdk.android.core.TwitterApiErrorConstants;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.DateFormatSymbols;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.commons.lang3.Validate;

/* JADX INFO: loaded from: classes51.dex */
public class FastDateFormat extends Format {
    public static final int FULL = 0;
    public static final int LONG = 1;
    public static final int MEDIUM = 2;
    public static final int SHORT = 3;
    private static final long serialVersionUID = 1;
    private final Locale mLocale;
    private transient int mMaxLengthEstimate;
    private final String mPattern;
    private transient Rule[] mRules;
    private final TimeZone mTimeZone;
    private static final FormatCache<FastDateFormat> cache = new FormatCache<FastDateFormat>() { // from class: org.apache.commons.lang3.time.FastDateFormat.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.apache.commons.lang3.time.FormatCache
        public FastDateFormat createInstance(String pattern, TimeZone timeZone, Locale locale) {
            return new FastDateFormat(pattern, timeZone, locale);
        }
    };
    private static ConcurrentMap<TimeZoneDisplayKey, String> cTimeZoneDisplayCache = new ConcurrentHashMap(7);

    private interface NumberRule extends Rule {
        void appendTo(StringBuffer stringBuffer, int i);
    }

    private interface Rule {
        void appendTo(StringBuffer stringBuffer, Calendar calendar);

        int estimateLength();
    }

    public static FastDateFormat getInstance() {
        return (FastDateFormat) cache.getDateTimeInstance(3, 3, null, null);
    }

    public static FastDateFormat getInstance(String pattern) {
        return (FastDateFormat) cache.getInstance(pattern, null, null);
    }

    public static FastDateFormat getInstance(String pattern, TimeZone timeZone) {
        return (FastDateFormat) cache.getInstance(pattern, timeZone, null);
    }

    public static FastDateFormat getInstance(String pattern, Locale locale) {
        return (FastDateFormat) cache.getInstance(pattern, null, locale);
    }

    public static FastDateFormat getInstance(String pattern, TimeZone timeZone, Locale locale) {
        return (FastDateFormat) cache.getInstance(pattern, timeZone, locale);
    }

    public static FastDateFormat getDateInstance(int style) {
        return (FastDateFormat) cache.getDateTimeInstance(Integer.valueOf(style), null, null, null);
    }

    public static FastDateFormat getDateInstance(int style, Locale locale) {
        return (FastDateFormat) cache.getDateTimeInstance(Integer.valueOf(style), null, null, locale);
    }

    public static FastDateFormat getDateInstance(int style, TimeZone timeZone) {
        return (FastDateFormat) cache.getDateTimeInstance(Integer.valueOf(style), null, timeZone, null);
    }

    public static FastDateFormat getDateInstance(int style, TimeZone timeZone, Locale locale) {
        return (FastDateFormat) cache.getDateTimeInstance(Integer.valueOf(style), null, timeZone, locale);
    }

    public static FastDateFormat getTimeInstance(int style) {
        return (FastDateFormat) cache.getDateTimeInstance(null, Integer.valueOf(style), null, null);
    }

    public static FastDateFormat getTimeInstance(int style, Locale locale) {
        return (FastDateFormat) cache.getDateTimeInstance(null, Integer.valueOf(style), null, locale);
    }

    public static FastDateFormat getTimeInstance(int style, TimeZone timeZone) {
        return (FastDateFormat) cache.getDateTimeInstance(null, Integer.valueOf(style), timeZone, null);
    }

    public static FastDateFormat getTimeInstance(int style, TimeZone timeZone, Locale locale) {
        return (FastDateFormat) cache.getDateTimeInstance(null, Integer.valueOf(style), timeZone, locale);
    }

    public static FastDateFormat getDateTimeInstance(int dateStyle, int timeStyle) {
        return (FastDateFormat) cache.getDateTimeInstance(Integer.valueOf(dateStyle), Integer.valueOf(timeStyle), null, null);
    }

    public static FastDateFormat getDateTimeInstance(int dateStyle, int timeStyle, Locale locale) {
        return (FastDateFormat) cache.getDateTimeInstance(Integer.valueOf(dateStyle), Integer.valueOf(timeStyle), null, locale);
    }

    public static FastDateFormat getDateTimeInstance(int dateStyle, int timeStyle, TimeZone timeZone) {
        return getDateTimeInstance(dateStyle, timeStyle, timeZone, null);
    }

    public static FastDateFormat getDateTimeInstance(int dateStyle, int timeStyle, TimeZone timeZone, Locale locale) {
        return (FastDateFormat) cache.getDateTimeInstance(Integer.valueOf(dateStyle), Integer.valueOf(timeStyle), timeZone, locale);
    }

    static String getTimeZoneDisplay(TimeZone tz, boolean daylight, int style, Locale locale) {
        TimeZoneDisplayKey key = new TimeZoneDisplayKey(tz, daylight, style, locale);
        String value = cTimeZoneDisplayCache.get(key);
        if (value == null) {
            String value2 = tz.getDisplayName(daylight, style, locale);
            String prior = cTimeZoneDisplayCache.putIfAbsent(key, value2);
            if (prior != null) {
                return prior;
            }
            return value2;
        }
        return value;
    }

    protected FastDateFormat(String pattern, TimeZone timeZone, Locale locale) {
        this.mPattern = pattern;
        this.mTimeZone = timeZone;
        this.mLocale = locale;
        init();
    }

    private void init() {
        List<Rule> rulesList = parsePattern();
        this.mRules = (Rule[]) rulesList.toArray(new Rule[rulesList.size()]);
        int len = 0;
        int i = this.mRules.length;
        while (true) {
            i--;
            if (i >= 0) {
                len += this.mRules[i].estimateLength();
            } else {
                this.mMaxLengthEstimate = len;
                return;
            }
        }
    }

    protected List<Rule> parsePattern() {
        Rule rule;
        DateFormatSymbols symbols = new DateFormatSymbols(this.mLocale);
        List<Rule> rules = new ArrayList<>();
        String[] ERAs = symbols.getEras();
        String[] months = symbols.getMonths();
        String[] shortMonths = symbols.getShortMonths();
        String[] weekdays = symbols.getWeekdays();
        String[] shortWeekdays = symbols.getShortWeekdays();
        String[] AmPmStrings = symbols.getAmPmStrings();
        int length = this.mPattern.length();
        int[] indexRef = new int[1];
        int i = 0;
        while (i < length) {
            indexRef[0] = i;
            String token = parseToken(this.mPattern, indexRef);
            int i2 = indexRef[0];
            int tokenLen = token.length();
            if (tokenLen != 0) {
                char c = token.charAt(0);
                switch (c) {
                    case MotionEventCompat.AXIS_GENERIC_8 /* 39 */:
                        String sub = token.substring(1);
                        if (sub.length() == 1) {
                            rule = new CharacterLiteral(sub.charAt(0));
                        } else {
                            rule = new StringLiteral(sub);
                        }
                        break;
                    case 'D':
                        rule = selectNumberRule(6, tokenLen);
                        break;
                    case 'E':
                        rule = new TextField(7, tokenLen < 4 ? shortWeekdays : weekdays);
                        break;
                    case 'F':
                        rule = selectNumberRule(8, tokenLen);
                        break;
                    case 'G':
                        rule = new TextField(0, ERAs);
                        break;
                    case 'H':
                        rule = selectNumberRule(11, tokenLen);
                        break;
                    case 'K':
                        rule = selectNumberRule(10, tokenLen);
                        break;
                    case Sync.LOGIN_ACTIVITY_CALLBACK /* 77 */:
                        if (tokenLen >= 4) {
                            rule = new TextField(2, months);
                        } else if (tokenLen == 3) {
                            rule = new TextField(2, shortMonths);
                        } else if (tokenLen == 2) {
                            rule = TwoDigitMonthField.INSTANCE;
                        } else {
                            rule = UnpaddedMonthField.INSTANCE;
                        }
                        break;
                    case 'S':
                        rule = selectNumberRule(14, tokenLen);
                        break;
                    case TwitterApiErrorConstants.CLIENT_NOT_PRIVILEGED /* 87 */:
                        rule = selectNumberRule(4, tokenLen);
                        break;
                    case 'Z':
                        if (tokenLen == 1) {
                            rule = TimeZoneNumberRule.INSTANCE_NO_COLON;
                        } else {
                            rule = TimeZoneNumberRule.INSTANCE_COLON;
                        }
                        break;
                    case 'a':
                        rule = new TextField(9, AmPmStrings);
                        break;
                    case 'd':
                        rule = selectNumberRule(5, tokenLen);
                        break;
                    case 'h':
                        rule = new TwelveHourField(selectNumberRule(10, tokenLen));
                        break;
                    case 'k':
                        rule = new TwentyFourHourField(selectNumberRule(11, tokenLen));
                        break;
                    case 'm':
                        rule = selectNumberRule(12, tokenLen);
                        break;
                    case 's':
                        rule = selectNumberRule(13, tokenLen);
                        break;
                    case 'w':
                        rule = selectNumberRule(3, tokenLen);
                        break;
                    case 'y':
                        if (tokenLen == 2) {
                            rule = TwoDigitYearField.INSTANCE;
                        } else {
                            if (tokenLen < 4) {
                                tokenLen = 4;
                            }
                            rule = selectNumberRule(1, tokenLen);
                        }
                        break;
                    case 'z':
                        if (tokenLen >= 4) {
                            rule = new TimeZoneNameRule(this.mTimeZone, this.mLocale, 1);
                        } else {
                            rule = new TimeZoneNameRule(this.mTimeZone, this.mLocale, 0);
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("Illegal pattern component: " + token);
                }
                rules.add(rule);
                i = i2 + 1;
            } else {
                return rules;
            }
        }
        return rules;
    }

    protected String parseToken(String pattern, int[] indexRef) {
        StringBuilder buf = new StringBuilder();
        int i = indexRef[0];
        int length = pattern.length();
        char c = pattern.charAt(i);
        if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
            buf.append(c);
            while (i + 1 < length) {
                char peek = pattern.charAt(i + 1);
                if (peek != c) {
                    break;
                }
                buf.append(c);
                i++;
            }
        } else {
            buf.append('\'');
            boolean inLiteral = false;
            while (i < length) {
                char c2 = pattern.charAt(i);
                if (c2 == '\'') {
                    if (i + 1 < length && pattern.charAt(i + 1) == '\'') {
                        i++;
                        buf.append(c2);
                    } else {
                        inLiteral = !inLiteral;
                    }
                } else {
                    if (!inLiteral && ((c2 >= 'A' && c2 <= 'Z') || (c2 >= 'a' && c2 <= 'z'))) {
                        i--;
                        break;
                    }
                    buf.append(c2);
                }
                i++;
            }
        }
        indexRef[0] = i;
        return buf.toString();
    }

    protected NumberRule selectNumberRule(int field, int padding) {
        switch (padding) {
            case 1:
                return new UnpaddedNumberField(field);
            case 2:
                return new TwoDigitNumberField(field);
            default:
                return new PaddedNumberField(field, padding);
        }
    }

    @Override // java.text.Format
    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
        if (obj instanceof Date) {
            return format((Date) obj, toAppendTo);
        }
        if (obj instanceof Calendar) {
            return format((Calendar) obj, toAppendTo);
        }
        if (obj instanceof Long) {
            return format(((Long) obj).longValue(), toAppendTo);
        }
        throw new IllegalArgumentException("Unknown class: " + (obj == null ? "<null>" : obj.getClass().getName()));
    }

    public String format(long millis) {
        return format(new Date(millis));
    }

    public String format(Date date) {
        Calendar c = new GregorianCalendar(this.mTimeZone, this.mLocale);
        c.setTime(date);
        return applyRules(c, new StringBuffer(this.mMaxLengthEstimate)).toString();
    }

    public String format(Calendar calendar) {
        return format(calendar, new StringBuffer(this.mMaxLengthEstimate)).toString();
    }

    public StringBuffer format(long millis, StringBuffer buf) {
        return format(new Date(millis), buf);
    }

    public StringBuffer format(Date date, StringBuffer buf) {
        Calendar c = new GregorianCalendar(this.mTimeZone, this.mLocale);
        c.setTime(date);
        return applyRules(c, buf);
    }

    public StringBuffer format(Calendar calendar, StringBuffer buf) {
        return applyRules(calendar, buf);
    }

    protected StringBuffer applyRules(Calendar calendar, StringBuffer buf) {
        Rule[] arr$ = this.mRules;
        for (Rule rule : arr$) {
            rule.appendTo(buf, calendar);
        }
        return buf;
    }

    @Override // java.text.Format
    public Object parseObject(String source, ParsePosition pos) {
        pos.setIndex(0);
        pos.setErrorIndex(0);
        return null;
    }

    public String getPattern() {
        return this.mPattern;
    }

    public TimeZone getTimeZone() {
        return this.mTimeZone;
    }

    public Locale getLocale() {
        return this.mLocale;
    }

    public int getMaxLengthEstimate() {
        return this.mMaxLengthEstimate;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof FastDateFormat)) {
            return false;
        }
        FastDateFormat other = (FastDateFormat) obj;
        return this.mPattern.equals(other.mPattern) && this.mTimeZone.equals(other.mTimeZone) && this.mLocale.equals(other.mLocale);
    }

    public int hashCode() {
        return this.mPattern.hashCode() + ((this.mTimeZone.hashCode() + (this.mLocale.hashCode() * 13)) * 13);
    }

    public String toString() {
        return "FastDateFormat[" + this.mPattern + "]";
    }

    private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
        in.defaultReadObject();
        init();
    }

    private static class CharacterLiteral implements Rule {
        private final char mValue;

        CharacterLiteral(char value) {
            this.mValue = value;
        }

        @Override // org.apache.commons.lang3.time.FastDateFormat.Rule
        public int estimateLength() {
            return 1;
        }

        @Override // org.apache.commons.lang3.time.FastDateFormat.Rule
        public void appendTo(StringBuffer buffer, Calendar calendar) {
            buffer.append(this.mValue);
        }
    }

    private static class StringLiteral implements Rule {
        private final String mValue;

        StringLiteral(String value) {
            this.mValue = value;
        }

        @Override // org.apache.commons.lang3.time.FastDateFormat.Rule
        public int estimateLength() {
            return this.mValue.length();
        }

        @Override // org.apache.commons.lang3.time.FastDateFormat.Rule
        public void appendTo(StringBuffer buffer, Calendar calendar) {
            buffer.append(this.mValue);
        }
    }

    private static class TextField implements Rule {
        private final int mField;
        private final String[] mValues;

        TextField(int field, String[] values) {
            this.mField = field;
            this.mValues = values;
        }

        @Override // org.apache.commons.lang3.time.FastDateFormat.Rule
        public int estimateLength() {
            int max = 0;
            int i = this.mValues.length;
            while (true) {
                i--;
                if (i >= 0) {
                    int len = this.mValues[i].length();
                    if (len > max) {
                        max = len;
                    }
                } else {
                    return max;
                }
            }
        }

        @Override // org.apache.commons.lang3.time.FastDateFormat.Rule
        public void appendTo(StringBuffer buffer, Calendar calendar) {
            buffer.append(this.mValues[calendar.get(this.mField)]);
        }
    }

    private static class UnpaddedNumberField implements NumberRule {
        private final int mField;

        UnpaddedNumberField(int field) {
            this.mField = field;
        }

        @Override // org.apache.commons.lang3.time.FastDateFormat.Rule
        public int estimateLength() {
            return 4;
        }

        @Override // org.apache.commons.lang3.time.FastDateFormat.Rule
        public void appendTo(StringBuffer buffer, Calendar calendar) {
            appendTo(buffer, calendar.get(this.mField));
        }

        @Override // org.apache.commons.lang3.time.FastDateFormat.NumberRule
        public final void appendTo(StringBuffer buffer, int value) {
            if (value < 10) {
                buffer.append((char) (value + 48));
            } else if (value < 100) {
                buffer.append((char) ((value / 10) + 48));
                buffer.append((char) ((value % 10) + 48));
            } else {
                buffer.append(Integer.toString(value));
            }
        }
    }

    private static class UnpaddedMonthField implements NumberRule {
        static final UnpaddedMonthField INSTANCE = new UnpaddedMonthField();

        UnpaddedMonthField() {
        }

        @Override // org.apache.commons.lang3.time.FastDateFormat.Rule
        public int estimateLength() {
            return 2;
        }

        @Override // org.apache.commons.lang3.time.FastDateFormat.Rule
        public void appendTo(StringBuffer buffer, Calendar calendar) {
            appendTo(buffer, calendar.get(2) + 1);
        }

        @Override // org.apache.commons.lang3.time.FastDateFormat.NumberRule
        public final void appendTo(StringBuffer buffer, int value) {
            if (value < 10) {
                buffer.append((char) (value + 48));
            } else {
                buffer.append((char) ((value / 10) + 48));
                buffer.append((char) ((value % 10) + 48));
            }
        }
    }

    private static class PaddedNumberField implements NumberRule {
        private final int mField;
        private final int mSize;

        PaddedNumberField(int field, int size) {
            if (size < 3) {
                throw new IllegalArgumentException();
            }
            this.mField = field;
            this.mSize = size;
        }

        @Override // org.apache.commons.lang3.time.FastDateFormat.Rule
        public int estimateLength() {
            return 4;
        }

        @Override // org.apache.commons.lang3.time.FastDateFormat.Rule
        public void appendTo(StringBuffer buffer, Calendar calendar) {
            appendTo(buffer, calendar.get(this.mField));
        }

        @Override // org.apache.commons.lang3.time.FastDateFormat.NumberRule
        public final void appendTo(StringBuffer buffer, int value) {
            int digits;
            if (value < 100) {
                int i = this.mSize;
                while (true) {
                    i--;
                    if (i >= 2) {
                        buffer.append('0');
                    } else {
                        buffer.append((char) ((value / 10) + 48));
                        buffer.append((char) ((value % 10) + 48));
                        return;
                    }
                }
            } else {
                if (value < 1000) {
                    digits = 3;
                } else {
                    Validate.isTrue(value > -1, "Negative values should not be possible", value);
                    digits = Integer.toString(value).length();
                }
                int i2 = this.mSize;
                while (true) {
                    i2--;
                    if (i2 >= digits) {
                        buffer.append('0');
                    } else {
                        buffer.append(Integer.toString(value));
                        return;
                    }
                }
            }
        }
    }

    private static class TwoDigitNumberField implements NumberRule {
        private final int mField;

        TwoDigitNumberField(int field) {
            this.mField = field;
        }

        @Override // org.apache.commons.lang3.time.FastDateFormat.Rule
        public int estimateLength() {
            return 2;
        }

        @Override // org.apache.commons.lang3.time.FastDateFormat.Rule
        public void appendTo(StringBuffer buffer, Calendar calendar) {
            appendTo(buffer, calendar.get(this.mField));
        }

        @Override // org.apache.commons.lang3.time.FastDateFormat.NumberRule
        public final void appendTo(StringBuffer buffer, int value) {
            if (value < 100) {
                buffer.append((char) ((value / 10) + 48));
                buffer.append((char) ((value % 10) + 48));
            } else {
                buffer.append(Integer.toString(value));
            }
        }
    }

    private static class TwoDigitYearField implements NumberRule {
        static final TwoDigitYearField INSTANCE = new TwoDigitYearField();

        TwoDigitYearField() {
        }

        @Override // org.apache.commons.lang3.time.FastDateFormat.Rule
        public int estimateLength() {
            return 2;
        }

        @Override // org.apache.commons.lang3.time.FastDateFormat.Rule
        public void appendTo(StringBuffer buffer, Calendar calendar) {
            appendTo(buffer, calendar.get(1) % 100);
        }

        @Override // org.apache.commons.lang3.time.FastDateFormat.NumberRule
        public final void appendTo(StringBuffer buffer, int value) {
            buffer.append((char) ((value / 10) + 48));
            buffer.append((char) ((value % 10) + 48));
        }
    }

    private static class TwoDigitMonthField implements NumberRule {
        static final TwoDigitMonthField INSTANCE = new TwoDigitMonthField();

        TwoDigitMonthField() {
        }

        @Override // org.apache.commons.lang3.time.FastDateFormat.Rule
        public int estimateLength() {
            return 2;
        }

        @Override // org.apache.commons.lang3.time.FastDateFormat.Rule
        public void appendTo(StringBuffer buffer, Calendar calendar) {
            appendTo(buffer, calendar.get(2) + 1);
        }

        @Override // org.apache.commons.lang3.time.FastDateFormat.NumberRule
        public final void appendTo(StringBuffer buffer, int value) {
            buffer.append((char) ((value / 10) + 48));
            buffer.append((char) ((value % 10) + 48));
        }
    }

    private static class TwelveHourField implements NumberRule {
        private final NumberRule mRule;

        TwelveHourField(NumberRule rule) {
            this.mRule = rule;
        }

        @Override // org.apache.commons.lang3.time.FastDateFormat.Rule
        public int estimateLength() {
            return this.mRule.estimateLength();
        }

        @Override // org.apache.commons.lang3.time.FastDateFormat.Rule
        public void appendTo(StringBuffer buffer, Calendar calendar) {
            int value = calendar.get(10);
            if (value == 0) {
                value = calendar.getLeastMaximum(10) + 1;
            }
            this.mRule.appendTo(buffer, value);
        }

        @Override // org.apache.commons.lang3.time.FastDateFormat.NumberRule
        public void appendTo(StringBuffer buffer, int value) {
            this.mRule.appendTo(buffer, value);
        }
    }

    private static class TwentyFourHourField implements NumberRule {
        private final NumberRule mRule;

        TwentyFourHourField(NumberRule rule) {
            this.mRule = rule;
        }

        @Override // org.apache.commons.lang3.time.FastDateFormat.Rule
        public int estimateLength() {
            return this.mRule.estimateLength();
        }

        @Override // org.apache.commons.lang3.time.FastDateFormat.Rule
        public void appendTo(StringBuffer buffer, Calendar calendar) {
            int value = calendar.get(11);
            if (value == 0) {
                value = calendar.getMaximum(11) + 1;
            }
            this.mRule.appendTo(buffer, value);
        }

        @Override // org.apache.commons.lang3.time.FastDateFormat.NumberRule
        public void appendTo(StringBuffer buffer, int value) {
            this.mRule.appendTo(buffer, value);
        }
    }

    private static class TimeZoneNameRule implements Rule {
        private final String mDaylight;
        private final String mStandard;
        private final TimeZone mTimeZone;

        TimeZoneNameRule(TimeZone timeZone, Locale locale, int style) {
            this.mTimeZone = timeZone;
            this.mStandard = FastDateFormat.getTimeZoneDisplay(timeZone, false, style, locale);
            this.mDaylight = FastDateFormat.getTimeZoneDisplay(timeZone, true, style, locale);
        }

        @Override // org.apache.commons.lang3.time.FastDateFormat.Rule
        public int estimateLength() {
            return Math.max(this.mStandard.length(), this.mDaylight.length());
        }

        @Override // org.apache.commons.lang3.time.FastDateFormat.Rule
        public void appendTo(StringBuffer buffer, Calendar calendar) {
            if (this.mTimeZone.useDaylightTime() && calendar.get(16) != 0) {
                buffer.append(this.mDaylight);
            } else {
                buffer.append(this.mStandard);
            }
        }
    }

    private static class TimeZoneNumberRule implements Rule {
        static final TimeZoneNumberRule INSTANCE_COLON = new TimeZoneNumberRule(true);
        static final TimeZoneNumberRule INSTANCE_NO_COLON = new TimeZoneNumberRule(false);
        final boolean mColon;

        TimeZoneNumberRule(boolean colon) {
            this.mColon = colon;
        }

        @Override // org.apache.commons.lang3.time.FastDateFormat.Rule
        public int estimateLength() {
            return 5;
        }

        @Override // org.apache.commons.lang3.time.FastDateFormat.Rule
        public void appendTo(StringBuffer buffer, Calendar calendar) {
            int offset = calendar.get(15) + calendar.get(16);
            if (offset < 0) {
                buffer.append('-');
                offset = -offset;
            } else {
                buffer.append('+');
            }
            int hours = offset / TimeHelper.HOUR;
            buffer.append((char) ((hours / 10) + 48));
            buffer.append((char) ((hours % 10) + 48));
            if (this.mColon) {
                buffer.append(':');
            }
            int minutes = (offset / TimeHelper.MINUTE) - (hours * 60);
            buffer.append((char) ((minutes / 10) + 48));
            buffer.append((char) ((minutes % 10) + 48));
        }
    }

    private static class TimeZoneDisplayKey {
        private final Locale mLocale;
        private final int mStyle;
        private final TimeZone mTimeZone;

        TimeZoneDisplayKey(TimeZone timeZone, boolean daylight, int style, Locale locale) {
            this.mTimeZone = timeZone;
            this.mStyle = daylight ? style | Integer.MIN_VALUE : style;
            this.mLocale = locale;
        }

        public int hashCode() {
            return (((this.mStyle * 31) + this.mLocale.hashCode()) * 31) + this.mTimeZone.hashCode();
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof TimeZoneDisplayKey)) {
                return false;
            }
            TimeZoneDisplayKey other = (TimeZoneDisplayKey) obj;
            return this.mTimeZone.equals(other.mTimeZone) && this.mStyle == other.mStyle && this.mLocale.equals(other.mLocale);
        }
    }
}
