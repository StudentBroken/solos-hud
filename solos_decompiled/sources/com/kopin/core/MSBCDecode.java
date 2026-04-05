package com.kopin.core;

import java.nio.ByteOrder;

/* JADX INFO: loaded from: classes5.dex */
public class MSBCDecode {
    public static final int FRAME_SIZE = 240;
    private static final int MAX_BLOCKS = 4;
    private IDecodeCallback mCallback;
    private long mSBC;

    public interface IDecodeCallback {
        void onDecoded(byte[] bArr, int i);
    }

    public MSBCDecode(IDecodeCallback callback) {
        this.mSBC = 0L;
        this.mCallback = null;
        this.mSBC = Core.initialiseMSBC(1);
        this.mCallback = callback;
    }

    public void cleanUp() {
        if (this.mSBC != 0) {
            Core.destroySBC(this.mSBC);
        }
    }

    public void decode(byte[] data, int offset, int length) {
        byte[] decodedBuffer = new byte[960];
        int[] written = new int[1];
        int frameOffset = 0;
        int count = length / 57;
        for (int i = 0; i < count; i++) {
            Core.decode(this.mSBC, data, offset + (i * 57), 57, decodedBuffer, frameOffset, 240, written);
            frameOffset += 240;
            if (frameOffset >= decodedBuffer.length) {
                frameOffset = sendEvent(decodedBuffer, frameOffset);
            }
        }
        if (frameOffset > 0) {
            sendEvent(decodedBuffer, frameOffset);
        }
    }

    public void setEndianess(ByteOrder order) {
        Core.setEndianess(this.mSBC, (byte) (order == ByteOrder.LITTLE_ENDIAN ? 0 : 1));
    }

    private int sendEvent(byte[] decodedBuffer, int bytesEncoded) {
        this.mCallback.onDecoded(decodedBuffer, bytesEncoded);
        return 0;
    }
}
