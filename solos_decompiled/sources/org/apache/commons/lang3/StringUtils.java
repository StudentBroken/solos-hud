package org.apache.commons.lang3;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes51.dex */
public class StringUtils {
    public static final String EMPTY = "";
    public static final int INDEX_NOT_FOUND = -1;
    private static final int PAD_LIMIT = 8192;
    private static final Pattern WHITESPACE_BLOCK = Pattern.compile("\\s+");

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isNotEmpty(CharSequence cs) {
        return !isEmpty(cs);
    }

    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }

    public static String trim(String str) {
        if (str == null) {
            return null;
        }
        return str.trim();
    }

    public static String trimToNull(String str) {
        String ts = trim(str);
        if (isEmpty(ts)) {
            return null;
        }
        return ts;
    }

    public static String trimToEmpty(String str) {
        return str == null ? "" : str.trim();
    }

    public static String strip(String str) {
        return strip(str, null);
    }

    public static String stripToNull(String str) {
        if (str == null) {
            return null;
        }
        String str2 = strip(str, null);
        if (str2.length() == 0) {
            str2 = null;
        }
        return str2;
    }

    public static String stripToEmpty(String str) {
        return str == null ? "" : strip(str, null);
    }

    public static String strip(String str, String stripChars) {
        return isEmpty(str) ? str : stripEnd(stripStart(str, stripChars), stripChars);
    }

    public static String stripStart(String str, String stripChars) {
        int strLen;
        if (str != null && (strLen = str.length()) != 0) {
            int start = 0;
            if (stripChars == null) {
                while (start != strLen && Character.isWhitespace(str.charAt(start))) {
                    start++;
                }
            } else if (stripChars.length() != 0) {
                while (start != strLen && stripChars.indexOf(str.charAt(start)) != -1) {
                    start++;
                }
            } else {
                return str;
            }
            return str.substring(start);
        }
        return str;
    }

    public static String stripEnd(String str, String stripChars) {
        int end;
        if (str != null && (end = str.length()) != 0) {
            if (stripChars == null) {
                while (end != 0 && Character.isWhitespace(str.charAt(end - 1))) {
                    end--;
                }
            } else if (stripChars.length() != 0) {
                while (end != 0 && stripChars.indexOf(str.charAt(end - 1)) != -1) {
                    end--;
                }
            } else {
                return str;
            }
            return str.substring(0, end);
        }
        return str;
    }

    public static String[] stripAll(String... strs) {
        return stripAll(strs, null);
    }

    public static String[] stripAll(String[] strs, String stripChars) {
        int strsLen;
        if (strs == null || (strsLen = strs.length) == 0) {
            return strs;
        }
        String[] newArr = new String[strsLen];
        for (int i = 0; i < strsLen; i++) {
            newArr[i] = strip(strs[i], stripChars);
        }
        return newArr;
    }

    public static String stripAccents(String input) {
        if (input == null) {
            return null;
        }
        try {
            if (InitStripAccents.java6NormalizeMethod != null) {
                String result = removeAccentsJava6(input);
                return result;
            }
            if (InitStripAccents.sunDecomposeMethod != null) {
                String result2 = removeAccentsSUN(input);
                return result2;
            }
            throw new UnsupportedOperationException("The stripAccents(CharSequence) method requires at least Java6, but got: " + InitStripAccents.java6Exception + "; or a Sun JVM: " + InitStripAccents.sunException);
        } catch (IllegalAccessException iae) {
            throw new RuntimeException("IllegalAccessException occurred", iae);
        } catch (IllegalArgumentException iae2) {
            throw new RuntimeException("IllegalArgumentException occurred", iae2);
        } catch (SecurityException se) {
            throw new RuntimeException("SecurityException occurred", se);
        } catch (InvocationTargetException ite) {
            throw new RuntimeException("InvocationTargetException occurred", ite);
        }
    }

    private static String removeAccentsJava6(CharSequence text) throws IllegalAccessException, InvocationTargetException {
        if (InitStripAccents.java6NormalizeMethod == null || InitStripAccents.java6NormalizerFormNFD == null) {
            throw new IllegalStateException("java.text.Normalizer is not available", InitStripAccents.java6Exception);
        }
        String result = (String) InitStripAccents.java6NormalizeMethod.invoke(null, text, InitStripAccents.java6NormalizerFormNFD);
        return InitStripAccents.java6Pattern.matcher(result).replaceAll("");
    }

    private static String removeAccentsSUN(CharSequence text) throws IllegalAccessException, InvocationTargetException {
        if (InitStripAccents.sunDecomposeMethod == null) {
            throw new IllegalStateException("sun.text.Normalizer is not available", InitStripAccents.sunException);
        }
        String result = (String) InitStripAccents.sunDecomposeMethod.invoke(null, text, Boolean.FALSE, 0);
        return InitStripAccents.sunPattern.matcher(result).replaceAll("");
    }

    private static class InitStripAccents {
        private static final Throwable java6Exception;
        private static final Method java6NormalizeMethod;
        private static final Object java6NormalizerFormNFD;
        private static final Method sunDecomposeMethod;
        private static final Throwable sunException;
        private static final Pattern sunPattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        private static final Pattern java6Pattern = sunPattern;

        private InitStripAccents() {
        }

        static {
            Object _java6NormalizerFormNFD = null;
            Method _java6NormalizeMethod = null;
            Method _sunDecomposeMethod = null;
            Throwable _java6Exception = null;
            Throwable _sunException = null;
            try {
                Class<?> normalizerFormClass = Thread.currentThread().getContextClassLoader().loadClass("java.text.Normalizer$Form");
                _java6NormalizerFormNFD = normalizerFormClass.getField("NFD").get(null);
                Class<?> normalizerClass = Thread.currentThread().getContextClassLoader().loadClass("java.text.Normalizer");
                _java6NormalizeMethod = normalizerClass.getMethod("normalize", CharSequence.class, normalizerFormClass);
            } catch (Exception e1) {
                _java6Exception = e1;
                try {
                    Class<?> normalizerClass2 = Thread.currentThread().getContextClassLoader().loadClass("sun.text.Normalizer");
                    _sunDecomposeMethod = normalizerClass2.getMethod("decompose", String.class, Boolean.TYPE, Integer.TYPE);
                } catch (Exception e2) {
                    _sunException = e2;
                }
            }
            java6Exception = _java6Exception;
            java6NormalizerFormNFD = _java6NormalizerFormNFD;
            java6NormalizeMethod = _java6NormalizeMethod;
            sunException = _sunException;
            sunDecomposeMethod = _sunDecomposeMethod;
        }
    }

    public static boolean equals(CharSequence cs1, CharSequence cs2) {
        return cs1 == null ? cs2 == null : cs1.equals(cs2);
    }

    public static boolean equalsIgnoreCase(CharSequence str1, CharSequence str2) {
        if (str1 == null || str2 == null) {
            return str1 == str2;
        }
        return CharSequenceUtils.regionMatches(str1, true, 0, str2, 0, Math.max(str1.length(), str2.length()));
    }

    public static int indexOf(CharSequence seq, int searchChar) {
        if (isEmpty(seq)) {
            return -1;
        }
        return CharSequenceUtils.indexOf(seq, searchChar, 0);
    }

    public static int indexOf(CharSequence seq, int searchChar, int startPos) {
        if (isEmpty(seq)) {
            return -1;
        }
        return CharSequenceUtils.indexOf(seq, searchChar, startPos);
    }

    public static int indexOf(CharSequence seq, CharSequence searchSeq) {
        if (seq == null || searchSeq == null) {
            return -1;
        }
        return CharSequenceUtils.indexOf(seq, searchSeq, 0);
    }

    public static int indexOf(CharSequence seq, CharSequence searchSeq, int startPos) {
        if (seq == null || searchSeq == null) {
            return -1;
        }
        return CharSequenceUtils.indexOf(seq, searchSeq, startPos);
    }

    public static int ordinalIndexOf(CharSequence str, CharSequence searchStr, int ordinal) {
        return ordinalIndexOf(str, searchStr, ordinal, false);
    }

    private static int ordinalIndexOf(CharSequence str, CharSequence searchStr, int ordinal, boolean lastIndex) {
        if (str == null || searchStr == null || ordinal <= 0) {
            return -1;
        }
        if (searchStr.length() == 0) {
            return lastIndex ? str.length() : 0;
        }
        int found = 0;
        int index = lastIndex ? str.length() : -1;
        do {
            if (lastIndex) {
                index = CharSequenceUtils.lastIndexOf(str, searchStr, index - 1);
            } else {
                index = CharSequenceUtils.indexOf(str, searchStr, index + 1);
            }
            if (index >= 0) {
                found++;
            } else {
                return index;
            }
        } while (found < ordinal);
        return index;
    }

    public static int indexOfIgnoreCase(CharSequence str, CharSequence searchStr) {
        return indexOfIgnoreCase(str, searchStr, 0);
    }

    public static int indexOfIgnoreCase(CharSequence str, CharSequence searchStr, int startPos) {
        if (str == null || searchStr == null) {
            return -1;
        }
        if (startPos < 0) {
            startPos = 0;
        }
        int endLimit = (str.length() - searchStr.length()) + 1;
        if (startPos > endLimit) {
            return -1;
        }
        if (searchStr.length() == 0) {
            return startPos;
        }
        for (int i = startPos; i < endLimit; i++) {
            if (CharSequenceUtils.regionMatches(str, true, i, searchStr, 0, searchStr.length())) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(CharSequence seq, int searchChar) {
        if (isEmpty(seq)) {
            return -1;
        }
        return CharSequenceUtils.lastIndexOf(seq, searchChar, seq.length());
    }

    public static int lastIndexOf(CharSequence seq, int searchChar, int startPos) {
        if (isEmpty(seq)) {
            return -1;
        }
        return CharSequenceUtils.lastIndexOf(seq, searchChar, startPos);
    }

    public static int lastIndexOf(CharSequence seq, CharSequence searchSeq) {
        if (seq == null || searchSeq == null) {
            return -1;
        }
        return CharSequenceUtils.lastIndexOf(seq, searchSeq, seq.length());
    }

    public static int lastOrdinalIndexOf(CharSequence str, CharSequence searchStr, int ordinal) {
        return ordinalIndexOf(str, searchStr, ordinal, true);
    }

    public static int lastIndexOf(CharSequence seq, CharSequence searchSeq, int startPos) {
        if (seq == null || searchSeq == null) {
            return -1;
        }
        return CharSequenceUtils.lastIndexOf(seq, searchSeq, startPos);
    }

    public static int lastIndexOfIgnoreCase(CharSequence str, CharSequence searchStr) {
        if (str == null || searchStr == null) {
            return -1;
        }
        return lastIndexOfIgnoreCase(str, searchStr, str.length());
    }

    public static int lastIndexOfIgnoreCase(CharSequence str, CharSequence searchStr, int startPos) {
        if (str == null || searchStr == null) {
            return -1;
        }
        if (startPos > str.length() - searchStr.length()) {
            startPos = str.length() - searchStr.length();
        }
        if (startPos < 0) {
            return -1;
        }
        if (searchStr.length() == 0) {
            return startPos;
        }
        for (int i = startPos; i >= 0; i--) {
            if (CharSequenceUtils.regionMatches(str, true, i, searchStr, 0, searchStr.length())) {
                return i;
            }
        }
        return -1;
    }

    public static boolean contains(CharSequence seq, int searchChar) {
        return !isEmpty(seq) && CharSequenceUtils.indexOf(seq, searchChar, 0) >= 0;
    }

    public static boolean contains(CharSequence seq, CharSequence searchSeq) {
        return (seq == null || searchSeq == null || CharSequenceUtils.indexOf(seq, searchSeq, 0) < 0) ? false : true;
    }

    public static boolean containsIgnoreCase(CharSequence str, CharSequence searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        int len = searchStr.length();
        int max = str.length() - len;
        for (int i = 0; i <= max; i++) {
            if (CharSequenceUtils.regionMatches(str, true, i, searchStr, 0, len)) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsWhitespace(CharSequence seq) {
        if (isEmpty(seq)) {
            return false;
        }
        int strLen = seq.length();
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(seq.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static int indexOfAny(CharSequence cs, char... searchChars) {
        if (isEmpty(cs) || ArrayUtils.isEmpty(searchChars)) {
            return -1;
        }
        int csLen = cs.length();
        int csLast = csLen - 1;
        int searchLen = searchChars.length;
        int searchLast = searchLen - 1;
        int i = 0;
        while (i < csLen) {
            char ch = cs.charAt(i);
            for (int j = 0; j < searchLen; j++) {
                if (searchChars[j] == ch && (i >= csLast || j >= searchLast || !Character.isHighSurrogate(ch) || searchChars[j + 1] == cs.charAt(i + 1))) {
                    return i;
                }
            }
            i++;
        }
        return -1;
    }

    public static int indexOfAny(CharSequence cs, String searchChars) {
        if (isEmpty(cs) || isEmpty(searchChars)) {
            return -1;
        }
        return indexOfAny(cs, searchChars.toCharArray());
    }

    public static boolean containsAny(CharSequence cs, char... searchChars) {
        if (isEmpty(cs) || ArrayUtils.isEmpty(searchChars)) {
            return false;
        }
        int csLength = cs.length();
        int searchLength = searchChars.length;
        int csLast = csLength - 1;
        int searchLast = searchLength - 1;
        for (int i = 0; i < csLength; i++) {
            char ch = cs.charAt(i);
            for (int j = 0; j < searchLength; j++) {
                if (searchChars[j] == ch) {
                    if (!Character.isHighSurrogate(ch) || j == searchLast) {
                        return true;
                    }
                    if (i < csLast && searchChars[j + 1] == cs.charAt(i + 1)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean containsAny(CharSequence cs, CharSequence searchChars) {
        if (searchChars == null) {
            return false;
        }
        return containsAny(cs, CharSequenceUtils.toCharArray(searchChars));
    }

    /* JADX WARN: Code restructure failed: missing block: B:20:0x003c, code lost:
    
        r3 = r3 + 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static int indexOfAnyBut(java.lang.CharSequence r10, char... r11) {
        /*
            r7 = -1
            boolean r8 = isEmpty(r10)
            if (r8 != 0) goto Ld
            boolean r8 = org.apache.commons.lang3.ArrayUtils.isEmpty(r11)
            if (r8 == 0) goto Lf
        Ld:
            r3 = r7
        Le:
            return r3
        Lf:
            int r2 = r10.length()
            int r1 = r2 + (-1)
            int r6 = r11.length
            int r5 = r6 + (-1)
            r3 = 0
        L19:
            if (r3 >= r2) goto L42
            char r0 = r10.charAt(r3)
            r4 = 0
        L20:
            if (r4 >= r6) goto Le
            char r8 = r11[r4]
            if (r8 != r0) goto L3f
            if (r3 >= r1) goto L3c
            if (r4 >= r5) goto L3c
            boolean r8 = java.lang.Character.isHighSurrogate(r0)
            if (r8 == 0) goto L3c
            int r8 = r4 + 1
            char r8 = r11[r8]
            int r9 = r3 + 1
            char r9 = r10.charAt(r9)
            if (r8 != r9) goto L3f
        L3c:
            int r3 = r3 + 1
            goto L19
        L3f:
            int r4 = r4 + 1
            goto L20
        L42:
            r3 = r7
            goto Le
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.StringUtils.indexOfAnyBut(java.lang.CharSequence, char[]):int");
    }

    public static int indexOfAnyBut(CharSequence seq, CharSequence searchChars) {
        if (isEmpty(seq) || isEmpty(searchChars)) {
            return -1;
        }
        int strLen = seq.length();
        for (int i = 0; i < strLen; i++) {
            char ch = seq.charAt(i);
            boolean chFound = CharSequenceUtils.indexOf(searchChars, ch, 0) >= 0;
            if (i + 1 < strLen && Character.isHighSurrogate(ch)) {
                char ch2 = seq.charAt(i + 1);
                if (chFound && CharSequenceUtils.indexOf(searchChars, ch2, 0) < 0) {
                    return i;
                }
            } else if (!chFound) {
                return i;
            }
        }
        return -1;
    }

    public static boolean containsOnly(CharSequence cs, char... valid) {
        if (valid == null || cs == null) {
            return false;
        }
        if (cs.length() != 0) {
            return valid.length != 0 && indexOfAnyBut(cs, valid) == -1;
        }
        return true;
    }

    public static boolean containsOnly(CharSequence cs, String validChars) {
        if (cs == null || validChars == null) {
            return false;
        }
        return containsOnly(cs, validChars.toCharArray());
    }

    public static boolean containsNone(CharSequence cs, char... searchChars) {
        if (cs == null || searchChars == null) {
            return true;
        }
        int csLen = cs.length();
        int csLast = csLen - 1;
        int searchLen = searchChars.length;
        int searchLast = searchLen - 1;
        for (int i = 0; i < csLen; i++) {
            char ch = cs.charAt(i);
            for (int j = 0; j < searchLen; j++) {
                if (searchChars[j] == ch) {
                    if (!Character.isHighSurrogate(ch) || j == searchLast) {
                        return false;
                    }
                    if (i < csLast && searchChars[j + 1] == cs.charAt(i + 1)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static boolean containsNone(CharSequence cs, String invalidChars) {
        if (cs == null || invalidChars == null) {
            return true;
        }
        return containsNone(cs, invalidChars.toCharArray());
    }

    public static int indexOfAny(CharSequence str, CharSequence... searchStrs) {
        int tmp;
        if (str == null || searchStrs == null) {
            return -1;
        }
        int ret = Integer.MAX_VALUE;
        for (CharSequence search : searchStrs) {
            if (search != null && (tmp = CharSequenceUtils.indexOf(str, search, 0)) != -1 && tmp < ret) {
                ret = tmp;
            }
        }
        if (ret == Integer.MAX_VALUE) {
            return -1;
        }
        return ret;
    }

    public static int lastIndexOfAny(CharSequence str, CharSequence... searchStrs) {
        int tmp;
        if (str == null || searchStrs == null) {
            return -1;
        }
        int ret = -1;
        for (CharSequence search : searchStrs) {
            if (search != null && (tmp = CharSequenceUtils.lastIndexOf(str, search, str.length())) > ret) {
                ret = tmp;
            }
        }
        return ret;
    }

    public static String substring(String str, int start) {
        if (str == null) {
            return null;
        }
        if (start < 0) {
            start += str.length();
        }
        if (start < 0) {
            start = 0;
        }
        if (start > str.length()) {
            return "";
        }
        return str.substring(start);
    }

    public static String substring(String str, int start, int end) {
        if (str == null) {
            return null;
        }
        if (end < 0) {
            end += str.length();
        }
        if (start < 0) {
            start += str.length();
        }
        if (end > str.length()) {
            end = str.length();
        }
        if (start > end) {
            return "";
        }
        if (start < 0) {
            start = 0;
        }
        if (end < 0) {
            end = 0;
        }
        return str.substring(start, end);
    }

    public static String left(String str, int len) {
        if (str == null) {
            return null;
        }
        if (len < 0) {
            return "";
        }
        return str.length() > len ? str.substring(0, len) : str;
    }

    public static String right(String str, int len) {
        if (str == null) {
            return null;
        }
        if (len < 0) {
            return "";
        }
        return str.length() > len ? str.substring(str.length() - len) : str;
    }

    public static String mid(String str, int pos, int len) {
        if (str == null) {
            return null;
        }
        if (len < 0 || pos > str.length()) {
            return "";
        }
        if (pos < 0) {
            pos = 0;
        }
        if (str.length() <= pos + len) {
            return str.substring(pos);
        }
        return str.substring(pos, pos + len);
    }

    public static String substringBefore(String str, String separator) {
        if (!isEmpty(str) && separator != null) {
            if (separator.length() == 0) {
                return "";
            }
            int pos = str.indexOf(separator);
            return pos != -1 ? str.substring(0, pos) : str;
        }
        return str;
    }

    public static String substringAfter(String str, String separator) {
        int pos;
        if (!isEmpty(str)) {
            if (separator == null || (pos = str.indexOf(separator)) == -1) {
                return "";
            }
            return str.substring(separator.length() + pos);
        }
        return str;
    }

    public static String substringBeforeLast(String str, String separator) {
        int pos;
        return (isEmpty(str) || isEmpty(separator) || (pos = str.lastIndexOf(separator)) == -1) ? str : str.substring(0, pos);
    }

    public static String substringAfterLast(String str, String separator) {
        int pos;
        if (!isEmpty(str)) {
            if (isEmpty(separator) || (pos = str.lastIndexOf(separator)) == -1 || pos == str.length() - separator.length()) {
                return "";
            }
            return str.substring(separator.length() + pos);
        }
        return str;
    }

    public static String substringBetween(String str, String tag) {
        return substringBetween(str, tag, tag);
    }

    public static String substringBetween(String str, String open, String close) {
        int start;
        int end;
        if (str == null || open == null || close == null || (start = str.indexOf(open)) == -1 || (end = str.indexOf(close, open.length() + start)) == -1) {
            return null;
        }
        return str.substring(open.length() + start, end);
    }

    public static String[] substringsBetween(String str, String open, String close) {
        int start;
        int start2;
        int end;
        if (str == null || isEmpty(open) || isEmpty(close)) {
            return null;
        }
        int strLen = str.length();
        if (strLen == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        int closeLen = close.length();
        int openLen = open.length();
        List<String> list = new ArrayList<>();
        int pos = 0;
        while (pos < strLen - closeLen && (start = str.indexOf(open, pos)) >= 0 && (end = str.indexOf(close, (start2 = start + openLen))) >= 0) {
            list.add(str.substring(start2, end));
            pos = end + closeLen;
        }
        if (list.isEmpty()) {
            return null;
        }
        return (String[]) list.toArray(new String[list.size()]);
    }

    public static String[] split(String str) {
        return split(str, null, -1);
    }

    public static String[] split(String str, char separatorChar) {
        return splitWorker(str, separatorChar, false);
    }

    public static String[] split(String str, String separatorChars) {
        return splitWorker(str, separatorChars, -1, false);
    }

    public static String[] split(String str, String separatorChars, int max) {
        return splitWorker(str, separatorChars, max, false);
    }

    public static String[] splitByWholeSeparator(String str, String separator) {
        return splitByWholeSeparatorWorker(str, separator, -1, false);
    }

    public static String[] splitByWholeSeparator(String str, String separator, int max) {
        return splitByWholeSeparatorWorker(str, separator, max, false);
    }

    public static String[] splitByWholeSeparatorPreserveAllTokens(String str, String separator) {
        return splitByWholeSeparatorWorker(str, separator, -1, true);
    }

    public static String[] splitByWholeSeparatorPreserveAllTokens(String str, String separator, int max) {
        return splitByWholeSeparatorWorker(str, separator, max, true);
    }

    private static String[] splitByWholeSeparatorWorker(String str, String separator, int max, boolean preserveAllTokens) {
        if (str == null) {
            return null;
        }
        int len = str.length();
        if (len == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        if (separator == null || "".equals(separator)) {
            return splitWorker(str, null, max, preserveAllTokens);
        }
        int separatorLength = separator.length();
        ArrayList<String> substrings = new ArrayList<>();
        int numberOfSubstrings = 0;
        int beg = 0;
        int end = 0;
        while (end < len) {
            end = str.indexOf(separator, beg);
            if (end > -1) {
                if (end > beg) {
                    numberOfSubstrings++;
                    if (numberOfSubstrings == max) {
                        end = len;
                        substrings.add(str.substring(beg));
                    } else {
                        substrings.add(str.substring(beg, end));
                        beg = end + separatorLength;
                    }
                } else {
                    if (preserveAllTokens) {
                        numberOfSubstrings++;
                        if (numberOfSubstrings == max) {
                            end = len;
                            substrings.add(str.substring(beg));
                        } else {
                            substrings.add("");
                        }
                    }
                    beg = end + separatorLength;
                }
            } else {
                substrings.add(str.substring(beg));
                end = len;
            }
        }
        return (String[]) substrings.toArray(new String[substrings.size()]);
    }

    public static String[] splitPreserveAllTokens(String str) {
        return splitWorker(str, null, -1, true);
    }

    public static String[] splitPreserveAllTokens(String str, char separatorChar) {
        return splitWorker(str, separatorChar, true);
    }

    private static String[] splitWorker(String str, char separatorChar, boolean preserveAllTokens) {
        if (str == null) {
            return null;
        }
        int len = str.length();
        if (len == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        List<String> list = new ArrayList<>();
        int i = 0;
        int start = 0;
        boolean match = false;
        boolean lastMatch = false;
        while (i < len) {
            if (str.charAt(i) == separatorChar) {
                if (match || preserveAllTokens) {
                    list.add(str.substring(start, i));
                    match = false;
                    lastMatch = true;
                }
                i++;
                start = i;
            } else {
                lastMatch = false;
                match = true;
                i++;
            }
        }
        if (match || (preserveAllTokens && lastMatch)) {
            list.add(str.substring(start, i));
        }
        return (String[]) list.toArray(new String[list.size()]);
    }

    public static String[] splitPreserveAllTokens(String str, String separatorChars) {
        return splitWorker(str, separatorChars, -1, true);
    }

    public static String[] splitPreserveAllTokens(String str, String separatorChars, int max) {
        return splitWorker(str, separatorChars, max, true);
    }

    /* JADX WARN: Removed duplicated region for block: B:40:0x007c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static java.lang.String[] splitWorker(java.lang.String r11, java.lang.String r12, int r13, boolean r14) {
        /*
            if (r11 != 0) goto L4
            r9 = 0
        L3:
            return r9
        L4:
            int r2 = r11.length()
            if (r2 != 0) goto Ld
            java.lang.String[] r9 = org.apache.commons.lang3.ArrayUtils.EMPTY_STRING_ARRAY
            goto L3
        Ld:
            java.util.ArrayList r3 = new java.util.ArrayList
            r3.<init>()
            r6 = 1
            r0 = 0
            r8 = 0
            r4 = 0
            r1 = 0
            if (r12 != 0) goto L43
            r7 = r6
        L1a:
            if (r0 >= r2) goto Lbc
            char r9 = r11.charAt(r0)
            boolean r9 = java.lang.Character.isWhitespace(r9)
            if (r9 == 0) goto L3e
            if (r4 != 0) goto L2a
            if (r14 == 0) goto Lc2
        L2a:
            r1 = 1
            int r6 = r7 + 1
            if (r7 != r13) goto L31
            r0 = r2
            r1 = 0
        L31:
            java.lang.String r9 = r11.substring(r8, r0)
            r3.add(r9)
            r4 = 0
        L39:
            int r0 = r0 + 1
            r8 = r0
            r7 = r6
            goto L1a
        L3e:
            r1 = 0
            r4 = 1
            int r0 = r0 + 1
            goto L1a
        L43:
            int r9 = r12.length()
            r10 = 1
            if (r9 != r10) goto Lc0
            r9 = 0
            char r5 = r12.charAt(r9)
            r7 = r6
        L50:
            if (r0 >= r2) goto L75
            char r9 = r11.charAt(r0)
            if (r9 != r5) goto L70
            if (r4 != 0) goto L5c
            if (r14 == 0) goto Lbe
        L5c:
            r1 = 1
            int r6 = r7 + 1
            if (r7 != r13) goto L63
            r0 = r2
            r1 = 0
        L63:
            java.lang.String r9 = r11.substring(r8, r0)
            r3.add(r9)
            r4 = 0
        L6b:
            int r0 = r0 + 1
            r8 = r0
            r7 = r6
            goto L50
        L70:
            r1 = 0
            r4 = 1
            int r0 = r0 + 1
            goto L50
        L75:
            r6 = r7
        L76:
            if (r4 != 0) goto L7c
            if (r14 == 0) goto L83
            if (r1 == 0) goto L83
        L7c:
            java.lang.String r9 = r11.substring(r8, r0)
            r3.add(r9)
        L83:
            int r9 = r3.size()
            java.lang.String[] r9 = new java.lang.String[r9]
            java.lang.Object[] r9 = r3.toArray(r9)
            java.lang.String[] r9 = (java.lang.String[]) r9
            goto L3
        L91:
            if (r0 >= r2) goto Lbc
            char r9 = r11.charAt(r0)
            int r9 = r12.indexOf(r9)
            if (r9 < 0) goto Lb5
            if (r4 != 0) goto La1
            if (r14 == 0) goto Lba
        La1:
            r1 = 1
            int r6 = r7 + 1
            if (r7 != r13) goto La8
            r0 = r2
            r1 = 0
        La8:
            java.lang.String r9 = r11.substring(r8, r0)
            r3.add(r9)
            r4 = 0
        Lb0:
            int r0 = r0 + 1
            r8 = r0
            r7 = r6
            goto L91
        Lb5:
            r1 = 0
            r4 = 1
            int r0 = r0 + 1
            goto L91
        Lba:
            r6 = r7
            goto Lb0
        Lbc:
            r6 = r7
            goto L76
        Lbe:
            r6 = r7
            goto L6b
        Lc0:
            r7 = r6
            goto L91
        Lc2:
            r6 = r7
            goto L39
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.StringUtils.splitWorker(java.lang.String, java.lang.String, int, boolean):java.lang.String[]");
    }

    public static String[] splitByCharacterType(String str) {
        return splitByCharacterType(str, false);
    }

    public static String[] splitByCharacterTypeCamelCase(String str) {
        return splitByCharacterType(str, true);
    }

    private static String[] splitByCharacterType(String str, boolean camelCase) {
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        char[] c = str.toCharArray();
        List<String> list = new ArrayList<>();
        int tokenStart = 0;
        int currentType = Character.getType(c[0]);
        for (int pos = 0 + 1; pos < c.length; pos++) {
            int type = Character.getType(c[pos]);
            if (type != currentType) {
                if (camelCase && type == 2 && currentType == 1) {
                    int newTokenStart = pos - 1;
                    if (newTokenStart != tokenStart) {
                        list.add(new String(c, tokenStart, newTokenStart - tokenStart));
                        tokenStart = newTokenStart;
                    }
                } else {
                    list.add(new String(c, tokenStart, pos - tokenStart));
                    tokenStart = pos;
                }
                currentType = type;
            }
        }
        list.add(new String(c, tokenStart, c.length - tokenStart));
        return (String[]) list.toArray(new String[list.size()]);
    }

    public static <T> String join(T... elements) {
        return join(elements, (String) null);
    }

    public static String join(Object[] array, char separator) {
        if (array == null) {
            return null;
        }
        return join(array, separator, 0, array.length);
    }

    public static String join(Object[] array, char separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        }
        int noOfItems = endIndex - startIndex;
        if (noOfItems <= 0) {
            return "";
        }
        StringBuilder buf = new StringBuilder(noOfItems * 16);
        for (int i = startIndex; i < endIndex; i++) {
            if (i > startIndex) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }

    public static String join(Object[] array, String separator) {
        if (array == null) {
            return null;
        }
        return join(array, separator, 0, array.length);
    }

    public static String join(Object[] array, String separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        }
        if (separator == null) {
            separator = "";
        }
        int noOfItems = endIndex - startIndex;
        if (noOfItems <= 0) {
            return "";
        }
        StringBuilder buf = new StringBuilder(noOfItems * 16);
        for (int i = startIndex; i < endIndex; i++) {
            if (i > startIndex) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }

    public static String join(Iterator<?> iterator, char separator) {
        if (iterator == null) {
            return null;
        }
        if (!iterator.hasNext()) {
            return "";
        }
        Object first = iterator.next();
        if (!iterator.hasNext()) {
            return ObjectUtils.toString(first);
        }
        StringBuilder buf = new StringBuilder(256);
        if (first != null) {
            buf.append(first);
        }
        while (iterator.hasNext()) {
            buf.append(separator);
            Object obj = iterator.next();
            if (obj != null) {
                buf.append(obj);
            }
        }
        return buf.toString();
    }

    public static String join(Iterator<?> iterator, String separator) {
        if (iterator == null) {
            return null;
        }
        if (!iterator.hasNext()) {
            return "";
        }
        Object first = iterator.next();
        if (!iterator.hasNext()) {
            return ObjectUtils.toString(first);
        }
        StringBuilder buf = new StringBuilder(256);
        if (first != null) {
            buf.append(first);
        }
        while (iterator.hasNext()) {
            if (separator != null) {
                buf.append(separator);
            }
            Object obj = iterator.next();
            if (obj != null) {
                buf.append(obj);
            }
        }
        return buf.toString();
    }

    public static String join(Iterable<?> iterable, char separator) {
        if (iterable == null) {
            return null;
        }
        return join(iterable.iterator(), separator);
    }

    public static String join(Iterable<?> iterable, String separator) {
        if (iterable == null) {
            return null;
        }
        return join(iterable.iterator(), separator);
    }

    public static String deleteWhitespace(String str) {
        int count;
        if (!isEmpty(str)) {
            int sz = str.length();
            char[] chs = new char[sz];
            int i = 0;
            int count2 = 0;
            while (i < sz) {
                if (Character.isWhitespace(str.charAt(i))) {
                    count = count2;
                } else {
                    count = count2 + 1;
                    chs[count2] = str.charAt(i);
                }
                i++;
                count2 = count;
            }
            return count2 != sz ? new String(chs, 0, count2) : str;
        }
        return str;
    }

    public static String removeStart(String str, String remove) {
        if (!isEmpty(str) && !isEmpty(remove) && str.startsWith(remove)) {
            return str.substring(remove.length());
        }
        return str;
    }

    public static String removeStartIgnoreCase(String str, String remove) {
        if (!isEmpty(str) && !isEmpty(remove) && startsWithIgnoreCase(str, remove)) {
            return str.substring(remove.length());
        }
        return str;
    }

    public static String removeEnd(String str, String remove) {
        if (!isEmpty(str) && !isEmpty(remove) && str.endsWith(remove)) {
            return str.substring(0, str.length() - remove.length());
        }
        return str;
    }

    public static String removeEndIgnoreCase(String str, String remove) {
        if (!isEmpty(str) && !isEmpty(remove) && endsWithIgnoreCase(str, remove)) {
            return str.substring(0, str.length() - remove.length());
        }
        return str;
    }

    public static String remove(String str, String remove) {
        return (isEmpty(str) || isEmpty(remove)) ? str : replace(str, remove, "", -1);
    }

    public static String remove(String str, char remove) {
        if (!isEmpty(str) && str.indexOf(remove) != -1) {
            char[] chars = str.toCharArray();
            int pos = 0;
            for (int i = 0; i < chars.length; i++) {
                if (chars[i] != remove) {
                    chars[pos] = chars[i];
                    pos++;
                }
            }
            return new String(chars, 0, pos);
        }
        return str;
    }

    public static String replaceOnce(String text, String searchString, String replacement) {
        return replace(text, searchString, replacement, 1);
    }

    public static String replace(String text, String searchString, String replacement) {
        return replace(text, searchString, replacement, -1);
    }

    public static String replace(String text, String searchString, String replacement, int max) {
        int i = 64;
        if (!isEmpty(text) && !isEmpty(searchString) && replacement != null && max != 0) {
            int start = 0;
            int end = text.indexOf(searchString, 0);
            if (end != -1) {
                int replLength = searchString.length();
                int increase = replacement.length() - replLength;
                if (increase < 0) {
                    increase = 0;
                }
                if (max < 0) {
                    i = 16;
                } else if (max <= 64) {
                    i = max;
                }
                StringBuilder buf = new StringBuilder(text.length() + (increase * i));
                while (end != -1) {
                    buf.append(text.substring(start, end)).append(replacement);
                    start = end + replLength;
                    max--;
                    if (max == 0) {
                        break;
                    }
                    end = text.indexOf(searchString, start);
                }
                buf.append(text.substring(start));
                return buf.toString();
            }
            return text;
        }
        return text;
    }

    public static String replaceEach(String text, String[] searchList, String[] replacementList) {
        return replaceEach(text, searchList, replacementList, false, 0);
    }

    public static String replaceEachRepeatedly(String text, String[] searchList, String[] replacementList) {
        int timeToLive = searchList == null ? 0 : searchList.length;
        return replaceEach(text, searchList, replacementList, true, timeToLive);
    }

    private static String replaceEach(String text, String[] searchList, String[] replacementList, boolean repeat, int timeToLive) {
        int greater;
        if (text != null && text.length() != 0 && searchList != null && searchList.length != 0 && replacementList != null && replacementList.length != 0) {
            if (timeToLive < 0) {
                throw new IllegalStateException("Aborting to protect against StackOverflowError - output of one loop is the input of another");
            }
            int searchLength = searchList.length;
            int replacementLength = replacementList.length;
            if (searchLength != replacementLength) {
                throw new IllegalArgumentException("Search and Replace array lengths don't match: " + searchLength + " vs " + replacementLength);
            }
            boolean[] noMoreMatchesForReplIndex = new boolean[searchLength];
            int textIndex = -1;
            int replaceIndex = -1;
            for (int i = 0; i < searchLength; i++) {
                if (!noMoreMatchesForReplIndex[i] && searchList[i] != null && searchList[i].length() != 0 && replacementList[i] != null) {
                    int tempIndex = text.indexOf(searchList[i]);
                    if (tempIndex == -1) {
                        noMoreMatchesForReplIndex[i] = true;
                    } else if (textIndex == -1 || tempIndex < textIndex) {
                        textIndex = tempIndex;
                        replaceIndex = i;
                    }
                }
            }
            if (textIndex != -1) {
                int start = 0;
                int increase = 0;
                for (int i2 = 0; i2 < searchList.length; i2++) {
                    if (searchList[i2] != null && replacementList[i2] != null && (greater = replacementList[i2].length() - searchList[i2].length()) > 0) {
                        increase += greater * 3;
                    }
                }
                StringBuilder buf = new StringBuilder(text.length() + Math.min(increase, text.length() / 5));
                while (textIndex != -1) {
                    for (int i3 = start; i3 < textIndex; i3++) {
                        buf.append(text.charAt(i3));
                    }
                    buf.append(replacementList[replaceIndex]);
                    start = textIndex + searchList[replaceIndex].length();
                    textIndex = -1;
                    replaceIndex = -1;
                    for (int i4 = 0; i4 < searchLength; i4++) {
                        if (!noMoreMatchesForReplIndex[i4] && searchList[i4] != null && searchList[i4].length() != 0 && replacementList[i4] != null) {
                            int tempIndex2 = text.indexOf(searchList[i4], start);
                            if (tempIndex2 == -1) {
                                noMoreMatchesForReplIndex[i4] = true;
                            } else if (textIndex == -1 || tempIndex2 < textIndex) {
                                textIndex = tempIndex2;
                                replaceIndex = i4;
                            }
                        }
                    }
                }
                int textLength = text.length();
                for (int i5 = start; i5 < textLength; i5++) {
                    buf.append(text.charAt(i5));
                }
                String result = buf.toString();
                return !repeat ? result : replaceEach(result, searchList, replacementList, repeat, timeToLive - 1);
            }
            return text;
        }
        return text;
    }

    public static String replaceChars(String str, char searchChar, char replaceChar) {
        if (str == null) {
            return null;
        }
        return str.replace(searchChar, replaceChar);
    }

    public static String replaceChars(String str, String searchChars, String replaceChars) {
        if (!isEmpty(str) && !isEmpty(searchChars)) {
            if (replaceChars == null) {
                replaceChars = "";
            }
            boolean modified = false;
            int replaceCharsLength = replaceChars.length();
            int strLength = str.length();
            StringBuilder buf = new StringBuilder(strLength);
            for (int i = 0; i < strLength; i++) {
                char ch = str.charAt(i);
                int index = searchChars.indexOf(ch);
                if (index >= 0) {
                    modified = true;
                    if (index < replaceCharsLength) {
                        buf.append(replaceChars.charAt(index));
                    }
                } else {
                    buf.append(ch);
                }
            }
            if (modified) {
                return buf.toString();
            }
            return str;
        }
        return str;
    }

    public static String overlay(String str, String overlay, int start, int end) {
        if (str == null) {
            return null;
        }
        if (overlay == null) {
            overlay = "";
        }
        int len = str.length();
        if (start < 0) {
            start = 0;
        }
        if (start > len) {
            start = len;
        }
        if (end < 0) {
            end = 0;
        }
        if (end > len) {
            end = len;
        }
        if (start > end) {
            int temp = start;
            start = end;
            end = temp;
        }
        return new StringBuilder(((len + start) - end) + overlay.length() + 1).append(str.substring(0, start)).append(overlay).append(str.substring(end)).toString();
    }

    public static String chomp(String str) {
        if (!isEmpty(str)) {
            if (str.length() == 1) {
                char ch = str.charAt(0);
                if (ch == '\r' || ch == '\n') {
                    return "";
                }
                return str;
            }
            int lastIdx = str.length() - 1;
            char last = str.charAt(lastIdx);
            if (last == '\n') {
                if (str.charAt(lastIdx - 1) == '\r') {
                    lastIdx--;
                }
            } else if (last != '\r') {
                lastIdx++;
            }
            return str.substring(0, lastIdx);
        }
        return str;
    }

    @Deprecated
    public static String chomp(String str, String separator) {
        return removeEnd(str, separator);
    }

    public static String chop(String str) {
        if (str == null) {
            return null;
        }
        int strLen = str.length();
        if (strLen < 2) {
            return "";
        }
        int lastIdx = strLen - 1;
        String ret = str.substring(0, lastIdx);
        char last = str.charAt(lastIdx);
        if (last == '\n' && ret.charAt(lastIdx - 1) == '\r') {
            return ret.substring(0, lastIdx - 1);
        }
        return ret;
    }

    public static String repeat(String str, int repeat) {
        if (str == null) {
            return null;
        }
        if (repeat <= 0) {
            return "";
        }
        int inputLength = str.length();
        if (repeat != 1 && inputLength != 0) {
            if (inputLength == 1 && repeat <= 8192) {
                return repeat(str.charAt(0), repeat);
            }
            int outputLength = inputLength * repeat;
            switch (inputLength) {
                case 1:
                    break;
                case 2:
                    char ch0 = str.charAt(0);
                    char ch1 = str.charAt(1);
                    char[] output2 = new char[outputLength];
                    for (int i = (repeat * 2) - 2; i >= 0; i = (i - 1) - 1) {
                        output2[i] = ch0;
                        output2[i + 1] = ch1;
                    }
                    break;
                default:
                    StringBuilder buf = new StringBuilder(outputLength);
                    for (int i2 = 0; i2 < repeat; i2++) {
                        buf.append(str);
                    }
                    break;
            }
            return str;
        }
        return str;
    }

    public static String repeat(String str, String separator, int repeat) {
        if (str == null || separator == null) {
            return repeat(str, repeat);
        }
        String result = repeat(str + separator, repeat);
        return removeEnd(result, separator);
    }

    public static String repeat(char ch, int repeat) {
        char[] buf = new char[repeat];
        for (int i = repeat - 1; i >= 0; i--) {
            buf[i] = ch;
        }
        return new String(buf);
    }

    public static String rightPad(String str, int size) {
        return rightPad(str, size, ' ');
    }

    public static String rightPad(String str, int size, char padChar) {
        if (str == null) {
            return null;
        }
        int pads = size - str.length();
        if (pads > 0) {
            if (pads > 8192) {
                return rightPad(str, size, String.valueOf(padChar));
            }
            return str.concat(repeat(padChar, pads));
        }
        return str;
    }

    public static String rightPad(String str, int size, String padStr) {
        if (str == null) {
            return null;
        }
        if (isEmpty(padStr)) {
            padStr = " ";
        }
        int padLen = padStr.length();
        int strLen = str.length();
        int pads = size - strLen;
        if (pads > 0) {
            if (padLen == 1 && pads <= 8192) {
                return rightPad(str, size, padStr.charAt(0));
            }
            if (pads == padLen) {
                return str.concat(padStr);
            }
            if (pads < padLen) {
                return str.concat(padStr.substring(0, pads));
            }
            char[] padding = new char[pads];
            char[] padChars = padStr.toCharArray();
            for (int i = 0; i < pads; i++) {
                padding[i] = padChars[i % padLen];
            }
            return str.concat(new String(padding));
        }
        return str;
    }

    public static String leftPad(String str, int size) {
        return leftPad(str, size, ' ');
    }

    public static String leftPad(String str, int size, char padChar) {
        if (str == null) {
            return null;
        }
        int pads = size - str.length();
        if (pads > 0) {
            if (pads > 8192) {
                return leftPad(str, size, String.valueOf(padChar));
            }
            return repeat(padChar, pads).concat(str);
        }
        return str;
    }

    public static String leftPad(String str, int size, String padStr) {
        if (str == null) {
            return null;
        }
        if (isEmpty(padStr)) {
            padStr = " ";
        }
        int padLen = padStr.length();
        int strLen = str.length();
        int pads = size - strLen;
        if (pads > 0) {
            if (padLen == 1 && pads <= 8192) {
                return leftPad(str, size, padStr.charAt(0));
            }
            if (pads == padLen) {
                return padStr.concat(str);
            }
            if (pads < padLen) {
                return padStr.substring(0, pads).concat(str);
            }
            char[] padding = new char[pads];
            char[] padChars = padStr.toCharArray();
            for (int i = 0; i < pads; i++) {
                padding[i] = padChars[i % padLen];
            }
            return new String(padding).concat(str);
        }
        return str;
    }

    public static int length(CharSequence cs) {
        if (cs == null) {
            return 0;
        }
        return cs.length();
    }

    public static String center(String str, int size) {
        return center(str, size, ' ');
    }

    public static String center(String str, int size, char padChar) {
        int strLen;
        int pads;
        if (str != null && size > 0 && (pads = size - (strLen = str.length())) > 0) {
            return rightPad(leftPad(str, (pads / 2) + strLen, padChar), size, padChar);
        }
        return str;
    }

    public static String center(String str, int size, String padStr) {
        if (str != null && size > 0) {
            if (isEmpty(padStr)) {
                padStr = " ";
            }
            int strLen = str.length();
            int pads = size - strLen;
            if (pads > 0) {
                return rightPad(leftPad(str, (pads / 2) + strLen, padStr), size, padStr);
            }
            return str;
        }
        return str;
    }

    public static String upperCase(String str) {
        if (str == null) {
            return null;
        }
        return str.toUpperCase();
    }

    public static String upperCase(String str, Locale locale) {
        if (str == null) {
            return null;
        }
        return str.toUpperCase(locale);
    }

    public static String lowerCase(String str) {
        if (str == null) {
            return null;
        }
        return str.toLowerCase();
    }

    public static String lowerCase(String str, Locale locale) {
        if (str == null) {
            return null;
        }
        return str.toLowerCase(locale);
    }

    public static String capitalize(String str) {
        int strLen;
        return (str == null || (strLen = str.length()) == 0) ? str : new StringBuilder(strLen).append(Character.toTitleCase(str.charAt(0))).append(str.substring(1)).toString();
    }

    public static String uncapitalize(String str) {
        int strLen;
        return (str == null || (strLen = str.length()) == 0) ? str : new StringBuilder(strLen).append(Character.toLowerCase(str.charAt(0))).append(str.substring(1)).toString();
    }

    public static String swapCase(String str) {
        if (!isEmpty(str)) {
            char[] buffer = str.toCharArray();
            for (int i = 0; i < buffer.length; i++) {
                char ch = buffer[i];
                if (Character.isUpperCase(ch)) {
                    buffer[i] = Character.toLowerCase(ch);
                } else if (Character.isTitleCase(ch)) {
                    buffer[i] = Character.toLowerCase(ch);
                } else if (Character.isLowerCase(ch)) {
                    buffer[i] = Character.toUpperCase(ch);
                }
            }
            return new String(buffer);
        }
        return str;
    }

    public static int countMatches(CharSequence str, CharSequence sub) {
        if (isEmpty(str) || isEmpty(sub)) {
            return 0;
        }
        int count = 0;
        int idx = 0;
        while (true) {
            int idx2 = CharSequenceUtils.indexOf(str, sub, idx);
            if (idx2 != -1) {
                count++;
                idx = idx2 + sub.length();
            } else {
                return count;
            }
        }
    }

    public static boolean isAlpha(CharSequence cs) {
        if (cs == null || cs.length() == 0) {
            return false;
        }
        int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isLetter(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAlphaSpace(CharSequence cs) {
        if (cs == null) {
            return false;
        }
        int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isLetter(cs.charAt(i)) && cs.charAt(i) != ' ') {
                return false;
            }
        }
        return true;
    }

    public static boolean isAlphanumeric(CharSequence cs) {
        if (cs == null || cs.length() == 0) {
            return false;
        }
        int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isLetterOrDigit(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAlphanumericSpace(CharSequence cs) {
        if (cs == null) {
            return false;
        }
        int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isLetterOrDigit(cs.charAt(i)) && cs.charAt(i) != ' ') {
                return false;
            }
        }
        return true;
    }

    public static boolean isAsciiPrintable(CharSequence cs) {
        if (cs == null) {
            return false;
        }
        int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!CharUtils.isAsciiPrintable(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNumeric(CharSequence cs) {
        if (cs == null || cs.length() == 0) {
            return false;
        }
        int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNumericSpace(CharSequence cs) {
        if (cs == null) {
            return false;
        }
        int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(cs.charAt(i)) && cs.charAt(i) != ' ') {
                return false;
            }
        }
        return true;
    }

    public static boolean isWhitespace(CharSequence cs) {
        if (cs == null) {
            return false;
        }
        int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAllLowerCase(CharSequence cs) {
        if (cs == null || isEmpty(cs)) {
            return false;
        }
        int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isLowerCase(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAllUpperCase(CharSequence cs) {
        if (cs == null || isEmpty(cs)) {
            return false;
        }
        int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isUpperCase(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String defaultString(String str) {
        return str == null ? "" : str;
    }

    public static String defaultString(String str, String defaultStr) {
        return str == null ? defaultStr : str;
    }

    public static <T extends CharSequence> T defaultIfBlank(T str, T defaultStr) {
        return isBlank(str) ? defaultStr : str;
    }

    public static <T extends CharSequence> T defaultIfEmpty(T str, T defaultStr) {
        return isEmpty(str) ? defaultStr : str;
    }

    public static String reverse(String str) {
        if (str == null) {
            return null;
        }
        return new StringBuilder(str).reverse().toString();
    }

    public static String reverseDelimited(String str, char separatorChar) {
        if (str == null) {
            return null;
        }
        String[] strs = split(str, separatorChar);
        ArrayUtils.reverse(strs);
        return join(strs, separatorChar);
    }

    public static String abbreviate(String str, int maxWidth) {
        return abbreviate(str, 0, maxWidth);
    }

    public static String abbreviate(String str, int offset, int maxWidth) {
        if (str == null) {
            return null;
        }
        if (maxWidth < 4) {
            throw new IllegalArgumentException("Minimum abbreviation width is 4");
        }
        if (str.length() > maxWidth) {
            if (offset > str.length()) {
                offset = str.length();
            }
            if (str.length() - offset < maxWidth - 3) {
                offset = str.length() - (maxWidth - 3);
            }
            if (offset <= 4) {
                return str.substring(0, maxWidth - 3) + "...";
            }
            if (maxWidth < 7) {
                throw new IllegalArgumentException("Minimum abbreviation width with offset is 7");
            }
            if ((offset + maxWidth) - 3 < str.length()) {
                return "..." + abbreviate(str.substring(offset), maxWidth - 3);
            }
            return "..." + str.substring(str.length() - (maxWidth - 3));
        }
        return str;
    }

    public static String abbreviateMiddle(String str, String middle, int length) {
        if (!isEmpty(str) && !isEmpty(middle) && length < str.length() && length >= middle.length() + 2) {
            int targetSting = length - middle.length();
            int startOffset = (targetSting / 2) + (targetSting % 2);
            int endOffset = str.length() - (targetSting / 2);
            StringBuilder builder = new StringBuilder(length);
            builder.append(str.substring(0, startOffset));
            builder.append(middle);
            builder.append(str.substring(endOffset));
            return builder.toString();
        }
        return str;
    }

    public static String difference(String str1, String str2) {
        if (str1 != null) {
            if (str2 == null) {
                return str1;
            }
            int at = indexOfDifference(str1, str2);
            if (at == -1) {
                return "";
            }
            return str2.substring(at);
        }
        return str2;
    }

    public static int indexOfDifference(CharSequence cs1, CharSequence cs2) {
        if (cs1 == cs2) {
            return -1;
        }
        if (cs1 == null || cs2 == null) {
            return 0;
        }
        int i = 0;
        while (i < cs1.length() && i < cs2.length() && cs1.charAt(i) == cs2.charAt(i)) {
            i++;
        }
        if (i < cs2.length() || i < cs1.length()) {
            return i;
        }
        return -1;
    }

    public static int indexOfDifference(CharSequence... css) {
        if (css == null || css.length <= 1) {
            return -1;
        }
        boolean anyStringNull = false;
        boolean allStringsNull = true;
        int arrayLen = css.length;
        int shortestStrLen = Integer.MAX_VALUE;
        int longestStrLen = 0;
        for (int i = 0; i < arrayLen; i++) {
            if (css[i] == null) {
                anyStringNull = true;
                shortestStrLen = 0;
            } else {
                allStringsNull = false;
                shortestStrLen = Math.min(css[i].length(), shortestStrLen);
                longestStrLen = Math.max(css[i].length(), longestStrLen);
            }
        }
        if (allStringsNull || (longestStrLen == 0 && !anyStringNull)) {
            return -1;
        }
        if (shortestStrLen == 0) {
            return 0;
        }
        int firstDiff = -1;
        for (int stringPos = 0; stringPos < shortestStrLen; stringPos++) {
            char comparisonChar = css[0].charAt(stringPos);
            int arrayPos = 1;
            while (true) {
                if (arrayPos >= arrayLen) {
                    break;
                }
                if (css[arrayPos].charAt(stringPos) == comparisonChar) {
                    arrayPos++;
                } else {
                    firstDiff = stringPos;
                    break;
                }
            }
            if (firstDiff != -1) {
                break;
            }
        }
        if (firstDiff != -1 || shortestStrLen == longestStrLen) {
            int shortestStrLen2 = firstDiff;
            return shortestStrLen2;
        }
        return shortestStrLen;
    }

    public static String getCommonPrefix(String... strs) {
        if (strs == null || strs.length == 0) {
            return "";
        }
        int smallestIndexOfDiff = indexOfDifference(strs);
        if (smallestIndexOfDiff == -1) {
            if (strs[0] == null) {
                return "";
            }
            return strs[0];
        }
        if (smallestIndexOfDiff == 0) {
            return "";
        }
        return strs[0].substring(0, smallestIndexOfDiff);
    }

    public static int getLevenshteinDistance(CharSequence s, CharSequence t) {
        if (s == null || t == null) {
            throw new IllegalArgumentException("Strings must not be null");
        }
        int n = s.length();
        int m = t.length();
        if (n == 0) {
            return m;
        }
        if (m == 0) {
            return n;
        }
        if (n > m) {
            s = t;
            t = s;
            n = m;
            m = t.length();
        }
        int[] p = new int[n + 1];
        int[] d = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            p[i] = i;
        }
        for (int j = 1; j <= m; j++) {
            char t_j = t.charAt(j - 1);
            d[0] = j;
            for (int i2 = 1; i2 <= n; i2++) {
                int cost = s.charAt(i2 + (-1)) == t_j ? 0 : 1;
                d[i2] = Math.min(Math.min(d[i2 - 1] + 1, p[i2] + 1), p[i2 - 1] + cost);
            }
            int[] _d = p;
            p = d;
            d = _d;
        }
        return p[n];
    }

    public static int getLevenshteinDistance(CharSequence s, CharSequence t, int threshold) {
        if (s == null || t == null) {
            throw new IllegalArgumentException("Strings must not be null");
        }
        if (threshold < 0) {
            throw new IllegalArgumentException("Threshold must not be negative");
        }
        int n = s.length();
        int m = t.length();
        if (n == 0) {
            if (m <= threshold) {
                return m;
            }
            return -1;
        }
        if (m == 0) {
            if (n <= threshold) {
                return n;
            }
            return -1;
        }
        if (n > m) {
            s = t;
            t = s;
            n = m;
            m = t.length();
        }
        int[] p = new int[n + 1];
        int[] d = new int[n + 1];
        int boundary = Math.min(n, threshold) + 1;
        for (int i = 0; i < boundary; i++) {
            p[i] = i;
        }
        Arrays.fill(p, boundary, p.length, Integer.MAX_VALUE);
        Arrays.fill(d, Integer.MAX_VALUE);
        for (int j = 1; j <= m; j++) {
            char t_j = t.charAt(j - 1);
            d[0] = j;
            int min = Math.max(1, j - threshold);
            int max = Math.min(n, j + threshold);
            if (min > max) {
                return -1;
            }
            if (min > 1) {
                d[min - 1] = Integer.MAX_VALUE;
            }
            for (int i2 = min; i2 <= max; i2++) {
                if (s.charAt(i2 - 1) == t_j) {
                    d[i2] = p[i2 - 1];
                } else {
                    d[i2] = Math.min(Math.min(d[i2 - 1], p[i2]), p[i2 - 1]) + 1;
                }
            }
            int[] _d = p;
            p = d;
            d = _d;
        }
        if (p[n] <= threshold) {
            return p[n];
        }
        return -1;
    }

    public static boolean startsWith(CharSequence str, CharSequence prefix) {
        return startsWith(str, prefix, false);
    }

    public static boolean startsWithIgnoreCase(CharSequence str, CharSequence prefix) {
        return startsWith(str, prefix, true);
    }

    private static boolean startsWith(CharSequence str, CharSequence prefix, boolean ignoreCase) {
        if (str == null || prefix == null) {
            return str == null && prefix == null;
        }
        if (prefix.length() <= str.length()) {
            return CharSequenceUtils.regionMatches(str, ignoreCase, 0, prefix, 0, prefix.length());
        }
        return false;
    }

    public static boolean startsWithAny(CharSequence string, CharSequence... searchStrings) {
        if (isEmpty(string) || ArrayUtils.isEmpty(searchStrings)) {
            return false;
        }
        for (CharSequence searchString : searchStrings) {
            if (startsWith(string, searchString)) {
                return true;
            }
        }
        return false;
    }

    public static boolean endsWith(CharSequence str, CharSequence suffix) {
        return endsWith(str, suffix, false);
    }

    public static boolean endsWithIgnoreCase(CharSequence str, CharSequence suffix) {
        return endsWith(str, suffix, true);
    }

    private static boolean endsWith(CharSequence str, CharSequence suffix, boolean ignoreCase) {
        if (str == null || suffix == null) {
            return str == null && suffix == null;
        }
        if (suffix.length() > str.length()) {
            return false;
        }
        int strOffset = str.length() - suffix.length();
        return CharSequenceUtils.regionMatches(str, ignoreCase, strOffset, suffix, 0, suffix.length());
    }

    public static String normalizeSpace(String str) {
        if (str == null) {
            return null;
        }
        return WHITESPACE_BLOCK.matcher(trim(str)).replaceAll(" ");
    }

    public static boolean endsWithAny(CharSequence string, CharSequence... searchStrings) {
        if (isEmpty(string) || ArrayUtils.isEmpty(searchStrings)) {
            return false;
        }
        for (CharSequence searchString : searchStrings) {
            if (endsWith(string, searchString)) {
                return true;
            }
        }
        return false;
    }

    public static String toString(byte[] bytes, String charsetName) throws UnsupportedEncodingException {
        return charsetName == null ? new String(bytes) : new String(bytes, charsetName);
    }
}
