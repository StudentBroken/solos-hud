package org.apache.commons.lang3.text;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

/* JADX INFO: loaded from: classes51.dex */
public class WordUtils {
    public static String wrap(String str, int wrapLength) {
        return wrap(str, wrapLength, null, false);
    }

    public static String wrap(String str, int wrapLength, String newLineStr, boolean wrapLongWords) {
        if (str == null) {
            return null;
        }
        if (newLineStr == null) {
            newLineStr = SystemUtils.LINE_SEPARATOR;
        }
        if (wrapLength < 1) {
            wrapLength = 1;
        }
        int inputLineLength = str.length();
        int offset = 0;
        StringBuilder wrappedLine = new StringBuilder(inputLineLength + 32);
        while (inputLineLength - offset > wrapLength) {
            if (str.charAt(offset) == ' ') {
                offset++;
            } else {
                int spaceToWrapAt = str.lastIndexOf(32, wrapLength + offset);
                if (spaceToWrapAt >= offset) {
                    wrappedLine.append(str.substring(offset, spaceToWrapAt));
                    wrappedLine.append(newLineStr);
                    offset = spaceToWrapAt + 1;
                } else if (wrapLongWords) {
                    wrappedLine.append(str.substring(offset, wrapLength + offset));
                    wrappedLine.append(newLineStr);
                    offset += wrapLength;
                } else {
                    int spaceToWrapAt2 = str.indexOf(32, wrapLength + offset);
                    if (spaceToWrapAt2 >= 0) {
                        wrappedLine.append(str.substring(offset, spaceToWrapAt2));
                        wrappedLine.append(newLineStr);
                        offset = spaceToWrapAt2 + 1;
                    } else {
                        wrappedLine.append(str.substring(offset));
                        offset = inputLineLength;
                    }
                }
            }
        }
        wrappedLine.append(str.substring(offset));
        return wrappedLine.toString();
    }

    public static String capitalize(String str) {
        return capitalize(str, null);
    }

    public static String capitalize(String str, char... delimiters) {
        int delimLen = delimiters == null ? -1 : delimiters.length;
        if (!StringUtils.isEmpty(str) && delimLen != 0) {
            char[] buffer = str.toCharArray();
            boolean capitalizeNext = true;
            for (int i = 0; i < buffer.length; i++) {
                char ch = buffer[i];
                if (isDelimiter(ch, delimiters)) {
                    capitalizeNext = true;
                } else if (capitalizeNext) {
                    buffer[i] = Character.toTitleCase(ch);
                    capitalizeNext = false;
                }
            }
            return new String(buffer);
        }
        return str;
    }

    public static String capitalizeFully(String str) {
        return capitalizeFully(str, null);
    }

    public static String capitalizeFully(String str, char... delimiters) {
        int delimLen = delimiters == null ? -1 : delimiters.length;
        return (StringUtils.isEmpty(str) || delimLen == 0) ? str : capitalize(str.toLowerCase(), delimiters);
    }

    public static String uncapitalize(String str) {
        return uncapitalize(str, null);
    }

    public static String uncapitalize(String str, char... delimiters) {
        int delimLen = delimiters == null ? -1 : delimiters.length;
        if (!StringUtils.isEmpty(str) && delimLen != 0) {
            char[] buffer = str.toCharArray();
            boolean uncapitalizeNext = true;
            for (int i = 0; i < buffer.length; i++) {
                char ch = buffer[i];
                if (isDelimiter(ch, delimiters)) {
                    uncapitalizeNext = true;
                } else if (uncapitalizeNext) {
                    buffer[i] = Character.toLowerCase(ch);
                    uncapitalizeNext = false;
                }
            }
            return new String(buffer);
        }
        return str;
    }

    public static String swapCase(String str) {
        if (!StringUtils.isEmpty(str)) {
            char[] buffer = str.toCharArray();
            boolean whitespace = true;
            for (int i = 0; i < buffer.length; i++) {
                char ch = buffer[i];
                if (Character.isUpperCase(ch)) {
                    buffer[i] = Character.toLowerCase(ch);
                    whitespace = false;
                } else if (Character.isTitleCase(ch)) {
                    buffer[i] = Character.toLowerCase(ch);
                    whitespace = false;
                } else if (Character.isLowerCase(ch)) {
                    if (whitespace) {
                        buffer[i] = Character.toTitleCase(ch);
                        whitespace = false;
                    } else {
                        buffer[i] = Character.toUpperCase(ch);
                    }
                } else {
                    whitespace = Character.isWhitespace(ch);
                }
            }
            return new String(buffer);
        }
        return str;
    }

    public static String initials(String str) {
        return initials(str, null);
    }

    public static String initials(String str, char... delimiters) {
        int count;
        if (!StringUtils.isEmpty(str)) {
            if (delimiters != null && delimiters.length == 0) {
                return "";
            }
            int strLen = str.length();
            char[] buf = new char[(strLen / 2) + 1];
            boolean lastWasGap = true;
            int i = 0;
            int count2 = 0;
            while (i < strLen) {
                char ch = str.charAt(i);
                if (isDelimiter(ch, delimiters)) {
                    lastWasGap = true;
                    count = count2;
                } else if (lastWasGap) {
                    count = count2 + 1;
                    buf[count2] = ch;
                    lastWasGap = false;
                } else {
                    count = count2;
                }
                i++;
                count2 = count;
            }
            return new String(buf, 0, count2);
        }
        return str;
    }

    private static boolean isDelimiter(char ch, char[] delimiters) {
        if (delimiters == null) {
            return Character.isWhitespace(ch);
        }
        for (char delimiter : delimiters) {
            if (ch == delimiter) {
                return true;
            }
        }
        return false;
    }
}
