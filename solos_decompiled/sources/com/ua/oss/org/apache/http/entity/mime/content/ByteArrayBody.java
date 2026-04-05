package com.ua.oss.org.apache.http.entity.mime.content;

import com.ua.oss.org.apache.http.entity.ContentType;
import com.ua.sdk.internal.Precondition;
import java.io.IOException;
import java.io.OutputStream;

/* JADX INFO: loaded from: classes65.dex */
public class ByteArrayBody extends AbstractContentBody {
    private final byte[] data;
    private final String filename;

    @Deprecated
    public ByteArrayBody(byte[] data, String mimeType, String filename) {
        this(data, ContentType.create(mimeType), filename);
    }

    public ByteArrayBody(byte[] data, ContentType contentType, String filename) {
        super(contentType);
        Precondition.isNotNull(data, "byte[]");
        this.data = data;
        this.filename = filename;
    }

    @Override // com.ua.oss.org.apache.http.entity.mime.content.ContentBody
    public String getFilename() {
        return this.filename;
    }

    @Override // com.ua.oss.org.apache.http.entity.mime.content.ContentBody
    public void writeTo(OutputStream out) throws IOException {
        out.write(this.data);
    }

    @Override // com.ua.oss.org.apache.http.entity.mime.content.AbstractContentBody, com.ua.oss.org.apache.http.entity.mime.content.ContentDescriptor
    public String getCharset() {
        return null;
    }

    @Override // com.ua.oss.org.apache.http.entity.mime.content.ContentDescriptor
    public String getTransferEncoding() {
        return "binary";
    }

    @Override // com.ua.oss.org.apache.http.entity.mime.content.ContentDescriptor
    public long getContentLength() {
        return this.data.length;
    }
}
