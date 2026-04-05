package com.nuance.android.vocalizer.util;

import android.content.res.AssetFileDescriptor;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import com.digits.sdk.vcard.VCardConfig;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/* JADX INFO: loaded from: classes16.dex */
public class ZipResourceFile {
    static final boolean LOGV = false;
    static final String LOG_TAG = "zipro";
    static final int kCDECRC = 16;
    static final int kCDECommentLen = 32;
    static final int kCDECompLen = 20;
    static final int kCDEExtraLen = 30;
    static final int kCDELen = 46;
    static final int kCDELocalOffset = 42;
    static final int kCDEMethod = 10;
    static final int kCDEModWhen = 12;
    static final int kCDENameLen = 28;
    static final int kCDESignature = 33639248;
    static final int kCDEUncompLen = 24;
    static final int kCompressDeflated = 8;
    static final int kCompressStored = 0;
    static final int kEOCDFileOffset = 16;
    static final int kEOCDLen = 22;
    static final int kEOCDNumEntries = 8;
    static final int kEOCDSignature = 101010256;
    static final int kEOCDSize = 12;
    static final int kLFHExtraLen = 28;
    static final int kLFHLen = 30;
    static final int kLFHNameLen = 26;
    static final int kLFHSignature = 67324752;
    static final int kMaxCommentLen = 65535;
    static final int kMaxEOCDSearch = 65557;
    static final int kZipEntryAdj = 10000;
    private MappedByteBuffer mDirectoryMap;
    private File mFile;
    private long mFileLength;
    private String mFileName;
    private int mNumEntries;
    private RandomAccessFile mZipFile;
    private HashMap<String, ZipEntryRO> mHashMap = new HashMap<>();
    public HashMap<File, ZipFile> mZipFiles = new HashMap<>();
    ByteBuffer mLEByteBuffer = ByteBuffer.allocate(4);

    int swapEndian(int i) {
        return ((i & 255) << 24) + ((65280 & i) << 8) + ((16711680 & i) >>> 8) + ((i >>> 24) & 255);
    }

    int swapEndian(short s) {
        return ((s & 255) << 8) | ((65280 & s) >>> 8);
    }

    public final class ZipEntryRO {
        public long mCRC32;
        public long mCompressedLength;
        public final String mFileName;
        public long mLocalHdrOffset;
        public int mMethod;
        public long mOffset = -1;
        public long mUncompressedLength;
        public long mWhenModified;

        public ZipEntryRO(String str) {
            this.mFileName = str;
        }

        public void setOffsetFromFile(RandomAccessFile randomAccessFile, ByteBuffer byteBuffer) throws IOException {
            long j = this.mLocalHdrOffset;
            try {
                randomAccessFile.seek(j);
                randomAccessFile.readFully(byteBuffer.array());
                if (byteBuffer.getInt(0) != ZipResourceFile.kLFHSignature) {
                    Log.w(ZipResourceFile.LOG_TAG, "didn't find signature at start of lfh");
                    throw new IOException();
                }
                this.mOffset = j + 30 + ((long) (byteBuffer.getShort(26) & 65535)) + ((long) (byteBuffer.getShort(28) & 65535));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }

        public long getOffset() {
            if (this.mOffset == -1) {
                try {
                    ByteBuffer byteBufferAllocate = ByteBuffer.allocate(30);
                    byteBufferAllocate.order(ByteOrder.LITTLE_ENDIAN);
                    setOffsetFromFile(ZipResourceFile.this.mZipFile, byteBufferAllocate);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
            return this.mOffset;
        }

        public boolean isUncompressed() {
            return this.mMethod == 0;
        }

        public AssetFileDescriptor getAssetFileDescriptor() {
            if (this.mMethod == 0) {
                try {
                    return new AssetFileDescriptor(ParcelFileDescriptor.open(ZipResourceFile.this.mFile, VCardConfig.FLAG_REFRAIN_QP_TO_NAME_PROPERTIES), getOffset(), this.mUncompressedLength);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        public String getZipFileName() {
            return ZipResourceFile.this.mFileName;
        }

        public File getZipFile() {
            return ZipResourceFile.this.mFile;
        }
    }

    public ZipResourceFile(String str) throws IOException {
        open(str, null);
    }

    public ZipResourceFile(String str, ZipResourceFile zipResourceFile) throws IOException {
        open(str, zipResourceFile);
    }

    ZipEntryRO[] getEntriesAt(String str) {
        Vector vector = new Vector();
        Collection<ZipEntryRO> collectionValues = this.mHashMap.values();
        if (str == null) {
            str = "";
        }
        int length = str.length();
        for (ZipEntryRO zipEntryRO : collectionValues) {
            if (zipEntryRO.mFileName.startsWith(str) && -1 == zipEntryRO.mFileName.indexOf(47, length)) {
                vector.add(zipEntryRO);
            }
        }
        return (ZipEntryRO[]) vector.toArray(new ZipEntryRO[vector.size()]);
    }

    public ZipEntryRO[] getAllEntries() {
        Collection<ZipEntryRO> collectionValues = this.mHashMap.values();
        return (ZipEntryRO[]) collectionValues.toArray(new ZipEntryRO[collectionValues.size()]);
    }

    public AssetFileDescriptor getAssetFileDescriptor(String str) {
        ZipEntryRO zipEntryRO = this.mHashMap.get(str);
        if (zipEntryRO != null) {
            return zipEntryRO.getAssetFileDescriptor();
        }
        return null;
    }

    public InputStream getInputStream(String str) throws IOException {
        ZipEntryRO zipEntryRO = this.mHashMap.get(str);
        if (zipEntryRO != null) {
            if (zipEntryRO.isUncompressed()) {
                return zipEntryRO.getAssetFileDescriptor().createInputStream();
            }
            ZipFile zipFile = this.mZipFiles.get(zipEntryRO.getZipFile());
            if (zipFile == null) {
                zipFile = new ZipFile(zipEntryRO.getZipFile(), 1);
                this.mZipFiles.put(zipEntryRO.getZipFile(), zipFile);
            }
            ZipEntry entry = zipFile.getEntry(str);
            if (entry != null) {
                return zipFile.getInputStream(entry);
            }
        }
        return null;
    }

    void open(String str, ZipResourceFile zipResourceFile) throws IOException {
        this.mFile = new File(str);
        RandomAccessFile randomAccessFile = new RandomAccessFile(this.mFile, "r");
        this.mFileLength = randomAccessFile.length();
        if (this.mFileLength < 22) {
            throw new IOException();
        }
        this.mFileName = str;
        this.mZipFile = randomAccessFile;
        if (zipResourceFile != null) {
            this.mHashMap = zipResourceFile.mHashMap;
        } else {
            this.mHashMap.clear();
        }
        mapCentralDirectory();
        parseCentralDirectory(true);
    }

    private int read4LE() throws IOException {
        return swapEndian(this.mZipFile.readInt());
    }

    void mapCentralDirectory() throws IOException {
        long j = 65557;
        if (65557 > this.mFileLength) {
            j = this.mFileLength;
        }
        this.mZipFile.seek(0L);
        int i = read4LE();
        if (i == kEOCDSignature) {
            Log.i(LOG_TAG, "Found Zip archive, but it looks empty");
            throw new IOException();
        }
        if (i != kLFHSignature) {
            Log.v(LOG_TAG, "Not a Zip archive");
            throw new IOException();
        }
        this.mZipFile.seek(this.mFileLength - j);
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate((int) j);
        byte[] bArrArray = byteBufferAllocate.array();
        this.mZipFile.readFully(bArrArray);
        byteBufferAllocate.order(ByteOrder.LITTLE_ENDIAN);
        int length = bArrArray.length - 22;
        while (length >= 0 && (bArrArray[length] != 80 || byteBufferAllocate.getInt(length) != kEOCDSignature)) {
            length--;
        }
        if (length < 0) {
            Log.d(LOG_TAG, "Zip: EOCD not found, " + this.mFileName + " is not zip");
        }
        short s = byteBufferAllocate.getShort(length + 8);
        long j2 = ((long) byteBufferAllocate.getInt(length + 12)) & 4294967295L;
        long j3 = ((long) byteBufferAllocate.getInt(length + 16)) & 4294967295L;
        if (j3 + j2 > this.mFileLength) {
            Log.w(LOG_TAG, "bad offsets (dir " + j3 + ", size " + j2 + ", eocd " + length + ")");
            throw new IOException();
        }
        if (s == 0) {
            Log.w(LOG_TAG, "empty archive?");
            throw new IOException();
        }
        this.mDirectoryMap = this.mZipFile.getChannel().map(FileChannel.MapMode.READ_ONLY, j3, j2);
        this.mDirectoryMap.order(ByteOrder.LITTLE_ENDIAN);
        this.mNumEntries = s;
    }

    private void parseCentralDirectory(boolean z) throws IOException {
        int i = this.mNumEntries;
        byte[] bArr = new byte[65535];
        int i2 = 0;
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(30);
        byteBufferAllocate.order(ByteOrder.LITTLE_ENDIAN);
        for (int i3 = 0; i3 < i; i3++) {
            if (this.mDirectoryMap.getInt(i2) != kCDESignature) {
                Log.w(LOG_TAG, "Missed a central dir sig (at " + i2 + ")");
                throw new IOException();
            }
            int i4 = this.mDirectoryMap.getShort(i2 + 28) & 65535;
            int i5 = this.mDirectoryMap.getShort(i2 + 30) & 65535;
            int i6 = this.mDirectoryMap.getShort(i2 + 32) & 65535;
            this.mDirectoryMap.position(i2 + 46);
            this.mDirectoryMap.get(bArr, 0, i4);
            this.mDirectoryMap.position(0);
            String str = new String(bArr, 0, i4);
            ZipEntryRO zipEntryRO = new ZipEntryRO(str);
            zipEntryRO.mMethod = this.mDirectoryMap.getShort(i2 + 10) & 65535;
            zipEntryRO.mWhenModified = ((long) this.mDirectoryMap.getInt(i2 + 12)) & 4294967295L;
            zipEntryRO.mCRC32 = this.mDirectoryMap.getLong(i2 + 16) & 4294967295L;
            zipEntryRO.mCompressedLength = this.mDirectoryMap.getLong(i2 + 20) & 4294967295L;
            zipEntryRO.mUncompressedLength = this.mDirectoryMap.getLong(i2 + 24) & 4294967295L;
            zipEntryRO.mLocalHdrOffset = ((long) this.mDirectoryMap.getInt(i2 + 42)) & 4294967295L;
            if (z) {
                byteBufferAllocate.clear();
                zipEntryRO.setOffsetFromFile(this.mZipFile, byteBufferAllocate);
            }
            this.mHashMap.put(str, zipEntryRO);
            i2 += i4 + 46 + i5 + i6;
        }
    }
}
