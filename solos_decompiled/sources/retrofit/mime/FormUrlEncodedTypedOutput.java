package retrofit.mime;

import com.google.firebase.analytics.FirebaseAnalytics;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

/* JADX INFO: loaded from: classes53.dex */
public final class FormUrlEncodedTypedOutput implements TypedOutput {
    final ByteArrayOutputStream content = new ByteArrayOutputStream();

    public void addField(String name, String value) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        if (value == null) {
            throw new NullPointerException(FirebaseAnalytics.Param.VALUE);
        }
        if (this.content.size() > 0) {
            this.content.write(38);
        }
        try {
            String name2 = URLEncoder.encode(name, "UTF-8");
            String value2 = URLEncoder.encode(value, "UTF-8");
            this.content.write(name2.getBytes("UTF-8"));
            this.content.write(61);
            this.content.write(value2.getBytes("UTF-8"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override // retrofit.mime.TypedOutput
    public String fileName() {
        return null;
    }

    @Override // retrofit.mime.TypedOutput
    public String mimeType() {
        return "application/x-www-form-urlencoded; charset=UTF-8";
    }

    @Override // retrofit.mime.TypedOutput
    public long length() {
        return this.content.size();
    }

    @Override // retrofit.mime.TypedOutput
    public void writeTo(OutputStream out) throws IOException {
        out.write(this.content.toByteArray());
    }
}
