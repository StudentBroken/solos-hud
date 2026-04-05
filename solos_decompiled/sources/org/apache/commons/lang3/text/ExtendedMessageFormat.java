package org.apache.commons.lang3.text;

import android.support.v4.view.MotionEventCompat;
import java.text.Format;
import java.text.MessageFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;

/* JADX INFO: loaded from: classes51.dex */
public class ExtendedMessageFormat extends MessageFormat {
    private static final String DUMMY_PATTERN = "";
    private static final char END_FE = '}';
    private static final String ESCAPED_QUOTE = "''";
    private static final int HASH_SEED = 31;
    private static final char QUOTE = '\'';
    private static final char START_FE = '{';
    private static final char START_FMT = ',';
    private static final long serialVersionUID = -2362048321261811743L;
    private final Map<String, ? extends FormatFactory> registry;
    private String toPattern;

    public ExtendedMessageFormat(String pattern) {
        this(pattern, Locale.getDefault());
    }

    public ExtendedMessageFormat(String pattern, Locale locale) {
        this(pattern, locale, null);
    }

    public ExtendedMessageFormat(String pattern, Map<String, ? extends FormatFactory> registry) {
        this(pattern, Locale.getDefault(), registry);
    }

    public ExtendedMessageFormat(String pattern, Locale locale, Map<String, ? extends FormatFactory> registry) {
        super("");
        setLocale(locale);
        this.registry = registry;
        applyPattern(pattern);
    }

    @Override // java.text.MessageFormat
    public String toPattern() {
        return this.toPattern;
    }

    @Override // java.text.MessageFormat
    public final void applyPattern(String pattern) {
        if (this.registry == null) {
            super.applyPattern(pattern);
            this.toPattern = super.toPattern();
            return;
        }
        ArrayList<Format> foundFormats = new ArrayList<>();
        ArrayList<String> foundDescriptions = new ArrayList<>();
        StringBuilder stripCustom = new StringBuilder(pattern.length());
        ParsePosition pos = new ParsePosition(0);
        char[] c = pattern.toCharArray();
        int fmtCount = 0;
        while (pos.getIndex() < pattern.length()) {
            switch (c[pos.getIndex()]) {
                case MotionEventCompat.AXIS_GENERIC_8 /* 39 */:
                    appendQuotedString(pattern, pos, stripCustom, true);
                    continue;
                case '{':
                    fmtCount++;
                    seekNonWs(pattern, pos);
                    int start = pos.getIndex();
                    int index = readArgumentIndex(pattern, next(pos));
                    stripCustom.append(START_FE).append(index);
                    seekNonWs(pattern, pos);
                    Format format = null;
                    String formatDescription = null;
                    if (c[pos.getIndex()] == ',' && (format = getFormat((formatDescription = parseFormatDescription(pattern, next(pos))))) == null) {
                        stripCustom.append(START_FMT).append(formatDescription);
                    }
                    foundFormats.add(format);
                    if (format == null) {
                        formatDescription = null;
                    }
                    foundDescriptions.add(formatDescription);
                    Validate.isTrue(foundFormats.size() == fmtCount);
                    Validate.isTrue(foundDescriptions.size() == fmtCount);
                    if (c[pos.getIndex()] != '}') {
                        throw new IllegalArgumentException("Unreadable format element at position " + start);
                    }
                    break;
            }
            stripCustom.append(c[pos.getIndex()]);
            next(pos);
        }
        super.applyPattern(stripCustom.toString());
        this.toPattern = insertFormats(super.toPattern(), foundDescriptions);
        if (containsElements(foundFormats)) {
            Format[] origFormats = getFormats();
            int i = 0;
            for (Format f : foundFormats) {
                if (f != null) {
                    origFormats[i] = f;
                }
                i++;
            }
            super.setFormats(origFormats);
        }
    }

    @Override // java.text.MessageFormat
    public void setFormat(int formatElementIndex, Format newFormat) {
        throw new UnsupportedOperationException();
    }

    @Override // java.text.MessageFormat
    public void setFormatByArgumentIndex(int argumentIndex, Format newFormat) {
        throw new UnsupportedOperationException();
    }

    @Override // java.text.MessageFormat
    public void setFormats(Format[] newFormats) {
        throw new UnsupportedOperationException();
    }

    @Override // java.text.MessageFormat
    public void setFormatsByArgumentIndex(Format[] newFormats) {
        throw new UnsupportedOperationException();
    }

    @Override // java.text.MessageFormat
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj != null && super.equals(obj) && !ObjectUtils.notEqual(getClass(), obj.getClass())) {
            ExtendedMessageFormat rhs = (ExtendedMessageFormat) obj;
            return (ObjectUtils.notEqual(this.toPattern, rhs.toPattern) || ObjectUtils.notEqual(this.registry, rhs.registry)) ? false : true;
        }
        return false;
    }

    @Override // java.text.MessageFormat
    public int hashCode() {
        int result = super.hashCode();
        return (((result * HASH_SEED) + ObjectUtils.hashCode(this.registry)) * HASH_SEED) + ObjectUtils.hashCode(this.toPattern);
    }

    private Format getFormat(String desc) {
        if (this.registry != null) {
            String name = desc;
            String args = null;
            int i = desc.indexOf(44);
            if (i > 0) {
                name = desc.substring(0, i).trim();
                args = desc.substring(i + 1).trim();
            }
            FormatFactory factory = this.registry.get(name);
            if (factory != null) {
                return factory.getFormat(name, args, getLocale());
            }
        }
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x003f A[PHI: r0
      0x003f: PHI (r0v1 'c' char) = (r0v0 'c' char), (r0v2 'c' char), (r0v2 'c' char) binds: [B:7:0x0029, B:9:0x0036, B:10:0x0038] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int readArgumentIndex(java.lang.String r9, java.text.ParsePosition r10) {
        /*
            r8 = this;
            r7 = 125(0x7d, float:1.75E-43)
            r6 = 44
            int r3 = r10.getIndex()
            r8.seekNonWs(r9, r10)
            java.lang.StringBuffer r2 = new java.lang.StringBuffer
            r2.<init>()
            r1 = 0
        L11:
            if (r1 != 0) goto L60
            int r4 = r10.getIndex()
            int r5 = r9.length()
            if (r4 >= r5) goto L60
            int r4 = r10.getIndex()
            char r0 = r9.charAt(r4)
            boolean r4 = java.lang.Character.isWhitespace(r0)
            if (r4 == 0) goto L3f
            r8.seekNonWs(r9, r10)
            int r4 = r10.getIndex()
            char r0 = r9.charAt(r4)
            if (r0 == r6) goto L3f
            if (r0 == r7) goto L3f
            r1 = 1
        L3b:
            r8.next(r10)
            goto L11
        L3f:
            if (r0 == r6) goto L43
            if (r0 != r7) goto L53
        L43:
            int r4 = r2.length()
            if (r4 <= 0) goto L53
            java.lang.String r4 = r2.toString()     // Catch: java.lang.NumberFormatException -> L52
            int r4 = java.lang.Integer.parseInt(r4)     // Catch: java.lang.NumberFormatException -> L52
            return r4
        L52:
            r4 = move-exception
        L53:
            boolean r4 = java.lang.Character.isDigit(r0)
            if (r4 != 0) goto L5e
            r1 = 1
        L5a:
            r2.append(r0)
            goto L3b
        L5e:
            r1 = 0
            goto L5a
        L60:
            if (r1 == 0) goto L8d
            java.lang.IllegalArgumentException r4 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "Invalid format argument index at position "
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.StringBuilder r5 = r5.append(r3)
            java.lang.String r6 = ": "
            java.lang.StringBuilder r5 = r5.append(r6)
            int r6 = r10.getIndex()
            java.lang.String r6 = r9.substring(r3, r6)
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.String r5 = r5.toString()
            r4.<init>(r5)
            throw r4
        L8d:
            java.lang.IllegalArgumentException r4 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "Unterminated format element at position "
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.StringBuilder r5 = r5.append(r3)
            java.lang.String r5 = r5.toString()
            r4.<init>(r5)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.text.ExtendedMessageFormat.readArgumentIndex(java.lang.String, java.text.ParsePosition):int");
    }

    private String parseFormatDescription(String pattern, ParsePosition pos) {
        int start = pos.getIndex();
        seekNonWs(pattern, pos);
        int text = pos.getIndex();
        int depth = 1;
        while (pos.getIndex() < pattern.length()) {
            switch (pattern.charAt(pos.getIndex())) {
                case MotionEventCompat.AXIS_GENERIC_8 /* 39 */:
                    getQuotedString(pattern, pos, false);
                    break;
                case '{':
                    depth++;
                    break;
                case '}':
                    depth--;
                    if (depth == 0) {
                        return pattern.substring(text, pos.getIndex());
                    }
                    break;
            }
            next(pos);
        }
        throw new IllegalArgumentException("Unterminated format element at position " + start);
    }

    private String insertFormats(String pattern, ArrayList<String> customPatterns) {
        if (containsElements(customPatterns)) {
            StringBuilder sb = new StringBuilder(pattern.length() * 2);
            ParsePosition pos = new ParsePosition(0);
            int fe = -1;
            int depth = 0;
            while (pos.getIndex() < pattern.length()) {
                char c = pattern.charAt(pos.getIndex());
                switch (c) {
                    case MotionEventCompat.AXIS_GENERIC_8 /* 39 */:
                        appendQuotedString(pattern, pos, sb, false);
                        continue;
                    case '{':
                        depth++;
                        if (depth == 1) {
                            fe++;
                            sb.append(START_FE).append(readArgumentIndex(pattern, next(pos)));
                            String customPattern = customPatterns.get(fe);
                            if (customPattern != null) {
                                sb.append(START_FMT).append(customPattern);
                            }
                        } else {
                            continue;
                        }
                        break;
                    case '}':
                        depth--;
                        break;
                }
                sb.append(c);
                next(pos);
            }
            return sb.toString();
        }
        return pattern;
    }

    private void seekNonWs(String pattern, ParsePosition pos) {
        char[] buffer = pattern.toCharArray();
        do {
            int len = StrMatcher.splitMatcher().isMatch(buffer, pos.getIndex());
            pos.setIndex(pos.getIndex() + len);
            if (len <= 0) {
                return;
            }
        } while (pos.getIndex() < pattern.length());
    }

    private ParsePosition next(ParsePosition pos) {
        pos.setIndex(pos.getIndex() + 1);
        return pos;
    }

    private StringBuilder appendQuotedString(String pattern, ParsePosition pos, StringBuilder appendTo, boolean escapingOn) {
        int start = pos.getIndex();
        char[] c = pattern.toCharArray();
        if (escapingOn && c[start] == '\'') {
            next(pos);
            if (appendTo == null) {
                return null;
            }
            return appendTo.append(QUOTE);
        }
        int lastHold = start;
        for (int i = pos.getIndex(); i < pattern.length(); i++) {
            if (escapingOn && pattern.substring(i).startsWith(ESCAPED_QUOTE)) {
                appendTo.append(c, lastHold, pos.getIndex() - lastHold).append(QUOTE);
                pos.setIndex(ESCAPED_QUOTE.length() + i);
                lastHold = pos.getIndex();
            } else {
                switch (c[pos.getIndex()]) {
                    case MotionEventCompat.AXIS_GENERIC_8 /* 39 */:
                        next(pos);
                        if (appendTo != null) {
                            return appendTo.append(c, lastHold, pos.getIndex() - lastHold);
                        }
                        return null;
                    default:
                        next(pos);
                        break;
                }
            }
        }
        throw new IllegalArgumentException("Unterminated quoted string at position " + start);
    }

    private void getQuotedString(String pattern, ParsePosition pos, boolean escapingOn) {
        appendQuotedString(pattern, pos, null, escapingOn);
    }

    private boolean containsElements(Collection<?> coll) {
        if (coll == null || coll.isEmpty()) {
            return false;
        }
        for (Object name : coll) {
            if (name != null) {
                return true;
            }
        }
        return false;
    }
}
