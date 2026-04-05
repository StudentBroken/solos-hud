package retrofit.mime;

import com.digits.sdk.vcard.VCardBuilder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/* JADX INFO: loaded from: classes53.dex */
public final class MultipartTypedOutput implements TypedOutput {
    public static final String DEFAULT_TRANSFER_ENCODING = "binary";
    private final String boundary;
    private final byte[] footer;
    private long length;
    private final List<MimePart> mimeParts;

    private static final class MimePart {
        private final TypedOutput body;
        private final String boundary;
        private boolean isBuilt;
        private final boolean isFirst;
        private final String name;
        private byte[] partBoundary;
        private byte[] partHeader;
        private final String transferEncoding;

        public MimePart(String name, String transferEncoding, TypedOutput body, String boundary, boolean isFirst) {
            this.name = name;
            this.transferEncoding = transferEncoding;
            this.body = body;
            this.isFirst = isFirst;
            this.boundary = boundary;
        }

        public void writeTo(OutputStream out) throws IOException {
            build();
            out.write(this.partBoundary);
            out.write(this.partHeader);
            this.body.writeTo(out);
        }

        public long size() {
            build();
            if (this.body.length() > -1) {
                return this.body.length() + ((long) this.partBoundary.length) + ((long) this.partHeader.length);
            }
            return -1L;
        }

        private void build() {
            if (!this.isBuilt) {
                this.partBoundary = MultipartTypedOutput.buildBoundary(this.boundary, this.isFirst, false);
                this.partHeader = MultipartTypedOutput.buildHeader(this.name, this.transferEncoding, this.body);
                this.isBuilt = true;
            }
        }
    }

    public MultipartTypedOutput() {
        this(UUID.randomUUID().toString());
    }

    MultipartTypedOutput(String boundary) {
        this.mimeParts = new LinkedList();
        this.boundary = boundary;
        this.footer = buildBoundary(boundary, false, true);
        this.length = this.footer.length;
    }

    List<byte[]> getParts() throws IOException {
        List<byte[]> parts = new ArrayList<>(this.mimeParts.size());
        for (MimePart part : this.mimeParts) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            part.writeTo(bos);
            parts.add(bos.toByteArray());
        }
        return parts;
    }

    public void addPart(String name, TypedOutput body) {
        addPart(name, "binary", body);
    }

    public void addPart(String name, String transferEncoding, TypedOutput body) {
        if (name == null) {
            throw new NullPointerException("Part name must not be null.");
        }
        if (transferEncoding == null) {
            throw new NullPointerException("Transfer encoding must not be null.");
        }
        if (body == null) {
            throw new NullPointerException("Part body must not be null.");
        }
        MimePart part = new MimePart(name, transferEncoding, body, this.boundary, this.mimeParts.isEmpty());
        this.mimeParts.add(part);
        long size = part.size();
        if (size == -1) {
            this.length = -1L;
        } else if (this.length != -1) {
            this.length += size;
        }
    }

    public int getPartCount() {
        return this.mimeParts.size();
    }

    @Override // retrofit.mime.TypedOutput
    public String fileName() {
        return null;
    }

    @Override // retrofit.mime.TypedOutput
    public String mimeType() {
        return "multipart/form-data; boundary=" + this.boundary;
    }

    @Override // retrofit.mime.TypedOutput
    public long length() {
        return this.length;
    }

    @Override // retrofit.mime.TypedOutput
    public void writeTo(OutputStream out) throws IOException {
        for (MimePart part : this.mimeParts) {
            part.writeTo(out);
        }
        out.write(this.footer);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static byte[] buildBoundary(String boundary, boolean first, boolean last) {
        try {
            StringBuilder sb = new StringBuilder(boundary.length() + 8);
            if (!first) {
                sb.append(VCardBuilder.VCARD_END_OF_LINE);
            }
            sb.append("--");
            sb.append(boundary);
            if (last) {
                sb.append("--");
            }
            sb.append(VCardBuilder.VCARD_END_OF_LINE);
            return sb.toString().getBytes("UTF-8");
        } catch (IOException ex) {
            throw new RuntimeException("Unable to write multipart boundary", ex);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static byte[] buildHeader(String name, String transferEncoding, TypedOutput value) {
        try {
            StringBuilder headers = new StringBuilder(128);
            headers.append("Content-Disposition: form-data; name=\"");
            headers.append(name);
            String fileName = value.fileName();
            if (fileName != null) {
                headers.append("\"; filename=\"");
                headers.append(fileName);
            }
            headers.append("\"\r\nContent-Type: ");
            headers.append(value.mimeType());
            long length = value.length();
            if (length != -1) {
                headers.append("\r\nContent-Length: ").append(length);
            }
            headers.append("\r\nContent-Transfer-Encoding: ");
            headers.append(transferEncoding);
            headers.append("\r\n\r\n");
            return headers.toString().getBytes("UTF-8");
        } catch (IOException ex) {
            throw new RuntimeException("Unable to write multipart header", ex);
        }
    }
}
