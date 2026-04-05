package retrofit.converter;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import retrofit.mime.MimeUtil;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

/* JADX INFO: loaded from: classes53.dex */
public class GsonConverter implements Converter {
    private String charset;
    private final Gson gson;

    public GsonConverter(Gson gson) {
        this(gson, "UTF-8");
    }

    public GsonConverter(Gson gson, String charset) {
        this.gson = gson;
        this.charset = charset;
    }

    @Override // retrofit.converter.Converter
    public Object fromBody(TypedInput body, Type type) throws Throwable {
        InputStreamReader isr;
        String charset = this.charset;
        if (body.mimeType() != null) {
            charset = MimeUtil.parseCharset(body.mimeType(), charset);
        }
        InputStreamReader isr2 = null;
        try {
            try {
                isr = new InputStreamReader(body.in(), charset);
            } catch (Throwable th) {
                th = th;
            }
        } catch (JsonParseException e) {
            e = e;
        } catch (IOException e2) {
            e = e2;
        }
        try {
            Object objFromJson = this.gson.fromJson(isr, type);
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e3) {
                }
            }
            return objFromJson;
        } catch (JsonParseException e4) {
            e = e4;
            throw new ConversionException(e);
        } catch (IOException e5) {
            e = e5;
            throw new ConversionException(e);
        } catch (Throwable th2) {
            th = th2;
            isr2 = isr;
            if (isr2 != null) {
                try {
                    isr2.close();
                } catch (IOException e6) {
                }
            }
            throw th;
        }
    }

    @Override // retrofit.converter.Converter
    public TypedOutput toBody(Object object) {
        try {
            return new JsonTypedOutput(this.gson.toJson(object).getBytes(this.charset), this.charset);
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    private static class JsonTypedOutput implements TypedOutput {
        private final byte[] jsonBytes;
        private final String mimeType;

        JsonTypedOutput(byte[] jsonBytes, String encode) {
            this.jsonBytes = jsonBytes;
            this.mimeType = "application/json; charset=" + encode;
        }

        @Override // retrofit.mime.TypedOutput
        public String fileName() {
            return null;
        }

        @Override // retrofit.mime.TypedOutput
        public String mimeType() {
            return this.mimeType;
        }

        @Override // retrofit.mime.TypedOutput
        public long length() {
            return this.jsonBytes.length;
        }

        @Override // retrofit.mime.TypedOutput
        public void writeTo(OutputStream out) throws IOException {
            out.write(this.jsonBytes);
        }
    }
}
