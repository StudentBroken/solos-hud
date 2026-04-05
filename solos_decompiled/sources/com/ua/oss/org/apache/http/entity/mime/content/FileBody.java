package com.ua.oss.org.apache.http.entity.mime.content;

import com.ua.oss.org.apache.http.entity.ContentType;
import com.ua.sdk.internal.Precondition;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* JADX INFO: loaded from: classes65.dex */
public class FileBody extends AbstractContentBody {
    private final File file;
    private final String filename;

    @Deprecated
    public FileBody(File file, String filename, String mimeType, String charset) {
        this(file, ContentType.create(mimeType, charset), filename);
    }

    public FileBody(File file, ContentType contentType, String filename) {
        super(contentType);
        Precondition.isNotNull(file, "File");
        this.file = file;
        this.filename = filename;
    }

    public InputStream getInputStream() throws IOException {
        return new FileInputStream(this.file);
    }

    @Override // com.ua.oss.org.apache.http.entity.mime.content.ContentBody
    public void writeTo(OutputStream out) throws IOException {
        Precondition.isNotNull(out, "Output stream");
        InputStream in = new FileInputStream(this.file);
        try {
            byte[] tmp = new byte[4096];
            while (true) {
                int l = in.read(tmp);
                if (l != -1) {
                    out.write(tmp, 0, l);
                } else {
                    out.flush();
                    return;
                }
            }
        } finally {
            in.close();
        }
    }

    @Override // com.ua.oss.org.apache.http.entity.mime.content.ContentDescriptor
    public String getTransferEncoding() {
        return "binary";
    }

    @Override // com.ua.oss.org.apache.http.entity.mime.content.ContentDescriptor
    public long getContentLength() {
        return this.file.length();
    }

    @Override // com.ua.oss.org.apache.http.entity.mime.content.ContentBody
    public String getFilename() {
        return this.filename;
    }

    public File getFile() {
        return this.file;
    }
}
