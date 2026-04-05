package com.ua.sdk.internal;

import com.ua.sdk.UaException;
import java.io.InputStream;

/* JADX INFO: loaded from: classes65.dex */
public interface JsonParser<T> {
    T parse(InputStream inputStream) throws UaException;
}
