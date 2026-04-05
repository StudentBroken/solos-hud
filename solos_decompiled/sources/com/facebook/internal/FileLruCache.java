package com.facebook.internal;

import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;
import java.util.Date;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicLong;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/* JADX INFO: loaded from: classes41.dex */
public final class FileLruCache {
    private static final String HEADER_CACHEKEY_KEY = "key";
    private static final String HEADER_CACHE_CONTENT_TAG_KEY = "tag";
    static final String TAG = FileLruCache.class.getSimpleName();
    private static final AtomicLong bufferIndex = new AtomicLong();
    private final File directory;
    private boolean isTrimInProgress;
    private boolean isTrimPending;
    private final Limits limits;
    private final String tag;
    private AtomicLong lastClearCacheTime = new AtomicLong(0);
    private final Object lock = new Object();

    private interface StreamCloseCallback {
        void onClose();
    }

    public FileLruCache(String tag, Limits limits) {
        this.tag = tag;
        this.limits = limits;
        this.directory = new File(FacebookSdk.getCacheDir(), tag);
        if (this.directory.mkdirs() || this.directory.isDirectory()) {
            BufferFile.deleteAll(this.directory);
        }
    }

    long sizeInBytesForTest() {
        synchronized (this.lock) {
            while (true) {
                if (!this.isTrimPending && !this.isTrimInProgress) {
                    break;
                }
                try {
                    this.lock.wait();
                } catch (InterruptedException e) {
                }
            }
        }
        File[] files = this.directory.listFiles();
        long total = 0;
        if (files != null) {
            for (File file : files) {
                total += file.length();
            }
        }
        return total;
    }

    public InputStream get(String key) throws IOException {
        return get(key, null);
    }

    public InputStream get(String key, String contentTag) throws IOException {
        File file = new File(this.directory, Utility.md5hash(key));
        try {
            FileInputStream input = new FileInputStream(file);
            BufferedInputStream buffered = new BufferedInputStream(input, 8192);
            try {
                JSONObject header = StreamHeader.readHeader(buffered);
                if (header == null) {
                    return null;
                }
                String foundKey = header.optString(HEADER_CACHEKEY_KEY);
                if (foundKey == null || !foundKey.equals(key)) {
                    if (0 == 0) {
                        buffered.close();
                    }
                    return null;
                }
                String headerContentTag = header.optString(HEADER_CACHE_CONTENT_TAG_KEY, null);
                if ((contentTag == null && headerContentTag != null) || (contentTag != null && !contentTag.equals(headerContentTag))) {
                    if (0 == 0) {
                        buffered.close();
                    }
                    return null;
                }
                long accessTime = new Date().getTime();
                Logger.log(LoggingBehavior.CACHE, TAG, "Setting lastModified to " + Long.valueOf(accessTime) + " for " + file.getName());
                file.setLastModified(accessTime);
                if (1 == 0) {
                    buffered.close();
                }
                return buffered;
            } finally {
                if (0 == 0) {
                    buffered.close();
                }
            }
        } catch (IOException e) {
            return null;
        }
    }

    public OutputStream openPutStream(String key) throws IOException {
        return openPutStream(key, null);
    }

    public OutputStream openPutStream(final String key, String contentTag) throws IOException {
        final File buffer = BufferFile.newFile(this.directory);
        buffer.delete();
        if (!buffer.createNewFile()) {
            throw new IOException("Could not create file at " + buffer.getAbsolutePath());
        }
        try {
            FileOutputStream file = new FileOutputStream(buffer);
            final long bufferFileCreateTime = System.currentTimeMillis();
            StreamCloseCallback renameToTargetCallback = new StreamCloseCallback() { // from class: com.facebook.internal.FileLruCache.1
                @Override // com.facebook.internal.FileLruCache.StreamCloseCallback
                public void onClose() {
                    if (bufferFileCreateTime >= FileLruCache.this.lastClearCacheTime.get()) {
                        FileLruCache.this.renameToTargetAndTrim(key, buffer);
                    } else {
                        buffer.delete();
                    }
                }
            };
            CloseCallbackOutputStream cleanup = new CloseCallbackOutputStream(file, renameToTargetCallback);
            BufferedOutputStream buffered = new BufferedOutputStream(cleanup, 8192);
            boolean success = false;
            try {
                try {
                    JSONObject header = new JSONObject();
                    header.put(HEADER_CACHEKEY_KEY, key);
                    if (!Utility.isNullOrEmpty(contentTag)) {
                        header.put(HEADER_CACHE_CONTENT_TAG_KEY, contentTag);
                    }
                    StreamHeader.writeHeader(buffered, header);
                    success = true;
                    return buffered;
                } catch (JSONException e) {
                    Logger.log(LoggingBehavior.CACHE, 5, TAG, "Error creating JSON header for cache file: " + e);
                    throw new IOException(e.getMessage());
                }
            } finally {
                if (!success) {
                    buffered.close();
                }
            }
        } catch (FileNotFoundException e2) {
            Logger.log(LoggingBehavior.CACHE, 5, TAG, "Error creating buffer output stream: " + e2);
            throw new IOException(e2.getMessage());
        }
    }

    public void clearCache() {
        final File[] filesToDelete = this.directory.listFiles(BufferFile.excludeBufferFiles());
        this.lastClearCacheTime.set(System.currentTimeMillis());
        if (filesToDelete != null) {
            FacebookSdk.getExecutor().execute(new Runnable() { // from class: com.facebook.internal.FileLruCache.2
                @Override // java.lang.Runnable
                public void run() {
                    File[] arr$ = filesToDelete;
                    for (File file : arr$) {
                        file.delete();
                    }
                }
            });
        }
    }

    public String getLocation() {
        return this.directory.getPath();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void renameToTargetAndTrim(String key, File buffer) {
        File target = new File(this.directory, Utility.md5hash(key));
        if (!buffer.renameTo(target)) {
            buffer.delete();
        }
        postTrim();
    }

    public InputStream interceptAndPut(String key, InputStream input) throws IOException {
        OutputStream output = openPutStream(key);
        return new CopyingInputStream(input, output);
    }

    public String toString() {
        return "{FileLruCache: tag:" + this.tag + " file:" + this.directory.getName() + "}";
    }

    private void postTrim() {
        synchronized (this.lock) {
            if (!this.isTrimPending) {
                this.isTrimPending = true;
                FacebookSdk.getExecutor().execute(new Runnable() { // from class: com.facebook.internal.FileLruCache.3
                    @Override // java.lang.Runnable
                    public void run() {
                        FileLruCache.this.trim();
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void trim() {
        synchronized (this.lock) {
            this.isTrimPending = false;
            this.isTrimInProgress = true;
        }
        try {
            Logger.log(LoggingBehavior.CACHE, TAG, "trim started");
            PriorityQueue<ModifiedFile> heap = new PriorityQueue<>();
            long size = 0;
            long count = 0;
            File[] filesToTrim = this.directory.listFiles(BufferFile.excludeBufferFiles());
            if (filesToTrim != null) {
                for (File file : filesToTrim) {
                    ModifiedFile modified = new ModifiedFile(file);
                    heap.add(modified);
                    Logger.log(LoggingBehavior.CACHE, TAG, "  trim considering time=" + Long.valueOf(modified.getModified()) + " name=" + modified.getFile().getName());
                    size += file.length();
                    count++;
                }
            }
            while (true) {
                if (size > this.limits.getByteCount() || count > this.limits.getFileCount()) {
                    File file2 = heap.remove().getFile();
                    Logger.log(LoggingBehavior.CACHE, TAG, "  trim removing " + file2.getName());
                    size -= file2.length();
                    count--;
                    file2.delete();
                } else {
                    synchronized (this.lock) {
                        this.isTrimInProgress = false;
                        this.lock.notifyAll();
                    }
                    return;
                }
            }
        } catch (Throwable th) {
            synchronized (this.lock) {
                this.isTrimInProgress = false;
                this.lock.notifyAll();
                throw th;
            }
        }
    }

    private static class BufferFile {
        private static final String FILE_NAME_PREFIX = "buffer";
        private static final FilenameFilter filterExcludeBufferFiles = new FilenameFilter() { // from class: com.facebook.internal.FileLruCache.BufferFile.1
            @Override // java.io.FilenameFilter
            public boolean accept(File dir, String filename) {
                return !filename.startsWith(BufferFile.FILE_NAME_PREFIX);
            }
        };
        private static final FilenameFilter filterExcludeNonBufferFiles = new FilenameFilter() { // from class: com.facebook.internal.FileLruCache.BufferFile.2
            @Override // java.io.FilenameFilter
            public boolean accept(File dir, String filename) {
                return filename.startsWith(BufferFile.FILE_NAME_PREFIX);
            }
        };

        private BufferFile() {
        }

        static void deleteAll(File root) {
            File[] filesToDelete = root.listFiles(excludeNonBufferFiles());
            if (filesToDelete != null) {
                for (File file : filesToDelete) {
                    file.delete();
                }
            }
        }

        static FilenameFilter excludeBufferFiles() {
            return filterExcludeBufferFiles;
        }

        static FilenameFilter excludeNonBufferFiles() {
            return filterExcludeNonBufferFiles;
        }

        static File newFile(File root) {
            String name = FILE_NAME_PREFIX + Long.valueOf(FileLruCache.bufferIndex.incrementAndGet()).toString();
            return new File(root, name);
        }
    }

    private static final class StreamHeader {
        private static final int HEADER_VERSION = 0;

        private StreamHeader() {
        }

        static void writeHeader(OutputStream stream, JSONObject header) throws IOException {
            String headerString = header.toString();
            byte[] headerBytes = headerString.getBytes();
            stream.write(0);
            stream.write((headerBytes.length >> 16) & 255);
            stream.write((headerBytes.length >> 8) & 255);
            stream.write((headerBytes.length >> 0) & 255);
            stream.write(headerBytes);
        }

        static JSONObject readHeader(InputStream stream) throws IOException {
            JSONObject header;
            int version = stream.read();
            if (version != 0) {
                return null;
            }
            int headerSize = 0;
            for (int i = 0; i < 3; i++) {
                int b = stream.read();
                if (b == -1) {
                    Logger.log(LoggingBehavior.CACHE, FileLruCache.TAG, "readHeader: stream.read returned -1 while reading header size");
                    return null;
                }
                headerSize = (headerSize << 8) + (b & 255);
            }
            byte[] headerBytes = new byte[headerSize];
            int count = 0;
            while (count < headerBytes.length) {
                int readCount = stream.read(headerBytes, count, headerBytes.length - count);
                if (readCount < 1) {
                    Logger.log(LoggingBehavior.CACHE, FileLruCache.TAG, "readHeader: stream.read stopped at " + Integer.valueOf(count) + " when expected " + headerBytes.length);
                    return null;
                }
                count += readCount;
            }
            String headerString = new String(headerBytes);
            JSONTokener tokener = new JSONTokener(headerString);
            try {
                Object parsed = tokener.nextValue();
                if (!(parsed instanceof JSONObject)) {
                    Logger.log(LoggingBehavior.CACHE, FileLruCache.TAG, "readHeader: expected JSONObject, got " + parsed.getClass().getCanonicalName());
                    header = null;
                } else {
                    header = (JSONObject) parsed;
                }
                return header;
            } catch (JSONException e) {
                throw new IOException(e.getMessage());
            }
        }
    }

    private static class CloseCallbackOutputStream extends OutputStream {
        final StreamCloseCallback callback;
        final OutputStream innerStream;

        CloseCallbackOutputStream(OutputStream innerStream, StreamCloseCallback callback) {
            this.innerStream = innerStream;
            this.callback = callback;
        }

        @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            try {
                this.innerStream.close();
            } finally {
                this.callback.onClose();
            }
        }

        @Override // java.io.OutputStream, java.io.Flushable
        public void flush() throws IOException {
            this.innerStream.flush();
        }

        @Override // java.io.OutputStream
        public void write(byte[] buffer, int offset, int count) throws IOException {
            this.innerStream.write(buffer, offset, count);
        }

        @Override // java.io.OutputStream
        public void write(byte[] buffer) throws IOException {
            this.innerStream.write(buffer);
        }

        @Override // java.io.OutputStream
        public void write(int oneByte) throws IOException {
            this.innerStream.write(oneByte);
        }
    }

    private static final class CopyingInputStream extends InputStream {
        final InputStream input;
        final OutputStream output;

        CopyingInputStream(InputStream input, OutputStream output) {
            this.input = input;
            this.output = output;
        }

        @Override // java.io.InputStream
        public int available() throws IOException {
            return this.input.available();
        }

        @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            try {
                this.input.close();
            } finally {
                this.output.close();
            }
        }

        @Override // java.io.InputStream
        public void mark(int readlimit) {
            throw new UnsupportedOperationException();
        }

        @Override // java.io.InputStream
        public boolean markSupported() {
            return false;
        }

        @Override // java.io.InputStream
        public int read(byte[] buffer) throws IOException {
            int count = this.input.read(buffer);
            if (count > 0) {
                this.output.write(buffer, 0, count);
            }
            return count;
        }

        @Override // java.io.InputStream
        public int read() throws IOException {
            int b = this.input.read();
            if (b >= 0) {
                this.output.write(b);
            }
            return b;
        }

        @Override // java.io.InputStream
        public int read(byte[] buffer, int offset, int length) throws IOException {
            int count = this.input.read(buffer, offset, length);
            if (count > 0) {
                this.output.write(buffer, offset, count);
            }
            return count;
        }

        @Override // java.io.InputStream
        public synchronized void reset() {
            throw new UnsupportedOperationException();
        }

        @Override // java.io.InputStream
        public long skip(long byteCount) throws IOException {
            int count;
            byte[] buffer = new byte[1024];
            long total = 0;
            while (total < byteCount && (count = read(buffer, 0, (int) Math.min(byteCount - total, buffer.length))) >= 0) {
                total += (long) count;
            }
            return total;
        }
    }

    public static final class Limits {
        private int fileCount = 1024;
        private int byteCount = 1048576;

        int getByteCount() {
            return this.byteCount;
        }

        int getFileCount() {
            return this.fileCount;
        }

        void setByteCount(int n) {
            if (n < 0) {
                throw new InvalidParameterException("Cache byte-count limit must be >= 0");
            }
            this.byteCount = n;
        }

        void setFileCount(int n) {
            if (n < 0) {
                throw new InvalidParameterException("Cache file count limit must be >= 0");
            }
            this.fileCount = n;
        }
    }

    private static final class ModifiedFile implements Comparable<ModifiedFile> {
        private static final int HASH_MULTIPLIER = 37;
        private static final int HASH_SEED = 29;
        private final File file;
        private final long modified;

        ModifiedFile(File file) {
            this.file = file;
            this.modified = file.lastModified();
        }

        File getFile() {
            return this.file;
        }

        long getModified() {
            return this.modified;
        }

        @Override // java.lang.Comparable
        public int compareTo(ModifiedFile another) {
            if (getModified() < another.getModified()) {
                return -1;
            }
            if (getModified() > another.getModified()) {
                return 1;
            }
            return getFile().compareTo(another.getFile());
        }

        public boolean equals(Object another) {
            return (another instanceof ModifiedFile) && compareTo((ModifiedFile) another) == 0;
        }

        public int hashCode() {
            int result = this.file.hashCode() + 1073;
            return (result * 37) + ((int) (this.modified % 2147483647L));
        }
    }
}
