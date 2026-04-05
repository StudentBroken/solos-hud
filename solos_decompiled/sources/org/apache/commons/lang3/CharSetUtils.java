package org.apache.commons.lang3;

/* JADX INFO: loaded from: classes51.dex */
public class CharSetUtils {
    public static String squeeze(String str, String... set) {
        if (!StringUtils.isEmpty(str) && !deepEmpty(set)) {
            CharSet chars = CharSet.getInstance(set);
            StringBuilder buffer = new StringBuilder(str.length());
            char[] chrs = str.toCharArray();
            int sz = chrs.length;
            char lastChar = ' ';
            for (int i = 0; i < sz; i++) {
                char ch = chrs[i];
                if (ch != lastChar || i == 0 || !chars.contains(ch)) {
                    buffer.append(ch);
                    lastChar = ch;
                }
            }
            return buffer.toString();
        }
        return str;
    }

    public static int count(String str, String... set) {
        if (StringUtils.isEmpty(str) || deepEmpty(set)) {
            return 0;
        }
        CharSet chars = CharSet.getInstance(set);
        int count = 0;
        char[] arr$ = str.toCharArray();
        for (char c : arr$) {
            if (chars.contains(c)) {
                count++;
            }
        }
        return count;
    }

    public static String keep(String str, String... set) {
        if (str == null) {
            return null;
        }
        if (str.length() == 0 || deepEmpty(set)) {
            return "";
        }
        return modify(str, set, true);
    }

    public static String delete(String str, String... set) {
        return (StringUtils.isEmpty(str) || deepEmpty(set)) ? str : modify(str, set, false);
    }

    private static String modify(String str, String[] set, boolean expect) {
        CharSet chars = CharSet.getInstance(set);
        StringBuilder buffer = new StringBuilder(str.length());
        char[] chrs = str.toCharArray();
        int sz = chrs.length;
        for (int i = 0; i < sz; i++) {
            if (chars.contains(chrs[i]) == expect) {
                buffer.append(chrs[i]);
            }
        }
        return buffer.toString();
    }

    private static boolean deepEmpty(String[] strings) {
        if (strings != null) {
            for (String s : strings) {
                if (StringUtils.isNotEmpty(s)) {
                    return false;
                }
            }
        }
        return true;
    }
}
