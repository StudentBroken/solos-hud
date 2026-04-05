package org.apache.commons.lang3.math;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.apache.commons.lang3.StringUtils;

/* JADX INFO: loaded from: classes51.dex */
public class NumberUtils {
    public static final Long LONG_ZERO = 0L;
    public static final Long LONG_ONE = 1L;
    public static final Long LONG_MINUS_ONE = -1L;
    public static final Integer INTEGER_ZERO = 0;
    public static final Integer INTEGER_ONE = 1;
    public static final Integer INTEGER_MINUS_ONE = -1;
    public static final Short SHORT_ZERO = 0;
    public static final Short SHORT_ONE = 1;
    public static final Short SHORT_MINUS_ONE = -1;
    public static final Byte BYTE_ZERO = (byte) 0;
    public static final Byte BYTE_ONE = (byte) 1;
    public static final Byte BYTE_MINUS_ONE = (byte) -1;
    public static final Double DOUBLE_ZERO = Double.valueOf(0.0d);
    public static final Double DOUBLE_ONE = Double.valueOf(1.0d);
    public static final Double DOUBLE_MINUS_ONE = Double.valueOf(-1.0d);
    public static final Float FLOAT_ZERO = Float.valueOf(0.0f);
    public static final Float FLOAT_ONE = Float.valueOf(1.0f);
    public static final Float FLOAT_MINUS_ONE = Float.valueOf(-1.0f);

    public static int toInt(String str) {
        return toInt(str, 0);
    }

    public static int toInt(String str, int defaultValue) {
        if (str != null) {
            try {
                return Integer.parseInt(str);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    public static long toLong(String str) {
        return toLong(str, 0L);
    }

    public static long toLong(String str, long defaultValue) {
        if (str != null) {
            try {
                return Long.parseLong(str);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    public static float toFloat(String str) {
        return toFloat(str, 0.0f);
    }

    public static float toFloat(String str, float defaultValue) {
        if (str != null) {
            try {
                return Float.parseFloat(str);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    public static double toDouble(String str) {
        return toDouble(str, 0.0d);
    }

    public static double toDouble(String str, double defaultValue) {
        if (str != null) {
            try {
                return Double.parseDouble(str);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    public static byte toByte(String str) {
        return toByte(str, (byte) 0);
    }

    public static byte toByte(String str, byte defaultValue) {
        if (str != null) {
            try {
                return Byte.parseByte(str);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    public static short toShort(String str) {
        return toShort(str, (short) 0);
    }

    public static short toShort(String str, short defaultValue) {
        if (str != null) {
            try {
                return Short.parseShort(str);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:83:0x0181 A[Catch: NumberFormatException -> 0x0191, TRY_LEAVE, TryCatch #0 {NumberFormatException -> 0x0191, blocks: (B:81:0x0177, B:83:0x0181), top: B:133:0x0177 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.lang.Number createNumber(java.lang.String r14) throws java.lang.NumberFormatException {
        /*
            Method dump skipped, instruction units count: 558
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.math.NumberUtils.createNumber(java.lang.String):java.lang.Number");
    }

    private static boolean isAllZeros(String str) {
        if (str == null) {
            return true;
        }
        for (int i = str.length() - 1; i >= 0; i--) {
            if (str.charAt(i) != '0') {
                return false;
            }
        }
        return str.length() > 0;
    }

    public static Float createFloat(String str) {
        if (str == null) {
            return null;
        }
        return Float.valueOf(str);
    }

    public static Double createDouble(String str) {
        if (str == null) {
            return null;
        }
        return Double.valueOf(str);
    }

    public static Integer createInteger(String str) {
        if (str == null) {
            return null;
        }
        return Integer.decode(str);
    }

    public static Long createLong(String str) {
        if (str == null) {
            return null;
        }
        return Long.decode(str);
    }

    public static BigInteger createBigInteger(String str) {
        if (str == null) {
            return null;
        }
        return new BigInteger(str);
    }

    public static BigDecimal createBigDecimal(String str) {
        if (str == null) {
            return null;
        }
        if (StringUtils.isBlank(str)) {
            throw new NumberFormatException("A blank string is not a valid number");
        }
        return new BigDecimal(str);
    }

    public static long min(long[] array) {
        if (array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        }
        if (array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        }
        long min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }

    public static int min(int[] array) {
        if (array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        }
        if (array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        }
        int min = array[0];
        for (int j = 1; j < array.length; j++) {
            if (array[j] < min) {
                min = array[j];
            }
        }
        return min;
    }

    public static short min(short[] array) {
        if (array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        }
        if (array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        }
        short min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }

    public static byte min(byte[] array) {
        if (array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        }
        if (array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        }
        byte min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }

    public static double min(double[] array) {
        if (array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        }
        if (array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        }
        double min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (!Double.isNaN(array[i])) {
                if (array[i] < min) {
                    min = array[i];
                }
            } else {
                return Double.NaN;
            }
        }
        return min;
    }

    public static float min(float[] array) {
        if (array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        }
        if (array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        }
        float min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (!Float.isNaN(array[i])) {
                if (array[i] < min) {
                    min = array[i];
                }
            } else {
                return Float.NaN;
            }
        }
        return min;
    }

    public static long max(long[] array) {
        if (array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        }
        if (array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        }
        long max = array[0];
        for (int j = 1; j < array.length; j++) {
            if (array[j] > max) {
                max = array[j];
            }
        }
        return max;
    }

    public static int max(int[] array) {
        if (array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        }
        if (array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        }
        int max = array[0];
        for (int j = 1; j < array.length; j++) {
            if (array[j] > max) {
                max = array[j];
            }
        }
        return max;
    }

    public static short max(short[] array) {
        if (array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        }
        if (array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        }
        short max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

    public static byte max(byte[] array) {
        if (array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        }
        if (array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        }
        byte max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

    public static double max(double[] array) {
        if (array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        }
        if (array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        }
        double max = array[0];
        for (int j = 1; j < array.length; j++) {
            if (!Double.isNaN(array[j])) {
                if (array[j] > max) {
                    max = array[j];
                }
            } else {
                return Double.NaN;
            }
        }
        return max;
    }

    public static float max(float[] array) {
        if (array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        }
        if (array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        }
        float max = array[0];
        for (int j = 1; j < array.length; j++) {
            if (!Float.isNaN(array[j])) {
                if (array[j] > max) {
                    max = array[j];
                }
            } else {
                return Float.NaN;
            }
        }
        return max;
    }

    public static long min(long a, long b, long c) {
        if (b < a) {
            a = b;
        }
        if (c < a) {
            return c;
        }
        return a;
    }

    public static int min(int a, int b, int c) {
        if (b < a) {
            a = b;
        }
        if (c < a) {
            return c;
        }
        return a;
    }

    public static short min(short a, short b, short c) {
        if (b < a) {
            a = b;
        }
        if (c < a) {
            return c;
        }
        return a;
    }

    public static byte min(byte a, byte b, byte c) {
        if (b < a) {
            a = b;
        }
        if (c < a) {
            return c;
        }
        return a;
    }

    public static double min(double a, double b, double c) {
        return Math.min(Math.min(a, b), c);
    }

    public static float min(float a, float b, float c) {
        return Math.min(Math.min(a, b), c);
    }

    public static long max(long a, long b, long c) {
        if (b > a) {
            a = b;
        }
        if (c > a) {
            return c;
        }
        return a;
    }

    public static int max(int a, int b, int c) {
        if (b > a) {
            a = b;
        }
        if (c > a) {
            return c;
        }
        return a;
    }

    public static short max(short a, short b, short c) {
        if (b > a) {
            a = b;
        }
        if (c > a) {
            return c;
        }
        return a;
    }

    public static byte max(byte a, byte b, byte c) {
        if (b > a) {
            a = b;
        }
        if (c > a) {
            return c;
        }
        return a;
    }

    public static double max(double a, double b, double c) {
        return Math.max(Math.max(a, b), c);
    }

    public static float max(float a, float b, float c) {
        return Math.max(Math.max(a, b), c);
    }

    public static boolean isDigits(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:101:0x0105, code lost:
    
        r8 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:123:?, code lost:
    
        return true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:124:?, code lost:
    
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:125:?, code lost:
    
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:126:?, code lost:
    
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:127:?, code lost:
    
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:128:?, code lost:
    
        return r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:129:?, code lost:
    
        return r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:130:?, code lost:
    
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:131:?, code lost:
    
        return r8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:132:?, code lost:
    
        return r8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x00a4, code lost:
    
        if (r5 >= r1.length) goto L98;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x00a8, code lost:
    
        if (r1[r5] < '0') goto L70;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x00ac, code lost:
    
        if (r1[r5] > '9') goto L70;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x00b5, code lost:
    
        if (r1[r5] == 'e') goto L124;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x00bb, code lost:
    
        if (r1[r5] == 'E') goto L125;
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x00c1, code lost:
    
        if (r1[r5] != '.') goto L79;
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x00c3, code lost:
    
        if (r3 != false) goto L126;
     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x00c5, code lost:
    
        if (r4 != false) goto L127;
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x00ca, code lost:
    
        if (r0 != false) goto L89;
     */
    /* JADX WARN: Code restructure failed: missing block: B:81:0x00d0, code lost:
    
        if (r1[r5] == 'd') goto L88;
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x00d6, code lost:
    
        if (r1[r5] == 'D') goto L88;
     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x00dc, code lost:
    
        if (r1[r5] == 'f') goto L88;
     */
    /* JADX WARN: Code restructure failed: missing block: B:87:0x00e2, code lost:
    
        if (r1[r5] != 'F') goto L89;
     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x00eb, code lost:
    
        if (r1[r5] == 'l') goto L93;
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x00f1, code lost:
    
        if (r1[r5] != 'L') goto L130;
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x00f3, code lost:
    
        if (r2 == false) goto L97;
     */
    /* JADX WARN: Code restructure failed: missing block: B:94:0x00f5, code lost:
    
        if (r4 != false) goto L97;
     */
    /* JADX WARN: Code restructure failed: missing block: B:95:0x00f7, code lost:
    
        if (r3 != false) goto L97;
     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x00fc, code lost:
    
        r8 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x00fe, code lost:
    
        if (r0 != false) goto L101;
     */
    /* JADX WARN: Code restructure failed: missing block: B:99:0x0100, code lost:
    
        if (r2 == false) goto L101;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static boolean isNumber(java.lang.String r15) {
        /*
            Method dump skipped, instruction units count: 263
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.math.NumberUtils.isNumber(java.lang.String):boolean");
    }
}
