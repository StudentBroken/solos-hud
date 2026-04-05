package org.apache.commons.lang3.text;

import java.io.Reader;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.SystemUtils;

/* JADX INFO: loaded from: classes51.dex */
public class StrBuilder implements CharSequence, Appendable {
    static final int CAPACITY = 32;
    private static final long serialVersionUID = 7628716375283629643L;
    protected char[] buffer;
    private String newLine;
    private String nullText;
    protected int size;

    public StrBuilder() {
        this(32);
    }

    public StrBuilder(int initialCapacity) {
        this.buffer = new char[initialCapacity <= 0 ? 32 : initialCapacity];
    }

    public StrBuilder(String str) {
        if (str == null) {
            this.buffer = new char[32];
        } else {
            this.buffer = new char[str.length() + 32];
            append(str);
        }
    }

    public String getNewLineText() {
        return this.newLine;
    }

    public StrBuilder setNewLineText(String newLine) {
        this.newLine = newLine;
        return this;
    }

    public String getNullText() {
        return this.nullText;
    }

    public StrBuilder setNullText(String nullText) {
        if (nullText != null && nullText.length() == 0) {
            nullText = null;
        }
        this.nullText = nullText;
        return this;
    }

    @Override // java.lang.CharSequence
    public int length() {
        return this.size;
    }

    public StrBuilder setLength(int length) {
        if (length < 0) {
            throw new StringIndexOutOfBoundsException(length);
        }
        if (length < this.size) {
            this.size = length;
        } else if (length > this.size) {
            ensureCapacity(length);
            int oldEnd = this.size;
            this.size = length;
            for (int i = oldEnd; i < length; i++) {
                this.buffer[i] = 0;
            }
        }
        return this;
    }

    public int capacity() {
        return this.buffer.length;
    }

    public StrBuilder ensureCapacity(int capacity) {
        if (capacity > this.buffer.length) {
            char[] old = this.buffer;
            this.buffer = new char[capacity * 2];
            System.arraycopy(old, 0, this.buffer, 0, this.size);
        }
        return this;
    }

    public StrBuilder minimizeCapacity() {
        if (this.buffer.length > length()) {
            char[] old = this.buffer;
            this.buffer = new char[length()];
            System.arraycopy(old, 0, this.buffer, 0, this.size);
        }
        return this;
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public StrBuilder clear() {
        this.size = 0;
        return this;
    }

    @Override // java.lang.CharSequence
    public char charAt(int index) {
        if (index < 0 || index >= length()) {
            throw new StringIndexOutOfBoundsException(index);
        }
        return this.buffer[index];
    }

    public StrBuilder setCharAt(int index, char ch) {
        if (index < 0 || index >= length()) {
            throw new StringIndexOutOfBoundsException(index);
        }
        this.buffer[index] = ch;
        return this;
    }

    public StrBuilder deleteCharAt(int index) {
        if (index < 0 || index >= this.size) {
            throw new StringIndexOutOfBoundsException(index);
        }
        deleteImpl(index, index + 1, 1);
        return this;
    }

    public char[] toCharArray() {
        if (this.size == 0) {
            return ArrayUtils.EMPTY_CHAR_ARRAY;
        }
        char[] chars = new char[this.size];
        System.arraycopy(this.buffer, 0, chars, 0, this.size);
        return chars;
    }

    public char[] toCharArray(int startIndex, int endIndex) {
        int len = validateRange(startIndex, endIndex) - startIndex;
        if (len == 0) {
            return ArrayUtils.EMPTY_CHAR_ARRAY;
        }
        char[] chars = new char[len];
        System.arraycopy(this.buffer, startIndex, chars, 0, len);
        return chars;
    }

    public char[] getChars(char[] destination) {
        int len = length();
        if (destination == null || destination.length < len) {
            destination = new char[len];
        }
        System.arraycopy(this.buffer, 0, destination, 0, len);
        return destination;
    }

    public void getChars(int startIndex, int endIndex, char[] destination, int destinationIndex) {
        if (startIndex < 0) {
            throw new StringIndexOutOfBoundsException(startIndex);
        }
        if (endIndex < 0 || endIndex > length()) {
            throw new StringIndexOutOfBoundsException(endIndex);
        }
        if (startIndex > endIndex) {
            throw new StringIndexOutOfBoundsException("end < start");
        }
        System.arraycopy(this.buffer, startIndex, destination, destinationIndex, endIndex - startIndex);
    }

    public StrBuilder appendNewLine() {
        if (this.newLine != null) {
            return append(this.newLine);
        }
        append(SystemUtils.LINE_SEPARATOR);
        return this;
    }

    public StrBuilder appendNull() {
        return this.nullText == null ? this : append(this.nullText);
    }

    public StrBuilder append(Object obj) {
        return obj == null ? appendNull() : append(obj.toString());
    }

    @Override // java.lang.Appendable
    public StrBuilder append(CharSequence seq) {
        return seq == null ? appendNull() : append(seq.toString());
    }

    @Override // java.lang.Appendable
    public StrBuilder append(CharSequence seq, int startIndex, int length) {
        return seq == null ? appendNull() : append(seq.toString(), startIndex, length);
    }

    public StrBuilder append(String str) {
        if (str == null) {
            return appendNull();
        }
        int strLen = str.length();
        if (strLen > 0) {
            int len = length();
            ensureCapacity(len + strLen);
            str.getChars(0, strLen, this.buffer, len);
            this.size += strLen;
            return this;
        }
        return this;
    }

    public StrBuilder append(String str, int startIndex, int length) {
        if (str == null) {
            return appendNull();
        }
        if (startIndex < 0 || startIndex > str.length()) {
            throw new StringIndexOutOfBoundsException("startIndex must be valid");
        }
        if (length < 0 || startIndex + length > str.length()) {
            throw new StringIndexOutOfBoundsException("length must be valid");
        }
        if (length > 0) {
            int len = length();
            ensureCapacity(len + length);
            str.getChars(startIndex, startIndex + length, this.buffer, len);
            this.size += length;
            return this;
        }
        return this;
    }

    public StrBuilder append(StringBuffer str) {
        if (str == null) {
            return appendNull();
        }
        int strLen = str.length();
        if (strLen > 0) {
            int len = length();
            ensureCapacity(len + strLen);
            str.getChars(0, strLen, this.buffer, len);
            this.size += strLen;
            return this;
        }
        return this;
    }

    public StrBuilder append(StringBuffer str, int startIndex, int length) {
        if (str == null) {
            return appendNull();
        }
        if (startIndex < 0 || startIndex > str.length()) {
            throw new StringIndexOutOfBoundsException("startIndex must be valid");
        }
        if (length < 0 || startIndex + length > str.length()) {
            throw new StringIndexOutOfBoundsException("length must be valid");
        }
        if (length > 0) {
            int len = length();
            ensureCapacity(len + length);
            str.getChars(startIndex, startIndex + length, this.buffer, len);
            this.size += length;
            return this;
        }
        return this;
    }

    public StrBuilder append(StrBuilder str) {
        if (str == null) {
            return appendNull();
        }
        int strLen = str.length();
        if (strLen > 0) {
            int len = length();
            ensureCapacity(len + strLen);
            System.arraycopy(str.buffer, 0, this.buffer, len, strLen);
            this.size += strLen;
            return this;
        }
        return this;
    }

    public StrBuilder append(StrBuilder str, int startIndex, int length) {
        if (str == null) {
            return appendNull();
        }
        if (startIndex < 0 || startIndex > str.length()) {
            throw new StringIndexOutOfBoundsException("startIndex must be valid");
        }
        if (length < 0 || startIndex + length > str.length()) {
            throw new StringIndexOutOfBoundsException("length must be valid");
        }
        if (length > 0) {
            int len = length();
            ensureCapacity(len + length);
            str.getChars(startIndex, startIndex + length, this.buffer, len);
            this.size += length;
            return this;
        }
        return this;
    }

    public StrBuilder append(char[] chars) {
        if (chars == null) {
            return appendNull();
        }
        int strLen = chars.length;
        if (strLen > 0) {
            int len = length();
            ensureCapacity(len + strLen);
            System.arraycopy(chars, 0, this.buffer, len, strLen);
            this.size += strLen;
            return this;
        }
        return this;
    }

    public StrBuilder append(char[] chars, int startIndex, int length) {
        if (chars == null) {
            return appendNull();
        }
        if (startIndex < 0 || startIndex > chars.length) {
            throw new StringIndexOutOfBoundsException("Invalid startIndex: " + length);
        }
        if (length < 0 || startIndex + length > chars.length) {
            throw new StringIndexOutOfBoundsException("Invalid length: " + length);
        }
        if (length > 0) {
            int len = length();
            ensureCapacity(len + length);
            System.arraycopy(chars, startIndex, this.buffer, len, length);
            this.size += length;
            return this;
        }
        return this;
    }

    public StrBuilder append(boolean value) {
        if (value) {
            ensureCapacity(this.size + 4);
            char[] cArr = this.buffer;
            int i = this.size;
            this.size = i + 1;
            cArr[i] = 't';
            char[] cArr2 = this.buffer;
            int i2 = this.size;
            this.size = i2 + 1;
            cArr2[i2] = 'r';
            char[] cArr3 = this.buffer;
            int i3 = this.size;
            this.size = i3 + 1;
            cArr3[i3] = 'u';
            char[] cArr4 = this.buffer;
            int i4 = this.size;
            this.size = i4 + 1;
            cArr4[i4] = 'e';
        } else {
            ensureCapacity(this.size + 5);
            char[] cArr5 = this.buffer;
            int i5 = this.size;
            this.size = i5 + 1;
            cArr5[i5] = 'f';
            char[] cArr6 = this.buffer;
            int i6 = this.size;
            this.size = i6 + 1;
            cArr6[i6] = 'a';
            char[] cArr7 = this.buffer;
            int i7 = this.size;
            this.size = i7 + 1;
            cArr7[i7] = 'l';
            char[] cArr8 = this.buffer;
            int i8 = this.size;
            this.size = i8 + 1;
            cArr8[i8] = 's';
            char[] cArr9 = this.buffer;
            int i9 = this.size;
            this.size = i9 + 1;
            cArr9[i9] = 'e';
        }
        return this;
    }

    @Override // java.lang.Appendable
    public StrBuilder append(char ch) {
        int len = length();
        ensureCapacity(len + 1);
        char[] cArr = this.buffer;
        int i = this.size;
        this.size = i + 1;
        cArr[i] = ch;
        return this;
    }

    public StrBuilder append(int value) {
        return append(String.valueOf(value));
    }

    public StrBuilder append(long value) {
        return append(String.valueOf(value));
    }

    public StrBuilder append(float value) {
        return append(String.valueOf(value));
    }

    public StrBuilder append(double value) {
        return append(String.valueOf(value));
    }

    public StrBuilder appendln(Object obj) {
        return append(obj).appendNewLine();
    }

    public StrBuilder appendln(String str) {
        return append(str).appendNewLine();
    }

    public StrBuilder appendln(String str, int startIndex, int length) {
        return append(str, startIndex, length).appendNewLine();
    }

    public StrBuilder appendln(StringBuffer str) {
        return append(str).appendNewLine();
    }

    public StrBuilder appendln(StringBuffer str, int startIndex, int length) {
        return append(str, startIndex, length).appendNewLine();
    }

    public StrBuilder appendln(StrBuilder str) {
        return append(str).appendNewLine();
    }

    public StrBuilder appendln(StrBuilder str, int startIndex, int length) {
        return append(str, startIndex, length).appendNewLine();
    }

    public StrBuilder appendln(char[] chars) {
        return append(chars).appendNewLine();
    }

    public StrBuilder appendln(char[] chars, int startIndex, int length) {
        return append(chars, startIndex, length).appendNewLine();
    }

    public StrBuilder appendln(boolean value) {
        return append(value).appendNewLine();
    }

    public StrBuilder appendln(char ch) {
        return append(ch).appendNewLine();
    }

    public StrBuilder appendln(int value) {
        return append(value).appendNewLine();
    }

    public StrBuilder appendln(long value) {
        return append(value).appendNewLine();
    }

    public StrBuilder appendln(float value) {
        return append(value).appendNewLine();
    }

    public StrBuilder appendln(double value) {
        return append(value).appendNewLine();
    }

    public StrBuilder appendAll(Object[] array) {
        if (array != null && array.length > 0) {
            for (Object element : array) {
                append(element);
            }
        }
        return this;
    }

    public StrBuilder appendAll(Iterable<?> iterable) {
        if (iterable != null) {
            Iterator<?> it = iterable.iterator();
            while (it.hasNext()) {
                append(it.next());
            }
        }
        return this;
    }

    public StrBuilder appendAll(Iterator<?> it) {
        if (it != null) {
            while (it.hasNext()) {
                append(it.next());
            }
        }
        return this;
    }

    public StrBuilder appendWithSeparators(Object[] array, String separator) {
        if (array != null && array.length > 0) {
            String separator2 = ObjectUtils.toString(separator);
            append(array[0]);
            for (int i = 1; i < array.length; i++) {
                append(separator2);
                append(array[i]);
            }
        }
        return this;
    }

    public StrBuilder appendWithSeparators(Iterable<?> iterable, String separator) {
        if (iterable != null) {
            String separator2 = ObjectUtils.toString(separator);
            Iterator<?> it = iterable.iterator();
            while (it.hasNext()) {
                append(it.next());
                if (it.hasNext()) {
                    append(separator2);
                }
            }
        }
        return this;
    }

    public StrBuilder appendWithSeparators(Iterator<?> it, String separator) {
        if (it != null) {
            String separator2 = ObjectUtils.toString(separator);
            while (it.hasNext()) {
                append(it.next());
                if (it.hasNext()) {
                    append(separator2);
                }
            }
        }
        return this;
    }

    public StrBuilder appendSeparator(String separator) {
        return appendSeparator(separator, (String) null);
    }

    public StrBuilder appendSeparator(String standard, String defaultIfEmpty) {
        String str = isEmpty() ? defaultIfEmpty : standard;
        if (str != null) {
            append(str);
        }
        return this;
    }

    public StrBuilder appendSeparator(char separator) {
        if (size() > 0) {
            append(separator);
        }
        return this;
    }

    public StrBuilder appendSeparator(char standard, char defaultIfEmpty) {
        if (size() > 0) {
            append(standard);
        } else {
            append(defaultIfEmpty);
        }
        return this;
    }

    public StrBuilder appendSeparator(String separator, int loopIndex) {
        if (separator != null && loopIndex > 0) {
            append(separator);
        }
        return this;
    }

    public StrBuilder appendSeparator(char separator, int loopIndex) {
        if (loopIndex > 0) {
            append(separator);
        }
        return this;
    }

    public StrBuilder appendPadding(int length, char padChar) {
        if (length >= 0) {
            ensureCapacity(this.size + length);
            for (int i = 0; i < length; i++) {
                char[] cArr = this.buffer;
                int i2 = this.size;
                this.size = i2 + 1;
                cArr[i2] = padChar;
            }
        }
        return this;
    }

    public StrBuilder appendFixedWidthPadLeft(Object obj, int width, char padChar) {
        if (width > 0) {
            ensureCapacity(this.size + width);
            String str = obj == null ? getNullText() : obj.toString();
            if (str == null) {
                str = "";
            }
            int strLen = str.length();
            if (strLen >= width) {
                str.getChars(strLen - width, strLen, this.buffer, this.size);
            } else {
                int padLen = width - strLen;
                for (int i = 0; i < padLen; i++) {
                    this.buffer[this.size + i] = padChar;
                }
                str.getChars(0, strLen, this.buffer, this.size + padLen);
            }
            this.size += width;
        }
        return this;
    }

    public StrBuilder appendFixedWidthPadLeft(int value, int width, char padChar) {
        return appendFixedWidthPadLeft(String.valueOf(value), width, padChar);
    }

    public StrBuilder appendFixedWidthPadRight(Object obj, int width, char padChar) {
        if (width > 0) {
            ensureCapacity(this.size + width);
            String str = obj == null ? getNullText() : obj.toString();
            if (str == null) {
                str = "";
            }
            int strLen = str.length();
            if (strLen >= width) {
                str.getChars(0, width, this.buffer, this.size);
            } else {
                int padLen = width - strLen;
                str.getChars(0, strLen, this.buffer, this.size);
                for (int i = 0; i < padLen; i++) {
                    this.buffer[this.size + strLen + i] = padChar;
                }
            }
            this.size += width;
        }
        return this;
    }

    public StrBuilder appendFixedWidthPadRight(int value, int width, char padChar) {
        return appendFixedWidthPadRight(String.valueOf(value), width, padChar);
    }

    public StrBuilder insert(int index, Object obj) {
        return obj == null ? insert(index, this.nullText) : insert(index, obj.toString());
    }

    public StrBuilder insert(int index, String str) {
        validateIndex(index);
        if (str == null) {
            str = this.nullText;
        }
        int strLen = str == null ? 0 : str.length();
        if (strLen > 0) {
            int newSize = this.size + strLen;
            ensureCapacity(newSize);
            System.arraycopy(this.buffer, index, this.buffer, index + strLen, this.size - index);
            this.size = newSize;
            str.getChars(0, strLen, this.buffer, index);
        }
        return this;
    }

    public StrBuilder insert(int index, char[] chars) {
        validateIndex(index);
        if (chars == null) {
            return insert(index, this.nullText);
        }
        int len = chars.length;
        if (len > 0) {
            ensureCapacity(this.size + len);
            System.arraycopy(this.buffer, index, this.buffer, index + len, this.size - index);
            System.arraycopy(chars, 0, this.buffer, index, len);
            this.size += len;
            return this;
        }
        return this;
    }

    public StrBuilder insert(int index, char[] chars, int offset, int length) {
        validateIndex(index);
        if (chars == null) {
            return insert(index, this.nullText);
        }
        if (offset < 0 || offset > chars.length) {
            throw new StringIndexOutOfBoundsException("Invalid offset: " + offset);
        }
        if (length < 0 || offset + length > chars.length) {
            throw new StringIndexOutOfBoundsException("Invalid length: " + length);
        }
        if (length > 0) {
            ensureCapacity(this.size + length);
            System.arraycopy(this.buffer, index, this.buffer, index + length, this.size - index);
            System.arraycopy(chars, offset, this.buffer, index, length);
            this.size += length;
            return this;
        }
        return this;
    }

    public StrBuilder insert(int index, boolean value) {
        validateIndex(index);
        if (value) {
            ensureCapacity(this.size + 4);
            System.arraycopy(this.buffer, index, this.buffer, index + 4, this.size - index);
            int index2 = index + 1;
            this.buffer[index] = 't';
            int index3 = index2 + 1;
            this.buffer[index2] = 'r';
            this.buffer[index3] = 'u';
            this.buffer[index3 + 1] = 'e';
            this.size += 4;
        } else {
            ensureCapacity(this.size + 5);
            System.arraycopy(this.buffer, index, this.buffer, index + 5, this.size - index);
            int index4 = index + 1;
            this.buffer[index] = 'f';
            int index5 = index4 + 1;
            this.buffer[index4] = 'a';
            int index6 = index5 + 1;
            this.buffer[index5] = 'l';
            this.buffer[index6] = 's';
            this.buffer[index6 + 1] = 'e';
            this.size += 5;
        }
        return this;
    }

    public StrBuilder insert(int index, char value) {
        validateIndex(index);
        ensureCapacity(this.size + 1);
        System.arraycopy(this.buffer, index, this.buffer, index + 1, this.size - index);
        this.buffer[index] = value;
        this.size++;
        return this;
    }

    public StrBuilder insert(int index, int value) {
        return insert(index, String.valueOf(value));
    }

    public StrBuilder insert(int index, long value) {
        return insert(index, String.valueOf(value));
    }

    public StrBuilder insert(int index, float value) {
        return insert(index, String.valueOf(value));
    }

    public StrBuilder insert(int index, double value) {
        return insert(index, String.valueOf(value));
    }

    private void deleteImpl(int startIndex, int endIndex, int len) {
        System.arraycopy(this.buffer, endIndex, this.buffer, startIndex, this.size - endIndex);
        this.size -= len;
    }

    public StrBuilder delete(int startIndex, int endIndex) {
        int endIndex2 = validateRange(startIndex, endIndex);
        int len = endIndex2 - startIndex;
        if (len > 0) {
            deleteImpl(startIndex, endIndex2, len);
        }
        return this;
    }

    public StrBuilder deleteAll(char ch) {
        int i = 0;
        while (i < this.size) {
            if (this.buffer[i] == ch) {
                int start = i;
                do {
                    i++;
                    if (i >= this.size) {
                        break;
                    }
                } while (this.buffer[i] == ch);
                int len = i - start;
                deleteImpl(start, i, len);
                i -= len;
            }
            i++;
        }
        return this;
    }

    public StrBuilder deleteFirst(char ch) {
        int i = 0;
        while (true) {
            if (i >= this.size) {
                break;
            }
            if (this.buffer[i] != ch) {
                i++;
            } else {
                deleteImpl(i, i + 1, 1);
                break;
            }
        }
        return this;
    }

    public StrBuilder deleteAll(String str) {
        int len = str == null ? 0 : str.length();
        if (len > 0) {
            int index = indexOf(str, 0);
            while (index >= 0) {
                deleteImpl(index, index + len, len);
                index = indexOf(str, index);
            }
        }
        return this;
    }

    public StrBuilder deleteFirst(String str) {
        int index;
        int len = str == null ? 0 : str.length();
        if (len > 0 && (index = indexOf(str, 0)) >= 0) {
            deleteImpl(index, index + len, len);
        }
        return this;
    }

    public StrBuilder deleteAll(StrMatcher matcher) {
        return replace(matcher, null, 0, this.size, -1);
    }

    public StrBuilder deleteFirst(StrMatcher matcher) {
        return replace(matcher, null, 0, this.size, 1);
    }

    private void replaceImpl(int startIndex, int endIndex, int removeLen, String insertStr, int insertLen) {
        int newSize = (this.size - removeLen) + insertLen;
        if (insertLen != removeLen) {
            ensureCapacity(newSize);
            System.arraycopy(this.buffer, endIndex, this.buffer, startIndex + insertLen, this.size - endIndex);
            this.size = newSize;
        }
        if (insertLen > 0) {
            insertStr.getChars(0, insertLen, this.buffer, startIndex);
        }
    }

    public StrBuilder replace(int startIndex, int endIndex, String replaceStr) {
        int endIndex2 = validateRange(startIndex, endIndex);
        int insertLen = replaceStr == null ? 0 : replaceStr.length();
        replaceImpl(startIndex, endIndex2, endIndex2 - startIndex, replaceStr, insertLen);
        return this;
    }

    public StrBuilder replaceAll(char search, char replace) {
        if (search != replace) {
            for (int i = 0; i < this.size; i++) {
                if (this.buffer[i] == search) {
                    this.buffer[i] = replace;
                }
            }
        }
        return this;
    }

    public StrBuilder replaceFirst(char search, char replace) {
        if (search != replace) {
            int i = 0;
            while (true) {
                if (i >= this.size) {
                    break;
                }
                if (this.buffer[i] != search) {
                    i++;
                } else {
                    this.buffer[i] = replace;
                    break;
                }
            }
        }
        return this;
    }

    public StrBuilder replaceAll(String searchStr, String replaceStr) {
        int searchLen = searchStr == null ? 0 : searchStr.length();
        if (searchLen > 0) {
            int replaceLen = replaceStr == null ? 0 : replaceStr.length();
            int index = indexOf(searchStr, 0);
            while (index >= 0) {
                replaceImpl(index, index + searchLen, searchLen, replaceStr, replaceLen);
                index = indexOf(searchStr, index + replaceLen);
            }
        }
        return this;
    }

    public StrBuilder replaceFirst(String searchStr, String replaceStr) {
        int index;
        int searchLen = searchStr == null ? 0 : searchStr.length();
        if (searchLen > 0 && (index = indexOf(searchStr, 0)) >= 0) {
            int replaceLen = replaceStr != null ? replaceStr.length() : 0;
            replaceImpl(index, index + searchLen, searchLen, replaceStr, replaceLen);
        }
        return this;
    }

    public StrBuilder replaceAll(StrMatcher matcher, String replaceStr) {
        return replace(matcher, replaceStr, 0, this.size, -1);
    }

    public StrBuilder replaceFirst(StrMatcher matcher, String replaceStr) {
        return replace(matcher, replaceStr, 0, this.size, 1);
    }

    public StrBuilder replace(StrMatcher matcher, String replaceStr, int startIndex, int endIndex, int replaceCount) {
        return replaceImpl(matcher, replaceStr, startIndex, validateRange(startIndex, endIndex), replaceCount);
    }

    private StrBuilder replaceImpl(StrMatcher matcher, String replaceStr, int from, int to, int replaceCount) {
        if (matcher != null && this.size != 0) {
            int replaceLen = replaceStr == null ? 0 : replaceStr.length();
            char[] buf = this.buffer;
            int i = from;
            while (i < to && replaceCount != 0) {
                int removeLen = matcher.isMatch(buf, i, from, to);
                if (removeLen > 0) {
                    replaceImpl(i, i + removeLen, removeLen, replaceStr, replaceLen);
                    to = (to - removeLen) + replaceLen;
                    i = (i + replaceLen) - 1;
                    if (replaceCount > 0) {
                        replaceCount--;
                    }
                }
                i++;
            }
        }
        return this;
    }

    public StrBuilder reverse() {
        if (this.size != 0) {
            int half = this.size / 2;
            char[] buf = this.buffer;
            int leftIdx = 0;
            int rightIdx = this.size - 1;
            while (leftIdx < half) {
                char swap = buf[leftIdx];
                buf[leftIdx] = buf[rightIdx];
                buf[rightIdx] = swap;
                leftIdx++;
                rightIdx--;
            }
        }
        return this;
    }

    public StrBuilder trim() {
        if (this.size != 0) {
            int len = this.size;
            char[] buf = this.buffer;
            int pos = 0;
            while (pos < len && buf[pos] <= ' ') {
                pos++;
            }
            while (pos < len && buf[len - 1] <= ' ') {
                len--;
            }
            if (len < this.size) {
                delete(len, this.size);
            }
            if (pos > 0) {
                delete(0, pos);
            }
        }
        return this;
    }

    public boolean startsWith(String str) {
        if (str == null) {
            return false;
        }
        int len = str.length();
        if (len == 0) {
            return true;
        }
        if (len > this.size) {
            return false;
        }
        for (int i = 0; i < len; i++) {
            if (this.buffer[i] != str.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    public boolean endsWith(String str) {
        if (str == null) {
            return false;
        }
        int len = str.length();
        if (len == 0) {
            return true;
        }
        if (len > this.size) {
            return false;
        }
        int pos = this.size - len;
        int i = 0;
        while (i < len) {
            if (this.buffer[pos] != str.charAt(i)) {
                return false;
            }
            i++;
            pos++;
        }
        return true;
    }

    @Override // java.lang.CharSequence
    public CharSequence subSequence(int startIndex, int endIndex) {
        if (startIndex < 0) {
            throw new StringIndexOutOfBoundsException(startIndex);
        }
        if (endIndex > this.size) {
            throw new StringIndexOutOfBoundsException(endIndex);
        }
        if (startIndex > endIndex) {
            throw new StringIndexOutOfBoundsException(endIndex - startIndex);
        }
        return substring(startIndex, endIndex);
    }

    public String substring(int start) {
        return substring(start, this.size);
    }

    public String substring(int startIndex, int endIndex) {
        return new String(this.buffer, startIndex, validateRange(startIndex, endIndex) - startIndex);
    }

    public String leftString(int length) {
        if (length <= 0) {
            return "";
        }
        if (length >= this.size) {
            return new String(this.buffer, 0, this.size);
        }
        return new String(this.buffer, 0, length);
    }

    public String rightString(int length) {
        if (length <= 0) {
            return "";
        }
        if (length >= this.size) {
            return new String(this.buffer, 0, this.size);
        }
        return new String(this.buffer, this.size - length, length);
    }

    public String midString(int index, int length) {
        if (index < 0) {
            index = 0;
        }
        if (length <= 0 || index >= this.size) {
            return "";
        }
        if (this.size <= index + length) {
            return new String(this.buffer, index, this.size - index);
        }
        return new String(this.buffer, index, length);
    }

    public boolean contains(char ch) {
        char[] thisBuf = this.buffer;
        for (int i = 0; i < this.size; i++) {
            if (thisBuf[i] == ch) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(String str) {
        return indexOf(str, 0) >= 0;
    }

    public boolean contains(StrMatcher matcher) {
        return indexOf(matcher, 0) >= 0;
    }

    public int indexOf(char ch) {
        return indexOf(ch, 0);
    }

    public int indexOf(char ch, int startIndex) {
        if (startIndex < 0) {
            startIndex = 0;
        }
        if (startIndex >= this.size) {
            return -1;
        }
        char[] thisBuf = this.buffer;
        for (int i = startIndex; i < this.size; i++) {
            if (thisBuf[i] == ch) {
                return i;
            }
        }
        return -1;
    }

    public int indexOf(String str) {
        return indexOf(str, 0);
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x003c, code lost:
    
        r0 = r0 + 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int indexOf(java.lang.String r9, int r10) {
        /*
            r8 = this;
            r6 = 0
            r5 = -1
            if (r10 >= 0) goto L5
            r10 = r6
        L5:
            if (r9 == 0) goto Lb
            int r7 = r8.size
            if (r10 < r7) goto Ld
        Lb:
            r10 = r5
        Lc:
            return r10
        Ld:
            int r3 = r9.length()
            r7 = 1
            if (r3 != r7) goto L1d
            char r5 = r9.charAt(r6)
            int r10 = r8.indexOf(r5, r10)
            goto Lc
        L1d:
            if (r3 == 0) goto Lc
            int r6 = r8.size
            if (r3 <= r6) goto L25
            r10 = r5
            goto Lc
        L25:
            char[] r4 = r8.buffer
            int r6 = r8.size
            int r6 = r6 - r3
            int r2 = r6 + 1
            r0 = r10
        L2d:
            if (r0 >= r2) goto L44
            r1 = 0
        L30:
            if (r1 >= r3) goto L42
            char r6 = r9.charAt(r1)
            int r7 = r0 + r1
            char r7 = r4[r7]
            if (r6 == r7) goto L3f
            int r0 = r0 + 1
            goto L2d
        L3f:
            int r1 = r1 + 1
            goto L30
        L42:
            r10 = r0
            goto Lc
        L44:
            r10 = r5
            goto Lc
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.text.StrBuilder.indexOf(java.lang.String, int):int");
    }

    public int indexOf(StrMatcher matcher) {
        return indexOf(matcher, 0);
    }

    public int indexOf(StrMatcher matcher, int startIndex) {
        if (startIndex < 0) {
            startIndex = 0;
        }
        if (matcher == null || startIndex >= this.size) {
            return -1;
        }
        int len = this.size;
        char[] buf = this.buffer;
        for (int i = startIndex; i < len; i++) {
            if (matcher.isMatch(buf, i, startIndex, len) > 0) {
                return i;
            }
        }
        return -1;
    }

    public int lastIndexOf(char ch) {
        return lastIndexOf(ch, this.size - 1);
    }

    public int lastIndexOf(char ch, int startIndex) {
        if (startIndex >= this.size) {
            startIndex = this.size - 1;
        }
        if (startIndex < 0) {
            return -1;
        }
        for (int i = startIndex; i >= 0; i--) {
            if (this.buffer[i] == ch) {
                return i;
            }
        }
        return -1;
    }

    public int lastIndexOf(String str) {
        return lastIndexOf(str, this.size - 1);
    }

    /* JADX WARN: Code restructure failed: missing block: B:22:0x003b, code lost:
    
        r0 = r0 - 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int lastIndexOf(java.lang.String r8, int r9) {
        /*
            r7 = this;
            r3 = -1
            int r4 = r7.size
            if (r9 < r4) goto L9
            int r4 = r7.size
            int r9 = r4 + (-1)
        L9:
            if (r8 == 0) goto Ld
            if (r9 >= 0) goto Lf
        Ld:
            r0 = r3
        Le:
            return r0
        Lf:
            int r2 = r8.length()
            if (r2 <= 0) goto L41
            int r4 = r7.size
            if (r2 > r4) goto L41
            r4 = 1
            if (r2 != r4) goto L26
            r3 = 0
            char r3 = r8.charAt(r3)
            int r0 = r7.lastIndexOf(r3, r9)
            goto Le
        L26:
            int r4 = r9 - r2
            int r0 = r4 + 1
        L2a:
            if (r0 < 0) goto L45
            r1 = 0
        L2d:
            if (r1 >= r2) goto Le
            char r4 = r8.charAt(r1)
            char[] r5 = r7.buffer
            int r6 = r0 + r1
            char r5 = r5[r6]
            if (r4 == r5) goto L3e
            int r0 = r0 + (-1)
            goto L2a
        L3e:
            int r1 = r1 + 1
            goto L2d
        L41:
            if (r2 != 0) goto L45
            r0 = r9
            goto Le
        L45:
            r0 = r3
            goto Le
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.text.StrBuilder.lastIndexOf(java.lang.String, int):int");
    }

    public int lastIndexOf(StrMatcher matcher) {
        return lastIndexOf(matcher, this.size);
    }

    public int lastIndexOf(StrMatcher matcher, int startIndex) {
        if (startIndex >= this.size) {
            startIndex = this.size - 1;
        }
        if (matcher == null || startIndex < 0) {
            return -1;
        }
        char[] buf = this.buffer;
        int endIndex = startIndex + 1;
        for (int i = startIndex; i >= 0; i--) {
            if (matcher.isMatch(buf, i, 0, endIndex) > 0) {
                return i;
            }
        }
        return -1;
    }

    public StrTokenizer asTokenizer() {
        return new StrBuilderTokenizer();
    }

    public Reader asReader() {
        return new StrBuilderReader();
    }

    public Writer asWriter() {
        return new StrBuilderWriter();
    }

    public boolean equalsIgnoreCase(StrBuilder other) {
        if (this == other) {
            return true;
        }
        if (this.size != other.size) {
            return false;
        }
        char[] thisBuf = this.buffer;
        char[] otherBuf = other.buffer;
        for (int i = this.size - 1; i >= 0; i--) {
            char c1 = thisBuf[i];
            char c2 = otherBuf[i];
            if (c1 != c2 && Character.toUpperCase(c1) != Character.toUpperCase(c2)) {
                return false;
            }
        }
        return true;
    }

    public boolean equals(StrBuilder other) {
        if (this == other) {
            return true;
        }
        if (this.size != other.size) {
            return false;
        }
        char[] thisBuf = this.buffer;
        char[] otherBuf = other.buffer;
        for (int i = this.size - 1; i >= 0; i--) {
            if (thisBuf[i] != otherBuf[i]) {
                return false;
            }
        }
        return true;
    }

    public boolean equals(Object obj) {
        if (obj instanceof StrBuilder) {
            return equals((StrBuilder) obj);
        }
        return false;
    }

    public int hashCode() {
        char[] buf = this.buffer;
        int hash = 0;
        for (int i = this.size - 1; i >= 0; i--) {
            hash = (hash * 31) + buf[i];
        }
        return hash;
    }

    @Override // java.lang.CharSequence
    public String toString() {
        return new String(this.buffer, 0, this.size);
    }

    public StringBuffer toStringBuffer() {
        return new StringBuffer(this.size).append(this.buffer, 0, this.size);
    }

    protected int validateRange(int startIndex, int endIndex) {
        if (startIndex < 0) {
            throw new StringIndexOutOfBoundsException(startIndex);
        }
        if (endIndex > this.size) {
            endIndex = this.size;
        }
        if (startIndex > endIndex) {
            throw new StringIndexOutOfBoundsException("end < start");
        }
        return endIndex;
    }

    protected void validateIndex(int index) {
        if (index < 0 || index > this.size) {
            throw new StringIndexOutOfBoundsException(index);
        }
    }

    class StrBuilderTokenizer extends StrTokenizer {
        StrBuilderTokenizer() {
        }

        @Override // org.apache.commons.lang3.text.StrTokenizer
        protected List<String> tokenize(char[] chars, int offset, int count) {
            return chars == null ? super.tokenize(StrBuilder.this.buffer, 0, StrBuilder.this.size()) : super.tokenize(chars, offset, count);
        }

        @Override // org.apache.commons.lang3.text.StrTokenizer
        public String getContent() {
            String str = super.getContent();
            if (str == null) {
                return StrBuilder.this.toString();
            }
            return str;
        }
    }

    class StrBuilderReader extends Reader {
        private int mark;
        private int pos;

        StrBuilderReader() {
        }

        @Override // java.io.Reader, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
        }

        @Override // java.io.Reader
        public int read() {
            if (!ready()) {
                return -1;
            }
            StrBuilder strBuilder = StrBuilder.this;
            int i = this.pos;
            this.pos = i + 1;
            return strBuilder.charAt(i);
        }

        @Override // java.io.Reader
        public int read(char[] b, int off, int len) {
            if (off < 0 || len < 0 || off > b.length || off + len > b.length || off + len < 0) {
                throw new IndexOutOfBoundsException();
            }
            if (len == 0) {
                return 0;
            }
            if (this.pos >= StrBuilder.this.size()) {
                return -1;
            }
            if (this.pos + len > StrBuilder.this.size()) {
                len = StrBuilder.this.size() - this.pos;
            }
            StrBuilder.this.getChars(this.pos, this.pos + len, b, off);
            this.pos += len;
            return len;
        }

        @Override // java.io.Reader
        public long skip(long n) {
            if (((long) this.pos) + n > StrBuilder.this.size()) {
                n = StrBuilder.this.size() - this.pos;
            }
            if (n < 0) {
                return 0L;
            }
            this.pos = (int) (((long) this.pos) + n);
            return n;
        }

        @Override // java.io.Reader
        public boolean ready() {
            return this.pos < StrBuilder.this.size();
        }

        @Override // java.io.Reader
        public boolean markSupported() {
            return true;
        }

        @Override // java.io.Reader
        public void mark(int readAheadLimit) {
            this.mark = this.pos;
        }

        @Override // java.io.Reader
        public void reset() {
            this.pos = this.mark;
        }
    }

    class StrBuilderWriter extends Writer {
        StrBuilderWriter() {
        }

        @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
        }

        @Override // java.io.Writer, java.io.Flushable
        public void flush() {
        }

        @Override // java.io.Writer
        public void write(int c) {
            StrBuilder.this.append((char) c);
        }

        @Override // java.io.Writer
        public void write(char[] cbuf) {
            StrBuilder.this.append(cbuf);
        }

        @Override // java.io.Writer
        public void write(char[] cbuf, int off, int len) {
            StrBuilder.this.append(cbuf, off, len);
        }

        @Override // java.io.Writer
        public void write(String str) {
            StrBuilder.this.append(str);
        }

        @Override // java.io.Writer
        public void write(String str, int off, int len) {
            StrBuilder.this.append(str, off, len);
        }
    }
}
