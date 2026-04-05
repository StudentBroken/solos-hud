package com.ua.oss.org.apache.http.entity.mime;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicHeader;

/* JADX INFO: loaded from: classes65.dex */
class MultipartFormEntity implements HttpEntity {
    private final long contentLength;
    private final org.apache.http.Header contentType;
    private final AbstractMultipartForm multipart;

    MultipartFormEntity(AbstractMultipartForm multipart, String contentType, long contentLength) {
        this.multipart = multipart;
        this.contentType = new BasicHeader("Content-Type", contentType);
        this.contentLength = contentLength;
    }

    @Override // org.apache.http.HttpEntity
    public boolean isRepeatable() {
        return this.contentLength != -1;
    }

    @Override // org.apache.http.HttpEntity
    public boolean isChunked() {
        return !isRepeatable();
    }

    @Override // org.apache.http.HttpEntity
    public boolean isStreaming() {
        return !isRepeatable();
    }

    @Override // org.apache.http.HttpEntity
    public long getContentLength() {
        return this.contentLength;
    }

    @Override // org.apache.http.HttpEntity
    public org.apache.http.Header getContentType() {
        return this.contentType;
    }

    @Override // org.apache.http.HttpEntity
    public org.apache.http.Header getContentEncoding() {
        return null;
    }

    @Override // org.apache.http.HttpEntity
    public void consumeContent() throws UnsupportedOperationException, IOException {
        if (isStreaming()) {
            throw new UnsupportedOperationException("Streaming entity does not implement #consumeContent()");
        }
    }

    @Override // org.apache.http.HttpEntity
    public InputStream getContent() throws IOException {
        throw new UnsupportedOperationException("Multipart form entity does not implement #getContent()");
    }

    @Override // org.apache.http.HttpEntity
    public void writeTo(OutputStream outstream) throws IOException {
        this.multipart.writeTo(outstream);
    }
}
