package com.ua.oss.org.apache.http.entity.mime.content;

import java.io.IOException;
import java.io.OutputStream;

/* JADX INFO: loaded from: classes65.dex */
public interface ContentBody extends ContentDescriptor {
    String getFilename();

    void writeTo(OutputStream outputStream) throws IOException;
}
