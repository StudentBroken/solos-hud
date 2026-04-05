package com.ua.sdk.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;

/* JADX INFO: loaded from: classes65.dex */
public class ProgressHttpEntity implements HttpEntity {
    private final HttpEntity httpEntity;

    public ProgressHttpEntity(HttpEntity httpEntity) {
        this.httpEntity = httpEntity;
    }

    @Override // org.apache.http.HttpEntity
    public boolean isRepeatable() {
        return this.httpEntity.isRepeatable();
    }

    @Override // org.apache.http.HttpEntity
    public boolean isChunked() {
        return this.httpEntity.isChunked();
    }

    @Override // org.apache.http.HttpEntity
    public long getContentLength() {
        return this.httpEntity.getContentLength();
    }

    @Override // org.apache.http.HttpEntity
    public Header getContentType() {
        return this.httpEntity.getContentType();
    }

    @Override // org.apache.http.HttpEntity
    public Header getContentEncoding() {
        return this.httpEntity.getContentEncoding();
    }

    @Override // org.apache.http.HttpEntity
    public InputStream getContent() throws IllegalStateException, IOException {
        return this.httpEntity.getContent();
    }

    @Override // org.apache.http.HttpEntity
    public void writeTo(OutputStream outputStream) throws IOException {
        this.httpEntity.writeTo(outputStream);
    }

    @Override // org.apache.http.HttpEntity
    public boolean isStreaming() {
        return this.httpEntity.isStreaming();
    }

    @Override // org.apache.http.HttpEntity
    public void consumeContent() throws IOException {
        this.httpEntity.consumeContent();
    }
}
