package com.google.tagmanager.protobuf;

import com.google.tagmanager.protobuf.ByteString;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/* JADX INFO: loaded from: classes49.dex */
class RopeByteString extends ByteString {
    private static final int[] minLengthByDepth;
    private int hash;
    private final ByteString left;
    private final int leftLength;
    private final ByteString right;
    private final int totalLength;
    private final int treeDepth;

    static {
        List<Integer> numbers = new ArrayList<>();
        int f1 = 1;
        int f2 = 1;
        while (f2 > 0) {
            numbers.add(Integer.valueOf(f2));
            int temp = f1 + f2;
            f1 = f2;
            f2 = temp;
        }
        numbers.add(Integer.MAX_VALUE);
        minLengthByDepth = new int[numbers.size()];
        for (int i = 0; i < minLengthByDepth.length; i++) {
            minLengthByDepth[i] = numbers.get(i).intValue();
        }
    }

    private RopeByteString(ByteString left, ByteString right) {
        this.hash = 0;
        this.left = left;
        this.right = right;
        this.leftLength = left.size();
        this.totalLength = this.leftLength + right.size();
        this.treeDepth = Math.max(left.getTreeDepth(), right.getTreeDepth()) + 1;
    }

    static ByteString concatenate(ByteString left, ByteString right) {
        RopeByteString leftRope = left instanceof RopeByteString ? (RopeByteString) left : null;
        if (right.size() == 0) {
            return left;
        }
        if (left.size() == 0) {
            return right;
        }
        int newLength = left.size() + right.size();
        if (newLength < 128) {
            ByteString result = concatenateBytes(left, right);
            return result;
        }
        if (leftRope != null && leftRope.right.size() + right.size() < 128) {
            ByteString newRight = concatenateBytes(leftRope.right, right);
            ByteString result2 = new RopeByteString(leftRope.left, newRight);
            return result2;
        }
        if (leftRope != null && leftRope.left.getTreeDepth() > leftRope.right.getTreeDepth() && leftRope.getTreeDepth() > right.getTreeDepth()) {
            ByteString newRight2 = new RopeByteString(leftRope.right, right);
            ByteString result3 = new RopeByteString(leftRope.left, newRight2);
            return result3;
        }
        int newDepth = Math.max(left.getTreeDepth(), right.getTreeDepth()) + 1;
        if (newLength >= minLengthByDepth[newDepth]) {
            ByteString result4 = new RopeByteString(left, right);
            return result4;
        }
        ByteString result5 = new Balancer().balance(left, right);
        return result5;
    }

    private static LiteralByteString concatenateBytes(ByteString left, ByteString right) {
        int leftSize = left.size();
        int rightSize = right.size();
        byte[] bytes = new byte[leftSize + rightSize];
        left.copyTo(bytes, 0, 0, leftSize);
        right.copyTo(bytes, 0, leftSize, rightSize);
        return new LiteralByteString(bytes);
    }

    static RopeByteString newInstanceForTest(ByteString left, ByteString right) {
        return new RopeByteString(left, right);
    }

    @Override // com.google.tagmanager.protobuf.ByteString
    public byte byteAt(int index) {
        if (index < 0) {
            throw new ArrayIndexOutOfBoundsException("Index < 0: " + index);
        }
        if (index > this.totalLength) {
            throw new ArrayIndexOutOfBoundsException("Index > length: " + index + ", " + this.totalLength);
        }
        if (index < this.leftLength) {
            byte result = this.left.byteAt(index);
            return result;
        }
        byte result2 = this.right.byteAt(index - this.leftLength);
        return result2;
    }

    @Override // com.google.tagmanager.protobuf.ByteString
    public int size() {
        return this.totalLength;
    }

    @Override // com.google.tagmanager.protobuf.ByteString
    protected int getTreeDepth() {
        return this.treeDepth;
    }

    @Override // com.google.tagmanager.protobuf.ByteString
    protected boolean isBalanced() {
        return this.totalLength >= minLengthByDepth[this.treeDepth];
    }

    @Override // com.google.tagmanager.protobuf.ByteString
    public ByteString substring(int beginIndex, int endIndex) {
        if (beginIndex < 0) {
            throw new IndexOutOfBoundsException("Beginning index: " + beginIndex + " < 0");
        }
        if (endIndex > this.totalLength) {
            throw new IndexOutOfBoundsException("End index: " + endIndex + " > " + this.totalLength);
        }
        int substringLength = endIndex - beginIndex;
        if (substringLength < 0) {
            throw new IndexOutOfBoundsException("Beginning index larger than ending index: " + beginIndex + ", " + endIndex);
        }
        if (substringLength == 0) {
            ByteString result = ByteString.EMPTY;
            return result;
        }
        if (substringLength == this.totalLength) {
            return this;
        }
        if (endIndex <= this.leftLength) {
            ByteString result2 = this.left.substring(beginIndex, endIndex);
            return result2;
        }
        if (beginIndex >= this.leftLength) {
            ByteString result3 = this.right.substring(beginIndex - this.leftLength, endIndex - this.leftLength);
            return result3;
        }
        ByteString leftSub = this.left.substring(beginIndex);
        ByteString rightSub = this.right.substring(0, endIndex - this.leftLength);
        ByteString result4 = new RopeByteString(leftSub, rightSub);
        return result4;
    }

    @Override // com.google.tagmanager.protobuf.ByteString
    protected void copyToInternal(byte[] target, int sourceOffset, int targetOffset, int numberToCopy) {
        if (sourceOffset + numberToCopy <= this.leftLength) {
            this.left.copyToInternal(target, sourceOffset, targetOffset, numberToCopy);
        } else {
            if (sourceOffset >= this.leftLength) {
                this.right.copyToInternal(target, sourceOffset - this.leftLength, targetOffset, numberToCopy);
                return;
            }
            int leftLength = this.leftLength - sourceOffset;
            this.left.copyToInternal(target, sourceOffset, targetOffset, leftLength);
            this.right.copyToInternal(target, 0, targetOffset + leftLength, numberToCopy - leftLength);
        }
    }

    @Override // com.google.tagmanager.protobuf.ByteString
    public void copyTo(ByteBuffer target) {
        this.left.copyTo(target);
        this.right.copyTo(target);
    }

    @Override // com.google.tagmanager.protobuf.ByteString
    public ByteBuffer asReadOnlyByteBuffer() {
        ByteBuffer byteBuffer = ByteBuffer.wrap(toByteArray());
        return byteBuffer.asReadOnlyBuffer();
    }

    @Override // com.google.tagmanager.protobuf.ByteString
    public List<ByteBuffer> asReadOnlyByteBufferList() {
        List<ByteBuffer> result = new ArrayList<>();
        PieceIterator pieces = new PieceIterator(this);
        while (pieces.hasNext()) {
            LiteralByteString byteString = pieces.next();
            result.add(byteString.asReadOnlyByteBuffer());
        }
        return result;
    }

    @Override // com.google.tagmanager.protobuf.ByteString
    public void writeTo(OutputStream outputStream) throws IOException {
        this.left.writeTo(outputStream);
        this.right.writeTo(outputStream);
    }

    @Override // com.google.tagmanager.protobuf.ByteString
    void writeToInternal(OutputStream out, int sourceOffset, int numberToWrite) throws IOException {
        if (sourceOffset + numberToWrite <= this.leftLength) {
            this.left.writeToInternal(out, sourceOffset, numberToWrite);
        } else {
            if (sourceOffset >= this.leftLength) {
                this.right.writeToInternal(out, sourceOffset - this.leftLength, numberToWrite);
                return;
            }
            int numberToWriteInLeft = this.leftLength - sourceOffset;
            this.left.writeToInternal(out, sourceOffset, numberToWriteInLeft);
            this.right.writeToInternal(out, 0, numberToWrite - numberToWriteInLeft);
        }
    }

    @Override // com.google.tagmanager.protobuf.ByteString
    public String toString(String charsetName) throws UnsupportedEncodingException {
        return new String(toByteArray(), charsetName);
    }

    @Override // com.google.tagmanager.protobuf.ByteString
    public boolean isValidUtf8() {
        int leftPartial = this.left.partialIsValidUtf8(0, 0, this.leftLength);
        int state = this.right.partialIsValidUtf8(leftPartial, 0, this.right.size());
        return state == 0;
    }

    @Override // com.google.tagmanager.protobuf.ByteString
    protected int partialIsValidUtf8(int state, int offset, int length) {
        int toIndex = offset + length;
        if (toIndex <= this.leftLength) {
            return this.left.partialIsValidUtf8(state, offset, length);
        }
        if (offset >= this.leftLength) {
            return this.right.partialIsValidUtf8(state, offset - this.leftLength, length);
        }
        int leftLength = this.leftLength - offset;
        int leftPartial = this.left.partialIsValidUtf8(state, offset, leftLength);
        return this.right.partialIsValidUtf8(leftPartial, 0, length - leftLength);
    }

    @Override // com.google.tagmanager.protobuf.ByteString
    public boolean equals(Object other) {
        int cachedOtherHash;
        if (other == this) {
            return true;
        }
        if (!(other instanceof ByteString)) {
            return false;
        }
        ByteString otherByteString = (ByteString) other;
        if (this.totalLength != otherByteString.size()) {
            return false;
        }
        if (this.totalLength == 0) {
            return true;
        }
        if (this.hash == 0 || (cachedOtherHash = otherByteString.peekCachedHashCode()) == 0 || this.hash == cachedOtherHash) {
            return equalsFragments(otherByteString);
        }
        return false;
    }

    private boolean equalsFragments(ByteString other) {
        int thisOffset = 0;
        Iterator<LiteralByteString> thisIter = new PieceIterator(this);
        LiteralByteString thisString = thisIter.next();
        int thatOffset = 0;
        Iterator<LiteralByteString> thatIter = new PieceIterator(other);
        LiteralByteString thatString = thatIter.next();
        int pos = 0;
        while (true) {
            int thisRemaining = thisString.size() - thisOffset;
            int thatRemaining = thatString.size() - thatOffset;
            int bytesToCompare = Math.min(thisRemaining, thatRemaining);
            boolean stillEqual = thisOffset == 0 ? thisString.equalsRange(thatString, thatOffset, bytesToCompare) : thatString.equalsRange(thisString, thisOffset, bytesToCompare);
            if (!stillEqual) {
                return false;
            }
            pos += bytesToCompare;
            if (pos >= this.totalLength) {
                if (pos == this.totalLength) {
                    return true;
                }
                throw new IllegalStateException();
            }
            if (bytesToCompare == thisRemaining) {
                thisOffset = 0;
                LiteralByteString thisString2 = thisIter.next();
                thisString = thisString2;
            } else {
                thisOffset += bytesToCompare;
            }
            if (bytesToCompare == thatRemaining) {
                thatOffset = 0;
                LiteralByteString thatString2 = thatIter.next();
                thatString = thatString2;
            } else {
                thatOffset += bytesToCompare;
            }
        }
    }

    @Override // com.google.tagmanager.protobuf.ByteString
    public int hashCode() {
        int h = this.hash;
        if (h == 0) {
            h = partialHash(this.totalLength, 0, this.totalLength);
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
        int toIndex = offset + length;
        if (toIndex <= this.leftLength) {
            return this.left.partialHash(h, offset, length);
        }
        if (offset >= this.leftLength) {
            return this.right.partialHash(h, offset - this.leftLength, length);
        }
        int leftLength = this.leftLength - offset;
        int leftPartial = this.left.partialHash(h, offset, leftLength);
        return this.right.partialHash(leftPartial, 0, length - leftLength);
    }

    @Override // com.google.tagmanager.protobuf.ByteString
    public CodedInputStream newCodedInput() {
        return CodedInputStream.newInstance(new RopeInputStream());
    }

    @Override // com.google.tagmanager.protobuf.ByteString
    public InputStream newInput() {
        return new RopeInputStream();
    }

    private static class Balancer {
        private final Deque<ByteString> prefixesStack;

        private Balancer() {
            this.prefixesStack = new ArrayDeque(RopeByteString.minLengthByDepth.length);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public ByteString balance(ByteString left, ByteString right) {
            doBalance(left);
            doBalance(right);
            ByteString partialString = this.prefixesStack.pop();
            while (!this.prefixesStack.isEmpty()) {
                ByteString newLeft = this.prefixesStack.pop();
                partialString = new RopeByteString(newLeft, partialString);
            }
            return partialString;
        }

        private void doBalance(ByteString root) {
            if (root.isBalanced()) {
                insert(root);
            } else {
                if (root instanceof RopeByteString) {
                    RopeByteString rbs = (RopeByteString) root;
                    doBalance(rbs.left);
                    doBalance(rbs.right);
                    return;
                }
                throw new IllegalArgumentException("Has a new type of ByteString been created? Found " + root.getClass());
            }
        }

        private void insert(ByteString byteString) {
            ByteString newTree;
            int depthBin = getDepthBinForLength(byteString.size());
            int binEnd = RopeByteString.minLengthByDepth[depthBin + 1];
            if (!this.prefixesStack.isEmpty() && this.prefixesStack.peek().size() < binEnd) {
                int binStart = RopeByteString.minLengthByDepth[depthBin];
                ByteString newTree2 = this.prefixesStack.pop();
                while (!this.prefixesStack.isEmpty() && this.prefixesStack.peek().size() < binStart) {
                    ByteString left = this.prefixesStack.pop();
                    newTree2 = new RopeByteString(left, newTree2);
                }
                ByteString newTree3 = new RopeByteString(newTree2, byteString);
                while (true) {
                    newTree = newTree3;
                    if (!this.prefixesStack.isEmpty()) {
                        int binEnd2 = RopeByteString.minLengthByDepth[getDepthBinForLength(newTree.size()) + 1];
                        if (this.prefixesStack.peek().size() >= binEnd2) {
                            break;
                        }
                        ByteString left2 = this.prefixesStack.pop();
                        newTree3 = new RopeByteString(left2, newTree);
                    } else {
                        break;
                    }
                }
                this.prefixesStack.push(newTree);
                return;
            }
            this.prefixesStack.push(byteString);
        }

        private int getDepthBinForLength(int length) {
            int depth = Arrays.binarySearch(RopeByteString.minLengthByDepth, length);
            if (depth < 0) {
                int insertionPoint = -(depth + 1);
                return insertionPoint - 1;
            }
            return depth;
        }
    }

    private static class PieceIterator implements Iterator<LiteralByteString> {
        private final Deque<RopeByteString> breadCrumbs;
        private LiteralByteString next;

        private PieceIterator(ByteString root) {
            this.breadCrumbs = new ArrayDeque(RopeByteString.minLengthByDepth.length);
            this.next = getLeafByLeft(root);
        }

        private LiteralByteString getLeafByLeft(ByteString root) {
            ByteString pos = root;
            while (pos instanceof RopeByteString) {
                RopeByteString rbs = (RopeByteString) pos;
                this.breadCrumbs.push(rbs);
                pos = rbs.left;
            }
            return (LiteralByteString) pos;
        }

        private LiteralByteString getNextNonEmptyLeaf() {
            while (!this.breadCrumbs.isEmpty()) {
                LiteralByteString result = getLeafByLeft(this.breadCrumbs.pop().right);
                if (!result.isEmpty()) {
                    return result;
                }
            }
            return null;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.next != null;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public LiteralByteString next() {
            if (this.next == null) {
                throw new NoSuchElementException();
            }
            LiteralByteString result = this.next;
            this.next = getNextNonEmptyLeaf();
            return result;
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    @Override // com.google.tagmanager.protobuf.ByteString, java.lang.Iterable
    public Iterator<Byte> iterator() {
        return new RopeByteIterator();
    }

    private class RopeByteIterator implements ByteString.ByteIterator {
        private ByteString.ByteIterator bytes;
        int bytesRemaining;
        private final PieceIterator pieces;

        /* JADX WARN: Type inference failed for: r0v3, types: [com.google.tagmanager.protobuf.ByteString$ByteIterator] */
        private RopeByteIterator() {
            this.pieces = new PieceIterator(RopeByteString.this);
            this.bytes = this.pieces.next().iterator();
            this.bytesRemaining = RopeByteString.this.size();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.bytesRemaining > 0;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public Byte next() {
            return Byte.valueOf(nextByte());
        }

        /* JADX WARN: Type inference failed for: r0v8, types: [com.google.tagmanager.protobuf.ByteString$ByteIterator] */
        @Override // com.google.tagmanager.protobuf.ByteString.ByteIterator
        public byte nextByte() {
            if (!this.bytes.hasNext()) {
                this.bytes = this.pieces.next().iterator();
            }
            this.bytesRemaining--;
            return this.bytes.nextByte();
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private class RopeInputStream extends InputStream {
        private LiteralByteString currentPiece;
        private int currentPieceIndex;
        private int currentPieceOffsetInRope;
        private int currentPieceSize;
        private int mark;
        private PieceIterator pieceIterator;

        public RopeInputStream() {
            initialize();
        }

        @Override // java.io.InputStream
        public int read(byte[] b, int offset, int length) {
            if (b == null) {
                throw new NullPointerException();
            }
            if (offset < 0 || length < 0 || length > b.length - offset) {
                throw new IndexOutOfBoundsException();
            }
            return readSkipInternal(b, offset, length);
        }

        @Override // java.io.InputStream
        public long skip(long length) {
            if (length < 0) {
                throw new IndexOutOfBoundsException();
            }
            if (length > 2147483647L) {
                length = 2147483647L;
            }
            return readSkipInternal(null, 0, (int) length);
        }

        private int readSkipInternal(byte[] b, int offset, int length) {
            int bytesRemaining = length;
            while (true) {
                if (bytesRemaining <= 0) {
                    break;
                }
                advanceIfCurrentPieceFullyRead();
                if (this.currentPiece == null) {
                    if (bytesRemaining == length) {
                        return -1;
                    }
                } else {
                    int currentPieceRemaining = this.currentPieceSize - this.currentPieceIndex;
                    int count = Math.min(currentPieceRemaining, bytesRemaining);
                    if (b != null) {
                        this.currentPiece.copyTo(b, this.currentPieceIndex, offset, count);
                        offset += count;
                    }
                    this.currentPieceIndex += count;
                    bytesRemaining -= count;
                }
            }
            return length - bytesRemaining;
        }

        @Override // java.io.InputStream
        public int read() throws IOException {
            advanceIfCurrentPieceFullyRead();
            if (this.currentPiece == null) {
                return -1;
            }
            LiteralByteString literalByteString = this.currentPiece;
            int i = this.currentPieceIndex;
            this.currentPieceIndex = i + 1;
            return literalByteString.byteAt(i) & 255;
        }

        @Override // java.io.InputStream
        public int available() throws IOException {
            int bytesRead = this.currentPieceOffsetInRope + this.currentPieceIndex;
            return RopeByteString.this.size() - bytesRead;
        }

        @Override // java.io.InputStream
        public boolean markSupported() {
            return true;
        }

        @Override // java.io.InputStream
        public void mark(int readAheadLimit) {
            this.mark = this.currentPieceOffsetInRope + this.currentPieceIndex;
        }

        @Override // java.io.InputStream
        public synchronized void reset() {
            initialize();
            readSkipInternal(null, 0, this.mark);
        }

        private void initialize() {
            this.pieceIterator = new PieceIterator(RopeByteString.this);
            this.currentPiece = this.pieceIterator.next();
            this.currentPieceSize = this.currentPiece.size();
            this.currentPieceIndex = 0;
            this.currentPieceOffsetInRope = 0;
        }

        private void advanceIfCurrentPieceFullyRead() {
            if (this.currentPiece != null && this.currentPieceIndex == this.currentPieceSize) {
                this.currentPieceOffsetInRope += this.currentPieceSize;
                this.currentPieceIndex = 0;
                if (this.pieceIterator.hasNext()) {
                    this.currentPiece = this.pieceIterator.next();
                    this.currentPieceSize = this.currentPiece.size();
                } else {
                    this.currentPiece = null;
                    this.currentPieceSize = 0;
                }
            }
        }
    }
}
