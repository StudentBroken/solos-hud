package retrofit.mime;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

/* JADX INFO: loaded from: classes53.dex */
public class TypedByteArray implements TypedInput, TypedOutput {
    private final byte[] bytes;
    private final String mimeType;

    public TypedByteArray(String mimeType, byte[] bytes) {
        mimeType = mimeType == null ? "application/unknown" : mimeType;
        if (bytes == null) {
            throw new NullPointerException("bytes");
        }
        this.mimeType = mimeType;
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return this.bytes;
    }

    @Override // retrofit.mime.TypedOutput
    public String fileName() {
        return null;
    }

    @Override // retrofit.mime.TypedInput
    public String mimeType() {
        return this.mimeType;
    }

    @Override // retrofit.mime.TypedInput
    public long length() {
        return this.bytes.length;
    }

    @Override // retrofit.mime.TypedOutput
    public void writeTo(OutputStream out) throws IOException {
        out.write(this.bytes);
    }

    @Override // retrofit.mime.TypedInput
    public InputStream in() throws IOException {
        return new ByteArrayInputStream(this.bytes);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TypedByteArray that = (TypedByteArray) o;
        return Arrays.equals(this.bytes, that.bytes) && this.mimeType.equals(that.mimeType);
    }

    public int hashCode() {
        int result = this.mimeType.hashCode();
        return (result * 31) + Arrays.hashCode(this.bytes);
    }

    public String toString() {
        return "TypedByteArray[length=" + length() + "]";
    }
}
