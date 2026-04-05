package com.ua.oss.org.apache.http.entity.mime.content;

import com.ua.oss.org.apache.http.Consts;
import com.ua.oss.org.apache.http.entity.ContentType;
import com.ua.oss.org.apache.http.entity.mime.MIME;
import com.ua.sdk.internal.Precondition;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

/* JADX INFO: loaded from: classes65.dex */
public class StringBody extends AbstractContentBody {
    private final byte[] content;

    @Deprecated
    public static StringBody create(String text, String mimeType, Charset charset) throws IllegalArgumentException {
        try {
            return new StringBody(text, mimeType, charset);
        } catch (UnsupportedEncodingException ex) {
            throw new IllegalArgumentException("Charset " + charset + " is not supported", ex);
        }
    }

    @Deprecated
    public static StringBody create(String text, Charset charset) throws IllegalArgumentException {
        return create(text, null, charset);
    }

    @Deprecated
    public static StringBody create(String text) throws IllegalArgumentException {
        return create(text, null, null);
    }

    @Deprecated
    public StringBody(String text, String mimeType, Charset charset) throws UnsupportedEncodingException {
        this(text, ContentType.create(mimeType, charset));
    }

    public StringBody(String text, ContentType contentType) {
        super(contentType);
        Precondition.isNotNull(text, "Text");
        Charset charset = contentType.getCharset();
        String csname = charset != null ? charset.name() : Consts.ASCII.name();
        try {
            this.content = text.getBytes(csname);
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedCharsetException(csname);
        }
    }

    public Reader getReader() {
        Charset charset = getContentType().getCharset();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.content);
        if (charset == null) {
            charset = Consts.ASCII;
        }
        return new InputStreamReader(byteArrayInputStream, charset);
    }

    @Override // com.ua.oss.org.apache.http.entity.mime.content.ContentBody
    public void writeTo(OutputStream out) throws IOException {
        Precondition.isNotNull(out, "Output stream");
        InputStream in = new ByteArrayInputStream(this.content);
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
    }

    @Override // com.ua.oss.org.apache.http.entity.mime.content.ContentDescriptor
    public String getTransferEncoding() {
        return MIME.ENC_8BIT;
    }

    @Override // com.ua.oss.org.apache.http.entity.mime.content.ContentDescriptor
    public long getContentLength() {
        return this.content.length;
    }

    @Override // com.ua.oss.org.apache.http.entity.mime.content.ContentBody
    public String getFilename() {
        return null;
    }
}
