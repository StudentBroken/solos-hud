package retrofit.mime;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* JADX INFO: loaded from: classes53.dex */
public class TypedFile implements TypedInput, TypedOutput {
    private static final int BUFFER_SIZE = 4096;
    private final File file;
    private final String mimeType;

    public TypedFile(String mimeType, File file) {
        if (mimeType == null) {
            throw new NullPointerException("mimeType");
        }
        if (file == null) {
            throw new NullPointerException("file");
        }
        this.mimeType = mimeType;
        this.file = file;
    }

    public File file() {
        return this.file;
    }

    @Override // retrofit.mime.TypedInput
    public String mimeType() {
        return this.mimeType;
    }

    @Override // retrofit.mime.TypedInput
    public long length() {
        return this.file.length();
    }

    @Override // retrofit.mime.TypedOutput
    public String fileName() {
        return this.file.getName();
    }

    @Override // retrofit.mime.TypedInput
    public InputStream in() throws IOException {
        return new FileInputStream(this.file);
    }

    @Override // retrofit.mime.TypedOutput
    public void writeTo(OutputStream out) throws IOException {
        byte[] buffer = new byte[4096];
        FileInputStream in = new FileInputStream(this.file);
        while (true) {
            try {
                int read = in.read(buffer);
                if (read != -1) {
                    out.write(buffer, 0, read);
                } else {
                    return;
                }
            } finally {
                in.close();
            }
        }
    }

    public void moveTo(TypedFile destination) throws IOException {
        if (!mimeType().equals(destination.mimeType())) {
            throw new IOException("Type mismatch.");
        }
        if (!this.file.renameTo(destination.file())) {
            throw new IOException("Rename failed!");
        }
    }

    public String toString() {
        return this.file.getAbsolutePath() + " (" + mimeType() + ")";
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof TypedFile) {
            TypedFile rhs = (TypedFile) o;
            return this.file.equals(rhs.file);
        }
        return false;
    }

    public int hashCode() {
        return this.file.hashCode();
    }
}
