package com.facebook;

import android.os.Handler;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes41.dex */
class ProgressNoopOutputStream extends OutputStream implements RequestOutputStream {
    private int batchMax;
    private final Handler callbackHandler;
    private GraphRequest currentRequest;
    private RequestProgress currentRequestProgress;
    private final Map<GraphRequest, RequestProgress> progressMap = new HashMap();

    ProgressNoopOutputStream(Handler callbackHandler) {
        this.callbackHandler = callbackHandler;
    }

    @Override // com.facebook.RequestOutputStream
    public void setCurrentRequest(GraphRequest currentRequest) {
        this.currentRequest = currentRequest;
        this.currentRequestProgress = currentRequest != null ? this.progressMap.get(currentRequest) : null;
    }

    int getMaxProgress() {
        return this.batchMax;
    }

    Map<GraphRequest, RequestProgress> getProgressMap() {
        return this.progressMap;
    }

    void addProgress(long size) {
        if (this.currentRequestProgress == null) {
            this.currentRequestProgress = new RequestProgress(this.callbackHandler, this.currentRequest);
            this.progressMap.put(this.currentRequest, this.currentRequestProgress);
        }
        this.currentRequestProgress.addToMax(size);
        this.batchMax = (int) (((long) this.batchMax) + size);
    }

    @Override // java.io.OutputStream
    public void write(byte[] buffer) {
        addProgress(buffer.length);
    }

    @Override // java.io.OutputStream
    public void write(byte[] buffer, int offset, int length) {
        addProgress(length);
    }

    @Override // java.io.OutputStream
    public void write(int oneByte) {
        addProgress(1L);
    }
}
