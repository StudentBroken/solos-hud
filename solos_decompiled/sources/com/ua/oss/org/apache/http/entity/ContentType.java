package com.ua.oss.org.apache.http.entity;

import com.ua.oss.org.apache.http.Consts;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Locale;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicHeaderValueParser;
import org.apache.http.message.HeaderValueParser;

/* JADX INFO: loaded from: classes65.dex */
public final class ContentType {
    private final Charset charset;
    private final String mimeType;
    public static final ContentType TEXT_PLAIN = create("text/plain", Consts.ISO_8859_1);
    public static final ContentType DEFAULT_TEXT = TEXT_PLAIN;

    ContentType(String mimeType, Charset charset) {
        this.mimeType = mimeType;
        this.charset = charset;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public Charset getCharset() {
        return this.charset;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(this.mimeType);
        if (this.charset != null) {
            buf.append("; charset=");
            buf.append(this.charset.name());
        }
        return buf.toString();
    }

    private static boolean valid(String s) {
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch == '\"' || ch == ',' || ch == ';') {
                return false;
            }
        }
        return true;
    }

    public static ContentType create(String mimeType, Charset charset) {
        if (mimeType == null) {
            throw new IllegalArgumentException("MIME type may not be null");
        }
        String type = mimeType.trim().toLowerCase(Locale.US);
        if (type.length() == 0) {
            throw new IllegalArgumentException("MIME type may not be empty");
        }
        if (!valid(type)) {
            throw new IllegalArgumentException("MIME type may not contain reserved characters");
        }
        return new ContentType(type, charset);
    }

    public static ContentType create(String mimeType) {
        return new ContentType(mimeType, (Charset) null);
    }

    public static ContentType create(String mimeType, String charset) throws UnsupportedCharsetException {
        return create(mimeType, charset != null ? Charset.forName(charset) : null);
    }

    private static ContentType create(HeaderElement helem) {
        String mimeType = helem.getName();
        String charset = null;
        NameValuePair param = helem.getParameterByName(HttpRequest.PARAM_CHARSET);
        if (param != null) {
            charset = param.getValue();
        }
        return create(mimeType, charset);
    }

    public static ContentType parse(String s) throws ParseException, UnsupportedCharsetException {
        if (s == null) {
            throw new IllegalArgumentException("Content type may not be null");
        }
        HeaderElement[] elements = BasicHeaderValueParser.parseElements(s, (HeaderValueParser) null);
        if (elements.length > 0) {
            return create(elements[0]);
        }
        throw new ParseException("Invalid content type: " + s);
    }

    public static ContentType get(HttpEntity entity) throws ParseException, UnsupportedCharsetException {
        Header header;
        if (entity == null || (header = entity.getContentType()) == null) {
            return null;
        }
        HeaderElement[] elements = header.getElements();
        if (elements.length > 0) {
            return create(elements[0]);
        }
        return null;
    }
}
