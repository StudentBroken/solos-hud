package com.google.tagmanager.protobuf;

import com.google.tagmanager.protobuf.MessageLite;
import com.kopin.accessory.utility.CallHelper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: loaded from: classes49.dex */
public final class CodedInputStream {
    private static final int BUFFER_SIZE = 4096;
    private static final int DEFAULT_RECURSION_LIMIT = 64;
    private static final int DEFAULT_SIZE_LIMIT = 67108864;
    private final byte[] buffer;
    private final boolean bufferIsImmutable;
    private int bufferPos;
    private int bufferSize;
    private int bufferSizeAfterLimit;
    private int currentLimit;
    private boolean enableAliasing;
    private final InputStream input;
    private int lastTag;
    private int recursionDepth;
    private int recursionLimit;
    private RefillCallback refillCallback;
    private int sizeLimit;
    private int totalBytesRetired;

    private interface RefillCallback {
        void onRefill();
    }

    public static CodedInputStream newInstance(InputStream input) {
        return new CodedInputStream(input);
    }

    public static CodedInputStream newInstance(byte[] buf) {
        return newInstance(buf, 0, buf.length);
    }

    public static CodedInputStream newInstance(byte[] buf, int off, int len) {
        CodedInputStream result = new CodedInputStream(buf, off, len);
        try {
            result.pushLimit(len);
            return result;
        } catch (InvalidProtocolBufferException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    public static CodedInputStream newInstance(ByteBuffer buf) {
        if (buf.hasArray()) {
            return newInstance(buf.array(), buf.arrayOffset() + buf.position(), buf.remaining());
        }
        ByteBuffer temp = buf.duplicate();
        byte[] buffer = new byte[temp.remaining()];
        temp.get(buffer);
        return newInstance(buffer);
    }

    static CodedInputStream newInstance(LiteralByteString byteString) {
        CodedInputStream result = new CodedInputStream(byteString);
        try {
            result.pushLimit(byteString.size());
            return result;
        } catch (InvalidProtocolBufferException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    public int readTag() throws IOException {
        if (isAtEnd()) {
            this.lastTag = 0;
            return 0;
        }
        this.lastTag = readRawVarint32();
        if (WireFormat.getTagFieldNumber(this.lastTag) == 0) {
            throw InvalidProtocolBufferException.invalidTag();
        }
        return this.lastTag;
    }

    public void checkLastTagWas(int value) throws InvalidProtocolBufferException {
        if (this.lastTag != value) {
            throw InvalidProtocolBufferException.invalidEndTag();
        }
    }

    public int getLastTag() {
        return this.lastTag;
    }

    public boolean skipField(int tag) throws IOException {
        switch (WireFormat.getTagWireType(tag)) {
            case 0:
                skipRawVarint();
                return true;
            case 1:
                skipRawBytes(8);
                return true;
            case 2:
                skipRawBytes(readRawVarint32());
                return true;
            case 3:
                skipMessage();
                checkLastTagWas(WireFormat.makeTag(WireFormat.getTagFieldNumber(tag), 4));
                return true;
            case 4:
                return false;
            case 5:
                skipRawBytes(4);
                return true;
            default:
                throw InvalidProtocolBufferException.invalidWireType();
        }
    }

    public boolean skipField(int tag, CodedOutputStream output) throws IOException {
        switch (WireFormat.getTagWireType(tag)) {
            case 0:
                long value = readInt64();
                output.writeRawVarint32(tag);
                output.writeUInt64NoTag(value);
                return true;
            case 1:
                long value2 = readRawLittleEndian64();
                output.writeRawVarint32(tag);
                output.writeFixed64NoTag(value2);
                return true;
            case 2:
                ByteString value3 = readBytes();
                output.writeRawVarint32(tag);
                output.writeBytesNoTag(value3);
                return true;
            case 3:
                output.writeRawVarint32(tag);
                skipMessage(output);
                int endtag = WireFormat.makeTag(WireFormat.getTagFieldNumber(tag), 4);
                checkLastTagWas(endtag);
                output.writeRawVarint32(endtag);
                return true;
            case 4:
                return false;
            case 5:
                int value4 = readRawLittleEndian32();
                output.writeRawVarint32(tag);
                output.writeFixed32NoTag(value4);
                return true;
            default:
                throw InvalidProtocolBufferException.invalidWireType();
        }
    }

    public void skipMessage() throws IOException {
        int tag;
        do {
            tag = readTag();
            if (tag == 0) {
                return;
            }
        } while (skipField(tag));
    }

    public void skipMessage(CodedOutputStream output) throws IOException {
        int tag;
        do {
            tag = readTag();
            if (tag == 0) {
                return;
            }
        } while (skipField(tag, output));
    }

    private class SkippedDataSink implements RefillCallback {
        private ByteArrayOutputStream byteArrayStream;
        private int lastPos;

        private SkippedDataSink() {
            this.lastPos = CodedInputStream.this.bufferPos;
        }

        @Override // com.google.tagmanager.protobuf.CodedInputStream.RefillCallback
        public void onRefill() {
            if (this.byteArrayStream == null) {
                this.byteArrayStream = new ByteArrayOutputStream();
            }
            this.byteArrayStream.write(CodedInputStream.this.buffer, this.lastPos, CodedInputStream.this.bufferPos - this.lastPos);
            this.lastPos = 0;
        }

        ByteBuffer getSkippedData() {
            if (this.byteArrayStream == null) {
                return ByteBuffer.wrap(CodedInputStream.this.buffer, this.lastPos, CodedInputStream.this.bufferPos - this.lastPos);
            }
            this.byteArrayStream.write(CodedInputStream.this.buffer, this.lastPos, CodedInputStream.this.bufferPos);
            return ByteBuffer.wrap(this.byteArrayStream.toByteArray());
        }
    }

    public void mergeToMessage(MutableMessageLite message) throws IOException {
        SkippedDataSink dataSink = new SkippedDataSink();
        this.refillCallback = dataSink;
        skipMessage();
        this.refillCallback = null;
        ByteBuffer data = dataSink.getSkippedData();
        if (!message.mergeFrom(data.array(), data.position(), data.remaining())) {
            throw InvalidProtocolBufferException.parseFailure();
        }
    }

    public double readDouble() throws IOException {
        return Double.longBitsToDouble(readRawLittleEndian64());
    }

    public float readFloat() throws IOException {
        return Float.intBitsToFloat(readRawLittleEndian32());
    }

    public long readUInt64() throws IOException {
        return readRawVarint64();
    }

    public long readInt64() throws IOException {
        return readRawVarint64();
    }

    public int readInt32() throws IOException {
        return readRawVarint32();
    }

    public long readFixed64() throws IOException {
        return readRawLittleEndian64();
    }

    public int readFixed32() throws IOException {
        return readRawLittleEndian32();
    }

    public boolean readBool() throws IOException {
        return readRawVarint32() != 0;
    }

    public String readString() throws IOException {
        int size = readRawVarint32();
        if (size <= this.bufferSize - this.bufferPos && size > 0) {
            String result = new String(this.buffer, this.bufferPos, size, "UTF-8");
            this.bufferPos += size;
            return result;
        }
        String result2 = new String(readRawBytes(size), "UTF-8");
        return result2;
    }

    public String readStringRequireUtf8() throws IOException {
        ByteString bs;
        int size = readRawVarint32();
        if (size <= this.bufferSize - this.bufferPos && size > 0) {
            bs = ByteString.copyFrom(this.buffer, this.bufferPos, size);
            this.bufferPos += size;
        } else {
            bs = new LiteralByteString(readRawBytes(size));
        }
        if (!bs.isValidUtf8()) {
            throw InvalidProtocolBufferException.invalidUtf8();
        }
        return bs.toStringUtf8();
    }

    public void readGroup(int fieldNumber, MessageLite.Builder builder, ExtensionRegistryLite extensionRegistry) throws IOException {
        if (this.recursionDepth >= this.recursionLimit) {
            throw InvalidProtocolBufferException.recursionLimitExceeded();
        }
        this.recursionDepth++;
        builder.mergeFrom(this, extensionRegistry);
        checkLastTagWas(WireFormat.makeTag(fieldNumber, 4));
        this.recursionDepth--;
    }

    public void readGroup(int fieldNumber, MutableMessageLite message, ExtensionRegistryLite extensionRegistry) throws IOException {
        if (this.recursionDepth >= this.recursionLimit) {
            throw InvalidProtocolBufferException.recursionLimitExceeded();
        }
        this.recursionDepth++;
        message.mergeFrom(this, extensionRegistry);
        checkLastTagWas(WireFormat.makeTag(fieldNumber, 4));
        this.recursionDepth--;
    }

    public <T extends MessageLite> T readGroup(int fieldNumber, Parser<T> parser, ExtensionRegistryLite extensionRegistry) throws IOException {
        if (this.recursionDepth >= this.recursionLimit) {
            throw InvalidProtocolBufferException.recursionLimitExceeded();
        }
        this.recursionDepth++;
        T result = parser.parsePartialFrom(this, extensionRegistry);
        checkLastTagWas(WireFormat.makeTag(fieldNumber, 4));
        this.recursionDepth--;
        return result;
    }

    @Deprecated
    public void readUnknownGroup(int fieldNumber, MessageLite.Builder builder) throws IOException {
        readGroup(fieldNumber, builder, (ExtensionRegistryLite) null);
    }

    public void readMessage(MessageLite.Builder builder, ExtensionRegistryLite extensionRegistry) throws IOException {
        int length = readRawVarint32();
        if (this.recursionDepth >= this.recursionLimit) {
            throw InvalidProtocolBufferException.recursionLimitExceeded();
        }
        int oldLimit = pushLimit(length);
        this.recursionDepth++;
        builder.mergeFrom(this, extensionRegistry);
        checkLastTagWas(0);
        this.recursionDepth--;
        popLimit(oldLimit);
    }

    public void readMessage(MutableMessageLite message, ExtensionRegistryLite extensionRegistry) throws IOException {
        int length = readRawVarint32();
        if (this.recursionDepth >= this.recursionLimit) {
            throw InvalidProtocolBufferException.recursionLimitExceeded();
        }
        int oldLimit = pushLimit(length);
        this.recursionDepth++;
        message.mergeFrom(this, extensionRegistry);
        checkLastTagWas(0);
        this.recursionDepth--;
        popLimit(oldLimit);
    }

    public <T extends MessageLite> T readMessage(Parser<T> parser, ExtensionRegistryLite extensionRegistry) throws IOException {
        int length = readRawVarint32();
        if (this.recursionDepth >= this.recursionLimit) {
            throw InvalidProtocolBufferException.recursionLimitExceeded();
        }
        int oldLimit = pushLimit(length);
        this.recursionDepth++;
        T result = parser.parsePartialFrom(this, extensionRegistry);
        checkLastTagWas(0);
        this.recursionDepth--;
        popLimit(oldLimit);
        return result;
    }

    public ByteString readBytes() throws IOException {
        int size = readRawVarint32();
        if (size == 0) {
            return ByteString.EMPTY;
        }
        if (size <= this.bufferSize - this.bufferPos && size > 0) {
            ByteString result = (this.bufferIsImmutable && this.enableAliasing) ? new BoundedByteString(this.buffer, this.bufferPos, size) : ByteString.copyFrom(this.buffer, this.bufferPos, size);
            this.bufferPos += size;
            return result;
        }
        return new LiteralByteString(readRawBytes(size));
    }

    public byte[] readByteArray() throws IOException {
        int size = readRawVarint32();
        if (size == 0) {
            return Internal.EMPTY_BYTE_ARRAY;
        }
        if (size <= this.bufferSize - this.bufferPos && size > 0) {
            byte[] bArrCopyOfRange = Arrays.copyOfRange(this.buffer, this.bufferPos, this.bufferPos + size);
            this.bufferPos += size;
            return bArrCopyOfRange;
        }
        byte[] result = readRawBytes(size);
        return result;
    }

    public ByteBuffer readByteBuffer() throws IOException {
        int size = readRawVarint32();
        if (size == 0) {
            return Internal.EMPTY_BYTE_BUFFER;
        }
        if (size <= this.bufferSize - this.bufferPos && size > 0) {
            ByteBuffer result = (this.input == null && !this.bufferIsImmutable && this.enableAliasing) ? ByteBuffer.wrap(this.buffer, this.bufferPos, size).slice() : ByteBuffer.wrap(Arrays.copyOfRange(this.buffer, this.bufferPos, this.bufferPos + size));
            this.bufferPos += size;
            return result;
        }
        return ByteBuffer.wrap(readRawBytes(size));
    }

    public int readUInt32() throws IOException {
        return readRawVarint32();
    }

    public int readEnum() throws IOException {
        return readRawVarint32();
    }

    public int readSFixed32() throws IOException {
        return readRawLittleEndian32();
    }

    public long readSFixed64() throws IOException {
        return readRawLittleEndian64();
    }

    public int readSInt32() throws IOException {
        return decodeZigZag32(readRawVarint32());
    }

    public long readSInt64() throws IOException {
        return decodeZigZag64(readRawVarint64());
    }

    public int readRawVarint32() throws IOException {
        byte tmp = readRawByte();
        if (tmp >= 0) {
            return tmp;
        }
        int result = tmp & 127;
        byte tmp2 = readRawByte();
        if (tmp2 >= 0) {
            return result | (tmp2 << 7);
        }
        int result2 = result | ((tmp2 & 127) << 7);
        byte tmp3 = readRawByte();
        if (tmp3 >= 0) {
            return result2 | (tmp3 << CallHelper.CallState.CALL_ENDED);
        }
        int result3 = result2 | ((tmp3 & 127) << 14);
        byte tmp4 = readRawByte();
        if (tmp4 >= 0) {
            return result3 | (tmp4 << 21);
        }
        int result4 = result3 | ((tmp4 & 127) << 21);
        byte tmp5 = readRawByte();
        int result5 = result4 | (tmp5 << 28);
        if (tmp5 < 0) {
            for (int i = 0; i < 5; i++) {
                if (readRawByte() >= 0) {
                    return result5;
                }
            }
            throw InvalidProtocolBufferException.malformedVarint();
        }
        return result5;
    }

    private void skipRawVarint() throws IOException {
        for (int i = 0; i < 10; i++) {
            if (readRawByte() >= 0) {
                return;
            }
        }
        throw InvalidProtocolBufferException.malformedVarint();
    }

    static int readRawVarint32(InputStream input) throws IOException {
        int firstByte = input.read();
        if (firstByte == -1) {
            throw InvalidProtocolBufferException.truncatedMessage();
        }
        return readRawVarint32(firstByte, input);
    }

    public static int readRawVarint32(int firstByte, InputStream input) throws IOException {
        if ((firstByte & 128) == 0) {
            return firstByte;
        }
        int result = firstByte & 127;
        int offset = 7;
        while (offset < 32) {
            int b = input.read();
            if (b == -1) {
                throw InvalidProtocolBufferException.truncatedMessage();
            }
            result |= (b & 127) << offset;
            if ((b & 128) != 0) {
                offset += 7;
            } else {
                return result;
            }
        }
        while (offset < 64) {
            int b2 = input.read();
            if (b2 == -1) {
                throw InvalidProtocolBufferException.truncatedMessage();
            }
            if ((b2 & 128) != 0) {
                offset += 7;
            } else {
                return result;
            }
        }
        throw InvalidProtocolBufferException.malformedVarint();
    }

    public long readRawVarint64() throws IOException {
        long result = 0;
        for (int shift = 0; shift < 64; shift += 7) {
            byte b = readRawByte();
            result |= ((long) (b & 127)) << shift;
            if ((b & CallHelper.CallState.FLAG_CALL_ACTIVE) == 0) {
                return result;
            }
        }
        throw InvalidProtocolBufferException.malformedVarint();
    }

    public int readRawLittleEndian32() throws IOException {
        byte b1 = readRawByte();
        byte b2 = readRawByte();
        byte b3 = readRawByte();
        byte b4 = readRawByte();
        return (b1 & 255) | ((b2 & 255) << 8) | ((b3 & 255) << 16) | ((b4 & 255) << 24);
    }

    public long readRawLittleEndian64() throws IOException {
        byte b1 = readRawByte();
        byte b2 = readRawByte();
        byte b3 = readRawByte();
        byte b4 = readRawByte();
        byte b5 = readRawByte();
        byte b6 = readRawByte();
        byte b7 = readRawByte();
        byte b8 = readRawByte();
        return (((long) b1) & 255) | ((((long) b2) & 255) << 8) | ((((long) b3) & 255) << 16) | ((((long) b4) & 255) << 24) | ((((long) b5) & 255) << 32) | ((((long) b6) & 255) << 40) | ((((long) b7) & 255) << 48) | ((((long) b8) & 255) << 56);
    }

    public static int decodeZigZag32(int n) {
        return (n >>> 1) ^ (-(n & 1));
    }

    public static long decodeZigZag64(long n) {
        return (n >>> 1) ^ (-(1 & n));
    }

    private CodedInputStream(byte[] buffer, int off, int len) {
        this.enableAliasing = false;
        this.currentLimit = Integer.MAX_VALUE;
        this.recursionLimit = 64;
        this.sizeLimit = 67108864;
        this.refillCallback = null;
        this.buffer = buffer;
        this.bufferSize = off + len;
        this.bufferPos = off;
        this.totalBytesRetired = -off;
        this.input = null;
        this.bufferIsImmutable = false;
    }

    private CodedInputStream(InputStream input) {
        this.enableAliasing = false;
        this.currentLimit = Integer.MAX_VALUE;
        this.recursionLimit = 64;
        this.sizeLimit = 67108864;
        this.refillCallback = null;
        this.buffer = new byte[4096];
        this.bufferSize = 0;
        this.bufferPos = 0;
        this.totalBytesRetired = 0;
        this.input = input;
        this.bufferIsImmutable = false;
    }

    private CodedInputStream(LiteralByteString byteString) {
        this.enableAliasing = false;
        this.currentLimit = Integer.MAX_VALUE;
        this.recursionLimit = 64;
        this.sizeLimit = 67108864;
        this.refillCallback = null;
        this.buffer = byteString.bytes;
        this.bufferPos = byteString.getOffsetIntoBytes();
        this.bufferSize = byteString.getOffsetIntoBytes() + byteString.size();
        this.totalBytesRetired = -this.bufferPos;
        this.input = null;
        this.bufferIsImmutable = true;
    }

    public void enableAliasing(boolean enabled) {
        this.enableAliasing = enabled;
    }

    public int setRecursionLimit(int limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("Recursion limit cannot be negative: " + limit);
        }
        int oldLimit = this.recursionLimit;
        this.recursionLimit = limit;
        return oldLimit;
    }

    public int setSizeLimit(int limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("Size limit cannot be negative: " + limit);
        }
        int oldLimit = this.sizeLimit;
        this.sizeLimit = limit;
        return oldLimit;
    }

    public void resetSizeCounter() {
        this.totalBytesRetired = -this.bufferPos;
    }

    public int pushLimit(int byteLimit) throws InvalidProtocolBufferException {
        if (byteLimit < 0) {
            throw InvalidProtocolBufferException.negativeSize();
        }
        int byteLimit2 = byteLimit + this.totalBytesRetired + this.bufferPos;
        int oldLimit = this.currentLimit;
        if (byteLimit2 > oldLimit) {
            throw InvalidProtocolBufferException.truncatedMessage();
        }
        this.currentLimit = byteLimit2;
        recomputeBufferSizeAfterLimit();
        return oldLimit;
    }

    private void recomputeBufferSizeAfterLimit() {
        this.bufferSize += this.bufferSizeAfterLimit;
        int bufferEnd = this.totalBytesRetired + this.bufferSize;
        if (bufferEnd > this.currentLimit) {
            this.bufferSizeAfterLimit = bufferEnd - this.currentLimit;
            this.bufferSize -= this.bufferSizeAfterLimit;
        } else {
            this.bufferSizeAfterLimit = 0;
        }
    }

    public void popLimit(int oldLimit) {
        this.currentLimit = oldLimit;
        recomputeBufferSizeAfterLimit();
    }

    public int getBytesUntilLimit() {
        if (this.currentLimit == Integer.MAX_VALUE) {
            return -1;
        }
        int currentAbsolutePosition = this.totalBytesRetired + this.bufferPos;
        return this.currentLimit - currentAbsolutePosition;
    }

    public boolean isAtEnd() throws IOException {
        return this.bufferPos == this.bufferSize && !refillBuffer(false);
    }

    public int getTotalBytesRead() {
        return this.totalBytesRetired + this.bufferPos;
    }

    private boolean refillBuffer(boolean mustSucceed) throws IOException {
        if (this.bufferPos < this.bufferSize) {
            throw new IllegalStateException("refillBuffer() called when buffer wasn't empty.");
        }
        if (this.totalBytesRetired + this.bufferSize == this.currentLimit) {
            if (mustSucceed) {
                throw InvalidProtocolBufferException.truncatedMessage();
            }
            return false;
        }
        if (this.refillCallback != null) {
            this.refillCallback.onRefill();
        }
        this.totalBytesRetired += this.bufferSize;
        this.bufferPos = 0;
        this.bufferSize = this.input == null ? -1 : this.input.read(this.buffer);
        if (this.bufferSize == 0 || this.bufferSize < -1) {
            throw new IllegalStateException("InputStream#read(byte[]) returned invalid result: " + this.bufferSize + "\nThe InputStream implementation is buggy.");
        }
        if (this.bufferSize == -1) {
            this.bufferSize = 0;
            if (mustSucceed) {
                throw InvalidProtocolBufferException.truncatedMessage();
            }
            return false;
        }
        recomputeBufferSizeAfterLimit();
        int totalBytesRead = this.totalBytesRetired + this.bufferSize + this.bufferSizeAfterLimit;
        if (totalBytesRead > this.sizeLimit || totalBytesRead < 0) {
            throw InvalidProtocolBufferException.sizeLimitExceeded();
        }
        return true;
    }

    public byte readRawByte() throws IOException {
        if (this.bufferPos == this.bufferSize) {
            refillBuffer(true);
        }
        byte[] bArr = this.buffer;
        int i = this.bufferPos;
        this.bufferPos = i + 1;
        return bArr[i];
    }

    public byte[] readRawBytes(int size) throws IOException {
        if (size < 0) {
            throw InvalidProtocolBufferException.negativeSize();
        }
        if (this.totalBytesRetired + this.bufferPos + size > this.currentLimit) {
            skipRawBytes((this.currentLimit - this.totalBytesRetired) - this.bufferPos);
            throw InvalidProtocolBufferException.truncatedMessage();
        }
        if (size <= this.bufferSize - this.bufferPos) {
            byte[] bytes = new byte[size];
            System.arraycopy(this.buffer, this.bufferPos, bytes, 0, size);
            this.bufferPos += size;
            return bytes;
        }
        if (size < 4096) {
            byte[] bytes2 = new byte[size];
            int pos = this.bufferSize - this.bufferPos;
            System.arraycopy(this.buffer, this.bufferPos, bytes2, 0, pos);
            this.bufferPos = this.bufferSize;
            refillBuffer(true);
            while (size - pos > this.bufferSize) {
                System.arraycopy(this.buffer, 0, bytes2, pos, this.bufferSize);
                pos += this.bufferSize;
                this.bufferPos = this.bufferSize;
                refillBuffer(true);
            }
            System.arraycopy(this.buffer, 0, bytes2, pos, size - pos);
            this.bufferPos = size - pos;
            return bytes2;
        }
        int originalBufferPos = this.bufferPos;
        int originalBufferSize = this.bufferSize;
        this.totalBytesRetired += this.bufferSize;
        this.bufferPos = 0;
        this.bufferSize = 0;
        int sizeLeft = size - (originalBufferSize - originalBufferPos);
        List<byte[]> chunks = new ArrayList<>();
        while (sizeLeft > 0) {
            byte[] chunk = new byte[Math.min(sizeLeft, 4096)];
            int pos2 = 0;
            while (pos2 < chunk.length) {
                int n = this.input == null ? -1 : this.input.read(chunk, pos2, chunk.length - pos2);
                if (n == -1) {
                    throw InvalidProtocolBufferException.truncatedMessage();
                }
                this.totalBytesRetired += n;
                pos2 += n;
            }
            sizeLeft -= chunk.length;
            chunks.add(chunk);
        }
        byte[] bytes3 = new byte[size];
        int pos3 = originalBufferSize - originalBufferPos;
        System.arraycopy(this.buffer, originalBufferPos, bytes3, 0, pos3);
        for (byte[] chunk2 : chunks) {
            System.arraycopy(chunk2, 0, bytes3, pos3, chunk2.length);
            pos3 += chunk2.length;
        }
        return bytes3;
    }

    public void skipRawBytes(int size) throws IOException {
        if (size < 0) {
            throw InvalidProtocolBufferException.negativeSize();
        }
        if (this.totalBytesRetired + this.bufferPos + size > this.currentLimit) {
            skipRawBytes((this.currentLimit - this.totalBytesRetired) - this.bufferPos);
            throw InvalidProtocolBufferException.truncatedMessage();
        }
        if (size <= this.bufferSize - this.bufferPos) {
            this.bufferPos += size;
            return;
        }
        int pos = this.bufferSize - this.bufferPos;
        this.bufferPos = this.bufferSize;
        refillBuffer(true);
        while (size - pos > this.bufferSize) {
            pos += this.bufferSize;
            this.bufferPos = this.bufferSize;
            refillBuffer(true);
        }
        this.bufferPos = size - pos;
    }
}
