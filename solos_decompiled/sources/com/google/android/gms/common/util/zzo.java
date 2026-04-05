package com.google.android.gms.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes67.dex */
public final class zzo {
    private static final Pattern zzaJV = Pattern.compile("^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$");
    private static final Pattern zzaJW = Pattern.compile("^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");
    private static final Pattern zzaJX = Pattern.compile("^((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)$");

    private static String decode(String str, String str2) {
        if (str2 == null) {
            str2 = "ISO-8859-1";
        }
        try {
            return URLDecoder.decode(str, str2);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static Map<String, String> zza(URI uri, String str) {
        Map<String, String> mapEmptyMap = Collections.emptyMap();
        String rawQuery = uri.getRawQuery();
        if (rawQuery == null || rawQuery.length() <= 0) {
            return mapEmptyMap;
        }
        HashMap map = new HashMap();
        Scanner scanner = new Scanner(rawQuery);
        scanner.useDelimiter("&");
        while (scanner.hasNext()) {
            String[] strArrSplit = scanner.next().split("=");
            if (strArrSplit.length == 0 || strArrSplit.length > 2) {
                throw new IllegalArgumentException("bad parameter");
            }
            String strDecode = decode(strArrSplit[0], str);
            String strDecode2 = null;
            if (strArrSplit.length == 2) {
                strDecode2 = decode(strArrSplit[1], str);
            }
            map.put(strDecode, strDecode2);
        }
        return map;
    }
}
