package com.kopin.solos.storage.field;

import android.database.Cursor;

/* JADX INFO: loaded from: classes54.dex */
public class FieldHelper {
    private static int defaultValue = 0;

    public static class FieldNoDataException extends Exception {
    }

    public static void setDefault(int value) {
        defaultValue = value;
    }

    public static int getInt(Cursor cursor, String name) {
        int pos = cursor.getColumnIndex(name);
        return (pos == -1 || cursor.isNull(pos)) ? defaultValue : cursor.getInt(pos);
    }

    public static long getLong(Cursor cursor, String name) {
        int pos = cursor.getColumnIndex(name);
        return pos != -1 ? cursor.getLong(pos) : defaultValue;
    }

    public static float getFloat(Cursor cursor, String name) {
        int pos = cursor.getColumnIndex(name);
        return pos != -1 ? cursor.getFloat(pos) : defaultValue;
    }

    public static double getDouble(Cursor cursor, String name) {
        int pos = cursor.getColumnIndex(name);
        return (pos == -1 || cursor.isNull(pos)) ? defaultValue : cursor.getFloat(pos);
    }

    public static String getString(Cursor cursor, String name) {
        int pos = cursor.getColumnIndex(name);
        return pos != -1 ? cursor.getString(pos) : "";
    }
}
