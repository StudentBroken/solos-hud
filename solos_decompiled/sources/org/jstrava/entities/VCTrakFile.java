package org.jstrava.entities;

import android.util.Base64;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

/* JADX INFO: loaded from: classes68.dex */
public class VCTrakFile {
    private String mDataType;
    private FileGenerator mFileData;
    private String mFileName;

    public interface FileGenerator {
        String generate();

        void generate(Writer writer) throws IOException;
    }

    public VCTrakFile(String dataType, String fileName, FileGenerator fileData) {
        this.mDataType = "";
        this.mFileName = "";
        this.mDataType = dataType;
        this.mFileName = fileName;
        this.mFileData = fileData;
    }

    public String getDataType() {
        return this.mDataType;
    }

    public String getFileName() {
        return this.mFileName;
    }

    public String base64Encoded() throws UnsupportedEncodingException {
        return Base64.encodeToString(this.mFileData.generate().getBytes("UTF-8"), 0);
    }

    public void write(DataOutputStream wr) throws IOException {
        wr.writeBytes(this.mFileData.generate());
    }

    public void write(Writer pw) throws IOException {
        this.mFileData.generate(pw);
    }

    public String toString() {
        return "DataType: " + this.mDataType + "; FileName: " + this.mFileName + ";";
    }
}
