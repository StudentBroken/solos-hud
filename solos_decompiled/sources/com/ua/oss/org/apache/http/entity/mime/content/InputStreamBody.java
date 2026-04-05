package com.ua.oss.org.apache.http.entity.mime.content;

import com.ua.oss.org.apache.http.entity.ContentType;
import com.ua.sdk.internal.Precondition;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* JADX INFO: loaded from: classes65.dex */
public class InputStreamBody extends AbstractContentBody {
    private final String filename;
    private final InputStream in;

    public InputStreamBody(InputStream in, ContentType contentType, String filename) {
        super(contentType);
        Precondition.isNotNull(in, "Input stream");
        this.in = in;
        this.filename = filename;
    }

    public InputStream getInputStream() {
        return this.in;
    }

    @Override // com.ua.oss.org.apache.http.entity.mime.content.ContentBody
    public void writeTo(OutputStream out) throws IOException {
        Precondition.isNotNull(out, "Output stream");
        try {
            byte[] tmp = new byte[4096];
            while (true) {
                int l = this.in.read(tmp);
                if (l != -1) {
                    out.write(tmp, 0, l);
                } else {
                    out.flush();
                    return;
                }
            }
        } finally {
            this.in.close();
        }
    }

    @Override // com.ua.oss.org.apache.http.entity.mime.content.ContentDescriptor
    public String getTransferEncoding() {
        return "binary";
    }

    @Override // com.ua.oss.org.apache.http.entity.mime.content.ContentDescriptor
    public long getContentLength() {
        return -1L;
    }

    @Override // com.ua.oss.org.apache.http.entity.mime.content.ContentBody
    public String getFilename() {
        return this.filename;
    }
}
