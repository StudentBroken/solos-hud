package com.google.tagmanager.protobuf;

import com.google.tagmanager.protobuf.ByteString;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/* JADX INFO: loaded from: classes49.dex */
class LiteralByteString extends ByteString {
    protected final byte[] bytes;
    private int hash = 0;

    LiteralByteString(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override // com.google.tagmanager.protobuf.ByteString
    public byte byteAt(int index) {
        return this.bytes[index];
    }

    @Override // com.google.tagmanager.protobuf.ByteString
    public int size() {
        return this.bytes.length;
    }

    @Override // com.google.tagmanager.protobuf.ByteString
    public ByteString substring(int beginIndex, int endIndex) {
        if (beginIndex < 0) {
            throw new IndexOutOfBoundsException("Beginning index: " + beginIndex + " < 0");
        }
        if (endIndex > size()) {
            throw new IndexOutOfBoundsException("End index: " + endIndex + " > " + size());
        }
        int substringLength = endIndex - beginIndex;
        if (substringLength < 0) {
            throw new IndexOutOfBoundsException("Beginning index larger than ending index: " + beginIndex + ", " + endIndex);
        }
        if (substringLength == 0) {
            ByteString result = ByteString.EMPTY;
            return result;
        }
        ByteString result2 = new BoundedByteString(this.bytes, getOffsetIntoBytes() + beginIndex, substringLength);
        return result2;
    }

    @Override // com.google.tagmanager.protobuf.ByteString
    protected void copyToInternal(byte[] target, int sourceOffset, int targetOffset, int numberToCopy) {
        System.arraycopy(this.bytes, sourceOffset, target, targetOffset, numberToCopy);
    }

    @Override // com.google.tagmanager.protobuf.ByteString
    public void copyTo(ByteBuffer target) {
        target.put(this.bytes, getOffsetIntoBytes(), size());
    }

    @Override // com.google.tagmanager.protobuf.ByteString
    public ByteBuffer asReadOnlyByteBuffer() {
        ByteBuffer byteBuffer = ByteBuffer.wrap(this.bytes, getOffsetIntoBytes(), size());
        return byteBuffer.asReadOnlyBuffer();
    }

    @Override // com.google.tagmanager.protobuf.ByteString
    public List<ByteBuffer> asReadOnlyByteBufferList() {
        List<ByteBuffer> result = new ArrayList<>(1);
        result.add(asReadOnlyByteBuffer());
        return result;
    }

    @Override // com.google.tagmanager.protobuf.ByteString
    public void writeTo(OutputStream outputStream) throws IOException {
        outputStream.write(toByteArray());
    }

    @Override // com.google.tagmanager.protobuf.ByteString
    void writeToInternal(OutputStream outputStream, int sourceOffset, int numberToWrite) throws IOException {
        outputStream.write(this.bytes, getOffsetIntoBytes() + sourceOffset, numberToWrite);
    }

    @Override // com.google.tagmanager.protobuf.ByteString
    public String toString(String charsetName) throws UnsupportedEncodingException {
        return new String(this.bytes, getOffsetIntoBytes(), size(), charsetName);
    }

    @Override // com.google.tagmanager.protobuf.ByteString
    public boolean isValidUtf8() {
        int offset = getOffsetIntoBytes();
        return Utf8.isValidUtf8(this.bytes, offset, size() + offset);
    }

    @Override // com.google.tagmanager.protobuf.ByteString
    protected int partialIsValidUtf8(int state, int offset, int length) {
        int index = getOffsetIntoBytes() + offset;
        return Utf8.partialIsValidUtf8(state, this.bytes, index, index + length);
    }

    @Override // com.google.tagmanager.protobuf.ByteString
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ByteString) && size() == ((ByteString) other).size()) {
            if (size() == 0) {
                return true;
            }
            if (other instanceof LiteralByteString) {
                return equalsRange((LiteralByteString) other, 0, size());
            }
            if (other instanceof RopeByteString) {
                return other.equals(this);
            }
            throw new IllegalArgumentException("Has a new type of ByteString been created? Found " + other.getClass());
        }
        return false;
    }

    boolean equalsRange(LiteralByteString other, int offset, int length) {
        if (length > other.size()) {
            throw new IllegalArgumentException("Length too large: " + length + size());
        }
        if (offset + length > other.size()) {
            throw new IllegalArgumentException("Ran off end of other: " + offset + ", " + length + ", " + other.size());
        }
        byte[] thisBytes = this.bytes;
        byte[] otherBytes = other.bytes;
        int thisLimit = getOffsetIntoBytes() + length;
        int thisIndex = getOffsetIntoBytes();
        int otherIndex = other.getOffsetIntoBytes() + offset;
        while (thisIndex < thisLimit) {
            if (thisBytes[thisIndex] == otherBytes[otherIndex]) {
                thisIndex++;
                otherIndex++;
            } else {
                return false;
            }
        }
        return true;
    }

    @Override // com.google.tagmanager.protobuf.ByteString
    public int hashCode() {
        int h = this.hash;
        if (h == 0) {
            int size = size();
            h = partialHash(size, 0, size);
            if (h == 0) {
                h = 1;
            }
            this.hash = h;
        }
        return h;
    }

    @Override // com.google.tagmanager.protobuf.ByteString
    protected int peekCachedHashCode() {
        return this.hash;
    }

    @Override // com.google.tagmanager.protobuf.ByteString
    protected int partialHash(int h, int offset, int length) {
        return hashCode(h, this.bytes, getOffsetIntoBytes() + offset, length);
    }

    static int hashCode(int h, byte[] bytes, int offset, int length) {
        for (int i = offset; i < offset + length; i++) {
            h = (h * 31) + bytes[i];
        }
        return h;
    }

    static int hashCode(byte[] bytes) {
        int h = hashCode(bytes.length, bytes, 0, bytes.length);
        if (h == 0) {
            return 1;
        }
        return h;
    }

    @Override // com.google.tagmanager.protobuf.ByteString
    public InputStream newInput() {
        return new ByteArrayInputStream(this.bytes, getOffsetIntoBytes(), size());
    }

    @Override // com.google.tagmanager.protobuf.ByteString
    public CodedInputStream newCodedInput() {
        return CodedInputStream.newInstance(this);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.tagmanager.protobuf.ByteString, java.lang.Iterable
    public Iterator<Byte> iterator() {
        return new LiteralByteIterator();
    }

    private class LiteralByteIterator implements ByteString.ByteIterator {
        private final int limit;
        private int position;

        private LiteralByteIterator() {
            this.position = 0;
            this.limit = LiteralByteString.this.size();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.position < this.limit;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public Byte next() {
            return Byte.valueOf(nextByte());
        }

        @Override // com.google.tagmanager.protobuf.ByteString.ByteIterator
        public byte nextByte() {
            try {
                byte[] bArr = LiteralByteString.this.bytes;
                int i = this.position;
                this.position = i + 1;
                return bArr[i];
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new NoSuchElementException(e.getMessage());
            }
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    @Override // com.google.tagmanager.protobuf.ByteString
    protected int getTreeDepth() {
        return 0;
    }

    @Override // com.google.tagmanager.protobuf.ByteString
    protected boolean isBalanced() {
        return true;
    }

    protected int getOffsetIntoBytes() {
        return 0;
    }
}
