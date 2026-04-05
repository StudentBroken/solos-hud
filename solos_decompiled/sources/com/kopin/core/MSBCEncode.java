package com.kopin.core;

import android.util.Log;
import java.nio.ByteOrder;

/* JADX INFO: loaded from: classes5.dex */
public class MSBCEncode {
    public static final int FRAME_SIZE = 57;
    private static final int MAX_BLOCKS = 4;
    private IEncodeCallback mCallback;
    private int mCodeSize;
    private EncodeBlock mPartialBlock = null;
    private long mSBC;

    public interface IEncodeCallback {
        void onEncoded(byte[] bArr, int i);
    }

    private class EncodeBlock {
        public byte[] data;
        public int length;
        public int offset;

        private EncodeBlock() {
        }
    }

    public MSBCEncode(IEncodeCallback callback) {
        this.mSBC = 0L;
        this.mCallback = null;
        this.mCodeSize = 0;
        this.mSBC = Core.initialiseMSBC(0);
        this.mCallback = callback;
        setEndianess(ByteOrder.nativeOrder());
        this.mCodeSize = Core.getCodeSize(this.mSBC);
    }

    public void cleanUp() {
        if (this.mSBC != 0) {
            Core.destroySBC(this.mSBC);
            this.mSBC = 0L;
        }
    }

    public void encode(byte[] data, int offset, int length) {
        if (this.mSBC != 0) {
            if (length < this.mCodeSize) {
                if (this.mPartialBlock != null) {
                    if (this.mPartialBlock.length + length >= this.mCodeSize) {
                        System.arraycopy(data, offset, this.mPartialBlock.data, this.mPartialBlock.offset + this.mPartialBlock.length, this.mCodeSize - this.mPartialBlock.length);
                        offset += this.mCodeSize - this.mPartialBlock.length;
                        length -= this.mCodeSize - this.mPartialBlock.length;
                        this.mPartialBlock.length = this.mCodeSize;
                    } else {
                        System.arraycopy(data, offset, this.mPartialBlock.data, this.mPartialBlock.offset + this.mPartialBlock.length, length);
                        this.mPartialBlock.length += length;
                        return;
                    }
                } else {
                    storePartialBlock(data, offset, length, length);
                    return;
                }
            }
            byte[] encodedBuffer = new byte[228];
            int[] written = new int[1];
            int frameOffset = 0;
            if (this.mPartialBlock != null) {
                if (this.mPartialBlock.length == this.mCodeSize) {
                    Core.encode(this.mSBC, this.mPartialBlock.data, this.mPartialBlock.offset, this.mCodeSize, encodedBuffer, 0, 57, written);
                } else {
                    byte[] tempBlock = new byte[this.mCodeSize];
                    System.arraycopy(this.mPartialBlock.data, this.mPartialBlock.offset, tempBlock, 0, this.mPartialBlock.length);
                    System.arraycopy(data, offset, tempBlock, this.mPartialBlock.length, this.mCodeSize - this.mPartialBlock.length);
                    offset += this.mCodeSize - this.mPartialBlock.length;
                    length -= this.mCodeSize - this.mPartialBlock.length;
                    Core.encode(this.mSBC, tempBlock, 0, this.mCodeSize, encodedBuffer, 0, 57, written);
                }
                frameOffset = 0 + written[0];
            }
            this.mPartialBlock = null;
            int count = length / this.mCodeSize;
            int remaining = length % this.mCodeSize;
            for (int i = 0; i < count; i++) {
                Core.encode(this.mSBC, data, offset + (this.mCodeSize * i), this.mCodeSize, encodedBuffer, frameOffset, 57, written);
                frameOffset += written[0];
                if (frameOffset >= encodedBuffer.length) {
                    frameOffset = sendEvent(encodedBuffer, frameOffset);
                }
                if (this.mSBC == 0) {
                    return;
                }
            }
            if (frameOffset > 0) {
                sendEvent(encodedBuffer, frameOffset);
            }
            storePartialBlock(data, offset, length, remaining);
        }
    }

    public void setEndianess(ByteOrder order) {
        Core.setEndianess(this.mSBC, (byte) (order == ByteOrder.LITTLE_ENDIAN ? 0 : 1));
    }

    private int sendEvent(byte[] encodedBuffer, int bytesEncoded) {
        this.mCallback.onEncoded(encodedBuffer, bytesEncoded);
        return 0;
    }

    private void storePartialBlock(byte[] data, int offset, int length, int remaining) {
        if (remaining > 0) {
            this.mPartialBlock = new EncodeBlock();
            this.mPartialBlock.data = new byte[this.mCodeSize];
            int realOffset = offset + (length - remaining);
            if (realOffset + remaining > data.length) {
                Log.e("MSBCEncode", "index out of bounds", new Exception("stack trace"));
                return;
            }
            System.arraycopy(data, realOffset, this.mPartialBlock.data, 0, remaining);
            this.mPartialBlock.offset = 0;
            this.mPartialBlock.length = remaining;
        }
    }
}
