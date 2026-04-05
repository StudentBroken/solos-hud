package com.ua.oss.org.apache.http.entity.mime;

import com.digits.sdk.vcard.VCardBuilder;
import com.ua.oss.org.apache.http.entity.mime.content.ContentBody;
import com.ua.sdk.internal.Precondition;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.List;
import org.apache.http.util.ByteArrayBuffer;

/* JADX INFO: loaded from: classes65.dex */
abstract class AbstractMultipartForm {
    private final String boundary;
    protected final Charset charset;
    private final String subType;
    private static final ByteArrayBuffer FIELD_SEP = encode(MIME.DEFAULT_CHARSET, ": ");
    private static final ByteArrayBuffer CR_LF = encode(MIME.DEFAULT_CHARSET, VCardBuilder.VCARD_END_OF_LINE);
    private static final ByteArrayBuffer TWO_DASHES = encode(MIME.DEFAULT_CHARSET, "--");

    protected abstract void formatMultipartHeader(FormBodyPart formBodyPart, OutputStream outputStream) throws IOException;

    public abstract List<FormBodyPart> getBodyParts();

    private static ByteArrayBuffer encode(Charset charset, String string) {
        ByteBuffer encoded = charset.encode(CharBuffer.wrap(string));
        ByteArrayBuffer bab = new ByteArrayBuffer(encoded.remaining());
        bab.append(encoded.array(), encoded.position(), encoded.remaining());
        return bab;
    }

    private static void writeBytes(ByteArrayBuffer b, OutputStream out) throws IOException {
        out.write(b.buffer(), 0, b.length());
    }

    private static void writeBytes(String s, Charset charset, OutputStream out) throws IOException {
        ByteArrayBuffer b = encode(charset, s);
        writeBytes(b, out);
    }

    private static void writeBytes(String s, OutputStream out) throws IOException {
        ByteArrayBuffer b = encode(MIME.DEFAULT_CHARSET, s);
        writeBytes(b, out);
    }

    protected static void writeField(MinimalField field, OutputStream out) throws IOException {
        writeBytes(field.getName(), out);
        writeBytes(FIELD_SEP, out);
        writeBytes(field.getBody(), out);
        writeBytes(CR_LF, out);
    }

    protected static void writeField(MinimalField field, Charset charset, OutputStream out) throws IOException {
        writeBytes(field.getName(), charset, out);
        writeBytes(FIELD_SEP, out);
        writeBytes(field.getBody(), charset, out);
        writeBytes(CR_LF, out);
    }

    public AbstractMultipartForm(String subType, Charset charset, String boundary) {
        Precondition.isNotNull(subType, "Multipart subtype");
        Precondition.isNotNull(boundary, "Multipart boundary");
        this.subType = subType;
        this.charset = charset == null ? MIME.DEFAULT_CHARSET : charset;
        this.boundary = boundary;
    }

    public Charset getCharset() {
        return this.charset;
    }

    public String getBoundary() {
        return this.boundary;
    }

    void doWriteTo(OutputStream out, boolean writeContent) throws IOException {
        ByteArrayBuffer boundary = encode(this.charset, getBoundary());
        for (FormBodyPart part : getBodyParts()) {
            writeBytes(TWO_DASHES, out);
            writeBytes(boundary, out);
            writeBytes(CR_LF, out);
            formatMultipartHeader(part, out);
            writeBytes(CR_LF, out);
            if (writeContent) {
                part.getBody().writeTo(out);
            }
            writeBytes(CR_LF, out);
        }
        writeBytes(TWO_DASHES, out);
        writeBytes(boundary, out);
        writeBytes(TWO_DASHES, out);
        writeBytes(CR_LF, out);
    }

    public void writeTo(OutputStream out) throws IOException {
        doWriteTo(out, true);
    }

    public long getTotalLength() {
        long contentLen = 0;
        for (FormBodyPart part : getBodyParts()) {
            ContentBody body = part.getBody();
            long len = body.getContentLength();
            if (len < 0) {
                return -1L;
            }
            contentLen += len;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            doWriteTo(out, false);
            byte[] extra = out.toByteArray();
            return ((long) extra.length) + contentLen;
        } catch (IOException e) {
            return -1L;
        }
    }
}
