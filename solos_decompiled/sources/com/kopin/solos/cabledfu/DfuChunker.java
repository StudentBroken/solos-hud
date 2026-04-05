package com.kopin.solos.cabledfu;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes38.dex */
public class DfuChunker {
    public static final int CHUNK_SIZE = 20;
    public static final int SECTION_SIZE = 50;
    private static final String TAG = "DfuChunker";
    private final int mTotalBytes;
    private final ArrayList<byte[]> mBytes = new ArrayList<>();
    private int mCurSection = 0;
    private int mCurOffset = 0;

    public DfuChunker(InputStream appImage) {
        this.mTotalBytes = loadBytes(appImage);
    }

    public int getBinarySize() {
        return this.mTotalBytes;
    }

    private int loadBytes(InputStream appImage) {
        int total = 0;
        int bytesRead = 0;
        try {
            byte[] buffer = new byte[1000];
            while (bytesRead != -1) {
                bytesRead = appImage.read(buffer);
                if (bytesRead != -1) {
                    total += bytesRead;
                    byte[] b = new byte[bytesRead];
                    System.arraycopy(buffer, 0, b, 0, bytesRead);
                    this.mBytes.add(b);
                }
            }
            appImage.close();
        } catch (FileNotFoundException fnf) {
            fnf.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return total;
    }

    public boolean hasMore() {
        return this.mCurSection < this.mBytes.size();
    }

    public byte[] nextSection() {
        if (this.mCurSection >= this.mBytes.size()) {
            return null;
        }
        byte[] bArr = this.mBytes.get(this.mCurSection);
        this.mCurSection++;
        return bArr;
    }

    public static byte[] readAllBytes(InputStream appImage) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            while (true) {
                int r = appImage.read(buffer);
                if (r != -1) {
                    outputStream.write(buffer, 0, r);
                } else {
                    appImage.close();
                    outputStream.flush();
                    return outputStream.toByteArray();
                }
            }
        } catch (FileNotFoundException fnf) {
            fnf.printStackTrace();
            return null;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        }
    }
}
