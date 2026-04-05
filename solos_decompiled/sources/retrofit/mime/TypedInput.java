package retrofit.mime;

import java.io.IOException;
import java.io.InputStream;

/* JADX INFO: loaded from: classes53.dex */
public interface TypedInput {
    InputStream in() throws IOException;

    long length();

    String mimeType();
}
