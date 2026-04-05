package retrofit.mime;

import java.io.IOException;
import java.io.OutputStream;

/* JADX INFO: loaded from: classes53.dex */
public interface TypedOutput {
    String fileName();

    long length();

    String mimeType();

    void writeTo(OutputStream outputStream) throws IOException;
}
