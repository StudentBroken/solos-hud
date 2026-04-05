package com.ua.oss.org.apache.http.entity.mime;

import com.ua.oss.org.apache.http.entity.ContentType;
import com.ua.oss.org.apache.http.entity.mime.content.AbstractContentBody;
import com.ua.oss.org.apache.http.entity.mime.content.ContentBody;
import com.ua.sdk.internal.Precondition;

/* JADX INFO: loaded from: classes65.dex */
public class FormBodyPart {
    private final ContentBody body;
    private final Header header;
    private final String name;

    public FormBodyPart(String name, ContentBody body) {
        Precondition.isNotNull(name, "Name");
        Precondition.isNotNull(body, "Body");
        this.name = name;
        this.body = body;
        this.header = new Header();
        generateContentDisp(body);
        generateContentType(body);
        generateTransferEncoding(body);
    }

    public String getName() {
        return this.name;
    }

    public ContentBody getBody() {
        return this.body;
    }

    public Header getHeader() {
        return this.header;
    }

    public void addField(String name, String value) {
        Precondition.isNotNull(name, "Field name");
        this.header.addField(new MinimalField(name, value));
    }

    protected void generateContentDisp(ContentBody body) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("form-data; name=\"");
        buffer.append(getName());
        buffer.append("\"");
        if (body.getFilename() != null) {
            buffer.append("; filename=\"");
            buffer.append(body.getFilename());
            buffer.append("\"");
        }
        addField(MIME.CONTENT_DISPOSITION, buffer.toString());
    }

    protected void generateContentType(ContentBody body) {
        ContentType contentType;
        if (body instanceof AbstractContentBody) {
            contentType = ((AbstractContentBody) body).getContentType();
        } else {
            contentType = null;
        }
        if (contentType != null) {
            addField("Content-Type", contentType.toString());
            return;
        }
        StringBuilder buffer = new StringBuilder();
        buffer.append(body.getMimeType());
        if (body.getCharset() != null) {
            buffer.append("; charset=");
            buffer.append(body.getCharset());
        }
        addField("Content-Type", buffer.toString());
    }

    protected void generateTransferEncoding(ContentBody body) {
        addField(MIME.CONTENT_TRANSFER_ENC, body.getTransferEncoding());
    }
}
