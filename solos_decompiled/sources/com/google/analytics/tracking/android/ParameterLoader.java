package com.google.analytics.tracking.android;

/* JADX INFO: loaded from: classes49.dex */
interface ParameterLoader {
    boolean getBoolean(String str);

    Double getDoubleFromString(String str);

    int getInt(String str, int i);

    String getString(String str);

    boolean isBooleanKeyPresent(String str);

    void setResourcePackageName(String str);
}
