package com.google.tagmanager.protobuf;

import com.google.tagmanager.protobuf.ByteString;
import java.util.Iterator;
import java.util.NoSuchElementException;

/* JADX INFO: loaded from: classes49.dex */
class BoundedByteString extends LiteralByteString {
    private final int bytesLength;
    private final int bytesOffset;

    BoundedByteString(byte[] bytes, int offset, int length) {
        super(bytes);
        if (offset < 0) {
            throw new IllegalArgumentException("Offset too small: " + offset);
        }
        if (length < 0) {
            throw new IllegalArgumentException("Length too small: " + offset);
        }
        if (((long) offset) + ((long) length) > bytes.length) {
            throw new IllegalArgumentException("Offset+Length too large: " + offset + "+" + length);
        }
        this.bytesOffset = offset;
        this.bytesLength = length;
    }

    @Override // com.google.tagmanager.protobuf.LiteralByteString, com.google.tagmanager.protobuf.ByteString
    public byte byteAt(int index) {
        if (index < 0) {
            throw new ArrayIndexOutOfBoundsException("Index too small: " + index);
        }
        if (index >= size()) {
            throw new ArrayIndexOutOfBoundsException("Index too large: " + index + ", " + size());
        }
        return this.bytes[this.bytesOffset + index];
    }

    @Override // com.google.tagmanager.protobuf.LiteralByteString, com.google.tagmanager.protobuf.ByteString
    public int size() {
        return this.bytesLength;
    }

    @Override // com.google.tagmanager.protobuf.LiteralByteString
    protected int getOffsetIntoBytes() {
        return this.bytesOffset;
    }

    @Override // com.google.tagmanager.protobuf.LiteralByteString, com.google.tagmanager.protobuf.ByteString
    protected void copyToInternal(byte[] target, int sourceOffset, int targetOffset, int numberToCopy) {
        System.arraycopy(this.bytes, getOffsetIntoBytes() + sourceOffset, target, targetOffset, numberToCopy);
    }

    @Override // com.google.tagmanager.protobuf.LiteralByteString, com.google.tagmanager.protobuf.ByteString, java.lang.Iterable
    public Iterator<Byte> iterator() {
        return new BoundedByteIterator();
    }

    private class BoundedByteIterator implements ByteString.ByteIterator {
        private final int limit;
        private int position;

        private BoundedByteIterator() {
            this.position = BoundedByteString.this.getOffsetIntoBytes();
            this.limit = this.position + BoundedByteString.this.size();
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
            if (this.position >= this.limit) {
                throw new NoSuchElementException();
            }
            byte[] bArr = BoundedByteString.this.bytes;
            int i = this.position;
            this.position = i + 1;
            return bArr[i];
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
