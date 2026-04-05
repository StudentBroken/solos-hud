package com.ua.oss.org.apache.http.entity.mime;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
class HttpBrowserCompatibleMultipart extends AbstractMultipartForm {
    private final List<FormBodyPart> parts;

    public HttpBrowserCompatibleMultipart(String subType, Charset charset, String boundary, List<FormBodyPart> parts) {
        super(subType, charset, boundary);
        this.parts = parts;
    }

    @Override // com.ua.oss.org.apache.http.entity.mime.AbstractMultipartForm
    public List<FormBodyPart> getBodyParts() {
        return this.parts;
    }

    @Override // com.ua.oss.org.apache.http.entity.mime.AbstractMultipartForm
    protected void formatMultipartHeader(FormBodyPart part, OutputStream out) throws IOException {
        Header header = part.getHeader();
        MinimalField cd = header.getField(MIME.CONTENT_DISPOSITION);
        writeField(cd, this.charset, out);
        String filename = part.getBody().getFilename();
        if (filename != null) {
            MinimalField ct = header.getField("Content-Type");
            writeField(ct, this.charset, out);
        }
    }
}
