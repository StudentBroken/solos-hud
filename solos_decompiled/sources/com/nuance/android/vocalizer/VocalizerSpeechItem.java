package com.nuance.android.vocalizer;

import java.util.concurrent.Semaphore;

/* JADX INFO: loaded from: classes16.dex */
public class VocalizerSpeechItem {
    protected Semaphore mSemaphore;
    private String mText = null;
    private String mId = null;

    protected VocalizerSpeechItem() {
        this.mSemaphore = null;
        this.mSemaphore = new Semaphore(1);
        try {
            this.mSemaphore.acquire();
        } catch (Exception e) {
        }
    }

    protected void setText(String str) {
        this.mText = str;
    }

    protected void setId(String str) {
        this.mId = str;
    }

    public String getText() {
        return this.mText;
    }

    public String getId() {
        return this.mId;
    }

    public void waitForComplete() {
        try {
            this.mSemaphore.acquire();
        } catch (Exception e) {
        }
    }
}
